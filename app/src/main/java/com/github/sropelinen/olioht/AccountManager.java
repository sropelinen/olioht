package com.github.sropelinen.olioht;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class AccountManager {

    private static AccountManager INSTANCE;
    private final UserDao userDao;

    private String name, password;
    private final Context context;

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

    public void addUser(String name, String password) {
        execute(() -> {
            userDao.insert(new User(name, Crypto.hashPassword(password, null)));
        });
    }

    public void login(String name, String password, Runnable fallback) {
        Handler handler = new Handler();
        execute(() -> {
            User user = userDao.getUser(name);
            if (user != null && Crypto.checkPassword(user.getPassword(), password)) {
                this.name = name;
                this.password = password;
                Profile.login(Crypto.decryptData(password, user.getData()));
                context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
                ((LoginActivity) context).finish();
            } else {
                handler.post(fallback);
            }
        });
    }

    public void logout() {
        if (name == null) return;
        execute(() -> {
            User user = userDao.getUser(name);
            user.setData(Crypto.encryptData(Profile.getInstance().getData(), password));
            userDao.update(user);
        });
    }
}
