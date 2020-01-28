package com.ali.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button startButton;

    public void start(View view){
        startButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton= findViewById(R.id.startButton);
        TextView sumTextView = findViewById(R.id.sumTextView);
        Random ranNum = new Random();
        int a = ranNum.nextInt(21);
        int b = ranNum.nextInt(21);
        sumTextView.setText(Integer.toString(a)+" + "+Integer.toString(b));
    }
}
