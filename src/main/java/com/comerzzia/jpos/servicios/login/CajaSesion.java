/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.login;

import com.comerzzia.jpos.entity.db.Caja;
import com.comerzzia.jpos.entity.db.CajaCajero;
import com.comerzzia.jpos.entity.db.CajaDet;
import com.comerzzia.jpos.entity.services.cierrecaja.CierreCaja;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.persistencia.cajas.GestionDeCajasDao;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.cajas.CajasServices;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoBono;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoNotaCredito;
import com.comerzzia.jpos.servicios.core.usuarios.ServicioUsuarios;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.tickets.ReferenciaTicket;
import es.mpsistemas.util.log.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;

import com.comerzzia.jpos.entity.db.XCintaAuditoraItemTbl;
import com.comerzzia.jpos.entity.db.XCintaAuditoraTbl;
import com.comerzzia.jpos.entity.db.XCintaAuditoraTblPK;

import com.comerzzia.jpos.servicios.cintaauditora.CintaAuditoraServices;
import com.comerzzia.jpos.servicios.core.contadores.ServicioContadores;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author MGRI
 */
public class CajaSesion {

    /* Tipos de movimiento de Caja          
    M ) Movimiento de caja. Actualmente se usa para aperturas 
    y anmpliación de límite de movimiento de caja. 
    T ) Movimientos de venta.
    En concepto Pone VENTA-DIFERIDO si es de un pago de tipo tarjeta con mas de una cuota.
    A ) Anulación. El movimiento es causado por una anulación.
    D ) Devolución
    R ) Tipo Abono de reserva
     */
    public final static char TIPO_MOVIMIENTO = 'M';
    public final static char TIPO_VENTA = 'T';
    public final static char TIPO_ANULACION = 'A';
    public final static char TIPO_EXPEDICION_BONO = 'E';
    public final static char TIPO_GIFTCARD = 'G';
    private Caja cajaActual;
    private CajaCajero cajaParcialActual;
    private RecuentoSesion recuento;
    private BigDecimal limiteRetiro;
    private BigDecimal efectivoEnCaja;
    private static Logger log = Logger.getMLogger(GestionDeCajasDao.class);

    public CajaSesion() {
        // Cálculo del limite de retiro antes de cerrar la sesión 

        // Limite de retiro establecido en las propiedades. En caso de que la caja estuviese cerrada
        limiteRetiro = Sesion.getDatosConfiguracion().getLimiteRetiro();

        //  
    }

    //<editor-fold defaultstate="collapsed" desc="GESTION DE CAJAS PARCIALES">
    /**
     * Apertura parcial de una caja
     *
     * @throws Exception
     */
    public CajaCajero creaAperturaParcialDeCaja(Caja caja) throws Exception {
        CajaCajero cajaP = null;

        setEfectivoEnCaja(BigDecimal.ZERO);

        if (caja != null) {
            try {
                cajaP = new CajaCajero();
                cajaP.setUidDiarioCaja(caja);
                cajaP.setUidCajeroCaja();
                cajaP.setFechaApertura(new Date());
                cajaP.setFechaCierre(null);

                cajaP.setIdUsuario(Sesion.getUsuario().getIdUsuario());

                recuento = new RecuentoSesion();

                setCajaParcialActual(cajaP);
            } catch (Exception e) {
                log.error("error en la apertura parcial de caja", e);
                throw e;
            }
        }
        return cajaP;
    }

    public void cierreParcialDeCaja() throws Exception {
        //cierre de caja
        try {
            CajasServices.cierreParcialDeCaja(getCajaParcialActual());
            log.debug("Se realizó el cierre parcial de caja correctamente");

            String caja = Sesion.getCajaActual().getCajaParcialActual().getUidDiarioCaja().getCodcaja();
            String almacen = Sesion.getCajaActual().getCajaParcialActual().getUidDiarioCaja().getCodalm();
            String autorizador = Sesion.getUsuAutorizadorGestionCajas();
            String usuario = Sesion.getCajaActual().getCajaParcialActual().getDesUsuario();
            Date fecha = Sesion.getCajaActual().getCajaParcialActual().getFechaCierre();

            setCajaParcialActual(null);
            recuento = new RecuentoSesion(); // lo reiniciamos.

            //INSERTAR EN LAS TABLAS DE CINTA AUDITORA            
            XCintaAuditoraTbl cintaAuditoraDB = new XCintaAuditoraTbl();
            cintaAuditoraDB.setXCintaAuditoraTblPK(new XCintaAuditoraTblPK(BigInteger.valueOf(ServicioContadores.obtenerContadorDefinitivo(ServicioContadores.CONTADOR_CINTA_AUDITORA)), almacen, caja));
            cintaAuditoraDB.setAutorizador(autorizador);
            cintaAuditoraDB.setUsuario(usuario);
            cintaAuditoraDB.setFecha(fecha);

            int size = 0;
            String fechaInicio = CintaAuditoraServices.getFecha(almacen, caja, "2");
            String fechaFin = CintaAuditoraServices.getFecha(almacen, caja, "1");
            Date fechaInicioD = null;
            Date fechaFinD = null;
            //SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY hh:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            if (fechaInicio != "") {
                fechaInicioD = sdf.parse(fechaInicio);
            }
            if (fechaFin != "") {
                fechaFinD = sdf.parse(fechaFin);
            }
            if (fechaInicio != "" && fechaFin != "") {
                size = 2;
            } else {
                size = 1;
            }
            cintaAuditoraDB.setFechaInicio(fechaInicioD);
            cintaAuditoraDB.setFechaFin(fechaFinD);
            cintaAuditoraDB.setXCintaAuditoraItemTblCollection(new ArrayList<XCintaAuditoraItemTbl>());

            CintaAuditoraServices.crearCintaAuditora(cintaAuditoraDB);

            List<XCintaAuditoraItemTbl> listaCintaAuditoraItem = null;
            EntityManagerFactory emf = Sesion.getEmf();
            EntityManager em = emf.createEntityManager();

            listaCintaAuditoraItem = CintaAuditoraServices.obtenerItemsCintaAuditora(em, almacen, caja, size, fechaInicio, fechaFin);

            for (int i = 0; i < listaCintaAuditoraItem.size(); i++) {
                listaCintaAuditoraItem.get(i).setIdCintaAuditoraItem(BigDecimal.valueOf(ServicioContadores.obtenerContadorDefinitivo(ServicioContadores.CONTADOR_CINTA_AUDITORA_ITEM)));
                listaCintaAuditoraItem.get(i).setXCintaAuditoraTbl(cintaAuditoraDB);

                CintaAuditoraServices.crearCintaAuditoraItem(listaCintaAuditoraItem.get(i));
            }
            //INSERTAR EN LAS TABLAS DE CINTA AUDITORA

            cintaAuditoraDB.setXCintaAuditoraItemTblCollection(listaCintaAuditoraItem);

            PrintServices ts = PrintServices.getInstance();
            if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
                ts.imprimirCintaAuditora(0, cintaAuditoraDB);
            } else {
                ts.imprimirCintaAuditora(1, cintaAuditoraDB);
            }
        } catch (Exception e) {
            log.error("Error realizando el cierre parcial de caja.", e);
            throw e;
        }
    }

    public void consultaCajaParcialAbierta() throws Exception {
        setCajaParcialActual(GestionDeCajasDao.consultaCajaParcialAbierta(cajaActual));
        if (getCajaParcialActual() != null) {
            UsuarioBean consultar = ServicioUsuarios.consultar(cajaActual.getIdUsuario());
            this.getCajaParcialActual().setDesUsuario(consultar.getDesUsuario());
            recuento = new RecuentoSesion(GestionDeCajasDao.consultaRecuento(cajaParcialActual.getUidCajeroCaja()));
        } else {
            recuento = new RecuentoSesion();
        }
    }

    //</editor-fold>
    public void aperturaDeCaja(BigDecimal metalicoInicial) throws Exception {
        Caja caja = null;
        setEfectivoEnCaja(BigDecimal.ZERO);

        if (cajaActual == null) {
            try {
                caja = new Caja();
                caja.setUidDiarioCaja();
                caja.setCodalm(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
                caja.setCodcaja(Sesion.getDatosConfiguracion().getCodcaja());
                caja.setFechaApertura(new Date());
                caja.setFechaCierre(null);
                caja.setIdUsuario(Sesion.getUsuario().getIdUsuario());

                recuento = new RecuentoSesion();

                CajaCajero cajaCajero = creaAperturaParcialDeCaja(caja);
                CajaDet apunte = new CajaDet(caja, cajaCajero, 0, "INGRESO INICIAL", "", new Date(), metalicoInicial, MediosPago.getInstancia().getPagoEfectivo(), 'M', 'N');

                GestionDeCajasDao.aperturaDeCaja(caja, cajaCajero, apunte);
                cajaActual = caja;
                cajaParcialActual.setDesUsuario(Sesion.getUsuario().getDesUsuario());

            } catch (Exception e) {
                log.error("Error en la Apertura de caja: ", e);
                throw e;
            }
        }
    }

    public void aperturaParcialDeCaja() throws Exception {
        log.debug("realizando apertura parcial de caja");
        try {
            CajaCajero cajaCajero = creaAperturaParcialDeCaja(cajaActual);
            GestionDeCajasDao.aperturaParcialDeCaja(cajaCajero);
            setCajaParcialActual(cajaCajero);
            recuento = new RecuentoSesion();
            cajaParcialActual.setDesUsuario(Sesion.getUsuario().getDesUsuario());
        } catch (Exception e) {
            log.error("error en la apertura parcial de caja", e);
            throw e;
        }
    }

    public void cierreDeCaja() throws Exception {
        //cierre de caja
        cajaActual.setFechaCierre(new Date());
        CajasServices.cierreDeCaja(cajaActual);
        PrintServices ts = PrintServices.getInstance();
        try {
            if (Sesion.getDatosConfiguracion().getTipoImpresionTicket().equals("PANTALLA")) {
                ts.imprimirPendientesEntrega(0, Sesion.getCajaActual().getCajaActual());
                ts.imprimirEnvioDomicilio(0, Sesion.getCajaActual().getCajaActual());

                ts.ImprimirMovimiento(0, Sesion.getCajaActual().consultaMovimientosES());
            } else {
                ts.imprimirPendientesEntrega(1, Sesion.getCajaActual().getCajaActual());
                ts.imprimirEnvioDomicilio(1, Sesion.getCajaActual().getCajaActual());
                ts.ImprimirMovimiento(1, Sesion.getCajaActual().consultaMovimientosES());
            }
        } catch (Exception e) {
            log.info("No se pudo imprimir los pendientes error general ");

            System.out.println("Descontinuados");
        }

        cajaActual = null;
    }

    public void consultaCajaAbierta() throws Exception {
        cajaActual = GestionDeCajasDao.consultaCajaAbierta(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
        if (cajaActual != null) {
            setEfectivoEnCaja(BigDecimal.ZERO);
            efectivoEnCaja = GestionDeCajasDao.consultaEfectivoEnCaja(cajaActual.getUidDiarioCaja());

            Date fechaUltimaAmpliacionRetiro = GestionDeCajasDao.consultaFechaUltimaAmpliaciónRetiro(cajaActual.getUidDiarioCaja());
            if (fechaUltimaAmpliacionRetiro != null) {
                BigDecimal baseLimiteRetiro = GestionDeCajasDao.consultaLimiteDeRetiro(cajaActual.getUidDiarioCaja(), fechaUltimaAmpliacionRetiro);
                setLimiteRetiro(baseLimiteRetiro.add(Sesion.config.getLimiteRetiro()));
            }
            consultaCajaParcialAbierta();
        }
    }

    public Caja getCajaActual() {
        return cajaActual;
    }

    public boolean isCajaAbierta() {
        return cajaActual != null;
    }

    public void setCajaActual(Caja cajaActual) {
        this.cajaActual = cajaActual;
    }

    public void crearApunte(EntityManager em, BigDecimal importe, String concepto, String documento, MedioPagoBean medioPago) throws Exception {
        try {

            char tipo = TIPO_MOVIMIENTO;
            int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(em, cajaActual.getUidDiarioCaja());
            CajaDet apunte = new CajaDet(cajaActual, getCajaParcialActual(), numerolinea, concepto, documento, null, importe, medioPago, tipo, 'N');
            GestionDeCajasDao.crearApunte(apunte, em);
            if (medioPago.getCodMedioPago().equals(MediosPago.getInstancia().getPagoEfectivo().getCodMedioPago())) {
                Sesion.getCajaActual().sumaEfectivoEnCaja(importe);
            }

        } catch (Exception e) {
            log.error("Error en la creación del Apunte: ", e);
            throw e;
        }
    }

    public void crearApunte3(EntityManager em, BigDecimal importe, String concepto, String documento, MedioPagoBean medioPago, String idDocumento) throws Exception {
        try {

            char tipo = TIPO_MOVIMIENTO;
            int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(em, cajaActual.getUidDiarioCaja());
            CajaDet apunte = new CajaDet(cajaActual, getCajaParcialActual(), numerolinea, concepto, documento, null, importe, medioPago, tipo, 'N');
            apunte.setIdDocumento(idDocumento);
            GestionDeCajasDao.crearApunte(apunte, em);
            if (medioPago.getCodMedioPago().equals(MediosPago.getInstancia().getPagoEfectivo().getCodMedioPago())) {
                Sesion.getCajaActual().sumaEfectivoEnCaja(importe);
            }

        } catch (Exception e) {
            log.error("Error en la creación del Apunte: ", e);
            throw e;
        }
    }

    public void crearApunte2(EntityManager em, BigDecimal importe, String concepto, String documento, MedioPagoBean medioPago, String idDocumento) throws Exception {
        try {

            char tipo = TIPO_VENTA;
            int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(em, cajaActual.getUidDiarioCaja());
            CajaDet apunte = new CajaDet(cajaActual, getCajaParcialActual(), numerolinea, concepto, documento, null, importe, medioPago, tipo, 'N', idDocumento);
            GestionDeCajasDao.crearApunte(apunte, em);
            if (medioPago.getCodMedioPago().equals(MediosPago.getInstancia().getPagoEfectivo().getCodMedioPago())) {
                Sesion.getCajaActual().sumaEfectivoEnCaja(importe);
            }

        } catch (Exception e) {
            log.error("Error en la creación del Apunte: ", e);
            throw e;
        }
    }

    /**
     * Crea un apunte de tipo movimiento
     *
     * @param importe
     * @param concepto
     * @param documento
     * @param medioPago
     * @throws Exception
     */
    public void crearApunteExpedicionBono(BigDecimal importe, String documento, boolean venta, EntityManager em) throws Exception {
        try {
            char tipo = TIPO_EXPEDICION_BONO;

            String concepto = null;
            if (venta) {
                concepto = "VENTA-BONO";
            } else {
                concepto = "RESERVA-BONO";
            }
            int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(cajaActual.getUidDiarioCaja());

            CajaDet apunte = new CajaDet(cajaActual, getCajaParcialActual(), numerolinea, concepto, documento, null, importe.negate(), MediosPago.getInstancia().getPagoBono(), tipo, 'N');
            GestionDeCajasDao.crearApunte(apunte, em);

        } catch (Exception e) {
            log.error("Error en la creación del Apunte: ", e);
            throw e;
        }
    }

    /**
     * Crea un apunte de Devolución
     *
     * @param importe
     * @param concepto
     * @param documento
     * @param medioPago
     * @throws Exception
     */
    public CajaDet crearApunteDevolucion(BigDecimal importe, Long idDevolucion) throws Exception {
        try {
            char tipo = 'D';
            String concepto = "DEV";
            String documento = "DEV " + idDevolucion;
            int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(cajaActual.getUidDiarioCaja());
            BigDecimal importeMovimiento = importe.multiply(new BigDecimal(-1));
            CajaDet apunte = new CajaDet(cajaActual, getCajaParcialActual(), numerolinea, concepto, documento, null, importeMovimiento, MediosPago.getInstancia().getPagoNotaCredito(), tipo, 'N');
            return apunte;

        } catch (Exception e) {
            log.error("Error en la creación del Apunte: ", e);
            throw e;
        }
    }

    public List<CajaDet> crearApunteVenta(List<Pago> pagos, ReferenciaTicket referencia, Character tipo) throws Exception {
        return crearApunteVenta(pagos, referencia, tipo, null);
    }

    /**
     * Crea un apunte de tipo venta
     *
     * @param pagos
     * @param referencia
     * @param tipo
     * @return
     * @throws Exception
     */
    public List<CajaDet> crearApunteVenta(List<Pago> pagos, ReferenciaTicket referencia, Character tipo, Long idAbono) throws Exception {

        List<CajaDet> listaMovimietos = new LinkedList<CajaDet>();
        String concepto = "";
        String documento = "";

        Iterator<Pago> it;
        if (tipo == null) {
            tipo = TIPO_VENTA;
            concepto = "VENTA";
            documento = referencia.getNumTicket();
        } else if (tipo.equals('R')) {
            documento = "RESERVACION";
            concepto = "ABONO";
        } else if (tipo.equals('G')) {
            concepto = "CARGA-GIFTCARD";

        }

        try {
            int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(cajaActual.getUidDiarioCaja());
            String conceptoOriginal = new String(concepto);
            it = pagos.iterator();
            while (it.hasNext()) {
                Character diferido = 'N';
                Pago p = it.next();
                if (p.isPagoTarjeta()) {
                    PagoCredito pagoC = (PagoCredito) p;
                    if (pagoC.getPlanSeleccionado().getNumCuotas() > 1) {
                        diferido = 'S';
                        if (concepto.equals("VENTA")) {
                            concepto = "VENTA-DIFERIDO";
                        }
                    }
                }
                BigDecimal importe = p.getUstedPaga();
                if (p.getMedioPagoActivo().isTarjetaCredito()) {
                    p.setReferencia(((PagoCredito) p).getTarjetaCredito().getNumeroOculto());
                }
                if (p.getMedioPagoActivo().isTarjetaSukasa()) {
                    p.setReferencia(((PagoCredito) p).getTarjetaCredito().getNumero());
                    importe = importe.add(p.getImporteInteres());
                }
                if (p instanceof PagoBono) {
                    importe = ((PagoBono) p).getSaldoBono();
                } else if (p instanceof PagoNotaCredito) {
                    importe = ((PagoNotaCredito) p).getSaldoNotaCredito();
                } else if (p.getMedioPagoActivo().isRetencion()) {
                    importe = p.getTotal();
                }
                CajaDet detalleCaja = new CajaDet(cajaActual, getCajaParcialActual(), numerolinea, concepto, documento, importe, referencia.getIdReferencia(), p.getMedioPagoActivo(), tipo, diferido);
                detalleCaja.setInteres(p.getImporteInteres());
                detalleCaja.setNumCuotas(p.getNumCuotas());
                detalleCaja.setReferencia(p.getReferencia());
                if (idAbono != null) {
                    detalleCaja.setIdAbono(idAbono);
                }
                listaMovimietos.add(detalleCaja);
                numerolinea = numerolinea + 1;
                concepto = conceptoOriginal;
            }

        } catch (Exception e) {
            log.error("crearApunteVenta() - Error procesando movimientos de caja: " + e.getMessage(), e);
            throw e;
        }
        return listaMovimietos;
    }

    public void crearApunteAmpliarRetiro() throws Exception {
        try {
            log.debug("Creando Apunte de Ampliación de Retiro");
            char tipo = 'M';
            String concepto = "A-R";
            String documento = "A-R";
            int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(cajaActual.getUidDiarioCaja());
            MedioPagoBean mpDev = MediosPago.getInstancia().getPagoEfectivo();
            BigDecimal importeMovimiento = BigDecimal.ZERO;
            CajaDet apunte = new CajaDet(cajaActual, getCajaParcialActual(), numerolinea, concepto, documento, null, importeMovimiento, mpDev, tipo, 'N');
            GestionDeCajasDao.crearApunte(apunte);
        } catch (Exception e) {
            log.error("Error en la creación del Apunte: ", e);
            throw e;
        }
    }

    public void crearApuntePrestamo(int tipoPrestamo, BigDecimal importe, String caja) throws Exception {
        int TIPO_RECIBIR = 0;
        int TIPO_REALIZAR = 1;
        BigDecimal importeMovimiento = null;
        try {
            log.debug("Creando Apunte de Ampliación de Retiro");
            char tipo = 'M';
            String concepto = "";
            String documento = "";
            if (tipoPrestamo == TIPO_RECIBIR) {
                concepto = "PRESTAMO";
                documento = "RECIBIR DE " + caja;
                importeMovimiento = importe;
            } else if (tipoPrestamo == TIPO_REALIZAR) {
                concepto = "PRESTAMO";
                documento = "REALIZAR A " + caja;
                importeMovimiento = importe.negate();
            } else {
                log.error("Se ha intentado crear un apunte de tipo no válido");
                throw new Exception();
            }

            int numerolinea = GestionDeCajasDao.consultaSiguenteNumeroLinea(cajaActual.getUidDiarioCaja());
            MedioPagoBean mpDev = MediosPago.getInstancia().getPagoEfectivo();
            CajaDet apunte = new CajaDet(cajaActual, getCajaParcialActual(), numerolinea, concepto, documento, null, importeMovimiento, mpDev, tipo, 'N');
            GestionDeCajasDao.crearApunte(apunte);

        } catch (Exception e) {
            log.error("Error en la creación del Apunte: ", e);
            throw e;
        }
    }

    public List<CajaDet> consultaMovimientos() {
        List<CajaDet> res = null;
        if (cajaActual != null) {
            try {
                res = GestionDeCajasDao.consultaMovimientos(cajaActual.getUidDiarioCaja());

            } catch (Exception ex) {
                log.error("Error en la consulta de movimientos", ex);
                res = new LinkedList<CajaDet>();
            }
        } else {
            res = new LinkedList<CajaDet>();
        }
        return res;
    }

    public List<CajaDet> consultaVentas() {
        List<CajaDet> res = null;
        if (cajaActual != null) {
            try {
                res = GestionDeCajasDao.consultaVentas(cajaActual.getUidDiarioCaja());

            } catch (Exception ex) {
                log.error("Error en la consulta de movimientos de ventas", ex);
                res = new LinkedList<CajaDet>();
            }
        } else {
            res = new LinkedList<CajaDet>();
        }
        return res;
    }

    public RecuentoSesion getRecuento() {
        return recuento;
    }

    public void setRecuento(RecuentoSesion recuento) {
        this.recuento = recuento;
    }

    public void guardarRecuento() throws Exception {
        this.recuento.guardarRecuento(this.getCajaParcialActual().getUidCajeroCaja());
    }

    public CierreCaja consultaMovimientosES() throws Exception {
        CierreCaja cc = new CierreCaja();

        List lineasMovimientos;
        // Modificación: Si la caja esta cerrada no consultará los movimientos
        if (isCajaAbierta()) {
            lineasMovimientos = GestionDeCajasDao.consultaTotalesESMovimientos(this.cajaActual.getUidDiarioCaja());
            cc.setRecuentos(GestionDeCajasDao.consultaTotalesRecuento(this.cajaActual.getUidDiarioCaja()));
        } else {
            lineasMovimientos = new LinkedList();
        }

        //
        // Modificación: El recuento se consulta cuando hay una Caja Parcial abierta, si no , será una lista vacia
        List lineasRecuentos;
        if (isCajaParcialAbierta()) {
            lineasRecuentos = GestionDeCajasDao.consultaTotalesESRecuentos(this.cajaParcialActual.getUidCajeroCaja());
        } else {
            lineasRecuentos = new LinkedList();
        }
        //
        cc.creaLineasCierrecaja(lineasMovimientos, lineasRecuentos);
        if (isCajaAbierta()) {
            cc.calculaDescuadreTotal();
        } else {
            cc.estableceDescuadreACero();
        }

        return cc;
    }

    /**
     * Comprobamos si el limite de retiro se ha alcanzado
     *
     * @return
     */
    public boolean limiteDeRetiro() {
        return (getEfectivoEnCaja().compareTo(getLimiteRetiro()) >= 0);
    }

    public void sumaEfectivoEnCaja(BigDecimal operacion) {
        this.efectivoEnCaja = this.efectivoEnCaja.add(operacion);
    }

    public BigDecimal getEfectivoEnCaja() {
        return efectivoEnCaja;
    }

    public void setEfectivoEnCaja(BigDecimal efectivoEnCaja) {
        this.efectivoEnCaja = efectivoEnCaja;
    }

    public boolean isCajaParcialAbierta() {
        return getCajaParcialActual() != null;
    }

    public CajaCajero getCajaParcialActual() {
        return cajaParcialActual;
    }

    public void setCajaParcialActual(CajaCajero cajaParcialActual) {
        this.cajaParcialActual = cajaParcialActual;
    }

    public BigDecimal getLimiteRetiro() {
        return limiteRetiro;
    }

    public void setLimiteRetiro(BigDecimal limiteRetiro) {
        this.limiteRetiro = limiteRetiro;
    }
}
