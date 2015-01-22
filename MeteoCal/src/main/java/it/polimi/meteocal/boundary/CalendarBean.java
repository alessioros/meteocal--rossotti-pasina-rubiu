package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.Day;
import it.polimi.meteocal.control.ManageCalendar;
import it.polimi.meteocal.control.ManagePersonalData;
import it.polimi.meteocal.control.RegisterValidation;
import it.polimi.meteocal.control.Week;
import it.polimi.meteocal.control.YahooQueries;
import it.polimi.meteocal.entity.Event;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

@ManagedBean
@ViewScoped
public class CalendarBean{    
    private Date date;
    private List<Week> cal = new ArrayList();    
    private int actual=13;
    
    private long viewDate;
    private List<Event> event = new ArrayList();
    
    private String cityHome;
    private String urlQuery;
    private String woeid;
    private String tempHome;
    private String codeHome;
    private String condHome;
    
    private String calusername=null;
    
    private String message;
    
    @Inject 
    ManageCalendar mc;
    
    @Inject 
    ManagePersonalData mpd;
    
    @Inject 
    RegisterValidation rv;
    
    @EJB
    YahooQueries yq;
    
    
    
    public CalendarBean() {                            
        this.getSessionDate();
        if(date == null){
            date=new Date();            
            date.setDate(1);
            this.setSessionDate();
        }       
    }
    
    public void today(){        
        date=new Date();  
        this.setSessionDate();        
    }
    
    public void weatherCal() {
        try {

            cityHome = "Milano";

            woeid=yq.woeidOfLocation(cityHome);

            urlQuery = "select * from weather.forecast where u=\"c\" and woeid=" + woeid;

            JSONObject jsonWeather = yq.yahooRestQuery(urlQuery);

            JSONObject jsonQueryWeather = jsonWeather.getJSONObject("query");
            JSONObject queryRes = jsonQueryWeather.getJSONObject("results");
            JSONObject channel = queryRes.getJSONObject("channel");
            JSONObject resItem = channel.getJSONObject("item");
            JSONObject condition = resItem.getJSONObject("condition");

            tempHome = condition.getString("temp");
            codeHome = condition.getString("code");
            condHome = condition.getString("text");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void next(){
        date.setDate(1);
        date.setMonth(this.getDate().getMonth()+1);
    }
    public void prev(){
        date.setDate(1);
        date.setMonth(this.getDate().getMonth()-1);      
    }
   
    public String weekDay(){ 
        if(date.getDay()==0&&date.getMonth()==actual)
            return "dayNumbHoly";
        else
            if(date.getDay()==0&&date.getMonth()!=actual)
                return "dayNumbOldHoly";
            else
                if(date.getMonth()==actual&&date.getDay()!=0)        
                    return "dayNumbWeek";
                else
                    return "dayNumbOld";
         }   
    public String eventClass(){

        if(mc.scheduleEvent(date,rv.getLoggedUser()))
            return "event";
        else
            return "empty";
    }   
    public String eventOutcome(){
        if(mc.scheduleEvent(date,rv.getLoggedUser()))
            return "today_events?date="+date.getTime();
        else
            return "createEvent?date="+date.getTime();
    }    
    public String eventOutcome(String str){ 
        if(str.equalsIgnoreCase("today")){
            Date tmp = new Date();
            return "createEvent?date="+tmp.getTime();
        }
        else
            return "createEvent";
    }    
    public void createCal(){
        //trovo il primo giorno da visualizzare                         
        actual=date.getMonth();
        date.setDate(1);
        if(date.getDay()==1)
            date.setDate(date.getDate()-7);
        else
            date.setDate(date.getDate()-((date.getDay()+6)%7));  
        
        for(int j=0;j<6;j++){   
            Week w = new Week();
            for(int i =1;i<8;i++){
                Day d = new Day(date,this.eventOutcome(),this.eventClass(),this.weekDay());
                w.getWeek().add(d);
                date.setDate(date.getDate()+1);                 
            }
            cal.add(w);
        }
        date.setMonth(date.getMonth()-1);
        date.setDate(1);   
    }
    
    public void dayEvents(){
        
        this.event = mc.dayEvent(new Date(this.viewDate),calusername);
    }
    
    public void createUserCal(){
        
        actual=date.getMonth();
        date.setDate(1);
        if(date.getDay()==1)
            date.setDate(date.getDate()-7);
        else
            date.setDate(date.getDate()-((date.getDay()+6)%7));  
        
        for(int j=0;j<6;j++){   
            Week w = new Week();
            for(int i =1;i<8;i++){
                Day d = new Day(date,this.eventUserOutcome(calusername),this.eventUserClass(calusername),this.weekDay());
                w.getWeek().add(d);
                date.setDate(date.getDate()+1);                 
            }
            cal.add(w);
        }
        date.setMonth(date.getMonth()-1);
        date.setDate(1);
    }    
    public String eventUserClass(String username){

        if(mc.scheduleEvent(date,mpd.getUserData(username)))
            return "event";
        else
            return "";
    }    
    public String eventUserOutcome(String username){
        if(mc.scheduleEvent(date,mpd.getUserData(username)))

            return "today_events?username="+username+"&date="+date.getTime();
        else
            return "";
    }    
    
    public String submitImportCalendar(){

    message=mc.importCalendar();
    
    return "calendar?faces-redirect=true";
  }
    public String submitExportCalendar() throws IOException, URISyntaxException{

    message=mc.exportCalendar();
    
    return "calendar?faces-redirect=true";
  }
    
    public boolean canView(Event event){
       // this.dayEvents();
        return event.getUserCollection().contains(rv.getLoggedUser());
    }
    
    // ----- Getters and setters -----

    public String getCondHome() {
        return condHome;
    }

    public void setCondHome(String condHome) {
        this.condHome = condHome;
    }
    
    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  
    private void getSessionDate(){
        HttpSession session;
        FacesContext context = FacesContext.getCurrentInstance();    
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        session = request.getSession();        
        date=(Date)session.getAttribute("cal");
    }  
    private void setSessionDate(){
        HttpSession session;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        session = request.getSession();
        session.setAttribute("cal", date);                  
    }
    public Date getDate() {       
        if(date == null){
            date=new Date();
            date.setDate(1);
            this.setSessionDate();
        }
        return date;
    }   
    public String getCityHome() {
        return cityHome;
    }

    public void setCityHome(String cityHome) {
        this.cityHome = cityHome;
    }

    public String getUrlQuery() {
        return urlQuery;
    }

    public void setUrlQuery(String urlQuery) {
        this.urlQuery = urlQuery;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getTempHome() {
        return tempHome;
    }

    public void setTempHome(String tempHome) {
        this.tempHome = tempHome;
    }

    public String getCodeHome() {
        return codeHome;
    }

    public void setCodeHome(String codeHome) {
        this.codeHome = codeHome;
    }
    
    public String getDayNum(){
        return new SimpleDateFormat("d",Locale.US).format(this.getDate());
    } 
    public String getDayStr(){
        return new SimpleDateFormat("E",Locale.US).format(this.getDate());
    }
    public String getMonthStr(){
        return new SimpleDateFormat("MMMM",Locale.US).format(this.getDate());
    }
    public String getMonthNum(){
        return new SimpleDateFormat("M",Locale.US).format(this.getDate());
    }
    public String getYear(){
        return new SimpleDateFormat("y",Locale.US).format(this.getDate());
    }
    public String getHour(){
        return new SimpleDateFormat("H",Locale.US).format(this.getDate());
    }
    public String getMinute(){
        return new SimpleDateFormat("m",Locale.US).format(this.getDate());
    }
    public List<Week> getCal() {
        return cal;
    }
    public void setCal(List<Week> cal) {
        this.cal = cal;
    }

    public long getViewDate() {
        return viewDate;
    }
    public void setViewDate(long viewDate) {
        this.viewDate = viewDate;
    }
    public List<Event> getEvent() {
        return event;
    }
    public void setEvent(List<Event> event) {
        this.event = event;
    }    
    
    public String getCalusername() {
        return calusername;
    }

    public void setCalusername(String calusername) {
        this.calusername = calusername;
    }
    
}
