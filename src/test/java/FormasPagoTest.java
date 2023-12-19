
import com.comerzzia.jpos.dto.ventas.MedioPagoDTO;
import com.comerzzia.jpos.dto.ventas.online.ItemOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.PedidoOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.PeticionPlanFinanciamientoDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.PlanPagoDTO;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServices;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServicesImpl;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.StringParser;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gabriel Simbania
 */
public class FormasPagoTest {

    public static void main(String[] args) {

        String usuario = "Administrador";
        String password = "Sukasa$2017";
        String identificacionCliente = "1718052770";
        BigDecimal totalAPagar = new BigDecimal("509.85");
        BigDecimal totalGarantiaExtendida = BigDecimal.ZERO;

        try {
            ClaseConfiguracionTest.leerConfiguracion();

            //Sesion.iniciaSesion(usuario, password);
            FormaPagoServices formaPagoServices = new FormaPagoServicesImpl();
//            List<MedioPagoDTO> listaFormaPago = formaPagoServices.obtenerFormasPago(4L);
//            MedioPagoDTO medioPagoSeleccionado=null;
//            for(MedioPagoDTO medioPagoDTO: listaFormaPago){
//                if("21".equals(medioPagoDTO.getCodMedPag())){
//                    medioPagoSeleccionado=medioPagoDTO;
//                    break;
//                }
//            }

            List<PedidoOnlineDTO> pedidos = new ArrayList<>();
            List<ItemOnlineDTO> items = new ArrayList<>();

            PedidoOnlineDTO pedidoDTO = new PedidoOnlineDTO();
            pedidoDTO.setIdPedido("123");
            pedidoDTO.setLugId(92L);
            pedidoDTO.setTipo(0);
            pedidoDTO.setItems(items);

            ItemOnlineDTO itemOnlineDTO1 = new ItemOnlineDTO();
            itemOnlineDTO1.setIdLinea(0L);
            itemOnlineDTO1.setItmCantidad(1L);
            itemOnlineDTO1.setItmCobraIva(Boolean.TRUE);
            itemOnlineDTO1.setItmCodigoI("788-920");
            itemOnlineDTO1.setItmPvpUnitario(new BigDecimal("688.99"));
            itemOnlineDTO1.setPorcentajeDescuento(new BigDecimal("35"));

            ItemOnlineDTO itemOnlineDTO2 = new ItemOnlineDTO();
            itemOnlineDTO2.setIdLinea(1L);
            itemOnlineDTO2.setItmCantidad(1L);
            itemOnlineDTO2.setItmCobraIva(Boolean.TRUE);
            itemOnlineDTO2.setItmCodigoI("2581-1");
            itemOnlineDTO2.setItmPvpUnitario(new BigDecimal("62.01"));
            itemOnlineDTO2.setIdLineaReferencia(0L);

            items.add(itemOnlineDTO1);
            items.add(itemOnlineDTO2);

            pedidos.add(pedidoDTO);

            PeticionPlanFinanciamientoDTO peticionPlanFinanciamientoDTO = new PeticionPlanFinanciamientoDTO();
            peticionPlanFinanciamientoDTO.setEsAfiliado(Boolean.TRUE);
            peticionPlanFinanciamientoDTO.setIdentificacion(identificacionCliente);
            peticionPlanFinanciamientoDTO.setPedidos(pedidos);

            StringParser.convertCodigoITocodArt(password);

            ArticulosServices articulosServices = ArticulosServices.getInstance();
            Articulos articulos = articulosServices.getArticuloCod(StringParser.convertCodigoITocodArt(itemOnlineDTO2.getItmCodigoI()));

            String codBarras = ArticulosServices.consultarCodigoBarras(articulos.getCodart());

            if (Variables.getVariable(Variables.GARANTIA_EXT_CODBARRAS).equals(codBarras)) {
                totalGarantiaExtendida = totalGarantiaExtendida.add(itemOnlineDTO2.getItmPvpUnitario());
            }

            System.out.println("Inicio " + new Date());

            MediosPago mediosPago = new MediosPago(new HashMap<String, MedioPagoBean>());
            mediosPago.inicializaMediosPago(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            //List<MedioPagoDTO> mediosPagoDTO = formaPagoServices.obtenerFormasPago(4L);

            List<MedioPagoDTO> mediosPagos = new ArrayList<>();
            List<MedioPagoBean> list = mediosPago.getListaMediosPago(4L, MedioPagoBean.TIPO_TARJETAS);

            for (MedioPagoBean medioPago : list) {
                if (!medioPago.isTarjetaSukasa() && medioPago.getInfoExtra1() == null) {
                    System.out.println("********************** INGRESO EL MEDIO DE PAGO " + medioPago.getDesMedioPago());
                    List<PlanPagoDTO> listaPlanes = formaPagoServices.obtenerPlanPago(medioPago, totalAPagar, totalGarantiaExtendida, identificacionCliente, true);

                    for (PlanPagoDTO plan : listaPlanes) {

                        List<ItemOnlineDTO> itemsPorPlan = new ArrayList<>();

                        ItemOnlineDTO itemOnlineDTONew1 = itemOnlineDTO1.clone();
                        ItemOnlineDTO itemOnlineDTONew2 = itemOnlineDTO2.clone();

                        BigDecimal pvpDescuento = Numero.menosPorcentajeR4(itemOnlineDTONew1.getItmPvpUnitario(), itemOnlineDTONew1.getPorcentajeDescuento());

                        BigDecimal pvpAfiliado = Numero.menosPorcentajeR4(pvpDescuento.setScale(2, BigDecimal.ROUND_HALF_UP), plan.getPorcentajeDescuento());
                        itemOnlineDTONew1.setItmPvpUnitario(pvpAfiliado.setScale(2, BigDecimal.ROUND_HALF_UP));

                        BigDecimal pvpAfiliado2 = Numero.menosPorcentajeR4(itemOnlineDTONew2.getItmPvpUnitario(), plan.getPorcentajeDescuento());
                        itemOnlineDTONew2.setItmPvpUnitario(pvpAfiliado2.setScale(2, BigDecimal.ROUND_HALF_UP));

                        itemsPorPlan.add(itemOnlineDTONew1);
                        itemsPorPlan.add(itemOnlineDTONew2);

                        List<PedidoOnlineDTO> pedidosPorMedio = new ArrayList<>();
                        PedidoOnlineDTO pedido = pedidoDTO.clone();
                        pedido.setItems(itemsPorPlan);
                        pedidosPorMedio.add(pedido);

                        PlanPagoDTO planPedido = plan.clone();
                        pedido.setPlanPedido(planPedido);
                        plan.setPedidos(pedidosPorMedio);
                        //System.out.println(p.getIdPlan() + " - " + p.getPlan() + " - " + p.getPorcentajeDescuento() + " - " + p.getNumCuotas() + "*" + p.getCuota() + "= " + p.getaPagar() + " - " + p.getAhorro() + " - " + p.getPorcentajeInteres() + " - " + p.getImporteInteres());
                    }
                    MedioPagoDTO medioDTO = new MedioPagoDTO();
                    medioDTO.setCodMedPag(medioPago.getCodMedioPago());
                    medioDTO.setDescripcionMedioPago(medioPago.getDesMedioPago());
                    medioDTO.setPlanes(listaPlanes);
                    mediosPagos.add(medioDTO);
                }
            }
            System.out.println("Fin de respuesta " + new Date());
            System.out.println(JsonUtil.objectToJson(mediosPagos));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
