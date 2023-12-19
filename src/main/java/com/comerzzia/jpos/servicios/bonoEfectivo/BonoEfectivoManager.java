/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.bonoEfectivo;

import com.comerzzia.jpos.persistencia.bonoEfectivo.BonoEfectivoBean;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class BonoEfectivoManager {

    private static List<BonoEfectivoBean> bonoEfectivo;
    private static BonoEfectivoManager instancia = null;

    private static BonoEfectivoBean letraSeleccionada;

    private static final Logger log = Logger.getMLogger(BonoEfectivoManager.class);

    public static BonoEfectivoManager getInstance() {
        if (instancia == null) {
            instancia = new BonoEfectivoManager();
        }
        return instancia;
    }

    public List<BonoEfectivoBean> getbonoEfectivo() {
        if (bonoEfectivo != null) {
            return bonoEfectivo;
        } else {
            return new ArrayList<BonoEfectivoBean>();
        }
    }

    public void setbonoEfectivo(List<BonoEfectivoBean> abonoEfectivo) {
        bonoEfectivo = abonoEfectivo;
    }

    public void accionBuscarCedula(String text) throws BonoEfectivoCambioException {
        bonoEfectivo = BonoEfectivoServices.consultarLetrasPorCliente(text);
    }

    public BonoEfectivoBean getLetraSeleccionada() {
        return letraSeleccionada;
    }

    public void inicia() {
        BonoEfectivoManager.letraSeleccionada = null;
        BonoEfectivoManager.bonoEfectivo = new ArrayList<BonoEfectivoBean>();
    }

}
