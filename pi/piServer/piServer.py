import socket
import sys
import piSerialHelper
import argparse

parser = argparse.ArgumentParser(description='Desc.')
parser.add_argument('-l', action='store', dest='HOST', help='The address to listen on')
parser.add_argument('-p', action='store', dest='PORT', type=int, help='The port to listen on')
parser.add_argument('-d', action='store', dest='DEVICE', help='The serial device to open')
parser.add_argument('-b', action='store', dest='BAUD', type=int, help='The baud rate of the serial device to open')

args = parser.parse_args()


HOST = ''
PORT = 8888
DEVICE = '/dev/ttyAMA0'
BAUD = 19200

if(args.HOST != None):
    HOST = args.HOST

if(args.PORT != None):
    PORT = args.PORT
    
if(args.DEVICE != None):
    DEVICE = args.DEVICE
    
if(args.BAUD != None):
    BAUD = args.BAUD   

print("Starting Server [HOST="+HOST+"] [PORT="+str(PORT)+"] [DEVICE="+DEVICE+"] [BAUD="+str(BAUD)+"]")

serWrite = piSerialHelper.piSerial(DEVICE, BAUD, True)
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

print('Socket created on '+socket.gethostbyname(socket.gethostname()))

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

    except KeyboardInterrupt:
        print("Closing Connections")
        conn.close()
        s.close()

