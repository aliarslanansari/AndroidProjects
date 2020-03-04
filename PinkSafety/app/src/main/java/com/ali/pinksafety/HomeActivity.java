package com.ali.pinksafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    TextView textView;
    Button logout, emergencyButton;
    LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    //the client
    private FusedLocationProviderClient mFusedLocationClient;

    Location location;
    SessionManager sessionManager;
    private String locaa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        emergencyButton = findViewById(R.id.emergencyButton);
        sessionManager = new SessionManager(HomeActivity.this);
        logout = findViewById(R.id.logoutButton);
        //startService(new Intent(this, MyBackgroundService.class));
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS("8805751865","PLEASE HELP ME, I'M in DANGER, My Current Location is https://www.google.com/maps/place/21.217226,81.307392/@21.239698,81.303403,15z");
                sendSMS("8554027909","PLEASE HELP ME, I'M in DANGER, My Current Location is https://www.google.com/maps/place/21.217226,81.307392/@21.239698,81.303403,15z");
                sendSMS("7350898584","PLEASE HELP ME, I'M in DANGER, My Current Location is https://www.google.com/maps/place/21.217226,81.307392/@21.239698,81.303403,15z");
                sendSmsOnline();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.logoutUser();
            }
        });
        textView = findViewById(R.id.uidText);
        //textView.setText(MainActivity.uid);
        updateToken();
    }

    private void sendSmsOnline() {
        String url = "https://ali-arslan-ansari.000webhostapp.com/sms_send.php";
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //try {
//                    JSONObject j = new JSONObject(response);
//                    String result = j.getString("result");
                    Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();
                //} catch (JSONException e) {
                  //  e.printStackTrace();
                //}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("EMER1", String.valueOf("8805751865"));
                params.put("EMER2", String.valueOf("8554027909"));
                params.put("EMER3", String.valueOf("7350898584"));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void updateToken() {
        String url = "https://ali-arslan-ansari.000webhostapp.com/update_token.php";
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    String result = j.getString("result");
                    Toast.makeText(HomeActivity.this, result, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uid", MainActivity.uid);
                params.put("token", sessionManager.getToken());
                Log.i("Params111",sessionManager.getToken());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void sendSMS(String phoneNo, String msg) {
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
    }


    private boolean checkLocationPermission() {
        //check the location permissions and return true or false.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permissions granted
            Toast.makeText(getApplicationContext(), "permissions granted", Toast.LENGTH_LONG).show();
            return true;
        } else {
            //permissions NOT granted
            //if permissions are NOT granted, ask for permissions
            Toast.makeText(getApplicationContext(), "Please enable permissions", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permissions request")
                        .setMessage("we need your permission for location in order to show you this example")
                        .setPositiveButton("Ok, I agree", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    getLocation();
                } else {
                    // permission denied
                     locaa = "location: permission denied";

                    new AlertDialog.Builder(HomeActivity.this)
                            .setMessage("Cannot get the location!")
                            .setPositiveButton("OK", null)
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
        }
    }

    public void getLocation() {
        Toast.makeText(getApplicationContext(), "getLocation", Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //request the last location and add a listener to get the response. then update the UI.
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location.
                    if (location != null) {
                        locaa = "location: " + location.toString();
                    } else {
                        locaa = "location: IS NULL";
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "getLocation ERROR", Toast.LENGTH_LONG).show();
        }
    }

}