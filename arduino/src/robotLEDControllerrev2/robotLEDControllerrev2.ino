// Program to control the adressable LED strip using input from
// the digital sidecar
// 
// Functions:
// -distance from wall
// -shooting animation
// -when the ball is in the craddle
// -ground pass
// -catch
// -idle: purple, newton's ball, one led going around, 
// -winning dance
// Diagrams of robot:
//   =========Front:==========
//     .__________________.
//  58 |       Top        | 21
//  57 |                  | 22
//   . |                  | .
//   . |     Sponsor      | .
//   . |      Panel       | .
//  41 |                  | 38
//  40 |      Bottom      | 39
//     .------------------. 
//   =========Back:==========
//     .__________________.
//  58 |       Top        | 21
//  59 |                  | 20
//   . |     Back of      | .
//   . |     Sponsor      | .
//   . |      Panel       | .
//  78 |                  | 1
//  79 |      Bottom      | 0
//     .------------------. 

//The led strip library
#include <FastLED.h>

//led string stuff:
const int ledsNumber = 80; //number of leds on the strip
int stripBrightness = 0;
CRGB leds[ledsNumber];

//color data
uint32_t distanceColor = 0xFF0000; //start off with the color being red

//used for debugging
int x;
int incomingByte;

//communication data
boolean dataRecieved[] = {
  false, false, false, false};

//for animations
int shooterFrontRight = 39;
int shooterFrontLeft = 40;
int shooterBackLeft = 76;
int shooterBackRight = 3;


void setup(){

  //communication pin
  pinMode(3, INPUT);

  //interupt pin
  attachInterrupt(0, readSidecar, RISING);

  //setup the led strip
  FastLED.addLeds<WS2801, RGB>(leds, ledsNumber);

  //clear the LED strip data
  FastLED.clear();

  //Debugging
  Serial.begin(9600);  //For debugging 
}

void loop(){
  //for debugging
  //Serial.println("It works!");

  //  //for debugging
  //  Serial.print("Data: ");
  //  Serial.print(dataRecieved[0]);
  //  Serial.print(dataRecieved[1]); 
  //  Serial.println(dataRecieved[2]);
  //  delay(80);

  //for testing
  if (Serial.available() > 0) {
    // read the incoming byte:
    incomingByte = Serial.read();
  }
  if (incomingByte == 97){ //a
    dataRecieved[0] = false;
    dataRecieved[1] = false;
    dataRecieved[2] = false;
    dataRecieved[3] = false;
    //    clearLeds();
  }
  else if (incomingByte == 98){ //b
    dataRecieved[0] = true;
    dataRecieved[1] = false;
    dataRecieved[2] = false;
    dataRecieved[3] = false;
    //    setRed();
  }
  else if (incomingByte == 99){ //c
    dataRecieved[0] = false;
    dataRecieved[1] = true;
    dataRecieved[2] = true;
    dataRecieved[3] = false;
    //    setGreen();
  }
  else if (incomingByte == 100){ //d
    dataRecieved[0] = false;
    dataRecieved[1] = true;
    dataRecieved[2] = false;
    dataRecieved[3] = false;
    //    shootAndCollect();
  }
  else if (incomingByte == 101){ //e
    distanceColor = 0x008000;
  }

  //Possible cases
  if (dataRecieved[0] == false && dataRecieved[1] == false && dataRecieved[2] == false && dataRecieved[3] == false){
    //clears the strip
    clearLeds();

    //ensure the variables stay the same
    dataRecieved[0] = false;
    dataRecieved[1] = false;  
    dataRecieved[2] = false;
    dataRecieved[3] = false;
    delay(10);
  }
  else if (dataRecieved[0] == true && dataRecieved[1] == false && dataRecieved[2] == false && dataRecieved[3] == false){
    //sets the strip to red and the distance color to red
//    setRed();
    distanceColor = 0xFF0000;
    
    //ensure the variables stay the same
    dataRecieved[0] = true;
    dataRecieved[1] = false;  
    dataRecieved[2] = false;
    dataRecieved[3] = false;
    delay(10);
  }  
  else if (dataRecieved[0] == false && dataRecieved[1] == true && dataRecieved[2] == false && dataRecieved[3] == false){
    //plays animation when shooting or collecting
    shootAndCollect();

    //ensure the variables stay the same
    dataRecieved[0] = false;
    dataRecieved[1] = true;  
    dataRecieved[2] = false;
    dataRecieved[3] = false;
    delay(10);
  }
  else if (dataRecieved[0] == false && dataRecieved[1] == false && dataRecieved[2] == true && dataRecieved[3] == false){
    //play animation when a ball is in the shooter
//    setYellow();
    distanceColor = 0xFFFF00;

    //ensure the variables stay the same
    dataRecieved[0] = false;
    dataRecieved[1] = false;  
    dataRecieved[2] = true;
    dataRecieved[3] = false;
    delay(10);
  }
  else if (dataRecieved[0] == false && dataRecieved[1] == true && dataRecieved[2] == true && dataRecieved[3] == false){
    //set lights to green
//    setGreen();
    distanceColor = 0x008000;
    
    //ensure the variables stay the same
    dataRecieved[0] = false;
    dataRecieved[1] = true;  
    dataRecieved[2] = true;
    dataRecieved[3] = false;
    delay(10);
  }
  else if (dataRecieved[0] == true && dataRecieved[1] == true && dataRecieved[2] == false && dataRecieved[3] == false){
    //signal to other teams
    signal();

    //ensure the variables stay the same
    dataRecieved[0] = true;
    dataRecieved[1] = true;  
    dataRecieved[2] = false;
    dataRecieved[3] = false;
    delay(10);
  } 

}


void readSidecar(){
  //reads the data coming from the sidecar when the interrupt is detected
  delayMicroseconds(5000);
  dataRecieved[0] = digitalRead(3);
  delayMicroseconds(10000);
  dataRecieved[1] = digitalRead(3);
  delayMicroseconds(10000);
  dataRecieved[2] = digitalRead(3);
  delayMicroseconds(100000);
  dataRecieved[3] = digitalRead(3);
  delayMicroseconds(100000);
}

void clearLeds(){
  //clears the whole strip
  for (int i = 0; i <= 80; i++){
    leds[i] = CRGB::Black;
    delayMicroseconds(50);
  }
  FastLED.show();
}

//void setRed(){
//  //sets the strip to be all red
//  FastLED.setBrightness(100);
//  for (int i = 0; i <= 80; i++){
//    leds[i] = CRGB:: Red;
//    delayMicroseconds(100);
//  }
//  FastLED.show();
//}

void shootAndCollect(){
  //showing when robot is shooting or collecting
  
  for (int i; i<3; i++){ //sets the top few leds to the distance color
  leds[20+i] = distanceColor;
  leds[59-i] = distanceColor;
  }
  
  for (int i = 0; i<5; i++){
    //creates the first and third segments
    leds[shooterFrontRight-i] = distanceColor;
    leds[shooterFrontRight-i-8] = distanceColor;
    leds[shooterFrontLeft+i] = distanceColor;
    leds[shooterFrontLeft+i+8] = distanceColor;
    leds[shooterBackRight+i] = distanceColor;
    leds[shooterBackRight+i+8] = distanceColor;
    leds[shooterBackLeft-i] = distanceColor;
    leds[shooterBackLeft-i-8] = distanceColor;

    //creates the second and fourth segments (opposite the first and second)
    leds[shooterFrontRight-i-4] = CRGB::Black;
    leds[shooterFrontRight-i-12] = CRGB::Black;
    leds[shooterFrontLeft+i+4] = CRGB::Black;
    leds[shooterFrontLeft+i+12] = CRGB::Black;
    leds[shooterBackRight+i+4] = CRGB::Black;
    leds[shooterBackRight+i+12] = CRGB::Black;
    leds[shooterBackLeft-i-4] = CRGB::Black;
    leds[shooterBackLeft-i-12] = CRGB::Black;

    //show the changes
    FastLED.show();
    if(i != 4) delay(75);
  }
  
  for (int i = 0; i<5; i++){
    //creates the first and third segments
    leds[shooterFrontRight-i] = CRGB::Black;
    leds[shooterFrontRight-i-8] = CRGB::Black;
    leds[shooterFrontLeft+i] = CRGB::Black;
    leds[shooterFrontLeft+i+8] = CRGB::Black;
    leds[shooterBackRight+i] = CRGB::Black;
    leds[shooterBackRight+i+8] = CRGB::Black;
    leds[shooterBackLeft-i] = CRGB::Black;
    leds[shooterBackLeft-i-8] = CRGB::Black;
    
    //creates the second and fourth segments (opposite the first and second)
    leds[shooterFrontRight-i-4] = distanceColor;
    leds[shooterFrontRight-i-12] = distanceColor;
    leds[shooterFrontLeft+i+4] = distanceColor;
    leds[shooterFrontLeft+i+12] = distanceColor;
    leds[shooterBackRight+i+4] = distanceColor;
    leds[shooterBackRight+i+12] = distanceColor;
    leds[shooterBackLeft-i-4] = distanceColor;
    leds[shooterBackLeft-i-12] = distanceColor;
    
    //show the changes
    FastLED.show(); 
    if (i != 4) delay(75);
  }

}

//void setYellow(){
//  //sets the strip to be all yellow
//  FastLED.setBrightness(100);
//  for (int i = 0; i <= 80; i++){
//    leds[i] = CRGB::Yellow;
//    delayMicroseconds(100);
//  }
//  FastLED.show();
//}

void disabled(){
  //shows when robot is diabled
}

void signal(){
  //shows when player clicks button on joystick
}

//void setGreen(){
//  //sets the strip to be all red
//  FastLED.setBrightness(100);
//  for (int i = 0; i <= 80; i++){
//    leds[i] = CRGB::Green;
//    delayMicroseconds(100);
//  }
//  FastLED.show();
//}



