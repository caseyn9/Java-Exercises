import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.io.IOUtils;

public class ProxyThread extends Thread {
	private Socket socket =null;
	Socket remoteSocket = null;
	int remotePort = -1;
	
	String callUrl = "";
	OutputStream outputStreamClient = null;
	String inputLine = null;
	String[] tokens = null;
	static ConcurrentHashMap<Integer, byte[]> cache;
	static ArrayList <String> blockedUrls;
	boolean http = true;
	
	public ProxyThread(Socket socket, int port, ConcurrentHashMap<Integer, byte[]> cache, ArrayList <String> blockedUrls){
		this.socket=socket;
		this.remotePort = port;
		this.start();
		ProxyThread.cache = cache;
		ProxyThread.blockedUrls = blockedUrls;
	}
	
	public void run(){
		try {			
			String inputLine = getRequest(socket);	// get request from browser
			if(inputLine != null){
				tokens = inputLine.split(" ");	//tokens[1] = url
				callUrl = tokens[1];
				System.out.println(inputLine + "\n" + callUrl);
			}
			else{
				System.out.println("inputLine is null!");
			}

			boolean blocked = checkBlocked();
			if(!blocked){
				InputStream inStreamServer = sendRequest();
				writeToClient(socket, inStreamServer);
			}
			else{
				System.out.println("URL BLOCKED");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean checkBlocked() {
		if(blockedUrls.contains(callUrl)){
			return true;
		}
		return false;
		
	}

	//writes content of inputStream or cache to the client.
	public void writeToClient(Socket sock, InputStream inStream){
		//send response to client
		int BUFFER_SIZE = 65536;
		byte[] reply = new byte[BUFFER_SIZE];
		int length=0;
		OutputStream outStream;
		try {
			outStream = sock.getOutputStream();
			if(http){
				byte[] bytes = cache.get(callUrl.hashCode());
				if(bytes != null){
					System.out.println("reading from cache");
					outStream.write(bytes);
				}
				else{
					//System.out.println("ERROR: bytes not in cache.");
				}
			}
				
			outStream.flush();
			if(outStream != null)
				outStream.close();
			if(inStream != null)
				inStream.close();
			socket.close();
			//System.out.println("cache: " + cache);
			System.out.println("*output written.*");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	//gets request from client, returns String.
	public String getRequest(Socket passSocket){
		try {
			InputStream inputStream = passSocket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String line = in.readLine();
			return line;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//end get request
		return null;
	}

	//Establish connection with url. Cache url contents.
	public InputStream sendRequest(){
		URL url;
		HttpURLConnection httpUrl;
		HttpsURLConnection httpsUrl;
		InputStream inStream = null;
		try {
			//if url is http
			//if(!(tokens[0].equals("CONNECT")))
			{
				if(cacheMiss(callUrl)){
					System.out.println("Cache miss");
					http = true;
					//System.out.println("http****");
					url = new URL(callUrl);
					httpUrl = (HttpURLConnection) url.openConnection();
					httpUrl = (HttpURLConnection) url.openConnection();
					httpUrl.setRequestProperty("User-Agent", "Mozilla/5.0");
					inStream = httpUrl.getInputStream();
					writeToCache(inStream, callUrl);
					System.out.println(callUrl + " now in cache i swear");
					return inStream;
				}
				else{
					System.out.println("Cache hit");
				}
			}
			//if url is https
			/*else{
				 // send ok message to client
				http = false;
                String connectResponse = "HTTP/1.0 200 Connection established\n" +
                                         "Proxy-agent: ProxyServer/1.0\n" +
                                         "\r\n";
                DataOutputStream out =  new DataOutputStream(socket.getOutputStream());
                out.write(connectResponse.getBytes(Charset.forName("UTF-8")));
                out.flush();
                System.out.println("responded OK to client");
                // end send ok to client
                
				System.out.println("httpS****");//url is https
				String tmpUrl = callUrl;
				tmpUrl = makeHttpsUrl(callUrl);
				System.out.println(tmpUrl);
				url = new URL(tmpUrl);
				httpsUrl = (HttpsURLConnection) url.openConnection();
				//httpsUrl.setRequestProperty("User-Agent", "Mozilla/5.0");
				inStream = httpsUrl.getInputStream();
				printContent(inStream);
				if(inStream == null){
					System.out.println("https instream is null");
				}
				return inStream;
				}
				*/
		}
		catch(Exception e){
			//System.out.println("EXCEPTION input stream now null");
			return null;
		}
		return inStream;
	}
	
	public String makeHttpsUrl(String original){
		return  "https://" + original.substring(0, original.indexOf(':', 0));
	}
	
	public static void printContent(InputStream inStream){
		BufferedReader br  = new BufferedReader(new InputStreamReader(inStream));
		String inputString;
		try {
			System.out.println("Printing content of printstream");
			while((inputString = br.readLine()) != null){
				System.out.println(inputString);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void writeToCache(InputStream is, String url){
		try {
			System.out.println("Writing " + url + " to cache.");
			byte[] bytes = IOUtils.toByteArray(is);
			cache.put(url.hashCode(), bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}	//input stream to byte array
	}
	public static boolean cacheMiss(String url){
		return !cache.containsKey(url.hashCode());
	}
	
	public static void blockUrl(String url){
		blockedUrls.add(url);
	}
}
		

