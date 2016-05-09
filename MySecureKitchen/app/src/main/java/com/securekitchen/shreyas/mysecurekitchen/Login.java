package com.securekitchen.shreyas.mysecurekitchen;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Login extends AppCompatActivity
{
    private EditText productCode;
    private EditText password;
    private Button signIn;
    private Button addAccount;
    private String pc;
    private String pwd;
    private String deviceToken;
    boolean isValidUser = false;

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        productCode = (EditText) findViewById(R.id.productCode);
        password = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.btn_sign_in);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Registration Success
                    String token = intent.getStringExtra("token");
                    deviceToken = token;
                    //Toast.makeText(getApplicationContext(),"GCM token : " + token,Toast.LENGTH_LONG).show();
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    // Registration Error
                    //Toast.makeText(getApplicationContext(),"GCM registration error!!!",Toast.LENGTH_LONG).show();
                } else {

                }
            }
        };

        // Check status of Google play service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode)
        {
            // Check type of error
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                Toast.makeText(getApplicationContext(),"Google play service i not installed or enabled in this device!",Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode,getApplicationContext());
            }
            else
            {
                Toast.makeText(getApplicationContext(),"This device does not support for Google Play Service!",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            // Start Service
            Intent intent = new Intent(this, GCMRegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.v("Login", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.v("Login", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    public void onSignIn(View view) {
        System.out.println("Sign in Clicked...");

        //Get product code and password
        pc = productCode.getText().toString();
        pwd = password.getText().toString();

        Log.d("ProductInfo","Product Code: "+productCode+", Password:"+pwd);
        //Validate credentials
        SaveSharedPreference.setDeviceToken(this, deviceToken);
        BackgroundLoginTask loginTask = new BackgroundLoginTask(pc,pwd,deviceToken,this);
        loginTask.start();


    }

    public void onBackgroundtaskCompleted()
    {
        Log.d("Login","Is Valid User:" + isValidUser);
        //If valid user send product code to Status activity and load it
        if (isValidUser == true)
        {
            SaveSharedPreference.setUserName(this,pc);
            navigtateToStatusPage();
        }
        else //Else give error message to user and stay on Login activity
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Opps!!");
            alertDialog.setMessage("Product code and/or Password is invalid");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Clear input
        productCode.setText("");
        password.setText("");
    }

    public void navigtateToStatusPage() {
        Intent objIntent = new Intent(this,Status.class);
        startActivity(objIntent);
        finish();
    }
}
