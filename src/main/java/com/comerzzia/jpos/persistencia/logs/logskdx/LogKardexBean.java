/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.logs.logskdx;

import com.comerzzia.util.base.MantenimientoBean;
import es.mpsistemas.util.fechas.Fecha;

/**
 *
 * @author SMLM
 */
public class LogKardexBean extends MantenimientoBean{

    @Override
    protected void initNuevoBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private String local;
    private String caja;
    private String factura;
    private int marca;
    private int item;
    private int cantidad;
    private int tipo;
    private Fecha fechaHora;
    private String tipoAccion;
    private String usuarioAutorizacion;
    private String observaciones;
    
    public static final String tipoAccionPlanNovio = "PLAN NOVIO";
    public static final String tipoAccionReservacion = "RESERVACION";
    public static final String tipoAccionVenta = "VENTA";
    public static final String tipoAccionAnulacion = "ANULACION";
    public static final String tipoAccionDevolucion = "DEVOLUCION";

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public int getMarca() {
        return marca;
    }

    public void setMarca(int marca) {
        this.marca = marca;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Fecha getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Fecha fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getUsuarioAutorizacion() {
        return usuarioAutorizacion;
    }

    public void setUsuarioAutorizacion(String usuarioAutorizacion) {
        this.usuarioAutorizacion = usuarioAutorizacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public boolean isTipoAccionVenta() {
        return tipoAccion != null && tipoAccion.equals(tipoAccionVenta);
    }
    
    public boolean isTipoAccionReservacion() {
        return tipoAccion != null && tipoAccion.equals(tipoAccionReservacion);
    }
    
    public boolean isTipoAccionAnulacion() {
        return tipoAccion != null && tipoAccion.equals(tipoAccionAnulacion);
    }
    
    public boolean isTipoAccionDevolucion() {
        return tipoAccion != null && tipoAccion.equals(tipoAccionDevolucion);
    }
    
    public boolean isTipoAccionPlanNovio() {
        return tipoAccion != null && tipoAccion.equals(tipoAccionPlanNovio);
    }    
}
