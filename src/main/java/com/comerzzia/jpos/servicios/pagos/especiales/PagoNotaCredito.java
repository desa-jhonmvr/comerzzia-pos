/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.especiales;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.devoluciones.NotaCreditoException;
import com.comerzzia.jpos.servicios.devoluciones.NotaCreditoXMLServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;


/**
 *
 * @author Admin
 */
public class PagoNotaCredito extends Pago {

    private static final Logger log = Logger.getMLogger(PagoNotaCredito.class);

    private BigDecimal saldoNotaCredito;
    private BigDecimal totalNotaCredito;
    private BigDecimal porcentajeDtoNotaCredito;
    private String uidNotaCredito;
    private String idNotaCreditoCompleto;
    private Date fechaCaducidad;
    private BigDecimal compensacion;

    public PagoNotaCredito(PagosTicket pagos, Cliente cliente, TotalesXML totales) {
        // no pasamos pagos para que no aplique descuentos de bonos
        super(null, cliente, totales, null);
    }

    public void crearPagoNotaCredito(MedioPagoBean mp, NotasCredito nota) {
        saldoNotaCredito = nota.getSaldo();
        totalNotaCredito = nota.getTotal();
        fechaCaducidad = nota.getFechaValidez();
        setPorcentajeDtoNotaCredito(nota.getDescuento());
        if (isModoSinDescuentos()){
            setPorcentajeDtoNotaCredito(BigDecimal.ZERO);
        }
        if (getPorcentajeDtoNotaCredito() != null){
            descuento = getPorcentajeDtoNotaCredito(); 
        }
        medioPagoActivo = mp;
        pagoActivo = PAGO_OTROS;
        uidNotaCredito = nota.getUidNotaCredito();
        idNotaCreditoCompleto = nota.getIdNotaCreditoCompleto();
        try{
            compensacion = NotaCreditoXMLServices.dameCompensacion(nota.getNotaCredito());
        } catch (NotaCreditoException e){
            log.error("crearPagoNotaCredito() - Error obteniendo la compensación de la nota de créditO: "+e.getMessage(),e);
            compensacion = BigDecimal.ZERO;
        }
        entregado = getSaldoNotaCredito();
    }

    public BigDecimal getSaldoNotaCredito() {
        return saldoNotaCredito.subtract(compensacion);
    }

    public void setSaldoNotaCredito(BigDecimal saldoNotaCredito) {
        this.saldoNotaCredito = saldoNotaCredito;
    }

    public BigDecimal getTotalNotaCredito() {
        return totalNotaCredito;
    }

    public void setTotalNotaCredito(BigDecimal totalNotaCredito) {
        this.totalNotaCredito = totalNotaCredito;
    }

    public String getUidNotaCredito() {
        return uidNotaCredito;
    }

    public String getIdNotaCreditoCompleto() {
        return idNotaCreditoCompleto;
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
            
            if (porcentajeDtoNotaCredito!=null){
                dto = porcentajeDtoNotaCredito.setScale(2, RoundingMode.HALF_UP);
            }
            else{
                dto = medioPagoActivo.getVencimientoDefault().getDescuento(this,Sesion.isAfiliadoPromo());
            }
            
            if (dto.compareTo(BigDecimal.ZERO) > 0) {
                this.total = Numero.calculaTotalDesdePorcentaje(this.ustedPaga, dto);
            }
            else {
                this.total = this.ustedPaga;
            }
            descuento = dto; 
            // si el saldo que tengo que pagar es menor, calculo a partir del saldo
            if (saldoInicial.compareTo(this.total) < 0) {
                resetear(saldoInicial);
                entregado = getSaldoNotaCredito();
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
        this.ustedPaga = getSaldoNotaCredito();
        BigDecimal dto = BigDecimal.ZERO;
        if (porcentajeDtoNotaCredito!=null){
            dto = porcentajeDtoNotaCredito.setScale(2, RoundingMode.HALF_UP);
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
            super.resetear(saldo);
            entregado = getSaldoNotaCredito();
        }
    }

    @Override
    public void establecerDescuento(BigDecimal descuento) {
        if (porcentajeDtoNotaCredito!=null){
            descuento = porcentajeDtoNotaCredito.setScale(2, RoundingMode.HALF_UP);
        }        
        this.descuento = descuento;
        calculaDescuento(descuento);
        //entregado = ustedPaga;
        ustedPaga = total.subtract(total.multiply(descuentoCalculado).divide(Numero.CIEN)).setScale(2, BigDecimal.ROUND_HALF_UP);
        if(ustedPaga.compareTo(getSaldoNotaCredito()) < 0){
            entregado = ustedPaga;
        }else{
            ustedPaga = getSaldoNotaCredito();
        }
    }
    

    public BigDecimal getPorcentajeDtoNotaCredito() {
        return porcentajeDtoNotaCredito;
    }

    public void setPorcentajeDtoNotaCredito(BigDecimal porcentajeDtoNotaCredito) {
        this.porcentajeDtoNotaCredito = porcentajeDtoNotaCredito;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public BigDecimal getCompensacion() {
        return compensacion;
    }

    public void setCompensacion(BigDecimal compensacion) {
        this.compensacion = compensacion;
    }
}
