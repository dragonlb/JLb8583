package ts.lb;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class TsHex {

	public static void main(String[] args) {
		BASE64Decoder decoder = new BASE64Decoder();
		BASE64Encoder encoder = new BASE64Encoder();
		try {
			String enSt = encoder.encode("中华人民共和国".getBytes("UTF-8"));
			System.out.println(enSt);
			System.out.println(new String(decoder.decodeBuffer(enSt)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
