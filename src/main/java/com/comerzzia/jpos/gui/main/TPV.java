/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.main;

import com.comerzzia.jpos.entity.db.SriCaja;
import com.comerzzia.jpos.gui.JInicioPC;
import com.comerzzia.jpos.gui.apariencia.BotonEnFocoPainter;
import com.comerzzia.jpos.gui.respaldo.JInicioFallo;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.servicios.core.contadores.ServicioContadores;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasServices;
import com.comerzzia.jpos.servicios.login.Apariencia;
import com.comerzzia.jpos.servicios.login.DatosConfiguracion;
import com.comerzzia.jpos.servicios.login.DatosDatabase;
import com.comerzzia.jpos.servicios.login.LecturaConfiguracion;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.util.thread.NotaCreditoSocketThread;
import com.comerzzia.jpos.util.ntp.NTPService;
import com.comerzzia.jpos.util.thread.BancosMediosPagoThread;
import com.comerzzia.jpos.util.thread.ConsultaCreditoDirectoThread;
import com.comerzzia.jpos.util.thread.EntregaDomicilioThread;
import com.comerzzia.jpos.util.thread.MarcasMedioPagoThread;
import com.comerzzia.jpos.util.thread.PlanFinanciamientoThread;
import com.comerzzia.jpos.util.thread.VentaLineaThread;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.Contador;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.swing.LookAndFeel;

import javax.persistence.EntityManagerFactory;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 *
 * @author MGRI
 */
public class TPV {

    // logger
    protected static Logger log = Logger.getMLogger(TPV.class);
    protected static String modoReserva = null;
    static final String ERROR_NTP = "La fecha que registra la caja no es correcta. Es necesario actualizar el reloj de la caja para poder continuar.... Comuniquese con el Departamento de Sistemas";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        log.info("-------------------------------------------------");
        log.info("-------------------------------------------------");
        log.info("---- COMERZZIA POS - " + Constantes.VERSION_NUMERO_POS + " - SUKASA --------------- ");
        log.info("------------- r. " + Constantes.VERSION_POS + " -----------------------"); // YYYYMMDD
        log.info("-------------------------------------------------");
        try {
            if (args != null && args.length >= 1) {
                String modo = args[0];
                String[] vmodo = modo.split("=");
                if (vmodo.length == 2 && vmodo[0].equals("modorespaldo") && vmodo[1].equals("si")) {
                    modoReserva = vmodo[1];
                }
            }
        } catch (Exception e) {
            log.error("Error recibiendo el parámetro de configuración modoreserva. Parámetro no válido");
            System.exit(1);
        }
        log.info("La fecha de hoy es: " + new Fecha());

        //RepaintManager.setCurrentManager(new MyRepaintManager());
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                try {
                    Contador.setContador(0);
                } catch (Exception e) {
                    log.error("DispatchKeyEvent() : Fallo en el refresco del contador." + e.getMessage());
                }
                return false;
            }
        });
        // Evento para capturar cualquier movimiento o pulsacion del ratón y que el contador de bloqueo se resetee.
        // Se ha comentado este código para mejorar la eficiencia de la aplicación y debido a que en bebemundo no van a usar mucho el ratón.
        /*
        long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK;
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
        
        @Override
        public void eventDispatched(AWTEvent awte) {
        if(awte instanceof MouseEvent){
        Contador.setContador(0);
        }
        }
        }, eventMask);*/
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                // Información sobre la plataforma
                try {
                    log.info("INFORMACIÓN SOBRE INTERFAZ GRÁFICA");
                    if (java.awt.Toolkit.getDefaultToolkit().isAlwaysOnTopSupported()) {
                        log.info("Soporte para AlwaysOnTop: OK");
                    } else {
                        log.error("Soporte para AlwaysOnTop: No soportado!!");
                    }
                    if (java.awt.Toolkit.getDefaultToolkit().isModalExclusionTypeSupported(ModalExclusionType.TOOLKIT_EXCLUDE)) {
                        log.info("Soporte para ModalExclusionType: OK TOOLKIT");
                    } else if (java.awt.Toolkit.getDefaultToolkit().isModalExclusionTypeSupported(ModalExclusionType.APPLICATION_EXCLUDE)) {
                        log.info("Soporte para ModalExclusionType: OK APLICATION");
                    } else {
                        log.error("Soporte para ModalExclusionType: NO SOPORTADO!!");
                    }
                    if (java.awt.Toolkit.getDefaultToolkit().isModalityTypeSupported(ModalityType.TOOLKIT_MODAL)) {
                        log.info("Soporte para ModalityTypeSupported: OK TOOLKIT");
                    } else if (java.awt.Toolkit.getDefaultToolkit().isModalityTypeSupported(ModalityType.APPLICATION_MODAL)) {
                        log.info("Soporte para ModalityTypeSupported: OK APLICATION");
                    } else {
                        log.warn("Soporte para ModalityTypeSupported: NO SOPORTADO!!");
                    }
                    log.info("PINPAD: La ruta al fichero INI debe de ser: " + System.getProperties().getProperty("user.dir") + File.separator + "ini" + File.separator + "UC-CAJA.ini");
                    File file = new File(System.getProperties().getProperty("user.dir") + File.separator + "ini" + File.separator + "UC-CAJA.ini");
                    if (!file.exists()) {
                        log.error("PINPAD: No se ha encontrado fichero ini en la ruta: " + System.getProperties().getProperty("user.dir") + File.separator + "ini" + File.separator + "UC-CAJA.ini");
                    } else {
                        log.info("PINPAD: Fichero ini encontrado");
                    }
                } catch (Exception e) {
                    log.error("Error leyendo las propiedades de la interfaz gráfica. (No impide ejecución)");
                }
                try {

                    //Verificación del la fecha contra el servidor NTP
                    log.info("Verificación del la fecha contra el servidor NTP");

                    // La carga de datos puede ser realizada también en el constructor de datos coorporativos
                    leerConfiguracion();

                    // Leer los datos de sesión de la base de datos
                    LecturaConfiguracion.leerDatosSesion(true);

                    //Verificación de fecha de la caja
                    verificarFechaNTP();

                    // Comprobamos que la fecha de fin de autorización no ha sido superada
                    Fecha fechaActual = ServicioContadores.obtenerFechaActual();
                    Fecha fechaFinAutorizacion = new Fecha(Sesion.getEmpresa().getFechafinAutorizacion());
                    Fecha fechaInicioAutorizacion = new Fecha(Sesion.getEmpresa().getFechaInicioAuorizacion());
                    if (fechaInicioAutorizacion.despues(fechaActual)) {
                        log.error("La empresa no ha comenzado su fecha de autorizacion. Fecha actual del servidor de BBDD: " + fechaActual.getString() + " Fecha de inicio de autorizacion: " + fechaInicioAutorizacion.getString());
                        throw new ValidationException("Fecha de inicio de autorizacion no vigente para la empresa");
                    }
                    if (fechaActual.despues(fechaFinAutorizacion)) {
                        log.error("La empresa ha superado su fecha de autorización. Fecha actual del servidor de BBDD: " + fechaActual.getString() + " Fecha de fin de autorización: " + fechaFinAutorizacion.getString());
                        throw new ValidationException("La empresa ha superado su fecha de autorización.");
                    }

                    // Comprobamos si tenemos que actualizar contadores
                    ServicioContadoresCaja.actualizarContadores();

                    // Comprobamos que el porcentaje de iva esta estableido
                    if (Sesion.getEmpresa().getPorcentajeIva() == null) {
                        log.error("La empresa no tiene establecido un porcentaje de IVA");
                        throw new ValidationException("La empresa no tiene establecido un porcentaje de IVA");
                    }
                    if (Sesion.getEmpresa().getPorcentajeRetencion() == null) {
                        log.error("La empresa no tiene establecido un porcentaje de Retención a la fuente");
                        throw new ValidationException("La empresa no tiene establecido un porcentaje de Retención a la fuente");
                    }

                    try {
                        Object apariencia = Class.forName(Variables.getVariable(Variables.POS_UI_LOOK_AND_FEEL)).newInstance();
                        if (apariencia instanceof LookAndFeel) {
                            UIManager.setLookAndFeel(new NimbusLookAndFeel() {

                                @Override
                                public UIDefaults getDefaults() {

                                    UIDefaults ret = super.getDefaults();
                                    // Para cambiar la fuente de la aplicación
                                    if (Variables.getVariable(Variables.POS_UI_FUENTE_BOTONES_TIPO) != null && !Variables.getVariable(Variables.POS_UI_FUENTE_BOTONES_TAMANO).isEmpty()) {
                                        log.debug("Cargando fuentes de la aplicación");
                                        Font sf = new Font(Variables.getVariable(Variables.POS_UI_FUENTE_TIPO), 0, Integer.parseInt(Variables.getVariable(Variables.POS_UI_FUENTE_TAMANO)));
                                        Font arialF = new Font(Variables.getVariable(Variables.POS_UI_FUENTE_BOTONES_TIPO), 0, Integer.parseInt(Variables.getVariable(Variables.POS_UI_FUENTE_BOTONES_TAMANO)));
                                        ret.put("Label.font", sf);
                                        ret.put("Button.font", arialF);
                                        //ret.put("Button.font", arialF);
                                        ret.put("ToggleButton.font", sf);
                                        ret.put("RadioButton.font", sf);
                                        ret.put("CheckBox.font", sf);
                                        ret.put("ColorChooser.font", sf);
                                        ret.put("ToggleButton.font", sf);
                                        ret.put("ComboBox.font", sf);
                                        ret.put("ComboBoxItem.font", sf);
                                        ret.put("InternalFrame.titleFont", sf);
                                        ret.put("List.font", sf);
                                        ret.put("MenuBar.font", sf);
                                        ret.put("Menu.font", sf);
                                        ret.put("MenuItem.font", sf);
                                        ret.put("RadioButtonMenuItem.font", sf);
                                        ret.put("CheckBoxMenuItem.font", sf);
                                        ret.put("PopupMenu.font", sf);
                                        ret.put("OptionPane.font", sf);
                                        ret.put("Panel.font", sf);
                                        ret.put("ProgressBar.font", sf);
                                        ret.put("ScrollPane.font", sf);
                                        ret.put("Viewport", sf);
                                        ret.put("TabbedPane.font", sf);
                                        ret.put("TableHeader.font", sf);
                                        ret.put("TextField.font", sf);
                                        ret.put("PasswordFiled.font", sf);
                                        ret.put("TextArea.font", sf);
                                        ret.put("TextPane.font", sf);
                                        ret.put("EditorPane.font", sf);
                                        ret.put("TitledBorder.font", sf);
                                        ret.put("ToolBar.font", sf);
                                        ret.put("ToolTip.font", sf);
                                        ret.put("Tree.font", sf);
                                    }
                                    ret.put("Panel[Enabled].background", Color.WHITE);
                                    //ret.put("JButton[Disabled].background", new Color(231, 214, 255));
                                    //ret.put("JButton[Disabled].background", new Color(120, 20, 20));

                                    //ret.put("Button.background", new Color(189, 168, 218)); //bebemundo
                                    ret.put("Button.background", new Color(121, 141, 157)); //Sukasa
                                    //ret.put("Button.disabled", new Color(231, 214, 255)); //Sukasa
                                    //ret.put("Button.disabled", new Color(120, 20, 20)); //Sukasa                                                                     

                                    //ButtonPainter b = (ButtonPainter)ret.get("Button[Disabled].backgroundPainter");
                                    //BotonDeshabilitadoPainter p = new BotonDeshabilitadoPainter(b);
                                    //ret.put("Button[Disabled].backgroundPainter", p);
                                    //ret.put("Button.disabled", new Color(231, 214, 255)); //Sukasa
                                    return ret;

                                }
                            });

                            UIDefaults botonDefaults = new UIDefaults();
                            UIDefaults cuadrosTextoDefaults = new UIDefaults();
                            UIManager.getDefaults().put("Panel[Enabled].background", Color.WHITE);
                            //UIManager.getDefaults().put("Label.font", new Font("GungsuhChe", 0, 12));
                            botonDefaults.put("Button[Focused].backgroundPainter", new BotonEnFocoPainter());
                            Apariencia.setAparienciaBoton(botonDefaults);
                            Apariencia.setAparienciaCuadroTexto(cuadrosTextoDefaults);
                        }

                        //Actualizamos la hora de la caja
                        String os = System.getProperty("os.name").toLowerCase();
                        if (os.contains("win")) {
                            //Operating system is based on Windows

                        } else if (os.contains("osx")) {
                            //Operating system is Apple OSX based
                        } else if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
                            //Operating system is based on Linux/Unix/*AIX
                            Runtime.getRuntime().exec("ntpdate pool.ntp.org");
                            log.info("Hora actualizada");
                        }

                    } catch (Exception e) {
                        log.warn("No se puede establecer la apariencia deseada", e);
                    }

                    log.info("Cargando pantalla de login...");
                    JInicioPC inicioPC = new JInicioPC();
                    inicioPC.setSize(1024, 768);
                    inicioPC.setLocationRelativeTo(null);
                    inicioPC.initFrame();

                    String puerto = Variables.getVariable(Variables.PUERTO_SOCKET_NOTACREDITO);
                    if (puerto != null) {
                        NotaCreditoSocketThread notaCreditoSocketThread = new NotaCreditoSocketThread(Integer.parseInt(puerto));
                        notaCreditoSocketThread.start();
                    }

                    if (Sesion.config.isVentaEnLinea()) {

                        //Venta en linea POS 
                        VentaLineaThread ventaLineaThread = new VentaLineaThread();
                        ventaLineaThread.start();

                        PlanFinanciamientoThread planFinanciamientoThread = new PlanFinanciamientoThread();
                        planFinanciamientoThread.start();

                        BancosMediosPagoThread bancosMediosPagoThread = new BancosMediosPagoThread();
                        bancosMediosPagoThread.start();

                        EntregaDomicilioThread entregaDomicilioThread = new EntregaDomicilioThread();
                        entregaDomicilioThread.start();

                        MarcasMedioPagoThread marcasMedioPagoThread = new MarcasMedioPagoThread();
                        marcasMedioPagoThread.start();
                        
                        ConsultaCreditoDirectoThread consultaCredito = new ConsultaCreditoDirectoThread();
                        consultaCredito.start();
                    }
                    
                    

                } catch (Exception e) {
                    log.error("Error en el arranque de la aplicación");
                    log.error(e.getMessage(), e);
                    boolean posmaster = false;
                    //
                    // Comprobamos que estamos en un POS correctamente configurado con propiedades de reserva.
                    ResourceBundle recursos = java.util.ResourceBundle.getBundle("jpos-cmz-config");
                    try {
                        String comprReserva = recursos.getString("comerzzia.jpos.database.reserva.esquema.config");
                        if (comprReserva != null && !comprReserva.isEmpty()) {
                            posmaster = true;
                        }
                    } catch (Exception ex) {
                    }

                    // Modificación para que salga aviso gráfico y opción de respaldo
                    JInicioFallo inicioFallo = new JInicioFallo();
                    inicioFallo.setSize(500, 400);
                    inicioFallo.setLocationRelativeTo(null);

                    if (e instanceof ValidationException) {
                        inicioFallo.addError(e.getMessage());
                        inicioFallo.setModoReserva(false);
                    } else {
                        inicioFallo.setModoReserva(true);
                    }

                    inicioFallo.initFrame(posmaster);
                }

            }

            /**
             * Lee los datos corporativos e inicia sesion
             */
            private void leerConfiguracion() throws ValidationException {
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

                    try {
                        datosConf.setUsuarioVentaOnline(recursos.getString("comerzzia.jpos.configuracion.usuario.venta.online"));
                        datosConf.setPasswordVentaOnline(recursos.getString("comerzzia.jpos.configuracion.password.venta.online"));
                        datosConf.setVentaEnLinea(recursos.getString("comerzzia.jpos.configuracion.activo.venta.online"));
                    } catch (MissingResourceException ex) {
                        log.error("No existe la variable para la venta online");
                    }

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

        });
    }

    private static void verificarFechaNTP() throws ValidationException {
        NTPService servicioNTP = new NTPService();
        log.info("Obteniendo fecha desde el servidor NTP");

        //Fecha NTP
        Date fechaNTP = servicioNTP.getNTPDate();
        log.info("Fecha-Hora NTP: " + fechaNTP);
        int anio = 0, mes = 0, dia = 0;
        if (fechaNTP != null) {
            //Consulta la fecha desde el internet. 
            Calendar calendarNTP = Calendar.getInstance();
            calendarNTP.setTimeInMillis(fechaNTP.getTime());
            anio = calendarNTP.get(Calendar.YEAR);
            mes = calendarNTP.get(Calendar.MONTH);
            dia = calendarNTP.get(Calendar.DATE);
        } else {
            //Consulta la fecha de la base de datos
            Fecha fechaActual = ServicioContadores.obtenerFechaActual();
            Date fechaBdd = fechaActual.getDate();
            Calendar calendarBdd = Calendar.getInstance();
            calendarBdd.setTimeInMillis(fechaBdd.getTime());
            anio = calendarBdd.get(Calendar.YEAR);
            mes = calendarBdd.get(Calendar.MONTH);
            dia = calendarBdd.get(Calendar.DATE);
        }

        //Fecha Máquina local
        Date fechaActual = new Date();
        log.info("Fecha-Hora caja: " + fechaActual);
        Calendar calendarActual = Calendar.getInstance();
        calendarActual.setTimeInMillis(fechaActual.getTime());
        int anioActual = calendarActual.get(Calendar.YEAR);
        int mesActual = calendarActual.get(Calendar.MONTH);
        int diaActual = calendarActual.get(Calendar.DATE);

        try {
            if (anio != anioActual || mes != mesActual || dia != diaActual) {
                log.warn(ERROR_NTP);
                throw new ValidationException("<html>Error: " + ERROR_NTP + " </html>");
            }
        } catch (ValidationException e) {
            throw e;
        }

        log.info("");
        log.info("");
        log.info("Fecha-Hora NTP: " + fechaNTP);

    }
}
