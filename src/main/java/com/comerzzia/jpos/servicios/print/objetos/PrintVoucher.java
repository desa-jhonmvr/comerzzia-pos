/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoSK;
import com.comerzzia.jpos.servicios.pagos.credito.PagoBonoSuperMaxiNavidad;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoGiftCard;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.util.cadenas.Cadena;
import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author MGRI
 */
public class PrintVoucher extends PrintDocument {

    public static final int TIPO_TARJETA_CREDITO = 1;
    public static final int TIPO_TARJETA_SUKASA = 2;
    public static final int TIPO_TARJETA_GIFTCARD = 3;
    public static final int TIPO_TARJETA_BONO_NAVIDAD = 4;

    int tipoVoucher;

    private String codigoEstablecimiento;
    private String fechaCaducidad;
    private String tarjeta;
    private String numeroTarjeta;
    private String idAppEmv;
    private String aidEmv;
    private String criptoEmv;
    private String verificacionPin;
    private String arqc;
    private String mensajePublicidad;
    private String numeroCreditoDirecto; // número de tarjeta o credito directo
    private String transaccion;
    private String fecha;
    private String hora;
    private String tipoTransaccion;
    private String autorizacion;
    private String plazo;
    private String base12;
    private String base0;
    private String subtotalBase;
    private String iva12;
    private String intereses;
    private String total;
    private String granTotal;
    private boolean mostrarPlazos;
    private String telefonoTienda;
    private String terminalId;
    private String merchantId;
    private String modoLectura;
    private String lotePinpad;
    private String secuencialTransaccion;
    private String nombreGrupoTarjeta;
    private String bancoAdquiriente;
    private String nombreTarjetaHabiente;
    private String valorDiferidoInteres;
    private BigDecimal interesFinanciamiento = BigDecimal.ZERO;
    private BigDecimal valorInteresPropio;
    private boolean datafast;

    // Para Voucher de Uso de Giftcard
    private String nombre;
    private String valorOriginal;
    private String valorDebitado;
    private String saldo;
    private String cedula;
    private String idUsoGiftcard;
    public boolean corriente;
    //Plan D
    private String valorTvr;
    private String valorTsi;

    public PrintVoucher(String documento, String procedencia) {
        super(true, new Fecha());

        transaccion = documento;

        // hora a la que se imprime el voucher
        Fecha fhora = new Fecha();
        this.fecha = fhora.getString("dd/MM/yy");
        this.hora = fhora.getString("HH:mm");

        tipoTransaccion = procedencia;
        mostrarPlazos = true;
    }

    public PrintVoucher(PagoCredito pagoCredito, String documento, String procedencia, boolean hayCompensacion) {
        this(documento, procedencia);

        if (hayCompensacion) {
            super.setPorcentajeIvaEmpresa(Sesion.getEmpresa().getPorcentajeIva().subtract(VariablesAlm.getVariableAsBigDecimal(VariablesAlm.PORCENTAJE_COMPENSACION_GOBIERNO)).setScale(0).toString().trim());
        }
        // código de establecimiento configurado en el medio de pago 
        codigoEstablecimiento = "" + pagoCredito.getCodEstablecimiento();
        fechaCaducidad = pagoCredito.getFechaCaducidadTarjeta();

        tarjeta = pagoCredito.getMedioPagoActivo().getDesMedioPago();
        transaccion = documento;
        if (pagoCredito instanceof PagoBonoSuperMaxiNavidad) {
            tipoVoucher = TIPO_TARJETA_BONO_NAVIDAD; // Tipo tarjeta de crédito

            numeroTarjeta = pagoCredito.getTarjetaCredito().getNumeroOculto();
            //numeroTarjeta = Cadena.ofuscar3Tarjeta(pagoCredito.getTarjetaCredito().getNumeroOculto());
            autorizacion = pagoCredito.getNumeroAutorizacionTarjeta();
            corriente = pagoCredito.getPlanSeleccionado().getVencimiento().isCorriente();

            if (pagoCredito.getPinpadRespuesta() != null) {
                numeroTarjeta = pagoCredito.getTarjetaCredito().getNumero();
                secuencialTransaccion = pagoCredito.getPinpadRespuesta().getSecuencialTransaccion();
                if (((PagoBonoSuperMaxiNavidad) pagoCredito).getCliente() != null) {
                    nombreTarjetaHabiente = ((PagoBonoSuperMaxiNavidad) pagoCredito).getCliente().getNombre() + " " + ((PagoBonoSuperMaxiNavidad) pagoCredito).getCliente().getApellido();
                    cedula = ((PagoBonoSuperMaxiNavidad) pagoCredito).getCliente().getCodcli();
                } else {
                    nombreTarjetaHabiente = pagoCredito.getPinpadRespuesta().getNombreTarjetaHabiente();
                    cedula = ((PagoBonoSuperMaxiNavidad) pagoCredito).getCedula();
                }
                codigoEstablecimiento = ((PagoBonoSuperMaxiNavidad) pagoCredito).getEstablecimiento();
                terminalId = pagoCredito.getPinpadRespuesta().getTerminalId();
            }
        } else if (pagoCredito instanceof PagoCreditoSK) {
            tipoVoucher = TIPO_TARJETA_SUKASA; // tipo tarjeta Sukasa o Credito directo

            Integer numeroCredito = ((PagoCreditoSK) pagoCredito).getPlastico() != null ? ((PagoCreditoSK) pagoCredito).getPlastico().getNumeroCredito() : ((TarjetaCreditoSK) ((PagoCreditoSK) pagoCredito).getTarjetaCredito()).getPlastico().getNumeroCredito();
            numeroCreditoDirecto = Cadena.completaconCeros(numeroCredito.toString(), 6);
            // TODO: Mirar cual es el código de autorizacion en un PagoCreditoSK
            autorizacion = ((PagoCreditoSK) pagoCredito).getCodigoValidacion();
            cedula = (((PagoCreditoSK) pagoCredito).getPlastico() != null ? ((PagoCreditoSK) pagoCredito).getPlastico().getCedulaCliente() : ((TarjetaCreditoSK) ((PagoCreditoSK) pagoCredito).getTarjetaCredito()).getPlastico().getCedulaCliente());
        } else {
            tipoVoucher = TIPO_TARJETA_CREDITO; // Tipo tarjeta de crédito

            numeroTarjeta = Cadena.ofuscar3Tarjeta(pagoCredito.getTarjetaCredito().getNumeroOculto());
            autorizacion = pagoCredito.getNumeroAutorizacionTarjeta();
            //Impresion Manual
            if (pagoCredito.getPinpadRespuesta() != null) {
                idAppEmv = pagoCredito.getPinpadRespuesta().getIdentificacionEmv();
                aidEmv = pagoCredito.getPinpadRespuesta().getAidEmv();
                criptoEmv = pagoCredito.getPinpadRespuesta().getValorEmv();
                verificacionPin = pagoCredito.getPinpadRespuesta().getVerificacionPin();
                arqc = pagoCredito.getPinpadRespuesta().getArqc();
                mensajePublicidad = pagoCredito.getPinpadRespuesta().getPublicidad();
                terminalId = pagoCredito.getPinpadRespuesta().getTerminalId();
                merchantId = pagoCredito.getPinpadRespuesta().getMerchantId();
                modoLectura = dameModoLectura(pagoCredito.getPinpadRespuesta().getModoLectura());
                lotePinpad = pagoCredito.getPinpadRespuesta().getNumeroLote();
                secuencialTransaccion = pagoCredito.getPinpadRespuesta().getSecuencialTransaccion();
                nombreGrupoTarjeta = pagoCredito.getPinpadRespuesta().getNombreGrupoTarjeta().trim();
                bancoAdquiriente = pagoCredito.getPinpadRespuesta().getNombreBancoAdquiriente();
                //G.S. Se reemplaza con vacio los caracteres especiales, para que se pueda imprimir el voucher
                if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
                    nombreTarjetaHabiente = parseaXML(pagoCredito.getPinpadRespuesta().getNombreTarjetaHabiente());
                } else {
                    nombreTarjetaHabiente = parseaXML(pagoCredito.getPinpadRespuesta().getNombreTarjetaHabiente());
                }
                valorDiferidoInteres = pagoCredito.getPinpadRespuesta().getFinanciamiento();
                if (autorizacion != null && (autorizacion.equals("") || autorizacion.startsWith(" "))) {
                    autorizacion = pagoCredito.getCodigoValidacionManual();
                }
                if (autorizacion == null && pagoCredito.getCodigoValidacionManual() != null) {
                    autorizacion = pagoCredito.getCodigoValidacionManual();
                }

                //plan D
                valorTvr = pagoCredito.getPinpadRespuesta().getTvr();
                valorTsi = pagoCredito.getPinpadRespuesta().getTsi();

            } else {
                idAppEmv = "";
                aidEmv = "";
                criptoEmv = "";
                verificacionPin = "";
                arqc = "";
                mensajePublicidad = "";
                terminalId = "";
                merchantId = "";
                modoLectura = "";
                lotePinpad = "";
                secuencialTransaccion = "";
                nombreGrupoTarjeta = "";
                bancoAdquiriente = "";
                nombreTarjetaHabiente = "";
                valorDiferidoInteres = "";

                if (pagoCredito.getLote() != null) {
                    lotePinpad = pagoCredito.getLote();
                }

                if (pagoCredito.getSecuencialTransaccion() != null) {
                    secuencialTransaccion = pagoCredito.getSecuencialTransaccion();
                }

                if (autorizacion == null) {
                    if (pagoCredito.getCodigoValidacionManual() != null) {
                        autorizacion = pagoCredito.getCodigoValidacionManual();
                    }
                } else {
                    if (autorizacion.equals("") || autorizacion.startsWith(" ")) {
                        if (pagoCredito.getCodigoValidacionManual() != null) {
                            autorizacion = pagoCredito.getCodigoValidacionManual();
                        }
                    }
                }
            }
            corriente = pagoCredito.getPlanSeleccionado().getVencimiento().isCorriente();
            if (!VariablesAlm.getVariableEstadoPinpadAsBoolean(VariablesAlm.PINPAD_FASTTRACK_ACTIVO)) {
                if (pagoCredito.getPlanSeleccionado().getVencimiento().isCorriente()) {
                    this.datafast = pagoCredito.getMedioPagoActivo().getTipoPagoCorriente() == 'D';
                } else {
                    this.datafast = pagoCredito.getMedioPagoActivo().getTipoPagoDiferido() == 'D';
                }
            } else {
                this.datafast = true;
            }
            if (hayValorDiferidoInteres()) {
                interesFinanciamiento = new BigDecimal(getValorDiferidoInteresPantalla());
            }
        }

        if (pagoCredito.getPlanSeleccionado().getMeses() == 0) {
            plazo = "1";
        } else {
            plazo = "" + pagoCredito.getPlanSeleccionado().getMeses();
        }

        base12 = pagoCredito.getSubtotalIva12().setScale(2, RoundingMode.HALF_UP).toString();
        base0 = pagoCredito.getSubtotalIva0().setScale(2, RoundingMode.HALF_UP).toString();
        subtotalBase = (pagoCredito.getSubtotalIva12().setScale(2, RoundingMode.HALF_UP).add(pagoCredito.getSubtotalIva0().setScale(2, RoundingMode.HALF_UP))).toString();
        iva12 = pagoCredito.getIva().toString();
        intereses = pagoCredito.getImporteInteres().setScale(2, RoundingMode.HALF_UP).toString();
        valorInteresPropio = pagoCredito.getImporteInteres().setScale(2, RoundingMode.HALF_UP);
        total = pagoCredito.getUstedPaga().add(pagoCredito.getImporteInteres()).setScale(2, RoundingMode.HALF_UP).toString();
        granTotal = (pagoCredito.getUstedPaga().add(pagoCredito.getImporteInteres()).setScale(2, RoundingMode.HALF_UP).add(interesFinanciamiento)).toString();
        mostrarPlazos = (pagoCredito.getPlanSeleccionado().getNumCuotas() > 1);
        this.telefonoTienda = Sesion.getTienda().getAlmacen().getTelefonos();
    }

    /**
     * @author Gabriel Simbania
     * @description Se reemplaza con vacio los caracteres especiales
     * @param sIn
     * @return
     */
    private String parseaXML(String sIn) {
        if (sIn != null && !sIn.equals("")) {
            String res = sIn;
            res = res.replaceAll("&", "");
            res = res.replaceAll("<", "");
            res = res.replaceAll(">", "");
            return res;
        }
        return sIn;
    }

    public PrintVoucher(PagoGiftCard pagoGC, String documento, String procedencia) {
        this(documento, procedencia);

        tipoVoucher = TIPO_TARJETA_GIFTCARD; // tipo de tarjeta giftcard

        // No tiene código e establecimiento ni fecha de caducidad
        codigoEstablecimiento = null;
        fechaCaducidad = null;

        // En principio no usaremos el nombre del medio de pago
        tarjeta = pagoGC.getMedioPagoActivo().getDesMedioPago();

        // numeroTarjeta 
        //numeroTarjeta = Cadena.ofuscarTarjeta(pagoGC.getGiftCard().getNumeroTarjeta());
        numeroTarjeta = pagoGC.getGiftCard().getNumeroTarjeta();
        nombre = pagoGC.getCliente().getApellido() + " " + pagoGC.getCliente().getNombre();
        valorOriginal = pagoGC.getEntregado().toString();
        valorDebitado = pagoGC.getUstedPaga().toString();
        saldo = pagoGC.getGiftCard().getSaldo().toString();
        mostrarPlazos = true;
        idUsoGiftcard = Sesion.getTienda().getCodalmSRI() + "-" + Sesion.getTienda().getCajaActiva().getCodcajaSri() + "-" + String.valueOf(pagoGC.getIdUsoGiftCard());
    }

    public String getCodigoEstablecimiento() {
        return codigoEstablecimiento;
    }

    public void setCodigoEstablecimiento(String codigoEstablecimiento) {
        this.codigoEstablecimiento = codigoEstablecimiento;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getNumeroCreditoDirecto() {
        return numeroCreditoDirecto;
    }

    public void setNumeroCreditoDirecto(String numeroCreditoDirecto) {
        this.numeroCreditoDirecto = numeroCreditoDirecto;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public String getBase12() {
        return base12;
    }

    public void setBase12(String base12) {
        this.base12 = base12;
    }

    public String getBase0() {
        return base0;
    }

    public void setBase0(String base0) {
        this.base0 = base0;
    }

    public String getSubtotalBase() {
        return subtotalBase;
    }

    public void setSubtotalBase(String subtotalBase) {
        this.subtotalBase = subtotalBase;
    }

    public String getIva12() {
        return iva12;
    }

    public void setIva12(String iva12) {
        this.iva12 = iva12;
    }

    public String getIntereses() {
        return intereses != null ? intereses.trim() : "";
    }

    public void setIntereses(String intereses) {
        this.intereses = intereses;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValorOriginal() {
        return valorOriginal;
    }

    public void setValorOriginal(String valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public String getValorDebitado() {
        return valorDebitado;
    }

    public void setValorDebitado(String valorDebitado) {
        this.valorDebitado = valorDebitado;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public boolean isVoucherGiftCard() {
        return this.tipoVoucher == TIPO_TARJETA_GIFTCARD;
    }

    public boolean isVoucherTarjetaCredito() {
        return this.tipoVoucher == TIPO_TARJETA_CREDITO;
    }

    public boolean isVoucherTarjetaSukasa() {
        return this.tipoVoucher == TIPO_TARJETA_SUKASA;
    }

    public boolean isVoucherTarjetaBonoNavidad() {
        return this.tipoVoucher == TIPO_TARJETA_BONO_NAVIDAD;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public int getTipoVoucher() {
        return tipoVoucher;
    }

    public void setTipoVoucher(int tipoVoucher) {
        this.tipoVoucher = tipoVoucher;
    }

    public boolean isMostrarPlazos() {
        return mostrarPlazos;
    }

    public void setMostrarPlazos(boolean mostrarPlazos) {
        this.mostrarPlazos = mostrarPlazos;
    }

    public String getIdUsoGiftcard() {
        return idUsoGiftcard;
    }

    public void setIdUsoGiftcard(String idUsoGiftcard) {
        this.idUsoGiftcard = idUsoGiftcard;
    }

    public String getIdAppEmv() {
        return idAppEmv;
    }

    public String getAidEmv() {
        return aidEmv;
    }

    //Plan D
// public String getAidEmv() {
//        return "AID : "+aidEmv;
//    }
    public String getCriptoEmv() {
        return criptoEmv;
    }
    //Plan D
//     public String getCriptoEmv() {
//        return "TC  : "+ criptoEmv;
//    }

    public String getVerificacionPin() {
        return verificacionPin;
    }

    public String getArqc() {
        return arqc;
    }
    // plan D
//     public String getArqc() {
//        return "ARQC: "+arqc;
//    }

    public String getMensajePublicidad() {
        return mensajePublicidad;
    }

    public void setIdAppEmv(String idAppEmv) {
        this.idAppEmv = idAppEmv;
    }

    public void setAidEmv(String aidEmv) {
        this.aidEmv = aidEmv;
    }

    public void setCriptoEmv(String criptoEmv) {
        this.criptoEmv = criptoEmv;
    }

    public void setVerificacionPin(String verificacionPin) {
        this.verificacionPin = verificacionPin;
    }

    public void setArqc(String arqc) {
        this.arqc = arqc;
    }

    public void setMensajePublicidad(String mensajePublicidad) {
        this.mensajePublicidad = mensajePublicidad;
    }

    public LineaEnTicket getPublicidadAsLineas() {
        return new LineaEnTicket(this.mensajePublicidad);
    }

    public String getTelefonoTienda() {
        return telefonoTienda;
    }

    public void setTelefonoTienda(String telefonoTienda) {
        this.telefonoTienda = telefonoTienda;
    }

    public boolean isTieneTelefonoTienda() {
        return (this.telefonoTienda != null && !this.telefonoTienda.isEmpty());
    }

    public boolean isTieneTarjetaFechaCaducidad() {
        return (this.fechaCaducidad != null && !this.fechaCaducidad.isEmpty());
    }

    public String getMidTidML() {
        return merchantId.trim() + " - " + terminalId.trim() + " - " + modoLectura.trim();
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getModoLectura() {
        return modoLectura;
    }

    public void setModoLectura(String modoLectura) {
        this.modoLectura = modoLectura;
    }

    public String getLotePinpad() {
        return lotePinpad;
    }

    public void setLotePinpad(String lotePinpad) {
        this.lotePinpad = lotePinpad;
    }

    public String getSecuencialTransaccion() {
        return secuencialTransaccion;
    }

    public void setSecuencialTransaccion(String secuencialTransaccion) {
        this.secuencialTransaccion = secuencialTransaccion;
    }

    public String getNombreGrupoTarjeta() {
        return nombreGrupoTarjeta;
    }

    public void setNombreGrupoTarjeta(String nombreGrupoTarjeta) {
        this.nombreGrupoTarjeta = nombreGrupoTarjeta;
    }

    public String getBancoAdquiriente() {
        return bancoAdquiriente != null ? bancoAdquiriente.trim() : "";
    }

    public void setBancoAdquiriente(String bancoAdquiriente) {
        this.bancoAdquiriente = bancoAdquiriente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombreTarjetaHabiente() {
        if (nombreTarjetaHabiente != null) {
            return nombreTarjetaHabiente.trim();
        }
        return "";
    }

    public void setNombreTarjetaHabiente(String nombreTarjetaHabiente) {
        this.nombreTarjetaHabiente = nombreTarjetaHabiente;
    }

    public String getValorDiferidoInteres() {
        return valorDiferidoInteres;
    }

    public void setValorDiferidoInteres(String valorDiferidoInteres) {
        this.valorDiferidoInteres = valorDiferidoInteres;
    }

    public boolean isCorriente() {
        return corriente;
    }

    public void setCorriente(boolean corriente) {
        this.corriente = corriente;
    }

    public boolean hayValorDiferidoInteres() {
        return valorDiferidoInteres != null && valorDiferidoInteres.trim().length() > 0;
    }

    public String getValorDiferidoInteresPantalla() {
        return Cadena.eliminaCeros(valorDiferidoInteres.substring(0, valorDiferidoInteres.length() - 2)) + "." + valorDiferidoInteres.substring(valorDiferidoInteres.length() - 2);
    }

    public String getGranTotal() {
        return granTotal;
    }

    public void setGranTotal(String granTotal) {
        this.granTotal = granTotal;
    }

    public BigDecimal getInteresFinanciamiento() {
        return interesFinanciamiento;
    }

    public void setInteresFinanciamiento(BigDecimal interesFinanciamiento) {
        this.interesFinanciamiento = interesFinanciamiento;
    }

    public BigDecimal getValorInteresPropio() {
        return valorInteresPropio;
    }

    public void setValorInteresPropio(BigDecimal valorInteresPropio) {
        this.valorInteresPropio = valorInteresPropio;
    }

    public boolean hayInteresPropio() {
        return valorInteresPropio != null && valorInteresPropio.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isDatafast() {
        return datafast;
    }

    public void setDatafast(boolean datafast) {
        this.datafast = datafast;
    }

    public String getValorTvr() {
        return "TVR : " + valorTvr;
    }

    public void setValorTvr(String valorTvr) {
        this.valorTvr = valorTvr;
    }

    public String getValorTsi() {
        return "TSI : " + valorTsi;
    }

    public void setValorTsi(String valorTsi) {
        this.valorTsi = valorTsi;
    }

    private String dameModoLectura(String modoLectura) {
        if (modoLectura.equals("01")) {
            return "MANUAL";
        } else if (modoLectura.equals("02")) {
            return "BANDA";
        } else if (modoLectura.equals("03")) {
            return "CHIP";
        } else if (modoLectura.equals("04")) {
            return "FALLBACK (CHIP(";
        } else if (modoLectura.equals("05")) {
            return "D1";
        } else if (modoLectura.equals("06")) {
            return "CTLS";
        } else {
            return modoLectura;
        }
    }
}
