package it.polimi.meteocal.control;

import it.polimi.meteocal.boundary.CalendarBean;
import it.polimi.meteocal.entity.User;
import it.polimi.meteocal.entity.Event;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.security.Principal;
import java.util.ArrayList;


import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

@ManagedBean
@RequestScoped
public class ManageCalendar {
    @PersistenceContext
    EntityManager em;
    
    @Inject
    Principal principal;
    
    @Inject
    RegisterValidation rv;
    
    @Resource
    UserTransaction utx;
    
    private User user;
    
    public boolean scheduleEvent(Date date,User user){
        
        try {
            Iterator<Event> cal;            
           
            this.user=user;
            cal = user.getEventCollection().iterator();           
            
            while(cal.hasNext())
            {
                Event e = cal.next();
                if( e.getStartTime().getYear()   <=  date.getYear() &&
                    e.getStartTime().getMonth()  <=  date.getMonth() &&
                    e.getStartTime().getDate()   <=  date.getDate() &&
                    e.getEndTime().getYear()   >=  date.getYear() &&
                    e.getEndTime().getMonth()  >=  date.getMonth() &&
                    e.getEndTime().getDate()   >=  date.getDate())
                            return true;                    
            }
            return false;                                    
        } catch (Exception e) {
            
            e.printStackTrace();
            
            
        }
        return false;
    }
    
    public List<Event> dayEvent(Date date){
        


        try {
            Iterator<Event> event; 
            List<Event> list = new ArrayList();
           


            this.user = rv.getLoggedUser();            


            event = user.getEventCollection().iterator();           
            
            while(event.hasNext())
            {
                Event e = event.next();
                if( e.getStartTime().getYear()   <=  date.getYear() &&
                    e.getStartTime().getMonth()  <=  date.getMonth() &&
                    e.getStartTime().getDate()   <=  date.getDate() &&
                    e.getEndTime().getYear()   >=  date.getYear() &&
                    e.getEndTime().getMonth()  >=  date.getMonth() &&
                    e.getEndTime().getDate()   >=  date.getDate())
                            list.add(e);
            }
            return list;                                    
        } catch (Exception e) {
            
            e.printStackTrace();                        
        }
        return null;
    }
    
    public String importCalendar() {

            return "Calendar has been imported!";
    }

    public String exportCalendar() throws IOException {
        
        user = rv.getLoggedUser();
        Iterator<Event> events;
        Event event;
        DateTime start,end;
        GregorianCalendar startDate,endDate;
        String fileName="calendar.ics";
        // Create a TimeZone
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone("Europe/Amsterdam");
        VTimeZone tz = timezone.getVTimeZone();

        UidGenerator ug = null;
        Uid uid;
        try {
            ug = new UidGenerator("uidGen");
        } catch (SocketException ex) {
            Logger.getLogger(ManageCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }


        VEvent calevent;
        events = user.getEventCollection().iterator();

        if (!events.hasNext()) {
            return "Your calendar is empty";
        } else {

            // Create the file and the calendar

            Calendar calendar = new Calendar();
            calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
            calendar.getProperties().add(Version.VERSION_2_0);
            calendar.getProperties().add(CalScale.GREGORIAN);

            while(events.hasNext()){

                event = events.next();

                // Start Date
                startDate = new GregorianCalendar();
                startDate.setTimeZone(timezone);
                startDate.set(java.util.Calendar.MONTH, event.getStartTime().getMonth());
                startDate.set(java.util.Calendar.DAY_OF_MONTH, event.getStartTime().getDay());
                startDate.set(java.util.Calendar.YEAR, event.getStartTime().getYear());
                startDate.set(java.util.Calendar.HOUR_OF_DAY, event.getStartTime().getHours());
                startDate.set(java.util.Calendar.MINUTE, event.getStartTime().getMinutes());
                startDate.set(java.util.Calendar.SECOND, event.getStartTime().getSeconds());

                 // End Date
                endDate = new GregorianCalendar();
                endDate.setTimeZone(timezone);
                endDate.set(java.util.Calendar.MONTH, event.getEndTime().getMonth());
                endDate.set(java.util.Calendar.DAY_OF_MONTH, event.getEndTime().getDay());
                endDate.set(java.util.Calendar.YEAR, event.getEndTime().getYear());
                endDate.set(java.util.Calendar.HOUR_OF_DAY, event.getEndTime().getHours());
                endDate.set(java.util.Calendar.MINUTE, event.getEndTime().getMinutes());
                endDate.set(java.util.Calendar.SECOND, event.getEndTime().getSeconds());

                start = new DateTime(startDate.getTime());
                end = new DateTime(endDate.getTime());
                calevent = new VEvent(start, end, event.getName());

                // Add the event to calendar
                calendar.getComponents().add(calevent);

                //aggiungo ID
                uid =  ug.generateUid();
                calevent.getProperties().add(uid);
            }
            
            /*----------- DOWLOAD ---------------*/
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            ec.responseReset();
            ec.setResponseContentType("application/calendar+xml"); 
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); 

            OutputStream output = ec.getResponseOutputStream();
            
            fc.responseComplete();
    
            CalendarOutputter outputter = new CalendarOutputter();
            try {
                outputter.output(calendar, output);
                
            } catch (net.fortuna.ical4j.model.ValidationException ex) {
                Logger.getLogger(CalendarBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CalendarBean.class.getName()).log(Level.SEVERE, null, ex);

            }
            
            return "Calendar has been exported!";
        }

    }
    
    // ----- Getters and setters -----
    
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
