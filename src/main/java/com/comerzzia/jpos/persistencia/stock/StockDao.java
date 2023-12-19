/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.stock;

import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import com.comerzzia.util.fechas.Fechas;
import es.mpsistemas.util.db.PreparedStatement;
import java.sql.ResultSet;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

/**
 *
 * @author MGRI
 */
public class StockDao extends MantenimientoDao {

    private static String TABLA = "KDX";
    private static String TABLA_KDX = "BDG_KARDEX";
    
    private static Logger log = Logger.getMLogger(StockDao.class);

    public static int consultarStockArticulo(Connection conn, int marca, int item, int tienda) throws SQLException {
        int res = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;


        sql = "SELECT KCANT "
                + "FROM " + TABLA + " ST "
                + "WHERE KLUGAR = '" + Sesion.getTienda().getSriTienda().getCodalminterno() + "' "
                + "AND KMARCA = '" + marca + "' "
                + "AND KITEM = '" + item + "' "
                + "AND KTIPO = 99 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticulo() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                res = rs.getInt("KCANT");
            }
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


        return res;
    }


    public static int consultarStockArticuloReserva(Connection conn, int marca, int item, int tienda) throws SQLException {
        int res = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;


        sql = "SELECT KCANT "
                + "FROM " + TABLA + " ST "
                + "WHERE KLUGAR = '" + Sesion.getTienda().getSriTienda().getCodalminterno() + "' "
                + "AND KMARCA = '" + marca + "' "
                + "AND KITEM = '" + item + "' "
                + "AND KTIPO = 52 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticulo() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                res = rs.getInt("KCANT");
            }
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


        return res;
    }

    public static List<StockKDXBean> consultarStockArticulos(Connection conn, int marca, int item) throws SQLException {
        List<StockKDXBean> res = new ArrayList<StockKDXBean>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;


        sql = "SELECT KLUGAR, KTIPO, KMARCA, KITEM, KCANT, KFECHA "
                + "FROM " + TABLA + " ST "
                + "WHERE KMARCA = '" + marca + "' "
                + "AND KITEM = '" + item + "' "
                + "AND KTIPO = 99 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticulos() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                StockKDXBean stock = new StockKDXBean();
                stock.setCantidad(rs.getInt("KCANT"));
                //stock.setFecha(rs.getDate("KFECHA"));
                stock.setLugar(rs.getInt("KLUGAR"));
                stock.setTipo(rs.getInt("KTIPO"));
                stock.setMarca(rs.getInt("KMARCA"));
                stock.setItem(rs.getInt("KITEM"));

                res.add(stock);
            }
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


        return res;
    }


    // amos
    public static int actualizarStockVenta(Connection conn, int marca, int item, int cantidad) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "UPDATE " + getNombreElementoSukasa(TABLA) + " SET KFECHA = SYSDATE, "
                + "KCANT = CASE "
                    + "WHEN KTIPO = 51 THEN KCANT + ? "
                    + "WHEN KTIPO = 99 THEN KCANT - ? "
                + "END "
                + "WHERE KMARCA = " + marca  
                + " AND KITEM = " + item  
                + " AND KLUGAR = " + Sesion.getTienda().getSriTienda().getCodalminterno()
                + " AND (KTIPO = 51 OR KTIPO = 99) ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setQueryTimeout(20);
            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, cantidad);

            log.debug("actualizarStockVenta() - " + pstmt);

            return pstmt.executeUpdate();
        }
        catch (SQLException e) {
           throw getDaoException(e);
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

    // amos
    public static int actualizarStockReserva(Connection conn, int marca, int item, int cantidad) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;

        sql = "UPDATE " + getNombreElementoSukasa(TABLA) + " SET KFECHA = SYSDATE, "
                + "KCANT = CASE "
                    + "WHEN KTIPO = 52 THEN KCANT + ? "
                    + "WHEN KTIPO = 99 THEN KCANT - ? "
                + "END "
                + "WHERE KMARCA = " + marca  
                + " AND KITEM = " + item  
                + " AND KLUGAR = " + Sesion.getTienda().getSriTienda().getCodalminterno()
                + " AND (KTIPO = 52 OR KTIPO = 99) ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, cantidad);

            log.debug("actualizarStockReserva() - " + pstmt);

            return pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw getDaoException(e);
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
    
    // amos
    public static int consultarStockArticuloTienda(Connection conn, int marca, int item) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;


        sql = "SELECT KCANT "
                + "FROM " + getNombreElementoSukasa(TABLA) 
                + "WHERE KMARCA = " + marca  
                + " AND KITEM = " + item  
                + " AND KLUGAR = " + Sesion.getTienda().getSriTienda().getCodalminterno()
                + " AND KTIPO = 99 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticuloTienda() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KCANT");
            }
            return 0;
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
    
        // SR. Metodo que consulta stock disponible en bodegas
     public static int consultarStockArticuloBodega(Connection conn, int marca, int item) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        Integer codAlmCenttralInt = new Integer(Variables.getVariable(Variables.DATABASE_CODIGO_CENTRAL_LOCAL));
        Integer codAlmCenttral2Int = new Integer(Variables.getVariable(Variables.DATABASE_CODIGO_CENTRAL2_LOCAL));

        sql = "SELECT KCANT "
                + "FROM " + getNombreElementoSukasa(TABLA) 
                + "WHERE KMARCA = " + marca  
                + " AND KITEM = " + item  
                 + " AND KLUGAR in ( " + codAlmCenttralInt + ","+ codAlmCenttral2Int + ")"
                + " AND KTIPO = 99 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticuloBodega() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KCANT");
            }
            return 0;
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
     
    public static Integer consultarExisteStockBodega(Connection conn, int marca, int item) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        Integer codAlmCenttralInt = new Integer(Variables.getVariable(Variables.DATABASE_CODIGO_CENTRAL_LOCAL));
        Integer codAlmCenttral2Int = new Integer(Variables.getVariable(Variables.DATABASE_CODIGO_CENTRAL2_LOCAL));

        sql = "SELECT KCANT "
                + "FROM " + getNombreElementoSukasa(TABLA) 
                + "WHERE KMARCA = " + marca  
                + " AND KITEM = " + item  
                + " AND KLUGAR in ( " + codAlmCenttralInt + ","+ codAlmCenttral2Int + ")"
                + " AND KTIPO = 99 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarExisteStock() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KCANT");
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
    
        // amos
    public static Integer consultarExisteStock(Connection conn, int marca, int item) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;


        sql = "SELECT KCANT "
                + "FROM " + getNombreElementoSukasa(TABLA) 
                + "WHERE KMARCA = " + marca  
                + " AND KITEM = " + item  
                + " AND KLUGAR = " + Sesion.getTienda().getSriTienda().getCodalminterno()
                + " AND KTIPO = 99 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarExisteStock() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KCANT");
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
    
    public static int consultarStockArticuloTotal (Connection conn, int marca, int item) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;


        sql = "SELECT SUM(KCANT) AS KCANT "
                + "FROM " + getNombreElementoSukasa(TABLA) 
                + "WHERE KMARCA = " + marca  
                + " AND KITEM = " + item  
                + " AND KTIPO = 99 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticuloTotal() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KCANT");
            }
            return 0;
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
    
    public static void insertStockArticulo (Connection conn, int marca, int item, int ktipo, int kcant) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = null;
        
        sql = "INSERT INTO " + getNombreElementoSukasa(TABLA) +
                "(KLUGAR, KTIPO, KMARCA, KITEM, KCANT, KFECHA) " +
                "VALUES (?, ?, ?, ?, ?, SYSDATE)";
        
        try{
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setQueryTimeout(20);
            pstmt.setInt(1, Integer.valueOf(Sesion.getTienda().getSriTienda().getCodalminterno()));
            pstmt.setInt(2, ktipo);
            pstmt.setInt(3, marca);
            pstmt.setInt(4, item);
            pstmt.setInt(5, kcant);         
            log.debug("insertStockArticulo() - " + pstmt);   
            
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
    
    public static int consultarPedidosFacturadosArticulo(Connection conn, int marca, int item) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "SELECT NVL(SUM(KCANT),0) KCANT "
                + "FROM " + getNombreElementoSukasa(TABLA) 
                + "WHERE KMARCA = " + marca  
                + " AND KITEM = " + item  
                + " AND KTIPO = 9 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarPedidosFacturadosArticulo() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KCANT");
            }
            return 0;
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
    
    /**
     * @author Gabriel Simbania
     * @param conn
     * @param marca
     * @param item
     * @return
     * @throws SQLException 
     */
    public static Long consultaDisponibleCD(Connection conn, int marca, int item) throws SQLException {
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        Long stockDisponible=0L;

        sql = "SELECT STOCK_DISPONIBLE "
                + " FROM VW_CD_STOCK_ACTUAL_DISPONIBLE@erp " 
                + " WHERE MARCA = " + marca  
                + " AND ITEM = " + item;

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultaDisponibleCD() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                stockDisponible=rs.getLong(1);
            }
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
        return stockDisponible;
    }
    
    public static int consultarStockArticuloTienda(Connection conn, String codArticulo) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;


        sql = "SELECT KDX_CANTIDAD "
                + " FROM " + getNombreElementoEmpresa(TABLA_KDX) 
                + " WHERE CODART = '" + codArticulo + "' "
                + " AND LUG_ID = " + Sesion.getTienda().getSriTienda().getCodalminterno()
                + " AND MOV_ID = 99 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticuloTienda() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KDX_CANTIDAD");
            }
            return 0;
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
    
    // SR. Metodo que consulta stock disponible en bodegas
     public static int consultarStockArticuloBodega(Connection conn, String codArticulo) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        Integer codAlmCenttralInt = new Integer(Variables.getVariable(Variables.DATABASE_CODIGO_CENTRAL_LOCAL));
        Integer codAlmCenttral2Int = new Integer(Variables.getVariable(Variables.DATABASE_CODIGO_CENTRAL2_LOCAL));

        sql = "SELECT KDX_CANTIDAD "
                + " FROM " + getNombreElementoCentral(TABLA_KDX) 
                + " WHERE CODART = '" + codArticulo + "' "
                + " AND LUG_ID in ( " + codAlmCenttralInt + ","+ codAlmCenttral2Int + ")"
                + " AND MOV_ID = 99 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticuloBodega() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KDX_CANTIDAD");
            }
            return 0;
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
     
     public static int consultarPedidosFacturadosArticulo(Connection conn, String codArticulo) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "SELECT NVL(SUM(KDX_CANTIDAD),0) KDX_CANTIDAD "
                + " FROM " + getNombreElementoCentral(TABLA_KDX) 
                + " WHERE CODART = '" + codArticulo + "' "
                + " AND MOV_ID = 9 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarPedidosFacturadosArticulo() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KDX_CANTIDAD");
            }
            return 0;
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
     
     public static int consultarStockArticuloTotal (Connection conn, String codArticulo) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;


        sql = "SELECT SUM(KDX_CANTIDAD) AS KDX_CANTIDAD "
                + " FROM " + getNombreElementoCentral(TABLA_KDX) 
                + " WHERE CODART = '" + codArticulo + "' "
                + " AND MOV_ID = 99 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticuloTotal() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KDX_CANTIDAD");
            }
            return 0;
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
     
     public static List<StockKDXBean> consultarStockArticulosPOS(Connection conn, int marca, int item) throws SQLException {
        List<StockKDXBean> res = new ArrayList<StockKDXBean>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;


        sql = "SELECT kd.LUG_ID KLUGAR, kd.MOV_ID KTIPO, it.CODMARCA KMARCA, it.COD_ITEM KITEM, kd.KDX_CANTIDAD KCANT, kd.KDX_FECHA KFECHA "
                + " FROM " + getNombreElementoCentral(TABLA_KDX) + " kd, "+ getNombreElementoCentral("D_ARTICULOS_TBL") + " it "
                + " WHERE kd.CODART = it.CODART"
                + " AND it.CODMARCA = '" + marca + "' "
                + " AND it.COD_ITEM = '" + item + "' "
                + " AND kd.MOV_ID = 99 ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticulosPOS() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                StockKDXBean stock = new StockKDXBean();
                stock.setCantidad(rs.getInt("KCANT"));
                //stock.setFecha(rs.getDate("KFECHA"));
                stock.setLugar(rs.getInt("KLUGAR"));
                stock.setTipo(rs.getInt("KTIPO"));
                stock.setMarca(rs.getInt("KMARCA"));
                stock.setItem(rs.getInt("KITEM"));

                res.add(stock);
            }
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


        return res;
    }
     
     public static int consultarStockArticuloTiendaERP(Connection conn, int codMarca, int codItem) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "SELECT kd.KDX_CANTIDAD"
                + " FROM BDG_KARDEX@erp kd, CPS_ITEM@erp it"
                + " WHERE kd.ITM_ID = it.ITM_ID"
                + " AND kd.MOV_ID = 99"
                + " AND it.MRC_ID = " + codMarca
                + " AND it.ITM_SEQ_MARCA = " + codItem
                + " AND kd.LUG_ID = " + Sesion.getTienda().getSriTienda().getCodalminterno();

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticuloTiendaERP() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KDX_CANTIDAD");
            }
            return 0;
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

    // SR. Metodo que consulta stock disponible en bodegas
    public static int consultarStockArticuloBodegaERP(Connection conn, int codMarca, int codItem) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        Integer codAlmCenttralInt = new Integer(Variables.getVariable(Variables.DATABASE_CODIGO_CENTRAL_LOCAL));
        Integer codAlmCenttral2Int = new Integer(Variables.getVariable(Variables.DATABASE_CODIGO_CENTRAL2_LOCAL));

        sql = "SELECT kd.KDX_CANTIDAD"
                + " FROM BDG_KARDEX@erp kd, CPS_ITEM@erp it"
                + " WHERE kd.ITM_ID = it.ITM_ID"
                + " AND kd.MOV_ID = 99"
                + " AND it.MRC_ID = " + codMarca
                + " AND it.ITM_SEQ_MARCA = " + codItem
                + " AND kd.LUG_ID in ( " + codAlmCenttralInt + "," + codAlmCenttral2Int + ")";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticuloBodegaERP() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KDX_CANTIDAD");
            }
            return 0;
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
    
    public static int consultarPedidosFacturadosArticuloERP(Connection conn, int codMarca, int codItem) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "SELECT NVL(SUM(kd.KDX_CANTIDAD),0) KDX_CANTIDAD"
                + " FROM BDG_KARDEX@erp kd, CPS_ITEM@erp it"
                + " WHERE kd.ITM_ID = it.ITM_ID"
                + " AND kd.MOV_ID = 99"
                + " AND it.MRC_ID = " + codMarca
                + " AND it.ITM_SEQ_MARCA = " + codItem;

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarPedidosFacturadosArticuloERP() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KDX_CANTIDAD");
            }
            return 0;
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
    
    public static int consultarStockArticuloTotalERP(Connection conn, int codMarca, int codItem) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "SELECT NVL(SUM(kd.KDX_CANTIDAD),0) KDX_CANTIDAD"
                + " FROM BDG_KARDEX@erp kd, CPS_ITEM@erp it"
                + " WHERE kd.ITM_ID = it.ITM_ID"
                + " AND kd.MOV_ID = 99"
                + " AND it.MRC_ID = " + codMarca
                + " AND it.ITM_SEQ_MARCA = " + codItem;

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticuloTotalERP() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("KDX_CANTIDAD");
            }
            return 0;
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
    
    public static List<StockKDXBean> consultarStockArticulosERP(Connection conn, int marca, int item) throws SQLException {
        List<StockKDXBean> res = new ArrayList<StockKDXBean>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "SELECT kd.LUG_ID KLUGAR, kd.MOV_ID KTIPO, it.MRC_ID KMARCA, it.ITM_SEQ_MARCA KITEM, kd.KDX_CANTIDAD KCANT, kd.KDX_FECHA KFECHA"
                + " FROM BDG_KARDEX@erp kd, CPS_ITEM@erp it"
                + " WHERE kd.ITM_ID = it.ITM_ID"
                + " AND kd.MOV_ID = 99"
                + " AND it.MRC_ID = " + marca
                + " AND it.ITM_SEQ_MARCA = " + item;

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarStockArticulosPOS() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                StockKDXBean stock = new StockKDXBean();
                stock.setCantidad(rs.getInt("KCANT"));
                //stock.setFecha(rs.getDate("KFECHA"));
                stock.setLugar(rs.getInt("KLUGAR"));
                stock.setTipo(rs.getInt("KTIPO"));
                stock.setMarca(rs.getInt("KMARCA"));
                stock.setItem(rs.getInt("KITEM"));

                res.add(stock);
            }
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


        return res;
    }
    
    /**
     * @author Gabriel Simbania
     * @param conn
     * @param codArt
     * @param movId
     * @param lugId
     * @return
     * @throws SQLException 
     */
    public static Long consultarKardex(Connection conn, String codArt, Long movId, Long lugId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT KDX_CANTIDAD CANTIDAD "
                + " FROM " + getNombreElementoEmpresa(TABLA_KDX) 
                + " WHERE CODART = '" + codArt+"' "  
                + " AND MOV_ID =" + movId
                + " AND LUG_ID="+lugId;

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarKardex() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("CANTIDAD");
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
    
    /**
     * @author Gabriel Simbania
     * @param conn
     * @param lugId
     * @param codArt
     * @param movId
     * @param kdxCantidad
     * @param itmId
     * @throws SQLException 
     */
    public static void insertaStockArticuloKardex (Connection conn, Long lugId, String codArt, Long movId, Long kdxCantidad,
            Long itmId) throws SQLException {
        PreparedStatement pstmt = null;
        String sql =  "INSERT INTO " + getNombreElementoEmpresa(TABLA_KDX) +
                "(LUG_ID, ITM_ID, CODART, MOV_ID, KDX_FECHA, KDX_CANTIDAD,KDX_VERSION) " +
                "VALUES (?, ?, ?, ?, ?, ?, 0)";
        
        try{
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setQueryTimeout(20);
            pstmt.setLong(1, lugId);
            pstmt.setLong(2, itmId);
            pstmt.setString(3, codArt);
            pstmt.setLong(4, movId);
            pstmt.setTimestamp(5, Fechas.toSqlTimestamp(new Date()));
            pstmt.setLong(6, kdxCantidad);
            log.debug("insertStockArticulo() - " + pstmt);   
            
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
    
    /**
     * @author Gabriel Simbania
     * @param conn
     * @param lugId
     * @param codArt
     * @param movId
     * @param kdxCantidad
     * @return 
     * @throws SQLException 
     */
    public static int actualizaStockArticuloKardex (Connection conn, Long lugId, String codArt, Long movId, Long kdxCantidad) throws SQLException {
       PreparedStatement pstmt = null;
        String sql = "UPDATE " + getNombreElementoEmpresa(TABLA_KDX) + " SET KDX_CANTIDAD = KDX_CANTIDAD+"+kdxCantidad+", "
                + " KDX_FECHA = SYSDATE "
                + " WHERE CODART = '" + codArt  +"' "
                + " AND LUG_ID = " + lugId  
                + " AND MOV_ID = " + movId;

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("actualizaStockArticulo() - " + pstmt);

            return pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw getDaoException(e);
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
    
        /**
     * @author Mónica Enríquez
     * @param conn
     * @param item
     * @return
     * @throws SQLException 
     */
    public static Long consultaDisponibleLocales(Connection conn, Long itmId) throws SQLException {
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        Long stockDisponible=0L;

        sql = "SELECT sum(nvl(cantidad,0)) cantidad "
                + " FROM VW_STOCK_LOCAL@erp " 
                + " WHERE ITEM = " + itmId;

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultaDisponibleLocales() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                stockDisponible=rs.getLong(1);
            }
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
        return stockDisponible;
    }
    
}
