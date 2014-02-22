/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.team3182.main;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Peter
 *
 */
public class Shooter extends Object implements Runnable {

    public static boolean shootCommand;
    private Talon shooterMotors;
    private Compressor compressor;

    public Shooter() {
        shooterMotors = new Talon(4);
        shootCommand = false;
        compressor = new Compressor(7, 1);
        compressor.start();
    }

    private void shoot() {

        compressor.stop();
        Collector.collectCommand = true;
        Timer.delay(.25);
        Collector.collectInCommand = true;
        Timer.delay(.3);
        Collector.collectOutCommand = true;
        Timer.delay(.45);
        shooterMotors.set(1);
        Timer.delay(1.4);
        shooterMotors.set(0);
        Timer.delay(.5);
        //start reload
        // collectorMotor.set(0);
        // remember to set negative
        shooterMotors.set(-.15);
        Timer.delay(1.5);
        shooterMotors.set(0);
        compressor.start();
    }

    public void run() {
        while (true) {
            if (shootCommand) {
                shoot();
                shootCommand = false;  
            }
            shootToDashboard();
            Timer.delay(.2);

        }
    }
    
    private void shootToDashboard(){
        SmartDashboard.putBoolean("Shoot Command",shootCommand );
        SmartDashboard.putNumber("Shooter Motor Value", shooterMotors.get());
    }
}
