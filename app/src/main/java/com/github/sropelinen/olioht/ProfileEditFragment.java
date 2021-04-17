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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileEditFragment extends Fragment {
    private View view;
    private EditText etFirstname;
    private EditText etLastname;
    private EditText etHeight;
    private EditText etWeight;
    private EditText etHome;
    private TextView tvName;
    private TextView tvDob;
    private TextView tvUsername;
    private String firstnameNew;
    private String firstnameOld;
    private String lastnameNew;
    private String lastnameOld;
    private String homeNew;
    private String homeOld;
    private int heightNew;
    private int heightOld;
    private int weightNew;
    private int weightOld;
    private Button btnSave;
    private Button btnGoBack;
    private Profile profile;
    private HashMap<String, Object> updatedValues = new HashMap<>();
    private List<String> infoKeys;


    public ProfileEditFragment(Profile profile) {
        this.profile = profile;
        infoKeys = profile.getInfoKeys();
        firstnameNew = profile.getValue("firstName").toString();
        firstnameOld = profile.getValue("firstName").toString();
        lastnameNew = profile.getValue("lastName").toString();
        lastnameOld = profile.getValue("lastName").toString();
        heightNew = (Integer) profile.getValue("height");
        heightOld = (Integer) profile.getValue("height");
        weightNew = (Integer) profile.getValue("weight");
        weightOld = weightNew;
        if (profile.getValue("home") != null) {
            homeNew = profile.getValue("home").toString();
            homeOld = profile.getValue("home").toString();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        etFirstname = view.findViewById(R.id.et_edit_firstname);
        etLastname = view.findViewById(R.id.et_edit_lastname);
        etHeight = view.findViewById(R.id.et_edit_height);
        etWeight = view.findViewById(R.id.et_edit_weight);
        btnSave = view.findViewById(R.id.btn_save_profile);
        btnGoBack = view.findViewById(R.id.btn_back_to_settings);
        tvName = view.findViewById(R.id.tv_name);
        tvDob = view.findViewById(R.id.tv_dob);
        tvUsername = view.findViewById(R.id.tv_username);
        etHome = view.findViewById(R.id.et_edit_home);

        etFirstname.setText(firstnameOld);
        etLastname.setText(lastnameOld);
        etHeight.setText(heightOld+"");
        etWeight.setText(weightOld+"");
        etHome.setText(homeOld);
//        tvDob.setText(profile.getValue("birthDate").toString()); // TODO activate when possible
//        tvUsername.setText();
        String name = profile.getValue("firstName").toString() + " " + profile.getValue("lastName").toString();
        tvName.setText(name);

        tvUsername.setText(profile.getValue("userName").toString());

        setListeners();
        return view;
    }

    private void setListeners() {
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

        etHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    heightNew = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    weightNew = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        etHome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                homeNew = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnSave.setOnClickListener(v -> {
            boolean changed = false;
            if (!firstnameNew.equals(firstnameOld)) {
                updatedValues.put("firstName", firstnameNew);
                firstnameOld = firstnameNew;
                changed = true;
            }
            if (!lastnameNew.equals(lastnameOld)) {
                updatedValues.put("lastName", lastnameNew);
                lastnameOld = lastnameNew;
                changed = true;
            }
            if (heightNew != heightOld) {
                updatedValues.put("height", heightNew);
                heightOld = heightNew;
                changed = true;
            }
            if (!homeNew.equals(homeOld)) {
                updatedValues.put("home", homeNew);
                homeOld = homeNew;
                changed = true;
            }
            if (weightNew != weightOld) {
                updatedValues.put("weight", weightNew);
                weightOld = weightNew;
                changed = true;
            }
            if (changed) {
                profile.setValues(updatedValues);
            }
        });

        btnGoBack.setOnClickListener(v -> ((MainActivity) getActivity()).setSettingsFragment());
    }

}