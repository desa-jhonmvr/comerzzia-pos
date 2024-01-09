/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.credito;

import com.comerzzia.jpos.dto.credito.tabla.amortizacion.EnumTipoTablaAmortizacion;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.mediospagos.VencimientoBean;
import com.comerzzia.jpos.pinpad.PinPad;
import com.comerzzia.jpos.servicios.credito.tabla.amortizacion.TablaAmortizacionService;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class PlanPagoCredito implements Comparable<PlanPagoCredito> {

    protected static final BigDecimal CTE_100 = new BigDecimal(100);
    private Long idPlan;
    private String plan;
    private VencimientoBean vencimiento;
    private BigDecimal descuento;
    private BigDecimal descuentoCalculado;
    private BigDecimal numCuotas;
    private BigDecimal cuota;
    private BigDecimal total;
    private BigDecimal aPagar;
    private BigDecimal ahorro;
    private BigDecimal pisoMinimo;
    private BigDecimal pisoMaximo;
    private BigDecimal porcentajeInteres;
    private BigDecimal importeInteres;
    private int meses; // 0 si es corriente, numero de Cuotas en otro caso
    
    private BigDecimal aPagarMasInteres;
    private BigDecimal cuotaMasInteres;
    private BigDecimal ahorroMasInteres;
    private BigDecimal totalGarantiaExtendida;
    protected MedioPagoBean medioPagoActivo;

    public PlanPagoCredito(VencimientoBean vencimiento, Pago pago, boolean afiliadoPromo) {
        idPlan = vencimiento.getIdMedioPagoVencimiento();
        plan = vencimiento.getDesVencimiento();
        descuento = vencimiento.getDescuento(pago, afiliadoPromo);
        medioPagoActivo = pago.getMedioPagoActivo();

        if(pago.getTotal()!=null){
            descuento = pago.calculaDescuento(descuento);
        }
        totalGarantiaExtendida = pago.restaGarantiaExtendida();
        porcentajeInteres = vencimiento.getInteres(pago);
        if(PinPad.getInstance().isAutomatico() && pago instanceof PagoCredito && ((PagoCredito)pago).isSoloPagoCredito()){
            porcentajeInteres = BigDecimal.ZERO;
        }
        numCuotas = new BigDecimal(vencimiento.getNumeroVencimientos());
        pisoMaximo = vencimiento.getPisoMaximo();
        pisoMinimo = vencimiento.getPisoMinimo(pago);
        meses = vencimiento.getNumeroVencimientos();
        if (vencimiento.isCorriente()){
            meses = 0;
        }
        this.vencimiento = vencimiento;
    } 
    
    
    public PlanPagoCredito() {
    }
    
    public void recalcularFromTotal(BigDecimal total, BigDecimal valorRetencion) {
        if(valorRetencion == null || valorRetencion.compareTo(BigDecimal.ZERO) == 0){
            this.total = total;
            ahorro = total.multiply(descuento).divide(CTE_100, 2, BigDecimal.ROUND_HALF_UP);
            aPagar = total.subtract(ahorro);
            ahorro = total.subtract(aPagar); // al ahorro le quitamos lo que se paga por intereses
            cuota = aPagar.divide(numCuotas, 2, BigDecimal.ROUND_HALF_UP);
            System.out.println("recalcularFromTotal cuota1 " + cuota);
            System.out.println("recalcularFromTotal " + cuota);
            // con intereses
           
            if(StringUtils.isNotEmpty(getVencimiento().getCalculaInteres())
                    && getVencimiento().getCalculaInteres().equals(TablaAmortizacionService.APLICA_INTERES_SI)){
                cuotaMasInteres = TablaAmortizacionService.init(aPagar,numCuotas,porcentajeInteres, getVencimiento().getTipoAmortizacion() ).getValorPrimeraCuota();
                importeInteres = TablaAmortizacionService.getTablaAmortizacion().getValorInteres();
            }else{
                aPagarMasInteres = Numero.masPorcentajeR(aPagar, porcentajeInteres);
                cuotaMasInteres = aPagarMasInteres.divide(numCuotas, 2, BigDecimal.ROUND_HALF_UP);
                importeInteres = Numero.porcentajeR(aPagar, porcentajeInteres);
            }
             aPagarMasInteres = aPagar.add(importeInteres); // con intereses
            //
            System.out.println("recalcularFromTotal cuotaMasInteres " + cuotaMasInteres);
            ahorroMasInteres = total.subtract(aPagarMasInteres);
            System.out.println("recalcularFromTotal total " + total);
            System.out.println("recalcularFromTotal ahorro " + ahorro);
            System.out.println("recalcularFromTotal aPagar " + aPagar);
            // con intereses
        }else{
            total = total.add(valorRetencion);
            this.total = total;
            ahorro = total.multiply(descuento).divide(CTE_100, 2, BigDecimal.ROUND_HALF_UP);
            aPagar = total.subtract(valorRetencion).subtract(ahorro);

            ahorro = ahorro.subtract(valorRetencion); // al ahorro le quitamos lo que se paga por intereses
            cuota = aPagar.divide(numCuotas, 2, BigDecimal.ROUND_HALF_UP);
            System.out.println("recalcularFromTotal cuota1 " + cuota);
            System.out.println("recalcularFromTotalR descuento " + descuento);

             System.out.println("recalcularFromTotalR total " + total);
             System.out.println("recalcularFromTotalR ahorro " + ahorro);
             System.out.println("recalcularFromTotalR aPagar " + aPagar);
             System.out.println("recalcularFromTotalR valorRetencion " + valorRetencion);
            // con intereses
            
            if(StringUtils.isNotEmpty(getVencimiento().getCalculaInteres())
                    && getVencimiento().getCalculaInteres().equals(TablaAmortizacionService.APLICA_INTERES_SI)){
                cuotaMasInteres = TablaAmortizacionService.init(aPagar,numCuotas,porcentajeInteres, getVencimiento().getTipoAmortizacion() ).getValorPrimeraCuota();
                importeInteres = TablaAmortizacionService.getTablaAmortizacion().getValorInteres();
            }else{
                aPagarMasInteres = Numero.masPorcentajeR(aPagar, porcentajeInteres);
                cuotaMasInteres = aPagarMasInteres.divide(numCuotas, 2, BigDecimal.ROUND_HALF_UP);
                importeInteres = Numero.porcentajeR(aPagar, porcentajeInteres);
            }
            aPagarMasInteres = aPagar.add(importeInteres); // con intereses
            System.out.println("recalcularFromTotalR cuotaMasInteres " + cuotaMasInteres);

            ahorroMasInteres = total.subtract(aPagarMasInteres).subtract(valorRetencion);
            System.out.println("recalcularFromTotalR ahorroMasInteres " + ahorroMasInteres);

        }
    }

    protected void recalcularFromCuota(BigDecimal cuotaConInteres) {
        cuotaMasInteres = cuotaConInteres;
        aPagarMasInteres = cuotaMasInteres.multiply(numCuotas); 
        
        aPagar = Numero.getAntesDePorcentajeR(aPagarMasInteres, porcentajeInteres);
        cuota = aPagar.divide(numCuotas, 2, BigDecimal.ROUND_HALF_UP);
        System.out.println("recalcularFromCuota " + cuota);
        importeInteres = aPagarMasInteres.subtract(aPagar);
        if(cuota.compareTo(totalGarantiaExtendida) > 0){
            total = Numero.getAntesDePorcentajeR(aPagar.subtract(totalGarantiaExtendida), descuento.negate());
            total = total.add(totalGarantiaExtendida);
             System.out.println("recalcularFromCuota ahorro " + ahorro);
            ahorro = total.subtract(aPagar);
            descuento = ahorro.multiply(CTE_100).divide(total,  2, BigDecimal.ROUND_HALF_UP);
            ahorroMasInteres = total.subtract(aPagarMasInteres);
        }else{
            total = aPagar;
           // total = total.add(totalGarantiaExtendida);
             System.out.println("recalcularFromCuota ahorro " + ahorro);
            ahorro = BigDecimal.ZERO;
            descuento = BigDecimal.ZERO;
            ahorroMasInteres = total.subtract(aPagarMasInteres);
        }
    }

    protected void recalcularFromAPagar(BigDecimal aPagarConInteres) {
        aPagarMasInteres = aPagarConInteres;
        cuotaMasInteres = aPagarMasInteres.divide(numCuotas, 2, BigDecimal.ROUND_HALF_UP);
        
        aPagar = Numero.getAntesDePorcentajeR(aPagarMasInteres, porcentajeInteres);
        cuota = aPagar.divide(numCuotas, 2, BigDecimal.ROUND_HALF_UP);
        importeInteres = aPagarMasInteres.subtract(aPagar);
        if(cuota.compareTo(totalGarantiaExtendida) > 0){
            System.out.println("recalcularFromAPagar " + cuota);
            total = Numero.getAntesDePorcentajeR(aPagar.subtract(totalGarantiaExtendida), descuento.negate());
            total = total.add(totalGarantiaExtendida);
            System.out.println("recalcularFromAPagar ahorro " + ahorro);
            ahorro = total.subtract(aPagar);
            descuento = ahorro.multiply(CTE_100).divide(total,  2, BigDecimal.ROUND_HALF_UP);
            ahorroMasInteres = total.subtract(aPagarMasInteres);
        }else{
            total = aPagar;
           // total = total.add(totalGarantiaExtendida);
             System.out.println("recalcularFromCuota ahorro " + ahorro);
            ahorro = BigDecimal.ZERO;
            descuento = BigDecimal.ZERO;
            ahorroMasInteres = total.subtract(aPagarMasInteres);
        }
    }

    protected void recalcularFromAPagarSinIntereses(BigDecimal aPagarSinInteres) {
        aPagar = aPagarSinInteres;
        aPagarMasInteres = Numero.masPorcentajeR(aPagar, porcentajeInteres);
        cuotaMasInteres = aPagarMasInteres.divide(numCuotas, 2, BigDecimal.ROUND_HALF_UP);
        
        cuota = aPagar.divide(numCuotas, 2, BigDecimal.ROUND_HALF_UP);
        importeInteres = aPagarMasInteres.subtract(aPagar);
         System.out.println("recalcularFromAPagarSinIntereses " + cuota);
        total = Numero.getAntesDePorcentajeR(aPagar.subtract(totalGarantiaExtendida), descuento.negate());
        total = total.add(totalGarantiaExtendida);
        System.out.println("recalcularFromAPagarSinIntereses ahorro " + ahorro);
        System.out.println("recalcularFromAPagarSinIntereses aPagar " + aPagar);
        ahorro = total.subtract(aPagar);
        descuento = ahorro.multiply(CTE_100).divide(total,  2, BigDecimal.ROUND_HALF_UP);
        ahorroMasInteres = total.subtract(aPagarMasInteres);
    }

    /** Comprueba que el total que se pagaría con este plan está en el intervalo
     * del piso máximo y mínimo, además de que no supera el saldo inicial de la cuenta.
     * @param saldoInicial
     * @return 
     */
    public boolean isPisoValido(BigDecimal saldoInicial, boolean filtrarConsumoMinimo) {
        if (filtrarConsumoMinimo && pisoMinimo.compareTo(total) > 0) {
            return false;
        }
        // si tiene pisoMaximo (mayor que 0) comprobamos el piso máximo
        if (pisoMaximo.compareTo(BigDecimal.ZERO) > 0 && pisoMaximo.compareTo(total) <= 0) {
            return false;
        }
        if (total.compareTo(saldoInicial) > 0) {
            return false;
        }
        return true;
    }

    public BigDecimal getCuota() {
        return cuota;
    }
    
    public void setCuota(BigDecimal cuota) {
         this.cuota= cuota;
    }

    public BigDecimal getaPagarMasInteres() {
        return aPagarMasInteres;
    }

    public BigDecimal getCuotaMasInteres() {
        return cuotaMasInteres;
    }

    public BigDecimal getAhorroConInteres(){
        return ahorroMasInteres;
    }
    
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    
    public BigDecimal getDescuento() {
        return descuento;
    }

    public int getNumCuotas() {
        if(numCuotas == null){
            return 1;
        }else{
            return numCuotas.intValue();
        }
    }

    public int getMeses() {
        return meses;
    }

    public String getPlan() {
        return plan;
    }
    
    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public BigDecimal getTotal() {
        return total;
    }

    public void setaPagar(BigDecimal aPagar) {
        this.aPagar = aPagar;
    }
    
    public BigDecimal getaPagar() {
        return aPagar;
    }

    public void setAhorro(BigDecimal ahorro) {
        this.ahorro = ahorro;
    }
    
    public BigDecimal getAhorro() {
        return ahorro;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public VencimientoBean getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(VencimientoBean vencimiento) {
        this.vencimiento = vencimiento;
    }

    public BigDecimal getPorcentajeInteres() {
        return porcentajeInteres;
    }
    public BigDecimal getImporteInteres() {
        return importeInteres;
    }
    
    public void setImporteInteres(BigDecimal importeInteres) {
        this.importeInteres = importeInteres;
    }

    @Override
    public int compareTo(PlanPagoCredito t) {
        if (meses > t.meses){
            return 1;
        }
        else if (meses < t.meses){
            return -1;
        }
        return 0;
    }

    public void setNumCuotas(BigDecimal numCuotas) {
        this.numCuotas = numCuotas;
    }

    public BigDecimal getPisoMinimo() {
        return pisoMinimo;
    }

    public void setPisoMinimo(BigDecimal pisoMinimo) {
        this.pisoMinimo = pisoMinimo;
    }

    public BigDecimal getPisoMaximo() {
        return pisoMaximo;
    }

    public void setPisoMaximo(BigDecimal pisoMaximo) {
        this.pisoMaximo = pisoMaximo;
    }

    public BigDecimal getAhorroMasInteres() {
        return ahorroMasInteres;
    }

    public void setAhorroMasInteres(BigDecimal ahorroMasInteres) {
        this.ahorroMasInteres = ahorroMasInteres;
    }

    public BigDecimal getTotalGarantiaExtendida() {
        return totalGarantiaExtendida;
    }

    public void setTotalGarantiaExtendida(BigDecimal totalGarantiaExtendida) {
        this.totalGarantiaExtendida = totalGarantiaExtendida;
    }

    public BigDecimal getDescuentoCalculado() {
        return descuentoCalculado;
    }

    public void setDescuentoCalculado(BigDecimal descuentoCalculado) {
        this.descuentoCalculado = descuentoCalculado;
    }

}
