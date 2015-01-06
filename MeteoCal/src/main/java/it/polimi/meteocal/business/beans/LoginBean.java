/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.beans;
import it.polimi.meteocal.business.entity.User;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author teo
 */
@Named
@RequestScoped        
public class LoginBean {
    
   // @Inject
   //  private Logger logger;
    
    private String password;
    private String username;
    private String message;
    
    @PersistenceContext
    EntityManager em;

    public LoginBean() {
    }
    
     public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        
        Query query;
        try {
            
            query = em.createQuery("select u from User u where u.username=:un and u.verified like :v");
            query.setParameter("un", username);
            query.setParameter("v", true);
            List check=query.getResultList();
            
            if (check.isEmpty()) {
                
                message="User not verified!";
                return "";
            
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.username, this.password);
            return "/loggeduser/calendar.xhtml?faces-redirect=true";
        } catch (ServletException e) {
            
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Login Failed","Login Failed"));
            //logger.log(Level.SEVERE,"Login Failed");
            message="Login Failed!";
            return "";
        } 
  
    }
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().invalidate();
        //logger.log(Level.INFO, "User Logged out");
        return "/index?faces-redirect=true";
    }
}
