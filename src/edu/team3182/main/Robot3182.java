/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.team3182.main;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot3182 extends IterativeRobot {

    //declaring thread variables
    private DriveTrain driveTrainVar;
    private Collector collectVar;
    private Shooter shooterVar;
    private Sensors sensorsVar;
    private ArduinoLights arduinoLightsVar;

    //declaration joystick variables
    private Joystick buttonsJoystick;

    //declaration dashboard variable
    public SmartDashboard dash;

    //declaring button states of buttons joystick
    boolean isPassing;
    boolean isShooting;
    boolean isCollecting;

    public void robotInit() {
        //initialize all of the threads
        driveTrainVar = new DriveTrain();
        new Thread(driveTrainVar, "DriveTrain").start();
        collectVar = new Collector();
        new Thread(collectVar, "Collector").start();
        shooterVar = new Shooter(collectVar);
        new Thread(shooterVar, "Shooter").start();
        sensorsVar = new Sensors();
        new Thread(sensorsVar, "Sensors").start();
        arduinoLightsVar = new ArduinoLights(sensorsVar);
        new Thread(arduinoLightsVar, "Arduino").start();

        //Joystick to control shooting, collecting, and arduinoLights
        buttonsJoystick = new Joystick(3);
    }

    public void disabledInit() {
        //turn everything off
        driveTrainVar.setLeftMotorCommand(0);
        driveTrainVar.setRightMotorCommand(0);
        shooterVar.setShootCommand(false);
        collectVar.setCollectMotorCommand(false);
        collectVar.setCollectorInCommand(false);
        collectVar.setCollectorOutCommand(false);
    }

    public void autonomousInit() {
        //**********************
        //LOW GOAL AUTO
        //**********************
//        driveTrainVar.setRightMotorCommand(-.7);
//        driveTrainVar.setLeftMotorCommand(-.7);
//        Timer.delay(.8);
//        driveTrainVar.setRightMotorCommand(-.6);
//        driveTrainVar.setLeftMotorCommand(-.6);
//        Timer.delay(1.4);
//        driveTrainVar.setRightMotorCommand(-.3);
//        driveTrainVar.setLeftMotorCommand(-.3);
//        Timer.delay(.5);
//        driveTrainVar.setRightMotorCommand(0);
//        driveTrainVar.setLeftMotorCommand(0);
//        collectVar.setCollectorOutCommand(true);
//        Timer.delay(1);
//        collectVar.setCollectorOutCommand(false);
        
        //disable joystick command over the wheels
        driveTrainVar.setJoystickStateCommand(false);
        double delayNum = 0;
        SmartDashboard.putNumber("Auto Delay Time", delayNum);
        double num = SmartDashboard.getNumber("Auto delay", delayNum);
       
        //drive forward
        Timer.delay(num);
        driveTrainVar.setRightMotorCommand(-.7);
        driveTrainVar.setLeftMotorCommand(-.7);
        Timer.delay(.8);
        driveTrainVar.setRightMotorCommand(-.6);
        driveTrainVar.setLeftMotorCommand(-.6);
        Timer.delay(1.5);
        driveTrainVar.setRightMotorCommand(-.3);
        driveTrainVar.setLeftMotorCommand(-.3);
        Timer.delay(.5);
        driveTrainVar.setRightMotorCommand(0);
        driveTrainVar.setLeftMotorCommand(0);
        //mo
        Timer.delay(1);
        collectVar.setCollectorOutCommand(true);
        Timer.delay(1.5);
        collectVar.setCollectMotorCommand(false);
        //Shoot
        shooterVar.setShootCommand(true);
    }

    public void autonomousPeriodic() {
        Timer.delay(.01);
    }

    public void teleopInit() {
        driveTrainVar.setJoystickStateCommand(true);
    }

    public void teleopPeriodic() {
        //get joystick vals
        isShooting = buttonsJoystick.getRawButton(1); //shooting
        isCollecting = buttonsJoystick.getRawButton(3); //collect
        isPassing = buttonsJoystick.getRawButton(5); //pass

        //send joystick data to Collector
        collectVar.setCollectorInCommand(buttonsJoystick.getRawButton(11)); //move the collector in
        collectVar.setCollectorOutCommand(buttonsJoystick.getRawButton(9)); //move the collector out
        collectVar.setCollectMotorCommand(isCollecting);
        collectVar.setPassCommand(isPassing);

        //send joystick data to Shooter
        shooterVar.setShootCommand(isShooting);
        shooterVar.setBackShootCommand(buttonsJoystick.getRawButton(2));

        //send joystick data to ArduinoLights
        arduinoLightsVar.signal(buttonsJoystick.getRawButton(4));
        arduinoLightsVar.kill(buttonsJoystick.getRawButton(6));
        arduinoLightsVar.isPassing(isPassing);
        arduinoLightsVar.isShooting(isShooting);
        arduinoLightsVar.isCollecting(isCollecting);
    }

    public void disabledPeriodic() {
        //nothing happens here
    }

    public void testInit() {
        //nothing needs to be here
    }

    public void testPeriodic() {
        shooterVar.setShootCommand(buttonsJoystick.getRawButton(1));
        collectVar.setCollectMotorCommand(buttonsJoystick.getRawButton(2));
        collectVar.setPassCommand(buttonsJoystick.getRawButton(3));
        collectVar.setCollectorInCommand(buttonsJoystick.getRawButton(5));
        collectVar.setCollectorOutCommand(buttonsJoystick.getRawButton(6));
        if (buttonsJoystick.getRawButton(11)) {
            //sendArduino(true, false, false, false);
        }
    }
}