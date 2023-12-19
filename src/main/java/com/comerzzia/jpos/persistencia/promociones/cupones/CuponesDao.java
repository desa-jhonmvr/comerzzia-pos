package com.comerzzia.jpos.persistencia.promociones.cupones;

import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.persistencia.promociones.TipoPromocionBean;
import com.comerzzia.jpos.persistencia.sukupon.SukuponBean;
import com.comerzzia.jpos.servicios.promociones.articulos.SukuponLinea;
import es.mpsistemas.util.fechas.Fecha;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.Connection;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CuponesDao extends MantenimientoDao {

    private static String TABLA = "X_CUPONES_TBL";
    private static String TABLA_DETALLE = "X_CUPONES_DET_TBL";
    private static String TABLA_PROMOCIONES = "D_PROMOCIONES_CAB_TBL";

    private static Logger log = Logger.getMLogger(CuponesDao.class);

    public static boolean existenCuponesCliente(Connection conn, String codCliente, Long idPromocion, Fecha fecha) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Consultas
        String sql = "SELECT *  FROM " + getNombreElementoEmpresa(TABLA)
                + "WHERE CODCLI = ? AND ID_PROMOCION = ? AND FECHA_EXPEDICION > ? ";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, codCliente);
            pstmt.setLong(2, idPromocion);
            pstmt.setTimestamp(3, fecha.getTimestamp());
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
            return rs.next();
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

    public static Cupon consultar(Connection conn, Long idCupon, String codAlmacen) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Consultas
        String sql = "SELECT ID_CUPON, CODALM, FECHA_EXPEDICION, ID_PROMOCION, "
                + "REFERENCIA_USO, TIPO_REFERENCIA_USO, REFERENCIA_ORIGEN, "
                + "TIPO_REFERENCIA_ORIGEN, UTILIZADO, CODCLI, PROCESADO, "
                + "FECHA_PROCESO, MENSAJE_PROCESO, VERSION, FECHA_VERSION, VARIABLE, FECHA_VALIDEZ,SALDO "
                + "FROM " + getNombreElementoEmpresa(TABLA)
                + "WHERE ID_CUPON = ? AND CODALM = ? ";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, idCupon);
            pstmt.setString(2, codAlmacen);
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Cupon(rs);
            }
            return null;
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

    public static List<SukuponBean> consultarIdCupon(Connection conn, Long idCupon, String codAlmacen) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        // Consultas
//        String sql = "SELECT A.ID_CUPON, A.SALDO, UID_NOTA_CREDITO, UID_TICKET  "
//                + "FROM " + getNombreElementoEmpresa(TABLA) + " A, " + getNombreElementoEmpresa(TABLA_DETALLE) + " B "
//                + "WHERE A.ID_CUPON = B.ID_CUPON   "
//                + "AND A.ID_CUPON = ? AND CODALM = ? ";
//        try {
//            pstmt = new PreparedStatement(conn, sql);
//            pstmt.setLong(1, idCupon);
//            pstmt.setString(2, codAlmacen);
//            log.debug("consultar() - " + pstmt);
//            rs = pstmt.executeQuery();
//            if (rs.next()) {
//                return (List<SukuponDetalle>) new SukuponDetalle(rs);
//            }
//            return null;
//        } finally {
//            try {
//                rs.close();
//            } catch (Exception ignore) {;
//            }
//            try {
//                pstmt.close();
//            } catch (Exception ignore) {;
//            }
//        }

        List<SukuponBean> res = new ArrayList<SukuponBean>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        Fecha fechaValidez;
        Fecha fechaExpedicion;

        sql = "SELECT a.codalm, B.CODART ID_CUPON, A.CODBARRAS,\n"
                + "    FECHA_VALIDEZ,\n"
                + "       FECHA_EXPEDICION,\n"
                + "       status utilizado,valor variable,\n"
                + "       b.utilizado util,\n"
                + "       b.saldo,  "
                + "  (select codalm||'-'||codcaja||'-'||ID_NOTA_CREDITO\n"
                + "       from X_NOTAS_CREDITO_TBL c\n"
                + "       where b.UID_NOTA_CREDITO =  c.UID_NOTA_CREDITO) notaCredito,\n"
                + "       (select codalm||'-'||codcaja||'-'||id_ticket\n"
                + "       from d_tickets_tbl c\n"
                + "       where b.UID_TICKET =  c.UID_TICKET) factura "
                + "FROM " + getNombreElementoEmpresa(TABLA) + " A, " + getNombreElementoEmpresa(TABLA_DETALLE) + " B "
                + "WHERE A.ID_CUPON = B.ID_CUPON   "
                + "AND A.ID_CUPON = " + idCupon + " AND CODALM = " + codAlmacen;

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarIdCupon() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                SukuponBean sukupon = new SukuponBean();
                sukupon.setCodAlm(rs.getString("CODALM"));
                sukupon.setIdCupon(rs.getString("ID_CUPON"));
                sukupon.setCodBarras(rs.getString("CODBARRAS"));
                fechaValidez = Fecha.getFecha(rs.getTimestamp("FECHA_VALIDEZ"));
                sukupon.setFechaValidez(fechaValidez);
                fechaExpedicion = new Fecha(rs.getDate("FECHA_EXPEDICION"));
                sukupon.setFechaExpedicion(fechaValidez);
                sukupon.setTotal(rs.getBigDecimal("VARIABLE"));
                sukupon.setUtilizado(rs.getString("UTILIZADO"));
                sukupon.setUtilizoItem(rs.getBigDecimal("UTIL"));
                sukupon.setSaldoItem(rs.getBigDecimal("SALDO"));
                sukupon.setNotaCredito(rs.getString("NOTACREDITO"));
                sukupon.setFactura(rs.getString("FACTURA"));
                res.add(sukupon);
            }
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

        return res;

    }

    /**
     * @author Gabriel Simbania
     * @description consulta para validacion de un sukupon en la tabla
     * x_cupones_tbl
     * @param conn
     * @param codAlmacen
     * @param caja
     * @param codcliente
     * @param uidTicket
     * @return
     * @throws SQLException
     */
    public static Cupon consultarCunponTarjet(Connection conn, String codAlmacen, String caja, String codcliente, String uidTicket) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Consultas
        String sql = " SELECT ID_CUPON, CODALM, FECHA_EXPEDICION, ID_PROMOCION, "
                + " REFERENCIA_USO, TIPO_REFERENCIA_USO, REFERENCIA_ORIGEN, "
                + " TIPO_REFERENCIA_ORIGEN, UTILIZADO, CODCLI, PROCESADO, "
                + " FECHA_PROCESO, MENSAJE_PROCESO, VERSION, FECHA_VERSION, VARIABLE, FECHA_VALIDEZ "
                + " FROM " + getNombreElementoEmpresa(TABLA)
                + " WHERE CODALM = ? AND CODCLI = ? AND VARIABLE IS NOT NULL"
                + " and REFERENCIA_ORIGEN=?";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, codAlmacen);
            pstmt.setString(2, codcliente);
            pstmt.setString(3, uidTicket);
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Cupon(rs);
            }
            return null;
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

    public static void insert(Connection conn, Cupon cupon) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO " + getNombreElementoEmpresa(TABLA)
                + "(ID_CUPON, CODALM, FECHA_EXPEDICION, ID_PROMOCION, REFERENCIA_ORIGEN, TIPO_REFERENCIA_ORIGEN, UTILIZADO, CODCLI, CODBARRAS, PROCESADO, VARIABLE, FECHA_VALIDEZ, SALDO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, cupon.getIdCupon());
            pstmt.setString(2, cupon.getCodAlmacen());
            pstmt.setTimestamp(3, cupon.getFechaExpedicion());
            pstmt.setLong(4, cupon.getPromocion().getIdPromocion());
            pstmt.setString(5, cupon.getReferenciaOrigen());
            pstmt.setString(6, cupon.getTipoReferenciaOrigen());
            pstmt.setString(7, "N");
            pstmt.setString(8, cupon.getCodCliente());
            pstmt.setString(9, cupon.getCodBarras());
            pstmt.setString(10, "N");
            pstmt.setString(11, cupon.getVariable());
            pstmt.setTimestamp(12, cupon.getFechaValidez());
            if (cupon.getIdTipoPromocion() != null) {
                if (Objects.equals(cupon.getIdTipoPromocion(), TipoPromocionBean.TIPO_PROMOCION_BILLETON)) {
                    pstmt.setBigDecimal(13, new BigDecimal(cupon.getVariable()));
                } else {
                    pstmt.setBigDecimal(13, null);
                }
            } else {
                pstmt.setBigDecimal(13, null);
            }

            log.debug("insert() - " + pstmt);
            pstmt.execute();
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void insertDetCupon(Connection conn, SukuponLinea sukuponLinea) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO " + getNombreElementoEmpresa(TABLA_DETALLE)
                + "(ID_CUPON, ID_LINEA,CODART, IMPORTE_ORIGEN, CANTIDAD, IMPORTE_FINAL, VALOR,UTILIZADO,SALDO, STATUS, ESTADO, CODALM, PROCESADO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, sukuponLinea.getIdCupon());
            pstmt.setInt(2, sukuponLinea.getIdlinea());
            pstmt.setString(3, sukuponLinea.getArticulo().getCodart());
            pstmt.setBigDecimal(4, sukuponLinea.getImporte());
            pstmt.setInt(5, sukuponLinea.getCantidad());
            pstmt.setBigDecimal(6, sukuponLinea.getImporteTotal());
            pstmt.setBigDecimal(7, sukuponLinea.getValor());
            pstmt.setBigDecimal(8, sukuponLinea.getUtilizado());
            pstmt.setBigDecimal(9, sukuponLinea.getSaldo());
            pstmt.setString(10, "N");
            pstmt.setLong(11, 1L);
            pstmt.setString(12, sukuponLinea.getCodAlm());
            pstmt.setString(13, "N");

            log.debug("insert() - " + pstmt);
            pstmt.execute();
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void updateUso(Connection conn, Cupon cupon) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET UTILIZADO = ?, REFERENCIA_USO = ?, TIPO_REFERENCIA_USO = ?, PROCESADO = ? , SALDO = ? "
                + "WHERE ID_CUPON = ? AND CODALM = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, "S");
            pstmt.setString(2, cupon.getReferenciaUso());
            pstmt.setString(3, cupon.getTipoReferenciaUso());
            pstmt.setString(4, "N");
            pstmt.setString(5, null);
            pstmt.setLong(6, cupon.getIdCupon());
            pstmt.setString(7, cupon.getCodAlmacen());

            log.debug("updateUso() - " + pstmt);
            pstmt.execute();
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void updateUsoDetalle(Connection conn, Cupon cupon) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getNombreElementoEmpresa(TABLA_DETALLE) + " a "
                + " SET STATUS = ?,UID_TICKET = ? , SALDO = ? "
                + " where exists(select   null from " + getNombreElementoEmpresa(TABLA) + "  b "
                + " where a.id_cupon = b.id_cupon "
                + " and   b.id_cupon = ? )"
                + " and   STATUS = 'N' ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, "S");
            pstmt.setString(2, cupon.getReferenciaUso());
            pstmt.setString(3, "0");
            pstmt.setLong(4, cupon.getIdCupon());

            log.debug("desactivarCuponesEmitidosEnFactura() - " + pstmt);
            pstmt.execute();
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static boolean consultarCuponesUsados(com.comerzzia.jpos.util.db.Connection conn, String uidTicket) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Consultas
        String sql = "SELECT ID_CUPON, CODALM, FECHA_EXPEDICION, ID_PROMOCION, "
                + "REFERENCIA_USO, TIPO_REFERENCIA_USO, REFERENCIA_ORIGEN, "
                + "TIPO_REFERENCIA_ORIGEN, UTILIZADO, CODCLI, PROCESADO, "
                + "FECHA_PROCESO, MENSAJE_PROCESO, VERSION, FECHA_VERSION, VARIABLE, FECHA_VALIDEZ "
                + "FROM " + getNombreElementoEmpresa(TABLA)
                + "WHERE REFERENCIA_ORIGEN = ? AND UTILIZADO = ? ";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, uidTicket);
            pstmt.setString(2, "S");
            log.debug("copnsultarCuponesUsados() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
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

    public static Cupon consultarSukuponFactura(com.comerzzia.jpos.util.db.Connection conn, String uidTicket) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Consultas
        String sql = "SELECT CUP.ID_CUPON, CUP.CODALM, CUP.FECHA_EXPEDICION, CUP.ID_PROMOCION, "
                + "CUP.REFERENCIA_USO, CUP.TIPO_REFERENCIA_USO, CUP.REFERENCIA_ORIGEN, "
                + "CUP.TIPO_REFERENCIA_ORIGEN, CUP.UTILIZADO, CUP.CODCLI, CUP.PROCESADO, "
                + "CUP.FECHA_PROCESO, CUP.MENSAJE_PROCESO, CUP.VERSION, CUP.FECHA_VERSION, CUP.VARIABLE, CUP.FECHA_VALIDEZ,CUP.SALDO "
                + "FROM " + getNombreElementoEmpresa(TABLA) + " CUP "
                + "INNER JOIN " + getNombreElementoEmpresa(TABLA_PROMOCIONES) + " PRO ON (CUP.ID_PROMOCION = PRO.ID_PROMOCION) "
                + "WHERE CUP.REFERENCIA_ORIGEN = ? AND PRO.ID_TIPO_PROMOCION = 20 ";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, uidTicket);
            log.debug("consultarSukuponFactura() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Cupon(rs);
            }
            return null;
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

    public static void activarCuponesUsadosEnFactura(com.comerzzia.jpos.util.db.Connection conn, String uidTicket) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET UTILIZADO = ?, REFERENCIA_USO = ?, TIPO_REFERENCIA_USO = ?, PROCESADO = ? "
                + "WHERE REFERENCIA_USO = ?  ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, "N");
            pstmt.setString(2, null);
            pstmt.setString(3, null);
            pstmt.setString(4, "N");
            pstmt.setString(5, uidTicket);

            log.debug("activarCuponesUsadosEnFactura() - " + pstmt);
            pstmt.execute();
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void desactivarCuponesEmitidosEnFactura(com.comerzzia.jpos.util.db.Connection conn, String uidTicket, BigDecimal saldo) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET UTILIZADO = ?, PROCESADO = ? ,SALDO = ?"
                + "WHERE REFERENCIA_ORIGEN = ?  ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, "S");
            pstmt.setString(2, "N");
            pstmt.setBigDecimal(3, saldo);
            pstmt.setString(4, uidTicket);

            log.debug("desactivarCuponesEmitidosEnFactura() - " + pstmt);
            pstmt.execute();
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void desactivarCuponesEmitidosEnFacturaDet(com.comerzzia.jpos.util.db.Connection conn, String uidTicket, String codArt, String uidNotaCredito) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getNombreElementoEmpresa(TABLA_DETALLE) + " a "
                + " SET STATUS = ?,uid_nota_credito = ? , SALDO = ? , UTILIZADO = ? "
                + " where exists(select   null from " + getNombreElementoEmpresa(TABLA) + "  b "
                + " where a.id_cupon = b.id_cupon "
                + " and   REFERENCIA_ORIGEN = ? )"
                + " and codart = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, "S");
            pstmt.setString(2, uidNotaCredito);
            pstmt.setString(3, "0");
            pstmt.setString(4, "0");
            pstmt.setString(5, uidTicket);
            pstmt.setString(6, codArt);

            log.debug("desactivarCuponesEmitidosEnFactura() - " + pstmt);
            pstmt.execute();
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static Cupon consultarCuponesEmitidosEnFactura(com.comerzzia.jpos.util.db.Connection conn, String uidTicket) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Consultas
        String sql = "SELECT *  FROM " + getNombreElementoEmpresa(TABLA)
                + " where   REFERENCIA_ORIGEN = ? "
                + " AND VARIABLE IS NOT NULL ";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, uidTicket);
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Cupon(rs);
            }
            return null;
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

    public static Cupon consultarCuponesEmitidosEnFacturaSaldo(com.comerzzia.jpos.util.db.Connection conn, String uidTicket) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Consultas
        String sql = "SELECT *  FROM " + getNombreElementoEmpresa(TABLA)
                + " where   REFERENCIA_ORIGEN = ? "
                + " AND SALDO IS NOT NULL ";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, uidTicket);
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Cupon(rs);
            }
            return null;
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

    public static Cupon consultarCuponesEmitidosEnFacturaDet(com.comerzzia.jpos.util.db.Connection conn, Long idCupon, String codArt) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Cupon cuponDet = new Cupon();

        // Consultas
        String sql = "SELECT ID_CUPON, CODART,CANTIDAD,VALOR,UTILIZADO,SALDO  FROM " + getNombreElementoEmpresa(TABLA_DETALLE)
                + " where   ID_CUPON = ? "
                + " and codart = ?  ";
        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setLong(1, idCupon);
            pstmt.setString(2, codArt);
            log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                cuponDet = new Cupon();
                cuponDet.setIdCupon(rs.getLong("ID_CUPON"));
                cuponDet.setCodart(rs.getString("CODART"));
                cuponDet.setCantidad(rs.getLong("CANTIDAD"));
                cuponDet.setValor(rs.getString("VALOR"));
                cuponDet.setUtilizadoValor(rs.getString("UTILIZADO"));
                cuponDet.setSaldo(rs.getString("SALDO"));
            }
            return cuponDet;
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

    public static void desactivarCuponEnFactura(com.comerzzia.jpos.util.db.Connection conn, String uidTicket, BigDecimal saldo) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET UTILIZADO = ?, PROCESADO = ? ,SALDO = ?"
                + "WHERE REFERENCIA_ORIGEN = ?  "
                + "AND SALDO IS NOT NULL";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, "N");
            pstmt.setString(2, "N");
            pstmt.setBigDecimal(3, saldo);
            pstmt.setString(4, uidTicket);

            log.debug("desactivarCuponesEmitidosEnFactura() - " + pstmt);
            pstmt.execute();
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void desactivarCuponEnFacturaTotal(com.comerzzia.jpos.util.db.Connection conn, String uidTicket, BigDecimal saldo) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET UTILIZADO = ?, PROCESADO = ? ,SALDO = ?"
                + "WHERE REFERENCIA_USO = ?  "
                + "AND SALDO IS NOT NULL";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, "S");
            pstmt.setString(2, "N");
            pstmt.setBigDecimal(3, saldo);
            pstmt.setString(4, uidTicket);

            log.debug("desactivarCuponesEmitidosEnFactura() - " + pstmt);
            pstmt.execute();
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void desactivarCuponesEmitidosFactura(com.comerzzia.jpos.util.db.Connection conn, String uidTicket, Long promocion) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getNombreElementoEmpresa(TABLA)
                + "SET UTILIZADO = ?, PROCESADO = ? "
                + "WHERE REFERENCIA_ORIGEN = ?  "
                + "AND ID_PROMOCION != ?";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, "S");
            pstmt.setString(2, "N");
            pstmt.setString(3, uidTicket);
            pstmt.setLong(4, promocion);

            log.debug("desactivarCuponesEmitidosEnFactura() - " + pstmt);
            pstmt.execute();
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

}
