/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.codigosBarra;

/**
 *
 * @author MGRI
 */
public abstract class CodigoBarrasBono extends CodigoBarrasRecibo{
    
    
    public CodigoBarrasBono(){
        super();
    }   

    public CodigoBarrasBono(String codBarras) throws NotAValidBarCodeException{
        super(codBarras);
    }

    @Override
    public Integer getTipoCodigo() {
        return TIPO_BONO;
    }
    
    @Override
    public abstract Integer getSubTipoCodigo();

    
   
}
