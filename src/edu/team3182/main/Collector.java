/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.team3182.main;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nodcah
 */
public class Collector extends Object implements Runnable {

    private final DriverStation driverStation;
    private volatile boolean collectCommand;
    private volatile boolean collectInCommand;
    private volatile boolean collectOutCommand;
    private volatile boolean passCommand;
    private final DoubleSolenoid leftCollector;
    private final DoubleSolenoid rightCollector;
    private final Talon collectorMotor;
    private boolean collectMotorVar = false;
    
    public Collector() {
        leftCollector = new DoubleSolenoid(1, 2);
        rightCollector = new DoubleSolenoid(3, 4);
        collectorMotor = new Talon(3);
        collectCommand = false;
        collectInCommand = false;
        collectOutCommand = false;
        passCommand = false;
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
            if (collectInCommand && !collectOutCommand) {
                collectIn();
            }

            //when button 11 is let go, the toggle will comence
            if (collectOutCommand && !collectInCommand) {
                collectOut();
            }
            //if collectOutCommand and collectInCommand are both pressed send a message to dashboard
            if (collectOutCommand && collectInCommand) {
                SmartDashboard.putString("Collect button error", "Both Collect buttons pressed");
            }
            //if collectCommand is pressed and passCommand is not pressed run the collect method
            if (collectCommand && !passCommand) {
                collect(1);
            }
            if (!collectCommand){
                collect(0);
            }
            //if passCommand is pressed and collectCommand is not, run the pass method
            if (passCommand && !collectCommand) {
                pass(-1);
            }
            if (!passCommand) {
                pass(0);
            }
            
            //if both passCommand and collectCommand are pressed, send a message to dashboard
            if (passCommand && collectCommand) {
                SmartDashboard.putString("Collect motor error", "Both collect buttons are pressed");
            }
            collectToDashboard();
            Timer.delay(.2);
        }

    }

    public void setCollectCommand(boolean collectCommand) {
        this.collectCommand = collectCommand;
    }

    public void setCollectInCommand(boolean collectInCommand) {
        this.collectInCommand = collectInCommand;
    }

    public void setCollectOutCommand(boolean collectOutCommand) {
        this.collectOutCommand = collectOutCommand;
    }

    public void setPassCommand(boolean passCommand) {
        this.passCommand = passCommand;
    }

    private void collectIn() {
        collectorMotor.set(.8);
        rightCollector.set(DoubleSolenoid.Value.kForward);
        leftCollector.set(DoubleSolenoid.Value.kForward);
    }

    private void collectOut() {
        collectorMotor.set(.8);
        rightCollector.set(DoubleSolenoid.Value.kReverse);
        leftCollector.set(DoubleSolenoid.Value.kReverse);
    }

    private void collect(double x) {
        collectorMotor.set(x);
    }

    private void pass(double x) {
        collectorMotor.set(x);
    }

    private void collectToDashboard() {
        SmartDashboard.putBoolean("isAlive True?", collectMotorVar);
        SmartDashboard.putNumber("Collector Motor value", collectorMotor.get());
        SmartDashboard.putString("Collector Solenoid Right", toString(rightCollector));
        SmartDashboard.putString("Collector Solenoid Left", toString(leftCollector));
    }

    public static String toString(DoubleSolenoid solenoid) {
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
