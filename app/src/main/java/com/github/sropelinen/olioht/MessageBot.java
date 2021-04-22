package com.github.sropelinen.olioht;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MessageBot{

    private static final MessageBot INSTANCE = new MessageBot();
    private ArrayList<String> messages = new ArrayList<>();
    private String greeting, goodJob, badJob;

    public static MessageBot getInstance() {
        return INSTANCE;
    }

    public void readMessages(Context context) {
        try {
            InputStream in = context.openFileInput("messages.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String s;
            goodJob = br.readLine();
            badJob = br.readLine();
            while ((s = br.readLine()) != null) {
                messages.add(s);
            }
            System.out.println("File read successfully");
            in.close();
        } catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        }
    }
    public String sentMessage(double changeInWeek, boolean isNewUser) {
        if (isNewUser) return greeting;
        else {
            int num = (int) (Math.random() * 2 - 0.001) ;
            if (num == 1) {
                if ( changeInWeek < 0) return goodJob;
                else return badJob;
            }
            else {
                return messages.get((int) (Math.random() * messages.size() - .001));
            }
        }
    }
}