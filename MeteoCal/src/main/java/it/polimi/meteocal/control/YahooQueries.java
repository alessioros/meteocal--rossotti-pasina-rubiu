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
import javax.ejb.Stateless;
import org.json.JSONObject;

/**
 *
 * @author teo
 */
@Stateless
public class YahooQueries {
    
    private URL weatherurl;
    private String urlPrefix = "https://query.yahooapis.com/v1/public/yql?q=";
    private String urlSuffix = "&format=json&diagnostics=true&callback=";
    
    
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
}
