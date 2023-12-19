/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.sukupon;

import com.comerzzia.jpos.persistencia.bonoEfectivo.BonoEfectivoBean;
import com.comerzzia.jpos.persistencia.sukupon.SukuponBean;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class SukuponManager {

    private static List<SukuponBean> sukupon;
    private static SukuponManager instancia = null;

    private static SukuponBean letraSeleccionada;

    private static final Logger log = Logger.getMLogger(SukuponManager.class);

    public static SukuponManager getInstance() {
        if (instancia == null) {
            instancia = new SukuponManager();
        }
        return instancia;
    }

    public List<SukuponBean> getSukupon() {
        if (sukupon != null) {
            return sukupon;
        } else {
            return new ArrayList<SukuponBean>();
        }
    }

    public void setsukupon(List<SukuponBean> sukupon) {
        sukupon = sukupon;
    }

    public void accionBuscarCedula(String text) throws SukuponCambioException {
        sukupon = SukuponServices.consultarLetrasPorCliente(text);
    }

    public SukuponBean getLetraSeleccionada() {
        return letraSeleccionada;
    }

    public void inicia() {
        SukuponManager.letraSeleccionada = null;
        SukuponManager.sukupon = new ArrayList<SukuponBean>();
    }

}
