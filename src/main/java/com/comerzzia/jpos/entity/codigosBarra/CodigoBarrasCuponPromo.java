package com.comerzzia.jpos.entity.codigosBarra;

public class CodigoBarrasCuponPromo extends CodigoBarrasRecibo{
    
    
    private Long idCupon;


    public CodigoBarrasCuponPromo(Long idCupon) {
        super();
        this.idCupon = idCupon;

    }

    public CodigoBarrasCuponPromo(String codigobarras) throws NotAValidBarCodeException {
        super(codigobarras);
        if (codBarras.length() < 20) {
            throw new NotAValidBarCodeException();
        }
        int tipoCodigo = new Integer(codBarras.substring(6, 8));
        if (!TIPO_CUPON.equals(tipoCodigo)) {
            throw new NotAValidBarCodeException();
        }
        if (codBarras.length()== 20){
            this.idCupon = new Long(codBarras.substring(10, 20));
        }
        else{
            this.idCupon = new Long(codBarras.substring(10, 21));
        }
    }

    @Override
    public String getCodigoBarras() {
        return getPrefijoCodigo() + formatear(idCupon.toString(), 11);
    }

    @Override
    public Integer getTipoCodigo() {
        return TIPO_CUPON;
    }

    @Override
    public Integer getSubTipoCodigo() {
        return SUBTIPO_CUPON_GENERAL;
    }

    public Long getIdCupon() {
        return idCupon;
    }
    
}
