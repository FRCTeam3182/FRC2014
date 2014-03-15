/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.team3182.main;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Peter
 *
 */
public class Shooter extends Object implements Runnable {

    private DriverStation driverStation;
    public volatile boolean shootCommand;
    public volatile boolean armUpCommand;
    public volatile boolean armDownCommand;

    public synchronized void setShootCommand(boolean shootCommand) {
        this.shootCommand = shootCommand;
    }
    private Talon shooterMotors;
    private Compressor compressor;
    private Collector collector;

    public Shooter(Collector collector) {
        shooterMotors = new Talon(4);
        shootCommand = false;
        compressor = new Compressor(7, 1);
        compressor.start();
        driverStation = DriverStation.getInstance();
        this.collector = collector;
    }

    private void shoot() {

        compressor.stop();
        collector.setCollectInCommand(true);
        collector.setCollectOutCommand(false);
        Timer.delay(.3);
        collector.setCollectOutCommand(true);
        collector.setCollectInCommand(false);
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
            if (driverStation.isEnabled()) {
                if (shootCommand && collector.getRightCollectorValue() == DoubleSolenoid.Value.kReverse) {
                    
                    shoot();
                    shootCommand = false;
                }
            }
            
            if (armUpCommand && !armDownCommand){
                armUp();
            }
            
            if (armDownCommand && !armUpCommand){
                armDown();
            }
            
            if (armDownCommand && armUpCommand){
                SmartDashboard.putString("Arm error", "Both buttons were pressed");
            }
            shootToDashboard();
            Timer.delay(.2);

        }
    }
    
    private void armUp(){
        //bring arm up
    }
    
    private void armDown(){
        //bring arm down
    }
    
    public void setArmUpCommand(boolean armUpCommand){
        this.armUpCommand = armUpCommand;
    }
    
    public void setArmDownCommand (boolean armDownCommand){
        this.armDownCommand = armDownCommand;
    }

    private void shootToDashboard() {
        SmartDashboard.putBoolean("Shoot Command", shootCommand);
        SmartDashboard.putNumber("Shooter Motor Value", shooterMotors.get());
        SmartDashboard.putBoolean("Compressor", compressor.enabled());
    }
}
