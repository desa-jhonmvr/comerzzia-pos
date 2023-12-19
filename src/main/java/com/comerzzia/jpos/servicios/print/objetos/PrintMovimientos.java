/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.entity.db.Caja;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.entity.db.Usuarios;
import com.comerzzia.jpos.entity.services.cierrecaja.CierreCaja;
import com.comerzzia.jpos.entity.services.cierrecaja.LineaCierreCaja;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuariosDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author CONTABILIDAD
 */
public class PrintMovimientos extends PrintDocument {
    private String pie;
    private String autorizado;
    private String cajaNumero;
    private String fecha;
    private CierreCaja caja;
    private String numeroCaja;
    private static Logger log = Logger.getMLogger(PrintTicket.class);
    private List<LineaCierreCaja> data;   
    public PrintMovimientos(CierreCaja cc){
        this.pie = "ORIGINAL";
        Fecha fechaac = new Fecha(new Date());
        Date fechacero = new Date();
        fechacero.setHours(0);
        fechacero.setMinutes(0);
        fechacero.setSeconds(0);
        fechaac.sumaDias(1);
        Fecha fechaDiaEnCero = new Fecha(fechacero);
        DateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy");
        this.fecha = fechaHora.format(fechaDiaEnCero.getDate());
        this.caja=cc;
        data = new ArrayList(cc.getListaCierreCaja().values());
        numeroCaja=Sesion.getCajaActual().getCajaActual().getCodcaja();
    }

    public String getPie() {
        return pie;
    }

    public void setPie(String pie) {
        this.pie = pie;
    }
    public String getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(String autorizado) {
        this.autorizado = autorizado;
    }



    public String getCajaNumero() {
        return cajaNumero;
    }

    public void setCajaNumero(String cajaNumero) {
        this.cajaNumero = cajaNumero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public CierreCaja getCaja() {
        return caja;
    }

    public void setCaja(CierreCaja caja) {
        this.caja = caja;
    }

    public List<LineaCierreCaja> getData() {
        return data;
    }

    public void setData(List<LineaCierreCaja> data) {
        this.data = data;
    }

    public String getNumeroCaja() {
        return numeroCaja;
    }

    public void setNumeroCaja(String numeroCaja) {
        this.numeroCaja = numeroCaja;
    }

}
