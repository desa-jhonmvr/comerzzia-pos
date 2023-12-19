package com.comerzzia.jpos.persistencia.logs.transaccioneserradas;


import com.comerzzia.util.base.MantenimientoBean;
import es.mpsistemas.util.fechas.Fecha;



public class TransaccionErradaBean extends MantenimientoBean {

    public static String TIPO_PROCESANDO = "Error procesando datos";
    public static String TIPO_GUARDANDO = "Error al salvar en bbdd";
    public static String TIPO_IMPRIMIENDO = "Error imprimiendo ticket";
    public static String TIPO_TRANSACCION_FACTURA = "Factura";
    public static String TIPO_TRANSACCION_NOTA_CREDITO = "Error imprimiendo ticket";
    public static String TIPO_TRANSACCION_GUIA_REMISION = "Error imprimiendo ticket";
    
    private static final long serialVersionUID = 1L;
	String codAlm;
	String codCaja;
	long idTransaccion;
	String tipotransaccion;
	String autorizador;
	String usaurio;
	Fecha fechaHora;
	String error;
	String procesado;
	long idUsuario;
	
	@Override
    protected void initNuevoBean() {
	    
    }

	
    public String getCodAlm() {
    	return codAlm;
    }

	
    public void setCodAlm(String codAlm) {
    	this.codAlm = codAlm;
    }

	
    public String getCodCaja() {
    	return codCaja;
    }

	
    public void setCodCaja(String codCaja) {
    	this.codCaja = codCaja;
    }

	
    public long getIdTransaccion() {
    	return idTransaccion;
    }

	
    public void setIdTransaccion(long idTransaccion) {
    	this.idTransaccion = idTransaccion;
    }

	
    public String getTipotransaccion() {
    	return tipotransaccion;
    }

	
    public void setTipotransaccion(String tipotransaccion) {
    	this.tipotransaccion = tipotransaccion;
    }

	
    public String getAutorizador() {
    	return autorizador;
    }

	
    public void setAutorizador(String autorizador) {
    	this.autorizador = autorizador;
    }

	
    public String getUsaurio() {
    	return usaurio;
    }

	
    public void setUsaurio(String usaurio) {
    	this.usaurio = usaurio;
    }

	
    public Fecha getFechaHora() {
    	return fechaHora;
    }

	
    public void setFechaHora(Fecha fechaHora) {
    	this.fechaHora = fechaHora;
    }

	
    public String getError() {
    	return error;
    }

	
    public void setError(String error) {
    	this.error = error;
    }

	
    public String getProcesado() {
    	return procesado;
    }

	
    public void setProcesado(String proceado) {
    	this.procesado = proceado;
    }

	
    public long getIdUsuario() {
    	return idUsuario;
    }

	
    public void setIdUsuario(long idUsuario) {
    	this.idUsuario = idUsuario;
    }
}