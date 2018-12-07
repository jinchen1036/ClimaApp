package com.londonappbrewery.clima_completed;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;



public class WeatherController extends AppCompatActivity {

    // Request Codes:
    final int REQUEST_CODE = 123; // Request Code for permission request callback
    final int NEW_CITY_CODE = 456; // Request code for starting new activity for result callback

    // Base URL for the OpenWeatherMap API. EDIT!!!
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/";

    // App ID to use OpenWeather data
    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";

    // Don't want to type 'Clima' in all the logs, so putting this in a constant here.
    final String LOGCAT_TAG = "Clima";

    // Set LOCATION_PROVIDER here. Using GPS_Provider for Fine Location (good for emulator):
    // Recommend using LocationManager.NETWORK_PROVIDER on physical devices (reliable & fast!)
    final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    final int forecastDay = 10;
    final static String Forecast = "Forecast";
    String firstCall = "true";

    // Member Variables:
    TextView mCityLabel;
    TextView mHumidity;
    TextView mPressure;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;
    RelativeLayout weatherControllerLayout;

    String City="new york";               //Jie Lan-which city
    String temperatureType="Centigrade"; //Jie Lan-what kind of temperature
    String background = "F";
    // Declaring a LocationManager and a LocationListener here:
    LocationManager mLocationManager;
    LocationListener mLocationListener;

//    private String City;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        // Linking the elements in the layout to Java code.
        // API 26 and above does not require casting anymore.
        // Can write: mCityLabel = findViewById(R.id.locationTV);
        // Instead of: mCityLabel = (TextView) findViewById(R.id.locationTV);

        mCityLabel = findViewById(R.id.locationTV);
        //Humidity and pressure text
        mHumidity = findViewById(R.id.humidity_txt);
        mPressure = findViewById(R.id.Pressure_txt);

        mWeatherImage = findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = findViewById(R.id.tempTV);
        weatherControllerLayout = findViewById(R.id.weatherControllerLayout);

        ImageButton changeCityButton = findViewById(R.id.changeCityButton);
        // Add an OnClickListener to the changeCityButton here:
        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(WeatherController.this, ChangeCityController.class);
                myIntent.putExtra("City", City);
                myIntent.putExtra("temperature", temperatureType);
                myIntent.putExtra("background", background);

                // Using startActivityForResult since we just get back the city name.
                // Providing an arbitrary request code to check against later.
                startActivityForResult(myIntent, NEW_CITY_CODE);
            }
        });


        // Edit Button for new forecast page
        ImageButton forecastButton = findViewById(R.id.forcastButton);
        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOGCAT_TAG, "Forecast weather for new city");
                RequestParams params = new RequestParams();
                params.put("q", City);
                params.put("cnt", forecastDay);
                params.put("appid", APP_ID);
                forecastWeather(params);

            }
        });
        Log.d(LOGCAT_TAG,"City Value: "+City);
    }



    // onResume() life cycle callback:
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOGCAT_TAG, "onResume() called");
        if (firstCall == "true")
            getWeatherForNewCity(City);

    }

    // Freeing up resources when the app enters the paused state.
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOGCAT_TAG, "onPause() called");

    }

    // Callback received when a new city name is entered on the second screen.
    // Checking request code and if result is OK before making the API call.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOGCAT_TAG, "onActivityResult() called");
        if (requestCode == NEW_CITY_CODE) {
            if (resultCode == RESULT_OK) {
                City = data.getStringExtra("City");
                background = data.getStringExtra("background");
                temperatureType = data.getStringExtra("temp");

                Log.d(LOGCAT_TAG, "onActivityResult City " + City);
                Log.d(LOGCAT_TAG, "onActivityResult background " + background);
                Log.d(LOGCAT_TAG, "onActivityResult temperatureType " + temperatureType);

                firstCall = "false";
                getWeatherForNewCity(City);
            }
        }
    }

    // Configuring the parameters when a new city has been entered:
    private void getWeatherForNewCity(String city) {
        Log.d(LOGCAT_TAG, "Getting weather for new city");
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", APP_ID);
        letsDoSomeNetworking(params);
    }

    // This is the actual networking code. Parameters are already configured.
    private void letsDoSomeNetworking(RequestParams params) {

        // AsyncHttpClient belongs to the loopj dependency.
        AsyncHttpClient client = new AsyncHttpClient();

        // Making an HTTP GET request by providing a URL and the parameters.
        String weatherURL = WEATHER_URL+"weather";
        client.get(weatherURL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(LOGCAT_TAG, "Success! JSON: " + response.toString());
                WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
                updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.e(LOGCAT_TAG, "Fail " + e.toString());
                Toast.makeText(WeatherController.this, "Request Failed", Toast.LENGTH_SHORT).show();

                Log.d(LOGCAT_TAG, "Status code " + statusCode);
                Log.d(LOGCAT_TAG, "Here's what we got instead " + response.toString());
            }

        });
    }

    /// EDIT!!! for update API
    private void forecastWeather(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();

        String forecastURL = WEATHER_URL+"forecast/daily";
        // Making an HTTP GET request by providing a URL and the parameters.
        client.get(forecastURL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(LOGCAT_TAG, "Success! JSON Forecast: " + response.toString());
                ForecastModel weatherData = ForecastModel.fromJson(response, forecastDay);
                updateForecast(weatherData);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.e(LOGCAT_TAG, "Fail " + e.toString());
                Toast.makeText(WeatherController.this, "Forecast Request Failed", Toast.LENGTH_SHORT).show();

                Log.d(LOGCAT_TAG, "Status code " + statusCode);
                Log.d(LOGCAT_TAG, "Here's what we got instead " + response.toString());
            }
        });
    }

    private void updateForecast(ForecastModel weather){
        Intent newForecastIntent = new Intent(WeatherController.this, WeatherForecast.class);
        newForecastIntent.putExtra(Forecast, weather);
        newForecastIntent.putExtra("city", City);
        newForecastIntent.putExtra("tempType", temperatureType);
        newForecastIntent.putExtra("background", background);
//        startActivity(newForecastIntent);
        startActivityForResult(newForecastIntent, NEW_CITY_CODE);
    }


    // Updates the information shown on screen.
    private void updateUI(WeatherDataModel weather) {
        //Jie Lan-this part is swich between °C and °F
        Log.d("Clima", "UpdateUI temperatureType  "+temperatureType);
        Log.d(LOGCAT_TAG, "UpdateUI background " + background);
        if(temperatureType.equals("Centigrade"))
            mTemperatureLabel.setText(Integer.parseInt(weather.getTemperature())+"°C");
        else
            mTemperatureLabel.setText(Integer.parseInt(weather.getTemperature())*9/5+32+"°F");

        if (background.equals("T"))
            weatherControllerLayout.setBackgroundResource((R.drawable.birdsbackground));

        mCityLabel.setText(weather.getCity());
        //pressure and humidity
        mHumidity.setText(weather.getHumidity());
        mPressure.setText(weather.getVisibility());

        // Update the icon based on the resource id of the image in the drawable folder.
        int resourceID = getResources().getIdentifier(weather.getIconName(), "drawable", getPackageName());
        mWeatherImage.setImageResource(resourceID);
    }





}











