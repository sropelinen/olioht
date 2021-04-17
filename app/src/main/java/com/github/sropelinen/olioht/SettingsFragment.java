package com.github.sropelinen.olioht;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
    private View view;
    private TextView showLog;
    private final Profile profile;
    private SettingsViewModel viewModel;
    private SwitchCompat switchDarkMode;
    private TextView tvEditProfile;

    public SettingsFragment(Profile profile) {
        this.profile = profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        showLog = view.findViewById(R.id.showLog);
        showLog.setOnClickListener(v -> toggleLog());
        viewModel = new ViewModelProvider(getActivity()).get(SettingsViewModel.class);

        tvEditProfile = view.findViewById(R.id.tv_edit_profile);
        tvEditProfile.setOnClickListener(v -> ((MainActivity) getActivity()).setProfileEditFragment());

        switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setDarkMode(isChecked));
        return view;
    }

    private void toggleLog() {
        // ToDo kunnol tää
        if (showLog.getText().toString().equals("View log")) {
            showLog.setText(profile.getLog());
        } else {
            showLog.setText("View log");
        }
    }

}