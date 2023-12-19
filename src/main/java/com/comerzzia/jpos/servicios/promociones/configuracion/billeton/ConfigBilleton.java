/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.configuracion.billeton;

import com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.ConfiguracionBilletonBean;
import com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.detalles.ConfiguracionBilletonDetalleBean;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author amos
 */
public class ConfigBilleton {
    private List<ConfiguracionBilletonBean> configuraciones;
    
    public ConfigBilleton(List<ConfiguracionBilletonBean> configuraciones){
        this.configuraciones = configuraciones;
    }

    public ConfiguracionBilletonDetalleBean getDetalleAplicable(String tipo, Boolean auspiciante, BigDecimal valor) {
        ConfiguracionBilletonDetalleBean detalle = null;
        for (ConfiguracionBilletonBean configuracion : configuraciones) {
            if (configuracion.getTipo().equals(tipo)){
                detalle = configuracion.getDetalleAplicable(auspiciante, valor);
                if (detalle != null) {
                    return detalle;
                }
            }
        }
        return detalle;
    }
    
}
