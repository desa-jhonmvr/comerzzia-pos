package com.comerzzia.jpos.servicios.credito.tabla.amortizacion;

import com.comerzzia.jpos.dto.credito.tabla.amortizacion.EnumTipoTablaAmortizacion;
import com.comerzzia.jpos.dto.credito.tabla.amortizacion.TablaAmortizacionCab;
import com.comerzzia.jpos.dto.credito.tabla.amortizacion.TablaAmortizacionDet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Variables
 *
 * VP: Valor del préstamo
 * TI: Tasa de interés
 * NM: Número de cuotas
 *
 * Fórmulas
 *
 * Interés equivalente, es el porcentaje de interés por cada mes
 * IE = TI /12
 *
 * Cuota a pagar, el valor de la cuota a pagar
 * CP = ( IE*(1+IE)^NM)*VP/(((1+IE)^NM)-1)
 *
 * Capital vivo, es el porcentaje de interés por cada mes
 * 		Si la cuota es la primera
 * CV = VP
 * 		Si es más de la primera cuota
 * 			CV = CV(mes anterior) - CA
 *
 * Interés mensual, es el interes que se va pagando por cada mes
 * IM = CV(mes anterior) * IE
 *
 *
 * Capital Amortizado
 * CA = CP - IM
 */
public class TablaAmortizacionService {
    private  static TablaAmortizacionCab tablaAmortizacion;
    private  static final int NUMERO_DECIMALES = 7;
    private  static final BigDecimal NUMERO_PAGOS_POR_ANIO = BigDecimal.valueOf(Double.parseDouble("12"));

    public static TablaAmortizacionCab getTablaAmortizacion() {
        return tablaAmortizacion;
    }



    public static TablaAmortizacionCab init(BigDecimal valorPrestamos, BigDecimal numeroCuotas, BigDecimal tasaInteres){
        tablaAmortizacion = TablaAmortizacionCab.builder()
                .numeroCuostas(numeroCuotas)
                .numeroPagosPorAnio(NUMERO_PAGOS_POR_ANIO)
                .tasaInteres(tasaInteres)
                .valorPrestamo(valorPrestamos)
                .tipo(EnumTipoTablaAmortizacion.FRANCES)
                .build();
        calcularInteresEquivalente();
        calcularPrimeraCuota();
        calcularValorInteres();
        calcularTablaDeAmortizacion();
        correccionPorRedondeoDecimal();
        return getTablaAmortizacion();
    }
    private static void calcularInteresEquivalente(){
        if(tablaAmortizacion != null){
            tablaAmortizacion.setInteresEquivalente(tablaAmortizacion.getTasaInteres()
                    .divide(tablaAmortizacion.getNumeroPagosPorAnio(),NUMERO_DECIMALES, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(Double.parseDouble("100")),NUMERO_DECIMALES,RoundingMode.HALF_UP)
            );
        }

    }
    private static void correccionPorRedondeoDecimal(){
        if(tablaAmortizacion == null){

        }
        if(tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.FRANCES)) {

            BigDecimal errorRedondeo = tablaAmortizacion.getCuotas().get(tablaAmortizacion.getCuotas().size() - 1).getCapitalVivo();
            BigDecimal cuotaAPagar  = tablaAmortizacion.getCuotas().get(tablaAmortizacion.getCuotas().size() - 1).getCuotaAPagar();
            tablaAmortizacion.getCuotas().get(tablaAmortizacion.getCuotas().size() - 1).setCuotaAPagar(cuotaAPagar.add(errorRedondeo));

        }else if (tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.ALEMAN)) {

        }
    }
    private static  void calcularValorInteres(){
        if (tablaAmortizacion == null) {
            return;
        }
        if(tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.FRANCES)){
            BigDecimal valorPrestamo = tablaAmortizacion.getValorPrestamo();
            BigDecimal interesEquivalente = tablaAmortizacion.getInteresEquivalente();
            BigDecimal numeroCuotas = tablaAmortizacion.getNumeroCuotas();

            BigDecimal valorInteresTotal = BigDecimal.ZERO;
            BigDecimal saldoRestante = valorPrestamo;

            for (int cuota = 0; cuota < numeroCuotas.intValue(); cuota++) {
                BigDecimal valorInteres = saldoRestante.multiply(interesEquivalente)
                        .setScale(NUMERO_DECIMALES, RoundingMode.HALF_UP);
                valorInteresTotal = valorInteresTotal.add(valorInteres);

                BigDecimal valorCapital = tablaAmortizacion.getValorPrimeraCuota().subtract(valorInteres);
                saldoRestante = saldoRestante.subtract(valorCapital);
            }

            tablaAmortizacion.setValorInteres(valorInteresTotal.setScale(2, RoundingMode.HALF_UP));
        }else if (tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.ALEMAN)){
            //TO DO
        }
    }
    private static void calcularTablaDeAmortizacion(){
        if (tablaAmortizacion == null) {
            return;
        }
        BigDecimal valorPrestamo = tablaAmortizacion.getValorPrestamo();
        BigDecimal interesEquivalente = tablaAmortizacion.getInteresEquivalente();
        BigDecimal numeroCuotas = tablaAmortizacion.getNumeroCuotas();
        BigDecimal valorCuota = tablaAmortizacion.getValorPrimeraCuota();

        List<TablaAmortizacionDet> cuotas = new ArrayList<>();

        BigDecimal saldoRestante = valorPrestamo;

        for (int cuota = 1; cuota <= numeroCuotas.intValue(); cuota++) {
            BigDecimal valorInteres = saldoRestante.multiply(interesEquivalente)
                    .setScale(NUMERO_DECIMALES, RoundingMode.HALF_UP);

            BigDecimal valorCapital = valorCuota.subtract(valorInteres);
            saldoRestante = saldoRestante.subtract(valorCapital);

            TablaAmortizacionDet nuevaCuota = TablaAmortizacionDet.builder()
                    .numeroCuota((long) cuota)
                    .cuotaAPagar(valorCuota.setScale(2, RoundingMode.HALF_UP))
                    .interes(valorInteres.setScale(2, RoundingMode.HALF_UP))
                    .capitalAmortizado(valorCapital.setScale(2, RoundingMode.HALF_UP))
                    .capitalVivo(saldoRestante.setScale(2, RoundingMode.HALF_UP))
                    .build();

            cuotas.add(nuevaCuota);
        }

        tablaAmortizacion.setCuotas(cuotas);
    }
    private static void calcularPrimeraCuota(){
        if(tablaAmortizacion == null){
            return;
        }
        if(tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.FRANCES)){
            if(tablaAmortizacion.getInteresEquivalente().compareTo(BigDecimal.ZERO) > 0){
                BigDecimal aux  = BigDecimal.ONE.add(tablaAmortizacion.getInteresEquivalente())
                        .pow(tablaAmortizacion.getNumeroCuotas().intValue());
                BigDecimal aux2  = BigDecimal.ONE.add(tablaAmortizacion.getInteresEquivalente())
                        .pow(tablaAmortizacion.getNumeroCuotas().intValue())
                        .subtract(BigDecimal.ONE);
                tablaAmortizacion.setValorPrimeraCuota(
                        aux.multiply(tablaAmortizacion.getInteresEquivalente()).multiply(tablaAmortizacion.getValorPrestamo())
                                .divide(aux2,2,RoundingMode.HALF_UP)
                );
            }else{
                tablaAmortizacion.setValorPrimeraCuota(tablaAmortizacion.getValorPrestamo().divide(tablaAmortizacion.getNumeroCuotas(),2,RoundingMode.HALF_UP));

            }

        }else if (tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.ALEMAN)){
            //TO DO
        }

    }


    public static String imprimir() {
        return tablaAmortizacion.toString();
    }
}
