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
    
    public UserBean() {
    }
    
    public String getName() {
        return rm.getLoggedUser().getName();
    }
    
    public boolean getCalendarVisibility(){
        return rm.getLoggedUser().getPublicCalendar();
    }
    public void setCalendarVisibility(boolean calendarVisibility){
        try {
            utx.begin();
            rm.getLoggedUser().setPublicCalendar(calendarVisibility);
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
}
