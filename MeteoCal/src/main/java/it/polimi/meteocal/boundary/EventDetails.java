package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.CheckFields;
import it.polimi.meteocal.control.ManageEvent;
import it.polimi.meteocal.control.RegisterValidation;
import it.polimi.meteocal.control.YahooQueries;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.Location;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.json.JSONObject;

@Named
@RequestScoped
public class EventDetails {

    @EJB
    YahooQueries yq;

    @EJB
    private ManageEvent me;

    @EJB
    private CheckFields cf;

    @EJB
    private RegisterValidation rv;

    private Event event;
    private Location loc=new Location();
    private String message;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy   HH:mm");
    private long startDateLong;
    private long endDateLong;
    private Date startDate;
    private Date endDate;
    private String state;
    private String city;
    private String address;
    private String query;

    public void dateConverter() {
        this.startDate = new Date(this.startDateLong);
        this.endDate = new Date(this.endDateLong + 3600000);
        //this.startDate.setTime(this.startDateLong);
        //this.endDate.setTime(this.endDateLong);               
    }

    public String create() {

        event.setIdOrganizer(rv.getLoggedUser());
        event.setStartTime(startDate);
        event.setEndTime(endDate);

        query = "select * from geo.placefinder where text=\"" + address + "," + city + "," + state+"\"";
        try {
            JSONObject json = yq.yahooRestQuery(query);

            // Ci faccio quel che devo.
            // Ad esempio, stampo le previsioni per tutti i giorni:
            JSONObject jsonQuery = json.getJSONObject("query");
            JSONObject queryResults = jsonQuery.getJSONObject("results");
            JSONObject pl = queryResults.getJSONObject("Result");
            System.out.println(Float.parseFloat(pl.getString("latitude")));
            loc.setLatitude(Float.parseFloat(pl.getString("latitude")));
            loc.setLongitude(Float.parseFloat(pl.getString("longitude")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        loc.setAddress(address);
        loc.setCity(city);
        loc.setState(state);

        if (!cf.checkDateTimes(event.getStartTime(), event.getEndTime())) {

            message = "Error! Start time must be after End Time";
            return "";
        } else if (!cf.checkCoordinates(loc.getLatitude(), loc.getLongitude())) {

            message = "Error! Invalid coordinates";
            return "";
        } else {

            me.createEvent(event, loc);

            message = "Event Created!";

            return "/loggeduser/invitePeople.xhtml?faces-redirect=true&event=" + event.getIdEvent();

        }

    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getStartDateLong() {
        return this.startDateLong;
    }

    public void setStartDateLong(long startDateLong) {
        this.startDateLong = startDateLong;
    }

    public long getEndDateLong() {
        return this.endDateLong;
    }

    public void setEndDateLong(long startDateLong) {
        this.endDateLong = startDateLong;
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
    

}
