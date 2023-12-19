
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasCuponPromo;
import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.entity.db.SriCaja;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasServices;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.DatosConfiguracion;
import com.comerzzia.jpos.servicios.login.DatosDatabase;
import com.comerzzia.jpos.servicios.login.LecturaConfiguracion;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.util.Constantes;
import es.mpsistemas.util.log.Logger;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.UUID;
import javax.persistence.EntityManagerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gabriel Simbania
 */
public class CrearCuponesBlobTest {

    protected static Logger log = Logger.getMLogger(CrearDocImpCuponesTest.class);
    protected static String modoReserva = null;

    public static void main(String[] args) {

        try {
            // La carga de datos puede ser realizada también en el constructor de datos coorporativos
            leerConfiguracion();

            // Leer los datos de sesión de la base de datos
            LecturaConfiguracion.leerDatosSesion(true);

            //DocumentosService.reconstruirCupones();
            List<DocumentosImpresosBean> impresos = new ArrayList<>();

            String[] idCupones = {"999034050003560246717",
                                  "999034050003560258405"};

            Short i = 7; //TODO Numero para comenzar en el id_cupon
            String uidDocumento = "0a6a4a30-b6d5-4cb1-8aff-fe8dff3460aa";

            for (String cupon : idCupones) {
                //CodigoBarrasCuponPromo codBarrasCupon = new CodigoBarrasCuponPromo(cupon.getIdCupon());
                //cupon.setCodBarras(codBarrasCupon.getCodigoBarras());

                DocumentosImpresosBean documentoImpreso = new DocumentosImpresosBean();
                documentoImpreso.setTipoImpreso(DocumentosImpresosBean.TIPO_CUPON);
                documentoImpreso.setUidDocumento(uidDocumento);
                documentoImpreso.setIdImpreso(i);

                /**
                 * ********** Verificar el formato para que se guarde en el
                 * blob de los documentos impresos y modificar el texto con los
                 * datos de la factura ***********
                 */
                String blob = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<output>\n"
                        + "	<ticket>\n"
                        + "		<line>\n"
                        + "			<text align=\"left\" length=\"40\" >"+cupon+"</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"left\" length=\"40\" >COMOHOGAR S.A       SUKASA VILLAGE</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"left\" length=\"40\" >CC VILLAGE</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"left\" length=\"11\">    R.U.C: </text>\n"
                        + "			<text align=\"left\" length=\"29\">1790746119001</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"left\" length=\"40\" >GUAYAQUIL, 08 de noviembre de 2023</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" bold=\"true\" >EL BUZON DE LOS SUEÃ‘OS\n"
                        + "			</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" bold=\"true\" >POR COMPRAS SUPERIORES A 100 PARTICIPA E</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" bold=\"true\" > EL SORTEO</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" bold=\"true\"/>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" bold=\"true\">CupÃ³n : "+cupon+"</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" bold=\"true\">Factura : 034-106-000003669</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" bold=\"true\">Fecha de la factura: 08-nov-2023 11:29</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"left\" length=\"40\">  SOCIO: RUC: 0905985081001</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"left\" length=\"40\">  Nombre: Pedro Torres</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"left\" length=\"40\">  TelÃ©fono: 093945204</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"left\" length=\"40\">  DirecciÃ³n: Urbanizacion Terrasol A 25</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"left\" length=\"40\">  Fecha de expediciÃ³n: 08-nov-2023 11:29</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"left\" length=\"40\">  Fecha de caducidad: 27-nov-2023 00:00</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" >UN NIÃ‘O DE LA FAMILIA PODRA ESCRIBIRLE A</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" >SANTA Y HACER REALIDAD SU SUEÃ‘O. VALOR M</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" >XIMO DEL SUEÃ‘O $5.000. PARTICIPAN NIÃ‘OS</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" >E 7 A 16 AÃ‘OS. LAS CARTAS VACIAS SERAN E</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" >TREGADAS EN CAJA JUNTO CON EL CUPON. UNA</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" >CARTA POR FACTURA . LA CARTA DEBERA SER</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" >SCRITA POR EL NIÃ‘O. DEPOSITA EL CUPON JU</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" >TO CON LA CARTA EN EL BUZON DEL RINCON D</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\" > LOS SUEÃ‘OS. APLICAN RESTRICCIONES.</text>\n"
                        + "		</line>\n"
                        + "		<line>\n"
                        + "			<text align=\"center\" length=\"40\">.</text>\n"
                        + "		</line>\n"
                        + "		<line/>\n"
                        + "		<line/>\n"
                        + "		<line/>\n"
                        + "		<line/>\n"
                        + "		<line/>\n"
                        + "		<line/>\n"
                        + "		<line/>\n"
                        + "		<line/>\n"
                        + "		<line/>\n"
                        + "		<line/>\n"
                        + "	</ticket>\n"
                        + "</output>";

                documentoImpreso.setImpreso(blob.getBytes("UTF-8"));

                impresos.add(documentoImpreso);
                i++;
            }
            DocumentosService.crearDocumentoImpresoOtroLocal(impresos);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);

    }

    private static void leerConfiguracion() throws ValidationException {
        log.info("Iniciando carga de fichero properties de configuración....");
        try {
            // Accedemos a las propiedades
            ResourceBundle recursos = java.util.ResourceBundle.getBundle("jpos-cmz-config");

            // leer datos de configuracion
            DatosConfiguracion datosConf = Sesion.getDatosConfiguracion();
            /**
             * RD PinPad Plan D
             */
            //datos Pin Pad Plan D
//                    datosConf.setPINPAD_IP(recursos.getString("commerzzia.jpos.pinpad.ip"));
//                    datosConf.setPINPAD_PUERTO(recursos.getString("commerzzia.jpos.pinpad.puerto"));
//                    datosConf.setPINPAD_IPHOSTDATAFASTR1(recursos.getString("commerzzia.jpos.pinpad.ipHostDatafastR1"));
//                    datosConf.setPINPAD_PUERTODATAFASTCPR1(recursos.getString("commerzzia.jpos.pinpad.puertoDataFastcpR1"));
//                    datosConf.setPINPAD_IPHOSTDATAFASTR1ALTERNA(recursos.getString("commerzzia.jpos.pinpad.ipHostDatafastR1Aleterna"));
//                    datosConf.setPINPAD_PUERTODATAFASTCPR1ALTERNO(recursos.getString("commerzzia.jpos.pinpad.puertoDataFastcpR1Alterno"));
//                    datosConf.setPINPAD_IPHOSTMEDIANETR2(recursos.getString("commerzzia.jpos.pinpad.ipHostMediaNetR2"));
//                    datosConf.setPINPAD_PUERTOMEDIANETCPR1(recursos.getString("commerzzia.jpos.pinpad.puertoMediaNetcpR1"));
//                    datosConf.setPINPAD_IPHOSTMEDIANETR1ALTERNA(recursos.getString("commerzzia.jpos.pinpad.ipHostMediaNetR1Aleterna"));
//                    datosConf.setPINPAD_PUERTOMEDIANETCPR1ALTERNO(recursos.getString("commerzzia.jpos.pinpad.puertoMediaNetcpR1Alterno"));
            ///////////////////////////////////////////////////////////////////////////
            //Datos tabla amortización//
            datosConf.setDATOS_TABLA_AMORTIZACION(recursos.getString("comerzia.jpos.tabla.amortizacion.categorias").split("-"));
            ///////////////////////////////////////////////////////////////////////////
            datosConf.setCodcaja(recursos.getString("comerzzia.jpos.datosCorporativos.caja"));
            datosConf.setTipoImpresionTicket(recursos.getString("comerzzia.jpos.configuracion.tipo.impresion")); //                    
            datosConf.setEsquemaFactura(recursos.getString("comerzzia.jpos.configuracion.esquema.factura"));
            datosConf.setEsquemaCotizacion(recursos.getString("comerzzia.jpos.configuracion.esquema.cotizacion"));
            datosConf.setEsquemaChequeAnverso(recursos.getString("comerzzia.jpos.configuracion.esquema.cheque.anverso"));
            datosConf.setEsquemaChequeReverso(recursos.getString("comerzzia.jpos.configuracion.esquema.cheque.reverso"));
            datosConf.setEsquemaReciboDevolucion(recursos.getString("comerzzia.jpos.configuracion.esquema.recibo.devolucion"));
            datosConf.setEsquemaReservacion(recursos.getString("comerzzia.jpos.configuracion.esquema.recibo.reservacion"));
            datosConf.setModoDesarrollo(recursos.getString("comerzzia.jpos.configuracion.modo.desarrollo"));
            datosConf.setUsuarioModoDesarrollo(recursos.getString("comerzzia.jpos.configuracion.modo.desarrollo.usuario"));
            datosConf.setPasswordModoDesarrollo(recursos.getString("comerzzia.jpos.configuracion.modo.desarrollo.password"));
            datosConf.setLimiteRetiro(new BigDecimal(recursos.getString("comerzzia.jpos.configuracion.limiteRetiro")));
            /**
             * RD Mensajes De texto
             */
            //datos para sms
            datosConf.setDATABASE_SMS_PASSWORD(recursos.getString("comerzzia.jpos.password.sms"));
            datosConf.setDATABASE_SMS_USUARIO(recursos.getString("comerzzia.jpos.usuario.sms"));
            datosConf.setDATABASE_SMS_URL(recursos.getString("comerzzia.jpos.url.sms"));
            /**
             * RD Envio De Correo Electronico
             */
            /*  datosConf.setDATABASE_CORREO_USUARIO(recursos.getString("comerzzia.jpos.usuario.correo"));
                    datosConf.setDATABASE_CORREO_PASSWORD(recursos.getString("comerzzia.jpos.password.correo"));
                    datosConf.setDATABASE_CORREO_HOST(recursos.getString("comerzzia.jpos.host.correo"));
                    datosConf.setDATABASE_CORREO_CORREO(recursos.getString("comerzzia.jpos.correo.correo"));
                    datosConf.setDATABASE_CORREO_ASUNTO(recursos.getString("comerzzia.jpos.asunto.correo"));*/

            datosConf.setEsquemaCreditoDirecto(DocumentosImpresosBean.PLANTILLA_CREDITO_DIRECTO);
            try {
                datosConf.setTimePinPad(new Long(recursos.getString("comerzzia.jpos.configuracion.tiempo.pinpad")));
            } catch (Exception ignore) {
            }

            try {
                String recursoFacturaPendienteDespacho = recursos.getString("comerzzia.jpos.configuracion.esquema.factura.pendiente.despacho");
                if (recursoFacturaPendienteDespacho != null && !recursoFacturaPendienteDespacho.isEmpty()) {
                    datosConf.setEsquemaFacturaPendienteDespacho(recursos.getString("comerzzia.jpos.configuracion.esquema.factura.pendiente.despacho"));
                } else {
                    datosConf.setEsquemaFacturaPendienteDespacho(recursos.getString(DocumentosImpresosBean.PLANTILLA_PENDIENTE_DESPACHO));
                }
            } catch (Exception e) {
                datosConf.setEsquemaFacturaPendienteDespacho(DocumentosImpresosBean.PLANTILLA_PENDIENTE_DESPACHO);
            }

            // Configuración de la impresora principal
            datosConf.setCadenaImpresoraTicket(recursos.getString("comerzzia.jpos.configuracion.dispositivo.impresora.tickets"));
            if (!datosConf.getCadenaImpresoraTicket().toUpperCase().contains("DRIVER")) {
                datosConf.setBitstTickets(Integer.valueOf(recursos.getString(("comerzzia.jpos.configuracion.dispositivo.impresora.tickets.bits"))));
                datosConf.setStopTickets(Integer.valueOf(recursos.getString(("comerzzia.jpos.configuracion.dispositivo.impresora.tickets.stop"))));
                datosConf.setParidadTickets(Integer.valueOf(recursos.getString(("comerzzia.jpos.configuracion.dispositivo.impresora.tickets.paridad"))));
                datosConf.setVelocidadTickets(Integer.valueOf(recursos.getString(("comerzzia.jpos.configuracion.dispositivo.impresora.tickets.velocidad"))));
            }

            // Configuración de la impresora adicional
            datosConf.setCadenaImpresoraAdicional(recursos.getString("comerzzia.jpos.configuracion.dispositivo.impresora.adicional"));
            if (!datosConf.getCadenaImpresoraAdicional().toUpperCase().contains("DRIVER")) {
                datosConf.setVelocidadTicketAdicional(Integer.valueOf(recursos.getString(("comerzzia.jpos.configuracion.dispositivo.impresora.adicional.velocidad"))));
                datosConf.setBitstTicketAdicional(Integer.valueOf(recursos.getString(("comerzzia.jpos.configuracion.dispositivo.impresora.adicional.bits"))));
                datosConf.setStopTicketAdicional(Integer.valueOf(recursos.getString(("comerzzia.jpos.configuracion.dispositivo.impresora.adicional.stop"))));
                datosConf.setParidadTicketAdicional(Integer.valueOf(recursos.getString(("comerzzia.jpos.configuracion.dispositivo.impresora.adicional.paridad"))));
            }
            //Configuración para impresora térmica
            if (recursos.containsKey("comerzzia.jpos.configuracion.dispositivo.impresora.termica")) {
                datosConf.setImpresoraTermica(recursos.getString("comerzzia.jpos.configuracion.dispositivo.impresora.termica"));
            }
            if (recursos.containsKey("comerzzia.jpos.configuracion.dispositivo.impresora.termica.archivos")) {
                datosConf.setRutaArchivosImpresoraTermica(recursos.getString("comerzzia.jpos.configuracion.dispositivo.impresora.termica.archivos"));
            }
            datosConf.setEsquemaComprobanteReservacion(recursos.getString("comerzzia.jpos.configuracion.esquema.recibo.comprobanteReservacion"));
            datosConf.setEsquemaComprobanteRetiro(recursos.getString("comerzzia.jpos.configuracion.esquema.recibo.comprobanteRetiro"));
            datosConf.setEsquemaComprobanteBono(recursos.getString("comerzzia.jpos.configuracion.esquema.recibo.comprobanteBono"));
            datosConf.setEsquemaListadoArticulosReserva(recursos.getString("comerzzia.jpos.configuracion.esquema.listado.articulos.reserva"));
            datosConf.setEsquemaCupon(recursos.getString("comerzzia.jpos.configuracion.esquema.cupon"));
            datosConf.setEsquemaComprobanteGiftCard(recursos.getString("comerzzia.jpos.configuracion.esquema.recibo.comprobante.giftcard"));
            datosConf.setEsquemaComprobanteVoucher(recursos.getString("comerzzia.jpos.configuracion.esquema.recibo.comprobante.voucher"));
            datosConf.setEsquemaListadoArticulosPlan(recursos.getString("comerzzia.jpos.configuracion.esquema.listado.articulos.plan"));
            datosConf.setEsquemaContratoPlan(recursos.getString("comerzzia.jpos.configuracion.esquema.contrato.plan"));
            datosConf.setEsquemaExtensionGarantia(recursos.getString("comerzzia.jpos.configuracion.esquema.extension.garantia"));
            datosConf.setEsquemaComprobantePagoCreditoDirecto(recursos.getString("comerzzia.jpos.configuracion.esquema.comprobante.pago.creditodirecto"));
            datosConf.setEsquemaComprobantePagoLetra(recursos.getString("comerzzia.jpos.configuracion.esquema.comprobante.pago.letra"));
            datosConf.setEsquemaComprobanteListaCancelacionesPendientes(recursos.getString("comerzzia.jpos.configuracion.esquema.listado.cancelaciones.pendientes"));
            datosConf.setEsquemaComprobanteAnulacion(recursos.getString("comerzzia.jpos.configuracion.esquema.comprobante.anulacion"));
            datosConf.setEsquemaComprobanteVoucherPinPad(recursos.getString("comerzzia.jpos.configuracion.esquema.recibo.comprobante.voucher.pinpad"));
            datosConf.setEsquemaComprobanteVoucherAnulacion(recursos.getString("comerzzia.jpos.configuracion.esquema.recibo.comprobante.voucherAnulacion"));

            // tiempo de espera para bloqueo
            datosConf.setTiempoBloqueo(Integer.valueOf(recursos.getString("comerzzia.jpos.configuracion.tiempoBloqueo")));
            datosConf.setModoReserva(modoReserva);

            // Datos relativos a la validación de tarjetas
            datosConf.setRutaValidacionLectura(recursos.getString("comerzzia.jpos.configuracion.validacion.tarjetas.ruta.lectura"));
            datosConf.setRutaValidacionEscritura(recursos.getString("comerzzia.jpos.configuracion.validacion.tarjetas.ruta.escritura"));

            // Datos de plan novios
            datosConf.setPlanNoviosExcelInvitados(recursos.getString("comerzzia.jpos.configuracion.plannovios.invitados.excel"));

            // Autorizador de los pago credito
            try {
                datosConf.setTipoModoPinPad(recursos.getString("comerzzia.jpos.configuracion.modo.autorizacion"));
            } catch (MissingResourceException e) {
                //Si no existe, que coja automáticamente manual
                datosConf.setTipoModoPinPad(DatosConfiguracion.VALIDACION_MANUAL);
            }

            // leer datos de base de datos
            DatosDatabase datosDatabase = Sesion.datosDatabase;

            if (modoReserva != null && modoReserva.equals("si")) {
                datosDatabase.setEsquemaConfig(recursos.getString("comerzzia.jpos.database.reserva.esquema.config"));
                datosDatabase.setEsquemaEmpresa(recursos.getString("comerzzia.jpos.database.reserva.esquema.empresa"));
                datosDatabase.setUrlConexion(recursos.getString("comerzzia.jpos.database.reserva.url"));
                datosDatabase.setDriver(recursos.getString("comerzzia.jpos.database.reserva.driver"));
                datosDatabase.setUsuario(recursos.getString("comerzzia.jpos.database.reserva.usuario"));
                datosDatabase.setPassword(recursos.getString("comerzzia.jpos.database.reserva.password"));
            } else {
                datosDatabase.setEsquemaConfig(recursos.getString("comerzzia.jpos.database.esquema.config"));
                datosDatabase.setEsquemaEmpresa(recursos.getString("comerzzia.jpos.database.esquema.empresa"));
                datosDatabase.setUrlConexion(recursos.getString("comerzzia.jpos.database.url"));
                datosDatabase.setDriver(recursos.getString("comerzzia.jpos.database.driver"));
                datosDatabase.setUsuario(recursos.getString("comerzzia.jpos.database.usuario"));
                datosDatabase.setPassword(recursos.getString("comerzzia.jpos.database.password"));
            }

            datosDatabase.setLogger(recursos.getString("comerzzia.jpos.database.logger"));
            datosDatabase.setLoggerLevel(recursos.getString("comerzzia.jpos.database.logger.level"));

            // Crear el entity Manager de la aplicación
            Map propiedades = new HashMap();
            propiedades.put("eclipselink.session.customizer", "com.comerzzia.util.log.JPAEclipseLinkSessionCustomizer");
            propiedades.put("javax.persistence.jdbc.url", datosDatabase.getUrlConexion());
            propiedades.put("javax.persistence.jdbc.password", datosDatabase.getPassword());
            propiedades.put("javax.persistence.jdbc.driver", datosDatabase.getDriver());
            propiedades.put("javax.persistence.jdbc.user", datosDatabase.getUsuario());
            propiedades.put("eclipselink.logging.level", "FINE");
            propiedades.put("eclipselink.allow-zero-id", "true");

            String unidadPersistencia = "TPVPU";
            EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory(unidadPersistencia, propiedades);
            Sesion.setEmf(emf);

            Map propiedadesCentral = new HashMap();
            propiedadesCentral.put("eclipselink.session.customizer", "com.comerzzia.util.log.JPAEclipseLinkSessionCustomizer");
            propiedadesCentral.put("javax.persistence.jdbc.url", Variables.getVariable(Variables.DATABASE_CENTRAL_URL));
            propiedadesCentral.put("javax.persistence.jdbc.password", Variables.getVariable(Variables.DATABASE_CENTRAL_PASSWORD));
            propiedadesCentral.put("javax.persistence.jdbc.driver", datosDatabase.getDriver());
            propiedadesCentral.put("javax.persistence.jdbc.user", Variables.getVariable(Variables.DATABASE_CENTRAL_USUARIO));
            propiedadesCentral.put("eclipselink.logging.level", "FINE");
            propiedadesCentral.put("eclipselink.allow-zero-id", "true");

            String unidadPersistenciaCentral = "CENTRALPU";
            EntityManagerFactory emfc = javax.persistence.Persistence.createEntityManagerFactory(unidadPersistenciaCentral, propiedadesCentral);
            Sesion.setEmfc(emfc);
            /**
             * RD JPA MENSAJES
             */
//                    Map propiedadesSms = new HashMap();
//                    propiedadesSms.put("eclipselink.session.customizer", "com.comerzzia.util.log.JPAEclipseLinkSessionCustomizer");
//                    propiedadesSms.put("javax.persistence.jdbc.url","jdbc:sqlserver://192.168.2.82;databaseName=SMS_CCC");
//                    propiedadesSms.put("javax.persistence.jdbc.password","123*abc*456");
//                    propiedadesSms.put("javax.persistence.jdbc.driver","com.microsoft.sqlserver.jdbc.SQLServerDriver");
//                    propiedadesSms.put("javax.persistence.jdbc.user","bayteq_sms");
//                    propiedadesSms.put("eclipselink.logging.level", "FINE");
//                    propiedadesSms.put("eclipselink.allow-zero-id", "true");
//
//                    String unidadPersistenciaSms = "SMS";
//                    EntityManagerFactory emfSms = javax.persistence.Persistence.createEntityManagerFactory(unidadPersistenciaSms, propiedadesSms);
//                    Sesion.setEmfsms(emfSms); 

            Sesion.Sesion();
            // Comprobamos que existe la tienda
            if (Sesion.getTienda() == null || Sesion.getTienda().getSriTienda() == null) {
                log.error("Error: LA TIENDA NO EXISTE O ESTÁ MAL CONFIGURADA.");
                throw new ValidationException("Error: LA TIENDA NO EXISTE O ESTÁ MAL CONFIGURADA. REVISAR TABLAS D_TIENDAS_TBL Y SRI_TIENDAS_TBL.");
            }
            // Comprobamos que la tienda esta activa
            if (!Sesion.getTienda().isTiendaActiva()) {
                log.error("Error: LA TIENDA NO ESTA ACTIVA POR EL SRI.");
                throw new ValidationException("<html>Error: LA TIENDA NO ESTA ACTIVA. REVISAR<br> TABLA SRI_TIENDA_TBL.</html>");
            }
            // Consultamos la caja y la guardamos en sesion
            TiendasServices.consultarCajaTiendaASesion(datosConf.getCodcaja(), Sesion.getTienda().getSriTienda().getCodalmsri());
            // Comprobamos que existe y se cargó la caja
            if (Sesion.getTienda().getSriTienda().getCajaActiva() == null) {
                log.error("Error: LA CAJA NO EXISTE. REVISAR TABLAS SRI_TIENDAS_CAJAS_TBL");
                throw new ValidationException("<html>Error: CAJA NO EXISTE EN EL SRI </html>");
            }
            // Comprobamos que la caja esta activa
            if (!Sesion.getTienda().getSriTienda().getCajaActiva().isActiva()) {
                log.error("Error: LA CAJA NO ESTA ACTIVA. REVISAR TABLAS SRI_TIENDAS_CAJAS_TBL");
                throw new ValidationException("<html>CAJA NO ESTÀ ACTIVA POR EL SRI</html>");
            }

            SriCaja sriCaja = Sesion.getTienda().getSriTienda().getCajaActiva();
            sriCaja.setVersion(Constantes.VERSION_NUMERO_POS);
            TiendasServices.actualizarVersion(sriCaja);
            log.info("Fichero de configuración cargado correctamente.");

        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error en la lectura de la configuración de la aplicación");
            log.error(e.getMessage(), e);
        }
        String rutaArchivosImpresoraTermica = Sesion.getDatosConfiguracion().getRutaArchivosImpresoraTermica();
        log.info("rutaArchivosImpresoraTermica " + rutaArchivosImpresoraTermica);
        if (rutaArchivosImpresoraTermica != null && !rutaArchivosImpresoraTermica.equals("")) {
            //Creando carpeta para impresiones
            Path path = Paths.get(rutaArchivosImpresoraTermica);
            if (!Files.exists(path)) {
                //Crear directorio
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //Borrar archivos
                File files = new File(rutaArchivosImpresoraTermica);
                for (File file : files.listFiles()) {
                    if (!file.isDirectory()) {
                        file.delete();
                    }
                }
            }
        }
    }

}
