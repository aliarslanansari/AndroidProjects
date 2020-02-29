package com.ali.pinksafety;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyBackgroundService extends Service {
    public MyBackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
