/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package MiniPC;

import MiniPC.controller.PCController;
import MiniPC.model.Memory;
import MiniPC.model.PCB;

/**
 *
 * @author ricardosoto
 */
public class MiniPC {

    /**
     * @param args the command line arguments
     */ 
    public static void main(String[] args) {
        
        
        
       PCController c = new PCController();                          
       c.init();
              
            
    }
    
}
