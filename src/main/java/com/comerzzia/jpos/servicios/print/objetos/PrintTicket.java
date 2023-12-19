/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author MGRI
 */
public class PrintTicket extends PrintDocument{
    
    
    private static Logger log = Logger.getMLogger(PrintTicket.class);
     
    private TicketS ticket;
    // VARIABLES PARA RELLENAR EL TICKET
     // - CABECERA            
            private String nombreEmpresa ;            
            private String nombreTienda ; // Enviado por el módulo SRI           
            private String direccionEmpresa ; // Enviado por el módulo SRI
            private String direccionLocal ; // Enviado por el módulo SRI
            private String ruc ; // Enviado por el módulo SRI
            private String porcentajeIvaEmpresa; //Enviado por el módulo SRI
            // FACTURA
            private String factura ;  // como factura ponemos el id de factura
            private String lugarYfecha ;// Region del local y fecha
            private String fecha ; // fecha con formato dd-MM-aaaa
            private String hora ;    
            private String horaContinua ; // Hora sin los dos puntos 
            // De cliente o datos de facturación
            private String nombre ; // MULTILINEA            
            private String cedRuc ;                        
            private String direccion ;
            private String telefono ;            
            private String tarjetaDescuento;                
            private String nombreCajero;
            private String nombreAutorizador; 
            private String horaFactura;            
            private boolean hayPendienteEntrega;            
            private String textoResolucion;
            private String autorizacionSRI;
            private String fechaInicioValidez;
            private String fechaFinValidez;
            private String desAutorizador;
            private String nroResolucionContribuyente;
              
    /**
      * Constructor. 
      * @param ticket 
      */
    public PrintTicket(TicketS ticket){
        try{
        this.ticket = ticket;    
        this.nombreEmpresa = Sesion.getEmpresa().getNombreComercial();            
        this.nombreTienda = Sesion.getTienda().getSriTienda().getDesalm(); // Enviado por el módulo SRI           
        this.direccionEmpresa = Sesion.getEmpresa().getDomicilio(); // Enviado por el módulo SRI
        this.direccionLocal = Sesion.getTienda().getSriTienda().getDomicilio(); // Enviado por el módulo SRI
        this.ruc = Sesion.getEmpresa().getCif() ; // Enviado por el módulo SRI
        this.porcentajeIvaEmpresa = Sesion.getEmpresa().getPorcentajeIva().toString().trim();
            
            // FACTURA
        this.factura = ticket.getIdFactura();  // como factura ponemos el id de factura
        this.lugarYfecha = Sesion.getTienda().getCodRegion().getDesregion() +", " +ticket.getFecha().getString("EEEEE dd 'de' MMMMM 'del' yyyy");
        
        this.hora = ticket.getFecha().getString("HH:mm");
        this.horaContinua = ticket.getFecha().getString("HHmm");        
        this.fecha = ticket.getFecha().getString("dd-MMM-yyyy").toUpperCase();
        
            // De cliente o datos de facturación
        this.nombre = ticket.getFacturacion().getNombreImpresion() + " " + ticket.getFacturacion().getApellidosImpresion(); // MULTILINEA
            
        this.cedRuc = "";
        if (ticket.getFacturacion().getDocumento() != null){
                    cedRuc = ticket.getFacturacion().getDocumento();
        }
        if (ticket.getFacturacion()!=null && ticket.getFacturacion().getDireccion() !=null){
                direccion = ticket.getFacturacion().getDireccion();
        }
        else {
                direccion = ".............";
        }        
        if (ticket.getFacturacion()!=null  && ticket.getFacturacion().getTelefono()!= null){
            telefono = ticket.getFacturacion().getTelefono();
        }
        else if (ticket.getFacturacion()!=null && ticket.getCliente().getTelefonoMovil()!=null){
            telefono = ticket.getCliente().getTelefonoMovil();
        }
        else{
            telefono = "......";
        }
        
        if (ticket.getCliente().isAplicaTarjetaAfiliado()){
            this.tarjetaDescuento = ofuscarTarjeta(ticket.getCliente().getTarjetaAfiliacion().getNumero());
        }

        this.nombreCajero = ticket.getCajero().getDesUsuario();    
        this.horaFactura = ticket.getFecha().getString("hhmm");            
        this.hayPendienteEntrega = ticket.hayEnvioADomicilio(); 
        if(ticket.getAutorizadorVenta()!= null){
            this.desAutorizador = ticket.getAutorizadorVenta().getDesUsuario();
        }
        this.nroResolucionContribuyente = Sesion.getEmpresa().getNroResolucionContribuyente();
        
        try{
            this.autorizacionSRI = Sesion.getEmpresa().getNumAutorizacion().toString();
            Fecha fechaInicioValidezF = new Fecha(Sesion.getEmpresa().getFechaInicioAuorizacion());
            Fecha fechaFinValidezF = new Fecha(Sesion.getEmpresa().getFechafinAutorizacion());
                    
            this.fechaInicioValidez = fechaInicioValidezF.getString("dd 'de' MMMMM 'del' yyyy"); // Poner formato correcto
            this.fechaFinValidez = fechaFinValidezF.getString("dd 'de' MMMMM 'del' yyyy");  // Poner formato correcto     
        }
        catch(NullPointerException e){
            log.error("La Empresa, no tiene un número de autorización asignado o sus fechas de validez no están establecidas");
        }
        }
        catch(Exception e){
            log.error("Error estableciendo valores del ticket.. : "+e.getMessage(),e);
        }
    }

    public TicketS getTicket() {
        return ticket;
    }

    public void setTicket(TicketS ticket) {
        this.ticket = ticket;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getDireccionEmpresa() {
        return direccionEmpresa;
    }
    
    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }

    public String getDireccionLocal() {
        return direccionLocal;
    }
    
    public void setDireccionLocal(String direccionLocal) {
        this.direccionLocal = direccionLocal;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getLugarYfecha() {
        return lugarYfecha;
    }

    public void setLugarYfecha(String lugarYfecha) {
        this.lugarYfecha = lugarYfecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedRuc() {
        return cedRuc;
    }

    public void setCedRuc(String cedRuc) {
        this.cedRuc = cedRuc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTarjetaDescuento() {
        return tarjetaDescuento;
    }

    public void setTarjetaDescuento(String tarjetaDescuento) {
        this.tarjetaDescuento = tarjetaDescuento;
    }

    public String getNombreCajero() {
        return nombreCajero;
    }

    public void setNombreCajero(String nombreCajero) {
        this.nombreCajero = nombreCajero;
    }

    public String getNombreAutorizador() {
        return nombreAutorizador;
    }

    public void setNombreAutorizador(String nombreAutorizador) {
        this.nombreAutorizador = nombreAutorizador;
    }

    public String getHoraFactura() {
        return horaFactura;
    }

    public void setHoraFactura(String horaFactura) {
        this.horaFactura = horaFactura;
    }

    public boolean isHayPendienteEntrega() {
        return hayPendienteEntrega;
    }

    public void setHayPendienteEntrega(boolean hayPendienteEntrega) {
        this.hayPendienteEntrega = hayPendienteEntrega;
    }

    public String getTextoResolucion() {
        return textoResolucion;
    }

    public void setTextoResolucion(String textoResolucion) {
        this.textoResolucion = textoResolucion;
    }

    public String getAutorizacionSRI() {
        return autorizacionSRI;
    }

    public void setAutorizacionSRI(String autorizacionSRI) {
        this.autorizacionSRI = autorizacionSRI;
    }

    public String getFechaInicioValidez() {
        return fechaInicioValidez;
    }

    public void setFechaInicioValidez(String fechaInicioValidez) {
        this.fechaInicioValidez = fechaInicioValidez;
    }

    public String getFechaFinValidez() {
        return fechaFinValidez;
    }

    public void setFechaFinValidez(String fechaFinValidez) {
        this.fechaFinValidez = fechaFinValidez;
    }
    
    
    // Métodos de Líneas
    public LineaEnTicket getDireccionEmpresaAsLineas() {
       return  new LineaEnTicket("MATRIZ : "+ this.direccionEmpresa);
    }
        
    public LineaEnTicket getDireccionLocalAsLineas() {
       return  new LineaEnTicket("SUCURSAL : "+this.direccionLocal);
    }
    
    public LineaEnTicket getLugarYfechaAsLineas() {
        return  new LineaEnTicket("FECHA : "+lugarYfecha);
    }
    
    public LineaEnTicket getLugarYfechaAsLineasSinFecha() {
       return  new LineaEnTicket(lugarYfecha); 
    }
    
    public LineaEnTicket getNombreAsLineasSinNombre() {
        return new LineaEnTicket(nombre);
    }
    
    public LineaEnTicket getNombreAsLineas() {
        return  new LineaEnTicket("Nombre : "+nombre);
    }
    
    public LineaEnTicket getDireccionAsLineas() {
        return  new LineaEnTicket("Direccion : "+direccion);
    }
    
    public LineaEnTicket getNombreCajeroAsLineas() {
        return  new LineaEnTicket("Elaborado por : "+nombreCajero);
    }

    public String getHoraContinua() {
       return this.horaContinua;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHoraContinua(String horaContinua) {
        this.horaContinua = horaContinua;
    }

    public String getDesAutorizador() {
        return desAutorizador;
    }

    public void setDesAutorizador(String autorizador) {
        this.desAutorizador = autorizador;
    }
    
    public LineaEnTicket getDesAutorizadorAsLineas() {
        if (desAutorizador == null){
            return new LineaEnTicket();
        }
        else{
            return  new LineaEnTicket("Autorizado por : "+desAutorizador);
        }
    }

    public String getNroResolucionContribuyente() {
        return nroResolucionContribuyente;
    }

    public void setNroResolucionContribuyente(String nroResolucionContribuyente) {
        this.nroResolucionContribuyente = nroResolucionContribuyente;
    }
        
    public boolean hayDatosFacturacion(){
        // La dirección es obligatoria y no la tiene un cliente generico
        return (ticket.getFacturacion()!=null && ticket.getFacturacion().getDireccion()!=null &&!ticket.getFacturacion().getDireccion().isEmpty());
    }
    
    public String getPorcentajeIvaEmpresa() {
        return porcentajeIvaEmpresa;
    }

    public void setPorcentajeIvaEmpresa(String porcentajeIvaEmpresa) {
        this.porcentajeIvaEmpresa = porcentajeIvaEmpresa;
    }
    
}
