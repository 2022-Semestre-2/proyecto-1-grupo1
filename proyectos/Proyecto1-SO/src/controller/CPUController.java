/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import model.CPURegister;
import model.FileLoader;
import model.Memory;
import model.MemoryRegister;
import view.CPU_Menu;


/**
 *
 * @author ricardosoto
 */
public class CPUController {
    
    
    private final HashMap<Integer,CPURegister> registerAddressMapper = new HashMap<>();
    private final CPURegister ax = new CPURegister(0);
    private final CPURegister bx = new CPURegister(0);
    private final CPURegister cx = new CPURegister(0);
    private final CPURegister dx = new CPURegister(0); 
    private Integer ir =0;
    private Integer pc =0;
    private Memory memory;
    private FileLoader loader;
    private boolean programFinished = false;
    private final CPURegister ac = new CPURegister(0);
    //CPU_Menu menu = new CPU_Menu();
    
    
    public CPUController(){
        this.registerAddressMapper.put(1, ax);
        this.registerAddressMapper.put(2, bx);
        this.registerAddressMapper.put(3, cx);
        this.registerAddressMapper.put(4, dx);
                
        
    }
    
    
    
    public Memory getMemory(){
        return this.memory;
    }

    public FileLoader getLoader() {
        return loader;
    }
    
    public boolean programFinished(){        
        return this.pc >= this.memory.getAllocationIndex()+this.memory.geAllocatedMemorySize();
    }
    
    
    
    
    //Ejecuta la instruccion segun el PC (una a una)
    public ArrayList<String> executeInstruction(){
        if(this.pc ==0 ){
            this.pc = this.memory.getAllocationIndex();
        }
        
        Optional<MemoryRegister> register = memory.getInstructions().get(this.pc);
      
        MemoryRegister instruction = register.get();
        String result = String.format("%16s", Integer.toBinaryString(instruction.getValue() & 0xFFFF)).replace(' ', '0');
        Integer res = Integer.parseInt(result,2);

        this.ir = Integer.parseInt(instruction.getOperator().toString() + instruction.getAdress().toString() + res.toString());
        switch (instruction.getOperator()) {                
            case 3 -> executeMov(instruction);
            case 1 -> executeLoad(instruction);
            case 2 -> executeStore(instruction);
            case 4 -> executeSub(instruction);
            case 5 -> executeAdd(instruction);
            default -> {
            }
         
        }
        
        
        
            ArrayList<String> list = new ArrayList<>();
            list.add(this.ax.getValue().toString());
            list.add(this.bx.getValue().toString());
            list.add(this.cx.getValue().toString());
            list.add(this.dx.getValue().toString());
            list.add(this.ac.getValue().toString());
            list.add(this.ir.toString());
            
            list.add(this.pc.toString());
            list.add(instruction.toBinaryString());
                System.out.println("-------------------------------");
            System.out.println("Ax Value:" + this.ax.getValue());
            System.out.println("Bx Value:" + this.bx.getValue());
            System.out.println("Cx Value:" + this.cx.getValue());
            System.out.println("Dx Value:" + this.dx.getValue());
            System.out.println("AC Value:" + this.ac.getValue());
            System.out.println("IR:" + this.ir.toString());
            System.out.println("PC:" + this.pc.toString());
            System.out.println("Binario:" + instruction.toBinaryString());
            System.out.println("-------------------------------");
           this.pc++;
            
        
            
            
            return list;
       
       
            
            
    }
        
    
    public void setCPUMemory(String  path, int memSize){
        this.memory = new Memory(memSize);
        this.loader = new FileLoader(path);        
        this.memory.allocate(loader.getInstrucionSet());
    }
    public void executeAll(String  path, int memSize){
        
        Memory memory = new Memory(memSize);
        FileLoader loader = new FileLoader(path);
        memory.allocate(loader.getInstrucionSet());
        
        
        
        
        for(int i = 0; i < memory.getInstructions().size(); i ++){
            Optional<MemoryRegister> register = memory.getInstructions().get(i);
            MemoryRegister instruction = null;
            if(register.isPresent()){
                instruction = register.get();
                this.pc = i;                
            } else {
                continue;                
            }
            
            String result = String.format("%16s", Integer.toBinaryString(instruction.getValue() & 0xFFFF)).replace(' ', '0');
            Integer res = Integer.parseInt(result,2);
            
            this.ir = Integer.parseInt(instruction.getOperator().toString() + instruction.getAdress().toString() + res.toString());
            switch (instruction.getOperator()) {                
                case 3 -> executeMov(instruction);
                case 1 -> executeLoad(instruction);
                case 2 -> executeStore(instruction);
                case 4 -> executeSub(instruction);
                case 5 -> executeAdd(instruction);
                default -> {
                }
            }
            

            
            
            System.out.println("PC:" + this.pc.toString());
            System.out.println("Binario:" + instruction.toBinaryString());
            /*
            System.out.println("-------------------------------");
            System.out.println("Ax Value:" + this.ax.getValue());
            System.out.println("Bx Value:" + this.bx.getValue());
            System.out.println("Cx Value:" + this.cx.getValue());
            System.out.println("Dx Value:" + this.dx.getValue());
            System.out.println("AC Value:" + this.ac.getValue());
            System.out.println("IR:" + this.ir.toString());
            System.out.println("PC:" + this.pc.toString());
            System.out.println("-------------------------------");
            */
        }
        
        
        
        
    }
    private void executeMov(MemoryRegister reg){
        Integer value = reg.getValue();       
        registerAddressMapper.get(reg.getAdress()).setValue(value);
        
        //Valor de el registro modificado                                           
    }

    private void executeLoad(MemoryRegister reg) {
        Integer value = this.registerAddressMapper.get(reg.getAdress()).getValue();
        this.ac.setValue(value);                        
        
    }

    private void executeStore(MemoryRegister reg) {
         Integer value = this.ac.getValue();
        this.registerAddressMapper.get(reg.getAdress()).setValue(value);
    }

    private void executeSub(MemoryRegister reg) {
        Integer value = this.registerAddressMapper.get(reg.getAdress()).getValue();
        this.ac.setValue(this.ac.getValue()-value);
        
    }

    private void executeAdd(MemoryRegister reg) {
        Integer value = this.registerAddressMapper.get(reg.getAdress()).getValue();
        this.ac.setValue(value+this.ac.getValue());
    }
    
    
    
    
}
