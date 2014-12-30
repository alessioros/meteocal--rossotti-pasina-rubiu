package it.polimi.business.meteocal.boundary;

import it.polimi.meteocal.business.control.ManageEvent;
import it.polimi.meteocal.business.entity.Event;
import it.polimi.meteocal.business.entity.Location;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class EventDetails {
    
    @EJB
    private ManageEvent me;

    private Event event;
    private Location loc;
    
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

    public String create() {
        me.createEvent(event,loc);
        return "/calendar.xhtml?faces-redirect=true";
    }
    
}
