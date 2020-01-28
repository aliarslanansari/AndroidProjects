package com.ali.timerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.timesTextView);
        new CountDownTimer(10000,1000){
            public void onTick(long millisecondsUntilDone){
                textView.setText(String.valueOf(millisecondsUntilDone/1000));
                Log.i("Seconds Left:", String.valueOf(millisecondsUntilDone/1000));
            }
            public void onFinish(){
                textView.setText(new String("Countdown Over"));
                Log.i("Done!", "Countdown Over");
            }
        }.start();

/*
        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            int i=0;

            @Override
            public void run() {
                Log.i("Runnable has Run", Integer.toString(++i));
                textView.setText(Integer.toString(i));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(run);*/
    }
}
