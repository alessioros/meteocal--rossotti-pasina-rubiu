/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Simone
 */
@Entity
@Table(name = "forecast")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Forecast.findAll", query = "SELECT f FROM Forecast f"),
    @NamedQuery(name = "Forecast.findByIdForecast", query = "SELECT f FROM Forecast f WHERE f.idForecast = :idForecast"),
    @NamedQuery(name = "Forecast.findByGeneral", query = "SELECT f FROM Forecast f WHERE f.general = :general"),
    @NamedQuery(name = "Forecast.findByPrecipitation", query = "SELECT f FROM Forecast f WHERE f.precipitation = :precipitation"),
    @NamedQuery(name = "Forecast.findByHumidity", query = "SELECT f FROM Forecast f WHERE f.humidity = :humidity"),
    @NamedQuery(name = "Forecast.findByMaxTemp", query = "SELECT f FROM Forecast f WHERE f.maxTemp = :maxTemp"),
    @NamedQuery(name = "Forecast.findByMinTemp", query = "SELECT f FROM Forecast f WHERE f.minTemp = :minTemp")})
public class Forecast implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idForecast")
    private Integer idForecast;
    @Size(max = 45)
    @Column(name = "General")
    private String general;
    @Size(max = 45)
    @Column(name = "Precipitation")
    private String precipitation;
    @Size(max = 45)
    @Column(name = "Humidity")
    private String humidity;
    @Size(max = 45)
    @Column(name = "MaxTemp")
    private String maxTemp;
    @Size(max = 45)
    @Column(name = "MinTemp")
    private String minTemp;
    @JoinColumn(name = "idLocation", referencedColumnName = "idLocation")
    @ManyToOne
    private Location idLocation;

    public Forecast() {
    }

    public Forecast(Integer idForecast) {
        this.idForecast = idForecast;
    }

    public Integer getIdForecast() {
        return idForecast;
    }

    public void setIdForecast(Integer idForecast) {
        this.idForecast = idForecast;
    }

    public String getGeneral() {
        return general;
    }

    public void setGeneral(String general) {
        this.general = general;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public Location getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(Location idLocation) {
        this.idLocation = idLocation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idForecast != null ? idForecast.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Forecast)) {
            return false;
        }
        Forecast other = (Forecast) object;
        if ((this.idForecast == null && other.idForecast != null) || (this.idForecast != null && !this.idForecast.equals(other.idForecast))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Forecast[ idForecast=" + idForecast + " ]";
    }
    
}
