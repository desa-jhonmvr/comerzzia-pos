/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.general.operadoresmoviles;

import com.comerzzia.jpos.entity.db.OperadorMovil;
import java.util.LinkedList;
import java.util.List;

/**
 *  Esta clase contiene las ciudades a mostrar en la aplicación.
 *  Contiene un metodo para la correcta representación de las ciudades en lso interfaes
 * 
 * @author MGRI
 */
public class OperadoresMoviles {

    private OperadorMovil  operadorPorDefecto;
    private List<OperadorMovil > listaOperadorMovil;

    public OperadoresMoviles () {
        super();
    }

    public OperadoresMoviles (List<OperadorMovil> listaCiudades, OperadorMovil ciudadPorDefecto) {
        this.listaOperadorMovil= new LinkedList();
        
        this.operadorPorDefecto = ciudadPorDefecto;        
        this.listaOperadorMovil.addAll(listaCiudades);
        ((LinkedList)listaOperadorMovil).addFirst(new OperadorMovil(""));
        
    }

    public OperadorMovil getOperadorMovilPorDefecto() {
        return operadorPorDefecto;
    }

    public void setOperadorMovilPorDefecto(OperadorMovil OperadorMovilPorDefecto) {
        this.operadorPorDefecto = OperadorMovilPorDefecto;
    }

    public List<OperadorMovil> getListaOperadorMovil() {
        return listaOperadorMovil;
    }

    public void setListaOperadorMovil(List<OperadorMovil> listaOperadorMovil) {
        this.listaOperadorMovil = listaOperadorMovil;
    }

    /**
     *  Método que contiene la lógica de renderizado de la entidad en un componente lista
     * @return 
     */  
}