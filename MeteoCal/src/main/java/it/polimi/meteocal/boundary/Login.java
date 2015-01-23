package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.AccessValidation;
import it.polimi.meteocal.control.CheckFields;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class Login {

   // @Inject
    //  private Logger logger;
    private String password;
    private String confpassword;
    private String username;
    private String message;
    private String email;
    private String key;

    @PersistenceContext
    EntityManager em;

    @Inject
    AccessValidation av;

    @EJB
    private CheckFields cf;

    /**
     * Get username and password and retrieve user, then checks if he/she has
     * verified his/her account
     *
     * @return redirect to user's calendar page
     */
    public String login() {

        Query query;
        try {
            
            query = em.createQuery("select u from User u where u.username=:un");
            query.setParameter("un", username);
            query.setParameter("v", true);
            List check = query.getResultList();

            if (check.isEmpty()) {

                message = "Incorrect username or password";
                return "";

            }
            
            query = em.createQuery("select u from User u where u.username=:un and u.verified like :v");
            query.setParameter("un", username);
            query.setParameter("v", true);
            check = query.getResultList();

            if (check.isEmpty()) {

                message = "User not verified!";
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

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Login Failed"));
            //logger.log(Level.SEVERE,"Login Failed");
            message = "Incorrect username or password";
            return "";
        }

    }

    /**
     * Logs out the user
     *
     * @return redirect to index page
     */
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().invalidate();
        //logger.log(Level.INFO, "User Logged out");
        return "/index?faces-redirect=true";
    }

    /**
     * Submits the recovery of the password
     */
    public void submitPasswordRecovery() {

        if (cf.checkEmail(email)) {
            message = "This email doesn't exist";

            return;
        }
        av.passwordRecovery(email);
        message = "Email sent! , please check your mail";
    }

    /**
     * Submits the new password
     */
    public void submitPasswordReset() {

        if (!cf.checkPassword(password, confpassword)) {
            message = "Passwords don't match";
        } else if (confpassword.length() < 6) {
            message = "Password should be at least 6 characters";
        } else {
            av.passwordReset(password, confpassword,key);
            message = "Password has been reset!";
        }
    }
    
    // ----- Getters and setters -----
    
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

    public String getConfpassword() {
        return this.confpassword;
    }

    public void setConfpassword(String confpassword) {
        this.confpassword = confpassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    

}
