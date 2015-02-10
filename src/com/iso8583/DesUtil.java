package com.iso8583;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DesUtil {

	    private static byte[] doCrypt(boolean encrypt, byte[] key, byte[] input, int offset, int length) {
	        if (length % 8 != 0) {
	            throw new RuntimeException("SYS_DES_INPUT_LENGTH_MUST_DIVIDED_BY_8: " + length);
	        }
	        if (key.length < 8) {
	            throw new RuntimeException("SYS_DES_KEY_LENGTH_MUST_GREATER_THAN_8: " + key.length);
	        }
	        if (key.length > 8) {
	            byte[] k = new byte[8];
	            System.arraycopy(key, 0, k, 0, 8);
	            key = k;
	        }
	        try{
	            Cipher c1 = Cipher.getInstance("DES/ECB/NoPadding");
	            c1.init((encrypt) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DES"));
	            return c1.doFinal(input, offset, length);
	        } catch (Throwable t) {
	            throw new RuntimeException("SYS_DES_DECRYPT_ERROR", t);
	        }
	    }

	    private static byte[] doTripleCrypt(boolean encrypt, byte[] key, byte[] input, int offset, int length) {
	        if (input.length % 8 != 0) {
	            throw new RuntimeException("SYS_3DES_INPUT_LENGTH_MUST_DIVIDED_BY_8: " + input.length);
	        }
	        if ((key.length < 0) || (key.length > 24) || (key.length % 8 != 0)) {
	            throw new RuntimeException("SYS_3DES_INVALID_KEY_LENGTH: " + key.length);
	        }
	        byte[] k = new byte[24];
	        if (key.length == 8) {
	            System.arraycopy(key, 0, k, 0, 8);
	            System.arraycopy(key, 0, k, 8, 8);
	            System.arraycopy(key, 0, k, 16, 8);
	        } else if (key.length == 16) {
	            System.arraycopy(key, 0, k, 0, 16);
	            System.arraycopy(key, 0, k, 16, 8);
	        } else {
	            k = key;
	        }
	        try{
	            Cipher c1 = Cipher.getInstance("DESede/ECB/NoPadding");
	            c1.init((encrypt) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, new SecretKeySpec(k, "DESede"));
	            return c1.doFinal(input, offset, length);
	        } catch (Throwable t) {
	            throw new RuntimeException("SYS_3DES_DECRYPT_ERROR", t);
	        }
	    }

	    public static byte[] tripleDecrypt(byte[] key, byte[] input) {
	        return doTripleCrypt(false, key, input, 0, input.length);
	    }

	    public static byte[] tripleEncrypt(byte[] key, byte[] input){
	        return doTripleCrypt(true, key, input, 0, input.length);
	    }

	    public static byte[] encrypt(byte[] key, byte[] input) {
	        return doCrypt(true, key, input, 0, input.length);
	    }

	    public static byte[] decrypt(byte[] key, byte[] input) {
	        return doCrypt(false, key, input, 0, input.length);
	    }

	}
