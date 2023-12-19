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
package com.comerzzia.jpos.servicios.core.permisos;

import com.comerzzia.jpos.persistencia.core.permisos.PermisoBean;
import com.comerzzia.jpos.persistencia.core.permisos.PermisosDao;
import java.sql.SQLException;
import java.util.List;

import es.mpsistemas.util.db.Connection;
import es.mpsistemas.util.log.Logger;

public class ServicioPermisos {

    protected static Logger log = Logger.getMLogger(ServicioPermisos.class);

    /**
     * Obtiene los permisos efectivos que el usuario tiene sobre la acción
     * siendo estos para cada operación definida para la acción el permiso
     * otorgado directamente al usuario y si este no lo tiene, el mayor de los
     * permisos otorgados a sus perfiles
     *
     * @param idAccion
     * @param idUsuario
     * @return
     * @throws PermisoException
     */
    public static PermisosEfectivosAccionBean obtenerPermisosEfectivos(Connection conn, Long idAccion,
            Long idUsuario) throws PermisoException {
        PermisosEfectivosAccionBean permisosEfectivos = new PermisosEfectivosAccionBean(idAccion, idUsuario);

        try {
            log.debug("obtenerPermisosEfectivos() - Obteniendo los permisos efectivos del usuario ["
                    + idUsuario + "] para la accion [" + idAccion + "]");

            // Obtenemos la lista de permisos heredados de los perfiles del usuario
            List<PermisoBean> permisosPerfiles = PermisosDao.consultarMaxPermisosAccionPerfilesUsuario(conn,
                    idAccion, idUsuario);

            // Obtenemos la lista de permisos de la acción definidos para el usuario
            List<PermisoBean> permisosUsuario = PermisosDao.consultarPermisosAccionUsuario(conn,
                    idAccion, idUsuario);

            // Esteblecemos primero la lista de permisos de los perfiles y luego la del usuario
            permisosEfectivos.setListaPermisos(permisosPerfiles);
            permisosEfectivos.setListaPermisos(permisosUsuario);

            return permisosEfectivos;
        } catch (SQLException e) {
            log.error("obtenerPermisosEfectivos() - " + e.getMessage());
            String mensaje = "Error al obtener los permisos efectivos de una acción: " + e.getMessage();

            throw new PermisoException(mensaje, e);
        }
    }

    public static Boolean obtenerPerfilUsuarioAdmin(Connection conn,
            Long idUsuario) throws PermisoException {
        Boolean perfilUsuarioAdmin = false;

        try {
            List<PermisoBean> permisosPerfilesUsuario = PermisosDao.consultarPerfilUsuario(conn, idUsuario);
            for (PermisoBean perfil : permisosPerfilesUsuario) {
                if (perfil.getIdPerfil().equals(Operaciones.PERFIL_ADMINISTRADOR) || perfil.getIdPerfil().equals(Operaciones.PERFIL_ADMINISTRADOR_LOCAL)) {
                    perfilUsuarioAdmin = true;
                }
            }
            return perfilUsuarioAdmin;
        } catch (SQLException e) {
            log.error("obtenerPermisosEfectivos() - " + e.getMessage());
            String mensaje = "Error al obtener los perfiles del Usuario: " + e.getMessage();

            throw new PermisoException(mensaje, e);
        }
    }

}
