package com.comerzzia.jpos.persistencia.giftcard;

import com.comerzzia.jpos.servicios.login.Sesion;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.mpsistemas.util.fechas.Fecha;

public class GiftCardBean {

    protected String idGiftCard;
    protected String codMedioPago;
    protected BigDecimal saldo;
    protected Fecha fechaCargaInicial;
    protected boolean procesado;
    protected Fecha fechaProceso;
    protected Long version;
    protected Fecha fechaVersion;
    protected String Observaciones;
    protected boolean anulado;
    
    public GiftCardBean() {
    }

    public GiftCardBean(ResultSet rs) throws SQLException {
        idGiftCard = rs.getString("ID_GIFTCARD");
        codMedioPago = rs.getString("CODMEDPAG");
        saldo = rs.getBigDecimal("SALDO");
        fechaCargaInicial = new Fecha(rs.getTimestamp("FECHA_CARGA_INICIAL"));
        procesado = rs.getString("PROCESADO").equals("S");
        fechaProceso = rs.getTimestamp("FECHA_PROCESADO") == null ? null : new Fecha(rs.getTimestamp("FECHA_PROCESADO"));
        fechaVersion = rs.getTimestamp("FECHA_VERSION") == null ? null : new Fecha(rs.getTimestamp("FECHA_VERSION"));
        version = rs.getLong("VERSION");
        anulado = rs.getString("ANULADO").equals("S");

    }

    public String getIdGiftCard() {
        return idGiftCard;
    }

    public void setIdGiftCard(String idGiftCard) {
        this.idGiftCard = idGiftCard;
    }

    public String getCodMedioPago() {
        return codMedioPago;
    }

    public void setCodMedioPago(String codMedioPago) {
        this.codMedioPago = codMedioPago;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Fecha getFechaCargaInicial() {
        return fechaCargaInicial;
    }

    public void setFechaCargaInicial(Fecha fechaCargaInicial) {
        this.fechaCargaInicial = fechaCargaInicial;
    }

    public boolean isProcesado() {
        return procesado;
    }

    public void setProcesado(boolean procesado) {
        this.procesado = procesado;
    }

    public String getProcesado() {
        return procesado ? "S" : "N";
    }

    public void setProcesado(String procesado) {
        this.procesado = procesado.equalsIgnoreCase("S");
    }

    public Fecha getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Fecha fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Fecha getFechaVersion() {
        return fechaVersion;
    }

    public void setFechaVersion(Fecha fechaVersion) {
        this.fechaVersion = fechaVersion;
    }
    
    public String getNumeroTarjeta(){
        String res ="";
        if (Sesion.isBebemundo()){
            res = "BM";
        }
        else{
            res = "SK";
        }
        res += codMedioPago;
        res += idGiftCard;
        
        return res;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String Observaciones) {
        this.Observaciones = Observaciones;
    }
    
    public boolean isAnulado() {
        return anulado;
    }

    public void setAnulado(boolean anulado) {
        this.anulado = anulado;
    }

    public String getAnulado() {
        return anulado ? "S" : "N";
    }

    public void setAnulado(String anulado) {
        this.anulado = anulado.equalsIgnoreCase("S");
    }
    
}
