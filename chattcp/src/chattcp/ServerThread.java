package chattcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ServerThread extends Thread {
	private final Socket socket;
	private final BufferedReader br;
	private final String clientId;
	private final Map<String, Object> clients;
	
	public ServerThread(Socket socket, Map<String, Object> clients) throws IOException {
		this.socket = socket;
		this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.clientId = br.readLine();
		this.clients = clients;
	}
	
	public void init() {
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			broadcast(String.format("%s님이 입장하셨습니다.", clientId));
			putClient(clientId, pw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void broadcast(String msg) {
		synchronized (clients) {
			clients.values()
			.stream()
			.forEach(value -> {
				PrintWriter pw = (PrintWriter) value;
				pw.println(msg);
				pw.flush();
			});
		}
	}
	
	private void putClient(String clientId, PrintWriter pw) {
		synchronized (clients) {
			clients.put(clientId, pw);
		}
	}
	
	public void run() {
		try (socket) {
			String message = null;
			
			while ((message = br.readLine()) != null) {
				if (message.startsWith("/quit")) {
					break;
				}
				if (message.startsWith("/to")) {
					sendTo(message.split(" ", 3));
					continue;
				}
				broadcast(String.format("[전체]%s: %s", clientId, message));
			}
			
			removeClient();
			broadcast(String.format("%s님이 퇴장하였습니다.", clientId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendTo(String[] msgParts) {
		PrintWriter pw = null;
		
		if (msgParts.length < 3) {
			pw = (PrintWriter) clients.get(clientId);
			pw.println(">> 입력 오류입니다.");
			pw.flush();
			return;
		}
		
		String toclientId = msgParts[1];
		String msg = msgParts[2];
		
		pw = (PrintWriter) clients.get(toclientId);
		pw.println(String.format("[귓속말]%s: %s", toclientId, msg));
		pw.flush();
	}
	
	private void removeClient() {
		synchronized (clients) {
			clients.remove(clientId);
		}
	}

}
