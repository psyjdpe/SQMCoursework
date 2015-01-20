import java.net.*;
import java.io.*;
import java.util.*;

//ChatThread class, used as a new thread for each client to login and enter commands

public class ChatThread extends Thread {
	//initial variables
	private Socket socket;
	private ChatServer cs;
	String state = "auth";
	boolean loggedIn;
	int messageCount = 0;
	
	//constructor to give each client the server socket they are using and their client socket
	public ChatThread(ChatServer server, Socket svrSocket){
		this.cs = server;
		this.socket = svrSocket;
		start();	//starts the thread on the given socket.
	}
	
	//run method on creation of thread
	public void run(){
		
		try (	//give client readers and writers for passing and receiving input
			PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));){

			String output;
			String input;
			String name;
			while(true){
				//if in authentication stage, give a username or proceed without
				if(state.equals("auth")){
					serverOutput.println("Would you like to sign in with a name? (y/n)");
					input = clientInput.readLine();	//take client input
					if(input.equals("y") || input.equals("Y")){
						serverOutput.println("Please enter a username");
						name = clientInput.readLine();
						if(ChatServer.online.containsKey(name)){	//if the given username already exists and proceed
							serverOutput.println("Username already exists, please try again");
						} else{
							serverOutput.println("Welcome " + name);
							cs.AddUser(name, socket);	//if username not taken, add the username and socket to ChatServer hashtable
							loggedIn = true;	//set logged in to true (used for some methods)
							state = "command";	//set state to command to allow use of the commands available
							break;
						}
					} else{
						//notify user they are not logged in
						serverOutput.println("You will continue not logged in, some functions will not be available");
						state = "command";	//set state to command to allow user to use commands
						loggedIn = false;	//but set logged in to equal false
						break;
					}
				}
			}
			//main loop for when user is (not)logged in to use the clients commands
			if(state.equals("command")){
				while((input = clientInput.readLine()) != null){	//take input
					output = handleInput(input);	////process input according to command handler
					if(output.contains("+OK client signing out")){	//if quit, break
						serverOutput.println(output);
						break;
					}
					serverOutput.println(output);	//server outputs the handled input
				}
			}
			System.out.println("User has logged out");	//server shows when a user has logged out
		}
		//exceptions for the above try block
		catch(SocketTimeoutException t){
			System.out.println("Connection has timed out");
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		catch(NullPointerException np){
			np.printStackTrace();
		}
		//if all else fails, close the socket
		finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//method for handling client input
	private String handleInput(String input){
		
		//splits the command by space characters, useful for commands that take extra arguments
		String[] split = (input.split(" "));
		String cmd = split[0].toUpperCase();	//set all to upper case to ensure no failed commands due to incorrect case
		String arg1 = null;
		String arg2 = null;
		
		//set extra arguments
        if (split.length > 1) { 	
            arg1 = split[1]; 
        } 
        
        if(split.length > 2) {	
        	arg1 = split[2];
        }
		
        //main switch statement to determine which methods to call
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
        
        //if none of the above, error
        return "Error, invalid input";
    }
	
	//returns number of users online
	public String statMethod(){
		int num = ChatServer.online.size();	//size of hashtable
		//return string according to login status
		if(loggedIn == true){
			return "+OK You are currently logged In: Numer of users online: " + num;
		} else {
			return "+OK You are not logged in: Numer of users online: " + num;
		}
	}
	
	//determine whether a username is being used
	public String idenMethod(String username){
		if(ChatServer.online.containsKey(username)){	//hashtable contains desired username?
			return "-ERR Username: " + username + " exists already";
		}
		else{
			return "+OK Username "+ username + " not being used";
		}
	}
	
	//list all online users
	public String listMethod(){
		Enumeration<String> users = ChatServer.online.keys();	//enumerates through the hashtable
		StringBuilder string = new StringBuilder("");			//string builder for output string
		if(loggedIn == false){
			return "-ERR You need to be logged in to do that";	//error for non logged in users
		}
		else {
			while(users.hasMoreElements()){						//while there are still more keys to be processed
				string.append(" " + users.nextElement());		//add key (username) to the string
			}
			String result = string.toString();					//ensure the built string is a string
			return "+OK Users currently online: " + result;		//return results
		}
	}
	
	//method to interact with the server, passes username and message to the server, server then sends the message to the correct username.
	public String mesgMethod(String arg1, String arg2){
		cs.Message(arg1, arg2);
		messageCount++;					//increments message count, used in other commands
		return "+OK Message Sent";
	}
	
	//broadcast method, distribute message to all connected clients through the chat server.
	public String hailMethod(String arg1){
		Enumeration<String> users = ChatServer.online.keys();		//enumerate through all online users.
		while(users.hasMoreElements()){								//until end of hashtable
			String username = users.nextElement();					//get the current username
			cs.Message(username, arg1);								//pass the current username and message to the chat server to distribute
		}
		messageCount++;												//add this as one message sent
		return "+OK Broadcast message to all online users";
	}
	
	//quit method, removes user from the list of online users, returns string to break the running while loop.
	public String quitMethod(){
		cs.RemoveUser(socket);
		return "+OK client signing out";
	}
}


