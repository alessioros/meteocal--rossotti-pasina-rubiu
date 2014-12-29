/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alessiorossotti
 */
@Stateless
public class ManagePersonalData {
    
    @PersistenceContext
    EntityManager em;
    
    private User user;
    
    public void setUser(User user){
        this.user=user;
    }
    
    public User getUser(){
    
        if(this.user==null){
            this.user=new User();
        }
        return user;
    }
    
    public User getData(){
        
        this.user=new User();
        user.setUsername("#provausername");
        user.setName("alessio");
        user.setSurname("provaros");
        user.setEmail("alessiorossotti@gmail.com");
        
        return this.user;
    }
    
    public void changeData(){
    
    
    }
    
}
