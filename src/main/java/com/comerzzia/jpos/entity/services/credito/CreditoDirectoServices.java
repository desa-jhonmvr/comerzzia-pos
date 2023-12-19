/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.credito;

import com.comerzzia.jpos.dto.credito.DatosCreditoDTO;
import com.comerzzia.jpos.servicios.credito.CreditoException;
import com.comerzzia.jpos.servicios.credito.CreditoNotFoundException;

/**
 *
 * @author Gabriel Simbania
 */
public interface CreditoDirectoServices {
    
    /**
     * 
     * @param identificacion
     * @return
     * @throws CreditoException
     * @throws CreditoNotFoundException 
     */
    DatosCreditoDTO consultaDatosByIdentificacion(String identificacion) throws CreditoException, CreditoNotFoundException ;
    
    
    
}
