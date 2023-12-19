package com.comerzzia.jpos.persistencia.reservaciones.reservaabono;

import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoBean;
import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;

public class ReservaAbonoBean extends ReservaAbonoKey {
    private BigDecimal cantidadAbono;

    private Long idInvitado;

    private Boolean procesado;

    private String codcaja;

    private Fecha fechaAbono;

    private Boolean procesadoTienda;

    private BigDecimal cantAbonoSinDto;

    private String tipoAbono;

    private String cajero;

    private Boolean anulado;

    private byte[] pagos;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    private ReservaBean reserva;
    
    private ReservaInvitadoBean reservaInvitado;
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------

    public ReservaAbonoBean() {
        procesado=false;
    }

    public ReservaAbonoBean(ReservaAbonoKey reservaAbonoKey) {
        setUidReservacion(reservaAbonoKey.getUidReservacion());
        setIdAbono(reservaAbonoKey.getIdAbono());
        procesado=false;
        this.reserva = new ReservaBean();
        this.reservaInvitado = new ReservaInvitadoBean();
    }

    public ReservaAbonoBean(ReservaAbonoKey reservaAbonoKey, BigDecimal cantidadAbono) {
        setUidReservacion(reservaAbonoKey.getUidReservacion());
        setIdAbono(reservaAbonoKey.getIdAbono());
        this.cantidadAbono = cantidadAbono;
        procesado=false;
        this.reserva = new ReservaBean();
        this.reservaInvitado = new ReservaInvitadoBean();
    }

    public ReservaAbonoBean(String uidReservacion, Long idAbono) {
        setUidReservacion(uidReservacion);
        setIdAbono(idAbono);
        procesado=false;
        this.reserva = new ReservaBean();
        this.reservaInvitado = new ReservaInvitadoBean();
    }    

    public BigDecimal getCantidadAbono() {
        return cantidadAbono;
    }

    public void setCantidadAbono(BigDecimal cantidadAbono) {
        this.cantidadAbono = cantidadAbono;
    }

    public Long getIdInvitado() {
        return idInvitado;
    }

    public void setIdInvitado(Long idInvitado) {
        this.idInvitado = idInvitado;
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja == null ? null : codcaja.trim();
    }

    public Fecha getFechaAbono() {
        return fechaAbono;
    }

    public void setFechaAbono(Fecha fechaAbono) {
        this.fechaAbono = fechaAbono;
    }

    public Boolean getProcesadoTienda() {
        return procesadoTienda;
    }

    public void setProcesadoTienda(Boolean procesadoTienda) {
        this.procesadoTienda = procesadoTienda;
    }

    public BigDecimal getCantAbonoSinDto() {
        return cantAbonoSinDto;
    }

    public void setCantAbonoSinDto(BigDecimal cantAbonoSinDto) {
        this.cantAbonoSinDto = cantAbonoSinDto;
    }

    public String getTipoAbono() {
        return tipoAbono;
    }

    public void setTipoAbono(String tipoAbono) {
        this.tipoAbono = tipoAbono == null ? null : tipoAbono.trim();
    }

    public String getCajero() {
        return cajero;
    }

    public void setCajero(String cajero) {
        this.cajero = cajero == null ? null : cajero.trim();
    }

    public Boolean getAnulado() {
        return anulado;
    }

    public void setAnulado(Boolean anulado) {
        this.anulado = anulado;
    }

    public byte[] getPagos() {
        return pagos;
    }

    public void setPagos(byte[] pagos) {
        this.pagos = pagos;
    }
    
    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------

    @Override
    public int hashCode() {
        int hash = 0;
        hash += ((getUidReservacion() != null && getIdAbono() != null) ? super.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReservaAbonoBean other = (ReservaAbonoBean) obj;
        return (this.getUidReservacion().equals(other.getUidReservacion()) && this.getIdAbono().equals(other.getIdAbono()));
    }


    @Override
    public String toString() {
        return "entity.db.ReservaAbonoBean[ reservaAbonoKey=" + super.toString() + " ]";
    }
    
    public ReservaBean getReserva() {
        return reserva;
    }

    public void setReserva(ReservaBean reserva) {
        this.reserva = reserva;
    }    
    
    public ReservaInvitadoBean getReservaInvitado() {
        return reservaInvitado;
    }

    public void setReservaInvitado(ReservaInvitadoBean reservaInvitado) {
        this.reservaInvitado = reservaInvitado;
    }    
    
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------

}