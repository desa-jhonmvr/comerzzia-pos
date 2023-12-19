/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.sukupon;

import com.comerzzia.jpos.persistencia.promociones.cupones.CuponesDao;
import com.comerzzia.jpos.persistencia.sukupon.SukuponBean;
import com.comerzzia.jpos.persistencia.sukupon.SukuponExample;
import com.comerzzia.jpos.persistencia.sukupon.SukuponMapper;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponException;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponNotFoundException;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.session.SqlSession;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author MGRI
 */
public class SukuponServices {

    protected static Logger log = Logger.getMLogger(SukuponServices.class);

    public static List<SukuponBean> consultarLetrasPorCliente(String cedula) throws SukuponCambioException {
        SqlSession sqlSession = new SqlSession();
        try {
            log.debug("consultarLetrasPorCliente() - Consultamos bonos Efectivo del cliente: " + cedula);
            sqlSession.openSession(SessionFactory.openSession());
            SukuponMapper mapper = sqlSession.getMapper(SukuponMapper.class);

            SukuponExample example = new SukuponExample();
            example.or().andCodClienteEqualTo(cedula);
            List<SukuponBean> sukupon = mapper.selectFromByExample(example);
            return sukupon;
        } catch (Exception e) {
            String msg = "Error consultando bonoEfectivo " + cedula;
            log.error("consultarLetrasPorCliente() - " + msg, e);
            throw new SukuponCambioException(msg, e);
        } finally {
            sqlSession.close();
        }
    }

    public static List<SukuponBean> consultarIdCupon(Long idCupon) throws SukuponCambioException {
        Connection conn = new Connection();
        List<SukuponBean> sukuponBean = null;

        try {
            log.debug("consultarCuponDescuento() - Consultando cupon: " + idCupon + " expedido en la tienda: ");
            conn.abrirConexion(Database.getConnection());
            sukuponBean = CuponesDao.consultarIdCupon(conn, idCupon, Sesion.getTienda().getCodalm());
            if (sukuponBean == null) {
                throw new CuponNotFoundException("El cupón consultado no se encuentra en el sistema: IdCupon: " + idCupon + " Tienda: ");
            }
        } catch (SQLException e) {
            try {
                log.error("consultarCuponDescuento() - " + e.getMessage());
                String mensaje = "Error al consultar cupon: " + e.getMessage();
                throw new CuponException(mensaje, e);
            } catch (CuponException ex) {
                java.util.logging.Logger.getLogger(SukuponServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (CuponNotFoundException ex) {
            java.util.logging.Logger.getLogger(SukuponServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conn.cerrarConexion();
        }
        return sukuponBean;
    }

}
