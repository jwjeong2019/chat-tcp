package chattcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMainThread extends Thread {
	
	private final Socket socket;
	private final PrintWriter pw;
	private final BufferedReader brMain;
	
	public ClientMainThread(Socket socket) throws IOException {
		this.socket = socket;
		this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		this.brMain = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void init() {
		try {
			System.out.printf("아이디 입력: ");
			String clientId = brMain.readLine();
			
			transmit(clientId);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		String message = null;
		try (socket; pw; brMain) {
			while ((message = brMain.readLine()) != null) {
				transmit(message);
				
				if (message.startsWith("/quit")) {
					break;
				}
			}
			System.out.println("접속을 종료합니다.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void transmit(String message) {
		try {
			pw.println(message);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
