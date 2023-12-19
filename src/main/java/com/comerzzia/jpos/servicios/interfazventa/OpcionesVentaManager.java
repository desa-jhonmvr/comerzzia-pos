/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.interfazventa;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.gui.IVenta;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.JVentas;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaBean;
import com.comerzzia.jpos.persistencia.listapda.detalle.DetalleSesionPdaBean;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.articulos.ArticuloNotFoundException;
import com.comerzzia.jpos.servicios.articulos.bloqueos.BloqueoFoundException;
import com.comerzzia.jpos.servicios.articulos.bloqueos.BloqueosServices;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.listapda.ListaPDAException;
import com.comerzzia.jpos.servicios.listapda.ListaPDAServices;
import com.comerzzia.jpos.servicios.tickets.TicketNuevaLineaException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author MGRI
 */
public class OpcionesVentaManager {

    private static final Logger log = Logger.getMLogger(OpcionesVentaManager.class);

    public static void anadeListaPDA(TicketS ticket, String codigoListaPDA, IVenta pantallaVenta) throws TicketNuevaLineaException, ListaPDAException  {
        try{
            if (ticket.tieneSesionPDAAsociada()){
                throw new ListaPDAException("La factura actual ya se encuentra asociada a una sesión PDA");
            }
            if (ticket.getTicketPromociones().isFacturaAsociadaDiaSocio()){
                throw new ListaPDAException("Sólo puede incluir artículos del día del socio en esta factura.");
            }
            
            SesionPdaBean sPDA = ListaPDAServices.consultar(codigoListaPDA);

            if (pantallaVenta.esTipoVenta() && !sPDA.isTipoVenta()){
                throw new ListaPDAException("La sesión PDA consultada no es de tipo Venta");
            }
            else if(!pantallaVenta.esTipoVenta() && !sPDA.isTipoReserva()){
                throw new ListaPDAException("La sesión PDA consultada no es de tipo Reserva");
            }
            if (sPDA.getDetalleSesionPdaList().isEmpty()) {
                log.warn("No se encontráron archivos para la sesión PDA con el identificador introducido: " + codigoListaPDA);
                throw new ListaPDAException("No se encontrraron artículos asociados a la Sesión PDA introducida");
            }
            else {
                pantallaVenta.setReferenciaSesionPDA(sPDA);
                for (DetalleSesionPdaBean ds : sPDA.getDetalleSesionPdaList()) {
                    Articulos art = ArticulosServices.getInstance().getArticuloCB(ds.getCodigoBarras());
                    if (comprobarArticuloBloqueado(art)) {
                        LineaTicket linea = pantallaVenta.crearLineaArticulo(ds.getCodigoBarras(), art, 1, true);
                        if(linea != null && sPDA.isTipoVenta()){
                            ((JVentas)pantallaVenta).accionCompruebaGarantiaExtendida(linea);
                            ((JVentas)pantallaVenta).accionCompruebaKitInstalacion(linea);
                        }
                    }
                }
            }
        }
        catch(ArticuloNotFoundException e){
            throw new TicketNuevaLineaException("No se encontraron artículos en base de datos recogidos por la PDA.");
        }
    }
    
    private static boolean comprobarArticuloBloqueado(Articulos art) {
        // Comprobamos si el artículo esta bloqeado
        try{
            BloqueosServices.isItemBloqueado(art.getCodmarca().getCodmarca(), art.getIdItem(), art.getCodart());
        } catch (BloqueoFoundException e){
             return false;
        }
        return true;
    }
}
