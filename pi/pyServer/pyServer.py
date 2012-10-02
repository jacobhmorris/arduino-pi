import socket
import sys

#import serial # if you have not already done so
#ser = serial.Serial('/dev/tty.usbserial', 9600)
#ser.write('5')

HOST = ''   # Symbolic name meaning all available interfaces

PORT = 8888 # Arbitrary non-privileged port

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

print 'Socket created'
 
try:
    s.bind((HOST, PORT))
except socket.error , msg:
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()

print 'Socket bind complete'

s.listen(10)
print 'Socket now listening'

#now keep talking with the client
while 1:

    #wait to accept a connection - blocking call
    conn, addr = s.accept()
    print 'Connected with ' + addr[0] + ':' + str(addr[1])
 

    data = conn.recv(1024)
    print data;
    reply = 'OK...' + data

    if not data:
        break

    conn.sendall(reply)

conn.close()

s.close()
