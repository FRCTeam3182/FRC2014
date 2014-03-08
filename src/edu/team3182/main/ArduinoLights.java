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
    boolean signal = false;
    boolean isCollecting = false;
    boolean isShooting = false;
    boolean isPassing = false;
    boolean isSendingColor = true;
    boolean isSendingAuto = true;
    boolean kill = false;
    int oldDistance = 2;
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
        if (driverStation.getAlliance() == DriverStation.Alliance.kBlue && isSendingColor) {
            //send arduino if color is blue

            isSendingColor = false;
        } else if (driverStation.getAlliance() == DriverStation.Alliance.kRed && isSendingColor) {
            //send arduino if color is red
            isSendingColor = false;
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
                dataToSend = new boolean[] {true, true, false, false};
                sendArduino(dataToSend);
                Timer.delay(2);
//                dataToSend = new boolean[] {false, true, false, true};
//                sendArduino(dataToSend);
            }
            if (distance == 1 && oldDistance != 1) {
                dataToSend = new boolean[]{false, true, true, false};
                sendArduino(dataToSend);
                oldDistance = 1;
            } else if (distance == 0 && oldDistance != 0) {
                dataToSend = new boolean[]{true, false, false, false};
                sendArduino(dataToSend);
                oldDistance = 0;
            } else if (distance == 2 && oldDistance != 2) {
                dataToSend = new boolean[]{false, false, true, false};
                sendArduino(dataToSend);
                oldDistance = 2;
            }
            if (isAuto && driverStation.isEnabled() && isSendingAuto) {
                dataToSend = new boolean[]{true, false, true, false};
                sendArduino(dataToSend);
                Timer.delay(1.01);
                dataToSend = new boolean[]{false, false, false, false};
                sendArduino(dataToSend);
                System.out.println(dataToSend[0]);
                Timer.delay(9.0);
                isSendingAuto = false;
            } else if (driverStation.isEnabled()) { //teleop arduino code with hierarchy of importance (least important to most important)
                //idle (if nothing is happening)
                dataToSend = new boolean[]{false, false, false, true};

                //passing
                if (isPassing){
                    dataToSend = new boolean[]{false, true, true, true};
                }
                //collecting / shooting
                if (isCollecting || isShooting){
                dataToSend = new boolean[]{false, true, false, false};
                }
                
                //signal
                if (signal) {
                    dataToSend = new boolean[]{false, false, true, true};
                }
                sendArduino(dataToSend);
            }
            else if (kill){
                dataToSend = new boolean[] {true, true, true, true};
                sendArduino(dataToSend);
            }
            else if (driverStation.isDisabled() && !(driverStation.getMatchTime() == 0 && thresh)){
                dataToSend = new boolean[] {false, false, false, false};
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

    public void signal(boolean signal) {
        this.signal = signal;
    }
    public void isCollecting(boolean isCollecting) {
        this.isCollecting = isCollecting;
    }
    public void isShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }
    public void isPassing(boolean isPassing) {
        this.isPassing = isPassing;
    }
    public void kill(boolean kill) {
        this.kill = kill;
    }
}
