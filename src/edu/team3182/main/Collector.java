/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.team3182.main;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Nodcah
 */
public class Collector extends Object implements Runnable {
    boolean toggleOut;
    boolean toggleIn;
    boolean collectorButton9;
    boolean collectorButton10;
    private DoubleSolenoid leftCollector;
    private DoubleSolenoid rightCollector;
    private Talon collectorMotor;
    boolean collect = false;
    boolean collectReverse = false;
    boolean collectorFoward = false;
    public Collector(){
        
    }
    
    public void run() {
        
    }
    
}
