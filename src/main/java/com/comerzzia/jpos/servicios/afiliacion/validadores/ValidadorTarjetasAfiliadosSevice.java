/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.afiliacion.validadores;

import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public class ValidadorTarjetasAfiliadosSevice {
    
    private List<IValidadorTarjetaAfiliado> validadores;
    private static ValidadorTarjetasAfiliadosSevice instance;
    
    private static final Logger log = Logger.getMLogger(ValidadorTarjetasAfiliadosSevice.class);
    
    private ValidadorTarjetasAfiliadosSevice(){
        validadores = new ArrayList<IValidadorTarjetaAfiliado>();
    }
    
    
    private void cargarValidadores(){
        if (Sesion.config.isModoDesarrollo()){
            validadores.add(new ValidadorTarjetaAfiliadoDesarrollo());
        }
        validadores.add(new ValidadorTarjetaAfiliadoTienda());
        validadores.add(new ValidadorTarjetaAfiliadoCedula());
        validadores.add(new ValidadorTarjetaAfiliadoBines());
        validadores.add(new ValidadorTarjetaAfiliadoCreditoSK());
    }
    
    public static ValidadorTarjetasAfiliadosSevice getInstance(){
        if (instance == null){
            instance = new ValidadorTarjetasAfiliadosSevice();
            instance.cargarValidadores();
        } 
        return instance;
    }
    
    public ITarjetaAfiliacion validarBanda(String banda) throws TarjetaAfiliadoNotValidException{
        String numero;
        if (banda.indexOf("B") == 0){
            numero = banda.substring(1, banda.indexOf("^"));
        }
        else if(banda.indexOf("000") == 0){
            numero = banda.substring(3, banda.indexOf("^"));
        }if(banda.contains("9999000000000000")){
            numero = banda;
        }
        else{
            numero = "";
        }
        log.debug("validarBanda() - Numero de tarjeta descuento: " + numero);
        return validar(null, numero);
    }
    public ITarjetaAfiliacion validarNumTarjeta(String numTarjeta) throws TarjetaAfiliadoNotValidException{
        return validar(null, numTarjeta);
    }
    private ITarjetaAfiliacion validar(String banda, String numTarjeta) throws TarjetaAfiliadoNotValidException{
        TarjetaAfiliadoNotValidException error = null;
        for (IValidadorTarjetaAfiliado validador : validadores) {
            validador.inicializar(numTarjeta, banda);
            try{
                validador.validar();
                return validador.getTarjetaAfiliacion();
            }
            catch(TarjetaAfiliadoNotValidException e){
                error = e;
            }
        }
        throw error;
    }
}
