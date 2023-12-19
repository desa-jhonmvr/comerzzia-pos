/**
 * CreditoWS_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.comerzzia.jpos.webservice.credito;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CreditoWS_PortType extends Remote {
    public VwBalance[] getEstado(String cuenta) throws RemoteException;
}
