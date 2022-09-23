/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

/**
 *
 * @author ricardosoto
 */
public class Memory {
    private int size;    
    private ArrayList<Optional<Register>> registers;    
    private final int START_INDEX = 10;
    private int allocationIndex = 0;
    private int allocatedMemorySize = 0; 
    private int currentIndex = 0;
   
    
    public Memory(int size){
        registers = new ArrayList<>();
        for(int i = 0 ; i <size ; i ++){
            registers.add(Optional.empty());                 
        }        
        this.size = size;
    }
    
    
    public ArrayList<Optional<Register>> getInstructions(){
        return this.registers;
    }
    private boolean spaceFull(int startingIndex, int space){        
        if(startingIndex + space > this.size){
                    System.out.println(startingIndex + space+"123123");
            
            return true;   
        }
        if(this.registers.get(startingIndex).isEmpty()){
            for(int i = startingIndex ; i < space; i ++){
                if(this.registers.get(i).isPresent()){                    
                    System.out.println(startingIndex + space+"6969969");
                    return true;
                }
            }
        }
        return false;
        
    }
    public boolean allocatePCB(PCB pcb){
        ArrayList<Integer> pcbData = pcb.getPCBData();
        ArrayList<MemoryRegister> instructions = pcb.getLoader().getInstrucionSet();
        //El PC
       
        if(this.currentIndex+pcbData.size()+instructions.size() >this.size){
            //Error por desbordamiento de memoria
            return false;
        }
        
        for(int i = 0 ; i < pcbData.size(); i ++){
            Register infoRegister = new InformationRegister();
            infoRegister.setValue(pcbData.get(i));
            this.registers.set(this.currentIndex,Optional.of(infoRegister));
            this.currentIndex++;
        }
        pcb.setProgramCounter(this.currentIndex);
        for(int i = 0 ; i < instructions.size(); i ++){
            Register memoryRegister = new MemoryRegister();
            memoryRegister = instructions.get(i);
            this.registers.set(this.currentIndex,Optional.of(memoryRegister));
            this.currentIndex++;
        }
        pcb.setMemory(this);
        System.out.println(this.currentIndex);        
        return true;
        
        
    }
    
    public void clean(){
       this.registers.clear();       
    }
    public int getAllocationIndex(){
        return this.allocationIndex;
    }
    
    public void allocate(ArrayList<MemoryRegister> instructions){
        //Inicia a partir del índice START_INDEX
        Random rand = new Random();
        int startAllocate = rand.nextInt(this.size);
        while(startAllocate <this.START_INDEX) {
            startAllocate++;
            
        }
        while(this.spaceFull(startAllocate,instructions.size())){
            startAllocate = rand.nextInt(this.size);
            while(startAllocate <this.START_INDEX) {
                startAllocate++;
            
            }
            
        }
        
        int j = 0;
        this.allocationIndex = startAllocate;
        //System.out.println(this.allocationIndex);
        for(int i = startAllocate ; i <instructions.size()+startAllocate; i ++){
            this.registers.add(i, Optional.of(instructions.get(j)));
            j++;
         }        
        this.allocatedMemorySize = instructions.size();
        
        
    }
    public int geAllocatedMemorySize(){
        return this.allocatedMemorySize;
    }

    public int getSize() {
        return size;
    }
    
    
    
    
}
