/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.login;

import com.comerzzia.jpos.dto.TipoDevolucionDTO;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.Empresa;
import com.comerzzia.jpos.entity.db.Tienda;
import com.comerzzia.jpos.servicios.general.ciudades.Ciudades;
import com.comerzzia.jpos.servicios.general.operadoresmoviles.OperadoresMoviles;
import com.comerzzia.jpos.persistencia.general.ciudades.CiudadesDao;
import com.comerzzia.jpos.persistencia.general.operadoresmoviles.OperadoresMovilesDao;
import com.comerzzia.jpos.persistencia.bonos.BonosDao;
import com.comerzzia.jpos.servicios.clientes.ClientesServices;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasServices;
import com.comerzzia.jpos.servicios.general.vendedores.VendedoresServices;

import com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.ConfiguracionBilletonBean;
import com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.ConfiguracionBilletonExample;
import com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.ConfiguracionBilletonMapper;
import com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.detalles.ConfiguracionBilletonDetalleExample;
import com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.detalles.ConfiguracionBilletonDetalleMapper;
import com.comerzzia.jpos.servicios.bonos.BonosServices;
import com.comerzzia.jpos.servicios.devoluciones.DevolucionException;
import com.comerzzia.jpos.servicios.devoluciones.DevolucionesServices;
import com.comerzzia.jpos.servicios.core.empresa.EmpresaException;
import com.comerzzia.jpos.servicios.core.empresa.EmpresaServices;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.clientes.afiliados.AfiliadosException;
import com.comerzzia.jpos.servicios.clientes.afiliados.ServicioAfiliados;
import com.comerzzia.jpos.servicios.clientes.tiposClientes.TiposClientes;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.ServicioPromociones;
import com.comerzzia.jpos.servicios.promociones.articulos.ArticulosEnPromocion;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCupon;
import com.comerzzia.jpos.servicios.promociones.totales.TotalesEnPromocion;
import com.comerzzia.jpos.servicios.reservaciones.ReservasException;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.servicios.promociones.configuracion.billeton.ConfigBilleton;
import com.comerzzia.jpos.servicios.promociones.cuotas.PromocionTipoNCuotasGratis;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosAcumula;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosCanjeo;
import com.comerzzia.jpos.servicios.reservaciones.ReservacionesServicios;
import com.comerzzia.jpos.util.EnumTipoDevolucion;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import es.mpsistemas.util.log.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Administrador
 */
public class LecturaConfiguracion {

    // logger
    protected static Logger log = Logger.getMLogger(LecturaConfiguracion.class);

    public static void leerDatosSesion(boolean inicio) throws Exception {
        log.info("Cargando/Recargando datos de sesión...");
        
        // Vaciamos la caché de imagenes de cupones
        Sesion.vaciarCacheImagenes();
        
        // Carga los medios de pago en la aplicación
        leerMediosDePago();

        // Carga lista de vendedores
        leerVendedores();

        // Carga lista de ciudades
        leerCiudades();

        // Carga los Operadores Móviles
        leerOperadoresMoviles();

        // Carga promociones vigentes
        leerPromociones();

        // Cargar datos cliente generico
        leerClienteGenerico();

        // Cargar tipos de afiliados
        leerTiposAfiliados();

        // Cargar tipos de afiliados
        leerTiposReservaciones();

        // Cargar Motivos de devolución
        leerTiposDevoluciones();

        // Cargar el listado de tiendas
        leerListadoTiendas();

        // Cargar el Tipo de Bono Efectivo
        leerTipoBonoEfectivo();

        // Las variables en el inicio no es necesario cargarlas. Se cargan automáticamente
        // cuando se solicita alguna
        if (!inicio) {
            VariablesAlm.obtieneVariables();
            Variables.obtieneVariables();
            Tienda tienda = TiendasServices.consultaTienda(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            Sesion.getTienda().setAceptarCedulasDesc(tienda.getAceptarCedulasDesc());
            Sesion.getTienda().setAceptarTarjAfiliacionDesc(tienda.getAceptarTarjAfiliacionDesc());

        }

        // Carga los datos de la Empresa
        leerEmpresa();
        
        // Carga de los tipos de clientes
        leerTiposClientes();

        log.info("Datos de sesión cargados correctamente.");
    }

    /** 
     * Lee los medios de pago y los pone en sesion
     */
    private static void leerMediosDePago() throws Exception {
        try {
            log.debug("Cargando medios de pago...");
            MediosPago.cargarMediosPago();
            String error = null;
            if (MediosPago.getInstancia().getPagoEfectivo() == null){
                error = "EFECTIVO";
            }
            else if (MediosPago.getInstancia().getPagoNotaCredito() == null){
                error = "NOTA DE CRÉDITO";
            }
            else if (MediosPago.getInstancia().getPagoBono() == null){
                error = "BONO";
            }
            else if (MediosPago.getInstancia().getPagoRetencion() == null){
                error = "RETENCIÓN";
            }
            if (error != null){
                log.error("ERROR EN CONFIGURACIÓN: NO SE HA CARGADO EL MEDIO DE PAGO " + error);
                log.error("Revisar sincronización de Medios de Pago para esta tienda. Revisar código de medio de pago " + error + " configurado en Variable Central.");
                throw new MedioPagoException("ERROR EN CONFIGURACIÓN: NO SE HA CARGADO EL MEDIO DE PAGO " + error);
            }
            log.debug("Medios de pago cargados correctamente.");
        }
        catch (Exception e) {
            log.error("Error leyendo medios de pago");
            log.error(e.getMessage(), e);
            throw (e);
        }
    }

    /**
     * Lee la lista de vendedores de la base de datos
     */
    private static void leerVendedores() throws Exception {
        try {
            VendedoresServices vs = new VendedoresServices();
            Sesion.setListaVendedoresStatic(vs.consultarVendedores(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN)));
        }
        catch (Exception e) {
            log.error("Error leyendo medios de pago");
            log.error(e.getMessage(), e);
            throw (e);
        }
    }

    private static void leerCiudades() throws Exception {
        try {
            CiudadesDao cd = new CiudadesDao();
            Ciudades ciudades = new Ciudades(cd.consultarCiudades(), null);
            Sesion.getDatosConfiguracion().setCiudades(ciudades);
        }
        catch (Exception e) {
            log.error("Error leyendo medios de pago");
            log.error(e.getMessage(), e);
            throw (e);
        }
    }

    private static void leerOperadoresMoviles() throws Exception {
        try {
            OperadoresMovilesDao omd = new OperadoresMovilesDao();
            OperadoresMoviles operadores = new OperadoresMoviles(omd.consultarOperadoresMoviles(), null);
            Sesion.getDatosConfiguracion().setOperadoresMoviles(operadores);
        }
        catch (Exception e) {
            log.error("Error leyendo medios de pago");
            log.error(e.getMessage(), e);
            throw (e);
        }
    }

    public static void leerPromociones() throws PromocionException {
        ArticulosEnPromocion.getNewInstance();
        TotalesEnPromocion.getNewInstance();
        Sesion.promociones = new ArrayList<Promocion>();
        Sesion.promocionesCupones = new ArrayList<PromocionTipoCupon>();
        Sesion.promocionesAcumulacionPuntos = new ArrayList<PromocionTipoPuntosAcumula>();
        Sesion.promocionesCanjeoPuntos = new ArrayList<PromocionTipoPuntosCanjeo>();
        Sesion.promocionesNCuotas = new ArrayList<PromocionTipoNCuotasGratis>();
        Sesion.mapaPromociones = new HashMap<Long, Promocion>();
        Sesion.indicesPromociones = new HashMap<Long, Integer>();
        Sesion.promocionDiaSocio = null;
        Sesion.promocionMesesGracia = null;
        ServicioPromociones.consultar();
        Collections.sort(Sesion.promociones);
        Collections.sort(Sesion.promocionesAcumulacionPuntos);
        Collections.sort(Sesion.promocionesCanjeoPuntos);
        Collections.sort(Sesion.promocionesNCuotas);
        log.debug("Promociones cargardas correctamente.");

        for (PromocionTipoCupon promoCupones : Sesion.promocionesCupones) {
            if (promoCupones.getTipoPromocion().isPromocionTipoBilleton()) {
                leerConfiguracionBilleton();
                break;
            }
        }
    }

    private static void leerConfiguracionBilleton() {
        SqlSession sqlSession =  SessionFactory.openSession();
        
        ConfiguracionBilletonMapper cabMapper = sqlSession.getMapper(ConfiguracionBilletonMapper.class);
        ConfiguracionBilletonDetalleMapper detMapper = sqlSession.getMapper(ConfiguracionBilletonDetalleMapper.class);
        
        ConfiguracionBilletonExample cabEx = new ConfiguracionBilletonExample();
        ConfiguracionBilletonDetalleExample detEx = new ConfiguracionBilletonDetalleExample();

        cabEx.or().andVigenteEqualTo(true);
        List<ConfiguracionBilletonBean> configuraciones = cabMapper.selectByExample(cabEx);
        if(configuraciones != null){
            log.debug("leerConfiguracionBilleton() - Se han encontrado "+configuraciones.size()+" configuraciones de billetón.");
        } else {
            log.debug("leerConfiguracionBilleton() - Configuración del billetón es null");
        }
        for (ConfiguracionBilletonBean configuracion : configuraciones) {
            detEx.clear();
            detEx.or().andIdConfBilletonEqualTo(configuracion.getIdConfBilleton()).andCodFormatoIsNull();
            detEx.or().andIdConfBilletonEqualTo(configuracion.getIdConfBilleton()).andCodFormatoEqualTo(Sesion.getTienda().getCodFormato());
            configuracion.setLstDetalle(detMapper.selectByExample(detEx));
        }
        
        Sesion.configBilleton = new ConfigBilleton(configuraciones);
    }

    private static void leerTiposAfiliados() throws AfiliadosException {
        try {
            Sesion.tiposAfiliados = ServicioAfiliados.consultarTiposAfiliados();
            log.debug("Tipo de afiliados cargardos correctamente.");
        }
        catch (AfiliadosException ex) {
            log.error("Error cargando tipos de afiliados disponibles: " + ex.getMessage(), ex);
            throw (ex);
        }
    }

    private static void leerTiposReservaciones() throws ReservasException {
        try {
            Sesion.tiposReservaciones = ReservacionesServicios.consultarTipos();
            log.debug("Tipo de reservaciones cargardos correctamente.");
        }
        catch (ReservasException ex) {
            log.error("Error cargando tipos de reservaciones disponibles: " + ex.getMessage(), ex);
            throw (ex);
        }
    }

    private static void leerClienteGenerico() throws ClienteException {
        try {
            Cliente clienteGenerico = new ClientesServices().consultaClienteNAfil(Variables.getVariable(Variables.POS_CONFIG_ID_CLIENTE_GENERICO));
            clienteGenerico.setTipoCliente(Variables.getVariableAsLong(Variables.TIPO_CLIENTE_DEFECTO));
            Sesion.setClienteGenerico(clienteGenerico);
        }
        catch (ClienteException ex) {
            log.error("Error leyendo cliente generico", ex);
            throw (ex);
        }
    }

    private static void leerTiposDevoluciones() throws DevolucionException {
        try {
            Sesion.tiposDevoluciones = DevolucionesServices.consultaMotivosDevolucion();
            Sesion.tipoDevolucionDTOs = new ArrayList<TipoDevolucionDTO>();
            for (EnumTipoDevolucion tipo : EnumTipoDevolucion.values()) {
                if (tipo.getValor().equals(EnumTipoDevolucion.TIPO_CONSUMIR_NC.getValor()) || tipo.getValor().equals(EnumTipoDevolucion.TIPO_DEVOLUCION_DINERO.getValor())) {
                    Sesion.tipoDevolucionDTOs.add(new TipoDevolucionDTO(tipo.getValor(), tipo.getObservacion()));
                }
            }
            
            log.debug("Tipo de devolucionescargardos leídos correctamente.");
        }
        catch (DevolucionException ex) {
            log.error("Error cargando tipos de devoluciones disponibles: " + ex.getMessage(), ex);
            throw (ex);
        }
    }

    private static void leerListadoTiendas() throws Exception {
        try {
            Sesion.listaTiendas = TiendasServices.consultarListaTiendas();
            log.debug("Listado de tiendas consultado correctamente.");
        }
        catch (Exception ex) {
            log.error("Error cargando Listado de tiendas: " + ex.getMessage(), ex);
            throw (ex);
        }
    }

    public static void leerTipoBonoEfectivo() throws Exception {
        BonosServices.setTipoBonoImporte(BonosDao.consultaBonoImporte(Variables.getVariableAsLong(Variables.POS_CONFIG_ID_BONO_EFECTIVO)));
    }

    private static void leerEmpresa() throws EmpresaException {
        Empresa emp = EmpresaServices.consultarDatosEmpresa();
        Sesion.setEmpresa(emp);
    }
    
    
    /** 
     * Carga los Tipos de Clientes y se inician en Sesion
     */
    private static void leerTiposClientes() throws Exception {
        try {
            log.debug("Cargando tipos de clientes...");
            TiposClientes.cargarTiposClientes();
            String error = null;
            if (TiposClientes.getInstancia().getTiposClientes() == null){
                log.error("ERROR EN CONFIGURACIÓN: NO SE HAN CARGADO LOS TIPOS DE CLIENTES " + error);
                log.error("Revisar sincronización de Tipos de Clientes para esta tienda.");
                throw new MedioPagoException("ERROR EN CONFIGURACIÓN: NO SE HA CARGADO LOS TIPOS DE CLIENTES " + error);
            }
            log.debug("Tipos de clientes cargados correctamente.");
        }
        catch (Exception e) {
            log.error("Error leyendo tipos de clientes");
            log.error(e.getMessage(), e);
            throw (e);
        }
    }
}
