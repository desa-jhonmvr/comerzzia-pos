/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.modelos.combo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author MGRI
 */
public class ComboGenericoModel extends AbstractListModel implements ComboBoxModel {

    ArrayList<IComboComponent> listaDeElementos;
    IComboComponent selection;

    @Override
    public int getSize() {
        return listaDeElementos.size();
    }

    @Override
    public Object getElementAt(int i) {
        return listaDeElementos.get(i).getComboValor();
    }

    @Override
    public void setSelectedItem(Object o) {
        selection = null;
        String buscado = (String) o;
        Iterator it = listaDeElementos.iterator();
        boolean encontrado = false;
        while (it.hasNext() && !encontrado) {
            IComboComponent aux = (IComboComponent) it.next();
            if (aux.getComboTexto().equals(buscado)) {
                selection = aux;
                encontrado = true;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
}
