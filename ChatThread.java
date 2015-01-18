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
			
			ClientInfo clientInfo = new ClientInfo();
			CommandHandler ch = new CommandHandler();
			ClientHandler chan = new ClientHandler();
			clientInfo.socket = this.socket;
			String output;
			String input;
			String name;
			serverOutput.println("Would you like to sign in with a name? (y/n)");
			input = clientInput.readLine();
			if(input.equals("y") || input.equals("Y")){
				serverOutput.println("Please enter a username");
				name = clientInput.readLine();
				serverOutput.println("Welcome " + name);
				clientInfo.username = name;
				chan.addClient(clientInfo);
			} else{
				serverOutput.println("You will continue not logged in, some functions will not be available");
				clientInfo.socket = null;
			}
			while((input = clientInput.readLine()) != null){
				output = ch.handleInput(input);
				if(output.contains("+OK client signing out")){
					serverOutput.println(output);
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
