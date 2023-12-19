/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.eventos;

import com.comerzzia.jpos.gui.components.form.JButtonForm;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.servicios.login.Sesion;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 *
 * @author MGRI
 */
public class SeleccionaFocusFormListener implements FocusListener {

    Color colorFondo = Color.WHITE;
    //private static final Color colorError = new Color(236, 121, 121, 255);
    private static final Color colorSeleccionBM = new Color(255, 228, 122, 255);
    private static final Color colorSeleccionSK = new Color(153, 202, 253, 255);
    

    @Override
    public void focusGained(FocusEvent fe) {
        Component cmpnt = fe.getComponent();

        if (cmpnt instanceof IValidableForm) {
            IValidableForm cmp = ((IValidableForm) cmpnt);
            //if (cmp.getBackground()!=null && !cmp.getBackground().equals(colorError)) {
            if (Sesion.isSukasa()){
                cmp.setBackground(colorSeleccionSK);
            }
            else{
                cmp.setBackground(colorSeleccionBM);
            }
            cmp.selectAll();
            //}
        }
        if (cmpnt instanceof JButtonForm) {
            ((JButtonForm) cmpnt).setForeground(new Color(162,28,28));
            //((JButtonForm) cmpnt).setFont(((JButtonForm) cmpnt).getFont().deriveFont(((JButtonForm) cmpnt).getFont().getStyle() | java.awt.Font.BOLD));
            //((JButtonForm) cmpnt).setFont(new java.awt.Font("Arial Rounded MT Bold", Font.BOLD, 12));
        }
    }

    @Override
    public void focusLost(FocusEvent fe) {
        Component cmpnt = fe.getComponent();
        if (cmpnt instanceof IValidableForm) {
            IValidableForm cmp = ((IValidableForm) cmpnt);
            //try {
            // cmp.validar();
            // Si la validación falla salta la excepción
            cmp.setBackground(colorFondo);

            //}
            /*catch (ValidationFormException ex) {
            cmp.setBackground(colorError);
            cmp.requestFocus();
            }*/
        }
        if (cmpnt instanceof JButtonForm) {
            ((JButtonForm) cmpnt).setForeground(Color.BLACK);
            //((JButtonForm) cmpnt).setFont(((JButtonForm) cmpnt).getFont().deriveFont(((JButtonForm) cmpnt).getFont().getStyle() | java.awt.Font.PLAIN));
            //((JButtonForm) cmpnt).setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12));
        }


    }
}