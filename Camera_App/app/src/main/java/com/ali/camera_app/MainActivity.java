package com.ali.camera_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private CameraCaptureSession myCameraCaptureSession;
    private String myCameraID;
    Button button;
    private CameraManager myCameraManager;
    private CameraDevice myCameraDevice;
    private TextureView myTextureView;
    private CaptureRequest.Builder myCaptureRequestBuilder;
    private CameraDevice.StateCallback myStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            myCameraDevice = camera;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            myCameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            myCameraDevice.close();
            myCameraDevice = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                myCameraDevice.close();
            }
        });
        myTextureView = findViewById(R.id.textureView);
        myCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        openCamera();
        //myCameraDevice.close();
    }

    @Override
    protected void onRestart() {
        openCamera();
        super.onRestart();
    }

    private void openCamera() {
        try {
            myCameraID = myCameraManager.getCameraIdList()[1];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                requestPermissions( new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            myCameraManager.openCamera(String.valueOf(myCameraID), myStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void cameraPreview(View view){
        SurfaceTexture mySurfaceTexture = myTextureView.getSurfaceTexture();
        Surface mySurface = new Surface(mySurfaceTexture);
        try {
            myCaptureRequestBuilder = myCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            myCaptureRequestBuilder.addTarget(mySurface);

                myCameraDevice.createCaptureSession(Arrays.asList(mySurface), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        myCameraCaptureSession = session;
                        myCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                        try {
                            myCameraCaptureSession.setRepeatingRequest(myCaptureRequestBuilder.build(), null,null);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                },null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}