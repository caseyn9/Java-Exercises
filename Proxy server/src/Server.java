import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

	public static void main(String[] args) {
		final int serverPort = 9999;	//port browser connects to
		final int remotePort = 80;
		ServerSocket socket = null;
		boolean on = true;
		ArrayList <String> blockedUrls = new ArrayList <String>();
		ConcurrentHashMap <Integer, byte[]> cache = new ConcurrentHashMap <Integer, byte[]>(100000);
		try {
			socket = new ServerSocket(serverPort);
			System.out.println("ServerSocket opened on port:  " + serverPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a url to block, 'start' to start server");
		String tmp = scanner.next();
		while(!tmp.equals("start")){
			blockedUrls.add(tmp);
			System.out.println("Enter a url to block, 'start' to start server");
			tmp = scanner.next();
		}
		try{
			scanner.close();
			System.out.println("Starting server");
			while(on){
				new ProxyThread(socket.accept(), remotePort, cache, blockedUrls);
			}
			//socket.close();
		}
		catch(Exception e){
		}
	}

}
