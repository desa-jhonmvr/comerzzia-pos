package com.comerzzia.jpos.persistencia.promociones.configuracion.billeton;

import com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.detalles.ConfiguracionBilletonDetalleBean;
import java.util.ArrayList;
import java.util.List;

import com.comerzzia.util.base.MantenimientoBean;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;

public class ConfiguracionBilletonBean extends MantenimientoBean {

    /**
     * 
     */
    private static final long serialVersionUID = 4003414603278133676L;
    private Long idConfBilleton;
    private String tipo;
    private Boolean vigente = false;
    private String descripcion;
    private Fecha fechaAlta;
    private Fecha fechaCambioVigencia;
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    private List<ConfiguracionBilletonDetalleBean> lstDetalle = new ArrayList<ConfiguracionBilletonDetalleBean>();

    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------
    public Long getIdConfBilleton() {
        return idConfBilleton;
    }

    public void setIdConfBilleton(Long idConfBilleton) {
        this.idConfBilleton = idConfBilleton;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo == null ? null : tipo.trim();
    }

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? null : descripcion.trim();
    }

    public Fecha getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Fecha fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Fecha getFechaCambioVigencia() {
        return fechaCambioVigencia;
    }

    public void setFechaCambioVigencia(Fecha fechaCambioVigencia) {
        this.fechaCambioVigencia = fechaCambioVigencia;
    }

    //INICIO METODOS PERSONALIZADOS--------------------------------------------
    public List<ConfiguracionBilletonDetalleBean> getLstDetalle() {
        return lstDetalle;
    }

    public void setLstDetalle(List<ConfiguracionBilletonDetalleBean> lstDetalle) {
        this.lstDetalle = lstDetalle;
    }

    @Override
    protected void initNuevoBean() {
    }

    public String getVigenteTexto() {
        return vigente ? "SI" : "NO";
    }

    public String getTipoTexto() {
        return tipo.equals("R") ? "Rango" : "Porcentaje";
    }

    public boolean isTipoCantidadRango() {
        return tipo.equals("R");
    }

    public ConfiguracionBilletonDetalleBean getDetalleAplicable(Boolean auspiciante, BigDecimal valor) {
        if (lstDetalle == null || lstDetalle.isEmpty()) {
            return null;
        }

        for (ConfiguracionBilletonDetalleBean det : lstDetalle) {
            if (det.isDetalleAplicable(auspiciante, valor)) {
                return det;
            }
        }
        return null;

    }
    //FIN METODOS PERSONALIZADOS-----------------------------------------------
}