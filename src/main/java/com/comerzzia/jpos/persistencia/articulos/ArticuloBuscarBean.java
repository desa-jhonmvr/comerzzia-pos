/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.articulos;


import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class ArticuloBuscarBean {

    private BigDecimal precio;
    private String codArticulo;
    private String desArticulo;
    
    // Campos para la búsqueda de artículos del pos
    private String marca;
    private String proveedor;
    private String seccion;
    private String subseccion;
    private String modelo;
    private String codigoBarras;
    private String codAlmLocal;
    private String codAlmCentral;
    private String codAlmCentral2;
    private Integer tiempoGarantia;
    private String garantiaExtendida;
    private String bloqueo;
    private Integer stockLocal;
    private Integer stockCentral;
    private Integer stockCentral2;
    private Integer existencia;
    private Integer stockDisponible;
    private String descontinuado;
    private Integer cantidadUnidadManejo;
    
    // Campos necesarios para el control de disponibilidad
    protected Integer idItem;
    protected String codMarca;
    
    public ArticuloBuscarBean() {
    }
    
    
    
    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getSubseccion() {
        return subseccion;
    }

    public void setSubseccion(String subseccion) {
        this.subseccion = subseccion;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getCodAlmLocal() {
        return codAlmLocal;
    }

    public void setCodAlmLocal(String codAlmLocal) {
        this.codAlmLocal = codAlmLocal;
    }

    public String getCodAlmCentral() {
        return codAlmCentral;
    }

    public void setCodAlmCentral(String codAlmCentral) {
        this.codAlmCentral = codAlmCentral;
    }

    public Integer getTiempoGarantia() {
        return tiempoGarantia;
    }

    public void setTiempoGarantia(Integer tiempoGarantia) {
        this.tiempoGarantia = tiempoGarantia;
    }

    public String getGarantiaExtendida() {
        return garantiaExtendida;
    }

    public void setGarantiaExtendida(String garantiaExtendida) {
        this.garantiaExtendida = garantiaExtendida;
    }

    public Integer getStockLocal() {
        return stockLocal;
    }

    public void setStockLocal(Integer stockLocal) {
        this.stockLocal = stockLocal;
    }

    public Integer getStockCentral() {
        return stockCentral;
    }

    public void setStockCentral(Integer stockCentral) {
        this.stockCentral = stockCentral;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

    public String getDesArticulo() {
        return desArticulo;
    }

    public void setDesArticulo(String desArticulo) {
        this.desArticulo = desArticulo;
    }

    /**
     * @return the idItem
     */
    public Integer getIdItem() {
        return idItem;
    }

    /**
     * @param idItem the idItem to set
     */
    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    /**
     * @return the codMarca
     */
    public String getCodMarca() {
        return codMarca;
    }

    /**
     * @param codMarca the codMarca to set
     */
    public void setCodMarca(String codMarca) {
        this.codMarca = codMarca;
    }

    public String getCodAlmCentral2() {
        return codAlmCentral2;
    }

    public void setCodAlmCentral2(String codAlmCentral2) {
        this.codAlmCentral2 = codAlmCentral2;
    }

    public Integer getStockCentral2() {
        return stockCentral2;
    }

    public void setStockCentral2(Integer stockCentral2) {
        this.stockCentral2 = stockCentral2;
    }    

    public String getBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(String bloqueo) {
        this.bloqueo = bloqueo;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }

    public Integer getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(Integer stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public String getDescontinuado() {
        return descontinuado;
    }

    public void setDescontinuado(String descontinuado) {
        this.descontinuado = descontinuado;
    }

    public Integer getCantidadUnidadManejo() {
        return cantidadUnidadManejo;
    }

    public void setCantidadUnidadManejo(Integer cantidadUnidadManejo) {
        this.cantidadUnidadManejo = cantidadUnidadManejo;
    }
    
    
}
