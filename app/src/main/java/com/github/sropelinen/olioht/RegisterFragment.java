package com.github.sropelinen.olioht;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class RegisterFragment extends Fragment {

    private EditText editFirstName, editLastName, editHeight, editWeight, editUserName, editPassword;
    private TextView textDisplayDate;
    private ArrayAdapter dateAdapter;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editFirstName = (EditText) view.findViewById(R.id.editFirstName);
        editLastName = (EditText) view.findViewById(R.id.editLastName);
        editHeight = (EditText) view.findViewById(R.id.editHeight);
        editWeight = (EditText) view.findViewById(R.id.editWeight);
        editUserName = (EditText) view.findViewById(R.id.editUserNameRegister);
        editPassword = (EditText) view.findViewById(R.id.editPasswordRegister);

        textDisplayDate = (TextView) view.findViewById(R.id.textDisplayDate);
        textDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar  = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        onDateSetListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textDisplayDate.setText(year + "/" + month + "/" + dayOfMonth);
            }
        };
    }


}
