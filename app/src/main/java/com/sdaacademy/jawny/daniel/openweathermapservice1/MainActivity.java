package com.sdaacademy.jawny.daniel.openweathermapservice1;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.getWarsaw, R.id.getLondon, R.id.getNewYork})
    public void getButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.getWarsaw:
                getWeather("Warsaw");
                break;
            case R.id.getLondon:
                getWeather("London");
                break;
            case R.id.getNewYork:
                getWeather("New York");
                break;
        }
    }

    private void getWeather(String city) {

    }


}
