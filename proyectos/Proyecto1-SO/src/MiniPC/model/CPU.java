/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

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
    public void executeInstruction(){
        if(this.processQueue.isEmpty()){
            if(this.currentPcbRegistersStatus!=null && !this.currentPcbRegistersStatus.isEmpty()){
                this.currentPcbRegistersStatus.clear();
            }
            
            return;
        }
        if(this.currentPcb.programFinished()){            
            processQueue.remove();
            if(this.processQueue.isEmpty()){
                   this.currentPcbRegistersStatus.clear();
                   return;
            }
            this.currentPcb = this.processQueue.peek();
            this.currentProcessIndex++;
            this.processInstructionIndex = 0;            
            
        }
        //Estado del PCB
        this.currentPcbRegistersStatus = this.currentPcb.executeInstruction();                
        
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
        }
        this.processQueue.add(pcb);
        System.out.println("ADED PCB");
    }
    
    
}
