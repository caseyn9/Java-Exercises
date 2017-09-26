

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Main {
	static StringBuilder text = new StringBuilder();
	static String emailSender="";
	static ArrayList<String> attachments = new ArrayList<String>();
	static KeyManager keyManager = new KeyManager();
	static String username;
	static String password;
	static String host = "pop.gmail.com";// change accordingly
	static String storeType = "pop3";
	static boolean debug = true;
	
	public static void main(String[] args) {
		if(debug == true){
			username = "nickycasey54321@gmail.com";
			password = "nicky123";
			viewEmail();
			
//			username = "nickycasey12345@gmail.com";
//			password = "nicky123";
//			sendSecureEMail();
			
//			keyManager.addUser("nickycasey12345@gmail.com");
//			keyManager.addUser("nickycasey54321@gmail.com");
//			keyManager.addUser("nicky.o.c.gcr@gmail.com");
//			keyManager.addUser("caseyn9@tcd.ie");
			
//			keyManager.deleteUser("caseyn9@tcd.ie");
//			keyManager.deleteUser("nicky.o.c.gcr@gmail.com");
			
//			String message = "howya";
//			String signed = null;
//			try {
//				signed = MessageSigner.sign(keyManager.getPrivateKey("nickycasey12345@gmail.com"), message);	//sign messge with private key of sender
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			String encrypted = encrypt(keyManager.getPublicKey("nickycasey54321@gmail.com"),message); //encrypt message to send.
//			//signature is appended to encypted message, message sent
//			//On receive, signature taken off of end of message. 
//			String decrypted = decrypt(keyManager.getPrivateKey("nickycasey54321@gmail.com"),encrypted); //decrypt message after signature removed.
//			
//			System.out.println("Signed message: " + message+signed);
//			System.out.println("Encrypted message: " + encrypted);  
//			System.out.println("Decrypted message: " + decrypted);
//			if(MessageSigner.validate(keyManager.getPublicKey("nickycasey12345@gmail.com"), message+signed)){	//validate signature with public key of sender
//				System.out.println("Valid");
//			}
		}
		else{
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter your username");
			username = scanner.next();
			System.out.println(username);
			if(!keyManager.searchUser(username)){
				System.out.println("Warning account not recognised, exit when prompted.");
			}
			System.out.println("Enter your password");
			password = scanner.next();
			System.out.println("Enter a command, '?' for help.");
			String command;
			command=scanner.next();
			while(!command.equals("exit")){
				switch(command){

				case "view_mail_list":	viewMailingList();
				break;
				case "add_user": addUser();
				break;
				case "delete_user": deleteUser(scanner.next());
				break;
				case "send_email": sendSecureEMail();
				break;
				case "view_email": viewEmail();
				break;
				case "?": System.out.println("COMMANDS\n,'view_mail_list'\n'add_user'\n'delete_user'\n'send_email'\n'view_email'\n");
				break;
				default:	System.out.println("Invalid command, enter '?' for help");
				}
				System.out.println("Enter a command, '?' for help.");
				if(scanner.hasNext()){
					command=scanner.next();
				}
			}
			scanner.close();
		}
		//keyManager.addUser("caseyn10@tcd.ie");
		//keyManager.deleteUser("caseyn10@tcd.ie");
		
//		String publicKey = "public.txt";
//		String privateKey = "private.txt";
//		
//		generateKeys(publicKey,privateKey);
//		File publicFile = new File("public.txt");
//		File privateFile = new File("private.txt");
//		FileReader fileReader;
//		String  pubKey, pvtKey;
//		byte[] encodedPub = null, encodedPvt = null;
//		try {
//			encodedPub = Files.readAllBytes(Paths.get("public.txt"));
//			encodedPvt = Files.readAllBytes(Paths.get("private.txt"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		pubKey = new String(encodedPub);
//		pvtKey = new String(encodedPvt);
//		String newMsg = "nathin happened boiii";
//		String signedMsg = "ERROR";
//		try {
//			signedMsg = MessageSigner.sign("private.txt", newMsg);
//			System.out.println("SIGNED MSG: " + signedMsg);
//			if(MessageSigner.validate("public.txt", "private.txt", signedMsg)){
//				System.out.println("---VALID---");
//			}
//			else{
//				System.out.println("---NOPE---");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(newMsg);
//		String encryptedMsg = RSAEncryption.encrypt("public.txt", newMsg);
//		System.out.println("\n\nENCRYPTED: " + encryptedMsg);
//		String decryptedMsg = RSADecryption.decrypt("private.txt", encryptedMsg);
//		System.out.println("DECRYPTED: " + decryptedMsg);
		
		//check(host, mailStoreType, username, password, 5);
		//sendEmail(username,password,recipient,"stratfob@tcd.ie");
	}
	
	public static void viewMailingList(){
		ArrayList<String> userList = KeyManager.users;
		for(int i=0; i<userList.size(); i++){
			System.out.println(userList.get(i));
		}
	}
	public static void addUser(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter an email to add, or 'exit' to return\n");
		String command;
		command=scanner.next();
		if(command.equals("exit")){
			scanner.close();
			return;
		}
		keyManager.addUser(command);
		scanner.close();
	}
	public static void deleteUser(String user){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter user to delete");
		keyManager.deleteUser(user);
		System.out.println("Deleted");
		scanner.close();
	}
	public static void sendSecureEMail(){
		Scanner scanner = new Scanner(System.in);
		String recipient = "";
		String subject;
		String message;
		if(debug){
			recipient = "nickycasey54321@gmail.com";
			subject = "subject";
			message="howya";
		}
		else{
			while(!keyManager.searchUser(recipient)){
				System.out.println("Enter recipient");
				recipient = scanner.next();
			}
			System.out.println("Enter subject line");
			scanner.nextLine();
			subject = scanner.nextLine();
			System.out.println("Enter message");
			message = scanner.nextLine();
		}
		String signedMessage="";
		try {
			//signing the message with senders private key.
			signedMessage = MessageSigner.sign(keyManager.getPrivateKey(username), message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Signed msg: " + signedMessage);
		String path = keyManager.getPublicKey(recipient);
		
		//encrypt unsigned message
		String encryptedMsg = encrypt(path,message);
		System.out.println("Encrypted msg: " + encryptedMsg);
		String trueMessage = encryptedMsg+signedMessage;
		System.out.println("true msg: " + trueMessage);
		sendEmail(recipient,subject,trueMessage);
		scanner.close();
	}
	public static void viewEmail(){
		check(1);
		//text is an instance variable that is built in the check method.
		String encryptedAndSignedMessage = text.toString();
		
		char c = 'a';
		int i;
		for(i=encryptedAndSignedMessage.length(); i!=0 && c!='/' ; i--){		//walk backwards on string until beginning of signature is encountered.
			c = encryptedAndSignedMessage.charAt(i-1);
		}
		//if(c!= '/'){ System.out.println("C != /");return ;}
		String signature = encryptedAndSignedMessage.substring(i+1, encryptedAndSignedMessage.length());
		//System.out.println(signature);
		String encryptedMessage = encryptedAndSignedMessage.substring(0,i);
		String decryptedMessage = decrypt(keyManager.getPrivateKey(username),encryptedMessage);
		System.out.println("Email body:\n" + decryptedMessage);
		String decryptedAndSignedMessage = decryptedMessage+"/"+signature;
		System.out.println("Decrypted and Signed message :" + decryptedAndSignedMessage);
		if(MessageSigner.validate(keyManager.getPublicKey(emailSender), decryptedAndSignedMessage)){
			System.out.println("SIGNATURE VALID");
		}
		else{
			System.out.println("SIGNATURE INVALID");
		}
	}
	public static void sendEmail(String recipient, String subject, String body){
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username,password);
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recipient));
			message.setSubject(subject);
			System.out.println("Email body: " + body);
			message.setText(body);

			Transport.send(message);

			System.out.println("Email sent.");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void check( int amount) 
		   {
		      try {

		      //create properties field
		      Properties properties = new Properties();

		      properties.put("mail.pop3.host", host);
		      properties.put("mail.pop3.port", "995");
		      properties.put("mail.pop3.starttls.enable", "true");
		      Session emailSession = Session.getDefaultInstance(properties);
		  
		      //create the POP3 store object and connect with the pop server
		      Store store = emailSession.getStore("pop3s");

		      store.connect(host, username, password);

		      //create the folder object and open it
		      Folder emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);

		      // retrieve the messages from the folder in an array and print it
		      Message[] messages = emailFolder.getMessages();
		      System.out.println("messages.length---" + messages.length);

		      for (int i = messages.length-1 ; i < messages.length; i++) {
		         Message message = messages[i];
		         System.out.println("---------------------------------");
		         System.out.println("Email Number " + (i + 1));
		         System.out.println("Subject: " + message.getSubject());
		         System.out.println("From: " + message.getFrom()[0]);
		         emailSender = message.getFrom()[0].toString();
		        // parseMultipart((MimeMultipart) message.getContent());
		         //System.out.println(message.getContent());
		         text.append(message.getContent());
		         //System.out.println(text);
		         //text.delete(0,text.length());
		         
		      }

		      //close the store and folder objects
		      emailFolder.close(false);
		      store.close();

		      } catch (NoSuchProviderException e) {
		         e.printStackTrace();
		      } catch (MessagingException e) {
		         e.printStackTrace();
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
		   }
//	private static void parseMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
//		  for (int i = 0; i < mimeMultipart.getCount(); i++) {
//		    BodyPart part = mimeMultipart.getBodyPart(i);
//		    if (part.getContent() instanceof MimeMultipart) {
//		      parseMultipart((MimeMultipart) part.getContent());
//
//		    }
//		    if (part.getContentType().contains("text/plain")){// || part.getContentType().contains("text/html")) {
//		    	text.append(part.getContent());		    
//		    }
//		    if (part.getHeader("Content-Disposition") != null) {
//		      attachments.add(part.getHeader("Content-Disposition")[0]);
//		    }
//		  }
//		}
	
	public static void generateKeys(String publicKeyFilename, String privateKeyFilename){
		GenerateRSAKeys.generate(publicKeyFilename, privateKeyFilename);
	}
	public static String encrypt(String pubKeyFilename, String text){
		return RSAEncryption.encrypt(pubKeyFilename, text,true);
	}
	public static String decrypt(String privateKeyFilename, String encryptedText){
		return RSADecryption.decrypt(privateKeyFilename, encryptedText,true);
	}
}