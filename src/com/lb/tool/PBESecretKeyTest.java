package com.lb.tool;

import java.security.spec.KeySpec;  

import javax.crypto.Cipher;  
import javax.crypto.SecretKey;  
import javax.crypto.SecretKeyFactory;  
import javax.crypto.spec.PBEKeySpec;  
import javax.crypto.spec.PBEParameterSpec; 

import org.jasypt.util.text.BasicTextEncryptor;

public class PBESecretKeyTest {
    public static byte[] secretEncrypt(String key, String data) throws Exception{       
        //使用PBEWithMD5AndDES算法获取Cipher实例  
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");  
        //初始化密钥  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");  
        KeySpec keySpec = new PBEKeySpec(key.toCharArray());  
        SecretKey secretKey = keyFactory.generateSecret(keySpec);  
        //初始化Cipher为加密器  
        PBEParameterSpec parameterSpec = new PBEParameterSpec(new byte[]{1,2,3,4,5,6,7,8},1000);          
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,parameterSpec);  
        //对数据进行加密  
        byte[] encryptedData = cipher.doFinal(data.getBytes());  
        //输出加密后的内容  
        System.out.println("加密后的数据为："+new String(encryptedData));
        return encryptedData;
    }  
      
    public static byte[] secretDecrypt(String key, byte[] data) throws Exception{  
        //使用PBEWithMD5AndDES算法获取Cipher实例  
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");  
        //初始化密钥  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");  
        KeySpec keySpec = new PBEKeySpec(key.toCharArray());  
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        //初始化Cipher为解密器  
        PBEParameterSpec parameterSpec = new PBEParameterSpec(new byte[]{1,2,3,4,5,6,7,8},1000);  
        cipher.init(Cipher.DECRYPT_MODE, secretKey,parameterSpec);  
        //获得解密后的数据  
        byte[] decryptedData = cipher.doFinal(data);  
        System.out.println("解密后的数据为："+new String(decryptedData));  
        return decryptedData;
    }
    
    public static String simpleEnc_encrypt(String key, String msg){
    	//PBEWithMD5AndDES  
        BasicTextEncryptor encryptor = new BasicTextEncryptor();  
        encryptor.setPassword(key);  
        String encrypted = encryptor.encrypt(msg);  
//        System.out.println(encrypted);
        return encrypted;
    }
    
    public static String simpleEnc_decrypt(String key, String msg){
    	//PBEWithMD5AndDES  
        BasicTextEncryptor encryptor = new BasicTextEncryptor();  
        encryptor.setPassword(key);  
        String encrypted = encryptor.decrypt(msg);  
//        System.out.println(encrypted);
        return encrypted;
    }
    
    public static void main(String[] args) throws Exception{
    	String key = "bcpay3.0";
        //定义需要进行加密的数据  
        String data ="alex zhuang";  
        System.out.println("加密前的数据为："+data);  
        //对数据进行加密  
        byte[] enSts = secretEncrypt(key, data);  
        //对数据进行解密  
        secretDecrypt(key, enSts);
  
        String enSt = simpleEnc_encrypt(key, "bclife");
        System.out.println("密文："+enSt);
        System.out.println("明文："+simpleEnc_decrypt(key, enSt));
        String enSt2 = "uhw4WmXtqlI7SH7lVjUMIg==";
        System.out.println("明文2："+simpleEnc_decrypt(key, enSt2));
    }  
      
  
} 
