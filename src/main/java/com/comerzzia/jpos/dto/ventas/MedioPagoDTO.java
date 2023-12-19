/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas;

import com.comerzzia.jpos.dto.ventas.paginaweb.PlanPagoDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Gabriel Simbania
 */
public class MedioPagoDTO implements Serializable {

    private static final long serialVersionUID = 2209755399163577803L;

    private String codMedPag;
    private String descripcionMedioPago;
    private BigDecimal cargo;
    private String cruzaEfectivo;
    private List<String> bines;
    private Integer diferidoMaximo;
    private List<PlanPagoDTO> planes;
    private String tarjetaComohogar;

    public MedioPagoDTO() {
    }

    public MedioPagoDTO(String codMedPag, String descripcionMedioPago, BigDecimal cargo, String cruzaEfectivo) {
        this.codMedPag = codMedPag;
        this.descripcionMedioPago = descripcionMedioPago;
        this.cargo = cargo;
        this.cruzaEfectivo = cruzaEfectivo;
    }

    public String getCodMedPag() {
        return codMedPag;
    }

    public void setCodMedPag(String codMedPag) {
        this.codMedPag = codMedPag;
    }

    public String getDescripcionMedioPago() {
        return descripcionMedioPago;
    }

    public void setDescripcionMedioPago(String descripcionMedioPago) {
        this.descripcionMedioPago = descripcionMedioPago;
    }

    public BigDecimal getCargo() {
        return cargo;
    }

    public void setCargo(BigDecimal cargo) {
        this.cargo = cargo;
    }

    public String getCruzaEfectivo() {
        return cruzaEfectivo;
    }

    public void setCruzaEfectivo(String cruzaEfectivo) {
        this.cruzaEfectivo = cruzaEfectivo;
    }

    public List<String> getBines() {
        return bines;
    }

    public void setBines(List<String> bines) {
        this.bines = bines;
    }

    public List<PlanPagoDTO> getPlanes() {
        return planes;
    }

    public void setPlanes(List<PlanPagoDTO> planes) {
        this.planes = planes;
    }

    public Integer getDiferidoMaximo() {
        return diferidoMaximo;
    }

    public void setDiferidoMaximo(Integer diferidoMaximo) {
        this.diferidoMaximo = diferidoMaximo;
    }

    public String getTarjetaComohogar() {
        return tarjetaComohogar;
    }

    public void setTarjetaComohogar(String tarjetaComohogar) {
        this.tarjetaComohogar = tarjetaComohogar;
    }
}
