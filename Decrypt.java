/*
 * Example of use:
 *   > java Decrypt QBJVAQKAJQABit2li7s=
 *
 * should return:
 *   8
*/

public class Decrypt {

	// https://github.com/TheThingsNetwork/croft/blob/master/server.go
	public static final byte [] SEMTECH_DEFAULT_KEY = new byte [] {
			0x2B, 0x7E, 0x15, 0x16, 0x28, (byte) 0xAE, (byte) 0xD2, (byte) 0xA6, 
			(byte) 0xAB, (byte) 0xF7, 0x15, (byte) 0x88, 0x09, (byte) 0xCF, 0x4F, 0x3C
	};

	public static final byte [] IV = new byte [] {
			0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
			0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0
	};
	
	// example data from a ttn message
	/* public static final String TEXT_RAW   = "QAQBSFoAhAABzUi9WfIrdF1DhDu2eLVk4A==";
	public static final String TEXT       = "MDAwNjAwMUMwMDNC";
	public static final String TEXT_PLAIN = "0006001C003B"; */

   public static final String TEXT_RAW   = "QBJVAQKAJQABit2li7s=";
	public static final String TEXT       = "MDAwNjAwMUMwMDNC";
	public static final String TEXT_PLAIN = "0006001C003B"; 
	

    public static void main(String[] args) throws Exception {
      if (args.length != 1) {
         System.out.println("Use: ./decrypt DATA_RAW");
      } else {
    	   //String decryptedText = Crypto.decrypt(TEXT_RAW, SEMTECH_DEFAULT_KEY, IV);
    	   String decryptedText = Crypto.decrypt(args[0], SEMTECH_DEFAULT_KEY, IV);
    	   String plainText = Crypto.toPlainText(decryptedText);

         //System.out.println(args[0]);
    	   //System.out.println("pruebaaaaa");
    	   //System.out.println("decrypted text does not match: " + TEXT + " | " + decryptedText);
    	   //System.out.println("base64 decoded text does not match: " + TEXT_PLAIN + " | " + plainText);
    	   System.out.println(plainText);
      }
    }
}
