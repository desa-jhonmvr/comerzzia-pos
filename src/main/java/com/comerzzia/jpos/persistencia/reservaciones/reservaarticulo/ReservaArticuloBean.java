package com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo;

import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ReservaArticuloBean extends ReservaArticuloKey {
    private String codart;

    private String desart;

    private Long cantidad;

    private BigDecimal precio;

    private BigDecimal precioTotal;

    private Boolean comprado;

    private String codbarras;

    private Long idInvitado;

    private Boolean compradoConAbono;

    private Boolean procesado;
   
    private Fecha fechaCompra;

    private Boolean procesadoTienda;

    private BigDecimal precioTotalSinDto;

    private BigDecimal precioTotalConDto;
    
    private String uidTicket;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    private String codMarca;

    private Integer idItem;

    private BigDecimal descuento;    
    
    private ReservaBean reserva;
    
    private ReservaInvitadoBean invitadoPagador;
    
    //propiedad no persistente
    private String entregado;
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------

    public ReservaArticuloBean() {
        procesado = false;
        this.reserva = new ReservaBean();
        this.invitadoPagador = new ReservaInvitadoBean();
    }

    public ReservaArticuloBean(ReservaArticuloKey reservaArticuloKey) {
        setUidReservacion(reservaArticuloKey.getUidReservacion());
        setIdLinea(reservaArticuloKey.getIdLinea());
        procesado = false;
        this.reserva = new ReservaBean();
        this.invitadoPagador = new ReservaInvitadoBean();
    }

    public ReservaArticuloBean(String uidReservacion, Long idLinea) {
        setUidReservacion(uidReservacion);
        setIdLinea(idLinea);
        procesado = false;
        this.reserva = new ReservaBean();
        this.invitadoPagador = new ReservaInvitadoBean();
    }    

    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart == null ? null : codart.trim();
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart == null ? null : desart.trim();
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        if (descuento != null && Numero.isMayorACero(descuento)){
            return  Numero.menosPorcentajeR(precio, descuento);
        }
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecioTotal() {
        if (descuento != null && Numero.isMayorACero(descuento)){
            return  Numero.menosPorcentajeR(precioTotal, descuento);
        }
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Boolean getComprado() {
        return comprado;
    }

    public void setComprado(Boolean comprado) {
        this.comprado = comprado;
    }

    public String getCodbarras() {
        return codbarras;
    }

    public void setCodbarras(String codbarras) {
        this.codbarras = codbarras == null ? null : codbarras.trim();
    }

    public Long getIdInvitado() {
        return idInvitado;
    }

    public void setIdInvitado(Long idInvitado) {
        this.idInvitado = idInvitado;
    }

    public Boolean getCompradoConAbono() {
        return compradoConAbono;
    }

    public void setCompradoConAbono(Boolean compradoConAbono) {
        this.compradoConAbono = compradoConAbono;
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    public Fecha getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Fecha fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Boolean getProcesadoTienda() {
        return procesadoTienda;
    }

    public void setProcesadoTienda(Boolean procesadoTienda) {
        this.procesadoTienda = procesadoTienda;
    }

    public BigDecimal getPrecioTotalSinDto() {
        return precioTotalSinDto;
    }

    public void setPrecioTotalSinDto(BigDecimal precioTotalSinDto) {
        this.precioTotalSinDto = precioTotalSinDto;
    }

    public BigDecimal getPrecioTotalConDto() {
        return precioTotalConDto;
    }

    public void setPrecioTotalConDto(BigDecimal precioTotalConDto) {
        this.precioTotalConDto = precioTotalConDto;
    }
    
    public String getUidTicket() {
        return uidTicket;
    }
    
    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }
    
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------

    public String getCodMarca() {
        if (codMarca == null) {
            generaCampos();
        }
        return codMarca;
    }

    public void setCodMarca(String codMarca) {
        this.codMarca = codMarca;
    }

    public Integer getIdItem() {
        if (idItem == null) {
            generaCampos();
        }
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    
    private void generaCampos() {
        if (Sesion.isSukasa()){
            String[] split = this.codart.split(Pattern.quote("."));
            this.codMarca = split[0];
            this.idItem = new Integer(split[1]);
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += ((getUidReservacion() != null && getIdLinea() != null) ? super.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof ReservaArticuloBean)) {
            return false;
        }
        ReservaArticuloBean other = (ReservaArticuloBean) object;

        return (getUidReservacion().equals(other.getUidReservacion()) && getIdLinea().equals(other.getIdLinea()));
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.ReservaArticulo[ reservaArticuloPK=" + super.toString() + " ]";
    }    

    public ReservaBean getReserva() {
        return reserva;
    }

    public void setReserva(ReservaBean reserva) {
        this.reserva = reserva;
    }    
    
    public ReservaInvitadoBean getInvitadoPagador() {
        return invitadoPagador;
    }

    public void setInvitadoPagador(ReservaInvitadoBean invitadoPagador) {
        this.invitadoPagador = invitadoPagador;
    }
    
    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------

    public String getEntregado() {
        return entregado;
    }

    public void setEntregado(String entregado) {
        this.entregado = entregado;
    }

}