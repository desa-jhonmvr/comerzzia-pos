package com.comerzzia.jpos.entity.db;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TablaAmortizacionDetPK implements Serializable {
    private static final long serialVersionUID = 6620716174172537576L;
    @Column(name = "UID_TABLA_AMORTIZACION", nullable = false, length = 40)
    private String uidTablaAmortizacion;

    @Column(name = "NUMERO_CUOTA", nullable = false)
    private Long numeroCuota;

    public String getUidTablaAmortizacion() {
        return uidTablaAmortizacion;
    }

    public void setUidTablaAmortizacion(String uidTablaAmortizacion) {
        this.uidTablaAmortizacion = uidTablaAmortizacion;
    }

    public Long getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(Long numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TablaAmortizacionDetPK entity = (TablaAmortizacionDetPK) o;
        return Objects.equals(this.numeroCuota, entity.numeroCuota) &&
                Objects.equals(this.uidTablaAmortizacion, entity.uidTablaAmortizacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroCuota, uidTablaAmortizacion);
    }

}