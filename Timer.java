/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toshiba.training;

/**
 *
 * @author
 */
public class Timer{
    /**
     * Time for stop(interrupt)
     */
    private int stopTime;
    
    /**
     * Constructor
     * @param stopTime : Time for stop(interrupt)
     */
    public Timer(int stopTime){
        this.stopTime = stopTime;
    }
    
    /**
     * Check whether time for stopping or not
     * @param instructionCount : number of executed instructions
     * @return 
     */
    public boolean isStopTime(int instructionCount) {
        if(instructionCount == stopTime) {
            return true;
        }
        return false;
    }
}
