/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.letras;

import com.comerzzia.jpos.persistencia.letras.LetraBean;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.numeros.Numero;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class LetraCambioManager {

    private static List<LetraBean> listaFacturas;
    private static LetraCambioManager instancia = null;

    private static LetraBean letraSeleccionada;

    private static final Logger log = Logger.getMLogger(LetraCambioManager.class);

    public static LetraCambioManager getInstance() {
        if (instancia == null) {
            instancia = new LetraCambioManager();
        }
        return instancia;
    }

    public List<LetraBean> getListaFacturas() {
        if (listaFacturas != null) {
            return listaFacturas;
        } else {
            return new ArrayList<LetraBean>();
        }
    }

    public void setListaFacturas(List<LetraBean> aListaFacturas) {
        listaFacturas = aListaFacturas;
    }

    public void accionBuscarCedula(String text) throws LetraCambioException {
        listaFacturas = LetraCambiosServices.consultarLetrasPorCliente(text);
    }

    public void accionBuscarFactura(String text, String codCaja) throws LetraCambioException {
//        if (text == null || text.isEmpty()){
//            throw new LetraCambioException("Debe indicar un número de factura emitida en esta caja.");
//        }
        String codAlmacen = VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN);
        long idTicket = Numero.getLong(text, 0L);
        if (idTicket == 0L) {

            listaFacturas = LetraCambiosServices.consultarLetrasPorCaja(codAlmacen, codCaja);
            //throw new LetraCambioException("Debe indicar un número de factura correcto.");
        } else {
            listaFacturas = LetraCambiosServices.consultarLetrasPorFactura(codAlmacen, codCaja, idTicket);
        }
    }
    
    public void accionBuscarLetras() throws LetraCambioException {
            listaFacturas = LetraCambiosServices.consultarLetras();
    }

    public void accionSeleccionaFactura(int selectedColumn) throws LetraCambioException {
        letraSeleccionada = listaFacturas.get(selectedColumn);
        letraSeleccionada = LetraCambiosServices.consultarLetra(letraSeleccionada.getUidLetra());
        Sesion.setLetraBean(letraSeleccionada);
    }

    public LetraBean getLetraSeleccionada() {
        return letraSeleccionada;
    }

    public void inicia() {
        LetraCambioManager.letraSeleccionada = null;
        LetraCambioManager.listaFacturas = new ArrayList<LetraBean>();
    }

}
