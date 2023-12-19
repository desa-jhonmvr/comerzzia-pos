/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.thread;

import com.comerzzia.jpos.servicios.devoluciones.DevolucionesServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.comerzzia.jpos.dto.ResponseDTO;
import com.comerzzia.jpos.dto.ventas.NotaCreditoLocalDTO;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.enums.EnumGenericoError;
import com.comerzzia.jpos.util.exception.SocketTPVException;
import java.net.BindException;

/**
 *
 * @author Gabriel Simbania
 */
public class NotaCreditoSocketThread extends Thread {

    private static final Logger log = Logger.getMLogger(NotaCreditoSocketThread.class);

    private final int port;

    public NotaCreditoSocketThread(int port) {
        this.port = port;
    }
    
    @Override
    public void run() {
        ServerSocket ss = null;
        DataOutputStream dout = null;
        DataInputStream dis = null;
        boolean cargar = true;
        try {
            Socket s = null;
            try {
                ss = new ServerSocket(port);
                s = ss.accept();
            } catch (BindException e) {
                //Si existe otro socket no vuelve a cargar el hilo
                 log.error("Error ya existe el socket: " + e.getMessage());
                cargar = false;
                throw e;
            }

            log.info("Ingresa a generar la nota de credito desde otro local");
            dis = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            String str = (String) dis.readUTF();
            //log.info("Contenido " + str);
            if (Sesion.getUsuario() == null ||  Sesion.cajaActual==null || Sesion.cajaActual.getCajaActual()==null || Sesion.cajaActual.getCajaParcialActual()==null) {
                throw new SocketTPVException(EnumGenericoError.ERROR_LOGIN.getCodigo(), EnumGenericoError.ERROR_LOGIN.getDescripcion());
            }
            NotaCreditoLocalDTO notaCreditoLocalDTO = JsonUtil.jsonToObject(str, NotaCreditoLocalDTO.class);
            ResponseDTO responseDTO = DevolucionesServices.generaNotaCreditoDesdeOtroLocal(notaCreditoLocalDTO);
            String responseString = JsonUtil.objectToJson(responseDTO);
            //dout.writeUTF(JsonUtil.objectToJson(responseDTO));
            dout.writeBytes(responseString);
            dout.flush();

        } catch (SocketTPVException e) {
            log.error("Error en el socket:" + e.getMessage());
            if (dout != null) {
                try {
                    ResponseDTO responseDTO = e.getResponseDTO();
                    dout.writeBytes(JsonUtil.objectToJson(responseDTO));
                    dout.flush();
                    dout.close();
                } catch (IOException ex) {
                    log.error("Error en el proceso IO");
                }
            }

        } catch (Exception e) {
            log.error("Error general en el socket:" + e.getMessage(),e);
            if (dout != null) {
                try {
                    ResponseDTO responseDTO = new ResponseDTO();
                    responseDTO.setExito(Boolean.FALSE);
                    responseDTO.setCodigoError(EnumGenericoError.ERROR_GENERAL.getCodigo());
                    responseDTO.setDescripcion(EnumGenericoError.ERROR_GENERAL.getDescripcion() + e.getMessage());
                    dout.writeBytes(JsonUtil.objectToJson(responseDTO));
                    dout.flush();
                    dout.close();
                } catch (IOException ex) {
                    log.error("Error en el proceso IO");
                }
            }
        } finally {

            try {
                if (ss != null) {
                    ss.close();
                }
                if (dout != null) {
                    dout.close();
                }
                if (dis != null) {
                    dis.close();
                }

            } catch (IOException ex) {
                log.error("Error:" + ex.getMessage());
            }
            if (cargar) {
                run();
            }
        }
    }

}
