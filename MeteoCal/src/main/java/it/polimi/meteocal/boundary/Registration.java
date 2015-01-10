package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.CheckFields;
import it.polimi.meteocal.control.PasswordEncrypter;
import it.polimi.meteocal.control.RegisterValidation;
import it.polimi.meteocal.entity.User;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Registration {

    @EJB
    private RegisterValidation rv;
    @EJB
    private CheckFields cf;

    private User user;
    private String message;
    private String confpassword;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getConfpassword() {
        return confpassword;
    }

    public void setConfpassword(String confpassword) {
        this.confpassword = confpassword;
    }


    /**
     * checks the registrations fields and if they are OK creates the user
     */
    public void register() {
        if (!cf.checkUsername(user.getUsername())) {
            message = "The username you have chosen already exists.";
        } else if (!cf.checkEmail(user.getEmail())) {
            message = "The email you have chosen already exists.";
        } else if (!cf.checkEmailCorrectness(user.getEmail())) {
            message = "Please insert a valid email address.";
        } else if (!cf.checkPassword(user.getPassword(), PasswordEncrypter.encryptPassword(confpassword))) {
            message = "Passwords don't match.";
        } else if (confpassword.length() < 6) {
            message = "Password should be at least 6 characters";
        } else {
            rv.createUser(user);
            message = "Confirmation email sent, please check your email";
        }
    }
    
}
