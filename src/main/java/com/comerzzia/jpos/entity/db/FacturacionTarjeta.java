/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.promociones.TipoPromocionBean;
import com.comerzzia.jpos.pinpad.PinPad;
import com.comerzzia.jpos.pinpad.TiposDiferidos;
import com.comerzzia.jpos.pinpad.respuesta.RespuestaProcesoPagos;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.core.contadores.ServicioContadores;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PlanPagoCredito;
import com.comerzzia.jpos.servicios.tickets.ReferenciaTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionPagoTicket;
import com.comerzzia.util.cadenas.Cadena;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "d_facturacion_tarjetas_tbl")
@XmlRootElement
public class FacturacionTarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "tipo_autorizacion")
    private String tipoAutorizacion;
    @Column(name = "transaccion")
    private Integer transaccion;
    @Column(name = "longitud_tarjeta")
    private Integer longitudTarjeta;
    @Column(name = "valor") // valor de la tarjeta
    private String valor;
    @Column(name = "fecha_transaccion")
    private Integer fechaTransaccion;
    @Column(name = "hora_transaccion")
    private Integer horaTransaccion;
    @Column(name = "codigo_local")
    private Integer codigoLocal;
    @Column(name = "numero_caja")
    private Integer numeroCaja;
    @Column(name = "tipo_credito_diferido")
    private String tipoCreditoDiferido;
    @Column(name = "numero_meses")
    private Integer numeroMeses;
    @Column(name = "numero_autorizacion")
    private String numeroAutorizacion;
    @Column(name = "numero_auditoria")
    private Integer numeroAuditoria;
    @Column(name = "codigo_respuesta")
    private String codigoRespuesta;
    @Column(name = "terminal_id")
    private String terminalId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "iva")
    private BigDecimal iva;
    @Column(name = "lote")
    private String lote;
    @Column(name = "interes")
    private BigDecimal interes;
    @Column(name = "banco")
    private String banco;
    @Column(name = "emisor")
    private String emisor;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "mensaje_promocional")
    private String mensajePromocional;
    @Column(name = "valor_base_12")
    private String valorBase12;
    @Column(name = "valor_base_0")
    private String valorBase0;
    @Column(name = "valor_neto")
    private String valorNeto;
    @Column(name = "interes_propio")
    private BigDecimal interesPropio;
    @Column(name = "procesado")
    private Character procesado;
    @Column(name = "tipo_documento")
    private Character tipoDocumento;
    @Column(name = "fecha_proceso")
    @Temporal(TemporalType.DATE)
    private Date fechaProceso;
    @Column(name = "mensaje_proceso")
    private String mensajeProceso;
    @Column(name = "CODMEDPAG")
    private String codmedpag;
    @Column(name = "DESMEDPAG")
    private String desmedpag;
    @Column(name = "ID_MEDPAG_VEN")
    private Long idMedpagVen;
    @Column(name = "TIPO_TRANSACCION")
    private String tipoTransaccion;
    @Column(name = "ID_REFERENCIA")
    private String idReferencia;
    @Column(name = "CODALM_REFERENCIA")
    private String codalmReferencia;
    @Column(name = "ID_ABONO_REFERENCIA")
    private BigInteger idAbonoReferencia;
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @Column(name = "TRACKI")
    private String trackI;
    @Column(name = "TRACKII")
    private String trackII;
    @Column(name = "MESES_GRACIA")
    private Integer mesesGracia;
    @Column(name = "CUOTAS_GRATIS")
    private Integer cuotasGratis;
    @Column(name = "CODALM_INTERNO")
    private String codalmInterno;
    @Column(name = "TIPO_LECTURA_TARJETA")
    private String tipoLecturaTarjeta;
    @Column(name = "ESTATUS_TRANSACCION")
    private String estatusTransaccion;
    @Column(name = "TIPO_CONSUMO")
    private String tipoConsumo;
    @Column(name = "SECUENCIAL_TRANSACCION")
    private String secuencialTransaccion;
    @Column(name = "PREFACTURA")
    private String prefactura;
    @Column(name = "TARJETA_HABIENTE")
    private String tarjetaHabiente;
    @Column(name = "POS_MOVIL")
    private String posMovil;
    @Column(name = "TRAMA_ENVIO")
    private String tramaEnvio;
    @Column(name = "TRAMA_RESPUESTA	")
    private String tramaRespuesta;

    public FacturacionTarjeta() {
    }

    public FacturacionTarjeta(PagoCredito p) {
        SimpleDateFormat sDFF = new SimpleDateFormat("MMdd");
        SimpleDateFormat sDFH = new SimpleDateFormat("HHmmss");
        // Rellenamos campos
        MedioPagoBean mp = p.getMedioPagoActivo();
        PagoCredito pc = (PagoCredito) p;
        this.tipoAutorizacion = "1"; // Siempre es autorización 
        this.codalmInterno = Sesion.getTienda().getSriTienda().getCodalminterno();

        // Campos que no tienen relación con tramas de petición y/o respuesta
        this.valorBase12 = p.getSubtotalIva12().toPlainString();
        this.valorBase0 = p.getSubtotalIva0().toPlainString();
        this.valorNeto = p.getUstedPaga().toPlainString();
        this.interesPropio = pc.getPorcentajeInteres();
        this.codmedpag = p.getMedioPagoActivo().getCodMedioPago();
        this.desmedpag = p.getMedioPagoActivo().getDesMedioPago();
        this.idMedpagVen = p.getPlanSeleccionado().getVencimiento().getIdMedioPagoVencimiento();

        if(pc.getMensajePromocional()!=null && pc.getMensajePromocional().length()>=79){
            this.mensajePromocional=pc.getMensajePromocional().substring(0,79);
        }else{
            this.mensajePromocional=pc.getMensajePromocional();
        }

        obtenerDatosTarjeta(p);
        // Si hay objeto de respuesta, establecemos los valores cambiados por la respuesta
        if (p.getPinpadRespuesta() != null) {
            this.banco = p.getPinpadRespuesta().getNombreBancoAdquiriente();
            this.numeroAutorizacion = p.getPinpadRespuesta().getNumeroAutorizacion();

            ///Validacion PinPad para guardar numero de autorizacion en caso de ser espacios RD
            if (this.numeroAutorizacion == null || this.numeroAutorizacion.startsWith(" ") || this.numeroAutorizacion.equals("")) {
                if (p.getCodigoValidacionManual() != null && !p.getCodigoValidacionManual().equals("")) {
                    this.numeroAutorizacion = p.getCodigoValidacionManual();
                }
            }
            this.codigoRespuesta = p.getPinpadRespuesta().getCodigoRespuestaTransaccion(); // Es un parámetro de devuelve la respuesta
            this.lote = p.getPinpadRespuesta().getNumeroLote();                      // Nº de lote asignado por el emisor, si no, se pone 0s
            this.mensajePromocional = p.getPinpadRespuesta().getPublicidad(); // Parámetro que viene en la respuesta
            this.secuencialTransaccion = p.getPinpadRespuesta().getSecuencialTransaccion();
            String inter = p.getPinpadRespuesta().getFinanciamiento();
            if (inter != null && !inter.trim().isEmpty()) {
                inter = inter.substring(0, inter.length() - 2) + "." + inter.substring(inter.length() - 2);
                this.interes = new BigDecimal(inter);
            } else {
                this.interes = BigDecimal.ZERO;
            }

            if ("01".equals(p.getPinpadRespuesta().getModoLectura())) {
                this.tipoLecturaTarjeta = "000";
            } else if ("02".equals(p.getPinpadRespuesta().getModoLectura())) {
                this.tipoLecturaTarjeta = "090";
            } else if ("03".equals(p.getPinpadRespuesta().getModoLectura())) {
                this.tipoLecturaTarjeta = "005";
            }
            String nombreTarjtHabiente = p.getPinpadRespuesta().getNombreTarjetaHabiente();

            this.tarjetaHabiente = nombreTarjtHabiente != null ? nombreTarjtHabiente.trim() : nombreTarjtHabiente;

            asignarEmisor(p);
        } else {
            // hubo validación manual creamos un nuevo número de auditoría 
            //(para que, en caso de realizar una reversa por devolución no coincida con la reversa automática ya realizada.
            try {
                this.numeroAuditoria = ServicioContadores.obtenerContadorDefinitivo(ServicioContadores.CONTADOR_AUDITORIA).intValue();
                this.numeroAutorizacion = p.getNumeroAutorizacionTarjeta();
                //Validacion Para Insertar autorizacion Manual Rd
                this.numeroAutorizacion = p.getCodigoValidacionManual();
                asignarEmisor(p);
            } catch (ContadorException e) {
                // TODO: [MGR] Tratar excepción
            }
        }

        // Si no hay petición o respuesta establecemos el número de autorización
        if (numeroAutorizacion == null) {
            this.numeroAutorizacion = p.getNumeroAutorizacionTarjeta();
        }
        if (fechaTransaccion == null) {
            Calendar cal = Calendar.getInstance();
            this.fechaTransaccion = new Integer(sDFF.format(cal.getTime()));
            this.horaTransaccion = new Integer(sDFH.format(cal.getTime()));
        }
        if (terminalId == null) {
            this.terminalId = "00" + Numero.completaconCeros(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), 3) + Numero.completaconCeros(Sesion.getDatosConfiguracion().getCodcaja(), 3);
        }
        this.fechaRegistro = new Fecha().getDate();

        // Cuotas gratis y meses de gracia
        PromocionPagoTicket promoNCuotas = p.getTipoPromocionAplicada(TipoPromocionBean.TIPO_PROMOCION_N_CUOTAS_GRATIS);
        PromocionPagoTicket promoMesesGracia = p.getTipoPromocionAplicada(TipoPromocionBean.TIPO_PROMOCION_MESES_GRACIA);
        if (promoNCuotas != null) {
            this.cuotasGratis = promoNCuotas.getNumCuotasPromocion();
        }
        if (promoMesesGracia != null) {
            this.mesesGracia = promoMesesGracia.getNumCuotasPromocion();
        }
    }

    private void asignarEmisor(PagoCredito p) {
        if (!p.getMedioPagoActivo().isOtros()) {
            //if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
            //El emisor se grabará con un 1 si es Datafast y un 2 si es Mediante
            if (p.getPlanSeleccionado().getVencimiento().isCorriente()) {
                this.emisor = p.getMedioPagoActivo().getTipoPagoCorriente() == 'D' ? "1" : "2";
            } else {
                this.emisor = p.getMedioPagoActivo().getTipoPagoDiferido() == 'D' ? "1" : "2";
            }
            //}else{
            //    this.emisor ="1";
            //}
        } else {
            this.emisor = null;
        }
    }

    /**
     * Función que utilizamos en procesar medio de pago, para guardar la
     * información de facturación de una tarjeta que ha sido validada
     *
     * @param p
     * @param referencia
     */
    public FacturacionTarjeta(PagoCredito p, ReferenciaTicket referencia) {
        this(p);
        this.id = p.getUidFacturacion();
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
            p.setUidFacturacion(this.id);
        }
        this.idReferencia = referencia.getNumDocRefTarjeta();
        if (referencia.isPagoTarjetaAbonoPlanNovio()) {
            this.codalmReferencia = VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN);
        } // En otro caso null
        this.idAbonoReferencia = referencia.getIdAbonoRefTarjeta();
        this.tipoTransaccion = referencia.getTipoRefTarjeta();
        this.estatusTransaccion = "O";

        if (referencia.getNumTicket() != null) {
            String[] id_ticket = referencia.getNumTicket().split("-");
            if (id_ticket != null && id_ticket.length == 3) {
                this.trackI = id_ticket[2];
            }
        }
        if (p.isDatafast()) {
            this.tipoConsumo = p.getVencimiento().getTipoCredito();
        }
        if (p.isMedianet()) {
            this.tipoConsumo = TiposDiferidos.dameDiferido(p.getVencimiento().getTipoCredito());
        }
    }

    public FacturacionTarjeta(PagoCredito p, TicketS ticket, String tipoTransaccion) {
        this(p);
        // Generamos un uid
        this.id = UUID.randomUUID().toString();
        if (ticket != null) {
            this.trackI = String.valueOf(ticket.getId_ticket());
        }
        this.estatusTransaccion = tipoTransaccion;
        if (p.isDatafast()) {
            this.tipoConsumo = p.getVencimiento().getTipoCredito();
        }
        if (p.isMedianet()) {
            this.tipoConsumo = TiposDiferidos.dameDiferido(p.getVencimiento().getTipoCredito());
        }
    }

    public FacturacionTarjeta(PagoCredito p, String tipoTransaccion, String idReferencia, String numeroAutorizacion) {
        this(p);
        // Generamos un uid
        this.id = UUID.randomUUID().toString();

        this.estatusTransaccion = tipoTransaccion;
        this.idReferencia = idReferencia;
        this.numeroAutorizacion = numeroAutorizacion;
        if (p.isDatafast()) {
            this.tipoConsumo = p.getVencimiento().getTipoCredito();
        }
        if (p.isMedianet()) {
            this.tipoConsumo = TiposDiferidos.dameDiferido(p.getVencimiento().getTipoCredito());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRamdomId(String id) {
        this.id = UUID.randomUUID().toString();
    }

    public String getTipoAutorizacion() {
        return tipoAutorizacion;
    }

    public void setTipoAutorizacion(String tipoAutorizacion) {
        this.tipoAutorizacion = tipoAutorizacion;
    }

    public Integer getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Integer transaccion) {
        this.transaccion = transaccion;
    }

    public Integer getLongitudTarjeta() {
        return longitudTarjeta;
    }

    public void setLongitudTarjeta(Integer longitudTarjeta) {
        this.longitudTarjeta = longitudTarjeta;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Integer getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(Integer fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Integer getHoraTransaccion() {
        return horaTransaccion;
    }

    public void setHoraTransaccion(Integer horaTransaccion) {
        this.horaTransaccion = horaTransaccion;
    }

    public Integer getCodigoLocal() {
        return codigoLocal;
    }

    public void setCodigoLocal(Integer codigoLocal) {
        this.codigoLocal = codigoLocal;
    }

    public Integer getNumeroCaja() {
        return numeroCaja;
    }

    public void setNumeroCaja(Integer numeroCaja) {
        this.numeroCaja = numeroCaja;
    }

    public String getTipoCreditoDiferido() {
        return tipoCreditoDiferido;
    }

    public void setTipoCreditoDiferido(String tipoCreditoDiferido) {
        this.tipoCreditoDiferido = tipoCreditoDiferido;
    }

    public Integer getNumeroMeses() {
        return numeroMeses;
    }

    public void setNumeroMeses(Integer numeroMeses) {
        this.numeroMeses = numeroMeses;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Integer getNumeroAuditoria() {
        return numeroAuditoria;
    }

    public void setNumeroAuditoria(Integer numeroAuditoria) {
        this.numeroAuditoria = numeroAuditoria;
    }

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMensajePromocional() {
        return mensajePromocional;
    }

    public void setMensajePromocional(String mensajePromocional) {
        this.mensajePromocional = mensajePromocional;
    }

    public String getValorBase12() {
        return valorBase12;
    }

    public void setValorBase12(String valorBase12) {
        this.valorBase12 = valorBase12;
    }

    public String getValorBase0() {
        return valorBase0;
    }

    public void setValorBase0(String valorBase0) {
        this.valorBase0 = valorBase0;
    }

    public String getValorNeto() {
        return valorNeto;
    }

    public void setValorNeto(String valorNeto) {
        this.valorNeto = valorNeto;
    }

    public BigDecimal getInteresPropio() {
        return interesPropio;
    }

    public void setInteresPropio(BigDecimal interesPropio) {
        this.interesPropio = interesPropio;
    }

    public Character getProcesado() {
        return procesado;
    }

    public void setProcesado(Character procesado) {
        this.procesado = procesado;
    }

    public Date getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public String getMensajeProceso() {
        return mensajeProceso;
    }

    public void setMensajeProceso(String mensajeProceso) {
        this.mensajeProceso = mensajeProceso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FacturacionTarjeta)) {
            return false;
        }
        FacturacionTarjeta other = (FacturacionTarjeta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.FacturacionTarjeta[ id=" + id + " ]";
    }

    public Character getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Character tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getCodmedpag() {
        return codmedpag;
    }

    public void setCodmedpag(String codmedpag) {
        this.codmedpag = codmedpag;
    }

    public String getDesmedpag() {
        return desmedpag;
    }

    public void setDesmedpag(String desmedpag) {
        this.desmedpag = desmedpag;
    }

    public Long getIdMedpagVen() {
        return idMedpagVen;
    }

    public void setIdMedpagVen(Long idMedpagVen) {
        this.idMedpagVen = idMedpagVen;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(String idReferencia) {
        this.idReferencia = idReferencia;
    }

    public String getCodalmReferencia() {
        return codalmReferencia;
    }

    public void setCodalmReferencia(String codalmReferencia) {
        this.codalmReferencia = codalmReferencia;
    }

    public BigInteger getIdAbonoReferencia() {
        return idAbonoReferencia;
    }

    public void setIdAbonoReferencia(BigInteger idAbonoReferencia) {
        this.idAbonoReferencia = idAbonoReferencia;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTrackI() {
        return trackI;
    }

    public void setTrackI(String trackI) {
        this.trackI = trackI;
    }

    public String getTrackII() {
        return trackII;
    }

    public void setTrackII(String trackII) {
        this.trackII = trackII;
    }

    public Integer getMesesGracia() {
        return mesesGracia;
    }

    public void setMesesGracia(Integer mesesGracia) {
        this.mesesGracia = mesesGracia;
    }

    public Integer getCuotasGratis() {
        return cuotasGratis;
    }

    public void setCuotasGratis(Integer cuotasGratis) {
        this.cuotasGratis = cuotasGratis;
    }

    public String getPrefactura() {
        return prefactura;
    }

    public void setPrefactura(String prefactura) {
        this.prefactura = prefactura;
    }

    public String getTarjetaHabiente() {
        return tarjetaHabiente;
    }

    public void setTarjetaHabiente(String tarjetaHabiente) {
        this.tarjetaHabiente = tarjetaHabiente;
    }

    private void obtenerDatosTarjeta(PagoCredito p) {
        // Si tenemos un objeto de petición, recuperamos de el los valores para la facturación       
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sDFF = new SimpleDateFormat("MMdd");
        SimpleDateFormat sDFH = new SimpleDateFormat("HHmmss");
        this.fechaTransaccion = new Integer(sDFF.format(cal.getTime()));
        this.horaTransaccion = new Integer(sDFH.format(cal.getTime()));
        this.numeroAutorizacion = p.getCodigoValidacionManual();
        //TODO ANULACION        
        try {
            this.numeroAuditoria = ServicioContadores.obtenerContadorDefinitivo(ServicioContadores.CONTADOR_AUDITORIA).intValue();
        } catch (ContadorException ex) {
            //Tratar Excepcion
        }
        this.codigoRespuesta = "00";
        if (!p.getMedioPagoActivo().isOtros()) {
            this.terminalId = p.getTID();
        } else {
            this.terminalId = null;
        }
        this.lote = PinPad.getInstance().getiPinPad().getLote();
        this.codigoLocal = NumberUtils.createInteger(p.getCodEstablecimiento());                              // P
        this.numeroCaja = es.mpsistemas.util.numeros.Numero.getEntero(Sesion.getDatosConfiguracion().getCodcaja(), null);                               // P
        this.tipoCreditoDiferido = p.getPlanSeleccionado().getVencimiento().getTipoCredito();   // P - Integer
        this.numeroMeses = p.getPlanSeleccionado().getNumCuotas();
        if (p.getPlanSeleccionado().getVencimiento().isCorriente()) {
            this.numeroMeses = 0;
        }
        if (VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
            if (numeroMeses == null || numeroMeses.equals(0)) {
                numeroMeses = 0;
                this.tipoCreditoDiferido = "00";
            } else {
                this.tipoCreditoDiferido = "01";
            }
        }
        this.interes = p.getImporteInteres();                                                   // P
        this.total = p.getUstedPaga();                                                            // Valor (comprobar)
        this.banco = "";                                                                        // NO SE GUARDA SI VIENE
        this.iva = p.getIva();                                                       // P

        TarjetaCredito tarjeta = p.getTarjetaCredito();
        if (tarjeta.isLecturaBandaManual()) {
            this.valor = tarjeta.getNumero();
            if (this.valor != null) {
                this.longitudTarjeta = tarjeta.getNumero().length();
                this.valor = Cadena.completaconCeros(null, this.longitudTarjeta);
                if (this.valor.length() == 15) {
                    this.valor = this.valor + "0";
                }
                this.trackI = completaconBlancos(null, 37);
                this.trackI = completaconBlancos(null, 78);
            }
        } else { // BANDA LEÍDA AUTOMÁTICAMENTE
            if (tarjeta.getNumero() != null) {
                longitudTarjeta = tarjeta.getNumero().length();
            }
            this.trackII = tarjeta.getTrackII();
            this.trackI = completaconBlancos(tarjeta.getTrackI(), 78);
        }
        this.valor = tarjeta.getNumeroOculto().trim();
        if (this.valor.length() > 16) {
            this.valor = valor.substring(0, 16);
        }
    }

    protected String completaconBlancos(String valor, int longitudNecesaria) {
        if (valor == null) {
            valor = "";
        }
        while (valor.length() < longitudNecesaria) {
            valor = valor + " ";
        }
        return valor;
    }

    public String getCodalmInterno() {
        return codalmInterno;
    }

    public void setCodalmInterno(String codalmInterno) {
        this.codalmInterno = codalmInterno;
    }

    public String getTipoLecturaTarjeta() {
        return tipoLecturaTarjeta;
    }

    public void setTipoLecturaTarjeta(String tipoLecturaTarjeta) {
        this.tipoLecturaTarjeta = tipoLecturaTarjeta;
    }

    public String getEstatusTransaccion() {
        return estatusTransaccion;
    }

    public void setEstatusTransaccion(String estatusTransaccion) {
        this.estatusTransaccion = estatusTransaccion;
    }

    public String getTipoConsumo() {
        return tipoConsumo;
    }

    public void setTipoConsumo(String tipoConsumo) {
        this.tipoConsumo = tipoConsumo;
    }

    public String getSecuencialTransaccion() {
        return secuencialTransaccion;
    }

    public void setSecuencialTransaccion(String secuencialTransaccion) {
        this.secuencialTransaccion = secuencialTransaccion;
    }

    public String getPosMovil() {
        return posMovil;
    }

    public void setPosMovil(String posMovil) {
        this.posMovil = posMovil;
    }

    public String getTramaEnvio() {
        return tramaEnvio;
    }

    public void setTramaEnvio(String tramaEnvio) {
        this.tramaEnvio = tramaEnvio;
    }

    public String getTramaRespuesta() {
        return tramaRespuesta;
    }

    public void setTramaRespuesta(String tramaRespuesta) {
        this.tramaRespuesta = tramaRespuesta;
    }

    public PagoCredito convertirFacturacionAPago(FacturacionTarjeta facturacionTarjetaDevolucion, PagoCredito pagoXml) {
        if (pagoXml == null) {
            PagoCredito pc = new PagoCredito();
            pc.setSubtotalIva12(new BigDecimal(facturacionTarjetaDevolucion.getValorBase12()));
            pc.setSubtotalIva0(new BigDecimal(facturacionTarjetaDevolucion.getValorBase0()));
            pc.setIva(facturacionTarjetaDevolucion.getIva());
            pc.setUstedPaga(new BigDecimal(facturacionTarjetaDevolucion.getValorNeto()));

            pc.setMedioPagoActivo(MediosPago.getInstancia().getMedioPago(facturacionTarjetaDevolucion.getCodmedpag()));
            pc.setPlanSeleccionado(new PlanPagoCredito());
            pc.getPlanSeleccionado().setVencimiento(MediosPago.getInstancia().getVencimiento(facturacionTarjetaDevolucion.getIdMedpagVen()));
            pc.getPlanSeleccionado().setNumCuotas(BigDecimal.valueOf(facturacionTarjetaDevolucion.getNumeroMeses()));
            pc.setImporteInteres(BigDecimal.ZERO);
            pc.setPinpadRespuesta(new RespuestaProcesoPagos());
            pc.getPinpadRespuesta().setNumeroAutorizacion(facturacionTarjetaDevolucion.getNumeroAutorizacion());
            pc.getPinpadRespuesta().setSecuencialTransaccion(facturacionTarjetaDevolucion.getSecuencialTransaccion());
            pc.setTarjetaCredito(new TarjetaCredito(facturacionTarjetaDevolucion.getValor(), "", ""));
            pc.setValidadoAutomatico(true);
            return pc;
        } else {
            pagoXml.setSubtotalIva12(new BigDecimal(facturacionTarjetaDevolucion.getValorBase12()));
            pagoXml.setSubtotalIva0(new BigDecimal(facturacionTarjetaDevolucion.getValorBase0()));
            pagoXml.setUstedPaga(new BigDecimal(facturacionTarjetaDevolucion.getValorNeto()));
            pagoXml.setIva(facturacionTarjetaDevolucion.getIva());
            pagoXml.setPinpadRespuesta(new RespuestaProcesoPagos());
            pagoXml.getPinpadRespuesta().setNumeroAutorizacion(facturacionTarjetaDevolucion.getNumeroAutorizacion());
            pagoXml.getPinpadRespuesta().setSecuencialTransaccion(facturacionTarjetaDevolucion.getSecuencialTransaccion());
            pagoXml.setPlanSeleccionado(new PlanPagoCredito());
            pagoXml.getPlanSeleccionado().setNumCuotas(BigDecimal.valueOf(facturacionTarjetaDevolucion.getNumeroMeses()));
            pagoXml.setImporteInteres(BigDecimal.ZERO);
            pagoXml.getPlanSeleccionado().setVencimiento(MediosPago.getInstancia().getVencimiento(facturacionTarjetaDevolucion.getIdMedpagVen()));
            pagoXml.setTarjetaCredito(new TarjetaCredito(facturacionTarjetaDevolucion.getValor(), "", ""));
            pagoXml.setValidadoAutomatico(true);
            return pagoXml;
        }
    }
}
