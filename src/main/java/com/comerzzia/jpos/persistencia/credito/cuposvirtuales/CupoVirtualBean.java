/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.credito.cuposvirtuales;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author amos
 */
public class CupoVirtualBean {
    private Integer numeroCredito;
    private BigDecimal cupo;
    private Fecha fecha;
    private BigDecimal consumo;
    
    public CupoVirtualBean(){
        
    }
    
    public CupoVirtualBean(ResultSet rs) throws SQLException{
        numeroCredito = rs.getInt("CREDITO");
        cupo = rs.getBigDecimal("CUPO");
        fecha = new Fecha(rs.getTimestamp("FECHA"));
    }

    public BigDecimal getCupo() {
        return cupo;
    }

    public void setCupo(BigDecimal cupo) {
        this.cupo = cupo;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public Integer getNumeroCredito() {
        return numeroCredito;
    }

    public void setNumeroCredito(Integer numeroCredito) {
        this.numeroCredito = numeroCredito;
    }

    public BigDecimal getConsumo() {
        return consumo;
    }

    public void setConsumo(BigDecimal consumo) {
        this.consumo = consumo;
    }
    
    
}
