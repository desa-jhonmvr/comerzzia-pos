/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.credito.supermaxi;

import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PagoBonoSuperMaxiNavidad;
import com.comerzzia.jpos.servicios.tramas.ParserTramaException;
import com.comerzzia.jpos.servicios.tramas.TramaBonoSupermaxi;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;

/**
 *
 * @author Gabriel Simbania
 */
public class CreditoSupermaxiServices {

    protected static Logger log = Logger.getMLogger(CreditoSupermaxiServices.class);

    /**
     * @author Gabriel Simbania
     * @param numeroTarjeta
     * @return
     * @throws com.comerzzia.jpos.servicios.tramas.ParserTramaException
     */
    public static BigDecimal consultaSaldoBonoSupermaxi(String numeroTarjeta) throws ParserTramaException, Exception {

        MedioPagoBean medPag = TarjetaCredito.getBINMedioPagoBanda(numeroTarjeta);
        BigDecimal saldoBono = BigDecimal.ZERO;
        TramaBonoSupermaxi tident;
        String respuesta;
        String trama;
        String saldoBonoSuper;
        String saldoBonoSuperD;

        if (medPag != null && (medPag.isBonoSuperMaxiNavidad() || medPag.isCreditoFilial())) {
            tident = new TramaBonoSupermaxi(numeroTarjeta, medPag.getCodMedioPago());

            PagoBonoSuperMaxiNavidad p = new PagoBonoSuperMaxiNavidad(null, null, null, null);
            if (Variables.getVariable(Variables.ACTIVO_SERVICIO_WEB_SUPERMAXI).equals("S")) {
                respuesta = p.consultarSaldoWeb(tident.getidBonoSuper());

                trama = ignorarComienzoRespuesta(respuesta);

                if (trama != null) {
                    saldoBonoSuper = trama.substring(0, 14);
                    int saldo = Integer.parseInt(saldoBonoSuper);
                    String saldoT = String.valueOf(saldo);
                    saldoBonoSuperD = trama.substring(14, 16);
                    log.debug("Se ha encontrado la Gift Card registrada en BD.");
                    saldoBono = new BigDecimal(saldoT + "." + saldoBonoSuperD);
                } else {
                    throw new Exception("Error al consultar la trama ");
                }
            } else {
                respuesta = p.consultarSaldo(tident.getidBonoSuper());
                trama = ignorarComienzoRespuesta(respuesta);

                if (trama != null) {
                    saldoBonoSuper = trama.substring(0, 8);
                    int saldo = Integer.parseInt(saldoBonoSuper);
                    String saldoT = String.valueOf(saldo);
                    saldoBonoSuperD = trama.substring(8, 10);
                    log.debug("Se ha encontrado la Gift Card registrada en BD.");
                    saldoBono = new BigDecimal(saldoT + "." + saldoBonoSuperD);
                } else {
                    throw new Exception("Error al consultar la trama ");
                }
            }

        } else {
            throw new ValidationException("La tarjeta deslizada no es un bono supermaxi o crédito de temporada");
        }
        return saldoBono;

    }

    private static String ignorarComienzoRespuesta(String trama) {
        //Si la trama empieza con %B, estamos leyendo la banda magnética y lo ignoramos
        if (trama.contains("APROBADO") || trama.contains("MAXIBONO")) {
            trama = trama.substring(151);
        } else {
            return null;
        }

        return trama;
    }

}
