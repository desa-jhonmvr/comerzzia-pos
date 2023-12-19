/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tramas;

import es.mpsistemas.util.log.Logger;

/**
 *
 * @author amos
 */
public class TramaTarjetaGiftCard extends TramaTarjeta {

    /* PR 01234 T MMMM 0123456789
     * PR: PREFIJO
     * 01234: PREFIJO NUMÉRICO >0 <10000
     * T: TIPO: 0
     * MMMM: CODMEDPAG
     * 0123456789: ID GIFTCARD
     */
    private static final Logger log = Logger.getMLogger(TramaTarjetaGiftCard.class);
        
    protected String codMedioPago;
    protected String idGiftCard;

    public TramaTarjetaGiftCard(String trama) throws ParserTramaException {
        super(trama);
        try {
            codMedioPago = trama.substring(8, 12);
            idGiftCard = trama.substring(12, 22);

            if (!tipo.equals("0")) {
                throw new ParserTramaException("Tipo de tarjeta indicado en la trama incorrecto: " + tipo);
            }

        }
        catch (Exception e) {
            throw new ParserTramaException("Error parseando trama de giftcard: " + trama + "\n\t" + e.getMessage(), e);
        }
        finally{
            log.debug("Trama Giftcard recibida: " + trama);
        }
    }
    
    public TramaTarjetaGiftCard(String trama, String codMedioPago) throws ParserTramaException {
        super(trama,codMedioPago);
        try{
            trama = ignorarComienzo(trama);
            this.codMedioPago = codMedioPago;
            idGiftCard = trama.substring(0,16);
        }
        catch (Exception e) {
            throw new ParserTramaException("Error parseando trama de giftcard: " + trama + "\n\t" + e.getMessage(), e);
        }
        finally{
            log.debug("Trama Giftcard recibida: " + trama);
        }        
    }

    public String getCodMedioPago() {
        return codMedioPago;
    }

    public String getIdGiftCard() {
        return idGiftCard;
    }
}
