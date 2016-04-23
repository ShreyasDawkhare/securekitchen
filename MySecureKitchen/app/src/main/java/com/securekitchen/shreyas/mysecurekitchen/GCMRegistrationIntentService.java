package com.securekitchen.shreyas.mysecurekitchen;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

public class GCMRegistrationIntentService extends IntentService
{
    public static final String REGISTRATION_SUCCESS ="RegistrationSuccess";
    public static final String REGISTRATION_ERROR ="RegistrationError";

    public GCMRegistrationIntentService() {
        super("");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        registerGSM();
    }

    private void registerGSM()
    {
        Intent registrationComplete = null;
        String token = null;
        try
        {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.v("GCMRegistrationService", "token : " + token);
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token",token);

        } catch(Exception e) {
            Log.v("GCMRegistrationService","Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
