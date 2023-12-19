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
package com.comerzzia.jpos.persistencia.core.permisos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.comerzzia.util.base.MantenimientoDao;

import es.mpsistemas.util.db.Connection;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;

public class PermisosDao extends MantenimientoDao {

    private static String VISTA = "CONFIG_ACCIONES_PERMISOS";
    private static String TABLA = "CONFIG_USUARIOS_PERFILES_TBL";

    private static Logger log = Logger.getMLogger(PermisosDao.class);

    /**
     * Obtiene la lista de permisos que el usuario tiene sobre la acción
     *
     * @param conn
     * @param idAccion
     * @param idUsuario
     * @return
     * @throws SQLException
     */
    public static List<PermisoBean> consultarPermisosAccionUsuario(Connection conn, Long idAccion,
            Long idUsuario) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        List<PermisoBean> permisos = new ArrayList<PermisoBean>();

        sql = "SELECT ID_ACCION, ID_OPERACION, DESOPERACION, ID_USUARIO, ID_PERFIL, ACCESO, ORDEN "
                + "FROM " + getNombreElementoConfiguracion(VISTA)
                + "WHERE ID_ACCION = ? "
                + "AND ID_USUARIO = ? "
                + "ORDER BY ORDEN";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, idAccion);
            pstmt.setLong(2, idUsuario);
            log.debug("consultarPermisosAccionUsuario() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                PermisoBean permiso = new PermisoBean();
                permiso.setIdAccion(rs.getLong("ID_ACCION"));
                permiso.getOperacion().setIdAccion(rs.getLong("ID_ACCION"));
                permiso.getOperacion().setIdOperacion(rs.getByte("ID_OPERACION"));
                permiso.getOperacion().setDesOperacion(rs.getString("DESOPERACION"));
                permiso.getOperacion().setOrden(rs.getByte("ORDEN"));
                permiso.setIdUsuario(rs.getLong("ID_USUARIO"));
                permiso.setIdPerfil(rs.getLong("ID_PERFIL"));
                permiso.setPermiso(rs.getByte("ACCESO"));

                permisos.add(permiso);
            }

            return permisos;
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

    /**
     * Obtiene la lista de permisos que el usuario tiene por sus perfiles sobre
     * la acción Si para un perfil el usuario tiene distintos permisos devuelve
     * el mayor de ellos
     *
     * @param conn
     * @param idAccion
     * @param idUsuario
     * @return
     * @throws SQLException
     */
    public static List<PermisoBean> consultarMaxPermisosAccionPerfilesUsuario(Connection conn,
            Long idAccion, Long idUsuario) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        List<PermisoBean> permisos = new ArrayList<PermisoBean>();

        sql = "SELECT ID_ACCION, ID_OPERACION, DESOPERACION, MAX(ACCESO) ACCESO, ORDEN "
                + "FROM " + getNombreElementoConfiguracion(VISTA)
                + "WHERE ID_ACCION = ? "
                + "AND ID_PERFIL IN (SELECT ID_PERFIL FROM "
                + getNombreElementoConfiguracion("CONFIG_USUARIOS_PERFILES_TBL")
                + "WHERE ID_USUARIO = ?) "
                + "GROUP BY ID_ACCION, ID_OPERACION, DESOPERACION, ORDEN "
                + "ORDER BY ORDEN";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, idAccion);
            pstmt.setLong(2, idUsuario);
            log.debug("consultarMaxPermisosAccionPerfilesUsuario() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                PermisoBean permiso = new PermisoBean();
                permiso.setIdAccion(rs.getLong("ID_ACCION"));
                permiso.getOperacion().setIdAccion(rs.getLong("ID_ACCION"));
                permiso.getOperacion().setIdOperacion(rs.getByte("ID_OPERACION"));
                permiso.getOperacion().setDesOperacion(rs.getString("DESOPERACION"));
                permiso.getOperacion().setOrden(rs.getByte("ORDEN"));
                permiso.setPermiso(rs.getByte("ACCESO"));

                permisos.add(permiso);
            }

            return permisos;
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

    /**
     * Obtiene la lista de permisos que el usuario tiene sobre la acción
     *
     * @param conn
     * @param idAccion
     * @param idUsuario
     * @return
     * @throws SQLException
     */
    public static List<PermisoBean> consultarPerfilUsuario(Connection conn, Long idUsuario) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        List<PermisoBean> permisos = new ArrayList<PermisoBean>();

        sql = "SELECT  ID_USUARIO, ID_PERFIL "
                + "FROM " + getNombreElementoConfiguracion(TABLA)
                + "WHERE ID_USUARIO = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, idUsuario);
            log.debug("consultarPermisosAccionUsuario() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                PermisoBean permiso = new PermisoBean();
                permiso.setIdUsuario(rs.getLong("ID_USUARIO"));
                permiso.setIdPerfil(rs.getLong("ID_PERFIL"));

                permisos.add(permiso);
            }

            return permisos;
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
