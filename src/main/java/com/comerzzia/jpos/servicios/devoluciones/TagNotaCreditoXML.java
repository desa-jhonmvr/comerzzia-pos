package com.comerzzia.jpos.servicios.devoluciones;


/**
 *
 * @author amos
 */
public class TagNotaCreditoXML {
    
    public static String TAG_NOTA_CREDITO       = "notaCredito";

    // ticket
    public static String TAG_CABECERA           = "cabecera";
    public static String TAG_LINEAS             = "lineas";
    public static String TAG_PAGOS              = "pagos";
    
    // cabecera
    public static String TAG_UID_NC             = "uid_notaCredito";
    public static String TAG_FECHA              = "fecha";
    public static String TAG_VERSION            = "version";
    public static String TAG_FECHA_VALIDEZ      = "fechaValidez";
    public static String TAG_ID_TIENDA          = "tienda";
    public static String TAG_CODCAJA            = "caja";
    public static String TAG_ID_NC              = "idNotaCredito";
    public static String TAG_ID_MOTIVO_DEV      = "idMotivoDevolucion";
    public static String TAG_DES_MOTIVO_DEV     = "desMotivoDevolucion";
    public static String TAG_OBSERVACIONES      = "observaciones";
    public static String TAG_UID_DIARIO_CAJA    = "uidDiarioCaja";
    public static String TAG_UID_CAJERO_CAJA    = "uidCajeroCaja";
    public static String TAG_AUTORIZADOR        = "usuarioAutorizador";
    public static String TAG_NOMBRE_EMPRESA           = "nombreEmpresa";
    public static String TAG_NOMBRE_TIENDA            = "nombreTienda";
    public static String TAG_DIRECCION_EMPRESA        = "direccionEmpresa";
    public static String TAG_DIRECCON_LOCAL           = "direccionLocal";
    public static String TAG_REGISTRO_MERCANTIL       = "registroMercantil";
    public static String TAG_REGION                   = "region";
    public static String TAG_AUTORIZACION_SRI         = "autorizacionSRI";
    public static String TAG_FECHA_INICIO_VALIDEZ_SRI     = "fechaInicioValidezSRI";
    public static String TAG_FECHA_FIN_VALIDEZ_SRI        = "fechaFinValidezSRI";
    public static String TAG_NRO_RESOLUCION_CONTRIBUYENTE = "nroResolucionContribuyente";
    public static String TAG_VUELTA             = "vuelta";

    // factura
    public static String TAG_FACTURA            = "factura";
    public static String TAG_FACTURA_UID        = "uid_factura"; 
    public static String TAG_FACTURA_ID_TIENDA  = "tienda"; 
    public static String TAG_FACTURA_CODCAJA    = "caja"; 
    public static String TAG_FACTURA_ID_FACTURA = "idFactura"; 

    // cliente
    public static String TAG_CLIENTE            = "cliente";
    public static String TAG_CLIENTE_TIPO_ID    = "tipoIdentificacion"; 
    public static String TAG_CLIENTE_ID         = "identificacion"; 
    public static String TAG_CLIENTE_NUM_AFILIADO = "numeroAfiliado"; 
    public static String TAG_CLIENTE_NOMBRE     = "nombre"; 
    public static String TAG_CLIENTE_APELLIDOS  = "apellidos"; 
    public static String TAG_CLIENTE_DIRECCION  = "direccion"; 
    public static String TAG_CLIENTE_TELEFONO   = "telefono"; 

    // cajero
    public static String TAG_CAJERO             = "cajero";
    public static String TAG_CAJERO_ID          = "idUsuario";
    public static String TAG_CAJERO_DES         = "descripcionUsuario";
    
    // vendedor
    public static String TAG_VENDEDOR           = "vendedor";
    public static String TAG_VENDEDOR_ID        = "idVendedor";
    public static String TAG_VENDEDOR_NOMBRE    = "nombreVendedor";
    public static String TAG_VENDEDOR_APELLIDOS = "apellidosVendedor";
    
    // linea
    public static String TAG_LINEA                      = "linea";
    public static String ATR_LINEA_IDLINEA              = "idlinea";
    public static String TAG_LINEA_DESART               = "desArticulo";
    public static String TAG_LINEA_CODART               = "codArticulo";
    public static String TAG_LINEA_CODBARRAS            = "codBarras";
    public static String TAG_LINEA_CANTIDAD             = "cantidad";
    public static String TAG_LINEA_PRECIO_TOTAL_TARIFA_ORIGEN = "precioTotalTarifaOrigen";  // precio con iva original (sin dto) 
    public static String TAG_LINEA_PRECIO_TARIFA_ORIGEN = "precioTarifaOrigen";             // precio sin iva original (sin dto) 
    public static String TAG_LINEA_DESCUENTO            = "descuento";
    public static String TAG_LINEA_PRECIO_TOTAL         = "precioTotal";        // precio con iva con dto
    public static String TAG_LINEA_PRECIO               = "precio";             // precio sin iva con dto
    public static String TAG_LINEA_IMPORTE              = "importe";
    public static String TAG_LINEA_IMPORTE_TOTAL        = "importeTotal";
    public static String TAG_LINEA_CODIMP               = "codImpuesto";
    public static String TAG_LINEA_DESCUENTO_FINAL      = "descuentoFinal";
    public static String TAG_LINEA_MODELO               = "modelo";
    public static String TAG_LINEA_INTERES              = "interes";
    
    // kit instalacion
    public static String TAG_KIT                  = "referenciaKitInstalacion"; // significa que la factura es de kit de instalación y está asociada a otra anterior
    public static String TAG_KIT_FACTURA          = "factura"; // identificador de factura origen
    public static String TAG_KIT_UID_FACTURA      = "uid_ticket"; // uid Factura origen
    public static String TAG_KIT_ARTICULO         = "codArticulo"; // artículo factura origen
    
    // garantía extendida
    public static String TAG_GARANTIA                  = "referenciaGarantiaExtendida"; // significa que la factura es de garantía extendida y está asociada a otra anterior
    public static String TAG_GARANTIA_FACTURA          = "factura"; // identificador de factura origen
    public static String TAG_GARANTIA_UID_FACTURA      = "uid_ticket"; // uid Factura origen
    public static String TAG_GARANTIA_ARTICULO         = "codArticulo"; // artículo factura origen
    public static String TAG_GARANTIA_PRECIO           = "precioArticuloOrigen"; //precio del artículo origen    
    
    // pagos
    public static String TAG_PAGO                = "pago";
    public static String TAG_PAGO_CODMEDPAG      = "codMedioPago";
    public static String TAG_PAGO_DESMEDPAG      = "desMedioPago";
    public static String TAG_PAGO_TOTAL          = "importeTotal";
    public static String TAG_PAGO_USTED_PAGA     = "importePagado";
    public static String TAG_PAGO_ENTREGADO      = "importeEntregado";
    public static String TAG_PAGO_INFO1          = "infoExtra1";
    public static String TAG_PAGO_INFO2          = "infoExtra2";
    public static String TAG_PAGO_INFO3          = "infoExtra3";
    public static String TAG_PAGO_NUMERO_TARJETA = "numeroTarjeta";
    public static String TAG_PAGO_PLAN_N_CUOTAS  = "numCuotas";
    public static String TAG_PAGO_VUELTA         = "vuelta";

    // totales
    public static String TAG_TOTALES                            = "totales"; 
    public static String TAG_TOTALES_TOTAL                      = "total";
    public static String TAG_TOTALES_PORCENTAJE_DTO_PAGOS       = "porcentajeAhorroPagos";
    public static String TAG_TOTALES_PROMOCIONES                = "promociones";
    public static String TAG_TOTALES_BASE                       = "base"; // total con descuentos y sin iva
    public static String TAG_TOTALES_IMPUESTOS                  = "impuestos"; // iva
    public static String TAG_TOTALES_SUBTOTALES_IVA             = "subtotalesPorIva";
    public static String TAG_TOTALES_SUBTOTALES_IVA_SUBTOTAL    = "subtotal";
    public static String TAG_TOTALES_SUBTOTALES_IVA_CODIGO      = "codigoImpuesto";
    public static String TAG_TOTALES_SUBTOTALES_IVA_PORCENTAJE  = "porcentaje";
    public static String TAG_TOTALES_SUBTOTALES_IVA_IMPORTE     = "importe";
    public static String TAG_TOTALES_SUBTOTAL0                  = "valorSubtotal0";
    public static String TAG_TOTALES_SUBTOTAL_IVA_VALOR         = "valorSubtotalIVA";
    public static String TAG_TOTALES_SUBTOTAL_IVA_PORCENTAJE    = "porcentajeSubtotalIVA";
    public static String TAG_TOTALES_COMPENSACION               = "compensacionGobierno";

}
