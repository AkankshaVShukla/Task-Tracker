package com.quinnox.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenGenerator {

	public TokenGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public static String generateUniqueToken() throws NoSuchAlgorithmException{

		// Unique token will be generated using System's nano-time
		
		MessageDigest instance = MessageDigest.getInstance("MD5");
		byte[] messageDigest = instance.digest(String.valueOf(System.nanoTime()).getBytes());
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < messageDigest.length; i++) {
		    String hex = Integer.toHexString(0xFF & messageDigest[i]);
		    if (hex.length() == 1) {
		        // could use a for loop, but we're only dealing with a single
		        // byte
		        hexString.append('0');
		    }
		    hexString.append(hex);
		}
		return hexString.toString();
	}
}
