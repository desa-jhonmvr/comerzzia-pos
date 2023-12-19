/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.webservice.credito;

import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.util.base.MantenimientoDao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DESARROLLO
 */
public class CreditoDao extends MantenimientoDao {

    public static VwBalance[] getEstado(String cuenta) throws SQLException {

        VwBalance item;
        String query;
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection conn = null;
        List lista = new ArrayList();
        String connectionUrl = Variables.getVariable(Variables.DATABASE_CREDITO_URL) + ";user=" + Variables.getVariable(Variables.DATABASE_CREDITO_USUARIO) + "; password=" + "" + Variables.getVariable(Variables.DATABASE_CREDITO_PASSWORD) + ";";
        conn = DriverManager.getConnection(connectionUrl);
        try {
            query = "SELECT vw_ciclofacturacion,vw_totalapagar from vw_balance where vw_cuenta = '" + cuenta + "' ";
            st = conn.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                item = new VwBalance();
                item.setCicloFacturacion(rs.getLong("vw_ciclofacturacion"));
                item.setTotalPagar(rs.getBigDecimal("vw_totalapagar"));
                lista.add(item);
            }

            return (VwBalance[]) lista.toArray(new VwBalance[lista.size()]);

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     *
     * @param credito
     * @return
     * @throws SQLException
     */
    public static String getNumeroTelefono(Long credito) throws SQLException {

        ResultSet rs = null;
        PreparedStatement st = null;
        String numeroTelefono = null;
        String connectionUrl = Variables.getVariable(Variables.DATABASE_CREDITO_URL) + ";user=" + Variables.getVariable(Variables.DATABASE_CREDITO_USUARIO) + "; password=" + "" + Variables.getVariable(Variables.DATABASE_CREDITO_PASSWORD) + ";";
        Connection conn = DriverManager.getConnection(connectionUrl);
        try {
            String query = "SELECT tf_telefono from nts_clientes.dbo.vw_telefonosms  where ma_cuenta = " + credito;
            st = conn.prepareStatement(query);
            rs = st.executeQuery();
            if (rs.next()) {
                numeroTelefono = rs.getString("tf_telefono");
            }

            return numeroTelefono;

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }

        }
    }

}
