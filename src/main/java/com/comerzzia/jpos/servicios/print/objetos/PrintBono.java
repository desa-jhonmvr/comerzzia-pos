/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.servicios.bonos.ComprobanteBono;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author MGRI
 */
public class PrintBono extends PrintDocument {

    private String tipoTransaccion;
    private String cajero;
    private String codalm;
    private String codalmBono;
    private String transaccion;
    
    private static Logger log = Logger.getMLogger(PrintBono.class);

    public PrintBono(ComprobanteBono cBono) {
        super(true, new Fecha());
        this.tipoTransaccion = cBono.getBono().getTipoTransaccion();
        this.cajero = cBono.getCajero().getDesUsuario();
        this.codalmBono = cBono.getBono().getBonoPK().getCodalm(); 
        this.codalm = Sesion.getTienda().getCodalm();
        this.transaccion = cBono.getBono().getTransaccion();
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public LineaEnTicket getCajeroAsLineas() {
        return new LineaEnTicket("LE ATENDIÓ: " + this.cajero + ", gracias.");
    }

    public String getCodalm() {
        return codalm;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public String getCodalmBono() {
        return codalmBono;
    }

    public void setCodalmBono(String codalmBono) {
        this.codalmBono = codalmBono;
    }
    
    public LineaEnTicket getFechaAsLineas() {
        return  new LineaEnTicket(Sesion.getTienda().getCodRegion().getDesregion() +", " +new Fecha().getString("EEEEE dd 'de' MMMMM 'del' yyyy"));
    }
    
}
