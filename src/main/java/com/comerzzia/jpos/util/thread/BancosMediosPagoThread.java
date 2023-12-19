/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.thread;

import com.comerzzia.jpos.dto.ResponseDTO;
import com.comerzzia.jpos.dto.ventas.BancoOnlineDTO;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServices;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServicesImpl;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.util.JsonUtil;
import es.mpsistemas.util.log.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class BancosMediosPagoThread extends Thread {

    private static final Logger LOG_POS = Logger.getMLogger(BancosMediosPagoThread.class);

    private static final int SOCKET=15288;
    
    @Override
    public void run() {
        ServerSocket ss = null;
        DataOutputStream dout = null;
        DataInputStream dis = null;
        boolean cargar = true;
        try {
            Socket s = null;
            try {
                ss = new ServerSocket(SOCKET);
                s = ss.accept();
            } catch (BindException e) {
                //Si existe otro socket no vuelve a cargar el hilo
                LOG_POS.error("Error ya existe el socket: " + e.getMessage());
                cargar = false;
                throw e;
            }
            LOG_POS.info("Inicio Consulta planes de financiamiento ");
            dis = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            ResponseDTO responseDTO = new ResponseDTO();
            try {
                FormaPagoServices formaPagoServices = new FormaPagoServicesImpl();
                List<BancoOnlineDTO> bancos = formaPagoServices.obtenerBancos();

                responseDTO.setExito(Boolean.TRUE);
                responseDTO.setObjetoRespuesta(bancos);
            } catch (MedioPagoException ex) {
                String error = "Error en el medio de pago " + ex.getMessage();
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
        LOG_POS.info("Fin Consulta planes de financiamiento ");
    }

}
