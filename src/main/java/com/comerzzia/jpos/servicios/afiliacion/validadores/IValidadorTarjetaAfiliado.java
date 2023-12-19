/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.afiliacion.validadores;

import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;

/**
 *
 * @author amos
 */
public interface IValidadorTarjetaAfiliado {
    
    public void inicializar(String numTarjeta, String banda);
    
    public void validar() throws TarjetaAfiliadoNotValidException;
    
    public ITarjetaAfiliacion getTarjetaAfiliacion();
    
}
