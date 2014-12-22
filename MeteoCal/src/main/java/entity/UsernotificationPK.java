/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Simone
 */
@Embeddable
public class UsernotificationPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "idNotification")
    private int idNotification;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idUser")
    private int idUser;

    public UsernotificationPK() {
    }

    public UsernotificationPK(int idNotification, int idUser) {
        this.idNotification = idNotification;
        this.idUser = idUser;
    }

    public int getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idNotification;
        hash += (int) idUser;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsernotificationPK)) {
            return false;
        }
        UsernotificationPK other = (UsernotificationPK) object;
        if (this.idNotification != other.idNotification) {
            return false;
        }
        if (this.idUser != other.idUser) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.UsernotificationPK[ idNotification=" + idNotification + ", idUser=" + idUser + " ]";
    }
    
}
