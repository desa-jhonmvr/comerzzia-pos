/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.general.ciudades;

import com.comerzzia.jpos.entity.db.Ciudad;
import java.util.List;

/**
 *  Esta clase contiene las ciudades a mostrar en la aplicación.
 *  Contiene un metodo para la correcta representación de las ciudades en lso interfaes
 * 
 * @author MGRI
 */
public class Ciudades {

    private Ciudad ciudadPorDefecto;
    private List<Ciudad> listaCiudades;

    public Ciudades() {
        super();
    }

    public Ciudades(List<Ciudad> listaCiudades, Ciudad ciudadPorDefecto) {
        this.ciudadPorDefecto = ciudadPorDefecto;
        this.listaCiudades = listaCiudades;
    }

    public Ciudad getCiudadPorDefecto() {
        return ciudadPorDefecto;
    }

    public void setCiudadPorDefecto(Ciudad ciudadPorDefecto) {
        this.ciudadPorDefecto = ciudadPorDefecto;
    }

    public List<Ciudad> getListaCiudades() {
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudad> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public void estableceCiudadPorDefecto(String poblacion) {
        Ciudad cDefectBuscar = new Ciudad(poblacion);
        int indice = this.listaCiudades.indexOf(cDefectBuscar);
        if (indice >= 0) {
            this.ciudadPorDefecto = listaCiudades.get(indice);
        }
    }
    /**
     *  Método que contiene la lógica de renderizado de la entidad en un componente lista
     * @return 
     */
}
