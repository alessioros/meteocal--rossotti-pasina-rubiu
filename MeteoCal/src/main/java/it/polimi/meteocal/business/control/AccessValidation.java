/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.beans.SendEmailBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import it.polimi.meteocal.business.control.PasswordEncrypter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 *
 * @author alessiorossotti
 */
public class AccessValidation {
    
    private String password;
    private String confpassword;
    private String username;
    private String message;
    private String email;
    
     @EJB
    private SendEmailBean sm;
    
    @EJB
    private CheckFields cf;
    
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getConfpassword() {
        return this.confpassword;
    }

    public void setConfpassword(String confpassword) {
        this.confpassword = confpassword;
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
    
    public void passwordRecovery(){
        
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        
        try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/meteocaldb", "root", "root");
                String sql = "select username from user where email='" + email + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    username=rs.getString(1);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        
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
        
        PreparedStatement ps = null;
        Connection con = null;
        int result;
        
        if (!cf.checkPassword(password, PasswordEncrypter.encryptPassword(confpassword))) {
            message = "Passwords don't match.";
        } else if (confpassword.length() < 6) {
            message = "Password should be at least 6 characters";
        } else {
        
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/meteocaldb", "root", "root");
                String sql = "update user set Password='" + password + "' where username='" + username + "'";
                ps = con.prepareStatement(sql);
                result = ps.executeUpdate();

                message="Password has been reset!";
            }
            catch (Exception e) {
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
    
}
