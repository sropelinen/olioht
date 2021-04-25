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

    /* Hashes password using SHA-512 and salt. Salt is stored as the first 16 bytes of byte array. */
    public static byte[] hashPassword(String password, byte[] salt) {
        byte[] hashed = null;
        try {
            if (salt == null) {
                // Creates new random salt
                SecureRandom random = new SecureRandom();
                salt = new byte[16];
                random.nextBytes(salt);
            }
            // Hashes the password
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            // Combines user specific salt and hashed password to one byte array
            hashed = Arrays.copyOf(salt, 16 + bytes.length);
            System.arraycopy(bytes, 0, hashed, 16, bytes.length);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashed;
    }

    /* Compares hash stored in the database to new hash. */
    public static boolean checkPassword(byte[] hash, String password) {
        byte[] salt = Arrays.copyOf(hash, 16);
        return Arrays.equals(hash, hashPassword(password, salt));
    }

    /* Encrypts user data (json string) to byte array. Uses AES-GCM without padding. */
    public static byte[] encryptData(String password, String data) {
        byte[] encrypted = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            // Creates new random salt
            final byte[] salt = new byte[12];
            new SecureRandom().nextBytes(salt);
            // Encrypts data
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(Arrays.copyOf(password.getBytes(StandardCharsets.UTF_8), 16), "AES"),
                    new GCMParameterSpec(128, salt));
            byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            // Combines salt and encrypted data to one byte array
            encrypted = Arrays.copyOf(salt, 12 + bytes.length);
            System.arraycopy(bytes, 0, encrypted, 12, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    /* Decrypts user data back to json string. */
    public static String decryptData(String password, byte[] data) {
        String decrypted = null;
        try {
            byte[] salt = Arrays.copyOf(data, 12);
            byte[] encrypted = Arrays.copyOfRange(data, 12, data.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(Arrays.copyOf(password.getBytes(StandardCharsets.UTF_8), 16), "AES"),
                    new GCMParameterSpec(128, salt));
            decrypted = new String(cipher.doFinal(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }
}
