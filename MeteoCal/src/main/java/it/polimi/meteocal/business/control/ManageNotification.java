/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.Notification;
import it.polimi.meteocal.business.entity.User;
import it.polimi.meteocal.business.entity.Usernotification;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author teo
 */
@Named
@RequestScoped
public class ManageNotification {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private RegisterValidation rv;   

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private List<Notification> notifications;

    public List<Notification> getNotifications() {

        user = rv.getLoggedUser();

        Query query = em.createQuery("SELECT n FROM Usernotification n WHERE n.user.idUser=:ID");
        query.setParameter("ID", user.getIdUser());
        List<Usernotification> tmplist = query.getResultList();
        Iterator<Usernotification> tmpIter = tmplist.iterator();
        while (tmpIter.hasNext()) {
            notifications.add(tmpIter.next().getNotification());
        }

        return notifications;
    }
}
