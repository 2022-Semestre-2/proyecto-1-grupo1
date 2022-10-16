/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package MiniPC.model.Algoritmos;

import MiniPC.controller.PCController;
import MiniPC.model.PCB;
import java.util.ArrayList;
import java.util.Queue;

/**
 *
 * @author ricardosoto
 */
public interface Algoritmos {
    PCB executeInstruction(Queue<PCB> colaProcesos, PCController cont);
    boolean programIsFinished ();
    ArrayList<String> getStatus();
    
    
    
}
