/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.beans.SendEmailBean;
import it.polimi.meteocal.business.entity.User;
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

    private String password;
    private String confpassword;
    private String message;
    private String email;
    private User user;
    private String key;

    @PersistenceContext
    EntityManager em;

    @EJB
    private SendEmailBean sm;

    @EJB
    private CheckFields cf;

    @Resource
    UserTransaction utx;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfpassword() {
        return this.confpassword;
    }

    public void setConfpassword(String confpassword) {
        this.confpassword = confpassword;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void passwordRecovery() {

        Query query;

        if (cf.checkEmail(email)) {
            message = "This email doesn't exist";

            return;
        }
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

        message = "Email sent! , please check your mail";
    }

    public void passwordReset() {

        Query query;

        if (!cf.checkPassword(password, confpassword)) {
            message = "Passwords don't match";
        } else if (confpassword.length() < 6) {
            message = "Password should be at least 6 characters";
        } else {

            try {

                utx.begin();
                query = em.createQuery("select u from User u where u.verificationkey=:vk");
                query.setParameter("vk", key);

                user = (User) query.getResultList().get(0);

                user.setPassword(password);

                utx.commit();
                user.setVerificationkey(null);
                message = "Password has been reset!";

            } catch (Exception e) {

                e.printStackTrace();
                try {
                    utx.rollback();
                } catch (IllegalStateException | SecurityException | SystemException exception) {
                }

            }

        }
    }

}
