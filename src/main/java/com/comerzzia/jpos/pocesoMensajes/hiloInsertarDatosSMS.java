/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.pocesoMensajes;

import com.comerzzia.jpos.entity.db.ReprocesoGeneral;
import com.comerzzia.jpos.gui.main.TPV;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.thread.ProcesoEncolarThread;
import com.comerzzia.util.Constantes;
import es.mpsistemas.util.log.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author CONTABILIDAD
 */
public class hiloInsertarDatosSMS implements Runnable{
    private String idTransaccion;
    private String numCelular;
    private String valor;
    private String uidTicket;
    private String numeroCedula;
    private static final Logger log = Logger.getMLogger(hiloInsertarDatosSMS.class);
   
    public hiloInsertarDatosSMS (String idTransaccion, String numCelular, String valor, String uidTicket, String numeroCedula){
        this.idTransaccion = idTransaccion;
        this.numCelular = numCelular;
        this.valor = valor;
        this.uidTicket = uidTicket;
        this.numeroCedula = numeroCedula;
    }

    @Override
    public void run() {
        String urlString = "";
        try {
            String pattern = "dd/MM/yyyy HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            urlString = "<pedido><metodo>SMSEnvio</metodo><id_cbm>"
                    + VariablesAlm.getVariable(VariablesAlm.SMS_IDCLIENTE)
                    + "</id_cbm><token>"
                    + VariablesAlm.getVariable(VariablesAlm.SMS_TOKEN)
                    + "</token><id_transaccion>"
                    + idTransaccion
                    + "</id_transaccion><telefono>"
                    + "XXXXXXXXXX"
                    + "</telefono><id_mensaje>"
                    + VariablesAlm.getVariable(VariablesAlm.SMS_IDMENSAJE)
                    + "</id_mensaje><dt_variable>1</dt_variable><datos><valor>"
                    + Sesion.getTienda().getSriTienda().getDesalm()
                    + "</valor><valor>"
                    + date + "h"
                    + "</valor><valor>"
                    + valor
                    + "</valor><valor>"
                    + Sesion.getTienda().getAlmacen().getTelefono1()
                    + " " + Sesion.getTienda().getSriTienda().getDesalm()
                    + "</valor></datos></pedido>";
            if(urlString.contains(" "))
                 urlString = urlString.replace(" ", "%20");
            
            EnvioSms envioSms = new EnvioSms(urlString, numeroCedula);
            String envioSmsString = JsonUtil.objectToJson(envioSms);

            ProcesoEncolarThread envioDomicilioThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), envioSmsString, Variables.getVariable(Variables.QUEUE_ENVIO_SMS), Constantes.PROCESO_ENVIO_SMS, numeroCedula);
            envioDomicilioThread.start();
            

        } catch (Exception ex) {
            EntityManagerFactory emf = Sesion.getEmf();
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            ReprocesoGeneral reprocesoGeneral = new ReprocesoGeneral(uidTicket, Constantes.PROCESO_ENVIO_SMS,new Date(), urlString,1L);
            try {
                em.persist(reprocesoGeneral);
                em.getTransaction().commit();
            } catch (Exception ex2) {
                em.getTransaction().rollback();
                ex2.printStackTrace();
            } 
            java.util.logging.Logger.getLogger(TPV.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
     
       
    }
    
}
