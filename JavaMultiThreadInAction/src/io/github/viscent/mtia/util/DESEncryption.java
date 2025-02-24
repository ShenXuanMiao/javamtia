package io.github.viscent.mtia.util;

import org.apache.commons.net.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;

public class DESEncryption {
    /**
     * @param args
     * @throws CryptoException
     */
    public static void main(String[] args) throws CryptoException {
        String content = "secret content中文";
        // 密码长度必须是8的倍数
        String password = "12345678";
        System.out.println("密钥：" + password);
        System.out.println("加密前：" + content);
        String result = encryptAsString(content, password);
        System.out.println("加密后：" + result);
        String decryResult = decryptString(result, password);
        System.out.println("解密后：" + decryResult);
    }

    /**
     * 加密
     *
     * @param content 待加密内容
     * @param key     加密的密钥
     * @return
     * @throws CryptoException
     */
    public static byte[] encrypt(String content, String key)
            throws CryptoException {
        byte[] result = null;
        try {
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            result = cipher.doFinal(content.getBytes());
        } catch (Exception e) {
            throw new CryptoException(e);
        }
        return result;
    }

    public static String encryptAsString(String content, String key)
            throws CryptoException {
        byte[] encryptedBytes = encrypt(content, key);
        byte[] bytesEncoded = new Base64().encode(encryptedBytes);
        try {
            return new String(bytesEncoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @param key     解密的密钥
     * @return
     */
    public static String decrypt(byte[] content, String key)
            throws CryptoException {
        try {
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] result = cipher.doFinal(content);
            return new String(result, "UTF-8");
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    public static String decryptString(String content, String key)
            throws CryptoException {
        byte[] bytesDecoded;
        try {
            bytesDecoded = new Base64().decode(content.getBytes("UTF-8"));
            return decrypt(bytesDecoded, key);
        } catch (UnsupportedEncodingException e) {
            throw new CryptoException(e);
        }
    }

    public static class CryptoException extends Exception {
        private static final long serialVersionUID = 1L;

        public CryptoException(Exception cause) {
            super(cause);
        }
    }
}