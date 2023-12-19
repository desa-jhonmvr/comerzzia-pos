/**
 * Copyright 2009-2011 RED.ES - Desarrollado por MP Sistemas Licencia con arreglo a la EUPL, versión 1.1 o -en cuanto
 * sean aprobadas por la comisión Europea- versiones posteriores de la EUPL (la "Licencia"). Solo podrá usarse esta obra
 * si se respeta la Licencia. http://ec.europa.eu/idabc/eupl.html Salvo cuando lo exija la legislación aplicable o se
 * acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye "TAL CUAL", SIN GARANTÍAS NI
 * CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. Véase la Licencia en el idioma concreto que rige los permisos
 * y limitaciones que establece la Licencia.
 */
package com.comerzzia.jpos.servicios.logs.logsistema;

import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.persistencia.logs.logsistema.LogSistemaDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import java.sql.SQLException;

import com.comerzzia.jpos.util.db.Connection;
import es.mpsistemas.util.log.Logger;

public class ServicioLogSistema {

    /**
     * Logger
     */
    protected static Logger log = Logger.getMLogger(ServicioLogSistema.class);

    public static void registrarAcceso(Connection conn, UsuarioBean usuario) {
        try {
            log.debug("registrarAcceso() - Registrando acceso usuario: " + usuario.getUsuario());
            LogSistemaDao.registrarAcceso(conn, usuario, Sesion.getTienda().getCodalm(), Sesion.getDatosConfiguracion().getCodcaja());
        }
        catch (SQLException e) {
            log.error("registrarAcceso() - No se ha podido registra el acceso del usuario. " + e.getMessage());
        }
    }
}
