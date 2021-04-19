package com.github.sropelinen.olioht;

import android.graphics.drawable.Drawable;
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
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AddTravelFragment extends Fragment {
    private View view;
    private HorizontalScrollView scrollView;
    private EditText etValueInput;
    private Button btnSubmit;
    private ToggleButton btnWalk, btnBike, btnTrain, btnBus, btnCar;
    private Drawable drawableWalk;
    private CalendarView calendar;
    private String mode = "";
    private String inputText;

    public AddTravelFragment(Profile profile) { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_travel, container, false);
        scrollView = view.findViewById(R.id.mode_scroll_view);
        etValueInput = view.findViewById(R.id.et_value_input);
        calendar = view.findViewById(R.id.calendar);
        btnBike = view.findViewById(R.id.btn_bike);
        btnBus = view.findViewById(R.id.btn_bus);
        btnCar = view.findViewById(R.id.btn_car);
        btnTrain = view.findViewById(R.id.btn_train);
        btnWalk = view.findViewById(R.id.btn_walk);
        btnSubmit = view.findViewById(R.id.btn_submit);

        modeSelected();

        btnSubmit.setOnClickListener(v -> sendData());

        return view;
    }

    // TODO form of the data to send?
    public void sendData() {
        etValueInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputText = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        switch (mode) {
            case "":
                break;
            case "walk":
                break;
            case "bike":
                break;
            case "bus":
                break;
            case "train":
                break;
            case "car":
                break;

        }
    }

    private void modeSelected() {
        btnWalk.setOnClickListener(v -> {
            btnWalk.setAlpha(1);
            btnBike.setAlpha(.7f);
            btnCar.setAlpha(.7f);
            btnBus.setAlpha(.7f);
            btnTrain.setAlpha(.7f);
            mode = "walk";
        });

        btnBike.setOnClickListener(v -> {
            btnBike.setAlpha(1);
            btnWalk.setAlpha(.7f);
            btnCar.setAlpha(.7f);
            btnBus.setAlpha(.7f);
            btnTrain.setAlpha(.7f);
            mode = "bike";
        });

        btnBus.setOnClickListener(v -> {
            btnBus.setAlpha(1);
            btnBike.setAlpha(.7f);
            btnCar.setAlpha(.7f);
            btnWalk.setAlpha(.7f);
            btnTrain.setAlpha(.7f);
            mode = "bus";
        });

        btnCar.setOnClickListener(v -> {
            btnCar.setAlpha(1);
            btnBike.setAlpha(.7f);
            btnWalk.setAlpha(.7f);
            btnBus.setAlpha(.7f);
            btnTrain.setAlpha(.7f);
            mode = "car";
        });

        btnTrain.setOnClickListener(v -> {
            btnTrain.setAlpha(1);
            btnBike.setAlpha(.7f);
            btnCar.setAlpha(.7f);
            btnBus.setAlpha(.7f);
            btnWalk.setAlpha(.7f);
            mode = "train";
        });
    }
}