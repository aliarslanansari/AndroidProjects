package com.ali.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebURLs = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    int chosenCeleb;
    ImageView imageView;
    int locationOfCorrectAns;
    String[] answers = new String[4];
    Button button0;
    Button button1;
    Button button2;
    Button button3;

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void chosenCeleb(View view){
        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAns))){
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Wrong! it was " + celebNames.get(chosenCeleb), Toast.LENGTH_SHORT).show();
        }
        createNewQuestion();
    }
    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            URL url;
            String result = "";
            HttpURLConnection connection = null;
            try{
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result+= current;
                    data = reader.read();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Failed1";
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed2";
            }
            return result;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button0 = findViewById(R.id.button);
        button1 = findViewById(R.id.button2);
        button2 = findViewById(R.id.button3);
        button3 = findViewById(R.id.button4);
        imageView=findViewById(R.id.imageView);
        DownloadTask downloadTask = new DownloadTask();
        String result = null;
        try {
            result = downloadTask.execute("http://www.posh24.se/kandisar").get();
            String[] splitResult = result.split("<div class=\"sidebarContainer\">");
            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m1 = p.matcher(splitResult[0]);
            while (m1.find()) {
                celebURLs.add(m1.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m1 = p.matcher(splitResult[0]);
            while (m1.find()) {
                celebNames.add(m1.group(1));
            }
            createNewQuestion();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createNewQuestion(){
        Random rand = new Random();
        chosenCeleb = rand.nextInt(celebURLs.size());
        ImageDownloader imageDownloader = new ImageDownloader();
        Bitmap celebImage = null;
        try {
            celebImage = imageDownloader.execute(celebURLs.get(chosenCeleb)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(celebImage);
        locationOfCorrectAns= rand.nextInt(4);
        int incorrectAnswerLocation;
        for(int i=0; i<4; i++){
            if(i==locationOfCorrectAns){
                answers[i]= celebNames.get(chosenCeleb)+"(Correct)";
            }
            else{
                incorrectAnswerLocation = rand.nextInt(celebNames.size());
                while(incorrectAnswerLocation==chosenCeleb){
                    incorrectAnswerLocation = rand.nextInt(celebNames.size());
                }
                answers[i]=celebNames.get(incorrectAnswerLocation);
            }
        }
        button0.setText(answers[0]);
        button1.setText(answers[1]);
        button2.setText(answers[2]);
        button3.setText(answers[3]);
    }
}
