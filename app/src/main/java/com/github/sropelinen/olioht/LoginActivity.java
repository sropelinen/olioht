package com.github.sropelinen.olioht;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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

        AccountManager am = AccountManager.getManager(this);
        am.login("testikayttaja", "salasana1");
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
        AccountManager am = AccountManager.getManager(this);
        am.login(loginFragment.getName(), loginFragment.getPassword());
    }

    public void register(View view) {

    }

}