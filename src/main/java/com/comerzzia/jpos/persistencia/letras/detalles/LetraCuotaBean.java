package com.comerzzia.jpos.persistencia.letras.detalles;

import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;

public class LetraCuotaBean extends LetraCuotaKey {

    public static final String ESTADO_PENDIENTE = "P";
    public static final String ESTADO_COBRADA = "C";
    public static final String ESTADO_ANULADA = "A" ;
    private BigDecimal valor; // con intereses
    private Fecha fechaVencimiento;
    private Fecha fechaCobro;
    private String estado;
    private BigDecimal mora; // porcentaje para el número días de mora
    private Boolean procesado;
    private byte[] pagos;
    private String codcajaAbono;
    private Long idAbono;
    private String observaciones;
    private boolean noPagarMora;
    private String usuarioAbono;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    private BigDecimal importeMora = BigDecimal.ZERO;
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Fecha getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Fecha fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Fecha getFechaCobro() {
        return fechaCobro;
    }

    public void setFechaCobro(Fecha fechaCobro) {
        this.fechaCobro = fechaCobro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado == null ? null : estado.trim();
    }

    public BigDecimal getMora() {
        if (mora != null) {
            return mora;
        }
        else {
            return BigDecimal.ZERO;
        }
    }

    public void setMora(BigDecimal mora) {
        this.mora = mora;
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    public byte[] getPagos() {
        return pagos;
    }

    public void setPagos(byte[] pagos) {
        this.pagos = pagos;
    }

    public String getCodcajaAbono() {
        return codcajaAbono;
    }

    public void setCodcajaAbono(String codcajaAbono) {
        this.codcajaAbono = codcajaAbono;
    }

    public Long getIdAbono() {
        return idAbono;
    }

    public void setIdAbono(Long idAbono) {
        this.idAbono = idAbono;
    }
    
    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    public boolean isPendienteCobro() {
        return estado.equals(ESTADO_PENDIENTE);
    }
    
    public boolean isCobrada() {
        return estado.equals(ESTADO_COBRADA);
    }

    public boolean isFechaVencida() {
        Fecha hoy = new Fecha();
        //Sukasa no tiene en cuenta un día después del vencimiento de la mora para todos los casos
        hoy.sumaDias(1);
        return hoy.despues(getFechaVencimiento()) && !hoy.equalsFecha(getFechaVencimiento());
    }

    public void calcultarImporteMora(BigDecimal interesMora) {
        Fecha hoy = new Fecha();
        int dias = hoy.diferenciaDias(getFechaVencimiento());
        //Sukasa no tiene en cuenta un día después del vencimiento de la mora para todos los casos
        dias--;
        if(dias>0) {
            mora = interesMora.multiply(new BigDecimal(dias)).divide(new BigDecimal(30), BigDecimal.ROUND_HALF_DOWN);
        }
        importeMora = Numero.porcentajeR(valor, getMora());
    }

    public void calcultarImporteMoraPagados() {
        importeMora = Numero.porcentajeR(valor, mora);
    }    

    public BigDecimal getTotal() {
        if(isNoPagarMora()){
            importeMora = BigDecimal.ZERO;
            mora = BigDecimal.ZERO;
        }
        return valor.add(importeMora);
    }
    
    public String getIdLetraCuota(String codAlmacen){
        String a = new Long(idAbono).toString();
        String b = new Long(idAbono).toString();
        for (int i = a.length(); i < 8; i++) {
            b = "0" + b;
        }
        return codAlmacen + "-" + codcajaAbono + "-" + b;        
    }

    public BigDecimal getImporteMora() {
        return importeMora;
    }
    
    public void setImporteMora(BigDecimal importeMora) {
        this.importeMora = importeMora;
    }
    
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public boolean isNoPagarMora() {
        return noPagarMora;
    }

    public void setNoPagarMora(boolean noPagarMora) {
        this.noPagarMora = noPagarMora;
    }   
    
    public String getUsuarioAbono() {
        return usuarioAbono;
    }

    public void setUsuarioAbono(String usuarioAbono) {
        this.usuarioAbono = usuarioAbono;
    }  
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------
}