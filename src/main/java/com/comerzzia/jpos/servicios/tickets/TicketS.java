/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets;

import com.comerzzia.jpos.dto.ProcesoEnvioDomicilioDTO;
import com.comerzzia.jpos.dto.ventas.online.ItemOnlineDTO;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasCuponPromo;
import com.comerzzia.jpos.entity.db.ArticuloPlanNovio;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.CabPrefactura;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.ConfigImpPorcentaje;
import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.entity.db.DetPrefactura;
import com.comerzzia.jpos.entity.db.Empresa;
import com.comerzzia.jpos.entity.db.FacturacionTicket;
import com.comerzzia.jpos.entity.db.Vendedor;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosAdicionalesLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosDeEnvio;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.tickets.componentes.VentaEntreLocales;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.gui.reservaciones.JReservacionesVentasCanBaby;
import com.comerzzia.jpos.gui.reservaciones.excepciones.ArticuloEnReservaNotFoundException;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaBean;
import com.comerzzia.jpos.persistencia.promociones.TipoPromocionBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.servicios.articulos.ArticulosParamBuscar;
import com.comerzzia.jpos.servicios.core.usuarios.ServicioUsuarios;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.articulos.ArticuloEnPromocion;
import com.comerzzia.jpos.servicios.promociones.articulos.ArticulosEnPromocion;
import com.comerzzia.jpos.servicios.promociones.cupones.ConfigEmisionCupones;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponException;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponNotFoundException;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponNotValidException;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCupon;
import com.comerzzia.jpos.servicios.promociones.cupones.ServicioCupones;
import com.comerzzia.jpos.servicios.promociones.detalles.ParPromocionDetalle;
import com.comerzzia.jpos.servicios.promociones.totales.TotalesEnPromocion;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.kit.KitException;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoRetencionFuente;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDescuentoCombinado;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDtoManualTotal;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionPagoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketPromociones;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketPromocionesFiltrosPagos;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketPuntosBean;
import com.comerzzia.jpos.util.enums.EnumCodigoItemBase;
import com.comerzzia.jpos.util.enums.catalogo.EnumTipoItem;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author MGRI
 */
public class TicketS {

    private static Logger log = Logger.getMLogger(TicketS.class);
    private LineasTicket lineas;
    private String uid_ticket;
    private long id_ticket;
    private Fecha fecha;
    private Fecha fechaFinDevolucion;
    private String tienda;
    private String codcaja;
    private String uid_diario_caja;
    private String uid_cajero_caja;
    private Cliente cliente;
    private FacturacionTicketBean facturacion;
    private UsuarioBean cajero;
    private TotalesXML totales;
    private PagosTicket pagos;
    private Vendedor vendedor;
    private Empresa empresa;
    private DatosDeEnvio datosEnvio;
    private ProcesoEnvioDomicilioDTO procesoEnvioDomicilioDTO = new ProcesoEnvioDomicilioDTO();
    private VentaEntreLocales ventaEntreLocales;
    private Cliente invitadoActivo;
    private List<Cupon> cuponesEmitidos = new ArrayList<Cupon>();
    private List<Cupon> cuponesAplicados = new ArrayList<Cupon>();
    private String autorizadorVentaOtroLocal;
    private UsuarioBean autorizadorVenta;
    private String observaciones;
    private SesionPdaBean referenciaSesionPDA;
    private TicketPuntosBean puntosTicket;
    private TicketPromociones promociones;
    private boolean finalizado = false;
    private UsuarioBean usuarioAplazado;
    private PromocionTipoDtoManualTotal promoDtoManualTotal;
    private Map<LineaTicket, List<Long>> lineasPromocion; // Linea del ticket y lista de promociones que aplica
    private boolean modoVenta = false;
    private CabPrefactura cabPrefactura;
    private boolean ventaManual; //G.S atributo para saber si la venta es normal o manual
    private Long promoPago = 0L;
    private List<LineaTicket> promoAceptada = new ArrayList<LineaTicket>();
    private Cupon cupon;
    private boolean ventaOnline;

    public TicketS() {
    }

    public TicketS(String uid_diario_caja, String uid_cajero_caja, boolean aplicaPromocionesLineas) {
        super();
        lineas = new LineasTicket();
        lineas.setTicket(this);
        uid_ticket = UUID.randomUUID().toString();
        this.uid_diario_caja = uid_diario_caja;
        this.uid_cajero_caja = uid_cajero_caja;
        totales = new TotalesXML(this);
        vendedor = new Vendedor();
        getTicketPromociones().setAplicaPromocionesLineas(aplicaPromocionesLineas);
        try {
            promoDtoManualTotal = new PromocionTipoDtoManualTotal(null);
            PromocionTipoDtoManualTotal.desActivar();
        } catch (Exception e) {
            // ignore
        }
    }

    public TicketS(boolean aplicaPromocionesLineas) {
        this(Sesion.getCajaActual().getCajaActual().getUidDiarioCaja(), Sesion.getCajaActual().getCajaParcialActual().getUidCajeroCaja(), aplicaPromocionesLineas);
        cliente = Sesion.getClienteGenericoReset();
        tienda = VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN);
        codcaja = Sesion.getDatosConfiguracion().getCodcaja();
        cajero = Sesion.getUsuario();
        fecha = new Fecha();
    }

    public TicketS(Reservacion reserva) {
        this(Sesion.getCajaActual().getCajaActual().getUidDiarioCaja(), Sesion.getCajaActual().getCajaParcialActual().getUidCajeroCaja(), reserva.getReservacion().getReservaTipo().getPermiteReservarPromocionados());
        // Dependiendo de la reserva tendremos que el cliente de la reserva cliente o el invitado
        if (reserva.getReservacion().getReservaTipo().getPermiteFacturarInvitado()) {

            if (reserva.getInvitadoActivo().getSocio() != null && reserva.getInvitadoActivo().isSocio()) {
                cliente = reserva.getInvitadoActivo();
            } else if (reserva.getReservacion().getCliente().getSocio() != null && reserva.getReservacion().getCliente().isSocio()) {
                cliente = reserva.getReservacion().getCliente();
            } else {
                cliente = reserva.getInvitadoActivo();
            }
            setFacturacion(reserva.getInvitadoActivo());
        }

        iniciaDatosBaseTicket();
    }

    public void inicializaTotales(BigDecimal total) {
        if (totales == null) {
            totales = new TotalesXML(this);
        }
        totales.setTotalAPagar(total);
        pagos = new PagosTicket(this);
    }

    public void inicializaPagos() {
        pagos = new PagosTicket(this);
    }

    public void inicializaPagos(PagosTicket pagos) {
        this.pagos = pagos;
    }

    /**
     *
     * @param codigo - Código de barras del artículo
     * @param articulo
     * @param cantidad - Cantidad de la línea
     * @param tarifa
     * @param cantAntigua - Cantidad antigua que tenía la línea cuando la
     * estamos editando
     * @return
     * @throws TicketNuevaLineaException
     */
    public LineaTicket crearLineaTicket(String codigo, Articulos articulo, Integer cantidad, Tarifas tarifa, int cantAntigua) throws TicketNuevaLineaException {
        // Comprobar que el artículo es compatible con promoción día del socio
        ArticuloEnPromocion articuloEnDiaSocio = ArticulosEnPromocion.getInstance().getDiaSocio(articulo.getCodart());
        if (articuloEnDiaSocio != null
                && articuloEnDiaSocio.isRestringido()
                && !getTicketPromociones().isFacturaAsociadaDiaSocio()) {
            throw new TicketNuevaLineaException("El artículo indicado sólo puede ser vendido a través de la promoción Día del Socio.");
        }

        // Comprobar que el artículo es compatible con promoción meses de gracia
        if (getTicketPromociones().isAplicaPromocionesLineas()
                && Sesion.promocionMesesGracia != null
                && Sesion.promocionMesesGracia.isAplicableACliente(cliente)
                && Sesion.promocionMesesGracia.isAplicableAFecha()) {
//            LineaTicket linea = new LineaTicket(codigo, articulo, cantidad, tarifa.getPrecioVenta(), tarifa.getPrecioTotal(),tarifa.getCostoLanded());
            LineaTicket linea = new LineaTicket(codigo, articulo, cantidad, tarifa.getPrecioVenta(), tarifa.getPrecioTotal(), tarifa.getPrecioCosto(), articulo.getCodcategoria(), tarifa.getPrecioReal());
            LineasTicket lineasAuxiliar = new LineasTicket();
            lineasAuxiliar.getLineas().add(linea);
            if (Sesion.promocionMesesGracia.isAplicableALineas(lineasAuxiliar)) {
                if (lineas.isEmpty() || lineas.soloLineasGarantiaExtendida()) { // si la promoción es aplicable a la línea, marcamos la factura como exclusiva para meses de gracia
                    getTicketPromociones().setMesesGraciaAplicado(true);
                } else if (!getTicketPromociones().isMesesGraciaAplicado()) { // si la promoción es aplicable, pero la factura tiene otros artículos que no, avisamos 
                    throw new TicketNuevaLineaException("Este producto SÍ pertenece a la promoción Meses de Gracia, facture de forma independiente.");
                }
            } else if (getTicketPromociones().isMesesGraciaAplicado()) { // si la promoción no es aplicable, pero la factura es sólo para meses de gracia, avisamos
                if (!linea.getCodigoBarras().equals(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS))
                        && !linea.getArticulo().getCodart().startsWith(Constantes.ARTICULO_FUNDA)) {
                    throw new TicketNuevaLineaException("Este producto NO pertenece a la promoción Meses de Gracia, facture de forma independiente.");
                }
            }
        }

        // Reseteamos canjeo de puntos
        getPuntosTicket().resetearCanjeo();

        // Creamos la nueva línea
        LineaTicket linea = lineas.nuevaLinea(codigo, articulo, cantidad, tarifa);

        // Aplicar promociones unitarias a la línea
        if (getTicketPromociones().isAplicaPromocionesLineas()) {
            ArticuloEnPromocion articuloEnPromocion = ArticulosEnPromocion.getInstance().get(articulo.getCodart());
            if (articuloEnPromocion != null) {
                ParPromocionDetalle promocionDetalle = articuloEnPromocion.getPromocionOptima(cliente, linea, tarifa);
                // es posible que no nos devuelva ninguna promoción porque no sean válidas para el cliente
                if (promocionDetalle != null) {
                    promocionDetalle.getPromocion().aplicaLineaUnitaria(this, linea, promocionDetalle.getDetalle(), tarifa);
                }
            }
        }

        // Aplicar promoción día del socio a la línea
        try {
            if (getTicketPromociones().isFacturaAsociadaDiaSocio()) {
                Sesion.promocionDiaSocio.aplicaLineaDiaSocio(linea, this, cantAntigua);
            }
            recalcularTotales();
        } catch (TicketNuevaLineaException e) {
            try {
                eliminarLineaTicket();
            } catch (KitException ex) {
                throw new TicketNuevaLineaException(ex.getMessage());
            }
            throw e;
        }

        return linea;
    }

    public void crearLineaCompraReserva(Reservacion reservacion, Articulos articulo, TicketS ticketReservacion, Tarifas tarifa, String codigo, int modo, int cantidad) throws ArticuloEnReservaNotFoundException, TicketNuevaLineaException {

        // Primer artículo encontrado no comprado
        ReservaArticuloBean pArt = null;

        if (modo == JReservacionesVentasCanBaby.MODO_PAGO_ARTICULOS) {

            List<ReservaArticuloBean> lineasOriginales = reservacion.getReservacion().getReservaArticuloList();
            int cantOrigen = 0;
            // cantidad de artículos devueltos en devoluciones anteriores
            int cantReservada = 0;

            // cantidad de artículos devueltos en la devolución actual        
            for (ReservaArticuloBean res : lineasOriginales) {
                if (res.getCodart().equals(articulo.getCodart())) {
                    cantOrigen = cantOrigen + res.getCantidad().intValue();
                    if (res.getComprado()) {
                        cantReservada = cantReservada + res.getCantidad().intValue();
                    } else {
                        if (pArt == null) {
                            pArt = res;
                        }
                    }
                }
            }

            if (cantOrigen == 0) {
                throw new ArticuloEnReservaNotFoundException("El Artículo indicado no encuentra en la lista de artículos de la reserva.");
            }

            int cantAComprar = lineas.getContains(false, articulo.getCodart(), new ArrayList());

            // cantidad que es posible comprar
            int cantPosibleCompra = cantOrigen - cantReservada - cantAComprar;

            if (cantPosibleCompra <= cantidad - 1) {
                throw new ArticuloEnReservaNotFoundException("Se ha superado el número de artículos de este tipo que permite la reserva");
            }
        }
        // Creamos la nueva línea
        lineas.nuevaLinea(codigo, articulo, cantidad, tarifa);
        recalcularTotales();
    }

    public void crearLineaCompraReserva(PlanNovioOBJ planNovio, Articulos articulo, Tarifas tarifa, String codigo, int modo, int cantidad) throws ArticuloEnReservaNotFoundException {
        ArticuloPlanNovio pArt = null;

        if (modo == JReservacionesVentasCanBaby.MODO_PAGO_ARTICULOS_PLAN_NOVIOS) {

            List<ArticuloPlanNovio> lineasOriginales = planNovio.getPlan().getListaArticulos();
            int cantOrigen = 0;
            // cantidad de artículos devueltos en devoluciones anteriores
            int cantReservada = 0;

            // cantidad de artículos devueltos en la devolución actual        
            for (ArticuloPlanNovio res : lineasOriginales) {
                if (res.getCodArt().equals(articulo.getCodart()) && res.getBorrado() != 'S') {
                    cantOrigen = cantOrigen + 1;
                    if (res.isCompradoOReservado()) {
                        cantReservada = cantReservada + 1;
                    } else {
                        if (pArt == null) {
                            pArt = res;
                        }
                    }
                }
            }

            if (cantOrigen == 0) {
                throw new ArticuloEnReservaNotFoundException("El Artículo indicado no encuentra en la lista de artículos del Plan Novios");
            }

            int cantAComprar = lineas.getContains(false, articulo.getCodart(), new ArrayList());

            // cantidad que es posible comprar
            int cantPosibleCompra = cantOrigen - cantReservada - cantAComprar;

            if (cantPosibleCompra <= cantidad - 1) {
                throw new ArticuloEnReservaNotFoundException("Se ha superado el numero de artículos de este tipo que permite la reserva");
            } else {
                LineaTicket linea = lineas.nuevaLinea(codigo, articulo, cantidad, tarifa);
            }

        } else {
            LineaTicket linea = lineas.nuevaLinea(codigo, articulo, cantidad, tarifa);
            if (getTicketPromociones().isAplicaPromocionesLineas()) {
                ArticuloEnPromocion articuloEnPromocion = ArticulosEnPromocion.getInstance().get(articulo.getCodart());
                if (articuloEnPromocion != null) {
                    ParPromocionDetalle promocionDetalle = articuloEnPromocion.getPromocionOptima(cliente, linea, tarifa);
                    // es posible que no nos devuelva ninguna promoción porque no sean válidas para el cliente
                    if (promocionDetalle != null) {
                        promocionDetalle.getPromocion().aplicaLineaUnitaria(this, linea, promocionDetalle.getDetalle(), tarifa);
                    }
                }
            }
        }
        recalcularTotales();

    }

    public void crearLineaTicketCupon(CodigoBarrasCuponPromo codigoCupon) throws CuponNotValidException, CuponException {
        if (getTicketPromociones().isFacturaAsociadaDiaSocio()) {
            throw new CuponException("No puede aplicar cupones en una factura Día del Socio.");
        }
        Cupon cupon = null;
        try {
            cupon = ServicioCupones.consultarCuponDescuento(codigoCupon.getIdCupon(), codigoCupon.getAlmacen());
        } catch (CuponNotFoundException e) {
            log.debug("El cupón indicado no está registrado en el sistema.: " + e.getMessage());
            throw new CuponNotValidException("El cupón indicado no está registrado en el sistema.");
        } catch (PromocionException e) {
            throw new CuponException("Se ha producido algún error consultando la promoción asociada al cupón indicado.", e);
        }
        for (Cupon cuponAplicado : cuponesAplicados) {
            if (cuponAplicado.equals(cupon)) {
                throw new CuponNotValidException("El cupón indicado ya se está aplicando en la factura actual.");
            }
        }
        if (!cupon.isVigente()) {
            throw new CuponNotValidException("El cupón indicado está caducado. La fecha de validez ya ha pasado.");
        }
        if (cupon.getPromocion() != null && !cupon.getPromocion().isAplicableACliente(cliente)) {
            throw new CuponNotValidException("El cupón indicado no es aplicable a este cliente.");
        }

        // aplicamos cupón
        cupon.getPromocion().aplicaCupon(this, cupon);

        // incluimos el cupón como aplicado
        cuponesAplicados.add(cupon);

    }

    public boolean tieneSukuponAplicado() {
        for (Cupon cuponAplicado : cuponesAplicados) {
            if (cuponAplicado.getPromocion().getTipoPromocion().isPromocionTipoBilleton()) {
                return true;
            }
        }
        return false;
    }

    public String getIdFactura() {
        String a = new Long(id_ticket).toString();
        String b = new Long(id_ticket).toString();
        for (int i = a.length(); i < 9; i++) {
            b = "0" + b;
        }
        return tienda + "-" + codcaja + "-" + b;
    }
//////ricardo

    private void ajustarImportesLineasSobreBase(BigDecimal baseAnterior, BigDecimal baseReal, List<LineaTicket> lineas) {
        BigDecimal diferencia = baseAnterior.subtract(baseReal);
        log.debug("ajustarImportesLineasSobreBase() - La diferencia a ajustar es de: " + diferencia);
        boolean restar = Numero.isMayorACero(diferencia);
        BigDecimal ajuste = new BigDecimal("0.01");
        if (restar) {
            ajuste = ajuste.negate();
        }
        for (int i = 0; i < lineas.size(); i++) {
            LineaTicket linea = lineas.get(i);
            log.debug("ajustarImportesLineasSobreBase() - Ajustando " + ajuste + " sobre línea: " + linea.getArticulo().getCodart() + " con importe " + linea.getImporteFinalPagado());
            if (Numero.isMayorACero(linea.getImporteFinalPagado())) {
                linea.setImporteFinalPagado(linea.getImporteFinalPagado().add(ajuste));
                linea.setImporteFinalPagadoSinRedondear(linea.getImporteFinalPagadoSinRedondear().add(ajuste));
                //aqui hay que hacer cambio hace un ajuste de ctvs en negativo
//                if (linea.getArticulo().getPmp() == null) {
//                    linea.setImporteTotalFinalMenosPromocionCabeceraYMedioPago(linea.getImporteTotalFinalMenosPromocionCabeceraYMedioPagoGetter().add(ajuste));
//                } else {
//                    linea.setImporteTotalFinalMenosPromocionCabeceraYMedioPago(BigDecimal.ZERO);
//                }
                linea.setImporteTotalFinalMenosPromocionCabeceraYMedioPago(linea.getImporteTotalFinalMenosPromocionCabeceraYMedioPagoGetter().add(ajuste));

                diferencia = diferencia.add(ajuste);

                BigDecimal importeTarifaOrigen = Numero.redondear(linea.getPrecioTarifaOrigen()).multiply(new BigDecimal(linea.getCantidad()));
                if (importeTarifaOrigen.equals(BigDecimal.ZERO)) {
                    linea.setDescuentoFinal(BigDecimal.ZERO);
                } else if (linea.getImporteFinalPagado().equals(BigDecimal.ZERO)) {
                    linea.setDescuentoFinal(new BigDecimal(100));
                } else {
                    linea.setDescuentoFinal(Numero.getTantoPorCientoMenosR(importeTarifaOrigen, linea.getImporteFinalPagado()));
                }
                linea.setDescuentosAcumulados(linea.getDescuentoFinal());
                if (Numero.isIgualACero(diferencia)) {
                    break;
                }
            }
            if (i == lineas.size() - 1 && !Numero.isIgualACero(diferencia)) {
                i = -1;
                //i=0;
                if (Numero.isMenorACero(diferencia)) {
                    break;
                }
            }
        }
    }
//Ricardo Promociones

    private void aplicarPromocionesLineasMultiple() {
        lineas.resetearPromocionesMultiples();
        getTicketPromociones().resetear();
        List<PromocionTipoDescuentoCombinado> promocionesParaPruducto = new ArrayList<PromocionTipoDescuentoCombinado>();
        for (Promocion promocion : Sesion.promociones) {
////            if (promocion.getIdPromocion() == 1030 || promocion.getIdPromocion() == 1027 || promocion.getIdPromocion() == 1029) {
////                continue;
//////                System.out.println("");
////            }
//// if(promocion.getIdPromocion()==1027)
////            {
////                System.out.println("");
////                continue;
////               
////            }
            if (promocion.isAplicableACliente(cliente)
                    && promocion.isAplicableALineas(lineas)
                    && promocion.isAplicableAFecha()
                    && promocion.isReaplicable(this)) {
                PromocionTipoDescuentoCombinado objeto = null;

                try {
                    if (promocion instanceof PromocionTipoDescuentoCombinado) {
                        objeto = (PromocionTipoDescuentoCombinado) promocion;
                    } else {
                        System.out.print("Tipo Diferente a PromocionTipoDescuentoCombinado ");
                    }

                } catch (Exception ex) {
                    System.out.print("Tipo Diferente a PromocionTipoDescuentoCombinado ");
                }
                if (objeto != null) {
                    promocionesParaPruducto.add(objeto);
//                    boolean aceptada = true;

                    TicketPromocionesFiltrosPagos promoFiltroPagos = Sesion.getTicket().getTicketPromociones().getPromocionesFiltrosPagos();
//                    if (!promoFiltroPagos.isAceptada(promocion.getIdPromocion())) {
//                    promoFiltroPagos.addPromocion(promocion, aceptada);
//                        promoAceptada = Sesion.getTicket().getLineas().getLineas();
//                        List<Long> listaPromoAceptada = new ArrayList<Long>();
//                        listaPromoAceptada.add(promocion.getIdPromocion());
//                        for (LineaTicket lineaPromo : promoAceptada) {
//                            lineaPromo.setListaPromocion(listaPromoAceptada);
//                        }
//                    } 
                } else {
                    promocion.aplicaLineasMultiple(lineas);
                }
            }
        }
        //pruebas Ricardo Delgado cambio de promociones
        List<PromocionTipoDescuentoCombinado> promocionescomunes = new ArrayList<>();
        PromocionTipoDescuentoCombinado promoultima = null;
        for (int j = 0; j < promocionesParaPruducto.size(); j++) {
            if (!promocionesParaPruducto.get(j).isTieneFiltroPagosTarjetaSukasa()) {
                promoultima = promocionesParaPruducto.get(j);
                promocionescomunes.add(promoultima);
                promocionesParaPruducto.remove(j);
                j--;
            }
        }
        promocionescomunes = ordenarMayorPromocion(promocionescomunes);
//         if(promoultima!=null){
//         promocionesParaPruducto.add(promoultima);
//         }
//         Sesion.setPromocionesPruebaricado(promocionesParaPruducto);
//        if(lineas.)
        if (!Sesion.isEdicion()) {

            String cadena = "<html>PARA OTORGAR PRECIO DE AFILIADO INGRESE UN NUMERO ENTRE 1 Y 4 SEGUN EL PLAZO: <HR><BR>";
            if (promocionesParaPruducto.size() > 0) {
                for (int i = 0; i < promocionesParaPruducto.size(); i++) {

                    System.out.println("Promociones ha aplicar");
                    System.out.println(promocionesParaPruducto.get(i).getDescripcionImpresion());
                    cadena = cadena + " " + (i + 1) + ". " + promocionesParaPruducto.get(i).getDescripcionImpresion()
                            + " " + promocionesParaPruducto.get(i).getDescuento() + "% DE DESCUENTO" + "<BR>";

//                  lista a=new lista();
//                  a.setVisible(true);
//                JOptionPane.showMessageDialog(null,promocionesParaPruducto.get(i).getDescripcion()+
//                        " "+promocionesParaPruducto.get(i).getDescuento()+" %");
//            promocionesParaPruducto.get(i).aplicaLineasMultiple(lineas);
//              v_descuento_linea.setVisible(false);
                }
                cadena = cadena + "</html>";
                Integer valor;

//             if(Sesion.getPromocionEnProceso()==-1){ 
                valor = JPrincipal.getInstance().crearVentanaPromocion(cadena, new BigDecimal(promocionesParaPruducto.size()));

//             if(valor!=null){  Comentado para mejorar
//             
//                if(valor>0 && valor <=promocionesParaPruducto.size()){
//                valor=valor-1;
//             
//             if(lineas.getTicket().getTicketPromociones().getPromocionesFiltrosPagos().isRechazada(promocionesParaPruducto.get(valor).getIdPromocion())){
//                 lineas.getTicket().getPromociones().getPromocionesFiltrosPagos().resetPromocion(promocionesParaPruducto.get(valor).getIdPromocion());
//             }
//             promocionesParaPruducto.get(valor).aplicaLineasMultiple(lineas);
////             Sesion.setPromocionEnProceso(valor);
//                }else{
//                    if(promocionescomunes.size()>0){
//                      if(lineas.getTicket().getTicketPromociones().getPromocionesFiltrosPagos().isRechazada(promocionescomunes.get(0).getIdPromocion())){
//                       lineas.getTicket().getPromociones().getPromocionesFiltrosPagos().resetPromocion(promocionescomunes.get(0).getIdPromocion());
//             }
//                      if(!lineas.getTicket().getPromociones().getPromocionesFiltrosPagos().isAceptada(promocionescomunes.get(0).getIdPromocion())){                          
//                         lineas.getTicket().getPromociones().getPromocionesFiltrosPagos().resetPromocion(promocionescomunes.get(0).getIdPromocion());
//                          promocionescomunes.get(0).aplicaLineasMultiple(lineas);  
//                      }else{
//                          //analizar
//                         promocionesParaPruducto.get(0).aplicaLineasMultiple(lineas);  
//                      }
//                                                   }
//                }
//            }
//           }else{
//                  promocionesParaPruducto.get(Sesion.getPromocionEnProceso()).aplicaLineasMultiple(lineas); 
//           }
                if (valor != null) {

                    if (valor > 0 && valor <= promocionesParaPruducto.size()) {
                        valor = valor - 1;

                        if (lineas.getTicket().getTicketPromociones().getPromocionesFiltrosPagos().isRechazada(promocionesParaPruducto.get(valor).getIdPromocion())) {
                            lineas.getTicket().getPromociones().getPromocionesFiltrosPagos().resetPromocion(promocionesParaPruducto.get(valor).getIdPromocion());
                        }
                        promocionesParaPruducto.get(valor).aplicaLineasMultiple(lineas);
                        escogerMejorPromocion(lineas);
                    } else {
                        if (promocionescomunes.size() > 0) {

                            if (lineas.getTicket().getTicketPromociones().getPromocionesFiltrosPagos().isRechazada(promocionescomunes.get(0).getIdPromocion())) {
                                lineas.getTicket().getPromociones().getPromocionesFiltrosPagos().resetPromocion(promocionescomunes.get(0).getIdPromocion());
                            } else {
                                promocionescomunes.get(0).aplicaLineasMultiple(lineas);
                                escogerMejorPromocion(lineas);
                            }

//                         if(!lineas.getTicket().getPromociones().getPromocionesFiltrosPagos().isAceptada(promocionescomunes.get(0).getIdPromocion())){                          
//                          lineas.getTicket().getPromociones().getPromocionesFiltrosPagos().resetPromocion(promocionescomunes.get(0).getIdPromocion());
//                          promocionescomunes.get(0).aplicaLineasMultiple(lineas); 
//                         }
                        } else {
                            escogerMejorPromocion(lineas);
                        }

                    }
                }
//        }else{  comentado para mejorar
//              if(promocionescomunes.size()>0){
//                  if(lineas.getTicket().getTicketPromociones().getPromocionesFiltrosPagos().isRechazada(promocionescomunes.get(0).getIdPromocion())){
//                       lineas.getTicket().getPromociones().getPromocionesFiltrosPagos().resetPromocion(promocionescomunes.get(0).getIdPromocion());
//             }
//                      if(!lineas.getTicket().getPromociones().getPromocionesFiltrosPagos().isAceptada(promocionescomunes.get(0).getIdPromocion())){                          
////                          lineas.getTicket().getPromociones().getPromocionesFiltrosPagos().resetPromocion(promocionescomunes.get(0).getIdPromocion());
//                          promocionescomunes.get(0).aplicaLineasMultiple(lineas);  
//                      }else{
//                          //analizar
////                         promocionesParaPruducto.get(0).aplicaLineasMultiple(lineas);  
//                      }
//                                                   } 
            } else {
                if (promocionescomunes.size() > 0) {
                    promocionescomunes.get(0).aplicaLineasMultiple(lineas);
                    escogerMejorPromocion(lineas);
                } else {
                    //CAMBIO PROMOCIONES
//                    if (getPromoPago() == 1L) {
                    escogerMejorPromocion(lineas);
//                        escogerMejorPromocion(lineas);
//                        Iterator<LineaTicket> lineasTicketIterator = getLineas().getLineas().iterator();

//                        while (lineasTicketIterator.hasNext()) {
//                            LineaTicket linea = lineasTicketIterator.next();
////                            if (!linea.getCodigoBarras().equals(Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS))) {
////                                if (linea.getListaPromocion().size() > 0) {
////                                    linea.resetDescuento();
////                                    linea.setPromocionLinea(null);
////                                }
////                            }
//                        }
//                    } else {
//                        escogerMejorPromocion(lineas);
//                    }
                }
            }

        }

    }

    /**
     * @author Gabriel Simbania
     * @description Se escoge la opcion con el mejor descuento
     * @param lineas
     */
    public void escogerMejorPromocion(LineasTicket lineas) {
        Long base = 0l;
        String desBase = null;
        BigDecimal precioBase = BigDecimal.ZERO;
        for (LineaTicket lineaTicket : lineas.getLineas()) {

            BigDecimal precioLinea = new BigDecimal(lineaTicket.getPrecio().toString());
            BigDecimal precioTotalLinea = new BigDecimal(lineaTicket.getPrecioTotalTarifaOrigen().toString());
            PromocionLineaTicket promocion = null;

            //G.S Busca si existe una promocion de forma de pago
            for (PromocionLineaTicket promo : lineaTicket.getPromocionLineaList()) {
                if (promo.isTieneFiltroPagosTarjetaSukasa()) {
                    promocion = promo;
                    break;
                }
            }

            boolean tienePromocionRegalo = Boolean.FALSE;
            PromocionLineaTicket promocionLineaTicket = lineaTicket.getPromocionLinea();

            //G.S Busca si tiene una promocion de regalo de compra
            if (promocion == null && promocionLineaTicket != null && Objects.equals(promocionLineaTicket.getIdTipoPromocion(), TipoPromocionBean.TIPO_PROMOCION_REGALO_COMPRA)) {
                tienePromocionRegalo = Boolean.TRUE;
            }

            //G.S. En caso que no exista la promocion de forma de pago se busca el mejor descuento
            if (promocion == null && !lineaTicket.getPromocionLineaList().isEmpty()) {
                Comparator<PromocionLineaTicket> comparador;
                comparador = new Comparator<PromocionLineaTicket>() {
                    public int compare(PromocionLineaTicket p1, PromocionLineaTicket p2) {
                        return (p1.getDescuento().compareTo(p2.getDescuento()));
                    }
                };
                promocion = Collections.max(lineaTicket.getPromocionLineaList(), comparador);
            }
            if (promocion != null && !tienePromocionRegalo) {
                lineaTicket.setPromocionLinea(promocion);
                //Coloca individualmente la promocion por forma de pago
                TicketPromocionesFiltrosPagos promoFiltroPagos = Sesion.getTicket().getTicketPromociones().getPromocionesFiltrosPagos();
                promoFiltroPagos.addPromocionAceptada(Sesion.getPromocion(promocion.getIdPromocion()));
                promoFiltroPagos.resetPromocionMediosPago(promocion.getIdPromocion(), lineaTicket);
                //linea.setPromocionLinea(promocionLinea);
                if (getPromoPago() == 1L) {
                    if (lineaTicket.getListaPromocion().size() > 0) {
                        if (lineaTicket.getItemBase() == null) {
                            lineaTicket.setDescuento(promocion.getDescuento());
                        } else {
                            if (lineaTicket.getItemBase().equals(0L)) {
                                lineaTicket.setDescuento(promocion.getDescuento());
                            }
                        }
                    }
                } else {
                    if (lineaTicket.getItemBase() == null) {
                        lineaTicket.setDescuento(promocion.getDescuento());
                    } else {
                        if (lineaTicket.getItemBase().equals(0L)) {
                            lineaTicket.setDescuento(promocion.getDescuento());
                        }
                    }
                }
                lineaTicket.recalcularPrecios();
                lineaTicket.recalcularAhorroPromocion();

                // Establecemos parametros para mostrar línea
                lineaTicket.setPreciosPantalla(precioLinea, precioTotalLinea);
                lineaTicket.setImpresionLineaDescuento(promocion.getTextoPromocion());
            }
            if (lineaTicket.getItemBase() != null) {
                if (lineaTicket.getItemBase().equals(EnumCodigoItemBase.ITEM_BASE_ACTIVO.getCodigo())) {
                    base = 1L;
                    desBase = lineaTicket.getDesArticuloBase();
                    precioBase = lineaTicket.getPrecioTotalTarifaOrigen();
                }
                if (lineaTicket.getItemBase().equals(EnumCodigoItemBase.ITEM_BASE_MAS.getCodigo())) {
                    base = 0L;
                    desBase = null;
                    precioBase = BigDecimal.ZERO;
                }
            }
            if (Sesion.getAutorizaItemBase() != null) {
                if (lineaTicket.getItemBase() == null) {
                    if (Sesion.getAutorizaItemBase().equals(EnumCodigoItemBase.ITEM_BASE_ACTIVO.getCodigo()) || Sesion.getAutorizaItemBase().equals(EnumCodigoItemBase.ITEM_BASE_MAS.getCodigo())) {
                        if (lineaTicket.getDesArticuloBase() == null) {
                            if ((lineaTicket.getItemBase() == null && base.equals(EnumCodigoItemBase.ITEM_BASE_INICIAL.getCodigo()))) {
                                lineaTicket.setDesArticuloBase(lineaTicket.getArticulo().getDesart().substring(0, 3));
                                lineaTicket.setItemBase(EnumCodigoItemBase.ITEM_BASE_ACTIVO.getCodigo());
                                lineaTicket.setPrecioBase(lineaTicket.getPrecioTotalTarifaOrigen());
                                base = 0L;
                                desBase = null;
                            } else {
                                if (lineaTicket.getItemBase() == null) {
                                    if (desBase.equals(lineaTicket.getArticulo().getDesart().substring(0, 3))) {
                                        if (precioBase.compareTo(lineaTicket.getPrecioTotalTarifaOrigen()) >= 0) {
                                            BigDecimal bd = new BigDecimal(40);
                                            lineaTicket.setDescuento(bd);
                                            lineaTicket.setItemBase(EnumCodigoItemBase.ITEM_BASE_MAS.getCodigo());
                                            Sesion.setAutorizaItemBase(EnumCodigoItemBase.ITEM_BASE_INICIAL.getCodigo());
                                            PromocionLineaTicket promocionLinea = new PromocionLineaTicket();
                                            promocionLinea.setIdPromocion(1L);//Se quema el numero de la promoción
                                            promocionLinea.setDesTipoPromocion("Promoción 22.2.22");
                                            promocionLinea.setIdTipoPromocion(2L);
                                            promocionLinea.setTextoPromocion("Promoción 22.2.22");
                                            promocionLinea.setPrecioTarifa(lineaTicket.getPrecio());
                                            promocionLinea.setPrecioTarifaTotal(lineaTicket.getPrecioTotal());
                                            promocionLinea.setCantidadPromocion(lineaTicket.getCantidad());
                                            promocionLinea.setDescuento(bd);
                                            promocionLinea.setTextoPromocion("Promoción 22.2.22");
//                                        promocionLinea.setTieneFiltroPagosTarjetaSukasa(isTieneFiltroPagosTarjetaSukasa());
                                            lineaTicket.getPromocionLineaList().add(promocionLinea);
                                            base = 0L;
                                            desBase = null;
//                                            String str = null;
//                                            Sesion.setDesArticuloBase(str);
                                            if (promocion == null && !lineaTicket.getPromocionLineaList().isEmpty()) {
                                                Comparator<PromocionLineaTicket> comparador;
                                                comparador = new Comparator<PromocionLineaTicket>() {
                                                    public int compare(PromocionLineaTicket p1, PromocionLineaTicket p2) {
                                                        return (p1.getDescuento().compareTo(p2.getDescuento()));
                                                    }
                                                };
                                                promocion = Collections.max(lineaTicket.getPromocionLineaList(), comparador);
                                            }
                                            if (promocion != null && !tienePromocionRegalo) {
                                                lineaTicket.setPromocionLinea(promocion);
                                                //Coloca individualmente la promocion por forma de pago
                                                TicketPromocionesFiltrosPagos promoFiltroPagos = Sesion.getTicket().getTicketPromociones().getPromocionesFiltrosPagos();
                                                promoFiltroPagos.resetPromocionMediosPago(promocion.getIdPromocion(), lineaTicket);
                                                //linea.setPromocionLinea(promocionLinea);
                                                if (getPromoPago() == 1L) {
                                                    if (lineaTicket.getListaPromocion().size() > 0) {
                                                        if (lineaTicket.getItemBase() == null) {
                                                            lineaTicket.setDescuento(promocion.getDescuento());
                                                        } else {
                                                            if (lineaTicket.getItemBase().equals(EnumCodigoItemBase.ITEM_BASE_INICIAL.getCodigo())) {
                                                                lineaTicket.setDescuento(promocion.getDescuento());
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    if (lineaTicket.getItemBase() == null) {
                                                        lineaTicket.setDescuento(promocion.getDescuento());
                                                    } else {
                                                        if (lineaTicket.getItemBase().equals(EnumCodigoItemBase.ITEM_BASE_INICIAL.getCodigo())) {
                                                            lineaTicket.setDescuento(promocion.getDescuento());
                                                        }
                                                    }
                                                }
                                                lineaTicket.recalcularPrecios();
                                                lineaTicket.recalcularAhorroPromocion();

                                                // Establecemos parametros para mostrar línea
                                                lineaTicket.setPreciosPantalla(precioLinea, precioTotalLinea);
                                                lineaTicket.setImpresionLineaDescuento(promocion.getTextoPromocion());
                                            }
                                        }
                                    }
                                } else {
                                    base = 0L;
                                    desBase = null;
                                    precioBase = BigDecimal.ZERO;
                                }
                            }
                        }
                    }
                }
            } else {
                lineaTicket.setItemBase(0L);
            }
            lineaTicket.setArticuloSeleccionado(true);//linea ya escogida y agregada a la factura
        }

    }
//    private void aplicarPromocionesLineasMultiple() {
//        lineas.resetearPromocionesMultiples();
//        getTicketPromociones().resetear();
//        for (Promocion promocion : Sesion.promociones) {
//            if (promocion.isAplicableACliente(cliente)
//                    && promocion.isAplicableALineas(lineas)
//                    && promocion.isAplicableAFecha()
//                    && promocion.isReaplicable(this)) {
//                promocion.aplicaLineasMultiple(lineas);
//            }
//        }
//    }

    static List<PromocionTipoDescuentoCombinado> ordenarMayorPromocion(List<PromocionTipoDescuentoCombinado> promocionesParaPruducto) {
        for (int i = 0; i < promocionesParaPruducto.size() - 1; i++) {
            for (int j = 0; j < promocionesParaPruducto.size() - 1; j++) {
                if (promocionesParaPruducto.get(j).getDescuento() < promocionesParaPruducto.get(j + 1).getDescuento()) {
                    PromocionTipoDescuentoCombinado tmp = promocionesParaPruducto.get(j + 1);
                    promocionesParaPruducto.set(j + 1, promocionesParaPruducto.get(j));
                    promocionesParaPruducto.set(j, tmp);
                }
            }
        }
        return promocionesParaPruducto;
    }

    static List<PromocionTipoDescuentoCombinado> ordenarMenorPromocion(List<PromocionTipoDescuentoCombinado> promocionesParaPruducto) {
        for (int i = 0; i < promocionesParaPruducto.size() - 1; i++) {
            for (int j = 0; j < promocionesParaPruducto.size() - 1; j++) {
                if (promocionesParaPruducto.get(j).getDescuento() > promocionesParaPruducto.get(j + 1).getDescuento()) {
                    PromocionTipoDescuentoCombinado tmp = promocionesParaPruducto.get(j + 1);
                    promocionesParaPruducto.set(j + 1, promocionesParaPruducto.get(j));
                    promocionesParaPruducto.set(j, tmp);
                }
            }
        }
        return promocionesParaPruducto;
    }

    public void recalcularTotales() {
        // borramos cupones aplicados
        cuponesAplicados.clear();

        // borramos lineas de descuento resultado de aplicar promociones múltiples
        lineas.resetearLineasDescuento();

        // reseteamos y recalculamos los totales de lineas y sus promociones (antes de calcular promociones para tener totales actualizados)
        totales.recalcularTotalesLineas(lineas);

        // aplicamos promociones que aplican a varias líneas
        if (getTicketPromociones().isAplicaPromocionesLineas()) {
            aplicarPromocionesLineasMultiple();
            // reseteamos y recalculamos los totales de lineas y sus promociones (después de aplicar promociones, para tener en cuenta nuevos descuentos)
            totales.recalcularTotalesLineas(lineas);
        }

        // aplicamos promociones que apliquen sobre los subtotales del ticket
        if (getTicketPromociones().isAplicaPromocionesLineas()) {
            List<Promocion> promociones = TotalesEnPromocion.getInstance().getPromocionesAplicables(totales, cliente);
            for (Promocion promocion : promociones) {
                promocion.aplicaSubtotales(totales, lineas);
            }
        }

        if (PromocionTipoDtoManualTotal.isActiva()) {
            promoDtoManualTotal.aplicaSubtotales(getTotales(), getLineas());
        }

        // redondeamos todas las cantidades
        totales.redondear();
    }

    public void recalcularFinalPagado() {
        totales.calcularTotalDescuentos(pagos.calculaAhorroTotal());
        // recalculamos lo pagado final en cada línea en base a los descuentos sobre subtotales y pagos
        if (lineas != null) {
            lineas.recalcularFinalPagado(totales.getTotalDtoPromoSubtotales(), totales.getTotalDtoPagos());
            lineas.recalcularImpuestos(getTotales());
        }
        totales.calcularSubtotalesProrrateados(lineas);
        totales.redondear();
    }

    public void recalcularFinalPagadoFinal(boolean recalcula) {
        // Obtenemos el total pagado por el cliente real
        BigDecimal totalPagadoReal = BigDecimal.ZERO;
        for (Pago pago : pagos.getPagos()) {
            totalPagadoReal = totalPagadoReal.add(pago.getUstedPaga());
        }

        // Separamos líneas de base 0 y de base 12, y calculamos el total de la base cero. Además, recalculamos final pagado redondeado
        List<LineaTicket> lineasBase0 = new ArrayList<LineaTicket>();
        List<LineaTicket> lineasBase12 = new ArrayList<LineaTicket>();
        BigDecimal baseCeroAnterior = BigDecimal.ZERO;
        BigDecimal baseDoceAnterior = BigDecimal.ZERO;
        BigDecimal importeTotalFinal = BigDecimal.ZERO;
        if (lineas == null || lineas.getLineas() == null) {
            return;
        }
        for (LineaTicket lineaTicket : lineas.getLineas()) {
            BigDecimal importeTarifaOrigen = Numero.redondear(lineaTicket.getPrecioTarifaOrigen().multiply(new BigDecimal(lineaTicket.getCantidad())));
            if (lineaTicket.getArticulo().getPmp() == null) {
                lineaTicket.setImporteFinalPagado(Numero.menosPorcentajeR(importeTarifaOrigen, Numero.redondear(lineaTicket.getDescuentoFinal())));
                importeTotalFinal = importeTotalFinal.add(Numero.menosPorcentajeR(importeTarifaOrigen, Numero.redondear(lineaTicket.getDescuentoFinal())));
            } else {
                lineaTicket.setDescuentoFinal(BigDecimal.ZERO);
                lineaTicket.setImporteFinalPagado(Numero.menosPorcentajeR(importeTarifaOrigen, Numero.redondear(lineaTicket.getDescuentoFinal())));
                importeTotalFinal = importeTotalFinal.add(Numero.menosPorcentajeR(importeTarifaOrigen, Numero.redondear(lineaTicket.getDescuentoFinal())));
            }
            if (lineaTicket.getArticulo().isArticuloNoAplicaIva()) {
                lineaTicket.setImporteFinalPagadoSinRedondear(lineaTicket.getImporteFinalPagado());
            } else {
                if (lineaTicket.getArticulo().getPmp() == null) {
                    lineaTicket.setImporteFinalPagadoSinRedondear(Numero.menosPorcentajeR4(importeTarifaOrigen, Numero.redondear(lineaTicket.getDescuentoFinal())));
                } else {
                    lineaTicket.setDescuentoFinal(BigDecimal.ZERO);
                }
                lineaTicket.setImporteFinalPagadoSinRedondear(Numero.menosPorcentajeR4(importeTarifaOrigen, Numero.redondear(lineaTicket.getDescuentoFinal())));
            }
            if (lineaTicket.getArticulo().isArticuloNoAplicaIva()) {
                lineasBase0.add(lineaTicket);
                baseCeroAnterior = baseCeroAnterior.add(Numero.redondear(lineaTicket.getImporteFinalPagado()));
            } else {
                if (lineaTicket.getImporteFinalPagado().compareTo(BigDecimal.ZERO) != 0) {
                    lineasBase12.add(lineaTicket);
                    baseDoceAnterior = baseDoceAnterior.add(Numero.redondear(lineaTicket.getImporteFinalPagado()));
                }
            }
            lineaTicket.setImporteTotalFinal(importeTotalFinal);
        }

        // Calculamos las bases reales
        BigDecimal baseDoceMasIvaReal = BigDecimal.ZERO;
        BigDecimal baseDoceReal = BigDecimal.ZERO;
        BigDecimal baseCeroReal = BigDecimal.ZERO;
        BigDecimal ivaReal = BigDecimal.ZERO;
        if (lineasBase0.isEmpty()) {
            log.debug("recalcularFinalPagadoFinal() - No hay líneas con base 0");
            baseDoceMasIvaReal = totalPagadoReal;
            baseCeroReal = BigDecimal.ZERO;
        } else if (lineasBase12.isEmpty()) {
            log.debug("recalcularFinalPagadoFinal() - No hay líneas con base " + Sesion.getEmpresa().getPorcentajeIva().toString());
            baseDoceMasIvaReal = BigDecimal.ZERO;
            baseCeroReal = totalPagadoReal;
        } else {
            log.debug("recalcularFinalPagadoFinal() - Hay líeas con ambas bases baseCeroAnterior " + baseCeroAnterior + " baseCeroReal " + baseCeroReal);
            baseCeroReal = baseCeroAnterior;
            baseDoceMasIvaReal = totalPagadoReal.subtract(baseCeroReal);
        }

        if (lineasBase12.isEmpty()) {
            baseDoceReal = BigDecimal.ZERO;
            ivaReal = BigDecimal.ZERO;
        } else {
            if (recalcula && pagos.getPagos().size() == 1 && pagos.contieneTarjetaCredito()) {
                baseDoceReal = Numero.getAntesDePorcentajeR(baseDoceMasIvaReal, Sesion.getEmpresa().getPorcentajeIva());
                ivaReal = Numero.porcentajeR(baseDoceReal, Sesion.getEmpresa().getPorcentajeIva());
                totalPagadoReal = baseCeroReal.add(baseDoceReal).add(ivaReal);
            } else {
                baseDoceReal = Numero.getAntesDePorcentajeR(baseDoceMasIvaReal, Sesion.getEmpresa().getPorcentajeIva());
                ivaReal = baseDoceMasIvaReal.subtract(baseDoceReal);
            }
        }

        log.debug("recalcularFinalPagadoFinal() - baseDoceReal " + baseDoceReal);
        log.debug("recalcularFinalPagadoFinal() - ivaReal " + ivaReal);
        log.debug("recalcularFinalPagadoFinal() - totalPagadoReal " + totalPagadoReal);

        // Actualizamos totales
        totales.getSubtotalesImpuestos().get(ConfigImpPorcentaje.COD_IMPUESTO_EXTENTO).setTotal(baseCeroReal);
        totales.getSubtotalesImpuestos().get(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL).setTotal(baseDoceReal);
        totales.setBase(baseCeroReal.add(baseDoceReal));
        totales.setImpuestos(ivaReal);
        totales.setTotalPagado(totalPagadoReal);

        // Ajustamos importes de líneas para que sumen las bases reales
        if (baseCeroAnterior.compareTo(baseCeroReal) != 0) {
            log.debug("recalcularFinalPagadoFinal() - Ajustando base 0. Base Anterior: " + baseCeroAnterior + ", baseReal: " + baseCeroReal);
            ajustarImportesLineasSobreBase(baseCeroAnterior, baseCeroReal, lineasBase0);
        }
        if (baseDoceAnterior.compareTo(baseDoceReal) != 0) {
            log.debug("recalcularFinalPagadoFinal() - Ajustando base " + Sesion.getEmpresa().getPorcentajeIva().toString() + ". Base Anterior: " + baseDoceAnterior + ", baseReal: " + baseDoceReal);
            ajustarImportesLineasSobreBase(baseDoceAnterior, baseDoceReal, lineasBase12);
        }
        //Ajustamos los descuentos electrónicos
        totales.setTotalDescuentoFinalElectronico(BigDecimal.ZERO);
        for (LineaTicket linea : lineas.getLineas()) {
            if (Numero.isMayorACero(linea.getImporteDescuentoFinal())) {
                totales.setTotalDescuentoFinalElectronico(totales.getTotalDescuentoFinalElectronico().add(linea.getImporteDescuentoFinal()));
            }
        }

        if (recalcula) {
            // Actualizamos las bases de los pagos (sólo Base Cero)
            BigDecimal baseCeroPagosAcumulada = BigDecimal.ZERO;
            for (Pago pago : pagos.getPagos()) {
                if (!pago.isPagoEfectivo() && !pago.isValidado()) {
                    BigDecimal totalPagadoRealMenosCompensacion = totalPagadoReal;
                    if (pagos.isCompensacionAplicada()) {
                        totalPagadoRealMenosCompensacion = totalPagadoRealMenosCompensacion.subtract(getTotales().getCompensacionGobierno());
                    }
                    BigDecimal porcentajeDePago = Numero.getTantoPorCientoContenido(totalPagadoRealMenosCompensacion, pago.getUstedPaga());
                    pago.setSubtotalIva0(Numero.porcentajeR(baseCeroReal, porcentajeDePago));
                    baseCeroPagosAcumulada = baseCeroPagosAcumulada.add(pago.getSubtotalIva0());
                }
            }
            if (!Numero.isIgual(baseCeroPagosAcumulada, baseCeroReal)) {
                BigDecimal diferencia = baseCeroPagosAcumulada.subtract(baseCeroReal);
                boolean restar = Numero.isMayorACero(diferencia);
                BigDecimal ajuste = new BigDecimal("0.01");
                if (restar) {
                    ajuste = ajuste.negate();
                }
                for (Pago pago : pagos.getPagos()) {
                    if (!pago.isPagoEfectivo() && !pago.isValidado()) {
                        pago.setSubtotalIva0(pago.getSubtotalIva0().add(ajuste));
                        diferencia = diferencia.add(ajuste);
                        if (Numero.isIgualACero(diferencia)) {
                            break;
                        }
                    }
                }
            }

            //Miramos las bases y los ivas de los pagos por si hubiera que realizar un pago de efectivo
            BigDecimal diferenciaPago = BigDecimal.ZERO;
            if (pagos.getPagos().size() > 1) {
                for (Pago pago : pagos.getPagos()) {
                    if (!pago.isPagoEfectivo() && !pago.getMedioPagoActivo().isCompensacion() && !pago.isValidado()) {
                        // pago.setUstedPaga(pago.getUstedPaga().add(diferenciaPago));
                        BigDecimal ustedPagaAntes = pago.getUstedPaga();
                        pago.recalculaFromUstedPagaTruncado(pagos.isCompensacionAplicada());
                        //diferenciaPago = ustedPagaAntes.subtract(pago.getUstedPaga());
                        diferenciaPago = diferenciaPago.add(ustedPagaAntes.subtract(pago.getUstedPaga()));
                    }
                }
            } else {
                Pago p = pagos.getPagos().get(0);
                p.setUstedPaga(totalPagadoReal);
                p.setSubtotalIva0(baseCeroReal);
                log.info("actualizando base 12 2 " + baseDoceReal);
                p.setSubtotalIva12(baseDoceReal);
                p.setIva(ivaReal);
            }

            if (diferenciaPago.setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) > 0) {
                if (pagos.isCompensacionAplicada()) {
                    pagos.getPagoCompensacion().setTotal(pagos.getPagoCompensacion().getTotal().add(diferenciaPago));
                } else {
                    if (pagos.contieneEfectivo()) {
                        pagos.getPagoEfectivo().setTotal(pagos.getPagoEfectivo().getTotal().add(diferenciaPago));
                    } else {
                        Pago p = new Pago(pagos, cliente);
                        p.setModalidad(Pago.PAGO_EFECTIVO);
                        p.setPagoActivo(Pago.PAGO_EFECTIVO);
                        p.setMedioPagoActivo(MediosPago.getInstancia().getPagoEfectivo());
                        p.resetear(diferenciaPago);
                        p.establecerDescuento(BigDecimal.ZERO);
                        pagos.addPago(p);
                    }
                }
            }
        } else {
            if (pagos.getPagos().size() > 1) {
                for (Pago pago : pagos.getPagos()) {
                    if (!pago.isPagoEfectivo()) {
                        pago.recalculaFromUstedPagaRedondeado(pagos.isCompensacionAplicada());
                    }
                }
            }
        }
    }

    /**
     * Realiza acciones asociadas a la finalización de la factura (cupones,
     * puntos, promociones). Si el parámetro "previsualizar" es TRUE, las
     * acciones será únicamente previsualizadas, pero no tendrán efecto.
     *
     * @param previsualizar
     */
    public void finalizarTicket(boolean previsualizar) {
        try {
            if (!previsualizar) {
                finalizado = true;
            }

            // Generamos cupones promocionales
            log.debug("Intentanmos emitir cupones...");
            getCuponesEmitidos().clear();
            getTicketPromociones().resetearCuponesPagosPrint();
            for (PromocionTipoCupon promocion : Sesion.promocionesCupones) {
                log.debug("Intentanmos emitir cupones de promoción: " + promocion);
                if (!promocion.isAplicableACliente(cliente)) {
                    continue;
                }
                if (!promocion.isAplicableAFecha()) {
                    continue;
                }
                if (!promocion.isAplicableAPagos(pagos.getPagos())) {
                    continue;
                }
                ConfigEmisionCupones configEmision = promocion.getConfigEmision();
                // intentamos emitir cupones para esta promoción si NO estamos previsualizando.
                if (!previsualizar) {
                    List<Cupon> cupones = configEmision.emitirCupones(this, configEmision);
                    // añadimos al ticket todos los que se hayan emitido, si  hay
                    if (cupones != null) {
                        try {
                            if (configEmision.permiteImprimirPorCadencia(cliente)) {
                                getCuponesEmitidos().addAll(cupones);
                            }
                        } catch (CuponException e) {
                            log.error("generarCuponesPromocionales() - No se ha podido comprobar la cadencia de un cupón, por lo que no se ha podido comprobar si era necesario expedirlo." + e.getMessage());
                        }
                    }
                }
            }
            log.debug("Cupones emitidos entre todas las promociones: " + getCuponesEmitidos().size());

            // Tratamos acumulación de puntos
            getPuntosTicket().resetearAcumulacion();
            getPuntosTicket().acumulaPuntosTicket();
            if (previsualizar) {
                getPuntosTicket().setClienteAcumulacion(cliente);
            }

            // obtenemos textos promocionales de pagos
            for (Pago pago : pagos.getPagos()) {
                if (pago.isPromocionAplicada()) {
                    for (PromocionPagoTicket promocionPago : pago.getPromociones()) {
                        getTicketPromociones().addPromocionCuponesPagosPrint(promocionPago.getPrintPromocion());
                    }
                }
            }

        } catch (Exception e) {
            log.error("generarCuponesPromocionales() - No se han podido generar los cupones promocionales debido a un error: " + e.getMessage(), e);
        }
    }

    public void eliminarLineaTicket() throws KitException {
        eliminarLineaTicket(lineas.getIndexUltimaLinea());

    }

    public void eliminarLineaTicket(int index) throws KitException {
        log.debug("eliminarLineaTicket() - Eliminando línea " + index);

        if (index < lineas.getNumLineas()) {
            LineaTicket linea = lineas.getLinea(index);

            //Si el articulo tiene kits de instalacion
            if (linea.getArticulo().isKitInstalacion()) {
                eliminarArticuloKitInstalacion(index, linea);
            } else {
                if (linea.getKitObligatorio() != null && linea.getKitObligatorio()) {
                    throw new KitException("No puede eliminar un Kit de instalaci\u00F3n obligatorio");
                }
                log.debug("eliminarLineaTicket() - Eliminando aticulo " + linea.getArticulo().getCodart());
                lineas.eliminarLinea(index);
            }
            totales.recalcularTotalesLineas(lineas);
            recalcularTotales();
            resetTicketVacio();
        }
    }

    private void eliminarArticuloKitInstalacion(int index, LineaTicket linea) {

        List<Integer> lineasAEliminar = new ArrayList<>();

        int i = 0;
        lineasAEliminar.add(0, index);
        for (LineaTicket lineaTicket : lineas.getLineas()) {
            if (lineaTicket.getReferenciaKit() != null
                    && lineaTicket.getReferenciaKit().getUidReferencia()!=null
                    && lineaTicket.getReferenciaKit().getUidReferencia().equals(linea.getKitReferenciaOrigen())) {
                log.debug("eliminarLineaTicket() - Eliminando aticulo " + lineaTicket.getArticulo().getCodart());
                lineasAEliminar.add(0, i);
            }
            i++;
        }

        //Elimina las lineas de los articulos 
        for (Integer indexEliminar : lineasAEliminar) {
            lineas.eliminarLinea(indexEliminar);
        }
    }

    public void resetTicketVacio() {
        if (lineas.isEmpty()) {
            setReferenciaSesionPDA(null);
            getTicketPromociones().setMesesGraciaAplicado(false);
        }
    }

    public Integer getIndexLinea(String codigo) {
        return lineas.getIndexLinea(codigo);
    }

    public Integer getIndexLineaCodigoArticulo(String codigo) {
        codigo = ArticulosParamBuscar.getCodArticuloSukasa(codigo);
        return lineas.getIndexLineaCodigoArticulo(codigo);
    }

    public void setDatosAdicionalesLinea(Integer index, DatosAdicionalesLineaTicket datosAdicionales) {
        lineas.setDatosAdicionalesLinea(index, datosAdicionales);
        recalcularTotales();
    }

    public BigDecimal getDescuentoLinea(Integer index) {
        return lineas.getDescuentoLinea(index);
    }

    public void crearNuevaLineaPago(Pago pago) throws ImporteInvalidoException {
        if (!(pago instanceof PagoCredito) && !pago.getMedioPagoActivo().isAbonoReservacion()) {
            if (pago.getMedioPagoActivo().getVencimientoDefault().getPisoMinimo(pago).compareTo(pago.getTotal()) > 0) {
                throw new ImporteInvalidoException("El importe mínimo para pagar con el medio de pago es " + pago.getMedioPagoActivo().getVencimientoDefault().getPisoMinimo(pago));
            }
            if (pago.getMedioPagoActivo().getVencimientoDefault().getPisoMaximo().compareTo(BigDecimal.ZERO) > 0
                    && pago.getMedioPagoActivo().getVencimientoDefault().getPisoMaximo().compareTo(pago.getTotal()) < 0) {
                throw new ImporteInvalidoException("El importe máximo para pagar con el medio de pago es " + pago.getMedioPagoActivo().getVencimientoDefault().getPisoMaximo());
            }
        }
        Toolkit.getDefaultToolkit().beep();
        pagos.addPago(pago);
    }

    // GET y SET
    public LineasTicket getLineas() {
        return lineas;
    }

    public void setLineas(LineasTicket lineas) {
        this.lineas = lineas;
    }

    public String getUid_ticket() {
        return uid_ticket;
    }

    public void setUid_ticket(String uid_ticket) {
        this.uid_ticket = uid_ticket;
    }

    public long getId_ticket() {
        return id_ticket;
    }

    public void setId_ticket(long id_ticket) {
        this.id_ticket = id_ticket;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public String getTienda() {
        return tienda;
    }

    public String getTiendaPrint() {
        return tienda + " " + Sesion.getTienda().getAlmacen().getDesalm();
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja;
    }

    public String getUid_diario_caja() {
        return uid_diario_caja;
    }

    public void setUid_diario_caja(String uid_diario_caja) {
        this.uid_diario_caja = uid_diario_caja;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public UsuarioBean getCajero() {
        return cajero;
    }

    public void setCajero(UsuarioBean cajero) {
        this.cajero = cajero;
    }

    public TotalesXML getTotales() {
        return totales;
    }

    public void setTotales(TotalesXML totales) {
        this.totales = totales;
    }

    public FacturacionTicketBean getFacturacion() {
        return facturacion;
    }

    public FacturacionTicket getFacturacionPN() {
        return new FacturacionTicket(facturacion);
    }

    public PagosTicket getPagos() {
        return pagos;
    }

    public void setPagos(PagosTicket pagos) {
        this.pagos = pagos;
    }

    public void eliminarLineaPago(int i) {
        pagos.eliminarPago(i);
        //recalcularTotales(); // Elimina cupones añadidos y recalcula promociones. Creemos que no es necesario. Lo quitamos.
    }

    public void eliminarLineaPago() {
        eliminarLineaPago(pagos.getIndexUltimaLinea());
    }

    public Pago editarLineaPago(int i) {
        return pagos.getPagos().get(i);

    }

    /**
     * Recalcula los totales para un pago cuando no se dispone de las líneas de
     * los artículos sino solo un importe
     */
    public void eliminarLineaPagoV() {
        eliminarLineaPagoV(pagos.getIndexUltimaLinea());
    }

    /**
     * Recalcula los totales para un pago cuando no se dispone de las líneas de
     * los artículos sino solo un importe
     */
    public void eliminarLineaPagoV(int i) {
        if (i < 0) {
            return;
        }
        Pago pag = pagos.getPagos().get(i);
        if (pag.isPromocionAplicada()) {
            for (PromocionPagoTicket promos : pag.getPromociones()) {
                eliminaLineaPromocion(promos.getIdPromocion());
            }
        }
        Boolean tieneRetencion = false;

        if (pag.getRetencion() != null) {
            tieneRetencion = true;
        }

        pagos.eliminarPago(i);
        if (tieneRetencion) {
            for (int j = 0; j < pagos.getPagos().size(); j++) {
                Pago p = pagos.getPagos().get(j);
                if (p instanceof PagoRetencionFuente) {
                    pagos.eliminarPago(j);
                    break;
                }
            }

        }

        pagos.recalculaTotales();

    }

    public Pago editarLineaPago() {
        return editarLineaPago(pagos.getIndexUltimaLinea());
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public void setFacturacionCliente() {
        setFacturacion(cliente);
    }

    public void setFacturacionCliente(Cliente cliente) {
        setFacturacion(cliente);
    }

    public void setFacturacionClienteGenerico() {
        setFacturacion(Sesion.clienteGenerico);
    }

    public void setFacturacion(FacturacionTicketBean facturacion) {
        this.facturacion = facturacion;
    }

    private void setFacturacion(Cliente cliente) {
        facturacion = new FacturacionTicketBean();
        facturacion.setNombre(cliente.getNombre());
        facturacion.setApellidos(cliente.getApellido());
        facturacion.setDocumento(cliente.getIdentificacion());
        facturacion.setDireccion(cliente.getDireccion());
        facturacion.setProvincia(cliente.getPoblacion());
        facturacion.setTelefono(cliente.getTelefonoFacturacion());
        facturacion.setTipoDocumento(cliente.getTipoIdentificacion());
        facturacion.setEmail(cliente.getEmail());
    }

    public boolean hayEnvioADomicilio() {
        return this.lineas.hayEnvioADomicilio();
    }

    public boolean hayRecogidaPosterior() {
        return this.lineas.hayRecogidaPosterior();
    }

    public void estableceDatosEnvio(String apellido, String movil, String nombre, String telefono, String horario, String direccion, String ciudad) {
        this.datosEnvio = new DatosDeEnvio(apellido, movil, nombre, telefono, horario, direccion, ciudad);
    }

    public DatosDeEnvio getDatosEnvio() {
        return datosEnvio;
    }

    public void setDatosEnvio(DatosDeEnvio datosEnvio) {
        this.datosEnvio = datosEnvio;
    }

    public String getUid_cajero_caja() {
        return uid_cajero_caja;
    }

    public void setUid_cajero_caja(String uid_cajero_caja) {
        this.uid_cajero_caja = uid_cajero_caja;
    }

    public void crearDatosVentaEntreLocales(String codAlmacenOrigen, String codAlmacenDestino, String desAlmacenOrigen, String desAlmacenDestino, String codigoConfirmacion) {
        this.ventaEntreLocales = new VentaEntreLocales(codAlmacenOrigen, codAlmacenDestino, desAlmacenOrigen, desAlmacenDestino, codigoConfirmacion);
    }

    public VentaEntreLocales getVentaEntreLocales() {
        return ventaEntreLocales;
    }

    public void setVentaEntreLocales(VentaEntreLocales ventaEntreLocales) {
        this.ventaEntreLocales = ventaEntreLocales;
    }

    public boolean esVentaEntreLocales() {
        return (this.ventaEntreLocales != null);
    }

    public boolean isClienteSocio() {
        return getCliente().isSocio();
    }

    public Fecha getFechaFinDevolucion() {
        return fechaFinDevolucion;
    }

    public void setFechaFinDevolucion(Fecha fechaFinDevolucion) {
        this.fechaFinDevolucion = fechaFinDevolucion;
    }

    public Cliente getInvitadoActivo() {
        return invitadoActivo;
    }

    public void setInvitadoActivo(Cliente invitadoActivo) {
        this.invitadoActivo = invitadoActivo;
    }

    public List<Cupon> getCuponesAplicados() {
        return cuponesAplicados;
    }

    public List<Cupon> getCuponesEmitidos() {
        return cuponesEmitidos;
    }

    public void ordenarCupones() {
        if (cuponesEmitidos != null) {
            Collections.sort(cuponesEmitidos);
        }
    }

    public void setAutorizadorVentaOtroLocal(String autorizador) {
        this.autorizadorVentaOtroLocal = autorizador;
    }

    public String getAutorizadorVentaOtroLocal() {
        return autorizadorVentaOtroLocal;
    }

    public void iniciaDatosBaseTicket() {
        tienda = VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN);
        codcaja = Sesion.getDatosConfiguracion().getCodcaja();
        cajero = Sesion.getUsuario();
        fecha = new Fecha();
    }

    public void iniciaUID() {
        this.uid_ticket = UUID.randomUUID().toString();
    }

    public boolean tieneSesionPDAAsociada() {
        return getReferenciaSesionPDA() != null;
    }

    public SesionPdaBean getReferenciaSesionPDA() {
        return referenciaSesionPDA;
    }

    public void setReferenciaSesionPDA(SesionPdaBean referenciaSesionPDA) {
        this.referenciaSesionPDA = referenciaSesionPDA;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public TicketPuntosBean getPuntosTicket() {
        if (puntosTicket == null) {
            puntosTicket = new TicketPuntosBean(this);
        }
        return puntosTicket;
    }

    public TicketPromociones getTicketPromociones() {
        if (promociones == null) {
            promociones = new TicketPromociones(this);
        }
        return promociones;
    }

    public UsuarioBean getAutorizadorVenta() {
        return autorizadorVenta;
    }

    public void setAutorizadorVenta(String autorizadorVenta) {
        if (autorizadorVenta != null && !autorizadorVenta.isEmpty()) {
            if (cajero != null && cajero.getUsuario() != null && cajero.getUsuario().equals(autorizadorVenta)) {
                return;
            }
            try {
                this.autorizadorVenta = ServicioUsuarios.consultar(autorizadorVenta.toUpperCase());
            } catch (Exception ex) {
                log.error("setAutorizadorVenta() - Excepción consultando datos del usuario : " + autorizadorVenta);
            }
        }
    }

    public boolean hayPagoCreditoTemporal() {
        return pagos.contieneMedioPagoCreditoTemporal();
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public LineaEnTicket getObservacionesLineaEnTicket() {
        return new LineaEnTicket(getObservaciones(), false, true);
    }

    public boolean isObservacionVacia() {
        if (observaciones == null || observaciones.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public UsuarioBean getUsuarioAplazado() {
        return usuarioAplazado;
    }

    public void setUsuarioAplazado(UsuarioBean usuarioAplazado) {
        this.usuarioAplazado = usuarioAplazado;
    }

    public Map<LineaTicket, List<Long>> getLineasPromocion() {
        if (lineasPromocion == null) {
            lineasPromocion = new HashMap<LineaTicket, List<Long>>();
        }
        return lineasPromocion;
    }

    public void setLineasPromocion(Map<LineaTicket, List<Long>> lineasPromocion) {
        this.lineasPromocion = lineasPromocion;
    }

    public void putLineaPromocion(LineaTicket lineaTicket, Long idPromocion) {
        if (lineasPromocion == null) {
            lineasPromocion = new HashMap<LineaTicket, List<Long>>();
        }
        if (!lineasPromocion.containsKey(lineaTicket)) {
            lineasPromocion.put(lineaTicket, new ArrayList<Long>());
        }
        lineasPromocion.get(lineaTicket).add(idPromocion);
    }

    public void resetearLineasPromocion() {
        lineasPromocion = new HashMap<LineaTicket, List<Long>>();
    }

    public void eliminaLineaPromocion(Long idPromocion) {
        for (List<Long> listaPromocion : getLineasPromocion().values()) {
            Iterator<Long> it = listaPromocion.iterator();
            while (it.hasNext()) {
                Long idPromo = it.next();
                if (idPromo.equals(idPromocion)) {
                    it.remove();
                }
            }
        }
    }

    public TicketPromociones getPromociones() {
        return promociones;
    }

    public void setPromociones(TicketPromociones promociones) {
        this.promociones = promociones;
    }

    public PromocionTipoDtoManualTotal getPromoDtoManualTotal() {
        return promoDtoManualTotal;
    }

    public void setPromoDtoManualTotal(PromocionTipoDtoManualTotal promoDtoManualTotal) {
        this.promoDtoManualTotal = promoDtoManualTotal;
    }

    public boolean isModoVenta() {
        return modoVenta;
    }

    public void setModoVenta(boolean modoVenta) {
        this.modoVenta = modoVenta;
    }

    public void eliminarPagoCompensacion() {
        if (pagos.getPagos() != null) {
            for (int i = 0; i < pagos.getPagos().size(); i++) {
                Pago pag = pagos.getPagos().get(i);
                if (pag.getMedioPagoActivo().isCompensacion()) {
                    pagos.eliminarPago(i);
                    pagos.recalculaTotales();
                }
            }
        }
    }

    public boolean facturacionConDatosDelCliente() {
        if (facturacion == null) {
            return true;
        }
        return false;

    }

    public boolean facturacionConOtrosDatos() {
        if (facturacion != null) {
            return true;
        }
        return false;
    }

    public CabPrefactura getCabPrefactura() {
        return cabPrefactura;
    }

    public void setCabPrefactura(CabPrefactura cabPrefactura) {
        this.cabPrefactura = cabPrefactura;
    }

    public boolean isVentaManual() {
        return ventaManual;
    }

    public void setVentaManual(boolean ventaManual) {
        this.ventaManual = ventaManual;
    }

    /**
     * @author Gabriel Simbania
     * @param codigo - Código de barras del artículo
     * @param articulo
     * @param cantidad - Cantidad de la línea
     * @param tarifa
     * @param detPrefactura - Detalle de la prefactura
     * @return
     * @throws TicketNuevaLineaException
     */
    public LineaTicket crearLineaTicketPrefactura(String codigo, Articulos articulo, Integer cantidad, Tarifas tarifa, DetPrefactura detPrefactura) throws TicketNuevaLineaException {
        // Comprobar que el artículo es compatible con promoción día del socio
        ArticuloEnPromocion articuloEnDiaSocio = ArticulosEnPromocion.getInstance().getDiaSocio(articulo.getCodart());
        if (articuloEnDiaSocio != null
                && articuloEnDiaSocio.isRestringido()
                && !getTicketPromociones().isFacturaAsociadaDiaSocio()) {
            throw new TicketNuevaLineaException("El artículo indicado sólo puede ser vendido a través de la promoción Día del Socio.");
        }

        // Reseteamos canjeo de puntos
        getPuntosTicket().resetearCanjeo();
        articulo.setCodimp(detPrefactura.getDetCodImp()); //Setea si tiene iva
        tarifa.setPrecioVentaSinRedondear(detPrefactura.getDetPvpUniSinIvaSinDsto()); //Setea el precio de la prefactura

        // Creamos la nueva línea
        LineaTicket linea = lineas.nuevaLinea(codigo, articulo, cantidad, tarifa);
        linea.setCantidad(detPrefactura.getDetCantidad());
        linea.setPrecioTotalTarifaOrigen(detPrefactura.getDetPvpUniConIvaSinDsto());
        //linea.setPorcentajeDescuentosMedioPagos(detPrefactura.getDetPorceDsto());
        linea.setDescuento(detPrefactura.getDetPorceDsto());
        linea.recalcularPrecios();
        //linea.setPrecioTarifaOrigen(detPrefactura.getDetPvpUniSinIvaSinDsto());
        //linea.setImporteTotalFinalPagado(detPrefactura.getDetPvpTotalConIvaConDsto());
        //linea.setImporteFinalPagadoSinRedondear(detPrefactura.getDetPvpTotalSinIvaConDsto());
        //linea.setImporteTotal(detPrefactura.getDetPvpUniConIvaSinDsto());
        //Recogida posterior
        DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
        datosAdicionales.setDescuento(detPrefactura.getDetPorceDsto());
        datosAdicionales.setRecogidaPosterior(Boolean.TRUE);
        linea.setDatosAdicionales(datosAdicionales);
        recalcularTotales();

        return linea;
    }

    public Long getPromoPago() {
        return promoPago;
    }

    public void setPromoPago(Long promoPago) {
        this.promoPago = promoPago;
    }

    /**
     * Crea la l&iacute;nea del ticket para el proceso de la venta online
     *
     * @author Gabriel Simbania
     * @param codigo
     * @param articulo
     * @param cantidad
     * @param tarifa
     * @param itemOnlineDTO
     * @return
     * @throws TicketNuevaLineaException
     */
    public LineaTicket crearLineaTicketOnline(String codigo, Articulos articulo, Integer cantidad, Tarifas tarifa, ItemOnlineDTO itemOnlineDTO
    ) throws TicketNuevaLineaException {
        // Comprobar que el artículo es compatible con promoción día del socio
        ArticuloEnPromocion articuloEnDiaSocio = ArticulosEnPromocion.getInstance().getDiaSocio(articulo.getCodart());
        if (articuloEnDiaSocio != null
                && articuloEnDiaSocio.isRestringido()
                && !getTicketPromociones().isFacturaAsociadaDiaSocio()) {
            throw new TicketNuevaLineaException("El artículo indicado sólo puede ser vendido a través de la promoción Día del Socio.");
        }

        // Reseteamos canjeo de puntos
        getPuntosTicket().resetearCanjeo();
        //tarifa.setPrecioVenta(BigDecimal.ZERO);
        articulo.setCodimp(itemOnlineDTO.getItmCobraIva() ? "1" : "0"); //Setea si tiene iva

        BigDecimal diferenciaPrecio = tarifa.getPrecioTotal().subtract(itemOnlineDTO.getItmPvpUnitario());
        if (diferenciaPrecio.abs().compareTo(new BigDecimal("0.01")) >= 0) {
            tarifa.cambiarPrecioTotal(itemOnlineDTO.getItmPvpUnitario(), articulo);
        }

        BigDecimal porcentajeDescuento = itemOnlineDTO.getPorcentajeDescuento() != null ? itemOnlineDTO.getPorcentajeDescuento() : BigDecimal.ZERO;

        // Creamos la nueva línea
        LineaTicket linea = lineas.nuevaLinea(codigo, articulo, cantidad, tarifa);
        linea.setCantidad(cantidad);
        //linea.setImporteFinalPagadoSinRedondear(itemOnlineDTO.getItmPvpAfivaTotal());
        //linea.setPrecioTotalTarifaOrigen(detPrefactura.getDetPvpUniConIvaSinDsto());
        //linea.setPorcentajeDescuentosMedioPagos(detPrefactura.getDetPorceDsto());
        linea.setDescuento(porcentajeDescuento);
        linea.recalcularPrecios();

        //Recogida posterior
        DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
        datosAdicionales.setDescuento(porcentajeDescuento);

        String codArtEntrega1 = Variables.getVariable(Variables.ITEM_ENTREGA_DOMICILIO_WEB_VALOR_1);
        String codArtEntrega2 = Variables.getVariable(Variables.ITEM_ENTREGA_DOMICILIO_WEB_VALOR_2);

        //Las entregas a domicilio no deben ser pedidos facturados y entrega a domicilio
        if (articulo.getCodart().equals(codArtEntrega1) || articulo.getCodart().equals(codArtEntrega2)
                || EnumTipoItem.EGO.equals(itemOnlineDTO.getTipoItem()) || EnumTipoItem.ENTREGA_DOMICILIO.equals(itemOnlineDTO.getTipoItem())) {
            datosAdicionales.setEnvioDomicilio(Boolean.FALSE);
            linea.setPedidoFacturado(Boolean.FALSE);
            itemOnlineDTO.setEntregaDomicilio(Boolean.FALSE);
        } else {
            datosAdicionales.setEnvioDomicilio(Boolean.TRUE);
            linea.setPedidoFacturado(Boolean.TRUE);
            itemOnlineDTO.setEntregaDomicilio(Boolean.TRUE);
        }

        linea.setDatosAdicionales(datosAdicionales);
        recalcularTotales();

        return linea;
    }

    public Cupon getCupon() {
        return cupon;
    }

    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
    }

    public boolean isVentaOnline() {
        return ventaOnline;
    }

    public void setVentaOnline(boolean ventaOnline) {
        this.ventaOnline = ventaOnline;
    }

    public ProcesoEnvioDomicilioDTO getProcesoEnvioDomicilioDTO() {
        return procesoEnvioDomicilioDTO;
    }

    public void setProcesoEnvioDomicilioDTO(ProcesoEnvioDomicilioDTO procesoEnvioDomicilioDTO) {
        this.procesoEnvioDomicilioDTO = procesoEnvioDomicilioDTO;
    }

}
