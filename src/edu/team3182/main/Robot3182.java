/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//sooting collector motors
//auto collector motors
//auto driving
//lights
//Stuff to test:
//-Ultrasonic
//-Pivoting

package edu.team3182.main;

import com.sun.squawk.util.Arrays;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.lang.Thread;
import edu.wpi.first.wpilibj.communication.Semaphore;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot3182 extends IterativeRobot {

    private DriveTrain driveTrainVar;
    private Collector collectVar;
    private Shooter shooterVar;
    private Sensors sensorsVar;
    private ArduinoLights arduinoLightsVar;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    //Initialization of code for robot drive functions
    private DriverStation driverStation;
    private RobotDrive drive;
    private Joystick rightJoystick;
    private Joystick leftJoystick;
    //Initialization of code for robot appendage functions
    private Joystick buttonsJoystick;
    private Talon shooterMotors;
    private Talon collectorMotor;
    private DoubleSolenoid leftShifter;
    private DoubleSolenoid rightShifter;
    private DoubleSolenoid leftCollector;
    private DoubleSolenoid rightCollector;
    private Compressor compressor;
    // Initialization of code for robot sensors
    private Encoder rightDriveEncoder;
    private Encoder leftDriveEncoder;
    public SmartDashboard dash;
    private DigitalOutput arduinoSignal;
    private DigitalOutput arduinoSignifier;
    private AnalogChannel rangeFinder;

    // Initialize variables to support functions above
    // yAxisLeft/Right read in values of joysticks, values of joysticks are output inversely like airplane drive 
    double yAxisRight;
    double yAxisLeft;
    double distance; //ultrasonic
    boolean toggleOut;
    boolean toggleIn;
    boolean collectorButton11;
    boolean collectorButton9;
    boolean signalLight = false;
    boolean killLights;
    boolean shoot = false;
    boolean reverseShooter = false;
    boolean collect = false;
    boolean collectReverse = false;
    boolean collectorFoward = false;
    boolean quarterTurnLeft = false;
    boolean quarterTurnRight = false;
    boolean halfTurnRight = false;
    boolean rightTrigger = false;
    boolean leftTrigger = false;
    boolean limitStat;
    
    boolean isPassing;
    boolean isShooting;
    boolean isCollecting;
    
    boolean[] lightData = new boolean[]{false, false, false, false};
    boolean[] dummy = new boolean[4];
    boolean isSame = false;
    double p = 0.10; //dead zone of joysticks for drive is between -p and p
    double smoothVarRight = 0; //for making joysticks linear function between of zero to 1
    double smoothVarLeft = 0;

    int shooterPotVal; //position of catapult
    double distanceRange;
    double getVoltage;
    double getAverageVoltage;

    //Coefficients of exponential function to ramp up speed of catapult (so ball doesn't fall out)
    final double a = .005;
    final double b = .9;

    /**
     * Called when the robot is first turned on. This is a substitute for using
     * the constructor in the class for consistency. This method is only called
     * once
     */
    public void robotInit() {
        driveTrainVar = new DriveTrain();
        new Thread(driveTrainVar, "DriveTrain").start();
        collectVar = new Collector();
        new Thread(collectVar, "Collector").start();
        shooterVar = new Shooter(collectVar);
        new Thread(shooterVar, "Shooter").start();
        sensorsVar = new Sensors();
        new Thread(sensorsVar, "Sensors").start();
        arduinoLightsVar = new ArduinoLights(sensorsVar);
        new Thread(arduinoLightsVar, "Arduino").start();

        //camera = AxisCamera.getInstance();
        // drive = new RobotDrive(1, 2);
        // drive.setSafetyEnabled(false);
        // rightJoystick = new Joystick(1);
        //leftJoystick = new Joystick(2);
        buttonsJoystick = new Joystick(3);
//        arduinoSignal = new DigitalOutput(5); //data line
//        arduinoSignifier = new DigitalOutput(6); //tells arduino when to read data
        /*------------------------------------------------------------
         Stuff below is disabled to test threads. Uncomment to run 
         Robot main.
         *------------------------------------------------------------*/
        //Motors and stuff
        // shooterMotors = new Talon(4);
        //collectorMotor = new Talon(3);
        //collectorMotor.setSafetyEnabled(true);
        //shooterMotors.setSafetyEnabled(false);

        //Encoders and stuff
        //rightDriveEncoder = new Encoder(4, 3);
        //leftDriveEncoder = new Encoder(2, 1);
        //rightDriveEncoder.reset();
        //rightDriveEncoder.setDistancePerPulse(.08168);
        //Solenoids
//        leftShifter = new DoubleSolenoid(5, 6);
//        rightShifter = new DoubleSolenoid(7, 8);
//        leftCollector = new DoubleSolenoid(1, 2);
//        rightCollector = new DoubleSolenoid(3, 4);
        //Range Finder and compressor
//        rangeFinder = new AnalogChannel(1, 2);
//        compressor = new Compressor(7, 1);
//        compressor.start();
    }
    
    public void disabledInit() {
        driveTrainVar.setLeftMotorCommand(0);
        driveTrainVar.setRightMotorCommand(0);
        shooterVar.setShootCommand(false);
        collectVar.setCollectCommand(false);
        collectVar.setCollectInCommand(false);
        collectVar.setCollectOutCommand(false);
    }

    /**
     * Called when the robot enters the autonomous period for the first time.
     * This is called on a transition from any other state.
     */
    public void autonomousInit() {
        //disable jystick command over the wheels
        driveTrainVar.setJoystickStateCommand(false);

        //Send command to Arduino for the light strip
        // sendArduino(true, false, true, false); //charging animation
        //sendArduino(false, false, false, false); //stop it imediatly after it finishes
        //drive forward
        driveTrainVar.setRightMotorCommand(-.71);
        driveTrainVar.setLeftMotorCommand(-.7);
        Timer.delay(1.7);
        driveTrainVar.setRightMotorCommand(-.6);
        driveTrainVar.setLeftMotorCommand(-.6);
        Timer.delay(1.5);
        driveTrainVar.setRightMotorCommand(-.5);
        driveTrainVar.setLeftMotorCommand(-.5);
        Timer.delay(.3);
        driveTrainVar.setRightMotorCommand(0);
        driveTrainVar.setLeftMotorCommand(0);
//        collectVar.setCollectCommand(true);
        collectVar.setCollectOutCommand(true);
        Timer.delay(1.5);
        collectVar.setCollectCommand(false);
        //Shoot:
        //quickly speed up motors, then wait for the ball to be shot
        shooterVar.setShootCommand(true);

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
        driveTrainVar.setJoystickStateCommand(true);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
      
        isShooting = buttonsJoystick.getRawButton(1); //shooting
        isCollecting = buttonsJoystick.getRawButton(3); //collect
        isPassing = buttonsJoystick.getRawButton(5); //pass
        
        collectVar.setCollectInCommand(buttonsJoystick.getRawButton(11));
        collectVar.setCollectOutCommand(buttonsJoystick.getRawButton(9));
        collectVar.setCollectCommand(isCollecting);
        collectVar.setPassCommand(isPassing);
        shooterVar.setShootCommand(isShooting);
        arduinoLightsVar.signal(buttonsJoystick.getRawButton(4));
        arduinoLightsVar.kill(buttonsJoystick.getRawButton(6));
        arduinoLightsVar.isPassing(isPassing);
        arduinoLightsVar.isShooting(isShooting);
        arduinoLightsVar.isCollecting(isCollecting);
   /*
        //----------------------------------------------------------------------
        // T E L E O P    D R I V E    C O D E
        //----------------------------------------------------------------------
        SmartDashboard.putBoolean("Collector Extended: ", toggleOut);
        //===================================================
        // team color
        //===================================================
        if (driverStation.getAlliance() == DriverStation.Alliance.kBlue) {
            // color to blue
            sendArduino(true, true, true, false);
        } else {
            //color to red
            sendArduino(true, true, false, true);

        }
        //---------------------------------------------------------------------
        //testing voltage for analog rangefinder
        //---------------------------------------------------------------------
        distance = rightDriveEncoder.getDistance();
        getAverageVoltage = rangeFinder.getAverageVoltage();
        getVoltage = rangeFinder.getVoltage();
        SmartDashboard.putNumber("Distance away: ", distanceRange);
        //System.out.println("Average Voltage: " + getAverageVoltage);
        System.out.println("Get Voltage : " + getVoltage);

        // Read commands from the joysticks
        //sets yAxisRight and yAxisLeft to the axis of corresponding joysticks
        yAxisRight = rightJoystick.getAxis(Joystick.AxisType.kY);
        yAxisLeft = leftJoystick.getAxis(Joystick.AxisType.kY);

        //shoot is button 1, collect is 2, ground pass/dump is 3
        // collector is buttons 9 (out) and 11 (in)
        shoot = buttonsJoystick.getRawButton(1);
        collect = buttonsJoystick.getRawButton(3);
        signalLight = buttonsJoystick.getRawButton(4);
        collectReverse = buttonsJoystick.getRawButton(5);
        killLights = buttonsJoystick.getRawButton(7);
        collectorButton11 = buttonsJoystick.getRawButton(11);
        collectorButton9 = buttonsJoystick.getRawButton(9);
        //Maneuvers (trigger on left is half turn, trigger on right is quarter turn)
        //NOTE: Reloading will be stopped when a maneuver is activated
        //NOTE: Maneuvers will not be activated if the collector motor is on
        //Buttons changed to 2 and 3, trigger is shifters
        rightTrigger = rightJoystick.getRawButton(1);
        leftTrigger = leftJoystick.getRawButton(1);
        quarterTurnLeft = leftJoystick.getRawButton(2);
        quarterTurnRight = rightJoystick.getRawButton(2);
        halfTurnRight = rightJoystick.getRawButton(3);

        // collector code 
        // if button 9 is pressed the collector will come out
        // if button 10 is pressed the collector will come in
        if (collectorButton11 == true) {
            toggleOut = true;
        }
        if (collectorButton9 == true) {
            toggleIn = true;
        }
        if (toggleOut && !collectorButton11) { //when button 10 is let go, the toggle will comence
            collectIn();
            toggleOut = false;
        }
        if (toggleIn && !collectorButton9) { //when button 11 is let go, the toggle will comence
            collectOut();
            toggleIn = false;
        }

        // if button 2 on support function joystick is pressed, run the collector motor at 90%
        // if button 3 on support function joystick is pressed, run the collector motor in reverse at 90% (ground pass)
        if (collect) {
            collect();
            sendArduino(false, true, false, false);
        }
        if (collectReverse) {
            pass();
            sendArduino(false, true, true, true);
        }

        //shifter code
        //while both of the triggers are clicked, the shifter are switched to high gear
        if (rightTrigger && leftTrigger) {
            // if (rightShifter.get() == DoubleSolenoid.Value.kReverse) {
            shiftHigh();
        }
        if (rightTrigger == false && leftTrigger == false) {
            // if (leftShifter.get() == DoubleSolenoid.Value.kForward) {
            shiftLow();

        }

        /*-------------------------------------------------------------
        makes sure joystick will not work at +/-10% throttle
        smoothVarRight/Left are output variables from a function
        to get power from 0 to 1 between P and full throttle on the joysticks
        same for full reverse throttle to -P
         -------------------------------------------------------------
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
        drive.tankDrive(-smoothVarLeft, -smoothVarRight);

        //does a clockwise 90 degree turn quickly 
        if (quarterTurnRight == true && collect == false && collectReverse == false) {
            pivot(90);
        }
        //does a counter-clockwise 90 degree turn quickly
        if (quarterTurnLeft == true && collect == false && collectReverse == false) {
            pivot(-90);
        }
        if (halfTurnRight == true && collect == false && collectReverse == false) {
            pivot(180);
        }

        //----------------------------------------------------------------------
        // T E L E O P    S H O O T    C O D E
        //----------------------------------------------------------------------
        //Shooting   
        //NOTE: You CANNOT shoot when the catapult is reloading OR when the collector spinning in reverse OR when the collector is in
        if (shoot == true && collectReverse == false && rightCollector.get() == DoubleSolenoid.Value.kReverse) {
            shoot();
            sendArduino(false, true, false, false);
        }

        if (signalLight) {
            //make LED some color as a signal to other teams
            sendArduino(false, false, true, true);
        }

        if (killLights) {
            //kills the lights until reset
            sendArduino(true, true, true, true);
        }
        //Display rate of encoder to the dashboard
        SmartDashboard.putNumber("Speed", rightDriveEncoder.getRate());
        SmartDashboard.putNumber("Speed", leftDriveEncoder.getRate());
        /*===========================
         Sensor to arduino code
         VALUES MUST BE CHANGED
         =============================
        if (getVoltage >= 60 && getVoltage <= 72) {
            sendArduino(false, true, true, false); //green
        } else if (getVoltage >= 3 && getVoltage < 60) {
            sendArduino(true, false, false, false); //red
        } else if (getVoltage >= 60 && getVoltage <= 72) {
            sendArduino(false, false, true, false); //yellow
        }

        //if nothing is happening
        if (shoot == false && signalLight == false && collect == false && collectReverse == false) {
            sendArduino(false, false, false, true); //idle
        }
        */
    }

 

    public void disabledPeriodic() {

    }

    public void testInit() {
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        shooterVar.setShootCommand(buttonsJoystick.getRawButton(1));
        collectVar.setCollectCommand(buttonsJoystick.getRawButton(2));

        collectVar.setPassCommand(buttonsJoystick.getRawButton(3));

        
        collectVar.setCollectInCommand(buttonsJoystick.getRawButton(5));
        collectVar.setCollectOutCommand(buttonsJoystick.getRawButton(6));

//        if (buttonsJoystick.getRawButton(7)) {
//            shiftHigh();
//        }
//        if (buttonsJoystick.getRawButton(8)) {
//            shiftLow();
//        }
        if (buttonsJoystick.getRawButton(11)) {

            //sendArduino(true, false, false, false);
        }
//        x = buttonsJoystick.getAxis(Joystick.AxisType.kY);
//        if (x > .25) {
//            leftCollector.set(DoubleSolenoid.Value.kForward);
//            rightCollector.set(DoubleSolenoid.Value.kForward);
//        } else if (x < -.25) {
//            leftCollector.set(DoubleSolenoid.Value.kReverse);
//            rightCollector.set(DoubleSolenoid.Value.kReverse);
//        } else {
//            leftCollector.set(DoubleSolenoid.Value.kOff);
//            rightCollector.set(DoubleSolenoid.Value.kOff);
//        }

    }

    // bring shooter up then down
//    private void shoot() {
//        compressor.stop();
//        collectorMotor.set(.8);
//        Timer.delay(.25);
//        collectIn();
//        Timer.delay(.3);
//        collectOut();
//        Timer.delay(.45);
//        shooterMotors.set(1);
//        Timer.delay(1.4);
//        shooterMotors.set(0);
//        Timer.delay(.5);
//        //start reload
//        collectorMotor.set(0);
//        // remember to set negative
//        shooterMotors.set(-.15);
//        Timer.delay(1.5);
//        shooterMotors.set(0);
//        compressor.start();
//    }

    // runs collect forward relies on safety config disabling
//    private void collect() {
//       // sendArduino(false, true, false, false);
//        collectorMotor.set(.8);
//
//    }

    // runs collect backward relies on safety config disabling
//    private void pass() {
//
//        collectorMotor.set(-.9);
//
//    }

    // pivots robot by some angle, positive is right, negative is left
//    private void pivot(float angle_deg) {
//        drive.drive(1, signum(angle_deg));
//        Timer.delay(Math.abs(angle_deg / 90));
//        drive.drive(0, 0);
//    }
//
//    private int signum(float num) {
//        if (num > 0) {
//            return 1;
//        } else if (num < 0) {
//            return -1;
//        } else {
//            return 0;
//        }
//
//    }
//
//    private void collectIn() {
//        collectorMotor.set(.8);
//        rightCollector.set(DoubleSolenoid.Value.kForward);
//        leftCollector.set(DoubleSolenoid.Value.kForward);
//
//    }
//
//    private void collectOut() {
//        collectorMotor.set(.8);
//        rightCollector.set(DoubleSolenoid.Value.kReverse);
//        leftCollector.set(DoubleSolenoid.Value.kReverse);
//    }
//
//    private void shiftHigh() {
//        leftShifter.set(DoubleSolenoid.Value.kForward);
//        rightShifter.set(DoubleSolenoid.Value.kForward);
//    }
//
//    private void shiftLow() {
//        leftShifter.set(DoubleSolenoid.Value.kReverse);
//        rightShifter.set(DoubleSolenoid.Value.kReverse);
//    }

//    private void sendArduino(boolean one, boolean two, boolean three, boolean four) {
//        //the fuction to send certain data to the arduino
//        dummy = new boolean[]{one, two, three, four};
//        isSame = Arrays.equals(dummy, lightData);
//
//        if (!isSame) {
//            arduinoSignifier.set(true);
//            arduinoSignal.set(one);
//            Timer.delay(.01);
//            arduinoSignal.set(two);
//            Timer.delay(.01);
//            arduinoSignal.set(three);
//            Timer.delay(.01);
//            arduinoSignal.set(four);
//            Timer.delay(.01);
//            arduinoSignal.set(false);
//            arduinoSignifier.set(false);
//        }
//        lightData = new boolean[]{one, two, three, four};
//        System.out.println("hey");
//
//    }

//    private void getUltraRange() {
//        if (getVoltage >= 60 && getVoltage <= 72) {
//            sendArduino(false, true, true, false); //green
//        } else if (getVoltage >= 3 && getVoltage < 60) {
//            sendArduino(true, false, false, false); //red
//        } else if (getVoltage >= 60 && getVoltage <= 72) {
//            sendArduino(false, false, true, false); //yellow
//        } else if (getVoltage > 60) {
//            sendArduino(false, false, false, true); //idle
//        }
//    }

}
