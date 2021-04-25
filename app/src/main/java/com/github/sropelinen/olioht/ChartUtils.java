package com.github.sropelinen.olioht;

import android.os.Handler;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class ChartUtils {

    /* Returns total kilometers of different travel types.
    * Time frame is from (today + deltaDay - days) to (today + deltaDay) */
    public int[] getTotalKms(HashMap<String, HashMap<Long, Integer>> data, int deltaDay, int days) {
        data = (HashMap<String, HashMap<Long, Integer>>) data.clone();
        data.remove("weight");
        // Change data hash map structure
        HashMap<Integer, HashMap<String, Integer>> dayData = new HashMap<>();
        for (String key : data.keySet()) {
            HashMap<Long, Integer> timeData = data.get(key);
            for (Long time : timeData.keySet()) {
                int day = (int) (time / (24 * 60 * 60));
                if (!dayData.containsKey(day)) {
                    dayData.put(day, new HashMap<>());
                }
                if (!dayData.get(day).containsKey(key)) {
                    dayData.get(day).put(key, 0);
                }
                dayData.get(day).put(key, dayData.get(day).get(key) + timeData.get(time));
            }
        }
        if (dayData.size() == 0) return new int[0];

        // Calculates time frame
        int today = (int) (System.currentTimeMillis() / (24 * 60 * 60 * 1000)) + deltaDay;
        int start = Collections.min(dayData.keySet());
        if (today - days > start) start = today - days;

        // Counts total kilometers
        int[] totalKms = new int[5];
        String[] keys = new String[] {"walk", "bike", "train", "bus", "car"};
        for (int day = start; day <= today; day++) {
            if (dayData.containsKey(day)) {
                for (int i = 0; i < 5; i++) {
                    if (dayData.get(day).containsKey(keys[i])) {
                        totalKms[i] += dayData.get(day).get(keys[i]);
                    }
                }
            }
        }
        return totalKms;
    }

    /* Calculates CO2 emission estimate.
     * Time frame is from (today + deltaDay - days) to (today + deltaDay) */
    public void getEmission(HashMap<String, HashMap<Long, Integer>> data, int deltaDay, int days, Callback callback) {

        // Calculates average distances
        int carDist = 0;
        for (int dist : data.get("car").values()) carDist += dist;
        int finalCarDistance = ((data.get("car").size() != 0) ? carDist / data.get("car").size() : 0);
        int busDist = 0;
        for (int dist : data.get("bus").values()) busDist += dist;
        int finalBusDistance = ((data.get("bus").size() != 0) ? busDist / data.get("bus").size() : 0);
        int trainDist = 0;
        for (int dist : data.get("train").values()) trainDist += dist;
        int finalTrainDistance = ((data.get("train").size() != 0) ? trainDist / data.get("train").size() : 0);

        Handler handler = new Handler();
        int[] totalKms = getTotalKms(data, deltaDay, days);

        EstimateParser estimateParser = EstimateParser.getInstance();
        estimateParser.getTransportEstimate(finalCarDistance, finalBusDistance, finalTrainDistance, new EstimateParser.Callback() {
            @Override
            public void run() {
                // Calculates estimate
                double estimate = 0;
                if (finalCarDistance != 0) estimate += carEstimate / finalCarDistance * totalKms[4];
                if (finalBusDistance != 0) estimate += busEstimate / finalBusDistance * totalKms[3];
                if (finalTrainDistance != 0) estimate += trainEstimate / finalTrainDistance * totalKms[2];
                callback.returnValue = estimate;
                handler.post(callback);
            }
        });
    }

    /* Returns JavaScript command to add data to WebView chart. */
    public String getKmJSCommand(HashMap<String, HashMap<Long, Integer>> data, int days) {

        data = (HashMap<String, HashMap<Long, Integer>>) data.clone();
        data.remove("weight");

        // JavaScript table headers
        StringBuilder headers = new StringBuilder();
        for (String h : data.keySet()) {
            headers.append("\"").append(h).append("\",");
        }
        headers.deleteCharAt(headers.length() - 1);

        // Changes data hash map structure
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
        if (dayData.size() == 0) return "";

        int amount = 0;
        StringBuilder dataTable = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        int maxY = 0;

        // Calculates time frame
        int today = (int) (System.currentTimeMillis() / (24 * 60 * 60 * 1000));
        int start = Collections.min(dayData.keySet());
        if (today - days > start) start = today - days;

        // Creates table from day data
        for (int day = start; day <= today; day++) {
            amount++;
            calendar.setTimeInMillis((long) day * 24 * 60 * 60 * 1000);
            dataTable.append("[new Date(").append(calendar.get(Calendar.YEAR)).append(",")
                    .append(calendar.get(Calendar.MONTH)).append(",")
                    .append(calendar.get(Calendar.DAY_OF_MONTH)).append("),");
            if (dayData.containsKey(day)) {
                for (int k = 0; k < dayData.get(day).length; k++) {
                    int d = dayData.get(day)[k];
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

        return String.format(Locale.ENGLISH, "javascript:draw(%d, [%s], [%s], %d)",
                amount, headers.toString(), dataTable.toString(), (int) (maxY * 1.5));
    }

    /* Returns JavaScript command to add data to WebView chart. */
    public String getWeightJSCommand(HashMap<String, HashMap<Long, Integer>> data, int days) {

        // Changes data hash map structure
        HashMap<Integer, Integer> dayData = new HashMap<>();
        for (Long time : data.get("weight").keySet()) {
            int day = (int) (time / (24 * 60 * 60));
            dayData.put(day, data.get("weight").get(time));
        }

        int amount = 0;
        StringBuilder dataTable = new StringBuilder();
        Calendar calendar = Calendar.getInstance();

        // Calculates time frame
        int start = Collections.min(dayData.keySet());
        int today = (int) (System.currentTimeMillis() / (24 * 60 * 60 * 1000));
        if (today - days > start) start = today - days;

        dayData.put(today, data.get("weight").get(Collections.max(data.get("weight").keySet())));

        // Creates table from day data
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

        return String.format(Locale.ENGLISH, "javascript:draw(%d, ['weight'], [%s], %d)",
                amount, dataTable.toString(), maxY);
    }

    /* Returns JavaScript command to add data to WebView chart. */
    public void getEmissionJSCommand(HashMap<String, HashMap<Long, Integer>> data, int days, Callback callback) {

        // Calculates average distances
        int carDist = 0;
        for (int dist : data.get("car").values()) carDist += dist;
        int finalCarDistance = ((data.get("car").size() != 0) ? carDist / data.get("car").size() : 0);
        int busDist = 0;
        for (int dist : data.get("bus").values()) busDist += dist;
        int finalBusDistance = ((data.get("bus").size() != 0) ? busDist / data.get("bus").size() : 0);
        int trainDist = 0;
        for (int dist : data.get("train").values()) trainDist += dist;
        int finalTrainDistance = ((data.get("train").size() != 0) ? trainDist / data.get("train").size() : 0);

        Handler handler = new Handler();

        EstimateParser estimateParser = EstimateParser.getInstance();
        estimateParser.getTransportEstimate(finalCarDistance, finalBusDistance, finalTrainDistance, new EstimateParser.Callback() {
            @Override
            public void run() {
                HashMap<String, Double> multipliers = new HashMap<>();
                multipliers.put("car", (finalCarDistance != 0) ? carEstimate / finalCarDistance : 0);
                multipliers.put("train", (finalTrainDistance != 0) ? trainEstimate / finalTrainDistance : 0);
                multipliers.put("bus", (finalBusDistance != 0) ? busEstimate / finalBusDistance : 0);

                // Changes data hash map structure and calculates emissions
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

                // Calculates time frame
                int today = (int) (System.currentTimeMillis() / (24 * 60 * 60 * 1000));
                int start = Collections.min(dayData.keySet());
                if (today - days > start) start = today - days;

                // Creates table from day data
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

                callback.returnValue = String.format(Locale.ENGLISH, "javascript:draw(%d, ['CO2 emission'], [%s], %d)",
                        amount, dataTable.toString(), (int) maxY);
                handler.post(callback);
            }
        });
    }

    /* Custom runnable to allow returning value when returning to main thread using handler. */
    public static abstract class Callback implements Runnable {
        public Object returnValue = null;
    }
}
