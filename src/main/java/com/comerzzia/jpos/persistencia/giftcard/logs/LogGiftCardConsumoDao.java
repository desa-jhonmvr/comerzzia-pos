package com.comerzzia.jpos.persistencia.giftcard.logs;

import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;

/**
 *
 * @author DESARROLLO
 */
public class LogGiftCardConsumoDao extends MantenimientoDao{
    
    private static String TABLA = "X_LOG_GIFTCARD_CONSUMO_TBL";
    private static Logger log = Logger.getMLogger(LogGiftCardConsumoDao.class);
    
    public static void insert(Connection conn, LogGiftCardConsumoBean logGifCardConsumo) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;


        sql = "INSERT INTO " + getNombreElementoEmpresa(TABLA)
                + " (UID_CONSUMO, UID_TICKET, ID_GIFTCARD, COD_ALM, USUARIO, FECHA, CONSUMO, COD_OPERACION, TIPO_CONSUMO, PROCESADO) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, logGifCardConsumo.getUidConsumo());
            pstmt.setString(2, logGifCardConsumo.getUidTicket());
            pstmt.setString(3, logGifCardConsumo.getIdGiftcard());
            pstmt.setString(4, logGifCardConsumo.getCodAlmacen());
            pstmt.setString(5, logGifCardConsumo.getUsuario());
            pstmt.setTimestamp(6, logGifCardConsumo.getFecha().getTimestamp());
            pstmt.setBigDecimal(7, logGifCardConsumo.getConsumo());
            pstmt.setString(8, logGifCardConsumo.getCodOperacion());
            pstmt.setString(9, logGifCardConsumo.getTipoConsumo());
            pstmt.setString(10, logGifCardConsumo.getProcesado());

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
}
