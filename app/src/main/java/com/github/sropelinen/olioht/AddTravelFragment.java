package com.github.sropelinen.olioht;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.Calendar;
import java.util.HashMap;

public class AddTravelFragment extends Fragment {
    private View view;
    private EditText etValueInput, etWeightInput;
    private CalendarView calendarView;
    private Calendar calendar;
    private Profile profile;
    private final Button[] toggles = new Button[5];
    private String[] keys = new String[] { "walk", "bike", "train", "bus", "car"};
    private final int[] ids = new int[] {
            R.id.btn_walk, R.id.btn_bike, R.id.btn_train, R.id.btn_bus, R.id.btn_car
    };
    private int btnIndex = 0;
    private int[] kmList = new int[5];
    private int km = 0, weight = 0;

    public AddTravelFragment(Profile profile) {
        this.profile = profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_travel, container, false);
        etValueInput = view.findViewById(R.id.et_value_input);
        etWeightInput = view.findViewById(R.id.et_update_weight);
        calendarView = view.findViewById(R.id.calendar_view);
        Button btnSubmit = view.findViewById(R.id.btn_submit);

        for (int i = 0; i < ids.length; i++) {
            toggles[i] = view.findViewById(ids[i]);
        }

        // set calendar to current date
        calendar = Calendar.getInstance();
        calendarView.setDate(calendar.getTimeInMillis());
        calendarView.setMaxDate(calendar.getTimeInMillis());
        // set calendar to match calendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth)
                -> calendar.set(year, month, dayOfMonth));

        // text listeners
        etValueInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    km = Integer.parseInt(s.toString());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        etWeightInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    weight = Integer.parseInt(s.toString());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        modeSelected();
        btnSubmit.setOnClickListener(v -> save());

        return view;
    }

    // ToDo/TeeTää Ei sule viel oikein
    private void save() {
        kmList[btnIndex] = km;
        for (int k = 0; k < 5; k++) {
            System.out.println(kmList[k]);
        }

        HashMap<String, Object> values = new HashMap<>();
        for (int i = 0; i < kmList.length; i++) {
            if (kmList[i] != 0) {
                values.put(keys[i], kmList[i]);
            }
            kmList[i] = 0;
        }
        values.put("time", calendar.getTimeInMillis()/1000);
        if (weight != 0) {
            values.put("weight", weight);
        }
        profile.setValues(values);
    }

    private void modeSelected() {
        for (int i = 0; i < ids.length; i++) {
            int finalI = i;
            toggles[i].setOnClickListener(v -> {
                kmList[btnIndex] = km;
                km = kmList[finalI];
                if (kmList[finalI] == 0) {
                    etValueInput.getText().clear();
                } else {
                    etValueInput.setText(""+kmList[finalI]);
                }
                btnIndex = finalI;

                // highlights selected mode
                for (int j = 0; j < toggles.length; j++) {
                    if (finalI == j) {
                        toggles[j].getForeground().setAlpha(255);
                    } else toggles[j].getForeground().setAlpha(100);
                }
            });
        }
    }
}