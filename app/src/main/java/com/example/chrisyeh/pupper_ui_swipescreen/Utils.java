package com.example.chrisyeh.pupper_ui_swipescreen;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris Yeh on 11/13/2017 for the purposes of UCSC CMPS115 class project: puppers
 * Utils.java: java class file created to load and bind profiles to cards in the UI
 *
 */

public class Utils {

    private static final String TAG = "Utils";

    public static List<Profile> loadProfiles(Context context) {
        // try build dog profile by loading json assets
        try {
            // open GSON builder
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONArray jArray = new JSONArray(loadJSONFromAsset(context, "profiles.json"));
            List<Profile> profileList = new ArrayList<>();

            // parse through JSON array and convert JSON objects to java objects
            for (int index = 0; index < jArray.length(); index++) {
                // convert JSON to java object profile & add it to profile array list
                Profile profile = gson.fromJson(jArray.getString(index), Profile.class);
                profileList.add(profile);
            }
            // return profile list if no exceptions thrown
            return profileList;
        } catch (Exception ex){
            ex.printStackTrace();;
            return null;
        }
    }

    private static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        InputStream inStream= null;

        try {
            AssetManager manager = context.getAssets();
            Log.d(TAG,"path " + jsonFileName);
            inStream = manager.open(jsonFileName);
            int size = inStream.available();
            byte[] buffer = new byte[size];
            inStream.read(buffer);
            inStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
