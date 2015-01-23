package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.ManageInvites;
import it.polimi.meteocal.control.RegisterValidation;
import it.polimi.meteocal.entity.Notification;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.Usernotification;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author teo
 */
@ManagedBean
@ViewScoped
public class NotificationPage {

    @Inject
    ManageInvites mi;

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

        Query query = em.createQuery("SELECT n FROM Usernotification n WHERE n.user=:USER and n.pending=true");
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
    public String accept(Notification notification) {
        mi.acceptInvite(notification);
        return "notifications?faces-redirect=true";
    }
    
    /**
     * submits decline notification to ManageNotifications 
     * @param notification 
     */
    public String decline(Notification notification) {
        mi.declineInvite(notification);
        return "notifications?faces-redirect=true";
    }
    
    public String ok(Notification notification) {
        mi.declineInvite(notification);
        return "notifications?faces-redirect=true";
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
