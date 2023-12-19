/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.devoluciones.articulos;

import com.comerzzia.jpos.persistencia.giftcard.GiftCardBean;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class ArticulosDevueltosDao extends MantenimientoDao {

    private static String TABLA = "X_ARTICULOS_DEVUELTOS_TBL";
    private static Logger log = Logger.getMLogger(ArticulosDevueltosDao.class);

    
    public static List<ArticuloDevueltoBean> consultar(Connection conn, String uidTicket) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ArticuloDevueltoBean> resultado = new ArrayList<ArticuloDevueltoBean>();
        try {
            String sql = "SELECT UID_TICKET, UID_NOTA_CREDITO, CODART, CANTIDAD, ID_LINEA "
                    + " FROM " + getNombreElementoEmpresa(TABLA)
                    + " WHERE UID_TICKET = '" + uidTicket + "'";
                    

            log.debug("consultar() - " + sql);

            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultar() - " + sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                resultado.add(new ArticuloDevueltoBean(rs, ArticuloDevueltoBean.MODO_NO_AGRUPADO));
            }
            return resultado;
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            }
            catch (Exception ignore) {;
            }
        }
    }
    
    public static List<ArticuloDevueltoBean> consultarAgrupadoUidTicket(Connection conn, String uidTicket) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ArticuloDevueltoBean> resultado = new ArrayList<ArticuloDevueltoBean>();
        
        try {
            String sql = "SELECT UID_TICKET, SUM(CANTIDAD) AS CANTIDAD, ID_LINEA "
                    + " FROM " + getNombreElementoEmpresa(TABLA)
                    + " WHERE UID_TICKET = '" + uidTicket + "'"
                    + " GROUP BY(UID_TICKET, ID_LINEA) ";

            log.debug("consultarAgrupadoUidTicket() - " + sql);

            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarAgrupadoUidTicket() - " + sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                resultado.add(new ArticuloDevueltoBean(rs, ArticuloDevueltoBean.MODO_AGRUPADO));
            }
            return resultado;
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            }
            catch (Exception ignore) {;
            }
        }
    }
    
    //UID_TICKET, UID_NOTA_CREDITO, CODART, SUCANTIDAD), ID_LINEA

    public static void insert(Connection conn, ArticuloDevueltoBean articuloDevuelto) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "INSERT INTO " + getNombreElementoEmpresa(TABLA)
                + "(UID_TICKET, UID_NOTA_CREDITO, CODART, CANTIDAD, ID_LINEA ) "
                + " VALUES (?, ?, ?, ?, ?)";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, articuloDevuelto.getUidTicket());
            pstmt.setString(2, articuloDevuelto.getUidNotaCredito());
            pstmt.setString(3, articuloDevuelto.getCodart());
            pstmt.setInt(4, articuloDevuelto.getCantidad());
            pstmt.setInt(5, articuloDevuelto.getIdLinea());

            log.debug("insert() - " + pstmt);
            pstmt.execute();
        }
        catch (SQLException e) {
            throw getDaoException(e);
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {;
            }
        }
    }


    public static void deletePorUidNotaCredito(Connection conn, String uidNotaCredito) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "DELETE FROM  " + getNombreElementoEmpresa(TABLA)
                + "WHERE UID_NOTA_CREDITO = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, uidNotaCredito);

            log.debug("delete() - " + pstmt);

            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw getDaoException(e);
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {;
            }
        }        
    }
}
