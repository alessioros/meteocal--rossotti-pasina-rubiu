/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.inject.Named;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Simone
 */
@Named
@Entity
@Table(name = "event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e")})
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue
    @Column(name = "idEvent")
    private Integer idEvent;
    @Size(max = 45)
    @Column(name = "Name")
    private String name;
    @Size(max = 240)
    @Column(name = "Description")
    private String description;
    @Size(max = 45)
    @Column(name = "Image")
    private String image;
    @Column(name = "StartTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "EndTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "OutDoor")
    private Boolean outDoor;
    @Column(name = "Public")
    private Boolean public1;
    @ManyToMany(mappedBy = "eventCollection")
    private Collection<User> userCollection;
    @OneToMany(mappedBy = "idEvent")
    private Collection<Notification> notificationCollection;
    @JoinColumn(name = "idOrganizer", referencedColumnName = "idUser")
    @ManyToOne
    private User idOrganizer;
    @JoinColumn(name = "idLocation", referencedColumnName = "idLocation")
    @ManyToOne
    private Location idLocation;

    public Event() {
    }

    public Event(Integer idEvent) {
        this.idEvent = idEvent;
    }

    public Integer getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Integer idEvent) {
        this.idEvent = idEvent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getOutDoor() {
        return outDoor;
    }

    public void setOutDoor(Boolean outDoor) {
        this.outDoor = outDoor;
    }

    public Boolean getPublic1() {
        return public1;
    }

    public void setPublic1(Boolean public1) {
        this.public1 = public1;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @XmlTransient
    public Collection<Notification> getNotificationCollection() {
        return notificationCollection;
    }

    public void setNotificationCollection(Collection<Notification> notificationCollection) {
        this.notificationCollection = notificationCollection;
    }

    public User getIdOrganizer() {
        return idOrganizer;
    }

    public void setIdOrganizer(User idOrganizer) {
        this.idOrganizer = idOrganizer;
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
        hash += (idEvent != null ? idEvent.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.idEvent == null && other.idEvent != null) || (this.idEvent != null && !this.idEvent.equals(other.idEvent))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.business.entity.Event[ idEvent=" + idEvent + " ]";
    }
    
}
