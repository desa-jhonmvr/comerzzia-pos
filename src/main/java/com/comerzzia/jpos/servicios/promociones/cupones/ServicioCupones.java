package com.comerzzia.jpos.servicios.promociones.cupones;

import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasCuponPromo;
import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.promociones.*;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.persistencia.promociones.PromocionesDao;
import com.comerzzia.jpos.persistencia.promociones.TipoPromocionBean;
import com.comerzzia.jpos.persistencia.promociones.cupones.CuponesDao;
import com.comerzzia.jpos.servicios.core.contadores.ServicioContadores;
import com.comerzzia.jpos.servicios.promociones.articulos.SukuponLinea;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.util.db.Database;
import java.sql.SQLException;
import com.comerzzia.jpos.util.db.Connection;
import es.mpsistemas.util.cadenas.Cadena;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.util.List;

public class ServicioCupones {

    protected static Logger log = Logger.getMLogger(ServicioCupones.class);

    public static Cupon consultarCuponDescuento(Long idCupon, String codAlmacen) throws CuponNotFoundException, CuponException, PromocionException {
        Connection conn = new Connection();

        try {
            log.debug("consultarCuponDescuento() - Consultando cupon: " + idCupon + " expedido en la tienda: " + codAlmacen);
            conn.abrirConexion(Database.getConnection());
            Cupon cupon = CuponesDao.consultar(conn, idCupon, codAlmacen);
            if (cupon == null) {
                throw new CuponNotFoundException("El cupón consultado no se encuentra en el sistema: IdCupon: " + idCupon + " Tienda: " + codAlmacen);
            }
            PromocionBean promocionBean = PromocionesDao.consultar(conn, cupon.getIdPromocion());
            /*if (promocionBean == null){
                throw new CuponNotFoundException("No existe la promoción asociada al cupón consultado: IdCupon: " + idCupon + " Tienda: " + codAlmacen);
            }
            if (!promocionBean.getTipoPromocion().isPromocionTipoCuponDescuento() && !promocionBean.getTipoPromocion().isPromocionTipoCuponDescuentoAzar()
                    && !promocionBean.getTipoPromocion().isPromocionTipoBilleton()){
                throw new CuponNotFoundException("La promoción asociada al cupón consultado no es de tipo cupón descuento o billetón: Cupón: " + idCupon + " Tienda: " + codAlmacen);
            }*/
            if (promocionBean == null) {
                promocionBean = new PromocionBean();
                promocionBean.setTipoPromocion(new TipoPromocionBean(TipoPromocionBean.TIPO_PROMOCION_BILLETON, ""));
                promocionBean.setIdPromocion(cupon.getIdPromocion());
            }
            Promocion promocion = PromocionBuilder.buildPromocion(promocionBean);
            promocion.setAfiliados(PromocionesDao.consultarAfiliadosPromocion(conn, promocion.getIdPromocion()));
            cupon.setPromocion((PromocionTipoCupon) promocion);
            return cupon;
        } catch (SQLException e) {
            log.error("consultarCuponDescuento() - " + e.getMessage());
            String mensaje = "Error al consultar cupon: " + e.getMessage();
            throw new CuponException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    public static Cupon consultarSukuponFactura(String uidTicket) throws CuponException {
        Connection conn = new Connection();

        try {
            log.debug("consultarSukuponFactura() - Consultando sukupon para la factura con uid: " + uidTicket);
            conn.abrirConexion(Database.getConnection());
            Cupon cupon = CuponesDao.consultarSukuponFactura(conn, uidTicket);
            return cupon;
        } catch (SQLException e) {
            log.error("consultarSukuponFactura() - " + e.getMessage());
            String mensaje = "Error al consultar cupon: " + e.getMessage();
            throw new CuponException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    public static boolean existenCuponesCliente(String codCliente, Long idPromocion, int cadencia) throws CuponException {
        Connection conn = new Connection();

        try {
            log.debug("existenCuponesCliente() - Consultando si existen cupones para cliente: " + codCliente + " expedido en cadencia: " + cadencia);
            conn.abrirConexion(Database.getConnection());
            Fecha fecha = new Fecha();
            fecha.sumaDias((-1) * cadencia);
            return CuponesDao.existenCuponesCliente(conn, codCliente, idPromocion, fecha);
        } catch (SQLException e) {
            log.error("existenCuponesCliente() - " + e.getMessage());
            String mensaje = "Error Consultando si existen cupones para cliente con cadencia determinada. " + e.getMessage();
            throw new CuponException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    public static void obtenerIdCupones(List<Cupon> cupones) throws CuponException {
        try {
            for (Cupon cupon : cupones) {
                log.debug("obtenerIdCupones() - Obtener contador para identificador del cupón... ");
                Long idCupon = ServicioContadores.obtenerContadorDefinitivo(ServicioContadores.CONTADOR_CUPON);
                cupon.setIdCupon(Long.parseLong(idCupon + Cadena.getRandomNumerico(4)));
            }
        } catch (ContadorException e) {
            log.error("obtenerIdCupones() - " + e.getMessage());
            String mensaje = "Error al obtener contador para el cupón: " + e.getMessage();
            throw new CuponException(mensaje, e);
        }
    }

    public static void crear(Connection conn, List<Cupon> cupones, List<LineaTicket> linea) throws CuponException {
        try {
            for (Cupon cupon : cupones) {
                Long idCupon = cupon.getIdCupon();
                CodigoBarrasCuponPromo codBarrasCupon = new CodigoBarrasCuponPromo(idCupon);
                cupon.setCodBarras(codBarrasCupon.getCodigoBarras());
                log.debug("crear() - Salvando cupon con id: " + idCupon);
                CuponesDao.insert(conn, cupon);
                log.debug("insert cabecera Cupon  " + idCupon);
                if (cupon.getVariable() != null) {
                    if (linea != null) {
                        for (LineaTicket lin : linea) {
                            List<SukuponLinea> lineaSukupon = lin.getSukuponesEmitidos();
                            if (lineaSukupon != null) {
                                SukuponLinea sukupon = new SukuponLinea();
                                for (SukuponLinea listSukupon : lineaSukupon) {
                                    sukupon.setIdlinea(lin.getIdlinea());
                                    sukupon.setIdCupon(idCupon);
                                    sukupon.setArticulo(listSukupon.getArticulo());
                                    sukupon.setCantidad(listSukupon.getCantidad());
                                    sukupon.setImporte(listSukupon.getImporte());
                                    sukupon.setImporteTotal(listSukupon.getImporteTotal());
                                    sukupon.setValor(listSukupon.getValor());
                                    sukupon.setUtilizado(listSukupon.getValor());
                                    sukupon.setSaldo(listSukupon.getValor());
                                    sukupon.setCodAlm(cupon.getCodAlmacen());
                                    CuponesDao.insertDetCupon(conn, sukupon);
                                    log.debug("valor sukupon." + listSukupon.getValor());
                                    log.debug("item." + listSukupon.getArticulo().getDesart());
                                    log.debug("cantidad." + listSukupon.getCantidad());
                                    log.debug("importe." + listSukupon.getImporte());
                                    log.debug("total." + listSukupon.getImporteTotal());
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            log.error("crear() - " + e.getMessage());
            String mensaje = "Error al insertar cupón en base de datos: " + e.getMessage();
            throw new CuponException(mensaje, e);
        }
    }

    public static void marcarCuponesUtilizados(Connection conn, TicketS ticket) throws CuponException {
        try {
            for (Cupon cupon : ticket.getCuponesAplicados()) {
                log.debug("marcarCuponesUtilizados() - Marcamos cupón utilizado con id: " + cupon.getIdCupon());
                cupon.setTipoReferenciaUso(Cupon.REF_USO_FACTURA_VENTA);
                cupon.setReferenciaUso(ticket.getUid_ticket());
                CuponesDao.updateUso(conn, cupon);
                CuponesDao.updateUsoDetalle(conn, cupon);
            }
        } catch (SQLException e) {
            log.error("marcarCuponesUtilizados() - " + e.getMessage());
            String mensaje = "Error al actualizar cupón en base de datos: " + e.getMessage();
            throw new CuponException(mensaje, e);
        }
    }
}
