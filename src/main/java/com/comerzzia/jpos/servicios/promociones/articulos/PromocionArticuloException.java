/**
 * Copyright 2009-2011 RED.ES - Desarrollado por MP Sistemas
 * 
 * Licencia con arreglo a la EUPL, versión 1.1 o -en cuanto 
 * sean aprobadas por la comisión Europea- versiones 
 * posteriores de la EUPL (la "Licencia").
 * Solo podrá usarse esta obra si se respeta la Licencia.
 * 
 * http://ec.europa.eu/idabc/eupl.html
 * 
 * Salvo cuando lo exija la legislación aplicable o se acuerde
 * por escrito, el programa distribuido con arreglo a la
 * Licencia se distribuye "TAL CUAL",
 * SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, 
 * ni expresas ni implícitas.
 * Véase la Licencia en el idioma concreto que rige
 * los permisos y limitaciones que establece la Licencia.
 */

package com.comerzzia.jpos.servicios.promociones.articulos;

import com.comerzzia.util.base.Exception;

public class PromocionArticuloException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1055368944126016144L;

	public PromocionArticuloException() {
		super();
	}
	public PromocionArticuloException(String msg) {
		super(msg);
	}
	
	public PromocionArticuloException(String msg, Throwable e) {
        super(msg, e);
    }

	public PromocionArticuloException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public PromocionArticuloException(String msg, String msgKey) {
		super(msg, msgKey);
	}
}
