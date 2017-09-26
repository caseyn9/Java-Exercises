import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class KeyManager {
		static ArrayList<String> users = new ArrayList();
		final static String MANAGER_NAME = "KeyManager";
		
		KeyManager(){
			File dir  = new File(MANAGER_NAME);
			if(!dir.exists()){
				dir.mkdir();
			}
			File publicKeys = new File(MANAGER_NAME+"\\"+"public_keys");
			if(!publicKeys.exists()){
				publicKeys.mkdir();
			}
			populate();
		}
		public void addUser(String email){
			new File(MANAGER_NAME+"\\"+email).mkdir();
			GenerateRSAKeys.generate(MANAGER_NAME+"\\"+email+"/public.txt", MANAGER_NAME+"\\"+email+"/private.txt");
			File publicKey = new File(MANAGER_NAME+"\\"+email+"/public.txt");
			File pubForList = new File(MANAGER_NAME+"\\"+"public_keys"+"\\"+email+".txt");
			//copy public key to the public key list
			InputStream input = null;
			OutputStream output = null;
			try {
				input = new FileInputStream(publicKey);
				output = new FileOutputStream(pubForList);
				byte[] buf = new byte[1024];
				int bytesRead;
				while ((bytesRead = input.read(buf)) > 0) {
					output.write(buf, 0, bytesRead);
				}
			}catch(Exception e){}
			
//			finally {
//				try {
////					input.close();
////					output.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			populate();

		}
		public void deleteUser(String email){
			File user = new File(MANAGER_NAME+"\\"+email);
			File userPub = new File(MANAGER_NAME+"\\"+email+"\\"+"public.txt");
			File userPvt = new File(MANAGER_NAME+"\\"+email+"\\"+"private.txt");
			File userPubKey = new File(MANAGER_NAME+"\\"+"public_keys"+"\\"+email+".txt");
			userPub.delete();
			userPvt.delete();
			user.delete();
			userPubKey.delete();
		}
		//populate KeyManager array list with users.
		public void populate(){
			File root = new File(MANAGER_NAME);
			if(!root.exists()){
				return;
			}
			String[] files = root.list();
			users=null;
			users = new ArrayList<String>();
			for(int i=0; i<files.length; i++){
				users.add(files[i]);
			}
		}
		public boolean searchUser(String username){
			boolean result = false;
			for(int i=0; i<users.size(); i++){
				if(username.equals(users.get(i)))
					result = true;
			}
			return result;
		}
		public String getPublicKey(String user){
			if(searchUser(user)){
				return(MANAGER_NAME+"\\"+"public_keys"+"\\"+user+".txt");
			}
			else return null;
		}
		public String getPrivateKey(String user){
			if(searchUser(user)){
				return(MANAGER_NAME+"\\"+user+"\\"+"private.txt");
			}
			else return null;
		}
}
