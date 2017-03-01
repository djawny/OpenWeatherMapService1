package com.sdaacademy.jawny.daniel.openweathermapservice1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.city)
    EditText mCity;

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
                String temperature = String.format("%s C", intent.getDoubleExtra("TEMPERATURE", 0));
                mTemp.setText(temperature);
                mPressure.setText(String.format("%s hpa", intent.getIntExtra("PRESSURE", 0)));
                mMain.setText(intent.getStringExtra("MAIN"));
                String dateString = convertDate(intent.getLongExtra("DATE", 0));
                mDate.setText(String.format("%s", dateString));
                String icon = intent.getStringExtra("ICON");
                Picasso.with(getApplicationContext())
                        .load("http://openweathermap.org/img/w/" + icon + ".png")
                        .into(mIcon);
            } else {
                showError("Pobranie pogody zakończyło się niepowodzeniem");
            }
        }
    };

    private String convertDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeInMillis(timestamp * 1000);
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        return sdf.format(calendar.getTime());
    }

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

    @OnClick({R.id.getWarsaw, R.id.getLondon, R.id.getNewYork, R.id.getCity})
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
            case R.id.city:
                Intent intent4 = new Intent(this, WeatherIntentService.class)
                        .setAction("GET_CURRENT_WEATHER")
                        .putExtra("CITY", mCity.getText().toString());
                startService(intent4);
                break;
        }
    }

    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }
}
