/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

import it.polimi.meteocal.boundary.SendEmailBean;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.Invitation;
import it.polimi.meteocal.entity.Notification;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.Usernotification;
import it.polimi.meteocal.entity.UsernotificationPK;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author teo
 */
@Stateless
public class ManageInvites {

    @EJB
    SendEmailBean se;

    @PersistenceContext
    EntityManager em;

    private Notification notificationInvite;
    private Invitation invitation;
    private Usernotification usernotifications;
    private UsernotificationPK usernotificationsPK;

    /**
     * Takes a list of users and a event and sets all the tables Invitation
     * Notification and Usernotification in the right way to create a invitation
     * list
     *
     * @param invited
     * @param event
     */
    public void createInvites(List<User> invited, Event event) throws MessagingException {

        notificationInvite = new Notification();
        invitation = new Invitation();
        usernotifications = new Usernotification();
        usernotificationsPK = new UsernotificationPK();

        notificationInvite.setDescription("You have been invited to the event " + event.getName());
        notificationInvite.setIdEvent(event);
        em.persist(notificationInvite);

       /* invitation.setIdNotification(notificationInvite);
        invitation.setAccepted(Boolean.FALSE);
        em.persist(invitation);*/

        for (User invite : invited) {
            usernotificationsPK.setIdNotification(notificationInvite.getIdNotification());
            usernotificationsPK.setIdUser(invite.getIdUser());

            usernotifications.setPending(Boolean.TRUE);
            usernotifications.setUsernotificationPK(usernotificationsPK);
            em.persist(usernotifications);
            em.flush();
            em.merge(usernotifications);
            em.refresh(usernotifications);
           /* if (event.getPublic1()) {
                se.generateAndSendEmail(invite.getEmail(), "Invite to event", "You have been invited"
                        + "to event <a href=\"http://localhost:8080/MeteoCal/eventDetails.xhtml?id=" + event.getIdEvent()
                        + "\">" + event.getName() + "</a>");
            } else {
                se.generateAndSendEmail(invite.getEmail(), "Invite to event", "You have been invited"
                        + "to event "+event.getName());
            }*/
        }
    }

}
