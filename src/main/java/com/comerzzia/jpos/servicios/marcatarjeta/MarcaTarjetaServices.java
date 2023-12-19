/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.marcatarjeta;

import com.comerzzia.jpos.persistencia.mediospagos.MarcaTarjetaBean;
import com.comerzzia.jpos.persistencia.mediospagos.MarcaTarjetaDao;
import com.comerzzia.jpos.persistencia.mediospagos.MarcaTarjetaMedioPagoBean;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class MarcaTarjetaServices {

    private static final Logger LOG = Logger.getMLogger(MarcaTarjetaServices.class);

    public List<MarcaTarjetaBean> consultarMarcasTarjeta() throws MedioPagoException {
        Connection conn = new Connection();

        try {
            conn.abrirConexion(Database.getConnection());
            return MarcaTarjetaDao.consultarMarcasTarjeta(conn);

        } catch (SQLException e) {
            LOG.error("consultarMarcasTarjeta() - " + e.getMessage());
            String mensaje = "Error al consultar las marcas de la tarjeta: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } catch (Exception e) {
            LOG.error("consultarMarcasTarjeta() - " + e.getMessage());
            String mensaje = "Error inesperado consultar las marcas de la tarjeta: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    /**
     *
     * @param codMarcaTarjeta
     * @return
     * @throws MedioPagoException
     */
    public List<MarcaTarjetaMedioPagoBean> consultarMarcasTarjeta(String codMarcaTarjeta) throws MedioPagoException {
        Connection conn = new Connection();

        try {
            conn.abrirConexion(Database.getConnection());
            return MarcaTarjetaDao.consultarMarcasTarjetaMedioPagoByMarca(conn, codMarcaTarjeta);

        } catch (SQLException e) {
            LOG.error("consultarMarcasTarjeta() - " + e.getMessage());
            String mensaje = "Error al consultar las marcas de la tarjeta: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } catch (Exception e) {
            LOG.error("consultarMarcasTarjeta() - " + e.getMessage());
            String mensaje = "Error inesperado consultar las marcas de la tarjeta: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }
}
