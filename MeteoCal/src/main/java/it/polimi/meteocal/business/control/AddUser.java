/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.User;
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
public class AddUser {
    
    @PersistenceContext
    EntityManager em;
    
    @Resource
    UserTransaction utx;

    @Inject
    RegisterValidation rv;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    private User user;
    
    /**
     * Aggiunge nella tabella contact un record con l'id dell'utente loggato e l'id
     * dell'utente di cui si è passato l'username
     * @param username 
     */
    public void addUser(String username) {
        try {

            List<User> checkexist = em.createQuery("select u from User u where u.username=:um").setParameter("um", username).getResultList();
            
            if (!checkexist.isEmpty()) {
                utx.begin();
                user = rv.getLoggedUser();
                user.getUserCollection().add(checkexist.get(0));
                utx.commit();
            }
            
        } catch (Exception e) {
            
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException exception) {
            }
            
        }
    }

}
