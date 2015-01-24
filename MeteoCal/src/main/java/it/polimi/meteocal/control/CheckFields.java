/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

import it.polimi.meteocal.entity.Event;
import java.util.Date;
import it.polimi.meteocal.entity.User;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author teo
 */
@Stateless
public class CheckFields {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * 
     * @param Username
     * @return true if there is no username like the one passed in the DB
     */
    public boolean checkUsername(String Username){
            Query query = em.createQuery("SELECT u FROM User u WHERE u.username=:Username");
            query.setParameter("Username", Username);
            List<User> tmplist = query.getResultList();
            return tmplist.isEmpty()==true;
    }
    
    /**
     * 
     * @param Email
     * @return true if there is no email like the one passed in the DB
     */
    public boolean checkEmail(String Email){
            Query query = em.createQuery("SELECT u FROM User u WHERE u.email=:Email");
            query.setParameter("Email", Email);
            List<User> tmplist = query.getResultList();
            return tmplist.isEmpty()==true;
    }
    public boolean checkEmailCorrectness(String Email){
             return Email.matches("^.*@.*\\..*$");
    }
    
    /**
     * 
     * @param password1
     * @param password2
     * @return true if the passwords match
     */
    public boolean checkPassword(String password1,String password2){
            return password1.equals(password2);
    }
    
    public boolean checkCoordinates(float lat, float lon){
    
        if(lat>=-85 && lat <=85 && lon>=-180 && lon <=180){
            return true;
        }
        else return false;
    }
    
    public boolean checkDateTimes(Date start, Date end){
        
        return  start.compareTo(end)<0;
    }
    
    public boolean checkAfterToday(Date start){
        
        Date date = new Date();
        
        return start.compareTo(date)>0;
    }
   
    public boolean checkOtherEvent(Date startTime, Date endTime, User user) {
      
      for (Event e : user.getEventCollection() )
          if(   (startTime.getTime()<=e.getStartTime().getTime()&& endTime.getTime()>e.getStartTime().getTime()) || //se l'evento inizia prima e finisce dopo l'inizio di un altro evento
                 (startTime.getTime()>=e.getStartTime().getTime() && startTime.getTime()<=e.getEndTime().getTime()) ) //se l'evento inizia durante un altro evento
              return false;
      return true;
    }
    public boolean checkSize(String str, int l){
        return str.length()<=l;
}
    
}
