package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.ManageInvites;
import it.polimi.meteocal.control.ManagePersonalData;
import it.polimi.meteocal.control.RegisterValidation;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author teo
 */
@ManagedBean
@ViewScoped
public class InvitePeople {

    private User user;
    private List<User> contacts;

    @PersistenceContext
    EntityManager em;

    @Inject
    ManagePersonalData mpd;

    @Inject
    RegisterValidation rv;

    @Inject
    ManageInvites mi;

    private int event;
    private Event pEvent;
    private String contact;
    private String message;

    private Map<Integer, Boolean> selectedIds = new HashMap<>();
    private List<User> invited;
    private List<User> alreadyInvited;

    /**
     * updates contacts then calls ManageInvites method createInvites
     */
    public String invite() throws MessagingException {

        contacts = updateInvitable();

        invited = new ArrayList<>();
        for (User contact : contacts) {

            if (selectedIds.get(contact.getIdUser())) {
                invited.add(contact);
                selectedIds.remove(contact.getIdUser());
            }
        }
        List<Event> tmplist = em.createQuery("SELECT e FROM Event e WHERE e.idEvent=:ID").setParameter("ID", event).getResultList();
        alreadyInvited = (List) tmplist.get(0).getUserCollection();
        for (User user : alreadyInvited) {
            if (invited.contains(user)) {
                invited.remove(user);
            }
        }
        
        mi.createInvites(invited, tmplist.get(0));
        return "calendar?faces-redirect=true";

    }

    /**
     * calls personalprofile submitAddUser() and updates the contacts
     */
    public void addUser() {
        user = rv.getLoggedUser();
        if (user.getUsername().equals(contact)) {
            message = "You can't add yourself!";
        } else if (!mpd.existUser(contact)) {
            message = "User " + contact + " doesn't exist!";
        } else if (mpd.userInContacts(user.getUsername(), contact)) {
            message = "User already added!";
        } else {
            mpd.addUser(contact);
            message = "User added!";
        }
        contacts = updateContacts();
    }

    /**
     * updates contacts from user's userCollection
     *
     * @return
     */
    public List<User> updateContacts() {

        user = rv.getLoggedUser();
        contacts = (List) user.getUserCollection();
        return contacts;
    }

    /**
     * updates list of contacts deleting the contacts already invited
     * @return list of users that can be invited
     */
    public List<User> updateInvitable() {
        
        contacts=new ArrayList();
        List<Event> tmplist = em.createQuery("SELECT e FROM Event e WHERE e.idEvent=:ID").setParameter("ID", event).getResultList();
        pEvent = tmplist.get(0);

        for (User user : pEvent.getUserCollection()) {
            if (contacts.contains(user)) {
                contacts.remove(user);
            }
        }
        user = rv.getLoggedUser();
        contacts = (List<User>) user.getUserCollection();
        return contacts;
    }

    // ----- Getters and setters -----
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<User> getInvited() {
        return invited;
    }

    public void setInvited(List<User> invited) {
        this.invited = invited;
    }

}
