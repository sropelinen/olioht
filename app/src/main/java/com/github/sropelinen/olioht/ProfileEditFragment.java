package com.github.sropelinen.olioht;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ProfileEditFragment extends Fragment {
    private View view;
    private EditText etFirstname;
    private EditText etLastname;
    private EditText etUsername;
    private String firstnameNew;
    private String lastnameNew;
    private String usernameNew;
    private Button btnSave;
    private Profile profile;


    public ProfileEditFragment(Profile profile) {
        this.profile = profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        etFirstname = view.findViewById(R.id.et_edit_firstname);
        etLastname = view.findViewById(R.id.et_edit_lastname);
        etUsername = view.findViewById(R.id.et_edit_username);

        // TODO remove comments if profile is active
//        etFirstname.setText(profile.getValue("firstName").toString());
//        etLastname.setText(profile.getValue("lastName").toString());
        btnSave = view.findViewById(R.id.btn_save_profile);

        etFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstnameNew = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        etLastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastnameNew = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameNew = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO save to profile
            }
        });
        return view;
    }
}