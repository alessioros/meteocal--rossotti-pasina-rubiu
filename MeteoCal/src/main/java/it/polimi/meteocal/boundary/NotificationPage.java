package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.ManageNotifications;
import it.polimi.meteocal.control.RegisterValidation;
import it.polimi.meteocal.entity.Notification;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.Usernotification;
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
    private String message;
    private List<Notification> notifications = new ArrayList();
    
    /**
     * Returns all the logged user's notifications
     * @return 
     */
    public List<Notification> updateNotifications() {
        user = rv.getLoggedUser();
        
        Query query = em.createQuery("SELECT n FROM Usernotification n WHERE n.user=:USER");
        query.setParameter("USER", user);
        List<Usernotification> tmplist = query.getResultList();
   
        for (Usernotification notification : tmplist) {
            if (notification != null) {
                notifications.add(notification.getNotification());
            }
        }
        return notifications;
    }
    
    /**
     * submits accept notification to ManageNotifications 
     * @param notification 
     */
    public void accept(Notification notification) {
        mn.acceptInvite(notification);
        message="Invite accepted!";
    }
    
    /**
     * submits decline notification to ManageNotifications 
     * @param notification 
     */
    public void decline(Notification notification) {
        mn.declineInvite(notification);
        message="Invite declined";
    }
    
    
    // ----- Getters and setters -----
    
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
