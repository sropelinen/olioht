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
    private String goodJob, badJob, okJob;

    public static MessageBot getInstance() {
        return INSTANCE;
    }

    public void readMessages(Context context) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    context.getAssets().open("messages.txt")));
            String s;
            goodJob = br.readLine();
            okJob = br.readLine();
            badJob = br.readLine();
            while ((s = br.readLine()) != null) {
                messages.add(s);
            }
            br.close();
        } catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        }
    }
    public String sendMessage(int changeInWeek) {
        int num = (int) (Math.random() * 2 - 0.00001) ;
        if (num == 1) {
            if ( changeInWeek < 0) return goodJob;
            else if (changeInWeek > 0) return badJob;
            else return okJob;
        }
        else {
            return messages.get((int) (Math.random() * messages.size() - .001));
        }
    }
}