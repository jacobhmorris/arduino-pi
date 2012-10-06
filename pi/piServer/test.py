import piSerialHelper

serWrite = piSerialHelper.piSerial("/dev/ttyUSB0", 19200, None, True)

serWrite.sendMotorValues(255, 126, 1, 2)
