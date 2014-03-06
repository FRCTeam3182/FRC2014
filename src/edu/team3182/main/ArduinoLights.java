/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.team3182.main;

import com.sun.squawk.util.Arrays;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author SWEFLL-1
 */
public class ArduinoLights extends Object implements Runnable {

    boolean[] lightData = new boolean[]{false, false, false, false};
    boolean[] dummy = new boolean[4];
    boolean[] dataToSend = new boolean[4];
    boolean isSame = false;
    boolean isAuto;
    boolean thresh = false;
    int distance;

    private DigitalOutput arduinoSignal;
    private DigitalOutput arduinoSignifier;
    private DriverStation driverStation;
    private Sensors sensors;
    
    public ArduinoLights(Sensors sensors) {
        arduinoSignal = new DigitalOutput(5); //data line
        arduinoSignifier = new DigitalOutput(6);//tells arduino when to read data
        driverStation = DriverStation.getInstance();
        this.sensors = sensors;
        Timer.delay(.5);

        //---------------------------------------------------
        //send certain data to arudino based on team color
        //---------------------------------------------------
        if (driverStation.getAlliance() == DriverStation.Alliance.kBlue) {
            //send arduino if color is blue

        } else if (driverStation.getAlliance() == DriverStation.Alliance.kRed) {
            //send arduino if color is red

        }
    }

    public void run() {
        while (true) {
            isAuto = driverStation.isAutonomous();
            distance = sensors.shootingDistance();
            //---------------------------------------------------
            //if the match time reaches 100 seoonds set thresh
            //to true. Then, if the match time is equal to 0
            //(end of match), and thresh is true. Set arduino
            //to "celebration mode".
            //---------------------------------------------------
            if (driverStation.getMatchTime() == 100) {
                thresh = true;
            }
            if (driverStation.getMatchTime() == 0 && thresh) {
                //send arduino to do celebration
            }
            if (distance == 1){
            
            }
            else if (distance == 0){
                
            }
            else if (distance == 2){
                
            }
            if (isAuto && driverStation.isEnabled()) {
                dataToSend = new boolean[]{true, false, true, false};
                sendArduino(dataToSend);
                dataToSend = new boolean[]{false, false, false, false};
                sendArduino(dataToSend);
                System.out.println(dataToSend[0]);
                Timer.delay(10.01);
            }
            else if(driverStation.isEnabled()){ //teleop arduino code with hierarchy of importance (least important to most important)
                //idle (if nothing is happening)
                dataToSend = new boolean[]{false, false, false, true};

                //signal
                dataToSend = new boolean[]{false, false, true, true};

                sendArduino(dataToSend);
            }
            
        }
    }

    private void sendArduino(boolean[] blah) {
        //the fuction to send certain data to the arduino
        dummy = blah;
        isSame = Arrays.equals(dummy, lightData);

        if (!isSame) {
            arduinoSignifier.set(true);
            arduinoSignal.set(blah[0]);
            Timer.delay(.01);
            arduinoSignal.set(blah[1]);
            Timer.delay(.01);
            arduinoSignal.set(blah[2]);
            Timer.delay(.01);
            arduinoSignal.set(blah[3]);
            Timer.delay(.01);
            arduinoSignal.set(false);
            arduinoSignifier.set(false);
            Timer.delay(.01);
            System.out.println("hey");
            System.out.println(blah[0]);
            System.out.println(blah[1]);
            System.out.println(blah[2]);
            System.out.println(blah[3]);
        }
        lightData = blah;
        
    }
}
