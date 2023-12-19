/**
 * Copyright 2009-2011 RED.ES - Desarrollado por MP Sistemas Licencia con arreglo a la EUPL, versión 1.1 o -en cuanto
 * sean aprobadas por la comisión Europea- versiones posteriores de la EUPL (la "Licencia"). Solo podrá usarse esta obra
 * si se respeta la Licencia. http://ec.europa.eu/idabc/eupl.html Salvo cuando lo exija la legislación aplicable o se
 * acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye "TAL CUAL", SIN GARANTÍAS NI
 * CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. Véase la Licencia en el idioma concreto que rige los permisos
 * y limitaciones que establece la Licencia.
 */
package com.comerzzia.jpos.servicios.core.usuarios;

import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuariosDao;
import com.comerzzia.jpos.util.db.Database;
import java.sql.SQLException;
import java.util.List;

import es.mpsistemas.util.db.Connection;
import es.mpsistemas.util.log.Logger;
import java.util.logging.Level;

public class ServicioUsuarios {

    /**
     * Logger
     */
    protected static Logger log = Logger.getMLogger(ServicioUsuarios.class);

    public static UsuarioBean consultar(Long idUsuario) throws UsuarioException, UsuarioNotFoundException {
        Connection conn = new Connection();

        try {
            log.debug("consultar() - Consultando datos del usuario: " + idUsuario);
            conn.abrirConexion(Database.getConnection());
            UsuarioBean usuario = UsuariosDao.consultar(conn, idUsuario);

            if (usuario == null) {
                String msg = "No se ha encontrado el usuario con identificador: " + idUsuario;
                log.info("consultar() - " + msg);
                throw new UsuarioNotFoundException(msg);
            }

            return usuario;
        }
        catch (SQLException e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar datos de un usuario: " + e.getMessage();

            throw new UsuarioException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
    }

    public static List<UsuarioBean> consultar() throws UsuarioException {
        Connection conn = new Connection();

        try {
            log.debug("consultar() - Consultando todos los usuarios");
            conn.abrirConexion(Database.getConnection());
            return UsuariosDao.consultar(conn);
        }
        catch (SQLException e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar todos los usuaios: " + e.getMessage();

            throw new UsuarioException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
    }

    public static void cambiarClaveUsuario(UsuarioBean usuario) throws UsuarioException {
        Connection conn = new Connection();
        Connection connCentral = new Connection();

        try {
            log.debug("cambiarClaveUsuario() - Cambiando clave del usuario " + usuario.getIdUsuario());
            conn.abrirConexion(Database.getConnection());
            connCentral.abrirConexion(Database.getConnectionCentral());
            conn.iniciaTransaccion();
            connCentral.iniciaTransaccion();
            UsuariosDao.cambiarClaveUsuarioCentral(connCentral, usuario);
            UsuariosDao.cambiarClaveUsuarioLocal(conn, usuario);
            conn.commit();
            connCentral.commit();
        }
        catch (SQLException e) {
            String msg = "Error actualizando clave de usario: " + e.getMessage();
            log.error("cambiarClaveUsuario() - " + msg);
            conn.deshacerTransaccion();
            connCentral.deshacerTransaccion();
            throw new UsuarioException(msg, e);
        }
        finally {
            conn.cerrarConexion();
            connCentral.cerrarConexion();
        }
    }
    
    /**
     * Busca un usuario por su usuario de login
     * @param codUsuario
     * @return
     * @throws UsuarioException
     * @throws UsuarioNotFoundException 
     */
     public static UsuarioBean consultar(String codUsuario) throws UsuarioException, UsuarioNotFoundException {
        Connection conn = new Connection();

        try {
            log.debug("consultar() - Consultando datos del usuario: " + codUsuario);
            conn.abrirConexion(Database.getConnection());
            UsuarioBean usuario = UsuariosDao.consultar(conn, codUsuario);

            if (usuario == null) {
                String msg = "No se ha encontrado el usuario con identificador: " + codUsuario;
                log.info("consultar() - " + msg);
                throw new UsuarioNotFoundException(msg);
            }
            return usuario;
        }
        catch (SQLException e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar datos de un usuario: " + e.getMessage();

            throw new UsuarioException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
    }

    

    
    
}
