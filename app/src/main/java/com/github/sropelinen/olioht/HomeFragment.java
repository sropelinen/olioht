package com.github.sropelinen.olioht;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment {
    View view;
    Button addBtn;
    Fragment addTravelFragment;
    Profile profile;


    public HomeFragment(Profile profile) {
        this.profile = profile;
        addTravelFragment = new AddTravelFragment(profile);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        addBtn = view.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setAddTravelFragment();
        });

        return view;
    }
}