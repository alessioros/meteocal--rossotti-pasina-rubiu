/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.business.meteocal.boundary;

import it.polimi.meteocal.business.control.RegisterValidation;
import it.polimi.meteocal.business.entity.User;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author alessiorossotti
 */
@Named
@RequestScoped
public class Registration {

    @EJB
    private RegisterValidation rv;

    private User user;
    private String message;

    public Registration() {
    }
     public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String register() {
        rv.createUser(user);
        message="Confirmation email sent, please check your email";
        return "";
    }
}




