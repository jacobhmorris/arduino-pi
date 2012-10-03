import serial
import struct

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
            
        self.ser = serial.Serial(self.serialPort, self.serialBaud)
        
    
    def C(self, val):
        return struct.pack('H', val)
    
    #takes values for speed between 0 and 255, and either 0 or 1 for direction
    def sendMotorValues(self, leftSpeed, rightSpeed, leftDirection, rightDirection):
        if(self.useHex):
            self.ser.write('#')
            self.ser.write(self.C(leftSpeed) + self.C(rightSpeed) + self.C(leftDirection) + self.C(rightDirection))
            
        else:
            self.ser.write('*')
            self.ser.write(str(leftSpeed) + str(rightSpeed) + str(leftDirection) + str(rightDirection))

        