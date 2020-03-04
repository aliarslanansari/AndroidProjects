package com.ali.camera_test;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sendSMS("+91 9423424553","Hello");
        //sendLongSMS();
        sendSmsMsgFnc("9423424553","HELLO WORLD");
    }

    /*public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }*/
    public void sendLongSMS() {
        String phoneNumber = "9423424553";
        String message = "Hello World! Now we are going to demonstrate " +
                "how to send a message with more than 160 characters from your Android application.";
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
    }
    void sendSmsMsgFnc(String mblNumVar, String smsMsgVar)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
            {
                try
                {
                    SmsManager smsMgrVar = SmsManager.getDefault();
                    smsMgrVar.sendTextMessage(mblNumVar, null, smsMsgVar, null, null);
                    Toast.makeText(getApplicationContext(), "Message Sent",
                            Toast.LENGTH_LONG).show();
                }
                catch (Exception ErrVar)
                {
                    Toast.makeText(getApplicationContext(),ErrVar.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ErrVar.printStackTrace();
                }
            }
            else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
                }
            }
        }

    }
}