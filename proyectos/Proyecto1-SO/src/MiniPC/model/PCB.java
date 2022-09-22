/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.Stack;


/**
 *
 * @author ricardosoto
 */
public class PCB {
    
         /** Estados; nuevo, preparado, ejecución, en espera, finalizado
 Contador del programa (ubicación del programa cargado en memoria)
* 
 Registros AC, AX, BX, CX, DX
 Información de la pila: definir tamaño de 5, y tomar en cuenta error de desbordamiento
 Información contable; el cpu donde se está ejecutando, tiempo de inicio, tiempo empleado.
 Información del estado de E/S; lista de archivos abiertos */
    //Formato BPC:
    /**
     * STATUS
     * AC
     * AX
     * BX
     * CX
     * DX
     * STACK VALUE 1
     * STACK VALUE 2
     * STACK VALUE 3
     * STACK VALUE 4
     * STACK VALUE 5
     * CPU1 // Puede ser 1 o 2, se guarda como string
     * STARTTIME
     * ELAPSEDTIME
     * Instrucción1
     * Instrucción2
     * ....
     * ....
     * Instrucción N
     */
    private final HashMap<Integer,CPURegister> registerAddressMapper = new HashMap<>();
    private final CPURegister ax = new CPURegister(0);
    private final CPURegister bx = new CPURegister(0);
    private final CPURegister cx = new CPURegister(0);
    private final CPURegister dx = new CPURegister(0); 
    private final CPURegister ac = new CPURegister(0);    
    private Integer ir =0;
    private Integer pc =0;
    private Memory memory;
    private final int STACKCAPACITY= 5;
    private FileLoader loader;
    private String currentCPU;
    private String status;    
    
    private boolean programFinished = false;   
    //Bandera para el comparador cmp ax, dx. Si son iguales la bandera se pone en true y por ende se usara el comparador
    private boolean comparatorFlag = false;
    //Pila
    private Stack<Integer> stack = new Stack();    
    //CPU_Menu menu = new CPU_Menu();
    
    
    public PCB(){        
        this.registerAddressMapper.put(1, ax);
        this.registerAddressMapper.put(2, bx);
        this.registerAddressMapper.put(3, cx);
        this.registerAddressMapper.put(4, dx);        
                
        
    }
    
    
    public ArrayList<Integer> getPCBData(){
        //Convierte todo a Entero, binario y lo envía en una lista para guardar en memoria
        ArrayList<Integer> returnArr = new ArrayList<Integer>();
        String statusParse = this.status;
        String kint = "";
        for(int i = 0; i < statusParse.toCharArray().length; i ++){
           kint+=Integer.toBinaryString(statusParse.toCharArray()[i]);          
       }
        returnArr.add(Integer.parseInt(kint,2));
        returnArr.add(this.ac.getValue());
        returnArr.add(this.ax.getValue());
        returnArr.add(this.bx.getValue());
        returnArr.add(this.cx.getValue());
        returnArr.add(this.dx.getValue());
        for(int i = 0 ; i < this.STACKCAPACITY; i ++){
            if(stack.get(i)!=null){
                returnArr.add(stack.get(i));
            }else{
                returnArr.add(0);
            }                        
        }
        String cpuCurr = this.currentCPU;
        String cpuToBinary = "";
        for(int i = 0; i < cpuCurr.toCharArray().length; i ++){
           cpuToBinary+=Integer.toBinaryString(cpuCurr.toCharArray()[i]);          
       }
        returnArr.add(Integer.parseInt(cpuToBinary,2));
        Long time = new Date().getTime();
        returnArr.add(time.intValue());
        //el tiempo recorrido se agrega cuando se termine el proceso
        returnArr.add(time.intValue());
        
        return null;
     /** STATUS
     * AC
     * AX
     * BX
     * CX
     * DX
     * STACK VALUE 1
     * STACK VALUE 2
     * STACK VALUE 3
     * STACK VALUE 4
     * STACK VALUE 5
     * CPU1 // Puede ser 1 o 2, se guarda como string
     * STARTTIME
     * ELAPSEDTIME
     * Instrucción1
     * Instrucción2
     * ....
     * ....
     * Instrucción N
     */
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
                case 9 -> executeInterruption(instruction);
                case 10 -> executeJmp(instruction);
                case 11 -> executeCmp(instruction);
                case 12 -> executeJe(instruction);
                case 13 -> executeJne(instruction);
                case 14 -> executeParam(instruction);
                case 15 -> executePush(instruction);
                case 16 -> executePop(instruction);
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
            System.out.println("Pila:" + Arrays.asList(this.stack));            
            System.out.println("Binario:" + instruction.toBinaryString());
            System.out.println("-------------------------------");
           this.pc++;
            
        
            
            
            return list;
       
       
            
            
    }
    public int getPCBinstrucctionSize(){
        return this.memory.getAllocationIndex()+this.loader.getInstrucionSet().size();
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
                case 11 -> executeCmp(instruction);
                case 12 -> executeJe(instruction);
                case 13 -> executeJne(instruction);
                case 14 -> executeParam(instruction);
                case 15 -> executePush(instruction);
                case 16 -> executePop(instruction);
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
            System.out.println("Pila:" + Arrays.asList(this.stack));            
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
    private void executeCmp(MemoryRegister reg){        
        
        this.comparatorFlag = this.registerAddressMapper.get(reg.getAdress()).getValue().equals(reg.getValue());        
        System.out.println(this.registerAddressMapper.get(reg.getAdress()).getValue()+" "+reg.getValue());
        
        //compara los valores, setea la bandera
    }
    private void executeJe(MemoryRegister reg){        
        if(this.comparatorFlag){           
            
            executeJmp(reg);
            this.comparatorFlag = !this.comparatorFlag;
        }
        
    }
    private void executeJne(MemoryRegister reg){        
        
        if(!this.comparatorFlag){
            
            
            executeJmp(reg);
        }
        
    }
    private void executeParam(MemoryRegister instruction){
        for(int value: instruction.getValues()){
            stack.push(value);
        }
        
    }
    private void executePush(MemoryRegister instruction){
        this.stack.push(registerAddressMapper.get(instruction.getAdress()).getValue());
    }
    
    private void executePop(MemoryRegister instruction){
        //Se setea el valor
        this.registerAddressMapper.get(instruction.getAdress()).setValue(this.stack.pop());
        
        
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
