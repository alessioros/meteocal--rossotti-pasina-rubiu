/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

import it.polimi.meteocal.entity.Forecast;
import it.polimi.meteocal.entity.Location;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.faces.bean.ViewScoped;
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
@ViewScoped
public class ManageForecast {

    private String urlQuery;
    private String woeid;
    private String cityHome;
    private String tempHome;
    private String condHome;
    private List<Location> place;
    private Forecast forecast;

    @EJB
    YahooQueries yq;

    @PersistenceContext
    EntityManager em;
    
    @Schedule(hour = "*/1", minute = "0", second = "0", persistent = false)
    public void updateForecast() {
        try {
            
            place = em.createQuery("select l from Location l").getResultList();
            for (Location location : place) {
                
                woeid=yq.woeidOfLocation(location.getAddress()+", "+location.getCity()+", "+location.getState());

                // Query da eseguire su Yahoo Weather
                urlQuery = "select * from weather.forecast where woeid="+woeid;

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
                    forecast=new Forecast();
                    forecast.setGeneral(fc.getString("text"));
                    forecast.setMaxTemp(fc.getString("high"));
                    forecast.setMinTemp(fc.getString("low"));
                    forecast.setIdLocation(location);
                    em.persist(forecast);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void forecast(Location location) {
        try {
                
                woeid=yq.woeidOfLocation(location.getAddress()+", "
                        +location.getCity()+", "
                        +location.getState());

                // Query da eseguire su Yahoo Weather
                urlQuery = "select * from weather.forecast where woeid="+woeid;

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
                    forecast=new Forecast();
                    forecast.setGeneral(fc.getString("text"));
                    forecast.setMaxTemp(fc.getString("high"));
                    forecast.setMinTemp(fc.getString("low"));
                    forecast.setIdLocation(location);
                    System.out.println(forecast.getGeneral()+forecast.getIdLocation());
                    em.persist(forecast);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

<<<<<<< HEAD
    public void weatherHome() {
        
        try {

            cityHome = "New York";

            woeid=woeidOfLocation(cityHome);

            urlQuery = "select * from weather.forecast where u=\"c\" and woeid=" + woeid;

            // Costruisco il JSON
            JSONObject jsonWeather = yq.yahooRestQuery(urlQuery);

            //Estraggo la temperatura dal JSON
            JSONObject jsonQueryWeather = jsonWeather.getJSONObject("query");
            JSONObject queryRes = jsonQueryWeather.getJSONObject("results");
            JSONObject channel = queryRes.getJSONObject("channel");
            JSONObject resItem = channel.getJSONObject("item");
            JSONObject condition = resItem.getJSONObject("condition");

            tempHome = condition.getString("temp");
            condHome = condition.getString("text");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ----- Getters and setters -----
    
=======
>>>>>>> 24646f87a85c3f0540042df741676d674582d302
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
