package edu.team3182.main;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Object implements Runnable {

    private final DoubleSolenoid leftShifter;
    private final DoubleSolenoid rightShifter;
    private final RobotDrive drive;
    private final Joystick rightJoystick;
    private final Joystick leftJoystick;
    private final DriverStation driverStation;
    private volatile boolean rightShifterCommand;
    private volatile boolean leftShifterCommand;
    private volatile boolean joystickStateCommand;
    private volatile double rightMotorCommand;
    private volatile double leftMotorCommand;
    boolean forwardCommand;
    boolean backwardCommand;
    double smoothVarRight = 0; //for making joysticks linear function between of zero to 1
    double smoothVarLeft = 0;
    double p = 0.10; //dead zone of joysticks for drive is between -p and p

    public DriveTrain() {
        //initializing everything
        drive = new RobotDrive(1, 2);
        drive.setSafetyEnabled(false);
        rightJoystick = new Joystick(1);
        leftJoystick = new Joystick(2);
        leftShifter = new DoubleSolenoid(5, 6);
        rightShifter = new DoubleSolenoid(7, 8);
        rightShifterCommand = false;
        leftShifterCommand = false;
        rightMotorCommand = 0;
        leftMotorCommand = 0;
        joystickStateCommand = false;
        driverStation = DriverStation.getInstance();
    }

    public void run() {

        while (true) {
            boolean isEna = driverStation.isEnabled();
            //if joystickStateCommand is true, initialize all this stuff
            if (joystickStateCommand) {
                rightMotorCommand = rightJoystick.getAxis(Joystick.AxisType.kY);
                leftMotorCommand = leftJoystick.getAxis(Joystick.AxisType.kY);
                rightShifterCommand = rightJoystick.getRawButton(1);
                leftShifterCommand = leftJoystick.getRawButton(1);
            }
            if (isEna) {
                //shifter code
                //while both of the triggers are clicked, the shifter are switched to high gear
                if (rightShifterCommand && leftShifterCommand) {
                    // if (rightShifter.get() == DoubleSolenoid.Value.kReverse) {
                    shiftHigh();
                }
                
                if (rightShifterCommand == false && leftShifterCommand == false) {
                    // if (leftShifter.get() == DoubleSolenoid.Value.kForward) {
                    shiftLow();
                }

                /*=================================================================
                 makes sure joystick will not work at +/-10% throttle
                 smoothVarRight/Left are output variables from a function
                 to get power from 0 to 1 between P and full throttle on the joysticks
                 same for full reverse throttle to -P
                 =================================================================*/
                if (rightMotorCommand < p && rightMotorCommand > (-p)) {
                    smoothVarRight = 0;
                }
                if (leftMotorCommand < p && leftMotorCommand > (-p)) {
                    smoothVarLeft = 0;
                }
                // yAxisLeft greater than P, which is pull back on the joystick
                if (leftMotorCommand >= p) {
                    smoothVarLeft = ((1 / (1 - p)) * leftMotorCommand + (1 - (1 / (1 - p))));
                }
                // yAxisLeft less than -P, which is push forward on the joystick 
                if (leftMotorCommand <= (-p)) {
                    smoothVarLeft = ((1 / (1 - p)) * leftMotorCommand - (1 - (1 / (1 - p))));
                }
                //smooth right joystick
                // yAxisRight greater than P, which is pull back on the joystick 
                if (rightMotorCommand >= p) {
                    smoothVarRight = ((1 / (1 - p)) * rightMotorCommand + (1 - (1 / (1 - p))));
                }
                // yAxisLeft less than -P, which is push forward on the joystick 
                if (rightMotorCommand <= (-p)) {
                    smoothVarRight = ((1 / (1 - p)) * rightMotorCommand - (1 - (1 / (1 - p))));
                }
                //drive using the joysticks
                drive.tankDrive(-smoothVarLeft, -smoothVarRight);
            }
            
            driveToDashboard();
            Timer.delay(.15);
        }
    }

    public synchronized void setRightShifterCommand(boolean rightShifterCommand) {
        this.rightShifterCommand = rightShifterCommand;
    }

    public synchronized void setLeftShifterCommand(boolean leftShifterCommand) {
        this.leftShifterCommand = leftShifterCommand;
    }
    
    public synchronized void setJoystickStateCommand(boolean joystickStateCommand) {
        this.joystickStateCommand = joystickStateCommand;
    }

    public synchronized void setRightMotorCommand(double rightMotorCommand) {
        this.rightMotorCommand = rightMotorCommand;
    }

    public synchronized void setLeftMotorCommand(double leftMotorCommand) {
        this.leftMotorCommand = leftMotorCommand;
    }

    private void shiftHigh() {
        leftShifter.set(DoubleSolenoid.Value.kForward);
        rightShifter.set(DoubleSolenoid.Value.kForward);
    }

    private void shiftLow() {
        leftShifter.set(DoubleSolenoid.Value.kReverse);
        rightShifter.set(DoubleSolenoid.Value.kReverse);
    }

    private void driveToDashboard() {
        SmartDashboard.putString("Left Shifter", leftShifter.toString());
        SmartDashboard.putString("Right Shifter", rightShifter.toString());
        SmartDashboard.putNumber("leftMotorCommand", leftMotorCommand);
        SmartDashboard.putNumber("rightMotorCommand", rightMotorCommand);
        SmartDashboard.putBoolean("Right Trigger Shifter Command", rightShifterCommand);
        SmartDashboard.putBoolean("Left Trigger Shifter Command", leftShifterCommand);
        SmartDashboard.putNumber("Smooth Var Left", smoothVarLeft);
        SmartDashboard.putNumber("Smooth Var Right", smoothVarRight);
        SmartDashboard.putBoolean("Joystick state", joystickStateCommand);
    }
}
