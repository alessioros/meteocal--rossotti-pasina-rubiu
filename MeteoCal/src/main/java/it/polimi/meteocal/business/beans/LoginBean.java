/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.beans;

import it.polimi.meteocal.business.control.CheckFields;
import it.polimi.meteocal.business.control.PasswordEncrypter;
import static it.polimi.meteocal.business.entity.Usernotification_.user;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author teo
 */
@Named
@RequestScoped        
public class LoginBean {
    
   // @Inject
   //  private Logger logger;
    

    private String username;
    private String password;
    private String confpassword;
    private String message;
    private String email;
    
    @EJB
    private SendEmailBean sm;
    
    @EJB
    private CheckFields cf;

    public LoginBean() {
    }
    
     public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    
    public void passwordRecovery(){
        
        try{ 
           
           sm.generateAndSendEmail(email,
                   "Reset Your Password",
                   "Hei, your username is:"+username+
                    ",<br>Click on the link to reset your password:"
                   + "<a href=\"http://localhost:8080/MeteoCal/reset.xhtml?email="+email
                    +"\">Reset</a>");
           
       }catch(AddressException e){
            e.printStackTrace();
       }catch(MessagingException e){
            e.printStackTrace();
       }
        message="Email sent! , please check your mail";
    }
    
    public void passwordReset(){
        
        if (!cf.checkPassword(password, PasswordEncrypter.encryptPassword(confpassword))) {
            message = "Passwords don't match.";
        } else if (confpassword.length() < 6) {
            message = "Password should be at least 6 characters";
        } else {
            message="Password has been reset!";
        }
    
    }
}
