/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.beans;

import it.polimi.meteocal.business.control.RegisterValidation;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author teo
 */
@Named
@RequestScoped
public class UserBean {
    @EJB
    RegisterValidation rm;
    
    public UserBean() {
    }
    
    public String getName() {
        return rm.getLoggedUser().getName();
    }
}
