/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.controller;

import MiniPC.model.CPU;
import MiniPC.model.Memory;
import MiniPC.model.PCB;
import MiniPC.model.Register;
import MiniPC.view.ProcessManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Queue;
import javax.swing.JFileChooser;
import javax.swing.JTable;

/**
 *
 * @author ricardosoto
 */
public class PCController {
    //Mostrar los procesos en 
    //Cola de trabajo
    private CPU cpu1;
    private CPU cpu2;
    private Queue<PCB> processQueue;
    private ProcessManager app;
    private Memory memory;
    private JTable memoryTable;
    private int memSize;
    private javax.swing.JButton btnFileLoad;
    //Botones que consultan los PCB's
    private javax.swing.JButton btnStepByStep;    
    
    private javax.swing.JButton btnExeAll;
    
    public PCController(){
        //Cola                     
        
    }
    
    public void init(){
        this.loadApp();        
        this.cpu1 = new CPU("CPU1");
        this.cpu2 = new CPU("CPU2");
        this.btnStepByStep = this.app.getStepByStep();
        this.btnFileLoad = this.app.getLoadBtn();
        this.memoryTable = app.getJTableMemory();
        this.btnExeAll = app.getBtnExeAll();
        this.btnExeAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExeAllActionPerformed(evt);
            }
        });
        this.btnFileLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmLoadActionPerformed(evt);
            }
        });
        this.btnStepByStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStepActionPerformed(evt);
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
    
    
    
    private void filesToPCB(File[] fileList){
        
        if(fileList !=null){
            for(int i = 0 ; i < fileList.length ; i ++){                
                //Se agregan a la cola de espera del CPU
                int oneOrCero = (int)Math.round(Math.random());
                if(oneOrCero==1){
                    PCB pcb = new PCB("Espera", "CPU1");
                    //En esta linea hay que validar que ningún PCB tenga un error;
                    pcb.setLoader(fileList[i].toString());                                                        
                    this.cpu1.addPCBtoQueue(pcb);
                    this.memory.allocatePCB(pcb);
                } else {
                    PCB pcb = new PCB("Espera", "CPU2");
                    pcb.setLoader(fileList[i].toString());                                    
                    this.cpu2.addPCBtoQueue(pcb);
                    this.memory.allocatePCB(pcb);
                }
                
                     
                System.out.println("PCb");
            }
            
        }    
        loadPCBstoMem();                
    }
    private void loadPCBstoMem(){
                             
        int i =0;
        for(Optional<Register> reg: this.memory.getInstructions()){
            if(!reg.isEmpty()){
                this.memoryTable.setValueAt(reg.get().toBinaryString(),i, 1);
                i++;
            } 
        }                         
    }
    
    
    private void btmLoadActionPerformed(java.awt.event.ActionEvent evt) {                                        
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);

        // Show the dialog; wait until dialog is closed
        chooser.showOpenDialog(null);

        // Retrieve the selected files.
        File[] files = chooser.getSelectedFiles();        
        this.filesToPCB(files);
        
       
       
    }               
    private void btnExeAllActionPerformed(java.awt.event.ActionEvent evt) {  
        
        
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
    
    private void btnStepActionPerformed(java.awt.event.ActionEvent evt) {                  
        //Coger un proceso y ejecutarlo en CPU
        this.cpu1.executeInstruction();
        this.app.getExecutionTables()[0].getModel().setValueAt(" ", this.cpu1.getCurrentProcessIndex(), this.cpu1.getProcessInstructionIndex());
        updateCPUComponents(this.cpu1);
        this.cpu2.executeInstruction();
        this.app.getExecutionTables()[1].getModel().setValueAt(" ", this.cpu2.getCurrentProcessIndex(), this.cpu2.getProcessInstructionIndex());
        updateCPUComponents(this.cpu2);
        
        
        
        
                
       
    }                                             
    private void updateCPUComponents(CPU cpu){
        /**list.add(this.ax.getValue().toString());
            list.add(this.bx.getValue().toString());
            list.add(this.cx.getValue().toString());
            list.add(this.dx.getValue().toString());
            list.add(this.ac.getValue().toString());
            list.add(this.ir.toString());
            
            list.add(this.pc.toString());
        */
        ArrayList<String> infoBPC = cpu.getPCBRegisterInfo();
        if(infoBPC== null || infoBPC.isEmpty()){return;}
        javax.swing.JLabel[] labels = this.app.getTextLabelsCpu(cpu.toSring());
        for(int i =0 ; i < 7 ; i ++){
            
            labels[i].setText(infoBPC.get(i));
        }
        
    }
    
    
    
}
