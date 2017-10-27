package com.quinnox.utils;

import javax.crypto.Cipher;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

public class EnctryptPassword {

	public EnctryptPassword() {
		// TODO Auto-generated constructor stub
	}
	
	public String encryptThis(String simpleText){
		
		 StringBuilder sb = new StringBuilder();
		 String enc = null;
		try{
			String key = "Bar12345Bar12345"; // 128 bit key
	        // Create key and cipher
	        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        // encrypt the text
	        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
	        byte[] encrypted = cipher.doFinal(simpleText.getBytes());
	       
	        for (byte b: encrypted) {
	            sb.append((char)b);
	        }
	        // the encrypted String
	        enc = sb.toString();
		}
		catch(Exception e) {
            e.printStackTrace();
        }
		return enc;		
	}
	
	public String decryptThis(String encryptedText){
				 
		 String decrypted = null;
		try{
			String key = "Bar12345Bar12345"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            
			byte[] bb = new byte[encryptedText.length()];
            for (int i=0; i<encryptedText.length(); i++) {
                bb[i] = (byte) encryptedText.charAt(i);
            }
            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            decrypted = new String(cipher.doFinal(bb));
		}
		catch(Exception e) {
           e.printStackTrace();
       }
		return decrypted;	
	}
}
