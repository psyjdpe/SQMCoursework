import java.net.*;
import java.io.*;

public class ChatServer{
	public static void main(String[] args) throws IOException {
	
		int timeout = 10000;
		int port;
		boolean waiting = true;
		ServerSocket svrSocket = null;
		
		
		if(args.length < 1){
			System.err.println("No port number specified, please try again");
			System.exit(1);
		}
		
		port = Integer.parseInt(args[0]);
		
		try{
			svrSocket = new ServerSocket(port);
			Socket userSocket;
			while(waiting){
				userSocket = svrSocket.accept();
				userSocket.setSoTimeout(timeout);
				System.out.println("Client accepted " + userSocket);
				new ChatThread(userSocket).start();
				
			}
		}
		catch (IOException ioe){
			ioe.printStackTrace();
			System.exit(-1);
		}
		finally{
			svrSocket.close();
		}
		
	}
	
}

//doesn't compile with javac on cs computers...
///ChatServer.java:27: undefined reference to `ChatThread::ChatThread(java::net::Socket*)'

