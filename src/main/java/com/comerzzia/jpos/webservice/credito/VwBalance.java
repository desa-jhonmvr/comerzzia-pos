/**
 * VwBalance.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.comerzzia.jpos.webservice.credito;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.namespace.QName;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class VwBalance  implements Serializable {
    private Long cicloFacturacion;

    private BigDecimal totalPagar;

    public VwBalance() {
    }

    public VwBalance(
           Long cicloFacturacion,
           BigDecimal totalPagar) {
           this.cicloFacturacion = cicloFacturacion;
           this.totalPagar = totalPagar;
    }


    /**
     * Gets the cicloFacturacion value for this VwBalance.
     * 
     * @return cicloFacturacion
     */
    public Long getCicloFacturacion() {
        return cicloFacturacion;
    }


    /**
     * Sets the cicloFacturacion value for this VwBalance.
     * 
     * @param cicloFacturacion
     */
    public void setCicloFacturacion(Long cicloFacturacion) {
        this.cicloFacturacion = cicloFacturacion;
    }


    /**
     * Gets the totalPagar value for this VwBalance.
     * 
     * @return totalPagar
     */
    public BigDecimal getTotalPagar() {
        return totalPagar;
    }


    /**
     * Sets the totalPagar value for this VwBalance.
     * 
     * @param totalPagar
     */
    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof VwBalance)) return false;
        VwBalance other = (VwBalance) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cicloFacturacion==null && other.getCicloFacturacion()==null) || 
             (this.cicloFacturacion!=null &&
              this.cicloFacturacion.equals(other.getCicloFacturacion()))) &&
            ((this.totalPagar==null && other.getTotalPagar()==null) || 
             (this.totalPagar!=null &&
              this.totalPagar.equals(other.getTotalPagar())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCicloFacturacion() != null) {
            _hashCode += getCicloFacturacion().hashCode();
        }
        if (getTotalPagar() != null) {
            _hashCode += getTotalPagar().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static TypeDesc typeDesc =
        new TypeDesc(VwBalance.class, true);

    static {
        typeDesc.setXmlType(new QName("http://webservice.creditows.credito.com/", "vwBalance"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("cicloFacturacion");
        elemField.setXmlName(new QName("", "cicloFacturacion"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("totalPagar");
        elemField.setXmlName(new QName("", "totalPagar"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType, 
           Class _javaType,  
           QName _xmlType) {
        return 
          new  BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType, 
           Class _javaType,  
           QName _xmlType) {
        return 
          new  BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
