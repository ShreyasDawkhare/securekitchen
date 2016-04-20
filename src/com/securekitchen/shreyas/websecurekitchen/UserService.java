
package com.securekitchen.shreyas.websecurekitchen;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/UserService")
public class UserService {

   SecureKitchenServer sks = new SecureKitchenServer();

   @GET
   @Path("/sensordata")
   @Produces(MediaType.TEXT_PLAIN)
   public String getData(){
	   String response = sks.getSensorData("MT2015039");
	   return response;
   }

   @POST
   @Path("/ask")
   @Consumes(MediaType.TEXT_PLAIN)
   public String getSensorData(String strSensorDataQuery) //[{productcode:"MT2015039"}]
   {
	   JSONObject objJSONObject;
	   String response="{}";
	   try
	   {
			objJSONObject = new JSONObject(strSensorDataQuery);
			response = sks.getSensorData(objJSONObject.get("productcode").toString());

	   } catch (JSONException e) {
		   response = "{error:true,message:\""+e.getMessage()+"\"}";
	   }

	   return response;

   }

   @POST
   @Path("/authenticate")
   @Consumes(MediaType.TEXT_PLAIN)
   public String authenticateUser(String strSensorDataQuery) //[{productcode:"MT2015039",password:"AbCd"}]
   {
	   JSONObject objJSONObject;
	   String response="{}";
	   try
	   {
			objJSONObject = new JSONObject(strSensorDataQuery);
			response = sks.authenticateUser(objJSONObject.get("productcode").toString(),objJSONObject.get("password").toString(),objJSONObject.get("devicetoken").toString());

	   } catch (JSONException e) {
		    response = "{error:true,message:\""+e.getMessage()+"\"}";
	   }

	   return response;

   }

   @POST
   @Path("/setdata")
   @Consumes(MediaType.TEXT_PLAIN)
   public String setSensorData(String strSensorData) //[{productcode:"MT2015039",reading:200,datetimestamp:"yyyy-MM-dd hh:mm:ss.SSS"}]
   {
	   JSONObject objJSONObject;
	   String response="{}";
	   try
	   {
			objJSONObject = new JSONObject(strSensorData);
			response = sks.setSensorData(objJSONObject.get("productcode").toString(),objJSONObject.get("datetimestamp").toString(),Integer.parseInt(objJSONObject.get("reading").toString()));

	   } catch (JSONException e) {
		    response = "{error:true,message:\""+e.getMessage()+"\"}";
	   }

	   return response;

   }

   @POST
   @Path("/notifydevice")
   @Consumes(MediaType.TEXT_PLAIN)
   public String notifyDevice(String strSensorData) //[{productcode:"MT2015039",message:"Dangerous level 1000"}]
   {
	   JSONObject objJSONObject;
	   String response="{}";
	   try
	   {
			objJSONObject = new JSONObject(strSensorData);
			response = sks.notifyDevice(objJSONObject.get("productcode").toString(),objJSONObject.get("message").toString());

	   } catch (JSONException e) {
		    response = "{error:true,message:\""+e.getMessage()+"\"}";
	   }

	   return response;

   }

}