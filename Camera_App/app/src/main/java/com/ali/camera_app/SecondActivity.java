    package com.ali.camera_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

    public class SecondActivity extends AppCompatActivity {
    public static TextView resultTextView;
    Button buttonBarCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        resultTextView = findViewById(R.id.resultTextView);
        buttonBarCode = findViewById(R.id.barCodeButton);
        buttonBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,Main2Activity.class));
            }
        });
    }
}
