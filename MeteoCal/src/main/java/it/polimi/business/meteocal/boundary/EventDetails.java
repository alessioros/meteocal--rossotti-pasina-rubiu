package it.polimi.business.meteocal.boundary;

import it.polimi.meteocal.business.control.CheckFields;
import it.polimi.meteocal.business.control.ManageEvent;
import it.polimi.meteocal.business.control.RegisterValidation;
import it.polimi.meteocal.business.entity.Event;
import it.polimi.meteocal.business.entity.Location;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class EventDetails {
    
    @EJB
    private ManageEvent me;
    
    @EJB
    private CheckFields cf;
    
    @EJB
    private RegisterValidation rv;

    private Event event;
    private Location loc;
    private String message;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy   HH:mm");
    private Date startDateStr;
    private Date endDateStr;
    private String lat;
    private String lon;

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
    
    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
    
    public Date getStartDateStr() {
        return this.startDateStr;
    }

    public void setStartDateStr(Date startDateStr) {
         this.startDateStr = startDateStr;
    }
    
    public Date getEndDateStr() {
        return this.endDateStr;
    }

    public void setEndDateStr(Date startDateStr) {
        this.endDateStr = startDateStr;
    }
    
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Location getLoc() {
        if (loc == null) {
            loc = new Location();
        }
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }
    
    public Event getEvent() {
        if (event == null) {
            event = new Event();
        }
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    
    public Date dateConverter(String input){
        
        Date date=null;
        try {
            date = formatter.parse(input);
        } catch (ParseException ex) {
            Logger.getLogger(EventDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return date;
    }
    
    public String create() {		
        
        event.setIdOrganizer(rv.getLoggedUser());
        event.setStartTime(startDateStr);
	event.setEndTime(endDateStr);
        loc.setLatitude(Float.parseFloat(lat));
        loc.setLongitude(Float.parseFloat(lon));
        
        if(!cf.checkDateTimes(event.getStartTime(), event.getEndTime())){
        
            message="Error! Start time must be after End Time";
            return "";
        }
        else if(!cf.checkCoordinates(loc.getLatitude(),loc.getLongitude())){
            
            message="Error! Invalid coordinates";
            return "";
        }
        else{
            
            me.createEvent(event,loc);
        
            message="Event Created!";

            return "/loggeduser/invitePeople.xhtml?faces-redirect=true&event="+event.getIdEvent();
            
        }
        
    }
    
}
