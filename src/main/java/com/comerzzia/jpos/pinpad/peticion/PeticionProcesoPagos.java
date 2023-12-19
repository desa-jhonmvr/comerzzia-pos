/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.pinpad.peticion;

import com.comerzzia.jpos.pinpad.PinPadAutomatico;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.promociones.cuotas.PromocionTipoMesesGracia;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionPagoTicket;
import com.comerzzia.util.cadenas.Cadena;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author SMLM
 */
public class PeticionProcesoPagos {
    
    public static String PAGO_CORRIENTE = "01";
    public static String PAGO_DIFERIDO = "02";
    public static String ANULACION_PAGO = "03";
    public static String REVERSO_PAGO = "04";
    
    private String tipoTransaccion;
    private String codigoIdentificacionRed;
    private String codigoDiferido;
    private String plazoDiferido;
    private String mesesGracia;
    private String diferidoIntereses;
    private String montoTotal;
    private String montoBaseIva;
    private String montoBaseNoIva;
    private String impuestoIva;
    private String impuestoServicioTransaccion;
    private String propinaTransaccion;
    private String montoFijo;
    private String secuencialTransaccion;
    private String horaTransaccion;
    private String fechaTransaccion;
    private String numeroAutorizacion;
    private String mid;
    private String tid;
    private boolean anulacion;
//     Valores para Plan D
//     private String cid;
//     private String filler1;
//    private String filler2;

    public PeticionProcesoPagos(PagoCredito pago, boolean anulacion){
        this.anulacion = anulacion;
        if(anulacion){
            this.tipoTransaccion=ANULACION_PAGO;
        } else {
            if(pago.getPlanSeleccionado().getVencimiento().isCorriente()){
                this.tipoTransaccion=PAGO_CORRIENTE;
            } else {
                this.tipoTransaccion=PAGO_DIFERIDO;
            }
        }
        if(pago.getPlanSeleccionado().getVencimiento().isCorriente()){
            this.codigoIdentificacionRed = pago.getMedioPagoActivo().getTipoPagoCorriente()=='D'?"1":"2";
        } else {
            this.codigoIdentificacionRed = pago.getMedioPagoActivo().getTipoPagoDiferido()=='D'?"1":"2";
        }
        //if(pago.getPlanSeleccionado().getVencimiento().isCorriente()){
        //    this.codigoDiferido="00";
        //} else {
            this.codigoDiferido = Numero.completaconCeros(pago.getPlanSeleccionado().getVencimiento().getTipoCredito(), 2);
        //}
        this.plazoDiferido = "  ";
        if(!pago.getPlanSeleccionado().getVencimiento().isCorriente()){
            this.plazoDiferido = Numero.completaconCeros(pago.getPlanSeleccionado().getNumCuotas(), 2);
        }
        this.mesesGracia = "  ";
        if(pago.getPromociones() != null){
            for(PromocionPagoTicket p:pago.getPromociones()){
                if(p.getTipoPromocion().isPromocionTipoMesesGracia()){
                    PromocionTipoMesesGracia promo = (PromocionTipoMesesGracia) Sesion.getPromocion(p.getIdPromocion());
                    this.mesesGracia = Numero.completaconCeros(promo.getNumMeses(), 2);
                    break;
                }
            }
        }
        this.diferidoIntereses=" ";
/*        if(!pago.getPlanSeleccionado().getVencimiento().isCorriente()){
            if(pago.getPlanSeleccionado().getImporteInteres().compareTo(BigDecimal.ZERO)>0){
                this.diferidoIntereses = "1";
            } else {
                this.diferidoIntereses = "0";
            }
        }*/
        DecimalFormat df = new DecimalFormat("#0.00");
        df.setMaximumFractionDigits(2);   
        
        this.montoTotal = Numero.completaconCeros(df.format(pago.getUstedPaga()).replace(",", ""), 12);
        
        this.montoBaseIva = Numero.completaconCeros(df.format(pago.getSubtotalIva12()).replace(",", ""), 12);
        this.montoBaseNoIva = Numero.completaconCeros(df.format(pago.getSubtotalIva0()).replace(",", ""), 12);
        this.impuestoIva = Numero.completaconCeros(df.format(pago.getIva()).replace(",", ""), 12);
        
        this.impuestoServicioTransaccion = Cadena.completaconBlancos("",12);
        this.propinaTransaccion = Cadena.completaconBlancos("", 12);
        this.montoFijo = Cadena.completaconBlancos("",12);
        this.secuencialTransaccion = Cadena.completaconBlancos("",6);
        if(anulacion){
            this.secuencialTransaccion = Cadena.completaconBlancos(pago.getPinpadRespuesta().getSecuencialTransaccion(), 6);
        }
        Date date = new Date();
        SimpleDateFormat formatHora = new SimpleDateFormat("HHmmss");
        SimpleDateFormat formatFecha = new SimpleDateFormat("yyyyMMdd");
        this.horaTransaccion = formatHora.format(date);
        this.fechaTransaccion = formatFecha.format(date);
        this.numeroAutorizacion = Cadena.completaconBlancos("", 6);
        if(anulacion){
            this.numeroAutorizacion = Cadena.completaconBlancos(pago.getPinpadRespuesta().getNumeroAutorizacion(), 6);
        }
        this.mid = pago.getMID();
        this.tid = pago.getTID();
//        Implementacion Plan D
//        //Identificador de la Caja con que está emparejado el PINPAD. Único por Caja Justificados con blancos a la derecha
////       this.cid=pago.getcid();
//         this.filler1=" ";
//         this.cid="               ";
////         this.cid="CP"+"0000000000001";
//        this.filler2=Cadena.completaconBlancos("", 20);
    }
    
    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getCodigoIdentificacionRed() {
        return codigoIdentificacionRed;
    }

    public void setCodigoIdentificacionRed(String codigoIdentificacionRed) {
        this.codigoIdentificacionRed = codigoIdentificacionRed;
    }

    public String getCodigoDiferido() {
        return codigoDiferido;
    }

    public void setCodigoDiferido(String codigoDiferido) {
        this.codigoDiferido = codigoDiferido;
    }

    public String getPlazoDiferido() {
        return plazoDiferido;
    }

    public void setPlazoDiferido(String plazoDiferido) {
        this.plazoDiferido = plazoDiferido;
    }

    public String getMesesGracia() {
        return mesesGracia;
    }

    public void setMesesGracia(String mesesGracia) {
        this.mesesGracia = mesesGracia;
    }

    public String getDiferidoIntereses() {
        return diferidoIntereses;
    }

    public void setDiferidoIntereses(String diferidoIntereses) {
        this.diferidoIntereses = diferidoIntereses;
    }

    public String getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(String montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getMontoBaseIva() {
        return montoBaseIva;
    }

    public void setMontoBaseIva(String montoBaseIva) {
        this.montoBaseIva = montoBaseIva;
    }

    public String getMontoBaseNoIva() {
        return montoBaseNoIva;
    }

    public void setMontoBaseNoIva(String montoBaseNoIva) {
        this.montoBaseNoIva = montoBaseNoIva;
    }

    public String getImpuestoIva() {
        return impuestoIva;
    }

    public void setImpuestoIva(String impuestoIva) {
        this.impuestoIva = impuestoIva;
    }

    public String getImpuestoServicioTransaccion() {
        return impuestoServicioTransaccion;
    }

    public void setImpuestoServicioTransaccion(String impuestoServicioTransaccion) {
        this.impuestoServicioTransaccion = impuestoServicioTransaccion;
    }

    public String getPropinaTransaccion() {
        return propinaTransaccion;
    }

    public void setPropinaTransaccion(String propinaTransaccion) {
        this.propinaTransaccion = propinaTransaccion;
    }

    public String getMontoFijo() {
        return montoFijo;
    }

    public void setMontoFijo(String montoFijo) {
        this.montoFijo = montoFijo;
    }

    public String getSecuencialTransaccion() {
        return secuencialTransaccion;
    }

    public void setSecuencialTransaccion(String secuencialTransaccion) {
        this.secuencialTransaccion = secuencialTransaccion;
    }

    public String getHoraTransaccion() {
        return horaTransaccion;
    }

    public void setHoraTransaccion(String horaTransaccion) {
        this.horaTransaccion = horaTransaccion;
    }

    public String getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(String fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public boolean isAnulacion() {
        return anulacion;
    }

    public void setAnulacion(boolean anulacion) {
        this.anulacion = anulacion;
    }

//    public String getCid() {
//        return cid;
//    }
//
//    public void setCid(String cid) {
//        this.cid = cid;
//    }
//
//    public String getFiller1() {
//        return filler1;
//    }
//
//    public void setFiller1(String filler1) {
//        this.filler1 = filler1;
//    }
//
//    public String getFiller2() {
//        return filler2;
//    }
//
//    public void setFiller2(String filler2) {
//        this.filler2 = filler2;
//    }
    
    public String dameTramaPagos(){
            String tramaPagos = PinPadAutomatico.PROCESO_PAGOS;
            tramaPagos+=getTipoTransaccion();
            tramaPagos+=getCodigoIdentificacionRed();
            tramaPagos+=getCodigoDiferido();
            tramaPagos+=getPlazoDiferido();
            tramaPagos+=getMesesGracia();
//             //Filler  implementacion plan D
//            tramaPagos += getFiller1();
            //Se elimina diferido Plan D
            tramaPagos+=getDiferidoIntereses();
            tramaPagos+=getMontoTotal();
            tramaPagos+=getMontoBaseIva();
            tramaPagos+=getMontoBaseNoIva();
            tramaPagos+=getImpuestoIva();
            tramaPagos+=getImpuestoServicioTransaccion();
            tramaPagos+=getPropinaTransaccion();
            tramaPagos+=getMontoFijo();
            tramaPagos+=getSecuencialTransaccion();
            tramaPagos+=getHoraTransaccion();
            tramaPagos+=getFechaTransaccion();
            tramaPagos+=getNumeroAutorizacion();
            tramaPagos+=getMid();
            tramaPagos+=getTid();
//            //se agregar los dos capos para plan D
//             tramaPagos += getCid();
//            tramaPagos += getFiller2();
            
            return tramaPagos;
    }
}
