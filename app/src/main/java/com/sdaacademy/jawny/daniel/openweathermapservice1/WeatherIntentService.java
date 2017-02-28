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
                    Intent intent1 = new Intent();
                    intent1.setAction("CURRENT_WEATHER_RESPONSE");
                    intent1.putExtra("SUCCESS", false);
                    e.printStackTrace();
                } catch (JSONException e) {
                    Intent intent1 = new Intent();
                    intent1.setAction("CURRENT_WEATHER_RESPONSE");
                    intent1.putExtra("SUCCESS", false);
                    e.printStackTrace();
                }
            }
        }
    }

    private void getCurrentWeather(String city) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(sentRequest(city));
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent();
        intent.setAction("CURRENT_WEATHER_RESPONSE");
        intent.putExtra("SUCCESS", true);
        double temp = jsonObject.getJSONObject("main").optDouble("temp");
        intent.putExtra("TEMPERATURE", temp);
        int pressure = jsonObject.getJSONObject("main").optInt("pressure");
        intent.putExtra("PRESSURE", pressure);
        String main = jsonObject.getJSONArray("weather").getJSONObject(0).optString("main");
        intent.putExtra("MAIN", main);
        long date = jsonObject.optLong("dt");
        intent.putExtra("DATE", date);
        String icon = jsonObject.getJSONArray("weather").getJSONObject(0).optString("icon");
        intent.putExtra("ICON", icon);
        broadcastManager.sendBroadcast(intent);
    }

    private String sentRequest(String city) throws JSONException, IOException {
        Request request = new Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/weather?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric&lang=pl&q=" + city)
                .build();
        OkHttpClient client = new OkHttpClient();
        return client.newCall(request).execute().body().string();
    }
}



