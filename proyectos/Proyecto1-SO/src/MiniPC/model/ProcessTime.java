/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniPC.model;

import java.util.Date;

/**
 *
 * @author Administrador
 */
public class ProcessTime {
    
    int startHour;
    int finishHour;
    
    int startMinute;
    int finishMinute;
    
    int duration;
    int index;

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getFinishHour() {
        return finishHour;
    }

    public void setFinishHour(int finishHour) {
        this.finishHour = finishHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getFinishMinute() {
        
        return finishMinute;
    }

    public void setFinishMinute(int finishMinute) {
        this.finishMinute = finishMinute;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void getFinishTime() {
        
        
        int seconds = this.duration;
        while(seconds>59) {
            this.finishMinute++;
            seconds-=60;
        }
        
        while (this.finishMinute>59) {
            this.finishHour++;
            this.finishMinute-=60;
        }
        
        
    }

    @Override
    public String toString() {
        getFinishTime();
        return "Proceso:"+this.index+ " | Hora inicio = "+startHour+":"+startMinute+" | Hora Fin = "+finishHour+":"+finishMinute+" | Duracion en segundos = " + duration;
    }

    

    
    
    
    
    
    
    
    
    
    
    
}
