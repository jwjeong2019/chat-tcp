package chattcp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
	
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 9000;
	
	public static void main(String[] args) {
		try {
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);
			ClientMainThread mainThread = new ClientMainThread(socket);
			mainThread.init();
			mainThread.start();
			
			ClientSocketThread socketThread = new ClientSocketThread(socket);
			socketThread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
