/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.business.meteocal.boundary;

import java.util.Date;
import java.text.SimpleDateFormat;

import it.polimi.meteocal.business.control.ManageCalendar;
import it.polimi.meteocal.business.entity.User;
import it.polimi.meteocal.business.entity.Event;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Simone
 */
@Named
@RequestScoped
public class Calendar {
    
    @EJB
    private ManageCalendar mc;
    
    private User user;
    private static Date date;
    
    
    public Date getDate() {
        if(date == null)
            date=new Date();
        return date;
    }   
    public String getDayNum(){
        return new SimpleDateFormat("d").format(this.getDate());
    } 
    public String getDayStr(){
        return new SimpleDateFormat("E").format(this.getDate());
    }
    public String getMonthStr(){
        return new SimpleDateFormat("MMMM").format(this.getDate());
    }
    public String getMonthNum(){
        return new SimpleDateFormat("M").format(this.getDate());
    }
    public String getYear(){
        return new SimpleDateFormat("y").format(this.getDate());
    }
    public String getHour(){
        return new SimpleDateFormat("H").format(this.getDate());
    }
    public String getMinute(){
        return new SimpleDateFormat("m").format(this.getDate());
    }
    public void next(){
        
            date.setDate(1);
            date.setMonth(this.getDate().getMonth()+1);
    }
    public void prev(){
        date.setDate(1);
        date.setMonth(this.getDate().getMonth()-1);
    }
    
    
    public void setUser(User user){
        this.user=user;
    }
    
    public User getUser(){
    
        if(this.user==null){
            this.user=new User();
        }
        return user;
    }
    
}
