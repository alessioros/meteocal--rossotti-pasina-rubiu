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
    private String contact;
    private String message="prova";
    private String confPassword;
    
    public String getConfPassword() {
        return this.confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }
    
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setUser(User user){
        this.user=user;
    }
    
    public User getUser(){
    
        if(this.user==null){
            this.user=new User();
        }
        return user;
    }
    
    public void setContact(String contact){
        this.contact=contact;
    }
    
    public String getContact(){
    
        return contact;
    }
    public void showPersonalData(){
        
        this.mpd=new ManagePersonalData();
        user=mpd.getData();
    }
    
    public String submitChangeData(){
        
        this.mpd=new ManagePersonalData();
        boolean success=false;
        this.message="prova";
        
        if(success){
            this.message="Data succesfully updated!";
        }
        else{
            this.message="Something wrong!";
        }
        
        success=mpd.changeData(user);
        
        return "";
        
    }
    
    public void submitAddUser(){
        
       mpd.addUser(contact);
    }
}
