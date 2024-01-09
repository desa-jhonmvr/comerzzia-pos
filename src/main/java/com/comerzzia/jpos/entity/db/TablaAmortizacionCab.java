package com.comerzzia.jpos.entity.db;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "D_TABLA_AMORTIZACION_CAB_TBL")
public class TablaAmortizacionCab {
    @Id
    @Column(name = "UID_TABLA_AMORTIZACION", nullable = false, length = 40)
    private String uidTablaAmortizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "UID_CAJERO_CAJA", referencedColumnName = "UID_CAJERO_CAJA"),
            @JoinColumn(name = "LINEA", referencedColumnName = "LINEA")
    })
    private CajaDet dCajaDetTbl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODMEDPAG")
    private MedioPagoCaja codmedpag;

    @Column(name = "TIPO", length = 1)
    private String tipo;

    @Column(name = "VALOR_PRESTAMO", precision = 13, scale = 3)
    private BigDecimal valorPrestamo;

    @Column(name = "TASA_INTERES", precision = 13, scale = 3)
    private BigDecimal tasaInteres;

    @Column(name = "NUMERO_CUOTAS", precision = 13, scale = 3)
    private BigDecimal numeroCuotas;

    @Column(name = "NUMERO_PAGOS_POR_ANIO", precision = 13, scale = 3)
    private BigDecimal numeroPagosPorAnio;

    @Column(name = "INTERES_EQUIVALENTE", precision = 13, scale = 3)
    private BigDecimal interesEquivalente;

    @Column(name = "VALOR_INTERES", precision = 13, scale = 3)
    private BigDecimal valorInteres;

    @Column(name = "VALOR_PRIMERA_CUOTA", precision = 13, scale = 3)
    private BigDecimal valorPrimeraCuota;

    @Column(name = "ID_DOCUMENTO", length = 40)
    private String idDocumento;

    public String getUidTablaAmortizacion() {
        return uidTablaAmortizacion;
    }

    public void setUidTablaAmortizacion(String uidTablaAmortizacion) {
        this.uidTablaAmortizacion = uidTablaAmortizacion;
    }

    public CajaDet getDCajaDetTbl() {
        return dCajaDetTbl;
    }

    public void setDCajaDetTbl(CajaDet dCajaDetTbl) {
        this.dCajaDetTbl = dCajaDetTbl;
    }

    public MedioPagoCaja getCodmedpag() {
        return codmedpag;
    }

    public void setCodmedpag(MedioPagoCaja codmedpag) {
        this.codmedpag = codmedpag;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValorPrestamo() {
        return valorPrestamo;
    }

    public void setValorPrestamo(BigDecimal valorPrestamo) {
        this.valorPrestamo = valorPrestamo;
    }

    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public BigDecimal getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(BigDecimal numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public BigDecimal getNumeroPagosPorAnio() {
        return numeroPagosPorAnio;
    }

    public void setNumeroPagosPorAnio(BigDecimal numeroPagosPorAnio) {
        this.numeroPagosPorAnio = numeroPagosPorAnio;
    }

    public BigDecimal getInteresEquivalente() {
        return interesEquivalente;
    }

    public void setInteresEquivalente(BigDecimal interesEquivalente) {
        this.interesEquivalente = interesEquivalente;
    }

    public BigDecimal getValorInteres() {
        return valorInteres;
    }

    public void setValorInteres(BigDecimal valorInteres) {
        this.valorInteres = valorInteres;
    }

    public BigDecimal getValorPrimeraCuota() {
        return valorPrimeraCuota;
    }

    public void setValorPrimeraCuota(BigDecimal valorPrimeraCuota) {
        this.valorPrimeraCuota = valorPrimeraCuota;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

}