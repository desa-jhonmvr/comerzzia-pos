/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;

/**
 *
 * @author SMLM
 */
public class Constantes {

    public static final String VERSION_POS = "20191111";
    public static final String VERSION_NUMERO_POS = "1.4.6";
    public static final Fecha junioPrimero2016 = new Fecha("01/06/2016", Fecha.PATRON_FECHA_CORTA);
    public static final Fecha junioPrimero2017 = new Fecha("01/06/2017", Fecha.PATRON_FECHA_CORTA);

    public static final int PORCENTAJE_MINIMO_EMPLEADO_COMOHOGAR = 5;
    public static final int PLAZO_RESERVACION_EMPLEADO_COMOHOGAR = 60;
    public static final BigDecimal VALOR_MINIMO_RESERVA_EMPLEADO_COMOHOGAR = new BigDecimal("5");
    public static final BigDecimal VALOR_MAXIMO_APROBAR_ENVIO_SECCION = new BigDecimal("500");
    public static final String VALOR_DEFECTO_CLAVE_ACCESO = "17907461";

    public static final String VALOR_DEFECTO_LOTE_MANUAL = "000000";
    public static final String VALOR_DEFECTO_FECHA_TARJETA = "9901";
    public static final String VALOR_DEFECTO_CCV = "123";

    public static final String PROCESO_PEDIDO_FACTURADO = "FACTURADO";
    public static final String PROCESO_ENVIO_DOMICILIO = "ENTREGA_DOMICILIO";
    public static final String PROCESO_TRAZABILIDAD_CONVENIO_ENTREGA = "TRAZABILIDAD_CONVENIO_ENTREGA";
    public static final String PROCESO_ENVIO_SMS = "ENVIO_SMS";
    public static final String PROCESO_KARDEX_POS = "KARDEX_POS";
    public static final String PROCESO_CREDITO_CUPO = "CREDITO_CUPO";
    public static final String PROCESO_MONITOR_ONLINE = "MONITOR_ONLINE";
    public static final String PROCESO_INTERCAMBIO_NOTA_CREDITO = "INTERCAMBIO_NOTA_CREDITO";
    public static final String PROCESO_PRE_FACTURA_COMPLETAR_ORDEN = "PREFACTURA_COMPLETAR_ORDEN";
    public static final String PROCESO_NOTIFICACION_CONSUMO_POS = "NOTIFICACION_CONSUMO_POS";
    public static final String PROCESO_ENVIO_DOMICILIO_POS = "ENVIO_DOMICILIO_POS";

    public static final String QUEUE_TRAZABILIDAD_ONLINE = "trazabilidad.entrega.online.in";
    public static final String QUEUE_NOTIFICACION_CONSUMO_POS = "pos.notificacion.consumo.in";
    public static final String QUEUE_ENVIO_DOMICILIO = "pos.envio.domicilio.in";

    public static final String MOVIMIENTO_51 = "51";
    public static final String MOVIMIENTO_52 = "52";
    public static final String MOVIMIENTO_99 = "99";

    public static final String SECCION_OBSEQUIOS = "58";
    public static final String ARTICULO_FUNDA = "1533";
    public static final String MEDIO_PAGO_SK_GOLD = "215";

    public static final String ARxTICULO_EXTENSION_GARANTIA = "2581.0001";

    public static final Integer[] LOCALES_NO_PRESENTAR = {};

    public static final String FORMAT_CODART_POS = "%04d";

    public static final BigDecimal CIEN = new BigDecimal("100");
    public static final String TEXT = "text";
    public static final String LINE = "line";

    //Puertos para los sockets
    
    public static final int SOCKET_VENTA_EN_LINEA = 13254;
    public static final int SOCKET_PLAN_FINANCIAMIENTO = 15287;
    public static final int SOCKET_ENTREGA_DOMICILIO = 15289;
    public static final int SOCKET_MARCAS_MEDIO_PAGO = 15290;
    public static final int SOCKET_CONSULTA_CREDITO = 15291;
    public static final int SOCKET_AUTORIZACION_CONSUMO = 15293;

}
