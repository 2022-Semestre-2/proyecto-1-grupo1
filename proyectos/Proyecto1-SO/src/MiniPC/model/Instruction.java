package MiniPC.model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ricardosoto
 */
public class Instruction {       
    private int weight;
    private String name;
    
    public Instruction(String name,int weight){
        this.name = name;
        this.weight = weight;
        
    }
    
    public int getWeight(){
        return this.weight;
    }
    public String getName(){
        return this.name;
    }
    
}
