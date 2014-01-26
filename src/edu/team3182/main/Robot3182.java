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
    private Talon shootermotor1;
    private Talon shootermotor2;
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
        shootermotor1 = new Talon(4);
        shootermotor2 = new Talon(5);
        collectormotor = new Talon(3);
        leftshifter = new Solenoid(5,6);
        rightshifter = new Solenoid(7,8);
        leftcollector = new Solenoid(1,2);
        rightcollector = new Solenoid(3,4);
        
        
        
    }

    /**
     * Called when the robot enters the autonomous period for the first time.
     * This is called on a transition from any other state.
     */
    public void autonomousInit() {

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
     * again provided that the state hasn�t changed.
     */
    public void teleopContinuous() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        double xAxisRight;
        double xAxisLeft;

        xAxisRight = rightjoystick.getAxis(Joystick.AxisType.kX);
        xAxisLeft = leftjoystick.getAxis(Joystick.AxisType.kX);

        if ((xAxisRight < .25 && xAxisRight > (-.25))) {
            xAxisRight = 0;
        }
        if (xAxisLeft < .25 && xAxisLeft > (-.25)) {
            xAxisLeft = 0;
        }
        drive.tankDrive(xAxisRight, xAxisLeft);

    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        Latch latch = new Latch();
        latch.toggle();

    }

}
