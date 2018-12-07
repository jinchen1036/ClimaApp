package com.londonappbrewery.clima_completed;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import android.support.constraint.ConstraintLayout;


public class WeatherForecast extends AppCompatActivity {
    // TextView to be update
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    TextView textView8;
    TextView textView9;
    TextView textView10;
    ConstraintLayout forecastLayout;
    ForecastModel data;
    String tempType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_forecast_layout);
        Log.d("Clima", "Inside WeatherForecast");

        ImageButton backButton = findViewById(R.id.backButton2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Go back and destroy the ChangeCityController
                    finish();
                }
//                startActivity(new Intent(WeatherForecast.this, WeatherController.class));
                // Go back and destroy the ChangeCityController
        });



        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        textView10 = findViewById(R.id.textView10);

        forecastLayout = findViewById(R.id.weatherForecastLayout);
        TextView maintitle = findViewById(R.id.MainTitle);

        Intent intent = getIntent();
        data = (ForecastModel)intent.getSerializableExtra(WeatherController.Forecast);
        String city = intent.getStringExtra("city");
        tempType = intent.getStringExtra("tempType");
        String background = intent.getStringExtra("background");

        if(tempType.equals("Centigrade"))
            maintitle.setText(String.format("%12s%-8s%17s%12s","","Date","Condition","°C"));
        else
            maintitle.setText(String.format("%12s%-8s%17s%12s","","Date","Condition","°F"));

        if (background.equals("T"))
            forecastLayout.setBackgroundResource((R.drawable.birdsbackground));

        // Data back to Main Page
        intent.putExtra("City", city);
        intent.putExtra("temp", tempType);
        intent.putExtra("background", background);

//        textView1.setText("In");
        Log.d("Clima", "Get WeatherForecast ");
//        ScrollView scoll = findViewById(R.id.scrollView);
        updateUI(data);

        setResult(Activity.RESULT_OK, intent);
    }

    private void updateUI(ForecastModel weather) {
        Log.d("Clima", "Inside UpdateUI for ForecastModel");

//        Get data from api
        ArrayList<String> Date = new ArrayList<String>(weather.getmDate());
        ArrayList<Integer> Temp = new ArrayList<Integer>(weather.getmTemperature());
        ArrayList<String> Condition = new ArrayList<String>(weather.getmCondition());
//        ArrayList<Integer> Date= weather.getmDate();
//        ArrayList<Integer> Temp = weather.getmTemperature();
//        ArrayList<String> Condition = weather.getmCondition();

//        Print data from api
        Log.d("Clima", "Finish getting date of UpdateUI for ForecastModel");
        Log.d("Clima", "Data Size: "+Integer.toString(Date.size()));
        Log.d("Clima", "Data for ForecastModel: "+Date.get(0));
        Log.d("Clima", "Condition for ForecastModel: "+Condition.get(0));
        Log.d("Clima", "Temp for ForecastModel: "+Temp.get(0).toString());

//        Assign the string to be display in the textView
        ArrayList<String> Display = new ArrayList<>(10);
        if (tempType.equals("Centigrade")){
            for(int i = 0; i<10; i++){
                Display.add(String.format("%-20s%8s%19s    ",Date.get(i),Condition.get(i),Temp.get(i).toString()));
            }
        }
        else
        {

            for(int i = 0; i<10; i++){
                int Ftemp = Temp.get(i)*9/5+32;
                Display.add(String.format("%-20s%8s%19s    ",Date.get(i),Condition.get(i),Integer.toString(Ftemp)));
            }
        }

        Log.d("Clima", "Finish Assign Display array");

        textView1.setText(String.format("%s",Display.get(0)));
        textView2.setText(String.format("%s",Display.get(1)));
        textView3.setText(String.format("%s",Display.get(2)));
        textView4.setText(String.format("%s",Display.get(3)));
        textView5.setText(String.format("%s",Display.get(4)));
        textView6.setText(String.format("%s",Display.get(5)));
        textView7.setText(String.format("%s",Display.get(6)));
        textView8.setText(String.format("%s",Display.get(7)));
        textView9.setText(String.format("%s",Display.get(8)));
        textView10.setText(String.format("%s",Display.get(9)));
        Log.d("Clima", "Finish SetText for all textView");

    }


}
