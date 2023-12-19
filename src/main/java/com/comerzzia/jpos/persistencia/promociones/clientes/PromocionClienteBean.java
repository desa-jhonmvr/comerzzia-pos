package com.comerzzia.jpos.persistencia.promociones.clientes;

import java.util.Date;

public class PromocionClienteBean extends PromocionClienteKey {
    private String codAlmacen;

    private Date fecha;

    private Integer version;

    private Boolean procesado;

    private String anulada;
    
    private String uidTicketDSocio;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------


    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen == null ? null : codAlmacen.trim();
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    public String getAnulada() {
        return anulada;
    }

    public void setAnulada(String anulada) {
        this.anulada = anulada == null ? null : anulada.trim();
    }

    public String getUidTicketDSocio() {
        return uidTicketDSocio;
    }

    public void setUidTicketDSocio(String uidTicketDSocio) {
        this.uidTicketDSocio = uidTicketDSocio;
    }
    
    //INICIO MÉTODOS PERSONALIZADOS--------------------------------------------
    
    //FIN MÉTODOS PERSONALIZADOS-----------------------------------------------
}