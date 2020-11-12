package net.idik.lib.cipher.so.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public final class AESEncryptor {

    private final static String HEX = "0123456789ABCDEF";
    private static final String AES_MODE = "AES/CBC/PKCS5Padding";
    private static byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static void setIv(String iv) {
        try {
            AESEncryptor.iv = iv.getBytes(StandardCharsets.UTF_8);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static String enc(String key, String message)
            throws Exception {
        byte[] rawKey = getRawKey(key);
        byte[] result = enc(rawKey, iv, message.getBytes());
        return toHex(result);
    }

    private static byte[] getRawKey(String key) {
        SecretKey keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        return keySpec.getEncoded();
    }

    private static byte[] enc(byte[] raw, byte[] iv, byte[] clear) throws Exception {
        SecretKey keySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        return cipher.doFinal(clear);
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (byte b : buf) {
            appendHex(result, b);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    private AESEncryptor() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
