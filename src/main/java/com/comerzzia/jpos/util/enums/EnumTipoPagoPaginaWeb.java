/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.enums;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public enum EnumTipoPagoPaginaWeb {

    CREDITO_DIRECTO,
    PLACE_TO_PAY,
    PAYMENTEZ,
    DATA_FAST;

    public static EnumTipoPagoPaginaWeb findEnumByOrdinal(int ordinal) {

        List<EnumTipoPagoPaginaWeb> list = Arrays.asList(EnumTipoPagoPaginaWeb.values());

        if (ordinal < list.size()) {
            return list.get(ordinal);
        }

        return null;
    }

}
