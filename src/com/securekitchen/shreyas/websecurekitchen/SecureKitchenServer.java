package com.securekitchen.shreyas.websecurekitchen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class SecureKitchenServer {

   public String getSensorData(String productCode)
   {
	   String response="";
	   String timestamp="";
	   int reading=0;
	   MysqlConnector objMysqlConnector = null;

       Connection con = null;
	   try 
	   {
		   objMysqlConnector = new MysqlConnector();
		   con = objMysqlConnector.getConnection();
		   PreparedStatement ps = con.prepareStatement("Select datetimestamp,reading from sensordata where sensordataid = (select max(sensordataid) from sensordata where productcode = ? );");
		   ps.setString(1, productCode);
		   ResultSet rs= ps.executeQuery();
		   while(rs.next())
		   {
			   timestamp = new SimpleDateFormat("HH:mm:ss").format(rs.getTimestamp(1));
			   reading = rs.getInt(2);
		   }

		   response = "{productcode:\""+productCode+"\", time:\""+timestamp +"\",timeformat:\"HH:mm:ss\", reading:"+reading+"}";
	   } catch (SQLException e) {
		   response = "{SQLException:"+e.getMessage()+"}";
		   e.printStackTrace();
		   return response;
	   } catch (Exception e) {
		   response = "{Exception:"+e.getMessage()+"}";
		   e.printStackTrace();
		   return response;
	   } finally{
		   if(objMysqlConnector != null)
		   {
			   objMysqlConnector.closeConnection(con);
		   }
	   }
	   
	   return response;
   }
   
   public String authenticateUser(String productCode, String password, String devicetoken)
   {
	   String response;
	   String db_pwd = "";
	   int rowsAffected=0;
	   Connection con = null;
	   MysqlConnector objMysqlConnector = null;
	   try 
	   {
		   objMysqlConnector = new MysqlConnector();
		   con = objMysqlConnector.getConnection();
		   PreparedStatement ps = con.prepareStatement("Select password from user where productcode = ?");
		   ps.setString(1, productCode);
		   ResultSet rs= ps.executeQuery();
		   while(rs.next())
		   {
			   db_pwd = rs.getString(1);
		   }

		   
		   if(password.equals(db_pwd))
		   {
			   ps = con.prepareStatement("insert into device (userid, devicetoken) select usr.userid,temp.devicetoken from (select ? as productcode, ? as devicetoken) as temp INNER JOIN user usr on usr.productcode = ? where not exists( Select d.deviceid from device d INNER JOIN user u on d.userid = u.userid where devicetoken = ?);");
			   System.out.println("ShreyasError: " + devicetoken);
			   ps.setString(1, productCode);
			   ps.setString(2, devicetoken);
			   ps.setString(3, productCode);
			   ps.setString(4, devicetoken);
			   
			   rowsAffected= ps.executeUpdate();
			   
			   response = "{productcode:\""+productCode+"\",authentication:true,rowsAffected:"+rowsAffected+"}";
		   }
		   else
		   {
			   response = "{productcode:\""+productCode+"\",authentication:false,rowsAffected:"+rowsAffected+"}";
		   }
		   
	   } catch (SQLException e) {
		   response = "{SQLException:\""+e.getMessage()+"\",authentication:false}";
		   e.printStackTrace();
		   return response;
	   } catch (Exception e) {
		   response = "{Exception:\""+e.getMessage()+"\",authentication:false}";
		   e.printStackTrace();
		   return response;
	   } finally{
		   if(objMysqlConnector != null)
		   {
			   objMysqlConnector.closeConnection(con);
		   }
	   }
	   
	   
	   return response;
   }
   public String setSensorData(String productCode, String timestamp, int reading)
   {
	   String response="";
	   Timestamp ts=null;
	   int rowsAffected=0;
	   MysqlConnector objMysqlConnector = null;
	   
       Connection con = null;
	   try 
	   {
		   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		   Date parsedDate = dateFormat.parse(timestamp);
		   ts = new java.sql.Timestamp(parsedDate.getTime());
		    
		   objMysqlConnector = new MysqlConnector();
		   con = objMysqlConnector.getConnection();
		   PreparedStatement ps = con.prepareStatement("insert into sensordata (productcode, reading, datetimestamp) select * from (select ? as productcode, ? as reading,? as datetimestamp ) as temp  where not exists( Select * from sensordata 	where productcode = ? and datetimestamp = ?  );");
		   
		   ps.setString(1, productCode);
		   ps.setInt(2, reading);
		   ps.setTimestamp(3, ts);
		   ps.setString(4, productCode);
		   ps.setTimestamp(5, ts);
		   
		   rowsAffected= ps.executeUpdate();
		   
		   response = "{productcode:\""+productCode+"\",rowsAffected:"+rowsAffected+"}";
	   } catch (SQLException e) {
		   response = "{SQLException:\""+e.getMessage()+"\"}";
		   e.printStackTrace();
		   return response;
	   } catch (Exception e) {
		   response = "{Exception:\""+e.getMessage()+"\"}";
		   e.printStackTrace();
		   return response;
	   } finally{
		   if(objMysqlConnector != null)
		   {
			   objMysqlConnector.closeConnection(con);
		   }
	   }
	   
	   return response;
   }
   
   public String notifyDevice(String productCode, String message)
   {
	   String response="";
	   
	   return response;
   }
  
}