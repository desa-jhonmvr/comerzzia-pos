/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import es.mpsistemas.util.fechas.Fecha;

/**
 *
 * @author MGRI
 */
public class PrintSukupon extends PrintDocument{
    
    private String codigoCupon;
    private String referencia; // Factura que genera el cupon
    
    private String cedRuc;
    private String cliente;
    private String telefono;    
    private String valor;
    
    private String nombreTienda;
    private String nombreEmpresa;
    
    private String vence;

    
    public PrintSukupon(TicketS ticket, Cupon cupon){
        super(true, new Fecha());
        
        this.nombreEmpresa = Sesion.getEmpresa().getNombreComercial();            
        this.nombreTienda = Sesion.getTienda().getSriTienda().getDesalm(); // Enviado por el módulo SRI  
        
        codigoCupon  = Sesion.getTienda().getCodalm()+"-"+cupon.getIdCupon();
        referencia = ticket.getIdFactura();
        
        cedRuc = ticket.getCliente().getIdentificacion();
        cliente = ticket.getFacturacion().getApellidos() + " "+ ticket.getFacturacion().getNombre();
        if (ticket.getFacturacion().getTelefono() != null && ticket.getFacturacion().getTelefono().length()==9){
            telefono = ticket.getFacturacion().getTelefono();
        }
        else if (ticket.getFacturacion()!=null && ticket.getCliente().getTelefonoMovil()!=null){
            telefono = ticket.getCliente().getTelefonoMovil();
        } else {
            telefono = "......";
        }
       
        valor =cupon.getVariable(); // Valor del sukupon 
        
        vence = cupon.getFechaValidez().getString("dd 'de' MMMMM 'de' yyyy");
        
    }
    
    public String getCodigoCupon() {
        return codigoCupon;
    }

    public void setCodigoCupon(String codigoCupon) {
        this.codigoCupon = codigoCupon;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getVence() {
        return vence;
    }

    public void setVence(String vence) {
        this.vence = vence;
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
    
}
