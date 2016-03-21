import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Implements TheThingsNetwork message decryption.
 * 
 * The implementation is based on the following sources:
 * - LoRa Specification, 2015
 * - https://github.com/TheThingsNetwork/croft/blob/master/server.go
 * - https://github.com/willem4ever/node-red-ttn/blob/master/90-TTN.js
 * - https://gist.github.com/bricef/2436364
 * 
 * @author @ZimMatthias
 *
 */
public class Crypto {

	/**
	 * Decrypts TTN data_raw payload to data according to the TTN REST API.
	 * @param rawText encrypted message payload from ttn mqtt message
	 * @param K the TTN application key 
	 * @param IV
	 * @return decrypted payload
	 * @throws Exception
	 */
	public static String decrypt(String rawText, byte [] K, byte [] IV) throws Exception {
		byte [] pld = Base64.getDecoder().decode(rawText);		
		byte [] devAddr = getDevAddr(pld);
		byte [] frameCounter = getFrameCounter(pld);
		byte [] result = initializeResult(pld);
		byte [] Ai = new byte[16];
		byte [] Si = null;

		for(int i = 0; i < result.length; i += 16) {
			int blockSeqCnt = (i >> 4) + 1;

			computeAi(Ai, devAddr, frameCounter, blockSeqCnt);
			Si = encryptAES(Ai, K, IV);

			for(int j=0; j < 16 && i+j < result.length; j++) {
				result[i+j] ^= Si[j];
			}
		}

		return Base64.getEncoder().encodeToString(result);
	}
	
	/**
	 * Converts TTN payload data to data_plain according to the TTN REST API.
	 * Decode a text using base 64 decoding. 
	 * @param decryptedText
	 * @return
	 */
	public static String toPlainText(String decryptedText) {
		byte [] data = Base64.getDecoder().decode(decryptedText);
		StringBuffer plain = new StringBuffer();
		
		for(int i = 0; i < data.length; i++) {
			plain.append((char)data[i]);
		}
		
		return plain.toString();
	}


	public static byte [] getDevAddr(byte [] payload) {
		byte [] devAddr = new byte[4];
		System.arraycopy(payload, 1, devAddr, 0, 4);
		return devAddr;
	}

	public static byte [] getFrameCounter(byte [] payload) {
		byte [] frameCounter = new byte[2];
		System.arraycopy(payload, 6, frameCounter, 0, 2);
		return frameCounter;
	}

	public static byte [] initializeResult(byte [] payload) {
		byte [] result = new byte[payload.length - 13];

		for(int i = 0; i < result.length; i++) {
			result[i] = payload[i+9];
		}

		return result;
	}

	public static void computeAi(byte [] a, byte [] devAddr, byte [] frameCounter, int blockSeqCnt) {
		a[0]  = 0x01;
		a[1]  = 0x00;
		a[2]  = 0x00;
		a[3]  = 0x00;
		a[4]  = 0x00;
		a[5]  = 0;               // 0 for uplink frames 1 for downlink frames;
		a[6]  = devAddr[0];      // LSB devAddr 4 bytes
		a[7]  = devAddr[1];      // ..
		a[8]  = devAddr[2];      // ..
		a[9]  = devAddr[3];      // MSB
		a[10] = frameCounter[0]; // LSB framecounter
		a[11] = frameCounter[1]; // MSB framecounter
		a[12] = 0x00;            // Frame counter upper Bytes
		a[13] = 0x00;
		a[14] = 0x00;
		a[15] = (byte)blockSeqCnt;	// block sequence counter 1,2,3...
	}

	/**
	 * AES encrpytion.
	 * @param data
	 * @param key
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptAES(byte [] data, byte [] key, byte [] iv) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "SunJCE");
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		return cipher.doFinal(data);
	}

}
