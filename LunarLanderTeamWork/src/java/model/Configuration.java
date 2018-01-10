/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juanm
 */
@Entity
@Table(name = "configuration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Configuration.findAll", query = "SELECT c FROM Configuration c")
    , @NamedQuery(name = "Configuration.findByConfigId", query = "SELECT c FROM Configuration c WHERE c.configId = :configId")
    , @NamedQuery(name = "Configuration.findByConfigName", query = "SELECT c FROM Configuration c WHERE c.configName = :configName")
    , @NamedQuery(name = "Configuration.findByDifficulty", query = "SELECT c FROM Configuration c WHERE c.difficulty = :difficulty")
    , @NamedQuery(name = "Configuration.findBySpaceship", query = "SELECT c FROM Configuration c WHERE c.spaceship = :spaceship")
    , @NamedQuery(name = "Configuration.findByMoon", query = "SELECT c FROM Configuration c WHERE c.moon = :moon")})
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "config_id")
    private Integer configId;
    @Column(name = "config_name")
    private String configName;
    @Column(name = "difficulty")
    private Integer difficulty;
    @Column(name = "spaceship")
    private Integer spaceship;
    @Column(name = "moon")
    private Integer moon;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "configId", fetch = FetchType.LAZY)
    private List<Score> scoreList;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users userId;

    public Configuration() {
    }

    public Configuration(Integer configId) {
        this.configId = configId;
    }

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getSpaceship() {
        return spaceship;
    }

    public void setSpaceship(Integer spaceship) {
        this.spaceship = spaceship;
    }

    public Integer getMoon() {
        return moon;
    }

    public void setMoon(Integer moon) {
        this.moon = moon;
    }

    @XmlTransient
    public List<Score> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configId != null ? configId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configuration)) {
            return false;
        }
        Configuration other = (Configuration) object;
        if ((this.configId == null && other.configId != null) || (this.configId != null && !this.configId.equals(other.configId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Configuration[ configId=" + configId + " ]";
    }
    
}