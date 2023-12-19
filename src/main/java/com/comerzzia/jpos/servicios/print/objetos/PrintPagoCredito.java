/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.persistencia.credito.abonos.AbonoCreditoBean;
import com.comerzzia.jpos.servicios.credito.CreditoDirectoBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import es.mpsistemas.util.fechas.Fecha;

/**
 *
 * @author MGRI
 */
public class PrintPagoCredito extends PrintDocument{
    
    private String numeroTransaccion;
    private String hora;    
    private String nombreCliente;
    private String fecha;
    private String direccionEmpresa;
    private String identificador;
    private String numeroDocumento;
    
    
     public PrintPagoCredito(TicketS ticket, CreditoDirectoBean creditoDirecto,AbonoCreditoBean abonoCredito){
        super(true, new Fecha());        
        Fecha fecha = new Fecha();
        
        numeroTransaccion = abonoCredito.getIdAbonoCredito();
        
        nombreCliente = creditoDirecto.getPlastico().getCliente().getApellido()+" "+creditoDirecto.getPlastico().getCliente().getNombre();
        // Formas de pago lo imprimimos como en facaturas         
        hora = fecha.getString("HH:mm");
        this.fecha = fecha.getString("dd-MMM-yyyy").toUpperCase();  
        this.direccionEmpresa = Sesion.getEmpresa().getDomicilio();
        this.identificador = abonoCredito.getIdAbonoCredito();
        this.numeroDocumento = abonoCredito.getCodCliente();
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    public LineaEnTicket getDireccionEmpresaAsLineas() {
       return  new LineaEnTicket("MATRIZ : "+ this.getDireccionEmpresa());
    }
        
    @Override
    public LineaEnTicket getDireccionLocalAsLineas() {
       return  new LineaEnTicket("SUCURSAL : "+ this.getDireccionLocal());
    }

    public String getDireccionEmpresa() {
        return direccionEmpresa;
    }

    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
    
    @Override
    public LineaEnTicket getLugarYfechaAsLineas() {
        return  new LineaEnTicket("FECHA : "+getLugarYfecha());
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    
}
