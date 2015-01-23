package it.polimi.meteocal.control;

import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.Location;
import it.polimi.meteocal.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Simone
 */
//@ManagedBean
//@RequestScoped
@Stateless
public class ManageEvent {

    @PersistenceContext
    EntityManager em;

    //   @Inject
    //  UserTransaction utx;
    @Inject
    RegisterValidation rv;

    @Inject
    ManageForecast mf;

    public void createEvent(Event event, Location loc) {

        Collection<Event> eventi;
        Collection<User> participants;
        //em.persist(loc);
        event.setIdLocation(loc);
        event.setIdOrganizer(rv.getLoggedUser());
                       
        em.persist(event);
        
        participants=new ArrayList<>();//event.getUserCollection();
        participants.add(rv.getLoggedUser());
        event.setUserCollection(participants);
        
        eventi = rv.getLoggedUser().getEventCollection();
        eventi.add(event);        
        rv.getLoggedUser().setEventCollection(eventi);
        
        mf.forecast(loc);
        em.flush();
        //em.merge(event);
        em.refresh(em.merge(event));
        
    }

    public void updateEvent(Event updated, Location l) {
        try {
            Event event = em.find(Event.class, updated.getIdEvent());

            //event = (Event)em.createQuery("SELECT e FROM Event e WHERE e.idEvent=:id").setParameter("id", updated.getIdEvent()).getResultList().get(0);
            em.persist(l);
            event.setName(updated.getName());
            event.setDescription(updated.getDescription());
            event.setStartTime(updated.getStartTime());
            event.setEndTime(updated.getEndTime());
            event.setOutDoor(updated.getOutDoor());
            event.setPublicEvent(updated.getPublicEvent());
            event.setIdLocation(l);
            //*/
            // event =em.merge(updated);
            em.persist(event);
            em.flush();
            //em.merge(event);
            em.refresh(em.merge(event));

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public Event findEvent(int id) {
        try {
            List<Event> event;
            event = em.createQuery("Select e from Event e where e.idEvent=:ID").setParameter("ID", id).getResultList();
            return event.get(0);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public void deleteEvent(int id) {
        try {
            Event event = em.find(Event.class, id);

            //event = (Event) em.createQuery("SELECT e FROM Event e WHERE e.idEvent=:id").setParameter("id", id).getResultList().get(0);                  
            ;
            em.remove(em.merge(event));
            //em.flush();             

        } catch (Exception e) {

            e.printStackTrace();
            /*try {
             utx.rollback();
             } catch (IllegalStateException | SecurityException | SystemException exception) {
             }*/

        }//*/
    }

    public void updateForecast() {
    }

    public void sendNotification() {
    }

}
