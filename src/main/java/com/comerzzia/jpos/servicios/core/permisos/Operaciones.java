/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.core.permisos;

/**
 *
 * @author amos
 */
public class Operaciones {

    public static final Byte EJECUTAR = 0;
    public static final Byte GESTIONAR_CAJA = 1;
    public static final Byte EDITAR_LINEA_VENTA = 2;
    public static final Byte BORRAR_LINEA_VENTA = 3;
    public static final Byte REALIZAR_DEVOLUCION = 4;
    public static final Byte CANCELAR_VENTA = 5;
    public static final Byte ADMITIR_LISTA_NEGRA = 6;
    public static final Byte AUTORIZAR_MEDIO_PAGO = 7;
    public static final Byte DESBLOQUEAR_POS = 8;
    public static final Byte VENTA_A_OTRO_LOCAL = 9;
    public static final Byte LIQUIDAR_RESERVA = 10;
    public static final Byte CANCELAR_RESERVA = 11;
    public static final Byte ABRIR_CAJON = 12;
    public static final Byte CARGAR_GIFTCARD = 13;
    public static final Byte APLAZAR_VENTA = 14;
    public static final Byte VISTA_FLASH_VENTAS_DIARIO = 15;
    public static final Byte DESCUENTO_COMPRA_ABONOS = 16;
    public static final Byte VER_RESERVA_CADUCADA = 17;
    public static final Byte PERMITE_POSTFECHAR_VOUCHER = 18;
    public static final Byte AUTORIZAR_VENTA_TIPO_CLIENTE = 19;
    public static final Byte ANULAR_DOCUMENTOS = 20;
    public static final Byte REIMPRIMIR = 21;
    public static final Byte STOCK = 22;
    public static final Byte LECTURA_GIFTCARD = 23;
    public static final Byte AUTORIZAR_PAGO_CREDITO_DIRECTO_BAJO_IMPORTE_MINIMO = 24;
    public static final Byte AUTORIZAR_PAGO_CREDITO_DIRECTO_SOBRE_IMPORTE_MINIMO = 25;
    public static final Byte PAGO_CREDITO_TEMPORAL_SIN_MORA = 26;
    public static final Byte VENDER_ITEM_BLOQUEADO = 27;
    public static final Byte PERMITE_FACTURAR = 28;
    public static final Byte PERMITE_MODIFICAR_ESTATUS_ENVIO = 29;
    public static final Byte PERMITE_DESCARGAR_ENTREGAS = 30;
    public static final Byte PERMITE_CONSULTAR_BONOS = 31;
    public static final Byte PERMITE_CONSULTAR_SUKUPON = 32;
    public static final Byte PERMITE_CAMBIAR_ESTADO_PINPAD = 33;
    public static final Byte PERMITE_GENERAR_PEDIDO_FACTURADO = 34;
    public static final Byte MODIFICAR_NUMERO_CELULAR = 35;
    public static final Byte AUTORIZA_FECHA_DEVOLUCION = 36;
    public static final Byte AUTORIZA_RESERVACION_SEPARACION = 38;
    public static final Byte AUTORIZA_NOTACREDITO_CADUCADA = 39;
    public static final Byte AUTORIZA_FACTURAR_PREFACTURA = 37;
    public static final Byte REIMPRIMIR_NOTA_CREDITO_BONO = 40;
    public static final Byte PERFIL_CAJERO = 41;

    public static final Long ID_ACCION_POSS = 100L;
    public static final Long PERFIL_ADMINISTRADOR = 0L;
    public static final Long PERFIL_ADMINISTRADOR_LOCAL = 1L;
}
