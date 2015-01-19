package it.polimi.meteocal.control;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Simone
 */
@Named
@RequestScoped
public class Week {
    private List<Day> week = new ArrayList();

    public List<Day> getWeek() {
        return week;
    }

    public void setWeek(List<Day> week) {
        this.week = week;
    }
    
    
}
