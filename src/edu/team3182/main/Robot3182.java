/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.team3182.main;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    private Encoder rightDriveEncoder;
    private Encoder leftDriveEncoder;
    private AnalogPotentiometer shooterPot;
    double yAxisRight;
    double yAxisLeft;
    double distance;
    boolean shoot;
    boolean reverseShooter;
    boolean collect;
    boolean collectReverse;
    boolean collectorFoward;
    double p = 0.25; //dead zone is between -p and p
    double smoothVarRight = 0; //for making joysticks exponential
    double smoothVarLeft = 0;
    final int endLoopDrive = 10; //length of for loops that control maneuver timing/ shooting timing
    final int endLoopShoot = 10;
    int shooterPotVal; //position of catapult
    final double a = .005;
    final double b = .9;

    
    /**
     * Called when the robot is first turned on. This is a substitute for using
     * the constructor in the class for consistency. This method is only called
     * once
     */
    public void robotInit() {
        drive = new RobotDrive(1, 2);
        drive.setSafetyEnabled(false);
        rightJoystick = new Joystick(1);
        leftJoystick = new Joystick(2);
        buttonsJoystick = new Joystick(3);
        shooterMotors = new Talon(1);
        collectorMotor = new Talon(2);
        shooterPot = new AnalogPotentiometer(1);
        rightDriveEncoder = new Encoder(1,2);
        leftDriveEncoder = new Encoder(3,4);
        rightDriveEncoder.reset();
        rightDriveEncoder.setDistancePerPulse(.08168);
        
//        leftShifter = new Solenoid(2, 6);
//        rightShifter = new Solenoid(2, 8);
//        leftCollector = new Solenoid(2, 2);
//        rightCollector = new Solenoid(2, 4);

    }

    /**
     * Called when the robot enters the autonomous period for the first time.
     * This is called on a transition from any other state.
     */
    public void autonomousInit() {
       rightDriveEncoder.start();
        
        
        //Send command to Arduino for the light strip

       // set the variable distance to the distance of encoder since reset
       distance = rightDriveEncoder.getDistance();
         //Drive forward for 2 seconds
        drive.drive(0.3, 0.0);
        Timer.delay(2.0);
        drive.drive(0.0, 0.0);

        //Shoot:
        //quickly speed up motors, then wait for the ball to be shot
        for (int i = 1; i <= endLoopShoot; i++) { //takes half a second to reach full speed
                shooterMotors.set(a* (MathUtils.exp(b*i)));
                Timer.delay(.01);
        }
        shooterMotors.set(1);
        Timer.delay(.1);
        shooterMotors.set(0);
        Timer.delay(1);
        shooterMotors.set(-.3);
        Timer.delay(.25);
        shooterMotors.set(0);
    }

    public void autonomousPeriodic() {
        Timer.delay(.01);
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

        //sets yAxisRight and yAxisLeft to the x axis of corresponding joysticks
        yAxisRight = rightJoystick.getAxis(Joystick.AxisType.kY);
        yAxisLeft = leftJoystick.getAxis(Joystick.AxisType.kY);

        

        //makes sure joystick will not work at +-25%
        if (yAxisRight < p && yAxisRight > (-p)) {
            
            smoothVarRight = 0;
        }
        if (yAxisLeft < p && yAxisLeft > (-p)) {
            
            smoothVarLeft = 0;
        }
       
        
        //smooth left joystick
        //positive
        if (yAxisLeft >= p) {
            smoothVarLeft = -1*((1 / (1 - p)) * yAxisLeft + (1 - (1 / (1 - p))));     
        }
        
        //negative
        if (yAxisLeft <= (-p)) {
            smoothVarLeft = -1*((1 / (1 - p)) * yAxisLeft - (1 - (1 / (1 - p))));  
        }
        
        //smooth right joystick
        //positive
        if (yAxisRight >= p) {
            smoothVarRight = -1*((1 / (1 - p)) * yAxisRight + (1 - (1 / (1 - p))));
        }
        
        //negative
        if (yAxisRight <= (-p)) {
            smoothVarRight = -1*((1 / (1 - p)) * yAxisRight - (1 - (1 / (1 - p)))); 
        }
        
        //drive using the joysticks
        drive.tankDrive(smoothVarLeft, smoothVarRight);

        //shoot is button 1, collect is 2, ground pass/dump is 3
        shoot = buttonsJoystick.getRawButton(1);
        collect = buttonsJoystick.getRawButton(2);
        collectReverse = buttonsJoystick.getRawButton(3);

        // When button 1 is pressed, set the motors to 70% for 1 second
        // When button 2 is pressed, set motors to reverse at 50% for 1 seconds
        if (shoot == true) {
            for (int i = 1; i <= endLoopShoot; i++) { //takes half a second to reach full speed
                shooterMotors.set(a*MathUtils.exp(b*i));
                Timer.delay(.01);
            }
            shooterMotors.set(1);
            Timer.delay(.1);
            shooterMotors.set(0);
            Timer.delay(1);
            shooterPotVal = (int) shooterPot.get();
            while (shooterPotVal >= 300){
                shooterMotors.set(-.2);
            }
            shooterMotors.set(0);    
        } 
        else if (shoot == false) {
            shooterMotors.set(0);
        }


        // if button 3 is pressed, run the collector motor at 90%
        // if button 4 is pressed, run the collector motor in reverse at 90%
        if (collect == true) {
            collectorMotor.set(.9);
        } else if (collect == false) {
            collectorMotor.set(0);
        }
        if (collectReverse == true) {
            collectorMotor.set(-.9);
        } else if (collectReverse == false) {
            collectorMotor.set(0);
        }
        
        //Maneuvers: (trigger on left is half turn, trigger on right is quarter turn)
        boolean quarterTurnLeft = leftJoystick.getRawButton(1);
        boolean quarterTurnRight = rightJoystick.getRawButton(1);
        boolean halfTurnLeft = leftJoystick.getRawButton(2);
        boolean halfTurnRight = rightJoystick.getRawButton(2);

        //does a clockwise quarter turn quickly 
        if (quarterTurnRight == true) {
            for (int i = 1; i <= endLoopDrive; i++) { ///takes 1/10th of a second reach full speed
                drive.drive(0, (i / endLoopDrive));
                Timer.delay(.01);
            }
            drive.drive(0, 1);
            Timer.delay(.4);
            drive.drive(0, 0);
        }
        //does a counter-clockwise quarter turn quickly
        if (quarterTurnLeft == true) {
            for (int i = 1; i <= endLoopDrive; i++) { //takes 1/10th of a second reach full speed
                drive.drive(0, -(i / endLoopDrive));
                Timer.delay(.01);
            }
            drive.drive(0, -1);
            Timer.delay(.4);
            drive.drive(0, 0);
        }
        if (halfTurnRight == true){
            for (int i = 1; i <= endLoopDrive; i++) { ///takes 1/10th of a second reach full speed
                drive.drive(0, (i / endLoopDrive));
                Timer.delay(.01);
            }
            drive.drive(0, 1);
            Timer.delay(.8);
            drive.drive(0, 0);
        }
       if (halfTurnLeft == true){
            for (int i = 1; i <= endLoopDrive; i++) { ///takes 1/10th of a second reach full speed
                drive.drive(0, (i / endLoopDrive));
                Timer.delay(.01);
            }
            drive.drive(0, -1);
            Timer.delay(.8);
            drive.drive(0, 0);
        }
        //Display rate of encoder to the dashboard
        SmartDashboard.putNumber("Encoder Rate", rightDriveEncoder.getRate());
    }
     

    public void disabledInit() {

    }

    public void disabledPeriodic() {

    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

    }

}
