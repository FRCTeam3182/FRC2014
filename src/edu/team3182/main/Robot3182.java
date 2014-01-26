/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.team3182.main;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Encoder;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot3182 extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    private RobotDrive drive;
    private Joystick rightjoystick;
    private Joystick leftjoystick;
    private Joystick buttonsjoystick;
    private Talon shootermotors;
    private Talon collectormotor;
    private Solenoid leftshifter;
    private Solenoid rightshifter;
    private Solenoid leftcollector;
    private Solenoid rightcollector;

    /**
     * Called when the robot is first turned on. This is a substitute for using
     * the constructor in the class for consistency. This method is only called
     * once
     */
    public void robotInit() {
        drive = new RobotDrive(1, 2);
        rightjoystick = new Joystick(1);
        leftjoystick = new Joystick(2);
        buttonsjoystick = new Joystick(3);
        shootermotors = new Talon(4);
        collectormotor = new Talon(3);
        leftshifter = new Solenoid(5, 6);
        rightshifter = new Solenoid(7, 8);
        leftcollector = new Solenoid(1, 2);
        rightcollector = new Solenoid(3, 4);

    }

    /**
     * Called when the robot enters the autonomous period for the first time.
     * This is called on a transition from any other state.
     */
    public void autonomousInit() {
        
        //Drive forward for 2 seconds
        drive.drive(0.3, 0.0);
        Timer.delay(2.0);
        drive.drive(0.0, 0.0)
                
        //Shoot:
        //Move motors forward and wait for the ball to be shot
        shootermotors.set(1.0);
        Timer.delay(2);
        //Reload
        shootermotors.set(-1.0);
        Timer.delay(2.0);
        //Stop 
        shootermotors.set(0.0);
        
       
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * Called when the robot enters the teleop period for the first time. This
     * is called on a transition from any other state.
     */
    public void teleopInit() {

    }

    /**
     * Called continuously while the in the autonomous part of the match. Each
     * time the program returns from this function, it is immediately called
     * again provided that the state hasn’t changed.
     */
    public void teleopContinuous() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        double xAxisRight;
        double xAxisLeft;
        boolean shoot;
        boolean reverseshooter;
        
        // sets xAxisRight and xAxisLeft to the x axis of corresponding joysticks
        xAxisRight = rightjoystick.getAxis(Joystick.AxisType.kX);
        xAxisLeft = leftjoystick.getAxis(Joystick.AxisType.kX);
        
        // makes sure joystick will not work at +-25%
        if ((xAxisRight < .25 && xAxisRight > (-.25))) {
            xAxisRight = 0;
        }
        if (xAxisLeft < .25 && xAxisLeft > (-.25)) {
            xAxisLeft = 0;
        }
        // drive using the joysticks
        drive.tankDrive(xAxisRight, xAxisLeft);
        
        // shoot is button 1, revershooter is button 2 
        shoot = buttonsjoystick.getRawButton(1);
        reverseshooter = buttonsjoystick.getRawButton(2);
        
        /* When button 1 is pressed, set the motors to 70%
         * When button 2 is pressed, set motors to reverse at 50% for 1 seconds
        */
        if (shoot == true) {
            shootermotors.set(.7);
        } else if (shoot == false) {
            shootermotors.set(0);
        }
        if (reverseshooter == true) {
            shootermotors.set(-.5);
            Timer.delay(1);
            shootermotors.set(0);
        } else if (reverseshooter == false) {
            shootermotors.set(0);
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        Latch latch = new Latch();
        latch.toggle();

    }

}
