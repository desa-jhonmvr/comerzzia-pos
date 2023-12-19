/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.bancos;

import com.comerzzia.jpos.persistencia.mediospagos.BancoBean;
import com.comerzzia.jpos.persistencia.mediospagos.MediosPagoDao;
import com.comerzzia.jpos.servicios.garantia.GarantiaExtendidaServices;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class BancoServices {

    private static final Logger LOG = Logger.getMLogger(BancoServices.class);

    public List<BancoBean> consultarBancos() throws MedioPagoException {
        Connection conn = new Connection();

        try {

            conn.abrirConexion(Database.getConnection());
            List<BancoBean> bancos = MediosPagoDao.consultarBancos(conn);

            return bancos;
        } catch (SQLException e) {
            LOG.error("consultarBancos() - " + e.getMessage());
            String mensaje = "Error al consultar los bancos: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } catch (Exception e) {
            LOG.error("consultarBancos() - " + e.getMessage());
            String mensaje = "Error inesperado consultar los bancos: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

}
