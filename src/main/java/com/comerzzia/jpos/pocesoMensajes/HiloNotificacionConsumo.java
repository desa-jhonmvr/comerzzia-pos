/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.pocesoMensajes;

import com.comerzzia.jpos.dto.ventas.NotificacionConsumoDTO;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.thread.ProcesoEncolarThread;
import com.comerzzia.util.Constantes;
import es.mpsistemas.util.log.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Gabriel Simbania
 */
public class HiloNotificacionConsumo implements Runnable {

    private final String idTransaccion;
    private final String numCelular;
    private final String valor;
    private final String uidTicket;
    private final String numeroCedula;
    private static final Logger LOG_POS = Logger.getMLogger(HiloNotificacionConsumo.class);
   
    public HiloNotificacionConsumo (String idTransaccion, String numCelular, String valor, String uidTicket, String numeroCedula){
        this.idTransaccion = idTransaccion;
        this.numCelular = numCelular;
        this.valor = valor;
        this.uidTicket = uidTicket;
        this.numeroCedula = numeroCedula;
    }
    
    
    @Override
    public void run() {
        
        try {
            String pattern = "dd/MM/yyyy HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            
            NotificacionConsumoDTO notificacionConsumoDTO = new NotificacionConsumoDTO();
            notificacionConsumoDTO.setIdCmbSms(Integer.parseInt(VariablesAlm.getVariable(VariablesAlm.SMS_IDCLIENTE)));
            notificacionConsumoDTO.setIdTransaccion(idTransaccion);
            notificacionConsumoDTO.setMessageIdSms(Integer.parseInt(VariablesAlm.getVariable(VariablesAlm.SMS_IDMENSAJE)));
            notificacionConsumoDTO.setNombreAlmacen(Sesion.getTienda().getSriTienda().getDesalm());
            notificacionConsumoDTO.setFechaHora(date);
            notificacionConsumoDTO.setValor(valor);
            notificacionConsumoDTO.setTelefonoAlmacen(Sesion.getTienda().getAlmacen().getTelefono1());
            notificacionConsumoDTO.setIdentificacion(numeroCedula);
            
            ProcesoEncolarThread envioDomicilioThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), JsonUtil.objectToJson(notificacionConsumoDTO), Constantes.QUEUE_NOTIFICACION_CONSUMO_POS, Constantes.PROCESO_NOTIFICACION_CONSUMO_POS, uidTicket);
            envioDomicilioThread.start();

        } catch (Exception ex) {
            LOG_POS.error("Error al enviar la notificacion de consumo "+ex.getMessage(),ex);
        }

    }
}
