/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;


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
            this.errorHandler = new ErrorHandler(linePos,"Asignación incorrecta","El valor de la asignación no es operable.");                
            return false;
        }
        if(opr==null){
            this.errorHandler = new ErrorHandler(linePos,"Operación no reconocida","La operación en la asignación no es reconocida.");      
        }
        if(reg==null){
            this.errorHandler = new ErrorHandler(linePos,"Registro inváildo","El registro en la asignación es inválido.");      
        }
        if(opr != 3){
            this.errorHandler = new ErrorHandler(linePos,"Operador inválido","El operador no es válido para asignación.");      
            return false;
        }
        
        
        return opr !=null && reg!=null;
              
    }
    private boolean validOperation(String[] splitedLine,int linePos){
         String[] asignation = splitedLine[0].split(" ");
          if(asignation.length <= 1 || asignation.length >2){
              this.errorHandler = new ErrorHandler(linePos,"Operación incorrecta","La sintáxis en la operación es incorrecta.");                
            return false;
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
        
        String[] comaSplit = line.split(",");        
        // Se asigna un valor
        
            String[] splitSpace = comaSplit[0].split(" ");
            int instruction = this.instructionMapper.get(splitSpace[0].toLowerCase());
            
            int address = this.registerMapper.get(splitSpace[1].toLowerCase());
            Integer value = 0;
            
            
            if(comaSplit.length == 2){
                value = Integer.parseInt(comaSplit[1].trim());
            }
            MemoryRegister register = new MemoryRegister(instruction, value,address);            
            
            
            return register;
            
      
        
        
               
        
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    public int getCountErrors() {
        return countErrors;
    }
       
    
    
    
    
    
    
}

