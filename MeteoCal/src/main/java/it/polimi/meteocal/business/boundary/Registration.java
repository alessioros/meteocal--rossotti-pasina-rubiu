/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.boundary;

import it.polimi.meteocal.business.control.RegisterValidation;
import it.polimi.meteocal.business.entity.User;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author miglie
 */
@Named
@RequestScoped
public class Registration {

    @EJB
    private RegisterValidation rv;

    private User user;

    public Registration() {
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
        return "index?faces-redirect=true";
    }

}
