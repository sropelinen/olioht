package com.github.sropelinen.olioht;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Profile {

    private final List<String> infoKeys = Arrays.asList("firstName", "lastName", "home", "age", "height");
    private final List<String> chartKeys = Arrays.asList("weight", "car", "train", "bus", "walk", "bike");

    private static final Profile INSTANCE = new Profile();

    private JSONObject json;
    private HashMap<String, Object> values;
    private HashMap<String, HashMap<Long, Integer>> chartData;

    public static Profile getInstance() {
        return INSTANCE;
    }

    public static Profile init(String data) {
        INSTANCE.setData(data);
        return INSTANCE;
    }

    private void setData(String data) {
        values = new HashMap<>();
        for (String key : infoKeys) {
            values.put(key, null);
        }
        chartData = new HashMap<>();
        for (String key : chartKeys) {
            chartData.put(key, new HashMap<>());
        }
        try {
            json = new JSONObject(data);
            for (Iterator<String> it = json.keys(); it.hasNext();) {
                String mainKey = it.next();
                long time = Long.parseLong(mainKey);
                JSONObject dataPoint = (JSONObject) json.get(mainKey);
                for (Iterator<String> it2 = dataPoint.keys(); it2.hasNext();) {
                    String key = it2.next();
                    if (infoKeys.contains(key)) {
                        values.put(key, dataPoint.get(key));
                    } else if (chartKeys.contains(key)) {
                        chartData.get(key).put(time, (int) dataPoint.get(key));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Object getValue(String key) {
        if (infoKeys.contains(key)) {
            return values.get(key);
        } else if (key.equals("weight")) {
            HashMap<Long, Integer> weights = chartData.get("weight");
            return weights.get(Collections.max(weights.keySet()));
        }
        return null;
    }

    public HashMap<String, Object> getValues(String[] keys) {
        HashMap<String, Object> values = new HashMap<>();
        for (String key : keys) {
            if (infoKeys.contains(key)) {
                values.put(key, values.get(key));
            } else if (key.equals("weight")) {
                HashMap<Long, Integer> weights = chartData.get("weight");
                values.put(key, weights.get(Collections.max(weights.keySet())));
            }
        }
        return values;
    }

    public HashMap<String, HashMap<Long, Integer>> getChartData() {
        return chartData;
    }

    public void setValues(HashMap<String, Object> newValues) {
        long time = System.currentTimeMillis() / 1000;
        for (String key : newValues.keySet()) {
            if (infoKeys.contains(key)) {
                values.put(key, newValues.get(key));
            } else if (chartKeys.contains(key)) {
                chartData.get(key).put(time, (int) newValues.get(key));
            }
        }
        try {
            json.put(String.valueOf(time), new JSONObject(newValues));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getLog() {
        try {
            return json.toString(4);
        } catch (JSONException e) {
            return e.toString();
        }
    }

    public String getData() {
        return json.toString();
    }
}
