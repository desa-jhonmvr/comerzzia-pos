package com.comerzzia.jpos.entity.codigosBarra;

import java.math.BigInteger;

/**
 *
 * @author Dren
 */
public class CodigoBarrasAbono extends CodigoBarrasReservacion{
    
    private Long nAbono;
    
    public CodigoBarrasAbono(){
        super();
    }
    
    public CodigoBarrasAbono(BigInteger nReserva,Long nAbono){
        super(nReserva);
        this.nAbono=nAbono;
    }
    
    @Override
    public Integer getSubTipoCodigo() {
        return SUBTIPO_RESERVACION_ABONO;
    }    
    
    @Override
    public String getCodigoBarras() {
        return super.getCodigoBarras() + formatear(nAbono.toString(), 2);
    }
    
}
