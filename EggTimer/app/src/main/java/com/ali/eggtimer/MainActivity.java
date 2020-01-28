package com.ali.eggtimer;

import androidx.appcompat.app.AppCompatActivity;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SeekBar timerSeekbar;
    int currentSeconds = 30;
    TextView countdownText;
    MediaPlayer mediaPlayer;
    Button goButton;
    Button stopButton;
    CountDownTimer countDownTimer;
    public void controlTimer(final View view){
        timerSeekbar.setEnabled(false);
        Log.i("Check","Hello");
        countDownTimer = new CountDownTimer(currentSeconds*1000, 1000){
            public void onTick(long millisecondsUntilDone){
                stopButton.setVisibility(View.VISIBLE);
                goButton.setVisibility(View.INVISIBLE);
                currentSeconds--;
                int minutes =(int) (millisecondsUntilDone/1000)/ 60;
                int seconds = (int) (millisecondsUntilDone/1000) - minutes * 60;
                countdownText.setText(String.format("%02d", minutes)+":"+String.format("%02d", seconds));
            }
            public void onFinish(){
                timerSeekbar.setEnabled(true);
                goButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                mediaPlayer.start();
            }
        }.start();
    }
    public void stopCountdown(View view){
        timerSeekbar.setEnabled(true);
        goButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        countDownTimer.cancel();
        timerSeekbar.setProgress(currentSeconds);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goButton = findViewById(R.id.goButton);
        stopButton = findViewById(R.id.stopButton);
        mediaPlayer = MediaPlayer.create(this,R.raw.airhorn);
        timerSeekbar = findViewById(R.id.timerSeekBar);
        timerSeekbar.setMax(600);
        timerSeekbar.setProgress(30);
        countdownText = findViewById(R.id.countdownText);
        timerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentSeconds = progress;
                int minutes =(int) progress / 60;
                int seconds = (int) progress - minutes * 60;
                countdownText.setText(String.format("%02d", minutes)+":"+String.format("%02d", seconds));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}