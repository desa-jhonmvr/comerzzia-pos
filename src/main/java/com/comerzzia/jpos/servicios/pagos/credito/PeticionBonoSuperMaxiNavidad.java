/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pagos.credito;

import com.comerzzia.jpos.dto.supermaxi.RespuestaSupermaxiDTO;
import com.comerzzia.jpos.dto.supermaxi.SolicitudSupermaxiDTO;
import com.comerzzia.jpos.entity.db.SriCaja;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasException;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasServices;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.DatosConfiguracion;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.util.ClienteRest;
import com.comerzzia.util.cadenas.Cadena;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author DAS
 */
public class PeticionBonoSuperMaxiNavidad {

    protected static Logger log = Logger.getMLogger(PeticionBonoSuperMaxiNavidad.class);
    private PagoCredito pago;

    public static int COMPRA = 1;
    public static int ANULACION = 2;
    public static int REVERSA = 3;
    public static int CONSULTA = 4;

    private String tipoMensaje;
    private String codigoProceso;
    private String numeroTarjeta;
    private String cvv;
    private String montoTransaccion;
    private String secuencialTransaccion;
    private String hora;
    private String fecha;
    private String fechaTarjetaA;
    private String fechaTarjetaM;
    private String modoEntrada;
    private String numeroAutorizacion;
    private String filler;
    private String primeraCompra;
    private String tipoCreditoMeses;
    private String codigoProveedor;
    private String campoPrivado;
    private String cupoCompartido;
    private String formaPago;
    private String tipoTransaccion;
    private String pinBlock;
    private String tipoMoneda;
    private String codigoEstablecimiento;
    private String codigoCajero;
    private String codigoTerminal;
    Calendar c = Calendar.getInstance();

    public static final String ERROR_TIMEOUT = "ERROR TIMEOUT";
    public static final String ERROR_TIMEOUT_OBSERVACION = "Existen problemas de comunicaci\u00F3n, Solicitar autorizaci\u00F3n a la corporaci\u00F3n";
    public static final String ERROR_TARJETA_NO_VALIDA = "Tarjeta no V\u00E1lida";
    public static final String URL_ENVIO_SUPERMAXI = "/supermaxi/consultaTarjeta";

    public PeticionBonoSuperMaxiNavidad(PagoCredito pago, int mensaje, String factura, String numAutorizacion, String numSecuencialEnvio) {
        tipoMensaje = "0200";
        if (mensaje == REVERSA) {
            tipoMensaje = "0400";
        }
        //Validacion para credito temporada

        if (Variables.getVariable(Variables.ACTIVO_SERVICIO_WEB_SUPERMAXI).equals("S")) {
            if (pago.getMedioPagoActivo().getCodMedioPago().equals("239") || pago.getMedioPagoActivo().getCodMedioPago().equals("220")) {
                codigoProceso = "003000";
            } else {
                codigoProceso = "004000"; //Compras
            }
        } else {
            if (pago.getMedioPagoActivo().getCodMedioPago().equals("239")) {
                codigoProceso = "003000";
            } else {
                codigoProceso = "004000"; //Compras
            }
        }
//        codigoProceso = "004000"; //Compras
        if (mensaje == ANULACION) {
            if (pago.getMedioPagoActivo().getCodMedioPago().equals("239") || pago.getMedioPagoActivo().getCodMedioPago().equals("220")) {
                codigoProceso = "203000";
            } else {
                codigoProceso = "204000"; //Anulaciones
            }
        }

        int longitudTarjetaYCaducidad = pago.getTarjetaCredito().getNumero().length() + String.valueOf(pago.getTarjetaCredito().getCaducidad()).length() + 1;
        numeroTarjeta = longitudTarjetaYCaducidad + pago.getTarjetaCredito().getNumero();
        numeroTarjeta = numeroTarjeta + "=" + String.valueOf(pago.getTarjetaCredito().getCaducidad());
        numeroTarjeta = String.format("%-39s", numeroTarjeta).replace(' ', '0');
        cvv = "000";
        montoTransaccion = pago.getUstedPaga().toPlainString();
        montoTransaccion = montoTransaccion.substring(0, montoTransaccion.indexOf('.')) + montoTransaccion.substring(montoTransaccion.indexOf('.') + 1, montoTransaccion.length());
        montoTransaccion = Numero.completaconCeros(montoTransaccion, 12);
        //Cambio para solucionar problema de trama Rd
        //cambio Rd para tener parte entera de secuencial 
        //Cambio M.E se trae secuencial de la tabla SRI_TIENDAS_CAJAS_TBL para envio tramaSupermaxi.
        DatosConfiguracion datosConf = Sesion.getDatosConfiguracion();
        SriCaja cajaSecuencial = null;
        try {
            cajaSecuencial = TiendasServices.consultarSecuencialCajaSupermaxi(datosConf.getCodcaja(), Sesion.getTienda().getSriTienda().getCodalmsri());
        } catch (TiendasException ex) {
            java.util.logging.Logger.getLogger(PeticionBonoSuperMaxiNavidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        int idsecuencial = cajaSecuencial.getSecuencialSuper();
        SriCaja sriCaja = Sesion.getTienda().getSriTienda().getCajaActiva();
        if (idsecuencial >= 99980) { //El secuencial no debe superar los 5 digitos por lo que se reinicia el numero cuando llegue a 99980
            sriCaja.setSecuencialSuper(1);
        } else {
            sriCaja.setSecuencialSuper(idsecuencial + 1);
        }
        try {
            TiendasServices.actualizarSecuencial(sriCaja);
        } catch (TiendasException ex) {
            java.util.logging.Logger.getLogger(PeticionBonoSuperMaxiNavidad.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("idfactura :"+factura);
//        String  nuevosecuencialCreado=factura.substring(1);
//        System.out.println("Secuncial menos un caracter "+nuevosecuencialCreado);
//        secuencialTransaccion = Numero.completaconCeros((nuevosecuencialCreado+pago.getIdTramaSuper()), 6);//6
//        System.out.println("Nuevo Secuencial: "+secuencialTransaccion); 
        System.out.println("Secuencial menos un caracter " + idsecuencial);
        //Se reali
        String valorAenviar = idsecuencial + pago.getIdTramaSuper();
        while (valorAenviar.length() > 5) {
            valorAenviar = valorAenviar.substring(1);
        }
//        secuencialTransaccion = Numero.completaconCeros((idsecuencial+pago.getIdTramaSuper()), 6);//6
        String secuencialSuper = Numero.completaconCeros((valorAenviar), 5);//6
        if (Sesion.getTicket() != null) {
            secuencialTransaccion = "1" + secuencialSuper;
        } else {
            secuencialTransaccion = "2" + secuencialSuper;
        }
        if (mensaje == ANULACION) {
            secuencialTransaccion = numSecuencialEnvio;
        }
        System.out.println("Nuevo Secuencial: " + secuencialTransaccion);
        //Antes 
//        secuencialTransaccion = Numero.completaconCeros(factura, 6);//6

        SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmss");
        Date now = new Date();
        hora = sdfDate.format(now);

        sdfDate = new SimpleDateFormat("yyyyMMdd");
        fecha = sdfDate.format(now);

        modoEntrada = "02"; //Banda magnética
        if (pago.getLecturaBandaManual() != null) {
            modoEntrada = "01"; //Lectura manual
        }
        modoEntrada += "2";

        numeroAutorizacion = "000000";
        if (mensaje == ANULACION) {
            numeroAutorizacion = Numero.completaconCeros(numAutorizacion, 6);
        }
        int plazo = pago.getPlanSeleccionado().getMeses();
        codigoTerminal = "00" + Sesion.getCajaActual().getCajaActual().getCodalm() + Sesion.getCajaActual().getCajaActual().getCodcaja();
        codigoCajero = Numero.completaconCeros(Sesion.getUsuario().getIdUsuario().toString(), 6);
        //codigoEstablecimiento (abajo)

        tipoMoneda = "840";
        pinBlock = Cadena.completaconBlancos("", 16);
        tipoTransaccion = plazo > 1 ? "2" : "1"; //si el plazo es 1 va 1, si es > 1 va 2
        formaPago = "20"; //OK
        cupoCompartido = "1"; //OK
        codigoProveedor = "00";  //OK
        campoPrivado = Cadena.completaconBlancos("", 13);

        tipoCreditoMeses = "00000000"; //Coriente
        String establecimiento = pago.getMedioPagoActivo().getCodEstCorriente(); //Corriente
        if (pago.getNumCuotas() != null && pago.getNumCuotas() > 1) { //Diferido
            String meses = plazo + "";
            meses = String.format("%2s", meses).replace(' ', '0');

            if (pago.getImporteInteres() != null && pago.getImporteInteres().compareTo(BigDecimal.ZERO) > 0) { //Con intereses    
                establecimiento = pago.getMedioPagoActivo().getCodEstDiferidoConIntereses();
                tipoCreditoMeses = "0200" + meses + "00";
            } else { //Sin intereses
                establecimiento = pago.getMedioPagoActivo().getCodEstDiferidoSinIntereses();
                tipoCreditoMeses = "0100" + meses + "00";
            }
        }
        codigoEstablecimiento = Cadena.completaconCeros(establecimiento, 10);

        primeraCompra = " ";
        filler = Cadena.completaconBlancos("", 39);
    }

    public PeticionBonoSuperMaxiNavidad(String bonoSupermaxi) {
        //Tipo Mensaje Consultas
        tipoMensaje = "0200";
        //Validacion para credito temporada
        codigoProceso = "314000";

//        int longitudTarjetaYCaducidad = pago.getTarjetaCredito().getNumero().length() + String.valueOf(pago.getTarjetaCredito().getCaducidad()).length() + 1;
//        numeroTarjeta = longitudTarjetaYCaducidad + pago.getTarjetaCredito().getNumero();
//        numeroTarjeta = numeroTarjeta + "=" +  String.valueOf(pago.getTarjetaCredito().getCaducidad());
        //  numeroTarjeta = String.format("%-39s", bonoSupermaxi).replace(' ', '0');
        //int longitudTarjetaYCaducidad = pago.getTarjetaCredito().getNumero().length() + String.valueOf(pago.getTarjetaCredito().getCaducidad()).length() + 1;
        SimpleDateFormat dateCaducidadA = new SimpleDateFormat("yy");
        Date hoy = new Date();
        fechaTarjetaA = dateCaducidadA.format(hoy);
        int c = Integer.parseInt(fechaTarjetaA);
        c = c + 1;
        String fechaTarjetaC = String.valueOf(c);
        SimpleDateFormat dateCaducidadM = new SimpleDateFormat("MM");
        Date hoyM = new Date();
        fechaTarjetaM = dateCaducidadM.format(hoyM);
        fechaTarjetaC = fechaTarjetaC + fechaTarjetaM;
        fechaTarjetaC = Numero.completaconCeros(fechaTarjetaC, 4);
        int longitudTarjetaYCaducidad = bonoSupermaxi.length() + 5;
        numeroTarjeta = longitudTarjetaYCaducidad + bonoSupermaxi;
        numeroTarjeta = numeroTarjeta + "=" + fechaTarjetaC;
        numeroTarjeta = String.format("%-39s", numeroTarjeta).replace(' ', '0');
        cvv = "000";
        montoTransaccion = "0";
        montoTransaccion = Numero.completaconCeros(montoTransaccion, 12);
        secuencialTransaccion = "0";
        secuencialTransaccion = Numero.completaconCeros(secuencialTransaccion, 6);
        SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmss");
        Date now = new Date();
        hora = sdfDate.format(now);

        sdfDate = new SimpleDateFormat("yyyyMMdd");
        fecha = sdfDate.format(now);

        modoEntrada = "02"; //Banda magnética
//        if(pago.getLecturaBandaManual() != null){
//            modoEntrada = "01"; //Lectura manual
//        }
        modoEntrada += "2";

        numeroAutorizacion = "000000";
        codigoTerminal = "0";
        codigoTerminal = Numero.completaconCeros(codigoTerminal, 8);
        codigoCajero = "0";
        codigoCajero = Numero.completaconCeros(codigoCajero, 6);
        //codigoEstablecimiento (abajo)

        tipoMoneda = "840";
        pinBlock = Cadena.completaconBlancos("", 16);
        tipoTransaccion = "1";
        formaPago = "20"; //OK
        cupoCompartido = "1"; //OK
        codigoProveedor = "00";  //OK
        campoPrivado = Cadena.completaconBlancos("", 13);

        tipoCreditoMeses = "00000000"; //Coriente
        //String establecimiento = "00000000"; 
        String establecimiento;
        String codMedioPago = "220";
        MedioPagoBean mp;
        try {
            mp = MediosPago.getCodigoEstablecimiento(codMedioPago);
            establecimiento = mp.getCodEstCorriente();
            codigoEstablecimiento = Cadena.completaconCeros(establecimiento, 10);
        } catch (MedioPagoException ex) {
            java.util.logging.Logger.getLogger(PeticionBonoSuperMaxiNavidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        primeraCompra = " ";
        filler = Cadena.completaconBlancos("", 39);

    }

    public String devolverTrama() {
        return "10`0000" + tipoMensaje + codigoProceso + numeroTarjeta + cvv + montoTransaccion + secuencialTransaccion
                + hora + fecha + modoEntrada + numeroAutorizacion + codigoTerminal + codigoCajero
                + codigoEstablecimiento + tipoMoneda + pinBlock + tipoTransaccion + formaPago + cupoCompartido
                + codigoProveedor + campoPrivado + tipoCreditoMeses + primeraCompra + filler;
    }

    public String enviarTrama(String msg) throws SocketTimeoutException, IOException {
        String mensajeRecibido = null;
        DataInputStream entrada = null;
        DataOutputStream salida = null;
        byte buffer[] = new byte[512];

        Socket socket = null;
        try {
            log.debug("enviarTrama() - Enviando trama navideña - " + msg);
            //El formato de la cadena es host:puerto
            String cadena = Variables.getVariable(Variables.POS_CONFIG_HOST_AUTORIZADOR_BONO_NAVI);
            String host = cadena.substring(0, cadena.indexOf(":"));
            Integer puerto = new Integer(cadena.substring(cadena.indexOf(":") + 1, cadena.length()));
            log.debug("host : " + host);
            log.debug("puerto : " + puerto);
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, puerto), 2000);
            if (socket != null) {
                int timeout = new Integer(Variables.getVariable(Variables.SWITCH_TARJETAS_TIMEOUT)); //Valor definido para el TIMEOUT
                socket.setSoTimeout((timeout > 0 ? timeout : 1) * 1000); //Pasamos el TIMEOUT a milisegundos

                int to = (timeout > 0 ? timeout : 1) * 1000;
                entrada = new DataInputStream(socket.getInputStream());
                salida = new DataOutputStream(socket.getOutputStream());
                log.debug("enviarTrama() - Enviamos la trama navideña timeout " + to);
                salida.write(msg.getBytes());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int s;
                do {
                    s = entrada.read(buffer); //Leemos la respuesta del servidor (entrada)
                    if (s != -1) {
                        baos.write(buffer, baos.size(), s);
                    }
                } while (s >= buffer.length);

                byte result[] = baos.toByteArray();
                mensajeRecibido = new String(result, "UTF-8");
                mensajeRecibido = mensajeRecibido.substring(7);
                log.debug("enviarTrama() - Mensaje recibido: " + mensajeRecibido);
            }

        } catch (Exception ex) {
            log.error("enviarTrama() - Error al enviar la trama de petición de bono supermaxi navideño: " + ex.getMessage(), ex);
            if (ex instanceof SocketTimeoutException) {
                throw new SocketTimeoutException(ex.getMessage());
            } else {
                throw new IOException(ex.getMessage());
            }
        } finally {
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
        return mensajeRecibido;
    }

    public String enviarTramaServicioWeb(SolicitudSupermaxiDTO solicitudSupermaxiDTO) throws SocketTimeoutException, IOException {
        String respuesta = null;
        log.debug("enviarTramaServicioWeb() - Enviando trama navideña con Servicio Web - " + solicitudSupermaxiDTO.getTrama());

        try {
            String url = Variables.getVariable(Variables.WEBSERVICE_ERP_MOVIL_ENDPOINT_URL);
            ClienteRest clienteRest = new ClienteRest();
            RespuestaSupermaxiDTO responseSupermaxiDTO = clienteRest.clientRestPOST(url + URL_ENVIO_SUPERMAXI, solicitudSupermaxiDTO, null, RespuestaSupermaxiDTO.class);
            respuesta = responseSupermaxiDTO.getTrama();

            log.info(respuesta);

        } catch (Exception ex) {
            log.error("enviarTramaServicioWeb() - Error al enviar la trama de petición de bono supermaxi navideño con Servicio Web:  " + ex.getMessage(), ex);
            if (ex instanceof SocketTimeoutException) {
                throw new SocketTimeoutException(ex.getMessage());
            } else {
                throw new IOException(ex.getMessage());
            }
        }
        return respuesta;
    }

    public boolean enviarTramaServicioWebAnulacion(SolicitudSupermaxiDTO solicitudSupermaxiDTO) throws SocketTimeoutException, IOException {
        boolean respuesta = false;
        log.debug("enviarTramaServicioWebAnulacion() - Enviando trama navideña con Servicio Web de Anulación- " + solicitudSupermaxiDTO.getTrama());

        try {
            String url = Variables.getVariable(Variables.WEBSERVICE_ERP_MOVIL_ENDPOINT_URL);
            ClienteRest clienteRest = new ClienteRest();
            RespuestaSupermaxiDTO responseSupermaxiDTO = clienteRest.clientRestPOST(url + URL_ENVIO_SUPERMAXI, solicitudSupermaxiDTO, null, RespuestaSupermaxiDTO.class);
            respuesta = responseSupermaxiDTO.getExito();

            log.info(respuesta);

        } catch (Exception ex) {
            log.error("enviarTramaServicioWeb() - Error al enviar la trama de petición de bono supermaxi navideño con Servicio Web:  " + ex.getMessage(), ex);
            if (ex instanceof SocketTimeoutException) {
                throw new SocketTimeoutException(ex.getMessage());
            } else {
                throw new IOException(ex.getMessage());
            }
        }
        return respuesta;
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        PeticionBonoSuperMaxiNavidad.log = log;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getCodigoProceso() {
        return codigoProceso;
    }

    public void setCodigoProceso(String codigoProceso) {
        this.codigoProceso = codigoProceso;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getMontoTransaccion() {
        return montoTransaccion;
    }

    public void setMontoTransaccion(String montoTransaccion) {
        this.montoTransaccion = montoTransaccion;
    }

    public String getSecuencialTransaccion() {
        return secuencialTransaccion;
    }

    public void setSecuencialTransaccion(String secuencialTransaccion) {
        this.secuencialTransaccion = secuencialTransaccion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getModoEntrada() {
        return modoEntrada;
    }

    public void setModoEntrada(String modoEntrada) {
        this.modoEntrada = modoEntrada;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public String getPrimeraCompra() {
        return primeraCompra;
    }

    public void setPrimeraCompra(String primeraCompra) {
        this.primeraCompra = primeraCompra;
    }

    public String getTipoCreditoMeses() {
        return tipoCreditoMeses;
    }

    public void setTipoCreditoMeses(String tipoCreditoMeses) {
        this.tipoCreditoMeses = tipoCreditoMeses;
    }

    public String getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(String codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public String getCampoPrivado() {
        return campoPrivado;
    }

    public void setCampoPrivado(String campoPrivado) {
        this.campoPrivado = campoPrivado;
    }

    public String getCupoCompartido() {
        return cupoCompartido;
    }

    public void setCupoCompartido(String cupoCompartido) {
        this.cupoCompartido = cupoCompartido;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getPinBlock() {
        return pinBlock;
    }

    public void setPinBlock(String pinBlock) {
        this.pinBlock = pinBlock;
    }

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public String getCodigoEstablecimiento() {
        return codigoEstablecimiento;
    }

    public void setCodigoEstablecimiento(String codigoEstablecimiento) {
        this.codigoEstablecimiento = codigoEstablecimiento;
    }

    public String getCodigoCajero() {
        return codigoCajero;
    }

    public void setCodigoCajero(String codigoCajero) {
        this.codigoCajero = codigoCajero;
    }

    public String getCodigoTerminal() {
        return codigoTerminal;
    }

    public void setCodigoTerminal(String codigoTerminal) {
        this.codigoTerminal = codigoTerminal;
    }

}
