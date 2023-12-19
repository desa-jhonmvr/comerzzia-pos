package com.comerzzia.jpos.persistencia.reservaciones.reservatipos;

import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class ReservaTiposBean {
    private String codTipo;

    private String desTipo;

    private Boolean abonoInicial;

    private BigDecimal porcentajeAbonoInicial;

    private Boolean articuloReservado;

    private Boolean listaInvitados;

    private Boolean permiteCompra;

    private Boolean liquidacion;

    private Boolean permiteReservarPromocionados;

    private Boolean abonosMayoresATotal;

    private BigDecimal plazoReservacion;

    private Boolean permiteEliminarArticulos;

    private Boolean permiteLiquidacionParcial;

    private Boolean permiteAbonosPropietario;

    private Boolean permiteAbonosInvitados;

    private Boolean permiteAbonosParciales;

    private Boolean permiteAltaPos;

    private Boolean permiteDatosAdicionales;

    private Boolean permiteFacturarInvitado;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    private List<ReservaBean> reservaList; 
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------

    public ReservaTiposBean() {
        this.reservaList = new LinkedList<ReservaBean>();
    }

    public ReservaTiposBean(String codTipo) {
        this.codTipo = codTipo;
        this.reservaList = new LinkedList<ReservaBean>();
    }

    public ReservaTiposBean(String codTipo, String desTipo, boolean abonoInicial, boolean articuloReservado, boolean listaInvitados, boolean permiteCompra, boolean liquidacion, boolean permiteReservarPromocionados, boolean abonosMayoresATotal, boolean permiteEliminarArticulos, boolean permiteLiquidacionParcial) {
        this.codTipo = codTipo;
        this.desTipo = desTipo;
        this.abonoInicial = abonoInicial;
        this.articuloReservado = articuloReservado;
        this.listaInvitados = listaInvitados;
        this.permiteCompra = permiteCompra;
        this.liquidacion = liquidacion;
        this.permiteReservarPromocionados = permiteReservarPromocionados;
        this.abonosMayoresATotal = abonosMayoresATotal;
        this.permiteEliminarArticulos = permiteEliminarArticulos;
        this.permiteLiquidacionParcial = permiteLiquidacionParcial;
        this.reservaList = new LinkedList<ReservaBean>();
    }
    
    public String getCodTipo() {
        return codTipo;
    }

    public void setCodTipo(String codTipo) {
        this.codTipo = codTipo == null ? null : codTipo.trim();
    }

    public String getDesTipo() {
        return desTipo;
    }

    public void setDesTipo(String desTipo) {
        this.desTipo = desTipo == null ? null : desTipo.trim();
    }

    public Boolean getAbonoInicial() {
        return abonoInicial;
    }

    public void setAbonoInicial(Boolean abonoInicial) {
        this.abonoInicial = abonoInicial;
    }

    public BigDecimal getPorcentajeAbonoInicial() {
        return porcentajeAbonoInicial;
    }

    public void setPorcentajeAbonoInicial(BigDecimal porcentajeAbonoInicial) {
        this.porcentajeAbonoInicial = porcentajeAbonoInicial;
    }

    public Boolean getArticuloReservado() {
        return articuloReservado;
    }

    public void setArticuloReservado(Boolean articuloReservado) {
        this.articuloReservado = articuloReservado;
    }

    public Boolean getListaInvitados() {
        return listaInvitados;
    }

    public void setListaInvitados(Boolean listaInvitados) {
        this.listaInvitados = listaInvitados;
    }

    public Boolean getPermiteCompra() {
        return permiteCompra;
    }

    public void setPermiteCompra(Boolean permiteCompra) {
        this.permiteCompra = permiteCompra;
    }

    public Boolean getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(Boolean liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Boolean getPermiteReservarPromocionados() {
        return permiteReservarPromocionados;
    }

    public void setPermiteReservarPromocionados(Boolean permiteReservarPromocionados) {
        this.permiteReservarPromocionados = permiteReservarPromocionados;
    }

    public Boolean getAbonosMayoresATotal() {
        return abonosMayoresATotal;
    }

    public void setAbonosMayoresATotal(Boolean abonosMayoresATotal) {
        this.abonosMayoresATotal = abonosMayoresATotal;
    }

    public BigDecimal getPlazoReservacion() {
        return plazoReservacion;
    }

    public void setPlazoReservacion(BigDecimal plazoReservacion) {
        this.plazoReservacion = plazoReservacion;
    }

    public Boolean getPermiteEliminarArticulos() {
        return permiteEliminarArticulos;
    }

    public void setPermiteEliminarArticulos(Boolean permiteEliminarArticulos) {
        this.permiteEliminarArticulos = permiteEliminarArticulos;
    }

    public Boolean getPermiteLiquidacionParcial() {
        return permiteLiquidacionParcial;
    }

    public void setPermiteLiquidacionParcial(Boolean permiteLiquidacionParcial) {
        this.permiteLiquidacionParcial = permiteLiquidacionParcial;
    }

    public Boolean getPermiteAbonosPropietario() {
        return permiteAbonosPropietario;
    }

    public void setPermiteAbonosPropietario(Boolean permiteAbonosPropietario) {
        this.permiteAbonosPropietario = permiteAbonosPropietario;
    }

    public Boolean getPermiteAbonosInvitados() {
        return permiteAbonosInvitados;
    }

    public void setPermiteAbonosInvitados(Boolean permiteAbonosInvitados) {
        this.permiteAbonosInvitados = permiteAbonosInvitados;
    }

    public Boolean getPermiteAbonosParciales() {
        return permiteAbonosParciales;
    }

    public void setPermiteAbonosParciales(Boolean permiteAbonosParciales) {
        this.permiteAbonosParciales = permiteAbonosParciales;
    }

    public Boolean getPermiteAltaPos() {
        return permiteAltaPos;
    }

    public void setPermiteAltaPos(Boolean permiteAltaPos) {
        this.permiteAltaPos = permiteAltaPos;
    }

    public Boolean getPermiteDatosAdicionales() {
        return permiteDatosAdicionales;
    }

    public void setPermiteDatosAdicionales(Boolean permiteDatosAdicionales) {
        this.permiteDatosAdicionales = permiteDatosAdicionales;
    }

    public Boolean getPermiteFacturarInvitado() {
        return permiteFacturarInvitado;
    }

    public void setPermiteFacturarInvitado(Boolean permiteFacturarInvitado) {
        this.permiteFacturarInvitado = permiteFacturarInvitado;
    }

    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    public List<ReservaBean> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<ReservaBean> reservaList) {
        this.reservaList = reservaList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codTipo != null ? codTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ReservaTiposBean)) {
            return false;
        }
        ReservaTiposBean other = (ReservaTiposBean) object;
        if ((this.codTipo == null && other.codTipo != null) || (this.codTipo != null && !this.codTipo.equals(other.codTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.ReservaTiposBean[ codTipo=" + codTipo + " ]";
    }    
    
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------    

}