package com.comerzzia.jpos.dto.credito.tabla.amortizacion;

import java.math.BigDecimal;

public class TablaAmortizacionDet {

    private Long numeroCuota;
    private BigDecimal cuotaAPagar;
    private BigDecimal interes;
    private BigDecimal capitalAmortizado;
    private BigDecimal capitalVivo;

    public TablaAmortizacionDet(Long numeroCuota, BigDecimal cuotaAPagar, BigDecimal interes, BigDecimal capitalAmortizado, BigDecimal capitalVivo) {
        this.numeroCuota = numeroCuota;
        this.cuotaAPagar = cuotaAPagar;
        this.interes = interes;
        this.capitalAmortizado = capitalAmortizado;
        this.capitalVivo = capitalVivo;
    }

    public TablaAmortizacionDet() {
    }

    public static TablaAmortizacionDetBuilder builder() {
        return new TablaAmortizacionDetBuilder();
    }

    public Long getNumeroCuota() {
        return this.numeroCuota;
    }

    public BigDecimal getCuotaAPagar() {
        return this.cuotaAPagar;
    }

    public BigDecimal getInteres() {
        return this.interes;
    }

    public BigDecimal getCapitalAmortizado() {
        return this.capitalAmortizado;
    }

    public BigDecimal getCapitalVivo() {
        return this.capitalVivo;
    }

    public void setNumeroCuota(Long numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public void setCuotaAPagar(BigDecimal cuotaAPagar) {
        this.cuotaAPagar = cuotaAPagar;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public void setCapitalAmortizado(BigDecimal capitalAmortizado) {
        this.capitalAmortizado = capitalAmortizado;
    }

    public void setCapitalVivo(BigDecimal capitalVivo) {
        this.capitalVivo = capitalVivo;
    }

    public String toString() {
        return this.getNumeroCuota() + "\t\t\t" + this.getCuotaAPagar() + "\t\t\t" + this.getInteres() + "\t\t\t" + this.getCapitalAmortizado() + "\t\t\t" + this.getCapitalVivo() + "\n";
    }

    public static class TablaAmortizacionDetBuilder {
        private Long numeroCuota;
        private BigDecimal cuotaAPagar;
        private BigDecimal interes;
        private BigDecimal capitalAmortizado;
        private BigDecimal capitalVivo;

        TablaAmortizacionDetBuilder() {
        }

        public TablaAmortizacionDetBuilder numeroCuota(Long numeroCuota) {
            this.numeroCuota = numeroCuota;
            return this;
        }

        public TablaAmortizacionDetBuilder cuotaAPagar(BigDecimal cuotaAPagar) {
            this.cuotaAPagar = cuotaAPagar;
            return this;
        }

        public TablaAmortizacionDetBuilder interes(BigDecimal interes) {
            this.interes = interes;
            return this;
        }

        public TablaAmortizacionDetBuilder capitalAmortizado(BigDecimal capitalAmortizado) {
            this.capitalAmortizado = capitalAmortizado;
            return this;
        }

        public TablaAmortizacionDetBuilder capitalVivo(BigDecimal capitalVivo) {
            this.capitalVivo = capitalVivo;
            return this;
        }

        public TablaAmortizacionDet build() {
            return new TablaAmortizacionDet(this.numeroCuota, this.cuotaAPagar, this.interes, this.capitalAmortizado, this.capitalVivo);
        }

        public String toString() {
            return "TablaAmortizacionDet.TablaAmortizacionDetBuilder(numeroCuota=" + this.numeroCuota + ", cuotaAPagar=" + this.cuotaAPagar + ", interes=" + this.interes + ", capitalAmortizado=" + this.capitalAmortizado + ", capitalVivo=" + this.capitalVivo + ")";
        }
    }
}
