import time, traceback
import datetime, json, requests
import os
import pyupm_buzzer as upmBuzzer

import random

class LPG:
	'''Class for LPG gas detection module'''
	
	def __init__(self):
		'''LPG gas detector constructor'''
		
		self.gasLevel = 0
		self.powerMode = 0 #Power mode
		self.alertStatus = False #Alert Status
		self.notifyStatus = False #Notify to Android status
		self.threshold = 4000 #Threshold value
		self.halfThreshold = self.threshold/2
		self.hostname = 'http://172.16.83.71:8080'
		self.mvWindowSize = 6
		self.dbRate = 5 #5sec
		self.nmlSmplRate = 5 #5Sec, Normal sample rate
		self.aggSmpRate = 0.1 #100msec, Aggresive sample rate
		self.mvWindow = []
		self.avgWindow = []
		self.currSampleRate = self.nmlSmplRate #current sample rate
		self.productcode = "" #Read product code from file
		self.prevDBTime = 0 #Last time data send to database
		self.buzzer = None
	
	def setupDev(self):
		'''
		Setup device
		'''
		print "[INFO] Setting up device"
		try:
			#@INIT Sensor
			self.buzzer = upmBuzzer.Buzzer(5)
			self.productcode = self.getProductCode()
			if(not self.productcode):
				return False
			return True
		except Exception:
			traceback.print_exc()
			self.cleanupDev()
			return False
	
	def getProductCode(self):
		'''
		Get productcode
		'''
		fp = None
		try:

			prfile = os.path.join(os.environ['HOME'],"product.info")
			productcode = None
			fp = open(prfile,'r')
			if(fp.readline().strip()=="[SecureKitchen]"):
				productcode = fp.readline().strip()
			fp.close()
			if(productcode==''):
				return None
		except Exception:
			traceback.print_exc();
			return None
		return productcode

	def getGasLevel(self):
		'''
		Read gas level from sensor
		'''
		gasLevel = random.randint(300,700) * 10
		print "Gas Level: "+str(self.gasLevel)
		return gasLevel

	def startDev(self):
		'''
		Start capturing LPG gas detection
		'''
		print "[INFO] Start LPG detection"
		index = 0
		avgGasLevel = 0
		try:
			while True:
				#TODO Read values from sensor
				self.gasLevel = self.getGasLevel()
				if((self.gasLevel > self.halfThreshold) and (self.powerMode==0)): #Second condition avoids lot of function calls
					self.setPowerMode(1)

				if((self.gasLevel <= self.halfThreshold) and (self.powerMode==1)): #Second condition avoids lot of function calls
					self.setPowerMode(0)

				#Fill moving window
				if(len(self.mvWindow)<self.mvWindowSize):
					self.mvWindow.append(self.gasLevel)
					index+=1
				#Get Moving average
				else:
					self.mvWindow[index%self.mvWindowSize] = self.gasLevel
					avgGasLevel = int(sum(self.mvWindow)/float(len(self.mvWindow))+0.5) #Round off
					index+=1

				#Moving average greater than thershold, notify
				if (avgGasLevel > self.threshold and not self.alertStatus): #Second condition avoids lot of function call
					self.setAlert(True)

				elif (avgGasLevel <= self.threshold and self.alertStatus): #Second condition avoids lot of function call
					self.setAlert(False)

				#Gas level crossed threshold and no notification sent to device
				if(avgGasLevel > self.threshold and not self.notifyStatus):
					self.notifyStatus = self.notifyDevice(avgGasLevel,self.productcode,self.hostname)

				self.sendData(avgGasLevel,self.productcode,self.hostname)

				time.sleep(self.currSampleRate)

		except Exception:
			traceback.print_exc()
		finally:
			self.cleanupDev()

	def setAlert(self,status = True):
		'''
		Sets alert
		'''
		self.alertStatus = status
		#Gas level came down, stop sending notification
		if(not status):
			self.notifyStatus = False
			self.buzzer.stopSound()
		else:
			self.buzzer.playSound(upmBuzzer.SI, 0)
		return 0

	def setPowerMode(self,mode=1):
		'Set Power mode'
		self.powerMode = mode
		if(mode==1):
			self.currSampleRate = self.aggSmpRate
		else:
			self.currSampleRate = self.nmlSmplRate

	def sendData(self,reading,productcode,hostname):
		'''
		Send data to server
		'''
		currTime = time.time()
		if((currTime-self.prevDBTime)%60<self.dbRate):
			self.avgWindow.append(reading)
			return True
		else:
			self.avgWindow.append(reading)
			reading = int(sum(self.avgWindow)/float(len(self.avgWindow)) + 0.5 )

		try:
			resp = None
			timestamp = datetime.datetime.now()
			url = hostname+'/SecureKitchen/rest/UserService/setdata/'
			data={"productcode" : str(productcode), "reading" : str(reading), "datetimestamp": timestamp.strftime('%Y-%m-%d %H:%M:%S.%f')[:-3]}
			print "DB: "+str(data)
			headers = {'Content-Type':'text/plain'}
			resp = requests.post(url , data= json.dumps(data), headers=headers)
			
			if(resp.status_code == 200):
				self.avgWindow = []
				self.prevDBTime = currTime
				return True
			return False
		
		except Exception:
			traceback.print_exc()
			return False
		finally:
			if(resp):
				resp.close()

	def notifyDevice(self,reading,productcode,hostname):
		'''
		Notify device about gas lekage
		'''
		try:
			resp = None
			url = hostname+'/SecureKitchen/rest/UserService/notifydevice/'
			data={"productcode" : str(productcode), "message" : "Gas lekage detected!!! Gas Level: "+str(reading)}
			headers = {'Content-Type':'text/plain'}
			resp = requests.post(url , data= json.dumps(data), headers=headers)
			print resp.text
			if(resp.status_code == 200):
				return True
			return False
		except Exception:
			traceback.print_exc()
			return False
		finally:
			if(resp):
				resp.close()

	def cleanupDev(self):
		'''
		Cleanup device
		'''
		#reset gas sensor
		del self.buzzer

def main():
	try:
		lpg = LPG();
		if(not lpg.setupDev()):
			print '[ERROR]: Unable to setup device'
			return 1
		lpg.startDev()
	except Exception:
		traceback.print_exc()
		lpg.cleanupDev()


if __name__ == "__main__":
	main()