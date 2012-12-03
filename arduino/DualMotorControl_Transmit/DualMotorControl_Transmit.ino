
//DUAL MOTOR CONTROL - Transmit

// INPUT

int forwardBackPin = A0;
int leftRightPin = A1;

// Variables

int forwardBack;
int leftRight;

int forwards;
int backwards;
int left;
int right;

int speedRight;
int speedLeft;
int directionLeft;
int directionRight;

int a;
int b;
int c;
int d;
int e;
int f;
int g;
int h;

// METHODS

void valueReset()
{
speedLeft = 0;
speedRight = 0;
forwards = 0;
backwards = 0;
directionRight = 0;
directionLeft = 0;
}

void sendData(){
Serial.print('*');
Serial.print(a);
Serial.print(b);
Serial.print(c);
Serial.print(d);
Serial.print(e);
Serial.print(f);
Serial.print(g);
Serial.print(h);
delay(20);
}

void speedDebug()
{
Serial.print("Speed Left  ");
if(directionLeft == 0){
Serial.print("Forward     -   ");  }
if(directionLeft == 1){
Serial.print("Backwards   -   ");  }
Serial.print(speedLeft);
Serial.println("");
Serial.print("Speed Right  ");
if(directionRight == 0){
Serial.print("Forward    -   ");  }
if(directionRight == 1){
Serial.print("Backwards  -   ");  }
Serial.print(speedRight);
Serial.println("");
delay(500);
}

void motorSpeedCalculation()
{
  if(forwards > 10){
    speedLeft = forwards - left;
    directionLeft = 0;
    speedRight = forwards - right;
    directionRight = 0;}
  
  if(backwards > 10){
    speedLeft = backwards - left;
    directionLeft = 1;
    speedRight = backwards - right;
    directionRight = 1;}
 
 speedLeft = constrain(speedLeft, 0, 255);
 speedRight = constrain(speedRight, 0, 255);
} 
  
void transmissionConversion()
{
a = speedLeft/100;
b = speedLeft - a*100;
b = b/10;
c = speedLeft - a*100 - b*10;
d = directionLeft;

e = speedRight/100;
f = speedRight - e*100;
f = f/10;
g = speedRight - e*100 - f*10;
h = directionRight;
}

void inputMapping()
{
forwardBack = analogRead(forwardBackPin);
forwards = map(forwardBack, 510, 1012, 0, 255);
backwards = map(forwardBack, 490, 358, 0, 255);
backwards = constrain(backwards, 0, 255);
forwards = constrain(forwards, 0, 255);

leftRight = analogRead(leftRightPin);
left = map(leftRight, 510, 1012, 0, 255);
right = map(leftRight, 490, 355, 0, 255);
left = constrain(left, 0, 255);
right = constrain(right, 0, 255);
}

void setup() 
{
Serial.begin(19200);
pinMode(forwardBackPin, INPUT);
pinMode(leftRightPin, INPUT);
}

void loop() {
 

inputMapping(); 

motorSpeedCalculation();

transmissionConversion();



sendData();


valueReset();
}
