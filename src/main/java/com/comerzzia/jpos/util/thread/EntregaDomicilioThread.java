/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.thread;

import com.comerzzia.jpos.dto.ResponseDTO;
import com.comerzzia.jpos.dto.ventas.DocumentoDTO;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.entregas.ServicioEntrega;
import com.comerzzia.jpos.servicios.entregas.ServicioEntregaImpl;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.exception.SocketTPVException;
import com.comerzzia.util.Constantes;
import es.mpsistemas.util.log.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

/**
 *
 * @author Gabriel Simbania
 */
public class EntregaDomicilioThread extends Thread {

    private static final Logger LOG_POS = Logger.getMLogger(EntregaDomicilioThread.class);

    @Override
    public void run() {
        ServerSocket ss = null;
        DataOutputStream dout = null;
        DataInputStream dis = null;
        boolean cargar = true;
        try {
            Socket s = null;
            try {
                ss = new ServerSocket(Constantes.SOCKET_ENTREGA_DOMICILIO);
                s = ss.accept();
            } catch (BindException e) {
                //Si existe otro socket no vuelve a cargar el hilo
                LOG_POS.error("Error ya existe el socket: " + e.getMessage());
                cargar = false;
                throw e;
            }
            LOG_POS.info("Inicio cambia a entregado ");
            dis = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            String str = (String) dis.readUTF();
            DocumentoDTO peticion = JsonUtil.jsonToObject(str, DocumentoDTO.class);
            ResponseDTO responseDTO = new ResponseDTO();
            try {
                ServicioEntrega servicioEntrega = new ServicioEntregaImpl();
                servicioEntrega.cambiaEstadoEntregado(peticion.getNumeroDocumento(), peticion.getObservacion());
                responseDTO.setExito(Boolean.TRUE);

            } catch (SocketTPVException ex) {
                responseDTO.setExito(Boolean.FALSE);
                responseDTO.setDescripcion("Error " + ex.getMessage());
                LOG_POS.error(ex);
            } catch (ClienteException ex) {
                responseDTO.setExito(Boolean.FALSE);
                responseDTO.setDescripcion("Error en el cliente " + ex.getMessage());
                LOG_POS.error(ex);
            } catch (SQLException ex) {
                responseDTO.setExito(Boolean.FALSE);
                responseDTO.setDescripcion("Error en el SQL " + ex.getMessage());
                LOG_POS.error(ex);
            } catch (TicketException ex) {
                String error = "Error en la entrega " + ex.getMessage();
                responseDTO.setExito(Boolean.FALSE);
                responseDTO.setDescripcion(error);
                LOG_POS.error(error, ex);
            } catch (Exception ex) {
                String error = "Error general " + ex.getMessage();
                responseDTO.setExito(Boolean.FALSE);
                responseDTO.setDescripcion(error);
                LOG_POS.error(error, ex);
            }
            dout.writeBytes(JsonUtil.objectToJson(responseDTO));
            dout.flush();

        } catch (Exception ex) {
            LOG_POS.error("Error ", ex);
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
                LOG_POS.error("Error en el IO:" + ex.getMessage());
            }
            if (cargar) {
                run();
            }
        }
        LOG_POS.info("Fin cambia a entregado ");
    }

}
