package com.github.sropelinen.olioht;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class ChartsFragment extends Fragment {
    private View view;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private Profile profile;
    private int[] ids = new int[] {
            R.id.tv_km_walk, R.id.tv_km_bike, R.id.tv_km_train, R.id.tv_km_bus, R.id.tv_km_car
    };
    private TextView[] textViews = new TextView[ids.length];
    private int[] kmList = new int[ids.length];
    public ChartsFragment(Profile profile) {
        this.profile = profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charts, container, false);
        for (int i = 0; i < ids.length; i++) {
            textViews[i] = view.findViewById(ids[i]);
        }
        spinner = view.findViewById(R.id.spinner_timeline);

        // initialize spinner
        adapter = new ArrayAdapter<>(((MainActivity) getActivity()), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.timeline));
        spinner.setAdapter(adapter);

        final WebView kmChart = view.findViewById(R.id.chart1);
        kmChart.getSettings().setJavaScriptEnabled(true);
        kmChart.loadUrl("file:///android_asset/index.html");
        kmChart.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView wv, String url) {
                updateKmChart(kmChart, profile.getChartData(), 7);
            }
        });

        final WebView weightChart = view.findViewById(R.id.chart2);
        weightChart.getSettings().setJavaScriptEnabled(true);
        weightChart.loadUrl("file:///android_asset/index.html");
        weightChart.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView wv, String url) {
                updateWeightChart(weightChart, profile.getChartData(), 7);
            }
        });

        final WebView emissionChart = view.findViewById(R.id.chart3);
        emissionChart.getSettings().setJavaScriptEnabled(true);
        emissionChart.loadUrl("file:///android_asset/index.html");
        emissionChart.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView wv, String url) {
                updateEmissionChart(emissionChart, profile.getChartData(), 7);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int days = 7;
                if (position == 1) days = 30;
                else if (position == 2) days = 365;
                updateKmChart(kmChart, profile.getChartData(), days);
                updateWeightChart(weightChart, profile.getChartData(), days);
                updateTotalKm();
                for (int i = 0; i < kmList.length; i++) {
                    kmList[i] = 0;
                }
                updateEmissionChart(emissionChart, profile.getChartData(), days);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    private void updateTotalKm() {
        textViews[0].setText("Kilometres travelled by foot: "+ kmList[0]);
        textViews[1].setText("Kilometres travelled by bike: "+ kmList[1]);
        textViews[2].setText("Kilometres travelled by train: "+ kmList[2]);
        textViews[3].setText("Kilometres travelled by bus: "+ kmList[3]);
        textViews[4].setText("Kilometres travelled by car: "+ kmList[4]);
    }

    private void updateKmChart(WebView webView, HashMap<String, HashMap<Long, Integer>> data, int days) {

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
        if (dayData.size() == 0) return;

        int amount = 0;
        StringBuilder dataTable = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        int today = (int) (System.currentTimeMillis() / (24 * 60 * 60 * 1000));
        int maxY = 0;
        int start = Collections.min(dayData.keySet());
        if (today - days > start) start = today - days;
        for (int day = start; day <= today; day++) {
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

    private void updateWeightChart(WebView webView, HashMap<String, HashMap<Long, Integer>> data, int days) {

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

        int start = Collections.min(dayData.keySet());
        if (today - days > start) start = today - days;
        for (int day = start; day <= today; day++) {
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

    private void updateEmissionChart(WebView webView, HashMap<String, HashMap<Long, Integer>> data, int days) {

        int carDist = 0;
        for (int dist : data.get("car").values()) carDist += dist;
        int finalCarDistance = ((data.get("car").size() != 0) ? carDist / data.get("car").size() : 0);
        int busDist = 0;
        for (int dist : data.get("bus").values()) busDist += dist;
        int finalBusDistance = ((data.get("bus").size() != 0) ? busDist / data.get("bus").size() : 0);
        int trainDist = 0;
        for (int dist : data.get("train").values()) trainDist += dist;
        int finalTrainDistance = ((data.get("train").size() != 0) ? trainDist / data.get("train").size() : 0);

        EstimateParser estimateParser = EstimateParser.getInstance();
        estimateParser.getTransportEstimate(finalCarDistance, finalBusDistance, finalTrainDistance, new EstimateParser.Callback() {
            @Override
            public void run() {
                HashMap<String, Double> multipliers = new HashMap<>();
                multipliers.put("car", carEstimate / finalCarDistance);
                multipliers.put("bus", busEstimate / finalBusDistance);
                multipliers.put("train", trainEstimate / finalTrainDistance);

                HashMap<Integer, Double> dayData = new HashMap<>();
                for (String key : data.keySet()) {
                    if (multipliers.containsKey(key)) {
                        HashMap<Long, Integer> timeData = data.get(key);
                        for (Long time : timeData.keySet()) {
                            int day = (int) (time / (24 * 60 * 60));
                            if (!dayData.containsKey(day)) {
                                dayData.put(day, 0.0);
                            }
                            dayData.put(day, dayData.get(day) + timeData.get(time) * multipliers.get(key));
                        }
                    }
                }
                if (dayData.size() == 0) return;

                int amount = 0;
                StringBuilder dataTable = new StringBuilder();
                Calendar calendar = Calendar.getInstance();
                int today = (int) (System.currentTimeMillis() / (24 * 60 * 60 * 1000));
                int start = Collections.min(dayData.keySet());
                if (today - days > start) start = today - days;
                for (int day = start; day <= today; day++) {
                    amount++;
                    calendar.setTimeInMillis((long) day * 24 * 60 * 60 * 1000);
                    dataTable.append("[new Date(").append(calendar.get(Calendar.YEAR)).append(",")
                            .append(calendar.get(Calendar.MONTH)).append(",")
                            .append(calendar.get(Calendar.DAY_OF_MONTH)).append("),");
                    if (dayData.containsKey(day)) {
                        dataTable.append(dayData.get(day));
                    } else {
                        dataTable.append(0);
                    }
                    dataTable.append(",0],");
                }
                dataTable.deleteCharAt(dataTable.length() - 1);

                double maxY = Collections.max(dayData.values()) * 1.5;
                if (maxY < 1) maxY = 1;

                String command = String.format(Locale.ENGLISH, "javascript:draw(%d, ['CO2 emission'], [%s], %d)",
                        amount, dataTable.toString(), (int) maxY);
                webView.loadUrl(command);
            }
        });
    }
}