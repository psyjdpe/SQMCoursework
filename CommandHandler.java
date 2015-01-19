import java.net.*;
import java.io.*;

public class CommandHandler {

	private ChatServer cs;
	
	String handleInput(String input){
		
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
        	return idenMethod();
        case "LIST":
        	return listMethod();
        case "MESG":
        	return mesgMethod();
        case "HAIL":
        	return hailMethod();
        case "QUIT":
        	return quitMethod();
        }
        
        	return "Error, invalid input";
    }
	//}
	
	
	public String statMethod(){
		int num = ChatServer.online.size();
		return "Numer of users online: " + num;
	}
	
	public String idenMethod(){
		return "identify";
	}
	
	public String listMethod(){
		return "list in handler";
	}
	
	public String mesgMethod(){
		return "message";
	}
	
	public String hailMethod(){
		return "broadcast";
	}
	
	public String quitMethod(){
		return "+OK client signing out";
	}
}
