/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.login;

/**
 *
 * @author amos
 */
public class DatosDatabase {
    private String esquemaConfig; // esquema de base de datos con tablas de configuración
    private String esquemaEmpresa; // esquema de base de datos con tablas de la empresa
    private String urlConexion;
    private String driver;
    private String usuario;
    private String password;
    private String logger;
    private String loggerLevel;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getEsquemaConfig() {
        return esquemaConfig;
    }

    public void setEsquemaConfig(String esquemaConfig) {
        this.esquemaConfig = esquemaConfig;
    }

    public String getEsquemaEmpresa() {
        return esquemaEmpresa;
    }

    public void setEsquemaEmpresa(String esquemaEmpresa) {
        this.esquemaEmpresa = esquemaEmpresa;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlConexion() {
        return urlConexion;
    }

    public void setUrlConexion(String urlConexion) {
        this.urlConexion = urlConexion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(String loggerLevel) {
        this.loggerLevel = loggerLevel;
    }

    
}
