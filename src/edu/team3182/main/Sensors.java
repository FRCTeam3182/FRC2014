/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.team3182.main;

import edu.wpi.first.wpilibj.AnalogChannel;
//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 * @author Peter
 */
public class Sensors extends Object implements Runnable {

    private AnalogChannel leftRangeFinder;
    private AnalogChannel rightRangeFinder;
    private Encoder rightDriveEncoder;
    private Encoder leftDriveEncoder;
    double leftAvgVolt;
    double rightAvgVolt;
    volatile int shootDistance;
    

    public Sensors() {
        rightDriveEncoder = new Encoder(4, 3);
        leftDriveEncoder = new Encoder(2, 1);
        leftRangeFinder = new AnalogChannel(1, 1);
        rightRangeFinder = new AnalogChannel(1, 2);
        rightDriveEncoder.setDistancePerPulse(.08168);

    }

    public void run() {
        while (true) {
            leftAvgVolt = leftRangeFinder.getAverageVoltage();
            rightAvgVolt = rightRangeFinder.getAverageVoltage();
            
            SmartDashboard.putNumber("Average Voltage left", leftAvgVolt);
            SmartDashboard.putNumber("Average Voltage right", rightAvgVolt);
            SmartDashboard.putNumber("Speed Right", rightDriveEncoder.getRate());
            SmartDashboard.putNumber("Speed Left", leftDriveEncoder.getRate());
            Timer.delay(.1);
             
            if (Math.abs(leftAvgVolt-rightAvgVolt) > .3) {
                leftAvgVolt = 0;
                rightAvgVolt = 0;
            }
            
            //distance from wall (for shooter)
            if (leftAvgVolt >= .45 && leftAvgVolt <= 1.2) {
            shootDistance = 1; //just right
            } else if (leftAvgVolt >= 3 && leftAvgVolt < 60) {
            shootDistance = 2; //too far
            } else if (leftAvgVolt >= 60 && leftAvgVolt <= 72) {
            shootDistance = 0; //too close
            }
        }
    }
    
    public synchronized int shootingDistance() {
        return shootDistance;
    }

}
