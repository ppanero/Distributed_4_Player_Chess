package Utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils{
    private static String cryptoAlg = "ARCFOUR";
    private static String cryptoMode = "";
    private static SecretKey key;
    private Cipher cipher;

    /**
     * true is encryption mode, false is decryption mode
     * @param enc
     */
    public CryptoUtils(boolean enc){
        try {
            String algMode = cryptoAlg;
            if(!cryptoMode.equals(""))
                cryptoAlg.concat(cryptoMode);

            cipher = Cipher.getInstance(algMode);
            if(enc){
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            else{
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public void setCryptoAlg(String alg){
        cryptoAlg = alg;
    }

    public void setCryptoMode(String mode){
        cryptoMode = mode;
    }

    public Cipher getCipher(){
        return cipher;
    }

    public static void initCipher(String passwd){
        key = new SecretKeySpec(passwd.getBytes(),cryptoAlg);
    }

    public byte[] encrypt(byte[] obj){
        try {
            return cipher.doFinal(obj);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decrypt(byte[] obj){
        try {
            return cipher.doFinal(obj);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Main to test encryption-decryption
    public static void main(String[] args) throws UnsupportedEncodingException {
        String test = "This is a string to test the encryption";
        String password = "FourPlayerChess!";
        System.out.println(password.getBytes().length);
        initCipher(password);
        System.out.println("Encrypting: " + test + " with password: " + password);
        System.out.println("Encyption:");
        byte[] enc = new CryptoUtils(true).encrypt(test.getBytes());
        System.out.println(enc);
        System.out.println("Dencyption:");
        try {
            System.out.println(new String(new CryptoUtils(false).decrypt(enc), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
