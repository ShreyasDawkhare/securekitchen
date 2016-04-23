package com.securekitchen.shreyas.mysecurekitchen;

import android.os.Handler;
import org.json.JSONException;
import org.json.JSONObject;

public class BackgroundSignOutTask implements Runnable
{
    private Thread t;
    private String devicetoken;
    private Status objStatus;
    private Handler handler;

    BackgroundSignOutTask( String devicetoken,Status objStatus)
    {
        this.objStatus = objStatus;
        this.devicetoken = devicetoken;
        handler = new Handler(); // <--- is this on main UI thread?
    }
    public void run()
    {
        int rowsAffected = 0;
        try
        {
            objStatus.isSignoutSuccess = false;
            SecureKitchenClient objSecureKitchenClient = new SecureKitchenClient();
            SecureKitchenClient.response = "~";
            objSecureKitchenClient.execute("3",devicetoken); // 3: signout

            while(SecureKitchenClient.response.equals("~"))
            {
                System.out.println("Waiting...");
                Thread.sleep(100);
            }
            if(!SecureKitchenClient.response.equals("error"))
            {
                JSONObject objJSONObject;
                try {
                    objJSONObject = new JSONObject(SecureKitchenClient.response);
                    System.out.println(SecureKitchenClient.response);
                    if(!((boolean)objJSONObject.get("error")))
                    {
                        rowsAffected = (int) objJSONObject.get("rowsAffected");

                        if (rowsAffected > 0) {
                            objStatus.isSignoutSuccess = true;
                        }
                    }
                    else
                    {
                        objStatus.isSignoutSuccess = false;
                        System.out.println("Error Occured!!!");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(objStatus != null) objStatus.onBackgroundtaskCompleted();
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
