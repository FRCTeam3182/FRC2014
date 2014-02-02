/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.team3182.main;

/**
 *
 * @author Lego
 */
public class LatchShifters {
    private boolean _lastState;
    public LatchShifters(){
       _lastState = false; 
    }
    public void toggle(){
        _lastState = !_lastState;
    }
}