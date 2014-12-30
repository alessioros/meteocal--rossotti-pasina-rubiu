/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.business.entity;

import it.polimi.meteocal.business.control.PasswordEncrypter;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Simone
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue
    @Column(name = "idUser")
    private Integer idUser;
    
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Email")
    private String email;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Username")
    private String username;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1)
    @Column(name = "Password")
    private String password;
    
    @Size(max = 45)
    @Column(name = "Name")
    private String name;
   
    @Size(max = 45)
    @Column(name = "Surname")
    private String surname;
    
    @Column(name="Verificationkey")
    private String verificationkey;
    
    @Column(name = "Verified")
    private Boolean verified;
    
    @Column(name = "PublicCalendar")
    private Boolean publicCalendar;
    
    @Size(max = 26)
    @Column(name = "Groupname")
    private String groupname;
    
    @JoinTable(name = "calendar", joinColumns = {
        @JoinColumn(name = "idUser", referencedColumnName = "idUser")}, inverseJoinColumns = {
        @JoinColumn(name = "idEvent", referencedColumnName = "idEvent")})
    @ManyToMany
    private Collection<Event> eventCollection;
    
    @JoinTable(name = "contact", joinColumns = {
        @JoinColumn(name = "idUser", referencedColumnName = "idUser")}, inverseJoinColumns = {
        @JoinColumn(name = "idContact", referencedColumnName = "idUser")})
    @ManyToMany
    private Collection<User> userCollection;
    
    @ManyToMany(mappedBy = "userCollection")
    private Collection<User> userCollection1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Usernotification> usernotificationCollection;
    
    @OneToMany(mappedBy = "idOrganizer")
    private Collection<Event> eventCollection1;

    public User() {
    }

    public User(Integer idUser) {
        this.idUser = idUser;
    }

    public User(Integer idUser, String email, String username, String password) {
        this.idUser = idUser;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PasswordEncrypter.encryptPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
    
    public String getVerificationkey() {
        return verificationkey;
    }

    public void setVerificationkey(String verificationkey) {
        this.verificationkey = verificationkey;
    }
    
    public Boolean getPublicCalendar() {
        return publicCalendar;
    }

    public void setPublicCalendar(Boolean publicCalendar) {
        this.publicCalendar = publicCalendar;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    @XmlTransient
    public Collection<Event> getEventCollection() {
        return eventCollection;
    }

    public void setEventCollection(Collection<Event> eventCollection) {
        this.eventCollection = eventCollection;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @XmlTransient
    public Collection<User> getUserCollection1() {
        return userCollection1;
    }

    public void setUserCollection1(Collection<User> userCollection1) {
        this.userCollection1 = userCollection1;
    }

    @XmlTransient
    public Collection<Usernotification> getUsernotificationCollection() {
        return usernotificationCollection;
    }

    public void setUsernotificationCollection(Collection<Usernotification> usernotificationCollection) {
        this.usernotificationCollection = usernotificationCollection;
    }

    @XmlTransient
    public Collection<Event> getEventCollection1() {
        return eventCollection1;
    }

    public void setEventCollection1(Collection<Event> eventCollection1) {
        this.eventCollection1 = eventCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.meteocal.business.entity.User[ idUser=" + idUser + " ]";
    }
    
}
