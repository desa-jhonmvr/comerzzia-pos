/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.promociones.articulos;

import com.comerzzia.jpos.persistencia.promociones.articulos.PromocionArticuloBean;
import com.comerzzia.jpos.persistencia.promociones.articulos.PromocionArticuloDao;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;

/**
 *
 * @author SMLM
 */
public class ServicioPromocionArticulo {
    protected static Logger log = Logger.getMLogger(ServicioPromocionArticulo.class);
    
    public static int consultarPromocionDiaSocio(Long idPromocion, String codArt) throws PromocionArticuloException {
        Connection conn = new Connection();
        try{
            log.debug("consultarPromocionDiaSocio() - Consultando artículos comprados para (promocion,articulo): ("+idPromocion+","+codArt+")");
            conn.abrirConexion(Database.getConnection());
            return PromocionArticuloDao.consultarPromocionDiaSocio(conn, idPromocion, codArt);
        }
        catch (SQLException e) {
            log.error("consultarPromocionDiaSocio() - " + e.getMessage());
            String mensaje = "Error al consultar los artículos comprados de la promoción: " + e.getMessage();

            throw new PromocionArticuloException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
    }
    
    public static int consultarPromocionFormaPago(String codArt) throws PromocionArticuloException {
        Connection conn = new Connection();
        try{
            log.debug("consultarPromocionPorFormaPago() - Consultando artículos comprados para (articulo): ("+codArt+")");
            conn.abrirConexion(Database.getConnection());
            return PromocionArticuloDao.consultarPromocionFormaPago(conn, codArt);
        }
        catch (SQLException e) {
            log.error("consultarPromocionPorFormaPago() - " + e.getMessage());
            String mensaje = "Error al consultar los artículos comprados de la promoción por Forma Pago: " + e.getMessage();

            throw new PromocionArticuloException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
    }
    
    public static void insert(Connection conn, TicketS ticket) throws PromocionArticuloException {
        try{
            for(LineaTicket linea:ticket.getLineas().getLineas()){
                if(linea.getPromocionLinea() != null && linea.getPromocionLinea().isTipoDiaSocio()){
                    PromocionArticuloBean promoArticulo = new PromocionArticuloBean();
                    promoArticulo.setIdPromocion(linea.getPromocionLinea().getIdPromocion());
                    promoArticulo.setCodart(linea.getArticulo().getCodart());
                    promoArticulo.setCantidad(linea.getCantidad());
                    promoArticulo.setUidTicket(ticket.getUid_ticket());
                    promoArticulo.setIdLinea(linea.getIdlinea());

                    PromocionArticuloDao.insert(conn, promoArticulo);
                }
            }
        }
        catch (SQLException e) {
            log.error("insert() - " + e.getMessage());
            String mensaje = "Error al insertar el artículo de la promoción comprado: " + e.getMessage();

            throw new PromocionArticuloException(mensaje, e);
        }        
    }
}
