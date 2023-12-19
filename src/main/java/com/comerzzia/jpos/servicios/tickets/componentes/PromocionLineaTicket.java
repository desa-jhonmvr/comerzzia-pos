/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class PromocionLineaTicket {

    public static final byte PROMO_LINEA_UNITARIA = 0;
    public static final byte PROMO_LINEA_MULTIPLE = 1;
    public static final byte PROMO_SUBTOTAL = 2;
    public static final byte PROMO_CUPON_DESCUENTO = 3;
    public static final byte PROMO_MANUAL = 4;
    public static final byte PROMO_DIA_SOCIO = 5;

    private Long idPromocion;
    private Long idTipoPromocion;
    private String desTipoPromocion;
    private String textoPromocion;
    private BigDecimal precioTarifa;
    private BigDecimal precioTarifaTotal;
    private Integer cantidadPromocion;
    private BigDecimal importePromocion;
    private BigDecimal importeTotalPromocion;
    private BigDecimal importeTotalAhorro;
    private BigDecimal importeAhorro;
    private byte tipoPromocionTicket;
    private DescuentoTicket descuentoTicket;  // linea de descuento asociada a esta promoción // TODO: PROMOCIONES - esta referencia sólo se está completando con las promociones a totales, habría que hacerlo con el resto
    private BigDecimal importeTotaltarifaOrigen;
    private BigDecimal importeTotalAPagar;
    private BigDecimal descuento;
    private boolean tieneFiltroPagosTarjetaSukasa;

    public PromocionLineaTicket(Promocion promocion, byte tipo) {
        if (tipo != PROMO_MANUAL) {
            idPromocion = promocion.getIdPromocion();
            idTipoPromocion = promocion.getIdTipoPromocion();
            desTipoPromocion = promocion.getDesTipoPromocion();
        }
        tipoPromocionTicket = tipo;
    }

    public PromocionLineaTicket() {
    }

    public void calculaAhorroDtoPorcentaje(BigDecimal descuento) {
        importePromocion = Numero.porcentaje(precioTarifa, descuento).multiply(new BigDecimal(cantidadPromocion));
        importeTotalPromocion = Numero.porcentaje(precioTarifaTotal, descuento).multiply(new BigDecimal(cantidadPromocion));
        importeTotalAhorro = importeTotalPromocion;
        importeAhorro = importePromocion;
        redondear();
    }

    public void calculaAhorroDtoPrecio(BigDecimal precio, BigDecimal precioTotal) {
        importePromocion = precioTarifa.subtract(precio).multiply(new BigDecimal(cantidadPromocion));
        importeTotalPromocion = precioTarifaTotal.subtract(precioTotal).multiply(new BigDecimal(cantidadPromocion));
        importeTotalAhorro = importeTotalPromocion;
        importeAhorro = importePromocion;
        redondear();
    }

    private void redondear() {
        importePromocion = Numero.redondear(importePromocion);
        importeTotalPromocion = Numero.redondear(importeTotalPromocion);
        importeTotalAhorro = Numero.redondear(importeTotalAhorro);
        importeAhorro = Numero.redondear(importeAhorro);
    }

    public Integer getCantidadPromocion() {
        return cantidadPromocion;
    }

    public BigDecimal getImporteTotalAhorro() {
        return importeTotalAhorro;
    }

    public BigDecimal getPrecioTarifa() {
        return precioTarifa;
    }

    public BigDecimal getPrecioTarifaTotal() {
        return precioTarifaTotal;
    }

    public void setCantidadPromocion(Integer cantidadPromocion) {
        this.cantidadPromocion = cantidadPromocion;
    }

    public void setImportesAhorro(BigDecimal importeAhorro, BigDecimal importeTotalAhorro) {
        this.importeTotalAhorro = importeTotalAhorro;
        this.importeAhorro = importeAhorro;
    }

    public void setPrecioTarifa(BigDecimal precioTarifa) {
        this.precioTarifa = precioTarifa;
    }

    public void setPrecioTarifaTotal(BigDecimal precioTarifaTotal) {
        this.precioTarifaTotal = precioTarifaTotal;
    }

    public BigDecimal getImportePromocion() {
        return importePromocion;
    }

    public void setImportePromocion(BigDecimal importePromocion) {
        this.importePromocion = importePromocion;
    }

    public BigDecimal getImporteTotalPromocion() {
        return importeTotalPromocion;
    }

    public void setImporteTotalPromocion(BigDecimal importeTotalPromocion) {
        this.importeTotalPromocion = importeTotalPromocion;
    }

    public String getTextoPromocion() {
        return textoPromocion;
    }

    public void setTextoPromocion(String textoPromocion) {
        this.textoPromocion = textoPromocion;
    }

    public String getDesTipoPromocion() {
        return desTipoPromocion;
    }

    public void setIdPromocion(Long idPromocion) {
        this.idPromocion = idPromocion;
    }

    public void setIdTipoPromocion(Long idTipoPromocion) {
        this.idTipoPromocion = idTipoPromocion;
    }

    public void setDesTipoPromocion(String desTipoPromocion) {
        this.desTipoPromocion = desTipoPromocion;
    }

    public Long getIdPromocion() {
        return idPromocion;
    }

    public Long getIdTipoPromocion() {
        return idTipoPromocion;
    }

    public boolean isTipoLineaUnitaria() {
        return (tipoPromocionTicket == PROMO_LINEA_UNITARIA || tipoPromocionTicket == PROMO_DIA_SOCIO);
    }

    public boolean isTipoLineaMultiple() {
        return tipoPromocionTicket == PROMO_LINEA_MULTIPLE;
    }

    public boolean isTipoSubtotal() {
        return tipoPromocionTicket == PROMO_SUBTOTAL;
    }

    public boolean isTipoManual() {
        return tipoPromocionTicket == PROMO_MANUAL;
    }

    public boolean isTipoDiaSocio() {
        return tipoPromocionTicket == PROMO_DIA_SOCIO;
    }

    public boolean isTipoCuponDescuento() {
        return tipoPromocionTicket == PROMO_CUPON_DESCUENTO;
    }

    public BigDecimal getImporteAhorro() {
        return importeAhorro;
    }

    public DescuentoTicket getDescuentoTicket() {
        return descuentoTicket;
    }

    public void setDescuentoTicket(DescuentoTicket descuentoTicket) {
        this.descuentoTicket = descuentoTicket;
    }

    public BigDecimal getImporteTotaltarifaOrigen() {
        return importeTotaltarifaOrigen;
    }

    public void setImporteTotaltarifaOrigen(BigDecimal importeTotaltarifaOrigen) {
        this.importeTotaltarifaOrigen = importeTotaltarifaOrigen;
    }

    public BigDecimal getImporteTotalAPagar() {
        return importeTotalAPagar;
    }

    public void setImporteTotalAPagar(BigDecimal importeTotalAPagar) {
        this.importeTotalAPagar = importeTotalAPagar;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public boolean isTieneFiltroPagosTarjetaSukasa() {
        return tieneFiltroPagosTarjetaSukasa;
    }

    public void setTieneFiltroPagosTarjetaSukasa(boolean tieneFiltroPagosTarjetaSukasa) {
        this.tieneFiltroPagosTarjetaSukasa = tieneFiltroPagosTarjetaSukasa;
    }

}
