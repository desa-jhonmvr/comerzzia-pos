/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.stock;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.SriTienda;
import com.comerzzia.jpos.persistencia.articulos.ArticulosDao;
import com.comerzzia.jpos.persistencia.logs.logskdx.LogKardexBean;
import com.comerzzia.jpos.persistencia.logs.logskdx.LogKardexDao;
import com.comerzzia.jpos.persistencia.stock.StockDao;
import com.comerzzia.jpos.persistencia.stock.StockKDXBean;
import com.comerzzia.jpos.servicios.core.tiendas.SriTiendas.SriTiendasServices;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.Constantes;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class ServicioStock {

    private static Logger log = Logger.getMLogger(ServicioStock.class);

    public static final Long MOVIMIENTO_51 = 51L;
    public static final Long MOVIMIENTO_52 = 52L;
    public static final Long MOVIMIENTO_99 = 99L;

    public static List<StockBean> consultarStockTienda(Articulos articuloBuscado) throws StockException {
        List<StockBean> res = new ArrayList<StockBean>();
        try {
            ArticulosDao aDao = new ArticulosDao();
            articuloBuscado = aDao.getArticuloCod(articuloBuscado.getCodart());
            int numeroItems = getStockDisponibleVenta(articuloBuscado);
            StockBean resS = new StockBean();
            resS.setCodAlm(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            resS.setCodArt(articuloBuscado.getCodart());
            resS.setDesAlm(Sesion.getTienda().getAlmacen().getDesalm());
            resS.setStock(numeroItems);
            res.add(resS);
        } catch (StockException ex) {
            log.error("consultarStockTienda()- Error consultando stock de articulo");
            throw ex;
        } catch (Exception e) {
            log.error("consultarStockTienda()- Error consultando stock de articulo: " + e.getMessage(), e);
            throw new StockException("Error consultando stock de articulo.");
        }
        return res;
    }

    public static List<StockBean> consultarStockTotal(Articulos articuloBuscado) throws StockException {
        List<StockBean> res = new ArrayList<StockBean>();
        try {
            ArticulosDao aDao = new ArticulosDao();
            articuloBuscado = aDao.getArticuloCod(articuloBuscado.getCodart());
            int numeroItems = getStockDisponibleVentaTotal(articuloBuscado);
            StockBean resS = new StockBean();
            resS.setCodAlm(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            resS.setCodArt(articuloBuscado.getCodart());
            resS.setDesAlm("STOCK TOTAL");
            resS.setStock(numeroItems);
            res.add(resS);
        } catch (StockException ex) {
            log.error("consultarStockTienda()- Error consultando stock de articulo");
            throw ex;
        } catch (Exception e) {
            log.error("consultarStockTienda()- Error consultando stock de articulo: " + e.getMessage(), e);
            throw new StockException("Error consultando stock de articulo.");
        }
        return res;
    }

    public static List<StockBean> consultarStockTiendas(Articulos articuloBuscado) throws StockException {
        List<StockBean> res = null;
        try {
            // Obtenemos los datos adicionales del artículo
            ArticulosDao aDao = new ArticulosDao();
            articuloBuscado = aDao.getArticuloCod(articuloBuscado.getCodart());

            // Consultamos la lista de almacenes activos
            List<SriTienda> consultarListaTiendas = SriTiendasServices.consultarListaTiendas();

            // Consultamos los stocks
            int tienda = VariablesAlm.getVariableAsInt(VariablesAlm.COD_ALMACEN);
            int marca = new Integer(articuloBuscado.getCodmarca().getCodmarca());
            int item = articuloBuscado.getIdItem();

            List<StockKDXBean> lstocks = consultarStockArticulos(marca, item, tienda);

            //G.S. Remueve locales a presentar
            removerLocales(lstocks);

            res = cruzaStocks(consultarListaTiendas, lstocks);

        } catch (Exception ex) {
            log.error("consultarStockTiendas() - Error en la consulta de stock: " + ex.getMessage(), ex);
            throw new StockException("Error en la consulta de stock", ex);
        }
        return res;

    }

    /**
     * Metodo que remueve los locales que no deben ser presentados en la pantalla de consulta
     * 
     * @author Gabriel Simbania
     * @param lstocks 
     */
    private static void removerLocales(List<StockKDXBean> lstocks) {
        List<StockKDXBean> tiendasEliminar = new ArrayList<>();
        for (StockKDXBean stock : lstocks) {
            for (Integer local : Constantes.LOCALES_NO_PRESENTAR) {
                if (stock.getLugar() == local) {
                    tiendasEliminar.add(stock);
                }
            }
        }

        lstocks.removeAll(tiendasEliminar);
    }

    private static List<StockBean> cruzaStocks(List<SriTienda> lSriTiendas, List<StockKDXBean> lStocks) {
        List<StockBean> res = new ArrayList<StockBean>();

        for (StockKDXBean stock : lStocks) {

            boolean enc = false;
            int i = 0;
            StockBean st = new StockBean();
            st.setCodAlm(String.valueOf(stock.getLugar()));
            st.setDesAlm("");
            st.setStock(stock.getCantidad());
            st.setCodArt("" + stock.getItem());  // Podría quitarse

            int idAlm = stock.getLugar();

            while (i < lSriTiendas.size() && !enc) {
                if (idAlm == new Integer(Variables.getVariable(Variables.DATABASE_CODIGO_CENTRAL_LOCAL))) {
                    st.setDesAlm("ALMACÉN CENTRAL");
                    enc = true;
                } else {
                    if (Integer.valueOf(lSriTiendas.get(i).getCodalminterno()) == idAlm) {
                        st.setDesAlm(lSriTiendas.get(i).getDesalm());
                        enc = true;
                    }
                }
                i++;
            }
            res.add(st);

        }

        return res;
    }

    @Deprecated
    public static void aumentaStockVenta(List<LineaTicket> lineas, LogKardexBean logKardex) throws StockException, StockTimeOutException {
        actualizaStockVenta(lineas, 1, logKardex);
    }

    @Deprecated
    public static void aumentaStockVenta(String codMarca, int idItem, int cantidad, LogKardexBean logKardex) throws StockException {
        actualizaStockVenta(codMarca, idItem, cantidad, 1, logKardex);
    }

    @Deprecated
    public static void disminuyeStockVenta(List<LineaTicket> lineas, LogKardexBean logKardex) throws StockException, StockTimeOutException {
        actualizaStockVenta(lineas, -1, logKardex);
    }

    private static void actualizaStockVenta(List<LineaTicket> lineas, int factor, LogKardexBean logKardex) throws StockException, StockTimeOutException {
        if (!Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK)) {
            return;
        }
        Connection connSukasa = new Connection();
        Connection conn = new Connection();
        try {
            log.debug("actualizaStockVenta() - Actualizando Stock de venta... ");
            conn.abrirConexion(Database.getConnection());
            conn.iniciaTransaccion();
            connSukasa.abrirConexion(Database.getConnectionSukasa());
            connSukasa.iniciaTransaccion();
            for (LineaTicket linea : lineas) {
                try {
                    int stocksActualizados = StockDao.actualizarStockVenta(
                            connSukasa,
                            Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()),
                            linea.getArticulo().getIdItem(),
                            linea.getCantidad() * factor);
                    //Sino existe ningun registro en kardex, insertamos los dos en BBDD
                    if (stocksActualizados == 0) {
                        StockDao.insertStockArticulo(connSukasa, Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 51, linea.getCantidad() * factor);
                        StockDao.insertStockArticulo(connSukasa, Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 99, -(linea.getCantidad() * factor));
                    }
                    //Si existe uno de los registros, vemos cual es para insertarlo
                    if (stocksActualizados == 1) {
                        if (StockDao.consultarExisteStock(connSukasa, Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem()) != null) {
                            StockDao.insertStockArticulo(connSukasa, Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 51, linea.getCantidad() * factor);
                        } else {
                            StockDao.insertStockArticulo(connSukasa, Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 99, -(linea.getCantidad() * factor));
                        }
                    }
                    try {
                        LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 51, linea.getCantidad() * factor, logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), "");
                        LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 99, -(linea.getCantidad() * factor), logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), "");
                        conn.commit();
                        conn.finalizaTransaccion();
                    } catch (SQLException e) {
                        log.error("Error al insertar el log del Kardex: " + e.getMessage(), e);
                    }
                } catch (SQLException e) {
                    connSukasa.deshacerTransaccion();
                    log.error("actualizaStockVenta() - " + e.getMessage());
                    String mensaje = "Error actualizando Stock de venta: " + e.getMessage();
                    try {
                        LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 51, linea.getCantidad() * factor, logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), mensaje);
                        LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 99, -(linea.getCantidad() * factor), logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), mensaje);
                        conn.commit();
                        conn.finalizaTransaccion();
                    } catch (SQLException ex) {
                        log.error("Error al insertar el log del Kardex: " + ex.getMessage(), ex);
                    }
                    if (e.getMessage().contains("ORA-01013")) {
                        throw new StockTimeOutException(e.getMessage());
                    }
                    throw new StockException(mensaje, e);
                }
            }
            connSukasa.commit();
            connSukasa.finalizaTransaccion();
        } catch (SQLException e) {
            conn.deshacerTransaccion();
            connSukasa.deshacerTransaccion();
            log.error("actualizaStockVenta() - " + e.getMessage());
            String mensaje = "Error actualizando Stock de venta: " + e.getMessage();
            if (e.getMessage().contains("ORA-01013")) {
                throw new StockTimeOutException(e.getMessage());
            }
            throw new StockException(mensaje, e);
        } catch (Exception e) {
            conn.deshacerTransaccion();
            connSukasa.deshacerTransaccion();
            log.error("actualizaStockVenta() - " + e.getMessage());
            String mensaje = "Error actualizando Stock de venta: " + e.getMessage();
            if (e.getMessage().contains("ORA-01013")) {
                throw new StockTimeOutException(e.getMessage());
            }
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
            connSukasa.cerrarConexion();
        }
    }

    private static void actualizaStockVenta(String codMarca, Integer idItem, int cantidad, int factor, LogKardexBean logKardex) throws StockException {
        if (!Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK)) {
            return;
        }
        Connection connSukasa = new Connection();
        Connection conn = new Connection();
        try {
            log.debug("actualizaStockReserva() - Actualizando Stock de venta... ");
            conn.abrirConexion(Database.getConnection());
            conn.iniciaTransaccion();
            connSukasa.abrirConexion(Database.getConnectionSukasa());
            connSukasa.iniciaTransaccion();
            int stocksActualizados = StockDao.actualizarStockVenta(
                    connSukasa,
                    Integer.parseInt(codMarca),
                    idItem,
                    cantidad * factor);
            //Sino existe ningun registro en kardex, insertamos los dos en BBDD
            if (stocksActualizados == 0) {
                StockDao.insertStockArticulo(connSukasa, Integer.parseInt(codMarca), idItem, 51, cantidad * factor);
                StockDao.insertStockArticulo(connSukasa, Integer.parseInt(codMarca), idItem, 99, -(cantidad * factor));
            }
            //Si existe uno de los registros, vemos cual es para insertarlo
            if (stocksActualizados == 1) {
                if (StockDao.consultarExisteStock(connSukasa, Integer.parseInt(codMarca), idItem) != null) {
                    StockDao.insertStockArticulo(connSukasa, Integer.parseInt(codMarca), idItem, 51, cantidad * factor);
                } else {
                    StockDao.insertStockArticulo(connSukasa, Integer.parseInt(codMarca), idItem, 99, -(cantidad * factor));
                }
            }
            try {
                LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(codMarca), idItem, 51, cantidad * factor, logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), "");
                LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(codMarca), idItem, 99, -(cantidad * factor), logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), "");
                conn.commit();
                conn.finalizaTransaccion();
            } catch (SQLException e) {
                log.error("Error al insertar el log del Kardex: " + e.getMessage(), e);
            }

            connSukasa.commit();
            connSukasa.finalizaTransaccion();
        } catch (SQLException e) {
            connSukasa.deshacerTransaccion();
            log.error("actualizaStockVenta() - " + e.getMessage());
            String mensaje = "Error actualizando Stock de venta: " + e.getMessage();
            try {
                LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(codMarca), idItem, 51, cantidad * factor, logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), mensaje);
                LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(codMarca), idItem, 99, -(cantidad * factor), logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), mensaje);
                conn.commit();
                conn.finalizaTransaccion();
            } catch (SQLException ex) {
                log.error("Error al insertar el log del Kardex: " + ex.getMessage(), ex);
            }
            throw new StockException(mensaje, e);
        } catch (Exception e) {
            conn.deshacerTransaccion();
            connSukasa.deshacerTransaccion();
            log.error("actualizaStockVenta() - " + e.getMessage());
            String mensaje = "Error actualizando Stock de venta: " + e.getMessage();
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
            connSukasa.cerrarConexion();
        }
    }

    @Deprecated
    public static void aumentaStockReserva(List<LineaTicket> lineas, LogKardexBean logKardex) throws StockException {
        actualizaStockReserva(lineas, 1, logKardex);
    }

    @Deprecated
    public static void disminuyeStockReserva(List<LineaTicket> lineas, LogKardexBean logKardex) throws StockException {
        actualizaStockReserva(lineas, -1, logKardex);
    }

    @Deprecated
    public static void aumentaStockReserva(String codMarca, int idItem, int cantidad, LogKardexBean logKardex) throws StockException {
        actualizaStockReserva(codMarca, idItem, cantidad, 1, logKardex);
    }

    @Deprecated
    public static void disminuyeStockReserva(String codMarca, int idItem, int cantidad, LogKardexBean logKardex) throws StockException {
        actualizaStockReserva(codMarca, idItem, cantidad, -1, logKardex);
    }

    private static void actualizaStockReserva(List<LineaTicket> lineas, int factor, LogKardexBean logKardex) throws StockException {
        if (!Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK)) {
            return;
        }
        Connection connSukasa = new Connection();
        Connection conn = new Connection();
        try {
            log.debug("actualizaStockReserva() - Actualizando Stock de reserva... ");
            connSukasa.abrirConexion(Database.getConnectionSukasa());
            connSukasa.iniciaTransaccion();
            conn.abrirConexion(Database.getConnection());
            conn.iniciaTransaccion();
            for (LineaTicket linea : lineas) {
                try {
                    int stocksActualizados = StockDao.actualizarStockReserva(
                            connSukasa,
                            Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()),
                            linea.getArticulo().getIdItem(),
                            linea.getCantidad() * factor);
                    //Sino existe ningun registro en kardex, insertamos los dos en BBDD
                    if (stocksActualizados == 0) {
                        StockDao.insertStockArticulo(connSukasa, Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 99, -(linea.getCantidad() * factor));
                        StockDao.insertStockArticulo(connSukasa, Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 52, linea.getCantidad() * factor);
                    }
                    //Si existe uno de los registros, vemos cual es para insertarlo
                    if (stocksActualizados == 1) {
                        if (StockDao.consultarExisteStock(connSukasa, Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem()) != null) {
                            StockDao.insertStockArticulo(connSukasa, Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 52, linea.getCantidad() * factor);
                        } else {
                            StockDao.insertStockArticulo(connSukasa, Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 99, -(linea.getCantidad() * factor));
                        }
                        try {
                            LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 52, linea.getCantidad() * factor, logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), "");
                            LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 99, -(linea.getCantidad() * factor), logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), "");
                            conn.commit();
                            conn.finalizaTransaccion();
                        } catch (SQLException e) {
                            log.error("Error al insertar el log del Kardex: " + e.getMessage(), e);
                        }
                    }
                } catch (SQLException ex) {
                    connSukasa.deshacerTransaccion();
                    log.error("actualizaStockVenta() - " + ex.getMessage());
                    String mensaje = "Error actualizando Stock de venta: " + ex.getMessage();
                    try {
                        LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 52, linea.getCantidad() * factor, logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), mensaje);
                        LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(linea.getArticulo().getCodmarca().getCodmarca()), linea.getArticulo().getIdItem(), 99, -(linea.getCantidad() * factor), logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), mensaje);
                        conn.commit();
                        conn.finalizaTransaccion();
                    } catch (SQLException e) {
                        log.error("Error al insertar el log del Kardex: " + e.getMessage(), e);
                    }
                    throw new StockException(mensaje, ex);
                }
            }
            connSukasa.commit();
            connSukasa.finalizaTransaccion();
        } catch (SQLException e) {
            conn.deshacerTransaccion();
            connSukasa.deshacerTransaccion();
            log.error("actualizaStockReserva() - " + e.getMessage());
            String mensaje = "Error actualizando Stock de reserva: " + e.getMessage();
            throw new StockException(mensaje, e);
        } catch (Exception e) {
            conn.deshacerTransaccion();
            connSukasa.deshacerTransaccion();
            log.error("actualizaStockReserva() - " + e.getMessage());
            String mensaje = "Error actualizando Stock de reserva: " + e.getMessage();
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
            connSukasa.cerrarConexion();
        }
    }

    private static void actualizaStockReserva(String codMarca, Integer idItem, int cantidad, int factor, LogKardexBean logKardex) throws StockException {
        if (!Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK)) {
            return;
        }
        Connection connSukasa = new Connection();
        Connection conn = new Connection();
        try {
            log.debug("actualizaStockReserva() - Actualizando Stock de reserva... ");
            connSukasa.abrirConexion(Database.getConnectionSukasa());
            connSukasa.iniciaTransaccion();
            conn.abrirConexion(Database.getConnection());
            conn.iniciaTransaccion();
            int stocksActualizados = StockDao.actualizarStockReserva(
                    connSukasa,
                    Integer.parseInt(codMarca),
                    idItem,
                    cantidad * factor);
            //Sino existe ningun registro en kardex, insertamos los dos en BBDD
            if (stocksActualizados == 0) {
                StockDao.insertStockArticulo(connSukasa, Integer.parseInt(codMarca), idItem, 52, cantidad * factor);
                StockDao.insertStockArticulo(connSukasa, Integer.parseInt(codMarca), idItem, 99, -(cantidad * factor));
            }
            //Si existe uno de los registros, vemos cual es para insertarlo
            if (stocksActualizados == 1) {
                if (StockDao.consultarExisteStock(connSukasa, Integer.parseInt(codMarca), idItem) != null) {
                    StockDao.insertStockArticulo(connSukasa, Integer.parseInt(codMarca), idItem, 52, cantidad * factor);
                } else {
                    StockDao.insertStockArticulo(connSukasa, Integer.parseInt(codMarca), idItem, 99, -(cantidad * factor));
                }
            }
            try {
                LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(codMarca), idItem, 52, cantidad * factor, logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), "");
                LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(codMarca), idItem, 99, -(cantidad * factor), logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), "");
                conn.commit();
                conn.finalizaTransaccion();
            } catch (SQLException e) {
                log.error("Error al insertar el log del Kardex: " + e.getMessage(), e);
            }
            connSukasa.commit();
            connSukasa.finalizaTransaccion();
        } catch (SQLException e) {
            connSukasa.deshacerTransaccion();
            log.error("actualizaStockReserva() - " + e.getMessage());
            String mensaje = "Error actualizando Stock de reserva: " + e.getMessage();
            try {
                LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(codMarca), idItem, 51, cantidad * factor, logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), mensaje);
                LogKardexDao.insertLogKardex(conn, logKardex.getFactura(), Integer.parseInt(codMarca), idItem, 99, -(cantidad * factor), logKardex.getTipoAccion(), logKardex.getUsuarioAutorizacion(), mensaje);
                conn.commit();
                conn.finalizaTransaccion();
            } catch (SQLException ex) {
                log.error("Error al insertar el log del Kardex: " + ex.getMessage(), ex);
            }
            throw new StockException(mensaje, e);
        } catch (Exception e) {
            conn.deshacerTransaccion();
            connSukasa.deshacerTransaccion();
            log.error("actualizaStockReserva() - " + e.getMessage());
            String mensaje = "Error actualizando Stock de reserva: " + e.getMessage();
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
            connSukasa.cerrarConexion();
        }
    }

    public static int isStockDisponibleVenta(Articulos articulo) throws StockException {
        return getStockDisponibleVenta(articulo);
        //return getStockDisponibleVenta(articulo) >= cantidad;
    }

    public static int isStockDisponibleVentaBodega(Articulos articulo) throws StockException {
        return getStockDisponibleVentaBodega(articulo);
        //return getStockDisponibleVentaBodega(articulo) >= cantidad;
    }

    public static int getStockDisponibleVenta(String codArticulo, int codMarca, int codItem) throws StockException {
        Connection conn = new Connection();
        try {
            log.debug("getStockDisponibleVenta() - Consultando Stock de venta... ");
            conn.abrirConexion(Database.getConnection());
//            if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK_ERP)){
//                return StockDao.consultarStockArticuloTiendaERP(conn, codMarca, codItem);
//            } else{
            return StockDao.consultarStockArticuloTienda(conn, codArticulo);
//            }

//            return StockDao.consultarStockArticuloTienda(conn, articulo.getCodart());
        } catch (SQLException e) {
            log.error("getStockDisponibleVenta() - " + e.getMessage());
            String mensaje = "Error Consultando Stock de venta: " + e.getMessage();
            throw new StockException(mensaje, e);
        } catch (Exception e) {
            log.error("getStockDisponibleVenta() - " + e.getMessage());
            String mensaje = "Error Consultando Stock de venta: " + e.getMessage();
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    private static int getStockDisponibleVenta(Articulos articulo) throws StockException {
        Connection conn = new Connection();
        try {
            log.debug("getStockDisponibleVenta() - Consultando Stock de venta... ");
            conn.abrirConexion(Database.getConnection());
//            return StockDao.consultarStockArticuloTienda(conn, Integer.parseInt(articulo.getCodmarca().getCodmarca()), articulo.getIdItem());
//            if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK_ERP)){
//                return StockDao.consultarStockArticuloTiendaERP(conn, Integer.valueOf(articulo.getCodmarca().getCodmarca()), articulo.getIdItem());
//            } else {
            return StockDao.consultarStockArticuloTienda(conn, articulo.getCodart());
//            }

        } catch (SQLException e) {
            log.error("getStockDisponibleVenta() - " + e.getMessage());
            String mensaje = "Error Consultando Stock de venta: " + e.getMessage();
            return Integer.MAX_VALUE; //Devolvemos el máximo valor posible para que el proceso de venta continue
        } catch (Exception e) {
            log.error("getStockDisponibleVenta() - " + e.getMessage());
            String mensaje = "Error Consultando Stock de venta: " + e.getMessage();
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    private static int getStockDisponibleVentaBodega(Articulos articulo) throws StockException {
        Connection conn = new Connection();
        try {
            log.debug("getStockDisponibleVentaBodega() - Consultando Stock de venta... ");
            conn.abrirConexion(Database.getConnection());
//            int stockBodega = StockDao.consultarStockArticuloBodega(conn, Integer.parseInt(articulo.getCodmarca().getCodmarca()), articulo.getIdItem());
//            int facturados = StockDao.consultarPedidosFacturadosArticulo(conn, Integer.parseInt(articulo.getCodmarca().getCodmarca()), articulo.getIdItem());
            int stockBodega = 0;
            int facturados = 0;
//            if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK_ERP)){
            stockBodega = StockDao.consultarStockArticuloBodegaERP(conn, Integer.valueOf(articulo.getCodmarca().getCodmarca()), articulo.getIdItem());
            facturados = StockDao.consultarPedidosFacturadosArticuloERP(conn, Integer.valueOf(articulo.getCodmarca().getCodmarca()), articulo.getIdItem());
//            } else {
//                stockBodega = StockDao.consultarStockArticuloBodega(conn, articulo.getCodart());
//                facturados = StockDao.consultarPedidosFacturadosArticulo(conn, articulo.getCodart());
//            }

            int disponible = stockBodega - facturados;
            return disponible;
        } catch (SQLException e) {
            log.error("getStockDisponibleVentaBodega() - " + e.getMessage());
            String mensaje = "Error Consultando Stock de venta: " + e.getMessage();
            return Integer.MAX_VALUE; //Devolvemos el máximo valor posible para que el proceso de venta continue
        } catch (Exception e) {
            log.error("getStockDisponibleVentaBodega() - " + e.getMessage());
            String mensaje = "Error Consultando Stock de venta: " + e.getMessage();
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    private static int getStockDisponibleVentaTotal(Articulos articulo) throws StockException {
        Connection conn = new Connection();
        try {
            log.debug("getStockDisponibleVentaTotal() - Consultando Stock de venta... ");
            conn.abrirConexion(Database.getConnection());
//            return StockDao.consultarStockArticuloTotal(conn, Integer.parseInt(articulo.getCodmarca().getCodmarca()), articulo.getIdItem());           
//            if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK_ERP)) {
            return StockDao.consultarStockArticuloTotalERP(conn, Integer.valueOf(articulo.getCodmarca().getCodmarca()), articulo.getIdItem());
//            } else {
//                return StockDao.consultarStockArticuloTotal(conn, articulo.getCodart());
//            }
        } catch (SQLException e) {
            log.error("getStockDisponibleVentaTotal() - " + e.getMessage());
            String mensaje = "Error Consultando Stock de venta: " + e.getMessage();
            throw new StockException(mensaje, e);
        } catch (Exception e) {
            log.error("getStockDisponibleVentaTotal() - " + e.getMessage());
            String mensaje = "Error Consultando Stock de venta: " + e.getMessage();
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    public static List<StockKDXBean> consultarStockArticulos(int marca, int item, int tienda) throws StockException {
        List<StockKDXBean> res = new ArrayList<StockKDXBean>();

        Connection conn = new Connection();
        try {
            log.debug("consultarStockArticulos() - Consultando Stock de tiendas... ");
            conn.abrirConexion(Database.getConnection());
//            if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK_ERP)) {
            res = StockDao.consultarStockArticulosERP(conn, marca, item);
//            } else {
//                res = StockDao.consultarStockArticulosPOS(conn, marca, item);
//            }
        } catch (SQLException e) {
            log.error("consultarStockArticulos() - " + e.getMessage());
            String mensaje = "Error consultando Stock de tiendas: " + e.getMessage();
            throw new StockException(mensaje, e);
        } catch (Exception e) {
            log.error("consultarStockArticulos() - " + e.getMessage());
            String mensaje = "Error consultando Stock de tiendas: " + e.getMessage();
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
        return res;
    }

    /**
     * @author Gabriel Simbania
     * @param marcaId
     * @param itemId
     * @return
     * @throws StockException
     */
    public static Long consultaDisponibleCD(int marcaId, int itemId) throws StockException {
        Connection conn = new Connection();

        try {
            conn.abrirConexion(Database.getConnection());
            Long stockDisponible = StockDao.consultaDisponibleCD(conn, marcaId, itemId);

            return stockDisponible;
        } catch (SQLException e) {
            log.error("consultarStockArticulos() - " + e.getMessage());
            String mensaje = "Error consultando Stock de tiendas: " + e.getMessage();
            throw new StockException(mensaje, e);
        } catch (Exception e) {
            log.error("consultarStockArticulos() - " + e.getMessage());
            String mensaje = "Error consultando Stock de tiendas: " + e.getMessage();
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }
    
        /**
     * @author Mónica Enríquez
     * @param itmId
     * @return
     * @throws StockException
     */
    public static Long consultaDisponibleLocales(Long itmId) throws StockException {
        Connection conn = new Connection();

        try {
            conn.abrirConexion(Database.getConnection());
            Long stockDisponible = StockDao.consultaDisponibleLocales(conn,itmId);

            return stockDisponible;
        } catch (SQLException e) {
            log.error("consultarStockArticulosLocalesLocales() - " + e.getMessage());
            String mensaje = "Error consultando Stock de tiendas: " + e.getMessage();
            throw new StockException(mensaje, e);
        } catch (Exception e) {
            log.error("consultarStockArticulosLocales() - " + e.getMessage());
            String mensaje = "Error consultando Stock de tiendas: " + e.getMessage();
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    /**
     * @author Gabriel Simbania
     * @param lineas
     * @param movId
     * @param codAlm
     * @param positivo
     * @throws com.comerzzia.jpos.servicios.stock.StockException
     */
    public static void actualizaListaArticulosKardexVentas(List<LineaTicket> lineas, Long movId, String codAlm, boolean positivo) throws StockException {

        for (LineaTicket lineaTicket : lineas) {
            if (positivo) {
                actualizaKardex(lineaTicket.getArticulo().getCodart(), movId, codAlm, (long) (lineaTicket.getCantidad()));
            } else {
                actualizaKardex(lineaTicket.getArticulo().getCodart(), movId, codAlm, (long) (lineaTicket.getCantidad() * -1));
            }
        }

    }

    /**
     * @author Gabriel Simbania
     * @param codArt
     * @param movId
     * @param codAlm
     * @param cantidad
     * @throws com.comerzzia.jpos.servicios.stock.StockException
     */
    public static void actualizaKardex(String codArt, Long movId, String codAlm, Long cantidad) throws StockException {

        Connection conn = new Connection();
        //Si la bandera esta en false no ingresa hacer el proceso
        if (!Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_KARDEX_POS)) {
            return;
        }

        try {
            conn.abrirConexion(Database.getConnection());
            SriTienda sriTienda = SriTiendasServices.consultaTiendaByCodAlm(codAlm);
            Long lugId = Long.parseLong(sriTienda.getCodalminterno());
            Long stockDisponible = StockDao.consultarKardex(conn, codArt, movId, lugId);

            if (stockDisponible == null) {

                ArticulosDao articulosDao = new ArticulosDao();
                Articulos articulos = articulosDao.getArticuloCod(codArt);
                if (articulos.getItmId() != null) {
                    //Inserto la linea
                    StockDao.insertaStockArticuloKardex(conn, lugId, codArt, movId, cantidad, articulos.getItmId());
                    //Inserto o actualizo la linea 99 
                    Long stock99 = StockDao.consultarKardex(conn, codArt, MOVIMIENTO_99, lugId);
                    if (stock99 == null) {
                        StockDao.insertaStockArticuloKardex(conn, lugId, codArt, MOVIMIENTO_99, cantidad, articulos.getItmId());
                    } else {
                        StockDao.actualizaStockArticuloKardex(conn, lugId, codArt, MOVIMIENTO_99, cantidad);
                    }
                }
            } else {
                //Actualizo la linea
                StockDao.actualizaStockArticuloKardex(conn, lugId, codArt, movId, cantidad);
                //Actualizo la linea 99
                StockDao.actualizaStockArticuloKardex(conn, lugId, codArt, MOVIMIENTO_99, cantidad);
            }

            conn.commit();
            conn.finalizaTransaccion();

        } catch (SQLException e) {
            log.error("consultaStockKardex() - " + e.getMessage());
            String mensaje = "Error consultando Stock de tiendas: " + e.getMessage();
            try {
                conn.rollback();
                conn.deshacerTransaccion();
            } catch (SQLException ex) {
                log.error("sql() - " + e.getMessage());
            }
            throw new StockException(mensaje, e);
        } catch (Exception e) {
            log.error("v() - " + e.getMessage());
            String mensaje = "Error consultando Stock de tiendas: " + e.getMessage();
            try {
                conn.rollback();
                conn.deshacerTransaccion();
            } catch (SQLException ex) {
                log.error("sql() - " + e.getMessage());
            }
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }

    }

    /**
     * @author Gabriel Simbania
     * @param codArt
     * @param movId
     * @param lugId
     * @return
     * @throws StockException
     */
    public static Long consultaStockKardex(String codArt, Long movId, Long lugId) throws StockException {
        Connection conn = new Connection();

        try {
            conn.abrirConexion(Database.getConnection());
            conn.iniciaTransaccion();
            Long stockDisponible = StockDao.consultarKardex(conn, codArt, movId, lugId);

            return stockDisponible;
        } catch (SQLException e) {
            log.error("consultaStockKardex() - " + e.getMessage());
            String mensaje = "Error consultando Stock de tiendas: " + e.getMessage();
            throw new StockException(mensaje, e);
        } catch (Exception e) {
            log.error("v() - " + e.getMessage());
            String mensaje = "Error consultando Stock de tiendas: " + e.getMessage();
            throw new StockException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

}
