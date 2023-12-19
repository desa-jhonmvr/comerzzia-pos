/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.especiales;

import com.comerzzia.jpos.entity.db.Bono;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author MGRI
 */
public class PagoBono extends Pago {

    private BigDecimal saldoBono;
    private BigDecimal totalBono;
    private String uidBono;
    private Bono bono;
    private BigDecimal porcentajeDescuento;

    public PagoBono(PagosTicket pagos, Cliente cliente) {
        super(pagos, cliente);
    }

    public void crearPagoBono(MedioPagoBean mp, Bono bono) {
        saldoBono = bono.getImporte();
        totalBono = bono.getImporte();
        entregado = saldoBono;
        medioPagoActivo = mp;
        pagoActivo = PAGO_OTROS;
        uidBono = bono.getBonoPK().getCodalm() + bono.getBonoPK().getIdBono();
        this.bono = bono;
        informacionExtra1 = bono.getBonoPK().getCodalm() + "-" + bono.getBonoPK().getIdBono();
                
        porcentajeDescuento = bono.getDescuento();   
         if (isModoSinDescuentos()){
           porcentajeDescuento = BigDecimal.ZERO;
        }
    
    }
    
    public BigDecimal getSaldoBono() {
        return saldoBono;
    }

    public void setSaldoBono(BigDecimal saldoNotaCredito) {
        this.saldoBono = saldoNotaCredito;
    }

    public BigDecimal getTotalBono() {
        return totalBono;
    }

    public void setTotalBono(BigDecimal totalNotaCredito) {
        this.totalBono = totalNotaCredito;
    }

    public String getUidBono() {
        return uidBono;
    }

    public void setUidBono(String uidNotaCredito) {
        this.uidBono = uidNotaCredito;
    }

    @Override
    public void recalcularFromTotal(String total) throws PagoInvalidException {
        try {
            resetear(new BigDecimal(total).setScale(2, RoundingMode.HALF_UP));
        }
        catch (NumberFormatException e) {
            resetear(saldoInicial);
            throw new PagoInvalidException("El valor indicado no es un número correcto.");
        }
    }

    @Override
    public void recalcularFromUstedPaga(String ustedPaga) throws PagoInvalidException {
        try {
            this.ustedPaga = new BigDecimal(ustedPaga).setScale(2, RoundingMode.HALF_UP);
        BigDecimal dto = BigDecimal.ZERO;
        
            if (porcentajeDescuento!=null){
                dto = porcentajeDescuento.setScale(2, RoundingMode.HALF_UP);
            }
            else{
                dto = medioPagoActivo.getVencimientoDefault().getDescuento(this,Sesion.isAfiliadoPromo()); 
            }
            if (dto.compareTo(BigDecimal.ZERO) > 0) {
                //this.total = this.ustedPaga.divide(BigDecimal.ONE.subtract(BigDecimal.ONE.divide(dto,4, RoundingMode.HALF_UP)), RoundingMode.HALF_UP);
                this.total = Numero.calculaTotalDesdePorcentaje(this.ustedPaga, dto);
            }
            else {
                this.total = this.ustedPaga;
            }
            descuento = dto;
            // si el saldo que tengo que pagar es menor, calculo a partir del saldo
            if (saldoInicial.compareTo(this.total) < 0) {
                resetear(saldoInicial);
                entregado = saldoBono;
            }
        }
        catch (NumberFormatException e) {
            resetear(saldoInicial);
            throw new PagoInvalidException("El valor indicado no es un número correcto.");
        }
    }

    @Override
    public void validar() throws PagoInvalidException {
    }

    @Override
    public void resetear(BigDecimal saldo) {
        this.ustedPaga = saldoBono;
        BigDecimal dto = BigDecimal.ZERO;
        
        if (porcentajeDescuento!=null){
                dto = porcentajeDescuento.setScale(2, RoundingMode.HALF_UP);
        }
        else{
                dto = medioPagoActivo.getVencimientoDefault().getDescuento(this,Sesion.isAfiliadoPromo()); 
        }
        
        if (dto.compareTo(BigDecimal.ZERO) > 0) {
            this.total = Numero.calculaTotalDesdePorcentaje(this.ustedPaga, dto);
        }
        else {
            this.total = ustedPaga;
        }
        descuento = dto;
        // si el saldo que tengo que pagar es menor, calculo a partir del saldo
        if (saldo.compareTo(this.total) < 0) {            
            super.saldoInicial = saldo;                      
            super.total = saldo;
            super.establecerDescuento(dto);
            super.entregado = saldoBono;  
        }
    }

    public Bono getBono() {
        return bono;
    }

    public void setBono(Bono bono) {
        this.bono = bono;
    }
}
