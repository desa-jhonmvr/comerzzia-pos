/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones;

import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasRecibo;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import es.mpsistemas.util.fechas.Fecha;

/**
 *
 * @author Dren
 */
public class ComprobanteAbono {
    PagosTicket pagos;
    String total;
    private String totalConDescuentos;
    private String codTienda;
    private String abonosRestantes;
    private String numeroAbonos;
    private Cliente clientePagador;
    FacturacionTicketBean facturacion;
    String fechaYHora;
    String fechaYHoraFin;
    String codReserva;
    UsuarioBean cajero;
    CodigoBarrasRecibo codigoBarras;
    private String totalAbonoEntregado;
    private String tipo;
    private String observaciones;
    private Integer numAbono;
    private String nombreBoda;

    public ComprobanteAbono(Reservacion reservacion, PagosTicket pagos, String total, FacturacionTicketBean facturacion, String fechaYHora, CodigoBarrasRecibo codigoBarras) {
        this.pagos=pagos;
        this.total=total;
        this.facturacion=facturacion;
        this.fechaYHora=fechaYHora;
        this.fechaYHoraFin=reservacion.getReservacion().getFechaCaducidadAsString();
        this.codReserva=reservacion.getReservacion().getCodReservacion().toString();
        cajero=Sesion.getUsuario();
        this.codigoBarras=codigoBarras;
        tipo = "RESERVACIÓN";
        if (reservacion.getReservacion().isTipoBabyShower()){
            tipo = "BABYSHOWER";
        }
        inicializaDatos(reservacion);
    }

    public ComprobanteAbono(PlanNovioOBJ planNovio, PagosTicket pagos, String total, FacturacionTicketBean facturacion, String fechaYHora, CodigoBarrasRecibo codigoBarras) {
        this.pagos=pagos;
        this.total=total;
        this.facturacion=facturacion;
        this.fechaYHora=fechaYHora;
        Fecha f = new Fecha(planNovio.getPlan().getCaducidad());
        this.fechaYHoraFin=f.getString("dd-MMM-yyyy");
        this.codReserva=planNovio.getCodPlanAsString();
        cajero=Sesion.getUsuario();
        this.codigoBarras=codigoBarras;
        tipo = "PLAN NOVIOS";        
        inicializaDatos(planNovio);
    }

    public String getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(String fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public String getCodReserva() {
        return codReserva;
    }

    public void setCodReserva(String codReserva) {
        this.codReserva = codReserva;
    }

    public FacturacionTicketBean getFacturacion() {
        return facturacion;
    }

    public void setFacturacion(FacturacionTicketBean facturacion) {
        this.facturacion = facturacion;
    }

    public PagosTicket getPagos() {
        return pagos;
    }

    public void setPagos(PagosTicket pagos) {
        this.pagos = pagos;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public UsuarioBean getCajero() {
        return cajero;
    }

    public void setCajero(UsuarioBean cajero) {
        this.cajero = cajero;
    }

    public CodigoBarrasRecibo getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(CodigoBarrasRecibo codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getTotalAbonoEntregado() {
        return totalAbonoEntregado;
    }

    public void setTotalAbonoEntregado(String totalAbonoEntregado) {
        this.totalAbonoEntregado = totalAbonoEntregado;
    }

    public String getTotalConDescuentos() {
        return totalConDescuentos;
    }

    public void setTotalConDescuentos(String totalConDescuentos) {
        this.totalConDescuentos = totalConDescuentos;
    }

    public String getCodTienda() {
        return codTienda;
    }

    public void setCodTienda(String codTienda) {
        this.codTienda = codTienda;
    }

    public String getAbonosRestantes() {
        return abonosRestantes;
    }

    public void setAbonosRestantes(String abonosRestantes) {
        this.abonosRestantes = abonosRestantes;
    }

    public String getNumeroAbonos() {
        return numeroAbonos;
    }

    public void setNumeroAbonos(String numeroAbonos) {
        this.numeroAbonos = numeroAbonos;
    }

    public Cliente getClientePagador() {
        return clientePagador;
    }

    public void setClientePagador(Cliente clientePagador) {
        this.clientePagador = clientePagador;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFechaYHoraFin() {
        return fechaYHoraFin;
    }

    public void setFechaYHoraFin(String fechaYHoraFin) {
        this.fechaYHoraFin = fechaYHoraFin;
    }
    
    public boolean isObservacionVacia(){
        if(observaciones == null || observaciones.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
    
    public LineaEnTicket getObservacionesLineaEnTicket(){
        return new LineaEnTicket(getObservaciones(), false, true);
    }
    

    private void inicializaDatos(Reservacion reservacion) {
        String tienda = Sesion.getTienda().getAlmacen().getDesalm();
        if (tienda.length()>40){
            tienda.substring(0, 40);
        }
        this.setCodTienda(tienda);
                
        if (reservacion.getInvitadoActivo()!=null){
            this.setClientePagador(reservacion.getInvitadoActivo());
        }
        else{
            this.setClientePagador(reservacion.getReservacion().getCliente());
        }                
        this.setNumeroAbonos(String.valueOf(reservacion.getReservacion().getReservaAbonoList().size()));
        if (!reservacion.getReservacion().getReservaTipo().getAbonosMayoresATotal()){
            this.setAbonosRestantes(reservacion.getAbonosPorAbonar().toPlainString());
        }
        this.observaciones = reservacion.getObservaciones();
    }

    private void inicializaDatos(PlanNovioOBJ planNovio) {
       String tienda = Sesion.getTienda().getAlmacen().getDesalm();
        if (tienda.length()>40){
            tienda.substring(0, 40);
        }
        this.setCodTienda(tienda);
                
        if (planNovio.getInvitado()!=null){
            this.setClientePagador(planNovio.getClienteSeleccionado());
        }
        else{
            this.setClientePagador(planNovio.getClienteLogueado());
        }                
        this.setNumeroAbonos(String.valueOf(planNovio.getPlan().getListaAbonos().size()));  
        this.observaciones = planNovio.getObservaciones();
    }

    public Integer getNumAbono() {
        return numAbono;
    }

    public void setNumAbono(Integer numAbono) {
        this.numAbono = numAbono;
    }

    public String getNombreBoda() {
        return nombreBoda;
    }

    public void setNombreBoda(String nombreBoda) {
        this.nombreBoda = nombreBoda;
    }
            
    
    
}
