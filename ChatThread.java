import java.net.*;
import java.io.*;

public class ChatThread extends Thread {
	private Socket socket = null;

	
	public ChatThread(Socket svrSocket){
		super("ChatThread");
		this.socket = svrSocket;
	}
	
	public void run(){
		
		try (
			PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));){
			
			CommandHandler ch = new CommandHandler();
			String output;
			String input;
			String name;
			serverOutput.println("Would you like to sign in with a name? (y/n)");
			input = clientInput.readLine();
			if(input.equals("y") || input.equals("Y")){
				serverOutput.println("Please enter a username");
				name = clientInput.readLine();
				serverOutput.println("Welcome " + name);
				//create method to add name to list of users
			} else{
				serverOutput.println("You will continue not logged in, some functions will not be available");
			}
			while((input = clientInput.readLine()) != null){
				output = ch.handleInput(input);
				if(output.contains("+OK client signing out")){
					break;
				}
				serverOutput.println(output);				
			}
			socket.close();	
			System.out.println("User has logged out");
			}
		
		catch(SocketTimeoutException t){
			System.out.println("Connection has timed out");
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		
	}
}
