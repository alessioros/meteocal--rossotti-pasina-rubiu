/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.Invitation;
import it.polimi.meteocal.business.entity.Notification;
import it.polimi.meteocal.business.entity.User;
import it.polimi.meteocal.business.entity.Usernotification;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
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
public class ManageNotifications {

    @Resource
    UserTransaction utx;

    @PersistenceContext
    EntityManager em;

    @Inject
    RegisterValidation rv;

    private List<Invitation> invite;
    private User user;
    private List<Usernotification> usernot;

    /**
     * Setta il parametro accepted in invitation come true e mette l'evento nel
     * calendario dell'utente
     *
     * @param notification
     */
    public void acceptInvite(Notification notification) {

        try {

            utx.begin();

            invite = em.createQuery("select i from Invitation i where i.idNotification=:N").setParameter("N", notification).getResultList();
            invite.get(0).setAccepted(Boolean.TRUE);

            user = rv.getLoggedUser();
            user.getEventCollection().add(notification.getIdEvent());

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
        usernot = em.createQuery("select un from Usernotification un where un.user=:U").setParameter("U", user).getResultList();
        for (Usernotification usernotification : usernot) {
            if (usernotification.getNotification().equals(notification)) {
                em.remove(usernotification);
            }
        }
    }
}
