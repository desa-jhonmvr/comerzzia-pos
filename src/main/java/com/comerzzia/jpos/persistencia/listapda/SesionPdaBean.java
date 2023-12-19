package com.comerzzia.jpos.persistencia.listapda;

import com.comerzzia.jpos.persistencia.listapda.detalle.DetalleSesionPdaBean;
import es.mpsistemas.util.fechas.Fecha;
import java.util.List;

public class SesionPdaBean {
    private String uidSesionPda;

    private String tipo;

    private Fecha fechaHora;

    private String codigo;

    private Boolean utilizado;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    private List<DetalleSesionPdaBean> detalleSesionPdaList;
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------

    public SesionPdaBean() {
    }

    public SesionPdaBean(String uidSesionPda) {
        this.uidSesionPda = uidSesionPda;
    }

    public SesionPdaBean(String uidSesionPda, String tipo, Fecha fechaHora, String codigo, boolean utilizado) {
        this.uidSesionPda = uidSesionPda;
        this.tipo = tipo;
        this.fechaHora = fechaHora;
        this.codigo = codigo;
        this.utilizado = utilizado;
    }
    
    public String getUidSesionPda() {
        return uidSesionPda;
    }

    public void setUidSesionPda(String uidSesionPda) {
        this.uidSesionPda = uidSesionPda == null ? null : uidSesionPda.trim();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo == null ? null : tipo.trim();
    }

    public Fecha getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Fecha fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo == null ? null : codigo.trim();
    }

    public Boolean getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(Boolean utilizado) {
        this.utilizado = utilizado;
    }
    
    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    
    public List<DetalleSesionPdaBean> getDetalleSesionPdaList() {
        return detalleSesionPdaList;
    }

    public void setDetalleSesionPdaList(List<DetalleSesionPdaBean> detalleSesionPdaList) {
        this.detalleSesionPdaList = detalleSesionPdaList;
    }    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidSesionPda != null ? uidSesionPda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SesionPdaBean)) {
            return false;
        }
        SesionPdaBean other = (SesionPdaBean) object;
        if ((this.uidSesionPda == null && other.uidSesionPda != null) || (this.uidSesionPda != null && !this.uidSesionPda.equals(other.uidSesionPda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.persistencia.listapda.SesionPda[ uidSesionPda=" + uidSesionPda + " ]";
    }    
    
    public boolean isTipoVenta(){
        return tipo.equals("V");
    }
    
    public boolean isTipoReserva(){
        return tipo.equals("R");
    }
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------

}