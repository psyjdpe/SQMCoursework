import java.net.*;
import java.io.*;

public class ChatThread extends Thread {
	private Socket socket = null;
	//private String nameInput = null;

	
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
			//String name;
			serverOutput.println("Please enter a username");
			while((input = clientInput.readLine()) != null){
				/*while(nameInput == null){

					nameInput = clientInput.readLine();
					if(nameInput == null){
						return;
					} else {
						name = nameInput;
						serverOutput.println("Welcome " + name);
					}*/
				
				output = ch.handleInput(input);
				serverOutput.println(output);
				if(output.contains("+OK client signing out")){
					break;
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
