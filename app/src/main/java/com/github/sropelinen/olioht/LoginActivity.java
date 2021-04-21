package com.github.sropelinen.olioht;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

//        AccountManager am = AccountManager.getManager(this);
//        am.login("testikayttaja", "salasana1", null);
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
                    () -> loginFragment.setErrorMessage("Invalid username or password"),
                    this
            );
        }
    }

    public void register(View view) {

        UserDataChecker checker = new UserDataChecker();
        String message;
        if (!((message = checker.validateFirstName(registerFragment.getFirstName())) == null)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (!((message = checker.validateLastName(registerFragment.getLastName())) == null)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (!((message = checker.validateBirthDate(registerFragment.getBirthDate())) == null)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (!((message = checker.validateHeight(registerFragment.getHeight())) == null)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (!((message = checker.validateWeight(registerFragment.getWeight())) == null)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (!((message = checker.validateUserName(registerFragment.getUserName())) == null)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (!((message = checker.validatePassword(registerFragment.getPassword())) == null)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> values = new HashMap<>();
            values.put("firstName", registerFragment.getFirstName());
            values.put("lastName", registerFragment.getLastName());
            values.put("birthDate", registerFragment.getBirthDate());
            values.put("height", Integer.parseInt(registerFragment.getHeight()));
            values.put("weight", Integer.parseInt(registerFragment.getWeight()));
            values.put("userName", registerFragment.getUserName());
            AccountManager.getManager(this).addUser(
                    registerFragment.getUserName(),
                    registerFragment.getPassword(),
                    values,
                    () -> Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show(),
                    this);
        }
    }
}