/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.credito.politica;

import com.comerzzia.jpos.persistencia.credito.politica.ProteccionDatosDao;
import com.comerzzia.jpos.servicios.credito.CreditoException;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;

/**
 *
 * @author Gabriel Simbania
 */
public class PoliticaProteccionDatosServices {

    protected static Logger LOG_POS = Logger.getMLogger(PoliticaProteccionDatosServices.class);

    /**
     * Consulta si existe la politica de aprobación de datos
     *
     * @param codCliente
     * @return
     * @throws com.comerzzia.jpos.servicios.credito.CreditoException
     */
    public static Boolean existeAprobacionPoliticas(String codCliente) throws CreditoException {

        Connection conn = new Connection();

        try {
            LOG_POS.debug("existeAprobacionPoliticas() - Consultando  para la identificacion: " + codCliente);
            conn.abrirConexion(Database.getConnection());

            if (ProteccionDatosDao.consultaProteccionDatos(conn, codCliente) != null) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }

        } catch (SQLException e) {
            String mensaje = "Error al consultar la politica de aprobación de datos : " + e.getMessage();
            LOG_POS.error("existeAprobacionPoliticas() - " + e.getMessage(), e);
            throw new CreditoException(mensaje, e);
        } catch (Exception e) {
            String mensaje = "Error al consultar la politica de aprobación de datos : " + e.getMessage();
            LOG_POS.error("existeAprobacionPoliticas() - " + e.getMessage());
            throw new CreditoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }

    }
}
