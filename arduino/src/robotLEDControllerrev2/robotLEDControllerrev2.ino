// Program to control the adressable LED strip using input from
// the digital sidecar
// 
// Functions:
// -distance from wall (DONE)
// -shooting animation (NEEDS TO BE TESTED)
// -when the ball is in the craddle (charging animation)
// -ground pass
// -catch
// -idle: purple, newton's ball, one led going around, 
// -winning dance
//
// Diagrams of robot:
//   =========Front:==========
//     .__________________.
//  56 |       Top        | 24
//  55 |                  | 25
//   . |                  | .
//   . |     Sponsor      | .
//   . |      Panel       | .
//  41 |                  | 38
//  40 |      Bottom      | 39
//     .------------------. 
//   =========Back:==========
//     .__________________.
//  56 |       Top        | 24
//  57 |                  | 23
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
int shooterFrontRight = 39; //pos of bottom LED for each of the four towers (2 towers, 2 sides)
int shooterFrontLeft = 40;
int shooterBackLeft = 76;
int shooterBackRight = 3;
//for random color in celebration
int r;

uint32_t randomColor[] = {
  0xFFFFFF, 0xFFFF00, 0xFF0000, 0xFF0066, 0x0000FF, 0x00FFFF, 0x00FF00, 0xFF6600
};

//choses which animation to play (changes every time something happens)
int idleAnim = 2; //1 is a starting animation, 2 is chase, 3 is newton's craddle, 4 is..........
//direction of chase
boolean chaseDir = false;
boolean newtonDir = false;
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

  //random stuff
  randomSeed(analogRead(0));
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
    dataRecieved[1] = true;
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
    dataRecieved[0] = false;
    dataRecieved[1] = true;
    dataRecieved[2] = false;
    dataRecieved[3] = false;
  }

  //Possible cases
  if (dataRecieved[0] == false && dataRecieved[1] == false && dataRecieved[2] == false && dataRecieved[3] == false){
    //clears the strip
    FastLED.clear();
    clearLeds();

    //ensure the variables stay the same
    dataRecieved[0] = false;
    dataRecieved[1] = false;  
    dataRecieved[2] = false;
    dataRecieved[3] = false;
    delay(10);
  }
  if (dataRecieved[0] == false && dataRecieved[1] == false && dataRecieved[2] == false && dataRecieved[3] == true){
    //clears the strip
    idle();

    //ensure the variables stay the same
    dataRecieved[0] = false;
    dataRecieved[1] = false;  
    dataRecieved[2] = false;
    dataRecieved[3] = true;
    delay(10);
  }
  else if (dataRecieved[0] == true && dataRecieved[1] == false && dataRecieved[2] == false && dataRecieved[3] == false){
    //sets the strip to red and the distance color to red
    setRed();
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
    setYellow();
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
    setGreen();
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
    celebration();

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

void shootAndCollect(){
  //showing when robot is shooting or collecting

  for (int i; i<3; i++){ //sets the top few leds to the distance color
    leds[23+i] = distanceColor;
    leds[55+i] = distanceColor;
  }

  for (int i = 0; i<5; i++){
    //creates the first and third segments
    leds[shooterFrontRight-i] = distanceColor;
    leds[shooterFrontRight-i-8] = distanceColor;
    leds[shooterFrontLeft+i] = distanceColor;
    leds[shooterFrontLeft+i+8] = distanceColor;
    leds[shooterBackRight+i] = distanceColor;
    leds[shooterBackRight+i+8] = distanceColor;
    leds[shooterBackRight+i+16] = distanceColor;
    leds[shooterBackLeft-i] = distanceColor;
    leds[shooterBackLeft-i-8] = distanceColor;
    leds[shooterBackLeft-i-16] = distanceColor;

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
    leds[shooterBackRight+i+16] = CRGB::Black;
    leds[shooterBackLeft-i] = CRGB::Black;
    leds[shooterBackLeft-i-8] = CRGB::Black;
    leds[shooterBackLeft-i-16] = CRGB::Black;

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

void disabled(){
  //shows when robot is diabled
}

void signal(){
  //shows when player clicks button on joystick
}

void setGreen(){
  //sets the strip to be all red
  FastLED.setBrightness(100);
  for (int i = 0; i <= 80; i++){
    leds[i] = CRGB::Green;
    delayMicroseconds(100);
  }
  FastLED.show();
}

void setYellow(){
  //sets the strip to be all yellow
  FastLED.setBrightness(100);
  for (int i = 0; i <= 80; i++){
    leds[i] = CRGB::Yellow;
    delayMicroseconds(100);
  }
  FastLED.show();
}

void setRed(){
  //sets the strip to be all red
  FastLED.setBrightness(100);
  for (int i = 0; i <= 80; i++){
    leds[i] = CRGB:: Red;
    delayMicroseconds(100);
  }
  FastLED.show();
}


void clearLeds(){
  //clears the whole strip
  for (int i = 0; i <= 80; i++){
    leds[i] = CRGB::Black;
    delayMicroseconds(50);
  }
  FastLED.show();
}

void celebration(){
  //plays an animation that is played at the end of every match
  for (int i = 0; i < 23; i++){
    leds[i] = CRGB::White;
    leds[79-i] = CRGB::White;
    FastLED.show();
    delay((pow(i, 2)*.8) + 50);
    leds[i] = CRGB::Black;
    leds[79-i] = CRGB::Black;
  }


  FastLED.show();
  for(int i = 0; i<10; i++){

    r = random(0,7);
    leds[23] = randomColor[r];
    r= random(0,7);
    leds[56] = randomColor[r];
    
    for(int z = 0; z<=10; z++){
      leds[12+z].r = leds[13+z].r;
      leds[12+z].g = leds[13+z].g;
      leds[12+z].b = leds[13+z].b;
      leds[34-z].r = leds[33-z].r;
      leds[34-z].g = leds[33-z].g;
      leds[34-z].b = leds[33-z].b;
      leds[46+z].r = leds[47+z].r;
      leds[46+z].g = leds[47+z].g;
      leds[46+z].b = leds[47+z].b;
      leds[65-z].r = leds[64-z].r;
      leds[65-z].g = leds[64-z].g;
      leds[65-z].b = leds[64-z].b;
      
    }
    
//    for(int blah = 0; blah<=10; blah++){
//      Serial.print(i);
//      Serial.print(": ");
//      Serial.print(leds[23-blah].r);
//      Serial.print(", ");
//      Serial.print(leds[23-blah].g);
//      Serial.print(", ");
//      Serial.println(leds[23-blah].b);
//      delay(20);
//    }
    FastLED.show();
    delay(pow(i,2)*4+30);
  }
  FastLED.clear();
}

void idle(){
  //plays when the robot isn't doing anything specific, but is just driving, defending, etc.
  if (idleAnim == 1){
    //starting animation

  }
  if (idleAnim == 2){
    //led chase
    FastLED.clear();
    FastLED.setBrightness(100);
    if (chaseDir == true){ //chase in one direction
      for (int i = 0; i < 39; i++){
        leds[i] = CRGB::Purple;
        leds[79-i] = CRGB::Purple;
        FastLED.show();
        delay(200);
        leds[79-i] = CRGB::Black;
        leds[i] = CRGB::Black;

      }
      chaseDir = false;  
    }
    else if (chaseDir = false){ //chase in the other direction
      for (int i = 0; i < 39; i++){
        leds[39-i] = CRGB::Purple;
        leds[40+i] = CRGB::Purple;
        FastLED.show();
        delay(200);
        leds[39-i] = CRGB::Black;
        leds[40+i] = CRGB::Black;
      }
      chaseDir = true; 
    }
  }
  if (idleAnim == 3){
    //newton's cradle animation
    for (int i = 0; i < 5; i++){ //the five middle leds.
      leds[54+i] = CRGB::Purple;
      leds[22+i] = CRGB::Purple;
    }
    FastLED.show();
    if (newtonDir == false){
      for (int i = 0; i < 8; i++){ //going up
        leds[22-i] = CRGB::Purple;
        leds[22-i+1] = CRGB::Black;
        leds[54+i] = CRGB::Purple;
        leds[54+i-1] = CRGB::Black;
        FastLED.show();
        delay((pow(i, 2)/.25) + 50);
      }
      for (int i = 0; i < 8; i++){ //going down
        leds[22+i] = CRGB::Purple;
        leds[22+i-1] = CRGB::Black;
        leds[54-i] = CRGB::Purple;
        leds[54-i+1] = CRGB::Black;
        FastLED.show();
        delay((pow(i, 2)/.25) + 50);
      }
    }
  }
}















