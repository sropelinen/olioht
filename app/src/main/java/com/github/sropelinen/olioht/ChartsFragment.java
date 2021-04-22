package com.github.sropelinen.olioht;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class ChartsFragment extends Fragment {
    private View view;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private Profile profile;


    public ChartsFragment(Profile profile) {
        this.profile = profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charts, container, false);
        spinner = view.findViewById(R.id.spinner_timeline);
        adapter = new ArrayAdapter<>(((MainActivity) getActivity()), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.timeline));
        spinner.setAdapter(adapter);

        final WebView kmChart = view.findViewById(R.id.chart1);
        kmChart.getSettings().setJavaScriptEnabled(true);
        kmChart.loadUrl("file:///android_asset/index.html");
        kmChart.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView wv, String url) {
                updateKmChart(kmChart, profile.getChartData());
            }
        });

        final WebView weightChart = view.findViewById(R.id.chart2);
        weightChart.getSettings().setJavaScriptEnabled(true);
        weightChart.loadUrl("file:///android_asset/index.html");
        weightChart.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView wv, String url) {
                updateWeightChart(weightChart, profile.getChartData());
            }
        });

        return view;
    }

    private void updateKmChart(WebView webView, HashMap<String, HashMap<Long, Integer>> data) {

        data = (HashMap<String, HashMap<Long, Integer>>) data.clone();
        data.remove("weight");

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

        int amount = 0;
        StringBuilder dataTable = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        int today = (int) (System.currentTimeMillis() / (24 * 60 * 60 * 1000));
        int maxY = 0;
        for (int day = Collections.min(dayData.keySet()); day <= today; day++) {
            amount++;
            calendar.setTimeInMillis((long) day * 24 * 60 * 60 * 1000);
            dataTable.append("[new Date(").append(calendar.get(Calendar.YEAR)).append(",")
                    .append(calendar.get(Calendar.MONTH)).append(",")
                    .append(calendar.get(Calendar.DAY_OF_MONTH)).append("),");
            if (dayData.containsKey(day)) {
                for (int d : dayData.get(day)) {
                    if (d > maxY) maxY = d;
                    dataTable.append(d);
                    dataTable.append(",");
                }
            } else {
                for (int t = 0; t < data.keySet().size(); t++) {
                    dataTable.append("0,");
                }
            }
            dataTable.append("0],");
        }
        dataTable.deleteCharAt(dataTable.length() - 1);

        String command = String.format(Locale.ENGLISH, "javascript:draw(%d, [%s], [%s], %d)",
                amount, headers.toString(), dataTable.toString(), (int) (maxY * 1.5));
        webView.loadUrl(command);
    }

    private void updateWeightChart(WebView webView, HashMap<String, HashMap<Long, Integer>> data) {

        HashMap<Integer, Integer> dayData = new HashMap<>();
        for (Long time : data.get("weight").keySet()) {
            int day = (int) (time / (24 * 60 * 60));
            dayData.put(day, data.get("weight").get(time));
        }
        int today = (int) (System.currentTimeMillis() / (24 * 60 * 60 * 1000));
        dayData.put(today, data.get("weight").get(Collections.max(data.get("weight").keySet())));

        int amount = 0;
        StringBuilder dataTable = new StringBuilder();
        Calendar calendar = Calendar.getInstance();

        for (int day = Collections.min(dayData.keySet()); day <= today; day++) {
            amount++;
            calendar.setTimeInMillis((long) day * 24 * 60 * 60 * 1000);
            dataTable.append("[new Date(").append(calendar.get(Calendar.YEAR)).append(",")
                    .append(calendar.get(Calendar.MONTH)).append(",")
                    .append(calendar.get(Calendar.DAY_OF_MONTH)).append("),");
            if (dayData.containsKey(day)) {
                dataTable.append(dayData.get(day));
            }
            dataTable.append(",0],");
        }
        dataTable.deleteCharAt(dataTable.length() - 1);

        int maxY = (int) (Collections.max(data.get("weight").values()) * 1.5);

        String command = String.format(Locale.ENGLISH, "javascript:draw(%d, ['weight'], [%s], %d)",
                amount, dataTable.toString(), maxY);
        webView.loadUrl(command);
    }
}