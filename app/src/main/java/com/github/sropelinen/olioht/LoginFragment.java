package com.github.sropelinen.olioht;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    private EditText editUsername, editPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editUsername = (EditText) view.findViewById(R.id.editUserNameLogin);
        editPassword = (EditText) view.findViewById(R.id.editPasswordLogin);
    }

    public String getName() {
        return editUsername.getText().toString();
    }

    public String getPassword() {
        return editPassword.getText().toString();
    }
}
