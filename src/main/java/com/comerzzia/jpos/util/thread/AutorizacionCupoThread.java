/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.thread;

import com.comerzzia.jpos.gui.bancos.JAutExcedeCupoTarjetaCreditoDirecto;
import es.mpsistemas.util.log.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Gabriel Simbania
 */
public class AutorizacionCupoThread extends Thread {

    private static final Logger LOG_POS = Logger.getMLogger(AutorizacionCupoThread.class);

    private JAutExcedeCupoTarjetaCreditoDirecto pantallaAutorizacion;
    private ServerSocket ss;

    public AutorizacionCupoThread(JAutExcedeCupoTarjetaCreditoDirecto pantallaAutorizacion, ServerSocket serverSocket) {
        this.pantallaAutorizacion = pantallaAutorizacion;
        this.ss = serverSocket;
    }

    @Override
    public void run() {

        //ServerSocket ss = null;
        DataOutputStream dout = null;
        DataInputStream dis = null;
        try {
            Socket s = null;
            try {
                s = ss.accept();
            } catch (BindException e) {
                LOG_POS.error("Error ya existe el socket: " + e.getMessage());
                throw e;
            }
            LOG_POS.info("Inicio esperando respuesta");
            String respuestaDispositivo = null;
            try {
                dis = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                respuestaDispositivo = dis.readUTF();

                dout.writeBytes("Exito");
                dout.flush();

            } finally {
                if (!ss.isClosed()) {
                    ss.close();
                }
                if (dout != null) {
                    dout.close();
                }
                if (dis != null) {
                    dis.close();
                }
            }

            pantallaAutorizacion.autorizaCupo(respuestaDispositivo);
            
        } catch (IOException ex) {
            LOG_POS.error("Error ", ex);
        } 

    }

}
