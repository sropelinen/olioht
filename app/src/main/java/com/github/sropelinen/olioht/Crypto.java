package com.github.sropelinen.olioht;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    public static byte[] hashPassword(String password, byte[] salt) {
        byte[] hashed = null;
        try {
            if (salt == null) {
                SecureRandom random = new SecureRandom();
                salt = new byte[16];
                random.nextBytes(salt);
            }
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            hashed = Arrays.copyOf(salt, 16 + bytes.length);
            System.arraycopy(bytes, 0, hashed, 16, bytes.length);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashed;
    }

    public static boolean checkPassword(byte[] hash, String password) {
        byte[] salt = Arrays.copyOf(hash, 16);
        return Arrays.equals(hash, hashPassword(password, salt));
    }

    public static byte[] encryptData(String password, String data) {
        byte[] encrypted = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            final byte[] nonce = new byte[12];
            new SecureRandom().nextBytes(nonce);
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(Arrays.copyOf(password.getBytes(StandardCharsets.UTF_8), 16), "AES"),
                    new GCMParameterSpec(128, nonce));
            byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            encrypted = Arrays.copyOf(nonce, 12 + bytes.length);
            System.arraycopy(bytes, 0, encrypted, 12, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public static String decryptData(String password, byte[] data) {
        String decrypted = null;
        try {
            byte[] nonce = Arrays.copyOf(data, 12);
            byte[] encrypted = Arrays.copyOfRange(data, 12, data.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(Arrays.copyOf(password.getBytes(StandardCharsets.UTF_8), 16), "AES"),
                    new GCMParameterSpec(128, nonce));
            decrypted = new String(cipher.doFinal(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }
}
