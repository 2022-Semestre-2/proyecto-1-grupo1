/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

/**
 *
 * @author ricardosoto
 */
public class CPURegister implements Register{
    private Integer value;
    
    public CPURegister(Integer value){
        this.value = value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
    
        
    
}
