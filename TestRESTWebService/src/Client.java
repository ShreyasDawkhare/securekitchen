import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Client 
{
	public static String host=Constants.HOST_IP;
	public static void main(String[] args)
	{
		//postRequestAsk();
		//postRequestAuthenticate();
		//getRequest();
		//scheduleJob();
		//postSetProductData();
		postRequestNotify();
	}
	
	private static void scheduleJob() {
		class Backgroundtask extends TimerTask {
		    public void run() {
		    	postRequestSetSensorData(); 
		    }
		 }

		 Timer timer = new Timer();
		 timer.schedule(new Backgroundtask(), 0, 5000);
	}

	public static void getRequest() 
	{
		String response="";
		try {

			//URL url = new URL("http://"+host+":8080/SecureKitchen/rest/UserService/sensordata");
			URL url = new URL("http://"+host+":8080/UtkarshaProductCatalog/rest/UserService/data");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "text/plain");

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
			
			while ((output = br.readLine()) != null) {
				response = response.concat(output);
			}

			System.out.println("GET Response : " + response);
			
			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		  }
	}
	public static void postRequestAuthenticate() 
	{
		String response="";
		String params[] = {"MT2015039","AbCd"}; 
		
        try 
        {
            URL url = new URL("http://"+host+":8080/SecureKitchen/rest/UserService/authenticate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            String input = "{productcode:\""+params[0]+"\",password:\""+params[1]+"\"}";

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

            System.out.println("POST Response : " + response);

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

	}
	public static void postRequestAsk() 
	{
		String response="";
		String params[] = {"MT2015039","12:43:45"}; 
		
        try 
        {
            URL url = new URL("http://"+host+":8080/SecureKitchen/rest/UserService/ask");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            String input = "{productcode:\""+params[0]+"\",sincetimestamp:\""+params[1]+"\"}";

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

            System.out.println("POST Response : " + response);

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

	}
	private static void postRequestSetSensorData() 
	{
		String response="";
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    
	    Random rand = new Random();

	    int randomNum = rand.nextInt((1000 - 200) + 1) + 200;
	    
		String params[] = {"MT2015039",Integer.toString(randomNum),strDate}; 
		
		
        try 
        {
            URL url = new URL("http://"+host+":8080/SecureKitchen/rest/UserService/setdata");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            String input = "{productcode:\""+params[0]+"\",reading:"+params[1]+",datetimestamp:\""+params[2]+"\"}";

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

            System.out.println("POST Response : " + response);

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
	}
	public static void postSetProductData() 
	{
		String response="";
		
        try 
        {
            URL url = new URL("http://"+host+":8080/UtkarshaProductCatalog/rest/dataservice/setproductdata");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            String input = "<ul><li>Example 1</li><li>Example 2</li><li>Example 3</li><li>Example 4</li></ul>";

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

            System.out.println("POST Response : " + response);

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

	}
	public static void postRequestNotify() 
	{
		String response="";
		String params[] = {"MT2015039","Aag lagli na be murkha!!!"}; 
		
        try 
        {
            URL url = new URL("http://"+host+":8080/SecureKitchen/rest/UserService/notifydevice");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            String input = "{productcode:\""+params[0]+"\",message:\""+params[1]+"\"}";

            System.out.println("Input : " + input);
            
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

            System.out.println("POST Response : " + response);

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

	}
	
}
