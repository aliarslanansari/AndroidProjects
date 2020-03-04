package com.ali.cameraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;

import android.content.pm.PackageManager;
//import android.graphics.Camera;
import android.os.Build;
import android.hardware.Camera;
import java.io.IOException;
//import java.lang.Object;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private static final String[] PERSMISSIONS = {
            Manifest.permission.CAMERA
    };
    private static final int REQUEST_PERMISSIONS =34;
    private static final int PERMISSIONS_COUNT = 1;
    private boolean arePermissionsDenied() {
        for (int i = 0; i < PERMISSIONS_COUNT; i++) {
            if (ContextCompat.checkSelfPermission(this, PERSMISSIONS[i])
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(arePermissionsDenied()){
            ((ActivityManager)(this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
            recreate();
        }else {
           // onResume();
        }
    }
    private boolean isCameraInitiated;
    private Camera mCamera = null;
    private static SurfaceHolder myHolder;
    private static CameraPreview mPreview;
    private FrameLayout preview;
    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionsDenied()){
            requestPermissions(PERSMISSIONS, REQUEST_PERMISSIONS);
            return;
        }
        if (!isCameraInitiated) {
            mPreview = new CameraPreview(this,mCamera);
            preview = findViewById(R.id.frameLaylout);
            preview.addView(mPreview);
        }
    }
    private static class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
        private SurfaceHolder mHolder;
        private static Camera mCamera;
        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            myHolder=holder;
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }
    private static int rotation;
    private void rotateCamera(){
        if(mCamera!=null){
            rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            if(rotation == 0){
                rotation = 90;
            }else if(rotation == 1){
                rotation = 0;
            }else if(rotation == 2){
                rotation = 270;
            }else{
                rotation = 180;
            }
        }
    }
}