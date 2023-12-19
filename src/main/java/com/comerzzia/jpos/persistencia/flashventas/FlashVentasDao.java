/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.flashventas;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import com.comerzzia.util.fechas.Fechas;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @author SMLM
 */
public class FlashVentasDao extends MantenimientoDao {
    
    private static Logger log = Logger.getMLogger(FlashVentasDao.class);
    private static String TABLA_TICK = "D_TICKETS_TBL";
    private static String TABLA_TICK_DET = "D_TICKETS_DET_TBL";
    private static String TABLA_NC = "X_NOTAS_CREDITO_TBL";
    private static String TABLA_NC_DET = "X_NOTAS_CREDITO_DET_TBL";
    private static String TABLA_ART = "D_ARTICULOS_TBL";
    private static String TABLA_USUARIO = "CONFIG_USUARIOS_TBL";
    
    public static FlashVentasBean consultarFlashSecciones (Connection conn, String codSeccion, Fecha fechaDesde, Fecha fechaHasta) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;    
        Set conjClientes = new HashSet<String>();
        Set conjFacturas = new HashSet<String>();
        BigDecimal descuentoTotal = new BigDecimal(0);
        BigDecimal sinDescuento = new BigDecimal(0);
        BigDecimal importeFinal = new BigDecimal(0);
        BigDecimal cantidadTotal = new BigDecimal(0);
        String sql = "SELECT DET.UID_TICKET, DET.CANTIDAD, TI.CODCLI, DET.IMPORTE_FINAL, TI.FECHA, DET.CODART, ART.CODSECCION, "+
                     "(DET.PRECIO_ORIGEN * DET.CANTIDAD) AS SIN_DESCUENTO FROM "+  getNombreElementoEmpresa(TABLA_TICK_DET) + " DET " +
                     "LEFT JOIN " + getNombreElementoEmpresa(TABLA_TICK) + " TI ON TI.UID_TICKET = DET.UID_TICKET " +
                     "LEFT JOIN " + getNombreElementoEmpresa(TABLA_ART) + " ART ON DET.CODART = ART.CODART " +
                     "WHERE CODSECCION = ? AND FECHA >= ? AND FECHA <= ? AND ANULADO = 'N'";

        String sqlNotas = "SELECT SUM(DET.IMPORTE_FINAL) AS DEVOLUCIONES "+
                     " FROM "+  getNombreElementoEmpresa(TABLA_NC_DET) + " DET " +
                     "INNER JOIN " + getNombreElementoEmpresa(TABLA_NC) + " NC ON (NC.UID_NOTA_CREDITO = DET.UID_NOTA_CREDITO) " +
                     "INNER JOIN " + getNombreElementoEmpresa(TABLA_ART) + " ART ON (DET.CODART = ART.CODART) " +
                     "WHERE CODSECCION = ? AND FECHA >= ? AND FECHA <= ? AND ANULADO = 'N'";

        try{
            // PRIMERO: VENTAS SECCION
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, codSeccion);
            pstmt.setTimestamp(2, Fechas.toSqlTimestamp(fechaDesde.getDate()));
            pstmt.setTimestamp(3, Fechas.toSqlTimestamp(fechaHasta.getDate()));
            log.debug("consultarFlashSecciones() - " + pstmt);
            rs = pstmt.executeQuery();
            int i=0;
            while(rs.next()){
                i++;
                conjClientes.add(rs.getString("CODCLI"));
                conjFacturas.add(rs.getString("UID_TICKET"));
                importeFinal = importeFinal.add(rs.getBigDecimal("IMPORTE_FINAL"));
                sinDescuento = sinDescuento.add(rs.getBigDecimal("SIN_DESCUENTO"));
                cantidadTotal = cantidadTotal.add(rs.getBigDecimal("CANTIDAD"));
            }
            if (Numero.isMayorACero(sinDescuento)){
                descuentoTotal = sinDescuento.subtract(importeFinal).multiply(Numero.CIEN).divide(sinDescuento, 2, RoundingMode.HALF_UP);
            }
            else{
                descuentoTotal = BigDecimal.ZERO;
            }

            
            // SEGUNDO: DEVOLUCIONES SECCION
            pstmt = new PreparedStatement(conn, sqlNotas);
            pstmt.setString(1, codSeccion);
            pstmt.setTimestamp(2, Fechas.toSqlTimestamp(fechaDesde.getDate()));
            pstmt.setTimestamp(3, Fechas.toSqlTimestamp(fechaHasta.getDate()));
            log.debug("consultarFlashSecciones() - " + pstmt);
            rs = pstmt.executeQuery();
            if(rs.next()){
                BigDecimal devoluciones = rs.getBigDecimal("DEVOLUCIONES");
                if (devoluciones != null){
                    importeFinal = importeFinal.subtract(devoluciones);
                }
            }
            
            
            FlashVentasBean flashVentas = new FlashVentasBean();
            flashVentas.setNumClientes(new Long(conjClientes.size()));
            flashVentas.setNumFacturas(new Long(conjFacturas.size()));
            flashVentas.setImporteFinal(importeFinal);
            flashVentas.setDescuento(descuentoTotal);
            
            return flashVentas;
        } catch (SQLException e){
            throw new SQLException(e);
        }
    }
    
    public static FlashVentasBean consultarFlashVendedores (Connection conn, String vendedor, Fecha fechaDesde, Fecha fechaHasta) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;    
        BigDecimal descuentoTotal = new BigDecimal(0);
        BigDecimal importeFinal = new BigDecimal(0);
        BigDecimal sinDescuento = new BigDecimal(0);
        BigDecimal cantidadTotal = new BigDecimal(0);
        BigDecimal acumuladoMes = new BigDecimal(0);
        BigDecimal acumuladoMesDevoluciones = new BigDecimal(0);
        /*
        String sql = "SELECT DET.UID_TICKET, DET.CANTIDAD, DET.IMPORTE_FINAL, TI.FECHA, TI.USUARIO, "+
                     "(DET.PRECIO_ORIGEN * DET.CANTIDAD) AS SIN_DESCUENTO FROM "+  getNombreElementoEmpresa(TABLA_TICK_DET) + " DET " +
                     "LEFT JOIN " + getNombreElementoEmpresa(TABLA_TICK) + " TI ON TI.UID_TICKET = DET.UID_TICKET " +
                     "WHERE USUARIO = ? AND FECHA >= ? AND FECHA <= ? AND ANULADO = 'N'";
        
        String sql2 = "SELECT SUM(IMPORTE_FINAL) AS TOTAL FROM "+  getNombreElementoEmpresa(TABLA_TICK_DET) + " DET " +
                     "LEFT JOIN " + getNombreElementoEmpresa(TABLA_TICK) + " TI ON TI.UID_TICKET = DET.UID_TICKET " +
                     "WHERE USUARIO = ? AND FECHA >= ? AND FECHA <= ? AND ANULADO = 'N'";        

        
        String sqlNotas = "SELECT SUM(DET.IMPORTE_FINAL) AS DEVOLUCIONES "+
                     " FROM "+  getNombreElementoEmpresa(TABLA_NC_DET) + " DET " +
                     "INNER JOIN " + getNombreElementoEmpresa(TABLA_NC) + " NC ON (NC.UID_NOTA_CREDITO = DET.UID_NOTA_CREDITO) " +
                     "INNER JOIN " + getNombreElementoEmpresa(TABLA_USUARIO) + " USU ON (USU.ID_USUARIO = DET.ID_CAJERO) " +
                     "WHERE USU.USUARIO = ? AND FECHA >= ? AND FECHA <= ? AND ANULADO = 'N'";
        
        String sqlNotas2 = "SELECT SUM(DET.IMPORTE_FINAL) AS TOTAL_DEVOLUCIONES "+
                     " FROM "+  getNombreElementoEmpresa(TABLA_NC_DET) + " DET " +
                     "INNER JOIN " + getNombreElementoEmpresa(TABLA_NC) + " NC ON (NC.UID_NOTA_CREDITO = DET.UID_NOTA_CREDITO) " +
                     "INNER JOIN " + getNombreElementoEmpresa(TABLA_USUARIO) + " USU ON (USU.ID_USUARIO = DET.ID_CAJERO) " +
                     "WHERE USU.USUARIO = ? AND FECHA >= ? AND FECHA <= ? AND ANULADO = 'N'";
                */
        
        
        String sql = "SELECT DET.UID_TICKET, DET.CANTIDAD, DET.IMPORTE_FINAL, TI.FECHA, TI.USUARIO, DET.COD_VENDEDOR, "+
                     "(DET.PRECIO_ORIGEN * DET.CANTIDAD) AS SIN_DESCUENTO FROM "+  getNombreElementoEmpresa(TABLA_TICK_DET) + " DET " +
                     "LEFT JOIN " + getNombreElementoEmpresa(TABLA_TICK) + " TI ON TI.UID_TICKET = DET.UID_TICKET " +
                     "WHERE DET.COD_VENDEDOR = ? AND FECHA >= ? AND FECHA <= ? AND ANULADO = 'N'";
        
        String sql2 = "SELECT SUM(IMPORTE_FINAL) AS TOTAL FROM "+  getNombreElementoEmpresa(TABLA_TICK_DET) + " DET " +
                     "LEFT JOIN " + getNombreElementoEmpresa(TABLA_TICK) + " TI ON TI.UID_TICKET = DET.UID_TICKET " +
                     "WHERE DET.COD_VENDEDOR = ? AND FECHA >= ? AND FECHA <= ? AND ANULADO = 'N'";        

        
        String sqlNotas = "SELECT SUM(DET.IMPORTE_FINAL) AS DEVOLUCIONES "+
                     " FROM "+  getNombreElementoEmpresa(TABLA_NC_DET) + " DET " +
                     "INNER JOIN " + getNombreElementoEmpresa(TABLA_NC) + " NC ON (NC.UID_NOTA_CREDITO = DET.UID_NOTA_CREDITO) " +
                     "INNER JOIN " + getNombreElementoEmpresa(TABLA_USUARIO) + " USU ON (USU.USUARIO = DET.COD_VENDEDOR) " +
                     "WHERE USU.USUARIO = ? AND FECHA >= ? AND FECHA <= ? AND ANULADO = 'N'";
        
        String sqlNotas2 = "SELECT SUM(DET.IMPORTE_FINAL) AS TOTAL_DEVOLUCIONES "+
                     " FROM "+  getNombreElementoEmpresa(TABLA_NC_DET) + " DET " +
                     "INNER JOIN " + getNombreElementoEmpresa(TABLA_NC) + " NC ON (NC.UID_NOTA_CREDITO = DET.UID_NOTA_CREDITO) " +
                     "INNER JOIN " + getNombreElementoEmpresa(TABLA_USUARIO) + " USU ON (USU.USUARIO = DET.COD_VENDEDOR) " +
                     "WHERE USU.USUARIO = ? AND FECHA >= ? AND FECHA <= ? AND ANULADO = 'N'";
        
        
        try{
            // PRIMERO: VENTAS DEL VENDEDOR SEGÚN FECHA CONSULTA
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, vendedor);
            pstmt.setTimestamp(2, Fechas.toSqlTimestamp(fechaDesde.getDate()));
            pstmt.setTimestamp(3, Fechas.toSqlTimestamp(fechaHasta.getDate()));
            log.debug("consultarFlashVendedores() - " + pstmt);
            rs = pstmt.executeQuery();
            int i=0;
            while(rs.next()){
                i++;
                importeFinal = importeFinal.add(rs.getBigDecimal("IMPORTE_FINAL"));
                sinDescuento = sinDescuento.add(rs.getBigDecimal("SIN_DESCUENTO"));
                cantidadTotal = cantidadTotal.add(rs.getBigDecimal("CANTIDAD"));
            }
            if (Numero.isMayorACero(sinDescuento)){
                descuentoTotal = sinDescuento.subtract(importeFinal).multiply(Numero.CIEN).divide(sinDescuento, 2, RoundingMode.HALF_UP);
            }
            else{
                descuentoTotal = BigDecimal.ZERO;
            }
            
            
            // SEGUNDO: DEVOLUCIONES VENDEDOR SEGÚN FECHA CONSULTA
            pstmt = new PreparedStatement(conn, sqlNotas);
            pstmt.setString(1, vendedor);
            pstmt.setTimestamp(2, Fechas.toSqlTimestamp(fechaDesde.getDate()));
            pstmt.setTimestamp(3, Fechas.toSqlTimestamp(fechaHasta.getDate()));
            log.debug("consultarFlashVendedores() - " + pstmt);
            rs = pstmt.executeQuery();
            if(rs.next()){
                BigDecimal devoluciones = rs.getBigDecimal("DEVOLUCIONES");
                if (devoluciones != null){
                    importeFinal = importeFinal.subtract(devoluciones);
                }
            }
            
            
            FlashVentasBean flashVentas = new FlashVentasBean();
            flashVentas.setImporteFinal(importeFinal);
            flashVentas.setDescuento(descuentoTotal);
            
            
            
            // TERCERO: ACUMULADO VENTAS MES VENDEDOR
            //Calculamos el acumulado para este mes
            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd-MMM-yyyy HH:mm", new Locale("es","ES"));
            String fec = formatoDeFecha.format(fechaDesde.getDate());
            String fecha1 = "01" + fec.substring(2,12) + "00:00";
            
            pstmt = new PreparedStatement(conn, sql2);
            pstmt.setString(1, vendedor);
            pstmt.setTimestamp(2, Fechas.toSqlTimestamp(new Fecha(fecha1,"dd-MMM-yyyy HH:mm").getDate()));
            pstmt.setTimestamp(3, Fechas.toSqlTimestamp(fechaHasta.getDate()));
            
            log.debug("consultarFlashVendedores() - " + pstmt);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                acumuladoMes = rs.getBigDecimal("TOTAL");
            }
            if (acumuladoMes == null){
                acumuladoMes = BigDecimal.ZERO;
            }
            
            // CUARTO: ACUMULADO DEVOLUCIONES MES VENDEDOR
            pstmt = new PreparedStatement(conn, sqlNotas2);
            pstmt.setString(1, vendedor);
            pstmt.setTimestamp(2, Fechas.toSqlTimestamp(new Fecha(fecha1,"dd-MMM-yyyy HH:mm").getDate()));
            pstmt.setTimestamp(3, Fechas.toSqlTimestamp(fechaHasta.getDate()));
            
            log.debug("consultarFlashVendedores() - " + pstmt);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                acumuladoMesDevoluciones = rs.getBigDecimal("TOTAL_DEVOLUCIONES");
            }
            if (acumuladoMesDevoluciones == null){
                acumuladoMesDevoluciones = BigDecimal.ZERO;
            }
            
            flashVentas.setAcumuladoMes(acumuladoMes.subtract(acumuladoMesDevoluciones));
            
            return flashVentas;
        } 
        catch (SQLException e){
            throw new SQLException(e);
        }
    }

    public static FlashVentasBean consultarFlashDiario (Connection conn, Fecha fechaDesde, Fecha fechaHasta) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;    
        Set conjClientes = new HashSet<String>();
        Set conjFacturas = new HashSet<String>();
        BigDecimal descuentoTotal = new BigDecimal(0);
        BigDecimal sinDescuento = new BigDecimal(0);
        BigDecimal importeFinal = new BigDecimal(0);
        BigDecimal cantidadTotal = new BigDecimal(0);
        String sql = "SELECT DET.UID_TICKET, DET.CANTIDAD, TI.CODCLI, DET.IMPORTE_FINAL, TI.FECHA, "+
                     "(DET.PRECIO_ORIGEN * DET.CANTIDAD) AS SIN_DESCUENTO FROM "+  getNombreElementoEmpresa(TABLA_TICK_DET) + " DET " +
                     "LEFT JOIN " + getNombreElementoEmpresa(TABLA_TICK) + " TI ON TI.UID_TICKET = DET.UID_TICKET " +
                     "WHERE FECHA >= ? AND FECHA <= ? AND ANULADO = 'N'";

        String sqlNotas = "SELECT SUM(DET.IMPORTE_FINAL) AS DEVOLUCIONES "+
                     "FROM "+  getNombreElementoEmpresa(TABLA_NC_DET) + " DET " +
                     "INNER JOIN " + getNombreElementoEmpresa(TABLA_NC) + " NC ON NC.uid_nota_credito = DET.uid_nota_credito " +
                     "WHERE NC.FECHA >= ? AND NC.FECHA <= ? AND NC.ANULADO = 'N' AND NC.CODALM = '" + Sesion.getTienda().getCodalm() + "' ";
        try{
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setTimestamp(1, Fechas.toSqlTimestamp(fechaDesde.getDate()));
            pstmt.setTimestamp(2, Fechas.toSqlTimestamp(fechaHasta.getDate()));
            
            log.debug("consultarFlashDiario() - " + pstmt);
            rs = pstmt.executeQuery();
            int i=0;
            while(rs.next()){
                i++;
                conjClientes.add(rs.getString("CODCLI"));
                conjFacturas.add(rs.getString("UID_TICKET"));
                importeFinal = importeFinal.add(rs.getBigDecimal("IMPORTE_FINAL"));
                sinDescuento = sinDescuento.add(rs.getBigDecimal("SIN_DESCUENTO"));
                cantidadTotal = cantidadTotal.add(rs.getBigDecimal("CANTIDAD"));
            }
            if (Numero.isMayorACero(sinDescuento)){
                descuentoTotal = sinDescuento.subtract(importeFinal).multiply(Numero.CIEN).divide(sinDescuento, 2, RoundingMode.HALF_UP);
            }
            else{
                descuentoTotal = BigDecimal.ZERO;
            }
            pstmt = new PreparedStatement(conn, sqlNotas);
            pstmt.setTimestamp(1, Fechas.toSqlTimestamp(fechaDesde.getDate()));
            pstmt.setTimestamp(2, Fechas.toSqlTimestamp(fechaHasta.getDate()));
            log.debug("consultarFlashDiario() - " + pstmt);
            rs = pstmt.executeQuery();
            if(rs.next()){
                BigDecimal devoluciones = rs.getBigDecimal("DEVOLUCIONES");
                if (devoluciones != null){
                    importeFinal = importeFinal.subtract(devoluciones);
                }
            }

                        
            
            FlashVentasBean flashVentas = new FlashVentasBean();
            flashVentas.setNumClientes(new Long(conjClientes.size()));
            flashVentas.setNumFacturas(new Long(conjFacturas.size()));
            flashVentas.setImporteFinal(importeFinal);
            flashVentas.setDescuento(descuentoTotal);
            
            return flashVentas;
        } catch (SQLException e){
            throw new SQLException(e);
        }
    }
    
}
