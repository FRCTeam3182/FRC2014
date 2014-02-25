/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.team3182.main;

import com.sun.squawk.util.Arrays;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author SWEFLL-1
 */
public class ArduinoLights extends Object implements Runnable {

    boolean[] lightData = new boolean[]{false, false, false, false};
    boolean[] dummy = new boolean[4];
    boolean isSame = false;

    private DigitalOutput arduinoSignal;
    private DigitalOutput arduinoSignifier;

    public ArduinoLights() {
        arduinoSignal = new DigitalOutput(5); //data line
        arduinoSignifier = new DigitalOutput(6); //tells arduino when to read data
    }

    public void run() {

    }

    private void sendArduino(boolean one, boolean two, boolean three, boolean four) {
        //the fuction to send certain data to the arduino
        dummy = new boolean[]{one, two, three, four};
        isSame = Arrays.equals(dummy, lightData);

        if (!isSame) {
            arduinoSignifier.set(true);
            arduinoSignal.set(one);
            Timer.delay(.01);
            arduinoSignal.set(two);
            Timer.delay(.01);
            arduinoSignal.set(three);
            Timer.delay(.01);
            arduinoSignal.set(four);
            Timer.delay(.01);
            arduinoSignal.set(false);
            arduinoSignifier.set(false);
        }
        lightData = new boolean[]{one, two, three, four};
        System.out.println("hey");

    }
}
