/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.Notification;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.Usernotification;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author teo
 */
@ManagedBean
@RequestScoped
public class ManageInvites {
    
    @Resource
    UserTransaction utx;

    @PersistenceContext
    EntityManager em;

    @Inject
    RegisterValidation rv;

    @EJB
    ManageNotifications mn;
    
<<<<<<< HEAD
    private List<Invitation> invite;
    private User user;
    private List<Usernotification> usernot;
    private List<Event> event;

    private Notification notificationInvite;
    private Invitation invitation;
    private Usernotification usernotifications;
    private UsernotificationPK usernotificationsPK;
=======
    private User user;
    private List<Usernotification> usernot;
    private List<Event> event;
>>>>>>> 79b1c1ffaaa7ed125d6bae05b95bf8486150aec7

    /**
     * Takes a list of users and a event and sets all the tables Invitation
     * Notification and Usernotification in the right way to create a invitation
     * list
     *
     * @param invited
     * @param event
     */
    public void createInvites(List<User> invited, Event event) throws MessagingException {

        mn.sendNotifications(invited,
                event,
                "You have been invited to event " + event.getName(),
                "Invite to event ",
                "You have been invited to event <a href=\"http://localhost:8080/MeteoCal/eventDetails.xhtml?id=" + event.getIdEvent() + "\">" + event.getName() + "</a>",
                "You have been invited to event " + event.getName());
    }

    /**
     * Setta il parametro accepted in invitation come true e mette l'evento nel
     * calendario dell'utente
     *
     * @param notification
     */
    public void acceptInvite(Notification notification) {

        try {

            utx.begin();

            /*invite = em.createQuery("select i from Invitation i where i.idNotification=:N").setParameter("N", notification).getResultList();
             invite.get(0).setAccepted(Boolean.TRUE);*/
            //puts the event in the user calendar
            user = rv.getLoggedUser();
            user.getEventCollection().add(notification.getIdEvent());
            //puts the user in the invited of the event
            event = em.createQuery("select e from Event e where e.idEvent=:E").setParameter("E", notification.getIdEvent().getIdEvent()).getResultList();
            event.get(0).getUserCollection().add(user);

            usernot = em.createQuery("select un from Usernotification un where un.user=:U").setParameter("U", user).getResultList();
            for (Usernotification usernotification : usernot) {
                if (usernotification.getNotification().equals(notification)) {
                    em.remove(usernotification);
                }
            }

            utx.commit();

        } catch (Exception e) {

            e.printStackTrace();
            try {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException exception) {
            }

        }
    }

    public void declineInvite(Notification notification) {
        try {

            utx.begin();

            user = rv.getLoggedUser();

            usernot = em.createQuery("select un from Usernotification un where un.user=:U").setParameter("U", user).getResultList();
            for (Usernotification usernotification : usernot) {
                if (usernotification.getNotification().equals(notification)) {
                    em.remove(usernotification);
                }
            }

            utx.commit();

        } catch (Exception e) {

            e.printStackTrace();
            try {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException exception) {
            }

        }
    }
}
