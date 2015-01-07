/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author teo
 */
package it.polimi.business.meteocal.boundary;

import it.polimi.meteocal.business.control.ManageNotifications;
import it.polimi.meteocal.business.control.RegisterValidation;
import it.polimi.meteocal.business.entity.Notification;
import it.polimi.meteocal.business.entity.User;
import it.polimi.meteocal.business.entity.Usernotification;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author teo
 */
@ManagedBean
@ViewScoped
public class NotificationPage {

    @Inject
    ManageNotifications mn;

    @PersistenceContext
    private EntityManager em;

    @EJB
    private RegisterValidation rv;

    private User user;
    private List<Notification> notifications = new ArrayList();
    private Notification notification;

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

    public void accept() {
        mn.acceptInvite(notification);
    }

    public void decline() {
        mn.declineInvite(notification);
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
    
    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
    
}
