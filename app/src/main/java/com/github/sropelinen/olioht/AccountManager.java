package com.github.sropelinen.olioht;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.HashMap;

public class AccountManager {

    private static AccountManager INSTANCE;
    private final UserDao userDao;
    private final Context context;
    private User currentUser = null;
    private String password;

    public static AccountManager getManager(Context context) {
        if (INSTANCE == null) INSTANCE = new AccountManager(context);
        return INSTANCE;
    }

    private AccountManager(Context context) {
        userDao = AccountDatabase.getDatabase(context).userDao();
        this.context = context;
    }

    private void execute(Runnable r) {
        new Thread(r).start();
    }

    public void addUser(String name, String password, HashMap<String, Object> values) {
        // ToDo fallback
        execute(() -> {
            if (userDao.getUser(name) != null) return;
            User newUser = new User(name, Crypto.hashPassword(password, null));
            userDao.insert(newUser);
            Profile profile = Profile.init("{}");
            profile.setValues(values);
            save(newUser, password);
            currentUser = newUser;
            this.password = password;
            context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
            ((LoginActivity) context).finish();
        });
    }

    public void login(String name, String password, Runnable fallback) {
        Handler handler = new Handler();
        execute(() -> {
            User user = userDao.getUser(name);
            if (user != null && Crypto.checkPassword(user.getPassword(), password)) {
                currentUser = user;
                this.password = password;
                Profile.init(Crypto.decryptData(password, user.getData()));
                context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
                ((LoginActivity) context).finish();
            } else {
                handler.post(fallback);
            }
        });
    }

    private void save(User user, String password) {
        execute(() -> {
            user.setData(Crypto.encryptData(password, Profile.getInstance().getData()));
            userDao.update(user);
        });
    }

    public void logout() {
        // ToDo tää o pakko tapahtuu aina ku sovellus menee kii, ainoo mikä tallentaa tietoo
        if (currentUser != null) {
            save(currentUser, password);
        }
    }
}
