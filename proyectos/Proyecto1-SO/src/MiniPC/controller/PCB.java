/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import MiniPC.model.CPURegister;
import MiniPC.model.FileLoader;
import MiniPC.model.Memory;
import MiniPC.model.MemoryRegister;
import java.util.Scanner;


/**
 *
 * @author ricardosoto
 */
public class PCB {
    
    
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
    //Bandera para el comparador cmp ax, dx. Si son iguales la bandera se pone en true y por ende se usara el comparador
    private boolean comparatorFlag = false;
    //CPU_Menu menu = new CPU_Menu();
    
    
    public PCB(){
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
            case 6 -> executeInc(instruction);
            case 7 -> executeDec(instruction);
            case 8 -> executeSwap(instruction);
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
        this.memory = new Memory(memSize);
        this.loader =  new FileLoader(path);;                        
        this.memory.allocate(this.loader.getInstrucionSet());                              
        if(this.pc ==0 ){
            this.pc = this.memory.getAllocationIndex();
        }                                                                
        
        while(this.pc < this.memory.getAllocationIndex()+this.loader.getInstrucionSet().size()){
            Optional<MemoryRegister> register = memory.getInstructions().get(this.pc);
            MemoryRegister instruction = null;
            if(register.isPresent()){
                instruction = register.get();                
            } else {
                continue;                
            }
            System.out.println(register.get().getValue());
            
            String result = String.format("%16s", Integer.toBinaryString(instruction.getValue() & 0xFFFF)).replace(' ', '0');
            Integer res = Integer.parseInt(result,2);
            
            this.ir = Integer.parseInt(instruction.getOperator().toString() + instruction.getAdress().toString() + res.toString());
            switch (instruction.getOperator()) {                
                case 3 -> executeMov(instruction);
                case 1 -> executeLoad(instruction);
                case 2 -> executeStore(instruction);
                case 4 -> executeSub(instruction);
                case 5 -> executeAdd(instruction);
                case 6 -> executeInc(instruction);
                case 7 -> executeDec(instruction);
                case 8 -> executeSwap(instruction);                
                case 9 -> executeInterruption(instruction);
                case 10 -> executeJmp(instruction);
                default -> {
                    
                }
            }
            

            
            
            System.out.println("PC:" + this.pc.toString());
            System.out.println("Binario:" + instruction.toBinaryString());
            
            System.out.println("-------------------------------");
            System.out.println("Ax Value:" + this.ax.getValue());
            System.out.println("Bx Value:" + this.bx.getValue());
            System.out.println("Cx Value:" + this.cx.getValue());
            System.out.println("Dx Value:" + this.dx.getValue());
            System.out.println("AC Value:" + this.ac.getValue());
            System.out.println("IR:" + this.ir.toString());
            System.out.println("PC:" + this.pc.toString());
            System.out.println("-------------------------------");
            this.pc++;
            
        }
        
        
        
        
    }
    private void executeMov(MemoryRegister reg){
        if(reg.getValue()==0){
            Integer valuereg2 = this.registerAddressMapper.get(reg.getRegisterValue()).getValue();
            this.registerAddressMapper.get(reg.getAdress()).setValue(valuereg2);
            return;
            
        }
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
    private void executeInc(MemoryRegister reg) {
        if(reg.getAdress()==0){
            this.ac.setValue(this.ac.getValue()+1);            
            
        }else {
            Integer value = this.registerAddressMapper.get(reg.getAdress()).getValue();
            this.registerAddressMapper.get(reg.getAdress()).setValue(value+1);
        }
        
        
    }
    private void executeDec(MemoryRegister reg) {
        if(reg.getAdress()==0){
            this.ac.setValue(this.ac.getValue()-1);            
            
        }else {
            Integer value = this.registerAddressMapper.get(reg.getAdress()).getValue();
            this.registerAddressMapper.get(reg.getAdress()).setValue(value-1);
        }
        
        
    }
    private void executeSwap(MemoryRegister reg){        
        Integer valuereg1 = this.registerAddressMapper.get(reg.getAdress()).getValue();
        Integer valuereg2 = this.registerAddressMapper.get(reg.getRegisterValue()).getValue();
        
        this.registerAddressMapper.get(reg.getAdress()).setValue(valuereg2);
        this.registerAddressMapper.get(reg.getRegisterValue()).setValue(valuereg1);

    }
    
    private void executeInterruption(MemoryRegister reg){
        // valor de la interrupción
        int value= reg.getValue();
        switch (value) {                
            
            case 9 -> System.out.println(this.dx.getValue());
            case 10 -> INT10H();
            
            default -> {
                
            }   
        }
        
    }
    private void executeJmp(MemoryRegister reg){
        this.pc = this.pc+reg.getValue();
                
    }
    private void INT10H(){
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object10       
        String userName = myObj.nextLine();  // Read user input
        Integer newDxValue = null;
        try{
            newDxValue = Integer.parseInt(userName);
        }catch(NumberFormatException e){
        }
        this.dx.setValue(newDxValue);
        
    }
    
    
    
    
    
    
}
