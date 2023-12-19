package com.comerzzia.jpos.persistencia.print.documentos;

import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DocumentosBean {
    
    public static final String FACTURA          = "FACTURA";
    public static final String NOTA_CREDITO     = "NOTA_CRED";
    public static final String COTIZACION       = "COTIZACION";
    public static final String GIFTCARD         = "GIFTCARD";
    public static final String GIFTCARD_P       = "GIFTCARD_P";
    public static final String CREDITO_ABONO    = "CRED_ABONO";
    public static final String LETRA_ABONO      = "LETR_ABONO";    
    public static final String ABONO            = "ABONO";
    public static final String RESERVACION      = "RESERVAC";
    public static final String PLAN_NOVIO       = "PLAN_NOVIO";
    public static final String ABONO_RESERVA    = "RE_ABONO";
    public static final String ABONO_PLAN_NOVIO = "PN_ABONO";
    public static final String LIQUIDACION_PN   = "PN_LIQUIDA";
    public static final String LIQUIDACION_RE   = "RE_LIQUIDA";
    public static final String BONO_RESERVA     = "RE_BONO";
    public static final String BONO             = "BONO";
    public static final String PENDIENTE_RESERVA= "PENDIENTE_RESERVA";
    
    //DAINOVY_CA
    public static final String CINTA_AUDITORA   = "C_AUDITORA";
    //DAINOVY_CA
    
    public static final String CODCAJA = "XXX";
    
    private String uidDocumento;

    private String tipo;

    private String codAlmacen;

    private String codCaja;

    private String idDocumento;

    private Fecha fecha;

    private String codCliente;

    private BigDecimal monto;

    private String estado;

    private String usuario;

    private String observaciones;
    
    private String referencia;
    
    private String numTransaccion;
    
    private Fecha fechaCaducidad;
    
    private String codCajaEmision;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    private List<DocumentosImpresosBean> impresos;
    
    private BigDecimal saldoGiftCard;
    
    private BigDecimal saldoUsado;
    
    private String bono;
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------


    public String getUidDocumento() {
        return uidDocumento;
    }

    public void setUidDocumento(String uidDocumento) {
        this.uidDocumento = uidDocumento == null ? null : uidDocumento.trim();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo == null ? null : tipo.trim();
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

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente == null ? null : codCliente.trim();
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado == null ? null : estado.trim();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario == null ? null : usuario.trim();
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones == null ? null : observaciones.trim();
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia == null ? null : referencia.trim();
    }

    public String getNumTransaccion() {
        return numTransaccion;
    }

    public void setNumTransaccion(String numTransaccion) {
        this.numTransaccion = numTransaccion == null ? null : numTransaccion.trim();
    }

    public Fecha getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Fecha fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getCodCajaEmision() {
        return codCajaEmision;
    }

    public void setCodCajaEmision(String codCajaEmision) {
        this.codCajaEmision = codCajaEmision;
    }
    
    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    
    public List<DocumentosImpresosBean> getImpresos() {
        return impresos;
    }

    public void setImpresos(List<DocumentosImpresosBean> impresos) {
        this.impresos = impresos;
    }
    
    public void addImpreso(DocumentosImpresosBean documentosImpresosBean){
        if(impresos == null){
            impresos = new ArrayList<DocumentosImpresosBean>();
        }
        impresos.add(documentosImpresosBean);
    }
    
     public BigDecimal getSaldoGiftCard() {
        return saldoGiftCard;
    }

    public void setSaldoGiftCard(BigDecimal saldoGiftCard) {
        this.saldoGiftCard = saldoGiftCard;
    }   
    
    public BigDecimal getSaldoUsado() {
        return saldoUsado;
    }

    public void setSaldoUsado(BigDecimal saldoUsado) {
        this.saldoUsado = saldoUsado;
    }
    
    public Boolean isBono() {
        if(bono == null){
            return false;
        }
        return bono.equals("S");
    }

    public void setBono(String bono) {
        this.bono = bono;
    }
    
    public String getBono() {
        return bono;
    }
    
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------

}