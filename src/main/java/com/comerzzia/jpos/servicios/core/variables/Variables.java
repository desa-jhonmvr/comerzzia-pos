/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.core.variables;

import com.comerzzia.jpos.entity.db.VariableAlm;
import com.comerzzia.jpos.persistencia.core.variables.VariablesDao;
import es.mpsistemas.util.cadenas.ToString;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MGRI
 */
public class Variables {

    private static final Logger log = Logger.getMLogger(Variables.class);
    public static final String ARTICULOS_DESGLOSE1_TITULO = "ARTICULOS.DESGLOSE1_TITULO";
    public static final String ARTICULOS_DESGLOSE2_TITULO = "ARTICULOS.DESGLOSE2_TITULO";
    public static final String TICKETS_USA_DESCUENTO_EN_LINEA = "TICKETS.USA_DESCUENTO_EN_LINEA";
    public static final String WEBSERVICES_WSCLIENTES = "WEBSERVICES.WSCLIENTES";
    public static final String SWITCH_TARJETAS_TIMEOUT = "SWITCH.TARJETAS.TIMEOUT";
    public static final String SWITCH_TARJETAS_NUMREINTENTOS = "SWITCH.TARJETAS.NUMREINTENTOS";
    public static final String SWITCH_TARJETAS_MSG_ERROR = "SWITCH.TARJETAS.MSG_ERROR";
    public static final String SWITCH_TARJETAS_VOUCHER_FIRMA = "SWITCH.TARJETAS.VOUCHER.FIRMA";
    public static final String SWITCH_TARJETAS_VOUCHER_FIRMA_MONTO_MIN = "SWITCH.TARJETAS.VOUCHER.FIRMA_MONTO_MIN";
    public static final String SWITCH_TARJETAS_MANUAL_AUTORIZACION = "SWITCH.TARJETAS.MANUAL.AUTORIZACION";
    public static final String CONFIGURACION_RETENCION_DIRECCION = "CONFIGURACION.RETENCION.DIRECCION";
    public static final String CONFIGURACION_RETENCION_NOMBRE = "CONFIGURACION.RETENCION.NOMBRE";
    public static final String CONFIGURACION_RETENCION_RUC = "CONFIGURACION.RETENCION.RUC";
    public static final String CONFIGURACION_RETENCION_TIPO_COMPROBANTE = "CONFIGURACION.RETENCION.TIPO_COMPROBANTE";

    public static final String DATABASE_CENTRAL_ESQUEMA_CONFIG = "DATABASE.CENTRAL.ESQUEMA.CONFIG";
    public static final String DATABASE_CENTRAL_ESQUEMA_EMPRESA = "DATABASE.CENTRAL.ESQUEMA.EMPRESA";
    public static final String DATABASE_CENTRAL_USUARIO = "DATABASE.CENTRAL.USUARIO";
    public static final String DATABASE_CENTRAL_PASSWORD = "DATABASE.CENTRAL.PASSWORD";
    public static final String DATABASE_CENTRAL_URL = "DATABASE.CENTRAL.URL";

    public static final String DATABASE_CENTRAL_ESQUEMA_BMSK = "DATABASE.CENTRAL.ESQUEMA.BMSK";
    public static final String DATABASE_CENTRAL_USUARIO_BMSK = "DATABASE.CENTRAL.USUARIO.BMSK";
    public static final String DATABASE_CENTRAL_PASSWORD_BMSK = "DATABASE.CENTRAL.PASSWORD.BMSK";
    public static final String DATABASE_CENTRAL_URL_BMSK = "DATABASE.CENTRAL.URL.BMSK";

    public static final String POS_CONFIG_ACCION = "POS.CONFIG.ACCION";
    public static final String POS_CONFIG_ID_BONO_EFECTIVO = "POS.CONFIG.ID_BONO.EFECTIVO";
    public static final String POS_CONFIG_ID_CLIENTE_GENERICO = "POS.CONFIG.ID_CLIENTE_GENERICO";
    public static final String POS_CONFIG_MAXIMOS_RESULTADOS_ARTICULOS = "POS.CONFIG.MAXIMOS.RESULTADOS.ARTICULOS";
    public static final String POS_CONFIG_MEDIO_PAGO_BONO = "POS.CONFIG.MEDIO_PAGO.BONO";
    public static final String POS_CONFIG_MEDIO_PAGO_BONO_NAVI = "POS.CONFIG.MEDIO_PAGO.BONO_NAVI";
    public static final String POS_CONFIG_MEDIO_PAGO_NOTA_CREDITO = "POS.CONFIG.MEDIO_PAGO.NOTA_CREDITO";
    public static final String POS_CONFIG_MEDIO_PAGO_EFECTIVO = "POS.CONFIG.MEDIO_PAGO.EFECTIVO";
    public static final String POS_CONFIG_MEDIO_PAGO_ABONO_A_RESERVAS = "POS.CONFIG.MEDIO_PAGO.ABONORESERVAS";
    public static final String POS_CONFIG_MEDIO_PAGO_CHEQUE = "POS.CONFIG.MEDIO_PAGO.CHEQUE";
    public static final String POS_CONFIG_TIPO_CLIENTE_SUPERMAXI = "POS.CONFIG.TIPO.CLIENTE.SUPERMAXI";
    public static final String POS_CONFIG_TIPO_CLIENTE_EMPLEADO = "POS.CONFIG.TIPO.CLIENTE.EMPLEADO";
    public static final String POS_CONFIG_MEDIO_PAGO_RETENCION = "POS.CONFIG.MEDIO_PAGO.RETENCION";
    public static final String POS_CONFIG_MEDIO_PAGO_COMPENSACION = "POS.CONFIG.MEDIO.PAGO.COMPENSACION";
    public static final String POS_CONFIG_NOTAS_CREDITO_DIAS_VALIDEZ = "POS.CONFIG.NOTAS_CREDITO.DIAS_VALIDEZ";
    public static final String POS_CONFIG_TIPO_AUTORIZADOR_TARJETAS = "POS.CONFIG.TIPO.AUTORIZADOR.TARJETAS";
    public static final String POS_CONFIG_HOST_AUTORIZADOR_BONO_NAVI = "POS.CONFIG.HOST.AUTORIZADOR.BONO_NAVI";
    public static final String POS_CONFIG_MEDIO_PAGO_FILIAL = "POS.CONFIG.MEDIO_PAGO.FILIAL";
    public static final String POS_CONFIG_INTERES_MORA = "POS.CONFIG.INTERES.MORA";
    public static final String POS_CONFIG_WEBSERVICE_CREDITO = "POS.CONFIG.WEBSERVICE.CREDITO";
    public static final String POS_UI_SKIN = "POS.UI.SKIN";
    public static final String POS_UI_LOOK_AND_FEEL = "POS.UI.LOOK_AND_FEEL";
    public static final String POS_UI_FUENTE_TIPO = "POS.UI.FUENTE.TIPO";
    public static final String POS_UI_FUENTE_TAMANO = "POS.UI.FUENTE.TAMANO";
    public static final String POS_UI_FUENTE_BOTONES_TIPO = "POS.UI.FUENTE.BOTONES.TIPO";
    public static final String POS_UI_FUENTE_BOTONES_TAMANO = "POS.UI.FUENTE.BOTONES.TAMANO";
    public static final String POS_VENTA_TICKET_VALIDEZ_DIAS = "POS.VENTA.TICKET.VALIDEZ.DIAS";
    public static final String MENSAJE_ERROR_GENERICO = "MENSAJE.ERROR.GENERICO";
    public static final String MENSAJE_AVISO_RETIRO = "MENSAJE.AVISO.RETIRO";
    public static final String MENSAJE_AVISO_CAJA_CERRADA = "MENSAJE.AVISO.CAJA_CERRADA";
    public static final String MENSAJE_COTIZACION_XML = "MENSAJE.COTIZACION.XML";
    public static final String MENSAJE_NO_SOCIOS = "MENSAJE.IMPRIMIR.NO.SOCIOS";
    public static final String PLANNOVIOS_MINIMO_CONSUMO_PLAN = "PLANNOVIOS.MINIMO.CONSUMO.PLAN";
    public static final String PLANNOVIOS_DIAS_VIGENCIA = "PLANNOVIOS.DIAS.VIGENCIA";
    public static final String TIPO_CLIENTE_DEFECTO = "POS.TIPO.CLIENTE.DEFECTO";
    public static final String POS_CONFIG_FUNC_ANULACIONES = "POS.CONFIG.FUNC.ANULACIONES";
    public static final String POS_CONFIG_FUNC_CONSULTA_STOCK = "POS.CONFIG.FUNC.CONSULTA_STOCK";
    public static final String POS_CONFIG_FUNC_CREDITO_PROPIO = "POS.CONFIG.FUNC.CREDITO_PROPIO";
    public static final String POS_CONFIG_FUNC_LINEA_TICKET_MULTIPLE = "POS.CONFIG.FUNC.LINEA_TICKET_MULTIPLE";
    public static final String SWITCH_TARJETAS_TIEMPO_ENVIO_REVERSA = "SWITCH.TARJETAS.TIEMPO_ENVIO_REVERSA";
    public static final String POS_CONFIG_MESES_POSFECHAR_VOUCHER = "POS.CONFIG.MESES.POSFECHAR.VOUCHER";
    public static final String POS_CONFIG_BINES_TARJETA_DESCUENTO = "POS.CONFIG.BINES.TARJETA.DESCUENTO";

    public static final String FUNCIONALIDAD_CONSULTA_STOCK = "POS.CONFIG.FUNC.CONSULTA_STOCK";
    public static final String FUNCIONALIDAD_KARDEX_POS = "POS.CONFIG.FUNC.KARDEX.POS";
    public static final String FUNCIONALIDAD_LINEA_MULTIPLE_EN_TICKET = "POS.CONFIG.FUNC.LINEA_TICKET_MULTIPLE";
    public static final String FUNCIONALIDAD_CONSULTA_STOCK_ERP = "POS.CONFIG.FUNC.CONSULTA_STOCK_ERP";

    public static final String DESCUENTO_AFILIADO_PORCENTAJE = "DESCUENTO.AFILIADO.PORCENTAJE";
    public static final String CODART_NOTA_CREDITO_RESERVAS = "POS.NOTA_CREDITO.RESERVAS.CODART";
    public static final String GARANTIA_EXT_CODBARRAS = "POS.GARANTIA_EXTENDIDA.CODIGO_BARRAS";
    public static final String ENTREGA_DOMICILIO_CODBARRAS = "POS.ENTREGA_DOMICILIO.CODIGO_BARRAS";
    public static final String GARANTIA_EXT_IMPORTE_MINIMO = "POS.GARANTIA_EXTENDIDA.IMPORTE_MINIMO";
    public static final String GARANTIA_EXT_PORCENTAJE_CALCULO = "POS.GARANTIA_EXTENDIDA.PORCENTAJE_CALCULO";
    public static final String GARANTIA_EXT_DURACION_MESES = "POS.GARANTIA_EXTENDIDA.DURACION";
    public static final String GARANTIA_EXT_MEDIO_PAGO_DESCUENTO = "POS.GARANTIA_EXTENDIDA.MEDIO_PAGO.DESCUENTO";
    public static final String TICKET_MENSAJE_EXTENSION_GARANTIA = "TICKET.MENSAJE.EXTENSION.GARANTIA";
    public static final String POS_CONFIG_FUNC_GUIA_REMISION = "POS.CONFIG.FUNC.GUIA_REMISION";
    public static final String FACTURACION_ESPECIAL_CODIGO = "POS.FACTURACION_ESPECIAL.CODIGO";
    public static final String POS_LIMITE_VALOR_STOCK = "POS.LIMITE.VALOR.STOCK";

    public static final String FUNCIONALIDAD_ITEM_BLOQUEO = "POS.CONFIG.FUNC.ITEM_BLOQUEO";
    public static final String DATABASE_PASARELA_ESQUEMA = "DATABASE.PASARELA.ESQUEMA";
    public static final String DATABASE_PASARELA_USUARIO = "DATABASE.PASARELA.USUARIO";
    public static final String DATABASE_PASARELA_PASSWORD = "DATABASE.PASARELA.PASSWORD";
    public static final String DATABASE_PASARELA_URL = "DATABASE.PASARELA.URL";
    public static final String CONFIGURACION_EMPRESA = "CONFIGURACION.EMPRESA";

    public static final String PROMO_DIA_SOCIO_ASOCIACION_CODBARRAS = "PROMO.DIA_SOCIO.ASOCIACION.CODBARRAS";
    public static final String PROMO_ITEM_BASE_ASOCIACION_CODBARRAS = "PROMO.ITEM.BASE.ASOCIACION.CODBARRAS";

    public static final String GARANTIA_EXTENDIDA_TIPO_AFILIADO_GRATIS = "POS.GARANTIA_EXTENDIDA.TIPO_AFILIADO_GRATIS";

    public static final String CONFIGURACION_EMPRESA_ES_SUKASA = "SUKASA";
    public static final String CONFIGURACION_EMPRESA_ES_BEBEMUNDO = "BEBEMUNDO";
    //NUEVAS VARIABLES
    public static final Integer CUPON_CADUCIDAD_DEFAULT = 365;
    public static final String GARANTIA_EXT_TEXTO = "CONSTE POR EL PRESENTE, LA CONTRATACIÓN QUE REALIZA EL CLIENTE A COMOHOGAR S.A., DE UNA EXTENSIÓN DE GARANTÍA ORIGINAL SOBRE EL BIEN ANTES DETALLADO. LOS TÉRMINOS Y CONDICIONES DE LA PRESENTE EXTENSIÓN, QUE GARANTIZA COMOHOGAR S.A. SON LAS MISMAS CONDICIONES QUE LA GARANTÍA ORIGINAL DEL FABRICANTE Y QUE EL CLIENTE DECLARA CONOCER Y ENTENDER AL MOMENTO DE LA SUSCRIPCIÓN DE ESTE. SE EXCLUYE: DAÑOS POR VARIACIÓN DE VOLTAJE; NO QUEDAN CUBIERTOS TODO TIPO DE CARTUCHOS, CASETES, DISQUETES, PROGRAMAS DE SOFTWARE Y PARTES DESECHABLES; Y, DAÑOS OCASIONADOS POR DESASTRE NORMAL. ESTA EXTENSIÓN DE GARANTÍA CADUCARÁ ANTICIPADAMENTE, SI LA INFORMACIÓN ENTREGADA AL SERVICIOS TÉCNICO AUTORIZADO."
            + "RESULTA SER FALSA O ENGAÑOSAMENTE INEXACTA. CADUCARA TAMBIÉN SI EL NÚMERO DE IDENTIFICACIÓN O SERIE DEL PRODUCTO ES ALTERADO EN CUALQUIER FORMA. EL PRECIO DE LA EXTENSIÓN DE GARANTÍA ES DE US$ #P. EL PLAZO DE VIGENCIA ES DE #M MESES CONTADOS A PARTIR DE QUE TERMINE LA GARANTÍA ORIGINAL DEL FABRICANTE. EL CLIENTE PODRÁ CEDER O TRANSFERIR LAS OBLIGACIONES Y DERECHOS DEL PRESENTE CONTRATO SIEMPRE QUE COMUNIQUE A COMOHOGAR S.A. POR ESCRITO LOS DATOS DEL NUEVO PROPIETARIO. CUALQUIER LITIGIO O DEMANDA DERIVADA DE ESTE O SU INCUMPLIMIENTO SE RESOLVERÁ INICIALMENTE POR MEDIACIÓN DIRECTA O ASISTIDA POR UN MEDIADOR DEL CENTRO DE LA "
            + "CÁMARA DE COMERCIO DE QUITO; DE NO LOGRARSE ACUERDO, EL PROCEDIMIENTO SERÁ EL ARBITRAJE EN DERECHO EN EL MISMO CENTRO INDICANDO. EL FALLO SERÁ OBLIGATORIO. EL CLIENTE DEBERÁ PRESENTAR LOS PRODUCTOS CUBIERTOS, JUNTO CON SU FACTURA, EN UNO DE LOS LOCALES DE COMOHOGAR S.A. PARA HACERLA EFECTIVA.";
    public static final String FACT_ELECTRONICA_SOLO_RUC = "FACT.ELECTRONICA.SOLO.RUC";

    public static final String ITEM_ENTREGA_DOMICILIO_WEB_VALOR_1 = "ITEM.ENTREGA.DOMICILIO.VALOR.1";
    public static final String ITEM_ENTREGA_DOMICILIO_WEB_VALOR_2 = "ITEM.ENTREGA.DOMICILIO.VALOR.2";
    public static final String PRECIO_ENTREGA_DOMICILIO = "PRECIO.ENTREGA.DOMICILIO";
    public static final BigDecimal VALOR_ENTREGA_DOMICILIO_WEB = new BigDecimal("500");

    //VARIABLES DE NÚMERO DE IMPRESIONES DE TICKETS
    public static final String COMPROBANTE_ABONO_CONFIG_IMPRESION = "COMPROBANTE.ABONO.CONFIG.IMPRESION";
    public static final String COMPROBANTE_ANULACION_CONFIG_IMPRESION = "COMPROBANTE.ANULACION.CONFIG.IMPRESION";
    public static final String COMPROBANTE_GIFTCARD_CONFIG_IMPRESION = "COMPROBANTE.GIFTCARD.CONFIG.IMPRESION";
    public static final String COMPROBANTE_PAGO_CREDITO_DIRECTO_CONFIG_IMPRESION = "COMPROBANTE.PAGO.CREDITO.DIRECTO.CONFIG.IMPRESION";
    public static final String COMPROBANTE_PAGO_LETRA_CONFIG_IMPRESION = "COMPROBANTE.PAG.LETRA.CONFIG.IMPRESION";
    public static final String COMPROBANTE_CONTRATO_PLAN_CONFIG_IMPRESION = "COMPROBANTE.CONTRATO.PLAN.CONFIG.IMPRESION";
    public static final String COMPROBANTE_VOUCHER_CONFIG_IMPRESION = "COMPROBANTE.VOUCHER.CONFIG.IMPRESION";
    public static final String COMPROBANTE_EXTENSION_GARANTIA_CONFIG_IMPRESION = "COMPROBANTE.EXTENSION.GARANTIA.CONFIG.IMPRESION";
    public static final String COMPROBANTE_ARICULOS_PLAN_CONFIG_IMPRESION = "COMPROBANTE.ARICULOS.PLAN.CONFIG.IMPRESION";
    public static final String COMPROBANTE_ARTICULOS_RESERVA_CONFIG_IMPRESION = "COMPROBANTE.ARTICULOS.RESERVA.CONFIG.IMPRESION";
    public static final String COMPROBANTE_CANCELACIONES_PENDIETES_CONFIG_IMPRESION = "COMPROBANTE.CANCELACIONES.PEND.CONFIG.IMPRESION";
    public static final String COMPROBANTE_BONO_CONFIG_IMPRESION = "COMPROBANTE.BONO.CONFIG.IMPRESION";
    public static final String COMPROBANTE_CUPON_CONFIG_IMPRESION = "COMPROBANTE.CUPON.CONFIG.IMPRESION";
    public static final String COMPROBANTE_DEVOLUCION_CONFIG_IMPRESION = "COMPROBANTE.DEVOLUCION.CONFIG.IMPRESION";
    public static final String COMPROBANTE_FACTURA_CONFIG_IMPRESION = "COMPROBANTE.FACTURA.CONFIG.IMPRESION";
    public static final String COMPROBANTE_COTIZACION_CONFIG_IMPRESION = "COMPROBANTE.COTIZACION.CONFIG.IMPRESION";
    public static final String COMPROBANTE_RESERVACION_CONFIG_IMPRESION = "COMPROBANTE.RESERVACION.CONFIG.IMPRESION";
    public static final String COMPROBANTE_RETIRO_CONFIG_IMPRESION = "COMPROBANTE.RETIRO.CONFIG.IMPRESION";
    public static final String COMPROBANTE_COMPRA_CON_ABONOS_CONFIG_IMPRESION = "COMPROBANTE.COMPRA.CON.ABONOS";
    public static final String COMPROBANTE_CHEQUE_CONFIG_IMPRESION = "COMPROBANTE.CHEQUE.CONFIG.IMPRESION";
    public static final String POS_ESTATUS_ENTREGA_MONTO = "POS.ESTATUS.ENTREGA.MONTO";

    public static final Integer TIEMPO_PASADO = 10;

    //plazos
    public static final String POS_LETRAS_PLAZO_UNA_CUOTA = "POS.LETRAS.PLAZO_UNA_CUOTA";
    public static final String POS_COTIZACION_PLAZO_VIGENCIA = "POS.COTIZACION.PLAZO.VIGENCIA";

    private static Map<String, String> variablesCentral = new HashMap<String, String>();

    // Codigo de local
    public static final String DATABASE_CODIGO_CENTRAL_LOCAL = "DATABASE.CODIGO.CENTRAL.LOCAL";
    public static final String DATABASE_CODIGO_CENTRAL2_LOCAL = "DATABASE.CODIGO.CENTRAL2.LOCAL";

    public static final String DATABASE_CREDITO_USUARIO = "DATABASE.CREDITO.USUARIO";
    public static final String DATABASE_CREDITO_PASSWORD = "DATABASE.CREDITO.PASSWORD";
    public static final String DATABASE_CREDITO_URL = "DATABASE.CREDITO.URL";

    public static final String URL_SERVIDOR_ACTIVEMQ = "URL.ACTIVEMQ";
    public static final String QUEUE_PEDIDO_FACTURADO = "QUEUE.PEDIDO.FACTURADO";
    public static final String QUEUE_ENVIO_DOMICILIO = "QUEUE.ENVIO.DOMICILIO";
    public static final String QUEUE_ENVIO_SMS = "QUEUE.ENVIO.SMS";
    public static final String QUEUE_KARDEX_POS = "QUEUE.KARDEX.POS";
    public static final String QUEUE_INTERCAMBIO_NOTA_CREDITO = "QUEUE.INTERCAMBIO.NOTA.CREDITO";
    public static final String PUERTO_SOCKET_NOTACREDITO = "PUERTO.SOCKET.NOTACREDITO";
    public static final String QUEUE_CREDITO_CUPO = "QUEUE.CREDITO.CUPO";
    public static final String QUEUE_COMPLETAR_ORDEN_PREFACTURA = "QUEUE.COMPLETAR.ORDEN.PREFACTURA";

    public static final String USUARIO_EGO_DEFECTO = "USUARIO.EGO.DEFECTO";

    public static final String WEBSERVICE_NOTACREDITO_URL = "WEBSERVICE.NOTACREDITO.URL";
    public static final String WEBSERVICE_PAGINA_WEB_ESB_URL = "WEBSERVICE.ESB.PAGINAWEB.URL";
    public static final String WEBSERVICE_CONSULTAS_ESB_URL = "WEBSERVICE.ESB.CONSULTAS";
    public static final String TIPO_PAGO_PLACE_PAY = "TIPO.PAGO.PLACE.PAY";
    public static final String TIPO_PAGO_PLACE_PAYMENTEZ = "TIPO.PAGO.PAYMENTEZ";
    public static final String WEBSERVICE_ERP_MOVIL_ENDPOINT_URL = "WEBSERVICE.ERP-MOVIL.ENDPOINT";
    public static final String CREDENCIALES_AUTENTICACION_ERP_MOVIL_JWT = "CREDENCIALES.AUT.ERP-MOVIL.JWT";
    public static final String VARIABLES_OTP_CREDITO_DIRECTO = "VARIABLES.OTP.CREDITO.DIRECTO";
    public static final String POS_VALIDACION_OTP = "POS.ACTIVO.VALIDACION.OTP";
    public static final String VARIABLES_TIEMPO_REIMPRESION_NOTA_CREDITO = "VARIABLES.TIEMPO.REIMPRESION.NOTACREDITO";
    public static final String VARIABLES_TIEMPO_REIMPRESION_BONO = "VARIABLES.TIEMPO.REIMPRESION.BONO";
    public static final String ACTIVO_TIEMPO_REIMPRESION = "ACTIVO.TIEMPO.REIMPRESION";
    public static final String POS_VALIDACION_PROTECCION_DATOS = "POS.ACTIVO.POLITICA.PROTECCION.DATOS";
    public static final String POS_GENERACION_CUPONES_ONLINE = "POS.GENERACION.CUPONES.ONLINE";
    public static final String VARIABLE_TIEMPO_GENERA_NOTA_CREDITO = "VARIABLE.TIEMPO.GENERA.NOTA.CREDITO";
    public static final String ACTIVO_SERVICIO_WEB_SUPERMAXI = "ACTIVO.SERVICIO.WEB.SUPERMAXI";
    public static final String ACTIVO_VARIABLE_PERFIL_NOTA_CREDITO = "ACTIVO.VARIABLE.PERFIL.NOTA_CREDITO";
    public static final String ACTIVO_NOTIFICA_AUT_CONSUMO_CREDITO = "ACTIVO.NOTIFICA.AUT.CREDITO";

    public static void obtieneVariables() {
        try {
            variablesCentral.clear();
            List<VariableAlm> lAux = VariablesDao.consultaVariables();
            for (VariableAlm vAux : lAux) {
                if (vAux.getValor() != null) {
                    variablesCentral.put(vAux.getIdVariable(), vAux.getValor());
                } else {
                    variablesCentral.put(vAux.getIdVariable(), vAux.getValorDefecto());
                }
            }
            log.debug("Listando valor de VARIABLES CENTRAL configurado:::: ");
            log.debug("\n" + ToString.toStringMap(variablesCentral));
        } catch (Exception ex) {
            log.error("VariablesServices: Error al Consultar las variables", ex);
        }
    }

    public static String getVariable(String var) {

        if (variablesCentral.isEmpty()) {
            obtieneVariables();
        }
        return variablesCentral.get(var);
    }

    public static Integer getVariableAsInt(String var) {
        try {
            return Integer.valueOf(getVariable(var));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long getVariableAsLong(String var) {
        try {
            return Long.valueOf(getVariable(var));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal getVariableAsBigDecimal(String var) {
        try {
            return new BigDecimal(getVariable(var));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Boolean getVariableAsBoolean(String var) {

        String boolVar = getVariable(var);
        if (boolVar != null && boolVar.equals("S")) {
            return true;
        } else if (boolVar != null && boolVar.equals("N")) {
            return false;
        } else {
            return null;
        }
    }

    public static ConfigImpresion getVariableAsConfigImpresion(String var) {

        return new ConfigImpresion(getVariable(var));

    }

    /**
     * Obtiene la hora y fecha del servidor del local
     *
     * @return
     * @throws Exception
     */
    public static Date consultaFechaHoraServidor() throws Exception {
        return VariablesDao.consultaFechaHoraServidor();
    }
}
