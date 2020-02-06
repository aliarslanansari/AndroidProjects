package com.ali.downloadingimages;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ImageView downloadedImage;
    public void downloadImage(View view) throws ExecutionException, InterruptedException {
        //https://sketchok.com/images/articles/01-cartoons/001-simpsons/20/14.jpg
        ImageDownloader imageDownloader = new ImageDownloader();
        Bitmap myBitmap;
        myBitmap = imageDownloader.execute("https://lh3.googleusercontent.com/-2G7jRn3ORWI/XJcrJG6bqFI/AAAAAAAAAJg/8QH2HFI5pyw0hBzFGn0xsF7lQQE5DnOogCEwYBhgL/w105-h140-p/IMG_20170102_141123.jpg").get();
                //("https://sketchok.com/images/articles/01-cartoons/001-simpsons/20/14.jpg").get();
        downloadedImage.setImageBitmap(myBitmap);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadedImage = findViewById(R.id.imageView);
    }
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings){

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
