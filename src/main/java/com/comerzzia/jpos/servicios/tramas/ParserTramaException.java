/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tramas;

/**
 *
 * @author amos
 */
public class ParserTramaException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6284695080640115110L;

    public ParserTramaException() {
        super();
    }

    public ParserTramaException(String msg) {
        super(msg);
    }

    public ParserTramaException(String msg, Throwable e) {
        super(msg, e);
    }
}