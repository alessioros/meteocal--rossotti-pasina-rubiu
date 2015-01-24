package it.polimi.meteocal.control;

import it.polimi.meteocal.boundary.SendEmailBean;
import it.polimi.meteocal.entity.User;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;



/**
 *
 * @author teo
 */
@Stateless
public class RegisterValidation {
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    Principal principal;
    
    @EJB
    SendEmailBean sm;
    
    /**
     * Persist the user that want to register and sends an email
     * for register confirmation with a generated key
     * Calendar is set public by default
     * @param user
     */
    public void createUser(User user) {

        user.setGroupname("USERS");
        user.setPublicCalendar(Boolean.TRUE);
        user.setVerified(Boolean.FALSE);
        user.setVerificationkey(UUID.randomUUID().toString());
        em.persist(user);
        
       try{ 
           
           sm.generateAndSendEmail(user.getEmail(),
                   "Confirm MeteoCal Account",
                   "Welcome to MeteoCal "+user.getName()
                    +",<br>Click on the link to confirm your account:"
                    +"<a href=\"http://localhost:8080/MeteoCal/activation.xhtml?key="+user.getVerificationkey()
                    +"\">Confirm</a>");
           
       }catch(AddressException e){
            e.printStackTrace();
       }catch(MessagingException e){
            e.printStackTrace();
       }
       
    }

    /**
     * Returns the currently logged user
     * @return loggedUser
     */
    public User getLoggedUser() {
         Query query = em.createQuery("SELECT u FROM User u WHERE u.username=:USERNAME");
        query.setParameter("USERNAME", principal.getName());
        List<User> user = query.getResultList();
        return user.get(0);
    }
    
}
