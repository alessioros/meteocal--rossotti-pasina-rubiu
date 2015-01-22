/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

import it.polimi.meteocal.entity.Event;
import it.polimi.meteocal.entity.Forecast;
import it.polimi.meteocal.entity.Location;
import it.polimi.meteocal.entity.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.ejb.EJB;
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
    private Date date;
    private Forecast goodforecast;
    private String messageGoodDay;
    private List<Event> events;
    private List<Forecast> oldForecasts;
    private List<Forecast> newForecasts;
    private List<Forecast> cancForecasts;
    private List<User> organizer;

    @EJB
    YahooQueries yq;

    @PersistenceContext
    EntityManager em;

    @EJB
    ManageNotifications mn;

    public void updateForecast() {
        try {
            System.out.println("Check the weather");
            date = new Date();
            //takes all the outdoor events
            events = em.createQuery("select e from Event e where e.outDoor=true").getResultList();

            for (Event event : events) {
                System.out.println(event.getName() + "\n");
                //if event starts after the current date
                if (event.getStartTime().after(date)) {

                    place = event.getIdLocation();

                    oldForecasts = new ArrayList();
                    oldForecasts = (List) place.getForecastCollection();

                    woeid = yq.woeidOfLocation(place.getAddress() + ", " + place.getCity() + ", " + place.getState());
                    System.out.println(woeid + "\n");
                    //if place exists
                    if (!woeid.equals("null")) {

                        //deletes old forecasts for that location
                        cancForecasts = new ArrayList();
                        cancForecasts = em.createQuery("select f from Forecast f where f.idLocation=:ID").setParameter("ID", place).getResultList();
                        for (Forecast cancforecast : cancForecasts) {
                            em.remove(cancforecast);
                        }

                        // Query da eseguire su Yahoo Weather
                        urlQuery = "select * from weather.forecast where woeid=" + woeid;

                        // Costruisco il JSON
                        JSONObject json = yq.yahooRestQuery(urlQuery);

                        JSONObject jsonQuery = json.getJSONObject("query");
                        JSONObject queryResults = jsonQuery.getJSONObject("results");
                        JSONObject channel = queryResults.getJSONObject("channel");
                        JSONObject resItem = channel.getJSONObject("item");
                        JSONArray forecasts = resItem.getJSONArray("forecast");
                        for (int i = 0; i < forecasts.length(); i++) {

                            JSONObject fc = forecasts.getJSONObject(i);
                            forecast = new Forecast();
                            DateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                            date = format.parse(fc.getString("date"));

                            forecast.setDate(date);
                            forecast.setCode(Integer.parseInt(fc.getString("code")));
                            forecast.setGeneral(fc.getString("text"));
                            forecast.setMaxTemp(fc.getString("high"));
                            forecast.setMinTemp(fc.getString("low"));
                            forecast.setIdLocation(place);

                            newForecasts = new ArrayList();
                            newForecasts.add(forecast);

                            System.out.println(forecast.getGeneral() + "\n");

                            em.persist(forecast);
                            em.flush();
                            em.merge(forecast);
                            em.refresh(forecast);
                        }

                        //checks if the weather has changed
                        for (Forecast oForecast : oldForecasts) {

                            for (Forecast nForecast : newForecasts) {

                                if (oForecast.getDate().equals(nForecast.getDate())) {

                                    if (checkWorsenedMeteo(oForecast, nForecast)) {

                                        if (nForecast.getCode() == 0) {

                                            System.out.println("OMG A FUCKING TORNADO!!!");

                                        } else {
                                            for (Forecast goodForecast : newForecasts) {
                                                if (goodForecast.getDate().after(nForecast.getDate())) {
                                                    if (goodForecast.getCode() >= 19 && (goodForecast.getCode() < 35 || goodForecast.getCode() == 36)) {
                                                        goodforecast = goodForecast;
                                                        messageGoodDay = ", nearest good day is " + goodforecast.getDate().getDay() + " "
                                                                + goodforecast.getDate().getMonth() + " "
                                                                + goodforecast.getDate().getYear()
                                                                + " that is " + goodforecast.getGeneral();
                                                        break;
                                                    }
                                                }
                                            }
                                            organizer = new ArrayList();
                                            organizer.add(event.getIdOrganizer());
                                            System.out.println(organizer.get(0).getName() + " " + organizer.size());
                                            mn.sendNotifications(organizer,
                                                    event,
                                                    false,
                                                    "New forecasts of your event " + event.getName() + " are bad" + messageGoodDay,
                                                    "Meteo has changed",
                                                    "New forecasts of your event " + event.getName() + " are bad" + messageGoodDay,
                                                    "New forecasts of your event " + event.getName() + " are bad" + messageGoodDay);
                                            return;
                                        }
                                    }
                                }
                            }

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

                DateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.ITALY);
                date = format.parse(fc.getString("date"));

                forecast.setDate(date);
                forecast.setCode(Integer.parseInt(fc.getString("code")));
                forecast.setGeneral(fc.getString("text"));
                forecast.setMaxTemp(fc.getString("high"));
                forecast.setMinTemp(fc.getString("low"));
                forecast.setIdLocation(location);
                System.out.println(forecast.getDate().toString() + forecast.getGeneral() + forecast.getIdLocation());
                em.persist(forecast);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkWorsenedMeteo(Forecast oForecast, Forecast nForecast) {
        /*checks in order: if old forecast and new forecast code are different
         if new forecast has a bad weather code
         if old forecast has a good weather code
         */
        return !Objects.equals(oForecast.getCode(), nForecast.getCode())
                && (nForecast.getCode() < 19
                || (nForecast.getCode() >= 35
                && nForecast.getCode() != 36))
                && (oForecast.getCode() >= 19
                && oForecast.getCode() < 35
                || oForecast.getCode() == 36);
    }

    public boolean checkBetterMeteo(Forecast oForecast, Forecast nForecast) {
        /*checks in order: if old forecast and new forecast code are different
         if old forecast has a bad weather code
         if new forecast has a good weather code
         */
        if (oForecast.getCode() != nForecast.getCode()
                && (oForecast.getCode() < 19
                || (oForecast.getCode() >= 35
                && oForecast.getCode() != 36))
                && (nForecast.getCode() >= 19
                && nForecast.getCode() < 35
                || nForecast.getCode() == 36)) {
            return true;
        }
        return false;
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
