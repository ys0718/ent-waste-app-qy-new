package com.grkj.ytjqy_pad.utils.common;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密
 * @author binzec
 */
public class DES {
	
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	/**
	 * 加密
	 * @param encryptString	需要加密的值
	 * @param DESKey		加密钥匙
	 * @return				加密值
	 */
	public static String encryptDES(String encryptString, String DESKey) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(DESKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
		return Base64.encode(encryptedData);
	}

	/**
	 * 解密
	 * @param decryptString	需要解密的值
	 * @param DESKey		解密钥匙
	 * @return				解密之后的值（如果解密失败，返回空字符串）
	 */
	public static String decryptDES(String decryptString, String DESKey) {
		try {
			@SuppressWarnings("static-access")
			byte[] byteMi = new Base64().decode(decryptString);
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(DESKey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte decryptedData[] = cipher.doFinal(byteMi);
			return new String(decryptedData);
		} catch (Exception e) {
			return "";
		}
	}
}