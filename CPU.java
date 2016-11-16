/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toshiba.training;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Random;

/**
 * @author
 */
public class CPU implements InstructionSet {

    /**
     * Timer Interrupt Type
     */
    public final static int TIMER_INTERRUPT_TYPE = 0;
    /**
     * System Interrupt Type
     */
    public final static int SYSTEM_INTERRUPT_TYPE = 1;
    /**
     * Empty Instruction
     */
    public static final int EMPTY_INSTRUCTION = 0;
    /**
     * Index Out of Range Message
     */
    public static final String INDEX_OUT_OF_RANGE = "Index out of range";
    /**
     * Invalid Access Message
     */
    public static final String INVALID_ACCESS_MESSAGE = "Invalid access to memory";
    /**
     * Register Set
     */
    private RegisterSet registerSet;
    /**
     * Memory
     */
    private Memory memory;
    /**
     * Timer
     */
    private Timer timer;
    /**
     * Program's status
     */
    private boolean isRunning;
    /**
     * True if in user mode, False if in system mode
     */
    private boolean userMode;
    /**
     * Number of executed instructions
     */
    private int instructionCount;
    /**
     * If interruptType = 0, Timer Interrupt. If interruptType =1, System
     * Interrupt.
     */
    private int interruptType;

    /**
     * Start program
     *
     * @param filePath : program file
     * @param interruptTime : interrupt time
     */
    public void start(String filePath, int interruptTime) {
        try {
            // Start timer
            timer = new Timer(interruptTime);

            // Include register set
            registerSet = new RegisterSet();

            // Initialize memory
            memory = new Memory();
            memory.loadProgramToMemory(filePath);

            userMode = true;
            isRunning = true;

            // Execute program 
            while (isRunning) {

                int instruction = memory.read(registerSet.getPC());
                registerSet.setPC(registerSet.getPC() + 1);
                registerSet.setIR(instruction);

                executeInstruction();
            }
        } catch (FileNotFoundException e) {
            System.out.print("File isn't found");
        } catch (IOException e) {
            System.out.print("Error in reading file");
        }
    }

    /**
     * Execute one instruction
     */
    private void executeInstruction() {
        int instruction = registerSet.getIR();
        switch (instruction) {
            case 1:
                loadValue();
                break;
            case 2:
                loadAddr();
                break;
            case 3:
                loadIndAddr();
                break;
            case 4:
                loadIdxXAddr();
                break;
            case 5:
                loadIdxYAddr();
                break;
            case 6:
                loadSpX();
                break;
            case 7:
                storeAddr();
                break;
            case 8:
                get();
                break;
            case 9:
                putPort();
                break;
            case 10:
                addX();
                break;
            case 11:
                addY();
                break;
            case 12:
                subX();
                break;
            case 13:
                subY();
                break;
            case 14:
                copyToX();
                break;
            case 15:
                copyFromX();
                break;
            case 16:
                copyToY();
                break;
            case 17:
                copyFromY();
                break;
            case 18:
                copyToSp();
                break;
            case 19:
                copyFromSp();
                break;
            case 20:
                jumpAddr();
                break;
            case 21:
                jumpAddrIfEqual();
                break;
            case 22:
                jumpAddrIfNotEqual();
                break;
            case 23:
                callAddr();
                break;
            case 24:
                ret();
                break;
            case 25:
                incX();
                break;
            case 26:
                decX();
                break;
            case 27:
                push();
                break;
            case 28:
                pop();
                break;
            case 29:
                sysInterrupt();
                break;
            case 30:
                iRet();
                break;
            case 50:
                end();
                break;
            default:
                end();
        }
        checkInterruptTimer();
    }

    /**
     * Check interrupt action by Timer
     */
    private void checkInterruptTimer() {
        if (userMode && isRunning) {
            if (timer.isStopTime(instructionCount)) {
                userMode = false;
                interruptType = TIMER_INTERRUPT_TYPE;

                int tmp = registerSet.getSP();
                registerSet.setSP(0);
                pushToStack(tmp);
                pushToStack(registerSet.getPC());
                registerSet.setPC(0);
                pushToStack(registerSet.getAC());
                pushToStack(registerSet.getX());
                pushToStack(registerSet.getY());
                instructionCount = 0;
            } else {
                instructionCount++;
            }
        }
    }

    /**
     * Get value in memory
     */
    private int readFromMemory() {
        int address;
        if (userMode) {
            address = registerSet.getPC();
        } else {
            if (interruptType == TIMER_INTERRUPT_TYPE) {
                address = Memory.TIMER_INTERRUPT_ADDRESS_FROM + registerSet.getPC();
            } else {
                address = Memory.SYSTEM_INTERRUPT_ADDRESS_FROM + registerSet.getPC();
            }
        }
        registerSet.setPC(registerSet.getPC() + 1);
        return memory.read(address);
    }

    /**
     * Push value to stack
     *
     * @param value
     */
    private void pushToStack(int value) {
        if (!isFullMemory()) {
            int address;
            if (userMode) {
                address = Memory.MAX_USER_ADDRESS - registerSet.getSP();
            } else {
                address = Memory.MAX_SYSTEM_ADDRESS - registerSet.getSP();
            }
            registerSet.setSP(registerSet.getSP() + 1);
            memory.write(address, value);
        }
    }

    /**
     * Check memory is full or not
     */
    private boolean isFullMemory() {
        int size = registerSet.getPC() + registerSet.getSP();
        if (userMode && size > 999) {
            return true;
        }
        if (!userMode && size > 499) {
            return true;
        }
        if (!userMode && registerSet.getSP() > 499) {
            return true;
        }
        return false;
    }

    /**
     * Pop value from stack
     *
     * @return value
     */
    private int popFromStack() {
        int address;
        if (registerSet.getSP() == 0) {
            throw new EmptyStackException();
        }
        registerSet.setSP(registerSet.getSP() - 1);
        if (userMode) {
            address = Memory.MAX_USER_ADDRESS - registerSet.getSP();
        } else {
            address = Memory.MAX_SYSTEM_ADDRESS - registerSet.getSP();
        }

        return memory.read(address);
    }

    /**
     * Print exception
     *
     * @param exceptionMessage
     */
    private void printException(String exceptionMessage) {
        memory.clear();
        int address = 0;
        char[] errorCharacters = exceptionMessage.toCharArray();
        for (char character : errorCharacters) {
            memory.write(address++, 1);
            memory.write(address++, (int) character);
            memory.write(address++, 9);
            memory.write(address++, 2);
        }
        memory.write(address++, 2);
        registerSet.setPC(0);
        userMode = true;

    }

    @Override
    public void loadValue() {
        try {
            int value = readFromMemory();
            registerSet.setAC(value);
        } catch (IndexOutOfBoundsException e) {
            printException(INDEX_OUT_OF_RANGE);
        }
    }

    @Override
    public void loadAddr() {
        try {
            int address = readFromMemory();
            if (userMode && address > Memory.MAX_USER_ADDRESS) {
                printException(INVALID_ACCESS_MESSAGE);
            } else {
                registerSet.setAC(memory.read(address));
            }
        } catch (IndexOutOfBoundsException e) {
            printException(INDEX_OUT_OF_RANGE);
        }
    }

    @Override
    public void loadIndAddr() {
        try {
            int address = readFromMemory();
            if (userMode && address > Memory.MAX_USER_ADDRESS) {
                printException(INVALID_ACCESS_MESSAGE);
            } else {
                int innerAddress = memory.read(address);
                if (userMode && innerAddress > Memory.MAX_USER_ADDRESS) {
                    printException(INVALID_ACCESS_MESSAGE);
                }
                registerSet.setAC(memory.read(innerAddress));
            }
        } catch (IndexOutOfBoundsException e) {
            printException(INDEX_OUT_OF_RANGE);
        }
    }

    @Override
    public void loadIdxXAddr() {
        try {
            int address = readFromMemory();
            address = address + registerSet.getX();
            if (userMode && address > Memory.MAX_USER_ADDRESS) {
                printException(INVALID_ACCESS_MESSAGE);
            } else {
                registerSet.setAC(memory.read(address));
            }
        } catch (IndexOutOfBoundsException exp) {
            printException(INDEX_OUT_OF_RANGE);
        }
    }

    @Override
    public void loadIdxYAddr() {
        try {
            int address = readFromMemory();
            address = address + registerSet.getY();
            if (userMode && address > Memory.MAX_USER_ADDRESS) {
                printException(INVALID_ACCESS_MESSAGE);
            } else {
                registerSet.setAC(memory.read(address));
            }
        } catch (IndexOutOfBoundsException e) {
            printException(INDEX_OUT_OF_RANGE);
        }
    }

    @Override
    public void loadSpX() {
        try {
            int address = registerSet.getSP() + registerSet.getX();
            if (userMode && address > Memory.MAX_USER_ADDRESS) {
                printException(INVALID_ACCESS_MESSAGE);
            } else {
                registerSet.setAC(memory.read(address));
            }
        } catch (IndexOutOfBoundsException e) {
            printException(INDEX_OUT_OF_RANGE);
        }
    }

    @Override
    public void storeAddr() {
        try {
            int address = readFromMemory();
            if (userMode && address > Memory.MAX_USER_ADDRESS) {
                printException(INVALID_ACCESS_MESSAGE);
            } else {
                memory.write(address, registerSet.getAC());
            }
        } catch (IndexOutOfBoundsException e) {
            printException(INDEX_OUT_OF_RANGE);
        }
    }

    @Override
    public void get() {
        Random random = new Random();
        registerSet.setAC(random.nextInt(10));
    }

    @Override
    public void putPort() {
        try {
            int port = readFromMemory();
            if (port == 1) {
                System.out.println(registerSet.getAC());
            } else {
                System.out.print((char) registerSet.getAC());
            }
        } catch (IndexOutOfBoundsException exp) {
            printException(INDEX_OUT_OF_RANGE);
        }
    }

    @Override
    public void addX() {
        registerSet.setAC(registerSet.getAC() + registerSet.getX());
    }

    @Override
    public void addY() {
        registerSet.setAC(registerSet.getAC() + registerSet.getY());
    }

    @Override
    public void subX() {
        registerSet.setAC(registerSet.getAC() - registerSet.getX());
    }

    @Override
    public void subY() {
        registerSet.setAC(registerSet.getAC() - registerSet.getY());
    }

    @Override
    public void copyToX() {
        registerSet.setX(registerSet.getAC());
    }

    @Override
    public void copyFromX() {
        registerSet.setAC(registerSet.getX());
    }

    @Override
    public void copyToY() {
        registerSet.setY(registerSet.getAC());
    }

    @Override
    public void copyFromY() {
        registerSet.setAC(registerSet.getY());
    }

    @Override
    public void copyToSp() {
        registerSet.setSP(registerSet.getAC());
    }

    @Override
    public void copyFromSp() {
        registerSet.setAC(registerSet.getSP());
    }

    @Override
    public void jumpAddr() {
        try {
            int address = readFromMemory();
            registerSet.setPC(address);
        } catch (IndexOutOfBoundsException e) {
            printException(INDEX_OUT_OF_RANGE);
        }
    }

    @Override
    public void jumpAddrIfEqual() {
        try {
            int address = readFromMemory();
            if (registerSet.getAC() == 0) {
                registerSet.setPC(address);
            }
        } catch (IndexOutOfBoundsException e) {
            printException(INDEX_OUT_OF_RANGE);
        }
    }

    @Override
    public void jumpAddrIfNotEqual() {
        try {
            int address = readFromMemory();
            if (registerSet.getAC() != 0) {
                registerSet.setPC(address);
            }
        } catch (IndexOutOfBoundsException e) {
            printException(INDEX_OUT_OF_RANGE);
        }
    }

    @Override
    public void callAddr() {
        int address = readFromMemory();
        pushToStack(registerSet.getPC());
        registerSet.setPC(address);
    }

    @Override
    public void ret() {
        int address = popFromStack();
        registerSet.setPC(address);
    }

    @Override
    public void incX() {
        registerSet.setX(registerSet.getX() + 1);
    }

    @Override
    public void decX() {
        registerSet.setX(registerSet.getX() - 1);
    }

    @Override
    public void push() {
        pushToStack(registerSet.getAC());
    }

    @Override
    public void pop() {
        registerSet.setAC(popFromStack());
    }

    @Override
    public void sysInterrupt() {
        int temp = registerSet.getSP();
        registerSet.setSP(0);
        pushToStack(temp);
        pushToStack(registerSet.getPC());
        registerSet.setPC(0);
        pushToStack(registerSet.getAC());
        pushToStack(registerSet.getX());
        pushToStack(registerSet.getY());
        userMode = false;
        interruptType = SYSTEM_INTERRUPT_TYPE;
    }

    @Override
    public void iRet() {
        if (!userMode) {
            registerSet.setSP(5);
            registerSet.setY(popFromStack());
            registerSet.setX(popFromStack());
            registerSet.setAC(popFromStack());
            registerSet.setPC(popFromStack());
            registerSet.setSP(popFromStack());
            userMode = true;
        }
    }

    @Override
    public void end() {
        isRunning = false;
    }
}
