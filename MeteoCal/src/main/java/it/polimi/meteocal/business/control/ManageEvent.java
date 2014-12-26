package it.polimi.meteocal.business.control;

import it.polimi.meteocal.business.entity.Event;
import java.security.Principal;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ManageEvent {
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    Principal principal;

    public void createEvent(Event event) {
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
