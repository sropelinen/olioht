package com.github.sropelinen.olioht;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class AddTravelFragment extends Fragment {
    private EditText etValueInput;
    private Calendar calendar;
    private final Profile profile;
    private final Button[] toggles = new Button[5];
    private final String[] keys = new String[] { "walk", "bike", "train", "bus", "car"};
    private final int[] ids = new int[] {
            R.id.btn_walk, R.id.btn_bike, R.id.btn_train, R.id.btn_bus, R.id.btn_car
    };
    private int btnIndex = 0;
    private int[] kmList = new int[5];
    private int km = 0, weight = 0;
    private boolean isPressed = false;
    private boolean isEntered = false;

    public AddTravelFragment(Profile profile) {
        this.profile = profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_add_travel, container, false);
        etValueInput = view1.findViewById(R.id.et_value_input);
        EditText etWeightInput = view1.findViewById(R.id.et_update_weight);
        CalendarView calendarView = view1.findViewById(R.id.calendar_view);
        Button btnSubmit = view1.findViewById(R.id.btn_submit);

        for (int i = 0; i < ids.length; i++) {
            toggles[i] = view1.findViewById(ids[i]);
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
                    isEntered = true;
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

        return view1;
    }

    /* this method saves values in km list to profile and exits fragment*/
    private void save() {
        if (isPressed && isEntered) {
            kmList[btnIndex] = km;
            HashMap<String, Object> values = new HashMap<>();
            for (int i = 0; i < kmList.length; i++) {
                if (kmList[i] != 0) {
                    values.put(keys[i], kmList[i]);
                }
                kmList[i] = 0;
            }
            values.put("time", calendar.getTimeInMillis() / 1000);
            if (weight != 0) {
                values.put("weight", weight);
            }
            profile.setValues(values);
            // closes keyboard
            InputMethodManager methodManager = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // return to home fragment
            ((MainActivity) Objects.requireNonNull(getActivity())).setHomeFragment();
        }
    }

    /*this method determines which of the five vehicle buttons is chosen and saves kilometres to a km list*/
    private void modeSelected() {
        for (int i = 0; i < ids.length; i++) {
            int finalI = i;
            toggles[i].setOnClickListener(v -> {
                isPressed = true;
                kmList[btnIndex] = km;
                km = kmList[finalI];
                if (kmList[finalI] == 0) {
                    etValueInput.getText().clear();
                } else {
                    etValueInput.setText(String.format(Locale.ENGLISH,"%d", kmList[finalI]));

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