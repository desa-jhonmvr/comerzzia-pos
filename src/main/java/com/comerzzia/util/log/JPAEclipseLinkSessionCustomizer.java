/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util.log;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.sessions.Session;

/**
 *
 * @author MGRI
 */
public class JPAEclipseLinkSessionCustomizer implements SessionCustomizer {
   
  protected boolean shouldPrintDate = true;
    
  public void customize(Session aSession) throws Exception {
 
  // create a custom logger
  SessionLog aCustomLogger = new CustomMPSessionLog();
  
  aCustomLogger.setLevel(3); // Logging level finest
  
  aSession.setSessionLog(aCustomLogger);
  }
}
