import serial

class piSerial:
    def __init__(self,serialPort,serialBaud,serialTimeout,useHex):
        self.serialPort = serialPort
        self.serialBaud = serialBaud
        
        #set defaults if nothing provided
        if(serialTimeout != None):
            self.serialTimout = serialTimeout
        else:
            self.serialTimout = 10000
        
        if(useHex != None):
            self.useHex = useHex
        else:
            self.useHex = False

        def sendMotorValues(leftSpeed, rightSpeed, leftDirection, rightDirection):
            print("hello")
            
        def sendHexMotorValues(leftSpeed, rightSpeed, leftDirection, rightDirection):