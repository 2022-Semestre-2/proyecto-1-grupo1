/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

/**
 *
 * @author ricardosoto
 */
public class InformationRegister implements Register {
    private Integer value;

    @Override
    public void setValue(Integer value) {
        this.value = value;
        
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
    @Override
    public String toBinaryString(){
        String ms = "";
         
        String value = String.format("%16s", Integer.toBinaryString(this.getValue() & 0xFFFF)).replace(' ', '0');                        
        ms = ms + value + " " + "\n";
        
        return ms;
    }
    
}
