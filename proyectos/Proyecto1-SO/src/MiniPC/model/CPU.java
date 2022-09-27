/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

import MiniPC.controller.PCController;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author ricardosoto
 */
//Organiza la ejecución de los procesos
public class CPU {
    private String cpuName;
    private PCB currentPcb;
    private int currentProcessIndex;
    private Queue<PCB> processQueue = new LinkedList<PCB>();
    private int processInstructionIndex;    
    private  ArrayList<String> currentPcbRegistersStatus;
    public CPU(String name){
        this.currentProcessIndex = 0;
        this.processInstructionIndex = 0;
        this.cpuName = name;
    }
    public void executeInstruction(Memory memory,Memory disk,CPU cpu1, CPU cpu2, PCController cont){
        if(this.processQueue.isEmpty()){
            if(this.currentPcbRegistersStatus!=null && !this.currentPcbRegistersStatus.isEmpty()){
                this.currentPcbRegistersStatus.clear();
            }
            
            return;
        }
        if(this.currentPcb.programFinished()){            
            PCB removed = processQueue.remove();
            removed.setStatus("Fin");               
            memory.deallocatePCB(removed);    
            if(this.processQueue.isEmpty()){     
                 if(!disk.getProcessesLoaded().isEmpty() && memory.PCBfits(disk.getProcessesLoaded().getFirst())){
                           System.out.println("ProcessDisconectedfromdiskAll");                       
                        PCB process = disk.getProcessesLoaded().getFirst();
                        disk.deallocatePCB(process);
                       int oneOrCero = (int)Math.round(Math.random());
                        if(oneOrCero==1){
                            cpu1.addPCBtoQueue(process);                            
                        } else {
                            cpu2.addPCBtoQueue(process);                            
                        }
                       
                       memory.allocatePCB(process);
                       process.setStatus("Listo");
                       
                   }
                    //memory.deallocatePCB(removed);                   
                   this.currentPcbRegistersStatus.clear();
                   return;
            }
            if(!disk.getProcessesLoaded().isEmpty() && memory.PCBfits(disk.getProcessesLoaded().getFirst())){
                           System.out.println("ProcessDisconectedfromdiskAll");                       
                        PCB process = disk.getProcessesLoaded().getFirst();
                        disk.deallocatePCB(process);
                       int oneOrCero = (int)Math.round(Math.random());
                        if(oneOrCero==1){
                            cpu1.addPCBtoQueue(process);                            
                        } else {
                            cpu2.addPCBtoQueue(process);                            
                        }
                        process.setStatus("Listo");
                       
                       memory.allocatePCB(process);
                       
                   }
            
            
             
            this.currentPcb = this.processQueue.peek();           
            this.currentProcessIndex++;
            this.processInstructionIndex = 0;            
            
        }
        //Estado del PCB
       
        this.currentPcb.setStatus("Exec");
        this.currentPcbRegistersStatus = this.currentPcb.executeInstruction(cont);                
        
        this.processInstructionIndex++;
    }
    public ArrayList<String> getPCBRegisterInfo(){
        return this.currentPcbRegistersStatus;
    }
    public int getCurrentProcessIndex(){
        return this.currentProcessIndex;
    }
    public int getProcessInstructionIndex(){
        return this.processInstructionIndex;
    }
    public String toSring(){
        return this.cpuName;
    }
    public void addPCBtoQueue(PCB pcb){
        //Si no hay elementos
        if(this.processQueue.isEmpty()){
            this.currentPcb = pcb;   
            
            this.currentPcb.setStatus("Listo");
        }
        this.processQueue.add(pcb);         

    }
    
    
    
}