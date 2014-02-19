/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.team3182.main;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nodcah
 */
public class Collector extends Object implements Runnable {

    public static boolean collectCommand;
    public static boolean collectInCommand;
    public static boolean collectOutCommand;
    public static boolean passCommand;
    private DoubleSolenoid leftCollector;
    private DoubleSolenoid rightCollector;
    private Talon collectorMotor;

    public Collector() {
        leftCollector = new DoubleSolenoid(1, 2);
        rightCollector = new DoubleSolenoid(3, 4);
        collectorMotor = new Talon(3);
        collectorMotor.setSafetyEnabled(true);
        collectCommand = false;
        collectInCommand = false;
        collectOutCommand = false;
        passCommand = false;
    }

    public void run() {
        while (true) {
            //when button 10 is let go, the toggle will comence
            if (collectInCommand && !collectOutCommand) {
                collectIn();
            }
            
            //when button 11 is let go, the toggle will comence
            if (collectOutCommand && !collectInCommand) {
                collectOut();
            }
            
            if (collectOutCommand && collectInCommand){
                SmartDashboard.putString("Collect button error", "Both Collect buttons pressed");
            }

            if (collectCommand && !passCommand) {
                collect();
            }
            if (passCommand && !collectCommand) {
                pass();
            }
            if (passCommand && collectCommand){
                SmartDashboard.putString("Collect motor error", "Both collect buttons are pressed");
            }
            Timer.delay(.1);
        }
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

    private void collect() {
        //sendArduino(false, true, false, false);
        collectorMotor.set(1);

    }

    private void pass() {

        collectorMotor.set(-.9);

    }

}
