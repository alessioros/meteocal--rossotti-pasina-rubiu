package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.CheckFields;
import it.polimi.meteocal.control.ManagePersonalData;
import it.polimi.meteocal.control.PasswordEncrypter;
import it.polimi.meteocal.control.RegisterValidation;
import it.polimi.meteocal.entity.User;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
@RequestScoped
public class PersonalProfile {

    @PersistenceContext
    EntityManager em;

    @Inject
    private ManagePersonalData mpd;

    @Inject
    RegisterValidation rv;

    @EJB
    private CheckFields cf;

    private User user;
    private String contact;
    private String message;
    private String confPassword;
    private List<User> contacts;
    
    /**
     * Returns current logged user
     * @return User
     */
    public User showPersonalData() {
        return user = rv.getLoggedUser();
    }
    
    /**
     * submits the data changes for user
     */
    public void submitChangeData() {

        if (!cf.checkUsername(user.getUsername())) {
            message = "The username you have chosen already exists.";
        } else if (!cf.checkEmail(user.getEmail())) {
            message = "The email you have chosen already exists.";
        } else if (!cf.checkEmailCorrectness(user.getEmail())) {
            message = "Please insert a valid email address.";
        } else if (!cf.checkPassword(user.getPassword(), PasswordEncrypter.encryptPassword(confPassword))) {
            message = "Passwords don't match.";
        } else if (confPassword.length() < 6) {
            message = "Password should be at least 6 characters";
        } else {
            user.setIdUser(rv.getLoggedUser().getIdUser());
            mpd.changeData(user);

            this.message = "Data succesfully updated!";
        }

    }
    
    /**
     * submits add for user seen in details
     */
    public void submitAddSeenUser() {
        String username = user.getUsername();
        user = rv.getLoggedUser();
        if (username.equals(user.getUsername())) {
            message = "You can't add yourself!";
        }else if(mpd.userInContacts(user.getUsername(), username)){
            message = "User already added!";
        }else {
            mpd.addUser(username);
            message = "User added!";
            user.setUsername(username);
        }
    }

    /**  
     * If user has a private calendar an error message is displayed,
     * If it's public instead is submitted showCalendar
     */
    public void submitShowCalendar() {

        showUserData();
        if (user.getPublicCalendar()) {
            mpd.showCalendar(user.getUsername());
        } else {
            message = "This user has a private calendar";
        }
    }

    /**
     * If the username is valid, add user is called
     */
    public void submitAddUser() {
        user = rv.getLoggedUser();
        if (user.getUsername().equals(contact)) {
            message = "You can't add yourself!";
        }else if(!mpd.existUser(contact)){
            message = "User "+contact+" doesn't exist!";
        }else if(mpd.userInContacts(user.getUsername(), contact)){
            message = "User already added!";
        }
        else{
            mpd.addUser(contact);
            message = "User added!";
        }
        contacts = updateContacts();
    }
    
    /**
     * submits delete user with username
     * @param username
     */
    public void submitDeleteUser(String username) {
        
        System.out.println("The username you want to delete is: "+username);
        //mpd.deleteUser(username);

        contacts = updateContacts();
    }

    /*  Gets user's data, based on the username in user
     *   in user_info.xhtml user.username is setted from URL
     */
    public void showUserData() {

        user = mpd.getUserData(user.getUsername());
    }

    /**
     * Gets contact collection, makes the cast to list and puts it in contacts
     *
     * @return
     */
    public List<User> updateContacts() {

        user = rv.getLoggedUser();
        contacts = (List<User>) user.getUserCollection();
        return contacts;
    }

    // ----- Getters and setters -----
    
    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }

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

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {

        if (this.user == null) {
            this.user = new User();
        }
        return user;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {

        return contact;
    }
}
