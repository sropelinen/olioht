package com.github.sropelinen.olioht;

import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Locale;


public class EstimateParser {

    private static final EstimateParser INSTANCE = new EstimateParser();

    private final String carApiUrl = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/TransportCalculator/CarEstimate?query.buildYear=%d&query.driveDistance=%d&query.size=%s&query.passengerCount=%d";
    private final String publicTransportApiUrl = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/TransportCalculator/PublicTransportEstimate?longDistanceBusYear=%d&longDistanceTrainYear=%d&cityBusWeek=%d&cityTrainWeek=%d";

    private final int buildYear = 2000, passengers = 1;
    private final String size = "smallFamily";

    public static EstimateParser getInstance() {
        return INSTANCE;
    }

    /* This method calculates estimated emission of public transport */
    public void getTransportEstimate(int carDistance, int busDistance, int trainDistance, Callback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            String carJson = getCarJson(carDistance);
            if (carJson != null) {
                /* API:s input is km/yr */
                callback.carEstimate = Double.parseDouble(carJson) / 365;
            }
            Object[] distances = new Object[] {0, 0, 0, 0};
            /* API:s input for long train and bus distances are km/yr
            *  but for short distances it's km/wk */
            if (busDistance > 15) distances[0] = busDistance * 365;
            else distances[2] = busDistance * 7;
            if (trainDistance > 15) distances[1] = trainDistance * 365;
            else distances[3] = trainDistance * 7;
            String json = getPublicJson(distances);
            if (json != null) {
                try {
                    JSONObject object = new JSONArray("[" + json + "]").getJSONObject(0);
                    callback.busEstimate = Double.parseDouble(object.getString("Bus")) / 365;
                    callback.trainEstimate = Double.parseDouble(object.getString("Train")) / 365;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            handler.post(callback);
        }).start();
    }

    /* this method returns .json of public transport emission */
    public String getPublicJson(Object[] distances) {
        String response = null;
        try {
            URL url = new URL(String.format(Locale.ENGLISH, publicTransportApiUrl, distances));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            response = sb.toString();
            in.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /* this method returns .json of car emission */
    public String getCarJson(int distance) {
        String response = null;
        try {
            URL url = new URL(String.format(Locale.ENGLISH, carApiUrl, buildYear, distance, size, passengers));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            response = sb.toString();
            in.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    public static abstract class Callback implements Runnable {
        public double carEstimate = 0;
        public double busEstimate = 0;
        public double trainEstimate = 0;
    }
}
