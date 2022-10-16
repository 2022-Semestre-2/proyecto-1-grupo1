/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

import MiniPC.model.Algoritmos.SJF;
import MiniPC.model.Algoritmos.Algoritmos;
import MiniPC.controller.PCController;
import MiniPC.model.Algoritmos.HRRN;
import MiniPC.model.Algoritmos.RoundRobin;
import MiniPC.model.Algoritmos.SRT;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
    //Estado de los registros del PCB
    private  ArrayList<String> currentPcbRegistersStatus;
    private ArrayList<ProcessTime> stats = new ArrayList<ProcessTime>();
    private Algoritmos algoritmo;
    private Queue<PCB> originalQueue = null;
    private HashMap<String,Algoritmos> hashAlgoritmos;
    
    
    //para probar
   
    
    public void initializeMapper(){
        this.hashAlgoritmos = new HashMap<>();
        this.hashAlgoritmos.put("SJF", new SJF());
        this.hashAlgoritmos.put("RR", new RoundRobin());
        this.hashAlgoritmos.put("HRRN", new HRRN());
        this.hashAlgoritmos.put("SRT", new SRT());
    }
    public CPU(String name, String algoritmoAutilizar){
        this.currentProcessIndex = 0;
        this.processInstructionIndex = 0;
        this.cpuName = name;
        this.initializeMapper();
        this.algoritmo = this.hashAlgoritmos.get(algoritmoAutilizar);
        
        //this.loadPCBSArrival();
        
    }
    private void diskValidation(Memory disk, Memory memory){
        if(this.processQueue.isEmpty()){     
                 if(!disk.getProcessesLoaded().isEmpty() && memory.PCBfits(disk.getProcessesLoaded().getFirst())){
                           System.out.println("ProcessDisconectedfromdiskAll");                       
                        PCB process = disk.getProcessesLoaded().getFirst();
                        disk.deallocatePCB(process);                       
                        
                       this.addPCBtoQueue(process); 
                       this.originalQueue.add(process);
                       process.setCurrentCPU("CPU1");
                        
                       
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
                       
                        
                        this.addPCBtoQueue(process);  
                        this.originalQueue.add(process);
                        
                        process.setCurrentCPU("CPU1");
                       
                        
                        process.setStatus("Listo");
                       
                       memory.allocatePCB(process);
                       
                   }
            
            
             
    }
     //
// Aquí ejecuta la instrucción pero todo es por defecto hasta que se cambie lo de memioria

    public void executeInstructionAlgorithm(Memory memory,Memory disk, PCController cont){
        
        
        
        
        if(this.processQueue.isEmpty()){
            return;
        }
        if(this.originalQueue==null){
            this.originalQueue = new LinkedList<PCB>();
            for(PCB pcb : this.processQueue){
                this.originalQueue.add(pcb);
            }
                
        }
         //METODO 
//---------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------------------------------------------------
        PCB pcb = this.algoritmo.executeInstruction(this.processQueue, cont);
        
        
        this.currentPcbRegistersStatus = this.algoritmo.getStatus();
        int i = 0 ;
        int index  = i;
        
        for(PCB proceso: this.originalQueue){
                if(proceso.equals(pcb)){
                    index = i;
                    break;
                }
                i++;
         }
        this.currentProcessIndex = index;      
        System.out.println(index);
        this.processInstructionIndex++;
            
        if(algoritmo.programIsFinished()){
            this.processQueue.remove(pcb);
            pcb.setStatus("Fin");
            memory.deallocatePCB(pcb);              
            
            this.diskValidation(disk, memory);
            
            
       }
        
       
       
        
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
    
    
    public void executeInstruction(Memory memory,Memory disk, PCController cont){
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
                        
                       this.addPCBtoQueue(process); 
                       process.setCurrentCPU("CPU1");
                        
                       
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
                       
                        
                        this.addPCBtoQueue(process);  
                            process.setCurrentCPU("CPU1");
                       
                        
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
        this.currentPcbRegistersStatus = this.currentPcb.executeInstruction(cont); //array con el estado del proceso     
        
        this.processInstructionIndex++;
    }
    public Queue<PCB> getProcesesQueue(){
        return this.processQueue;
    }
    public void executeAll(Memory memory,Memory disk,CPU cpu1, CPU cpu2, PCController cont){
        
        
        while(!this.processQueue.isEmpty()){                        
        
            if(this.cpuName.equals("CPU1")){
                
                this.executeInstructionAlgorithm(memory, disk, cont);            
//                        
                
                cont.updatePCBStatusTable();
                cont.loadPCBstoMem();                                        
                if(cpu1.getProcessInstructionIndex()!=0){
                    cont.getApp().getExecutionTables()[0].getModel().setValueAt(" ", cpu1.getCurrentProcessIndex(),cpu1.getProcessInstructionIndex());     
                }                
                cont.updateCPUComponents(cpu1);
//                        long i = 0 ;
//                while(i < 10000000){i++;};
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