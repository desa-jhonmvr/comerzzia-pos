package com.comerzzia.jpos.persistencia.letras;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaBean;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class LetraBean {
    private String uidLetra;

    private String uidTicket;

    private String codCliente;

    private String codAlmacen;

    private String codCaja;

    private BigDecimal total;

    private Short plazo;

    private BigDecimal intereses;

    private Fecha fecha;

    private Boolean procesado;

    private String estado;

    private Integer identificador;

    private Long idTicket;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    List<LetraCuotaBean> cuotas;
    private Cliente cliente;    
    private TicketOrigen ticketOrigen; // ticket origen que fue pagado con esta letra
    
    public static final String ESTADO_PENDIENTE     = "P";
    public static final String ESTADO_PENDIENTE_R   = "Q"; // retención aplicada en algún pago
    public static final String ESTADO_ANULADA       = "A";
    public static final String ESTADO_COMPLETA      = "C";
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------

    
    public String getUidLetra() {
        return uidLetra;
    }

    public void setUidLetra(String uidLetra) {
        this.uidLetra = uidLetra == null ? null : uidLetra.trim();
    }

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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Short getPlazo() {
        return plazo;
    }

    public void setPlazo(Short plazo) {
        this.plazo = plazo;
    }

    public BigDecimal getIntereses() {
        return intereses;
    }

    public void setIntereses(BigDecimal intereses) {
        this.intereses = intereses;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado == null ? null : estado.trim();
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }
    
    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    public BigDecimal getTotalConIntereses(){
        return Numero.masPorcentaje(getTotal(), intereses);
    }
    public BigDecimal getTotalConInteresesR(){
        return Numero.redondear(getTotalConIntereses());
    }
    public BigDecimal getImporteIntereses(){
        return getTotalConInteresesR().subtract(getTotal());
    }
    public BigDecimal getImporteCuotaParcial(){
        if (getPlazo() == null || getPlazo() < 1 || getTotal() == null){
            return BigDecimal.ZERO;
        }
        return getTotalConIntereses().divide(new BigDecimal(getPlazo()), 2, RoundingMode.HALF_DOWN);
        
    }
    public void addCuota(LetraCuotaBean cuota){
        if (cuotas == null){
            cuotas = new ArrayList<LetraCuotaBean> ();        
        }
        cuotas.add(cuota);
    }
    public List<LetraCuotaBean> getCuotas() {
        return cuotas;
    }

    public void setCuotas(List<LetraCuotaBean> cuotas) {
        this.cuotas = cuotas;
    }
    public void setPlazo(Integer meses) {
        setPlazo(meses.shortValue());
    }
    
    public boolean isCuotasPendientesCobro(){
        for (LetraCuotaBean letraCuotaBean : cuotas) {
            if (letraCuotaBean.isPendienteCobro()){
                return true;
            }
        }
        return false;
    }
    public LetraCuotaBean getProximaCuotaCobro(){
        for (LetraCuotaBean letraCuotaBean : cuotas) {
            if (letraCuotaBean.isPendienteCobro()){
                return letraCuotaBean;
            }
        }
        return null;
    }
    
    public LetraCuotaBean getUltimaCuotaCobrada(){
        LetraCuotaBean letraCuota = null;
        for (LetraCuotaBean letraCuotaBean : cuotas) {
            if (letraCuotaBean.isCobrada()){
                letraCuota = letraCuotaBean;
            }
        }
        return letraCuota;
    }
    
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void establecerInteresMora(BigDecimal interes){
        for (LetraCuotaBean cuota : cuotas) {
            if (cuota.isFechaVencida() && cuota.isPendienteCobro()){
                cuota.calcultarImporteMora(interes);
            } else {
                cuota.calcultarImporteMoraPagados();
            }
        }
    }
    
    public String getIdFactura() {
        String a = new Long(idTicket).toString();
        String b = new Long(idTicket).toString();
        for (int i = a.length(); i < 8; i++) {
            b = "0" + b;
        }
        return codAlmacen + "-" + codCaja + "-" + b;
    }    
    public TicketOrigen getTicketOrigen() {
        return ticketOrigen;
    }

    public void setTicketOrigen(TicketOrigen ticketOrigen) {
        this.ticketOrigen = ticketOrigen;
    }
    public boolean isRetencionAplicada(){
        return estado.equals(ESTADO_PENDIENTE_R);
    }
    
    
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------


}