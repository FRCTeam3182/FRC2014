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

    private AnalogChannel rangeFinder;
    private Encoder rightDriveEncoder;
    private Encoder leftDriveEncoder;
    double distance;
    double avgVolt;
    double voltage;
    //private DigitalInput limitLED;

    public Sensors() {
        rightDriveEncoder = new Encoder(4, 3);
        leftDriveEncoder = new Encoder(2, 1);
        rangeFinder = new AnalogChannel(1, 1);
        double distanceRange;
        rightDriveEncoder.setDistancePerPulse(.08168);

    }

    public void run() {
        while (true) {
            distance = rightDriveEncoder.getDistance();
            avgVolt = rangeFinder.getAverageVoltage();
            voltage = rangeFinder.getVoltage();
            SmartDashboard.getNumber("Voltage Range Sensor", voltage);
            SmartDashboard.putNumber("Average Voltage Range Sensor", avgVolt);
            SmartDashboard.putNumber("Speed Right", rightDriveEncoder.getRate());
            SmartDashboard.putNumber("Speed Left", leftDriveEncoder.getRate());
            Timer.delay(.1);
        }
    }

}
