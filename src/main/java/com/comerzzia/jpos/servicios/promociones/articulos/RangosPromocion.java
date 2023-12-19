/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.articulos;

import es.mpsistemas.util.xml.XMLDocumentNode;
import es.mpsistemas.util.xml.XMLDocumentNodeNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AMS
 */
public class RangosPromocion {

    private List<RangoPromociones> rangos;

    public RangosPromocion(XMLDocumentNode contieneRangos) {
        try {
            this.rangos = new ArrayList<RangoPromociones>();
            List<XMLDocumentNode> rangosXML = contieneRangos.getNodo("rangos", true).getHijos();
            if (rangosXML != null) {
                for (XMLDocumentNode rango : rangosXML) {

                    BigDecimal inicio = rango.getNodo("inicio").getValueAsBigDecimal();
                    BigDecimal fin = rango.getNodo("fin").getValueAsBigDecimal();
                    String descripcion = rango.getNodo("descripcion").getValue();

                    RangoPromociones rang = new RangoPromociones(inicio, fin, descripcion);
                    rangos.add(rang);
                }
                Collections.sort(rangos);
            }
        }
        catch (XMLDocumentNodeNotFoundException ex) {
            Logger.getLogger(RangosPromocion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getRangoAplicable(BigDecimal valor) {
        for (RangoPromociones rango : rangos) {
            if (rango.isAplicable(valor)) {
                return rango.getDescripcion();
            }
        }
        // comprobamos si es mayor que todos y devolvemos el último
        if (!rangos.isEmpty()) {
            RangoPromociones rangoMayor = rangos.get(rangos.size() - 1);
            if (rangoMayor.getFin().compareTo(valor) <= 0) {
                return rangoMayor.getDescripcion();
            }
        }
        return null;
    }

    public Integer getRangoAplicableAsInt(BigDecimal valor) {
        String result = getRangoAplicable(valor);
        if (result == null) {
            return null;
        }
        return Integer.parseInt(result);
    }

    public List<RangoPromociones> getRangos() {
        return rangos;
    }

    public void setRangos(List<RangoPromociones> rangos) {
        this.rangos = rangos;
    }

    public void ajustaRangoMinimo(BigDecimal minimo) {
        if (!rangos.isEmpty()) {
            rangos.get(0).setInicio(minimo);
        }

    }
}
