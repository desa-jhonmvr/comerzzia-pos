/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.afiliacion.validadores;

import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.afiliacion.TarjetaAfiliacionGeneral;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author amos
 */
public class ValidadorTarjetaAfiliadoTienda  implements IValidadorTarjetaAfiliado {

    private final String tarjetaLocalValido = "999900000000000";
    private String numero;
    private static Logger log = Logger.getMLogger(ValidadorTarjetaAfiliadoTienda.class);
    
    
    @Override
    public void validar() throws TarjetaAfiliadoNotValidException {
        log.debug("validar() - Validando tarjeta de afiliación local");
        if(Sesion.getTienda().getAceptarTarjAfiliacionDesc() == 'S'){
            if(numero.equals(tarjetaLocalValido)){
                log.debug("validar() - Tarjeta validada por tarjeta de afiliación de local");
                return;
            }
            throw new TarjetaAfiliadoNotValidException("Número de tarjeta de afiliado local no válido.");
        }
        throw new TarjetaAfiliadoNotValidException("No se permiten tarjetas de afiliados locales.");
    }

    @Override
    public ITarjetaAfiliacion getTarjetaAfiliacion() {
        TarjetaAfiliacionGeneral tarjeta = new TarjetaAfiliacionGeneral();
        tarjeta.setNumero(numero);
        tarjeta.setTipoAfiliacion(ITarjetaAfiliacion.TIPO_AFILIACION_TARJETA_TIENDA);
        tarjeta.setTipoLectura(ITarjetaAfiliacion.TIPO_LECTURA_MANUAL);
        return tarjeta;
    }

    @Override
    public void inicializar(String numTarjeta, String banda) {
        this.numero = numTarjeta;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
}
