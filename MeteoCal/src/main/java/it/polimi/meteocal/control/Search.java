package it.polimi.meteocal.control;

import javax.ejb.EJB;

/**
 *
 * @author alessiorossotti
 */
public class Search {
    
    @EJB
    ManagePersonalData mpd;
    
    private String username;
    
    /**
     * searches in the system for inserted username and display results
     */
    public String searchUser(){
        
        return "/loggeduser/search_results.xhtml?faces-redirect=true";
    }
    
    /**
     * Calls ManagePersonalData method add user, which adds
     * the username to user's addresslist if exists
     * @param username
     */
    public void addUser(String username){
        
        mpd.addUser(username);
    }
    
}
