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
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
   private Joystick rightJoystick;
   private Joystick leftJoystick;
   private Joystick buttonsJoystick;
   private Talon shooterMotors;
   private Talon collectorMotor;
   private Solenoid leftShifter;
   private Solenoid rightShifter;
   private Solenoid leftCollector;
   private Solenoid rightCollector;
   private Encoder driveEncoder;
//    
    /**
     * Called when the robot is first turned on. This is a substitute for using
     * the constructor in the class for consistency. This method is only called
     * once
     */
    public void robotInit() {
        drive = new RobotDrive(1, 2);
       rightJoystick = new Joystick(1);
        leftJoystick = new Joystick(2);
        buttonsJoystick = new Joystick(3);
        shooterMotors = new Talon(4);
        collectorMotor = new Talon(3);
        //leftShifter = new Solenoid(5, 6);
        //rightShifter = new Solenoid(7, 8);
        //leftCollector = new Solenoid(1, 2);
        //rightCollector = new Solenoid(3, 4);
        
      
       
        

    }

    /**
     * Called when the robot enters the autonomous period for the first time.
     * This is called on a transition from any other state.
     */
    public void autonomousInit() {
        
        //Drive forward for 2 seconds
          drive.drive(0.3, 0.0);
        Timer.delay(2.0);
        drive.drive(0.0, 0.0);
                
        //Shoot:
        //Move motors forward and wait for the ball to be shot
         shooterMotors.set(1.0);
        Timer.delay(2);
        //Reload
        shooterMotors.set(-1.0);
        Timer.delay(2.0);
        //Stop 
        shooterMotors.set(0.0);
        
       
    }
    
    /**
     * Called when the robot enters the teleop period for the first time. This
     * is called on a transition from any other state.
     */
    public void teleopInit() {
       
    }


    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        double yAxisRight;
        double yAxisLeft;
        boolean shoot;
        boolean reverseshooter;
        
         //sets yAxisRight and yAxisLeft to the x axis of corresponding joysticks
        yAxisRight = rightJoystick.getAxis(Joystick.AxisType.kY);
        yAxisLeft = leftJoystick.getAxis(Joystick.AxisType.kY);
        
         //makes sure joystick will not work at +-25%
       if ((yAxisRight < .25 && yAxisRight > (-.25))) {
           yAxisRight = 0;
       }
       if (yAxisLeft < .25 && yAxisLeft > (-.25)) {
          yAxisLeft = 0;
      }
         //drive using the joysticks
        
        drive.tankDrive(yAxisRight, yAxisLeft);
        
         //shoot is button 1, revershooter is button 2 
        shoot = buttonsJoystick.getRawButton(1);
        reverseshooter = buttonsJoystick.getRawButton(2);
        
        // When button 1 is pressed, set the motors to 70%
        // When button 2 is pressed, set motors to reverse at 50% for 1 seconds
        
        if (shoot == true) {
            shooterMotors.set(.7);
        } else if (shoot == false) {
            shooterMotors.set(0);
        }
        if (reverseshooter == true) {
            shooterMotors.set(-.5);
            Timer.delay(1);
            shooterMotors.set(0);
        } else if (reverseshooter == false) {
            shooterMotors.set(0);
        
        }
        // SmartDashboard.putNumber("Speed", shooterMotors.getSpeed());
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        

    }

}
