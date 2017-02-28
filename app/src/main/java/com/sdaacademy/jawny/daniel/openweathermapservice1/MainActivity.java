package com.sdaacademy.jawny.daniel.openweathermapservice1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.temp)
    TextView mTemp;

    @BindView(R.id.pressure)
    TextView mPressure;

    @BindView(R.id.main)
    TextView mMain;

    @BindView(R.id.date)
    TextView mDate;

    @BindView(R.id.icon)
    ImageView mIcon;
    private LocalBroadcastManager broadcastManager;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra("SUCCESS", false);
            if (success) {
                mTemp.setText(intent.getStringExtra("TEMPERATURE"));
                mPressure.setText(intent.getStringExtra("PRESSURE"));
                mMain.setText(intent.getStringExtra("MAIN"));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("CURRENT_WEATHER_RESPONSE");
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @OnClick({R.id.getWarsaw, R.id.getLondon, R.id.getNewYork})
    public void getButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.getWarsaw:
                Intent intent1 = new Intent(this, WeatherIntentService.class)
                        .setAction("GET_CURRENT_WEATHER")
                        .putExtra("CITY", "Warsaw");
                startService(intent1);
                break;
            case R.id.getLondon:
                Intent intent2 = new Intent(this, WeatherIntentService.class)
                        .setAction("GET_CURRENT_WEATHER")
                        .putExtra("CITY", "London");
                startService(intent2);
                break;
            case R.id.getNewYork:
                Intent intent3 = new Intent(this, WeatherIntentService.class)
                        .setAction("GET_CURRENT_WEATHER")
                        .putExtra("CITY", "NewYork");
                startService(intent3);
                break;
        }
    }
}
