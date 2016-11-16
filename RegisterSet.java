/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toshiba.training;

/**
 * Register Set Class
 * @author
 */
public class RegisterSet {

    /**
     * PC register
     */
    private int regPC;
    /**
     * AC register
     */
    private int regAC;
    /**
     * SP register
     */
    private int regSP;
    /**
     * X register
     */
    private int regX;
    /**
     * Y register
     */
    private int regY;
    /**
     * IR register
     */
    private int regIR;

    /**
     * Get PC Register
     *
     * @return regPC
     */
    public int getPC() {
        return this.regPC;
    }

    /**
     * Set value to PC Register
     *
     * @param regPC
     */
    public void setPC(int regPC) {
        this.regPC = regPC;
    }

    /**
     * Get AC Register
     *
     * @return regAC
     */
    public int getAC() {
        return this.regAC;
    }

    /**
     * Set value to AC Register
     *
     * @param regAC
     */
    public void setAC(int regAC) {
        this.regAC = regAC;
    }

    /**
     * Get SP Register
     *
     * @return regSP
     */
    public int getSP() {
        return this.regSP;
    }

    /**
     * Set value to SP Register
     *
     * @param regSP
     */
    public void setSP(int regSP) {
        this.regSP = regSP;
    }

    /**
     * Get X Register
     *
     * @return regX
     */
    public int getX() {
        return this.regX;
    }

    /**
     * Set value to X Register
     *
     * @param regX
     */
    public void setX(int regX) {
        this.regX = regX;
    }

    /**
     * Get Y Register
     *
     * @return regY
     */
    public int getY() {
        return this.regY;
    }

    /**
     * Set value to Y Register
     *
     * @param regY
     */
    public void setY(int regY) {
        this.regY = regY;
    }

    /**
     * Get IR Register
     *
     * @return regIR
     */
    public int getIR() {
        return this.regIR;
    }

    /**
     * Set value to IR Register
     *
     * @param regIR
     */
    public void setIR(int regIR) {
        this.regIR = regIR;
    }
}
