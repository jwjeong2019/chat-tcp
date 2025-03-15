package chattcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
	
	private static final int SERVER_PORT = 9000;
	
	public static void main(String[] args) {
		try {
			runServer(new ServerSocket(SERVER_PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void runServer(ServerSocket serverSocket) throws IOException {
		Map<String, Object> clients = new HashMap<>();
		while (true) {
			System.out.println("Wating Connected from Clients...");
			
			Socket socket = serverSocket.accept();
			
			ServerThread serverThread = new ServerThread(socket, clients);
			serverThread.init();
			serverThread.start();
		}
	}

}
