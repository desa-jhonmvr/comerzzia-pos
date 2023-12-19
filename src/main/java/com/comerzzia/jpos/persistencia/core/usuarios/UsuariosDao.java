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
package com.comerzzia.jpos.persistencia.core.usuarios;

import com.comerzzia.jpos.entity.db.Usuarios;
import com.comerzzia.jpos.servicios.login.Sesion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.Connection;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;

import es.mpsistemas.util.fechas.Fecha;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class UsuariosDao extends MantenimientoDao {

    private static String TABLA = "CONFIG_USUARIOS_TBL";
    private static Logger log = Logger.getMLogger(UsuariosDao.class);

    public static UsuarioBean consultar(Connection conn, Long idUsuario) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UsuarioBean usuario = null;
        String sql = null;

        sql = "SELECT ID_USUARIO, USUARIO, DESUSUARIO, CLAVE, ACTIVO, APLICACION_POR_DEFECTO,"
                + "PUEDE_CAMBIAR_APLICACION, CLAVE_CORTA, CLAVE_ANTIGUA_1, CLAVE_ANTIGUA_2, "
                + "CLAVE_CORTA_ANTIGUA_1, CLAVE_CORTA_ANTIGUA_2, FECHA_PASSWORD, FECHA_PASSWORD_CORTA, PRIMER_ACCESO "
                + "FROM " + getNombreElementoConfiguracion(TABLA)
                + "WHERE ID_USUARIO = ?";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, idUsuario);
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new UsuarioBean();
                usuario.setIdUsuario(rs.getLong("ID_USUARIO"));
                usuario.setUsuario(rs.getString("USUARIO"));
                usuario.setDesUsuario(rs.getString("DESUSUARIO"));
                usuario.setClave(rs.getString("CLAVE"));
                usuario.setActivo(rs.getString("ACTIVO"));
                usuario.setAplicacion(rs.getString("APLICACION_POR_DEFECTO"));
                usuario.setPuedeCambiarAplicacion(rs.getString("PUEDE_CAMBIAR_APLICACION"));
                usuario.setClaveCorta(rs.getString("CLAVE_CORTA"));
                usuario.setClaveAntigua1(rs.getString("CLAVE_ANTIGUA_1"));
                usuario.setClaveAntigua2(rs.getString("CLAVE_ANTIGUA_2"));
                usuario.setClaveCortaAntigua1(rs.getString("CLAVE_CORTA_ANTIGUA_1"));
                usuario.setClaveCortaAntigua2(rs.getString("CLAVE_CORTA_ANTIGUA_2"));
                usuario.setFechaPassword(new Fecha(rs.getDate("FECHA_PASSWORD")));
                usuario.setFechaPasswordCorta(new Fecha(rs.getDate("FECHA_PASSWORD_CORTA")));
                usuario.setPrimerAcceso(rs.getString("PRIMER_ACCESO"));
            }

            return usuario;
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
    //Cambio

    public static UsuarioBean consultar(Connection conn, String login) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UsuarioBean usuario = null;
        String sql = null;

        sql = "SELECT ID_USUARIO, USUARIO, DESUSUARIO, CLAVE, ACTIVO, APLICACION_POR_DEFECTO,"
                + "PUEDE_CAMBIAR_APLICACION, CLAVE_CORTA, CLAVE_ANTIGUA_1, CLAVE_ANTIGUA_2, "
                + "CLAVE_CORTA_ANTIGUA_1, CLAVE_CORTA_ANTIGUA_2, FECHA_PASSWORD, FECHA_PASSWORD_CORTA, PRIMER_ACCESO "
                + "FROM " + getNombreElementoConfiguracion(TABLA)
                + " a WHERE USUARIO = ? "
                + " and a.activo = 'S' "
                + " and exists(select null from D_ALMACENES_USUARIOS_TBL b "
                + " where a.id_usuario = b.id_usuario) ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, login);
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new UsuarioBean();
                usuario.setIdUsuario(rs.getLong("ID_USUARIO"));
                usuario.setUsuario(rs.getString("USUARIO"));
                usuario.setDesUsuario(rs.getString("DESUSUARIO"));
                usuario.setClave(rs.getString("CLAVE"));
                usuario.setActivo(rs.getString("ACTIVO"));
                usuario.setAplicacion(rs.getString("APLICACION_POR_DEFECTO"));
                usuario.setPuedeCambiarAplicacion(rs.getString("PUEDE_CAMBIAR_APLICACION"));
                usuario.setClaveCorta(rs.getString("CLAVE_CORTA"));
                usuario.setClaveAntigua1(rs.getString("CLAVE_ANTIGUA_1"));
                usuario.setClaveAntigua2(rs.getString("CLAVE_ANTIGUA_2"));
                usuario.setClaveCortaAntigua1(rs.getString("CLAVE_CORTA_ANTIGUA_1"));
                usuario.setClaveCortaAntigua2(rs.getString("CLAVE_CORTA_ANTIGUA_2"));
                usuario.setFechaPassword(new Fecha(rs.getDate("FECHA_PASSWORD")));
                usuario.setFechaPasswordCorta(new Fecha(rs.getDate("FECHA_PASSWORD_CORTA")));
                usuario.setPrimerAcceso(rs.getString("PRIMER_ACCESO"));
            }

            return usuario;
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

    //Consulta para Garantia Rd
    public static Usuarios obtenerUsuarioporNumero(String Numero) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Query a = em.createNamedQuery("Usuarios.findByUsuario");
        a.setParameter("usuario", Numero);

        Usuarios usu = null;
        try {
            usu = (Usuarios) a.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error al buscar empleado" + e);
            usu = null;
        }
        return usu;

    }

    public static List<UsuarioBean> consultar(Connection conn) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = null;
        List<UsuarioBean> resultados = new ArrayList<UsuarioBean>();

        sql = "SELECT ID_USUARIO, USUARIO, DESUSUARIO "
                + "FROM " + getNombreElementoConfiguracion(TABLA);

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            log.debug("consultar() - " + sql);

            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UsuarioBean usuario = new UsuarioBean();
                usuario.setIdUsuario(rs.getLong("ID_USUARIO"));
                usuario.setUsuario(rs.getString("USUARIO"));
                usuario.setDesUsuario(rs.getString("DESUSUARIO"));

                resultados.add(usuario);
            }

            return resultados;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {;
            }
            try {
                stmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void cambiarClaveUsuarioCentral(Connection conn, UsuarioBean usuario) throws SQLException {
        cambiarClaveUsuario(conn, usuario, getNombreElementoCentral(TABLA));
    }

    public static void cambiarClaveUsuarioLocal(Connection conn, UsuarioBean usuario) throws SQLException {
        cambiarClaveUsuario(conn, usuario, getNombreElementoConfiguracion(TABLA));
    }

    private static void cambiarClaveUsuario(Connection conn, UsuarioBean usuario, String tabla) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "UPDATE " + tabla
                + "SET CLAVE = ?, CLAVE_CORTA = ?, CLAVE_ANTIGUA_1 = ?, CLAVE_ANTIGUA_2 = ?, "
                + "CLAVE_CORTA_ANTIGUA_1 = ?, CLAVE_CORTA_ANTIGUA_2 = ?, FECHA_PASSWORD = ?, FECHA_PASSWORD_CORTA = ?, PRIMER_ACCESO = ? "
                + "WHERE ID_USUARIO = ?";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, usuario.getClave());
            pstmt.setString(2, usuario.getClaveCorta());
            pstmt.setString(3, usuario.getClaveAntigua1());
            pstmt.setString(4, usuario.getClaveAntigua2());
            pstmt.setString(5, usuario.getClaveCortaAntigua1());
            pstmt.setString(6, usuario.getClaveCortaAntigua2());
            pstmt.setDate(7, usuario.getFechaPassword().getSQL());
            pstmt.setDate(8, usuario.getFechaPasswordCorta().getSQL());
            pstmt.setString(9, usuario.isPrimerAcceso() ? "S" : "N");
            pstmt.setLong(10, usuario.getIdUsuario());

            log.debug("cambiarClaveUsuario() - " + pstmt);

            pstmt.execute();
        } catch (SQLException e) {
            throw getDaoException(e);
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

}
