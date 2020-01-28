package com.ali.gameconnect3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    TextView textView;
    boolean isGameLive=true;
    int[] gameState = {2,2,2,2,2,2,2,2,2};
    int playState=1;
    int[][] winningState={{0,1,2}, {3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    public void getIn(View view){
        ImageView imageView = (ImageView) view;
        int imageTag = Integer.parseInt(imageView.getTag().toString());
        if(gameState[imageTag]==2 && isGameLive){
            imageView.setTranslationY(-1000f);
            if(playState==1){
                imageView.setImageResource(R.drawable.x_image);
                gameState[imageTag]=1;
                playState=0;
            }else{
                imageView.setImageResource(R.drawable.o_image);
                gameState[imageTag]=0;
                playState=1;
            }
            imageView.animate().translationYBy(1000f).rotation(360).setDuration(500);
            for(int[] winState : winningState ){
                 if(gameState[winState[0]]==gameState[winState[1]]&&
                         gameState[winState[1]]==gameState[winState[2]]){
                     if(gameState[winState[0]]==1){
                         linearLayout.setVisibility(View.VISIBLE);
                         Toast.makeText(this, "X is winner", Toast.LENGTH_SHORT).show();
                         textView.setText("X is Winner");
                         isGameLive=false;
                     }
                     else if(gameState[winState[0]]==0){
                         linearLayout.setVisibility(View.VISIBLE);
                         Toast.makeText(this, "O is winner", Toast.LENGTH_SHORT).show();
                         textView.setText("O is Winner");
                         isGameLive=false;
                     }
                 }
            }

        }
        int flag=0;
        for(int i = 0; i < gameState.length; i++){
            if(gameState[i]==2){
                flag=1;
            }
        }
        if(flag==0){
            linearLayout.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Draw", Toast.LENGTH_SHORT).show();
            textView.setText("It's a Draw");
            isGameLive=false;
        }

    }
    public void playAgainButton(View view){
        isGameLive=true;
        gameState = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2};
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for(int i=0; i < gridLayout.getChildCount(); i++){
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.setImageResource(0);
        }
        linearLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.linearLayout);
        textView = findViewById(R.id.winnerMessage);
    }
}