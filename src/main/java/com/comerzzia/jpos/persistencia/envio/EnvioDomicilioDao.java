/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.envio;

import com.comerzzia.jpos.entity.db.EnvioDomicilioTbl;
import com.comerzzia.jpos.servicios.envio.domicilio.GvtParametroEntregaBean;
import com.comerzzia.jpos.util.db.Connection;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Mónica Enríquez
 */
public class EnvioDomicilioDao {

    private static Logger log = Logger.getMLogger(EnvioDomicilioDao.class);

    public static void crear(EnvioDomicilioTbl envioDomicilio, EntityManager em) throws Exception {
        em.persist(envioDomicilio);
    }

    public static List<GvtParametroEntregaBean> getParametrosEntrega(Connection conn, String lugar, Long camion) throws SQLException {
        List<GvtParametroEntregaBean> res = new ArrayList<GvtParametroEntregaBean>();
//        Date fechaActual = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = sdf.format(new Date());
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "select PEN_ID, PEN_NUMERO_CAMION,PEN_CANTIDAD,PEN_DOMINGO, PEN_LUNES,PEN_MARTES, PEN_MIERCOLES, PEN_JUEVES, PEN_VIERNES,PEN_SABADO "
                + " from Gvt_Parametro_Entrega@erp gvt "
                + " WHERE 1= 1 "
                + " and  lug_id in (select lug_id "
                + " from cor_lugar@erp "
                + " where  lug_codigo_sri = '" + lugar + "')"
                + " and gvt.pen_fecha_desde <= sysdate "
                + " and gvt.pen_fecha_hasta >= sysdate "
                + " and gvt.pen_estado = 1 ";
        if (camion != null) {
            sql = sql + " and pen_numero_camion = " + camion + " order by 1 ";
        } else {
            sql = sql + " order by 1";
        }

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarCamionEntregaDomicilio() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                GvtParametroEntregaBean parametro = new GvtParametroEntregaBean();
                parametro.setPenNumeroCamion(rs.getLong("PEN_NUMERO_CAMION"));
                parametro.setPenCantidad(rs.getLong("PEN_CANTIDAD"));
                parametro.setPenDomingo(rs.getLong("PEN_DOMINGO"));
                parametro.setPenLunes(rs.getLong("PEN_LUNES"));
                parametro.setPenMartes(rs.getLong("PEN_MARTES"));
                parametro.setPenMiercoles(rs.getLong("PEN_MIERCOLES"));
                parametro.setPenJueves(rs.getLong("PEN_JUEVES"));
                parametro.setPenViernes(rs.getLong("PEN_VIERNES"));
                parametro.setPenSabado(rs.getLong("PEN_SABADO"));
                parametro.setPenId(rs.getLong("PEN_ID"));
                res.add(parametro);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }

        return res;
    }

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Valida que el cami&oacute;n para la fecha indicada est&eacute; con los
     * cupos disponibles
     * </p>
     *
     * @param conn
     * @param penId
     * @param fechaEntrega
     * @return
     * @throws SQLException
     */
    public static boolean validarCuposDisponiblesPorFechaCamion(Connection conn, Long penId, String fechaEntrega) throws SQLException {

        boolean fechaValida = Boolean.FALSE;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sqlBuilder = new StringBuilder("select sum(nvl(G.AEN_CANTIDAD_CUPOS,1)) cupos_utilizados, P.PEN_CANTIDAD cupos");
        sqlBuilder.append(" from GVT_ASIGNACION_ENTREGA@erp g , Gvt_Parametro_Entrega@erp p ");
        sqlBuilder.append(" where G.PEN_ID=  ?");
        sqlBuilder.append(" and to_date(to_char(G.AEN_FECHA,'dd/MM/yyyy'),'dd/MM/yyyy') =to_date('").append(fechaEntrega).append("','dd/MM/yyyy')");
        sqlBuilder.append(" and  G.PEN_ID= P.PEN_ID ");
        sqlBuilder.append(" group by P.PEN_CANTIDAD");
        sqlBuilder.append(" order by 1 desc");

        try {
            pstmt = new PreparedStatement(conn, sqlBuilder.toString());
            log.debug("validarCuposDisponiblesPorFechaCamion() - " + pstmt);

            pstmt.setLong(1, penId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                long cuposUtilizados = rs.getLong("cupos_utilizados");
                long cuposDisponibles = rs.getLong("cupos");
                if (cuposUtilizados < cuposDisponibles) {
                    fechaValida = Boolean.TRUE;
                }

            } else {
                fechaValida = Boolean.TRUE;
            }
        } finally {

            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        return fechaValida;
    }

}
