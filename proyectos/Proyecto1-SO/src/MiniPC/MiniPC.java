/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package MiniPC;

import MiniPC.controller.PCController;
import MiniPC.model.PCB;
import java.util.Date;

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
        //PCB pcb = new PCB();
        
        //pcb.setCPUMemory("test/asm1.asm", 200);
        
        
        
        
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
        
        PCController c = new PCController();                          
        
       // pcb.executeAll("test/asm1.asm", 200);
       /**
        * Estados; nuevo, preparado, ejecución, en espera, finalizado
 Contador del programa (ubicación del programa cargado en memoria)
* 10
 Registros AC, AX, BX, CX, DX
 Información de la pila: definir tamaño de 5, y tomar en cuenta error de desbordamiento
 Información contable; el cpu donde se está ejecutando, tiempo de inicio, tiempo empleado.
 Información del estado de E/S; lista de archivos abiertos 
        */
       String a = "espera";
       String k = "";
       for(int i = 0; i < a.toCharArray().length; i ++){
           k+=Integer.toBinaryString(a.toCharArray()[i]);
           
       }
       System.out.println(k);       
       Long d = Long.parseLong(k, 2);
       System.out.println(d.intValue());
       System.out.println(d);
       System.out.println(new Date().getTime());
       
               
        
        
        
       
         

               
        
        
       
            
    }
    
}
