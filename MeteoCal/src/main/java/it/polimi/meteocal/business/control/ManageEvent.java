package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.Event;
import it.polimi.meteocal.business.entity.Location;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ManageEvent {
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    RegisterValidation rv;
    
  
    public void createEvent(Event event,Location loc) {
        em.persist(loc);
        event.setIdLocation(loc);
        event.setIdOrganizer(rv.getLoggedUser());
        em.persist(event);
    }
    
    public void deleteEvent(){
    }
    
    public void updateEvent(){
    }
    
    public void updateForecast(){
    }
    
    public void sendNotification(){
    }
    
    
}
