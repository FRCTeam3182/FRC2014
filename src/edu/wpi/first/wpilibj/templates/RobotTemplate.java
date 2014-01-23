/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
public class RobotTemplate extends SimpleRobot 
{
      
    
    
    public void autonomous() {
        
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {

    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
      //public static void main(String[] args) {
      Joystick rightjoystick = new Joystick(1);
        
        boolean  val1, val2, val3, val4,over;
       
        over=false;
        
        val1 = rightjoystick.getRawButton(1);
        val2 = rightjoystick.getRawButton(2);
        val3 = rightjoystick.getRawButton(3);
        val4 = rightjoystick.getRawButton(4);
        while (over==false) {
        if (val1 == true){
            System.out.println("1 is pressed");
        }
        else if(val2 == true){
            System.out.print("2 is pressed");
        }
        else if(val3 == true){
            System.out.println("3 is pressed");
        }
        else if (val4 == true){
            System.out.println("4 is pressed");
            over=true;
            
        }
        }
                
               
    }
   
    
  
    }
//}
