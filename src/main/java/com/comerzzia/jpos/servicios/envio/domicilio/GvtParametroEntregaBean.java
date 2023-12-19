/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.envio.domicilio;

import com.comerzzia.jpos.servicios.login.Sesion;
import java.util.List;
import javax.swing.JComboBox;

/**
 *
 * @author Mónica Enríquez
 */
public class GvtParametroEntregaBean {

    private Long penNumeroCamion;
    private Long penCantidad;
    private Long penDomingo;
    private Long penLunes;
    private Long penMartes;
    private Long penMiercoles;
    private Long penJueves;
    private Long penViernes;
    private Long penSabado;
    private Long penId;

    public GvtParametroEntregaBean() {
    }

    public Long getPenCantidad() {
        return penCantidad;
    }

    public void setPenCantidad(Long penCantidad) {
        this.penCantidad = penCantidad;
    }

    public Long getPenDomingo() {
        return penDomingo;
    }

    public void setPenDomingo(Long penDomingo) {
        this.penDomingo = penDomingo;
    }

    public Long getPenLunes() {
        return penLunes;
    }

    public void setPenLunes(Long penLunes) {
        this.penLunes = penLunes;
    }

    public Long getPenMartes() {
        return penMartes;
    }

    public void setPenMartes(Long penMartes) {
        this.penMartes = penMartes;
    }

    public Long getPenMiercoles() {
        return penMiercoles;
    }

    public void setPenMiercoles(Long penMiercoles) {
        this.penMiercoles = penMiercoles;
    }

    public Long getPenJueves() {
        return penJueves;
    }

    public void setPenJueves(Long penJueves) {
        this.penJueves = penJueves;
    }

    public Long getPenViernes() {
        return penViernes;
    }

    public void setPenViernes(Long penViernes) {
        this.penViernes = penViernes;
    }

    public Long getPenSabado() {
        return penSabado;
    }

    public void setPenSabado(Long penSabado) {
        this.penSabado = penSabado;
    }

    public Long getPenNumeroCamion() {
        return penNumeroCamion;
    }

    public void setPenNumeroCamion(Long penNumeroCamion) {
        this.penNumeroCamion = penNumeroCamion;
    }

    public Long getPenId() {
        return penId;
    }

    public void setPenId(Long penId) {
        this.penId = penId;
    }

    public void llenar_combo(JComboBox<Long> combo_parametro) {
        try {
            List<GvtParametroEntregaBean> parametro = ServicioEnvioDomicilio.getParametrosEntrega(Sesion.getTienda().getCodalm(), null);
            combo_parametro.removeAllItems();
            for (GvtParametroEntregaBean p : parametro) {
                combo_parametro.addItem(p.getPenNumeroCamion());
            }
        } catch (Exception e) {
        }
    }

}
