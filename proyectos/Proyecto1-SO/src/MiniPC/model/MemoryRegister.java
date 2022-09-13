/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

import java.util.Optional;

/**
 *
 * @author ricardosoto
 */
public class MemoryRegister implements Register{
    private Integer operator;
    //Valor de entero, por ejemplo cuando se hace un Mov ax,10 . El valor de registro es el valor de 0 y el value es 10
    private Integer value;
    private Integer address;
    //Valor de registro, por ejemplo cuando se hace un Mov ax,bx. El valor de registro es el valor de bx y el value es 0
    private Integer registerValue;
    private boolean isInterruption = false;
    
    
    public MemoryRegister(Integer operator,Integer value, Integer address){
        this.address = address;
        this.operator = operator;
        this.value = value;
    }
    
    

    public Integer getOperator() {
        return operator;
    }
    public Integer getAdress() {
        return this.address;
    }
    @Override
    public Integer getValue() {
        return this.value;
    }
    
    @Override
    public void setValue(Integer value){
        this.value = value;
        
    }
    
    @Override
    public String toString(){
        String ms = "";
        ms = ms + this.operator.toString() + "\n";
        ms = ms + this.address.toString() + "\n";
        ms = ms + this.value.toString() + "\n";
        
        return ms;
    }
    public String toBinaryString(){
        String ms = "";
         
        String value = String.format("%16s", Integer.toBinaryString(this.getValue() & 0xFFFF)).replace(' ', '0');
        String op = String.format("%16s", Integer.toBinaryString(this.operator & 0xFFFF)).replace(' ', '0');
        String address = String.format("%16s", Integer.toBinaryString(this.address & 0xFFFF)).replace(' ', '0');
        
        ms = ms + op + " ";
        ms = ms + address + " ";
        ms = ms + value + " " + "\n";
        
        return ms;
    }
    public void setRegisterValue(Integer value){
        this.registerValue = value;   
    }
    public Integer getRegisterValue(){
        return this.registerValue;
    }
    public void setInterruption(boolean isInterruption){
        this.isInterruption = isInterruption;
    }
    public boolean isInterruption(){
        return this.isInterruption;
    }
  
    
}
