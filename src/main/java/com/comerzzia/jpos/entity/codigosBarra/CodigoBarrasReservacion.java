package com.comerzzia.jpos.entity.codigosBarra;

import com.comerzzia.jpos.servicios.login.Sesion;
import java.math.BigInteger;

/**
 *
 * @author Dren
 */
public class CodigoBarrasReservacion extends CodigoBarrasRecibo {

    private BigInteger nReserva;

    public CodigoBarrasReservacion() {
        super();
    }

    public CodigoBarrasReservacion(BigInteger nReserva) {
        super();
        this.nReserva = nReserva;

    }

    public CodigoBarrasReservacion(String codigobarras) throws NotAValidBarCodeException, NotAReservationOfThisShopException {
        super(codigobarras);
        if (codBarras.length() < 13) {
            throw new NotAValidBarCodeException();
        }

        int tipoCodigo = new Integer(codBarras.substring(6, 8));
        if (!TIPO_RESERVACION.equals(tipoCodigo)) {
            throw new NotAValidBarCodeException();
        }
        this.nReserva = new BigInteger(codBarras.substring(8, 13));
        if (!getAlmacen().equals(Sesion.getTienda().getAlmacen().getCodalm())) {
            throw new NotAReservationOfThisShopException();
        }
    }

    @Override
    public String getCodigoBarras() {
        return IDENTIFICADOR_INTERNO 
                + formatear(getAlmacen(), 3)
                + formatear(getTipoCodigo(), 2)
                + formatear(nReserva.intValue(), 5);
    }

    @Override
    public Integer getTipoCodigo() {
        return TIPO_RESERVACION;
    }

    @Override
    public Integer getSubTipoCodigo() {
        return SUBTIPO_RESERVACION_GENERAL;
    }

    public BigInteger getnReserva() {
        return nReserva;
    }
}
