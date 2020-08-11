package com.example.lolhistory.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Iterator;

public class ChampionParsing {

    Context context;

    public ChampionParsing(Context context) {
        this.context = context;
    }

    Gson gson = new Gson();

    public String getChampionName(int championId) {
        Log.d("TESTLOG", "[getChampionName] championId: " + championId);
        String championName = "";
        try {
            InputStream is = context.getAssets().open("champion.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            String dataValue = jsonObject.getString("data");

            JSONObject dataObject = new JSONObject(dataValue);
            Iterator i = dataObject.keys();

            while(i.hasNext()) {
                String name = i.next().toString();
                String championValue = dataObject.getString(name);
                JSONObject championObject = new JSONObject(championValue);
                if (String.valueOf(championId).equals(championObject.getString("key"))) {
                    championName = name;
                    break;
                }
            }
        } catch (Exception e) {
            Log.d("TESTLOG", "[getChampionName] exception: " + e);
            e.printStackTrace();
        }

        return championName;
    }

}
