/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author ricardosoto
 */
public class FileLoader {
    
    String fileDirectory;
    ArrayList<MemoryRegister> instructionSet = new ArrayList<>();
    HashMap<String,Integer> instructionMapper;
    ArrayList<String> instructions;
    HashMap<String,Integer> registerMapper;
    int countErrors = 0;
    private ErrorHandler errorHandler;
    
    public FileLoader(String path){       
  
        this.loadMapper();
        this.loadInstructionSet(path);
        
    }  
    
    
    // Retorna un array con las instrucciones en string: {"MOV AX, 10", "ADD BX", "MOV BX, 10"}
    // El IRController se encarga de descomponer cada instruccion
    public ArrayList<MemoryRegister> getInstrucionSet(){
        return this.instructionSet;
    }
    
    private void loadInstructionSet(String path){        
        BufferedReader reader;        
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            int linePos = 0;
            while(line != null) {          
                if(!this.validGrammar(line,linePos)){                                                                               
                    countErrors++;
                    return;
                } 
                this.instructionSet.add(processInstruction(line));
                linePos++;
                line = reader.readLine();
                
            }
            if(linePos ==0){
                countErrors++;
                this.errorHandler = new ErrorHandler(-1,"Archivo vació","El archivo cargado no tiene contendio.");                               
         }
        
            
        } catch (IOException e) {
            countErrors++;
            this.errorHandler = new ErrorHandler(-1,"Lectura de archivo","La lectura en el archivo ha sido fallida.");                                        
        }
    }
    
    private void loadMapper(){
        this.instructionMapper = new HashMap<>();
        this.registerMapper = new HashMap<>();
        
        this.instructionMapper.put("mov", 3);
        this.instructionMapper.put("load", 1);
        this.instructionMapper.put("store", 2);
        this.instructionMapper.put("sub", 4);
        this.instructionMapper.put("add", 5);
        this.instructionMapper.put("inc", 6);
        this.instructionMapper.put("dec", 7);
        this.instructionMapper.put("swap", 8);         
        this.instructionMapper.put("int", 9);
        this.instructionMapper.put("jmp", 10);
        this.instructionMapper.put("cmp", 12);
        this.instructionMapper.put("je", 13);
        this.instructionMapper.put("jne", 14);
        this.instructionMapper.put("param", 15);
        this.instructionMapper.put("push", 16);
        this.instructionMapper.put("pop", 17);        
         
        
        this.registerMapper.put("ax", 1);
        this.registerMapper.put("bx", 2);
        this.registerMapper.put("cx", 3);
        this.registerMapper.put("dx", 4);
        
    }
    public String getErrorMessage(){
        return this.errorHandler.returnErrorMesage();
    }
    private boolean validGrammar(String line, int linePos){
        String[] comaSplit = line.split(",");        
        switch (comaSplit.length) {
            case 2:
                return this.validAsignation(comaSplit,linePos);
            case 1:
                return this.validOperation(comaSplit,linePos);
            default:
                this.errorHandler = new ErrorHandler(linePos,"Sintaxis inválida","La sintaxis no es reconocida.");                
                return false;
        }
        
        
    }
    
    private boolean validAsignation(String[] splitedLine,int linePos){
        
        String[] asignation = splitedLine[0].split(" ");
        Integer opr = instructionMapper.get(asignation[0].toLowerCase());
        if(asignation.length <= 1 || asignation.length >2){
            this.errorHandler = new ErrorHandler(linePos,"Asignación incorrecta","La sintáxis en la asignación es incorrecta.");                
            return false;
            
        }
        Integer reg = registerMapper.get(asignation[1].toLowerCase());
        try{             
            Integer.parseInt(splitedLine[1].trim());            
        } catch(NumberFormatException e){          
            //Si es un registro, caso swap ax, bx ó mov ax, cx
            if(registerMapper.get(splitedLine[1].trim().toLowerCase()) != null){
                
            } else {
                this.errorHandler = new ErrorHandler(linePos,"Asignación incorrecta","El valor de la asignación no es operable.");                
                return false;
            }
            
        }
        if(opr==null){
            this.errorHandler = new ErrorHandler(linePos,"Operación no reconocida","La operación en la asignación no es reconocida.");      
        }
        if(reg==null){
            this.errorHandler = new ErrorHandler(linePos,"Registro inváildo","El registro en la asignación es inválido.");      
        }
        if(opr != 3 && opr!=8 && opr!=12 && opr!=15){
            this.errorHandler = new ErrorHandler(linePos,"Operador inválido","El operador no es válido para asignación.");      
            return false;
        }
        
        
        return opr !=null && reg!=null;
              
    }
    private boolean validOperation(String[] splitedLine,int linePos){
         String[] asignation = splitedLine[0].split(" ");
         
          if(asignation.length < 1 || asignation.length >2){
              this.errorHandler = new ErrorHandler(linePos,"Operación incorrecta","La sintáxis en la operación es incorrecta.");                
            return false;
        }
          //Si es una interrupción
         if(instructionMapper.get(asignation[0].toLowerCase())==9){
             
             return this.validInterruption(asignation);
         }
         //Si es un jump
         int inst=instructionMapper.get(asignation[0].toLowerCase());
         if(inst ==10 || inst==11 || inst==12){             
             return this.validJump(asignation);
         }
         if(asignation.length==1){
             return true;
         }
        
          
        Integer opr = instructionMapper.get(asignation[0].toLowerCase());
        Integer reg = registerMapper.get(asignation[1].toLowerCase());
        if(opr==null){
            this.errorHandler = new ErrorHandler(linePos,"Operador no reconocido","El operador en la operación no es reonocido.");      
        }
        if(reg==null){
            
            this.errorHandler = new ErrorHandler(linePos,"Registro inváildo","El registro en la operación es inválido.");      
        }
        
        if(opr == 3){
            this.errorHandler = new ErrorHandler(linePos,"Operador inválido","El operador (MOV) no es válido para la operación.");      
            return false;
        }
        return opr!=null && reg!=null;
        
    }
    private MemoryRegister processInstruction(String line){
        MemoryRegister register= null;
        String[] comaSplit = line.split(",");        
        // Se asigna un valor
        
            //Si es una interrupción
            
            String[] splitSpace = comaSplit[0].split(" ");
            
            int instruction = this.instructionMapper.get(splitSpace[0].toLowerCase());
            
            //En el caso de que sea INC o DEC
            if(splitSpace.length==1){
                register= new MemoryRegister(instruction, 0,0);
                return register;
                
            }
            // En el caso de que sea una interrupción
            if(instruction==9){                
                String hexa = splitSpace[1];
                hexa = hexa.replace("h"," ");
                hexa =hexa.trim();               
                // En el value de la interrupción se envía la debida                                
                register= new MemoryRegister(instruction, Integer.parseInt(hexa),0);
                register.setInterruption(true);
                return register;
                
            }
            // En el caso de que sea un JMP
               
            if(instruction ==10 || instruction==11 || instruction==12){                                              
                register= new MemoryRegister(instruction, Integer.parseInt(splitSpace[1]),0);
                
                return register;
                
            }
            int address = this.registerMapper.get(splitSpace[1].toLowerCase());
            Integer value = 0;
            
            
            if(comaSplit.length == 2){
                try{
                    value = Integer.parseInt(comaSplit[1].trim());
                    register= new MemoryRegister(instruction, value,address);
                } catch(NumberFormatException e){
                    value = this.registerMapper.get(comaSplit[1].trim().toLowerCase());                                        
                    //El value es 0 porque se utiliza valor de registro                    
                    register= new MemoryRegister(instruction, 0,address);
                    register.setRegisterValue(value);
                    
                    
                }
                
            } else {
                register= new MemoryRegister(instruction, value,address);
            }
            return register;
            
                      
      
        
        
               
        
    }
    private boolean validJump(String[] asignation){
        try{
            Integer.parseInt(asignation[1].trim());
            return true;
        }catch(NumberFormatException e){
            
        }
        return false;
    }
        
    private boolean validInterruption(String[] asignation){
        Pattern pattern = Pattern.compile("[^h]*h");
        Matcher matcher = pattern.matcher(asignation[1].toLowerCase());
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        String hexa = asignation[1].toLowerCase();
        if(count==1){
            hexa = hexa.replace("h"," ");
            hexa =hexa.trim();
            try{
                Integer.parseInt(hexa, 16);
                return true;
            } catch(NumberFormatException e){
                
            }
        }

        

        return false;
    }
        
    public String getFileDirectory() {
        return fileDirectory;
    }

    public int getCountErrors() {
        return countErrors;
    }
       
    
    
    
    
    
    
}

