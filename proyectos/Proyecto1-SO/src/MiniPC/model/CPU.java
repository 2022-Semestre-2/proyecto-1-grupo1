/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

import MiniPC.controller.PCController;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

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
    private ArrayList<ProcessTime> stats = new ArrayList<ProcessTime>();
    
    public CPU(String name){
        this.currentProcessIndex = 0;
        this.processInstructionIndex = 0;
        this.cpuName = name;
    }
    
    
    public void startTime(ProcessTime time){
        LocalDateTime locaDate = LocalDateTime.now();
        int hours  = locaDate.getHour();
        int minutes = locaDate.getMinute();
        time.setStartHour(hours);
        time.setStartMinute(minutes);
    }
    
    public void finishTime(ProcessTime time) {
        LocalDateTime locaDate = LocalDateTime.now();
        int hours  = locaDate.getHour();
        int minutes = locaDate.getMinute();
        time.setFinishHour(hours);
        time.setFinishMinute(minutes);
        time.setDuration(this.currentPcb.getPCBDuration());
        time.setIndex(currentProcessIndex+1);
        this.stats.add(time);
    }
    
    
    public void executeInstruction(Memory memory,Memory disk,CPU cpu1, CPU cpu2, PCController cont){
        ProcessTime time = new ProcessTime();
        startTime(time);
        if(this.processQueue.isEmpty()){
            if(this.currentPcbRegistersStatus!=null && !this.currentPcbRegistersStatus.isEmpty()){
                this.currentPcbRegistersStatus.clear();
                
            }
                   
                
            return;
        }
        if(this.currentPcb.programFinished()){ 
            finishTime(time);
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
                            process.setCurrentCPU("CPU1");
                        } else {
                            cpu2.addPCBtoQueue(process);
                            process.setCurrentCPU("CPU2");                            
                        }
                       
                       memory.allocatePCB(process);
                       process.setStatus("Listo");
                       this.currentPcb = this.processQueue.peek();                                  
                       
                   }
                    //memory.deallocatePCB(removed);                         
                    this.currentProcessIndex++;
                    this.processInstructionIndex = 0;                            
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
                            process.setCurrentCPU("CPU1");
                        } else {
                            cpu2.addPCBtoQueue(process);                            
                            process.setCurrentCPU("CPU2");
                        }
                        
                        process.setStatus("Listo");
                       
                       memory.allocatePCB(process);
                       
                   }
            
            
             
            this.currentPcb = this.processQueue.peek();       
            

            this.currentProcessIndex++;
            this.processInstructionIndex = 0;            
            
        }
        //Estado del PCB
            DefaultTableModel model3 = null;
            
            if(this.cpuName.equals("CPU1")){
                model3 = (DefaultTableModel) cont.getApp().getExecutionTables()[0].getModel();
            } else {
                model3 = (DefaultTableModel) cont.getApp().getExecutionTables()[1].getModel();
            }
            if(model3.getRowCount()<=currentProcessIndex+1){
                Vector row = new Vector();
                row.add("P"+(currentProcessIndex+2));      
                Vector row2 = new Vector();
                row2.add("P"+(currentProcessIndex+3)); 
                model3.addRow(row);
                model3.addRow(row2);
                
            }
          
            
            
        this.currentPcb.setStatus("Exec");
        this.currentPcbRegistersStatus = this.currentPcb.executeInstruction(cont);                
        
        this.processInstructionIndex++;
    }
    public Queue<PCB> getProcesesQueue(){
        return this.processQueue;
    }
    public void executeAll(Memory memory,Memory disk,CPU cpu1, CPU cpu2, PCController cont){
        while(!this.processQueue.isEmpty()){            
            
            if(this.cpuName.equals("CPU1")){
                this.executeInstruction(memory, disk, cpu1, cpu2, cont);            
                cont.updatePCBStatusTable();
                cont.loadPCBstoMem();                                        
                if(cpu1.getProcessInstructionIndex()!=0){
                    cont.getApp().getExecutionTables()[0].getModel().setValueAt(" ", cpu1.getCurrentProcessIndex(),cpu1.getProcessInstructionIndex());     
                }                
                cont.updateCPUComponents(cpu1);
            } else {
                this.executeInstruction(memory, disk, cpu1, cpu2, cont);            
                cont.updatePCBStatusTable();
                cont.loadPCBstoMem();          
                 if(cpu2.getProcessInstructionIndex()!=0){
                    cont.getApp().getExecutionTables()[1].getModel().setValueAt(" ", cpu2.getCurrentProcessIndex(),cpu2.getProcessInstructionIndex());     
                }    
                
                cont.updateCPUComponents(cpu2);                
                
            }                        
            
        }
        
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
    
    public ArrayList<ProcessTime> getStats() {
        return this.stats;
    }
    
}