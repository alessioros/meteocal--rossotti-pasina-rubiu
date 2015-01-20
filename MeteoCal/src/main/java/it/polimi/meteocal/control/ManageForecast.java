/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.Forecast;
import it.polimi.meteocal.entity.Location;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author teo
 */
@Singleton
public class ManageForecast {

    private String urlQuery;
    private String woeid;
    private String cityHome;
    private String tempHome;
    private String condHome;
    private Location place;
    private Forecast forecast;
    private List<Event> events;
    private List<Forecast> oldForecasts=new ArrayList();
    private List<Forecast> newForecasts=new ArrayList();

    @EJB
    YahooQueries yq;

    @PersistenceContext
    EntityManager em;

    @Schedule(hour = "*/1", minute = "0", second = "0", persistent = false)
    public void updateForecast() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Date date = new Date();
            events = em.createQuery("select e from Event e where e.outDoor=:true").getResultList();
            for (Event event : events) {
                if (event.getStartTime().after(date)) {

                    place = event.getIdLocation();

                    oldForecasts = (List) place.getForecastCollection();
                    Collections.sort(oldForecasts, new Comparator<Forecast>() {

                        @Override
                        public int compare(Forecast arg0, Forecast arg1) {

                            return arg0.getIdForecast() - arg1.getIdForecast();
                        }
                    });
                    

                    woeid = yq.woeidOfLocation(place.getAddress() + ", " + place.getCity() + ", " + place.getState());

                    // Query da eseguire su Yahoo Weather
                    urlQuery = "select * from weather.forecast where woeid=" + woeid;

                    // Costruisco il JSON
                    JSONObject json = yq.yahooRestQuery(urlQuery);

                    // stampo le previsioni per tutti i giorni:
                    JSONObject jsonQuery = json.getJSONObject("query");
                    JSONObject queryResults = jsonQuery.getJSONObject("results");
                    JSONObject channel = queryResults.getJSONObject("channel");
                    JSONObject resItem = channel.getJSONObject("item");
                    JSONArray forecasts = resItem.getJSONArray("forecast");
                    for (int i = 0; i < forecasts.length(); i++) {

                        JSONObject fc = forecasts.getJSONObject(i);
                        forecast = new Forecast();
                        forecast.setGeneral(fc.getString("text"));
                        forecast.setMaxTemp(fc.getString("high"));
                        forecast.setMinTemp(fc.getString("low"));
                        forecast.setIdLocation(place);
                        newForecasts.add(forecast);
                        em.persist(forecast);
                    }
                    
                    //checks if the weather has changed
                    for(Forecast forecast: oldForecasts){
                        if(!forecast.getGeneral().equals(newForecasts.get(0).getGeneral())){
                        
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void forecast(Location location) {
        try {

            woeid = yq.woeidOfLocation(location.getAddress() + ", "
                    + location.getCity() + ", "
                    + location.getState());

            // Query da eseguire su Yahoo Weather
            urlQuery = "select * from weather.forecast where woeid=" + woeid;

            // Costruisco il JSON
            JSONObject json = yq.yahooRestQuery(urlQuery);

            // stampo le previsioni per tutti i giorni:
            JSONObject jsonQuery = json.getJSONObject("query");
            JSONObject queryResults = jsonQuery.getJSONObject("results");
            JSONObject channel = queryResults.getJSONObject("channel");
            JSONObject resItem = channel.getJSONObject("item");
            JSONArray forecasts = resItem.getJSONArray("forecast");
            for (int i = 0; i < forecasts.length(); i++) {

                JSONObject fc = forecasts.getJSONObject(i);
                forecast = new Forecast();
                forecast.setGeneral(fc.getString("text"));
                forecast.setMaxTemp(fc.getString("high"));
                forecast.setMinTemp(fc.getString("low"));
                forecast.setIdLocation(location);
                System.out.println(forecast.getGeneral() + forecast.getIdLocation());
                em.persist(forecast);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // ----- Getters and setters -----
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

    public String getCityHome() {
        return cityHome;
    }

    public void setCityHome(String cityHome) {
        this.cityHome = cityHome;
    }

    public String getTempHome() {
        return tempHome;
    }

    public void setTempHome(String tempHome) {
        this.tempHome = tempHome;
    }

    public String getCondHome() {
        return condHome;
    }

    public void setCondHome(String condHome) {
        this.condHome = condHome;
    }

}
