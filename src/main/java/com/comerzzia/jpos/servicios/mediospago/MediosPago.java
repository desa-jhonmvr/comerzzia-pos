/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.mediospago;

import com.comerzzia.jpos.persistencia.mediospagos.BancoBean;
import com.comerzzia.jpos.persistencia.mediospagos.DescuentoBean;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.mediospagos.MediosPagoDao;
import com.comerzzia.jpos.persistencia.mediospagos.VencimientoBean;
import com.comerzzia.jpos.persistencia.clientes.tiposclientes.TipoClienteBean;
import com.comerzzia.jpos.persistencia.mediospagos.MarcaTarjetaMedioPagoBean;
import com.comerzzia.jpos.servicios.marcatarjeta.MarcaTarjetaServices;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author amos
 */
public class MediosPago {

    protected static Logger log = Logger.getMLogger(MediosPago.class);
    private static MediosPago instancia;

    // Medios de pago especiales
    private MedioPagoBean pagoEfectivo;
    private MedioPagoBean pagoRetencion;
    private MedioPagoBean pagoNotaCredito;
    private MedioPagoBean pagoBono;

    // Mapas
    private Map<String, MedioPagoBean> binesMedioPagos; // Map<Bin, MedioPago>
    private Map<String, MedioPagoBean> mediosPago; // Map<CodMedioPago, MedioPago>
    private Map<Long, MedioPagoBean> mediosPagoVencimientos; // Map<IdVencimiento, MedioPago>
    private Map<Long, VencimientoBean> vencimientos; // Map<IdVencimiento, Vencimiento>
    private Map<Byte, List<MedioPagoBean>> mediosPagoTipos; // Map<TipoPago, List<MedioPago>>
    private Map<Long, Map<Byte, List<MedioPagoBean>>> mediosPagoCliente; // Map<TipoCliente, Map<TipoPago, List<MedioPago>>
    private Map<Long, Map<Long, DescuentoBean>> descuentosCliente; // Map<TipoCliente, Map<Vencimiento, Descuento>>
    // Listas
    private List<MedioPagoBean> mediosPagoLista;
    private List<Long> vencimientosInactivos;

    private MediosPago() {
        mediosPago = new HashMap<String, MedioPagoBean>();
        mediosPagoCliente = new HashMap<Long, Map<Byte, List<MedioPagoBean>>>();
        descuentosCliente = new HashMap<Long, Map<Long, DescuentoBean>>();
        mediosPagoTipos = getMapaMediosPagoTipoVacio();
        binesMedioPagos = new HashMap<String, MedioPagoBean>();
        vencimientosInactivos = new ArrayList<Long>();
        mediosPagoVencimientos = new HashMap<Long, MedioPagoBean>();
        vencimientos = new HashMap<Long, VencimientoBean>();
    }

    public MediosPago(HashMap<String, MedioPagoBean> mediosPago) {
        this.mediosPago = mediosPago;
        mediosPagoCliente = new HashMap<Long, Map<Byte, List<MedioPagoBean>>>();
        descuentosCliente = new HashMap<Long, Map<Long, DescuentoBean>>();
        mediosPagoTipos = getMapaMediosPagoTipoVacio();
        binesMedioPagos = new HashMap<String, MedioPagoBean>();
        vencimientosInactivos = new ArrayList<Long>();
        mediosPagoVencimientos = new HashMap<Long, MedioPagoBean>();
        vencimientos = new HashMap<Long, VencimientoBean>();
    }

    private Map<Byte, List<MedioPagoBean>> getMapaMediosPagoTipoVacio() {
        Map<Byte, List<MedioPagoBean>> mapaMediosPagoTipos = new HashMap<Byte, List<MedioPagoBean>>();
        mapaMediosPagoTipos.put(MedioPagoBean.TIPO_CONTADO, new ArrayList<MedioPagoBean>());
        mapaMediosPagoTipos.put(MedioPagoBean.TIPO_OTROS, new ArrayList<MedioPagoBean>());
        mapaMediosPagoTipos.put(MedioPagoBean.TIPO_TARJETAS, new ArrayList<MedioPagoBean>());
        return mapaMediosPagoTipos;
    }

    public MedioPagoBean getMedioPago(String codMedioPago) {
        return mediosPago.get(codMedioPago);
    }

    public VencimientoBean getVencimiento(Long idVencimiento) {
        return vencimientos.get(idVencimiento);
    }

    public List<MedioPagoBean> getMediosPagoLista() {
        if (mediosPagoLista == null) {
            mediosPagoLista = new ArrayList<MedioPagoBean>(mediosPago.values());
        }
        return mediosPagoLista;
    }

    public static MediosPago getInstancia() {
        if (instancia == null) {
            throw new RuntimeException("Los medios de pago no han sido cargados.");
        }
        return instancia;
    }

    public static void cargarMediosPago() throws MedioPagoException {
        instancia = new MediosPago();
        Connection conn = new Connection();
        try {
            log.debug("cargarMediosPago() - Cargando datos de medios de pago del sistema... ");
            conn.abrirConexion(Database.getConnection());

            // consultamos todos los medios de pago
            MediosPagoDao.consultarMediosPago(conn, instancia);
            MediosPagoDao.consultarBancos(conn, instancia);
            MediosPagoDao.consultarTiposClientes(conn, instancia);
            MediosPagoDao.consultarVencimientos(conn, instancia);
            MediosPagoDao.consultarDescuentos(conn, instancia);
            instancia.eliminarVencimientosInactivos();
        } catch (SQLException e) {
            log.error("cargarMediosPago() - " + e.getMessage());
            String mensaje = "Error al consultar datos de medios de pago: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } catch (Exception e) {
            log.error("cargarMediosPago() - " + e.getMessage());
            String mensaje = "Error inesperado cargando medios de pago: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    public static MedioPagoBean consultar(String codMedPag) throws MedioPagoException {
        Connection conn = new Connection();
        try {
            conn.abrirConexion(Database.getConnection());
            return MediosPagoDao.consultar(conn, codMedPag);
        } catch (SQLException e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar datos de medios de pago: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    public static MedioPagoBean getCodigoEstablecimiento(String codMedPag) throws MedioPagoException {
        Connection conn = new Connection();
        try {
            conn.abrirConexion(Database.getConnection());
            return MediosPagoDao.getCodigoEstablecimiento(conn, codMedPag);
        } catch (SQLException e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar datos de medios de pago: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    public void addMedioPago(MedioPagoBean medioPago) {
        // Añadimos al medio de pago al mapa general
        mediosPago.put(medioPago.getCodMedioPago(), medioPago);

        // Añadimos al mapa de tipos de medios de pago
        Byte tipoMedioPago = medioPago.getTipo();
        if (tipoMedioPago != null) {
            mediosPagoTipos.get(tipoMedioPago).add(medioPago);
        }

        // Si es de tipo tarjeta y tiene bines configurados, los añadimos al mapa de bines
        if ((medioPago.isTarjetaCredito() || medioPago.isOtros() || medioPago.isGiftCard()) && medioPago.getBines() != null) {
            for (String bine : medioPago.getBines()) {
                binesMedioPagos.put(bine, medioPago);
            }
        }
        // Revisamos casos especiales
        if (medioPago.isEfectivo()) {
            pagoEfectivo = medioPago;
        } else if (medioPago.isNotaCredito()) {
            pagoNotaCredito = medioPago;
        } else if (medioPago.isRetencion()) {
            pagoRetencion = medioPago;
        } else if (medioPago.isBonoEfectivo()) {
            pagoBono = medioPago;
        }

    }

    public void addBanco(BancoBean banco, String codMedioPago) {
        MedioPagoBean medioPago = mediosPago.get(codMedioPago);
        if (medioPago != null) {
            medioPago.getBancos().add(banco);
        }
    }

    public void addDescuento(DescuentoBean nuevoDescuento) {
        Long idTipoCliente = nuevoDescuento.getCodTipoCliente();
        Long idVencimiento = nuevoDescuento.getIdMedioPagoVencimiento();

        if (!nuevoDescuento.isActivo()) { // significa que el vencimiento no debe estar activo para esta tienda
            vencimientosInactivos.add(idVencimiento);
        } else {
            Map<Long, DescuentoBean> descuentos = descuentosCliente.get(idTipoCliente);
            // si descuentos es null es porque este tipo de cliente no tiene activo ningún medio de pago
            if (descuentos != null) {
                DescuentoBean descuento = descuentos.get(idVencimiento);
                if (descuento == null) { // todavía no tenemos descuento para ese vencimiento y ese cliente
                    descuentos.put(idVencimiento, nuevoDescuento);
                } else { // ya teníamos un descuento incluido (especial o no especial), lo completamos con el nuevo
                    descuento.completarDescuento(nuevoDescuento);
                }
            }
        }
    }

    public void addVencimiento(VencimientoBean vencimiento) {
        MedioPagoBean medioPago = mediosPago.get(vencimiento.getCodMedioPago());
        if (medioPago != null) {
            medioPago.getPlanes().add(vencimiento);
            mediosPagoVencimientos.put(vencimiento.getIdMedioPagoVencimiento(), medioPago);
            vencimientos.put(vencimiento.getIdMedioPagoVencimiento(), vencimiento);
        }
    }

    public void addTipoCliente(TipoClienteBean tipoCliente, String codMedioPago) {
        MedioPagoBean medioPago = mediosPago.get(codMedioPago);
        if (medioPago != null) {
            // construimos la lista de medios de pago por tipos para este tipo de cliente
            Map<Byte, List<MedioPagoBean>> mapaMediosPagoCliente = mediosPagoCliente.get(tipoCliente.getCodTipoCliente());
            if (mapaMediosPagoCliente == null) {
                mapaMediosPagoCliente = getMapaMediosPagoTipoVacio();
                mediosPagoCliente.put(tipoCliente.getCodTipoCliente(), mapaMediosPagoCliente);
            }
            Byte tipoMedioPago = medioPago.getTipo();
            if (tipoMedioPago != null) {
                mapaMediosPagoCliente.get(tipoMedioPago).add(medioPago);
            }

            // incluimos el tipo de cliente en el mapa de descuentos por cliente
            Map<Long, DescuentoBean> descuentos = descuentosCliente.get(tipoCliente.getCodTipoCliente());
            if (descuentos == null) {
                descuentos = new HashMap<Long, DescuentoBean>();
                descuentosCliente.put(tipoCliente.getCodTipoCliente(), descuentos);
            }
        }
    }

    /**
     * @author Gabriel Simbania
     * @param conn
     * @throws MedioPagoException
     */
    public void inicializaMediosPagoByLocal(Connection conn) throws MedioPagoException {
        try {
            log.debug("cargarMediosPago() - Cargando datos de medios de pago del sistema... ");
            conn.abrirConexion(Database.getConnection());

            // consultamos todos los medios de pago
            MediosPagoDao.consultarMediosPago(conn, this);
            MediosPagoDao.consultarBancos(conn, this);
            MediosPagoDao.consultarTiposClientes(conn, this);
            MediosPagoDao.consultarVencimientos(conn, this);
            MediosPagoDao.consultarDescuentos(conn, this);
            this.eliminarVencimientosInactivos();
        } catch (SQLException e) {
            log.error("cargarMediosPago() - " + e.getMessage());
            String mensaje = "Error al consultar datos de medios de pago: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } catch (Exception e) {
            log.error("cargarMediosPago() - " + e.getMessage());
            String mensaje = "Error inesperado cargando medios de pago: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    public void inicializaMediosPago(String codAlm) throws MedioPagoException {
        Connection conn = new Connection();
        try {
            log.debug("cargarMediosPago() - Cargando datos de medios de pago del sistema... ");
            conn.abrirConexion(Database.getConnection());

            // consultamos todos los medios de pago
            MediosPagoDao.consultarMediosPago(conn, this);
            MediosPagoDao.consultarBancos(conn, this);
            MediosPagoDao.consultarTiposClientes(conn, this);
            MediosPagoDao.consultarVencimientos(conn, this);
            MediosPagoDao.consultarDescuentos(conn, this, codAlm);
            this.eliminarVencimientosInactivos();
        } catch (SQLException e) {
            log.error("cargarMediosPago() - " + e.getMessage());
            String mensaje = "Error al consultar datos de medios de pago: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } catch (Exception e) {
            log.error("cargarMediosPago() - " + e.getMessage());
            String mensaje = "Error inesperado cargando medios de pago: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    private void eliminarVencimientosInactivos() {
        for (MedioPagoBean medioPago : getMediosPagoLista()) {
            for (Iterator<VencimientoBean> iterador = medioPago.getPlanes().iterator(); iterador.hasNext();) {
                VencimientoBean vencimiento = iterador.next();
                if (vencimientosInactivos.contains(vencimiento.getIdMedioPagoVencimiento())) {
                    iterador.remove();
                }
            }
        }
        // Asumimos como obligatorio que todo medio de pago debe tener al menos un vencimiento activo. 
        // Por lo que no tratamos el caso de que un medio de pago tenga todos sus vencimientos inactivos.
    }

    public Map<String, MedioPagoBean> getBinesMedioPagos() {
        return binesMedioPagos;
    }

    public List<MedioPagoBean> getListaMediosPago(Long tipoCliente, Byte tipoMedioPago) {
        List<MedioPagoBean> resultado;
        Map<Byte, List<MedioPagoBean>> mapaMediosPago = mediosPagoCliente.get(tipoCliente);
        if (mapaMediosPago == null) {
            resultado = new ArrayList<MedioPagoBean>(); // no hay medios de pago definidos para este tipo de cliente
        } else {
            resultado = mapaMediosPago.get(tipoMedioPago);
        }
        return resultado;
    }

    public List<MedioPagoBean> getListaMediosPago(Byte tipoMedioPago) {
        return mediosPagoTipos.get(tipoMedioPago);
    }

    public List<MedioPagoBean> getMediosPagoByBanco(String codBan) {

        List<MedioPagoBean> medios = new ArrayList<>();

        for (MedioPagoBean medioPagoBean : mediosPagoTipos.get(MedioPagoBean.TIPO_TARJETAS)) {
            if (codBan.equals(medioPagoBean.getCodBan())) {
                medios.add(medioPagoBean);
            }
        }

        return medios;
    }

    public List<MarcaTarjetaMedioPagoBean> getMediosPagoByMarcaTarjeta(String codMarcaTarjeta) throws MedioPagoException {

        
        MarcaTarjetaServices marcaTarjetaServices = new MarcaTarjetaServices();
        List<MarcaTarjetaMedioPagoBean> marcaMedioPagoList = marcaTarjetaServices.consultarMarcasTarjeta(codMarcaTarjeta);

        for (MarcaTarjetaMedioPagoBean marcaMedioPago : marcaMedioPagoList) {
            for (MedioPagoBean medioPagoBean : mediosPagoTipos.get(MedioPagoBean.TIPO_TARJETAS)) {
                if (medioPagoBean.getCodMedioPago().equals(marcaMedioPago.getCodMedPag())) {
                    marcaMedioPago.setMedioPagoBean(medioPagoBean);
                }
            }
        }

        return marcaMedioPagoList;
    }

    public MedioPagoBean getPagoBono() {
        return pagoBono;
    }

    public MedioPagoBean getPagoEfectivo() {
        return pagoEfectivo;
    }

    public MedioPagoBean getPagoNotaCredito() {
        return pagoNotaCredito;
    }

    public MedioPagoBean getPagoRetencion() {
        return pagoRetencion;
    }

    public Map<Long, Map<Long, DescuentoBean>> getDescuentosCliente() {
        return descuentosCliente;
    }

    public static MedioPagoBean getEmpleadosAfiliado(String codMedPag, String tarjeta) throws MedioPagoException {
        Connection conn = new Connection();
        try {
            conn.abrirConexion(Database.getConnection());
            return MediosPagoDao.getEmpleadosAfiliado(conn, codMedPag, tarjeta);
        } catch (SQLException e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar datos de medios de pago: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

    public static MedioPagoBean consultarByIdPlan(Long idVencimiento) throws MedioPagoException {
        Connection conn = new Connection();
        try {
            conn.abrirConexion(Database.getConnection());
            String codMedPag = MediosPagoDao.consultarCodMedByIdPlan(conn, idVencimiento);
            return MediosPagoDao.consultar(conn, codMedPag);
        } catch (SQLException e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar datos de medios de pago: " + e.getMessage();
            throw new MedioPagoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
        }
    }

}
