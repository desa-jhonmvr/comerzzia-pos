/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.ntp;

import es.mpsistemas.util.log.Logger;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

/**
 *
 * @author cggm
 */
public class NTPService {

    protected static Logger log = Logger.getMLogger(NTPService.class);

    public Date getNTPDate() {
        String[] hosts = new String[]{"2.ec.pool.ntp.org", "2.south-america.pool.ntp.org", "0.south-america.pool.ntp.org"};

        Date fechaRecibida;
        NTPUDPClient cliente = new NTPUDPClient();
        cliente.setDefaultTimeout(5000);
        for (String host : hosts) {
            try {
                log.info("Obteniendo fecha desde: " + host);
                InetAddress hostAddr = InetAddress.getByName(host);
                TimeInfo fecha = cliente.getTime(hostAddr);
                fechaRecibida = new Date(fecha.getMessage().getTransmitTimeStamp().getTime());
                return fechaRecibida;
            } catch (IOException e) {                
                log.error("NO SE PUDO CONECTAR AL SERVIDOR " + host);
                log.error(e.getMessage(), e);
            }
        }
        log.error("No se pudo conectar con servidor, regresando hora local de la caja.");
        cliente.close();
        return null;
    }
}
