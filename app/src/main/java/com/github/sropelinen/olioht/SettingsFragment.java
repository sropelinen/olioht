package com.github.sropelinen.olioht;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
     private View view;
     private TextView showLog;
     private final Profile profile;

    public SettingsFragment(Profile profile) {
        this.profile = profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        showLog = view.findViewById(R.id.showLog);
        showLog.setOnClickListener(v -> toggleLog());
        return view;
    }


    public void goProfile(View view) {

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