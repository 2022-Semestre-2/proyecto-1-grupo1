/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

/**
 *
 * @author ricardosoto
 */
public class ErrorHandler {
    private final Integer line;
    private final String name;
    private final String description;
    public ErrorHandler(int line, String name, String description){
        this.line = line;
        this.name = name;
        this.description = description;
        
    }
    public String returnErrorMesage(){
        String ms = "";
        Integer linePlus = line+1;
        if(line ==-1){
            ms = ms + "Error: " +name+".\n" + description;
            return ms;
        }
        ms = ms + "Error: " +name+". Línea: " + linePlus.toString()+ "\n" + description;
        return ms;
    }
}
