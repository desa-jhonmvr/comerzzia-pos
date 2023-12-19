package com.comerzzia.jpos.entity.codigosBarra;

public class CodigoBarrasBonoManual extends CodigoBarrasBono {

    private Long idBono;

    public CodigoBarrasBonoManual(String codigobarras) throws NotAValidBarCodeException {
        super(codigobarras);
        if (codigobarras.length() < 20) {
            throw new NotAValidBarCodeException();
        }
        int tipo = new Integer(codigobarras.substring(6, 8));
        if (!TIPO_BONO.equals(tipo)) {
            throw new NotAValidBarCodeException();
        }
        int subtipo = new Integer(codigobarras.substring(8, 10));
        if (!SUBTIPO_BONO_MANUAL.equals(subtipo)) {
            throw new NotAValidBarCodeException();
        }
        this.idBono = new Long(codigobarras.substring(10, 20));
    }

    @Override
    public String getCodigoBarras() {
        return getPrefijoCodigo() + formatear(getIdBono().toString(), 10);
    }

    @Override
    public Integer getSubTipoCodigo() {
        return SUBTIPO_BONO_MANUAL;
    }

    public Long getIdBono() {
        return idBono;
    }
}
