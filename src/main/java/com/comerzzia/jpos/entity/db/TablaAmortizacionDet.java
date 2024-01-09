package com.comerzzia.jpos.entity.db;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "D_TABLA_AMORTIZACION_DET_TBL")
public class TablaAmortizacionDet {
    @EmbeddedId
    private TablaAmortizacionDetPK id;

    @MapsId("uidTablaAmortizacion")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "UID_TABLA_AMORTIZACION", nullable = false)
    private TablaAmortizacionCab uidTablaAmortizacion;

    @Column(name = "CUOTA_A_PAGAR", precision = 13, scale = 3)
    private BigDecimal cuotaAPagar;

    @Column(name = "INTERES", precision = 13, scale = 3)
    private BigDecimal interes;

    @Column(name = "CAPITAL_AMORTIZADO", precision = 13, scale = 3)
    private BigDecimal capitalAmortizado;

    @Column(name = "CAPITAL_VIVO", precision = 13, scale = 3)
    private BigDecimal capitalVivo;

    public TablaAmortizacionDetPK getId() {
        return id;
    }

    public void setId(TablaAmortizacionDetPK id) {
        this.id = id;
    }

    public TablaAmortizacionCab getUidTablaAmortizacion() {
        return uidTablaAmortizacion;
    }

    public void setUidTablaAmortizacion(TablaAmortizacionCab uidTablaAmortizacion) {
        this.uidTablaAmortizacion = uidTablaAmortizacion;
    }

    public BigDecimal getCuotaAPagar() {
        return cuotaAPagar;
    }

    public void setCuotaAPagar(BigDecimal cuotaAPagar) {
        this.cuotaAPagar = cuotaAPagar;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getCapitalAmortizado() {
        return capitalAmortizado;
    }

    public void setCapitalAmortizado(BigDecimal capitalAmortizado) {
        this.capitalAmortizado = capitalAmortizado;
    }

    public BigDecimal getCapitalVivo() {
        return capitalVivo;
    }

    public void setCapitalVivo(BigDecimal capitalVivo) {
        this.capitalVivo = capitalVivo;
    }

}