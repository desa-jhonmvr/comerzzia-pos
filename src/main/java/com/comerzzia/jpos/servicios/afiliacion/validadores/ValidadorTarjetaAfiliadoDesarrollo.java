/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.afiliacion.validadores;

import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.afiliacion.TarjetaAfiliacionGeneral;

/**
 *
 * @author amos
 */
public class ValidadorTarjetaAfiliadoDesarrollo implements IValidadorTarjetaAfiliado {

    private String numero;
    
    @Override
    public void validar() throws TarjetaAfiliadoNotValidException {
    }

    @Override
    public ITarjetaAfiliacion getTarjetaAfiliacion() {
        TarjetaAfiliacionGeneral tarjeta = new TarjetaAfiliacionGeneral();
        tarjeta.setNumero(numero);
        tarjeta.setTipoAfiliacion(ITarjetaAfiliacion.TIPO_AFILIACION_DESARROLLO);
        tarjeta.setTipoLectura(ITarjetaAfiliacion.TIPO_LECTURA_MANUAL);
        return tarjeta;
    }

    @Override
    public void inicializar(String numTarjeta, String banda) {
        this.numero = numTarjeta;
    }
    
}
