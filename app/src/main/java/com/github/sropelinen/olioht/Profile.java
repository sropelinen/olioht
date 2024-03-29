package com.github.sropelinen.olioht;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Profile {

    private final List<String> infoKeys = Arrays.asList("firstName", "lastName", "home", "height", "userName", "birthDate");
    private final List<String> chartKeys = Arrays.asList("weight", "car", "train", "bus", "walk", "bike");

    private static final Profile INSTANCE = new Profile();
    private Runnable saver;

    private JSONObject json;
    private HashMap<String, Object> values; // {key: value}
    private HashMap<String, HashMap<Long, Integer>> chartData; // {key: {time: value}}

    /* Main activity accesses already created singleton instance. */
    public static Profile getInstance() {
        return INSTANCE;
    }

    /* Login activity initializes singleton instance. */
    public static Profile init(String data) {
        INSTANCE.setData(data);
        return INSTANCE;
    }

    /* Updates user database without AccountManager instance in Profile. */
    public void setSaver(Runnable saver) {
        this.saver = saver;
    }

    /* Loads profile information to values and chartData from json string. */
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
                        // "time" key can override data save time in chartData
                        long t = time;
                        if (dataPoint.has("time")) {
                            t = Long.parseLong(dataPoint.get("time").toString());
                        }
                        chartData.get(key).put(t, (int) dataPoint.get(key));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getData() {
        return json.toString();
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

    /* Updates profile information and json object and saves new information to the database. */
    public void setValues(HashMap<String, Object> newValues) {
        long time = System.currentTimeMillis() / 1000;
        for (String key : newValues.keySet()) {
            if (infoKeys.contains(key)) {
                values.put(key, newValues.get(key));
            } else if (chartKeys.contains(key)) {
                // "time" key can override current time in chartData
                long t = time;
                if (newValues.containsKey("time")) {
                    t = Long.parseLong(newValues.get("time").toString());
                }
                if (!chartData.get(key).containsKey(t)) {
                    chartData.get(key).put(t, 0);
                }
                chartData.get(key).put(t, chartData.get(key).get(t) + (int) newValues.get(key));
            }
        }
        try {
            json.put(String.valueOf(time), new JSONObject(newValues));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (saver != null) {
            saver.run();
        }
    }

    /* Same as getData(), but easier to read. */
    public String getLog() {
        try {
            return json.toString(4);
        } catch (JSONException e) {
            return e.toString();
        }
    }
}
