
import com.comerzzia.jpos.dto.ventas.MedioPagoDTO;
import com.comerzzia.jpos.dto.ventas.online.ItemOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.PedidoOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.PeticionPlanFinanciamientoDTO;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServices;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServicesImpl;
import com.comerzzia.jpos.util.JsonUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
public class MedioPagoOnlineTest {

    public static void main(String[] args) {

        String usuario = "Administrador";
        String password = "Sukasa$2017";
        String identificacionCliente = "1718052770";
        BigDecimal totalAPagar = new BigDecimal("509.85");
        BigDecimal totalGarantiaExtendida = BigDecimal.ZERO;

        try {
            ClaseConfiguracionTest.leerConfiguracion();

            FormaPagoServices formaPagoServices = new FormaPagoServicesImpl();
            //Sesion.iniciaSesion(usuario, password);

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

            System.out.println(JsonUtil.objectToJson(peticionPlanFinanciamientoDTO));

            System.out.println("**************** Ingreso de respuesta " + new Date());

            List<MedioPagoDTO> mediosPagos = formaPagoServices.obtenerMediosPago(peticionPlanFinanciamientoDTO);

            System.out.println("**************** Fin de respuesta " + new Date());
            System.out.println(JsonUtil.objectToJson(mediosPagos));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
