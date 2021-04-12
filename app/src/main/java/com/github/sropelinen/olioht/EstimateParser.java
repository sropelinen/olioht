package com.github.sropelinen.olioht;

import android.os.Handler;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

// ToDo error handling

public class EstimateParser {

    private static final EstimateParser INSTANCE = new EstimateParser();

    private final String carApiUrl = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/TransportCalculator/CarEstimate?query.buildYear=%d&query.driveDistance=%d&query.size=%s&query.passengerCount=%d";
    private final String publicTransportApiUrl = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/TransportCalculator/PublicTransportEstimate?longDistanceBusYear=%d&longDistanceTrainYear=%d&cityBusWeek=%d&cityTrainWeek=%d";

    // ToDo pitäiskö nää ottaa asetuksista
    private final int buildYear = 2000, passengers = 1;
    private final String size = "mini";

    public static EstimateParser getInstance() {
        return INSTANCE;
    }

    public void getCarEstimate(int distance, Callback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            String url = String.format(Locale.ENGLISH, carApiUrl, buildYear, distance * 365, size, passengers);
            try {
                Scanner in = new Scanner(new URL(url).openStream());
                callback.carEstimate = Double.parseDouble(in.nextLine()) / 365;
                in.close();
                handler.post(callback);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getPublicTransportEstimate(int busDistance, int trainDistance, Callback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            Object[] distances = new Object[] {0, 0, 0, 0};
            if (busDistance > 15) distances[0] = busDistance * 365;
            else distances[2] = busDistance * 365;
            if (trainDistance > 15) distances[1] = trainDistance * 365;
            else distances[3] = trainDistance * 365;
            String url = String.format(Locale.ENGLISH, publicTransportApiUrl, distances);
            try {
                Scanner in = new Scanner(new URL(url).openStream());
                callback.trainEstimate = Double.parseDouble(in.findInLine("(\\d+\\.\\d+)"));
                callback.busEstimate = Double.parseDouble(in.findInLine("(\\d+\\.\\d+)"));
                in.close();
                handler.post(callback);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static abstract class Callback implements Runnable {
        public double carEstimate = 0;
        public double busEstimate = 0;
        public double trainEstimate = 0;
    }
}
