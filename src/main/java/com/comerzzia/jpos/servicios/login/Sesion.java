/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.login;

import com.comerzzia.jpos.dto.TipoDevolucionDTO;
import com.comerzzia.jpos.dto.ventas.DocumentoDTO;
import com.comerzzia.jpos.dto.ventas.MedioPagoDTO;
import com.comerzzia.jpos.persistencia.clientes.afiliados.TipoAfiliadoBean;
import com.comerzzia.jpos.entity.db.Almacen;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.Empresa;
import com.comerzzia.jpos.entity.db.Impuestos;
import com.comerzzia.jpos.entity.db.MotivoDevolucion;
import com.comerzzia.jpos.entity.db.Vendedor;
import com.comerzzia.jpos.entity.db.Tienda;
import com.comerzzia.jpos.servicios.devoluciones.Devolucion;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuariosDao;
import com.comerzzia.jpos.persistencia.core.usuarios.perfiles.PerfilesUsuariosDao;
import com.comerzzia.jpos.persistencia.credito.plasticos.PlasticoBean;
import com.comerzzia.jpos.persistencia.letras.LetraBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposBean;
import com.comerzzia.jpos.servicios.articulos.ArticuloNotFoundException;
import com.comerzzia.jpos.servicios.clientes.ClientesServices;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasServices;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.devoluciones.NotaCreditoException;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.logs.logsistema.ServicioLogSistema;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.PermisoException;
import com.comerzzia.jpos.servicios.core.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.jpos.servicios.core.permisos.ServicioPermisos;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.core.usuarios.ServicioUsuarios;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCupon;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.promociones.configuracion.billeton.ConfigBilleton;
import com.comerzzia.jpos.servicios.promociones.cuotas.PromocionTipoMesesGracia;
import com.comerzzia.jpos.servicios.promociones.cuotas.PromocionTipoNCuotasGratis;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosCanjeo;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosAcumula;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDescuentoCombinado;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDiaSocio;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDtoManualTotal;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.criptografia.CriptoException;
import com.comerzzia.jpos.util.db.Connection;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import java.awt.Image;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class Sesion {

    // TODO: CONFIG - la clase que tiene los datos del properties no puede pertenecer a la sesión, debe estar en un paquete de utilidades como capa horizontal
    public static DatosConfiguracion config = new DatosConfiguracion();
    public static DatosDatabase datosDatabase = new DatosDatabase();
    // Vendedores de la tienda
    private static List<Vendedor> listaVendedores;
    // Ticket en sesion: sustituirÃ¡ al Ticketactual
    private static TicketS ticket;
    // ReservaciÃ³n de sesiÃ³n
    private static Reservacion reservacion;
    // ReservaciÃ³n de sesiÃ³n
    private static Devolucion devolucion;
    // Usuario
    private static UsuarioBean usuario;
    //Guarda informacion de Usuario que  autoriza alguna acción R
    public static String numeroAutorizador;
    // Ventas aparcadas
    // Lista de ventas aparcadas
    // Nota: Desde que la impresiÃ³n hace uso del objeto ticket, este ahora contendrÃ¡ los siguientes
    // objetos que sin embargo referencian a los pertenecientes al ticket actual.
    private static Tienda tienda;
    private static Empresa empresa;
    private static Impuestos impuestos;
    public static CajaSesion cajaActual;
    public static Map<Long, Promocion> mapaPromociones;
    public static Map<Long, Integer> indicesPromociones;
    public static List<Promocion> promociones;
    public static List<PromocionTipoCupon> promocionesCupones;
    public static List<PromocionTipoPuntosCanjeo> promocionesCanjeoPuntos;
    public static List<PromocionTipoPuntosAcumula> promocionesAcumulacionPuntos;
    public static List<PromocionTipoNCuotasGratis> promocionesNCuotas;
    public static List<PromocionTipoDescuentoCombinado> promocionesPruebaricado;
    public static PromocionTipoDiaSocio promocionDiaSocio;
    public static PromocionTipoMesesGracia promocionMesesGracia;
    public static List<ReservaTiposBean> tiposReservaciones;
    public static List<MotivoDevolucion> tiposDevoluciones;
    public static List<Almacen> listaTiendas;
    public static Map<String, TipoAfiliadoBean> tiposAfiliados;
    public static PermisosEfectivosAccionBean permisos;
    // referencia al ultimo articulo introducido
    // entity manager factory
    private static EntityManagerFactory emf;
    // entity manager factory de la central
    private static EntityManagerFactory emfc;
    // entity manager factory de la Sms para envio de sms
    private static EntityManagerFactory emfsms;
    private static Apariencia apariencia;
    public static List<TicketS> ticketsAparcados;
    // logger
    private static final Logger log = Logger.getMLogger(Sesion.class);
    //Cliente generico
    public static Cliente clienteGenerico;
    private static String autorizadorDevolucion;
    private static String autorizadorReservacion;
    private static String autorizadorConsumoNC;
    private static UsuarioBean autorizador;
    private static JPrincipal ventanaPadre;
    public static ConfigBilleton configBilleton;
    //Usuario que autoriza las anulaciones
    private static UsuarioBean autorizadorAnulacion;

    private static Map<String, Long> tiemposEjecucion;
    // Caché de logos (para no tener que consultar al gestor de procesos cada vez)
    private static Map<String, Image> logosDisponibles;
    private static int promocionEnProceso = -1;
    private static boolean edicion = Boolean.FALSE;
    public static List<TipoDevolucionDTO> tipoDevolucionDTOs;
    public static DocumentoDTO documentoDTO;

    private static String autorizadorPrefactura;
    private static Long autorizaItemBase;
    private static String primeraFirmaNCredito = null;
    private static String primeraFirmaNUsuario = null;
    private static String reimpresionNC = null;
    private static boolean afiliadoPromo = false;
    private static PlasticoBean plasticoBean;
    private static LetraBean letraBean;

    public static void Sesion() {

        consultarDatosTienda();  // consulta los datos de la empresa y la tienda
        ticketsAparcados = new ArrayList<TicketS>();
        logosDisponibles = new HashMap<String, Image>();
    }

    public static String autenticaOperacion(Long idUsuario, Byte idOperacion) throws InvalidLoginException,
            LoginException,
            SinPermisosException {
        log.debug("autentica(): Operación con id: " + idOperacion + " para usuario con idUsuario ");
        Connection conn = new Connection();
        try {
            // Obtenemos una conexiÃ³n
            conn.abrirConexion(Database.getConnection());

            // Obtenemos los datos del usuario
            UsuarioBean usuario = getUsuario(conn, idUsuario);

            if (usuario == null) {
                throw new InvalidLoginException("Usuario invalido", "login.sinPermiso");
            }

            // obtenemos permisos efectivos del usuario
            PermisosEfectivosAccionBean permisos = ServicioPermisos.obtenerPermisosEfectivos(conn, Variables.getVariableAsLong(Variables.POS_CONFIG_ACCION), usuario.getIdUsuario());
            if (!permisos.isPuede(idOperacion)) {
                if (Operaciones.PERFIL_CAJERO.equals(idOperacion)) {
                    return null;
                } else {
                    throw new SinPermisosException("El usuario no tiene permisos para ejecutar esta operación.");
                }
            }

            return usuario.getUsuario();
        } catch (SQLException e) {
            log.error("autentica() - " + e.getMessage());
            String mensaje = "Error al autenticar usuario : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } catch (PermisoException e) {
            log.error("autentica() - " + e.getMessage());
            String mensaje = "Error al consultar permisos efectivos del usuario : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    public static void autenticaOperacion(String login, String password, Byte idOperacion) throws InvalidLoginException,
            LoginException,
            SinPermisosException,
            PrimeraFirmaException {
        log.debug("autentica(): Operación con id: " + idOperacion + " para usuario: " + login);
        Connection conn = new Connection();
        try {
            // Obtenemos una conexiÃ³n
            conn.abrirConexion(Database.getConnection());

            // Obtenemos los datos del usuario
            UsuarioBean usuario = getUsuario(conn, login);

            // Comprobamos la clave
            if (!usuario.isClaveCortaCorrecta(password) && !usuario.isClaveCorrecta(password)) {
                throw new InvalidLoginException("Usuario/clave no válidos", "login.sinPermiso");
            }

            // obtenemos permisos efectivos del usuario
            PermisosEfectivosAccionBean permisos = ServicioPermisos.obtenerPermisosEfectivos(conn, Variables.getVariableAsLong(Variables.POS_CONFIG_ACCION), usuario.getIdUsuario());
            if (!permisos.isPuede(idOperacion)) {
                throw new SinPermisosException("El usuario no tiene permisos para ejecutar esta operación.");
            }
            //Guarda la informacion de del usario autorizador R
            setNumeroAutorizador("" + usuario.getUsuario());

            if (Sesion.getPrimeraFirmaNCredito() == null) {
                if (idOperacion.equals(Operaciones.REIMPRIMIR)) {
                    Boolean perfilUsu = ServicioPermisos.obtenerPerfilUsuarioAdmin(conn, usuario.getIdUsuario());
                    if (perfilUsu) {
                        Sesion.setPrimeraFirmaNCredito(usuario.getUsuario());
                    }
                }
            } else {
                if (idOperacion.equals(Operaciones.REIMPRIMIR_NOTA_CREDITO_BONO)) {
                    if (Sesion.getPrimeraFirmaNCredito() != null) {
                        if (Sesion.getPrimeraFirmaNCredito().equals(usuario.getUsuario())) {
                            Sesion.setReimpresionNC("N");
                            throw new NullPointerException("Usuario ya tiene la primera Firma. ");
                        } else {
                            Sesion.setReimpresionNC("S");
                            setPrimeraFirmaNUsuario(Sesion.getPrimeraFirmaNCredito());
                        }
                    } else {
                        throw new NullPointerException("Usuario ya tiene la primera Firma. ");
                    }
                    Sesion.setPrimeraFirmaNCredito(null);
                }
            }

        } catch (NullPointerException e) {
            log.error("autentica() - " + e.getMessage());
            String mensaje = "El usuario ya tiene la primera Firma en la reimpresion : " + e.getMessage();
            throw new PrimeraFirmaException(mensaje, e);
        } catch (SQLException e) {
            log.error("autentica() - " + e.getMessage());
            String mensaje = "Error al autenticar usuario : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } catch (CriptoException e) {
            log.error("autentica() - " + e.getMessage());
            String mensaje = "Error al cifrar clave del usuario en MD5 : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } catch (PermisoException e) {
            log.error("autentica() - " + e.getMessage());
            String mensaje = "Error al consultar permisos efectivos del usuario : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }

    }

    public static void desbloqueaOperacion(String login, String password) throws InvalidLoginException,
            LoginException,
            SinPermisosException {
        log.debug("desbloquea(): Operación con usuario: " + login);
        Connection conn = new Connection();
        try {
            // Obtenemos una conexiÃ³n
            conn.abrirConexion(Database.getConnection());

            UsuarioBean usuarioconsultado = getUsuario(conn, login);

            // Obtenemos los datos del usuario
            if (login.equalsIgnoreCase(Sesion.getUsuario().getUsuario())) {
                // Comprobamos la clave
                if (!usuarioconsultado.isClaveCortaCorrecta(password) && !usuarioconsultado.isClaveCorrecta(password)) {
                    throw new InvalidLoginException("Usuario/clave no válidos", "login.sinPermiso");
                }
            } else {

                if (!usuarioconsultado.isClaveCortaCorrecta(password) && !usuarioconsultado.isClaveCorrecta(password)) {
                    throw new InvalidLoginException("Usuario/clave no válidos", "login.sinPermiso");
                } else {
                    // Vemos que sea administrador
                    PermisosEfectivosAccionBean permisos;
                    try {
                        permisos = ServicioPermisos.obtenerPermisosEfectivos(conn, Variables.getVariableAsLong(Variables.POS_CONFIG_ACCION), usuarioconsultado.getIdUsuario());

                        if (!permisos.isPuede(Operaciones.DESBLOQUEAR_POS)) {
                            throw new SinPermisosException("El usuario no tiene permisos para ejecutar esta operación.");
                        } else {
                            try {
                                ServicioLogAcceso.crearAccesoLogDesbloquearPos(usuarioconsultado.getUsuario());
                            } catch (Exception e) {
                                log.error("Error creando log de desbloqueo de pos > administrador" + usuarioconsultado.getUsuario());
                            }

                        }

                    } catch (PermisoException ex) {
                        log.info("El usuario no tiene permisos para realizar el desbloqueo de la operación", ex);
                        throw new SinPermisosException("El usuario no tiene permisos para ejecutar esta operación.");
                    }

                }

            }

        } catch (SQLException e) {
            log.error("autentica() - " + e.getMessage());
            String mensaje = "Error al autenticar usuario : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } catch (CriptoException e) {
            log.error("autentica() - " + e.getMessage());
            String mensaje = "Error al cifrar clave del usuario en MD5 : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }

    }

    public static void iniciaSesion(String login, String password) throws InvalidLoginException,
            LoginException,
            SinPermisosException {

        log.debug("iniciaSesion(): Iniciando sesión del usuario con usuario y password: " + login);

        Connection conn = new Connection();
        try {

            // Obtenemos una conexión
            conn.abrirConexion(Database.getConnection());

            // Obtenemos los datos del usuario
            usuario = getUsuario(conn, login);

            // Comprobamos la clave
            if (!usuario.isClaveCorrecta(password)) {
                throw new InvalidLoginException("Usuario/clave no válidos", "login.sinPermiso");
            }

            // consultamos caducidad de clave del usuario
            usuario.setCaducidad(PerfilesUsuariosDao.consultarCaducidadClave(conn, usuario.getIdUsuario()));

            // obtenemos permisos efectivos del usuario
            permisos = ServicioPermisos.obtenerPermisosEfectivos(conn, Variables.getVariableAsLong(Variables.POS_CONFIG_ACCION), usuario.getIdUsuario());

            ServicioLogSistema.registrarAcceso(conn, usuario);

            if (!permisos.isPuede(Operaciones.EJECUTAR)) {
                throw new SinPermisosException("El usuario no tiene permisos para ejecutar esta acción.");
            }

            inicializaSesion();

        } catch (SQLException e) {
            log.error("autentica() - " + e.getMessage(), e);
            String mensaje = "Error al autenticar usuario : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } catch (CriptoException e) {
            log.error("autentica() - " + e.getMessage(), e);
            String mensaje = "Error al cifrar clave del usuario en MD5 : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } catch (PermisoException e) {
            log.error("autentica() - " + e.getMessage(), e);
            String mensaje = "Error al consultar permisos efectivos del usuario : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } finally {
            try {
                conn.close();
                conn.cerrarConexion();
            } catch (Exception e) {
                log.error("iniciaSesion() - " + e);
            }
        }
    }

    public static void iniciaSesion(String login) throws InvalidLoginException,
            LoginException,
            SinPermisosException {

        log.debug("iniciaSesion(): Iniciando sesión del usuario con usuario y password: " + login);

        Connection conn = new Connection();
        try {

            // Obtenemos una conexión
            conn.abrirConexion(Database.getConnection());

            // Obtenemos los datos del usuario
            usuario = getUsuario(conn, login);

            // consultamos caducidad de clave del usuario
            usuario.setCaducidad(PerfilesUsuariosDao.consultarCaducidadClave(conn, usuario.getIdUsuario()));

            // obtenemos permisos efectivos del usuario
            permisos = ServicioPermisos.obtenerPermisosEfectivos(conn, Variables.getVariableAsLong(Variables.POS_CONFIG_ACCION), usuario.getIdUsuario());

            ServicioLogSistema.registrarAcceso(conn, usuario);

            if (!permisos.isPuede(Operaciones.EJECUTAR)) {
                throw new SinPermisosException("El usuario no tiene permisos para ejecutar esta acción.");
            }

            inicializaSesion();

        } catch (SQLException e) {
            log.error("autentica() - " + e.getMessage(), e);
            String mensaje = "Error al autenticar usuario : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } catch (PermisoException e) {
            log.error("autentica() - " + e.getMessage(), e);
            String mensaje = "Error al consultar permisos efectivos del usuario : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } finally {
            try {
                conn.close();
                conn.cerrarConexion();
            } catch (Exception e) {
                log.error("iniciaSesion() - " + e);
            }
        }
    }

    public static void iniciaSesion(Long idUsuario) throws InvalidLoginException,
            LoginException,
            SinPermisosException {

        log.debug("iniciaSesion(): Iniciando sesión del usuario a partir del idUsuario: " + idUsuario);

        Connection conn = new Connection();
        try {

            // Obtenemos una conexión
            conn.abrirConexion(Database.getConnection());

            // Obtenemos los datos del usuario
            usuario = getUsuario(conn, idUsuario);

            // Comprobamos la clave
            if (usuario == null) {
                throw new InvalidLoginException("Usuario invalido", "login.sinPermiso");
            }

            // consultamos caducidad de clave del usuario
            usuario.setCaducidad(PerfilesUsuariosDao.consultarCaducidadClave(conn, usuario.getIdUsuario()));

            // obtenemos permisos efectivos del usuario
            permisos = ServicioPermisos.obtenerPermisosEfectivos(conn, Variables.getVariableAsLong(Variables.POS_CONFIG_ACCION), usuario.getIdUsuario());

            ServicioLogSistema.registrarAcceso(conn, usuario);

            if (!permisos.isPuede(Operaciones.EJECUTAR)) {
                throw new SinPermisosException("El usuario no tiene permisos para ejecutar esta acción.");
            }

            inicializaSesion();

        } catch (SQLException e) {
            log.error("autentica() - " + e.getMessage());
            String mensaje = "Error al autenticar usuario : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } catch (PermisoException e) {
            log.error("autentica() - " + e.getMessage());
            String mensaje = "Error al consultar permisos efectivos del usuario : " + e.getMessage();
            throw new LoginException(mensaje, e);
        } finally {
            try {
                conn.close();
                conn.cerrarConexion();
            } catch (Exception e) {
                log.error("iniciaSesion() - " + e);
            }
        }
    }

    public static void cerrarSesion() {
        usuario = null;
        permisos = null;
        cajaActual = null;
        borrarTicket();
    }

    private static void inicializaSesion() {
        cajaActual = new CajaSesion();
        try {
            Sesion.cajaActual.consultaCajaAbierta(); // Inicializa la caja Actual
        } catch (Exception ex) {
            log.error("Excepcion al iniciar la sesión: " + ex.getMessage(), ex);
        }
    }

    private static UsuarioBean getUsuario(Connection conn, String login) throws SQLException,
            InvalidLoginException {

        // Obtenemos los datos del usuario
        UsuarioBean usuario = UsuariosDao.consultar(conn, login.toUpperCase());

        // Validamos al usuario
        if (usuario != null) {
            // Comprobamos si estÃ¡ activo
            if (!usuario.isActivo()) {
                throw new InvalidLoginException("No tiene permiso para acceder a la aplicación", "login.invalido");
            }
        } else {
            throw new InvalidLoginException("Usuario/clave no válidos", "login.sinPermiso");
        }
        return usuario;
    }

    private static UsuarioBean getUsuario(Connection conn, Long idUsuario) throws SQLException,
            InvalidLoginException {

        // Obtenemos los datos del usuario
        UsuarioBean usuario = UsuariosDao.consultar(conn, idUsuario);

        // Validamos al usuario
        if (usuario != null) {
            // Comprobamos si estÃ¡ activo
            if (!usuario.isActivo()) {
                throw new InvalidLoginException("No tiene permiso para acceder a la aplicación", "login.invalido");
            }
        } else {
            throw new InvalidLoginException("Usuario/clave no válidos", "login.sinPermiso");
        }
        return usuario;
    }

    /**
     * Metodo que consulta la tienda y empresa a la que pertenece el tpv
     */
    private static void consultarDatosTienda() {
        try {
            log.info("Consultando datos de la tienda: " + VariablesAlm.COD_ALMACEN);
            tienda = TiendasServices.consultaTienda(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
        } catch (Exception e) {
            log.fatal("No se puede iniciar sesión ya que no se han podido obtener los datos de la tienda configurada: " + e.getMessage(), e);
            throw new RuntimeException("Imposible iniciar sesión. No se pudieron obtener los datos de la tienda configurada.", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS Y SETTERS">
    public static EntityManagerFactory getEmf() {
        return emf;
    }

    public static void setEmf(EntityManagerFactory aEmf) {
        emf = aEmf;
    }

    public static EntityManagerFactory getEmfsms() {
        return emfsms;
    }

    public static void setEmfsms(EntityManagerFactory emfsms) {
        Sesion.emfsms = emfsms;
    }

    public static Tienda getTienda() {
        return tienda;
    }

    public static UsuarioBean getUsuario() {
        return usuario;
    }

    public static TicketS getTicket() {
        return ticket;
    }

    public static void setTicket(TicketS t) {
        ticket = t;
    }

    public static CajaSesion getCajaActual() {
        return cajaActual;
    }

    public static void setCajaActual(CajaSesion cajaActual) {
        Sesion.cajaActual = cajaActual;
    }
    //</editor-fold>

    /**
     * FunciÃ³n que crea e inicializa los datos de un ticket con los datos de
     * sesion
     */
    public static void iniciaNuevoTicket(Cliente cliente) throws ContadorException {
        ticket = creaTicket(cliente, true);

    }

    private static TicketS creaTicket(Cliente cliente, boolean aplicaPromocionesLineas) throws ContadorException {
        TicketS res = new TicketS(cajaActual.getCajaActual().getUidDiarioCaja(), cajaActual.getCajaParcialActual().getUidCajeroCaja(), aplicaPromocionesLineas);
        res.setCodcaja(Sesion.getDatosConfiguracion().getCodcaja());
        res.setTienda(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
        res.setCliente(cliente);
        res.setCajero(getUsuario());
        res.setId_ticket(ServicioContadoresCaja.obtenerContadorFactura());
        res.setFecha(new Fecha());
        return res;
    }

    /**
     * Función que crea e inicializa los datos de un ticket con los datos de
     * sesion
     */
    public static void iniciaNuevaReserva(Cliente cliente, ReservaTiposBean tipo, Reservacion reservacion) throws TicketException {

        if (reservacion != null) {
            setReservacion(reservacion);
        } else {
            setReservacion(new Reservacion());
        }
        try {
            getReservacion().iniciaNuevaReservacion(creaTicket(cliente, tipo.getPermiteReservarPromocionados()), tipo);
        } catch (ContadorException ex) {
            log.error("error en la consulta de contador a la reserva");
            throw new TicketException("Error al crear la reserva. No se pudo consultar el identificador de reserva");
        }
    }

    public static void iniciaNuevaReserva(Cliente cliente, ReservaTiposBean tipo, String nombreOrganizadora, String apellidosOrganizadora, String telefonoOrganizadora, String direccionEvento, Fecha fechaEvento) throws TicketException {
        setReservacion(new Reservacion());
        try {
            getReservacion().iniciaNuevaReservacion(creaTicket(cliente, tipo.getPermiteReservarPromocionados()), tipo, nombreOrganizadora, apellidosOrganizadora, telefonoOrganizadora, direccionEvento, fechaEvento);
        } catch (ContadorException ex) {
            log.error("error en la consulta de contador a la reserva");
            throw new TicketException("Error al crear la reserva. No se pudo consultar el identificador de reserva");
        }
    }

    /**
     * Funcion que crea e inicializa los datos de un ticket con los datos de
     * sesion
     *
     * @param codAlmacen
     * @param codCaja
     * @param idTicket
     * @param motivoDevolucion
     * @param estadoMercaderia
     * @param observaciones
     * @param tipoDevolucion
     * @param formasPago
     * @param realizaValidacion campo realizar validacion
     * @return
     * @throws com.comerzzia.jpos.gui.validation.ValidationFormException
     */
    public static String iniciaNuevaDevolucion(String codAlmacen, String codCaja, String idTicket, MotivoDevolucion motivoDevolucion, String estadoMercaderia, String observaciones, TipoDevolucionDTO tipoDevolucion, List<MedioPagoDTO> formasPago,
            boolean realizaValidacion) throws NoResultException, ValidationFormException, Exception {
        try {
            String usu = null;
            byte[] xmlTicket = TicketService.consultarXmlTicket(Long.parseLong(idTicket), codCaja, codAlmacen);
            TicketOrigen ticketOrigen = TicketOrigen.getTicketOrigen(new XMLDocument(xmlTicket));
            String codAdministrador = null;
            Calendar calFecha = new GregorianCalendar();
            Calendar calendarSys = Calendar.getInstance();
            calFecha.setTime(ticketOrigen.getFecha().getDate());
            int difHoras = (calendarSys.get(Calendar.HOUR_OF_DAY) - calFecha.get(Calendar.HOUR_OF_DAY));
            int difMinutos = (calendarSys.get(Calendar.MINUTE) - calFecha.get(Calendar.MINUTE));
            int minutos = (difHoras * 60) + difMinutos;
            try {
                if (Variables.getVariable(Variables.ACTIVO_VARIABLE_PERFIL_NOTA_CREDITO).equals("S")) {
                    usu = Sesion.autenticaOperacion(Sesion.getUsuario().getIdUsuario(), Operaciones.PERFIL_CAJERO);
                } else {
                    usu = null;
                }
            } catch (Exception e) {
                usu = null;
            }
            if (usu != null) {
                String variablesNCR = Variables.getVariable(Variables.VARIABLE_TIEMPO_GENERA_NOTA_CREDITO);
                if (minutos > 0 && minutos <= Integer.valueOf(variablesNCR)) {
                    if (ticketOrigen.isFechaFinDevolucionCaducada() && realizaValidacion) {
//                throw new ValidationFormException("Se ha superado la fecha máxima de devolución");
                        log.info("PERMISOS: Creando Pantalla de Autorización por fecha de devolución......");
                        codAdministrador = JPrincipal.getInstance().compruebaAutorizacion(Operaciones.AUTORIZA_FECHA_DEVOLUCION, "Se ha superado la fecha máxima de devolución.");
                    }
                    Cliente clienteDevolucion = ticketOrigen.getCliente();
                    Cliente clienteFacturaElect = ClientesServices.getInstance().consultaClienteIdenti(clienteDevolucion.getCodcli());
                    if (!clienteFacturaElect.isClienteGenerico() && (!Variables.getVariableAsBoolean(Variables.FACT_ELECTRONICA_SOLO_RUC) || clienteFacturaElect.isTipoRuc()) && VariablesAlm.getVariableAsBoolean(VariablesAlm.REALIZA_FACT_ELECTRONICA) && (clienteFacturaElect.getEmail() == null || clienteFacturaElect.getEmail().isEmpty())) {
                        return "<html>El cliente no tiene email para fact. electrónicamente</html>";
                    }
                    TicketS ticket = new TicketS(cajaActual.getCajaActual().getUidDiarioCaja(), cajaActual.getCajaParcialActual().getUidCajeroCaja(), false);
                    ticket.setCodcaja(Sesion.getDatosConfiguracion().getCodcaja());
                    ticket.setTienda(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
                    ticket.setCliente(clienteDevolucion);
                    ticket.setCajero(getUsuario());
                    ticket.setId_ticket(ServicioContadoresCaja.obtenerContadorNotaCredito());
                    ticket.setFecha(new Fecha());
                    devolucion = new Devolucion(ticketOrigen, ticket, motivoDevolucion, estadoMercaderia, observaciones);
                    devolucion.setAutorizador(realizaValidacion && codAdministrador != null ? codAdministrador : autorizadorDevolucion);
                    devolucion.setLocalOrigen(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
                    devolucion.setTipoDevolucion(tipoDevolucion == null ? 1l : tipoDevolucion.getIdTipoDevolucion());
                    if (formasPago != null) {
                        devolucion.getFormasPago().addAll(formasPago);
                    }
                    if (devolucion.getTicketDevolucion().getLineas().isEmpty()) {
                        return "<html>Se han devuelto todos los artículos del ticket original</html>";
                    }
                } else {
                    return "<html>El tiempo expiro. Realizar en Atención al cliente. </html>";
                }
            } else {
                if (ticketOrigen.isFechaFinDevolucionCaducada() && realizaValidacion) {
//                throw new ValidationFormException("Se ha superado la fecha máxima de devolución");
                    log.info("PERMISOS: Creando Pantalla de Autorización por fecha de devolución......");
                    codAdministrador = JPrincipal.getInstance().compruebaAutorizacion(Operaciones.AUTORIZA_FECHA_DEVOLUCION, "Se ha superado la fecha máxima de devolución.");
                }
                Cliente clienteDevolucion = ticketOrigen.getCliente();
                Cliente clienteFacturaElect = ClientesServices.getInstance().consultaClienteIdenti(clienteDevolucion.getCodcli());
                if (!clienteFacturaElect.isClienteGenerico() && (!Variables.getVariableAsBoolean(Variables.FACT_ELECTRONICA_SOLO_RUC) || clienteFacturaElect.isTipoRuc()) && VariablesAlm.getVariableAsBoolean(VariablesAlm.REALIZA_FACT_ELECTRONICA) && (clienteFacturaElect.getEmail() == null || clienteFacturaElect.getEmail().isEmpty())) {
                    return "<html>El cliente no tiene email para fact. electrónicamente</html>";
                }
                TicketS ticket = new TicketS(cajaActual.getCajaActual().getUidDiarioCaja(), cajaActual.getCajaParcialActual().getUidCajeroCaja(), false);
                ticket.setCodcaja(Sesion.getDatosConfiguracion().getCodcaja());
                ticket.setTienda(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
                ticket.setCliente(clienteDevolucion);
                ticket.setCajero(getUsuario());
                ticket.setId_ticket(ServicioContadoresCaja.obtenerContadorNotaCredito());
                ticket.setFecha(new Fecha());
                devolucion = new Devolucion(ticketOrigen, ticket, motivoDevolucion, estadoMercaderia, observaciones);
                devolucion.setAutorizador(realizaValidacion && codAdministrador != null ? codAdministrador : autorizadorDevolucion);
                devolucion.setLocalOrigen(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
                devolucion.setTipoDevolucion(tipoDevolucion == null ? 1l : tipoDevolucion.getIdTipoDevolucion());
                if (formasPago != null) {
                    devolucion.getFormasPago().addAll(formasPago);
                }
                if (devolucion.getTicketDevolucion().getLineas().isEmpty()) {
                    return "<html>Se han devuelto todos los artículos del ticket original</html>";
                }
            }
        } catch (ArticuloNotFoundException e) {
            log.error("No se han encontrado todos los artículos e la devolución en el sistema", e);
            throw e;
        } catch (NoResultException e) {
            log.error("No se encontró el registro", e);
            throw e;
        } catch (XMLDocumentException ex) {
            log.error("Error de formato XML", ex);
            throw ex;
        } catch (TicketException e) {
            log.error("Error en la construcción del ticket", e);
            throw e;
        } catch (NotaCreditoException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            throw e;
        }
        return null;
    }

    public static TicketS getTicketReservacion() {
        return getReservacion().getTicket();
    }

    public static TicketS getTicketDevolucion() {
        return devolucion.getTicketDevolucion();
    }

    /**
     * FunciÃ³n que crea e inicializa los datos de un ticket con los datos de
     * sesion
     */
    public static void recuperaTicket(TicketS ticketRecuperado) throws ContadorException {
        ticket = ticketRecuperado;
        ticketsAparcados.remove(ticketRecuperado);
        ticket.setCodcaja(Sesion.getDatosConfiguracion().getCodcaja());
        ticket.setId_ticket(ServicioContadoresCaja.obtenerContadorFactura());

    }

    public static void borrarTicket() {
        ticket = null;
        PromocionTipoDtoManualTotal.desActivar();
    }

    public static void borrarTicketReservacion() {
        Sesion.reservacion.setTicket(null);
    }

    public static DatosConfiguracion getDatosConfiguracion() {
        return config;
    }

    public static void setDatosConfiguracion(DatosConfiguracion aDatosConfiguracion) {
        config = aDatosConfiguracion;
    }

    /**
     * Getter y setter de forma estatica
     *
     * @return
     */
    public static List<Vendedor> getListaVendedoresStatic() {
        return listaVendedores;
    }

    public static void setListaVendedoresStatic(List<Vendedor> aListaVendedores) {
        listaVendedores = aListaVendedores;
    }

    public static boolean existenTicketsAparcados() {
        return !ticketsAparcados.isEmpty();
    }

    public static void aparcarTicket() {
        ticket.setUsuarioAplazado(getUsuario());
        ticketsAparcados.add(ticket);
        borrarTicket();
    }

    public static Apariencia getApariencia() {
        return Sesion.apariencia;
    }

    public static Reservacion getReservacion() {
        return reservacion;
    }

    public static void setReservacion(Reservacion aReservacion) {
        reservacion = aReservacion;
    }

    public static Devolucion getDevolucion() {
        return Sesion.devolucion;
    }

    public static List<ReservaTiposBean> getTiposReservacionesFiltroSeleccion() {
        List<ReservaTiposBean> res = new LinkedList();
        if (tiposReservaciones != null) {
            for (ReservaTiposBean r : tiposReservaciones) {
                if (r.getPermiteAltaPos()) {
                    res.add(r);
                }
            }
        }
        return res;
    }

    public static void setAutorizadorDevolucion(String autorizador) {
        autorizadorDevolucion = autorizador;
    }

    public static String getAutorizadorDevolucion() {
        return autorizadorDevolucion;
    }

    public static String getAutorizadorPrefactura() {
        return autorizadorPrefactura;
    }

    public static void setAutorizadorPrefactura(String autorizadorPrefactura) {
        Sesion.autorizadorPrefactura = autorizadorPrefactura;
    }

    public static JPrincipal getVentanaPadre() {
        return ventanaPadre;
    }

    public static void setVentanaPadre(JPrincipal aVentanaPadre) {
        ventanaPadre = aVentanaPadre;
    }

    public static void setAutorizadorGestionCajas(String autorizador) {
        if (autorizador != null && !autorizador.isEmpty()) {
            try {
                Sesion.autorizador = ServicioUsuarios.consultar(autorizador.toUpperCase());
            } catch (Exception ex) {
                log.error("setAutorizadorVenta() - Excepción consultando datos del usuario : " + autorizador);
            }
        }
    }

    public static String getUsuAutorizadorGestionCajas() {
        return Sesion.autorizador.getUsuario();
    }

    public static String getDesAutorizadorGestionCajas() {
        return Sesion.autorizador.getDesUsuario();
    }

    public static EntityManagerFactory getEmfc() {
        return emfc;
    }

    public static void setEmfc(EntityManagerFactory aEmfc) {
        emfc = aEmfc;
    }

    public static boolean isValidacionManualSoloComoAdministrador() {
        return Variables.getVariableAsBoolean(Variables.SWITCH_TARJETAS_MANUAL_AUTORIZACION);
    }

    public static Empresa getEmpresa() {
        return empresa;
    }

    public static void setEmpresa(Empresa aEmpresa) {
        empresa = aEmpresa;
    }

    public static Map<String, Image> getLogos() {
        return logosDisponibles;
    }

    static void vaciarCacheImagenes() {
        logosDisponibles = new HashMap<String, Image>();
    }

    public static void addPromocion(Promocion promocion) {
        mapaPromociones.put(promocion.getIdPromocion(), promocion);
    }

    public static Promocion getPromocion(Long idPromocion) {
        return mapaPromociones.get(idPromocion);
    }

    public static Cliente getClienteGenericoReset() {
        clienteGenerico.setAplicaTarjetaAfiliado(null);
        clienteGenerico.setTarjetaAfiliacion(null);
        return getClienteGenerico();
    }

    public static void addIndicePromocion(Promocion promocion) {
        indicesPromociones.put(promocion.getIdPromocion(), 1);
    }

    public static Integer getIndicePromocion(Long idPromocion) {
        return indicesPromociones.get(idPromocion);
    }

    public static void actualizaIndice(Long idPromocion, Integer valor) {
        indicesPromociones.put(idPromocion, valor);
    }

    /**
     * Getter de forma no estatica, necesario para realizar el binding al objeto
     * swing
     *
     * @return
     */
    public List<Vendedor> getListaVendedores() {
        return listaVendedores;
    }

    public List<Almacen> getListaTiendas() {
        return listaTiendas;
    }

    public void setListaTiendas(List<Almacen> aListaTiendas) {
        listaTiendas = aListaTiendas;
    }

    public List<MotivoDevolucion> getListaMotivosDevolucion() {
        return tiposDevoluciones;
    }

    public List<TipoDevolucionDTO> getListaTipoDevolucionDTOs() {
        return tipoDevolucionDTOs;
    }

    public static DocumentoDTO getDocumentoDTO() {
        return documentoDTO;
    }

    public static void setDocumentoDTO(DocumentoDTO documentoDTO) {
        Sesion.documentoDTO = documentoDTO;
    }

    public static List<TicketS> getTicketsAparcados() {
        return ticketsAparcados;
    }

    public static Cliente getClienteGenerico() {
        return clienteGenerico;
    }

    public static void setClienteGenerico(Cliente clienteGenerico) {
        Sesion.clienteGenerico = clienteGenerico;
    }

    public static boolean isSukasa() {
        return Variables.getVariable(Variables.CONFIGURACION_EMPRESA).equals(Variables.CONFIGURACION_EMPRESA_ES_SUKASA);
    }

    public static boolean isBebemundo() {
        return Variables.getVariable(Variables.CONFIGURACION_EMPRESA).equals(Variables.CONFIGURACION_EMPRESA_ES_BEBEMUNDO);
    }

    public static void setAutorizadorAnulacion(String autorizador) {
        if (autorizador != null && !autorizador.isEmpty()) {
            try {
                autorizadorAnulacion = ServicioUsuarios.consultar(autorizador.toUpperCase());
            } catch (Exception ex) {
                log.error("setAutorizadorAnulacion() - Excepción consultando datos del usuario : " + autorizador);
            }
        }
    }

    public static UsuarioBean getAutorizadorAnulacion() {
        return autorizadorAnulacion;
    }

    public static void putTiempoEjecucion(String key, Long time) {
        if (tiemposEjecucion == null) {
            tiemposEjecucion = new HashMap<String, Long>();
        }
        tiemposEjecucion.put(key, time);
    }

    public static Map<String, Long> getTiemposEjecucion() {
        return tiemposEjecucion;
    }

    public static void setTiemposEjecucion(Map<String, Long> tiemposEjecucion) {
        Sesion.tiemposEjecucion = tiemposEjecucion;
    }

    public static double getDameTiempo(String keyInicio, String keyFin) {
        try {
            long diferencia = tiemposEjecucion.get(keyFin) - tiemposEjecucion.get(keyInicio);
            return diferencia / 1000.00;
        } catch (NullPointerException e) {
            log.error("getDameTiempo() - No se calculará el tiempo porque " + keyInicio + " es nulo o " + keyFin + " es nulo");
            return 0d;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="GestiÃ³n de Cajas">
    public static void aperturaDeCaja(BigDecimal saldoapertura) throws Exception {
        try {
            cajaActual.aperturaDeCaja(saldoapertura);

        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void cierreDeCaja() {
        try {
            cajaActual.cierreDeCaja();
        } catch (Exception ex) {
        }
    }

    //</editor-fold>  
    //Id autorizador
    public static String getNumeroAutorizador() {
        return numeroAutorizador;
    }

    public static void setNumeroAutorizador(String numeroAutorizador) {
        Sesion.numeroAutorizador = numeroAutorizador;
    }

    public static List<PromocionTipoDescuentoCombinado> getPromocionesPruebaricado() {
        return promocionesPruebaricado;
    }

    public static void setPromocionesPruebaricado(List<PromocionTipoDescuentoCombinado> promocionesPruebaricado) {
        Sesion.promocionesPruebaricado = promocionesPruebaricado;
    }

    public static int getPromocionEnProceso() {
        return promocionEnProceso;
    }

    public static void setPromocionEnProceso(int promocionEnProceso) {
        Sesion.promocionEnProceso = promocionEnProceso;
    }

    public static boolean isEdicion() {
        return edicion;
    }

    public static void setEdicion(boolean edicion) {
        Sesion.edicion = edicion;
    }

    /**
     * @author Gabriel Simbania
     * @description Metodo para generar las Devoluciones desde otro local
     * @param codAlmacen
     * @param codCaja
     * @param idTicket
     * @param motivoDevolucion
     * @param estadoMercaderia
     * @param observaciones
     * @return
     * @throws NoResultException
     * @throws ValidationFormException
     * @throws Exception
     */
    public static Devolucion iniciaNuevaDevolucionPorOtroLocal(String codAlmacen, String codCaja, String idTicket, MotivoDevolucion motivoDevolucion, String estadoMercaderia, String observaciones, Long tipoDevolucion, String localOrigen) throws NoResultException, ValidationFormException, Exception {
        try {
            byte[] xmlTicket = TicketService.consultarXmlTicket(Long.parseLong(idTicket), codCaja, codAlmacen);
            TicketOrigen ticketOrigen = TicketOrigen.getTicketOrigen(new XMLDocument(xmlTicket));

            /*Se comenta, el sistema permite realizar notas de facturas ya caducadas */
//            if (ticketOrigen.isFechaFinDevolucionCaducada()) {
//                throw new ValidationFormException("Se ha superado la fecha máxima de devolución");
//            }
            Cliente clienteDevolucion = ticketOrigen.getCliente();
            Cliente clienteFacturaElect = ClientesServices.getInstance().consultaClienteIdenti(clienteDevolucion.getCodcli());
            if (!clienteFacturaElect.isClienteGenerico() && (!Variables.getVariableAsBoolean(Variables.FACT_ELECTRONICA_SOLO_RUC) || clienteFacturaElect.isTipoRuc()) && VariablesAlm.getVariableAsBoolean(VariablesAlm.REALIZA_FACT_ELECTRONICA) && (clienteFacturaElect.getEmail() == null || clienteFacturaElect.getEmail().isEmpty())) {
                throw new ValidationFormException("El cliente no tiene email para fact. electr\u00F3nicamente");
            }
            TicketS ticket = new TicketS(cajaActual.getCajaActual().getUidDiarioCaja(), cajaActual.getCajaParcialActual().getUidCajeroCaja(), false);
            ticket.setCodcaja(Sesion.getDatosConfiguracion().getCodcaja());
            ticket.setTienda(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            ticket.setCliente(clienteDevolucion);
            ticket.setCajero(getUsuario());
            ticket.setId_ticket(ServicioContadoresCaja.obtenerContadorNotaCredito());
            ticket.setFecha(new Fecha());
            Devolucion devolucionPorOtroLocal = new Devolucion(ticketOrigen, ticket, motivoDevolucion, estadoMercaderia, observaciones, tipoDevolucion, localOrigen);
            devolucionPorOtroLocal.setAutorizador(autorizadorDevolucion);
            if (devolucionPorOtroLocal.getTicketDevolucion().getLineas().isEmpty()) {
                throw new ValidationFormException("Se han devuelto todos los artículos del ticket original");
            }

            return devolucionPorOtroLocal;
        } catch (ArticuloNotFoundException e) {
            log.error("No se han encontrado todos los artículos e la devolución en el sistema", e);
            throw e;
        } catch (NoResultException e) {
            log.error("No se encontró el registro", e);
            throw e;
        } catch (XMLDocumentException ex) {
            log.error("Error de formato XML", ex);
            throw ex;
        } catch (TicketException e) {
            log.error("Error en la construcción del ticket", e);
            throw e;
        } catch (NotaCreditoException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public static Impuestos getImpuestos() {
        return impuestos;
    }

    public static void setImpuestos(Impuestos impuestos) {
        Sesion.impuestos = impuestos;
    }

    public static String getAutorizadorReservacion() {
        return autorizadorReservacion;
    }

    public static void setAutorizadorReservacion(String autorizadorReservacion) {
        Sesion.autorizadorReservacion = autorizadorReservacion;
    }

    public static String getAutorizadorConsumoNC() {
        return autorizadorConsumoNC;
    }

    public static void setAutorizadorConsumoNC(String autorizadorConsumoNC) {
        Sesion.autorizadorConsumoNC = autorizadorConsumoNC;
    }

    public static Long getAutorizaItemBase() {
        return autorizaItemBase;
    }

    public static void setAutorizaItemBase(Long autorizaItemBase) {
        Sesion.autorizaItemBase = autorizaItemBase;
    }

    public static String getPrimeraFirmaNCredito() {
        return primeraFirmaNCredito;
    }

    public static void setPrimeraFirmaNCredito(String primeraFirmaNCredito) {
        Sesion.primeraFirmaNCredito = primeraFirmaNCredito;
    }

    public static String getReimpresionNC() {
        return reimpresionNC;
    }

    public static void setReimpresionNC(String reimpresionNC) {
        Sesion.reimpresionNC = reimpresionNC;
    }

    public static String getPrimeraFirmaNUsuario() {
        return primeraFirmaNUsuario;
    }

    public static void setPrimeraFirmaNUsuario(String primeraFirmaNUsuario) {
        Sesion.primeraFirmaNUsuario = primeraFirmaNUsuario;
    }

    public static boolean isAfiliadoPromo() {
        return afiliadoPromo;
    }

    public static void setAfiliadoPromo(boolean afiliadoPromo) {
        Sesion.afiliadoPromo = afiliadoPromo;
    }

    public static PlasticoBean getPlasticoBean() {
        return plasticoBean;
    }

    public static void setPlasticoBean(PlasticoBean plasticoBean) {
        Sesion.plasticoBean = plasticoBean;
    }

    public static LetraBean getLetraBean() {
        return letraBean;
    }

    public static void setLetraBean(LetraBean letraBean) {
        Sesion.letraBean = letraBean;
    }

}
