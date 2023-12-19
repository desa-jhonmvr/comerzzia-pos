package com.comerzzia.jpos.servicios.tickets;

import com.comerzzia.util.base.Exception;

/**
 *
 * @author MGRI
 */
public class TicketNuevaLineaException extends Exception {

    private String causa;

    public TicketNuevaLineaException() {
        super();
    }

    public TicketNuevaLineaException(String msg) {
        super(msg);
    }

    public TicketNuevaLineaException(String msg, Throwable e) {
        super(msg, e);
    }

    public TicketNuevaLineaException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

    public TicketNuevaLineaException(String msg, String msgKey) {
        super(msg, msgKey);
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }
}
