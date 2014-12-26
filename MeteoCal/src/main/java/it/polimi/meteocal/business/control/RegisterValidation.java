/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.User;
import java.security.Principal;
import javax.ejb.Stateless;
import javax.inject.Inject;
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

    public void createUser(User user) {
        user.setGroupname("USERS");
        em.persist(user);
    }

    public void unregister() {
        em.remove(getLoggedUser());
    }
    
     public User getLoggedUser() {
        return em.find(User.class, principal.getName());
    }
    
}
