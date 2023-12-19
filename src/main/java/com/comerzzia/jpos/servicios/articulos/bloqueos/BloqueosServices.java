/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.articulos.bloqueos;

import com.comerzzia.jpos.persistencia.articulos.bloqueos.BloqueosDao;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import java.sql.SQLException;

/**
 *
 * @author MGRI
 */
public class BloqueosServices {

    protected static Logger log = Logger.getMLogger(BloqueosServices.class);

    public static boolean isItemBloqueado(String marca, Integer item, String codArticulo) throws BloqueoFoundException {

        if (!Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_ITEM_BLOQUEO)) {
            return false;
        }
        boolean res = false;

        Integer idMarca = new Integer(marca);

        Connection conn = new Connection();
        try {
            log.debug("isItemBloqueado() - Comprobando Bloqueo de Artículo");
            conn.abrirConexion(Database.getConnection());
            res = BloqueosDao.isItemBloqueado(conn, codArticulo, Sesion.getTienda().getSriTienda().getCodalminterno());
            if (res) {
                log.debug("isItemBloqueado() - El artículo está bloqueado, comprobando su stock en tienda.");
                if (ServicioStock.getStockDisponibleVenta(codArticulo, Integer.valueOf(marca), item) < 1) {
                    throw new BloqueoFoundException("El Artículo esta bloqueado y no hay stock disponible.");
                }
            }
        } catch (BloqueoFoundException e) {
            //Si es un bloqueo, no hay stock en tienda y el artículo no está disponible
            throw e;
        } catch (Exception e) {
            String mensaje = "Error consultando bloqueo de artículo: marca:" + idMarca + "  item:" + item + " " + e.getMessage() + "\n Por favor, contacte con algún administrador.";
            log.error("isItemBloqueado() - " + mensaje, e);
            //Si hay algún error, tenemos que seguir con la compra.
            //throw new BloqueoFoundException("Error consultando artículo en tabla de bloqueo: " + e.getMessage());
        } finally {
            conn.cerrarConexion();
        }

        return res;

    }

    /**
     * @author Gabriel Simbania
     * @param codArticulo
     * @return 
     * @throws java.sql.SQLException 
     */
    public static boolean validarItemBloqueado( String codArticulo) throws SQLException {

        if (!Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_ITEM_BLOQUEO)) {
            return false;
        }
        boolean res = false;

        Connection conn = new Connection();
        try {
            log.debug("validarItemBloqueado() - Comprobando Bloqueo de Artículo");
            conn.abrirConexion(Database.getConnection());
            res = BloqueosDao.isItemBloqueado(conn, codArticulo, Sesion.getTienda().getSriTienda().getCodalminterno());

        } catch (SQLException e) {
            String mensaje = "Error consultando bloqueo de artículo: " + codArticulo + " " + e.getMessage() + "\n Por favor, contacte con algún administrador.";
            log.error("validarItemBloqueado() - " + mensaje, e);
            //Si hay algún error, tenemos que seguir con la compra.
            throw new SQLException("Error consultando artículo en tabla de bloqueo: " + e.getMessage());
        } finally {
            conn.cerrarConexion();
        }

        return res;

    }

}
