/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.business.meteocal.boundary;

import it.polimi.meteocal.business.control.ManagePersonalData;
import it.polimi.meteocal.business.entity.User;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author alessiorossotti
 */
@Named
@RequestScoped
public class PersonalProfile {
    
    @EJB
    private ManagePersonalData mpd;
    
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
    
    public void showPersonalData(){
        
        user=mpd.getData();
    }
    
}
