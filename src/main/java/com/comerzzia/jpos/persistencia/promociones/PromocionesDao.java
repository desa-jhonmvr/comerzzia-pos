package com.comerzzia.jpos.persistencia.promociones;

import com.comerzzia.jpos.servicios.promociones.Promocion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.Connection;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;

public class PromocionesDao extends MantenimientoDao {

    private static String VISTA = "D_PROMOCIONES_CAB";
    private static String TABLA_AFILIADOS = "X_PROMOCIONES_AFILIADOS_TBL";
    private static String TABLA_VEN = "D_PROMOCIONES_VEN_TBL";
    private static String TABLA_MEDIOS_PAGO_VEN = "D_MEDIOS_PAGO_VEN_TBL";
    private static String TABLA_MEDIOS_PAGO = "D_MEDIOS_PAGO_TBL";
    private static String TABLA_DESC_SOCIO = "D_PROMO_SOCIO_TBL";

    private static Logger log = Logger.getMLogger(PromocionesDao.class);

    public static List consultar(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Inicializamos la página de resultados
        List<PromocionBean> resultados = new ArrayList<PromocionBean>();

        // Consultas
        String sql = "SELECT ID_PROMOCION, DESCRIPCION, CODTAR, DESTAR, "
                + "FECHA_INICIO, FECHA_FIN, SOLO_FIDELIZACION, ID_TIPO_PROMOCION, DESTIPOPROMOCION, "
                + "VERSION_TARIFA, TEXTO_PROMOCION, DATOS_PROMOCION "
                + "FROM " + getNombreElementoEmpresa(VISTA)
                + "WHERE VERSION_TARIFA IS NOT NULL AND FECHA_INICIO <= ? AND FECHA_FIN >= ? "
                + "ORDER BY FECHA_INICIO DESC ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            Fecha mañana = new Fecha();
            mañana.sumaDias(1);
            pstmt.setDate(1, mañana);
            pstmt.setTimestamp(2, new Fecha().getTimestamp());
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                resultados.add(new PromocionBean(rs));
            }
            return resultados;
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

    public static PromocionBean consultar(Connection conn, Long idPromocion) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT ID_PROMOCION, DESCRIPCION, CODTAR, DESTAR, "
                + "FECHA_INICIO, FECHA_FIN, SOLO_FIDELIZACION, ID_TIPO_PROMOCION, DESTIPOPROMOCION, "
                + "VERSION_TARIFA, TEXTO_PROMOCION, DATOS_PROMOCION "
                + "FROM " + getNombreElementoEmpresa(VISTA)
                + "WHERE ID_PROMOCION = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, idPromocion);
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new PromocionBean(rs);
            }
            return null;
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
     *
     * @param conn
     * @param idPromocion
     * @return
     * @throws SQLException
     */
    public static PromocionBean consultarPromocion(Connection conn, Long idPromocion) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Inicializamos la página de resultados
        PromocionBean resultado = null;

        // Consultas
        String sql = "SELECT ID_PROMOCION, DESCRIPCION, CODTAR, DESTAR, "
                + " FECHA_INICIO, FECHA_FIN, SOLO_FIDELIZACION, ID_TIPO_PROMOCION, DESTIPOPROMOCION, "
                + " VERSION_TARIFA, TEXTO_PROMOCION, DATOS_PROMOCION "
                + " FROM " + getNombreElementoEmpresa(VISTA)
                + " WHERE VERSION_TARIFA IS NOT NULL AND FECHA_INICIO <= ? AND FECHA_FIN >= ? "
                + " AND ID_PROMOCION = ?"
                + " ORDER BY FECHA_INICIO DESC ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            Fecha mañana = new Fecha();
            mañana.sumaDias(1);
            pstmt.setDate(1, mañana);
            pstmt.setTimestamp(2, new Fecha().getTimestamp());
            pstmt.setLong(3, idPromocion);
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                resultado = new PromocionBean(rs);
            }
            return resultado;
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

    public static List<String> consultarAfiliadosPromocion(Connection conn, Long idPromocion) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> lstAfiliadosPromocion = new ArrayList<String>();
        String sql = null;

        sql = "SELECT COD_TIPO_AFILIADO FROM "
                + getNombreElementoEmpresa(TABLA_AFILIADOS)
                + "WHERE ID_PROMOCION = ?";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, idPromocion);
            log.debug("consultarAfiliadosPromocion() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                lstAfiliadosPromocion.add(rs.getString("COD_TIPO_AFILIADO"));
            }
            return lstAfiliadosPromocion;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static void consultarMediosPagoPromocion(Connection conn, Promocion promocion) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        List<Long> vencimientos = new ArrayList<Long>();

        sql = "SELECT PROMO.ID_MEDPAG_VEN, MED.TARJETA_SUKASA FROM "
                + getNombreElementoEmpresa(TABLA_VEN) + " PROMO "
                + "INNER JOIN " + getNombreElementoEmpresa(TABLA_MEDIOS_PAGO_VEN) + " VEN "
                + "ON PROMO.ID_MEDPAG_VEN = VEN.ID_MEDPAG_VEN "
                + "INNER JOIN " + getNombreElementoEmpresa(TABLA_MEDIOS_PAGO) + " MED "
                + "ON VEN.CODMEDPAG = MED.CODMEDPAG "
                + "WHERE ID_PROMOCION = ?";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, promocion.getIdPromocion());
            log.debug("consultarMediosPagoPromocion() - " + pstmt);

            rs = pstmt.executeQuery();

            boolean soloTarjetaSukasa = true;
            while (rs.next()) {
                vencimientos.add(rs.getLong("ID_MEDPAG_VEN"));
                if (!rs.getString("TARJETA_SUKASA").equals("S")) {
                    soloTarjetaSukasa = false;
                }
            }
            if (!vencimientos.isEmpty()) {
                promocion.setVencimientos(vencimientos);
                promocion.setTieneFiltroPagos(true);
                promocion.setTieneFiltroPagosTarjetaSukasa(soloTarjetaSukasa);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static PromocionSocioBean consultarPromoSocio(Connection conn, String cedula) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Inicializamos la página de resultados
        PromocionSocioBean resultado = new PromocionSocioBean();

        // Consultas
        String sql = "SELECT ID,  "
                + " FECHA_INICIO, FECHA_FIN, DESCUENTO, CEDULA , EXCLUIR_ITEMS,VENCIMIENTOS  "
                + " FROM " + getNombreElementoEmpresa(TABLA_DESC_SOCIO)
                + " WHERE FECHA_INICIO <= ? AND FECHA_FIN >= ? "
                + " AND CEDULA = ? "
                + " AND UID_TICKET IS NULL ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setDate(1, new Fecha());
            pstmt.setDate(2, new Fecha());
            pstmt.setString(3, cedula);
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                resultado = new PromocionSocioBean(rs);
            }
            return resultado;
        } finally {
            try {
                if(rs!=null){
                    rs.close();
                }
            } catch (Exception ignore) {;
            }
            try {
                if(pstmt!=null){
                    pstmt.close();
                }
            } catch (Exception ignore) {;
            }
        }
    }

    public static boolean updatePromoClienteSocio(Connection conn, String uidTicket, String cedula,String promoActiva) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "UPDATE " + getNombreElementoEmpresa(TABLA_DESC_SOCIO)
                + " SET UID_TICKET = ? , PROMO_ACTIVA = ? "
                + " WHERE CEDULA = ? "
                + " AND UID_TICKET IS NULL ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, uidTicket);
            pstmt.setString(2, promoActiva);
            pstmt.setString(3, cedula);

            log.debug("incrementar() - " + pstmt);

            return (pstmt.executeUpdate() > 0);
        } catch (SQLException e) {
            throw getDaoException(e);
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }
    
    public static PromocionSocioBean consultarPromoSocioNC(Connection conn, String cedula, String uidTicket) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Inicializamos la página de resultados
        PromocionSocioBean resultado = new PromocionSocioBean();

        // Consultas
        String sql = "SELECT ID,  "
                + " FECHA_INICIO, FECHA_FIN, DESCUENTO, CEDULA , EXCLUIR_ITEMS,VENCIMIENTOS  "
                + " FROM " + getNombreElementoEmpresa(TABLA_DESC_SOCIO)
                + " WHERE FECHA_INICIO <= ? AND FECHA_FIN >= ? "
                + " AND CEDULA = ? "
                + " AND UID_TICKET = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setDate(1, new Fecha());
            pstmt.setDate(2, new Fecha());
            pstmt.setString(3, cedula);
            pstmt.setString(4, uidTicket);
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                resultado = new PromocionSocioBean(rs);
            }
            return resultado;
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

    
    public static boolean updatePromoClienteSocioNC(Connection conn, String uidTicket, String cedula, String uidNotaCredito) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "UPDATE " + getNombreElementoEmpresa(TABLA_DESC_SOCIO)
                + "SET UID_NOTA_CREDITO = ? , UID_TICKET = NULL "
                + " WHERE CEDULA = ? "
                + " AND UID_TICKET = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, uidNotaCredito);
            pstmt.setString(2, cedula);
            pstmt.setString(3, uidTicket);

            log.debug("incrementar() - " + pstmt);

            return (pstmt.executeUpdate() > 0);
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
