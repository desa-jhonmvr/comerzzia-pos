
package com.comerzzia.jpos.servicios.tickets;

/**
 *
 * @author MGRI
 */
public class ImporteInvalidoException extends Exception{
    public ImporteInvalidoException() {
        super();
    }

    public ImporteInvalidoException(String msg) {
        super(msg);
    }

    public ImporteInvalidoException(String msg, Throwable e) {
        super(msg, e);
    }

    public ImporteInvalidoException(Throwable e) {
        super(e);
    }
}
