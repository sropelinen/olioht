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

public class ChartsFragment extends Fragment {
    private Spinner spinner;
    private final Profile profile;
    private final int[] ids = new int[] {
            R.id.tv_km_walk, R.id.tv_km_bike, R.id.tv_km_train, R.id.tv_km_bus, R.id.tv_km_car
    };
    private final TextView[] textViews = new TextView[ids.length];
    private final WebView[] charts = new WebView[3];
    private ChartUtils cUtils;

    public ChartsFragment(Profile profile) {
        this.profile = profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts, container, false);
        for (int i = 0; i < ids.length; i++) {
            textViews[i] = view.findViewById(ids[i]);
        }
        spinner = view.findViewById(R.id.spinner_timeline);

        // initialize spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.timeline));
        spinner.setAdapter(adapter);

        cUtils = new ChartUtils();

        int[] ids = new int[] {R.id.chart1, R.id.chart2, R.id.chart3};

        final int[] webViewsLoaded = {0};
        for (int i = 0; i < 3; i++) {
            charts[i] = view.findViewById(ids[i]);
            charts[i].getSettings().setJavaScriptEnabled(true);
            charts[i].loadUrl("file:///android_asset/index.html");
            charts[i].setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView wv, String url) {
                    webViewsLoaded[0]++;
                    if (webViewsLoaded[0] >= 3) updateCharts();
                }
            });
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateCharts();
                updateTotalKm();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    private void updateCharts() {
        int days = getDays();
        charts[0].loadUrl(cUtils.getKmJSCommand(profile.getChartData(), days));
        charts[1].loadUrl(cUtils.getWeightJSCommand(profile.getChartData(), days));
        cUtils.getEmissionJSCommand(profile.getChartData(), days, new ChartUtils.Callback() {
            @Override
            public void run() {
                charts[2].loadUrl((String) returnValue);
            }
        });
    }

    private void updateTotalKm() {
        int[] kmList = cUtils.getTotalKms(profile.getChartData(), 0, getDays());
        String[] words = new String[] {"foot", "bike", "train", "bus", "car"};
        for (int i = 0; i < kmList.length; i++) {
            textViews[i].setText("Kilometres travelled by " + words[i] +": " + kmList[i]);
        }
    }

    private int getDays() {
        int position = spinner.getSelectedItemPosition();
        int days = 7;
        if (position == 1) days = 30;
        else if (position == 2) days = 365;
        return days;
    }

    // set spinner to weekly on resume
    @Override
    public void onResume() {
        super.onResume();
        spinner.setSelection(0);
    }
}