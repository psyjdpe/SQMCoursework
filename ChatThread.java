import java.net.*;
import java.io.*;

public class ChatThread extends Thread {
	private Socket socket = null;
	private ChatServer cs;
	String state = "auth";
	
	public ChatThread(ChatServer server, Socket svrSocket){
		this.cs = server;
		this.socket = svrSocket;
		start();
	}
	
	
	public void run(){
		
		try (
			PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));){
			
			CommandHandler ch = new CommandHandler();

			String output;
			String input;
			String name;
			if(state.equals("auth")){
				serverOutput.println("Would you like to sign in with a name? (y/n)");
				input = clientInput.readLine();
				if(input.equals("y") || input.equals("Y")){
					serverOutput.println("Please enter a username");
					name = clientInput.readLine();
					if(ChatServer.online.containsKey(name)){
						serverOutput.println("Username already exists, please try again");
						//need to test this...
					} else{
						serverOutput.println("Welcome " + name);
						cs.AddUser(name, socket);
						state = "command";
					}
				} else{
					serverOutput.println("You will continue not logged in, some functions will not be available");
					state = "command";
				}
			}
			if(state.equals("command")){
				while((input = clientInput.readLine()) != null){
					output = ch.handleInput(input);
					if(output.contains("+OK client signing out")){
						serverOutput.println(output);
						break;
					}
					serverOutput.println(output);
				}
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
