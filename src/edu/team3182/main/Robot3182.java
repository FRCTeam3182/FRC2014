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
import edu.wpi.first.wpilibj.Compressor;
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
    //Initialization of code for robot drive functions
    private RobotDrive drive;
    private Joystick rightJoystick;
    private Joystick leftJoystick;

    //Initialization of code for robot appendage functions
    private Joystick buttonsJoystick;
    private Talon shooterMotors;
    private Talon collectorMotor;
    private Solenoid leftShifter;
    private Solenoid rightShifter;
    private Solenoid leftCollector;
    private Solenoid rightCollector;
    private Compressor compressor;
    // Initialization of code for robot sensors
    private Encoder rightDriveEncoder;
    private Encoder leftDriveEncoder;
    public SmartDashboard dash;
    //private AnalogPotentiometer shooterPot;

    // Initialize variables to support functions above
    // yAxisLeft/Right read in values of joysticks, values of joysticks are output inversely like airplane drive 
    double yAxisRight;
    double yAxisLeft;
    double distance;
    boolean toggleOut;
    boolean toggleIn;
    boolean collectorButton10;
    boolean collectorButton11;
    boolean shoot = false;
    boolean reverseShooter = false;
    boolean collect = false;
    boolean collectReverse = false;
    boolean collectorFoward = false;
    boolean quarterTurnLeft = false;
    boolean quarterTurnRight = false;
    boolean halfTurnLeft = false;
    boolean halfTurnRight = false;
    boolean rightTrigger = false;
    boolean leftTrigger = false;
    double p = 0.25; //dead zone of joysticks for drive is between -p and p
    double smoothVarRight = 0; //for making joysticks linear function between of zero to 1
    double smoothVarLeft = 0;
    final int endLoopDrive = 10; //length of for loops that control maneuver timing/ shooting timing
    final int endLoopShoot = 10;
    //int shooterPotVal; //position of catapult

    //Coefficients of exponential function to ramp up speed of catapult (so ball doesn't fall out)
    final double a = .005;
    final double b = .9;
    boolean isReloading = false; //prevents shooting when reloading

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

        //UNCOMMENT WHEN remainder of electronics board is complete
//        shooterMotors = new Talon(1);
//        collectorMotor = new Talon(2);
        //UNCOMMENT WHEN potentiometer is hooked up
        //shooterPot = new AnalogPotentiometer(1);
        rightDriveEncoder = new Encoder(4, 3);
        leftDriveEncoder = new Encoder(2, 1);
        rightDriveEncoder.reset();
        rightDriveEncoder.setDistancePerPulse(.08168);

        // UNCOMMENT WHEN solenoids are available on electronics board
        leftShifter = new Solenoid(2, 6);
        rightShifter = new Solenoid(2, 8);
//        leftCollector = new Solenoid(2, 2);
//        rightCollector = new Solenoid(2, 4);
//=================Needs Change:================================
//        compressor = new Compressor(0,0);

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
        //Drive forward for 2 seconds with linear acceleration function
        for (int i = 1; i <= 30; i++) { //takes 1.5 seconds reach full speed
            drive.drive(0, (i / 100));
            Timer.delay(.05);
        }

        drive.drive(0.3, 0.0);
        Timer.delay(1.0);
        drive.drive(0.0, 0.0);

        //Shoot:
        // SHOULD WE ADD LOGIC TO TURN AROUND AFTER FIRING
        //quickly speed up motors, then wait for the ball to be shot
        for (int i = 1; i <= endLoopShoot; i++) { //takes half a second to reach full speed
            shooterMotors.set(a * (MathUtils.exp(b * i)));
            Timer.delay(.01);
        }
        shooterMotors.set(1);
        Timer.delay(.1);
        shooterMotors.set(0);
        Timer.delay(1);
        shooterMotors.set(-.3);
        Timer.delay(.25);
        shooterMotors.set(0);

        //----------------------------------------------------------------------
        //Possibly add additional code here to turn the robot around to prep for
        //teleop period
        //----------------------------------------------------------------------
    }

    public void autonomousPeriodic() {

        //what is this for?? - RJJ
        Timer.delay(.01);
    }

    /**
     * Called when the robot enters the teleop period for the first time. This
     * is called on a transition from any other state.
     */
    public void teleopInit() {
        rightDriveEncoder.start();
        leftDriveEncoder.start();
        compressor.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //----------------------------------------------------------------------
        // T E L E O P    D R I V E    C O D E
        //----------------------------------------------------------------------
       
        SmartDashboard.putBoolean("Collector Extended: ", toggleOut);
        
        // Read commands from the joysticks
        //sets yAxisRight and yAxisLeft to the axis of corresponding joysticks
        yAxisRight = rightJoystick.getAxis(Joystick.AxisType.kY);
        yAxisLeft = leftJoystick.getAxis(Joystick.AxisType.kY);

        //shoot is button 1, collect is 2, ground pass/dump is 3
        // collector is buttons 10 (out) and 11 (in)
        shoot = buttonsJoystick.getRawButton(1);
        collect = buttonsJoystick.getRawButton(2);
        collectReverse = buttonsJoystick.getRawButton(3);
        collectorButton10 = buttonsJoystick.getRawButton(10);
        collectorButton11 = buttonsJoystick.getRawButton(11);

        //Maneuvers (trigger on left is half turn, trigger on right is quarter turn)
        //NOTE: Reloading will be stopped when a maneuver is activated
        //NOTE: Maneuvers will not be activated if the collector motor is on
        //Buttons changed to 2 and 3, trigger is shifters
        rightTrigger = rightJoystick.getRawButton(1);
        leftTrigger = leftJoystick.getRawButton(1);
        quarterTurnLeft = leftJoystick.getRawButton(2);
        quarterTurnRight = rightJoystick.getRawButton(2);
        halfTurnLeft = leftJoystick.getRawButton(3);
        halfTurnRight = rightJoystick.getRawButton(3);

        // collector code 
        // if button 10 is pressed the collector will come out
        // if button 11 is pressed the collector will come in
        if (collectorButton10 == true) {
            toggleOut = true;
        } else if (collectorButton11 == true) {
            toggleIn = true;
        } else if (toggleOut && !collectorButton10) {
            rightCollector.set(true);
            leftCollector.set(true);
            toggleOut = false;
        } else if (toggleIn && !collectorButton11) {
            rightCollector.set(false);
            leftCollector.set(false);
            toggleIn = false;
        }

        //shifter code
        //while one of the triggers are clicked, the shifters activate
        if (rightTrigger && leftTrigger) {
            if (rightShifter.get() == false) {
                leftShifter.set(true);
                rightShifter.set(true);
            }
        } else if (rightTrigger == false && leftTrigger == false) {
            if (leftShifter.get() == true) {
                leftShifter.set(false);
                rightShifter.set(false);
            }
        }

        //makes sure joystick will not work at +/-25% throttle
        //smoothVarRight/Left are output variables from a function
        // to get power from 0 to 1 between P and full throttle on the joysticks
        // same for full reverse throttle to -P
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
        if (quarterTurnRight == true && collect == false && collectReverse == false) {
            shooterMotors.set(0); //prevents the shooter from running longer than it should when reloading
            for (int i = 1; i <= endLoopDrive; i++) { ///takes 1/10th of a second reach full speed
                drive.drive(0, (i / endLoopDrive));
                Timer.delay(.01);
            }
            drive.drive(0, 1);
            Timer.delay(.4);
            drive.drive(0, 0);
        }
        //does a counter-clockwise 90 degree turn quickly
        if (quarterTurnLeft == true && collect == false && collectReverse == false) {
            shooterMotors.set(0); //prevents the shooter from running longer than it should when reloading
            for (int i = 1; i <= endLoopDrive; i++) { //takes 1/10th of a second reach full speed
                drive.drive(0, -(i / endLoopDrive));
                Timer.delay(.01);
            }
            drive.drive(0, -1);
            Timer.delay(.4);
            drive.drive(0, 0);
        }
        if (halfTurnRight == true && collect == false && collectReverse == false) {
            shooterMotors.set(0); //prevents the shooter from running longer than it should when reloading
            for (int i = 1; i <= endLoopDrive; i++) { ///takes 1/10th of a second reach full speed
                drive.drive(0, (i / endLoopDrive));
                Timer.delay(.01);
            }
            drive.drive(0, 1);
            Timer.delay(.8);
            drive.drive(0, 0);
        }
        if (halfTurnLeft == true && collect == false && collectReverse == false) {
            shooterMotors.set(0); //prevents the shooter from running longer than it should when reloading
            for (int i = 1; i <= endLoopDrive; i++) { ///takes 1/10th of a second reach full speed
                drive.drive(0, -(i / endLoopDrive));
                Timer.delay(.01);
            }
            drive.drive(0, -1);
            Timer.delay(.8);
            drive.drive(0, 0);
        }

        //----------------------------------------------------------------------
        // T E L E O P    S H O O T    C O D E
        //----------------------------------------------------------------------
        //Shooting   
        //NOTE: You CANNOT shoot when the catapult is reloading or when the collector spinning in reverse
        if (shoot == true && isReloading == false && collectReverse == false) {

            for (int i = 1; i <= endLoopShoot; i++) { //takes half a second to reach full speed
                shooterMotors.set(a * MathUtils.exp(b * i));
                Timer.delay(.01);
            }
            shooterMotors.set(1);
            Timer.delay(.1);
            shooterMotors.set(0);
            Timer.delay(.5);
//          //start reload
//            shooterMotors.set(-.2);
//            isReloading = true; //prevents shooting when being reloaded
//          
//        } 
//        else if (shoot == false && isReloading == false) {
//            shooterMotors.set(0);
//        }
//        //continues reloading if it was stopped
//        if (shooterPotVal > 500){
//            shooterMotors.set(-.2);
//        }
//        //finish reload
//        shooterPotVal = (int) shooterPot.get();
//        if (shooterPotVal <= 500){
//            shooterMotors.set(-.05);
//        }
//        if (shooterPotVal <= 300){
//              shooterMotors.set(0);
//              isReloading = false;
//        }

        // if button 2 on support function joystick is pressed, run the collector motor at 90%
            // if button 3 on support function joystick is pressed, run the collector motor in reverse at 90% (ground pass)
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

        //Display rate of encoder to the dashboard
            //SmartDashboard.putNumber("Encoder Rate", rightDriveEncoder.getRate());
            System.out.println(distance);
            System.out.println(rightDriveEncoder.get());
            System.out.println("Encoder rate: " + rightDriveEncoder.getRate());
            System.out.println("Encoder rate left: " + leftDriveEncoder.getRate());
        }

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
