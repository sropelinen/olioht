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
        HashMap<String, Object> values = new HashMap<>();
        values.put("firstName", registerFragment.getFirstName());
        values.put("lastName", registerFragment.getLastName());
        //birthdate
        values.put("height", registerFragment.getHeight());
        values.put("weight", registerFragment.getWeight());
        values.put("userName", registerFragment.getUserName());
        // ToDo kaikki tarkistukset
        AccountManager.getManager(this).addUser(registerFragment.getUserName(), registerFragment.getPassword(), values);
    }

}