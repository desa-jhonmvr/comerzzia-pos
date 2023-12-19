/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.afiliacion.validadores;

import com.comerzzia.jpos.persistencia.credito.plasticos.PlasticoBean;
import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.afiliacion.TarjetaAfiliacionGeneral;
import com.comerzzia.jpos.servicios.credito.CreditoException;
import com.comerzzia.jpos.servicios.credito.CreditoNotFoundException;
import com.comerzzia.jpos.servicios.credito.CreditoServices;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author amos
 */
public class ValidadorTarjetaAfiliadoCreditoSK implements IValidadorTarjetaAfiliado {

    private String numero;
    private static Logger log = Logger.getMLogger(ValidadorTarjetaAfiliadoCreditoSK.class);

    @Override
    public void validar() throws TarjetaAfiliadoNotValidException {
        log.debug("validar() - Validando tarjeta mediante crédito Sukasa");
        try {
            PlasticoBean plastico = null;
            if (numero.length() == 10){
                plastico = CreditoServices.consultarPlasticoValidoPorCedula(numero);
            }
            else{
                plastico = CreditoServices.consultarPlasticoPorNumero(numero);
            }
            if(plastico.getEstado() == null){
                throw new TarjetaAfiliadoNotValidException("No válida: Tarjeta sin estado");
            }
            if (plastico.getEstado().isAnulada()) {
                throw new TarjetaAfiliadoNotValidException("No válida: Tarjeta cancelada");
            }
            log.debug("validar() - Tarjeta validada por plástico: "+plastico.getNumeroTarjeta().toString());
        }
        catch (CreditoNotFoundException e) {
            throw new TarjetaAfiliadoNotValidException("<html>Nro. de tarjeta no encontrado, replicando información, espere<br> un momento y vuelva a intentar</html>", e);
        }
        catch (CreditoException e) {
            throw new TarjetaAfiliadoNotValidException(e.getMessage(), e);
        }
    }

    @Override
    public ITarjetaAfiliacion getTarjetaAfiliacion() {
        TarjetaAfiliacionGeneral tarjeta = new TarjetaAfiliacionGeneral();
        tarjeta.setNumero(numero);
        tarjeta.setTipoAfiliacion(ITarjetaAfiliacion.TIPO_AFILIACION_CREDITO_SK);
        tarjeta.setTipoLectura(ITarjetaAfiliacion.TIPO_LECTURA_MANUAL);
        return tarjeta;
    }

    @Override
    public void inicializar(String numTarjeta, String banda) {
        this.numero = numTarjeta;
    }
}
