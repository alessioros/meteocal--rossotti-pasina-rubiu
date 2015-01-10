/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.business.meteocal.boundary;

import it.polimi.meteocal.business.control.Day;
import it.polimi.meteocal.business.control.ManageCalendar;
import it.polimi.meteocal.business.control.Week;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Simone
 */
@Named
@RequestScoped
public class CalendarBean{    
    private Date date;
    private List<Week> cal = new ArrayList();    
    private int actual=13;
    
    @Inject 
    ManageCalendar mc;
    
    public CalendarBean() {                            
        this.getSessionDate();
        if(date == null){
            date=new Date();
            date.setMinutes(((int)(date.getMinutes()/15)+1)*15);
            date.setDate(1);
            this.setSessionDate();
        }       
    }
    private void getSessionDate(){
        HttpSession session;
        FacesContext context = FacesContext.getCurrentInstance();    
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        session = request.getSession();        
        date=(Date)session.getAttribute("cal");
    }  
    private void setSessionDate(){
        HttpSession session;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        session = request.getSession();
        session.setAttribute("cal", date);                  
    }
    public Date getDate() {       
        if(date == null){
            date=new Date();
            date.setDate(1);
            date.setMinutes(((int)(date.getMinutes()/15)+1)*15);
            this.setSessionDate();
        }
        return date;
    }   
    public void today(){        
        date=new Date();  
        date.setMinutes(((int)(date.getMinutes()/15)+1)*15);
        this.setSessionDate();        
    }
    public String getDayNum(){
        return new SimpleDateFormat("d",Locale.US).format(this.getDate());
    } 
    public String getDayStr(){
        return new SimpleDateFormat("E",Locale.US).format(this.getDate());
    }
    public String getMonthStr(){
        return new SimpleDateFormat("MMMM",Locale.US).format(this.getDate());
    }
    public String getMonthNum(){
        return new SimpleDateFormat("M",Locale.US).format(this.getDate());
    }
    public String getYear(){
        return new SimpleDateFormat("y",Locale.US).format(this.getDate());
    }
    public String getHour(){
        return new SimpleDateFormat("H",Locale.US).format(this.getDate());
    }
    public String getMinute(){
        return new SimpleDateFormat("m",Locale.US).format(this.getDate());
    }
    public List<Week> getCal() {
        return cal;
    }
    public void setCal(List<Week> cal) {
        this.cal = cal;
    }
    public void next(){
        date.setDate(1);
        date.setMonth(this.getDate().getMonth()+1);
    }
    public void prev(){
        date.setDate(1);
        date.setMonth(this.getDate().getMonth()-1);      
    }
   
    public String weekDay(){ 
        if(date.getDay()==0&&date.getMonth()==actual)
            return "dayNumbHoly";
        else
            if(date.getDay()==0&&date.getMonth()!=actual)
                return "dayNumbOldHoly";
            else
                if(date.getMonth()==actual&&date.getDay()!=0)        
                    return "dayNumbWeek";
                else
                    return "dayNumbOld";
         }   
    public String eventClass(){
        if(mc.scheduleEvent(date))
            return "event";
        else
            return "empty";
    }
    public String eventOutcome(){        
        if(mc.scheduleEvent(date))
            return "today_events?date="+date.getTime();
        else
            return "createEvent?date="+date.getTime();
    }
    public String eventOutcome(String str){ 
        if(str.equalsIgnoreCase("today")){
            Date tmp = new Date();
            return "createEvent?date="+tmp.getTime();
        }
        else
            return "createEvent";
    }

    
    
    public void createCal(){
        //trovo il primo giorno da visualizzare                         
        actual=date.getMonth();
        date.setDate(1);
        if(date.getDay()==1)
            date.setDate(date.getDate()-7);
        else
            date.setDate(date.getDate()-((date.getDay()+6)%7));  
        
        for(int j=0;j<6;j++){   
            Week w = new Week();
            for(int i =1;i<8;i++){
                Day d = new Day(date,this.eventOutcome(),this.eventClass(),this.weekDay());
                w.getWeek().add(d);
                date.setDate(date.getDate()+1);                 
            }
            cal.add(w);
        }
        date.setMonth(date.getMonth()-1);
        date.setDate(1);
        
            
        
    }
}
