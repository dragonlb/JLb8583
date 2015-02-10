package com;

import com.iso8583.CupsUtil;
import com.iso8583.DesUtil;
import com.lb.tool.HexTool;

public class PinTs {
	public static void main(String[] args) throws Exception{
        String indexKey = "1234567890abcdef1234567890abcdef";        //主密钥
        String tranKeyM = "E901DB67382ACDE0E5379A3FC8F83377";        //传输密钥
        String cardNo = "6223230000000000121";                        //主账号
        String passWd = "111111";                                    //密码

        //对传输密钥解密
        System.out.println(".......加密..........");
        byte[] tranKey = DesUtil.tripleDecrypt(HexTool.hexString2Bytes(indexKey), HexTool.hexString2Bytes(tranKeyM));
        System.out.println("解密出传输密钥："+HexTool.bytes2HexString(tranKey));
        byte[] pin = CupsUtil.combinePinWithAccountNo(passWd, cardNo);
        System.out.println("加密前的PIN："+HexTool.bytes2HexString(pin));
        byte[] pinM = DesUtil.tripleEncrypt(tranKey, pin);
        System.out.println("加密后的PIN："+HexTool.bytes2HexString(pinM));

        System.out.println(".......解密..........");
        tranKey = DesUtil.tripleDecrypt(HexTool.hexString2Bytes(indexKey), HexTool.hexString2Bytes(tranKeyM));
        System.out.println("解密出传输密钥："+HexTool.bytes2HexString(tranKey));
        System.out.println("解密前的PIN："+HexTool.bytes2HexString(pinM));
        pin = DesUtil.tripleDecrypt(tranKey, pinM);
        System.out.println("解密后的PIN-WITH-BLOCK："+HexTool.bytes2HexString(pin));
        byte[] pinblock = CupsUtil.combinePinBlock(cardNo);
        System.out.println("解密后的PIN-BLOCK："+HexTool.bytes2HexString(pinblock));
        pin = CupsUtil.doMaxDissident(pin, pinblock);
        System.out.println("解密后的PIN："+HexTool.bytes2HexString(pin));

        int len = (int)pin[0];
        String aimPin = HexTool.bytes2HexString(pin);
        aimPin = aimPin.substring(2, len+2);
        System.out.println("用户密码是："+aimPin);
    }
}
