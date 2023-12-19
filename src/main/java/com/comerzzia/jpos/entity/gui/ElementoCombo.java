/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.gui;

import com.comerzzia.jpos.gui.modelos.combo.IComboComponent;

/**
 *
 * @author MGRI
 */
public class ElementoCombo implements IComboComponent {

    private String texto;
    private String valor;

    @Override
    public String getComboTexto() {
        return texto;
    }

    @Override
    public String getComboValor() {
        return valor;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
