//package com.londonappbrewery.clima_completed;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import android.widget.Button;
//import android.widget.RelativeLayout;
//import android.graphics.drawable.Drawable;
//
//public class ChangeCityController extends AppCompatActivity {
//    RadioGroup radioGroup;
//    RadioButton radioButton;
//    RadioButton radioButton1;
//    RadioButton radioButton2;
//
//    String tempType;
//    String newCity;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.change_city_layout);
//
//        RadioGroup radiogroup;
//
//        radiogroup=(RadioGroup)findViewById(R.id.radioGroup);
//
//        Intent myIntent = getIntent();
//        tempType = myIntent.getStringExtra("temperature");
//        newCity = myIntent.getStringExtra("City");
//
//        if(tempType.equals("Centigrade"))
//            radiogroup.check(R.id.radioButton);
//        else
//            radiogroup.check(R.id.radioButton2);
//
//        final RelativeLayout rl1 = findViewById(R.id.relativelayout1);
//        final RelativeLayout rl2 = findViewById(R.id.relativelayout2);
//
//        Button button = findViewById(R.id.button);
//
//        final EditText editTextField = findViewById(R.id.queryET);
//        ImageButton backButton = findViewById(R.id.backButton);
//
//        radioGroup = findViewById(R.id.radioGroup);
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Go back and destroy the ChangeCityController
//                finish();
//            }
//        });
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                rl1.setBackgroundColor(Color.RED);
//                //rl2.setBackgroundColor(Color.RED);
//            }
//        });
//        // Buttons can have a listener for clicks.
//        // EditTexts can have listeners for keyboard presses like hitting the enter key.
//        editTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                Intent myIntent = getIntent();
//                String newCity = editTextField.getText().toString();
//
//                Intent newCityIntent = new Intent(ChangeCityController.this, WeatherController.class);
//
//                // Adds what was entered in the EditText as an extra to the intent.
//                newCityIntent.putExtra("City", newCity);
//                newCityIntent.putExtra("temp", tempType);
//
//                // We started this activity for a result, so now we are setting the result.
////                setResult(Activity.RESULT_OK, newCityIntent);
//                startActivity(newCityIntent);
//
//                // This destroys the ChangeCityController.
//                finish();
//                return true;
//            }
//        });
//
//    }
//
//    //Jie Lan click event for the radio button
//    public void radioClick(View v){
//        int radioBUttionid = radioGroup.getCheckedRadioButtonId();
//        radioButton = findViewById(radioBUttionid);
//        Intent intent = new Intent(this, WeatherController.class);
//        intent.putExtra("temp", radioButton.getText());
//
//        intent.putExtra("City", newCity);
//        startActivity(intent);
//    }
//}
package com.londonappbrewery.clima_completed;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;


public class ChangeCityController extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    RelativeLayout changeCityLayout;
    ImageButton backButton;

    String tempType;
    String newCity;
    String changeBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city_layout);

//        RadioGroup radiogroup;
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        changeCityLayout = findViewById(R.id.changeCityLayout);

        // Information from main page
        Intent myIntent = getIntent();
        tempType = myIntent.getStringExtra("temperature");
        newCity = myIntent.getStringExtra("City");
        changeBackground = myIntent.getStringExtra("background");

        if (changeBackground.equals("T"))
            changeCityLayout.setBackgroundResource((R.drawable.birdsbackground));
        // Set click on radioButton
        if(tempType.equals("Centigrade"))
            radioGroup.check(R.id.radioButton);
        else
            radioGroup.check(R.id.radioButton2);


//        final RelativeLayout rl2 = findViewById(R.id.relativelayout2);


//        radioGroup = findViewById(R.id.radioGroup);


        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back and destroy the ChangeCityController
                Intent newCityIntent = new Intent(ChangeCityController.this, WeatherController.class);

                // Adds what was entered in the EditText as an extra to the intent.
                newCityIntent.putExtra("City", newCity);
                newCityIntent.putExtra("temp", tempType);
                newCityIntent.putExtra("background", changeBackground);
                // We started this activity for a result, so now we are setting the result.
                Log.d("Clima", "radioButton getText "+tempType);
                Log.d("Clima", "changeBackground: "+changeBackground);

                setResult(Activity.RESULT_OK, newCityIntent);

//                startActivity(newCityIntent);
                finish();
            }
        });

        Button backgroundButton = findViewById(R.id.backgroundButton);
        backgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int resourceID = getResources().getIdentifier("birdsbackground", "drawable", getPackageName());
//                mWeatherImage.setImageResource(resourceID);

                changeBackground = "T";
                changeCityLayout.setBackgroundResource((R.drawable.birdsbackground));
                //rl2.setBackgroundColor(Color.RED);
            }
        });


        final EditText editTextField = findViewById(R.id.queryET);
        // EditTexts can have listeners for keyboard presses like hitting the enter key.
        editTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                newCity = editTextField.getText().toString();
                backButton.callOnClick();
                return true;
            }
        });

    }

    //Jie Lan click event for the radio button
    public void radioClick(View v){
        int radioButtonid = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioButtonid);
        tempType = (String)radioButton.getText();
    }
}
