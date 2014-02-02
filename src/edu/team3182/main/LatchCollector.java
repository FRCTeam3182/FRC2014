/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.team3182.main;

/**
 *
 * @author Nodcah
 */
public class LatchCollector {
    private boolean _lastState;
    public LatchCollector(){
       _lastState = false; 
    }
    public void toggle(){
        _lastState = !_lastState;
    }
}
