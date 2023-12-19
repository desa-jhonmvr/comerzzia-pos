/**
 * CreditoWS_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.comerzzia.jpos.webservice.credito;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

public class CreditoWS_ServiceLocator extends Service implements CreditoWS_Service {

    public CreditoWS_ServiceLocator() {
    }


    public CreditoWS_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CreditoWS_ServiceLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CreditoWSPort
    private String CreditoWSPort_address = "http://localhost:8080/CreditoWS/CreditoWS";

    public String getCreditoWSPortAddress() {
        return CreditoWSPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String CreditoWSPortWSDDServiceName = "CreditoWSPort";

    public String getCreditoWSPortWSDDServiceName() {
        return CreditoWSPortWSDDServiceName;
    }

    public void setCreditoWSPortWSDDServiceName(String name) {
        CreditoWSPortWSDDServiceName = name;
    }

    public CreditoWS_PortType getCreditoWSPort() throws ServiceException {
       URL endpoint;
        try {
            endpoint = new URL(CreditoWSPort_address);
        }
        catch (MalformedURLException e) {
            throw new ServiceException(e);
        }
        return getCreditoWSPort(endpoint);
    }

    public CreditoWS_PortType getCreditoWSPort(URL portAddress) throws ServiceException {
        try {
            CreditoWSSoapBindingStub _stub = new CreditoWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getCreditoWSPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCreditoWSPortEndpointAddress(String address) {
        CreditoWSPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (CreditoWS_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                CreditoWSSoapBindingStub _stub = new CreditoWSSoapBindingStub(new URL(CreditoWSPort_address), this);
                _stub.setPortName(getCreditoWSPortWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new ServiceException(t);
        }
        throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(QName portName, Class serviceEndpointInterface) throws ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("CreditoWSPort".equals(inputPortName)) {
            return getCreditoWSPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public QName getServiceName() {
        return new QName("http://webservice.creditows.credito.com/", "CreditoWS");
    }

    private HashSet ports = null;

    public Iterator getPorts() {
        if (ports == null) {
            ports = new HashSet();
            ports.add(new QName("http://webservice.creditows.credito.com/", "CreditoWSPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws ServiceException {
        
if ("CreditoWSPort".equals(portName)) {
            setCreditoWSPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(QName portName, String address) throws ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
