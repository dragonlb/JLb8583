package com;

import com.iso8583.CupsUtil;
import com.iso8583.DesUtil;
import com.lb.tool.HexTool;

public class MacTs {
	
	public void test1(){
		byte[] macKey = HexTool.hexString2Bytes("3131313131313131");
		String msg = "128014A0048000C000110000000000010012303130303538343335353434302020202020202020200001700120003832323435383838303030303030303030303030303030000802000001";
		msg = "129018B800800AC000110000000000010077010CBBE3BDF0CDA8CFFBB7D1B5A50208D2DAC3C0BBE3BDF0030E574549504F53B2E2CAD4BBEEB6AF0414574549504F53BBEEB6AFB6CCD0C5B6FECEACC2EB050A323032302D31322D31360601003139303120202020202000017009462510160031353230393436323236363830303832323435383838303030303030303030303030303030000802000001";
		byte[] hBytes = HexTool.hexString2Bytes(msg);
		byte[] tb1 = new byte[8];
		byte[] tb2 = new byte[8];
		for(int i=0;i<hBytes.length;i+=8){
			if(i==0){
				System.arraycopy(hBytes, i, tb1, 0, 8);
				i = i+8;
			}
			if(hBytes.length-i<8){
				System.arraycopy(hBytes, i, tb2, 0, hBytes.length-i);
				for( int k=hBytes.length-i;k<8;k++){
					tb2[k] = 0x00;
				}
			}
			else{
				System.arraycopy(hBytes, i, tb2, 0, 8);
			}
			String tempSt = HexTool.bytes2HexString(tb1)+"\t^\t"+HexTool.bytes2HexString(tb2);
			tb1 = CupsUtil.doMaxDissident(tb1, tb2);
			System.out.println( tempSt +" = "+ HexTool.bytes2HexString(tb1) );
		}
		String enSt = HexTool.bytes2HexString(tb1);
		System.out.println("EN hex code = "+enSt);
		byte[] enBytes = enSt.getBytes();
		byte[] pres = new byte[8];
		byte[] lasts = new byte[8];
		System.arraycopy(enBytes, 0, pres, 0, 8);
		System.arraycopy(enBytes, 8, lasts, 0, 8);
		System.out.println("pres  = "+HexTool.bytes2HexString(pres));
		System.out.println("lasts = "+HexTool.bytes2HexString(lasts));
		byte[] enc = DesUtil.encrypt(macKey, pres);
		System.out.println("ENS  encrypt 1 = "+HexTool.bytes2HexString(enc));
		enc = CupsUtil.doMaxDissident(enc, lasts);
		System.out.println("ENS ^ lasts = "+HexTool.bytes2HexString(enc));
		enc = DesUtil.encrypt(macKey, enc);
		enSt = HexTool.bytes2HexString(enc);
		System.out.println("ENS mac all = "+enSt);
		enBytes = enSt.getBytes();
		pres = new byte[8];
		System.arraycopy(enBytes, 0, pres, 0, 8);
		enSt = HexTool.bytes2HexString(pres);
		System.out.println("Result MAC = "+enSt);
	}
	
	public static void main(String[] args) {
		byte[] macKey = HexTool.hexString2Bytes("3131313131313131");
		String msg = "128014A0048000C000110000000000010012303130303538343335353434302020202020202020200001700120003832323435383838303030303030303030303030303030000802000001";
//		CupsUtil.generateMAC(macKey, HexTool.hexString2Bytes(msg), true);
		CupsUtil.generateMAC("11111111".getBytes(), HexTool.hexString2Bytes(msg), true);
	}
}
