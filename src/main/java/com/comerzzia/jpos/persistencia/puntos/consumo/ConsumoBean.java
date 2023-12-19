package com.comerzzia.jpos.persistencia.puntos.consumo;

import com.comerzzia.util.base.MantenimientoBean;
import java.util.Date;

public class ConsumoBean extends MantenimientoBean {
    /**
     * 
     */
    private static final long serialVersionUID = -3628315637004363585L;

	private String uidTicket;

    private String codCliente;

    private Integer puntos;

    private Date fecha;

    private Integer version;

    private Boolean procesado;

    private String codAlm;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------


    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket == null ? null : uidTicket.trim();
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente == null ? null : codCliente.trim();
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm == null ? null : codAlm.trim();
    }

	@Override
    protected void initNuevoBean() {
	    
    }
    
    //INICIO MÉTODOS PERSONALIZADOS--------------------------------------------
    
    //FIN MÉTODOS PERSONALIZADOS-----------------------------------------------

}