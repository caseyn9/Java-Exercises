
public class MessageSigner {

	//Signs text by finding hashValue of text, then encrypting hashValue with private key of sender and appending onto message.
	public static String sign(String pvtKeyFilename, String message) throws Exception {
		Integer hashValue= message.hashCode();
		System.out.println("hashValue :" + hashValue);
		String signature = RSAEncryption.encrypt(pvtKeyFilename, hashValue.toString(), false);
		return "/" + signature;
	}
	
	//validates decrypted message by extracting signature
	public static boolean validate(String pubKeyFilename, String message) {
		char c = 'a';
		int i;
		for(i=message.length(); i!=0 && c!='/' ; i--){		//walk backwards on string until beginning of signature is encountered.
			c = message.charAt(i-1);
			//System.out.println(c);
		}
		if(c!= '/'){ System.out.println("C != /");return false;}
		String signature = message.substring(i+1, message.length());
		String trueMsg = message.substring(0,i);
		System.out.println("Signature: " + signature);
		int sigHashValue = Integer.parseInt(RSADecryption.decrypt(pubKeyFilename, signature, false));
		int msgHashValue = trueMsg.hashCode();
		System.out.println("signedHashV: " + sigHashValue+ "\nmsgHashV: " + msgHashValue);
		if(sigHashValue == msgHashValue){
			return true;
		}
		return false;
	}
}
