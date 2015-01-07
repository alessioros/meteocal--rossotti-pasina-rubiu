/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.beans;

import it.polimi.meteocal.business.entity.User;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author teo
 */
@ManagedBean
@RequestScoped
public class ActivationBean {
    
    @Resource
    UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    @ManagedProperty(value = "#{param.key}")
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @PostConstruct
    public void init(){
        try{
            utx.begin();
            // Get User based on activation key.
            Query query = em.createQuery("SELECT u FROM User u WHERE u.verificationkey=:key");
            query.setParameter("key", key);
            List<User> tmplist = query.getResultList();

            if (tmplist.isEmpty() == false) {
                // Delete activation key from database.
                tmplist.get(0).setVerificationkey(null);
                tmplist.get(0).setVerified(true);
                valid = true;
                utx.commit();
            }
            
        } catch(Exception e){
            e.printStackTrace();
            try{utx.rollback();}catch(IllegalStateException | SecurityException | SystemException exception){}
        }
    }
}
