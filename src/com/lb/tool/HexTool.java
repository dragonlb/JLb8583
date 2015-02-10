package com.lb.tool;

public class HexTool {

    /** 
      * 将两个ASCII字符合成一个字节； 
      * 如："EF"--> 0xEF 
      * @param src0 byte 
      * @param src1 byte 
      * @return byte 
      */ 
    public static byte uniteBytes(byte src0, byte src1) { 
       byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue(); 
       _b0 = (byte)(_b0 << 4); 
       byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue(); 
       byte ret = (byte)(_b0 ^ _b1); 
       return ret; 
    }

    /** 
      * 将byte数组 转换成 十六进制字符串表达式
      * @param b byte[] 
      * @return String 
      */ 
    public static String bytes2HexString(byte[] b) {
       StringBuffer ret = new StringBuffer(); 
       for (int i = 0; i < b.length; i++) { 
         String hex = Integer.toHexString(b[i] & 0xFF); 
         if (hex.length() == 1) { 
             ret.append("0"); 
         }
         ret.append(hex.toUpperCase()); 
       }
       return ret.toString(); 
    }

    /**
     * 由字符串组十六进制数组
     * 如 90AB -----> {0x90, 0xAB}
     * @param hexStr
     * @return
     */
    public static byte[] hexString2Bytes(String hexStr){
        byte[] temp = hexStr.getBytes();
        byte[] ret = new byte[temp.length/2];
        for(int i=0;i<ret.length;i++){
            ret[i] = HexTool.uniteBytes(temp[i*2], temp[i*2+1]);
        }
        return ret;
    }

}
