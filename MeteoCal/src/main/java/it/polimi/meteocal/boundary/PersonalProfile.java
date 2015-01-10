/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.CheckFields;
import it.polimi.meteocal.control.ManagePersonalData;
import it.polimi.meteocal.control.PasswordEncrypter;
import it.polimi.meteocal.control.RegisterValidation;
import it.polimi.meteocal.entity.User;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alessiorossotti
 */
@Named
@RequestScoped
public class PersonalProfile {
    
    @PersistenceContext
    EntityManager em;

    @Inject
    private ManagePersonalData mpd;

    @Inject
    RegisterValidation rv;
    
    @EJB
    private CheckFields cf;

    private User user;
    private String contact;
    private String message;
    private String confPassword;
    private List<User> contacts;

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }

    public String getConfPassword() {
        return this.confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {

        if (this.user == null) {
            this.user = new User();
        }
        return user;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {

        return contact;
    }

    public User showPersonalData() {
        return user = rv.getLoggedUser();
    }

    public void submitChangeData() {
        
        if (!cf.checkUsername(user.getUsername())) {
            message = "The username you have chosen already exists.";
        } else if (!cf.checkEmail(user.getEmail())) {
            message = "The email you have chosen already exists.";
        } else if (!cf.checkEmailCorrectness(user.getEmail())) {
            message = "Please insert a valid email address.";
        } else if (!cf.checkPassword(user.getPassword(), PasswordEncrypter.encryptPassword(confPassword))) {
            message = "Passwords don't match.";
        } else if (confPassword.length() < 6) {
            message = "Password should be at least 6 characters";
        } else {
            user.setIdUser(rv.getLoggedUser().getIdUser());
            mpd.changeData(user);
        
            this.message = "Data succesfully updated!";
        }
        

    }

    public void submitAddUser() {
        user = rv.getLoggedUser();
        if(!user.getUsername().equals(contact)){
                    mpd.addUser(contact);
        }
        contacts=updateContacts();
    }
    
    /**
     * Prende la collection dei contatti, la casta a list e la mette in contacts
     * @return 
     */
    public List<User> updateContacts() {

        user = rv.getLoggedUser();
        contacts=(List<User>) user.getUserCollection();
        return contacts;   
    }
    
}
