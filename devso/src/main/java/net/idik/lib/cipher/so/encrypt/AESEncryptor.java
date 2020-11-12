package net.idik.lib.cipher.so.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public final class AESEncryptor {
    private static final String AES_MODE = "AES/CBC/PKCS5Padding";

    public static String dec(String key, String iv, String cipherMessage)
            throws Exception {
        byte[] enc = toByte(cipherMessage);
        byte[] result = dec(key, iv.getBytes(StandardCharsets.UTF_8), enc);
        return new String(result);
    }

    private static byte[] dec(String key, byte[] iv, byte[] encrypted)
            throws Exception {
        SecretKey keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

        return cipher.doFinal(encrypted);
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    private AESEncryptor() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}