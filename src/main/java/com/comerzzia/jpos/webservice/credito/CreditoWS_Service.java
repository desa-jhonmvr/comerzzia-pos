/**
 * CreditoWS_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.comerzzia.jpos.webservice.credito;

import java.net.URL;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface CreditoWS_Service extends Service {
    public String getCreditoWSPortAddress();

    public CreditoWS_PortType getCreditoWSPort() throws ServiceException;

    public CreditoWS_PortType getCreditoWSPort(URL portAddress) throws ServiceException;
}
