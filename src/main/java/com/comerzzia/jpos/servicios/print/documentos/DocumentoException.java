/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.documentos;

/**
 *
 * @author SMLM
 */
public class DocumentoException extends Exception {
 	public DocumentoException () {
		super();
	}
	
	public DocumentoException (String msg) {
		super(msg);
	}
	
	public DocumentoException (String msg, Throwable e) {
            super(msg, e);
        }   
        
        public DocumentoException(String msg, Exception e) {
            super(msg, e);
        }

}
