package com.github.sropelinen.olioht;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Objects;


public class HomeFragment extends Fragment {
    private View view;
    private Button addBtn;
    private Profile profile;
    private TextView message, tvCO2, tvChange;

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

        addBtn.setOnClickListener(v -> ((MainActivity)
                Objects.requireNonNull(getActivity())).setAddTravelFragment());

        message = (TextView) view.findViewById(R.id.daily_tip);
        tvCO2 = view.findViewById(R.id.tv_CO2_est_number);
        tvChange = view.findViewById(R.id.tv_change_percent);

        MessageBot bot = MessageBot.getInstance();
        bot.readMessages(Objects.requireNonNull(getContext()));

        ChartUtils utils = new ChartUtils();
        final double[] CO2Estimate = new double[1];
        utils.getEmission(profile.getChartData(), 0, 7, new ChartUtils.Callback() {
            @Override
            public void run() {
                CO2Estimate[0] = (double) returnValue;
                tvCO2.setText(String.format(Locale.ENGLISH,"%.2f CO2eq", CO2Estimate[0]));
                utils.getEmission(profile.getChartData(), -7, 7, new ChartUtils.Callback() {
                    @Override
                    public void run() {
                        if ((double) returnValue == 0 || CO2Estimate[0] == 0) {
                            tvChange.setText("No data");
                            message.setText("I wish I had some data to calculate... add by clicking ADD");
                        }
                        else {
                            int CO2Change = (int) ((CO2Estimate[0] / (double) returnValue - 1) * 100);
                            tvChange.setText(String.format(Locale.ENGLISH, "%d", CO2Change) + "%");
                            message.setText(String.format(bot.sendMessage(CO2Change),
                                    profile.getValue("firstName")));
                        }
                    }
                });
            }
        });

        return view;
    }
}
