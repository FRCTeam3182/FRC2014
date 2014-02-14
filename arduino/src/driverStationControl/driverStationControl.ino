//Controls the driver station's RGB LEDs

int softPot; //val of soft pot


struct led{
  byte r;
  byte g;
  byte b;
};

//the three leds
led one = {
  0,0,0};
led two = {
  0,0,0};
led three = {
  0,0,0};

void setup(){
  pinMode(0, OUTPUT); //r1
  pinMode(8, OUTPUT); //g1
  pinMode(9, OUTPUT); //b1
  
  pinMode(3, OUTPUT); //r2
  pinMode(4, OUTPUT); //g2
  pinMode(5, OUTPUT); //b2
  
  pinMode(6, OUTPUT); //r3
  pinMode(7, OUTPUT); //g3
  pinMode(10, OUTPUT); //b3
  
}

void loop(){

  ledAnalogWrite(one.r, 0);
    ledAnalogWrite(one.r, 8);
  ledAnalogWrite(one.r, 9);
  
  ledAnalogWrite(two.r, 0);
  ledAnalogWrite(one.g, 0);
  ledAnalogWrite(one.b, 0);
  
  ledAnalogWrite(three.r, 0);
  ledAnalogWrite(three.g, 0);
  ledAnalogWrite(three.b, 0);
}

