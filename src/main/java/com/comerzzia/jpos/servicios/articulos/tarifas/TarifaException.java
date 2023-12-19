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

package com.comerzzia.jpos.servicios.articulos.tarifas;

import com.comerzzia.jpos.servicios.articulos.ArticuloException;

public class TarifaException extends ArticuloException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6773085643659183673L;

	public TarifaException() {
	}

	public TarifaException(String msg) {
		super(msg);
	}

	public TarifaException(String msg, Throwable e) {
		super(msg, e);
	}

	public TarifaException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public TarifaException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

}
