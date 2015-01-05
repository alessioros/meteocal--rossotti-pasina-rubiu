/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.Event;
import it.polimi.meteocal.business.entity.Invitation;
import it.polimi.meteocal.business.entity.Notification;
import it.polimi.meteocal.business.entity.User;
import it.polimi.meteocal.business.entity.Usernotification;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author teo
 */
@Stateless
public class ManageInvites {
    
    @Resource
    UserTransaction utx;
    
    private Notification notificationInvite;
    private Invitation invitation;
    private Usernotification userwithnotifications;
    
    public void createInvites(List<User> invited, Event event){
        try{
            
            utx.begin();
            
            notificationInvite.setDescription("You have been invited to the event "+ event.getName());
            notificationInvite.setIdEvent(event);
            
            invitation.setIdNotification(notificationInvite);
            invitation.setAccepted(Boolean.FALSE);
            
            for(User invite: invited){
                userwithnotifications.setNotification(notificationInvite);
                userwithnotifications.setUser(invite);
                userwithnotifications.setPending(Boolean.TRUE);
            }
            
            utx.commit();
        }catch(Exception e){
            e.printStackTrace();
            try{utx.rollback();}catch(IllegalStateException | SecurityException | SystemException exception){}
        }
    }
    
}