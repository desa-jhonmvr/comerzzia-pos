/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones;

import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoBean;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;

/**
 *
 * @author MGRI
 */
public class DatosAbono {
    
    private boolean esultimoAbono =false;
    private PagosTicket ultimosPagos;
    private ReservaAbonoBean ultimoAbono;
            
    void setPagos(PagosTicket pagos) {
        this.setUltimosPagos(pagos);
    }

    void setAbono(ReservaAbonoBean nuevoAbono) {
        this.setUltimoAbono(nuevoAbono);
    }

    public PagosTicket getUltimosPagos() {
        return ultimosPagos;
    }

    public void setUltimosPagos(PagosTicket ultimosPagos) {
        this.ultimosPagos = ultimosPagos;
    }

    public ReservaAbonoBean getUltimoAbono() {
        return ultimoAbono;
    }

    public void setUltimoAbono(ReservaAbonoBean ultimoAbono) {
        this.ultimoAbono = ultimoAbono;
    }

    public boolean isEsultimoAbono() {
        return esultimoAbono;
    }

    public void setEsultimoAbono(boolean esultimoAbono) {
        this.esultimoAbono = esultimoAbono;
    }
      
}
