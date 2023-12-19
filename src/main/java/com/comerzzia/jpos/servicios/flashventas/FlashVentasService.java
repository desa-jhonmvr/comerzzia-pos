/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.flashventas;

import com.comerzzia.jpos.persistencia.flashventas.FlashVentasBean;
import com.comerzzia.jpos.persistencia.flashventas.FlashVentasDao;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;

/**
 *
 * @author SMLM
 */
public class FlashVentasService {
    private static final Logger log = Logger.getMLogger(FlashVentasService.class);
    
    public static FlashVentasBean consultarFlashSecciones(String codSeccion, Fecha fechaDesde, Fecha fechaHasta) throws SQLException {
        Connection conn = new Connection();
        try{
            log.debug("consultarFlashSecciones() - Consultando todos los tickets para la seccion "+codSeccion);
            conn.abrirConexion(Database.getConnection());
            return FlashVentasDao.consultarFlashSecciones(conn, codSeccion, fechaDesde, fechaHasta);
        } catch (SQLException e){
            throw new SQLException(e);
        } finally {
            conn.cerrarConexion();
        }
    }
    
    public static FlashVentasBean consultarFlashVendedores(String vendedor, Fecha fechaDesde, Fecha fechaHasta) throws SQLException {
        Connection conn = new Connection();
        try{
            log.debug("consultarFlashVendedores() - Consultando todos los tickets para el vendedor "+vendedor);
            conn.abrirConexion(Database.getConnection());
            return FlashVentasDao.consultarFlashVendedores(conn, vendedor, fechaDesde, fechaHasta);
        } catch (SQLException e){
            throw new SQLException(e);
        } finally {
            conn.cerrarConexion();
        }        
    }
    
    public static FlashVentasBean consultarFlashDiario(Fecha fechaDesde, Fecha fechaHasta) throws SQLException {
        Connection conn = new Connection();
        try{
            log.debug("consultarFlashDiario() - Consultando todos los tickets para el período "+fechaDesde +" a "+fechaHasta);
            conn.abrirConexion(Database.getConnection());
            return FlashVentasDao.consultarFlashDiario(conn, fechaDesde, fechaHasta);
        } catch (SQLException e){
            throw new SQLException(e);
        } finally {
            conn.cerrarConexion();
        }
    }
    
}
