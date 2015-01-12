package it.polimi.meteocal.control;

import it.polimi.meteocal.entity.User;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alessiorossotti
 */
@ManagedBean
@ViewScoped
public class Search {

    private String input;
    private String output;
    private List<User> results;
    private String username;

    @PersistenceContext
    EntityManager em;

    @Inject
    ManagePersonalData mpd;

    /**
     * searches in the system for inserted username and display results
     *
     * @return page of results
     */
    public String searchUser() {
        return "/loggeduser/searchresults.xhtml?faces-redirect=true&searchinput="+input;
    }

    public void searchResults() {
        results = em.createQuery("select u from User u where u.username=:U or u.name=:U or u.surname=:U").setParameter("U", input).getResultList();
    }

    /**
     * Calls ManagePersonalData method add user, which adds the username to
     * user's addresslist if exists
     *
     * @param username
     */
    public void addUser(String username) {

        mpd.addUser(username);
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public List<User> getResults() {
        return results;
    }

    public void setResults(List<User> results) {
        this.results = results;
    }

}
