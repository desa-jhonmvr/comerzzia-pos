/**
 * Copyright 2009-2011 RED.ES - Desarrollado por MP Sistemas Licencia con arreglo a la EUPL, versión 1.1 o -en cuanto
 * sean aprobadas por la comisión Europea- versiones posteriores de la EUPL (la "Licencia"). Solo podrá usarse esta obra
 * si se respeta la Licencia. http://ec.europa.eu/idabc/eupl.html Salvo cuando lo exija la legislación aplicable o se
 * acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye "TAL CUAL", SIN GARANTÍAS NI
 * CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. Véase la Licencia en el idioma concreto que rige los permisos
 * y limitaciones que establece la Licencia.
 */
package com.comerzzia.jpos.persistencia.clientes.tiposclientes;

import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TiposClientesDao extends MantenimientoDao {

    private static String TABLA = "X_TIPOS_CLIENTES_TBL";
    private static Logger log = Logger.getMLogger(TiposClientesDao.class);

    public static List<TipoClienteBean> consultar(Connection conn) throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<TipoClienteBean> lstTipoClientes = new ArrayList<TipoClienteBean>();
        String sql = null;

        sql = "SELECT COD_TIPO_CLIENTE, DES_TIPO_CLIENTE,POST_FECHAR_VOUCHER, REQUIERE_AUTO_VENTA, SOLICITA_TARJETA_SUPERMAXI "
                + "FROM " + getNombreElementoEmpresa(TABLA);

        try {
            pstmt = new PreparedStatement(conn, sql);

            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            TipoClienteBean tipoCliente;
            while (rs.next()) {
                tipoCliente = new TipoClienteBean();
                tipoCliente.setCodTipoCliente(rs.getLong("COD_TIPO_CLIENTE"));
                tipoCliente.setDesTipoCliente(rs.getString("DES_TIPO_CLIENTE"));
                tipoCliente.setPostFecharVoucher(rs.getString("POST_FECHAR_VOUCHER").equals("S")?true:false);
                tipoCliente.setRequiereAutorizacionEnVenta(rs.getString("REQUIERE_AUTO_VENTA").equals("S")?true:false);
                tipoCliente.setSolicitaTarjetaSupermaxi(rs.getString("SOLICITA_TARJETA_SUPERMAXI").equals("S")?true:false);
                lstTipoClientes.add(tipoCliente);
            }

            return lstTipoClientes;
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