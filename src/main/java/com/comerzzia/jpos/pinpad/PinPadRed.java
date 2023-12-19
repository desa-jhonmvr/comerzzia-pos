/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.pinpad;

import static com.comerzzia.jpos.pinpad.PinPadAutomatico.log;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class PinPadRed {
    
//    public static String enviarTrama(String msg) throws SocketTimeoutException, IOException {
//        String mensajeRecibido = null;
//        DataInputStream entrada = null;
//        DataOutputStream salida = null;
//         Socket socket=null;
//        byte buffer[] = new byte[1024];
//
//        try {
//            log.debug("enviarTrama() - Enviando trama PinPad - " + msg);
//            //El formato de la cadena es host:puerto
////            String cadena = Variables.PINPAD_IP+":"+Variables.PINPAD_PUERTO;
//            String cadena = Sesion.getDatosConfiguracion().getPINPAD_IP()+":"+Sesion.getDatosConfiguracion().getPINPAD_PUERTO();
//            String host = cadena.substring(0, cadena.indexOf(":"));
//            Integer puerto = new Integer(cadena.substring(cadena.indexOf(":") + 1, cadena.length()));
//
//             socket = new Socket(host, puerto);
//            if (socket != null) {
////                int timeout = new Integer(1500); //Valor definido para el TIMEOUT
////                socket.setSoTimeout((timeout > 0 ? timeout : 1)*1000); //Pasamos el TIMEOUT a milisegundos
////                
//                entrada = new DataInputStream(socket.getInputStream());
//                salida = new DataOutputStream(socket.getOutputStream());
//                log.debug("enviarTrama() - Enviamos la trama PinPad");
//                salida.write(msg.getBytes());
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                int s;
//                do {
//                    s = entrada.read(buffer); //Leemos la respuesta del servidor (entrada)
//                    if (s != -1) {
//                        baos.write(buffer, baos.size(), s);
//                    }
//                } while (s >= buffer.length);
//
//                byte result[] = baos.toByteArray();
//                mensajeRecibido = new String(result, "UTF-8");
//                System.out.println(mensajeRecibido);
////                mensajeRecibido = mensajeRecibido.substring(7);
//                log.debug("enviarTrama() - Mensaje recibido: " + mensajeRecibido);
//            }
//            entrada.close();
//            salida.close();
//            socket.close();
//        } catch (Exception ex) {
//            log.error("enviarTrama() - Error al enviar la trama de petición PinPad: " + ex.getMessage(), ex);
//            if (ex instanceof SocketTimeoutException) {
//                 entrada.close();
//                salida.close();
//                socket.close();
//                throw new SocketTimeoutException(ex.getMessage());
//            } else {
//                throw new IOException(ex.getMessage());
//                
//            }
//            
//        }
//        return mensajeRecibido;
//    }

public static String completarTamanoTrama(String trama){
         String hexa = Integer.toHexString(trama.length());
          hexa= hexa.toUpperCase();
         hexa="00"+hexa; 
         trama=hexa+trama;
    return trama;    
} 
}
