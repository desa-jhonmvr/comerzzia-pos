package com.comerzzia.jpos.persistencia.giftcard;

import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class GiftCardDao extends MantenimientoDao {

    private static String TABLA = "X_GIFTCARD_TBL";
    private static Logger log = Logger.getMLogger(GiftCardDao.class);

    public static GiftCardBean consultar(Connection conn, String idGiftCard) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT ID_GIFTCARD, CODMEDPAG, SALDO, FECHA_CARGA_INICIAL, PROCESADO, VERSION, FECHA_PROCESADO, FECHA_VERSION, ANULADO "
                    + "FROM " + getNombreElementoEmpresa(TABLA)
                    + "WHERE ID_GIFTCARD = '" + idGiftCard + "'";

            log.debug("consultar() - " + sql);

            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultar() - " + sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new GiftCardBean(rs);
            }
            return null;
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

    public static void insert(Connection conn, GiftCardBean giftCard) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;


        sql = "INSERT INTO " + getNombreElementoEmpresa(TABLA)
                + "(ID_GIFTCARD, CODMEDPAG, SALDO, FECHA_CARGA_INICIAL, PROCESADO, ANULADO) "
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, giftCard.getIdGiftCard());
            pstmt.setString(2, giftCard.getCodMedioPago());
            pstmt.setBigDecimal(3, giftCard.getSaldo());
            pstmt.setTimestamp(4, giftCard.getFechaCargaInicial().getTimestamp());
            pstmt.setString(5, giftCard.getProcesado());
            pstmt.setString(6, giftCard.getAnulado());

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

    
     public static boolean actualizaSaldoJPA(EntityManager em, GiftCardBean giftCard, BigDecimal saldoAntiguo) throws SQLException {
           
         
         String sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET SALDO = ?, PROCESADO = ? "
                + "WHERE ID_GIFTCARD = ? AND SALDO = ?";
        Query nativeQuery = em.createNativeQuery(sql);
        nativeQuery.setParameter(1, giftCard.getSaldo());
        nativeQuery.setParameter(2, giftCard.getProcesado());
        nativeQuery.setParameter(3, giftCard.getIdGiftCard());
        nativeQuery.setParameter(4, saldoAntiguo);
        int executeUpdate = nativeQuery.executeUpdate();
        
        log.debug("actualizaSaldoJPA() - " + nativeQuery);
        return executeUpdate >0;
     }
    
    
    public static boolean actualizaSaldo(Connection conn, GiftCardBean giftCard, BigDecimal saldoAntiguo) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET SALDO = ?, PROCESADO = ? "
                + "WHERE ID_GIFTCARD = ? AND SALDO = ?";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setBigDecimal(1, giftCard.getSaldo());
            pstmt.setString(2, giftCard.getProcesado());
            pstmt.setString(3, giftCard.getIdGiftCard());
            pstmt.setBigDecimal(4, saldoAntiguo);

            log.debug("actualizaSaldo() - " + pstmt);

            return (pstmt.executeUpdate() > 0);
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
    
    public static void actualizarGiftCard(Connection conn, GiftCardBean giftCard) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET PROCESADO = ?, ANULADO = ?, SALDO = ? "
                + "WHERE ID_GIFTCARD = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, giftCard.getProcesado());
            pstmt.setString(2, giftCard.getAnulado());
            pstmt.setBigDecimal(3, giftCard.getSaldo());
            pstmt.setString(4, giftCard.getIdGiftCard());

            log.debug("actualizaProcesadoYAnulado() - " + pstmt);

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
    
    public static void delete(Connection conn, GiftCardBean giftCard) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "DELETE FROM  " + getNombreElementoEmpresa(TABLA)
                + "WHERE ID_GIFTCARD = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, giftCard.getIdGiftCard());

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
