import com.comerzzia.jpos.dto.ResponseDTO;
import com.comerzzia.jpos.dto.ventas.MedioPagoDTO;
import com.comerzzia.jpos.dto.ventas.online.ItemOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.PedidoOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.PeticionPlanFinanciamientoDTO;
import com.comerzzia.jpos.dto.ventas.online.VentaOnlineDTO;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServices;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServicesImpl;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.tickets.TicketService;
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
public class VentaOnlineTest {

    public static void main(String[] args) {

        String usuario = "Administrador";
        String password = "Sukasa$2017";
        String identificacionCliente = "1720431038";

        try {
            ClaseConfiguracionTest.leerConfiguracion();

            String str = "{\n" +
                    "\t\"idPedido\": \"8cdb8e99-b9ca-4699-8746-de906eeb6ff3\",\n" +
                    "\t\"referenciaPedido\": \"1-000000015\",\n" +
                    "\t\"cliente\": {\n" +
                    "\t\t\"identificacion\": \"0916481310\",\n" +
                    "\t\t\"tipoIdentificacion\": \"CED\",\n" +
                    "\t\t\"nombres\": \"ROXANA \",\n" +
                    "\t\t\"apellidos\": \"NAVARRETE\",\n" +
                    "\t\t\"numeroCelular\": \"0993623714\",\n" +
                    "\t\t\"email\": \"rnavarrete@sukasa.com\",\n" +
                    "\t\t\"direccion\": \"Urb. La Joya etapa Perla mz. 1 v. 1\",\n" +
                    "\t\t\"provincia\": \"WEB\"\n" +
                    "\t},\n" +
                    "\t\"datosFacturacion\": {\n" +
                    "\t\t\"identificacion\": \"0916481310\",\n" +
                    "\t\t\"tipoIdentificacion\": \"CED\",\n" +
                    "\t\t\"nombres\": \"ROXANA \",\n" +
                    "\t\t\"apellidos\": \"NAVARRETE\",\n" +
                    "\t\t\"numeroCelular\": \"0993623714\",\n" +
                    "\t\t\"email\": \"rnavarrete@sukasa.com\",\n" +
                    "\t\t\"direccion\": \"Urb. La Joya etapa Perla mz. 1 v. 1\"\n" +
                    "\t},\n" +
                    "\t\"lugId\": 92,\n" +
                    "\t\"items\": [\n" +
                    "\t\t{\n" +
                    "\t\t\t\"idLinea\": 1,\n" +
                    "\t\t\t\"itmCodigoI\": \"788-920\",\n" +
                    "\t\t\t\"itmCantidad\": 1,\n" +
                    "\t\t\t\"itmCobraIva\": true,\n" +
                    "\t\t\t\"itmPrecioFinanciamiento\": 620.09,\n" +
                    "\t\t\t\"itmPvpUnitario\": 688.99,\n" +
                    "\t\t\t\"itmPrecioTotal\": 620.09,\n" +
                    "\t\t\t\"porcentajeDescuento\": 0,\n" +
                    "\t\t\t\"idPromocion\": 0,\n" +
                    "\t\t\t\"idLineaReferencia\": 0,\n" +
                    "\t\t\t\"tipoItem\": \"NORMAL\"\n" +
                    "\t\t}\n" +
                    "\t],\n" +
                    "\t\"formasPago\": [\n" +
                    "\t\t{\n" +
                    "\t\t\t\"tipoPago\": 0,\n" +
                    "\t\t\t\"numeroTarjeta\": \"40400XXXXXX48047\",\n" +
                    "\t\t\t\"idPlan\": \"1276\",\n" +
                    "\t\t\t\"porcentajeDescuento\": 10,\n" +
                    "\t\t\t\"ahorro\": 68.90,\n" +
                    "\t\t\t\"aPagar\": 620.09,\n" +
                    "\t\t\t\"cuota\": 212.06,\n" +
                    "\t\t\t\"numCuotas\": 3,\n" +
                    "\t\t\t\"subtotalIvaCero\": 0.0,\n" +
                    "\t\t\t\"subtotalIva\": 553.65,\n" +
                    "\t\t\t\"iva\": 66.44,\n" +
                    "\t\t\t\"porcentajeInteres\": 15.5,\n" +
                    "\t\t\t\"importeInteres\": 16.09,\n" +
                    "\t\t\t\"referenciaAutorizacion\": \"Pago - Syscard\",\n" +
                    "\t\t\t\"nReferenciaAutorizacion\": \"2212202315\",\n" +
                    "\t\t\t\"lote\": \"2212202315\",\n" +
                    "\t\t\t\"referencia\": \"2212202315\"\n" +
                    "\t\t}\n" +
                    "\t],\n" +
                    "\t\"direccionEnvio\": {\n" +
                    "\t\t\"provincia\": \"GUAYAS\",\n" +
                    "\t\t\"localidad\": \"DAULE\",\n" +
                    "\t\t\"ubigeo\": \"090601\",\n" +
                    "\t\t\"callePrincipal\": \"URB. LA JOYA ETAPA PERLA MZ. 1 V. 1\",\n" +
                    "\t\t\"numeracion\": \"\",\n" +
                    "\t\t\"calleSecundaria\": \"\",\n" +
                    "\t\t\"referencia\": \"urb la joya la aurora \",\n" +
                    "\t\t\"nombreContacto\": \"ROXANA NAVARRETE\",\n" +
                    "\t\t\"identificacionContacto\": \"0916481310\",\n" +
                    "\t\t\"telefonoContacto\": \"0993623714\"\n" +
                    "\t}\n" +
                    "}";


            try {
                VentaOnlineDTO ventaOnlineDTO = JsonUtil.jsonToObject(str, VentaOnlineDTO.class);
                String numFactura = TicketService.crearFacturaVentaLineaPos(ventaOnlineDTO);
                System.out.println("numFactura:"+numFactura);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(0);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }

    }
}
