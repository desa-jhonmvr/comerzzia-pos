package com.comerzzia.jpos.persistencia.mediospagos;

import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.pagos.Pago;
import java.math.BigDecimal;
import java.util.Map;



public class VencimientoBean  implements Comparable<VencimientoBean> {

    /**
     * 
     */
    private static final long serialVersionUID = 5462763665265715552L;
    private Long idMedioPagoVencimiento;
    private String codMedioPago;
    private String desVencimiento;
    private Integer numeroVencimientos;
    private BigDecimal pisoMaximo;
    private String tipoCredito;
    private boolean placeToPay;
    private final MediosPago mediosPago;
    


    public VencimientoBean(MediosPago mediosPago) {
        this.mediosPago = mediosPago;
    }

    // devuelve el descuento que se debe aplicar en función de si es para un afiliado, para un empleado
    public BigDecimal getDescuento(Pago pago, boolean afiliadoPromo) {
        if (pago.isModoSinDescuentos()){
            return BigDecimal.ZERO;
        }
        if (pago.getTipoCliente() == null || pago.getTipoCliente() == 0L){
            // si no tenemos tipo de cliente, devolvemos 0
            return BigDecimal.ZERO;
        }
        Map<Long, DescuentoBean> descuentoTipoCliente = mediosPago.getDescuentosCliente().get(pago.getTipoCliente());
        if (descuentoTipoCliente == null){
            // si no hay descuentos definidos para el tipo de cliente, devolvemos 0
            return BigDecimal.ZERO;
        }
        DescuentoBean descuento = descuentoTipoCliente.get(idMedioPagoVencimiento);
        if (descuento == null){
            return BigDecimal.ZERO;
        }
        return descuento.getDescuento(pago.isClienteAfiliado(), afiliadoPromo);
    }
    
    public BigDecimal getInteres(Pago pago) {
        return getInteres(pago.getTipoCliente(), pago.isClienteAfiliado());
    }

    public BigDecimal getInteres(Long tipoCliente, boolean afiliado) {
        if (tipoCliente == null || tipoCliente == 0){
            // si no tenemos tipo de cliente, devolvemos 0
            return BigDecimal.ZERO;
        }
        Map<Long, DescuentoBean> descuentoTipoCliente = mediosPago.getDescuentosCliente().get(tipoCliente);
        if (descuentoTipoCliente == null){
            // si no hay descuentos definidos para el tipo de cliente, devolvemos 0
            return BigDecimal.ZERO;
        }
        DescuentoBean descuento = descuentoTipoCliente.get(idMedioPagoVencimiento);
        if (descuento == null){
            return BigDecimal.ZERO;
        }
        return descuento.getInteres(afiliado);
    }

    public BigDecimal getPisoMinimo(Pago pago) {
        if (pago.getTipoCliente() == null || pago.getTipoCliente() == 0L){
            // si no tenemos tipo de cliente, devolvemos 0
            return BigDecimal.ZERO;
        }
        Map<Long, DescuentoBean> descuentoTipoCliente = mediosPago.getDescuentosCliente().get(pago.getTipoCliente());
        if (descuentoTipoCliente == null){
            // si no hay descuentos definidos para el tipo de cliente, devolvemos 0
            return BigDecimal.ZERO;
        }
        DescuentoBean descuento = descuentoTipoCliente.get(idMedioPagoVencimiento);
        if (descuento == null){
            return BigDecimal.ZERO;
        }
        return descuento.getPisoMinimo();
    }

    public String getCodMedioPago() {
        return codMedioPago;
    }

    public void setCodMedioPago(String codMedioPago) {
        this.codMedioPago = codMedioPago;
    }

    public String getDesVencimiento() {
        return desVencimiento;
    }

    public void setDesVencimiento(String desVencimiento) {
        this.desVencimiento = desVencimiento;
    }

    public Integer getNumeroVencimientos() {
        return numeroVencimientos;
    }

    public void setNumeroVencimientos(Integer numeroVencimientos) {
        this.numeroVencimientos = numeroVencimientos;
    }

    public Long getIdMedioPagoVencimiento() {
        return idMedioPagoVencimiento;
    }

    public void setIdMedioPagoVencimiento(Long idMedioPagoVencimiento) {
        this.idMedioPagoVencimiento = idMedioPagoVencimiento;
    }

    public String getTipoCredito() {
        if (tipoCredito!=null){
            return tipoCredito.trim();
        }
        return tipoCredito;
    }

    public void setTipoCredito(String tipoCredito) {
        this.tipoCredito = tipoCredito;
    }

    public BigDecimal getPisoMaximo() {
        return pisoMaximo;
    }

    public void setPisoMaximo(BigDecimal pisoMaximo) {
        this.pisoMaximo = pisoMaximo;
    }

    public boolean isCorriente() {
        return numeroVencimientos == 1;
    }

    public boolean isPlaceToPay() {
        return placeToPay;
    }

    public void setPlaceToPay(boolean placeToPay) {
        this.placeToPay = placeToPay;
    }
    
    

    @Override
    public int compareTo(VencimientoBean t) {
        return numeroVencimientos.compareTo(t.getNumeroVencimientos());
    }


}
