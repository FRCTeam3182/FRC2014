/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.team3182.main;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import java.lang.Thread;
import edu.wpi.first.wpilibj.communication.Semaphore;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 *
 * @author Nodcah
 */
public class DriveTrain extends Object implements Runnable {

    private DoubleSolenoid leftShifter;
    private DoubleSolenoid rightShifter;
    private RobotDrive drive;
    private Joystick rightJoystick;
    private Joystick leftJoystick;
    public static boolean rightTriggerCommand;
    public static boolean leftTriggerCommand;
    public static boolean quarterTurnLeftCommand;
    public static boolean quarterTurnRightCommand;
    public static boolean halfTurnRightCommand;

    //yAxisLeft/Right read in values of joysticks, values of joysticks are output inversely like airplane drive 
    double yAxisRight;
    double yAxisLeft;
    double smoothVarRight = 0; //for making joysticks linear function between of zero to 1
    double smoothVarLeft = 0;
    double p = 0.10; //dead zone of joysticks for drive is between -p and p

    public DriveTrain() {
        drive = new RobotDrive(1, 2);
        drive.setSafetyEnabled(false);
        rightJoystick = new Joystick(1);
        leftJoystick = new Joystick(2);
        leftShifter = new DoubleSolenoid(5, 6);
        rightShifter = new DoubleSolenoid(7, 8);
        rightTriggerCommand = false;
        leftTriggerCommand = false;
        quarterTurnLeftCommand = false;
        quarterTurnRightCommand = false;
        halfTurnRightCommand = false;

    }

    public void run() {
        while (true) {
            //shifter code
            //while both of the triggers are clicked, the shifter are switched to high gear
            if (rightTriggerCommand && leftTriggerCommand) {
                // if (rightShifter.get() == DoubleSolenoid.Value.kReverse) {
                shiftHigh();
            }
            if (rightTriggerCommand == false && leftTriggerCommand == false) {
                // if (leftShifter.get() == DoubleSolenoid.Value.kForward) {
                shiftLow();

            }
            /*=================================================================
                makes sure joystick will not work at +/-10% throttle
                smoothVarRight/Left are output variables from a function
                to get power from 0 to 1 between P and full throttle on the joysticks
                same for full reverse throttle to -P
            =================================================================*/
            if (yAxisRight < p && yAxisRight > (-p)) {
                smoothVarRight = 0;
            }
            if (yAxisLeft < p && yAxisLeft > (-p)) {
                smoothVarLeft = 0;
            }
            // yAxisLeft greater than P, which is pull back on the joystick
            if (yAxisLeft >= p) {
                smoothVarLeft = ((1 / (1 - p)) * yAxisLeft + (1 - (1 / (1 - p))));
            }
            // yAxisLeft less than -P, which is push forward on the joystick 
            if (yAxisLeft <= (-p)) {
                smoothVarLeft = ((1 / (1 - p)) * yAxisLeft - (1 - (1 / (1 - p))));
            }
            //smooth right joystick
            // yAxisRight greater than P, which is pull back on the joystick 
            if (yAxisRight >= p) {
                smoothVarRight = ((1 / (1 - p)) * yAxisRight + (1 - (1 / (1 - p))));
            }
            // yAxisRight less than -P, which is push forward on the joystick 
            if (yAxisRight <= (-p)) {
                smoothVarRight = ((1 / (1 - p)) * yAxisRight - (1 - (1 / (1 - p))));
            }
            //drive using the joysticks
            drive.tankDrive(smoothVarLeft, smoothVarRight);

            //does a clockwise 90 degree turn quickly 
            if (quarterTurnRightCommand && !quarterTurnLeftCommand && !halfTurnRightCommand) { //&&&&&&&&&&&&&&&&&&&&&&&&&& add semaphore to see is collector is in
                pivot(90);
            }
            //does a counter-clockwise 90 degree turn quickly
            if (quarterTurnLeftCommand && !quarterTurnRightCommand && !halfTurnRightCommand) { //&&&&&&&&&&&&&&&&&&&&&&&&&& add semaphore to see is collector is in
                pivot(-90);
            }
            if (halfTurnRightCommand && !quarterTurnRightCommand && !quarterTurnLeftCommand) { //&&&&&&&&&&&&&&&&&&&&&&&&&& add semaphore to see is collector is in
                pivot(180);
            }
        }
    }

    // pivots robot by some angle, positive is right, negative is left
    private void pivot(float angle_deg) {

        drive.drive(1, signum(angle_deg));
        Timer.delay(Math.abs(angle_deg / 300));
        drive.drive(0, 0);
    }

    private int signum(float num) {
        if (num > 0) {
            return 1;
        } else if (num < 0) {
            return -1;
        } else {
            return 0;
        }

    }

    private void shiftHigh() {
        leftShifter.set(DoubleSolenoid.Value.kForward);
        rightShifter.set(DoubleSolenoid.Value.kForward);
    }

    private void shiftLow() {
        leftShifter.set(DoubleSolenoid.Value.kReverse);
        rightShifter.set(DoubleSolenoid.Value.kReverse);
    }

}
