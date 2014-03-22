package edu.team3182.main;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sensors extends Object implements Runnable {

    private AnalogChannel leftRangeFinder;
    private AnalogChannel rightRangeFinder;
    private Encoder rightDriveEncoder;
    private Encoder leftDriveEncoder;
    double leftVoltage;
    double rightVoltage;
    volatile int shootDistance;
    

    public Sensors() {
        //initializing everything
        rightDriveEncoder = new Encoder(4, 3);
        leftDriveEncoder = new Encoder(2, 1);
        leftRangeFinder = new AnalogChannel(1, 1);
        rightRangeFinder = new AnalogChannel(1, 2);
        rightDriveEncoder.setDistancePerPulse(.08168);

    }

    public void run() {
        while (true) {
            leftVoltage = leftRangeFinder.getVoltage();
            rightVoltage = rightRangeFinder.getVoltage();
            
            SmartDashboard.putNumber("Average Voltage left", leftVoltage);
            SmartDashboard.putNumber("Average Voltage right", rightVoltage);
            SmartDashboard.putNumber("Speed Right", rightDriveEncoder.getRate());
            SmartDashboard.putNumber("Speed Left", leftDriveEncoder.getRate());
            Timer.delay(.1);
             
            if (Math.abs(leftVoltage-rightVoltage) > .3) {
                leftVoltage = 0;
                rightVoltage = 0;
            }
            
            //If voltage is between x and y, we're in the right position
            if (leftVoltage >= .45 && leftVoltage <= 1.2) {
            shootDistance = 1; 
            //If voltage is between x and y, we're too far
            } else if (leftVoltage >= 3 && leftVoltage < 60) {
            shootDistance = 2; 
            //If voltage is between x and y, we;re too close
            } else if (leftVoltage >= 60 && leftVoltage <= 72) {
            shootDistance = 0; 
            }
        }
    }
    
    public synchronized int shootingDistance() {
        return shootDistance;
    }

}
