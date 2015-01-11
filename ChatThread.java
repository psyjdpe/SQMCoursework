import java.net.*;
//import java.util.ArrayList;
import java.io.*;

public class ChatThread extends Thread {
	private Socket socket = null;
	//private static ArrayList <String> users;
	
	public ChatThread(Socket svrSocket){
		super("ChatThread");
		this.socket = svrSocket;
	}
	
	public void run(){
		
		try {
			PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			CommandHandler ch = new CommandHandler();
			String output;
			String input;
			output = ch.handleInput(null);
			serverOutput.println(output);
			
			while((input = clientInput.readLine()) != null){
				serverOutput.println("Please enter a username");
				String nameInput = clientInput.readLine();
				if(nameInput == null){
					return;
				}
				/*try{
					synchronized(users){
						users.add(nameInput);
					}
				}
				catch(Exception e){
					System.out.println(e);
				}*/
				input = clientInput.readLine();
				output = ch.handleInput(input);
				serverOutput.println(output);
				if(output.contains("+OK client signing out")){
					break;
				}
			}
			socket.close();	
		}
		catch(SocketTimeoutException t){
			System.out.println("Connection has timed out");
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		
	}
}
