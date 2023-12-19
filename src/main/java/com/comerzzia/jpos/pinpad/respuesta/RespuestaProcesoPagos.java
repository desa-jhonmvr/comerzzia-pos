/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.pinpad.respuesta;

import com.comerzzia.jpos.pinpad.PinPadException;

/**
 *
 * @author SMLM
 */
public class RespuestaProcesoPagos extends RespuestaPinPad {
    private String codigoIdentificacionRed;
    private String codigoRespuestaTransaccion;
    private String secuencialTransaccion;
    private String numeroLote;
    private String horaTransaccion;
    private String fechaTransaccion;
    private String numeroAutorizacion;
    private String terminalId;
    private String merchantId;
    private String financiamiento;
    private String publicidad;
    private String codigoBancoAdquiriente;
    private String numeroBancoAdquiriente;
    private String nombreBancoAdquiriente;
    private String nombreGrupoTarjeta;
    private String modoLectura;
    private String nombreTarjetaHabiente;
    private String montoFijo;
    private String identificacionEmv;
    private String aidEmv;
    private String valorEmv;
    private String verificacionPin;
    private String arqc;
//    //plan D
     private String tvr;
    private String tsi;
    private String numeroTarjetaTruncado;
    private String fechavencimientoTarjeta;
    private String numeroTarjetaEncriptado;
    private String filler;

    public RespuestaProcesoPagos(){
    }
    
    public RespuestaProcesoPagos(String respuesta) throws PinPadException{
//         // Plan D
//         if (respuesta == null || respuesta.length() < 490) {
//            throw new PinPadException("Se ha recibido una trama de respuesta incorrecta. Respuesta: " + respuesta);
//        }
        if(respuesta == null || respuesta.length()<364){
            throw new PinPadException ("Se ha recibido una trama de respuesta incorrecta. Respuesta: "+respuesta);
        }
// //Analizar porque no va en Plan D
//        setNumeroBancoAdquiriente("");
//        //Respuests Acorde al Documento Especificaciones Funcionales PINPAD Multiredes_REDES 4_2
//        //Respuesta Total
//        setRespuesta(respuesta);
//        //Tipo de Mensaje longitud 2
//        setTipoMensaje(respuesta.substring(4, 6));
//        //Código de Respuesta de Mensaje longitud 2
//        setCodigoRespuestaMensaje(respuesta.substring(6, 8));
//        //Código de Identificación de la Red longitud 2
//        setCodigoIdentificacionRed(respuesta.substring(8, 10));
//        //Código de Respuesta del Autorizador longitud 2
//        setCodigoRespuestaTransaccion(respuesta.substring(10, 12));
//        //Mensaje de Respuesta longitud 20
//        setMensajeRespuesta(respuesta.substring(12,32));
//        //Secuencial Transacción longitud 6
//        setSecuencialTransaccion(respuesta.substring(32, 38));
//        //Número de Lote longitud 6
//        setNumeroLote(respuesta.substring(38, 44));
//        //Hora de la Transacción longitud 6
//        setHoraTransaccion(respuesta.substring(44,50));
//        //Fecha de la Transacción longitud 8
//        setFechaTransaccion(respuesta.substring(50, 58));
//        //Número de Autorización longitud 6
//        setNumeroAutorizacion(respuesta.substring(58, 64));
//        //Terminal Id longitud 8
//        setTerminalId(respuesta.substring(64, 72));
//        //Merchant Id longitud 15
//        setMerchantId(respuesta.substring(72, 87));
//        //Valor del Interés o Financiamiento  longitud 12
//        setFinanciamiento(respuesta.substring(87, 99));
//        //Mensaje para Impresión de Premios o Publicidad longitud 80
//        String publicidad = respuesta.substring(99, 179).trim();
//        //Si viene 016, no considerarlo porque no se maneja voucher rápido.
//        if (publicidad.equals("016")) {
//            setPublicidad("");
//        } else {
//            setPublicidad(publicidad);
//        }
//        //Código de Banco Adquiriente longitud 3
//        setCodigoBancoAdquiriente(respuesta.substring(179, 182));
//        //Nombre de Banco Adquiriente longitud 30
//        setNombreBancoAdquiriente(respuesta.substring(182, 212));
//        //Nombre del Grupo de Tarjeta longitud 25
//        setNombreGrupoTarjeta(respuesta.substring(212, 237));
//        //Modo de Lectura longirud 2
//        setModoLectura(respuesta.substring(237, 239));
//        //Nombre del Tarjeta-Habiente Longitud 40
//        setNombreTarjetaHabiente(respuesta.substring(239, 279));
//        //Monto Fijo (solo aplica para transacciones en Gasolineras) longitud 12
//        setMontoFijo(respuesta.substring(279, 291));
//        //Identificación de Aplicación EMV (Aplicación Label o Application Preferred Name) (Solo aplica para modo de lectura con Chip - 03) longitud 20
//        setIdentificacionEmv(respuesta.substring(291, 311));
//        //AID – EMV (Solo aplica para modo de lectura con Chip - 03) longitud 20
//        setAidEmv(respuesta.substring(311, 331));
//        //Tipo de Criptograma y valor – EMV (Solo aplica para modo de lectura con Chip - 03) longitud 22
//        setValorEmv(respuesta.substring(331, 353));
//        //Verificación de PIN (Solo aplica para modo de lectura con Chip - 03) longitud 15
//        setVerificacionPin(respuesta.substring(353, 368));
//        //ARQC (Solo aplica para modo de lectura con Chip - 03) longitud 16 antes 364
//        setArqc(respuesta.substring(368, 384));
//        //TVR longitud de 10
//        setTvr(respuesta.substring(384, 394));
//        //TSI longitud 4
//        setTsi(respuesta.substring(394, 398));
//        //Número de Tarjeta Truncado longitud 25
//        setNumeroTarjetaTruncado(respuesta.substring(398, 423));
//        //Fecha de vencimiento de la Tarjeta longitud longitud 4
//        setFechavencimientoTarjeta(respuesta.substring(423, 427));
//        if (respuesta.length() == 494) {
//          //Número de Tarjeta Encriptado longitud longitud 40, 64
//                setNumeroTarjetaEncriptado(respuesta.substring(427, 467));
//                //Filler longitud 27
//                setFiller(respuesta.substring(467, 494));
//        } else {
//            if (respuesta.length() > 494) {
//                       //Número de Tarjeta Encriptado longitud longitud 40, 64
//            setNumeroTarjetaEncriptado(respuesta.substring(427, 491));
//            //Filler longitud 27 511 en lugar de 518
//            setFiller(respuesta.substring(491, 518));
//           
//            }
//        }

        //plan C
        setRespuesta(respuesta);
        setTipoMensaje(respuesta.substring(0,2));
        setCodigoRespuestaMensaje(respuesta.substring(2,4));
        setCodigoIdentificacionRed(respuesta.substring(4,6));
        setCodigoRespuestaTransaccion(respuesta.substring(6,8));
        setMensajeRespuesta(respuesta.substring(8,28));
        setSecuencialTransaccion(respuesta.substring(28,34));
        setNumeroLote(respuesta.substring(34,40));
        setHoraTransaccion(respuesta.substring(40,46));
        setFechaTransaccion(respuesta.substring(46,54));
        setNumeroAutorizacion(respuesta.substring(54,60));
        setTerminalId(respuesta.substring(60,68));
        setMerchantId(respuesta.substring(68,83));
        setFinanciamiento(respuesta.substring(83,95));
        String publicidad = respuesta.substring(95,175).trim();
        //Si viene 016, no considerarlo porque no se maneja voucher rápido.
        if(publicidad.equals("016")){
            setPublicidad("");
        } else {
            setPublicidad(publicidad);
        }
        setCodigoBancoAdquiriente(respuesta.substring(175,178));
        setNombreBancoAdquiriente(respuesta.substring(178,208));
        setNombreGrupoTarjeta(respuesta.substring(208,233));
        setModoLectura(respuesta.substring(233,235));
        setNombreTarjetaHabiente(respuesta.substring(235,275));
        setMontoFijo(respuesta.substring(275,287));
        setIdentificacionEmv(respuesta.substring(287,307));
        setAidEmv(respuesta.substring(307,327));
        setValorEmv(respuesta.substring(327,349));
        setVerificacionPin(respuesta.substring(349,364));
        setArqc(respuesta.substring(364));            
    }

    public String getTvr() {
        return tvr;
    }

    public void setTvr(String tvr) {
        this.tvr = tvr;
    }

    public String getTsi() {
        return tsi;
    }

    public void setTsi(String tsi) {
        this.tsi = tsi;
    }

    public String getNumeroTarjetaTruncado() {
        return numeroTarjetaTruncado;
    }

    public void setNumeroTarjetaTruncado(String numeroTarjetaTruncado) {
        this.numeroTarjetaTruncado = numeroTarjetaTruncado;
    }

    public String getFechavencimientoTarjeta() {
        return fechavencimientoTarjeta;
    }

    public void setFechavencimientoTarjeta(String fechavencimientoTarjeta) {
        this.fechavencimientoTarjeta = fechavencimientoTarjeta;
    }

    public String getNumeroTarjetaEncriptado() {
        return numeroTarjetaEncriptado;
    }

    public void setNumeroTarjetaEncriptado(String numeroTarjetaEncriptado) {
        this.numeroTarjetaEncriptado = numeroTarjetaEncriptado;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public String getCodigoIdentificacionRed() {
        return codigoIdentificacionRed;
    }

    public void setCodigoIdentificacionRed(String codigoIdentificacionRed) {
        this.codigoIdentificacionRed = codigoIdentificacionRed;
    }

    public String getCodigoRespuestaTransaccion() {
        return codigoRespuestaTransaccion;
    }

    public void setCodigoRespuestaTransaccion(String codigoRespuestaTransaccion) {
        this.codigoRespuestaTransaccion = codigoRespuestaTransaccion;
    }

    public String getSecuencialTransaccion() {
        return secuencialTransaccion;
    }

    public void setSecuencialTransaccion(String secuencialTransaccion) {
        this.secuencialTransaccion = secuencialTransaccion;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
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

    public String getFinanciamiento() {
        return financiamiento;
    }

    public void setFinanciamiento(String financiamiento) {
        this.financiamiento = financiamiento;
    }

    public String getPublicidad() {
        return publicidad;
    }

    public void setPublicidad(String publicidad) {
        this.publicidad = publicidad;
    }

    public String getCodigoBancoAdquiriente() {
        return codigoBancoAdquiriente;
    }

    public void setCodigoBancoAdquiriente(String codigoBancoAdquiriente) {
        this.codigoBancoAdquiriente = codigoBancoAdquiriente;
    }

    public String getNumeroBancoAdquiriente() {
        return numeroBancoAdquiriente;
    }

    public void setNumeroBancoAdquiriente(String numeroBancoAdquiriente) {
        this.numeroBancoAdquiriente = numeroBancoAdquiriente;
    }

    public String getNombreBancoAdquiriente() {
        return nombreBancoAdquiriente;
    }

    public void setNombreBancoAdquiriente(String nombreBancoAdquiriente) {
        this.nombreBancoAdquiriente = nombreBancoAdquiriente;
    }

    public String getNombreGrupoTarjeta() {
        return nombreGrupoTarjeta;
    }

    public void setNombreGrupoTarjeta(String nombreGrupoTarjeta) {
        this.nombreGrupoTarjeta = nombreGrupoTarjeta;
    }

    public String getModoLectura() {
        return modoLectura;
    }

    public void setModoLectura(String modoLectura) {
        this.modoLectura = modoLectura;
    }

    public String getNombreTarjetaHabiente() {
        return nombreTarjetaHabiente;
    }

    public void setNombreTarjetaHabiente(String nombreTarjetaHabiente) {
        this.nombreTarjetaHabiente = nombreTarjetaHabiente;
    }

    public String getMontoFijo() {
        return montoFijo;
    }

    public void setMontoFijo(String montoFijo) {
        this.montoFijo = montoFijo;
    }

    public String getIdentificacionEmv() {
        return identificacionEmv;
    }

    public void setIdentificacionEmv(String identificacionEmv) {
        this.identificacionEmv = identificacionEmv;
    }

    public String getAidEmv() {
        return aidEmv;
    }

    public void setAidEmv(String aidEmv) {
        this.aidEmv = aidEmv;
    }

    public String getValorEmv() {
        return valorEmv;
    }

    public void setValorEmv(String valorEmv) {
        this.valorEmv = valorEmv;
    }

    public String getVerificacionPin() {
        return verificacionPin;
    }

    public void setVerificacionPin(String verificacionPin) {
        this.verificacionPin = verificacionPin;
    }

    public String getArqc() {
        return arqc;
    }

    public void setArqc(String arqc) {
        this.arqc = arqc;
    }
    
}
