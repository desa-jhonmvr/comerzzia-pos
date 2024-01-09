package com.comerzzia.jpos.servicios.credito.tabla.amortizacion;

import com.comerzzia.jpos.dto.credito.tabla.amortizacion.EnumTipoTablaAmortizacion;
import com.comerzzia.jpos.dto.credito.tabla.amortizacion.TablaAmortizacionCabDTO;
import com.comerzzia.jpos.dto.credito.tabla.amortizacion.TablaAmortizacionDetDTO;
import org.apache.commons.lang.StringUtils;

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
    public static final String APLICA_INTERES_SI = "S";
    public static final String APLICA_INTERES_NO = "N";
    private  static TablaAmortizacionCabDTO tablaAmortizacion;
    private  static final int NUMERO_DECIMALES = 8;
    public   static final String ALEMAN = "A";

    public  static final String FRANCES = "F";

    private  static final BigDecimal NUMERO_PAGOS_POR_ANIO = BigDecimal.valueOf(Double.parseDouble("12"));

    private static BigDecimal errorRedondeo;
    private static BigDecimal errorRedondeoInteres;
    public static TablaAmortizacionCabDTO getTablaAmortizacion() {
        return tablaAmortizacion;
    }

    /**
     *CALCULA TABLA DE AMORTIZACION
     * @param valorPrestamos
     * @param numeroCuotas
     * @param tasaInteres
     * @param tipo F = FRACES, A = ALEMAN
     * @return
     */

    public static TablaAmortizacionCabDTO init(BigDecimal valorPrestamos, BigDecimal numeroCuotas, BigDecimal tasaInteres, String tipo){

        if(valorPrestamos == null || numeroCuotas == null || tasaInteres == null || numeroCuotas.compareTo(BigDecimal.ZERO) == 0 || valorPrestamos.compareTo(BigDecimal.ZERO) == 0){
            tablaAmortizacion = null;
            return null;
        }
        if(StringUtils.isNotEmpty(tipo)){
            EnumTipoTablaAmortizacion tipoEnum = null;
            if(tipo.equals(FRANCES)){
                tipoEnum = EnumTipoTablaAmortizacion.FRANCES;
            } else if (tipo.equals(ALEMAN)) {
                tipoEnum = EnumTipoTablaAmortizacion.ALEMAN;
            }
            tablaAmortizacion = TablaAmortizacionCabDTO.builder()
                    .numeroCuostas(numeroCuotas)
                    .numeroPagosPorAnio(NUMERO_PAGOS_POR_ANIO)
                    .tasaInteres(tasaInteres)
                    .valorPrestamo(valorPrestamos)
                    .tipo(tipoEnum)
                    .build();
            calcularInteresEquivalente();
            calcularValorInteres();
            calcularTablaDeAmortizacion();
            correccionPorRedondeoDecimal();
            if(tablaAmortizacion != null && tablaAmortizacion.getValorInteres() != null){
                tablaAmortizacion.setValorPrestamoInteres(tablaAmortizacion.getValorPrestamo().add(tablaAmortizacion.getValorInteres()));
            }

            return getTablaAmortizacion();
        }else{
            tablaAmortizacion = TablaAmortizacionCabDTO.builder()
                    .numeroCuostas(numeroCuotas)
                    .numeroPagosPorAnio(NUMERO_PAGOS_POR_ANIO)
                    .tasaInteres(tasaInteres)
                    .valorPrestamo(valorPrestamos)
                    .tipo(EnumTipoTablaAmortizacion.FRANCES)
                    .build();
            calcularInteresEquivalente();
            calcularValorInteres();
            calcularTablaDeAmortizacion();
            correccionPorRedondeoDecimal();
            if(tablaAmortizacion != null && tablaAmortizacion.getValorInteres() != null){
                tablaAmortizacion.setValorPrestamoInteres(tablaAmortizacion.getValorPrestamo().add(tablaAmortizacion.getValorInteres()));
            }return getTablaAmortizacion();
        }

    }



    private static void calcularInteresEquivalente(){
        if(tablaAmortizacion == null){
            return;
        }
        if (tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.FRANCES)){

            tablaAmortizacion.setInteresEquivalente(tablaAmortizacion.getTasaInteres()
                    .divide(tablaAmortizacion.getNumeroPagosPorAnio(),NUMERO_DECIMALES, RoundingMode.DOWN)
                    .divide(BigDecimal.valueOf(Double.parseDouble("100")),NUMERO_DECIMALES,RoundingMode.HALF_UP)
            );
        }else if (tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.ALEMAN)){
            //TO DO
        }

    }
    private static void correccionPorRedondeoDecimal(){

        if (tablaAmortizacion == null || tablaAmortizacion.getCuotas() == null || tablaAmortizacion.getCuotas().isEmpty()) {
            return;
        }

        if (tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.FRANCES)) {
            List<TablaAmortizacionDetDTO> cuotas = tablaAmortizacion.getCuotas();
            TablaAmortizacionDetDTO ultimaCuota = cuotas.get(cuotas.size() - 1);

            BigDecimal cuotaAPagar = ultimaCuota.getCuotaAPagar();
            BigDecimal interesUltimaCuota = ultimaCuota.getInteres();



            //errorRedondeo = ultimaCuota.getCapitalVivo();


            ultimaCuota.setCapitalAmortizado(ultimaCuota.getCapitalAmortizado().add(errorRedondeo));
            ultimaCuota.setCapitalVivo(ultimaCuota.getCapitalAmortizado());
            ultimaCuota.setInteres(interesUltimaCuota.add(errorRedondeoInteres));
            ultimaCuota.setCuotaAPagar(ultimaCuota.getCapitalAmortizado().add(ultimaCuota.getInteres()));

        }else if (tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.ALEMAN)){
            //TO DO
        }
    }
    private static void calcularValorInteres() {
        if (tablaAmortizacion == null) {
            return;
        }
        if (tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.FRANCES)) {
            BigDecimal valorPrestamo = tablaAmortizacion.getValorPrestamo();
            BigDecimal interesEquivalente = tablaAmortizacion.getInteresEquivalente();
            BigDecimal numeroCuotas = tablaAmortizacion.getNumeroCuotas();
            BigDecimal primeraCuota = tablaAmortizacion.getValorPrestamo().divide(tablaAmortizacion.getNumeroCuotas(),2,RoundingMode.HALF_UP);
            if(tablaAmortizacion.getInteresEquivalente().compareTo(BigDecimal.ZERO) > 0){
                BigDecimal aux  = BigDecimal.ONE.add(tablaAmortizacion.getInteresEquivalente())
                        .pow(tablaAmortizacion.getNumeroCuotas().intValue());
                BigDecimal aux2  = BigDecimal.ONE.add(tablaAmortizacion.getInteresEquivalente())
                        .pow(tablaAmortizacion.getNumeroCuotas().intValue())
                        .subtract(BigDecimal.ONE);
                primeraCuota = aux.multiply(tablaAmortizacion.getInteresEquivalente()).multiply(tablaAmortizacion.getValorPrestamo())
                                .divide(aux2,NUMERO_DECIMALES,RoundingMode.HALF_UP);

            }


            BigDecimal valorInteresTotal = BigDecimal.ZERO;
            BigDecimal saldoRestante = valorPrestamo;

            for (int cuota = 1; cuota <= numeroCuotas.intValue(); cuota++) {
                BigDecimal valorInteres = saldoRestante.multiply(interesEquivalente)
                        .setScale(NUMERO_DECIMALES, RoundingMode.HALF_UP);

                BigDecimal valorCapital = primeraCuota.subtract(valorInteres);
                saldoRestante = saldoRestante.subtract(valorCapital);

                // Agregar el interés calculado para esta cuota al valor total de intereses
                valorInteresTotal = valorInteresTotal.add(valorInteres);

                // Recalcular el valor de la cuota, tomando en cuenta el interés calculado
                primeraCuota = valorCapital.add(valorInteres);
            }
            tablaAmortizacion.setValorPrimeraCuota(primeraCuota.setScale(2, RoundingMode.HALF_UP));
            tablaAmortizacion.setValorInteres(valorInteresTotal.setScale(2, RoundingMode.HALF_UP));
        } else if (tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.ALEMAN)) {
            // TO DO
        }
    }
    private static void calcularTablaDeAmortizacion(){
        if (tablaAmortizacion == null) {
            return;
        }
        if(tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.FRANCES)) {
            BigDecimal valorPrestamo = tablaAmortizacion.getValorPrestamo();
            BigDecimal interesEquivalente = tablaAmortizacion.getInteresEquivalente();
            BigDecimal numeroCuotas = tablaAmortizacion.getNumeroCuotas();
            BigDecimal valorCuota = tablaAmortizacion.getValorPrimeraCuota();

            List<TablaAmortizacionDetDTO> cuotas = new ArrayList<>();

            BigDecimal saldoRestante = valorPrestamo;
            BigDecimal errorAmortizado = BigDecimal.ZERO;
            BigDecimal errorInteres = BigDecimal.ZERO;

            for (int cuota = 1; cuota <= numeroCuotas.intValue(); cuota++) {
                BigDecimal valorInteres = saldoRestante.multiply(interesEquivalente)
                        .setScale(NUMERO_DECIMALES, RoundingMode.HALF_UP);


                BigDecimal valorCapital = valorCuota.subtract(valorInteres);


                TablaAmortizacionDetDTO nuevaCuota = TablaAmortizacionDetDTO.builder()
                        .numeroCuota((long) cuota)
                        .cuotaAPagar(valorCuota.setScale(2, RoundingMode.HALF_UP))
                        .interes(valorInteres.setScale(2, RoundingMode.HALF_UP))
                        .capitalAmortizado(valorCapital.setScale(2, RoundingMode.HALF_UP))
                        .capitalVivo(saldoRestante.setScale(2, RoundingMode.HALF_UP))
                        .build();
                saldoRestante = saldoRestante.subtract(valorCapital);
                errorAmortizado = errorAmortizado.add(valorCapital.setScale(2, RoundingMode.HALF_UP));
                errorInteres = errorInteres.add(valorInteres.setScale(2, RoundingMode.HALF_UP));
                cuotas.add(nuevaCuota);
            }
            errorRedondeo = valorPrestamo.subtract(errorAmortizado);
            errorRedondeoInteres = tablaAmortizacion.getValorInteres().subtract(errorInteres);
            tablaAmortizacion.setCuotas(cuotas);
        }else if (tablaAmortizacion.getTipo().equals(EnumTipoTablaAmortizacion.ALEMAN)){
            //TO DO
        }
    }


    /**
     * Calcular el interes en funcion del promedio de la venta por producto
     * @param totalVenta
     * @param totalInteres
     * @param valorProducto
     * @return
     */

    public static BigDecimal calcularInteresPorProducto(BigDecimal totalVenta, BigDecimal totalInteres, BigDecimal valorProducto){

        if(valorProducto == null || valorProducto.equals(BigDecimal.ZERO)){
            return BigDecimal.ZERO;
        }
        if(totalVenta == null || totalVenta.equals(BigDecimal.ZERO)){
            return BigDecimal.ZERO;
        }
        if(totalInteres == null || totalInteres.equals(BigDecimal.ZERO)){
            return BigDecimal.ZERO;
        }
        BigDecimal promedioProducto = valorProducto.divide(totalVenta,NUMERO_DECIMALES, RoundingMode.HALF_UP);

        return totalInteres.multiply(promedioProducto).setScale(BigDecimal.ROUND_CEILING,RoundingMode.HALF_UP);

    }

    public static String imprimir() {
        System.out.println("Error de redondeo" + errorRedondeo);
        return tablaAmortizacion.toString();
    }
}
