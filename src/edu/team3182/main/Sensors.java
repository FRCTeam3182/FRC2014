/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.team3182.main;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Peter
 */
public class Sensors extends Object implements Runnable {

    private Encoder rightDriveEncoder;
    private Encoder leftDriveEncoder;
    private Ultrasonic rangeFinder;
    private AnalogPotentiometer shooterPot;
    private DigitalInput limitLED;

    public Sensors() {
        rightDriveEncoder = new Encoder(4, 3);
        leftDriveEncoder = new Encoder(2, 1);
        rangeFinder = new Ultrasonic(8, 9);
        int shooterPotVal;
        double distanceRange;
    }

    public void run() {
        double distance;
        double distanceRange;
        distanceRange = rangeFinder.getRangeInches();
        distance = rightDriveEncoder.getDistance();
        SmartDashboard.putNumber("Distance away: ", distanceRange);
    }

}
