/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.control;

import it.polimi.meteocal.entity.Forecast;
import it.polimi.meteocal.entity.Location;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 *
 * @author teo
 */
public class ForecastTest {

    private ManageForecast mf;
    private RegisterValidation rv;
    
    @PersistenceContext
    EntityManager em;
     

    public ForecastTest() {
    }

    @Before
    public void setUp() {
        mf = new ManageForecast();
        mf.em = mock(EntityManager.class);
        mf.yq = mock(YahooQueries.class);
        mf.mn = mock(ManageNotifications.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void forecastCheckWorsenedMeteoTest(){
        
        Location loc=new Location();
        loc.setAddress("via feltre");
        loc.setCity("milano");
        loc.setState("italia");
        Date date=new Date();
        
         Forecast oforecast=new Forecast();
        oforecast.setCode(0);
        oforecast.setGeneral("Tornado");
        oforecast.setMaxTemp("20");
        oforecast.setMinTemp("10");
        oforecast.setIdLocation(loc);
        oforecast.setDate(date);
        
         Forecast nforecast=new Forecast();
        nforecast.setCode(32);
        nforecast.setGeneral("Sunny");
        nforecast.setMaxTemp("20");
        nforecast.setMinTemp("10");
        nforecast.setIdLocation(loc);
        nforecast.setDate(date);
        
        nforecast.setDate(null);
         
        assertTrue(mf.checkBetterMeteo(oforecast, nforecast));
        assertFalse(mf.checkWorsenedMeteo(oforecast, nforecast));
    }
}
