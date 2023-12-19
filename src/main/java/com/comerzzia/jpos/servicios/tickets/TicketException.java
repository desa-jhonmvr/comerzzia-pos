package com.comerzzia.jpos.servicios.tickets;

import com.comerzzia.util.base.Exception;

/**
 *
 * @author MGRI
 */
public class TicketException extends Exception {

    private String causa;

    public TicketException() {
        super();
    }

    public TicketException(String msg) {
        super(msg);
    }

    public TicketException(String msg, Throwable e) {
        super(msg, e);
    }

    public TicketException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

    public TicketException(String msg, String msgKey) {
        super(msg, msgKey);
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }
}
