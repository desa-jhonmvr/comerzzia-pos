package com.comerzzia.jpos.persistencia.giftcard.logs;

import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class LogGiftCardDao extends MantenimientoDao {

    private static String TABLA = "X_LOG_GIFTCARD_TBL";
    private static Logger log = Logger.getMLogger(LogGiftCardDao.class);

    public static void insert(Connection conn, LogGiftCardBean logGifCard) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;


        sql = "INSERT INTO " + getNombreElementoEmpresa(TABLA)
                + "(UID_LOG, FECHA_HORA, ID_GIFTCARD, USUARIO, AUTORIZADOR, COD_ALM, COD_CAJA, COD_OPERACION, SALDO, PROCESADO, COD_CLI, ABONO, PAGOS, ID_CARGA_GIFTCARD, ANULADO ) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, logGifCard.getUidLog());
            pstmt.setTimestamp(2, logGifCard.getFechaHora().getTimestamp());
            pstmt.setString(3, logGifCard.getIdGiftCard());
            pstmt.setString(4, logGifCard.getUsuario());
            pstmt.setString(5, logGifCard.getUsuarioAutorizador());
            pstmt.setString(6, logGifCard.getCodAlmacen());
            pstmt.setString(7, logGifCard.getCodCaja());
            pstmt.setString(8, logGifCard.getCodOperacion());
            pstmt.setBigDecimal(9, logGifCard.getSaldo());
            pstmt.setString(10, logGifCard.getProcesado());
            pstmt.setString(11, logGifCard.getCodCliente());
            pstmt.setBigDecimal(12, logGifCard.getAbono());
            pstmt.setBytes( 13, logGifCard.getPagos());
            pstmt.setLong(14, logGifCard.getIdCargaGiftCard());
            pstmt.setString(15, logGifCard.getAnulado());

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
    
    public static LogGiftCardBean consultar(Connection conn, String codAlm, String codCaja, Long idGC) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;      
        ResultSet rs = null;
        LogGiftCardBean logGiftCard = new LogGiftCardBean();
        
        sql = "SELECT UID_LOG, FECHA_HORA, ID_GIFTCARD, USUARIO, AUTORIZADOR, COD_ALM, COD_CAJA, COD_OPERACION, SALDO, PROCESADO, COD_CLI, ABONO, PAGOS, ID_CARGA_GIFTCARD, ANULADO "+
                "FROM "+getNombreElementoEmpresa(TABLA)+
                "WHERE COD_ALM = ? AND COD_CAJA = ? AND ID_CARGA_GIFTCARD = ?";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, codAlm);
            pstmt.setString(2, codCaja);
            pstmt.setLong(3, idGC);

            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();
            if(rs.next()){
                logGiftCard.setUidLog(rs.getString("UID_LOG"));
                logGiftCard.setFechaHora(new Fecha(rs.getTimestamp("FECHA_HORA")));
                logGiftCard.setIdGiftCard(rs.getString("ID_GIFTCARD"));
                logGiftCard.setUsuario(rs.getString("USUARIO"));
                logGiftCard.setUsuarioAutorizador(rs.getString("AUTORIZADOR"));
                logGiftCard.setCodAlmacen(rs.getString("COD_ALM"));
                logGiftCard.setCodCaja(rs.getString("COD_CAJA"));
                logGiftCard.setCodOperacion(rs.getString("COD_OPERACION"));
                logGiftCard.setSaldo(rs.getBigDecimal("SALDO"));
                logGiftCard.setProcesado(rs.getString("PROCESADO"));
                logGiftCard.setCodCliente(rs.getString("COD_CLI"));
                logGiftCard.setAbono(rs.getBigDecimal("ABONO"));
                logGiftCard.setPagos(rs.getBytes("PAGOS"));
                logGiftCard.setIdCargaGiftCard(rs.getLong("ID_CARGA_GIFTCARD"));
                logGiftCard.setAnulado(rs.getString("ANULADO"));
            }
            
            return logGiftCard;
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
    
    public static void actualizarLogGiftCard(Connection conn, LogGiftCardBean logGiftCard) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;      
        sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET PROCESADO = ?, ANULADO = ? "
                + "WHERE UID_LOG = ?";
        try{
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, logGiftCard.getProcesado());
            pstmt.setString(2, logGiftCard.getAnulado());
            pstmt.setString(3, logGiftCard.getUidLog());
            
            log.debug("actualizarLogGiftCard() - " + pstmt);

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
    
    public static List<LogGiftCardBean> consultarUsos(Connection conn, String idGiftCard) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;      
        ResultSet rs = null;
        List<LogGiftCardBean> logGiftCardList = new LinkedList<LogGiftCardBean>();
        
        sql = "SELECT UID_LOG, FECHA_HORA, ID_GIFTCARD, USUARIO, AUTORIZADOR, COD_ALM, COD_CAJA, COD_OPERACION, SALDO, PROCESADO, COD_CLI, ABONO, PAGOS, ID_CARGA_GIFTCARD, ANULADO "+
                "FROM "+getNombreElementoEmpresa(TABLA)+
                "WHERE ID_GIFTCARD = ? AND ANULADO = 'N'";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, idGiftCard);

            log.debug("consultarUsos() - " + pstmt);

            rs = pstmt.executeQuery();
            while(rs.next()){
                LogGiftCardBean logGiftCard = new LogGiftCardBean();
                logGiftCard.setUidLog(rs.getString("UID_LOG"));
                logGiftCard.setFechaHora(new Fecha(rs.getTimestamp("FECHA_HORA")));
                logGiftCard.setIdGiftCard(rs.getString("ID_GIFTCARD"));
                logGiftCard.setUsuario(rs.getString("USUARIO"));
                logGiftCard.setUsuarioAutorizador(rs.getString("AUTORIZADOR"));
                logGiftCard.setCodAlmacen(rs.getString("COD_ALM"));
                logGiftCard.setCodCaja(rs.getString("COD_CAJA"));
                logGiftCard.setCodOperacion(rs.getString("COD_OPERACION"));
                logGiftCard.setSaldo(rs.getBigDecimal("SALDO"));
                logGiftCard.setProcesado(rs.getString("PROCESADO"));
                logGiftCard.setCodCliente(rs.getString("COD_CLI"));
                logGiftCard.setAbono(rs.getBigDecimal("ABONO"));
                logGiftCard.setPagos(rs.getBytes("PAGOS"));
                logGiftCard.setIdCargaGiftCard(rs.getLong("ID_CARGA_GIFTCARD"));
                logGiftCard.setAnulado(rs.getString("ANULADO"));
                logGiftCardList.add(logGiftCard);
            }
            
            return logGiftCardList;
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
