package it.polimi.business.meteocal.boundary;

import it.polimi.meteocal.business.control.ManageEvent;
import it.polimi.meteocal.business.entity.Event;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class EventDetails {
    
    @EJB
    private ManageEvent me;

    private Event event;
    
     public EventDetails() {
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
        me.createEvent(event);
        return "/calendar.xhtml?faces-redirect=true";
    }
    
}
