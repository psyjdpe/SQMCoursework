import java.io.*;
import java.net.*;

public class ClientMain {
    
	//main client class that users run in order to establish a connection to the server
	//acts as a text based message response system
	
	public static void main(String[] args) throws IOException {
        
		//takes command line arguments (host/IP and port number)
        if (args.length != 2) {
            System.err.println(
                "Error, please give host name and port number");
            System.exit(1);
        }
        
        String hostName = args[0];											//set hostName as host given by client
        int port = Integer.parseInt(args[1]);								//set port number
        System.out.println("Trying to connect to server, please wait...");
        
        //create readers and writers for socket
        try (
            Socket socket = new Socket(hostName, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        ) {
        	//reader to read input
            BufferedReader input =
                new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;
            System.out.println("Connected");
            
            //while server can pass text
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);	//print response form the server
                
                //print user input
                fromUser = input.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Couldn't find host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
}