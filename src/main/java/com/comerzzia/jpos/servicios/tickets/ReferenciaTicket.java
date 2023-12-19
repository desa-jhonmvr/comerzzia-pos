/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets;

import com.comerzzia.jpos.entity.db.AbonoPlanNovio;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.persistencia.credito.abonos.AbonoCreditoBean;
import com.comerzzia.jpos.persistencia.letras.LetraBean;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoBean;
import java.math.BigInteger;

/**
 *
 * @author amos
 */
public class ReferenciaTicket {

    private static final String REF_TICKET      = "TICKET";
    private static final String REF_LETRA       = "ABONO_LETRA";
    private static final String REF_CREDITO     = "ABONO_CREDITO";
    private static final String REF_RESERVA     = "RESERVA";
    private static final String REF_GIFTCARD    = "GIFTCARD";
    
    private static final String PAGO_TARJETA_FACTURA            = "FACTURA";
    private static final String PAGO_TARJETA_ABONO_RESERVA      = "ABONO_RESERVA";
    private static final String PAGO_TARJETA_ABONO_BABY         = "ABONO_BABY";
    private static final String PAGO_TARJETA_ABONO_PLAN_NOVIO   = "ABONO_PN";
    private static final String PAGO_TARJETA_ABONO_GIFTCARD     = "ABONO_GIFTCARD";
    
    
    private String numDocRefTarjeta;
    private String tipoRefTarjeta;
    private BigInteger idAbonoRefTarjeta;
    private String idReferencia;
    private String tipoReferencia;
    private String numTicket;
    private boolean ventaOnline;

    private ReferenciaTicket() {
    }

    public static ReferenciaTicket getReferenciaFactura(TicketS ticket) {
        ReferenciaTicket referencia = new ReferenciaTicket();
        referencia.numDocRefTarjeta = ticket.getUid_ticket();
        referencia.tipoRefTarjeta = PAGO_TARJETA_FACTURA;
        referencia.idAbonoRefTarjeta = null;
        referencia.idReferencia = ticket.getUid_ticket();
        referencia.tipoReferencia = REF_TICKET;
        referencia.numTicket = ticket.getIdFactura();
        referencia.ventaOnline= ticket.isVentaOnline();
        return referencia;
    }
    
    public static ReferenciaTicket getReferenciaFactura(String uidTicket) {
        ReferenciaTicket referencia = new ReferenciaTicket();
        referencia.numDocRefTarjeta = uidTicket;
        referencia.tipoRefTarjeta = PAGO_TARJETA_FACTURA;
        referencia.idAbonoRefTarjeta = null;
        referencia.idReferencia = uidTicket;
        referencia.tipoReferencia = REF_TICKET;
        return referencia;        
    }
    
    public static ReferenciaTicket getReferenciaLetra(LetraBean letra, LetraCuotaBean cuota) {
        ReferenciaTicket referencia = new ReferenciaTicket();
        referencia.numDocRefTarjeta = letra.getUidLetra();
        referencia.tipoRefTarjeta = REF_LETRA;
        referencia.idAbonoRefTarjeta = new BigInteger(cuota.getCuota().toString());
        referencia.idReferencia = letra.getUidLetra();
        referencia.tipoReferencia = REF_LETRA;
        return referencia;
    }

    public static ReferenciaTicket getReferenciaCredito(AbonoCreditoBean abonoCredito) {
        ReferenciaTicket referencia = new ReferenciaTicket();
        referencia.numDocRefTarjeta = abonoCredito.getUidCreditoPago();
        referencia.tipoRefTarjeta = REF_CREDITO;
        referencia.idAbonoRefTarjeta = null;
        referencia.idReferencia = abonoCredito.getUidCreditoPago();;
        referencia.tipoReferencia = REF_CREDITO;
        return referencia;
    }
    
    public static ReferenciaTicket getReferenciaAbonoPlanNovios(PlanNovioOBJ planNovio, AbonoPlanNovio abono) {
        ReferenciaTicket referencia = new ReferenciaTicket();
        referencia.numDocRefTarjeta = planNovio.getPlan().getPlanNovioPK().getIdPlan().toString();
        referencia.tipoRefTarjeta = PAGO_TARJETA_ABONO_PLAN_NOVIO;
        referencia.idAbonoRefTarjeta = abono.getAbonoPlanNovioPK().getIdAbono();
        referencia.idReferencia = planNovio.getCodPlanAsString();
        referencia.tipoReferencia = REF_RESERVA;
        return referencia;
    }

    public static ReferenciaTicket getReferenciaAbonoReservacion(ReservaBean reservacion, ReservaAbonoBean abono) {
        ReferenciaTicket referencia = new ReferenciaTicket();
        referencia.numDocRefTarjeta = reservacion.getUidReservacion();
        if (reservacion.isTipoBabyShower()){
            referencia.tipoRefTarjeta = ReferenciaTicket.PAGO_TARJETA_ABONO_BABY;
        }
        else{
            referencia.tipoRefTarjeta = ReferenciaTicket.PAGO_TARJETA_ABONO_RESERVA;
        }
        referencia.idAbonoRefTarjeta = new BigInteger(abono.getIdAbono().toString());
        referencia.idReferencia = reservacion.getUidReservacion();
        referencia.tipoReferencia = REF_RESERVA;
        return referencia;
    }

    public static ReferenciaTicket getReferenciaGiftCard(String idGiftCard) {
        ReferenciaTicket referencia = new ReferenciaTicket();
        referencia.numDocRefTarjeta = idGiftCard;
        referencia.tipoRefTarjeta = PAGO_TARJETA_ABONO_GIFTCARD;
        referencia.idAbonoRefTarjeta = null;
        referencia.idReferencia = idGiftCard;
        referencia.tipoReferencia = REF_GIFTCARD;
        return referencia;
    }
    
    
    public BigInteger getIdAbonoRefTarjeta() {
        return idAbonoRefTarjeta;
    }

    public String getIdReferencia() {
        return idReferencia;
    }

    public String getNumDocRefTarjeta() {
        return numDocRefTarjeta;
    }

    public String getTipoReferencia() {
        return tipoReferencia;
    }

    public String getTipoRefTarjeta() {
        return tipoRefTarjeta;
    }

    public String getNumTicket() {
        return numTicket;
    }

    public void setNumTicket(String numTicket) {
        this.numTicket = numTicket;
    }
    
    public Character getTipoMovimientoCaja(){
        Character tipoMovimiento = null;
        if (tipoReferencia.equals(ReferenciaTicket.REF_RESERVA)){
            tipoMovimiento = 'R';
        }
        else if (tipoReferencia.equals(ReferenciaTicket.REF_GIFTCARD)){
            tipoMovimiento = 'G';
        }
        return tipoMovimiento;
    } 
    
    public boolean isPagoTarjetaAbonoPlanNovio(){
        return tipoRefTarjeta.equals(PAGO_TARJETA_ABONO_PLAN_NOVIO);
    }
    public boolean isReferenciaReserva(){
        return tipoReferencia.equals(REF_RESERVA);
    }

    public boolean isVentaOnline() {
        return ventaOnline;
    }

    public void setVentaOnline(boolean ventaOnline) {
        this.ventaOnline = ventaOnline;
    }
    
    
}
