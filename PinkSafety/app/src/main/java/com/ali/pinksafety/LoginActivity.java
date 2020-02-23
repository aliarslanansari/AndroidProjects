package com.ali.pinksafety;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.CertificateException;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button gotoRegisterButton;
    ProgressDialog progressDialog;
    EditText usernameEditText;
    EditText passwordEditText;
    SessionManager session;
    String uid;

    String mob, pswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);
        Log.i("IsLoggedin",String.valueOf(session.isLoggedIn()));

        loginButton = findViewById(R.id.loginButton);
        gotoRegisterButton = findViewById(R.id.goToRegisterButton);
        usernameEditText = findViewById(R.id.userNameLogin);
        passwordEditText = findViewById(R.id.passwordLogin);
        if (session.isLoggedIn() == true) {
            HashMap<String, String> roles = session.getUserDetails();
            mob = roles.get(SessionManager.KEY_EMAIL_admin);
            pswd = roles.get(SessionManager.KEY_PASSWORD_admin);
            Login();
            Log.i("IsLoggedIn","True");
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()) {

                    Toast.makeText(LoginActivity.this, " Please fill all the fields!", Toast.LENGTH_SHORT).show();

                } else {
                    mob = usernameEditText.getText().toString();
                    pswd = passwordEditText.getText().toString();
                    Login();
                }
            }
        });
        gotoRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("checkAct","onResumeLogin");
    }

    private void Login(){

        progressDialog = new ProgressDialog(LoginActivity.this);
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String url = "https://ali-arslan-ansari.000webhostapp.com/login_api.php";
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
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
                        Toast.makeText(LoginActivity.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                        MainActivity.uid = uid;
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                        alert.setTitle("Invalid Credentials");
                        alert.setMessage("You have Incorrect Username and password");
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
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                NetworkDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", mob);
                params.put("password", pswd);
                Log.i("Params","Paramss");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(LoginActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                Login();
            }
        });
        dialogs.show();
    }
}