package com.github.sropelinen.olioht;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ChartsFragment extends Fragment {
    private View view;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;



    public ChartsFragment(Profile profile) {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charts, container, false);
        spinner = view.findViewById(R.id.spinner_timeline);
        adapter = new ArrayAdapter<>(((MainActivity) getActivity()), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.timeline));
        spinner.setAdapter(adapter);
        return view;
    }

}