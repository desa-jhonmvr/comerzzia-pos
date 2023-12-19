/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.pagos;

import com.comerzzia.jpos.entity.db.FacturacionTicket;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketS;

/**
 *
 * @author MGRI
 * Funciones auxiliares comunes de la pantalla de pagos que hacen uso de elementos gráficos de presentación
 */
public class ManejadorPagos {

    /**
     * Comprueba si el cliente tiene que facturar por una compra al intentar efectuar el pago
     * @param ticket
     * @return 
     */
    public static boolean comprobarFacturacionClienteGenerico(TicketS ticket) {

        boolean cancelado = false;
        FacturacionTicketBean ft = ticket.getFacturacion();

        if ((ft == null || ft.isCliente(Sesion.getClienteGenerico())) && ticket.getPagos().getUstedPaga().compareTo(VariablesAlm.IMPORTE_MAXIMO_SIN_FACTURAR) >= 0) {
            if (ft.isCliente(Sesion.getClienteGenerico())) {
                ft = null;
            }
            while (ft == null && !cancelado) {  // Mientras no se acepte una facturación o se cancele la operación se lanzará el panel de facturación
                ft = JPrincipal.crearVentanaFacturacion(true, ticket);
                if (ft == null) {
                    if (!JPrincipal.getInstance().crearVentanaConfirmacion("No se puede facturar como Consumidor Final por un valor igual o superior a $" + VariablesAlm.IMPORTE_MAXIMO_SIN_FACTURAR + " . Acepte para ingresar datos o Cancele la transacción.")) {
                        cancelado = true;   // Cancelamos la operación y no seguimos.                            
                    }
                }
            }
        }
        return cancelado;
    }
}
