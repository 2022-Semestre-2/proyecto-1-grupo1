/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author ricardosoto
 *  obj.get("MemorySize");
    this.diskSize = (int) (long)obj.get("DiskSize");
 */
public class JsonParser {
    private HashMap<String,Integer> instructionMapper;
    private final String CONFIG_FILE = "test/config.json";
    //En el hash se guarda: Instrucción, peso.
    private final HashMap<Integer,Integer> weightMapper;
    public JsonParser(){
        this.weightMapper = new HashMap<Integer,Integer>();
    }
    public HashMap<Integer,Integer> loadJsonWeights(){
        try{
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject)parser.parse(new FileReader(CONFIG_FILE));        
        this.loadInstructionMapper();
        for (String key : this.instructionMapper.keySet()) {
            //System.out.println(this.instructionMapper.get(key)+", "+obj.get(key));
            this.weightMapper.put(this.instructionMapper.get(key), (int)(long) obj.get(key));
            
        }
        return this.weightMapper;
        

        
        
    } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
    }
      return null;
        
    }
    private void loadInstructionMapper(){
        this.instructionMapper = new HashMap<String,Integer>();
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
        this.instructionMapper.put("cmp", 11);
        this.instructionMapper.put("je", 12);
        this.instructionMapper.put("jne", 13);
        this.instructionMapper.put("param", 14);
        this.instructionMapper.put("push", 15);
        this.instructionMapper.put("pop", 16);        
    }

    
}
