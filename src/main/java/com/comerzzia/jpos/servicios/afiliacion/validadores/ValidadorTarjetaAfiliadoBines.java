/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.afiliacion.validadores;

import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.afiliacion.TarjetaAfiliacionGeneral;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author amos
 */
public class ValidadorTarjetaAfiliadoBines  implements IValidadorTarjetaAfiliado {

    private String numero;
    private static Logger log = Logger.getMLogger(ValidadorTarjetaAfiliadoBines.class);
    
    @Override
    public void validar() throws TarjetaAfiliadoNotValidException {
        log.debug("validar() - Validando tarjeta mediante bines");
        String binesValidos = Variables.getVariable(Variables.POS_CONFIG_BINES_TARJETA_DESCUENTO);
        String[] todosBines = binesValidos.split(";");
        for(int i=1;i<=numero.length()-1;i++){
            String subTarjeta = numero.substring(0,i);
            for(String bin:todosBines){
                if(bin.equals(subTarjeta)){
                    log.debug("validar() - Tarjeta validada por bin: "+bin);
                    return;
                }
            }
        }
        throw new TarjetaAfiliadoNotValidException();
    }

    @Override
    public ITarjetaAfiliacion getTarjetaAfiliacion() {
        TarjetaAfiliacionGeneral tarjeta = new TarjetaAfiliacionGeneral();
        tarjeta.setNumero(numero);
        tarjeta.setTipoAfiliacion(ITarjetaAfiliacion.TIPO_AFILIACION_TARJETA_BINES);
        tarjeta.setTipoLectura(ITarjetaAfiliacion.TIPO_LECTURA_MANUAL);
        return tarjeta;
    }

    @Override
    public void inicializar(String numTarjeta, String banda) {
        this.numero = numTarjeta;
        if (banda!= null && numero == null){
            numero = banda.substring(4, 20);
        }
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
}
