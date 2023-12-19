/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.credito;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.persistencia.credito.plasticos.PlasticoBean;
import java.math.BigDecimal;

/**
 *
 * @author MGRI
 */
public class CreditoDirectoBean {
    
    private BigDecimal totalAPagar;
    private int fechaDeCorte;
    private PlasticoBean plastico;
    private BigDecimal aPagar;


    public BigDecimal getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(BigDecimal totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public int getFechaDeCorte() {
        return fechaDeCorte;
    }

    public void setFechaDeCorte(int fechaDeCorte) {
        this.fechaDeCorte = fechaDeCorte;
    }

    public BigDecimal getaPagar() {
        return aPagar;
    }

    public void setaPagar(BigDecimal aPagar) {
        this.aPagar = aPagar;
    }

    public PlasticoBean getPlastico() {
        return plastico;
    }

    public void setPlastico(PlasticoBean plastico) {
        this.plastico = plastico;
    }
    
}
