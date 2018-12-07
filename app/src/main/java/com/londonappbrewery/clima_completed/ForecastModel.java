package com.londonappbrewery.clima_completed;

import android.util.Log;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ForecastModel implements Serializable {
    // Member variables that hold our relevant weather infromation.
    private ArrayList<String> mDate;
    private ArrayList<String> mCondition;
    private ArrayList<Integer> mTemperature;



    // Create a ForecastModel from a JSON, call this instead of the standard constructor.
    public static ForecastModel fromJson(JSONObject jsonObject, int forecastDay) {

        // JSON parsing is risky business. Need to surround the parsing code with a try-catch block.
        try {
            ForecastModel weatherData = new ForecastModel();

            // Initialize variables
            weatherData.mDate = new ArrayList<String>(forecastDay);
            weatherData.mCondition = new ArrayList<String>(forecastDay);
            weatherData.mTemperature = new ArrayList<Integer>(forecastDay);


            Log.d("Clima", "Inside ForecastModel");

            // temporary variable need
            Integer temp;
            int UNIXtimestamp;

            // Loop get data from API
            for(int i = 0; i<forecastDay; i++){

                // Get temperature data
                temp =  (int) Math.rint( jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("temp").getInt("day")- 273.15);
                weatherData.mTemperature.add(temp);

                // Get Date
                UNIXtimestamp=jsonObject.getJSONArray("list").getJSONObject(i).getInt("dt");
                Date time=new Date((long)UNIXtimestamp*1000);
                weatherData.mDate.add(String.format("%d/%d/%d",time.getMonth()+1,time.getDate(),time.getYear()+1900));
//                Log.d("Clima", "Inside ForecastModel Number Date1： "+weatherData.mDate.get(0));
                // Get Weather Condition
                weatherData.mCondition.add(jsonObject.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main"));
            }
            Log.d("Clima", "Number of Forecast Day： "+Integer.toString(forecastDay));

//            Log.d("Clima", "Inside ForecastModel Number Condition1： "+weatherData.mCondition.get(0));
//            Log.d("Clima", "Inside ForecastModel Number Date1： "+Integer.toString(weatherData.mDate.get(0)));
            return weatherData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getmDate() {
        return mDate;
    }

    public ArrayList<Integer> getmTemperature() {
        return mTemperature;
    }

    public ArrayList<String> getmCondition() {
        return mCondition;
    }

}
