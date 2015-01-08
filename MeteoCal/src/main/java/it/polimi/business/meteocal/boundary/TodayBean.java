/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.business.meteocal.boundary;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author roobew
 */
@Named
@RequestScoped
public class TodayBean {
    private Date date;

    public Date getDate() {
        return date;
    }

    public void prova(){
        System.out.println(date);
    }
    public void setDate(Date date) {
        this.date = date;
    }
    
}
