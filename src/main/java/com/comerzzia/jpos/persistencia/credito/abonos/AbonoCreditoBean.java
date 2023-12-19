package com.comerzzia.jpos.persistencia.credito.abonos;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;

public class AbonoCreditoBean {
    private String uidCreditoPago;

    private String codAlmacen;

    private String codCaja;

    private Integer identificador;

    private Integer numCredito;

    private String codCliente;

    private String codVendedor;

    private BigDecimal totalSinDto;

    private BigDecimal totalConDto;

    private String observaciones;

    private Boolean procesado;

    private Fecha fecha;

    private String numeroTarjeta;

    private byte[] pagos;
    
    private Boolean anulado;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------


    public String getUidCreditoPago() {
        return uidCreditoPago;
    }

    public void setUidCreditoPago(String uidCreditoPago) {
        this.uidCreditoPago = uidCreditoPago == null ? null : uidCreditoPago.trim();
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen == null ? null : codAlmacen.trim();
    }

    public String getCodCaja() {
        return codCaja;
    }

    public void setCodCaja(String codCaja) {
        this.codCaja = codCaja == null ? null : codCaja.trim();
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public Integer getNumCredito() {
        return numCredito;
    }

    public void setNumCredito(Integer numCredito) {
        this.numCredito = numCredito;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente == null ? null : codCliente.trim();
    }

    public String getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(String codVendedor) {
        this.codVendedor = codVendedor == null ? null : codVendedor.trim();
    }

    public BigDecimal getTotalSinDto() {
        return totalSinDto;
    }

    public void setTotalSinDto(BigDecimal totalSinDto) {
        this.totalSinDto = totalSinDto;
    }

    public BigDecimal getTotalConDto() {
        return totalConDto;
    }

    public void setTotalConDto(BigDecimal totalConDto) {
        this.totalConDto = totalConDto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones == null ? null : observaciones.trim();
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta == null ? null : numeroTarjeta.trim();
    }

    public byte[] getPagos() {
        return pagos;
    }

    public void setPagos(byte[] pagos) {
        this.pagos = pagos;
    }
    
    public Boolean getAnulado() {
        return anulado;
    }

    public void setAnulado(Boolean anulado) {
        this.anulado = anulado;
    }
    
    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    public String getIdAbonoCredito() {
        String a = new Long(identificador).toString();
        String b = new Long(identificador).toString();
        for (int i = a.length(); i < 8; i++) {
            b = "0" + b;
        }
        return codAlmacen + "-" + codCaja + "-" + b;
    }    
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------

}