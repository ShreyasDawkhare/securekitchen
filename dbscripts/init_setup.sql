#create database if not exists securekitchen;

#CREATE TABLE user
#(
#	userid int NOT NULL AUTO_INCREMENT,
#	productcode varchar(100),
#	password varchar(100),
#	PRIMARY KEY (userid)
#);

#CREATE TABLE device
#(
#	deviceid int NOT NULL AUTO_INCREMENT,
#	userid int NOT NULL,
#	devicetoken varchar(500),
#	PRIMARY KEY (deviceid),
#	FOREIGN KEY (userid) 
#        REFERENCES securekitchen.user(userid)
#        ON DELETE CASCADE
#);

#CREATE TABLE sensordata
#(
#	sensordataid int NOT NULL AUTO_INCREMENT,
#	productcode varchar(100),
#	reading int,
#	datetimestamp timestamp,
#	PRIMARY KEY (sensordataid)
#);

#insert into user (productcode, password)
#select * from (select "MT2015039" as productcode,"abcd" as password ) as temp
#where not exists(
#	Select * from user where productcode = "MT2015039"
#);


#insert into user(productcode,password)
#select 'MT2015046','abcd'


insert into sensordata (productcode, sreading, sdate, stimestamp)
select * from (select "MT2015039" as productcode,	
					  200 as sreading,"07-apr-2016" as sdate,
					  "16:23:35" as stimestamp ) as temp
where not exists(
	Select * 
	from sensordata 
	where productcode = "MT2015039" and stimestamp = "16:23:35"
);
Select stimestamp,sreading 
from sensordata 
where sensordataid = 
	(
		select max(sensordataid) 
		from sensordata 
		where productcode = "MT2015039"
	);

use securekitchen;
select getdate()
