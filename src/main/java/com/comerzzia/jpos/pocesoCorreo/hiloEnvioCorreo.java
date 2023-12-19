/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.pocesoCorreo;

import com.comerzzia.jpos.correo.DatosCorreo;
import com.comerzzia.jpos.correo.EnvioCorreo;
import com.comerzzia.jpos.servicios.sms.ServicioSms;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author CONTABILIDAD
 */
public class hiloEnvioCorreo implements Runnable{
    private DatosCorreo correo;
    private long time;
       private static final Logger log = Logger.getMLogger(hiloEnvioCorreo.class);
   
public hiloEnvioCorreo (DatosCorreo correo, long initialTime){
		this.correo = correo;
		this.time = initialTime;
	}

    @Override
    public void run() {
        EnvioCorreo.envio(correo);
       long segundos=((System.currentTimeMillis()-time)/1000);
       log.debug("Tiempo de demora del proceso:"+segundos+" Segundos");
       
    }
    
}
