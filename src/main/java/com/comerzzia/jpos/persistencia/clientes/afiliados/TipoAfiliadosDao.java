package com.comerzzia.jpos.persistencia.clientes.afiliados;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.comerzzia.util.base.MantenimientoDao;
import com.comerzzia.jpos.util.db.Connection;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.util.HashMap;
import java.util.Map;

public class TipoAfiliadosDao extends MantenimientoDao {

    private static String TABLA_AFILIADOS = "X_TIPOS_AFILIADOS_TBL";
    private static Logger log = Logger.getMLogger(TipoAfiliadosDao.class);

    public static Map<String, TipoAfiliadoBean> consultar(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        Map<String, TipoAfiliadoBean> tipos = new HashMap<String, TipoAfiliadoBean>();

        sql = "SELECT COD_TIPO_AFILIADO, DES_TIPO_AFILIADO, COMPRA_SIGUIENTE_NIVEL, PORCENTAJE_ABONO_INICIAL FROM "
                + getNombreElementoEmpresa(TABLA_AFILIADOS) + " ORDER BY COD_TIPO_AFILIADO ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();
            TipoAfiliadoBean tipoAfiliadoAnterior = null;
            while (rs.next()) {
                TipoAfiliadoBean tipoAfiliado = new TipoAfiliadoBean();
                tipoAfiliado.setCodTipoAfiliado(rs.getString("COD_TIPO_AFILIADO"));
                tipoAfiliado.setDesTipoAfiliado(rs.getString("DES_TIPO_AFILIADO"));
                tipoAfiliado.setCompraSiguienteNivel(rs.getLong("COMPRA_SIGUIENTE_NIVEL"));
                tipoAfiliado.setPorcentajeAbonoInicial(rs.getBigDecimal("PORCENTAJE_ABONO_INICIAL").toBigInteger());
                tipos.put(tipoAfiliado.getCodTipoAfiliado(), tipoAfiliado);
                if (tipoAfiliadoAnterior != null) {
                    tipoAfiliadoAnterior.setSiguienteNivel(tipoAfiliado);
                }
                tipoAfiliadoAnterior = tipoAfiliado;
            }
            return tipos;

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
