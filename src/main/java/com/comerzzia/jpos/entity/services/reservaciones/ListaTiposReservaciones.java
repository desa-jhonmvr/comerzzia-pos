
package com.comerzzia.jpos.entity.services.reservaciones;

import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposBean;
import java.util.List;


public class ListaTiposReservaciones {

    private ReservaTiposBean tipoReservavionPorDefecto;
    private List<ReservaTiposBean> listaTiposReservaciones;

    public ListaTiposReservaciones() {
        super();
    }

    public ListaTiposReservaciones(List<ReservaTiposBean> listaTiposReservaciones, ReservaTiposBean tipoReservacionPorDefecto) {
        this.tipoReservavionPorDefecto = tipoReservacionPorDefecto;
        this.listaTiposReservaciones = listaTiposReservaciones;
    }

    public ReservaTiposBean getTipoReservacionPorDefecto() {
        return tipoReservavionPorDefecto;
    }

    public void setTipoReservacionPorDefecto(ReservaTiposBean tipoReservavionPorDefecto) {
        this.tipoReservavionPorDefecto = tipoReservavionPorDefecto;
    }

    public List<ReservaTiposBean> getListaTiposReservaciones() {
        return listaTiposReservaciones;
    }

    public void setListaTiposReservaciones(List<ReservaTiposBean> listaTiposReservaciones) {
        this.listaTiposReservaciones = listaTiposReservaciones;
    }

   
}
