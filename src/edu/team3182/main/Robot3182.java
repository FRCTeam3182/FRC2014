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

    public void robotInit() {

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    public void teleopInit() {

        drive = new RobotDrive(1, 2);
        rightjoystick = new Joystick(1);
        leftjoystick = new Joystick(2);

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

        while (isOperatorControl() && isEnabled()) {
            drive.tankDrive(rightjoystick, leftjoystick);
        }

    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        Latch latch = new Latch();
        latch.Toggle();

    }

}
