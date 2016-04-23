package com.securekitchen.shreyas.mysecurekitchen;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SecureKitchenClient extends AsyncTask<String, Void, Void>
{
    public static String response="~";
    public static String  host_ip = "172.16.86.163";

    @Override
    protected Void doInBackground(String... params)
    {
        switch (params[0])
        {
            case "1" : SecureKitchenClient.response = postRequestAuthenticate(params);
                break;
            case "2" : SecureKitchenClient.response = postRequestAsk(params);
                break;
            case "3" : SecureKitchenClient.response = postRequestSignOut(params);
                break;
        }

        return null;
    }

    public String postRequestAsk(String params[])
    {
        String response="";

        try
        {
            URL url = new URL("http://"+host_ip+":8080/SecureKitchen/rest/UserService/ask");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            String input = "{productcode:\""+params[1]+"\"}";

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(input);
            bw.flush();
            bw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;

            while ((output = br.readLine()) != null)
            {
                response = response.concat(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            response="error";
            e.printStackTrace();

        } catch (IOException e) {
            response="error";
            e.printStackTrace();

        }
        return response;
    }

    public String postRequestAuthenticate(String params[])
    {
        String response="";

        try
        {
            URL url = new URL("http://"+host_ip+":8080/SecureKitchen/rest/UserService/authenticate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            String input = "{productcode:\""+params[1]+"\",password:\""+params[2]+"\",devicetoken:\""+params[3]+"\"}";

            System.out.println(input);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(input);
            bw.flush();
            bw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;

            while ((output = br.readLine()) != null)
            {
                response = response.concat(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {



            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return response;
    }
    public String postRequestSignOut(String params[])
    {
        String response="";

        try
        {
            URL url = new URL("http://"+host_ip+":8080/SecureKitchen/rest/UserService/signout");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");


            String input = "{devicetoken:\""+params[1]+"\"}";

            System.out.println(input);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(input);
            bw.flush();
            bw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;

            while ((output = br.readLine()) != null)
            {
                response = response.concat(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {



            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return response;
    }


}
