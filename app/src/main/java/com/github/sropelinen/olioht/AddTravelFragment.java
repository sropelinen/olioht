package com.github.sropelinen.olioht;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

// TODO change text in toolbar
public class AddTravelFragment extends Fragment {
    View view;
    HorizontalScrollView scrollView;
    TextView valueInput;

    public AddTravelFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_travel, container, false);
        scrollView = view.findViewById(R.id.mode_scroll_view);
        valueInput = view.findViewById(R.id.value_input);


        return view;
    }
}