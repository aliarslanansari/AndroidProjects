package com.ali.demoapppp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {
    String gender;
    TextView genderTextView;
    Button buttonTakePicture;
    String pathToFile;
    ImageView image;
    private final FaceServiceClient faceServiceClient = new FaceServiceRestClient("https://pooja-face.cognitiveservices.azure.com/face/v1.0", "0074d87571ff426c86a09748906e2513");

    public class FaceFeatureClass extends AsyncTask<Bitmap, String, Face[]>
    {
        String exceptionMessage="";
        @Override
        protected Face[] doInBackground(Bitmap... bitmaps) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 10, outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            try {
                //publishProgress("Detecting...");
                Face[] result = faceServiceClient.detect(
                        inputStream,
                        false,         // returnFaceId
                        false,        // returnFaceLandmarks
                        // returnFaceAttributes:
                        new FaceServiceClient.FaceAttributeType[] {
                                FaceServiceClient.FaceAttributeType.Age,
                                FaceServiceClient.FaceAttributeType.Gender }
                );
                if (result == null){
                    publishProgress(
                            "Detection Finished. Nothing detected");
                    return null;
                }
                publishProgress(String.format(
                        "Detection Finished. %d face(s) detected",
                        result.length));
                Log.i("ImageSent", "RESULT====");
                return result;
            } catch (Exception e) {
                exceptionMessage = String.format("Detection failed: %s", e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            //TODO: show progress dialog
            //detectionProgressDialog.show();
        }
        @Override
        protected void onProgressUpdate(String... progress) {
            //TODO: update progress
            // detectionProgressDialog.setMessage(progress[0]);
        }
        @Override
        protected void onPostExecute(Face[] result) {
            //TODO: update face frames
            //detectionProgressDialog.dismiss();
            if(result != null) {
                if (result.length >= 1) {
                    gender = String.valueOf(result[0].faceAttributes.gender);
                } else {
                    gender = "not found";
                }
            }
            else
            {
                gender = "Null Result";
            }
            Log.i("ImageFace", gender);
            Toast.makeText(MainActivity.this, gender, Toast.LENGTH_SHORT).show();
            genderTextView.setText(gender);
            if (!exceptionMessage.equals("")) {
                showError(exceptionMessage);
            }
            if (result == null) return;
        }

    }
    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }})
                .create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        genderTextView = findViewById(R.id.genderTextView);
        image = findViewById(R.id.image);
        buttonTakePicture = findViewById(R.id.buttonTakePicture);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},2);
        }
        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPictureTakerAction();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                image.setImageBitmap(bitmap);
                FaceFeatureClass faceFeatureClass = new FaceFeatureClass();
                faceFeatureClass.execute(bitmap);
                System.out.println(pathToFile);
            }
        }
    }
    private void dispatchPictureTakerAction() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            photoFile =  createPhotoFile();
            if(photoFile != null){
                pathToFile = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(MainActivity.this,"android.support.v4.content.FileProviderr", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePic, 1);
            }
        }
    }
    private File createPhotoFile() {
        File image1 = null;
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            image1 = File.createTempFile(name,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image1;
    }
}