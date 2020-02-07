package com.ali.json_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    public class DownloadTask extends AsyncTask<String, Void, String> {
        String result = "";
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data;
                data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result+=current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("MSG","FAILED1");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("MSG","FAILED2");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Website Contents", weatherInfo);
                JSONArray jsonArray = new JSONArray(weatherInfo);
                for(int i = 0; i< jsonArray.length(); i++){
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    Log.i("main", jsonPart.getString("main"));
                    Log.i("description", jsonPart.getString("description"));

                }
                textView.setText(weatherInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.TextView);
        DownloadTask task = new DownloadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q=mumbai&appid=31baf0a48cd1362c2f6823f90467638c");
    }
}