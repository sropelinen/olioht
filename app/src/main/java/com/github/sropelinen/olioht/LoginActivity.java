package com.github.sropelinen.olioht;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

// TODO Loput toiminnallisuudet

public class LoginActivity extends AppCompatActivity {

    private LoginFragment loginFragment = new LoginFragment();
    private RegisterFragment registerFragment = new RegisterFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameFragmentContainer,
                    loginFragment).commit();
        }

        //AccountManager am = AccountManager.getManager(this);
        //am.login("testikayttaja", "salasana1", null);
    }

    public void returnLogin(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameFragmentContainer,
                loginFragment).commit();
    }

    public void returnRegister(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameFragmentContainer,
                registerFragment).commit();
    }

    public void login(View view) {
        String username = loginFragment.getName();
        String password = loginFragment.getPassword();
        if (username.length() == 0 || password.length() == 0) {
            loginFragment.setErrorMessage("Missing username or password");
        } else {
            AccountManager.getManager(this).login(
                    loginFragment.getName(),
                    loginFragment.getPassword(),
                    () -> loginFragment.setErrorMessage("Invalid username or password")
            );
        }
    }

    public void register(View view) {
        // ToDo tarkista et kaikki tiedot oikein ja tää loppuun
        HashMap<String, Object> values = new HashMap<>();
        values.put("height", 153);
        values.put("firstName", "Henk");
        AccountManager.getManager(this).addUser("knimi", "ssana", values);
    }

}