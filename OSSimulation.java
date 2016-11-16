/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toshiba.training;
public class OSSimulation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            while(true) {
                if(args.length == 2) {
                    String filePath = args[0];
                    int interrputTime = Integer.parseInt(args[1]);
                    if (interrputTime < 0) {
                        System.out.println("Interrupt time mustn't be negative");
                    } else {
                        CPU cpu = new CPU();
                        cpu.start(filePath, interrputTime);
                    }
                    break;
                } else {
                    System.out.println("Please follows format: [java 'project_name' 'program_file.txt' 'number']");
                }                
            }
        } catch (NumberFormatException e) {
            System.out.println("Interrupt time must be numeric");
        } 
    }
    
}