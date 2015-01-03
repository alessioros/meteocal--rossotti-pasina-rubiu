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
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    private Collection<User> contacts;

    public Collection<User> getContacts() {
        return contacts;
    }

    public void setContacts(Collection<User> contacts) {
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
        boolean success = false;

        success = mpd.changeData(user);

        if (success) {
            this.message = "Data succesfully updated!";
        } else {
            this.message = "Something wrong!";
        }

        return "";

    }

    public void submitAddUser() {
        ad.addUser(contact);
    }
    
    /**
     * In teoria dovrebbe aggiornare i contatti per visualizzare nella pagina ma
     * non so bene come fare la query, quindi per ora non funziona
     * @return 
     */
    public Collection<User> updateContacts() {

        user = rv.getLoggedUser();

        Query query = em.createQuery("SELECT c.idContact FROM join table Contact c WHERE c.idUser=:ID");
        query.setParameter("ID", user.getIdUser());
        Collection<User> tmplist = query.getResultList();
        Iterator<User> tmpIter = tmplist.iterator();
        while (tmpIter.hasNext()) {
            contacts.add(tmpIter.next());
        }

        return contacts;
    }
    
}
