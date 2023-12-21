package com.comerzzia.jpos.dto.credito.tabla.amortizacion;

import java.math.BigDecimal;
import java.util.List;


public class TablaAmortizacionCab {

    private BigDecimal valorPrestamo;

    private BigDecimal tasaInteres;
    private BigDecimal numeroCuotas;

    private BigDecimal numeroPagosPorAnio;
    private BigDecimal interesEquivalente;
    private BigDecimal valorInteres;
    private BigDecimal valorPrimeraCuota;
    private EnumTipoTablaAmortizacion tipo;

    private List<TablaAmortizacionDet> cuotas;

    public TablaAmortizacionCab(BigDecimal valorPrestamo, BigDecimal tasaInteres, BigDecimal numeroCuostas, BigDecimal interesEquivalente, BigDecimal valorPrimeraCuota, EnumTipoTablaAmortizacion tipo, List<TablaAmortizacionDet> cuotas, BigDecimal valorInteres, BigDecimal numeroPagosPorAnio) {
        this.valorPrestamo = valorPrestamo;
        this.tasaInteres = tasaInteres;
        this.numeroCuotas = numeroCuostas;
        this.interesEquivalente = interesEquivalente;
        this.valorPrimeraCuota = valorPrimeraCuota;
        this.tipo = tipo;
        this.cuotas = cuotas;
        this.valorInteres = valorInteres;
        this.numeroPagosPorAnio = numeroPagosPorAnio;
    }

    public TablaAmortizacionCab() {
    }


    public BigDecimal getNumeroPagosPorAnio() {
        return numeroPagosPorAnio;
    }

    public void setNumeroPagosPorAnio(BigDecimal numeroPagosPorAnio) {
        this.numeroPagosPorAnio = numeroPagosPorAnio;
    }
    public static TablaAmortizacionCabBuilder builder() {
        return new TablaAmortizacionCabBuilder();
    }

    public BigDecimal getValorPrestamo() {
        return this.valorPrestamo;
    }

    public BigDecimal getTasaInteres() {
        return this.tasaInteres;
    }

    public BigDecimal getNumeroCuotas() {
        return this.numeroCuotas;
    }

    public BigDecimal getInteresEquivalente() {
        return this.interesEquivalente;
    }

    public BigDecimal getValorPrimeraCuota() {
        return this.valorPrimeraCuota;
    }

    public List<TablaAmortizacionDet> getCuotas() {
        return this.cuotas;
    }

    public void setValorPrestamo(BigDecimal valorPrestamo) {
        this.valorPrestamo = valorPrestamo;
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public void setNumeroCuotas(BigDecimal numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public void setInteresEquivalente(BigDecimal interesEquivalente) {
        this.interesEquivalente = interesEquivalente;
    }

    public void setValorPrimeraCuota(BigDecimal valorPrimeraCuota) {
        this.valorPrimeraCuota = valorPrimeraCuota;
    }

    public void setCuotas(List<TablaAmortizacionDet> cuotas) {
        this.cuotas = cuotas;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("| %-12s | %-12s | %-12s | %-18s | %-12s |%n",
                "No. Cuota", "Cuota", "Interés", "Capital Amortizado", "Capital Vivo"));
        sb.append("|--------------|--------------|--------------|--------------------|--------------|\n");

        for (TablaAmortizacionDet cuota : this.cuotas) {
            sb.append(String.format("| %-12d | %-12.2f | %-12.2f | %-18.2f | %-12.2f |%n",
                    cuota.getNumeroCuota(), cuota.getCuotaAPagar(), cuota.getInteres(),
                    cuota.getCapitalAmortizado(), cuota.getCapitalVivo()));
        }

        sb.append("|--------------|--------------|--------------|--------------------|--------------|\n");

        // Totales
        BigDecimal totalCuota = BigDecimal.ZERO;
        BigDecimal totalInteres = BigDecimal.ZERO;
        BigDecimal totalCapitalAmortizado = BigDecimal.ZERO;

        for (TablaAmortizacionDet cuota : this.cuotas) {
            totalCuota = totalCuota.add(cuota.getCuotaAPagar());
            totalInteres = totalInteres.add(cuota.getInteres());
            totalCapitalAmortizado = totalCapitalAmortizado.add(cuota.getCapitalAmortizado());
        }

        BigDecimal totalCapitalVivo = this.cuotas.get(this.cuotas.size() - 1).getCapitalVivo();

        sb.append(String.format("| %-12s | %-12.2f | %-12.2f | %-18.2f | %-12.2f |%n",
                "Totales", totalCuota, totalInteres, totalCapitalAmortizado, totalCapitalVivo));

        return "TablaAmortizacionCab.TablaAmortizacionCabBuilder(valorPrestamo=" + this.valorPrestamo + ",\n tasaInteres=" + this.tasaInteres + ",\n numeroCuostas=" + this.numeroCuotas + ",\n interesEquivalente=" + this.interesEquivalente + ",\n valorPrimeraCuota=" + this.valorPrimeraCuota + ",\n valorInteres=" + this.valorInteres+ ",\n tipo=" + this.tipo+
                ",\n cuotas= \n" + sb.toString() + "\n)";
    }

    public EnumTipoTablaAmortizacion getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoTablaAmortizacion tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValorInteres() {
        return valorInteres;
    }

    public void setValorInteres(BigDecimal valorInteres) {
        this.valorInteres = valorInteres;
    }

    public static class TablaAmortizacionCabBuilder {
        private BigDecimal valorPrestamo;
        private BigDecimal tasaInteres;
        private BigDecimal numeroCuostas;
        private BigDecimal numeroPagosPorAnio;
        private BigDecimal interesEquivalente;
        private BigDecimal valorPrimeraCuota;
        private List<TablaAmortizacionDet> cuotas;
        private EnumTipoTablaAmortizacion tipo;
        private BigDecimal valorInteres;

        TablaAmortizacionCabBuilder() {
        }

        public TablaAmortizacionCabBuilder valorPrestamo(BigDecimal valorPrestamo) {
            this.valorPrestamo = valorPrestamo;
            return this;
        }
        public TablaAmortizacionCabBuilder numeroPagosPorAnio(BigDecimal numeroPagosPorAnio) {
            this.numeroPagosPorAnio = numeroPagosPorAnio;
            return this;
        }

        public TablaAmortizacionCabBuilder tasaInteres(BigDecimal tasaInteres) {
            this.tasaInteres = tasaInteres;
            return this;
        }

        public TablaAmortizacionCabBuilder numeroCuostas(BigDecimal numeroCuostas) {
            this.numeroCuostas = numeroCuostas;
            return this;
        }

        public TablaAmortizacionCabBuilder interesEquivalente(BigDecimal interesEquivalente) {
            this.interesEquivalente = interesEquivalente;
            return this;
        }

        public TablaAmortizacionCabBuilder valorPrimeraCuota(BigDecimal valorPrimeraCuota) {
            this.valorPrimeraCuota = valorPrimeraCuota;
            return this;
        }

        public TablaAmortizacionCabBuilder tipo(EnumTipoTablaAmortizacion tipo) {
            this.tipo = tipo;
            return this;
        }
        public TablaAmortizacionCabBuilder cuotas(List<TablaAmortizacionDet> cuotas) {
            this.cuotas = cuotas;
            return this;
        }public TablaAmortizacionCabBuilder cuotas(BigDecimal valorInteres) {
            this.valorInteres = valorInteres;
            return this;
        }

        public TablaAmortizacionCab build() {
            return new TablaAmortizacionCab(this.valorPrestamo, this.tasaInteres, this.numeroCuostas, this.interesEquivalente, this.valorPrimeraCuota,this.tipo , this.cuotas, this.valorInteres, this.numeroPagosPorAnio);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("| %-12s | %-12s | %-12s | %-18s | %-12s |%n",
                    "No. Cuota", "Cuota", "Interés", "Capital Amortizado", "Capital Vivo"));
            sb.append("|--------------|--------------|--------------|---------------------|--------------|\n");

            for (TablaAmortizacionDet cuota : this.cuotas) {
                sb.append(String.format("| %-12d | %-12.2f | %-12.2f | %-18.2f | %-12.2f |%n",
                        cuota.getNumeroCuota(), cuota.getCuotaAPagar(), cuota.getInteres(),
                        cuota.getCapitalAmortizado(), cuota.getCapitalVivo()));
            }

            sb.append("|--------------|--------------|--------------|---------------------|--------------|\n");

            // Totales
            BigDecimal totalCuota = BigDecimal.ZERO;
            BigDecimal totalInteres = BigDecimal.ZERO;
            BigDecimal totalCapitalAmortizado = BigDecimal.ZERO;

            for (TablaAmortizacionDet cuota : this.cuotas) {
                totalCuota = totalCuota.add(cuota.getCuotaAPagar());
                totalInteres = totalInteres.add(cuota.getInteres());
                totalCapitalAmortizado = totalCapitalAmortizado.add(cuota.getCapitalAmortizado());
            }

            BigDecimal totalCapitalVivo = this.cuotas.get(this.cuotas.size() - 1).getCapitalVivo();

            sb.append(String.format("| %-12s | %-12.2f | %-12.2f | %-18.2f | %-12.2f |%n",
                    "Totales", totalCuota, totalInteres, totalCapitalAmortizado, totalCapitalVivo));

            return "TablaAmortizacionCab.TablaAmortizacionCabBuilder(valorPrestamo=" + this.valorPrestamo + ",\n tasaInteres=" + this.tasaInteres + ",\n numeroCuostas=" + this.numeroCuostas + ",\n interesEquivalente=" + this.interesEquivalente + ",\n valorPrimeraCuota=" + this.valorPrimeraCuota
                    + ",\n valorInteres=" + this.valorInteres+",\n tipo=" + this.tipo+",\n numeroPagosPorAnio=" + this.numeroPagosPorAnio+
                    ",\n cuotas= \n" + sb.toString() + ")";
        }


    }
}
