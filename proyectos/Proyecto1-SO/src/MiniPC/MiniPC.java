/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package MiniPC;

import java.util.Random;
import MiniPC.controller.PCB;
import MiniPC.view.Main_Menu;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ricardosoto
 */
public class MiniPC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        //new Main_Menu().setVisible(true);
        PCB pcb = new PCB();
        //pcb.setCPUMemory("/Users/ricardosoto/downloads/tso1/asm1.asm", 200);
        
           //System.out.println(pcb.getLoader().getErrorMessage());
        
        /**
        for(int i = 0 ; i < pcb.getLoader().getInstrucionSet().size(); i ++){
            System.out.print(pcb.getLoader().getInstrucionSet().get(i).getOperator());
            System.out.print(" ");
            System.out.print(pcb.getLoader().getInstrucionSet().get(i).getAdress());
            System.out.print(" ");
            System.out.print(pcb.getLoader().getInstrucionSet().get(i).getValue());
            System.out.println(" ");
            
        }
        */
        
        pcb.executeAll("/Users/ricardosoto/downloads/tso1/asm1.asm", 200);
        
        
       
         

               
        
        
       
            
    }
    
}
