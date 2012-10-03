//DUAL MOTOR CONTROL - Recieve

// OUTPUT

int speedRightOutputPin = 10;
int directionRightOutputPin = 12;
int speedLeftOutputPin = 3;
int directionLeftOutputPin = 11;
int ledPin = 3;

// VARIABLES

int leftSpeed;
int leftDirection;
int rightSpeed;
int rightDirection;

int cmd;

void readHex() {
 leftSpeed = Serial.read();
 Serial.read();
 rightSpeed = Serial.read();
 Serial.read();
 leftDirection = Serial.read();
 Serial.read();
 rightDirection = Serial.read();
 Serial.read();

 Serial.print("left s: ");
 Serial.println(leftSpeed, DEC);
 Serial.print("right s: ");
 Serial.println(rightSpeed, DEC);
 Serial.print("left d: ");
 Serial.println(leftDirection, DEC);
 Serial.print("right d: ");
 Serial.println(rightDirection, DEC);
}
   
void inputWrite()
{
analogWrite(speedRightOutputPin, leftSpeed);
analogWrite(speedLeftOutputPin, rightSpeed);
digitalWrite(directionRightOutputPin, leftDirection);
digitalWrite(directionLeftOutputPin, rightDirection);
}
 
void setup()
{
Serial.begin(19200);
pinMode(speedRightOutputPin, OUTPUT);
pinMode(directionRightOutputPin, OUTPUT);
pinMode(speedLeftOutputPin, OUTPUT);
pinMode(directionLeftOutputPin, OUTPUT);
pinMode(ledPin, OUTPUT);
}


void loop() 
{
  
if(Serial.available() >= 9 ){
   
   cmd = Serial.read();
   if(cmd == '#') {
      readHex();
      inputWrite(); 
   }
   
}
}


