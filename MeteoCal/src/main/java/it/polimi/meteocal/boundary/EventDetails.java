package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.CheckFields;
import it.polimi.meteocal.control.ManageEvent;
import it.polimi.meteocal.control.ManageNotifications;
import it.polimi.meteocal.control.RegisterValidation;
import it.polimi.meteocal.control.YahooQueries;
import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.Forecast;
import it.polimi.meteocal.entity.Location;
import it.polimi.meteocal.entity.User;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import org.json.JSONObject;

@ManagedBean
@ViewScoped
public class EventDetails {

    @EJB
    YahooQueries yq;

    @Inject
    private ManageEvent me;

    @EJB
    private CheckFields cf;

    @Inject
    private RegisterValidation rv;
       
    private Event event;
    private Location loc = new Location();
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
    private String minTemp;
    private String maxTemp;
    private String condEvent;
    private Boolean availableData;

    private int idEvent = 0;

    public void dateConverter() {
        this.startDate = new Date(this.startDateLong);
        this.startDate.setMinutes((((int) (this.startDate.getMinutes() / 15) + 1) * 15));
        this.endDate = new Date(this.endDateLong + 3600000);
        this.endDate.setMinutes((((int) (this.endDate.getMinutes() / 15) + 1) * 15));
        //this.startDate.setTime(this.startDateLong);
        //this.endDate.setTime(this.endDateLong);               
    }

    public String create() {

        event.setIdOrganizer(rv.getLoggedUser());
        event.setStartTime(startDate);
        event.setEndTime(endDate);

        query = "select * from geo.placefinder where text=\"" + address + "," + city + "," + state + "\"";
        try {
            JSONObject json = yq.yahooRestQuery(query);

            JSONObject jsonQuery = json.getJSONObject("query");
            JSONObject queryResults = jsonQuery.getJSONObject("results");
            JSONObject pl = queryResults.getJSONObject("Result");

            loc.setLatitude(Float.parseFloat(pl.getString("latitude")));
            loc.setLongitude(Float.parseFloat(pl.getString("longitude")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (!cf.checkSize(event.getName(), 45)) {
            message = "Error! Name is too long (max 45 character)";
            return "";
        } else if (!cf.checkSize(event.getDescription(), 250)) {
            message = "Error! Description is too long (max 250 character)";
            return "";
        } else if (!cf.checkSize(state, 45)) {
            message = "Error! State is too long (max 45 character)";
            return "";
        } else if (!cf.checkSize(city,45)) {
            message = "Error! City is too long (max 45 character)";
            return "";
        } else if (!cf.checkSize(address, 45)) {
            message = "Error! Address is too long (max 45 character)";
            return "";
        } else if (!cf.checkDateTimes(event.getStartTime(), event.getEndTime())) {

            message = "Error! Start time must be after End Time";
            return "";
        } else if (!cf.checkAfterToday(event.getStartTime())) {

            message = "Error! Start time must be after today";
            return "";

        } else if (!cf.checkOtherEvent(event.getStartTime(), event.getEndTime(), rv.getLoggedUser())) {
            message = "Error! You have an other event at this time";
            return "";
        } else if (!cf.checkCoordinates(loc.getLatitude(), loc.getLongitude())) {

            message = "Error! Invalid coordinates";
            return "";
        } else {
            loc.setAddress(address);
            loc.setCity(city);
            loc.setState(state);
            me.createEvent(event, loc);

            message = "Event Created!";

            return "/loggeduser/eventDetails.xhtml?faces-redirect=true&id=" + event.getIdEvent();

        }

    }

    public void eventWeather() {

        List<Forecast> forecasts;
        this.availableData=true;
        forecasts = (List) event.getIdLocation().getForecastCollection();

        if (!forecasts.isEmpty()) {
            minTemp = forecasts.get(0).getMinTemp();
            maxTemp = forecasts.get(0).getMaxTemp();
            condEvent = forecasts.get(0).getGeneral();
            
        } else {
            this.availableData=false;
            condEvent = "Data not available yet";
        }
    }

    public String buttonValue() {
        if (editing()) {
            return "EDIT";
        } else {
            return "CREATE";
        }
    }

    public String buttonAction(){
        if (editing()) {
            return this.updateEvent();
        } else {
            return this.create();
        }
    }

    public String updateEvent() {
        event.setIdEvent(this.idEvent);
        event.setStartTime(startDate);
        event.setEndTime(endDate);
        query = "select * from geo.placefinder where text=\"" + address + "," + city + "," + state + "\"";
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

            me.updateEvent(event, loc);

            message = "Event Updated!";
                      
            return "/loggeduser/eventDetails?faces-redirect=true&id=" + event.getIdEvent();

        }

    }

    public String deleteEvent() {
        this.findEvent();
        me.deleteEvent(this.idEvent);

        return "calendar.xhtml?faces-redirect=true";
    }

    public void findEvent() {
        if (this.idEvent != 0) {
            event = me.findEvent(this.idEvent);
            this.startDate = event.getStartTime();
            this.endDate = event.getEndTime();
            this.address = event.getIdLocation().getAddress();
            this.city = event.getIdLocation().getCity();
            this.state = event.getIdLocation().getState();

        }

    }

    public boolean editing() {
        return this.idEvent != 0;
    }

    public boolean canEdit() {
        User u = rv.getLoggedUser();
        this.findEvent();
        return u.equals(event.getIdOrganizer());

    }

    public Date today() {
        return new Date();
    }

    // ----- Getters and setters -----

    public Boolean getAvailableData() {
        return availableData;
    }

    public void setAvailableData(Boolean availableData) {
        this.availableData = availableData;
    }
    
    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getCondEvent() {
        return condEvent;
    }

    public void setCondEvent(String condEvent) {
        this.condEvent = condEvent;
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

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

}
