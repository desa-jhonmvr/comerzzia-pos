/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.numeros.NumeroALetra;
import es.mpsistemas.util.fechas.Fecha;
import org.apache.log4j.Logger;

/**
 *
 * @author SMLM
 */
public class PrintCheque extends PrintDocument{
    
    private static Logger log = Logger.getLogger(PrintCheque.class);
    
    private TicketS ticket;
    private Pago pago;
    private String nombreEmpresa;
    
    // Valores del anverso del cheque
    private String valorChequeNumero;
    private String valorChequeLetra;
    private String lugarYfecha ;// Region del local y fecha
    
    // Valores del reverso del cheque
    private String nombreYApellidos ;         
    private String cedRuc ;                        
    private String telefono ; 
    private String provincia;
    private String codigoCajero;
    private String codigoLocal;
    private String nombreTienda;
    private String numeroCuenta;

    public PrintCheque(TicketS ticket, Pago pago){
        try{
            this.ticket = ticket;
            this.pago = pago;
            this.valorChequeNumero=String.valueOf(pago.getUstedPaga());
            this.valorChequeLetra=NumeroALetra.convertir(String.valueOf(pago.getUstedPaga()), true);
            Fecha fecha = ticket.getFecha();
            if(fecha==null) {
                fecha = new Fecha();
            }
            this.lugarYfecha = Sesion.getTienda().getCodRegion().getDesregion() +", " + fecha.getString("yyyy'-'MM'-'dd");
            // De cliente o datos de facturación
            if(ticket.getFacturacion() == null){
                ticket.setFacturacionCliente(ticket.getCliente());
            }
            this.nombreYApellidos = ticket.getFacturacion().getNombreImpresion() + " " + ticket.getFacturacion().getApellidosImpresion(); // MULTILINEA  
            this.cedRuc = "";
            if (ticket.getFacturacion().getDocumento() != null){
                this.cedRuc = ticket.getFacturacion().getDocumento();
            }
            if (ticket.getFacturacion()!=null  && ticket.getFacturacion().getTelefono()!= null){
                this.telefono = ticket.getFacturacion().getTelefono();
            }
            else if (ticket.getFacturacion()!=null && ticket.getCliente().getTelefonoMovil()!=null){
                this.telefono = ticket.getCliente().getTelefonoMovil();
            }
            else{
                this.telefono = "......";
            } 
            if(Sesion.getTienda().getAlmacen().getProvincia() != null){
                this.provincia = Sesion.getTienda().getAlmacen().getProvincia() ;
            }
            else{
                this.provincia = "......";
            }
            
            if(ticket.getCajero() == null){
                ticket.setCajero(Sesion.getUsuario());
            }
            this.codigoCajero = ticket.getCajero().getUsuario().toString();
            this.codigoLocal = Sesion.getTienda().getCodalm();
            this.nombreEmpresa = Sesion.getEmpresa().getNombreComercial();            
            this.nombreTienda = Sesion.getTienda().getSriTienda().getDesalm(); // Enviado por el módulo SRI  
            this.numeroCuenta = Sesion.getEmpresa().getRegistroMercantil();
        } catch (Exception e) {
            log.error("Error estableciendo valores del cheque.. : "+e.getMessage(),e);
        }
    }
    
    public TicketS getTicket() {
        return ticket;
    }

    public void setTicket(TicketS ticket) {
        this.ticket = ticket;
    }

    public String getValorChequeNumero() {
        return valorChequeNumero;
    }

    public void setValorChequeNumero(String valorChequeNumero) {
        this.valorChequeNumero = valorChequeNumero;
    }

    public String getValorChequeLetra() {
        return valorChequeLetra;
    }

    public void setValorChequeLetra(String valorChequeLetra) {
        this.valorChequeLetra = valorChequeLetra;
    }

    public String getNombreYApellidos() {
        return nombreYApellidos;
    }

    public void setNombreYApellidos(String nombreYApellidos) {
        this.nombreYApellidos = nombreYApellidos;
    }

    public String getCedRuc() {
        return cedRuc;
    }

    public void setCedRuc(String cedRuc) {
        this.cedRuc = cedRuc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCodigoCajero() {
        return codigoCajero;
    }

    public void setCodigoCajero(String codigoCajero) {
        this.codigoCajero = codigoCajero;
    }

    public String getCodigoLocal() {
        return codigoLocal;
    }

    public void setCodigoLocal(String codigoLocal) {
        this.codigoLocal = codigoLocal;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }
    
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }
    
    public String getLugarYfecha() {
        return lugarYfecha;
    }

    public void setLugarYfecha(String lugarYfecha) {
        this.lugarYfecha = lugarYfecha;
    }
 
    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }
    
    public String getValorChequeLetrasAsImpresion(){
        String s = getValorChequeLetra()+"  XXXXXXXXXX";
        return s;
    }
    
    public String getNombreEmpresayValorCheque(){
        String s=getNombreEmpresa();
        for(int i=s.length();i<=68;i++){
            s+=' ';
        }
        s+=getValorChequeNumero();
        return s;
    }
    
    public String getDatos(){
        String s=getCedRuc();
        s+="  -  "+getNombreYApellidos();
        s+="  -  "+getTelefono();
        return s;
    }
    
    public String getCodCajeroYCodLocal(){
        String s = getCodigoCajero();
        s+="  -  "+getCodigoLocal();
        return s;
    }
    public String getDameRayaPunteada(){
        String s="";
        for(int i=0;i<=getNombreEmpresa().length();i++){
            s+='-';
        }
        return s;
    }
}
