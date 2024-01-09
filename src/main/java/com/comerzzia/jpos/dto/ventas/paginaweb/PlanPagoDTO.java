/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb;

import com.comerzzia.jpos.dto.credito.tabla.amortizacion.TablaAmortizacionCabDTO;
import com.comerzzia.jpos.dto.ventas.online.ItemOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.PedidoOnlineDTO;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class PlanPagoDTO implements Cloneable {

    private static final long serialVersionUID = 8111655003004802970L;

    private Long idPlan;
    private String plan;
    //@SerializedName("descuento")
    private BigDecimal porcentajeDescuento;
    private Long numCuotas;
    private BigDecimal cuota;
    private BigDecimal aPagar;
    //@SerializedName("total")
    private BigDecimal totalSinDescuento;
    private BigDecimal ahorro;
    private BigDecimal porcentajeInteres;
    private BigDecimal descuentosPromocion;
    private BigDecimal importeInteres;
    private List<PedidoOnlineDTO> pedidos;
    private TablaAmortizacionCabDTO tablaAmortizacion;

    private transient PagoCredito pagoCredito;
    private transient List<ItemOnlineDTO> items = new ArrayList<>();

    public PlanPagoDTO clone() throws CloneNotSupportedException {
        return (PlanPagoDTO) super.clone();
    }

    public PlanPagoDTO() {
    }

    public PlanPagoDTO(Long idPlan, String plan, BigDecimal porcentajeDescuento, Long numCuotas, BigDecimal cuota, BigDecimal aPagar, BigDecimal total, BigDecimal ahorro, BigDecimal porcentajeInteres, BigDecimal importeInteres,
            PagoCredito pagoCredito) {
        this.idPlan = idPlan;
        this.plan = plan;
        this.porcentajeDescuento = porcentajeDescuento;
        this.numCuotas = numCuotas;
        this.cuota = cuota;
        this.aPagar = aPagar;
        this.totalSinDescuento = total;
        this.ahorro = ahorro;
        this.porcentajeInteres = porcentajeInteres;
        this.importeInteres = importeInteres;
        this.pagoCredito = pagoCredito;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Long getNumCuotas() {
        return numCuotas;
    }

    public void setNumCuotas(Long numCuotas) {
        this.numCuotas = numCuotas;
    }

    public BigDecimal getCuota() {
        return cuota;
    }

    public void setCuota(BigDecimal cuota) {
        this.cuota = cuota;
    }

    public BigDecimal getaPagar() {
        return aPagar;
    }

    public void setaPagar(BigDecimal aPagar) {
        this.aPagar = aPagar;
    }

    public BigDecimal getTotalSinDescuento() {
        return totalSinDescuento;
    }

    public void setTotalSinDescuento(BigDecimal totalSinDescuento) {
        this.totalSinDescuento = totalSinDescuento;
    }

    public BigDecimal getAhorro() {
        return ahorro;
    }

    public void setAhorro(BigDecimal ahorro) {
        this.ahorro = ahorro;
    }

    public BigDecimal getPorcentajeInteres() {
        return porcentajeInteres;
    }

    public void setPorcentajeInteres(BigDecimal porcentajeInteres) {
        this.porcentajeInteres = porcentajeInteres;
    }

    public BigDecimal getDescuentosPromocion() {
        return descuentosPromocion;
    }

    public void setDescuentosPromocion(BigDecimal descuentosPromocion) {
        this.descuentosPromocion = descuentosPromocion;
    }

    

    public BigDecimal getImporteInteres() {
        return importeInteres;
    }

    public void setImporteInteres(BigDecimal importeInteres) {
        this.importeInteres = importeInteres;
    }

    public List<PedidoOnlineDTO> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<PedidoOnlineDTO> pedidos) {
        this.pedidos = pedidos;
    }

    public PagoCredito getPagoCredito() {
        return pagoCredito;
    }

    public void setPagoCredito(PagoCredito pagoCredito) {
        this.pagoCredito = pagoCredito;
    }

    public List<ItemOnlineDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemOnlineDTO> items) {
        this.items = items;
    }


    public TablaAmortizacionCabDTO getTablaAmortizacion() {
        return tablaAmortizacion;
    }

    public void setTablaAmortizacion(TablaAmortizacionCabDTO tablaAmortizacion) {
        this.tablaAmortizacion = tablaAmortizacion;
    }
}
