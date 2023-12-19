/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.gui;

import com.comerzzia.jpos.entity.db.CajaDet;
import java.util.List;

/**
 * Clase para la representación de movimientos dentro de una tabla de movimientos en la capa GUI
 * @author MGRI
 */
public class Movimientos {
    private List<CajaDet> listaMovimientos;

    public List<CajaDet> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<CajaDet> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }
}
