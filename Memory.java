/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toshiba.training;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Memory Class
 * @author
 */
public class Memory {

    /**
     * Size of memory
     */
    public static final int MEMORY_CAPACITY = 2000;
    
    /**
     * User address is from 0 to 999
     */
    public static final int MIN_USER_ADDRESS = 0;
    public static final int MAX_USER_ADDRESS = 999;
    
    /**
     * User address is from 1000 to 1999
     */
    public static final int MIN_SYSTEM_ADDRESS = 1000;
    public static final int MAX_SYSTEM_ADDRESS = 1999;
    
    /**
     * Timer interrupt execute from 1000
     */
    public static final int TIMER_INTERRUPT_ADDRESS_FROM = 1000;
    
    /**
     * System interrupt execute from 1500
     */
    public static final int SYSTEM_INTERRUPT_ADDRESS_FROM = 1500;
    
    /**
     * Timer handler comment
     */
    public static final String TIMER_HANDLER_STRING = ".1000";
    
    /**
     * Entries of memory
     */
    private int[] entries = new int[MEMORY_CAPACITY];

    /**
     * Read from memory
     *
     * @param address : address contains data
     * @return data value
     * @throws IndexOutOfBoundsException
     */
    public int read(int address) throws IndexOutOfBoundsException {
        return entries[address];
    }

    /**
     * Write into memory
     *
     * @param address : address in memory to store data
     * @param value : data to be written
     */
    public void write(int address, int value) throws IndexOutOfBoundsException{
        entries[address] = value;
    }

    /**
     * Clear memory
     */
    public void clear() {
        entries = new int[MEMORY_CAPACITY];
    }

    /**
     * Load program into memory
     *
     * @param filePath : program file path
     */
    public void loadProgramToMemory(String filePath) throws FileNotFoundException, IOException {
        int addressIdx = 0;
        Scanner scanner = new Scanner(new FileInputStream(filePath));
        while (scanner.hasNext()) {
            String currentLine = scanner.nextLine();
            if (!"".equals(currentLine)) {
                if (currentLine.contains(TIMER_HANDLER_STRING)) {
                    addressIdx = TIMER_INTERRUPT_ADDRESS_FROM;
                } else {
                    if (isInstruction(currentLine)) {
                        int instructionValue = getInstruction(currentLine);
                        write(addressIdx, instructionValue);
                        addressIdx++;
                    }
                }
            }
        }
    }

    /**
     * Check line is instruction or not
     *
     * @param line
     * @return
     */
    public boolean isInstruction(String line) {
        char c;
        c = line.charAt(0);
        if (c != '-' || c < '0' || c > '9') {
            return false;
        }
        return true;
    }

    /**
     * Get instruction in each line
     *
     * @param line
     * @return
     */
    public int getInstruction(String line) throws NumberFormatException {
        char c;
        int instructionValue = 0;
        for (int i = 1; i < line.length(); i++) {
            c = line.charAt(i);
            if (c < '0' || c > '9') {
                String value = line.substring(0, i);
                instructionValue = Integer.parseInt(value);
                break;
            }
        }
        return instructionValue;
    }
}
