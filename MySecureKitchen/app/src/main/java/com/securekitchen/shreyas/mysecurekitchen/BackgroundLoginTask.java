package com.securekitchen.shreyas.mysecurekitchen;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class BackgroundLoginTask implements Runnable
{
    private Thread t;
    private String pc;
    private String pwd;
    private String devicetoken;
    private Login objLogin;
    private Handler handler;

    BackgroundLoginTask( String pc,String pwd,String devicetoken,Login objLogin)
    {
        this.pc= pc;
        this.pwd = pwd;
        this.objLogin = objLogin;
        this.devicetoken = devicetoken;
        handler = new Handler(); // <--- is this on main UI thread?
    }
    public void run()
    {
        boolean isValidUser = false;
        try
        {
            SecureKitchenClient objSecureKitchenClient = new SecureKitchenClient();
            SecureKitchenClient.response = "~";
            objSecureKitchenClient.execute("1",pc, pwd, devicetoken); // 1: authenticate

            while(SecureKitchenClient.response.equals("~"))
            {
                Log.d("Login","Waiting...");
                Thread.sleep(100);
            }

            JSONObject objJSONObject;
            try
            {
                objJSONObject = new JSONObject(SecureKitchenClient.response);
                Log.d("Login",SecureKitchenClient.response);
                isValidUser = (boolean)objJSONObject.get("authentication");
                objLogin.isValidUser = isValidUser;

            } catch (JSONException e) {
                Log.e("Login",e.getMessage());
            }

        } catch (Exception e) {
           Log.e("Login",e.getMessage());

        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(objLogin != null) objLogin.onBackgroundtaskCompleted();
            }
        });
    }


    public void start ()
    {
        if (t == null)
        {
            t = new Thread(this);
            t.start ();
        }
    }

}
