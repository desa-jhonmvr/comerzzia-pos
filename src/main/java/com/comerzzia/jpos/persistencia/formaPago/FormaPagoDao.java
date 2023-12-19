/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.formaPago;

import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;

/**
 *
 * @author DESARROLLO
 */
public class FormaPagoDao extends MantenimientoDao {

    private static String TABLA = "X_FORMA_PAGO_NC";
    private static final Logger log = Logger.getMLogger(FormaPagoDao.class);

    public static void insert(Connection conn, FormaPagoBean formaPago) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "INSERT INTO " + getNombreElementoConfiguracion(TABLA)
                + "(CODALM, CODCAJA, ID_NOTA_CREDITO, CODMEDPAG, DESCRIPCION_MEDIO, VALOR, CRUZA_EFECTIVO, PROCESADO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, formaPago.getCodigoAlmacen());
            pstmt.setString(2, formaPago.getCodigoCaja());
            pstmt.setLong(3, formaPago.getIdNotaCredito());
            pstmt.setString(4, formaPago.getCodigoMedioPago());
            pstmt.setString(5, formaPago.getDescripcionMedioPago());
            pstmt.setBigDecimal(6, formaPago.getValor());
            pstmt.setString(7, formaPago.getCruzaEfectivo());
            pstmt.setString(8, formaPago.getProcesado());

            log.debug("insert() - " + pstmt);

            pstmt.execute();
        } catch (SQLException e) {
            throw getDaoException(e);
        } finally {
            try {
                pstmt.close();
//                pstmt2.close();
            } catch (Exception ignore) {;
            }
        }
    }

}
