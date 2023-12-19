/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tramas;

/**
 *
 * @author amos
 */
public class TramaTarjetaIdentificacion extends TramaTarjeta {

    protected Long idUsuario;

    public TramaTarjetaIdentificacion(String trama) throws ParserTramaException {
        super(trama);
        try {
            idUsuario = Long.parseLong(trama.substring(8, 18));

            if (!tipo.equals("1")) {
                throw new ParserTramaException("Tipo de tarjeta indicado en la trama incorrecto: " + tipo);
            }

        }
        catch (Exception e) {
            throw new ParserTramaException("Error parseando trama de tarjeta identificativa: " + trama + "\n\t" + e.getMessage(), e);
        }
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

}
