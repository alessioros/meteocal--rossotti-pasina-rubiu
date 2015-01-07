/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.User;
import java.security.Principal;
import java.sql.*;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author alessiorossotti
 */
@ManagedBean
@RequestScoped
public class ManagePersonalData {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    @EJB
    private RegisterValidation rv;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {

        if (this.user == null) {
            this.user = new User();
        }
        return user;
    }

    public void changeData(User updated) {

        try {

            utx.begin();
            user = rv.getLoggedUser();
            if (!"".equals(updated.getName())) {
                user.setName(updated.getName());
            }
            if (!"".equals(updated.getSurname())) {
                user.setSurname(updated.getSurname());
            }
            if (!"".equals(updated.getPassword())) {
                user.setPassword(updated.getPassword());
            }
            if (!"".equals(updated.getUsername())) {
                user.setUsername(updated.getUsername());
            }
            
            utx.commit();  

        } catch (Exception e) {

            e.printStackTrace();
            try {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException exception) {
            }

        }

    }
}
