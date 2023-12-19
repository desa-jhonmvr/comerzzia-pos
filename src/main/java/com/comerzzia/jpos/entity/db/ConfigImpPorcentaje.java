/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConfigImpPorcentaje  {

    public static final String COD_IMPUESTO_NORMAL  = "1";
    public static final String COD_IMPUESTO_EXTENTO = "0";
    
    private String codImpuesto;
    private BigDecimal porcentaje;
    private BigDecimal total;
    
    public ConfigImpPorcentaje(BigDecimal porcentaje, String codImpuesto) {
        total = BigDecimal.ZERO;
        this.porcentaje = porcentaje;
        this.codImpuesto = codImpuesto;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCodImpuesto() {
        return codImpuesto;
    }

    public void setCodImpuesto(String codImpuesto) {
        this.codImpuesto = codImpuesto;
    }

    public String getTotalString() {
        return Numero.redondear(total).toString();
    }
    
    public String getPorcentajeImpresion() {
        return porcentaje.toBigInteger().toString();
    }
    
    public BigDecimal getImpuestos() {
        return (total.multiply(porcentaje).divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP);
    }
    
}
