/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.login;

import com.comerzzia.jpos.servicios.general.ciudades.Ciudades;
import com.comerzzia.jpos.servicios.general.operadoresmoviles.OperadoresMoviles;
import java.math.BigDecimal;

/**
 *
 * @author MGRI
 */
public class DatosConfiguracion {

    public static DatosDatabase getDatosDatabaseCentral() {
        return datosDatabaseCentral;
    }

    public static void setDatosDatabaseCentral(DatosDatabase aDatosDatabaseCentral) {
        datosDatabaseCentral = aDatosDatabaseCentral;
    }

    public static String VALIDACION_EN_FICHERO = "FICHERO";
    public static String VALIDACION_EN_TABLA = "TABLA";
    public static String VALIDACION_MANUAL = "MANUAL";
    public static String VALIDACION_AUTOMATICA = "AUTOMATICA";

    private String modoReserva;
    private OperadoresMoviles operadoresMoviles;
    private Ciudades ciudades;
    private String tipoImpresionTicket; // tipo de impresion PANTALLA o IMPRESORA
    private String cadenaImpresoraTicket; //cadena de impresión del ticket. Indica que impresora usar
    private String modoDesarrollo; // SI o NO 
    private String esquemaFactura; // fichero xml donde esta definido el esquema del recibo
    private String esquemaReciboDevolucion;
    private String usuarioModoDesarrollo;
    private String passwordModoDesarrollo;
    private BigDecimal limiteRetiro;
    private String esquemaReservacion;
    private String retencionNumeroComprobante;
    private String retencionBaseRetencion;
    private String retencionImpuestoRetenido;
    private int velocidadTickets;
    private int bitstTickets;
    private int stopTickets;
    private int paridadTickets;
    private String cadenaImpresoraAdicional;
    private int velocidadTicketAdicional;
    private int bitstTicketAdicional;
    private int stopTicketAdicional;
    private int paridadTicketAdicional;
    private String esquemaPruebaCodigoBarras;
    private String esquemaComprobanteReservacion;
    private String esquemaComprobanteRetiro;
    private String esquemaComprobanteBono;
    private String esquemaCotizacion;
    private String esquemaListadoArticulosReserva;
    private String esquemaComprobanteGiftCard;
    private String esquemaComprobanteVoucher;
    private String esquemaComprobanteVoucherAnulacion;
    private String esquemaListadoArticulosPlan;
    private String esquemaContratoPlan;
    private String esquemaExtensionGarantia;
    private String esquemaComprobantePagoCreditoDirecto;
    private String esquemaComprobantePagoLetra;
    private String esquemaComprobanteListaCancelacionesPendientes;
    private String esquemaComprobanteAnulacion;
    private String esquemaChequeAnverso;
    private String esquemaChequeReverso;
    private String esquemaFacturaPendienteDespacho;
    private String esquemaCreditoDirecto; //Para la impresión adicional en ventas con crédito directo
    private String esquemaComprobanteVoucherPinPad;

    private String esquemaCupon;

    private Integer tiempoBloqueo;
    private BigDecimal maximoPagos;

    private String rutaValidacionLectura;
    private String rutaValidacionEscritura;

    private String codcaja;

    private String planNoviosExcelInvitados;

    private String tipoModoPinPad;

    private String impresoraTermica;
    private String rutaArchivosImpresoraTermica;

    private static DatosDatabase datosDatabaseCentral = new DatosDatabase();

    private Long timePinPad = new Long(2000);
    //PinPad D
    private String PINPAD_IP = "";
    private String PINPAD_PUERTO = "";
    private String PINPAD_IPHOSTDATAFASTR1 = "";
    private String PINPAD_PUERTODATAFASTCPR1 = "";
    private String PINPAD_IPHOSTDATAFASTR1ALTERNA = "";
    private String PINPAD_PUERTODATAFASTCPR1ALTERNO = "";
    private String PINPAD_IPHOSTMEDIANETR2 = "";
    private String PINPAD_PUERTOMEDIANETCPR1 = "";
    private String PINPAD_IPHOSTMEDIANETR1ALTERNA = "";
    private String PINPAD_PUERTOMEDIANETCPR1ALTERNO = "";
    ////Datos configuracion SMS
    private String DATABASE_SMS_USUARIO;
    private String DATABASE_SMS_PASSWORD;
    private String DATABASE_SMS_URL;
    //Datos para Tabla De Amortizacion
    private String[] DATOS_TABLA_AMORTIZACION;

    private String DATABASE_CORREO_USUARIO;
    private String DATABASE_CORREO_PASSWORD;
    private String DATABASE_CORREO_HOST;
    private String DATABASE_CORREO_CORREO;
    private String DATABASE_CORREO_ASUNTO;

    private String usuarioVentaOnline;
    private String passwordVentaOnline;
    private String ventaEnLinea;

    public DatosConfiguracion() {
        this.maximoPagos = new BigDecimal(100000);
    }

    public String getEsquemaAbono() {
        return esquemaComprobanteReservacion;
    }

    public String getModoReserva() {
        return modoReserva;
    }

    public void setModoReserva(String modoReserva) {
        this.modoReserva = modoReserva;
    }

    public void setEsquemaComprobanteReservacion(String esquemaComprobanteReservacion) {
        this.esquemaComprobanteReservacion = esquemaComprobanteReservacion;
    }

    public String getEsquemaPruebaCodigoBarras() {
        return esquemaPruebaCodigoBarras;
    }

    public void setEsquemaPruebaCodigoBarras(String esquemaPruebaCodigoBarras) {
        this.esquemaPruebaCodigoBarras = esquemaPruebaCodigoBarras;
    }

    public int getBitstTicketAdicional() {
        return bitstTicketAdicional;
    }

    public void setBitstTicketAdicional(int bitstTicketAdicional) {
        this.bitstTicketAdicional = bitstTicketAdicional;
    }

    public String getCadenaImpresoraAdicional() {
        return cadenaImpresoraAdicional;
    }

    public void setCadenaImpresoraAdicional(String cadenaImpresoraAdicional) {
        this.cadenaImpresoraAdicional = cadenaImpresoraAdicional;
    }

    public int getParidadTicketAdicional() {
        return paridadTicketAdicional;
    }

    public void setParidadTicketAdicional(int paridadTicketAdicional) {
        this.paridadTicketAdicional = paridadTicketAdicional;
    }

    public int getStopTicketAdicional() {
        return stopTicketAdicional;
    }

    public void setStopTicketAdicional(int stopTicketAdicional) {
        this.stopTicketAdicional = stopTicketAdicional;
    }

    public int getVelocidadTicketAdicional() {
        return velocidadTicketAdicional;
    }

    public void setVelocidadTicketAdicional(int velocidadTicketAdicional) {
        this.velocidadTicketAdicional = velocidadTicketAdicional;
    }

    public int getBitstTickets() {
        return bitstTickets;
    }

    public void setBitstTickets(int bitstTickets) {
        this.bitstTickets = bitstTickets;
    }

    public int getParidadTickets() {
        return paridadTickets;
    }

    public void setParidadTickets(int paridadTickets) {
        this.paridadTickets = paridadTickets;
    }

    public int getStopTickets() {
        return stopTickets;
    }

    public void setStopTickets(int stopTickets) {
        this.stopTickets = stopTickets;
    }

    public int getVelocidadTickets() {
        return velocidadTickets;
    }

    public void setVelocidadTickets(int velocidadTickets) {
        this.velocidadTickets = velocidadTickets;
    }

    public String getRetencionBaseRetencion() {
        return retencionBaseRetencion;
    }

    public void setRetencionBaseRetencion(String retencionBaseRetencion) {
        this.retencionBaseRetencion = retencionBaseRetencion;
    }

    public String getRetencionImpuestoRetenido() {
        return retencionImpuestoRetenido;
    }

    public void setRetencionImpuestoRetenido(String retencionImpuestoRetenido) {
        this.retencionImpuestoRetenido = retencionImpuestoRetenido;
    }

    public String getRetencionNumeroComprobante() {
        return retencionNumeroComprobante;
    }

    public void setRetencionNumeroComprobante(String retencionNumeroComprobante) {
        this.retencionNumeroComprobante = retencionNumeroComprobante;
    }

    public String getTipoImpresionTicket() {
        return tipoImpresionTicket;
    }

    public void setTipoImpresionTicket(String tipoImpresionTicket) {
        this.tipoImpresionTicket = tipoImpresionTicket;
    }

    public String getCadenaImpresoraTicket() {
        return cadenaImpresoraTicket;
    }

    public void setCadenaImpresoraTicket(String cadenaImpresoraTicket) {
        this.cadenaImpresoraTicket = cadenaImpresoraTicket;
    }

    public boolean isModoDesarrollo() {
        return modoDesarrollo != null && modoDesarrollo.equalsIgnoreCase("SI");
    }

    public void setModoDesarrollo(String modoDesarrollo) {
        this.modoDesarrollo = modoDesarrollo;
    }

    public String getEsquemaFactura() {
        return esquemaFactura;
    }

    public void setEsquemaFactura(String esquemaRecibo) {
        this.esquemaFactura = esquemaRecibo;
    }

    public Ciudades getCiudades() {
        return ciudades;
    }

    public void setCiudades(Ciudades ciudades) {
        this.ciudades = ciudades;
    }

    public String getPasswordModoDesarrollo() {
        return passwordModoDesarrollo;
    }

    public void setPasswordModoDesarrollo(String passwordModoDesarrollo) {
        this.passwordModoDesarrollo = passwordModoDesarrollo;
    }

    public String getUsuarioModoDesarrollo() {
        return usuarioModoDesarrollo;
    }

    public void setUsuarioModoDesarrollo(String usuarioModoDesarrollo) {
        this.usuarioModoDesarrollo = usuarioModoDesarrollo;
    }

    public BigDecimal getLimiteRetiro() {
        return limiteRetiro;
    }

    public void setLimiteRetiro(BigDecimal limiteRetiro) {
        this.limiteRetiro = limiteRetiro;
    }

    public OperadoresMoviles getOperadoresMoviles() {
        return operadoresMoviles;
    }

    public void setOperadoresMoviles(OperadoresMoviles operadoresMoviles) {
        this.operadoresMoviles = operadoresMoviles;
    }

    public String getEsquemaReciboDevolucion() {
        return esquemaReciboDevolucion;
    }

    public void setEsquemaReciboDevolucion(String esquemaReciboDevolucion) {
        this.esquemaReciboDevolucion = esquemaReciboDevolucion;
    }

    public String getEsquemaComprobanteRetiro() {
        return esquemaComprobanteRetiro;
    }

    public void setEsquemaComprobanteRetiro(String esquemaComprobanteRetiro) {
        this.esquemaComprobanteRetiro = esquemaComprobanteRetiro;
    }

    public String getEsquemaReservacion() {
        return esquemaReservacion;
    }

    public void setEsquemaReservacion(String esquemaReservacion) {
        this.esquemaReservacion = esquemaReservacion;
    }

    public String getEsquemaComprobanteBono() {
        return esquemaComprobanteBono;
    }

    public void setEsquemaComprobanteBono(String esquemaComprobanteBono) {
        this.esquemaComprobanteBono = esquemaComprobanteBono;
    }

    public Integer getTiempoBloqueo() {
        return tiempoBloqueo;
    }

    public void setTiempoBloqueo(Integer tiempoBloqueo) {
        this.tiempoBloqueo = tiempoBloqueo;
    }

    public boolean isModoReserva() {
        if (this.modoReserva != null && this.modoReserva.equals("si")) {
            return true;
        }
        return false;
    }

    public String getEsquemaCupon() {
        return esquemaCupon;
    }

    public void setEsquemaCupon(String esquemaCupon) {
        this.esquemaCupon = esquemaCupon;
    }

    public BigDecimal getMaximoPagos() {
        return maximoPagos;
    }

    public void setMaximoPagos(BigDecimal maximoPagos) {
        this.maximoPagos = maximoPagos;
    }

    public String getEsquemaComprobanteGiftCard() {
        return esquemaComprobanteGiftCard;
    }

    public void setEsquemaComprobanteGiftCard(String esquemaComprobanteGiftCard) {
        this.esquemaComprobanteGiftCard = esquemaComprobanteGiftCard;
    }

    public String getRutaValidacionLectura() {
        return rutaValidacionLectura;
    }

    public void setRutaValidacionLectura(String rutaValidacionEntrada) {
        this.rutaValidacionLectura = rutaValidacionEntrada;
    }

    public String getRutaValidacionEscritura() {
        return rutaValidacionEscritura;
    }

    public void setRutaValidacionEscritura(String rutaValidacionSalida) {
        this.rutaValidacionEscritura = rutaValidacionSalida;
    }

    public void setEsquemaComprobanteVoucher(String comprobante) {
        this.esquemaComprobanteVoucher = comprobante;
    }

    public String getEsquemaComprobanteVoucher() {
        return esquemaComprobanteVoucher;
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja;
    }

    //Devuelve codCaja en cifra de tres digitos, que es como almacena la trama de cancelación el sistema Switch
    public String codCajaSK() {
        String res = codcaja;
        while (res.length() < 3) {
            res = "0" + res;
        }
        return res;
    }

    public String getEsquemaListadoArticulosPlan() {
        return this.esquemaListadoArticulosPlan;
    }

    public void setEsquemaListadoArticulosPlan(String esquemaListadoArticulosPlan) {
        this.esquemaListadoArticulosPlan = esquemaListadoArticulosPlan;
    }

    public String getPlanNoviosExcelInvitados() {
        return planNoviosExcelInvitados;
    }

    public void setPlanNoviosExcelInvitados(String planNoviosExcelInvitados) {
        this.planNoviosExcelInvitados = planNoviosExcelInvitados;
    }

    public String getEsquemaContratoPlan() {
        return this.esquemaContratoPlan;
    }

    public void setEsquemaContratoPlan(String esquemaContratoPlan) {
        this.esquemaContratoPlan = esquemaContratoPlan;
    }

    public String getEsquemaExtensionGarantia() {
        return this.esquemaExtensionGarantia;
    }

    public void setEsquemaExtensionGarantia(String esquemaExtensionGarantia) {
        this.esquemaExtensionGarantia = esquemaExtensionGarantia;
    }

    public String getEsquemaComprobantePagoCreditoDirecto() {
        return this.esquemaComprobantePagoCreditoDirecto;
    }

    public void setEsquemaComprobantePagoCreditoDirecto(String esquemaComprobantePagoCreditoDirecto) {
        this.esquemaComprobantePagoCreditoDirecto = esquemaComprobantePagoCreditoDirecto;
    }

    public String getEsquemaComprobantePagoLetra() {
        return this.esquemaComprobantePagoLetra;
    }

    public void setEsquemaComprobantePagoLetra(String esquemaComprobantePagoLetra) {
        this.esquemaComprobantePagoLetra = esquemaComprobantePagoLetra;
    }

    public String getEsquemaListadoArticulosReserva() {
        return esquemaListadoArticulosReserva;
    }

    public void setEsquemaListadoArticulosReserva(String esquemaListadoArticulosReserva) {
        this.esquemaListadoArticulosReserva = esquemaListadoArticulosReserva;
    }

    public String getEsquemaComprobanteListaCancelacionesPendientes() {
        return esquemaComprobanteListaCancelacionesPendientes;
    }

    public void setEsquemaComprobanteListaCancelacionesPendientes(String esquemaComprobanteListaCancelacionesPendientes) {
        this.esquemaComprobanteListaCancelacionesPendientes = esquemaComprobanteListaCancelacionesPendientes;
    }

    public String getEsquemaComprobanteAnulacion() {
        return esquemaComprobanteAnulacion;
    }

    public void setEsquemaComprobanteAnulacion(String esquemaComprobanteAnulacion) {
        this.esquemaComprobanteAnulacion = esquemaComprobanteAnulacion;
    }

    public String getEsquemaCotizacion() {
        return esquemaCotizacion;
    }

    public void setEsquemaCotizacion(String esquemaCotizacion) {
        this.esquemaCotizacion = esquemaCotizacion;
    }

    public String getEsquemaChequeAnverso() {
        return esquemaChequeAnverso;
    }

    public void setEsquemaChequeAnverso(String esquemaChequeAnverso) {
        this.esquemaChequeAnverso = esquemaChequeAnverso;
    }

    public String getEsquemaChequeReverso() {
        return esquemaChequeReverso;
    }

    public void setEsquemaChequeReverso(String esquemaChequeReverso) {
        this.esquemaChequeReverso = esquemaChequeReverso;
    }

    public String getEsquemaFacturaPendienteDespacho() {
        return esquemaFacturaPendienteDespacho;
    }

    public void setEsquemaFacturaPendienteDespacho(String esquemaFacturaPendienteDespacho) {
        this.esquemaFacturaPendienteDespacho = esquemaFacturaPendienteDespacho;
    }

    public String getTipoModoPinPad() {
        return tipoModoPinPad;
    }

    public void setTipoModoPinPad(String tipoModoPinPad) {
        this.tipoModoPinPad = tipoModoPinPad;
    }

    public String getEsquemaComprobanteVoucherAnulacion() {
        return esquemaComprobanteVoucherAnulacion;
    }

    public void setEsquemaComprobanteVoucherAnulacion(String esquemaComprobanteVoucherAnulacion) {
        this.esquemaComprobanteVoucherAnulacion = esquemaComprobanteVoucherAnulacion;
    }

    public String getEsquemaComprobanteVoucherPinPad() {
        return esquemaComprobanteVoucherPinPad;
    }

    public void setEsquemaComprobanteVoucherPinPad(String esquemaComprobanteVoucherPinPad) {
        this.esquemaComprobanteVoucherPinPad = esquemaComprobanteVoucherPinPad;
    }

    public String getEsquemaCreditoDirecto() {
        return esquemaCreditoDirecto;
    }

    public void setEsquemaCreditoDirecto(String esquemaCreditoDirecto) {
        this.esquemaCreditoDirecto = esquemaCreditoDirecto;
    }

    public Long getTimePinPad() {
        return timePinPad;
    }

    public void setTimePinPad(Long timePinPad) {
        this.timePinPad = timePinPad;
    }

    public String getDATABASE_SMS_USUARIO() {
        return DATABASE_SMS_USUARIO;
    }

    public void setDATABASE_SMS_USUARIO(String DATABASE_SMS_USUARIO) {
        this.DATABASE_SMS_USUARIO = DATABASE_SMS_USUARIO;
    }

    public String getDATABASE_SMS_PASSWORD() {
        return DATABASE_SMS_PASSWORD;
    }

    public void setDATABASE_SMS_PASSWORD(String DATABASE_SMS_PASSWORD) {
        this.DATABASE_SMS_PASSWORD = DATABASE_SMS_PASSWORD;
    }

    public String getDATABASE_SMS_URL() {
        return DATABASE_SMS_URL;
    }

    public void setDATABASE_SMS_URL(String DATABASE_SMS_URL) {
        this.DATABASE_SMS_URL = DATABASE_SMS_URL;
    }

    public String getPINPAD_IP() {
        return PINPAD_IP;
    }

    public void setPINPAD_IP(String PINPAD_IP) {
        this.PINPAD_IP = PINPAD_IP;
    }

    public String getPINPAD_PUERTO() {
        return PINPAD_PUERTO;
    }

    public void setPINPAD_PUERTO(String PINPAD_PUERTO) {
        this.PINPAD_PUERTO = PINPAD_PUERTO;
    }

    public String getPINPAD_IPHOSTDATAFASTR1() {
        return PINPAD_IPHOSTDATAFASTR1;
    }

    public void setPINPAD_IPHOSTDATAFASTR1(String PINPAD_IPHOSTDATAFASTR1) {
        this.PINPAD_IPHOSTDATAFASTR1 = PINPAD_IPHOSTDATAFASTR1;
    }

    public String getPINPAD_PUERTODATAFASTCPR1() {
        return PINPAD_PUERTODATAFASTCPR1;
    }

    public void setPINPAD_PUERTODATAFASTCPR1(String PINPAD_PUERTODATAFASTCPR1) {
        this.PINPAD_PUERTODATAFASTCPR1 = PINPAD_PUERTODATAFASTCPR1;
    }

    public String getPINPAD_IPHOSTDATAFASTR1ALTERNA() {
        return PINPAD_IPHOSTDATAFASTR1ALTERNA;
    }

    public void setPINPAD_IPHOSTDATAFASTR1ALTERNA(String PINPAD_IPHOSTDATAFASTR1ALTERNA) {
        this.PINPAD_IPHOSTDATAFASTR1ALTERNA = PINPAD_IPHOSTDATAFASTR1ALTERNA;
    }

    public String getPINPAD_PUERTODATAFASTCPR1ALTERNO() {
        return PINPAD_PUERTODATAFASTCPR1ALTERNO;
    }

    public void setPINPAD_PUERTODATAFASTCPR1ALTERNO(String PINPAD_PUERTODATAFASTCPR1ALTERNO) {
        this.PINPAD_PUERTODATAFASTCPR1ALTERNO = PINPAD_PUERTODATAFASTCPR1ALTERNO;
    }

    public String getPINPAD_IPHOSTMEDIANETR2() {
        return PINPAD_IPHOSTMEDIANETR2;
    }

    public void setPINPAD_IPHOSTMEDIANETR2(String PINPAD_IPHOSTMEDIANETR2) {
        this.PINPAD_IPHOSTMEDIANETR2 = PINPAD_IPHOSTMEDIANETR2;
    }

    public String getPINPAD_PUERTOMEDIANETCPR1() {
        return PINPAD_PUERTOMEDIANETCPR1;
    }

    public void setPINPAD_PUERTOMEDIANETCPR1(String PINPAD_PUERTOMEDIANETCPR1) {
        this.PINPAD_PUERTOMEDIANETCPR1 = PINPAD_PUERTOMEDIANETCPR1;
    }

    public String getPINPAD_IPHOSTMEDIANETR1ALTERNA() {
        return PINPAD_IPHOSTMEDIANETR1ALTERNA;
    }

    public void setPINPAD_IPHOSTMEDIANETR1ALTERNA(String PINPAD_IPHOSTMEDIANETR1ALTERNA) {
        this.PINPAD_IPHOSTMEDIANETR1ALTERNA = PINPAD_IPHOSTMEDIANETR1ALTERNA;
    }

    public String getPINPAD_PUERTOMEDIANETCPR1ALTERNO() {
        return PINPAD_PUERTOMEDIANETCPR1ALTERNO;
    }

    public void setPINPAD_PUERTOMEDIANETCPR1ALTERNO(String PINPAD_PUERTOMEDIANETCPR1ALTERNO) {
        this.PINPAD_PUERTOMEDIANETCPR1ALTERNO = PINPAD_PUERTOMEDIANETCPR1ALTERNO;
    }

    public String[] getDATOS_TABLA_AMORTIZACION() {
        return DATOS_TABLA_AMORTIZACION;
    }

    public void setDATOS_TABLA_AMORTIZACION(String[] DATOS_TABLA_AMORTIZACION) {
        this.DATOS_TABLA_AMORTIZACION = DATOS_TABLA_AMORTIZACION;
    }

    public String getDATABASE_CORREO_USUARIO() {
        return DATABASE_CORREO_USUARIO;
    }

    public void setDATABASE_CORREO_USUARIO(String DATABASE_CORREO_USUARIO) {
        this.DATABASE_CORREO_USUARIO = DATABASE_CORREO_USUARIO;
    }

    public String getDATABASE_CORREO_PASSWORD() {
        return DATABASE_CORREO_PASSWORD;
    }

    public void setDATABASE_CORREO_PASSWORD(String DATABASE_CORREO_PASSWORD) {
        this.DATABASE_CORREO_PASSWORD = DATABASE_CORREO_PASSWORD;
    }

    public String getDATABASE_CORREO_HOST() {
        return DATABASE_CORREO_HOST;
    }

    public void setDATABASE_CORREO_HOST(String DATABASE_CORREO_HOST) {
        this.DATABASE_CORREO_HOST = DATABASE_CORREO_HOST;
    }

    public String getDATABASE_CORREO_CORREO() {
        return DATABASE_CORREO_CORREO;
    }

    public void setDATABASE_CORREO_CORREO(String DATABASE_CORREO_CORREO) {
        this.DATABASE_CORREO_CORREO = DATABASE_CORREO_CORREO;
    }

    public String getDATABASE_CORREO_ASUNTO() {
        return DATABASE_CORREO_ASUNTO;
    }

    public void setDATABASE_CORREO_ASUNTO(String DATABASE_CORREO_ASUNTO) {
        this.DATABASE_CORREO_ASUNTO = DATABASE_CORREO_ASUNTO;
    }

    public String getImpresoraTermica() {
        return impresoraTermica;
    }

    public void setImpresoraTermica(String impresoraTermica) {
        this.impresoraTermica = impresoraTermica;
    }

    public String getRutaArchivosImpresoraTermica() {
        return rutaArchivosImpresoraTermica;
    }

    public void setRutaArchivosImpresoraTermica(String rutaArchivosImpresoraTermica) {
        this.rutaArchivosImpresoraTermica = rutaArchivosImpresoraTermica;
    }

    public String getUsuarioVentaOnline() {
        return usuarioVentaOnline;
    }

    public void setUsuarioVentaOnline(String usuarioVentaOnline) {
        this.usuarioVentaOnline = usuarioVentaOnline;
    }

    public String getPasswordVentaOnline() {
        return passwordVentaOnline;
    }

    public void setPasswordVentaOnline(String passwordVentaOnline) {
        this.passwordVentaOnline = passwordVentaOnline;
    }

    public String getVentaEnLinea() {
        return ventaEnLinea;
    }

    public void setVentaEnLinea(String ventaEnLinea) {
        this.ventaEnLinea = ventaEnLinea;
    }
    
    public boolean isVentaEnLinea() {
        return ventaEnLinea != null && ventaEnLinea.equalsIgnoreCase("SI");
    }

}
