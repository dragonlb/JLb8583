package com.iso8583;

import com.lb.tool.HexTool;
import com.lb.tool.StringTool;

public class CupsUtil {

    /**
     * 组装加密加的 PINBLOCK
     * @param pin_data
     * @param accountNo
     * @return
     */
    public static byte[] combinePinWithAccountNo(String pin_data, String accountNo){
        byte[] pin = combinePin(pin_data);
        byte[] pan = combinePinBlock(accountNo);
        return doMaxDissident(pin, pan);
    }

    /**
     * 从主账号中截取PAN
     * @param pan 主账号
     * @return PAN　byte[8]
     * 如：6223230000000000121 ----> {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x12}
     * @rule 主体:取主账号的右12 位（不包括最右边的校验位），主账号不足12位左补0
     */
    public static byte[] combinePinBlock(String pan){
        if(pan==null){
            return null;
        }
        pan = StringTool.escapeString(pan, " ");
        String temp = "";
        if(pan.length()>=13){
            temp = pan.substring(pan.length()-13, pan.length()-1);
        }
        else{
            temp = pan.substring(0, pan.length()-1);
        }
        while(temp.length()<16){
            temp = "0"+temp;
        }
        return HexTool.hexString2Bytes(temp);
    }

    /**
     * 组 pin
     * @param pin byte[8]　第一个字节是pin长度　后面是密码，不足7位右补F
     * 如：123456 ----> {0x06,0x12,0x34,0x56,0xFF,0xFF,0xFF,0xFF}
     * @return
     */
    private static byte[] combinePin(String pin){
        if(pin==null){
            return null;
        }
        if(pin.length()<4 || pin.length()>12){
            System.out.printf("pin has wrong len %d", pin.length());
            return null;
        }
        String temp = pin;
        int pinLen = pin.length();
        byte pinLenByte = Byte.decode(String.valueOf(pinLen)).byteValue();
        while(temp.length()<14){
            temp += "F";
        }
        byte[] tempPin = temp.getBytes();
        byte[] ret = new byte[tempPin.length/2+1];
        for(int i=0;i<ret.length-1;i++){
            ret[i+1] = HexTool.uniteBytes(tempPin[i*2], tempPin[i*2+1]);
        }
        ret[0] = pinLenByte;
        return ret;
    }

    public static byte[] doMaxDissident(byte[] _b1, byte[] _b2){
        int maxLength = _b1.length>_b2.length?_b1.length:_b2.length;
        byte bufByte = (byte)0x00;
        byte[] retByte = new byte[maxLength];
        for(int i=0;i<maxLength;i++){
            if(i>=_b1.length){
                retByte[i] = (byte)(bufByte ^ _b2[i]);
            }
            if(i>=_b2.length){
                retByte[i] = (byte)(_b1[i] ^ bufByte);
            }
            else{
                retByte[i] = (byte)(_b1[i] ^ _b2[i]);
            }
        }
        return retByte;
    }
    
    public static String generateMAC(byte[] macKey, byte[] msgArray){
    	return generateMAC(macKey, msgArray, false);
    }
    
    public static String generateMAC(byte[] macKey, byte[] msgArray, boolean isDebug){
    	String macSt = "";
    	StringBuffer logBuf = new StringBuffer();
    	byte[] tb1 = new byte[8];
		byte[] tb2 = new byte[8];
		for(int i=0;i<msgArray.length;i+=8){
			if(i==0){
				System.arraycopy(msgArray, i, tb1, 0, 8);
				i = i+8;
			}
			if(msgArray.length-i<8){
				System.arraycopy(msgArray, i, tb2, 0, msgArray.length-i);
				for( int k=msgArray.length-i;k<8;k++){
					tb2[k] = 0x00;
				}
			}
			else{
				System.arraycopy(msgArray, i, tb2, 0, 8);
			}
			String tempSt = HexTool.bytes2HexString(tb1)+"\t^\t"+HexTool.bytes2HexString(tb2);
			tb1 = CupsUtil.doMaxDissident(tb1, tb2);
			logBuf.append(tempSt +" = "+ HexTool.bytes2HexString(tb1)+"\n");
		}
		String enSt = HexTool.bytes2HexString(tb1);
		logBuf.append("EN hex code = "+enSt+"\n");
		byte[] enBytes = enSt.getBytes();
		byte[] pres = new byte[8];
		byte[] lasts = new byte[8];
		System.arraycopy(enBytes, 0, pres, 0, 8);
		System.arraycopy(enBytes, 8, lasts, 0, 8);
		logBuf.append("pres   = "+HexTool.bytes2HexString(pres)+"\n");
		logBuf.append("lasts  = "+HexTool.bytes2HexString(lasts)+"\n");
		byte[] enc = DesUtil.encrypt(macKey, pres);
		logBuf.append("ENS  encrypt 1 = "+HexTool.bytes2HexString(enc)+"\n");
		enc = CupsUtil.doMaxDissident(enc, lasts);
		logBuf.append("ENS ^ lasts = "+HexTool.bytes2HexString(enc)+"\n");
		enc = DesUtil.encrypt(macKey, enc);
		enSt = HexTool.bytes2HexString(enc);
		logBuf.append("ENS mac all = "+enSt+"\n");
		enBytes = enSt.getBytes();
		pres = new byte[8];
		System.arraycopy(enBytes, 0, pres, 0, 8);
		enSt = HexTool.bytes2HexString(pres);
		logBuf.append("Result MAC = "+enSt);
		if(isDebug)	System.out.println(logBuf.toString());
    	return macSt;
    }
}
