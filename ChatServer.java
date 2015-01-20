import java.net.*;
import java.io.*;
import java.util.*;

//Main class for the server
//initiates the server on a given port and waits for client connections

public class ChatServer{
	//set variables
	private static int timeout = 10000;
	private static int port;
	ServerSocket svrSocket;
	
	//hashtable to store usernames and their corresponding sockets
	//protected to help maintain concurrency
	protected static Hashtable<String, Socket> online = new Hashtable<String, Socket>();
	
	//constructor to set server on a given port number
	public ChatServer(int port) throws IOException{
		waiting(port);
	}
	
	
	public static void main(String[] args) throws IOException {
		//take port from command line
		port = Integer.parseInt(args[0]);
		if(args.length < 1){
			System.err.println("No port number specified, please try again");
			System.exit(1);
		}
		System.out.println("Server started on port: " + port + " waiting for clients...");
		//instantiate server on the given port number
		new ChatServer(port);
	}
	
	
	private void waiting(int port) throws IOException{
		boolean waiting = true;
		try{
			svrSocket = new ServerSocket(port); //instantiates new socket for server
		}catch (IOException ioe){
			ioe.printStackTrace();
			System.exit(-1);
		}
		while(waiting){
			Socket userSocket = svrSocket.accept();	//accepts a socket for a client upon request 
			userSocket.setSoTimeout(timeout * 1000);	//user socket terminates after a period of time
			System.out.println("Client accepted " + userSocket);	//prints to server that a client has connected
			new ChatThread(this, userSocket);	//creates a new thread for the connected client
		}
	}
	
	//method to add username and the corresponding client socket to the hashtable
	//protected and synchronized to maintain concurrency
	protected void AddUser(String username, Socket socket){
		synchronized(online){
			online.put(username, socket);
		}
	}
	
	//same as add user except removing the user instead upon exit
	protected void RemoveUser(Socket socket){
		synchronized(online){
			online.remove(socket);
		}
	}
	
	//method to send message to a given user 
	//(hail uses this class through enumeration to message all connected clients
	protected void Message(String username, String message){
		Socket client = online.get(username);	//gets socket at key username
		PrintWriter writer;	//initialise printwriter for printing to console
		try {
			writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));	//gets the client outputstream
			writer.write("(" + username + "): " + message + "\n");	//prints message
			writer.flush();	//flush all writes in temporary memory (free space)
		} catch (IOException e) {
			e.printStackTrace();
		}

	}	
}

