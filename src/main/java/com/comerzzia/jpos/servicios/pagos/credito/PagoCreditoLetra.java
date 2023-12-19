/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.credito;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.persistencia.letras.LetraBean;
import com.comerzzia.jpos.pinpad.excepciones.AutorizadorException;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import java.util.List;

/**
 *
 * @author amos
 */
public class PagoCreditoLetra extends PagoCredito {

    private LetraBean letra;
    
    public PagoCreditoLetra(PagosTicket pagos, Cliente cliente, String autorizador) {
        super(pagos, cliente, autorizador);
    }

    public LetraBean getLetra() {
        return letra;
    }

    public void setLetra(LetraBean letra) {
        this.letra = letra;
    }
    
    public boolean isCuotasGeneradas(){
        return (letra != null && letra.getCuotas()!=null && letra.getCuotas().size()>0);
    }

    @Override
    public void autorizarPago() throws AutorizadorException { 
        setValidadoAutomatico(true);
    }
    
    
    @Override
    public void anularAutorizacionPago(List<Pago> anuladosImpresos) {
        setValidadoAutomatico(false);
    }
    
    @Override
    public boolean isSoloPagoCredito(){
        return false;
    }
}
