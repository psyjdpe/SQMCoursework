import java.net.*;
import java.util.Vector;
import java.io.*;

public class ClientHandler {

	private Vector<ClientInfo> clients = new Vector<ClientInfo>();
	
	public synchronized void addClient(ClientInfo info){
		clients.add(info);
	}
	
	public synchronized int numClients(){
		int num = clients.size();
		return num;
	}
}
