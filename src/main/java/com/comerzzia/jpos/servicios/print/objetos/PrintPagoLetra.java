/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.persistencia.letras.LetraBean;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaBean;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;

/**
 *
 * @author MGRI
 */
public class PrintPagoLetra extends PrintDocument{
    
    private String numeroTransaccion;
    private String fechaTransaccion;
    private String nombreCliente;
    private String observaciones;
    private String idGrupoLetra;
    private String idLetra;
    private String fechaVencimiento;
    private String valorSinMora;
    private String moraAdelanto;
    private String total;    
    private String numeroLetraAbono;
    // Formas de pago lo imprimimos como en facaturas    
        
    
    public PrintPagoLetra(TicketS ticket, LetraBean letra, LetraCuotaBean letraCuota){
        super(true, new Fecha());
        
        Fecha fecha = new Fecha();
        numeroTransaccion = letra.getIdFactura()+"-"+letraCuota.getCuota().toString();
        numeroLetraAbono = letra.getCodAlmacen()+"-"+letraCuota.getCodcajaAbono()+"-"+Numero.completaconCeros(letraCuota.getIdAbono().toString(),7);
        fechaTransaccion = fecha.getString("dd/MM/yyyy");
        nombreCliente = letra.getCliente().getApellido()+" "+letra.getCliente().getNombre();
        observaciones = letraCuota.getObservaciones();
        if(observaciones==null){
            observaciones="REVISADO";
        }
        idGrupoLetra = letra.getIdFactura();
        idLetra = letraCuota.getCuota().toString();
        fechaVencimiento = letraCuota.getFechaVencimiento().getString("dd/MM/yyyy");
        valorSinMora = letraCuota.getValor().toString();        
        moraAdelanto = letraCuota.getImporteMora().toString();        
        total = letraCuota.getTotal().toString();    
        // Formas de pago lo imprimimos como en facaturas         
        
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(String fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LineaEnTicket getObservacionesAsLineas() {
        return new LineaEnTicket("Observ. : " + this.observaciones);
    }
    
    public String getIdGrupoLetra() {
        return idGrupoLetra;
    }

    public void setIdGrupoLetra(String idGrupoLetra) {
        this.idGrupoLetra = idGrupoLetra;
    }

    public String getIdLetra() {
        return idLetra;
    }

    public void setIdLetra(String idLetra) {
        this.idLetra = idLetra;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getValorSinMora() {
        return valorSinMora;
    }

    public void setValorSinMora(String valorSinMora) {
        this.valorSinMora = valorSinMora;
    }

    public String getMoraAdelanto() {
        return moraAdelanto;
    }

    public void setMoraAdelanto(String moraAdelanto) {
        this.moraAdelanto = moraAdelanto;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNumeroLetraAbono() {
        return numeroLetraAbono;
    }

    public void setNumeroLetraAbono(String numeroLetraAbono) {
        this.numeroLetraAbono = numeroLetraAbono;
    }
    
}
