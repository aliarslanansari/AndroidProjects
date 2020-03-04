package com.ali.pinksafety;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class MyBackgroundService extends Service {
    public MyBackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //  Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //for(int i = 0 ; i < 1000; i++){
          //  Toast.makeText(this, String.valueOf(i), Toast.LENGTH_SHORT).show();
        //}

        new CountDownTimer(10000, 1000){
            public void onTick(long millisecondsUntilDone){
                Toast.makeText(MyBackgroundService.this, String.valueOf(millisecondsUntilDone/1000), Toast.LENGTH_SHORT).show();
                }
            public void onFinish(){
                Toast.makeText(MyBackgroundService.this, "Hogaya", Toast.LENGTH_SHORT).show();
            }
        }.start();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
