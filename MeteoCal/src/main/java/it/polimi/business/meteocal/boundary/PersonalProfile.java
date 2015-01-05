/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.business.meteocal.boundary;

import it.polimi.meteocal.business.control.AddUser;
import it.polimi.meteocal.business.control.ManagePersonalData;
import it.polimi.meteocal.business.control.RegisterValidation;
import it.polimi.meteocal.business.entity.User;
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

    @EJB
    private ManagePersonalData mpd;

    @Inject
    RegisterValidation rv;
    
    @Inject
    AddUser ad;

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

    public String submitChangeData() {

        this.mpd = new ManagePersonalData();

        mpd.changeData(user);
        
        this.message = "Data succesfully updated!";

        return "";

    }

    public void submitAddUser() {
        user = rv.getLoggedUser();
        if(!user.getUsername().equals(contact)){
                    ad.addUser(contact);
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
