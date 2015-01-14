/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author teo
 */
@ManagedBean
@ViewScoped
public class ManageForecast {

    private URL weatherurl;
    private String urlPrefix = "https://query.yahooapis.com/v1/public/yql?q=";
    private String urlSuffix = "&format=json&diagnostics=true&callback=";
    private String urlQuery;
    private String woeid;
    private String location;
    private String cityHome;
    private String tempHome;
    
    @PersistenceContext
    EntityManager em;

    //  @PostConstruct
    //  @Schedule(hour = "*/1", minute = "0", second = "0", persistent = false)
  /*  public void updateForecast() {
     try {
     urlPrefix = "https://query.yahooapis.com/v1/public/yql?q=";
     urlSuffix = "&format=json&diagnostics=true&callback=";
     // Query da eseguire su Yahoo Weather
     urlQuery = "select * from weather.forecast where woeid=2502265";

     // Costruisco il JSON
     JSONObject json = yahooRestQuery(urlQuery);

     // Ci faccio quel che devo.
     // Ad esempio, stampo le previsioni per tutti i giorni:
     JSONObject jsonQuery = json.getJSONObject("query");
     JSONObject queryResults = jsonQuery.getJSONObject("results");
     JSONObject channel = queryResults.getJSONObject("channel");
     JSONObject resItem = channel.getJSONObject("item");
     JSONArray forecasts = resItem.getJSONArray("forecast");
     for (int i = 0; i < forecasts.length(); i++) {
     JSONObject fc = forecasts.getJSONObject(i);

     System.out.println("Data: " + fc.getString("date"));
     System.out.println("Previsione: " + fc.getString("text"));
     System.out.println("High: " + fc.getString("high"));
     System.out.println("Low: " + fc.getString("low"));
     System.out.println();
     }
     } catch (Exception e) {
     e.printStackTrace();
     }

     }*/
    public void getWoeidOfLocation() {
        try {

            // Query da eseguire su Yahoo Weather
            urlQuery = "select * from geo.placefinder where text=\"" + location + "\"";

            // Costruisco il JSON
            JSONObject json = yahooRestQuery(urlQuery);

            // Ci faccio quel che devo.
            // Ad esempio, stampo le previsioni per tutti i giorni:
            JSONObject jsonQuery = json.getJSONObject("query");
            JSONObject queryResults = jsonQuery.getJSONObject("results");
            JSONObject pl = queryResults.getJSONObject("Result");

            woeid = pl.getString("woeid");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getForecast() {
        try {
            getWoeidOfLocation();

            urlQuery = "select * from weather.forecast where woeid=" + woeid;

            // Costruisco il JSON
            JSONObject jsonWeather = yahooRestQuery(urlQuery);

  
            JSONObject jsonQueryWeather = jsonWeather.getJSONObject("query");
            JSONObject queryRes = jsonQueryWeather.getJSONObject("results");
            JSONObject channel = queryRes.getJSONObject("channel");
            JSONObject resItem = channel.getJSONObject("item");
            JSONArray forecasts = resItem.getJSONArray("forecast");
            for (int i = 0; i < forecasts.length(); i++) {
                JSONObject fc = forecasts.getJSONObject(i);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void weatherHome() {
        try {

            cityHome = "Milano";

            location = cityHome;

            getWoeidOfLocation();

            urlQuery = "select * from weather.forecast where u=\"c\" and woeid=" + woeid;

            // Costruisco il JSON
            JSONObject jsonWeather = yahooRestQuery(urlQuery);
            
            //Estraggo la temperatura dal JSON
            JSONObject jsonQueryWeather = jsonWeather.getJSONObject("query");
            JSONObject queryRes = jsonQueryWeather.getJSONObject("results");
            JSONObject channel = queryRes.getJSONObject("channel");
            JSONObject resItem = channel.getJSONObject("item");
            JSONObject condition = resItem.getJSONObject("condition");

            tempHome = condition.getString("temp");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Receives a query, it asks yahooWeather service whit the query. It receives
     * the data and rebuilds the JSON, it returns this.
     * @param urlQuery
     * @return well formed json object with query answer
     */
    public JSONObject yahooRestQuery(String urlQuery) {

        try {

            // Costruisco l'URL (avendo l'accortezza di fare l'encoding della query)    
            weatherurl = new URL(urlPrefix + URLEncoder.encode(urlQuery, "UTF-8") + urlSuffix);

            // Ottengo la connessione al servizio
            URLConnection con = weatherurl.openConnection();

            // Preparo lo StringBuilder che accoglierà il JSON
            StringBuilder myJson = new StringBuilder();

            // Eseguo la request e leggo il risultato
            InputStream is = con.getInputStream();

            // Essendo che ricevo un JSON (quindi una stringa) posso usare in Reader
            InputStreamReader reader = new InputStreamReader(is);
            try (BufferedReader br = new BufferedReader(reader)) {
                String line = null;

                // Leggo finchè ci sono dati da leggere
                while ((line = br.readLine()) != null) {
                    myJson.append(line);
                }

                // Ho finito, chiudo la connessione
                br.close();
            }

            JSONObject json = new JSONObject(myJson.toString());
            return json;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

}
