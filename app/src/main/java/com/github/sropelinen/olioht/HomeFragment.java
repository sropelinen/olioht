package com.github.sropelinen.olioht;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class HomeFragment extends Fragment {
    private View view;
    private Button addBtn;
    private Profile profile;
    private TextView message, tvCO2, tvChange;
    private double changeInWeek;
    private double CO2Estimate;

    public HomeFragment(Profile profile) {
        this.profile = profile;
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

        addBtn.setOnClickListener(v -> ((MainActivity) getActivity()).setAddTravelFragment());

        message = (TextView) view.findViewById(R.id.daily_tip);
        tvCO2 = view.findViewById(R.id.CO2_est_number);

        MessageBot bot = MessageBot.getInstance();
        bot.readMessages(getContext());
        message.setText(String.format(bot.sendMessage(.1), profile.getValue("firstName")));

        return view;
    }
}
