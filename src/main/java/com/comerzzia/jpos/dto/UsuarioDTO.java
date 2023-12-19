/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto;

import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class UsuarioDTO implements Serializable {

    private static final long serialVersionUID = 1586448252856255865L;

    private String usuario;
    private String password;
    private Long lugId;
    private Long codEmpleado;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getLugId() {
        return lugId;
    }

    public void setLugId(Long lugId) {
        this.lugId = lugId;
    }

    public Long getCodEmpleado() {
        return codEmpleado;
    }

    public void setCodEmpleado(Long codEmpleado) {
        this.codEmpleado = codEmpleado;
    }

}
