package com.example.timecard.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptPassword
{
	private static String salt, password,securePassword;

	public static String setHashPassword(String pass)
	{
		salt = "lksajdhfjjd";
		password = pass;
		securePassword = makeSecurePassword();
		return securePassword;
	}

	private static String makeSecurePassword()
	{
		try{
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(salt.getBytes());
			byte[] bytes = messageDigest.digest(password.getBytes());
			StringBuilder tempString = new StringBuilder();
			for(int i = 0; i<bytes.length;i++){
				tempString.append(Integer.toString((bytes[i]&0xff)+0x100,16).substring(1));
			}
			securePassword = tempString.toString();
		}
		catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return securePassword;
	}
	private byte[] getSalt() throws NoSuchAlgorithmException
	{
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		byte[] saltArray = new byte[16];
		secureRandom.nextBytes(saltArray);
		return saltArray;
	}

	private static String getSecurePassword()
	{
		return securePassword;
	}
	@Override
	public String toString()
	{
		return getSecurePassword();
	}
}
