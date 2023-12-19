/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.mediospagos;

import com.comerzzia.util.base.MantenimientoBean;

/**
 *
 * @author Gabriel Simbania
 */
public class MarcaTarjetaBean extends MantenimientoBean {

    private String codMarcaTarjeta;
    private String desMarcaTarjeta;

    @Override
    protected void initNuevoBean() {

    }

    public MarcaTarjetaBean(String codMarcaTarjeta, String desMarcaTarjeta) {
        super();
        this.codMarcaTarjeta = codMarcaTarjeta;
        this.desMarcaTarjeta = desMarcaTarjeta;

    }

    public String getCodMarcaTarjeta() {
        return codMarcaTarjeta;
    }

    public void setCodMarcaTarjeta(String codMarcaTarjeta) {
        this.codMarcaTarjeta = codMarcaTarjeta;
    }

    public String getDesMarcaTarjeta() {
        return desMarcaTarjeta;
    }

    public void setDesMarcaTarjeta(String desMarcaTarjeta) {
        this.desMarcaTarjeta = desMarcaTarjeta;
    }

    

}
