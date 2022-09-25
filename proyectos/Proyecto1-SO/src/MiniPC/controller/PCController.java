/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.controller;

import MiniPC.model.CPU;
import MiniPC.model.Disk;
import MiniPC.model.FileLoader;
import MiniPC.model.Memory;
import MiniPC.model.PCB;
import MiniPC.model.Register;
import MiniPC.view.ProcessManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Queue;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
    private Disk disk;
    private JTable diskTable;
    private int memSize;
    private javax.swing.JButton btnFileLoad;
    //Botones que consultan los PCB's
    private javax.swing.JButton btnStepByStep;    
    
    private javax.swing.JButton btnExeAll;
    private ArrayList<PCB> pcbList = new ArrayList<PCB>();
    
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
        this.diskTable = app.getJTableDisk();
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
        this.disk = new Disk(app.getDiskSize());
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

                // Se verifica si el archivo es valido 
                
                if (testFile(fileList[i].toString())) {
                //Se agregan a la cola de espera del CPU
                    int oneOrCero = (int)Math.round(Math.random());
                    if(oneOrCero==1){
                        PCB pcb = new PCB("Listo", "CPU1");
                        pcb.setLoader(fileList[i].toString());                                                        
                        this.cpu1.addPCBtoQueue(pcb);
                        this.pcbList.add(pcb);
                        this.memory.allocatePCB(pcb);
                    } else {
                        PCB pcb = new PCB("Listo", "CPU2");
                        pcb.setLoader(fileList[i].toString());                                    
                        this.cpu2.addPCBtoQueue(pcb);
                        this.pcbList.add(pcb);
                        this.memory.allocatePCB(pcb);
                    }
                   this.updatePCBStatusTable();
                }
            }            
        }    
        loadPCBstoMem();                
    }
    
    private boolean testFile(String filepath) {
        if (filepath.endsWith(".asm")) {
            FileLoader fileLoader = new FileLoader(filepath);
            if (fileLoader.getCountErrors()>0) {
                JOptionPane.showMessageDialog(this.app, "El archivo en la ruta: " + filepath + " presenta errores de sintaxís\n"+ fileLoader.getErrorMessage(),"MiniPC", 0);
                return false;
            } else {
                return true;
            }
        }
        JOptionPane.showMessageDialog(this.app, "El archivo " + filepath + " no es un archivo .asm\n","MiniPC", 0);
        return false;
    }
    
    
    
    private void loadPCBstoMem(){
                             
        int i =0;
        for(Optional<Register> reg: this.memory.getInstructions()){
            if(!reg.isEmpty()){
                this.memoryTable.setValueAt(reg.get().toBinaryString(),i, 1);
                
            } else {
                this.memoryTable.setValueAt(" ",i, 1);                
            }
            i++;
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
        this.cpu1.executeInstruction(this.memory);                
        this.updatePCBStatusTable();
        loadPCBstoMem();
        this.app.getExecutionTables()[0].getModel().setValueAt(" ", this.cpu1.getCurrentProcessIndex(), this.cpu1.getProcessInstructionIndex());     
        this.updateCPUComponents(this.cpu1);
        this.cpu2.executeInstruction(this.memory);
        loadPCBstoMem();
        this.updatePCBStatusTable();
        this.app.getExecutionTables()[1].getModel().setValueAt(" ", this.cpu2.getCurrentProcessIndex(), this.cpu2.getProcessInstructionIndex());
        this.updateCPUComponents(this.cpu2);
        
        
        
        
        
        
                
       
    }                                           
    private void updatePCBStatusTable(){        
         DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int j) {return false;};
        };
          
        model.setRowCount(this.pcbList.size());
        model.addColumn("Proceso");    
        model.addColumn("Estado");    
        this.app.getProcessTable().setModel(model);
        int i = 0;      
        for(PCB pcb: this.pcbList){                               
            
            this.app.getProcessTable().setValueAt(i, i,0 );
            this.app.getProcessTable().setValueAt(pcb.getStatus(), i,1 );
            i++;
        }
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
