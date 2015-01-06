/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.Event;
import it.polimi.meteocal.business.entity.Invitation;
import it.polimi.meteocal.business.entity.Notification;
import it.polimi.meteocal.business.entity.User;
import it.polimi.meteocal.business.entity.Usernotification;
import it.polimi.meteocal.business.entity.UsernotificationPK;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author teo
 */
@Stateless
public class ManageInvites {

    @PersistenceContext
    EntityManager em;

    private Notification notificationInvite=new Notification();
    private Invitation invitation=new Invitation();
    private Usernotification usernotifications=new Usernotification();
    private UsernotificationPK usernotificationsPK=new UsernotificationPK();

    /**
     * Takes a list of users and a event and sets all the tables Invitation
     * Notification and Usernotification in the right way to create a invitation
     * list
     *
     * @param invited
     * @param event
     */
    public void createInvites(List<User> invited, Event event) {

        notificationInvite.setDescription("You have been invited to the event " + event.getName());
        notificationInvite.setIdEvent(event);
        em.persist(notificationInvite);

        invitation.setIdNotification(notificationInvite);
        invitation.setAccepted(Boolean.FALSE);
        em.persist(invitation);

        for (User invite : invited) {
            usernotificationsPK.setIdNotification(notificationInvite.getIdNotification());
            usernotificationsPK.setIdUser(invite.getIdUser());

            usernotifications.setPending(Boolean.TRUE);
            usernotifications.setUsernotificationPK(usernotificationsPK);
            em.persist(usernotifications);
        }
    }

}
