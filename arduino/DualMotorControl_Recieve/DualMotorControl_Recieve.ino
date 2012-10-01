//DUAL MOTOR CONTROL - Recieve

// OUTPUT

int speedRightOutputPin = 11;
int directionRightOutputPin = 12;
int speedLeftOutputPin = 9;
int directionLeftOutputPin = 10;
int ledPin = 13;

// VARIABLES

int leftSpeed;
int leftDirection;
int rightSpeed;
int rightDirection;

int cmd;

int a;
int b;
int c;
int d;
int e;
int f;
int g;
int h;

void inputRead()
{
a = Serial.read() - '0';           
b = Serial.read() - '0';
c = Serial.read() - '0';
d = Serial.read() - '0';           
e = Serial.read() - '0';
f = Serial.read() - '0';
g = Serial.read() - '0';           
h = Serial.read() - '0';

leftSpeed = a*100 + b*10 + c;
rightSpeed = e*100 + f*10 + g;
leftDirection = d;
rightDirection = h;
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
  
if(Serial.available() >= 9){
   
   cmd = Serial.read();
   if(cmd == '*')
   {
     
   inputRead();
  
   inputWrite();
   

   
   }
}
}


