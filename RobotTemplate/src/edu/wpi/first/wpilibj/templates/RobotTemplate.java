/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import java.util.Random;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    public static void main(String[] args) {
        // RobotDrive drive = new RobotDrive(1,2);
        Joystick leftStick = new Joystick(1);
        // Joystick rightStick = new Joystick(2);
        
        String[] Response = {"Ouch","Watch it","Don't push my buttons","TeeHee","Do it again","Stop it","Tell me punk, do you feel lucky?"};
        int lengthResponse = Response.length;
        Random rand = new Random();
        boolean kill = false;
        
        while (kill == false) {
            if (leftStick.getRawButton(1) == true) {
                int randNum = rand.nextInt(lengthResponse);
                System.out.println("Button 1 says " + Response[randNum+1]);       
            } else if (leftStick.getRawButton(2) == true) {
                int randNum = rand.nextInt(lengthResponse);
                System.out.println("Button 2 says " + Response[randNum+1]);       
            } else if (leftStick.getRawButton(3) == true) {
                int randNum = rand.nextInt(lengthResponse);
                System.out.println("Button 3 says " + Response[randNum+1]);       
            } else if (leftStick.getRawButton(4) == true) {
                int randNum = rand.nextInt(lengthResponse);
                System.out.println("Button 4 says " + Response[randNum+1]);       
            } else if (leftStick.getRawButton(5) == true) {
                int randNum = rand.nextInt(lengthResponse);
                System.out.println("Button 5 says " + Response[randNum+1]);       
            } else if (leftStick.getRawButton(6) == true) {
                int randNum = rand.nextInt(lengthResponse);
                System.out.println("Button 6 says " + Response[randNum+1]);       
            } else if (leftStick.getRawButton(7) == true) {
                kill=true;
                System.out.println("NOOOOO, You killed me.  How could you?");
            } else {
                //here so the code doesn't crash, just continue
            } //end the if statements if buttons are pressed
        } // end the while loop
    }
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {

    }

    /**
    * This function is called once each time the robot enters operator control.
    */
    public void operatorControl() {

    }

    /**
    * This function is called once each time the robot enters test mode.
    */
    public void test() {

    }
}

