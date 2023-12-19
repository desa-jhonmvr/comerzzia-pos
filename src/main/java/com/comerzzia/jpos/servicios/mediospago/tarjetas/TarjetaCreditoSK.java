/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.mediospago.tarjetas;

import com.comerzzia.jpos.persistencia.credito.plasticos.PlasticoBean;
import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.credito.CreditoException;
import com.comerzzia.jpos.servicios.credito.CreditoNotFoundException;
import com.comerzzia.jpos.servicios.credito.CreditoServices;
import es.mpsistemas.util.fechas.Fecha;

/**
 *
 * @author amos
 */
public class TarjetaCreditoSK extends TarjetaCredito implements ITarjetaAfiliacion{

    protected PlasticoBean plastico;
    
    
    public TarjetaCreditoSK(String numero, String caducidad, String cvv) {
        super(numero, caducidad, cvv);
    }
    
    public TarjetaCreditoSK(String numero, String caducidad, String cvv, boolean esLecturaDesdeCedula) {
        super(numero, caducidad, cvv, esLecturaDesdeCedula);
    }

    public TarjetaCreditoSK(String bandaMagnetica) {
        super(bandaMagnetica);
    }
    
    public TarjetaCreditoSK(String bandaMagnetica,PlasticoBean plastico ) {
        super(bandaMagnetica);
        this.plastico=plastico;
    }
    
    public TarjetaCreditoSK(String numero, boolean esLecturaDesdeCedula) {
        super(numero,esLecturaDesdeCedula);
    }

    @Override
    public void validaTarjetaCredito() throws TarjetaInvalidaException {
        try{
            validar();
            CreditoServices.consultarCupoTarjeta(plastico);
            if (plastico.getEstado().isAnulada()){
                throw new TarjetaInvalidaException("Esta tarjeta está cancelada, consulte status.", false);
            }
        }
        catch(TarjetaInvalidaException e){
            throw new TarjetaInvalidaException(e.getMessage(), e, false);
        }
        catch(CreditoNotFoundException e){
            throw new TarjetaInvalidaException(e.getMessage(), e, false);
        }
        catch(CreditoException e){
            throw new TarjetaInvalidaException("Error intentando obtener datos de la tarjeta contra el sistema de crédito.", e, false);
        }
    }

    public void validar() throws TarjetaInvalidaException{
        try{
            if (plastico == null){
                plastico = CreditoServices.consultarPlasticoPorNumero(getNumero());
            }
            if (plastico.getEstado() == null){
                throw new TarjetaInvalidaException("La tarjeta no tiene un estado válido.");
            }
            if (plastico.getEstado().isAnulada()){
                throw new TarjetaInvalidaException("Esta tarjeta está cancelada, consulte status.");
            }
//            Retiramos validacion fechas Ricardo
//            Integer anyoCaducidadTarjeta = super.getCaducidad() / 100;
//            Integer mesCaducidadTarjeta = super.getCaducidad() - (anyoCaducidadTarjeta*100);
//            Fecha fecha = new Fecha();
//            Integer anyoAhora = fecha.getAño() % 100;
//            if(anyoAhora > anyoCaducidadTarjeta || (anyoCaducidadTarjeta == anyoAhora && fecha.getMesNumero() > mesCaducidadTarjeta)){
//                throw new TarjetaInvalidaException("Esta tarjeta está caducada");
//            }
        }
        catch(CreditoNotFoundException e){
            throw new TarjetaInvalidaException("El número de plástico no existe.", e);
        }
        catch(CreditoException e){
            throw new TarjetaInvalidaException(e.getMessage(), e);
        }
    }
    
    public PlasticoBean getPlastico() {
        return plastico;
    }

    public void setPlastico(PlasticoBean plastico) {
        this.plastico = plastico;
    }
    
    
    
    @Override
    public String getTipoAfiliacion(){
        return ITarjetaAfiliacion.TIPO_AFILIACION_CREDITO_SK;
    }

    @Override
    public String getTipoLectura() {
        return isLecturaBandaManual() ? ITarjetaAfiliacion.TIPO_LECTURA_MANUAL : ITarjetaAfiliacion.TIPO_LECTURA_AUTOMATICA;
    }
    
    
    
    
}
