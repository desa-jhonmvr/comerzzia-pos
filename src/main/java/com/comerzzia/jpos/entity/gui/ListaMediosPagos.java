/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.gui;

import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class ListaMediosPagos {


    public List<MedioPagoBean> getListaMediosPagos(){
        return MediosPago.getInstancia().getMediosPagoLista();
    }
    
    public List<MedioPagoBean> getListaMediosPagosVacio(){
        LinkedList<MedioPagoBean> mediosPago = new LinkedList<MedioPagoBean>(getListaMediosPagos());
        mediosPago.addFirst(MedioPagoBean.getMedioPagoVacio());
        return mediosPago;
    }
}
