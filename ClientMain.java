import java.io.*;
import java.net.*;

public class ClientMain {
    
	public static void main(String[] args) throws IOException {
        
        if (args.length != 2) {
            System.err.println(
                "Error, please give host name and port number");
            System.exit(1);
        }

        String hostName = args[0];
        int port = Integer.parseInt(args[1]);
        System.out.println("Trying to connect to server, please wait...");

        try (
            Socket socket = new Socket(hostName, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        ) {
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;
            System.out.println("Connected");

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                
                fromUser = stdIn.readLine();
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