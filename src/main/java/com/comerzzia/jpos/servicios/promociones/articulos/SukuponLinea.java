/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.articulos;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Cupon;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class SukuponLinea {

    private Cupon sukupon;
    private boolean auspiciante;
    private BigDecimal valor;
    private int idlinea;
    private Articulos articulo;
    private BigDecimal importe;
    private BigDecimal importeTotal;
    private BigDecimal utilizado;
    private BigDecimal saldo;
    private int cantidad;
    private Long idCupon;
    private String codAlm;
    private String Procesado;

    public boolean isAuspiciante() {
        return auspiciante;
    }

    public String getAuspiciante() {
        return auspiciante ? "true" : "false";
    }

    public void setAuspiciante(boolean auspiciante) {
        this.auspiciante = auspiciante;
    }

    public Cupon getSukupon() {
        return sukupon;
    }

    public void setSukupon(Cupon sukupon) {
        this.sukupon = sukupon;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdlinea() {
        return idlinea;
    }

    public void setIdlinea(int idlinea) {
        this.idlinea = idlinea;
    }

    public Long getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(Long idCupon) {
        this.idCupon = idCupon;
    }

    public BigDecimal getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(BigDecimal utilizado) {
        this.utilizado = utilizado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }

    public String getProcesado() {
        return Procesado;
    }

    public void setProcesado(String Procesado) {
        this.Procesado = Procesado;
    }

}
