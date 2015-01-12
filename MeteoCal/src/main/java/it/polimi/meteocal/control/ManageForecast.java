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
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author teo
 */
@ManagedBean
@RequestScoped
public class ManageForecast {

    private URL weatherurl;
    private String urlPrefix;
    private String urlSuffix;
    private String urlQuery;

    public void updateForecast() throws Exception {
        
        urlPrefix = "https://query.yahooapis.com/v1/public/yql?q=";
        urlSuffix = "&format=json&diagnostics=true&callback=";

        // Query da eseguire su Yahoo Weather
        urlQuery = "select * from weather.forecast where woeid=2502265";

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

        // Costruisco il JSON
        JSONObject json = new JSONObject(myJson.toString());

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

    }

}
