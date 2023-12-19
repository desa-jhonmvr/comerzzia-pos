/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.credito.plasticos;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.persistencia.credito.cuposvirtuales.CupoVirtualBean;
import es.mpsistemas.util.fechas.Fecha;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author amos
 */
public class PlasticoBean {
    private String numeroTarjeta;
    private Integer numeroCredito;
    private String cedulaCliente;
    private Cliente cliente;
    private boolean nuevo;
    private CupoVirtualBean cupo;
    private PlasticoEstadoBean estado;
    private Fecha fechaCaducidad;

    public PlasticoBean(){
        
    }
    
     public PlasticoBean(Integer numeroCredito){
        this.numeroCredito = numeroCredito;
    }
    
    public PlasticoBean(ResultSet rs) throws SQLException {
        numeroTarjeta = rs.getString("P_NUM");
        numeroCredito = rs.getInt("P_CREDITO");
        String nuevo = rs.getString("P_NUEVO");
        this.nuevo = !(nuevo != null && nuevo.equalsIgnoreCase("NO"));
        cedulaCliente = rs.getString("P_ID");
        fechaCaducidad = new Fecha(rs.getDate("P_FECHAVENCE"));
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }

    public Integer getNumeroCredito() {
        return numeroCredito;
    }

    public void setNumeroCredito(Integer numeroCredito) {
        this.numeroCredito = numeroCredito;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public CupoVirtualBean getCupo() {
        return cupo;
    }

    public void setCupo(CupoVirtualBean cupo) {
        this.cupo = cupo;
    }

    public PlasticoEstadoBean getEstado() {
        return estado;
    }

    public void setEstado(PlasticoEstadoBean estado) {
        this.estado = estado;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Fecha getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Fecha fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }
 
    public boolean isCaducada(){
        return new Fecha(new Date()).despues(fechaCaducidad);
    }
}
