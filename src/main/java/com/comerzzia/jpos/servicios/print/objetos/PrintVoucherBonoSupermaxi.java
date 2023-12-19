/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoBonoSuperMaxiNavidad;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DESARROLLO
 */
public class PrintVoucherBonoSupermaxi extends PrintDocument {

    private static Logger log = Logger.getMLogger(PrintVoucherBonoSupermaxi.class);

    public static final int TIPO_TARJETA_CREDITO = 1;
    public static final int TIPO_TARJETA_SUKASA = 2;
    public static final int TIPO_TARJETA_GIFTCARD = 3;
    public static final int TIPO_TARJETA_BONO_NAVIDAD = 4;

    private String transaccion;
    private String fecha;
    private String hora;
    private String tipoTransaccion;
    private String codigoEstablecimiento;
    private String fechaCaducidad;
    private String tarjeta;
    private String numeroTarjeta;
    int tipoVoucher;
    private String autorizacion;
    private String plazo;
    private String base12;
    private String base0;
    private String subtotalBase;
    private String iva12;
    private String intereses;
    private String total;
    private String granTotal;
    public boolean corriente;
    private String secuencialTransaccion;
    private String nombreTarjetaHabiente;
    private String cedula;
    private String terminalId;
    private BigDecimal valorInteresPropio;
    private boolean mostrarPlazos;
    private String telefonoTienda;
    private BigDecimal interesFinanciamiento = BigDecimal.ZERO;
    private List<BonoSupermaxi> bonosSupermaxi;
    private BigDecimal vBase12 = BigDecimal.ZERO;
    private BigDecimal vBase0 = BigDecimal.ZERO;
    private BigDecimal vSubtotalBase = BigDecimal.ZERO;
    private BigDecimal vIva12 = BigDecimal.ZERO;
    private BigDecimal vIntereses = BigDecimal.ZERO;
    private BigDecimal vTotal = BigDecimal.ZERO;
    private BigDecimal vGranTotal = BigDecimal.ZERO;

    public PrintVoucherBonoSupermaxi(String documento, String procedencia) {
        super(true, new Fecha());

        transaccion = documento;

        // hora a la que se imprime el voucher
        Fecha fhora = new Fecha();
        this.fecha = fhora.getString("dd/MM/yy");
        this.hora = fhora.getString("HH:mm");

        tipoTransaccion = procedencia;
        mostrarPlazos = true;
    }

    public PrintVoucherBonoSupermaxi(List<Pago> pagosCredito, String documento, String procedencia, boolean hayCompensacion) {
        this(documento, procedencia);
        bonosSupermaxi = new ArrayList<>();
        tipoVoucher = TIPO_TARJETA_BONO_NAVIDAD; // Tipo tarjeta de crédito

        if (hayCompensacion) {
            super.setPorcentajeIvaEmpresa(Sesion.getEmpresa().getPorcentajeIva().subtract(VariablesAlm.getVariableAsBigDecimal(VariablesAlm.PORCENTAJE_COMPENSACION_GOBIERNO)).setScale(0).toString().trim());
        }
        // código de establecimiento configurado en el medio de pago 
        for (Pago pago : pagosCredito) {
            PagoCredito pagoCredito = (PagoCredito) pago;
            codigoEstablecimiento = "" + pagoCredito.getCodEstablecimiento();
            fechaCaducidad = pagoCredito.getFechaCaducidadTarjeta();
            tarjeta = pagoCredito.getMedioPagoActivo().getDesMedioPago();
            numeroTarjeta = pagoCredito.getTarjetaCredito().getNumero();
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

            bonosSupermaxi.add(new BonoSupermaxi(numeroTarjeta, autorizacion, pagoCredito.getEntregado().setScale(2, RoundingMode.HALF_UP).toString()));

            if (pagoCredito.getPlanSeleccionado().getMeses() == 0) {
                plazo = "1";
            } else {
                plazo = "" + pagoCredito.getPlanSeleccionado().getMeses();
            }

            vBase12 = vBase12.add(pagoCredito.getSubtotalIva12());
            vBase0 = vBase0.add(pagoCredito.getSubtotalIva0());
            vSubtotalBase = vSubtotalBase.add(pagoCredito.getSubtotalIva12().add(pagoCredito.getSubtotalIva0()));
            vIva12 = vIva12.add(pagoCredito.getIva());
            vIntereses = vIntereses.add(pagoCredito.getImporteInteres());
            vTotal = vTotal.add(pagoCredito.getUstedPaga().add(pagoCredito.getImporteInteres()));
            vGranTotal = vGranTotal.add(pagoCredito.getUstedPaga().add(pagoCredito.getImporteInteres()).add(interesFinanciamiento));

            valorInteresPropio = pagoCredito.getImporteInteres().setScale(2, RoundingMode.HALF_UP);
            mostrarPlazos = (pagoCredito.getPlanSeleccionado().getNumCuotas() > 1);
        }
        base12 = vBase12.toString();
        base0 = vBase0.toString();
        subtotalBase = vSubtotalBase.toString();
        iva12 = vIva12.toString();
        intereses = vIntereses.toString();
        total = vTotal.toString();
        granTotal = vGranTotal.toString();
        this.telefonoTienda = Sesion.getTienda().getAlmacen().getTelefonos();
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

    public int getTipoVoucher() {
        return tipoVoucher;
    }

    public void setTipoVoucher(int tipoVoucher) {
        this.tipoVoucher = tipoVoucher;
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
        return intereses;
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

    public String getGranTotal() {
        return granTotal;
    }

    public void setGranTotal(String granTotal) {
        this.granTotal = granTotal;
    }

    public boolean isCorriente() {
        return corriente;
    }

    public void setCorriente(boolean corriente) {
        this.corriente = corriente;
    }

    public String getSecuencialTransaccion() {
        return secuencialTransaccion;
    }

    public void setSecuencialTransaccion(String secuencialTransaccion) {
        this.secuencialTransaccion = secuencialTransaccion;
    }

    public String getNombreTarjetaHabiente() {
        return nombreTarjetaHabiente;
    }

    public void setNombreTarjetaHabiente(String nombreTarjetaHabiente) {
        this.nombreTarjetaHabiente = nombreTarjetaHabiente;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public BigDecimal getValorInteresPropio() {
        return valorInteresPropio;
    }

    public void setValorInteresPropio(BigDecimal valorInteresPropio) {
        this.valorInteresPropio = valorInteresPropio;
    }

    public boolean isMostrarPlazos() {
        return mostrarPlazos;
    }

    public void setMostrarPlazos(boolean mostrarPlazos) {
        this.mostrarPlazos = mostrarPlazos;
    }

    public String getTelefonoTienda() {
        return telefonoTienda;
    }

    public void setTelefonoTienda(String telefonoTienda) {
        this.telefonoTienda = telefonoTienda;
    }

    public BigDecimal getInteresFinanciamiento() {
        return interesFinanciamiento;
    }

    public void setInteresFinanciamiento(BigDecimal interesFinanciamiento) {
        this.interesFinanciamiento = interesFinanciamiento;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public boolean isTieneTelefonoTienda() {
        return (this.telefonoTienda != null && !this.telefonoTienda.isEmpty());
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

    public List<BonoSupermaxi> getBonosSupermaxi() {
        return bonosSupermaxi;
    }

    public void setBonosSupermaxi(List<BonoSupermaxi> bonosSupermaxi) {
        this.bonosSupermaxi = bonosSupermaxi;
    }

    public BigDecimal getvBase12() {
        return vBase12;
    }

    public void setvBase12(BigDecimal vBase12) {
        this.vBase12 = vBase12;
    }

    public BigDecimal getvBase0() {
        return vBase0;
    }

    public void setvBase0(BigDecimal vBase0) {
        this.vBase0 = vBase0;
    }

    public BigDecimal getvSubtotalBase() {
        return vSubtotalBase;
    }

    public void setvSubtotalBase(BigDecimal vSubtotalBase) {
        this.vSubtotalBase = vSubtotalBase;
    }

    public BigDecimal getvIva12() {
        return vIva12;
    }

    public void setvIva12(BigDecimal vIva12) {
        this.vIva12 = vIva12;
    }

    public BigDecimal getvIntereses() {
        return vIntereses;
    }

    public void setvIntereses(BigDecimal vIntereses) {
        this.vIntereses = vIntereses;
    }

    public BigDecimal getvTotal() {
        return vTotal;
    }

    public void setvTotal(BigDecimal vTotal) {
        this.vTotal = vTotal;
    }

    public BigDecimal getvGranTotal() {
        return vGranTotal;
    }

    public void setvGranTotal(BigDecimal vGranTotal) {
        this.vGranTotal = vGranTotal;
    }

}
