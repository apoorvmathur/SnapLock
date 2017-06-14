package tk.apoorvmathur.multithreadtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Apoorv on 5/4/2015.
 */
public class CryptoProvider {

    public static SecretKey generateKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        final PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), password.getBytes(), 30, 128);
        SecretKey keyTemp = secretKeyFactory.generateSecret(keySpec);
        SecretKey key = new SecretKeySpec(keyTemp.getEncoded(), "AES");
        return key;
    }

    public static void encrypt(InputStream inputStream, OutputStream outputStream, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InvalidKeySpecException {
        SecretKey key = generateKey(password);
        Cipher encryptCipher = Cipher.getInstance("AES/ECB/NoPadding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] inBuf = new byte[1024];
        byte[] outBuf;
        int len;
        while ((len = inputStream.read(inBuf)) > 0) {
            outBuf = encryptCipher.update(inBuf);
            outputStream.write(outBuf, 0, len);
        }
    }

    public static void decrypt(InputStream inputStream, OutputStream outputStream, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InvalidKeySpecException {
        SecretKey key = generateKey(password);
        Cipher encryptCipher = Cipher.getInstance("AES/ECB/NoPadding");
        encryptCipher.init(Cipher.DECRYPT_MODE, key);
        byte[] inBuf = new byte[1024];
        byte[] outBuf;
        int len;
        while ((len = inputStream.read(inBuf)) > 0) {
            outBuf = encryptCipher.update(inBuf);
            outputStream.write(outBuf, 0, len);
        }
    }
}
