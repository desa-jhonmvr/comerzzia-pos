/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.logs.logskdx;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.util.UUID;

/**
 *
 * @author SMLM
 */
public class LogKardexDao extends MantenimientoDao{
    
    private static final String TABLA = "X_LOG_KDX_TBL";
    private static final Logger log = Logger.getMLogger(LogKardexDao.class);    
    
    public static void insertLogKardex(Connection conn, String id, int marca, int item, int ktipo, int kcant, String accion, String usuario, String observaciones) throws SQLException {
        PreparedStatement pstmt = null;
        String sql;
        
        sql = "INSERT INTO " + getNombreElementoEmpresa(TABLA) +
                "(ID, LOCAL, CAJA, FACTURA, MARCA, ITEM, CANTIDAD, TIPO, FECHA_HORA, TIPO_ACCION, USUARIO_AUTORIZACION, OBSERVACIONES) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try{
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, Sesion.getTienda().getSriTienda().getCodalmsri());
            pstmt.setString(3, Sesion.getTienda().getSriTienda().getCajaActiva().getCodcajaSri());
            String factura = Sesion.getTienda().getSriTienda().getCodalminterno()+"-"+Sesion.getTienda().getSriTienda().getCajaActiva().getSriCajaPK().getCodcaja()+"-"+id;
            pstmt.setString(4, factura);
            pstmt.setInt(5, marca);    
            pstmt.setInt(6, item);
            pstmt.setInt(7, kcant);
            pstmt.setInt(8, ktipo);
            pstmt.setTimestamp(9, new Fecha().getTimestamp());
            pstmt.setString(10, accion);
            pstmt.setString(11, usuario);
            pstmt.setString(12, observaciones);
            log.debug("insertLogKardex() - " + pstmt);   
            
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
