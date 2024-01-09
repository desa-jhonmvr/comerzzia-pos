/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.xml;

/**
 *
 * @author amos
 */
public class TagTicketXML {

    public static final String TAG_LINEA_INTERES = "interes";
    public static String TAG_TIKET              = "ticket";

    // ticket
    public static String TAG_CABECERA           = "cabecera";
    public static String TAG_LINEAS             = "lineas";
    public static String TAG_PAGOS              = "pagos";
    
    // cabecera
    public static String TAG_UID_TICKET               = "uid_ticket";
    public static String TAG_FECHA                    = "fecha";
    public static String TAG_VERSION                  = "version";
    public static String TAG_ID_TIENDA                = "tienda";
    public static String TAG_CODCAJA                  = "caja";
    public static String TAG_ID_TICKET                = "idTicket";
    public static String TAG_UID_DIARIO_CAJA          = "uidDiarioCaja";
    public static String TAG_UID_CAJERO_CAJA          = "uidCajeroCaja";
    public static String TAG_FECHA_FIN_DEVOLUCION     = "fechaFinDevolucion";
    public static String TAG_COD_FORMATO              = "codFormato";
    public static String TAG_EMPRESA                  = "codEmpresa";
    public static String TAG_AUTORIZADOR              = "autorizador";
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
    public static String TAG_PORCENTAJE_COMPENSACION  = "porcentajeCompensacion";
    public static String TAG_OBSERVACION  = "observacion";
    

    //aplazado
    public static String TAG_APLAZAR_VENTA = "aplazarVenta";
    public static String TAG_ID_USUARIO_APLAZADO = "idUsuario";
    public static String TAG_USUARIO_APLAZADO = "usuario";
    public static String TAG_DES_USUARIO_APLAZADO = "desUsuario";
    
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
    public static String TAG_GARANTIA_IMPORTE          = "importeArticuloOrigen"; //importe del artículo origen
    
    // cliente
    public static String TAG_CLIENTE            = "cliente";
    public static String TAG_CLIENTE_TIPO_ID    = "tipoIdentificacion"; 
    public static String TAG_CLIENTE_ID         = "identificacion"; 
    public static String TAG_CLIENTE_NUM_AFILIADO = "numeroAfiliado"; 
    public static String TAG_CLIENTE_NOMBRE     = "nombre"; 
    public static String TAG_CLIENTE_APELLIDOS  = "apellidos"; 
    public static String TAG_CLIENTE_TARJETA_SUPERMAXI  = "tarjetaSupermaxi"; // se registra ofuscada para la impresión
    public static String TAG_CLIENTE_TARJETA_AFILIACION_TIPO   = "tipoTarjetaAfiliacion";
    public static String TAG_CLIENTE_TARJETA_AFILIACION_LECTURA= "lecturaTarjetaAfiliacion";
    public static String TAG_CLIENTE_TARJETA_AFILIACION_NUMERO = "numeroTarjetaAfiliacion";
    
    // facturacion
    public static String TAG_DATOS_FACTURACION  = "datosFacturacion";
    public static String TAG_DATOS_FACT_TIPO_ID = "tipoIdentificacion"; 
    public static String TAG_DATOS_FACT_ID      = "identificacion"; 
    public static String TAG_DATOS_FACT_NOMBRE  = "nombre"; 
    public static String TAG_DATOS_FACT_APELLIDOS = "apellidos"; 
    public static String TAG_DATOS_FACT_DIRECCION = "direccion"; 
    public static String TAG_DATOS_FACT_CIUDAD  = "ciudad"; 
    public static String TAG_DATOS_FACT_TELEFONO = "telefono"; 

    // cajero
    public static String TAG_CAJERO             = "cajero";
    public static String TAG_USUARIO_ID         = "idUsuario";
    public static String TAG_USUARIO_COD        = "codUsuario";
    public static String TAG_USUARIO_DES        = "descripcionUsuario";
    
    // vendedor
    public static String TAG_VENDEDOR           = "vendedor";
    public static String TAG_VENDEDOR_ID        = "idVendedor";
    public static String TAG_VENDEDOR_NOMBRE    = "nombreVendedor";
    public static String TAG_VENDEDOR_APELLIDOS = "apellidosVendedor";
    
    // linea
    public static String TAG_LINEA                      = "linea";
    public static String ATR_LINEA_IDLINEA              = "idlinea";
    public static String TAG_LINEA_REF_GARANTIA         = "referenciaGarantiaExtendida"; // significa que el artículo es una garantía extendida y la referencia es el artículo asociado
    public static String TAG_LINEA_REF_KIT              = "referenciaKitInstalacion"; // significa que el artículo es un kit de instalación, y la referencia es el artículo origen
    public static String TAG_LINEA_DESART               = "desArticulo";
    public static String TAG_LINEA_CODART               = "codArticulo";
    public static String TAG_LINEA_CODBARRAS            = "codBarras";
    public static String TAG_LINEA_MODELO               = "modelo";
    public static String TAG_LINEA_CANTIDAD             = "cantidad";
    public static String TAG_LINEA_PRECIO_TOTAL_TARIFA_ORIGEN = "precioTotalTarifaOrigen";  // precio con iva original (sin dto) 
    public static String TAG_LINEA_PRECIO_TARIFA_ORIGEN = "precioTarifaOrigen";             // precio sin iva original (sin dto) 
    public static String TAG_LINEA_DESCUENTO            = "descuento"; // sólo % de descuento manual
    public static String TAG_LINEA_DESCUENTO_FINAL      = "descuentoFinal"; // descuento acumulado
    public static String TAG_LINEA_PRECIO_TOTAL         = "precioTotal";        // precio con iva con dto
    public static String TAG_LINEA_PRECIO               = "precio";             // precio sin iva con dto
    public static String TAG_LINEA_IMPORTE              = "importe";
    public static String TAG_LINEA_IMPORTE_TOTAL        = "importeTotal";
    public static String TAG_LINEA_IMPORTE_FINAL        = "importeFinalPagado";     // quitando todos los descuentos y promociones (de pagos, de totales, de líneas)
    public static String TAG_LINEA_IMPORTE_TOTAL_FINAL  = "importeTotalFinalPagado";// quitando todos los descuentos y promociones (de pagos, de totales, de líneas)
    public static String TAG_LINEA_CODIMP               = "codImpuesto";
    public static String TAG_LINEA_ENVIO_DOMICILIO      = "envioDomicilio";
    public static String TAG_LINEA_RECOGIDA_POSTERIOR   = "recogidaPosterior";
    public static String TAG_LINEA_AUTORIZADOR          = "autorizador";
    public static String TAG_LINEA_GARANTIA_ORIGINAL    = "garantiaOriginal"; //unidades de garantía original del producto
    public static String TAG_PORCENTAJE_IVA             = "porcentajeIva";
//    public static String TAG_LINEA_IMPORTE_ICE          = "importeIce";
    
    public static String TAG_SUKUPONES                  = "sukupones";
    public static String TAG_SUKUPON                    = "sukupon";
    public static String TAG_SUKUPON_AUSPICIANTE        = "auspiciante";
    public static String TAG_SUKUPON_VALOR              = "valor";

    // promocion
    public static String TAG_PROMOCION                  = "promocion";
    public static String TAG_PROMOCION_N_CUOTAS         = "promocionNCuotas";
    public static String TAG_PROMOCION_MESES_GRACIA     = "promocionMesesGracia";
    public static String TAG_PROMO_FACTURA_DIA_SOCIO    = "uidTicketDiaSocio";
    public static String TAG_PROMO_DIA_SOCIO            = "idPromoDiaSocio";
    public static String TAG_PROMO_PRECIO_TARIFA        = "precioTarifa";
    public static String TAG_PROMO_PRECIO_TOTAL_TARIFA  = "precioTotalTarifa";
    public static String TAG_PROMO_CANT_PROMO           = "cantidadPromocion";
    public static String TAG_PROMO_IMPORTE_PROMO        = "importePromocion";
    public static String TAG_PROMO_IMPORTE_TOTAL_PROMO  = "importeTotalPromocion";
    public static String TAG_PROMO_IMPORTE_AHORRO       = "importeAhorro"; 
    public static String TAG_PROMO_IMPORTE_TOTAL_AHORRO = "importeTotalAhorro";
    public static String TAG_PROMO_ID                   = "idPromocion";
    public static String TAG_PROMO_ID_TIPO              = "idTipoPromocion";
    public static String TAG_PROMO_DES_TIPO             = "desTipoPromocion";
    public static String TAG_PROMO_TEXTO                = "textoPromocion";
    public static String TAG_PROMO_NUM_CUOTAS           = "numCuotasPromocion";
    public static String TAG_PROMO_IMPORTE_CUOTA        = "importeCuotaPromocion";
    public static String TAG_LINEAS_PROMOCIONES         = "lineasPromociones";
    public static String TAG_LINEA_PROMOCION            = "lineaPromocion";
    
    
    // pagos 
    public static String TAG_PAGO               = "pago";
    public static String ATR_ID_PAGO            = "idPago";
    public static String TAG_PAGO_CODMEDPAG     = "codMedioPago";
    public static String TAG_PAGO_DESMEDPAG     = "desMedioPago";
    public static String TAG_PAGO_TOTAL         = "importeTotal";
    public static String TAG_PAGO_TOTAL_SIN_IVA = "importeTotalSinIva";
    public static String TAG_PAGO_USTED_PAGA    = "importePagado";
    public static String TAG_PAGO_USTED_PAGA_SIN_IVA = "importePagadoSinIva";
    public static String TAG_PAGO_ENTREGADO     = "importeEntregado";
    public static String TAG_PAGO_AHORRO        = "ahorro";
    public static String TAG_PAGO_DESCUENTO     = "descuento";
    public static String TAG_PAGO_INFO1         = "infoExtra1";
    public static String TAG_PAGO_INFO2         = "infoExtra2";
    public static String TAG_PAGO_INFO3         = "infoExtra3";
    public static String TAG_PAGO_NOTA_CREDITO  = "notaCredito";
    public static String TAG_PAGO_AUTORIZADOR   = "autorizador";
    public static String TAG_PAGO_INTERES_PORC  = "interesPorcentaje";
    public static String TAG_PAGO_INTERES_IMP   = "interesImporte";
    public static String TAG_PAGO_MESES_POSFECHADO = "mesesPosfechado";

    // plan
    public static String TAG_PAGO_PLAN          = "plan";
    public static String TAG_PAGO_PLAN_DESC     = "descripcion";
    public static String TAG_PAGO_PLAN_N_CUOTAS = "numCuotas";
    public static String TAG_PAGO_PLAN_CUOTA    = "cuota";
    
    // letra
    public static String TAG_PAGO_LETRA         = "letra";
    public static String TAG_PAGO_LETRA_UID     = "uidLetra";
    public static String TAG_PAGO_LETRA_INTERES = "intereses";
    public static String TAG_PAGO_LETRA_CUOTAS      = "cuotas";
    public static String TAG_PAGO_LETRA_CUOTA       = "cuota";
    public static String ATR_PAGO_LETRA_CUOTA_NUM   = "numero";
    public static String TAG_PAGO_LETRA_CUOTA_VEN   = "fechaVencimiento";
    public static String TAG_PAGO_LETRA_CUOTA_VALOR = "valor";
    
    
    public static String TAG_PAGO_NUMERO_CREDITO = "numeroCredito";
    


    // totales
    public static String TAG_TOTALES                            = "totales"; 
    public static String TAG_TOTALES_IMPORTE_TARIFA_ORIGEN      = "importeTarifaOrigen"; 
    public static String TAG_TOTALES_IMPORTE_TOTAL_TARIFA_ORIGEN = "importeTotalTarifaOrigen"; 
    //public static String TAG_TOTALES_TOTAL_SIN_PROMOCIONES      = "totalSinPromociones"; 
    //public static String TAG_TOTALES_TOTAL_PROMOCIONES_LINEAS   = "totalPromocionesLineas"; 
    //public static String TAG_TOTALES_TOTAL_PROMOCIONES_TOTALES  = "totalPromocionesTotales";
    //public static String TAG_TOTALES_TOTAL_PROMOCIONES          = "totalPromociones";
    public static String TAG_TOTALES_TOTAL_A_PAGAR              = "totalAPagar";
    public static String TAG_TOTALES_INTERES                    = "totalInteres";
    public static String TAG_TOTALES_TOTAL_AHORRO_PAGOS         = "totalAhorroPagos";
    public static String TAG_TOTALES_AHORRO_PAGOS               = "ahorroPagos";
    public static String TAG_TOTALES_TOTAL_PAGADO               = "totalPagado"; // total con descuentos y con iva
    public static String TAG_TOTALES_PORCENTAJE_DTO_PAGOS       = "porcentajeAhorroPagos";
    
    public static String TAG_TOTALES_BASE                       = "base"; // total con descuentos y sin iva
    public static String TAG_TOTALES_IMPUESTOS                  = "impuestos"; // iva
    public static String TAG_TOTALES_IMPUESTOS_ICE              = "impuestosIce"; // ice

    public static String TAG_TOTALES_SUBTOTALES_IVA             = "subtotalesPorIva";
    public static String TAG_TOTALES_SUBTOTALES_IVA_SUBTOTAL    = "subtotal";
    public static String TAG_TOTALES_SUBTOTALES_IVA_CODIGO      = "codigoImpuesto";
    public static String TAG_TOTALES_SUBTOTALES_IVA_PORCENTAJE  = "porcentaje";
    public static String TAG_TOTALES_SUBTOTALES_IVA_IMPORTE     = "importe";
    

    public static String TAG_TOTALES_DEDUCIBLE_ALIMENTACION     = "totalDeducibleAlimentacion";
    public static String TAG_TOTALES_DEDUCIBLE_MEDICINA         = "totalDeducibleMedicina";
    public static String TAG_TOTALES_DEDUCIBLE_ROPA             = "totalDeducibleRopa";
    public static String TAG_TOTALES_DEDUCIBLE_EDUCACION        = "totalDeducibleEducacion";
    public static String TAG_TOTALES_DEDUCIBLE_VIVIENDA         = "totalDeducibleVivienda";
    public static String TAG_TOTALES_PROMOCIONES                = "promociones";
    public static String TAG_PORCENTAJE_DESCUENTO_GLOBAL        = "porcentajeDescuentoGlobal"; // ice
    public static String TAG_TOTALES_DESCUENTO_GLOBAL           = "descuentoGlobal"; // ice

    // Venta en otro local
     public static String TAG_VENTA_ENTRE_LOCALES                            = "ventaEntreLocales";
     public static String TAG_VENTA_ENTRE_LOCALES_AUTORIZADOR                = "autorizador";
     public static String TAG_VENTA_ENTRE_LOCALES_LOCAL_VENTA                = "localVenta";
     public static String TAG_VENTA_ENTRE_LOCALES_LOCAL_VENTA_COD_LOCAL      = "codLocal";
     public static String TAG_VENTA_ENTRE_LOCALES_LOCAL_VENTA_DES_LOCAL      = "desLocal";
     public static String TAG_VENTA_ENTRE_LOCALES_LOCAL_RECOGIDA             = "localRecogida";
     public static String TAG_VENTA_ENTRE_LOCALES_LOCAL_RECOGIDA_COD_LOCAL   = "codLocal";
     public static String TAG_VENTA_ENTRE_LOCALES_LOCAL_RECOGIDA_DES_LOCAL   = "desLocal";
     public static String TAG_VENTA_ENTRE_LOCALES_CODIGO_CONFIRMACION        = "codConfirmacion";
    
     
     // Envío a Domicilio        
     public static String TAG_ENVIO_DOMICILIO                = "envioDomicilio";
     public static String TAG_ENVIO_DOMICILIO_NOMBRE         = "nombre";
     public static String TAG_ENVIO_DOMICILIO_APELLIDOS      = "apellido";
     public static String TAG_ENVIO_DOMICILIO_DIRECCION      = "direccion";
     public static String TAG_ENVIO_DOMICILIO_TELEFONO       = "telefono";
     public static String TAG_ENVIO_DOMICILIO_MOVIL          = "movil";
     public static String TAG_ENVIO_DOMICILIO_HORARIO        = "horario";
     public static String TAG_ENVIO_DOMICILIO_CIUDAD         = "ciudad";
     
     // Cupones
     public static String TAG_CUPONES                        = "cupones";
     public static String TAG_CUPONES_EMITIDOS               = "cuponesEmitidos";
     public static String TAG_CUPONES_APLICADOS              = "cuponesAplicados";
     public static String TAG_CUPONES_CUPON                  = "cupon";
     public static String TAG_CUPONES_ID_CUPON               = "idCupon";
     public static String TAG_CUPONES_CODALM                 = "codAlmacen";
     public static String TAG_CUPONES_CODIGO_BARRAS          = "codigoBarras";

     // Promociones
     public static String TAG_PROMOCIONES                               = "promociones";
     public static String TAG_PROMOCIONES_DESCRIPCIONES                 = "descripciones";
     public static String TAG_PROMOCIONES_DESCRIPCIONES_DESCRIPCION     = "descripcion";     
     
     // Puntos
     public static String TAG_PUNTOS                         = "puntos";
     public static String TAG_PUNTOS_ANTERIORES              = "puntosAnteriores";
     public static String TAG_PUNTOS_CONSUMIDOS              = "puntosConsumidos";
     public static String TAG_PUNTOS_ACUMULADOS              = "puntosAcumulados";
     public static String TAG_PUNTOS_SALDO                   = "puntosSaldo"; 
     public static String TAG_PUNTOS_CLIENTE                 = "puntosCliente";
     public static String TAG_PUNTOS_CEDIDOS                 = "puntosCedidos";
     
     public static String TAG_PAGO_AUDITORIA                 = "auditoria";
     public static String TAG_PAGO_AUTORIZACION              = "autorizacion";
     public static String TAG_PAGO_TIPO_AUTORIZACION         = "tipoAutorizacion";
     public static String TAG_PAGO_FACTURACION               = "uidFacturacion";
     public static String TAG_PAGO_FECHA_CADUCIDAD           = "caducidad";
     public static String TAG_PAGO_CVV                       = "cvv";     
     public static String TAG_PAGO_NUMERO_TARJETA            = "numeroTarjeta";
     public static String TAG_PAGO_CLIENTE_TARJETA           = "cedulaTarjeta";
     public static String TAG_PAGO_VUELTA                    = "vuelta";
     public static String TAG_PAGO_LECTURA_MANUAL            = "lecturaTarjetaManual";
     
}
