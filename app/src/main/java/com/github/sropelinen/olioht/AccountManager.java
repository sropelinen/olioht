package com.github.sropelinen.olioht;

import android.content.Context;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AccountManager {

    private static AccountManager INSTANCE;
    private final UserDao userDao;

    public static AccountManager getManager(Context context) {
        if (INSTANCE == null) INSTANCE = new AccountManager(context);
        return INSTANCE;
    }

    private AccountManager(Context context) {
        userDao = AccountDatabase.getDatabase(context).userDao();
    }

    public void addUser(String name, String password) {
        String hashed = hashPassword(password, null);
        execute(() -> {
            User newUser = new User(name, hashed);
            userDao.insert(newUser);
        });
    }

    public String validateName(String name) {
        // ToDo Tekstit pitää olla strings.xml tiedostossa
        if (name.matches("[A-Za-z0-9]+")) {
            return null;
        } else {
            return "Nimi voi sisältää vain kirjaimia a-z ja numeroita";
        }
    }

    public String validatePassword(String password) {
        // ToDo Tekstit pitää olla strings.xml tiedostossa
        if (password.length() < 12) {
            return "Salasanan pitää olla vähintään 12 merkkiä pitkä";
        } else if (!password.matches(".*[a-z].*")) {
            return "Salasanan pitää sisältää vähintään yksi pieni kirjain";
        } else if (!password.matches(".*[A-Z].*")) {
            return "Salasanan pitää sisältää vähintään yksi iso kirjain";
        } else if (!password.matches(".*[0-9].*")) {
            return "Salasanan pitää sisältää vähintään yksi numero";
        } else if (!password.matches(".*[^A-Za-z0-9].*")) {
            return "Salasanan pitää sisältää vähintään yksi erikoismerkki";
        }
        return null;
    }

    private void execute(Runnable r) {
        new Thread(r).start();
    }

    private String hashPassword(String password, byte[] salt) {

        String hashed = "";

        if (salt == null) {
            SecureRandom random = new SecureRandom();
            salt = new byte[16];
            random.nextBytes(salt);
        }
        hashed += Base64.encodeToString(salt, Base64.NO_WRAP);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            hashed += Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashed;
    }

    private boolean checkPassword(String hash, String password) {
        byte[] salt = Base64.decode(hash.split("==")[0], Base64.NO_WRAP);
        String newHash = hashPassword(password, salt);
        return hash.equals(newHash);
    }
}
