package com.lb.tool;

public class StringTool {
    /**
    * 删除子串
    * @param _original
    * @param _block
    * @return
    */
    public static String escapeString(String _original, String _block){
        while(_original.indexOf(_block)>=0){
            if(_original.indexOf(_block)==0){
                _original = _original.substring(_block.length()+1);
                continue;
            }
        _original = _original.substring(0,_original.indexOf(_block))+_original.substring(_original.indexOf(_block)+_block.length());
        }
        return _original;
    }
    /**
    * 字符串转字节数组
    * 
    * @param s
    * @return
    */
    public static byte[] str2ByteArray(String s) {
        int byteArrayLength = s.length()/2;
        byte[] b = new byte[byteArrayLength];
        for (int i = 0; i < byteArrayLength; i++) {
            byte b0 = (byte) Integer.valueOf(s.substring(i*2, i*2+2), 16).intValue();
            b[i] = b0;
        }
        return b;
    }
}