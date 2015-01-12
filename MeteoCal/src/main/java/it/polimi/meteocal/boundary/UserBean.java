/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.RegisterValidation;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author teo
 */
@Named
@RequestScoped
public class UserBean {
    @EJB
    RegisterValidation rm;
    @Resource
    UserTransaction utx;
    boolean publicCalendar;
    
    public UserBean() {
    }
    
    public String getName() {
        return rm.getLoggedUser().getName();
    }

    public boolean isPublicCalendar() {
        this.publicCalendar = rm.getLoggedUser().getPublicCalendar();
        return publicCalendar;
    }

    public void setPublicCalendar(boolean publicCalendar) {
        ;//this.publicCalendar = publicCalendar;
    }
    
    
    public void change(){
       this.publicCalendar = !this.isPublicCalendar();
        //System.out.println("qui");
        try {
            utx.begin();
            rm.getLoggedUser().setPublicCalendar(publicCalendar);
            utx.commit();
        } catch (Exception e) {

            e.printStackTrace();
            try {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException exception){
                ;
            }
        
        }
    }
    public void setCalendarVisibility(boolean calendarVisibility){
        
    }
}
