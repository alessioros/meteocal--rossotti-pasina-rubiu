/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Simone
 */
@Entity
@Table(name = "invitation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invitation.findAll", query = "SELECT i FROM Invitation i")})
public class Invitation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue
    @NotNull
    @Column(name = "idInvitation")
    private Integer idInvitation;
    @Column(name = "Accepted")
    private Boolean accepted;
    @JoinColumn(name = "idNotification", referencedColumnName = "idNotification")
    @ManyToOne
    private Notification idNotification;

    public Invitation() {
    }

    public Invitation(Integer idInvitation) {
        this.idInvitation = idInvitation;
    }

    public Integer getIdInvitation() {
        return idInvitation;
    }

    public void setIdInvitation(Integer idInvitation) {
        this.idInvitation = idInvitation;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Notification getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(Notification idNotification) {
        this.idNotification = idNotification;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInvitation != null ? idInvitation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invitation)) {
            return false;
        }
        Invitation other = (Invitation) object;
        if ((this.idInvitation == null && other.idInvitation != null) || (this.idInvitation != null && !this.idInvitation.equals(other.idInvitation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.business.entity.Invitation[ idInvitation=" + idInvitation + " ]";
    }
    
}
