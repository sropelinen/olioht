package com.github.sropelinen.olioht;

import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EstimateParser {

    private static final EstimateParser INSTANCE = new EstimateParser();

    private final String carApiUrl = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/TransportCalculator/CarEstimate?query.buildYear=%d&query.driveDistance=%d&query.size=%s&query.passengerCount=%d";
    private final String publicTransportApiUrl = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/TransportCalculator/PublicTransportEstimate?longDistanceBusYear=%d&longDistanceTrainYear=%d&cityBusWeek=%d&cityTrainWeek=%d";

    private final int buildYear = 2000, passengers = 1;
    private final String size = "smallFamily";

    public static EstimateParser getInstance() {
        return INSTANCE;
    }

    public void getTransportEstimate(int carDistance, int busDistance, int trainDistance, Callback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            String carJson = getCarJson(carDistance);
            if (carJson != null) {
                callback.carEstimate = Double.parseDouble(carJson) / 365;
            }
            Object[] distances = new Object[] {0, 0, 0, 0};
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
