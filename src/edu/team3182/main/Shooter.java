package edu.team3182.main;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Object implements Runnable {

    private DriverStation driverStation;
    public volatile boolean shootCommand;
    public volatile boolean backShootCommand;
    
    public synchronized void setShootCommand(boolean shootCommand) {
        this.shootCommand = shootCommand;
    }
    public synchronized void setBackShootCommand(boolean backShootCommand) {
        this.backShootCommand = backShootCommand;
    }
    private Talon shooterMotors;
    private Compressor compressor;
    private Collector collector;

    public Shooter(Collector collector) {
        //initializing everything
        shooterMotors = new Talon(4);
        shootCommand = false;
        compressor = new Compressor(7, 1);
        compressor.start();
        driverStation = DriverStation.getInstance();
        this.collector = collector;
    }

    private void shoot() {
        // turn off compressor then bring the collector in 
        // shoot then turn compressor back on
        compressor.stop();
        shooterMotors.set(1);
        Timer.delay(1.4);
        shooterMotors.set(0);
        Timer.delay(.5);
        
        //start reload
        shooterMotors.set(-.25);
        Timer.delay(1.5);
        shooterMotors.set(0);
        compressor.start();
    }

    public void run() {

        while (true) {
        // If the robot is enabled check if button to shoot was pressed and if the 
        // collector is reversed. If both true, shoot, then set shootCommand to false
            if (driverStation.isEnabled()) {
                if (shootCommand && collector.getRightCollectorValue() == DoubleSolenoid.Value.kReverse) {
                    shootToDashboard();
                    shoot();
                    shootCommand = false;
                }
                if(backShootCommand){
                    shooterMotors.set(-.2);
                }
                else if(backShootCommand == false){
                    shooterMotors.set(0);
                }
            }
            
            //shows shooter data on dashboard
            shootToDashboard();
            Timer.delay(.2);
        }
    }
    

    private void shootToDashboard() {
// print stuff to dashboard
        SmartDashboard.putBoolean("Shoot Command", shootCommand);
        SmartDashboard.putNumber("Shooter Motor Value", shooterMotors.get());
        SmartDashboard.putBoolean("Compressor", compressor.enabled());
    }
}
