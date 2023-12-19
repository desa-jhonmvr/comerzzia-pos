/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.thread;

import com.comerzzia.jpos.util.ActiveMQUtil;

/**
 *
 * @author GabrielSimbania
 */
public class ProcesoEncolarThread extends Thread {

    private final String url;
    private final String mensaje;
    private final String queue;
    private final String proceso;
    private final String uid;

    public ProcesoEncolarThread(String url, String mensaje, String queue, String proceso, String uid) {
        this.url = url;
        this.mensaje = mensaje;
        this.queue = queue;
        this.proceso = proceso;
        this.uid = uid;
    }

    @Override
    public void run() {
        ActiveMQUtil activeMQUtil = new ActiveMQUtil();
        activeMQUtil.encolarMensaje(url, mensaje, queue, proceso, uid);
    }

}
