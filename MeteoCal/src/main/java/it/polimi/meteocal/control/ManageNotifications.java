/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

import it.polimi.meteocal.boundary.SendEmailBean;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.Notification;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.Usernotification;
import it.polimi.meteocal.entity.UsernotificationPK;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author teo
 */
@Stateless
public class ManageNotifications {

    @PersistenceContext
    EntityManager em;

    @Inject
    RegisterValidation rv;

    @EJB
    SendEmailBean se;

    private Notification notification;
    private Usernotification usernotifications;
    private UsernotificationPK usernotificationsPK;

    public void sendNotifications(List<User> users, Event event, String description, String emailSubject, String emailTextP, String emailText) throws MessagingException {

        notification = new Notification();

        notification.setDescription(description);
        notification.setIdEvent(event);
        em.persist(notification);

        for (User user : users) {
            
            usernotifications = new Usernotification();
            usernotificationsPK = new UsernotificationPK();
            
            usernotificationsPK.setIdNotification(notification.getIdNotification());
            usernotificationsPK.setIdUser(user.getIdUser());

            usernotifications.setPending(Boolean.TRUE);
            usernotifications.setUsernotificationPK(usernotificationsPK);
            em.persist(usernotifications);

            em.flush();
            em.merge(usernotifications);
            em.refresh(usernotifications);
            /* if (event.getPublic1()) {
             se.generateAndSendEmail(user.getEmail(), emailSubject ,emailTextP );
             } else {
             se.generateAndSendEmail(user.getEmail(), emailSubject, emailText);
             }*/
        }
    }
}
