/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.especiales;

import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.pagos.Pago;
import static com.comerzzia.jpos.servicios.pagos.Pago.PAGO_OTROS;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class PagoRetencionFuente extends Pago{
    
    private BigDecimal porcentajeRetencion;
    
    public PagoRetencionFuente(PagosTicket pagos) {
        super(pagos, null, null, null); // el autorizador tendrá que ser seteado a posteriori
    }
    
    public void inicializar(BigDecimal base, BigDecimal iva, BigDecimal descuento, BigDecimal valor) throws PagoInvalidException{
        this.porcentajeRetencion = BigDecimal.ZERO;
        saldoInicial = base; 
        //this.descuento = Numero.redondear(descuento); 
        
        this.descuento = Numero.redondear(new BigDecimal(0));
        //this.total = Numero.porcentajeR(saldoInicial, porcentajeRtencionFuente);
        //this.total = this.total.add(Numero.porcentajeR(iva, new BigDecimal(10)));
        this.ustedPaga = valor;
        recalcularFromUstedPaga(String.valueOf(ustedPaga));
        entregado = ustedPaga;
        medioPagoActivo = MediosPago.getInstancia().getPagoRetencion();
        pagoActivo = PAGO_OTROS;
    }

    @Override
    public String getUstedPagaRenderer() {
        return " $ " + getTotal().toString() + " ";
    }

    @Override
    public BigDecimal getAhorroRenderer() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getUstedPagaPrint() {
        return total;
    }

    public BigDecimal getPorcentajeRetencion() {
        return porcentajeRetencion;
    }
    
    
}
