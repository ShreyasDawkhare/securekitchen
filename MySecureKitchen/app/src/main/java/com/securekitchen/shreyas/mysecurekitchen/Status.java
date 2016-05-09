package com.securekitchen.shreyas.mysecurekitchen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Status extends AppCompatActivity
{
    ArrayList<Entry> entries;
    ArrayList<String> labels;
    LineDataSet dataset;
    LineData data;
    LineChart lineChart;
    int k;
    Timer timer;
    TimerTask doAsynchronousTask;
    String productcode="";
    Boolean isSignoutSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);


    }
    @Override
    protected void onStop() {
        super.onStop();
        stopLoadingDataFromServer();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        stopLoadingDataFromServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SaveSharedPreference.getUserName(this).length() == 0)
        {
            stopLoadingDataFromServer();

            // Invalid user -> call Login activity
            Intent objIntent = new Intent(this,Login.class);
            startActivity(objIntent);
            finish();
            Log.e("Satus","InValid user...Redirecting to Login Activity...");
        }
        else
        {
            productcode = SaveSharedPreference.getUserName(this);
            loadChart();
            // Stay at the current activity
            Log.d("Status","Valid user...");
            //handler.post(timedTask);

            callAsynchronousTask();

        }
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        timer = new Timer();
        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try
                        {
                            SecureKitchenClient objSecureKitchenClient = new SecureKitchenClient();
                            objSecureKitchenClient.execute("2",productcode); // 2: ask
                            SecureKitchenClient.response = "~";

                            while(SecureKitchenClient.response.equals("~"))
                            {
                                //System.out.println("Waiting...");
                                Thread.sleep(500);
                            }

                            JSONObject objJSONObject;
                            try
                            {
                                Log.d("Status",SecureKitchenClient.response);
                                objJSONObject = new JSONObject(SecureKitchenClient.response);

                                entries.add(new Entry(Float.parseFloat(objJSONObject.get("reading").toString()), k));
                                labels.add(objJSONObject.get("time").toString());
                                k++;
                                Log.d("Status","Reading : " + Float.parseFloat(objJSONObject.get("reading").toString()));
                                Log.d("Status","Time    : " + objJSONObject.get("time").toString());

                            } catch (JSONException e) {
                                Log.e("Status",e.getMessage());
                            }

                            data = new LineData(labels, dataset);
                            //dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
                            dataset.setDrawCubic(true);
                            dataset.setDrawFilled(true);
                            //lineChart.setScaleMinima((float) data.getXValCount() / 5f, 1f);
                            lineChart.setAutoScaleMinMaxEnabled(true);
                            lineChart.setData(data);

                            if(k>5)
                            {
                                Entry e = entries.get((k>100?100:k) - 5);
                                lineChart.centerViewTo(e.getXIndex(), e.getVal(), YAxis.AxisDependency.LEFT);
                            }

                        } catch (Exception e) {
                            Log.e("Status",e.getMessage());

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000); //execute in every 50000 ms
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_Signout) {
            Log.d("Status","Sign Out clicked...");
            BackgroundSignOutTask signoutTask = new BackgroundSignOutTask(SaveSharedPreference.getDeviceToken(this),this);
            signoutTask.start();

            return true;
        }

        if (id == R.id.action_Settings) {
            Log.d("Status","Settings clicked...");
            stopLoadingDataFromServer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadChart()
    {
        lineChart = (LineChart) findViewById(R.id.chart);
        entries = new ArrayList<>();
        dataset = new LineDataSet(entries, "LPG gas level");
        labels = new ArrayList<String>();
        data = new LineData(labels, dataset);

        k = 0;

        // dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        lineChart.setData(data);
    }
    private void stopLoadingDataFromServer()
    {
        if(timer!=null) {
            doAsynchronousTask.cancel();
            timer.cancel();
            timer.purge();
        }
    }
    public void onBackgroundtaskCompleted()
    {
        Log.d("Status","Outside thread :) " + isSignoutSuccess);
        //If valid user send product code to Status activity and load it
        if (isSignoutSuccess == true)
        {
            stopLoadingDataFromServer();

            SaveSharedPreference.setUserName(this, "");
            Intent objIntent = new Intent(this,Login.class);
            startActivity(objIntent);
            finish();
            Log.d("Status","InValid user...Redirecting to Login Activity...");
        }
        else //Else give error message to user and stay on Login activity
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Opps!!");
            alertDialog.setMessage("Something went wrong!!!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }
}
