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
package com.comerzzia.jpos.persistencia.core.usuarios.perfiles;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.Connection;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;

public class PerfilesUsuariosDao extends MantenimientoDao {

    private static String VISTA = "CONFIG_USUARIOS_PERFILES";
    private static Logger log = Logger.getMLogger(PerfilesUsuariosDao.class);

    public static Integer consultarCaducidadClave(Connection conn, Long idUsuario) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "SELECT MIN(CADUCIDAD_PASSWORD) AS CADUCIDAD_CLAVE_MINIMA "
                + "FROM " + getNombreElementoConfiguracion(VISTA)
                + "WHERE ID_USUARIO = ? AND CADUCIDAD_PASSWORD > 0";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, idUsuario);
            log.debug("consultarCaducidadClave() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("CADUCIDAD_CLAVE_MINIMA");
            }
            return 0;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }
}
