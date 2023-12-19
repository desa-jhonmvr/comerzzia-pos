/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.afiliacion.validadores;

import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.afiliacion.TarjetaAfiliacionGeneral;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.util.ValidadorCedula;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author amos
 */
public class ValidadorTarjetaAfiliadoCedula implements IValidadorTarjetaAfiliado {

    private String numero;
    private static Logger log = Logger.getMLogger(ValidadorTarjetaAfiliadoCedula.class);
    
    @Override
    public void validar() throws TarjetaAfiliadoNotValidException { 
        log.debug("validar() - Validando tarjeta de afiliado mediante cédula");
        if(Sesion.getTienda().getAceptarCedulasDesc()=='S'){
            if(ValidadorCedula.verificarIdEcuador(ValidadorCedula.CEDULA, numero)){
                log.debug("validar() - Tarjeta validada por cédula");
                return;
            }
            throw new TarjetaAfiliadoNotValidException("Cédula no válida.");
        }
        throw new TarjetaAfiliadoNotValidException("No se permite la Cédula como tarjeta de afiliado.");
    }

    @Override
    public ITarjetaAfiliacion getTarjetaAfiliacion() {
        TarjetaAfiliacionGeneral tarjeta = new TarjetaAfiliacionGeneral();
        tarjeta.setNumero(numero);
        tarjeta.setTipoAfiliacion(ITarjetaAfiliacion.TIPO_AFILIACION_CEDULA);
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
