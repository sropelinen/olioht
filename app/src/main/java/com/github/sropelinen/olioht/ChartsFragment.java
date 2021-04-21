package com.github.sropelinen.olioht;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

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

        HashMap<String, HashMap<Long, Integer>> test = new HashMap<>();
        test.put("car", new HashMap<>());
        test.get("car").put(1618230223L, 12);
        test.get("car").put(1618316623L, 5);
        test.get("car").put(1618489423L, 13);
        test.get("car").put(1618403023L, 1);
        test.get("car").put(1618582819L, 3);
        test.get("car").put(1618669219L, 5);
        test.get("car").put(1618755619L, 4);
        test.put("bus", new HashMap<>());
        test.get("bus").put(1618230223L, 3);
        test.get("bus").put(1618316623L, 2);
        test.get("bus").put(1618489423L, 3);
        test.get("bus").put(1618403023L, 2);
        test.get("bus").put(1618582819L, 4);
        test.get("bus").put(1618669219L, 7);
        test.get("bus").put(1618755619L, 4);
        test.put("walk", new HashMap<>());
        test.get("walk").put(1618230223L, 2);
        test.get("walk").put(1618403023L, 4);
        test.get("walk").put(1618489423L, 7);
        test.get("walk").put(1618582819L, 8);
        test.get("walk").put(1618669219L, 3);
        test.get("walk").put(1618755619L, 1);

        final WebView webView = view.findViewById(R.id.chart1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView wv, String url) {
                update(wv, test);
            }
        });

        return view;
    }


    private void update(WebView webView, HashMap<String, HashMap<Long, Integer>> data) {

        StringBuilder headers = new StringBuilder();
        for (String h : data.keySet()) {
            headers.append("\"").append(h).append("\",");
        }
        headers.deleteCharAt(headers.length() - 1);

        HashMap<Integer, int[]> dayData = new HashMap<>();
        int i = 0;
        for (HashMap<Long, Integer> timeData : data.values()) {
            for (Long time : timeData.keySet()) {
                int day = (int) (time / (24 * 60 * 60));
                if (!dayData.containsKey(day)) {
                    dayData.put(day, new int[data.size()]);
                }
                dayData.get(day)[i] += timeData.get(time);
            }
            i++;
        }

        int amount = dayData.size();
        StringBuilder dataTable = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        for (Integer day : dayData.keySet()) {
            calendar.setTimeInMillis((long) day * 24 * 60 * 60 * 1000);
            dataTable.append("[new Date(").append(calendar.get(Calendar.YEAR)).append(",")
                    .append(calendar.get(Calendar.MONTH)).append(",")
                    .append(calendar.get(Calendar.DAY_OF_MONTH)).append("),");
            for (int d : dayData.get(day)) {
                if (d != 0) dataTable.append(d);
                dataTable.append(",");
            }
            dataTable.append("0],");
        }
        dataTable.deleteCharAt(dataTable.length() - 1);

        String command = String.format(Locale.ENGLISH, "javascript:draw(%d, [%s], [%s])",
                amount, headers.toString(), dataTable.toString());
        webView.loadUrl(command);
    }
}