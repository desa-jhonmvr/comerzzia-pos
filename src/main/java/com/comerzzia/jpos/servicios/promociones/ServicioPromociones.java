package com.comerzzia.jpos.servicios.promociones;

import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.persistencia.promociones.PromocionesDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.promociones.cuotas.PromocionTipoMesesGracia;
import com.comerzzia.jpos.servicios.promociones.cuotas.PromocionTipoNCuotasGratis;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCupon;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosAcumula;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosCanjeo;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDiaSocio;
import com.comerzzia.jpos.util.db.Database;
import java.sql.SQLException;
import com.comerzzia.jpos.util.db.Connection;
import es.mpsistemas.util.log.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioPromociones {

    protected static Logger log = Logger.getMLogger(ServicioPromociones.class);

    public static void consultar() throws PromocionException {
        Connection conn = new Connection();

        try {
            log.debug("consultar() - Consultando promociones");
            conn.abrirConexion(Database.getConnection());
            List<PromocionBean> promocionesBean = PromocionesDao.consultar(conn);
            log.debug("consultar() - Se ha encontrado " + promocionesBean.size() + " promociones en BBDD activas. Procesando promociones...");
            for (PromocionBean promocionBean : promocionesBean) {
                Promocion promocion = PromocionBuilder.buildPromocion(promocionBean);
                if (promocion == null) {
                    log.warn("ATENCIÓN: Se ha encontrado una promoción activa que no está implementada aún en esta versión del POS. Será ignorada.");
                    continue; // promoción no implementada aún.
                }
                promocion.setAfiliados(PromocionesDao.consultarAfiliadosPromocion(conn, promocion.getIdPromocion()));
                PromocionesDao.consultarMediosPagoPromocion(conn, promocion);
                if (promocionBean.getTipoPromocion().isPromocionTipoCupon()) {
                    PromocionTipoCupon promocionCupon = (PromocionTipoCupon) promocion;
                    // sólo nos interesan las que tienen un tipo de emisión automática
                    if (!promocionCupon.getConfigEmision().isTipoManual()) {
                        Sesion.promocionesCupones.add(promocionCupon);
                    }
                } else if (promocionBean.getTipoPromocion().isPromocionTipoPuntosAcumula()) {
                    Sesion.promocionesAcumulacionPuntos.add((PromocionTipoPuntosAcumula) promocion);
                } else if (promocionBean.getTipoPromocion().isPromocionTipoPuntosCanjea()) {
                    Sesion.promocionesCanjeoPuntos.add((PromocionTipoPuntosCanjeo) promocion);
                    Sesion.promociones.add(promocion);
                } else if (promocionBean.getTipoPromocion().isPromocionTipoNCuotasGratis()) {
                    Sesion.promocionesNCuotas.add((PromocionTipoNCuotasGratis) promocion);
                } else if (promocionBean.getTipoPromocion().isPromocionTipoDiaSocio()) {
                    Sesion.promocionDiaSocio = ((PromocionTipoDiaSocio) promocion); // sólo puede haber una vigente
                } else if (promocionBean.getTipoPromocion().isPromocionTipoMesesGracia()) {
                    Sesion.promocionMesesGracia = ((PromocionTipoMesesGracia) promocion); // sólo puede haber una vigente
                } else {// para el resto de promociones que no son cupones
                    Sesion.promociones.add(promocion);
                }
                Sesion.addPromocion(promocion);
            }
        } catch (SQLException e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar promociones: " + e.getMessage();

            throw new PromocionException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    /**
     * 
     * @param idPromociones
     * @return
     * @throws PromocionException 
     */
    public static Map<Long, Promocion> devuelveMapaPromociones(List<Long> idPromociones) throws PromocionException {

        Connection conn = new Connection();
        Map<Long, Promocion> mapaPromociones = new HashMap<>();

        try {
            //log.debug("consultar() - Consultando promociones");
            conn.abrirConexion(Database.getConnection());

            for (Long idPromocion : idPromociones) {
                PromocionBean promocionBean = PromocionesDao.consultarPromocion(conn, idPromocion);
                //log.debug("consultar() - Se ha encontrado " + promocionesBean.size() + " promociones en BBDD activas. Procesando promociones...");
                if (promocionBean != null) {
                    Promocion promocion = PromocionBuilder.buildPromocion(promocionBean);
                    if (promocion == null) {
                        log.warn("ATENCIÓN: Se ha encontrado una promoción activa que no está implementada aún en esta versión del POS. Será ignorada.");
                        continue; // promoción no implementada aún.
                    }
                    promocion.setAfiliados(PromocionesDao.consultarAfiliadosPromocion(conn, promocion.getIdPromocion()));
                    PromocionesDao.consultarMediosPagoPromocion(conn, promocion);
                    mapaPromociones.put(promocion.getIdPromocion(), promocion);
                }
            }

            return mapaPromociones;
        } catch (SQLException e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar promociones: " + e.getMessage();

            throw new PromocionException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }

    }

}
