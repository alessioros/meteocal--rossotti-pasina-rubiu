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
    private List<Usernotification> userNotifications = new ArrayList();
    
    /**
     * Returns all the logged user's userNotifications
     * @return 
     */
    public List<Usernotification> updateNotifications() {

        user = rv.getLoggedUser();

        Query query = em.createQuery("SELECT n FROM Usernotification n WHERE n.user=:USER and n.pending=true");
        query.setParameter("USER", user);
        List<Usernotification> tmplist = query.getResultList();
        
        for (Usernotification un : tmplist) {
            if (un != null) {
                userNotifications.add(un);
            }
        }
      
      
        return userNotifications;
    }
    
    /**
     * submits accept notification to ManageNotifications 
     * @param notification 
     */
    public String accept(Usernotification un) {
         mi.acceptInvite(un);
        return "/loggeduser/eventDetails.xhtml?faces-redirect=true&id=" + un.getNotification().getIdEvent().getIdEvent();
    }
    
    /**
     * submits decline notification to ManageNotifications 
     * @param un 
     */
    public String decline(Usernotification un) {
        mi.declineInvite(un);
        return "notifications?faces-redirect=true";
    }
    
    public String ok(Usernotification un) {
        mi.declineInvite(un);
        return "/loggeduser/eventDetails.xhtml?faces-redirect=true&id=" + un.getNotification().getIdEvent().getIdEvent();
    }
    

    // ----- Getters and setters -----
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Usernotification> getUserNotifications() {
        return userNotifications;
    }

    public void setUserNotifications(List<Usernotification> userNotifications) {
        this.userNotifications = userNotifications;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
