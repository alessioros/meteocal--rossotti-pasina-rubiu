package it.polimi.meteocal.control;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Simone
 */
@Named
@RequestScoped
public class Day {
    private Date date;
    private String eventOutcome;
    private String eventClass;
    private String weekDay;

    public Day(Date date, String eventOutcome, String eventClass, String weekDay) {
        this.date = (Date)date.clone();
        this.eventOutcome = eventOutcome;
        this.eventClass = eventClass;
        this.weekDay = weekDay;
    }
    public Date getDate() {
        return (Date)date.clone();
    }

    public void setDate(Date date) {
        this.date =(Date) date.clone();
    }

    public String getEventOutcome() {
        return eventOutcome;
    }

    public void setEventOutcome(String eventOutcome) {
        this.eventOutcome = eventOutcome;
    }

    public String getEventClass() {
        return eventClass;
    }

    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }
    
    public int dayNum(){
        return date.getDate();
    }
}
