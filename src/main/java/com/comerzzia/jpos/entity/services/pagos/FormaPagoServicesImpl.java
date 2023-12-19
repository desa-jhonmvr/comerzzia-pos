/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.pagos;

import com.comerzzia.jpos.dto.ventas.BancoOnlineDTO;
import com.comerzzia.jpos.dto.ventas.MarcaTarjetaOnlineDTO;
import com.comerzzia.jpos.dto.ventas.MedioPagoDTO;
import com.comerzzia.jpos.dto.ventas.online.ItemOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.PedidoOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.PeticionPlanFinanciamientoDTO;
import com.comerzzia.jpos.dto.ventas.online.SubtotalOnlineDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.PlanPagoDTO;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.mediospagos.BancoBean;
import com.comerzzia.jpos.persistencia.mediospagos.MarcaTarjetaBean;
import com.comerzzia.jpos.persistencia.mediospagos.MarcaTarjetaMedioPagoBean;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.mediospagos.VencimientoBean;
import com.comerzzia.jpos.servicios.articulos.ArticuloException;
import com.comerzzia.jpos.servicios.articulos.ArticuloNotFoundException;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.articulos.tarifas.TarifasServices;
import com.comerzzia.jpos.servicios.bancos.BancoServices;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.clientes.ClientesServices;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.marcatarjeta.MarcaTarjetaServices;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCredito;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoBuilder;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoBuilder;
import com.comerzzia.jpos.servicios.pagos.credito.PlanPagoCredito;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionFormaPagoException;
import com.comerzzia.jpos.servicios.promociones.ServicioPromociones;
import com.comerzzia.jpos.servicios.promociones.articulos.PromocionArticuloException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.util.StringParser;
import com.comerzzia.jpos.util.enums.catalogo.EnumTipoItem;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gabriel Simbania
 */
public class FormaPagoServicesImpl implements FormaPagoServices {

    private static final BigDecimal CUOTA_MINIMA = BigDecimal.TEN;
    
    @Override
    public List<MedioPagoDTO> obtenerFormasPago(Long tipoCliente) throws MedioPagoException {

        //MediosPago.cargarMediosPago();
        MediosPago mediosPago = new MediosPago(new HashMap<String, MedioPagoBean>());
        mediosPago.inicializaMediosPago(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
        List<MedioPagoBean> list = mediosPago.getListaMediosPago(tipoCliente, MedioPagoBean.TIPO_TARJETAS);
        List<MedioPagoDTO> medioList = new ArrayList<>();
        for (MedioPagoBean medioPagoBean : list) {
            MedioPagoDTO medioPagoDTO = new MedioPagoDTO(medioPagoBean.getCodMedioPago(), medioPagoBean.getDesMedioPago(), null, null);
            medioList.add(medioPagoDTO);
        }

        return medioList;
    }

    @Override
    public List<PlanPagoDTO> obtenerPlanPago(MedioPagoBean medioPago, BigDecimal totalAPagar, BigDecimal totalGarantiaExtendida,
            String identificacionCliente, boolean esAfiliadoSupermaxi) throws ValidationException, ClienteException, ContadorException, MedioPagoException {

        ClientesServices clientesServices = ClientesServices.getInstance();

        Cliente cliente;

        try {
            cliente = clientesServices.consultaClienteIdenti(identificacionCliente);
        } catch (NoResultException e) {
            cliente = Sesion.getClienteGenericoReset();
        }

        if ("00".equals(cliente.getCodcli())) {
            cliente = Sesion.getClienteGenericoReset();
        }
        //MediosPago mediosPagoTodo = new MediosPago(new HashMap<String, MedioPagoBean>());
        //mediosPagoTodo.inicializaMediosPago(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));

        //MedioPagoBean medioPago =  TarjetaCredito.getBINMedioPagoBanda(numeroTarjeta,mediosPagoTodo);
        if (medioPago == null) {
            throw new ValidationException("No existe el bin en los medios de pago ");
        }

        if (!medioPago.isTarjetaSukasa()) {
            List<VencimientoBean> planes = medioPago.getPlanes();
            List<VencimientoBean> planesActivos = new ArrayList<>();
            for (VencimientoBean vencimiento : planes) {
                //if(vencimiento.isPlaceToPay()){
                planesActivos.add(vencimiento);
                //}
            }
            medioPago.setPlanes(planesActivos);
        }

        //G.S.
        if (esAfiliadoSupermaxi) {
            cliente.setAplicaTarjetaAfiliado(true);
        }

        //Crea el cliente
        //Sesion.iniciaNuevoTicket(cliente);
        TicketS ticket = new TicketS("", "", true);
        ticket.setCliente(cliente);
        //ticket.setFacturacionCliente(cliente);
        TotalesXML totalesXML = new TotalesXML(ticket);
        totalesXML.setTotalAPagar(totalAPagar);
        totalesXML.setTotalGarantiaExtendida(totalGarantiaExtendida);
        ticket.setTotales(totalesXML);

        PagosTicket pagosTicket = new PagosTicket(ticket);
        ticket.setPagos(pagosTicket);

        byte modo = 1;

        TarjetaCredito tc = TarjetaCreditoBuilder.create(medioPago, TarjetaCredito.TARJETA_DEFECTO, false);
        PagoCredito pagoCredito = null;
        try {
            pagoCredito = PagoCreditoBuilder.createPagoCredito(medioPago, null, tc, ticket, modo);
        } catch (PromocionArticuloException ex) {
            Logger.getLogger(FormaPagoServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PromocionFormaPagoException ex) {
            Logger.getLogger(FormaPagoServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<PlanPagoDTO> listaDTO = new ArrayList<>();
        for (PlanPagoCredito plan : pagoCredito.getPlanes()) {
            listaDTO.add(new PlanPagoDTO(plan.getIdPlan(), plan.getPlan(), plan.getDescuento(), (long) plan.getNumCuotas(), plan.getCuota(), plan.getaPagar(), plan.getTotal(), plan.getAhorro(), plan.getPorcentajeInteres(), plan.getImporteInteres(), pagoCredito));
        }

        return listaDTO;
    }

    public PlanPagoDTO obtenerPlanUnicoPago(MedioPagoBean medioPago, BigDecimal totalAPagar, BigDecimal totalGarantiaExtendida,
            String identificacionCliente, boolean esAfiliadoSupermaxi, Long idPlan) throws ValidationException, ClienteException, ContadorException, MedioPagoException {

        List<PlanPagoDTO> listaDTO = obtenerPlanPago(medioPago, totalAPagar, totalGarantiaExtendida, identificacionCliente, esAfiliadoSupermaxi);

        PlanPagoDTO planPagoDTO = null;
        for (PlanPagoDTO plan : listaDTO) {
            if (plan.getIdPlan().equals(idPlan)) {
                planPagoDTO = plan;
                break;
            }
        }

        return planPagoDTO;
    }

    @Override
    @Deprecated
    public List<MedioPagoDTO> obtenerMediosPago(PeticionPlanFinanciamientoDTO peticionPlanFinanciamientoDTO) throws Exception {

        List<MedioPagoDTO> mediosPagos = new ArrayList<>();

        MediosPago mediosPago = new MediosPago(new HashMap<String, MedioPagoBean>());
        mediosPago.inicializaMediosPago(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
        List<MedioPagoBean> list = mediosPago.getListaMediosPago(4L, MedioPagoBean.TIPO_TARJETAS);
        List<Long> promocionesIds = new ArrayList<>();
        for (PedidoOnlineDTO pedido : peticionPlanFinanciamientoDTO.getPedidos()) {
            for (ItemOnlineDTO item : pedido.getItems()) {
                if (item.getIdPromocion() != null) {
                    if (!promocionesIds.contains(item.getIdPromocion())) {
                        promocionesIds.add(item.getIdPromocion());
                    }
                }
            }
        }

        Map<Long, Promocion> mapaPromociones = ServicioPromociones.devuelveMapaPromociones(promocionesIds);

        /*for (PedidoOnlineDTO pedidoOnlineDTO : peticionPlanFinanciamientoDTO.getPedidos()) {
            //G.S. Agrega la entrega a domicilio
            if (pedidoOnlineDTO.getEntregaDomicilio() != null && pedidoOnlineDTO.getEntregaDomicilio()) {
                pedidoOnlineDTO.getItems().add(agregaEntregaDomicilio(pedidoOnlineDTO, (long) pedidoOnlineDTO.getItems().size() + 1));
            }
        }*/
        //G.S. Para el banco dinners se agregan los medios de pago de pichincha, pero se excluye a Xperta
        for (MedioPagoBean medioPago : list) {
            if (!medioPago.isTarjetaSukasa() && medioPago.getInfoExtra1() == null
                    && medioPago.getCodBan() != null && peticionPlanFinanciamientoDTO.getCodBan().equals(medioPago.getCodBan())
                    || (peticionPlanFinanciamientoDTO.getCodBan().equals("6") && "1".equals(medioPago.getCodBan())
                    && !medioPago.getCodMedioPago().equals("241"))) {
                for (PedidoOnlineDTO pedidoOnlineDTO : peticionPlanFinanciamientoDTO.getPedidos()) {

                    asignarArticulosItem(pedidoOnlineDTO);

                    BigDecimal totalGarantiaExtendida = obtenerValorGarantiaExtendida(pedidoOnlineDTO);

                    List<PlanPagoDTO> listaPlanes = new ArrayList<>();

                    // ************* Agrega los planes de financiamiento ********************** //
                    for (VencimientoBean vencimientoBean : medioPago.getPlanes()) {

                        List<ItemOnlineDTO> items = obtenerItemsParaPromocion(pedidoOnlineDTO, vencimientoBean, mapaPromociones);
                        BigDecimal totalConPromocion = obtenerValorTotal(items);
                        PlanPagoDTO planPromo = obtenerPlanUnicoPago(medioPago, totalConPromocion, totalGarantiaExtendida, "00",
                                Boolean.TRUE, vencimientoBean.getIdMedioPagoVencimiento());
                        planPromo.setItems(items);
                        if (planPromo.getCuota().compareTo(CUOTA_MINIMA) >= 0 || planPromo.getNumCuotas() == 1L) {
                            listaPlanes.add(0, planPromo);
                        }

                    }

                    // ************* Coloca los valores correspondientes en los itemas  ********************** //
                    asignarValoresItem(listaPlanes, pedidoOnlineDTO, medioPago.getCodMedioPago());

                    MedioPagoDTO medioDTO = new MedioPagoDTO();
                    medioDTO.setCodMedPag(medioPago.getCodMedioPago());
                    medioDTO.setDescripcionMedioPago(medioPago.getDesMedioPago());
                    medioDTO.setPlanes(listaPlanes);
                    medioDTO.setTarjetaComohogar(medioPago.getTarjetaSukasa());
                    mediosPagos.add(medioDTO);
                }
            }
        }

        return mediosPagos;
    }

    @Override
    public List<MedioPagoDTO> obtenerMediosPagoByMarcaTarjeta(PeticionPlanFinanciamientoDTO peticionPlanFinanciamientoDTO) throws Exception {

        List<MedioPagoDTO> mediosPagos = new ArrayList<>();

        MediosPago mediosPago = new MediosPago(new HashMap<String, MedioPagoBean>());
        mediosPago.inicializaMediosPago(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
        List<MedioPagoBean> list = mediosPago.getListaMediosPago(4L, MedioPagoBean.TIPO_TARJETAS);
        List<Long> promocionesIds = new ArrayList<>();
        for (PedidoOnlineDTO pedido : peticionPlanFinanciamientoDTO.getPedidos()) {
            for (ItemOnlineDTO item : pedido.getItems()) {
                if (item.getIdPromocion() != null) {
                    if (!promocionesIds.contains(item.getIdPromocion())) {
                        promocionesIds.add(item.getIdPromocion());
                    }
                }
            }
        }

        Map<Long, Promocion> mapaPromociones = ServicioPromociones.devuelveMapaPromociones(promocionesIds);

        /*for (PedidoOnlineDTO pedidoOnlineDTO : peticionPlanFinanciamientoDTO.getPedidos()) {
            //G.S. Agrega la entrega a domicilio
            if (pedidoOnlineDTO.getEntregaDomicilio() != null && pedidoOnlineDTO.getEntregaDomicilio()) {
                pedidoOnlineDTO.getItems().add(agregaEntregaDomicilio(pedidoOnlineDTO, (long) pedidoOnlineDTO.getItems().size() + 1));
            }
        }*/
        MarcaTarjetaServices marcaTarjetaServices = new MarcaTarjetaServices();
        List<MarcaTarjetaMedioPagoBean> marcaTarjetaMedioPagoBeans = marcaTarjetaServices.consultarMarcasTarjeta(peticionPlanFinanciamientoDTO.getCodMarcaTarjeta());

        for (MarcaTarjetaMedioPagoBean marcaTarjetaMedioPagoBean : marcaTarjetaMedioPagoBeans) {
            for (MedioPagoBean medioPago : list) {
                if (marcaTarjetaMedioPagoBean.getCodMedPag().equals(medioPago.getCodMedioPago()) && visualizaMediosPago(medioPago)) {
                    for (PedidoOnlineDTO pedidoOnlineDTO : peticionPlanFinanciamientoDTO.getPedidos()) {

                        asignarArticulosItem(pedidoOnlineDTO);

                        //Si el medio de pago es SK Gold no debe cobrar el EGO
                        BigDecimal totalGarantiaExtendida = Constantes.MEDIO_PAGO_SK_GOLD.equals(medioPago.getCodMedioPago()) ? BigDecimal.ZERO : obtenerValorGarantiaExtendida(pedidoOnlineDTO);

                        List<PlanPagoDTO> listaPlanes = new ArrayList<>();

                        // ************* Agrega los planes de financiamiento ********************** //
                        for (VencimientoBean vencimientoBean : medioPago.getPlanes()) {

                            List<ItemOnlineDTO> items = obtenerItemsParaPromocion(pedidoOnlineDTO, vencimientoBean, mapaPromociones);
                            BigDecimal totalConPromocion = obtenerValorTotal(items);
                            PlanPagoDTO planPromo = obtenerPlanUnicoPago(medioPago, totalConPromocion, totalGarantiaExtendida, "00",
                                    Boolean.TRUE, vencimientoBean.getIdMedioPagoVencimiento());
                            planPromo.setItems(items);
                            if (planPromo.getCuota().compareTo(CUOTA_MINIMA) >= 0 || planPromo.getNumCuotas() == 1L) {
                                listaPlanes.add(0, planPromo);
                            }

                        }

                        // ************* Coloca los valores correspondientes en los itemas  ********************** //
                        asignarValoresItem(listaPlanes, pedidoOnlineDTO, medioPago.getCodMedioPago());

                        MedioPagoDTO medioDTO = new MedioPagoDTO();
                        medioDTO.setCodMedPag(medioPago.getCodMedioPago());
                        medioDTO.setDescripcionMedioPago(marcaTarjetaMedioPagoBean.getDescripcion());
                        medioDTO.setPlanes(listaPlanes);
                        medioDTO.setTarjetaComohogar(medioPago.getTarjetaSukasa());
                        mediosPagos.add(medioDTO);
                    }
                    break;
                }
            }
        }

        return mediosPagos;
    }

    /**
     *
     * @param listaPlanes
     * @param pedidoOnlineDTO
     * @throws CloneNotSupportedException
     */
    private void asignarValoresItem(List<PlanPagoDTO> listaPlanes, PedidoOnlineDTO pedidoOnlineDTO, String codMedPag) throws CloneNotSupportedException {

        for (PlanPagoDTO plan : listaPlanes) {

            List<ItemOnlineDTO> itemsPorPlan = new ArrayList<>();
            BigDecimal otrosDescuentos = BigDecimal.ZERO;
            //Asigna los valores aplicando los descuentos correspondientes
            for (ItemOnlineDTO itemDTONew : plan.getItems()) {

                BigDecimal precioAfiliado = Numero.menosPorcentajeR4(itemDTONew.getItmPvpUnitario().setScale(2, BigDecimal.ROUND_HALF_UP), plan.getPorcentajeDescuento());
                BigDecimal precioDescuento = Numero.menosPorcentajeR4(precioAfiliado, itemDTONew.getPorcentajeDescuento() != null ? itemDTONew.getPorcentajeDescuento() : BigDecimal.ZERO);
                if (itemDTONew.getPorcentajeDescuento() != null && itemDTONew.getPorcentajeDescuento().compareTo(BigDecimal.ZERO) > 0) {
                    otrosDescuentos = otrosDescuentos.add(Numero.porcentaje(itemDTONew.getItmPvpUnitario(), itemDTONew.getPorcentajeDescuento()).multiply(new BigDecimal(itemDTONew.getItmCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
                }

                itemDTONew.setItmCobraIva(itemDTONew.getArticulo().isArticuloTieneIva());
                BigDecimal precioTotal = precioDescuento.multiply(new BigDecimal(itemDTONew.getItmCantidad()));
                if (BigDecimal.ZERO.compareTo(precioTotal) == 0) {
                    precioAfiliado = BigDecimal.ZERO;
                }
                itemDTONew.setItmPrecioFinanciamiento(precioAfiliado.setScale(2, BigDecimal.ROUND_HALF_UP));
                itemDTONew.setItmPrecioTotal(precioTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
                itemsPorPlan.add(itemDTONew);
            }

            List<PedidoOnlineDTO> pedidosPorMedio = new ArrayList<>();
            PedidoOnlineDTO pedido = pedidoOnlineDTO.clone();
            pedido.setItems(itemsPorPlan);
            pedidosPorMedio.add(pedido);
            plan.setDescuentosPromocion(otrosDescuentos);

            PlanPagoDTO planPedido = plan.clone();
            pedido.setPlanPedido(planPedido);
            plan.setPedidos(pedidosPorMedio);
            ajustarDescuadre(itemsPorPlan, plan.getaPagar());
            pedido.setSubtotales(generarSubtotales(planPedido, itemsPorPlan));

            //Colocar los valores correspondientes para el EGO
            for (ItemOnlineDTO itemDTO : itemsPorPlan) {
                if (EnumTipoItem.EGO.equals(itemDTO.getTipoItem()) && !Constantes.MEDIO_PAGO_SK_GOLD.equals(codMedPag)) {
                    itemDTO.setItmPrecioFinanciamiento(itemDTO.getItmPvpUnitario());
                    itemDTO.setItmPrecioTotal(itemDTO.getItmPrecioFinanciamiento().multiply(new BigDecimal(itemDTO.getItmCantidad())));
                }
            }
        }
    }

    /**
     *
     * @param pedidoOnlineDTO
     * @param vencimientoBean
     * @param mapaPromociones
     * @return
     * @throws CloneNotSupportedException
     */
    private List<ItemOnlineDTO> obtenerItemsParaPromocion(PedidoOnlineDTO pedidoOnlineDTO, VencimientoBean vencimientoBean,
            Map<Long, Promocion> mapaPromociones) throws CloneNotSupportedException {

        List<ItemOnlineDTO> items = new ArrayList<>();

        for (ItemOnlineDTO itemOriginal : pedidoOnlineDTO.getItems()) {
            ItemOnlineDTO item = itemOriginal.clone();
            BigDecimal porcentajeDescuento = itemOriginal.getPorcentajeDescuento();
            Long idPromocion = itemOriginal.getIdPromocion();
            String observacionPromocion = null;

            if (idPromocion != null && idPromocion > 0L) {

                Promocion promocion = mapaPromociones.get(item.getIdPromocion() != null ? item.getIdPromocion() : 0L);
                //Promocion promocion = Sesion.mapaPromociones.get(item.getIdPromocion() != null ? item.getIdPromocion() : 0L);
                LineasTicket lineas = new LineasTicket();
                List<LineaTicket> lineaTickets = new ArrayList<>();

                Articulos articulos = item.getArticulo();
                LineaTicket lineaTicket = new LineaTicket(StringUtils.EMPTY, articulos, itemOriginal.getItmCantidad().intValue(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                lineaTickets.add(lineaTicket);
                lineas.setLineas(lineaTickets);

                if (promocion != null) {
                    List<Long> vencimientos = promocion.getVencimientos();
                    if (!promocion.isAplicableALineas(lineas)) {
                        porcentajeDescuento = BigDecimal.ZERO;
                        observacionPromocion = "El \u00EDtem no corresponde a la promoci\u00F3n  ";
                        idPromocion = null;
                    } else if (vencimientos != null && !vencimientos.isEmpty()) {
                        if (vencimientos.contains(vencimientoBean.getIdMedioPagoVencimiento())) {
                            porcentajeDescuento = item.getPorcentajeDescuento();
                            idPromocion = itemOriginal.getIdPromocion();
                            observacionPromocion = promocion.getDescripcion();
                        } else {
                            porcentajeDescuento = BigDecimal.ZERO;
                            observacionPromocion = "La promoci\u00F3n no cumple con el plan de finaciamiento ";
                            idPromocion = null;
                        }
                    }
                } else {
                    porcentajeDescuento = BigDecimal.ZERO;
                    idPromocion = null;
                    observacionPromocion = "La promoci\u00F3n no esta vigente ";
                }
            }

            // Para los items de EGO y ENTREGA A DOMICILIO deben tener 100% de descuento
            // cuando el pago sea con sukasa GOLD
            if (Constantes.MEDIO_PAGO_SK_GOLD.equals(vencimientoBean.getCodMedioPago()) && (EnumTipoItem.ENTREGA_DOMICILIO.equals(item.getTipoItem()) || EnumTipoItem.EGO.equals(item.getTipoItem()))) {
                porcentajeDescuento = new BigDecimal("100");
            }

            item.setPorcentajeDescuento(porcentajeDescuento);
            item.setIdPromocion(idPromocion);
            item.setObservacionPromocion(observacionPromocion);

            items.add(item);

        }
        return items;

    }

    /**
     *
     * @param items
     * @return
     */
    private BigDecimal obtenerValorTotal(List<ItemOnlineDTO> items) {

        BigDecimal totalFactura = BigDecimal.ZERO;
        for (ItemOnlineDTO item : items) {

            BigDecimal pvpConDescuento = Numero.menosPorcentajeR4(item.getItmPvpUnitario(), item.getPorcentajeDescuento() != null ? item.getPorcentajeDescuento() : BigDecimal.ZERO);
            BigDecimal pvpConDescuentoTotal = pvpConDescuento.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(item.getItmCantidad()));
            totalFactura = totalFactura.add(pvpConDescuentoTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return totalFactura.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     *
     * @param pedidoOnlineDTO
     * @return
     * @throws ArticuloNotFoundException
     * @throws ArticuloException
     */
    private BigDecimal obtenerValorGarantiaExtendida(PedidoOnlineDTO pedidoOnlineDTO) throws ArticuloNotFoundException, ArticuloException {

        BigDecimal totalGarantiaExtendida = BigDecimal.ZERO;
        for (ItemOnlineDTO item : pedidoOnlineDTO.getItems()) {

            Articulos articulos = item.getArticulo();

            if (articulos == null) {
                ArticulosServices articulosServices = ArticulosServices.getInstance();
                item.setArticulo(articulosServices.getArticuloCod(StringParser.convertCodigoITocodArt(item.getItmCodigoI())));
                articulos = item.getArticulo();
            }

            if (articulos == null) {
                throw new ArticuloNotFoundException("No existe el item " + item.getItmCodigoI());
            }

            String codBarras = ArticulosServices.consultarCodigoBarras(articulos.getCodart());

            if (Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS).equals(codBarras)) {
                totalGarantiaExtendida = totalGarantiaExtendida.add(item.getItmPvpUnitario().multiply(new BigDecimal(item.getItmCantidad())));
                item.setIsGarantiaExtendida(Boolean.TRUE);
            }
        }
        return totalGarantiaExtendida.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     *
     * @param items
     * @param total
     */
    private void ajustarDescuadre(List<ItemOnlineDTO> items, BigDecimal total) {

        BigDecimal valorAcumulado = BigDecimal.ZERO;

        for (ItemOnlineDTO item : items) {
            valorAcumulado = valorAcumulado.add(item.getItmPrecioTotal());
        }

        if (valorAcumulado.compareTo(total) != 0) {
            ItemOnlineDTO item = items.get(0);
            BigDecimal diferencia = total.subtract(valorAcumulado);
            item.setItmPrecioTotal(item.getItmPrecioTotal().add(diferencia));
        }
    }

    /**
     *
     * @param planPagoDTO
     * @param items
     * @return
     */
    private SubtotalOnlineDTO generarSubtotales(PlanPagoDTO planPagoDTO, List<ItemOnlineDTO> items) {

        SubtotalOnlineDTO subtotalOnlineDTO = new SubtotalOnlineDTO();
        BigDecimal subtotalIva = BigDecimal.ZERO;
        BigDecimal subtotalCero = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;

        subtotalOnlineDTO.setTotal(planPagoDTO.getaPagar());

        for (ItemOnlineDTO itemOnlineDTO : items) {

            if (itemOnlineDTO.getItmCobraIva()) {
                BigDecimal divisor = Constantes.CIEN.add(Sesion.getEmpresa().getPorcentajeIva()).divide(Constantes.CIEN, 6, BigDecimal.ROUND_HALF_UP);
                BigDecimal precioSinIva = itemOnlineDTO.getItmPrecioTotal().divide(divisor, 6, BigDecimal.ROUND_HALF_UP);
                subtotalIva = subtotalIva.add(precioSinIva);
            } else {
                subtotalCero = subtotalCero.add(itemOnlineDTO.getItmPvpUnitario());
            }

        }
        subtotalIva = subtotalIva.setScale(2, BigDecimal.ROUND_HALF_UP);
        subtotalCero = subtotalCero.setScale(2, BigDecimal.ROUND_HALF_UP);
        iva = planPagoDTO.getaPagar().subtract(subtotalIva).subtract(subtotalCero);

        subtotalOnlineDTO.setIva(iva);
        subtotalOnlineDTO.setSubtotalIva(subtotalIva);
        subtotalOnlineDTO.setSubtotalIvaCero(subtotalCero);

        return subtotalOnlineDTO;

    }

    /**
     *
     * @param pedidoOnlineDTO
     * @param idLinea
     * @return
     * @throws ArticuloNotFoundException
     * @throws ArticuloException
     */
    private ItemOnlineDTO agregaEntregaDomicilio(PedidoOnlineDTO pedidoOnlineDTO, Long idLinea) throws ArticuloNotFoundException, ArticuloException {

        BigDecimal totalFactura = obtenerValorTotal(pedidoOnlineDTO.getItems());

        String codArt;
        if (Variables.VALOR_ENTREGA_DOMICILIO_WEB.compareTo(totalFactura) > 0) {
            codArt = Variables.getVariable(Variables.ITEM_ENTREGA_DOMICILIO_WEB_VALOR_1);
        } else {
            codArt = Variables.getVariable(Variables.ITEM_ENTREGA_DOMICILIO_WEB_VALOR_2);
        }

        ArticulosServices articulosServices = ArticulosServices.getInstance();
        TarifasServices tarifasServices = new TarifasServices();

        Articulos articulos = articulosServices.getArticuloCod(codArt);
        Tarifas tarifa = tarifasServices.getTarifaArticulo(codArt);
        String itmCodigoI = StringParser.convertCodArtToCodigoI(codArt);

        ItemOnlineDTO itemOnlineDTO = new ItemOnlineDTO();
        itemOnlineDTO.setItmCodigoI(itmCodigoI);
        itemOnlineDTO.setIdLinea(idLinea);
        itemOnlineDTO.setItmCantidad(1L);
        itemOnlineDTO.setItmCobraIva(articulos.isArticuloTieneIva());
        itemOnlineDTO.setItmPvpUnitario(tarifa.getPrecioTotal());
        itemOnlineDTO.setIdPromocion(null);
        itemOnlineDTO.setArticulo(articulos);

        return itemOnlineDTO;
    }

    @Override
    public List<BancoOnlineDTO> obtenerBancos() throws MedioPagoException {

        BancoServices bancoServices = new BancoServices();

        List<BancoBean> bancos = bancoServices.consultarBancos();
        List<BancoOnlineDTO> bancosList = new ArrayList<>();
        MediosPago mediosPagoTodo = new MediosPago(new HashMap<String, MedioPagoBean>());
        mediosPagoTodo.inicializaMediosPago(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));

        for (BancoBean bancoBean : bancos) {

            BancoOnlineDTO bancoOnlineDTO = new BancoOnlineDTO();
            bancoOnlineDTO.setCodBan(bancoBean.getCodBan());
            bancoOnlineDTO.setNombreBanco(bancoBean.getDesBan().toUpperCase());
            List<MedioPagoDTO> medioPagoList = new ArrayList<>();

            for (MedioPagoBean mediosPagoBean : mediosPagoTodo.getMediosPagoByBanco(bancoBean.getCodBan())) {

                if (!mediosPagoBean.isTarjetaSukasa() && mediosPagoBean.getInfoExtra1() == null) {

                    MedioPagoDTO medioPagoDTO = new MedioPagoDTO();
                    medioPagoDTO.setCodMedPag(mediosPagoBean.getCodMedioPago());
                    medioPagoDTO.setDescripcionMedioPago(mediosPagoBean.getDesMedioPago());
                    medioPagoDTO.setDiferidoMaximo(mediosPagoBean.getPlanes().get(0).getNumeroVencimientos());
                    medioPagoDTO.setBines(mediosPagoBean.getBines());
                    medioPagoDTO.setTarjetaComohogar(mediosPagoBean.getTarjetaSukasa());

                    medioPagoList.add(medioPagoDTO);

                }
            }
            bancoOnlineDTO.setMedioPagos(medioPagoList);

            bancosList.add(bancoOnlineDTO);
        }

        //********* Se obtiene los medios de pago del pichincha excepto Xperta
        List<MedioPagoDTO> medioPagoPichincha = new ArrayList<>();

        for (BancoOnlineDTO bancoDTO : bancosList) {
            if ("1".equals(bancoDTO.getCodBan())) {
                for (MedioPagoDTO medioPago : bancoDTO.getMedioPagos()) {
                    if (!"241".equals(medioPago.getCodMedPag())) {
                        medioPagoPichincha.add(medioPago);
                    }
                }
                break;
            }
        }

        //********* Se aniade los medios de pago a Diners
        for (BancoOnlineDTO bancoDTO : bancosList) {
            if ("6".equals(bancoDTO.getCodBan())) {
                bancoDTO.getMedioPagos().addAll(medioPagoPichincha);
                break;
            }
        }

        return bancosList;
    }

    /**
     *
     * @param pedidoOnlineDTO
     */
    private void asignarArticulosItem(PedidoOnlineDTO pedidoOnlineDTO) {

        for (ItemOnlineDTO itemOnlineDTO : pedidoOnlineDTO.getItems()) {
            String codArt = StringParser.convertCodigoITocodArt(itemOnlineDTO.getItmCodigoI());
            ArticulosServices articulosServices = ArticulosServices.getInstance();
            Articulos articulos = articulosServices.getArticuloCod(codArt);
            itemOnlineDTO.setArticulo(articulos);

        }

    }

    @Override
    public List<MarcaTarjetaOnlineDTO> obtenerMarcasTarjetasMedioPago() throws MedioPagoException {

        List<MarcaTarjetaOnlineDTO> marcaTarjetaList = new ArrayList<>();

        MarcaTarjetaServices marcaTarjetaServices = new MarcaTarjetaServices();

        List<MarcaTarjetaBean> marcas = marcaTarjetaServices.consultarMarcasTarjeta();
        MediosPago mediosPagoTodo = new MediosPago(new HashMap<String, MedioPagoBean>());
        mediosPagoTodo.inicializaMediosPago(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));

        for (MarcaTarjetaBean marcaBean : marcas) {

            MarcaTarjetaOnlineDTO marcaDTO = new MarcaTarjetaOnlineDTO();
            marcaDTO.setCodMarcaTarjeta(marcaBean.getCodMarcaTarjeta());
            marcaDTO.setNombreMarca(marcaBean.getDesMarcaTarjeta());
            List<MedioPagoDTO> medioPagoList = new ArrayList<>();

            for (MarcaTarjetaMedioPagoBean marcaTarjetaMedioPagoBean : mediosPagoTodo.getMediosPagoByMarcaTarjeta(marcaBean.getCodMarcaTarjeta())) {

                MedioPagoBean mediosPagoBean = marcaTarjetaMedioPagoBean.getMedioPagoBean();
                if (mediosPagoBean != null && visualizaMediosPago(mediosPagoBean) ) {

                    MedioPagoDTO medioPagoDTO = new MedioPagoDTO();
                    medioPagoDTO.setCodMedPag(mediosPagoBean.getCodMedioPago());
                    medioPagoDTO.setDescripcionMedioPago(marcaTarjetaMedioPagoBean.getDescripcion());
                    medioPagoDTO.setDiferidoMaximo(mediosPagoBean.getPlanes().get(0).getNumeroVencimientos());
                    medioPagoDTO.setBines(mediosPagoBean.getBines());
                    medioPagoDTO.setTarjetaComohogar(mediosPagoBean.getTarjetaSukasa());

                    medioPagoList.add(medioPagoDTO);

                }
            }
            marcaDTO.setMedioPagos(medioPagoList);

            marcaTarjetaList.add(marcaDTO);
        }

        return marcaTarjetaList;
    }
    
    /**
     * 
     * @param mediosPagoBean
     * @return 
     */
    private boolean visualizaMediosPago( MedioPagoBean mediosPagoBean){
        
        if((mediosPagoBean.isTarjetaSukasa() && mediosPagoBean.getInfoExtra1()!=null) || (!mediosPagoBean.isTarjetaSukasa() && mediosPagoBean.getInfoExtra1()==null)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
        
    }

}
