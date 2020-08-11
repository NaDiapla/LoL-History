package com.example.lolhistory.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class RuneParsing {
    Context context;

    public RuneParsing(Context context) {
        this.context = context;
    }

    public String getRuneIconURL(int runeId) {
        String iconUrl = "";
        try {
            InputStream is = context.getAssets().open("runes.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONArray mainArray = new JSONArray(json);
            for (int i=0; i<mainArray.length(); i++) {
                JSONObject mainRuneObject = mainArray.getJSONObject(i);
                if (mainRuneObject.get("id").equals(runeId)) {
                    iconUrl = mainRuneObject.getString("icon");
                    break;
                } else {
                    String runes = mainRuneObject.getString("slots");
                    JSONArray runeArray = new JSONArray(runes);
                    JSONObject keystonesArrayObject = runeArray.getJSONObject(0);
                    runes = keystonesArrayObject.getString("runes");
                    JSONArray keystoneArray = new JSONArray(runes);
                    for (int j=0; j<keystoneArray.length(); j++) {
                        JSONObject keystoneObject = keystoneArray.getJSONObject(j);
                        if (keystoneObject.get("id").equals(runeId)) {
                            iconUrl = keystoneObject.getString("icon");
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d("TESTLOG", "[getRuneIconURL] exception: " + e);
            e.printStackTrace();
        }

        return iconUrl;
    }
}
