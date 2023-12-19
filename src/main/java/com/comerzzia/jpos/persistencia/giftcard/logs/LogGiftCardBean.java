package com.comerzzia.jpos.persistencia.giftcard.logs;

import java.util.UUID;


import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;

public class LogGiftCardBean {

    public static final String OPERACION_CARGA_INICIAL = "CARGA_INICIAL_POS";
    public static final String OPERACION_RECARGA       = "RECARGA_POS";
    public static final String OPERACION_CONSUMO       = "CONSUMO_POS";

    private String uidLog;
    private Fecha fechaHora;
    private String idGiftCard;
    private String usuario;
    private String usuarioAutorizador;
    private String codAlmacen;
    private String codCaja;
    private String codOperacion;
    private String codCliente;
    protected BigDecimal saldo;
    private BigDecimal abono;
    private byte[] pagos;
    private boolean procesado;
    private Long idCargaGiftCard;
    private boolean anulado;

    public LogGiftCardBean() {
        uidLog = UUID.randomUUID().toString();
        fechaHora = new Fecha();
        procesado = false;
        anulado = false;
    }

    public String getUidLog() {
        return uidLog;
    }

    public void setUidLog(String uidLog) {
        this.uidLog = uidLog;
    }

    public Fecha getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Fecha fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getIdGiftCard() {
        return idGiftCard;
    }

    public void setIdGiftCard(String idGiftCard) {
        this.idGiftCard = idGiftCard;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuarioAutorizador() {
        return usuarioAutorizador;
    }

    public void setUsuarioAutorizador(String usuarioAutorizador) {
        this.usuarioAutorizador = usuarioAutorizador;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getCodCaja() {
        return codCaja;
    }

    public void setCodCaja(String codCaja) {
        this.codCaja = codCaja;
    }

    public String getCodOperacion() {
        return codOperacion;
    }

    public void setCodOperacion(String codOperacion) {
        this.codOperacion = codOperacion;
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
    
    public BigDecimal getSaldo() {
    	return saldo;
    }

	
    public void setSaldo(BigDecimal saldo) {
    	this.saldo = saldo;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public BigDecimal getAbono() {
        return abono;
    }

    public void setAbono(BigDecimal abono) {
        this.abono = abono;
    }

    public byte[] getPagos() {
        return pagos;
    }

    public void setPagos(byte[] pagos) {
        this.pagos = pagos;
    }

    public Long getIdCargaGiftCard() {
        return idCargaGiftCard;
    }

    public void setIdCargaGiftCard(Long idCargaGiftCard) {
        this.idCargaGiftCard = idCargaGiftCard;
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
    
    public String getIdGiftCardAsString() {
        String a = new Long(idCargaGiftCard).toString();
        String b = new Long(idCargaGiftCard).toString();
        for (int i = a.length(); i < 8; i++) {
            b = "0" + b;
        }
        return codAlmacen + "-" + codCaja + "-" + b;
    }      
    
}
