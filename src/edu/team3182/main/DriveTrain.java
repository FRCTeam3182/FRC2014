/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.team3182.main;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

/**
 *
 * @author Peter
 */
public class DriveTrain extends RobotDrive {

    public DriveTrain(int leftMotorChannel, int rightMotorChannel) {
        super(leftMotorChannel, rightMotorChannel);
    }

    public DriveTrain(int frontLeftMotor, int rearLeftMotor, int frontRightMotor, int rearRightMotor) {
        super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    }

    public DriveTrain(SpeedController leftMotor, SpeedController rightMotor) {
        super(leftMotor, rightMotor);
    }

    public DriveTrain(SpeedController frontLeftMotor, SpeedController rearLeftMotor, SpeedController frontRightMotor, SpeedController rearRightMotor) {
        super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    }

    public void tankDrive(double leftValue, double rightValue) {
        if ((leftValue < .3 && leftValue > (-.3))) {
            leftValue = 0;
        }
        if (rightValue < .3 && rightValue > (-.3)) {
            rightValue = 0;
        }
        super.tankDrive(leftValue, rightValue); //To change body of generated methods, choose Tools | Templates.

    }
}
