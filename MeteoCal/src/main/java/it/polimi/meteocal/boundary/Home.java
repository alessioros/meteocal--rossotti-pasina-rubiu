/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.boundary;

import it.polimi.meteocal.control.YahooQueries;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.json.JSONObject;

/**
 *
 * @author teo
 */
@ManagedBean
@ViewScoped
public class Home {
    
    @EJB
    YahooQueries yq;
    
    private String cityHome;
    private String urlQuery;
    private String woeid;
    private String tempHome;
    private String codeHome;
    
    public void weatherHome() {
        try {

            cityHome = "New York";

            woeid=yq.woeidOfLocation(cityHome);

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
            codeHome = condition.getString("code");

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    
    
}
