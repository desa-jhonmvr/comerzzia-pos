/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.reservaciones;

import java.math.BigDecimal;

/**
 *
 * @author MGRI
 */
public interface IPagoImporteCall {
    
    
    public void crearVentanaPagos(BigDecimal importeMinimo,BigDecimal importeMaximo);
}
