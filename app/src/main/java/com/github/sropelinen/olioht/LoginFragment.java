package com.github.sropelinen.olioht;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private EditText editUsername, editPassword;
    private TextView errorMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editUsername = view.findViewById(R.id.editUserNameLogin);
        editPassword = view.findViewById(R.id.editPasswordLogin);
        errorMessage = view.findViewById(R.id.errorMessage);

        editPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ((LoginActivity) view.getContext()).login(view);
                // closes keyboard
                InputMethodManager methodManager = (InputMethodManager) Objects.
                        requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(Objects.requireNonNull(getActivity()).
                                getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return false;
        });

        for (EditText et : new EditText[] {editUsername, editPassword}) {
            et.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    errorMessage.setText("");
                }
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            });
        }
    }

    public String getName() {
        return editUsername.getText().toString();
    }

    public String getPassword() {
        return editPassword.getText().toString();
    }

    public void setErrorMessage(String error) {
        errorMessage.setText(error);
    }
}
