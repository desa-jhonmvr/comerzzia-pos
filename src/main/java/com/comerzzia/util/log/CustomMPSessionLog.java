/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util.log;

import com.comerzzia.jpos.gui.reservaciones.JMostrarReservacion;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author MGRI
 */
public class CustomMPSessionLog extends AbstractSessionLog implements SessionLog {
    /* @see org.eclipse.persistence.logging.AbstractSessionLog#log(org.eclipse.persistence.logging.SessionLogEntry)
     */
    private static Logger log = Logger.getMLogger(CustomMPSessionLog.class);
    
    @Override
    public void log(SessionLogEntry sessionLogEntry) {
        if (sessionLogEntry.getLevel()>=7){
         log.error("CONSULTA: "+sessionLogEntry.getMessage(),sessionLogEntry.getException());
        }
        else{
            log.debug("CONSULTA: "+sessionLogEntry.getMessage());
        }
            
        //System.out.println("CONSULTA: " + sessionLogEntry.getMessage()); // untranslated/undecoded message_id
    }
}