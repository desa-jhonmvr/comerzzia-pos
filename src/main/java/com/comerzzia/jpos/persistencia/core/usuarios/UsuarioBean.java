/**
 * Copyright 2009-2011 RED.ES - Desarrollado por MP Sistemas
 * 
 * Licencia con arreglo a la EUPL, versión 1.1 o -en cuanto 
 * sean aprobadas por la comisión Europea- versiones 
 * posteriores de la EUPL (la "Licencia").
 * Solo podrá usarse esta obra si se respeta la Licencia.
 * 
 * http://ec.europa.eu/idabc/eupl.html
 * 
 * Salvo cuando lo exija la legislación aplicable o se acuerde
 * por escrito, el programa distribuido con arreglo a la
 * Licencia se distribuye "TAL CUAL",
 * SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, 
 * ni expresas ni implícitas.
 * Véase la Licencia en el idioma concreto que rige
 * los permisos y limitaciones que establece la Licencia.
 */
package com.comerzzia.jpos.persistencia.core.usuarios;

import com.comerzzia.util.base.MantenimientoBean;
import es.mpsistemas.util.criptografia.CriptoException;
import es.mpsistemas.util.criptografia.CriptoUtil;

import es.mpsistemas.util.fechas.Fecha;
import org.apache.log4j.Logger;

public class UsuarioBean extends MantenimientoBean {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2943295250151468696L;
    private Long idUsuario;
    private String usuario;
    private String desUsuario;
    private String clave = "";
    private String aplicacion;
    private boolean puedeCambiarAplicacion;
    private String claveCorta = "";
    private String claveAntigua1;
    private String claveAntigua2;
    private String claveCortaAntigua1;
    private String claveCortaAntigua2;
    private Fecha fechaPassword;
    private Fecha fechaPasswordCorta;
    private boolean claveCaducada;
    private boolean claveCortaCaducada;
    private Integer caducidad;
    private boolean primerAcceso;

    public UsuarioBean() {
    }

    
    public UsuarioBean(String usuario, String desUsuario) {
        this.usuario = usuario;
        this.desUsuario = desUsuario;
    }

    protected void initNuevoBean() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario.toUpperCase();
    }

    public String getDesUsuario() {
        return desUsuario;
    }

    public void setDesUsuario(String desUsuario) {
        this.desUsuario = desUsuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = (clave != null) ? clave : "";
    }

    public String claveCandidataValidaString(char[] password) throws CriptoException {
        String res = null;
        String passwordPlano = null;
        String passwordEnc = null;
        passwordPlano = new String(password);
        passwordEnc = CriptoUtil.cifrar(CriptoUtil.ALGORITMO_MD5, passwordPlano.getBytes());
        if (isClaveCandidataValida(passwordEnc)) {
            res = passwordEnc;
        }
        return res;
    }

    public String claveCortaCandidataValidaString(char[] password) throws CriptoException {
        String res = null;
        String passwordPlano = null;
        String passwordEnc = null;
        passwordPlano = new String(password);
        passwordEnc = CriptoUtil.cifrar(CriptoUtil.ALGORITMO_MD5, passwordPlano.getBytes());
        if (isClaveCortaCandidataValida(passwordEnc)) {
            res = passwordEnc;
        }
        return res;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public boolean isPuedeCambiarAplicacion() {
        return puedeCambiarAplicacion;
    }

    public void setPuedeCambiarAplicacion(boolean puedeCambiarAplicacion) {
        this.puedeCambiarAplicacion = puedeCambiarAplicacion;
    }

    public void setPuedeCambiarAplicacion(String puedeCambiarAplicacion) {
        this.puedeCambiarAplicacion = puedeCambiarAplicacion.equals(TRUE);
    }

    public String getPuedeCambiarAplicacion() {
        return (puedeCambiarAplicacion) ? TRUE : FALSE;
    }

    public String getClaveCorta() {
        return claveCorta;
    }

    public void setClaveCorta(String claveCorta) {
        this.claveCorta = claveCorta;
    }

    public String getClaveAntigua1() {
        return claveAntigua1;
    }

    public void setClaveAntigua1(String claveAntigua1) {
        this.claveAntigua1 = claveAntigua1;
    }

    public String getClaveAntigua2() {
        return claveAntigua2;
    }

    public void setClaveAntigua2(String claveAntigua2) {
        this.claveAntigua2 = claveAntigua2;
    }

    public String getClaveCortaAntigua1() {
        return claveCortaAntigua1;
    }

    public void setClaveCortaAntigua1(String claveCortaAntigua1) {
        this.claveCortaAntigua1 = claveCortaAntigua1;
    }

    public String getClaveCortaAntigua2() {
        return claveCortaAntigua2;
    }

    public void setClaveCortaAntigua2(String claveCortaAntigua2) {
        this.claveCortaAntigua2 = claveCortaAntigua2;
    }

    public Fecha getFechaPassword() {
        return fechaPassword;
    }

    public void setFechaPassword(Fecha fechaPassword) {
        this.fechaPassword = fechaPassword;
    }

    public Fecha getFechaPasswordCorta() {
        return fechaPasswordCorta;
    }

    public void setFechaPasswordCorta(Fecha fechaPasswordCorta) {
        this.fechaPasswordCorta = fechaPasswordCorta;
    }

    public void cambiarClave(String nuevaClave) {
        // establecemos las nuevas claves históricas
        claveAntigua2 = claveAntigua1;
        claveAntigua1 = clave;
        primerAcceso = false;

        // establecemos la fecha de password
        fechaPassword = new Fecha();

        // establecemos la nueva password
        clave = nuevaClave;

        // recalculamos la caducidad de las claves
        calcularCaducidadClaves();
        
    }

    public void cambiarClaveCorta(String nuevaClaveCorta) {
        // establecemos las nuevas claves históricas
        claveCortaAntigua2 = claveCortaAntigua1;
        claveCortaAntigua1 = claveCorta;
        primerAcceso = false;

        // establecemos la fecha de password
        fechaPasswordCorta = new Fecha();

        // establecemos la nueva password
        claveCorta = nuevaClaveCorta;
        
        // recalculamos la caducidad de las claves
        calcularCaducidadClaves();
    }

    public boolean isClaveCandidataValida(String clave) {
        // no puede ser igual a las dos últimas claves introducidas
        return !clave.equals(this.clave) && !clave.equals(claveAntigua1) && !clave.equals(claveAntigua2);
    }

    public boolean isClaveCortaCandidataValida(String clave) {
        // no puede ser igual a las dos últimas claves introducidas
        return !clave.equals(this.claveCorta) && !clave.equals(claveCortaAntigua1) && !clave.equals(claveCortaAntigua2);
    }

    public boolean isClaveCorrecta(String clave) throws CriptoException {
        String claveEnc = CriptoUtil.cifrar(CriptoUtil.ALGORITMO_MD5, clave.getBytes());
        return this.clave.equals(claveEnc);

    }

    public boolean isClaveCortaCorrecta(String claveCorta) throws CriptoException {
        try {
            String claveEnc = CriptoUtil.cifrar(CriptoUtil.ALGORITMO_MD5, claveCorta.getBytes());
            return this.claveCorta.equals(claveEnc);
        }
        catch (Exception e) {
            Logger.getLogger(this.getClass()).error("Error no controlado al comprobor clave corta.", e);
        }
        return false;
    }

    public boolean isClaveCaducada() {
        return claveCaducada;
    }

    public boolean isClaveCortaCaducada() {
        return claveCortaCaducada;
    }

    public String getMensajeClaveCaducada(){
        if (isPrimerAcceso()){
            return "Es su primer acceso al sistema. Debe cambiar las claves por defecto asignadas para continuar trabajando.";
        }
        if (isClaveCaducada() && isClaveCortaCaducada()){
            return "La clave y la clave corta han caducado. Debe cambiarlas para continuar trabajando.";
        }
        if (isClaveCaducada()){
            return "Su clave ha caducado. Debe cambiarla para continuar trabajando.";
        }
        if (isClaveCortaCaducada()){
            return "La clave corta ha caducado. Debe cambiarla para continuar trabajando.";
        }
        return null;
        
        
    }
    
    public void setCaducidad(Integer caducidad) {
        this.caducidad = caducidad;
        calcularCaducidadClaves();
    }
    
    private void calcularCaducidadClaves(){
        // a la fecha de hoy le restamos los dias de caducidad de la password del usuario
        Fecha hoyMenosCaducidad = new Fecha();
        hoyMenosCaducidad.sumaDias(-1 * caducidad);

        // si la caducidad es 0 la password nunca caduca
        // comprobamos si la fecha de password es mayor que hoy menos la caducidad  
        if (caducidad > 0 && fechaPassword.antesOrEquals(hoyMenosCaducidad)) {
            claveCaducada = true;
        }
        else {
            claveCaducada = false;
        }
        if (caducidad > 0 && fechaPasswordCorta.antesOrEquals(hoyMenosCaducidad)) {
            claveCortaCaducada = true;
        }
        else {
            claveCortaCaducada = false;
        }
        
    }
    
    public boolean isPrimerAcceso() {
    	return primerAcceso;
    }

    public void setPrimerAcceso(boolean primerAcceso) {
    	this.primerAcceso = primerAcceso;
    }
	
    public void setPrimerAcceso(String primerAcceso) {
    	this.primerAcceso = (primerAcceso != null && primerAcceso.equals("S"));
    }    
    
}
