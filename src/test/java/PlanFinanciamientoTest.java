
import com.comerzzia.jpos.dto.ventas.MedioPagoDTO;
import com.comerzzia.jpos.dto.ventas.online.ItemOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.PedidoOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.PeticionPlanFinanciamientoDTO;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServices;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServicesImpl;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.StringParser;
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
public class PlanFinanciamientoTest {

    public static void main(String[] args) {

        String usuario = "Administrador";
        String password = "Sukasa$2017";
        String identificacionCliente = "1720431038";

        try {
            ClaseConfiguracionTest.leerConfiguracion();

            //Sesion.iniciaSesion(usuario, password);
            FormaPagoServices formaPagoServices = new FormaPagoServicesImpl();

            List<PedidoOnlineDTO> pedidos = new ArrayList<>();
            List<ItemOnlineDTO> items = new ArrayList<>();

            PedidoOnlineDTO pedidoDTO = new PedidoOnlineDTO();
            pedidoDTO.setIdPedido("identificacionCliente");
            pedidoDTO.setLugId(92L);
            pedidoDTO.setTipo(0);
            pedidoDTO.setEntregaDomicilio(Boolean.TRUE);
            pedidoDTO.setItems(items);

            ItemOnlineDTO itemOnlineDTO1 = new ItemOnlineDTO();
            itemOnlineDTO1.setIdLinea(0L);
            itemOnlineDTO1.setItmCantidad(1L);
            itemOnlineDTO1.setItmCobraIva(Boolean.TRUE);
            itemOnlineDTO1.setItmCodigoI("788-920");
            itemOnlineDTO1.setItmPvpUnitario(new BigDecimal("688.99"));
            //itemOnlineDTO1.setPorcentajeDescuento(new BigDecimal("35"));

            items.add(itemOnlineDTO1);

            pedidos.add(pedidoDTO);

            PeticionPlanFinanciamientoDTO peticionPlanFinanciamientoDTO = new PeticionPlanFinanciamientoDTO();
            peticionPlanFinanciamientoDTO.setEsAfiliado(Boolean.TRUE);
            peticionPlanFinanciamientoDTO.setIdentificacion(identificacionCliente);
            peticionPlanFinanciamientoDTO.setCodMarcaTarjeta("9");
            peticionPlanFinanciamientoDTO.setPedidos(pedidos);

            StringParser.convertCodigoITocodArt(password);

//            ArticulosServices articulosServices = ArticulosServices.getInstance();
//            Articulos articulos = articulosServices.getArticuloCod(StringParser.convertCodigoITocodArt(itemOnlineDTO2.getItmCodigoI()));


            System.out.println("Inicio " + new Date());

            MediosPago mediosPago = new MediosPago(new HashMap<String, MedioPagoBean>());
            mediosPago.inicializaMediosPago(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            //List<MedioPagoDTO> mediosPagoDTO = formaPagoServices.obtenerFormasPago(4L);

            List<MedioPagoDTO> mediosPagos = formaPagoServices.obtenerMediosPagoByMarcaTarjeta(peticionPlanFinanciamientoDTO);

            System.out.println("Fin de respuesta " + new Date());
            System.out.println(JsonUtil.objectToJson(mediosPagos));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
