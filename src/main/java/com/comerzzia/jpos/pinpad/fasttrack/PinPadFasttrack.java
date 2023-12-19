/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.pinpad.fasttrack;

import DF.LANConfig;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.DatosConfiguracion;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author SMLM
 */
public class PinPadFasttrack {
   private static IPinPadFasttrack pinpad;
   private static PinPadFasttrack instance;
   private static String tipo;
   private static final Logger log = Logger.getMLogger(PinPadFasttrack.class);
   public static LANConfig cfg;
   public static int PUERTO_PINPAD = 9999;
   static{
       if(Sesion.getTienda().getSriTienda().getCajaActiva().getIpPinpad() != null){
            cfg = new LANConfig(Sesion.getTienda().getSriTienda().getCajaActiva().getIpPinpad(), 
                   PUERTO_PINPAD, 
                   VariablesAlm.getVariableAsInt(VariablesAlm.PINPAD_TIMEOUT) * 1000, 
                   //3600,
                   VariablesAlm.getVariable(VariablesAlm.PINPAD_MID_DATAFAST), 
                   Sesion.getTienda().getSriTienda().getCajaActiva().getTidDatafast(), 
                   "00" + Numero.completaconCeros(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), 3) + Numero.completaconCeros(Sesion.getDatosConfiguracion().getCodcaja(), 3),
                   2, 2);
       }
   }
   
   public static PinPadFasttrack getInstance() {
       if(instance == null){
           instance = new PinPadFasttrack();
           
           if(Sesion.getDatosConfiguracion().getTipoModoPinPad().equals(DatosConfiguracion.VALIDACION_MANUAL)){
               pinpad = new PinPadFasttrackManual();
               tipo = DatosConfiguracion.VALIDACION_MANUAL;
           } else {
               //try {
                   pinpad = PinPadFasttrackAutomatico.getInstance();
                   //pinpad.configuracionPinPad();
                   tipo = DatosConfiguracion.VALIDACION_AUTOMATICA;
               //} catch (PinPadFasttrackException ex) {
               //    java.util.logging.Logger.getLogger(PinPadFasttrack.class.getName()).log(Level.SEVERE, null, ex);
               //}
           }
       }
       return instance;
   }
   /**
    * Solución Problema de Permisos PinPad
    */
   public void controlPermisosPinPad(){
       //////////////////////////////////////////////Solucion PinPad Plan C//////////////////////////////////////////////////////////////////
               File file = new File(System.getProperties().getProperty("user.dir") + File.separator + "ini" + File.separator + "UC-CAJA.ini");
            if (!file.exists()) {
                log.error("PINPAD: No se ha encontrado fichero ini en la ruta: " + System.getProperties().getProperty("user.dir") + File.separator + "ini" + File.separator + "UC-CAJA.ini");
            } else {
                log.info("PINPAD: Fichero ini encontrado");
            }
            try {
                Runtime.getRuntime().exec("chmod -R 777 /dev/lock/");
                String respuestaLock = "";
                Process process = Runtime.getRuntime().exec("ls /var/lock");
                BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((respuestaLock = inputStreamReader.readLine()) != null) {
                    if (respuestaLock.equals("LCK..ttyS" + 9)) {
                        String comando = "rm -f /var/lock/" + respuestaLock;
                        Runtime.getRuntime().exec(comando);
                    } else if (respuestaLock.equals("LCK..ttyUSB" + 9)) {
                        String comando = "rm -f /var/lock/" + respuestaLock;
                        Runtime.getRuntime().exec(comando);
                    }
                }
            } catch (IOException ex) {
               log.info("PINPAD: error comandos linux ");
            }

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            try {
                String respuestaCrearVar = "";
                Runtime.getRuntime().exec("chmod -R 777 /dev/");
                 log.debug("Permisos PinPad - Se le dio permisos con el siguiente comando chmod -R 777 /dev/");
                Process processCrear = Runtime.getRuntime().exec("ls /dev/");
                BufferedReader inputStreamCrear = new BufferedReader(new InputStreamReader(processCrear.getInputStream()));
                boolean banderaACM = false;   
                boolean banderattyS = false;
                while ((respuestaCrearVar = inputStreamCrear.readLine()) != null) {
                    if (respuestaCrearVar.equals("ttyACM0")) {
                        banderaACM = true;
                         Runtime.getRuntime().exec("chmod -R 777 /dev/ttyACM0");
                       log.debug("Permisos PinPad -ttyACM0 encontrado Se le dio permisos con el siguiente comando chmod -R 777 /dev/ttyACM0");

                    }
                    if (respuestaCrearVar.equals("ttyS" + 9)) {
                       Runtime.getRuntime().exec("chmod -R 777 /dev/ttyS" + 9);
                       log.debug("Permisos PinPad -ttyS simbolico encontrado Se le dio permisos con el siguiente comando chmod -R 777 /dev/ttyS");
                        banderattyS = true;                      
                    }
                }
                if (banderaACM == true) {
                      log.debug("Puerto PinPad - Puerto Origen ttyACM0 Encontrado...");
                      log.debug("PinPad - continuar con envio PinPad");
                    
                } else {
                     log.debug("Puerto PinPad - Puerto Origen ttyACM0 No Encontrado");
                    if(banderattyS){
                     log.debug("Puerto PinPad - Puerto Simbolico ttyS Encontrado Pero No Tiene Origen Donde  Apuntar");
                     Runtime.getRuntime().exec("rm -rf /dev/ttyS" + 9);
                      log.debug("PinPad - Se Ejecuta el comando rm -rf /dev/ttyS para eliminar enlaces rotos");
                     log.debug("PinPad -  /dev/ttyS eliminado");

                    }
                }
                if (!banderattyS) {
                     log.debug("Puerto PinPad - No Se Econtro El Puerto Simbolico ttyS ");
                     log.debug("Puerto PinPad - Se creara Puerto Simbolico desde libreria hiper");
                }
                if(!banderaACM && !banderattyS){
                     log.debug("Puerto PinPad - No Se Econtro Un Puerto Activo Conecte y Desconecte PinPad");
                    
                }
            } catch (IOException ex) {
               log.info("PINPAD: error comandos linux ");
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////       
  
   }
   public boolean isManual() {
       return DatosConfiguracion.VALIDACION_MANUAL.equals(tipo);
   }
   
   public boolean isAutomatico() {
       return DatosConfiguracion.VALIDACION_AUTOMATICA.equals(tipo);
   }
   
   public IPinPadFasttrack getiPinPad(){
       return pinpad;
   }

    public static String getTipo() {
        return tipo;
    }

    public static void setTipo(String tipo) {
        PinPadFasttrack.tipo = tipo;
    }
   
   
}
