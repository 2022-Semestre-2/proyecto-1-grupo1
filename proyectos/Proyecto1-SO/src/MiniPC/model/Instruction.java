package MiniPC.model;

import java.util.HashMap;

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
    private int operator;
    private int currentWeight;
    private HashMap<Integer,Integer> weightMapper;
    
    public Instruction(int operator){
        this.currentWeight = 1;
        this.operator = operator;
        JsonParser parser = new JsonParser();
        this.weightMapper = parser.loadJsonWeights();
        this.weight = this.weightMapper.get(this.operator);
        
    }
    
    public int getWeight(){
        return this.weight;
    }
    public boolean isReady(){
        return this.currentWeight==this.weight;
        
    }
    public void registerExe(){
        this.currentWeight++;
    }
    
}
