/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.envio.domicilio;

import com.comerzzia.jpos.dto.envioDomicilio.EnvioDomicilioDTO;
import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.ProcesoEnvioDomicilioDTO;
import com.comerzzia.jpos.entity.db.EnvioDomicilioTbl;
import com.comerzzia.jpos.persistencia.envio.EnvioDomicilioDao;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.enums.EnumEstado;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Mónica Enríquez
 */
public class ServicioEnvioDomicilio {

    protected static Logger log = Logger.getMLogger(ServicioEnvioDomicilio.class);

    /**
     *
     * @param procesoEnvioDomicilioDTO
     * @param em
     * @throws Exception
     */
    public static void insertarEnvioDomicilio(ProcesoEnvioDomicilioDTO procesoEnvioDomicilioDTO, EntityManager em) throws Exception {

        try {
            for (EnvioDomicilioDTO envioD : procesoEnvioDomicilioDTO.getListEnvioDomicilioDTO()) {
                for (ItemDTO item : envioD.getItemDtoLista()) {
                    EnvioDomicilioTbl envioDomicilio = new EnvioDomicilioTbl(envioD.getUidTicket(), envioD.getDatosContacto().getIdentificacion(), envioD.getDatosContacto().getNombres(), envioD.getDatosContacto().getApellidos(), envioD.getDatosContacto().getDireccion(), envioD.getDatosContacto().getProvincia(), envioD.getDatosContacto().getEmail(), envioD.getDatosContacto().getNumeroTelefono(), envioD.getDatosContacto().getNumeroCelular(), envioD.getDatosCamion().getHorario(), envioD.getDatosCamion().getCamion(), envioD.getDatosCamion().getFechaEntrega(), envioD.getObservacion(), item.getCodigoI(), item.getCantidad(), item.getEntregaDomicilio(), envioD.getLugar(), procesoEnvioDomicilioDTO.getFactura(), envioD.getVendedor(), EnumEstado.ACTIVO);
                    EnvioDomicilioDao.crear(envioDomicilio, em);
                }
            }
        } catch (SQLException e) {
            log.error("insertarEnvioDomicilio() - " + e.getMessage());
            String mensaje = "Error al actualizar en envio a domicilio: " + e.getMessage();
            throw new Exception(mensaje, e);
        }
    }

    /**
     * @author Mónica Enríquez
     * @param lugId
     * @return
     * @throws Exception
     */
    public static List<GvtParametroEntregaBean> getParametrosEntrega(String lugar, Long camion) throws Exception {
        Connection conn = new Connection();

        try {
            conn.abrirConexion(Database.getConnection());
            List<GvtParametroEntregaBean> envioDomicilioBean = new ArrayList<>();
            envioDomicilioBean = EnvioDomicilioDao.getParametrosEntrega(conn, lugar, camion);
            return envioDomicilioBean;
        } catch (SQLException e) {
            log.error("getParametrosEntrega() - " + e.getMessage());
            String mensaje = "Error consultando Parametro camión: " + e.getMessage();
            throw new Exception(mensaje, e);
        } catch (Exception e) {
            log.error("getParametrosEntrega() - " + e.getMessage());
            String mensaje = "Error consultando Stock de tiendas: " + e.getMessage();
            throw new Exception(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Valida que el cami&oacute;n para la fecha indicada est&eacute; con los
     * cupos disponibles  </p>
     *
     * @param penId
     * @param fechaEntrega
     * @return
     * @throws Exception
     */
    public static boolean validarCuposDisponiblesPorFechaCamion(Long penId, String fechaEntrega) throws Exception {
        Connection conn = new Connection();

        try {
            conn.abrirConexion(Database.getConnection());

            return EnvioDomicilioDao.validarCuposDisponiblesPorFechaCamion(conn, penId, fechaEntrega);

        } catch (SQLException e) {
            log.error("validarCuposDisponiblesPorFechaCamion() - " + e.getMessage());
            String mensaje = "Error consultando los cupos disponibles: " + e.getMessage();
            throw new Exception(mensaje, e);
        } catch (Exception e) {
            log.error("validarCuposDisponiblesPorFechaCamion() - " + e.getMessage());
            String mensaje = "Error consultando los cupos disponibles: " + e.getMessage();
            throw new Exception(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

}
