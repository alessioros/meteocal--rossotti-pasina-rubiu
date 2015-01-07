/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.Notification;
import it.polimi.meteocal.business.entity.User;
import it.polimi.meteocal.business.entity.Usernotification;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
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
    private List<Notification> notifications=new ArrayList();

    public List<Notification> updateNotifications() {
        user = rv.getLoggedUser();
        //prende tutte le notifiche dell'utente loggato
        Query query = em.createQuery("SELECT n FROM Usernotification n WHERE n.user=:USER");
        query.setParameter("USER", user);
        List<Usernotification> tmplist = query.getResultList();
        //mette le notifiche in notifications
        for (Usernotification notification : tmplist) {
            if (notification != null) {
                notifications.add(notification.getNotification());
            }
        }

        return notifications;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
