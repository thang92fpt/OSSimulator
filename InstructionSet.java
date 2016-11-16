/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toshiba.training;

/**
 * Instruction Set Class
 * @author
 */
public interface InstructionSet {
    /**
     * Load value into AC
     */
    public void loadValue();

    /**
     * Load the value at the address into the AC
     */
    public void loadAddr();

    /**
     * Load the value from the address found in the given address into the AC
     */
    public void loadIndAddr();

    /**
     * Load the value at (address+X) into the AC
     */
    public void loadIdxXAddr();

    /**
     * Load the value at (address+Y) into the AC
     */
    public void loadIdxYAddr();

    /**
     * Load from (Sp+X) into the AC
     */
    public void loadSpX();

    /**
     * Store the value in the AC into the address
     */
    public void storeAddr();

    /**
     * Gets a random int from 1 to 100 into the AC
     */
    public void get();

    /**
     * If port=1, writes AC as an int to the screen. If port=2, writes AC as a char to the screen
     */
    public void putPort();

    /**
     * Add the value in X to the AC
     */
    public void addX();

    /**
     * Add the value in Y to the AC
     */
    public void addY();

    /**
     * Subtract the value in X from the AC
     */
    public void subX();

    /**
     * Subtract the value in Y from the AC
     */
    public void subY();

    /**
     * Copy the value in the AC to X
     */
    public void copyToX();

    /**
     * Copy the value in X to the AC
     */
    public void copyFromX();

    /**
     * Copy the value in the AC to Y
     */
    public void copyToY();

    /**
     * Copy the value in Y to the AC
     */
    public void copyFromY();

    /**
     * Copy the value in AC to the SP
     */
    public void copyToSp();

    /**
     * Copy the value in SP to the AC *
     */
    public void copyFromSp();

    /**
     * Jump to the address
     */
    public void jumpAddr();

    /**
     * Jump to the address only if the value in the AC is zero
     */
    public void jumpAddrIfEqual();

    /**
     * Jump to the address only if the value in the AC is not zero
     */
    public void jumpAddrIfNotEqual();

    /**
     * Push return address onto stack, jump to the address
     */
    public void callAddr();

    /**
     * Push return address onto stack, jump to the address
     */
    public void ret();

    /**
     * Pop return address from the stack, jump to the address Increment the
     * value in X
     *
     */
    public void incX();

    /**
     * Decrement the value in X Push AC onto stack Pop from stack into AC
     */
    public void decX();

    /**
     * Push AC onto stack
     */
    public void push();

    /**
     * Pop from stack into AC
     */
    public void pop();

    /**
     * Set system mode, switch stack, push SP and PC, set new SP and PC
     */
    public void sysInterrupt();

    /**
     * Restore registers, set user mode
     */
    public void iRet();

    /**
     * End
     */
    public void end();
}
