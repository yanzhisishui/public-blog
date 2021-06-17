package com.syc.blog.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class MD5Helper {

	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	/**
	 * @description 把明文密码用MD5算法进行加密处理，返回一串加密后的字符串
	 * @author SYC
	 * @param password : 要进行加密的密码
	 * @return 加密后的字符串
	 * */
	public static String encrypt(String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] inputByteArray = password.getBytes();
			messageDigest.update(inputByteArray);
			byte[] resultByteArray = messageDigest.digest();
			return byteArrayToHex(resultByteArray);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	/**
	 * 生成盐
	 * @return
	 */
	public static String createSalt(){
		byte[] salt = new byte[16];
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.nextBytes(salt);
			return byteArrayToHex(salt);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	/**
	 * @description 把明文密码用MD5算法进行加密处理，返回一串加密后的字符串
	 * @author SYC
	 * @param byteArray : 字节数组
	 * @return 加密后的字符串
	 * */
	public static String byteArrayToHex(byte[] byteArray) {
		char[] resultCharArray = new char[byteArray.length * 2];
		int index = 0;
		for (byte b : byteArray) {
 			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
	 	return new String(resultCharArray);
	}

	public static String encryptBySalt(String password,String salt){
		return encrypt(encrypt(password)+salt);
	}

	public static void main(String[] args)  {

		String zhang520620 = encrypt("cch19960119");
		System.out.println(zhang520620);
	}


}
