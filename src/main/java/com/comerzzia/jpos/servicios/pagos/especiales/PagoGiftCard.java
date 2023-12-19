/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.especiales;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.persistencia.giftcard.GiftCardBean;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Admin
 */
public class PagoGiftCard extends Pago {

    private BigDecimal saldoGiftCard;
    private BigDecimal porcentajeDtoGiftCard;
    private String idGiftCard;
    private GiftCardBean giftCard;
    private Long idUsoGiftCard;

    public PagoGiftCard(PagosTicket pagos, Cliente cliente) {
        // no pasamos pagos para que no aplique descuentos de bonos
        super(null, cliente);
    }

    public void crearPagoGiftCard(MedioPagoBean mp, GiftCardBean giftCard) {
        setSaldoGiftCard(giftCard.getSaldo());
        idGiftCard = giftCard.getIdGiftCard();
        this.setGiftCard(giftCard);
        
        entregado = saldoGiftCard;

        medioPagoActivo = mp;
        BigDecimal dto = medioPagoActivo.getVencimientoDefault().getDescuento(this,Sesion.isAfiliadoPromo());
        
        informacionExtra1 = giftCard.getIdGiftCard();
        
        descuento = dto;         
        pagoActivo = PAGO_OTROS;

    }

    public BigDecimal getSaldoNotaCredito() {
        return getSaldoGiftCard();
    }

    public void setSaldoNotaCredito(BigDecimal saldoNotaCredito) {
        this.setSaldoGiftCard(saldoNotaCredito);
    }

    public String getIdGiftCard() {
        return idGiftCard;
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

            if (getPorcentajeDtoGiftCard() != null) {
                dto = getPorcentajeDtoGiftCard().setScale(2, RoundingMode.HALF_UP);
            }
            else {
                dto = medioPagoActivo.getVencimientoDefault().getDescuento(this,Sesion.isAfiliadoPromo());
            }

            descuento = dto;
            
            if (dto.compareTo(BigDecimal.ZERO) > 0) {
                this.total = Numero.calculaTotalDesdePorcentaje(this.ustedPaga, dto);
            }
            else {
                this.total = this.ustedPaga;
            }
            
                        
            // si el saldo que tengo que pagar es menor, calculo a partir del saldo
            if (saldoInicial.compareTo(this.total) < 0) {
                resetear(saldoInicial);
                entregado = getSaldoGiftCard();
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
        this.ustedPaga = getSaldoGiftCard();
        BigDecimal dto = BigDecimal.ZERO;
        if (getPorcentajeDtoGiftCard() != null) {
            dto = getPorcentajeDtoGiftCard().setScale(2, RoundingMode.HALF_UP);
        }
        else {
            dto = medioPagoActivo.getVencimientoDefault().getDescuento(this,Sesion.isAfiliadoPromo());
        }
        if (dto.compareTo(BigDecimal.ZERO) > 0) {
            total = Numero.calculaTotalDesdePorcentaje(this.ustedPaga, dto);
        }
        else {
            total = ustedPaga;
        }
        descuento = dto;
        // si el saldo que tengo que pagar es menor, calculo a partir del saldo
        if (saldo.compareTo(this.total) < 0) {
            super.resetear(saldo);
            entregado = getSaldoGiftCard();
        }
    }

    @Override
    public void establecerDescuento(BigDecimal descuento) {
        if (getPorcentajeDtoGiftCard() != null) {
            descuento = getPorcentajeDtoGiftCard().setScale(2, RoundingMode.HALF_UP);
        }
        this.descuento = descuento;
        ustedPaga = total.subtract(total.multiply(descuento).divide(Numero.CIEN)).setScale(2, BigDecimal.ROUND_HALF_UP);
        entregado = ustedPaga;
    }

    public BigDecimal getSaldoGiftCard() {
        return saldoGiftCard;
    }

    public void setSaldoGiftCard(BigDecimal saldoGiftCard) {
        this.saldoGiftCard = saldoGiftCard;
    }

    public BigDecimal getPorcentajeDtoGiftCard() {
        return porcentajeDtoGiftCard;
    }

    public void setPorcentajeDtoGiftCard(BigDecimal porcentajeDtoGiftCard) {
        this.porcentajeDtoGiftCard = porcentajeDtoGiftCard;
    }

    public GiftCardBean getGiftCard() {
        return giftCard;
    }

    public void setGiftCard(GiftCardBean giftCard) {
        this.giftCard = giftCard;
    }

    public Long getIdUsoGiftCard() {
        return idUsoGiftCard;
    }

    public void setIdUsoGiftCard(Long idUsoGiftCard) {
        this.idUsoGiftCard = idUsoGiftCard;
    }
    
}
