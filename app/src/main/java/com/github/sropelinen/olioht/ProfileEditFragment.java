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
import android.widget.TextView;

import java.util.HashMap;

// ToDo birth date

public class ProfileEditFragment extends Fragment {
    private View view;

    private TextView tvName, tvDob, tvUsername;
    private final EditText[] textFields = new EditText[5];
    private final Object[] newValues = new Object[5];
    private final Object[] oldValues = new Object[5];
    private final String[] keys = new String[] {
            "firstName", "lastName", "height", "weight", "home"
    };
    private final int[] ids = new int[] {
            R.id.et_edit_firstname, R.id.et_edit_lastname, R.id.et_edit_height,
            R.id.et_edit_weight, R.id.et_edit_home
    };

    private Button btnSave;
    private Button btnGoBack;
    private Profile profile;
    private HashMap<String, Object> updatedValues = new HashMap<>();


    public ProfileEditFragment(Profile profile) {
        this.profile = profile;
        for (int i = 0; i < keys.length; i++) {
            Object value = profile.getValue(keys[i]);
            newValues[i] = value;
            oldValues[i] = value;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);


        for (int i = 0; i < 5; i++) {
            textFields[i] = view.findViewById(ids[i]);
            if (oldValues[i] != null) textFields[i].setText(oldValues[i].toString());
        }

        btnSave = view.findViewById(R.id.btn_save_profile);
        btnGoBack = view.findViewById(R.id.btn_back_to_settings);
        tvName = view.findViewById(R.id.tv_name);
        tvDob = view.findViewById(R.id.tv_dob);
        tvUsername = view.findViewById(R.id.tv_username);

        String name = profile.getValue("firstName").toString() + " " +
                profile.getValue("lastName").toString();
        tvName.setText(name);

        tvUsername.setText(profile.getValue("userName").toString());

        setListeners();
        return view;
    }

    private void setListeners() {
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            textFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (finalI == 2 || finalI == 3) {
                        newValues[finalI] = Integer.parseInt(s.toString());
                    } else {
                        newValues[finalI] = s.toString();
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        btnSave.setOnClickListener(v -> {
            boolean changed = false;
            for (int i = 0; i < 5; i++) {
                if (!newValues[i].equals(oldValues[i])) {
                    updatedValues.put(keys[i], newValues[i]);
                    oldValues[i] = newValues[i];
                    changed = true;
                }
            }
            if (changed) {
                profile.setValues(updatedValues);
                updatedValues.clear();
            }
        });

        btnGoBack.setOnClickListener(v -> ((MainActivity) getActivity()).setSettingsFragment());
    }

}