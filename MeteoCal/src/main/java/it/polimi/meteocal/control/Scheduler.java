package it.polimi.meteocal.control;


import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.mail.MessagingException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author teo
 */
@Startup
@Singleton
public class Scheduler {
    
    @Inject
    ManageForecast mf;
    
    @Schedule(second="0",minute="0",hour="*/12",persistent=false)
    public void prova() throws MessagingException{
        mf.updateForecast();
        mf.notifyBadConditions();
    }
}
