package com.github.sropelinen.olioht;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

public class SettingsFragment extends Fragment {
    private SettingsViewModel viewModel;
    private View view;
    SwitchCompat switchDarkMode;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(SettingsViewModel.class);

        switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setDarkMode(isChecked));
        return view;
    }


    public void goProfile(View view) {

    }
}