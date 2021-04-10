package com.github.sropelinen.olioht;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends Fragment {
     private View view;

    public SettingsFragment(Profile profile) {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }


    public void goProfile(View view) {

    }
}