import socket
import sys
import piSerialHelper

HOST = ''   # Symbolic name meaning all available interfaces

PORT = 8889 # Arbitrary non-privileged port
serWrite = piSerialHelper.piSerial("/dev/ttyACM0", 19200, None, True)
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

print 'Socket created'
 
try:
    s.bind((HOST, PORT))
except socket.error , msg:
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()

#print 'Socket bind complete'

s.listen(10)
#print 'Socket now listening'

while 1:

    #wait to accept a connection - blocking call
    conn, addr = s.accept()
    #print 'Connected with ' + addr[0] + ':' + str(addr[1])
    try:
        data = conn.recv(4096)
        print("received the following data: " + data)
        data = data.split(',')
        leftSpeed = data[0]
        rightSpeed = data[1]
        leftDirection = data[2]
        rightDirection = data[3]
    except Exception, msg:
        print("Error receiving data from client: "+msg[0])
    
    try:
        serWrite.sendMotorValues(int(leftSpeed), int(rightSpeed), int(leftDirection), int(rightDirection))
    except Exception, msg:
        print("Error sending data via serial: "+msg[0])
    

conn.close()

s.close()
