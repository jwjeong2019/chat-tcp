package chattcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSocketThread extends Thread {
	
	private final Socket socket;
	private final BufferedReader brSocket;
	
	public ClientSocketThread(Socket socket) throws IOException {
		this.socket = socket;
		this.brSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void run() {
		String message = null;
		try (socket; brSocket) {
			while ((message = brSocket.readLine()) != null) {
				System.out.println(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
