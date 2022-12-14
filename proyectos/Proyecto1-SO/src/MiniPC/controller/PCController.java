/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.controller;
import MiniPC.model.CPU;
import MiniPC.model.FileLoader;
import MiniPC.model.Memory;
import MiniPC.model.PCB;
import MiniPC.model.Register;
import MiniPC.view.ProcessManager;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ricardosoto
 */
public class PCController {
    //Mostrar los procesos en 
    //Cola de trabajo
    private CPU cpu1;
        
    private ProcessManager app;
    private Memory memory;
    private JTable memoryTable;    
    private JTable diskTable;
    private JTable keyboardTable;
    private JTextField inputArea;        
    private Memory disk;
    private javax.swing.JButton btnFileLoad;
    //Botones que consultan los PCB's
    private javax.swing.JButton btnStepByStep;
    private javax.swing.JButton btnStats;
    
    private javax.swing.JButton btnExeAll;
    private ArrayList<PCB> pcbList = new ArrayList<PCB>();
    
    private int keys = 0;
    
    public PCController(){
        //Cola                     
        
    }
    
    public void init(){
        this.loadApp();        
        
        this.cpu1 = new CPU("CPU1","SJF");        
        this.btnStepByStep = this.app.getStepByStep();
        this.btnFileLoad = this.app.getLoadBtn();
        this.btnStats = this.app.getButtonStats();
        this.memoryTable = app.getJTableMemory();
        this.diskTable = app.getJTableDisk();
        this.keyboardTable = this.app.getJTableKeyboard();
        this.inputArea = this.app.getTextField();
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
        this.app.getButtonClear().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear(evt);
            }
        });
        this.app.getButtonStats().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStats(evt);
            }
        });
        
        this.memory = new Memory(app.getMemSize());
        this.disk = new Memory(app.getDiskSize());
    }
    public void btnClear(java.awt.event.ActionEvent evt) { 
        this.memory = new Memory(app.getMemSize());
        this.disk = new Memory(app.getDiskSize());
        //this.app.dispose();
        this.app.reset();
        this.pcbList.clear();
        updatePCBStatusTable();
        this.cpu1 = new CPU("CPU1","SJF");
        
        

    }
    
    public void btnStats(java.awt.event.ActionEvent evt) {
        String process = "-CPU1-\n";
        for (int i = 0; i < this.cpu1.getStats().size(); i++) {
            process += this.cpu1.getStats().get(i).toString();
            process += "\n";
        }
        /**
        process += "\n-CPU2-\n";
        for (int i = 0; i < this.cpu2.getStats().size(); i++) {
            process += this.cpu2.getStats().get(i).toString();
            process += "\n";
        }
        */
        JOptionPane.showMessageDialog(app, process, "MiniPC", 1);
    }
    private void loadPCBArrival(){  

                JFrame frame = new JFrame("Porfavor ingrese el tiempo de arrivo para los procesos");
        
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try 
                {
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                   e.printStackTrace();
                }
                
                
                
                
                
                
//                
                
                JPanel inputpanel = new JPanel();       
                

                JScrollPane scroller = new JScrollPane(inputpanel);
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                inputpanel.setLayout(new GridLayout(10,10));
                //inputpanel.add(new JLabel ("Porfavor ingrese el tiempo de arrivo para los procesos"));   
                
                
                int i = 0;
                ArrayList<JTextField> inputs = new ArrayList<>();
                for(PCB proc: this.pcbList){
                    JTextField input = new JTextField(20);
                    JLabel label = new JLabel("Proceso "+Integer.toString(i+1)+" \n" +proc.getFileName() );
                    
                    inputpanel.add(label);                    
                    
                    inputpanel.add(input);          
                    inputs.add(input);
                    
                    
                    input.requestFocus();
                    i++;
                }
                
                
                
                
                
                
                
                //inputpanel.add(inputpanel);
                
                JButton boton = new JButton("Confirmar");
                boton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleInputArrival(evt,inputs,frame);
            }
        });
                inputpanel.add(boton);
                
                frame.getContentPane().add(BorderLayout.CENTER, inputpanel);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);
                
                
        
       
       
    }  
    private void handleInputArrival(java.awt.event.ActionEvent evt,ArrayList<JTextField> inputs ,JFrame frame) {
        int i = 0;
        for (JTextField input: inputs){
            this.pcbList.get(i).setArrivalTime(Integer.parseInt(input.getText()));
            System.out.println(this.pcbList.get(i).getArrivalTime());
            i++;
        }
        frame.setVisible(false);
        
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
                    
                        PCB pcb = new PCB("Listo"); 
                        pcb.setFileName(fileList[i].toString());
                        
                        pcb.setLoader(fileList[i].toString());                                                        
                        if(pcb.getPCBData().size()+pcb.getInstructions().size() >this.memory.getSize()){
                            JOptionPane.showMessageDialog(this.app, "El archivo " + fileList[i] + " no se carg?? debido a que su tama??o no cabe en memoria\n","MiniPC", 0);
                            continue;
                        }
                        
                        this.pcbList.add(pcb);
                        if(!this.memory.allocatePCB(pcb)){                            
                            this.disk.allocatePCB(pcb);
                            pcb.setStatus("Prep");
                            
                        }else {
                            this.cpu1.addPCBtoQueue(pcb);
                        }
                                                                   
                    
                   this.updatePCBStatusTable();
                }
            }            
        }    
        loadPCBstoMem();                
     //   cpu1.loadPCBSArrival();
    }
    
    private boolean testFile(String filepath) {
        if (filepath.endsWith(".asm")) {
            FileLoader fileLoader = new FileLoader(filepath);
            if (fileLoader.getCountErrors()>0) {
                JOptionPane.showMessageDialog(this.app, "El archivo en la ruta: " + filepath + " presenta errores de sintax??s\n"+ fileLoader.getErrorMessage(),"MiniPC", 0);
                return false;
            } else {
                return true;
            }
        }
        JOptionPane.showMessageDialog(this.app, "El archivo " + filepath + " no es un archivo .asm\n","MiniPC", 0);
        return false;
    }
    
    
    
    public void loadPCBstoMem(){
                             
        int i =0;
        for(Optional<Register> reg: this.memory.getInstructions()){
            if(!reg.isEmpty()){
                this.memoryTable.setValueAt(reg.get().toBinaryString(),i, 1);
                
            } else {
                this.memoryTable.setValueAt(" ",i, 1);                
            }
            i++;
        }                         
        int j =0;
        for(Optional<Register> reg: this.disk.getInstructions()){
            if(!reg.isEmpty()){
                this.app.getDiskTable().setValueAt(reg.get().toBinaryString(),j, 1);
                
            } else {
                this.app.getDiskTable().setValueAt(" ",j, 1);                
            }
            j++;
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
        this.loadPCBArrival();
        
       
       
    }               
    private void btnExeAllActionPerformed(java.awt.event.ActionEvent evt) { 
        while(!this.cpu1.getProcesesQueue().isEmpty() ){
            
                 this.cpu1.executeAll(this.memory, this.disk, this.cpu1, this.cpu1, this); 
                
            

                
                    // here goes your code to delay
            
        
           
            
            
            
                  // Your database code here            
             
        }
        
    }        
    public ProcessManager getApp(){
        return this.app;
    }
    
    private void btnStepActionPerformed(java.awt.event.ActionEvent evt) {                  
        //Coger un proceso y ejecutarlo en CPU|
        PCController c = this;
        this.cpu1.executeInstructionAlgorithm (this.memory, this.disk,c);
        this.updatePCBStatusTable();
        this.loadPCBstoMem();
        if(this.cpu1.getProcessInstructionIndex()!=0){
            this.app.getExecutionTables()[0].getModel().setValueAt(" ", this.cpu1.getCurrentProcessIndex(), this.cpu1.getProcessInstructionIndex());     
        }
        
        this.updateCPUComponents(this.cpu1);
                
        
        
        
        
        
        
                
       
    }                                           
    public void updatePCBStatusTable(){        
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
    
    public void updateCPUComponents(CPU cpu){
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
        if (infoBPC.get(8).equals("true")) {
            this.app.getJTableKeyboard().setValueAt(infoBPC.get(3), this.app.getKeys(), 0);
            
            this.app.setKeys(this.app.getKeys()+1);
        }
    }
    
    
    
}
