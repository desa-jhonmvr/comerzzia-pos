/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.credito;

import com.comerzzia.jpos.dto.credito.DatosCreditoDTO;
import com.comerzzia.jpos.persistencia.credito.plasticos.PlasticoBean;
import com.comerzzia.jpos.servicios.credito.CreditoException;
import com.comerzzia.jpos.servicios.credito.CreditoNotFoundException;
import com.comerzzia.jpos.servicios.credito.CreditoServices;

/**
 *
 * @author Gabriel Simbania
 */
public class CreditoDirectoServicesImpl implements CreditoDirectoServices {

    @Override
    public DatosCreditoDTO consultaDatosByIdentificacion(String identificacion) throws CreditoException, CreditoNotFoundException {

        DatosCreditoDTO datosCredito = new DatosCreditoDTO();
        PlasticoBean plasticoBean = CreditoServices.consultarPlasticoPorCedula(identificacion);
        if (plasticoBean.getEstado().isValida()){
            CreditoServices.consultarCupoTarjeta(plasticoBean);
            datosCredito.setCedula(identificacion);
            datosCredito.setCredito((long) plasticoBean.getNumeroCredito());
            datosCredito.setPlastico(mascaraEnPlastico(plasticoBean.getNumeroTarjeta()));
            datosCredito.setCupo(plasticoBean.getCupo().getCupo());
        }else{
            throw new CreditoException("El cr\u00E9dito no se encuentra activo");
        }
        return datosCredito;

    }
    
    /**
     * 
     * @param numeroPlastico
     * @return 
     */
    private String mascaraEnPlastico(String numeroPlastico){
        
        String primerosNumeros = numeroPlastico.substring(0,4);
        String ultimosNumeros = numeroPlastico.substring(numeroPlastico.length()-5,numeroPlastico.length()-1);
        
        String resultadoFinal=primerosNumeros.concat("XXXXXXX").concat(ultimosNumeros);
        
        return resultadoFinal;
    }
}
