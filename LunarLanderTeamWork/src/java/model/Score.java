/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juanm
 */
@Entity
@Table(name = "score")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Score.findAll", query = "SELECT s FROM Score s")
    , @NamedQuery(name = "Score.findByScoreId", query = "SELECT s FROM Score s WHERE s.scoreId = :scoreId")
    , @NamedQuery(name = "Score.findBySpeed", query = "SELECT s FROM Score s WHERE s.speed = :speed")
    , @NamedQuery(name = "Score.findByInittime", query = "SELECT s FROM Score s WHERE s.inittime = :inittime")
    , @NamedQuery(name = "Score.findByEndtime", query = "SELECT s FROM Score s WHERE s.endtime = :endtime")})
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "score_id")
    private Integer scoreId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "speed")
    private BigDecimal speed;
    @Column(name = "inittime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inittime;
    @Column(name = "endtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtime;
    @JoinColumn(name = "config_id", referencedColumnName = "config_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Configuration configId;

    public Score() {
    }

    public Score(Integer scoreId) {
        this.scoreId = scoreId;
    }

    public Integer getScoreId() {
        return scoreId;
    }

    public void setScoreId(Integer scoreId) {
        this.scoreId = scoreId;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public Date getInittime() {
        return inittime;
    }

    public void setInittime(Date inittime) {
        this.inittime = inittime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Configuration getConfigId() {
        return configId;
    }

    public void setConfigId(Configuration configId) {
        this.configId = configId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scoreId != null ? scoreId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Score)) {
            return false;
        }
        Score other = (Score) object;
        if ((this.scoreId == null && other.scoreId != null) || (this.scoreId != null && !this.scoreId.equals(other.scoreId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Score[ scoreId=" + scoreId + " ]";
    }
    
}
