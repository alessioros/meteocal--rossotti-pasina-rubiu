/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.business.meteocal.boundary;

import it.polimi.meteocal.business.control.CheckFields;
import it.polimi.meteocal.business.control.PasswordEncrypter;
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
    @EJB
    private CheckFields cf;

    private User user;
    private String message;
    private String confpassword;

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

    public String getConfpassword() {
        return confpassword;
    }

    public void setConfpassword(String confpassword) {
        this.confpassword = confpassword;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * checks the fields of the registrations and if OK creates the user
     */
    public void register() {
        if (!cf.checkUsername(user.getUsername())) {
            message = "The username you have chosen already exists.";
        } else if (!cf.checkEmail(user.getEmail())) {
            message = "The email you have chosen already exists.";
        } else if (!cf.checkEmailCorrectness(user.getEmail())) {
            message = "Please insert a valid email address.";
        } else if (!cf.checkPassword(user.getPassword(), PasswordEncrypter.encryptPassword(confpassword))) {
            message = "Passwords don't match.";
        } else if (confpassword.length() < 6) {
            message = "Password should be at least 6 characters";
        } else {
            rv.createUser(user);
            message = "Confirmation email sent, please check your email";
        }
    }
}
