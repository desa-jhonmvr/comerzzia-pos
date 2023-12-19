/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gabriel Simbania
 */
@Entity
@Cacheable(false)
@XmlRootElement
@Table(name = "D_REPROCESO_GENERAL_TBL")
public class ReprocesoGeneral implements Serializable {

    private static final long serialVersionUID = 6056052249675841648L;

    @Id

    @SequenceGenerator(name = "SEQ", sequenceName = "SQ_REPROCESO_GENERAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ")
    @Column(name = "REPROCESO_ID")
    private Long reprocesoId;

    @Column(name = "UID_DOCUMENTO")
    private String uidDocumento;

    @Column(name = "PROCESO")
    private String proceso;

    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "OBJETO")
    @Lob
    private String objeto;

    @Column(name = "ESTADO")
    private Long estado;

    public ReprocesoGeneral() {
    }

    public ReprocesoGeneral(String uidDocumento, String proceso, Date fecha, String objeto, Long estado) {
        this.uidDocumento = uidDocumento;
        this.proceso = proceso;
        this.fecha = fecha;
        this.objeto = objeto;
        this.estado = estado;
    }

    public Long getReprocesoId() {
        return reprocesoId;
    }

    public void setReprocesoId(Long reprocesoId) {
        this.reprocesoId = reprocesoId;
    }

    public String getUidDocumento() {
        return uidDocumento;
    }

    public void setUidDocumento(String uidDocumento) {
        this.uidDocumento = uidDocumento;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

}
