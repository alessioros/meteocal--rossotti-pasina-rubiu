/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.User;
import it.polimi.meteocal.business.beans.SendEmailBean;
import java.security.Principal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author teo
 */
@Stateless
public class RegisterValidation {
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    Principal principal;
    
    @EJB
    private SendEmailBean sm;

    public void createUser(User user) {
        user.setGroupname("USERS");
        em.persist(user);
        
       try{ 
           
           sm.generateAndSendEmail(user.getEmail(),"Confirm MeteoCal Account","Click on the link to confirm your account");
           
       }catch(AddressException e){
            e.printStackTrace();
       }catch(MessagingException e){
            e.printStackTrace();
       }
       
    }

    public void unregister() {
        em.remove(getLoggedUser());
    }
    
     public User getLoggedUser() {
        return em.find(User.class, principal.getName());
    }
    
}
