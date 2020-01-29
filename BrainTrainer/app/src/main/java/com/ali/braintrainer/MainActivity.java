package com.ali.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    CountDownTimer countDownTimer;
    TextView sumTextView;
    TextView resultTextView;
    TextView timerTextView;
    TextView scoreTextView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button playAgain;
    GridLayout gridLayout;
    ConstraintLayout constraintLayout;
    int score=0;
    int noOfQues=0;
    int locationOfCorrectAns;
    ArrayList<Integer> answers = new ArrayList<Integer>();

    public void start(View view){
        playAgain(findViewById(R.id.playAgainButton));
        startButton.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(ConstraintLayout.VISIBLE);
    }
    public void chooseAnswer(View view){
        int tag= Integer.parseInt(view.getTag().toString());
        if(locationOfCorrectAns==tag){
            resultTextView.setText("Correct");
            score++;
        }else{
            resultTextView.setText("Wrong");
        }
        answers.clear();
        noOfQues++;
        scoreTextView.setText(Integer.toString(score)+"/"+Integer.toString(noOfQues));
        generateQuestions();
    }
    public void generateQuestions(){
        Random ranNum = new Random();
        int a = ranNum.nextInt(21);
        int b = ranNum.nextInt(21);

        sumTextView.setText(Integer.toString(a)+" + "+Integer.toString(b));
        locationOfCorrectAns = ranNum.nextInt(4);

        for(int i=0; i<4; i++){
            if(i == locationOfCorrectAns){
                answers.add(a+b);
            }
            else{
                int incorrectAns = ranNum.nextInt(41);
                while (incorrectAns== a + b ){
                    incorrectAns = ranNum.nextInt(41);
                }
                answers.add(incorrectAns);
            }
        }
        button1.setText(Integer.toString(answers.get(0)));
        button2.setText(Integer.toString(answers.get(1)));
        button3.setText(Integer.toString(answers.get(2)));
        button4.setText(Integer.toString(answers.get(3)));
    }
    public void playAgain(View view){
        generateQuestions();
        gridLayout.setVisibility(View.VISIBLE);
        scoreTextView.setText("0/0");
        score=0;
        noOfQues=0;
        resultTextView.setText("");
        timerTextView.setText("30s");
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished/1000)+"s");
            }
            @Override
            public void onFinish() {
                resultTextView.setText("Score: "+String.valueOf(score)+"/"+String.valueOf(noOfQues));
                playAgain.setVisibility(View.VISIBLE);
                gridLayout.setEnabled(false);
                this.cancel();
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton= findViewById(R.id.startButton);
        playAgain = findViewById(R.id.playAgainButton);
        sumTextView = findViewById(R.id.sumTextView);
        resultTextView = findViewById(R.id.resultTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        playAgain.setVisibility(View.INVISIBLE);
        constraintLayout = findViewById(R.id.constraintLayout);
        gridLayout = findViewById(R.id.gridLayout);
    }
}
