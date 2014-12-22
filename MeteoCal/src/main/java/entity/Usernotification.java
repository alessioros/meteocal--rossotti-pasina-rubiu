/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Simone
 */
@Entity
@Table(name = "usernotification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usernotification.findAll", query = "SELECT u FROM Usernotification u"),
    @NamedQuery(name = "Usernotification.findByIdNotification", query = "SELECT u FROM Usernotification u WHERE u.usernotificationPK.idNotification = :idNotification"),
    @NamedQuery(name = "Usernotification.findByIdUser", query = "SELECT u FROM Usernotification u WHERE u.usernotificationPK.idUser = :idUser")})
public class Usernotification implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsernotificationPK usernotificationPK;
    @Lob
    @Column(name = "Pending")
    private byte[] pending;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @JoinColumn(name = "idNotification", referencedColumnName = "idNotification", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Notification notification;

    public Usernotification() {
    }

    public Usernotification(UsernotificationPK usernotificationPK) {
        this.usernotificationPK = usernotificationPK;
    }

    public Usernotification(int idNotification, int idUser) {
        this.usernotificationPK = new UsernotificationPK(idNotification, idUser);
    }

    public UsernotificationPK getUsernotificationPK() {
        return usernotificationPK;
    }

    public void setUsernotificationPK(UsernotificationPK usernotificationPK) {
        this.usernotificationPK = usernotificationPK;
    }

    public byte[] getPending() {
        return pending;
    }

    public void setPending(byte[] pending) {
        this.pending = pending;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usernotificationPK != null ? usernotificationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usernotification)) {
            return false;
        }
        Usernotification other = (Usernotification) object;
        if ((this.usernotificationPK == null && other.usernotificationPK != null) || (this.usernotificationPK != null && !this.usernotificationPK.equals(other.usernotificationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Usernotification[ usernotificationPK=" + usernotificationPK + " ]";
    }
    
}
