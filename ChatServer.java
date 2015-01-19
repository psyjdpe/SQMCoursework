import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer{
	
	private static int timeout = 10000;
	private static int port;
	ServerSocket svrSocket = null;
	
	protected static Hashtable<String, Socket> online = new Hashtable<String, Socket>();
	
	public ChatServer(int port) throws IOException{
		waiting(port);
	}
	
	
	public static void main(String[] args) throws IOException {
	
		port = Integer.parseInt(args[0]);
		if(args.length < 1){
			System.err.println("No port number specified, please try again");
			System.exit(1);
		}
		System.out.println("Server started on port: " + port + " waiting for clients...");
		new ChatServer(port);
	}
	
	
	private void waiting(int port) throws IOException{
		boolean waiting = true;
		try{
			svrSocket = new ServerSocket(port);
		}catch (IOException ioe){
			ioe.printStackTrace();
			System.exit(-1);
		}	
		while(waiting){
			Socket userSocket = svrSocket.accept();
			userSocket.setSoTimeout(timeout * 1000);
			System.out.println("Client accepted " + userSocket);
			new ChatThread(this, userSocket);
		}
	}
	
	protected void AddUser(String username, Socket socket){
		synchronized(online){
			online.put(username, socket);
		}
	}
	
	protected void RemoveUser(Socket socket){
		synchronized(online){
			online.remove(socket);
		}
	}
	
	
	
}

