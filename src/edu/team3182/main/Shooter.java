/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.team3182.main;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Peter
 * 
 */
public class Shooter extends Object implements Runnable {
    private Talon shooterMotors; 
    
    public Shooter() {
        
    
    shooterMotors = new Talon(4);
}

public void run() {
    final int endLoopShoot = 10;
    for (int i = 1; i <= endLoopShoot; i++) { //takes half a second to reach full speed
            shooterMotors.set(1);
            Timer.delay(.01);
        }
   
        shooterMotors.set(1);
        Timer.delay(.2);
        shooterMotors.set(0);
        Timer.delay(.5);

        //start reload
        shooterMotors.set(-.25);
        Timer.delay(2);
        shooterMotors.set(0);
    }
}
