/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.credito;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.persistencia.credito.plasticos.PlasticoBean;
import com.comerzzia.jpos.pinpad.excepciones.AutorizadorAutomaticoNoPermitidoException;
import com.comerzzia.jpos.pinpad.excepciones.AutorizadorException;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoSK;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.sukasa.AutorizadorManual;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author amos
 */
public class PagoCreditoSK extends PagoCredito {

    protected static Logger log = Logger.getMLogger(PagoCreditoSK.class);
    protected PlasticoBean plastico;
    protected Integer mesesPosfechado;
    protected String textoPosfechado;

    public PagoCreditoSK(PagosTicket pagos, Cliente cliente, String autorizador) {
        super(pagos, cliente, autorizador);
        mesesPosfechado = 0;
    }

    public PagoCreditoSK(PagosTicket pagos, Cliente cliente, String autorizador, Integer numeroCredito, String cedulaTarjetaCD) {
        this(pagos, cliente, autorizador);
        plastico = new PlasticoBean(numeroCredito);
        plastico.setCedulaCliente(cedulaTarjetaCD);
        mesesPosfechado = 0;
    }

    @Override
    public void autorizarPago() throws AutorizadorException {
        if (isValidado()) {
            return;
        }
        
        TarjetaCreditoSK tarjeta = (TarjetaCreditoSK) getTarjetaCredito();
        plastico = tarjeta.getPlastico();

        if (!isAdmiteAutorizacionAutomatica()){
            throw new AutorizadorAutomaticoNoPermitidoException("No está permitida la autorización automática para este pago.", this);
        }
        try {
            if (plastico.getEstado() == null) {
                throw new AutorizadorException("La tarjeta indicada no tiene un estado válido.");
            }
            if (plastico.getEstado().isRequiereAutorizacion()) {
                throw new AutorizadorException("El estado de la tarjeta indicada no permite autorizar la operación.");
            }
            BigDecimal cupoVirtual = plastico.getCupo().getCupo();
            //Error Probar NUmero Tarjeta 24/11/2017
            if (Numero.isMenor(cupoVirtual, getUstedPaga())) {
//Comantado Para realizar pruebas
//                throw new AutorizadorException("El cupo disponible es menor que el importe que desea pagar.");
            }
            setValidadoAutomatico(true);
        }
        catch (AutorizadorException e) {
            e.setPago(this);
            setFalloValidacion(true);
            throw e;
        }
    }

    @Override
    public String validarAutorizacionManual() {
        log.debug("validarAutorizacionManual() - Validando código de autorización manual: " + getCodigoValidacionManual());
        Integer numeroCredito = plastico != null ? plastico.getNumeroCredito() : ((TarjetaCreditoSK)getTarjetaCredito()).getPlastico().getNumeroCredito();
         //autorizado antes RD
//        boolean autorizado = AutorizadorManual.autorizacionManual(getCodigoValidacionManual(), numeroCredito, getUstedPaga());
        boolean autorizado = AutorizadorManual.autorizacionManual(getCodigoValidacionManual(), numeroCredito,getUstedPaga());
         if (autorizado) {
            log.debug("validarAutorizacionManual() - Validación correcta.");
            return null;
        }
        return "El código de autorización no es correcto.";
    }

    public PlasticoBean getPlastico() {
        return plastico;
    }

    @Override
    public void anularAutorizacionPago(List<Pago> anuladosImpresos) {
        setValidadoManual(false);
        setValidadoAutomatico(false);
        setFalloValidacion(false);
        setAuditoria(0);
    }

    @Override
    public String getDocumento() {
        if(getPlastico() != null){
            return getPlastico().getCedulaCliente();
        }else{
            return ((TarjetaCreditoSK)getTarjetaCredito()).getPlastico().getCedulaCliente();
        }
    }

    public Integer getMesesPosfechado() {
        return mesesPosfechado;
    }

    public void setMesesPosfechado(Integer mesesPosfechado) {
        this.mesesPosfechado = mesesPosfechado;
    }

    public boolean isPosfechado(){
        return mesesPosfechado > 0;
    }

    public String getTextoPosfechado() {
        return textoPosfechado;
    }

    public void setTextoPosfechado(String textoPosfechado) {
        this.textoPosfechado = textoPosfechado;
    }

    @Override
    public boolean isSoloPagoCredito(){
        return false;
    }

}
