/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package MiniPC;

import MiniPC.controller.PCB;
import java.util.Arrays;

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
        
        
        
        /**
        for(int i = 0 ; i < pcb.getLoader().getInstrucionSet().size(); i ++){
            System.out.print("line: "+i+"  "+pcb.getLoader().getInstrucionSet().get(i).getOperator());
            System.out.print(" ");
            System.out.print(pcb.getLoader().getInstrucionSet().get(i).getAdress());
            System.out.print(" ");
            System.out.print(pcb.getLoader().getInstrucionSet().get(i).getValue());
            
            System.out.println(" ");            
        }
        */
        
        
        pcb.executeAll("test/asm1.asm", 200);
        if(pcb.getLoader().getCountErrors() !=0){
            System.out.println(pcb.getLoader().getErrorMessage());
        }
        
        
        
       
         

               
        
        
       
            
    }
    
}
