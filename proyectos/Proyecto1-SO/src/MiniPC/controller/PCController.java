/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.controller;

import MiniPC.model.Memory;
import MiniPC.model.PCB;
import MiniPC.view.ProcessManager;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JTable;

/**
 *
 * @author ricardosoto
 */
public class PCController {
    //Mostrar los procesos en 
    //Cola de trabajo
    private Queue<PCB> processQueue;
    private ProcessManager app;
    private Memory memory;
    private JTable memoryTable;
    private int memSize;
    
    //Botones que consultan los PCB's
    private javax.swing.JButton btnStepByStep;
    
    private javax.swing.JButton btnExeAll;
    
    public PCController(){
        //Cola
        
        this.loadApp();
        this.app.setVisible(true);        
        this.processQueue = new LinkedList<PCB>();
        this.btnStepByStep = this.app.getStepByStep();
        this.memoryTable = app.getJTableMemory();
        this.btnStepByStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStepByStepActionPerformed(evt);
            }
        });
        
        this.memory = new Memory(app.getMemSize());

        
        
    }
    
    private void loadApp(){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProcessManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProcessManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProcessManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProcessManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        this.app  = new ProcessManager();
        this.app.setVisible(true);
    }
    
    
    
    private void filesToPCB(){
        File[] fileList = this.app.getLoadedFileList();
        if(fileList !=null){
            for(int i = 0 ; i < fileList.length ; i ++){
                //Aqui hay que validar que ningún PCB tenga un error;
                PCB pcb = new PCB("Espera", "CPU1");
                pcb.setLoader(fileList[i].toString());                
                processQueue.add(pcb);               
                System.out.println("PCb");
            }
            
        }    
        loadPCBstoMem();
        
        
    }
    public void loadPCBstoMem(){
        
        for(PCB pcb: processQueue){
          this.memory.allocatePCB(pcb);            
        }        
          
        
    }
    private void btnStepByStepActionPerformed(java.awt.event.ActionEvent evt) {  
        
        if(this.processQueue.isEmpty()){
            filesToPCB();
        }    
        int i = 0;
        while (!processQueue.isEmpty()){
            PCB pcb = processQueue.remove();   
            System.out.println(pcb.getPCBinstrucctionSize());
            for(int j = 0 ; j < pcb.getPCBinstrucctionSize();j++){
                pcb.executeInstruction();
                System.out.println("PCb executed");
                app.getExecutionTables()[0].getModel().setValueAt(" ", i, j+1);
                //Tabla añadir proceso y colocar con X
            }
            i++;
            
            
            
        }
        
       
    }                                             
    
    
    
}
