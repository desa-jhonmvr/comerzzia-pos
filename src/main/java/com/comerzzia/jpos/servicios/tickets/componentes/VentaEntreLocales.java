/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

/**
 *
 * @author MGRI
 */
public class VentaEntreLocales {
    
    private String desTiendaOrigen;
    private String desTiendaDestino;
    private String codTiendaOrigen;
    private String codTiendaDestino;
    private String codigoConfirmacion;

    public VentaEntreLocales(String codAlmacenOrigen,String codAlmacenDestino,String desTiendaOrigen, String desTiendaDestino, String codigoConfirmacion) {
        this.desTiendaOrigen = desTiendaOrigen;
        this.desTiendaDestino = desTiendaDestino;
        this.codTiendaOrigen = codAlmacenOrigen;
        this.codTiendaDestino = codAlmacenDestino;
        this.codigoConfirmacion = codigoConfirmacion;
    }

    public VentaEntreLocales() {
    }

    public String getDesTiendaOrigen() {
        return desTiendaOrigen;
    }

    public void setDesTiendaOrigen(String desTiendaOrigen) {
        this.desTiendaOrigen = desTiendaOrigen;
    }

    public String getDesTiendaDestino() {
        return desTiendaDestino;
    }

    public void setDesTiendaDestino(String desTiendaDestino) {
        this.desTiendaDestino = desTiendaDestino;
    }

    public String getCodTiendaOrigen() {
        return codTiendaOrigen;
    }

    public void setCodTiendaOrigen(String codTiendaOrigen) {
        this.codTiendaOrigen = codTiendaOrigen;
    }

    public String getCodTiendaDestino() {
        return codTiendaDestino;
    }

    public void setCodTiendaDestino(String codTiendaDestino) {
        this.codTiendaDestino = codTiendaDestino;
    }

    public String getCodigoConfirmacion() {
        return codigoConfirmacion;
    }

    public void setCodigoConfirmacion(String codigoConfirmacion) {
        this.codigoConfirmacion = codigoConfirmacion;
    }
    
    
    
}
