/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.clientes;

/**
 *
 * @author Administrador
 */
public class ParamBuscarClientes {
    private String codcli;
    private String nombre_com;
    private String descli;
    private String tipoDocumento;
    private String numTransaccion;
    
    public ParamBuscarClientes(){
        this.codcli="";
        this.nombre_com="";
        this.descli="";
        this.tipoDocumento="";
        this.numTransaccion="";
    }

    public String getDescli() {
        return descli;
    }

    public void setDescli(String descli) {
        this.descli = descli;
    }

    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(String codcli) {
        this.codcli = codcli;
    }

    public String getNombre_com() {
        return nombre_com;
    }

    public void setNombre_com(String nombre_com) {
        this.nombre_com = nombre_com;
    }

    public String getNumTransaccion() {
        return numTransaccion;
    }

    public void setNumTransaccion(String numTransaccion) {
        this.numTransaccion = numTransaccion;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    
}
