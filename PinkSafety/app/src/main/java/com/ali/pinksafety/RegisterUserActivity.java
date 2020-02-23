package com.ali.pinksafety;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class RegisterUserActivity extends AppCompatActivity {

    EditText fullname, username, email, genderText,cnfPassword,password, contactno, emergency1,emergency2 ,emergency3;
    String gender;
    TextView genderTextView;
    FaceFeatureClass faceFeatureClass;
    Button FinalRegisterButton;
    Intent takePic;
    ProgressDialog progressDialog;
    SessionManager session;
    String pathToFile;
    private final FaceServiceClient faceServiceClient = new FaceServiceRestClient("https://pooja-face.cognitiveservices.azure.com/face/v1.0", "0074d87571ff426c86a09748906e2513");
    private String uid;
    private String mob;
    private String pswd;

    public class FaceFeatureClass extends AsyncTask<Bitmap, String, Face[]> {
        private ProgressDialog dialog;
        public FaceFeatureClass() {
            dialog = new ProgressDialog(RegisterUserActivity.this);
        }

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
            dialog.setMessage("Detecting Gender, please wait.");
            dialog.show();
        }
        @Override
        protected void onProgressUpdate(String... progress) {
            //TODO: update progress
            // detectionProgressDialog.setMessage(progress[0]);
        }
        @Override
        protected void onPostExecute(Face[] result) {
            //TODO: update face frames
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
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
            if(!gender.equals("female")){
                notFemale();
            }
            else{
                genderText.setText("Female");
            }
            Toast.makeText(RegisterUserActivity.this, "Detected: "+gender, Toast.LENGTH_SHORT).show();
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
    private void notFemale() {
        new AlertDialog.Builder(this)
                .setTitle("Not Female")
                .setMessage("Only female user can register on this application")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        genderTextView = findViewById(R.id.alreadyAccText);
        genderTextView.setPaintFlags(genderTextView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        FinalRegisterButton = findViewById(R.id.registerButton);
        fullname = findViewById(R.id.nameEditText);
        username = findViewById(R.id.usernameEditText);
        email = findViewById(R.id.emaiEditText);
        password = findViewById(R.id.passwordEditText);
        session = new SessionManager(RegisterUserActivity.this);
        cnfPassword = findViewById(R.id.confirmPasswordEditText);
        contactno = findViewById(R.id.contactNoEditText);
        emergency1 = findViewById(R.id.emergencyEditText1);
        emergency2 = findViewById(R.id.emergencyEditText2);
        emergency3 = findViewById(R.id.emergencyEditText3);
        genderText = findViewById(R.id.genderEditText);
        genderText.setEnabled(false);
        faceFeatureClass = new FaceFeatureClass();
        takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        dispatchPictureTakerAction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},2);
        }
        FinalRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullname.getText().toString().isEmpty() || username.getText().toString().isEmpty() ||
                        email.getText().toString().isEmpty() || password.getText().toString().isEmpty() ||
                        cnfPassword.getText().toString().isEmpty() || contactno.getText().toString().isEmpty()
                        || emergency1.getText().toString().isEmpty() || emergency2.getText().toString().isEmpty()
                        || emergency3.getText().toString().isEmpty()) {

                    Toast.makeText(RegisterUserActivity.this, " Please fill all the fields!", Toast.LENGTH_SHORT).show();

                } else {
                    if(password.getText().toString().equals(cnfPassword.getText().toString())){
                        mob = username.getText().toString();
                        pswd = password.getText().toString();
                        RegisterUser();
                    }else{
                        Toast.makeText(RegisterUserActivity.this, "Password didn't match, Please re-enter!", Toast.LENGTH_SHORT).show();
                        password.setText("");
                        cnfPassword.setText("");
                    }

                }
            }
        });
        genderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("checkAct","onResume--");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("checkAct","onRestart--");
    }

    private void RegisterUser(){

        progressDialog = new ProgressDialog(RegisterUserActivity.this);
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String url = "https://ali-arslan-ansari.000webhostapp.com/register_user.php";
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterUserActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Hiding the progress dialog after all task complete.
                progressDialog.dismiss();
                try {
                    JSONObject j = new JSONObject(response);
                    String result = j.getString("result");
                    if (result.trim().equals("Success")) {
                        uid = j.getString("id");
                        session.createLoginSession(mob, pswd);
                        Toast.makeText(RegisterUserActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                        MainActivity.uid = uid;
                        Intent i=new Intent(RegisterUserActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterUserActivity.this);
                        alert.setTitle("Invalid Credentials");
                        alert.setMessage(result);
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterUserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                NetworkDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", mob);
                params.put("fullname", fullname.getText().toString().trim());
                params.put("contactno", contactno.getText().toString().trim());
                params.put("email", email.getText().toString().trim());
                params.put("password", pswd);
                params.put("emergency1", emergency1.getText().toString().trim());
                params.put("emergency2", emergency2.getText().toString().trim());
                params.put("emergency3", emergency3.getText().toString().trim());
                params.put("gender", gender);
                Log.i("Params","Paramss");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(RegisterUserActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                RegisterUser();
            }
        });
        dialogs.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                faceFeatureClass.execute(bitmap);
                Log.i("","");
                System.out.println(pathToFile);
            }
        }
    }
    private void dispatchPictureTakerAction() {
        if(takePic.resolveActivity(getPackageManager()) != null){
            File photoFile;
            photoFile =  createPhotoFile();
            if(photoFile != null){
                pathToFile = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(RegisterUserActivity.this,"android.support.v4.content.FileProvider1", photoFile);
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