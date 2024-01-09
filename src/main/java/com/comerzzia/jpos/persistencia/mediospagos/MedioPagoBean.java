package com.comerzzia.jpos.persistencia.mediospagos;

import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import java.util.ArrayList;
import java.util.List;

import com.comerzzia.util.base.MantenimientoBean;
import java.math.BigDecimal;
import java.util.Arrays;

public class MedioPagoBean extends MantenimientoBean {

    public static final byte TIPO_OTROS = 1;
    public static final byte TIPO_TARJETAS = 2;
    public static final byte TIPO_CONTADO = 3;

    private static final long serialVersionUID = -1645910587482065530L;
    private static final String COD_MEDIO_PAGO_ABONO = "41";
    private String codMedioPago;
    private String desMedioPago;
    private boolean contado;
    private boolean tarjetaCredito;
    private boolean creditoDirecto; // credito temporal --> Letras
    private boolean tarjetaSukasa; // credito directo --> tarjetas sukasa
    private boolean otros;
    private String codTipoEfecto;
    private String desTipoEfecto;
    private boolean visibleVenta;
    private boolean visibleTiendaVirtual;
    private boolean visibleCompra;
    private boolean admitePagoCreditoTemporal;
    private boolean admiteAbonoTarjetaPropia;
    private boolean abrirCajon;
    private boolean admiteVuelto;
    private boolean admiteAbonoReservacionCorriente;
    private boolean admiteAbonoReservacionDiferido;
    private boolean requiereAutorizacion;
    private boolean giftCard;
    private boolean permitePagarGiftCardCorriente;
    private boolean permitePagarGiftCardDiferido;
    private boolean tieneInfoExtra1;
    private String infoExtra1;
    private boolean tieneInfoExtra2;
    private String infoExtra2;
    private boolean tieneInfoExtra3;
    private String infoExtra3;
    private String codEstCorriente;
    private String codEstDiferidoSinIntereses;
    private String codEstDiferidoConIntereses;
    private List<String> bines;
    private List<BancoBean> bancos;
    private List<VencimientoBean> planes;
    private Character tipoPagoCorriente;
    private Character tipoPagoDiferido;
    private boolean requiereAutorizacionLecturaManual;
    private boolean admiteAutorizacionManualDiferido;
    private boolean admiteAutorizacionManualCorriente;
    private boolean admiteAutorizacionAutomaticaDiferido;
    private boolean admiteAutorizacionAutomaticaCorriente;
    private BigDecimal montoMaximoAutorizacion;
    private String codMedPagElec;
    private String codBan;


    public MedioPagoBean() {
        bancos = new ArrayList<BancoBean>();
        planes = new ArrayList<VencimientoBean>();
    }

    public VencimientoBean getVencimientoDefault() {
        if (planes == null || planes.isEmpty()) {
            return null;
        }
        return planes.get(0);
    }

    public static MedioPagoBean getMedioPagoAbonoReservacion() {
        MedioPagoBean medioPago = new MedioPagoBean();
        medioPago.setCodMedioPago(COD_MEDIO_PAGO_ABONO);
        medioPago.setDesMedioPago("ABONO RESERVACIÓN");
        MedioPagoBean medioPagoActivo = MediosPago.getInstancia().getMedioPago(COD_MEDIO_PAGO_ABONO);
        medioPago.setCodMedPagElec(medioPagoActivo.getCodMedPagElec());
        medioPago.setActivo(true);
        return medioPago;
    }

    public static MedioPagoBean getMedioPagoVacio() {
        MedioPagoBean medioPago = new MedioPagoBean();
        medioPago.setCodMedioPago("");
        medioPago.setDesMedioPago("");
        medioPago.setActivo(false);
        return medioPago;
    }

    public boolean isAbonoReservacion() {
        return codMedioPago.equals(COD_MEDIO_PAGO_ABONO);
    }

    public boolean isEfectivo() {
        return codMedioPago.equals(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_EFECTIVO));
    }

    public boolean isNotaCredito() {
        return codMedioPago.equals(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_NOTA_CREDITO));
    }

    public boolean isRetencion() {
        return codMedioPago.equals(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_RETENCION));
    }

    public boolean isBonoEfectivo() {
        return codMedioPago.equals(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_BONO));
    }

    public boolean isCheque() {
        return codMedioPago.equals(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_CHEQUE));
    }

    public boolean isCompensacion() {
        return codMedioPago.equals(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_COMPENSACION));
    }

    public boolean isBonoSuperMaxiNavidad() {
        return codMedioPago.equals(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_BONO_NAVI));
    }

    public boolean isCreditoFilial() {
        return codMedioPago.equals(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_FILIAL));
    }

    @Override
    protected void initNuevoBean() {
    }

    public List<VencimientoBean> getPlanes() {
        return planes;
    }

    public void setPlanes(List<VencimientoBean> planes) {
        this.planes = planes;
    }

    public String getCodMedioPago() {
        return codMedioPago;
    }

    public void setCodMedioPago(String codMedioPago) {
        this.codMedioPago = codMedioPago;
    }

    public String getDesMedioPago() {
        return desMedioPago;
    }

    public void setDesMedioPago(String desMedioPago) {
        this.desMedioPago = desMedioPago;
    }

    public boolean isContado() {
        return contado;
    }

    public void setContado(boolean contado) {
        this.contado = contado;
    }

    public void setContado(String contado) {
        this.contado = contado.equals(TRUE);
    }

    public void setAbrirCajon(boolean abrirCajon) {
        this.abrirCajon = abrirCajon;
    }

    public void setAbrirCajon(String abrirCajon) {
        this.abrirCajon = abrirCajon.equals(TRUE);
    }

    public String getAbrirCajon() {
        return (abrirCajon) ? TRUE : FALSE;
    }

    public boolean isAbrirCajon() {
        return abrirCajon;
    }

    public String getTarjetaCredito() {
        return (tarjetaCredito) ? TRUE : FALSE;
    }

    public boolean isTarjetaCredito() {
        return tarjetaCredito;
    }

    public void setTarjetaCredito(boolean tarjetaCredito) {
        this.tarjetaCredito = tarjetaCredito;
    }

    public void setTarjetaCredito(String tarjetaCredito) {
        this.tarjetaCredito = tarjetaCredito.equals(TRUE);
    }

    @Deprecated
    public String getCreditoDirecto() {
        return (creditoDirecto) ? TRUE : FALSE;
    }

    @Deprecated // utilizar isCreditoTemporal()
    public boolean isCreditoDirecto() { // Esto se refiere al crédito temporal, a las letras
        return creditoDirecto;
    }

    public boolean isCreditoTemporal() {
        return creditoDirecto;
    }

    @Deprecated
    public void setCreditoDirecto(boolean creditoDirecto) {
        this.creditoDirecto = creditoDirecto;
    }

    public void setCreditoDirecto(String creditoDirecto) {
        this.creditoDirecto = creditoDirecto.equals(TRUE);
    }

    public String getOtros() {
        return (otros) ? TRUE : FALSE;
    }

    public boolean isOtros() {
        return otros;
    }

    public void setOtros(boolean otros) {
        this.otros = otros;
    }

    public void setOtros(String otros) {
        this.otros = otros.equals(TRUE);
    }

    public String getCodTipoEfecto() {
        return codTipoEfecto;
    }

    public void setCodTipoEfecto(String codTipoEfecto) {
        this.codTipoEfecto = codTipoEfecto;
    }

    public String getDesTipoEfecto() {
        return desTipoEfecto;
    }

    public void setDesTipoEfecto(String desTipoEfecto) {
        this.desTipoEfecto = desTipoEfecto;
    }

    public boolean isVisibleVenta() {
        return visibleVenta;
    }

    public void setVisibleVenta(boolean visibleVenta) {
        this.visibleVenta = visibleVenta;
    }

    public String getVisibleVenta() {
        return (visibleVenta) ? TRUE : FALSE;
    }

    public void setVisibleVenta(String visibleVenta) {
        this.visibleVenta = visibleVenta.equals(TRUE);
    }

    public boolean isVisibleTiendaVirtual() {
        return visibleTiendaVirtual;
    }

    public void setVisibleTiendaVirtual(boolean visibleTiendaVirtual) {
        this.visibleTiendaVirtual = visibleTiendaVirtual;
    }

    public String getVisibleTiendaVirtual() {
        return (visibleTiendaVirtual) ? TRUE : FALSE;
    }

    public void setVisibleTiendaVirtual(String visibleTiendaVirtual) {
        this.visibleTiendaVirtual = visibleTiendaVirtual.equals(TRUE);
    }

    public boolean isVisibleCompra() {
        return visibleCompra;
    }

    public void setVisibleCompra(boolean visibleCompra) {
        this.visibleCompra = visibleCompra;
    }

    public String getVisibleCompra() {
        return (visibleCompra) ? TRUE : FALSE;
    }

    public void setVisibleCompra(String visibleCompra) {
        this.visibleCompra = visibleCompra.equals(TRUE);
    }

    public List<String> getBines() {
        return bines;
    }

    public void setBines(String[] bines) {
        if (bines == null || bines.length == 0) {
            this.bines = null;
        } else {
            this.bines = Arrays.asList(bines);
        }
    }

    public boolean isAdmiteVuelto() {
        return admiteVuelto;
    }

    public void setAdmiteVuelto(boolean admiteVuelto) {
        this.admiteVuelto = admiteVuelto;
    }

    public void setAdmiteVuelto(String admiteVuelto) {
        this.admiteVuelto = admiteVuelto.equals(TRUE);
    }

    public boolean isAdmitePagoCreditoTemporal() {
        return admitePagoCreditoTemporal;
    }

    public void setAdmitePagoCreditoTemporal(boolean admitePagoCreditoTemporal) {
        this.admitePagoCreditoTemporal = admitePagoCreditoTemporal;
    }

    public void setAdmitePagoCreditoTemporal(String admitePagoCreditoTemporal) {
        this.admitePagoCreditoTemporal = admitePagoCreditoTemporal.equals(TRUE);
    }

    public boolean isAdmiteAbonoTarjetaPropia() {
        return admiteAbonoTarjetaPropia;
    }

    public void setAdmiteAbonoTarjetaPropia(boolean admiteAbonoTarjetaPropia) {
        this.admiteAbonoTarjetaPropia = admiteAbonoTarjetaPropia;
    }

    public void setAdmiteAbonoTarjetaPropia(String admiteAbonoTarjetaPropia) {
        this.admiteAbonoTarjetaPropia = admiteAbonoTarjetaPropia.equals(TRUE);
    }

    public boolean isAdmiteAbonoReservacionCorriente() {
        return admiteAbonoReservacionCorriente;
    }

    public void setAdmiteAbonoReservacionCorriente(boolean admiteAbonoReservacionCorriente) {
        this.admiteAbonoReservacionCorriente = admiteAbonoReservacionCorriente;
    }

    public void setAdmiteAbonoReservacionCorriente(String admiteAbonoReservacionCorriente) {
        this.admiteAbonoReservacionCorriente = admiteAbonoReservacionCorriente.equals(TRUE);
    }

    public boolean isAdmiteAbonoReservacionDiferido() {
        return admiteAbonoReservacionDiferido;
    }

    public void setAdmiteAbonoReservacionDiferido(boolean admiteAbonoReservacionDiferido) {
        this.admiteAbonoReservacionDiferido = admiteAbonoReservacionDiferido;
    }

    public void setAdmiteAbonoReservacionDiferido(String admiteAbonoReservacionDiferido) {
        this.admiteAbonoReservacionDiferido = admiteAbonoReservacionDiferido.equals(TRUE);
    }

    public boolean isRequiereAutorizacion() {
        return requiereAutorizacion;
    }

    public void setRequiereAutorizacion(boolean requiereAutorizacion) {
        this.requiereAutorizacion = requiereAutorizacion;
    }

    public void setRequiereAutorizacion(String requiereAutorizacion) {
        this.requiereAutorizacion = requiereAutorizacion.equals(TRUE);
    }

    public boolean isGiftCard() {
        return giftCard;
    }

    public void setGiftCard(boolean giftCard) {
        this.giftCard = giftCard;
    }

    public void setGiftCard(String giftCard) {
        this.giftCard = giftCard.equals(TRUE);
    }

    public boolean isPermitePagarGiftCardCorriente() {
        return permitePagarGiftCardCorriente;
    }

    public void setPermitePagarGiftCardCorriente(boolean permitePagarGiftCardCorriente) {
        this.permitePagarGiftCardCorriente = permitePagarGiftCardCorriente;
    }

    public void setPermitePagarGiftCardCorriente(String permitePagarGiftCardCorriente) {
        this.permitePagarGiftCardCorriente = permitePagarGiftCardCorriente.equals(TRUE);
    }

    public boolean isPermitePagarGiftCardDiferido() {
        return permitePagarGiftCardDiferido;
    }

    public void setPermitePagarGiftCardDiferido(boolean permitePagarGiftCardDiferido) {
        this.permitePagarGiftCardDiferido = permitePagarGiftCardDiferido;
    }

    public void setPermitePagarGiftCardDiferido(String permitePagarGiftCardDiferido) {
        this.permitePagarGiftCardDiferido = permitePagarGiftCardDiferido.equals(TRUE);
    }

    public boolean isTieneInfoExtra1() {
        return tieneInfoExtra1;
    }

    public void setTieneInfoExtra1(boolean tieneInfoExtra1) {
        this.tieneInfoExtra1 = tieneInfoExtra1;
    }

    public void setTieneInfoExtra1(String tieneInfoExtra1) {
        this.tieneInfoExtra1 = tieneInfoExtra1.equals(TRUE);
    }

    public boolean isTieneInfoExtra2() {
        return tieneInfoExtra2;
    }

    public void setTieneInfoExtra2(boolean tieneInfoExtra2) {
        this.tieneInfoExtra2 = tieneInfoExtra2;
    }

    public void setTieneInfoExtra2(String tieneInfoExtra2) {
        this.tieneInfoExtra2 = tieneInfoExtra2.equals(TRUE);
    }

    public boolean isTieneInfoExtra3() {
        return tieneInfoExtra3;
    }

    public void setTieneInfoExtra3(boolean tieneInfoExtra3) {
        this.tieneInfoExtra3 = tieneInfoExtra3;
    }

    public void setTieneInfoExtra3(String tieneInfoExtra3) {
        this.tieneInfoExtra3 = tieneInfoExtra3.equals(TRUE);
    }

    public String getInfoExtra1() {
        return infoExtra1;
    }

    public void setInfoExtra1(String infoExtra1) {
        this.infoExtra1 = infoExtra1;
    }

    public String getInfoExtra2() {
        return infoExtra2;
    }

    public void setInfoExtra2(String infoExtra2) {
        this.infoExtra2 = infoExtra2;
    }

    public String getInfoExtra3() {
        return infoExtra3;
    }

    public void setInfoExtra3(String infoExtra3) {
        this.infoExtra3 = infoExtra3;
    }

    public List<BancoBean> getBancos() {
        return bancos;
    }

    public void setBancos(List<BancoBean> bancos) {
        this.bancos = bancos;
    }

    public String getCodEstCorriente() {
        return codEstCorriente;
    }

    public void setCodEstCorriente(String codEstCorriente) {
        this.codEstCorriente = codEstCorriente;
    }

    public String getCodEstDiferidoConIntereses() {
        return codEstDiferidoConIntereses;
    }

    public void setCodEstDiferidoConIntereses(String codEstDiferidoConIntereses) {
        this.codEstDiferidoConIntereses = codEstDiferidoConIntereses;
    }

    public String getCodEstDiferidoSinIntereses() {
        return codEstDiferidoSinIntereses;
    }

    public void setCodEstDiferidoSinIntereses(String codEstDiferidoSinIntereses) {
        this.codEstDiferidoSinIntereses = codEstDiferidoSinIntereses;
    }

    public boolean isPermitePagarGiftCard() {
        return permitePagarGiftCardCorriente || permitePagarGiftCardDiferido;
    }

    public boolean isAdmiteAbonoReservacion() {
        return admiteAbonoReservacionCorriente || admiteAbonoReservacionDiferido;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof MedioPagoBean) {
            MedioPagoBean other = (MedioPagoBean) object;
            if ((codMedioPago == null && other.codMedioPago != null) || (this.codMedioPago != null && !this.codMedioPago.equals(other.codMedioPago))) {
                return false;
            }
        } else if (object instanceof String) {
            String codigo = (String) object;
            if (this.codMedioPago == null || !this.codMedioPago.equals(codigo)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public Byte getTipo() {
        if (isEfectivo()) {
            return null;
        }
        if (isOtros() || isCreditoDirecto() || isGiftCard()) {
            return TIPO_OTROS;
        }
        if (isTarjetaCredito()) {
            return TIPO_TARJETAS;
        }
        if (isContado()) {
            return TIPO_CONTADO;
        }
        return null;
    }

    // **** Tarjeta Sukasa *****//
    public boolean isTarjetaSukasa() {
        return tarjetaSukasa;
    }

    public void setTarjetaSukasa(boolean tarjetaSukasa) {
        this.tarjetaSukasa = tarjetaSukasa;
    }

    public String getTarjetaSukasa() {
        return (tarjetaSukasa) ? TRUE : FALSE;
    }

    public void setTarjetaSukasa(String tarjetaSukasa) {
        this.tarjetaSukasa = tarjetaSukasa.equals(TRUE);
    }

    public String getTarjetaSukasaTexto() {
        return this.isTarjetaSukasa() ? "Sí" : "";
    }

    @Override
    public String toString() {
        return codMedioPago + " // " + desMedioPago;
    }

    public Character getTipoPagoCorriente() {
        return tipoPagoCorriente;
    }

    public void setTipoPagoCorriente(Character tipoPagoCorriente) {
        this.tipoPagoCorriente = tipoPagoCorriente;
    }

    public void setTipoPagoCorriente(String tipoPagoCorrienteS) {
        this.tipoPagoCorriente = tipoPagoCorrienteS.charAt(0);
    }

    public Character getTipoPagoDiferido() {
        return tipoPagoDiferido;
    }

    public void setTipoPagoDiferido(Character tipoPagoDiferido) {
        this.tipoPagoDiferido = tipoPagoDiferido;
    }

    public void setTipoPagoDiferido(String tipoPagoDiferidoS) {
        this.tipoPagoDiferido = tipoPagoDiferidoS.charAt(0);
    }

    public boolean isAdmiteAutorizacionAutomaticaDiferido() {
        return admiteAutorizacionAutomaticaDiferido;
    }

    public void setAdmiteAutorizacionAutomaticaDiferido(boolean admiteAutorizacionAutomaticaDiferido) {
        this.admiteAutorizacionAutomaticaDiferido = admiteAutorizacionAutomaticaDiferido;
    }

    public boolean isAdmiteAutorizacionAutomaticaCorriente() {
        return admiteAutorizacionAutomaticaCorriente;
    }

    public void setAdmiteAutorizacionAutomaticaCorriente(boolean admiteAutorizacionAutomaticaCorriente) {
        this.admiteAutorizacionAutomaticaCorriente = admiteAutorizacionAutomaticaCorriente;
    }

    public boolean isAdmiteAutorizacionManualCorriente() {
        return admiteAutorizacionManualCorriente;
    }

    public void setAdmiteAutorizacionManualCorriente(boolean admiteAutorizacionManualCorriente) {
        this.admiteAutorizacionManualCorriente = admiteAutorizacionManualCorriente;
    }

    public boolean isAdmiteAutorizacionManualDiferido() {
        return admiteAutorizacionManualDiferido;
    }

    public void setAdmiteAutorizacionManualDiferido(boolean admiteAutorizacionManualDiferido) {
        this.admiteAutorizacionManualDiferido = admiteAutorizacionManualDiferido;
    }

    public boolean isRequiereAutorizacionLecturaManual() {
        return requiereAutorizacionLecturaManual;
    }

    public void setRequiereAutorizacionLecturaManual(boolean requiereAutorizacionLecturaManual) {
        this.requiereAutorizacionLecturaManual = requiereAutorizacionLecturaManual;
    }

    public void setAdmiteAutorizacionAutomaticaDiferido(String string) {
        this.admiteAutorizacionAutomaticaDiferido = string.equals(TRUE);
    }

    public void setAdmiteAutorizacionAutomaticaCorriente(String string) {
        this.admiteAutorizacionAutomaticaCorriente = string.equals(TRUE);
    }

    public void setRequiereAutorizacionLecturaManual(String string) {
        this.requiereAutorizacionLecturaManual = string.equals(TRUE);
    }

    public void setAdmiteAutorizacionManualDiferido(String string) {
        this.admiteAutorizacionManualDiferido = string.equals(TRUE);
    }

    public void setAdmiteAutorizacionManualCorriente(String string) {
        this.admiteAutorizacionManualCorriente = string.equals(TRUE);
    }

    public BigDecimal getMontoMaximoAutorizacion() {
        return montoMaximoAutorizacion;
    }

    public void setMontoMaximoAutorizacion(BigDecimal montoMaximoAutorizacion) {
        this.montoMaximoAutorizacion = montoMaximoAutorizacion;
    }

    public String getCodMedPagElec() {
        return codMedPagElec;
    }

    public void setCodMedPagElec(String codMedPagElec) {
        this.codMedPagElec = codMedPagElec;
    }

    public String getCodBan() {
        return codBan;
    }

    public void setCodBan(String codBan) {
        this.codBan = codBan;
    }

}
