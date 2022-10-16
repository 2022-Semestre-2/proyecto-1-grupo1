/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model.Algoritmos;

import MiniPC.model.Algoritmos.Algoritmos;
import MiniPC.controller.PCController;
import MiniPC.model.PCB;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author ricardosoto
 */
public class SJF implements Algoritmos{
    private Queue<PCB> procesosEsperando;
    private PCB currentPCB;
    private int currentTime;
    private boolean programFinished;
    private ArrayList<String> status;
    
    public SJF(){
        
        this.currentTime =1;
        this.procesosEsperando = new LinkedList<PCB>();
        this.programFinished = false;
        
        
    }
    @Override
    //Lo que procura este método es que retornen el PCB(proceso) que se va a ejecutar, aunque deben ejecutarlo antes para
    // tener información contable del mismo
    public PCB executeInstruction(Queue<PCB> colaProcesosCPU,PCController cont){
        //ESTO ES PARTE DEL MÉTODO A IMPLEMENTAR
//---------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------------------------------------------------
        
        Queue<PCB> col = colaProcesosCPU;
        
        this.programFinished = false;
        for(PCB proceso: col){
           //los procesos llegan en el instante actual
           if(proceso.getArrivalTime()<=this.currentTime && !this.procesosEsperando.contains(proceso)){   
               
              if(this.procesosEsperando.isEmpty()){
                   this.currentPCB = proceso;
               }               
               this.procesosEsperando.add(proceso);
               //col.remove(proceso);
               
               
               
           }
        } 
        
        
        for(PCB proceso: this.procesosEsperando){
            if(proceso.getRafaga()<this.currentPCB.getRafaga()){
                this.currentPCB = proceso;
            }
        }
        //ESTO ES PARTE DEL MÉTODO A IMPLEMENTAR
//---------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------------------------------------------------
        //ESTO ES PARTE DEL METODO POR DEFECTO, PROCUREN RETORNAR EL PCB que según el algoritmo se debe ejecutar
       PCB result =  this.currentPCB;     
       
       this.status = result.executeInstruction(cont);
       if(this.currentPCB.programFinished()){   
           this.programFinished = true;
           this.procesosEsperando.remove(this.currentPCB);     
           this.currentPCB = this.procesosEsperando.peek();
           
       }
       
       this.currentTime++;
       return result;
       
        
    }
    @Override
     public boolean programIsFinished(){                
        return this.programFinished;
     }
    @Override 
    public ArrayList<String> getStatus(){
        return this.status;
    }        
    
    
}
