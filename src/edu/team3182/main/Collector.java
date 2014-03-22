package edu.team3182.main;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Collector extends Object implements Runnable {

    private final DriverStation driverStation;
    private volatile boolean collectMotorCommand;
    private volatile boolean collectorInCommand;
    private volatile boolean collectorOutCommand;
    private volatile boolean passMotorCommand;
    private final DoubleSolenoid leftCollector;
    private final DoubleSolenoid rightCollector;
    private final Talon collectorMotor;
    private boolean collectMotorVar = false;

    public Collector() {
        //initializing everything
        leftCollector = new DoubleSolenoid(1, 2);
        rightCollector = new DoubleSolenoid(3, 4);
        collectorMotor = new Talon(3);
        collectMotorCommand = false;
        collectorInCommand = false;
        collectorOutCommand = false;
        passMotorCommand = false;
        driverStation = DriverStation.getInstance();
    }

    public synchronized DoubleSolenoid.Value getRightCollectorValue() {
        return rightCollector.get();
    }

    public void run() {

        while (true) {
            if (!driverStation.isEnabled()) {
                Timer.delay(.1);
                continue;
            }
            if (collectorMotor.isAlive()) {
                collectMotorVar = true;
            }

            //when button 10 is let go, the toggle will comence
            if (collectorInCommand && !collectorOutCommand) {
                collectorIn();
            }

            //when button 11 is let go, the toggle will comence
            if (collectorOutCommand && !collectorInCommand) {
                collectorOut();
            }
            //if collectorOutCommand and collectorInCommand are both pressed send a message to dashboard
            if (collectorOutCommand && collectorInCommand) {
                SmartDashboard.putString("Collect button error", "Both Collect buttons pressed");
            }
            //if collectCommand is pressed and passCommand is not pressed run the collect method
            if (collectMotorCommand && !passMotorCommand) {
                //move collectorMoter to collect ball
                moveCollectorMotor(1);
            }
            //if passCommand is pressed and collectCommand is not, run the pass method
            if (passMotorCommand && !collectMotorCommand) {
                //move collectorMotor to pass
                moveCollectorMotor(-1);
            }
            if (!passMotorCommand && !collectMotorCommand) {
                //stop collectorMotor
                moveCollectorMotor(0);
            }

            if (passMotorCommand && collectMotorCommand) {
                //if both passCommand and collectCommand are pressed, send a message to dashboard
                SmartDashboard.putString("Collect motor error", "Both collect buttons are pressed");
            }
            //print all of the vars to smartboard
            collectToDashboard();
            Timer.delay(.2);
        }
    }

    public void setCollectMotorCommand(boolean collectMotorCommand) {
        this.collectMotorCommand = collectMotorCommand;
    }

    public void setCollectorInCommand(boolean collectorInCommand) {
        this.collectorInCommand = collectorInCommand;
    }

    public void setCollectorOutCommand(boolean collectorOutCommand) {
        this.collectorOutCommand = collectorOutCommand;
    }

    public void setPassCommand(boolean passCommand) {
        this.passMotorCommand = passCommand;
    }

    private void collectorIn() {
        collectorMotor.set(.8);
        rightCollector.set(DoubleSolenoid.Value.kForward);
        leftCollector.set(DoubleSolenoid.Value.kForward);
        Timer.delay(.5);
        collectorMotor.set(0);
    }

    private void collectorOut() {
        collectorMotor.set(.8);
        rightCollector.set(DoubleSolenoid.Value.kReverse);
        leftCollector.set(DoubleSolenoid.Value.kReverse);
        Timer.delay(.5);
        collectorMotor.set(0);
    }

    private void moveCollectorMotor(double x) {
        collectorMotor.set(x);
    }

    private void collectToDashboard() {
        SmartDashboard.putBoolean("isAlive True?", collectMotorVar);
        SmartDashboard.putNumber("Collector Motor value", collectorMotor.get());
        SmartDashboard.putString("Collector Solenoid Right", toString(rightCollector));
        SmartDashboard.putString("Collector Solenoid Left", toString(leftCollector));
    }

    public static String toString(DoubleSolenoid solenoid) {
    //Override toString
        String str;
        if (solenoid.get() == DoubleSolenoid.Value.kForward) {
            str = "Collector Foward";
        } else if (solenoid.get() == DoubleSolenoid.Value.kReverse) {
            str = "Collector Reverse";
        } else {
            str = "Collector OFF";
        }
        return str;
    }
}
