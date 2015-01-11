
public class CommandHandler {

	
	String handleInput(String input){
		
		String[] split = (input.split(" "));
		String cmd = split[0].toUpperCase();
		/*String arg1 = null;
		String arg2 = null;
		
        if (split.length > 1) { 	
            arg1 = split[1]; 
        } 
        
        if(split.length > 2) {	
        	arg1 = split[2];
        }*/
		
        
        if(cmd == "STAT"){
        	return statMethod();
        }
        else if(cmd == "IDEN"){
        	return idenMethod();
        }
        else if(cmd == "LIST"){
        	return listMethod();
        }
        else if(cmd == "MESG"){
        	return mesgMethod();
        }
        else if(cmd == "HAIL"){
        	return hailMethod();
        }
        else if(cmd == "QUIT"){
        	return quitMethod();
        } else{
        
        /*switch(cmd){
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
        }*/
        
        	return "Error, invalid input";
        }
	}
	
	public String statMethod(){
		return "stat";
	}
	
	public String idenMethod(){
		return "identify";
	}
	
	public String listMethod(){
		return "list";
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
