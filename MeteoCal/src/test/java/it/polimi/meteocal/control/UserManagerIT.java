/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

/**
 *
 * @author teo
 */
import it.polimi.meteocal.control.CheckFields;
import it.polimi.meteocal.control.PasswordEncrypter;
import it.polimi.meteocal.control.RegisterValidation;
import it.polimi.meteocal.entity.User;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

/**
 *
 * @author miglie
 */
@RunWith(Arquillian.class)
public class UserManagerIT {

    @EJB
    RegisterValidation rv;

    @EJB
    CheckFields cf;

    @PersistenceContext
    EntityManager em;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(RegisterValidation.class)
                .addPackage(RegisterValidation.class.getPackage())
                .addPackage(User.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void RegisterValidationShouldBeInjected() {
        assertNotNull(rv);
    }

    @Test
    public void EntityManagerShouldBeInjected() {
        assertNotNull(em);
    }

    @Test
    public void emailShouldBeValid() {
        assertFalse(cf.checkEmailCorrectness("invalidemail"));
    }

    @Test
    public void passwordsShouldBeEncryptedOnDB() {
        User newUser = new User();
        String email = "test@test.com";
        String password = "password";
        newUser.setEmail(email);
        newUser.setName("Marco");
        newUser.setPassword(password);
        newUser.setSurname("surname");
        newUser.setUsername("username");
        rv.createUser(newUser);
        User foundUser = em.find(User.class, newUser.getIdUser());
        assertNotNull(foundUser);
        assertThat(foundUser.getPassword(), is(PasswordEncrypter.encryptPassword(password)));
    }

}
