/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.persistencia.notacredito.detalle;

import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SMLM
 */
public class NotaCreditoDetalleDao extends MantenimientoDao{
    
    private static String TABLA = "X_NOTAS_CREDITO_DET_TBL";
    private static Logger log = Logger.getMLogger(NotaCreditoDetalleDao.class);
    
    public static void insert (NotaCreditoDetalleBean detalle, Connection conn) throws SQLException {
        String sql = null;
        PreparedStatement pstmt = null;
        
        sql = "INSERT INTO " + TABLA + " (UID_NOTA_CREDITO, ID_CAJERO, COD_VENDEDOR, ID_LINEA, CODART, CODIGO_BARRAS, CANTIDAD, PRECIO, PRECIO_TOTAL, IMPORTE_FINAL, IMPORTE_TOTAL_FINAL, UID_TICKET, CODIMP, PORCENTAJE,COSTO_LANDED, VALOR_INTERES) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try{
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, detalle.getUidNotaCredito());
            pstmt.setLong(2, detalle.getIdCajero());
            pstmt.setString(3, detalle.getCodVendedor());
            pstmt.setLong(4, detalle.getIdLinea());
            pstmt.setString(5, detalle.getCodart());
            pstmt.setString(6, detalle.getCodigoBarras());
            pstmt.setInt(7, detalle.getCantidad());
            pstmt.setBigDecimal(8, detalle.getPrecio());
            pstmt.setBigDecimal(9, detalle.getPrecioTotal());
            pstmt.setBigDecimal(10, detalle.getImporteFinal());
            pstmt.setBigDecimal(11, detalle.getImporteTotalFinal());
            pstmt.setString(12, detalle.getUidTicket());
            pstmt.setString(13, detalle.getCodImp());
            pstmt.setBigDecimal(14, detalle.getPorcentaje());
            pstmt.setBigDecimal(15, detalle.getCostoLanded());
            pstmt.setBigDecimal(16, detalle.getValorInteres());
            
            log.debug("insert() - " + pstmt);

            pstmt.execute();
        }
        finally {
            try {
                pstmt.close();
            }
            catch (Exception ignore) {
                ;
            }
        }        
    }
    
    public static List<NotaCreditoDetalleBean> consultar (Connection conn, String uidNotaCredito) throws SQLException {
        List<NotaCreditoDetalleBean> detalles = new ArrayList<NotaCreditoDetalleBean>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        
        sql = "SELECT UID_NOTA_CREDITO, ID_CAJERO, COD_VENDEDOR, ID_LINEA, CODART, CODIGO_BARRAS, CANTIDAD, PRECIO, PRECIO_TOTAL, IMPORTE_FINAL, IMPORTE_TOTAL_FINAL, UID_TICKET, " +
                "CODIMP, PORCENTAJE " +
                "FROM " + TABLA + " WHERE UID_NOTA_CREDITO = ?";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, uidNotaCredito);
            
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();        
            while(rs.next()){
                NotaCreditoDetalleBean detalle = new NotaCreditoDetalleBean();
                detalle.setUidNotaCredito(rs.getString("UID_NOTA_CREDITO"));
                detalle.setIdCajero(rs.getLong("ID_CAJERO"));
                detalle.setCodVendedor(rs.getString("COD_VENDEDOR"));
                detalle.setIdLinea(rs.getLong("ID_LINEA"));
                detalle.setCodart(rs.getString("CODART"));;
                detalle.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
                detalle.setCantidad(rs.getInt("CANTIDAD"));
                detalle.setPrecio(rs.getBigDecimal("PRECIO"));
                detalle.setPrecioTotal(rs.getBigDecimal("PRECIO_TOTAL"));
                detalle.setImporteFinal(rs.getBigDecimal("IMPORTE_FINAL"));
                detalle.setImporteTotalFinal(rs.getBigDecimal("IMPORTE_TOTAL_FINAL"));
                detalle.setUidTicket(rs.getString("UID_TICKET"));
                detalle.setCodImp(rs.getString("CODIMP"));
                detalle.setPorcentaje(rs.getBigDecimal("PORCENTAJE"));
                
                detalles.add(detalle);
            }
            return detalles;
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignore) {
                ;
            }
            try {
                pstmt.close();
            }
            catch (Exception ignore) {
                ;
            }
        }
    }
    
    public static NotaCreditoDetalleBean consultar (Connection conn, String uidNotaCredito, Long idLinea) throws SQLException {
        NotaCreditoDetalleBean detalle = new NotaCreditoDetalleBean();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        
        sql = "SELECT UID_NOTA_CREDITO, ID_CAJERO, COD_VENDEDOR, ID_LINEA, CODART, CODIGO_BARRAS, CANTIDAD, PRECIO, PRECIO_TOTAL, IMPORTE_FINAL, IMPORTE_TOTAL_FINAL, UID_TICKET, " +
                "CODIMP, PORCENTAJE " +
                "FROM " + TABLA + " WHERE UID_NOTA_CREDITO = ? AND ID_LINEA = ?";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, uidNotaCredito);
            pstmt.setLong(2, idLinea);
            
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();        
            if(rs.next()){
                detalle.setUidNotaCredito(rs.getString("UID_NOTA_CREDITO"));
                detalle.setIdCajero(rs.getLong("ID_CAJERO"));
                detalle.setCodVendedor(rs.getString("COD_VENDEDOR"));
                detalle.setIdLinea(rs.getLong("ID_LINEA"));
                detalle.setCodart(rs.getString("CODART"));;
                detalle.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
                detalle.setCantidad(rs.getInt("CANTIDAD"));
                detalle.setPrecio(rs.getBigDecimal("PRECIO"));
                detalle.setPrecioTotal(rs.getBigDecimal("PRECIO_TOTAL"));
                detalle.setImporteFinal(rs.getBigDecimal("IMPORTE_FINAL"));
                detalle.setImporteTotalFinal(rs.getBigDecimal("IMPORTE_TOTAL_FINAL"));
                detalle.setUidTicket(rs.getString("UID_TICKET"));
                detalle.setCodImp(rs.getString("CODIMP"));
                detalle.setPorcentaje(rs.getBigDecimal("PORCENTAJE"));
            }
            return detalle;
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignore) {
                ;
            }
            try {
                pstmt.close();
            }
            catch (Exception ignore) {
                ;
            }
        }
    }   
    
    public static List<NotaCreditoDetalleBean> consultarByUidTicket (Connection conn, String uidTicket) throws SQLException {
        List<NotaCreditoDetalleBean> detalles = new ArrayList<NotaCreditoDetalleBean>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        
        sql = "SELECT UID_NOTA_CREDITO, ID_CAJERO, COD_VENDEDOR, ID_LINEA, CODART, CODIGO_BARRAS, CANTIDAD, PRECIO, PRECIO_TOTAL, IMPORTE_FINAL, IMPORTE_TOTAL_FINAL, UID_TICKET, " +
                "CODIMP, PORCENTAJE " +
                "FROM " + TABLA + " WHERE UID_TICKET = ?";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, uidTicket);
            
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();        
            while(rs.next()){
                NotaCreditoDetalleBean detalle = new NotaCreditoDetalleBean();
                detalle.setUidNotaCredito(rs.getString("UID_NOTA_CREDITO"));
                detalle.setIdCajero(rs.getLong("ID_CAJERO"));
                detalle.setCodVendedor(rs.getString("COD_VENDEDOR"));
                detalle.setIdLinea(rs.getLong("ID_LINEA"));
                detalle.setCodart(rs.getString("CODART"));;
                detalle.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
                detalle.setCantidad(rs.getInt("CANTIDAD"));
                detalle.setPrecio(rs.getBigDecimal("PRECIO"));
                detalle.setPrecioTotal(rs.getBigDecimal("PRECIO_TOTAL"));
                detalle.setImporteFinal(rs.getBigDecimal("IMPORTE_FINAL"));
                detalle.setImporteTotalFinal(rs.getBigDecimal("IMPORTE_TOTAL_FINAL"));
                detalle.setUidTicket(rs.getString("UID_TICKET"));
                detalle.setCodImp(rs.getString("CODIMP"));
                detalle.setPorcentaje(rs.getBigDecimal("PORCENTAJE"));
                
                detalles.add(detalle);
            }
            return detalles;
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignore) {
                ;
            }
            try {
                pstmt.close();
            }
            catch (Exception ignore) {
                ;
            }
        }
    }
    
}
