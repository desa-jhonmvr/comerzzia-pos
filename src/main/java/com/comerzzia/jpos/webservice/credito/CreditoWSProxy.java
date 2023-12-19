package com.comerzzia.jpos.webservice.credito;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

public class CreditoWSProxy implements CreditoWS_PortType {
  private String _endpoint = null;
  private CreditoWS_PortType creditoWS_PortType = null;
  
  public CreditoWSProxy() {
    _initCreditoWSProxy();
  }
  
  public CreditoWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initCreditoWSProxy();
  }
  
  private void _initCreditoWSProxy() {
    try {
      creditoWS_PortType = (new CreditoWS_ServiceLocator()).getCreditoWSPort();
      if (creditoWS_PortType != null) {
        if (_endpoint != null)
          ((Stub)creditoWS_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((Stub)creditoWS_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (creditoWS_PortType != null)
      ((Stub)creditoWS_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public CreditoWS_PortType getCreditoWS_PortType() {
    if (creditoWS_PortType == null)
      _initCreditoWSProxy();
    return creditoWS_PortType;
  }
  
  public VwBalance[] getEstado(java.lang.String cuenta) throws RemoteException{
    if (creditoWS_PortType == null)
      _initCreditoWSProxy();
    return creditoWS_PortType.getEstado(cuenta);
  }
  
  
}