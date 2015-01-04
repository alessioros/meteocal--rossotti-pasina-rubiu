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
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author alessiorossotti
 */
@Stateless
public class ManagePersonalData {

    

    @PersistenceContext
    EntityManager em;

    @Inject
    Principal principal;
    

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

    public User getData() {

        this.user = new User();
        principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        String username = principal.getName();
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/meteocaldb", "root", "root");
            String sql = "select * from user where username='" + username + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                user.setUsername(username);
                user.setName(rs.getString("Name"));
                user.setSurname(rs.getString("Surname"));
                user.setEmail(rs.getString("Email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return this.user;
    }

    public void changeData(User updated) {

        principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        String username = principal.getName();
        updated.setUsername(username);
        String field = "";

        PreparedStatement ps = null;
        Connection con = null;
        int result;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/meteocaldb", "root", "root");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            
            field = updated.getName();
            if (!"".equals(field)) {
                String sql = "update user set Name='" + field + "' where username='" + username + "'";
                ps = con.prepareStatement(sql);
                result = ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            field = updated.getSurname();
            if (!"".equals(field)) {
                String sql = "update user set Surname='" + field + "' where username='" + username + "'";
                ps = con.prepareStatement(sql);
                result = ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            field = updated.getPassword();
            if (!"".equals(field)) {
                String sql = "update user set Password='" + field + "' where username='" + username + "'";
                ps = con.prepareStatement(sql);
                result = ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            field = updated.getUsername();
            if (!"".equals(field)) {
                String sql = "update user set Username='" + field + "' where username='" + username + "'";
                ps = con.prepareStatement(sql);
                result = ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
