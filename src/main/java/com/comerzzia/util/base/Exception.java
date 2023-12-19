package com.comerzzia.util.base;

import java.util.Locale;
import java.util.ResourceBundle;


public class Exception extends java.lang.Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4499603132946556598L;

	/**
	 * Archivo de propiedades con los mensajes de errores
	 */
	private String mensajes = "errores";
	
	/**
	 * Clave para traducción del mensaje de error por defecto
	 */
	private String msgKey = "error.default";
	
	/** 
     * Constructor por defecto sin mensaje de error
     */
    public Exception() {
    }
    
    /**
     * Constructor con mensaje de error
     * @param msg Mensaje de error
     */
    public Exception(String msg) {
        super(msg);
    }
    
    /**
     * Constructor con mensaje de error, causa
     * @param msg Mensaje de error
     * @param e Causa origen del error
     */
    public Exception(String msg, Throwable e) {
        super(msg);
        initCause(e);
    }
    
    /**
     * Constructor con mensaje de error y clave para el mensaje traducido
     * @param msg Mensaje de error
     * @param msg Clave del mensaje para traducción
     */
    public Exception(String msg, String msgKey) {
        super(msg);
        this.msgKey = msgKey;
    }
    
    /**
     * Constructor con mensaje de error, clave y causa
     * @param msg Mensaje de error
     * @param msg Clave del mensaje para traducción
     * @param e Causa origen del error
     */
    public Exception(String msg, String msgKey, Throwable e) {
        super(msg);
        initCause(e);
        this.msgKey = msgKey;
    }
    
    
    /**
     * Obtiene el mensaje de error en el locale indicado
     * @param locale Locale en el que se desea obtener el mensaje
     * @return Mensaje de error traducido. Si no se ha indicado clave se devuelve un
     * mensaje de error general, y  si no se encuentra traducción de la clave indicada
     * se devuelve el mensaje original de la excepción
     */
    public String getLocalizedMessage(Locale locale) {
    	ResourceBundle rb = ResourceBundle.getBundle(mensajes, locale);

    	String mensaje = rb.getString(msgKey);
    	return (mensaje != null ? mensaje : getMessage());
    }
    
    
    public String getMessageKey() {
    	return msgKey;
    }
}
