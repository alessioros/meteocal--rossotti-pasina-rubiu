/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

import it.polimi.meteocal.boundary.SendEmailBean;
import it.polimi.meteocal.entity.Group;
import it.polimi.meteocal.entity.User;
import javax.persistence.EntityManager;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author teo
 */
public class UserManagerTest {
    
    private RegisterValidation rv;
    
    @Before
    public void setUp() {
        rv = new RegisterValidation();
        rv.em = mock(EntityManager.class);
        rv.sm = mock(SendEmailBean.class);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void newUsersShouldBelongToUserGroupAndSavedOnce() {
        User newUser = new User();
        newUser.setName("prova");
        newUser.setEmail("nini@mimi.ci");
        newUser.setPassword("prova");
        newUser.setSurname("surname");
        newUser.setUsername("username");
        rv.createUser(newUser);
        assertThat(newUser.getGroupname(), is(Group.USERS));
        verify(rv.em,times(1)).persist(newUser);
    }
}
