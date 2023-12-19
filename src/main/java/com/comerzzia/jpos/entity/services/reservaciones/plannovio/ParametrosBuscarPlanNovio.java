/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones.plannovio;

import com.comerzzia.jpos.gui.validation.IValidableForm;
import es.mpsistemas.util.log.Logger;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class ParametrosBuscarPlanNovio {
    
    private static Logger log = Logger.getMLogger(ParametrosBuscarPlanNovio.class);
    protected static SimpleDateFormat formateadorFechaCorta = new SimpleDateFormat("dd-MMM-yyyy");
    protected static SimpleDateFormat formateadorFechaLarga = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    
    private BigInteger numeroReserva;
    
    private String nombresNovia;
    private String nombresNovio;
    private String apellidosNovia; 
    private String apellidosNovio; 
    private String documentoNovia;
    private String documentoNovio;
    private BigInteger idPlanSukasa;
    
    
    private String boda;    
    private String estadoPlan;    
    private Date desde;
    private Date hasta;
    private String tiendaPlan;
    

       
    public ParametrosBuscarPlanNovio() {
        numeroReserva = null;
        nombresNovia = "";
        nombresNovio = "";
        apellidosNovia = "";
        apellidosNovio = "";        
        documentoNovia= "";
        documentoNovio= "";
        boda = "";
        estadoPlan = "";
        idPlanSukasa=null;
        desde = null;
        hasta = null;
        tiendaPlan="";
    }

    public void estableceParametros(List<IValidableForm> formularioBusqueda) {
        
        log.debug("Estableciendo numero de reserva");
        IValidableForm tnumero_reserva  = formularioBusqueda.get(0);
        if (!tnumero_reserva.getText().trim().isEmpty()){
            this.numeroReserva = new BigInteger(tnumero_reserva.getText().trim());
        }
        else{
            this.numeroReserva = null;
        }
        //t_numero_reserva);
        log.debug("Estableciendo boda");
        IValidableForm tboda = formularioBusqueda.get(1);
        this.boda = tboda.getText().trim();
        //t_documento);
        log.debug("Estableciendo documento de novia");
        IValidableForm tdocumentoNovia = formularioBusqueda.get(2);
        this.setDocumentoNovia(tdocumentoNovia.getText().trim());
        
        log.debug("Estableciendo documento de novio");
        IValidableForm tdocumentoNovio = formularioBusqueda.get(3);
        this.setDocumentoNovio(tdocumentoNovio.getText().trim());
        //t_numero_fidelizado);
        log.debug("Estableciendo nombres de novia");
        IValidableForm tnombresNovia = formularioBusqueda.get(4);
        this.setNombresNovia(tnombresNovia.getText().trim());
        
        log.debug("Estableciendo nombres de novio");
        IValidableForm tnombresNovio = formularioBusqueda.get(5);
        this.setNombresNovio(tnombresNovio.getText().trim());
        //t_nombre);
        log.debug("Estableciendo apellidos de novia");
        IValidableForm tapellidosNovia = formularioBusqueda.get(6);
        this.setApellidosNovia(tapellidosNovia.getText().trim());
        
        log.debug("Estableciendo apellidos de novio");
        IValidableForm tapellidosNovio = formularioBusqueda.get(7);
        this.setApellidosNovio(tapellidosNovio.getText().trim());
        //t_apellido);
        log.debug("Estableciendo fecha desde");
        IValidableForm tdesde  = formularioBusqueda.get(8);        
        if (!tdesde.getText().trim().isEmpty()) {
            try {
                this.desde = formateadorFechaCorta.parse(tdesde.getText().trim());
            }
            catch (ParseException ex) {
                log.debug("Error de formato en fecha Desde, lo ponemos a null");
                tdesde.setText("");
                this.desde = null;
            }
        }
        else{
            this.desde = null;
        }
        //t_fecha_desde);
        log.debug("Estableciendo fecha hasta");
        IValidableForm thasta  = formularioBusqueda.get(9);
        if (!thasta.getText().trim().isEmpty()) {
            try {
                this.hasta = formateadorFechaLarga.parse(thasta.getText().trim()+" 24:59:59");
                
             }
            catch (ParseException ex) {
                log.debug("Error de formato en fecha Hasta, lo ponemos a null");
                this.hasta = null;
            }   
        }
        else{
            this.hasta = null;
        }
        
        IValidableForm tidSukasa = formularioBusqueda.get(10);        
         if (!tidSukasa.getText().trim().isEmpty()){
            this.idPlanSukasa = new BigInteger(tidSukasa.getText().trim());
        }
        else{
            this.idPlanSukasa = null;
        }        
    }
       


    public String getBoda() {
        return boda;
    }

    public void setBoda(String boda) {
        this.boda = boda;
    }

    public String getEstadoPlan() {
        return estadoPlan;
    }

    public void setEstadoPlan(String estadoPlan) {
        this.estadoPlan = estadoPlan;
    }



    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }


    public BigInteger getNumeroReserva() {
        return numeroReserva;
    }

    public void setNumeroReserva(BigInteger numeroReserva) {
        this.numeroReserva = numeroReserva;
    }

    public String getNombresNovia() {
        return nombresNovia;
    }

    public void setNombresNovia(String nombresNovia) {
        this.nombresNovia = nombresNovia;
    }

    public String getNombresNovio() {
        return nombresNovio;
    }

    public void setNombresNovio(String nombresNovio) {
        this.nombresNovio = nombresNovio;
    }

    public String getApellidosNovia() {
        return apellidosNovia;
    }

    public void setApellidosNovia(String apellidosNovia) {
        this.apellidosNovia = apellidosNovia;
    }

    public String getApellidosNovio() {
        return apellidosNovio;
    }

    public void setApellidosNovio(String apellidosNovio) {
        this.apellidosNovio = apellidosNovio;
    }

    public String getDocumentoNovia() {
        return documentoNovia;
    }

    public void setDocumentoNovia(String documentoNovia) {
        this.documentoNovia = documentoNovia;
    }

    public String getDocumentoNovio() {
        return documentoNovio;
    }

    public void setDocumentoNovio(String documentoNovio) {
        this.documentoNovio = documentoNovio;
    }

    public BigInteger getIdPlanSukasa() {
        return idPlanSukasa;
    }

    public void setIdPlanSukasa(BigInteger idPlanSukasa) {
        this.idPlanSukasa = idPlanSukasa;
    }

    public String getTiendaPlan() {
        return tiendaPlan;
    }

    public void setTiendaPlan(String tiendaPlan) {
        this.tiendaPlan = tiendaPlan;
    }
    
}
