
/*
 * JTextRelojAnalogico.java
 *
 * Created on 04-jul-2011, 11:31:17
 */
package com.comerzzia.jpos.gui.components;

/**
 *
 * @author MGRI
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Calendar;


public class JLabelRelojAnalogico extends JLabel {
    javax.swing.Timer m_t;

    //Contructor
    public JLabelRelojAnalogico() {
        //... Seteamos los atributos por defecto.
        
        setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        setForeground(Color.getHSBColor(0, 149, 227));

        //... Creamos un temporizador de intervalo 1 segundo.
        m_t = new javax.swing.Timer(1000, new ClockTickAction());
        m_t.start();  // Refrescamos cada segundo
        m_t.setDelay(1000*60);
    }

    //Clase interna listener
    private class ClockTickAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Pintamos la fecha y hora
            Calendar now = Calendar.getInstance();
            int dia = now.get(Calendar.DAY_OF_MONTH);
            int mes = now.get(Calendar.MONTH) + 1;
            int anho = now.get(Calendar.YEAR);
            int h = now.get(Calendar.HOUR_OF_DAY);
            int m = now.get(Calendar.MINUTE);
            int s = now.get(Calendar.SECOND);
            String ph="",pm="",ps="";
            
            if (h<10)
                ph="0";
            if (m<10)
                pm="0";
            if (s<10)
                ps="0";
            setText(""+ dia +"/"+ mes +"/"+ anho + "  " +ph+h + ":" + pm+m); 
        }
    }
}
