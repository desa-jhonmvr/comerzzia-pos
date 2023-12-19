/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import static com.comerzzia.jpos.servicios.print.objetos.PrintVoucher.TIPO_TARJETA_CREDITO;
import static com.comerzzia.jpos.servicios.print.objetos.PrintVoucher.TIPO_TARJETA_SUKASA;
import com.comerzzia.util.cadenas.Cadena;
import es.mpsistemas.util.fechas.Fecha;
import java.math.RoundingMode;

/**
 *
 * @author SMLM
 */
public class PrintVoucherAnulacion  extends PrintDocument {
    
    private String terminalId;
    private String merchantId;
    private String modoLectura;
    private String nombreGrupoTarjeta;
    private String numeroTarjeta;
    private String secuencialPinpad;
    private String secuencialTransaccion;
    private String nombreBancoAdquiriente;
    private String numeroAutorizacion;
    private String arqc;
    private String aidemv;
    private String total;
    
    public PrintVoucherAnulacion(PagoCredito pagoCredito) {
        super(true, new Fecha());
        
        terminalId = pagoCredito.getPinpadRespuesta().getTerminalId();
        merchantId = pagoCredito.getPinpadRespuesta().getMerchantId();
        modoLectura = pagoCredito.getPinpadRespuesta().getModoLectura();
        nombreGrupoTarjeta = pagoCredito.getPinpadRespuesta().getNombreGrupoTarjeta();
        numeroTarjeta = pagoCredito.getTarjetaCredito().getNumeroOculto();
        secuencialPinpad = pagoCredito.getPinpadRespuesta().getNumeroLote();
        secuencialTransaccion = pagoCredito.getPinpadRespuesta().getSecuencialTransaccion();
        nombreBancoAdquiriente = pagoCredito.getPinpadRespuesta().getNombreBancoAdquiriente();
        numeroAutorizacion = pagoCredito.getNumeroAutorizacionTarjeta();
        arqc = pagoCredito.getPinpadRespuesta().getArqc();
        aidemv = pagoCredito.getPinpadRespuesta().getAidEmv();
        
        
        total = "-"+pagoCredito.getUstedPaga().add(pagoCredito.getImporteInteres()).setScale(2, RoundingMode.HALF_UP).toString();
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

    public String getNombreGrupoTarjeta() {
        return nombreGrupoTarjeta;
    }

    public void setNombreGrupoTarjeta(String nombreGrupoTarjeta) {
        this.nombreGrupoTarjeta = nombreGrupoTarjeta;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getSecuencialPinpad() {
        return secuencialPinpad;
    }

    public void setSecuencialPinpad(String secuencialPinpad) {
        this.secuencialPinpad = secuencialPinpad;
    }

    public String getSecuencialTransaccion() {
        return secuencialTransaccion;
    }

    public void setSecuencialTransaccion(String secuencialTransaccion) {
        this.secuencialTransaccion = secuencialTransaccion;
    }

    public String getNombreBancoAdquiriente() {
        return nombreBancoAdquiriente;
    }

    public void setNombreBancoAdquiriente(String nombreBancoAdquiriente) {
        this.nombreBancoAdquiriente = nombreBancoAdquiriente;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public String getArqc() {
        return arqc;
    }

    public void setArqc(String arqc) {
        this.arqc = arqc;
    }

    public String getAidemv() {
        return aidemv;
    }

    public void setAidemv(String aidemv) {
        this.aidemv = aidemv;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    
    public String getMidTidML(){
        return merchantId.trim() + " - " + terminalId.trim() + " - " + modoLectura.trim(); 
    }    
}
