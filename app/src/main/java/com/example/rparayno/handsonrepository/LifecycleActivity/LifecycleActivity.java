package com.example.rparayno.handsonrepository.LifecycleActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

import com.example.rparayno.handsonrepository.R;

import java.util.Calendar;
import java.util.Locale;

public class LifecycleActivity extends AppCompatActivity {

    // counter vars
    static int onCreateCTR;
    static int onStartCTR;
    static int onResumeCTR;
    static int onPauseCTR;
    static int onStopCTR;
    static int onDestroyCTR;

    // counter textviews
    private TextView onCreateTV;
    private TextView onStartTV;
    private TextView onResumeTV;
    private TextView onPauseTV;
    private TextView onStopTV;
    private TextView onDestroyTV;


    // timestamp textviews
    private TextView onCreateTVTime;
    private TextView onStartTVTime;
    private TextView onResumeTVTime;
    private TextView onPauseTVTime;
    private TextView onStopTVTime;
    private TextView onDestroyTVTime;

    private String timestampDF;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("onCreateCTR", onCreateCTR);
        outState.putInt("onStartCTR", onStartCTR);
        outState.putInt("onResumeCTR", onResumeCTR);
        outState.putInt("onPauseCTR", onPauseCTR);
        outState.putInt("onStopCTR", onStopCTR);
        outState.putInt("onDestroyCTR", onDestroyCTR);

        outState.putString("onCreateTV", (String) onCreateTV.getText());
        outState.putString("onStartTV", (String) onStartTV.getText());
        outState.putString("onResumeTV", (String) onResumeTV.getText());
        outState.putString("onPauseTV", (String) onPauseTV.getText());
        outState.putString("onStopTV", (String) onStopTV.getText());
        outState.putString("onDestroyTV", (String) onDestroyTV.getText());

        outState.putString("onCreateTVTime", (String) onCreateTVTime.getText());
        outState.putString("onStartTVTime", (String) onStartTVTime.getText());
        outState.putString("onResumeTVTime", (String) onResumeTVTime.getText());
        outState.putString("onPauseTVTime", (String) onPauseTVTime.getText());
        outState.putString("onStopTVTime", (String) onStopTVTime.getText());
        outState.putString("onDestroyTVTime", (String) onDestroyTVTime.getText());

        outState.putString("timestampDF", timestampDF);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);

        // initialize vars
        onCreateTV = (TextView) findViewById(R.id.onCreateTimesCalled);
        onStartTV = (TextView) findViewById(R.id.onStartTimesCalled);
        onResumeTV = (TextView) findViewById(R.id.onResumeTimesCalled);
        onPauseTV = (TextView) findViewById(R.id.onPauseTimesCalled);
        onStopTV = (TextView) findViewById(R.id.onStopTimesCalled);
        onDestroyTV = (TextView) findViewById(R.id.onDestroyTimesCalled);

        onCreateTVTime = (TextView) findViewById(R.id.onCreateTime);
        onStartTVTime = (TextView) findViewById(R.id.onStartTime);
        onResumeTVTime = (TextView) findViewById(R.id.onResumeTime);
        onPauseTVTime = (TextView) findViewById(R.id.onPauseTime);
        onStopTVTime = (TextView) findViewById(R.id.onStopTime);
        onDestroyTVTime = (TextView) findViewById(R.id.onDestroyTime);


        onCreateCTR += 1;
        onCreateTV.setText(Integer.toString(onCreateCTR));

        Long timestampLong = System.currentTimeMillis()/1000;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestampLong * 1000L);
        String timestampDF = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        onCreateTVTime = findViewById(R.id.onCreateTime);
        onCreateTVTime.setText(timestampDF);

        Log.d("STATUS", "onCreate Pass");



        if (savedInstanceState != null) {
            this.onCreateCTR = savedInstanceState.getInt("onCreateCTR");
            this.onStartCTR = savedInstanceState.getInt("onStartCTR");
            this.onResumeCTR = savedInstanceState.getInt("onResumeCTR");
            this.onPauseCTR = savedInstanceState.getInt("onPauseCTR");
            this.onStopCTR = savedInstanceState.getInt("onStopCTR");
            this.onDestroyCTR = savedInstanceState.getInt("onDestroyCTR");

            this.onCreateTV.setText(Integer.toString(onCreateCTR) + "");
            this.onStartTV.setText(Integer.toString(onStartCTR) + "");
            this.onResumeTV.setText(Integer.toString(onResumeCTR) + "");
            this.onPauseTV.setText(Integer.toString(onPauseCTR) + "");
            this.onStopTV.setText(Integer.toString(onStopCTR) + "");
            this.onDestroyTV.setText(Integer.toString(onDestroyCTR) + "");

            this.onCreateTVTime.setText(timestampDF + "");
            this.onStartTVTime.setText(timestampDF + "");
            this.onResumeTVTime.setText(timestampDF + "");
            this.onPauseTVTime.setText(timestampDF + "");
            this.onStopTVTime.setText(timestampDF + "");
            this.onDestroyTVTime.setText(timestampDF + "");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        onStartCTR += 1;
        onStartTV.setText(Integer.toString(onStartCTR));

        Long timestampLong = System.currentTimeMillis()/1000;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestampLong * 1000L);
        String timestampDF = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        onStartTVTime.setText(timestampDF);

        Log.d("STATUS", "onStart Pass");

    }

    @Override
    public void onResume() {
        onResumeCTR += 1;
        onResumeTV.setText(Integer.toString(onResumeCTR));

        Long timestampLong = System.currentTimeMillis()/1000;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestampLong * 1000L);
        String timestampDF = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        onResumeTVTime.setText(timestampDF);

        Log.d("STATUS", "onResume Pass");

        super.onResume();
    }

    @Override
    public void onPause() {
        onPauseCTR += 1;
        onPauseTV.setText(Integer.toString(onPauseCTR));

        Long timestampLong = System.currentTimeMillis()/1000;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestampLong * 1000L);
        String timestampDF = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        onPauseTVTime.setText(timestampDF);

        Log.d("STATUS", "onPause Pass");

        super.onPause();
    }

    @Override
    public void onStop() {
        onStopCTR += 1;
        onStopTV.setText(Integer.toString(onStopCTR));

        Long timestampLong = System.currentTimeMillis()/1000;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestampLong * 1000L);
        String timestampDF = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        onStopTVTime.setText(timestampDF);

        Log.d("STATUS", "onStop Pass");

        super.onStop();
    }

    @Override
    public void onDestroy() {
        onDestroyCTR += 1;
        onDestroyTV.setText(Integer.toString(onDestroyCTR));

        Long timestampLong = System.currentTimeMillis()/1000;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestampLong * 1000L);
        String timestampDF = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        onDestroyTVTime.setText(timestampDF);

        Log.d("STATUS", "onDestroy Pass");

        super.onDestroy();
    }


}
