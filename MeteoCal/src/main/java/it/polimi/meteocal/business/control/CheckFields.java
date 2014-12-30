/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author teo
 */
@Stateless
public class CheckFields {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * 
     * @param Username
     * @return true if there is no username like the one passed in the DB
     */
    public boolean checkUsername(String Username){
            Query query = em.createQuery("SELECT u FROM User u WHERE u.username=:Username");
            query.setParameter("Username", Username);
            List<User> tmplist = query.getResultList();
            return tmplist.isEmpty()==true;
    }
    
    /**
     * 
     * @param Email
     * @return true if there is no email like the one passed in the DB
     */
    public boolean checkEmail(String Email){
            Query query = em.createQuery("SELECT u FROM User u WHERE u.email=:Email");
            query.setParameter("Email", Email);
            List<User> tmplist = query.getResultList();
            return tmplist.isEmpty()==true;
    }
    public boolean checkEmailCorrectness(String Email){
             return Email.matches("^.*@.*\\..*$");
    }
    
    /**
     * 
     * @param password1
     * @param password2
     * @return true if the passwords match
     */
    public boolean checkPassword(String password1,String password2){
            return password1.equals(password2);
    }
    
}
