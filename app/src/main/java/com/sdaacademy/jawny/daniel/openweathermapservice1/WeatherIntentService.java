package com.sdaacademy.jawny.daniel.openweathermapservice1;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WeatherIntentService extends IntentService {

    public WeatherIntentService() {
        super("WeatherIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if ("GET_CURRENT_WEATHER".equals(action)) {
                String city = intent.getStringExtra("CITY");
                try {
                    getCurrentWeather(city);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getCurrentWeather(String city) throws IOException, JSONException {
        JSONObject jsonObject =new JSONObject(sentRequest(city));
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent();
        intent.setAction("CURRENT_WEATHER_RESPONSE");
        intent.putExtra("SUCCESS", true);
        intent.putExtra("TEMPERATURE", jsonObject.optDouble("temp"));
        broadcastManager.sendBroadcast(intent);

    }

    private String sentRequest(String city) throws JSONException, IOException {
        Request request = new Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/weather?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&q=" + city)
                .build();
        OkHttpClient client = new OkHttpClient();
        return client.newCall(request).execute().body().string();
    }
}



