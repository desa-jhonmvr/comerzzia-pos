package com.comerzzia.jpos.persistencia.cotizaciones;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;

public class CotizacionBean {
    private String uidCotizacion;

    private String codalm;

    private String codcaja;

    private Long idCotizacion;

    private Fecha fecha;

    private String usuario;

    private String codcli;

    private Fecha fechaVigencia;

    private BigDecimal total;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    private String descli;
    
    private boolean caducado;
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------


    public String getUidCotizacion() {
        return uidCotizacion;
    }

    public void setUidCotizacion(String uidCotizacion) {
        this.uidCotizacion = uidCotizacion == null ? null : uidCotizacion.trim();
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm == null ? null : codalm.trim();
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja == null ? null : codcaja.trim();
    }

    public Long getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(Long idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario == null ? null : usuario.trim();
    }

    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(String codcli) {
        this.codcli = codcli == null ? null : codcli.trim();
    }

    public Fecha getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Fecha fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    
    public String getDescli() {
        return descli;
    }

    public void setDescli(String descli) {
        this.descli = descli;
    }
    
    public boolean isCaducado() {
        return this.caducado;
    }
    
    public void setCaducado(boolean caducado) {
        this.caducado = caducado;
    }
    
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------

}