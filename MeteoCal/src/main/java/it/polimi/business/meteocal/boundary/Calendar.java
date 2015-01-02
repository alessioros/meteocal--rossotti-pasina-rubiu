/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.business.meteocal.boundary;

import java.util.Date;
import java.text.SimpleDateFormat;

import it.polimi.meteocal.business.control.ManageCalendar;
import it.polimi.meteocal.business.entity.User;
import it.polimi.meteocal.business.entity.Event;
import java.security.Principal;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Simone
 */
@Named
@RequestScoped
public class Calendar{
    
    @EJB
    private ManageCalendar mc;
    
    private User user;
    private Date date;
    private boolean set =false;
    private int actual=13;
    
    public Calendar() {
        Principal principal;
        HttpSession session;
        FacesContext context = FacesContext.getCurrentInstance();
        principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        session = request.getSession(); 
        session.setAttribute("id", principal.getName());
        date=this.getSessionDate();
        if(date == null)
        {
            date=new Date();
            date.setDate(1);
            session.setAttribute("cal", date);
        }
        //this.today();
        
    }
    private Date getSessionDate(){
        Principal principal;
        HttpSession session;
        FacesContext context = FacesContext.getCurrentInstance();
        principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        session = request.getSession();        
        date=(Date)session.getAttribute("cal");
        return date;
    }  
    public Date getDate() {
        date=this.getSessionDate();
        if(date == null){
            date=new Date();
            date.setDate(1);
        }
        return date;
    }   
    public void today(){
        date=this.getSessionDate();
        date=new Date();
    }
    public String getDayNum(){
        date=this.getSessionDate();
        return new SimpleDateFormat("d",Locale.US).format(this.getDate());
    } 
    public String getDayStr(){
        date=this.getSessionDate();
        return new SimpleDateFormat("E",Locale.US).format(this.getDate());
    }
    public String getMonthStr(){
        date=this.getSessionDate();
        return new SimpleDateFormat("MMMM",Locale.US).format(this.getDate());
    }
    public String getMonthNum(){
        date=this.getSessionDate();
        return new SimpleDateFormat("M",Locale.US).format(this.getDate());
    }
    public String getYear(){
        date=this.getSessionDate();
        return new SimpleDateFormat("y",Locale.US).format(this.getDate());
    }
    public String getHour(){
        date=this.getSessionDate();
        return new SimpleDateFormat("H",Locale.US).format(this.getDate());
    }
    public String getMinute(){
        date=this.getSessionDate();
        return new SimpleDateFormat("m",Locale.US).format(this.getDate());
    }
    public void next(){
        date=this.getSessionDate();
            date.setDate(1);
            date.setMonth(this.getDate().getMonth()+1);
            set = false;
    }
    public void prev(){
        date=this.getSessionDate();
        date.setDate(1);
        date.setMonth(this.getDate().getMonth()-1);
        set = false;
    }
    public String displayNumDay(int pos){
        date=this.getSessionDate();
         String result="";
         if(!set)
            if(pos==1){
                actual=date.getMonth();
                date.setDate(1);
                if(date.getDay()==1)
                    date.setDate(date.getDate()-7);
                else
                    date.setDate(date.getDate()-((date.getDay()+6)%7));                  
            }
            if(date.getDay()==pos%7){
                result=this.getDayNum();                    
                if(pos<42)
                    date.setDate(date.getDate()+1); 
                else{
                    date.setMonth(date.getMonth()-1);
                    date.setDate(1);
                    set=true;
                }
            }                
         return result;   
     }   
    public String displayColDay(int pos){ 
        if(pos%7==0&&date.getMonth()==actual)
            return "dayNumbHoly";
        else
            if(pos%7==0&&date.getMonth()!=actual)
                return "dayNumbOldHoly";
        else
            if(date.getMonth()==actual&&pos%7!=0)        
                return "dayNumbWeek";
        else
            //if(date.getMonth()!=actual&&pos%7!=0)  
                return "dayNumbOld";
     }   
    public void setUser(User user){
        this.user=user;
    }    
    public User getUser(){
    
        if(this.user==null){
            this.user=new User();
        }
        return user;
    }
    
}
