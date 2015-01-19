import java.net.*;
import java.io.*;
import java.util.*;

public class ChatThread extends Thread {
	private Socket socket;
	private ChatServer cs;
	String state = "auth";
	boolean loggedIn;
	
	public ChatThread(ChatServer server, Socket svrSocket){
		this.cs = server;
		this.socket = svrSocket;
		start();
	}
	
	
	public void run(){
		
		try (
			PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));){

			String output;
			String input;
			String name;
			while(true){
				if(state.equals("auth")){
					serverOutput.println("Would you like to sign in with a name? (y/n)");
					input = clientInput.readLine();
					if(input.equals("y") || input.equals("Y")){
						serverOutput.println("Please enter a username");
						name = clientInput.readLine();
						if(ChatServer.online.containsKey(name)){
							serverOutput.println("Username already exists, please try again");
						} else{
							serverOutput.println("Welcome " + name);
							cs.AddUser(name, socket);
							loggedIn = true;
							state = "command";
							break;
						}
					} else{
						serverOutput.println("You will continue not logged in, some functions will not be available");
						state = "command";
						loggedIn = false;
						break;
					}
				}
			}
			if(state.equals("command")){
				while((input = clientInput.readLine()) != null){
					output = handleInput(input);
					if(output.contains("+OK client signing out")){
						serverOutput.println(output);
						break;
					}
					serverOutput.println(output);
				}
			}
			System.out.println("User has logged out");
		}
		
		catch(SocketTimeoutException t){
			System.out.println("Connection has timed out");
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		catch(NullPointerException np){
			np.printStackTrace();
		}
		finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String handleInput(String input){
		
		String[] split = (input.split(" "));
		String cmd = split[0].toUpperCase();
		String arg1 = null;
		String arg2 = null;
		
        if (split.length > 1) { 	
            arg1 = split[1]; 
        } 
        
        if(split.length > 2) {	
        	arg1 = split[2];
        }
		        
        switch(cmd){
        case "STAT":
        	return statMethod();
        case "IDEN":
        	return idenMethod(arg1);
        case "LIST":
        	return listMethod();
        case "MESG":
        	return mesgMethod(arg1, arg2);
        case "HAIL":
        	return hailMethod(arg1);
        case "QUIT":
        	return quitMethod();

        }
        
        	return "Error, invalid input";
    }
	
	public String statMethod(){
		int num = ChatServer.online.size();
		if(loggedIn == true){
			return "You are currently logged In: Numer of users online: " + num;
		} else {
			return "You are not logged in: Numer of users online: " + num;
		}
	}
	
	public String idenMethod(String username){
		if(ChatServer.online.containsKey(username)){
			return "Username: " + username + " exists already";
		}
		else{
			return "Username "+ username + " not being used";
		}
	}
	
	public String listMethod(){
		Enumeration<String> users = ChatServer.online.keys();
		StringBuilder string = new StringBuilder("");
		while(users.hasMoreElements()){
			string.append(" " + users.nextElement());
		}
		String result = string.toString();
		return "Users currently online: " + result;
	}
	
	public String mesgMethod(String arg1, String arg2){
		cs.Message(arg1, arg2);
		return "Message Sent";
	}
	
	public String hailMethod(String arg1){
		Enumeration<String> users = ChatServer.online.keys();
		while(users.hasMoreElements()){
			String username = users.nextElement();
			cs.Message(username, arg1);
		}
		return "Broadcast message to all online users";
	}
	
	public String quitMethod(){
		cs.RemoveUser(socket);
		return "+OK client signing out";
	}
}


