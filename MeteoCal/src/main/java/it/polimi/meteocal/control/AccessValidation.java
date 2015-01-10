/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

import it.polimi.meteocal.boundary.SendEmailBean;
import it.polimi.meteocal.entity.User;
import java.util.UUID;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author alessiorossotti
 */
@ManagedBean
@RequestScoped
public class AccessValidation {

    private User user;

    @PersistenceContext
    EntityManager em;

    @EJB
    private SendEmailBean sm;

    @Resource
    UserTransaction utx;

    /**
     * Sends an email to the user with info for recoverying the password
     *
     * @param email of the user
     */
    public void passwordRecovery(String email) {

        Query query;

        try {
            utx.begin();

            query = em.createQuery("select u from User u where u.email=:em");
            query.setParameter("em", email);
            user = (User) query.getResultList().get(0);
            user.setVerificationkey(UUID.randomUUID().toString());

            utx.commit();

        } catch (Exception e) {

            e.printStackTrace();
            try {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException exception) {
            }

        }

        try {

            sm.generateAndSendEmail(email,
                    "Reset Your Password",
                    "Hei, your username is: " + user.getUsername()
                    + ",<br>Click on the link to reset your password:"
                    + "<a href=\"http://localhost:8080/MeteoCal/reset.xhtml?key=" + user.getVerificationkey()
                    + "\">Reset</a>");

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Checks the two password, if the fields are correct changes user's
     * password
     */
    public void passwordReset(String password, String confpassword,String key) {

        Query query;

        try {

            utx.begin();
            query = em.createQuery("select u from User u where u.verificationkey=:vk");
            query.setParameter("vk", key);

            user = (User) query.getResultList().get(0);
            user.setPassword(password);
            user.setVerificationkey(null);

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
