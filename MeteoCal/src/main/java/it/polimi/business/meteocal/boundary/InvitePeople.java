/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.business.meteocal.boundary;

import it.polimi.meteocal.business.control.ManageInvites;
import it.polimi.meteocal.business.control.RegisterValidation;
import it.polimi.meteocal.business.entity.Event;
import it.polimi.meteocal.business.entity.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author teo
 */
@ManagedBean
@ViewScoped
public class InvitePeople {
    
    private User user;
    private List<User> contacts;
    private Event event;
    
    @Inject
    RegisterValidation rv;
    
    @Inject
    ManageInvites mi;
    
    private Map<Integer, Boolean> selectedIds = new HashMap<>();
    private List<User> invited;

    // Actions -----------------------------------------------------------------------------------

    public void invite() {
        
        contacts=updateContacts();

        invited= new ArrayList<>();
        for (User contact : contacts) {

            if (selectedIds.get(contact.getIdUser())) {
                invited.add(contact);
                selectedIds.remove(contact.getIdUser()); 
            }
        }
        mi.createInvites(invited, event);
        
    }
    
    public List<User> updateContacts() {

        user = rv.getLoggedUser();
        contacts=(List<User>) user.getUserCollection();
        return contacts;   
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }
    
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<User> getSelectedDataList() {
        return invited;
    }

    public void setSelectedDataList(List<User> selectedDataList) {
        this.invited = selectedDataList;
    }


}
