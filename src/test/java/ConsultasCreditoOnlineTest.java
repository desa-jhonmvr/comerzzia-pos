
import com.comerzzia.jpos.dto.credito.DatosCreditoDTO;
import com.comerzzia.jpos.entity.services.credito.CreditoDirectoServices;
import com.comerzzia.jpos.entity.services.credito.CreditoDirectoServicesImpl;
import com.comerzzia.jpos.util.JsonUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gabriel Simbania
 */
public class ConsultasCreditoOnlineTest {

     public static void main(String[] args) {

        String identificacion = "1713829226";

        try {
            ClaseConfiguracionTest.leerConfiguracion();

            System.out.println("Consultando la: "+identificacion);
            CreditoDirectoServices creditoDirectoServices = new CreditoDirectoServicesImpl();
            DatosCreditoDTO datos = creditoDirectoServices.consultaDatosByIdentificacion(identificacion);

            System.out.println(JsonUtil.objectToJson(datos));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
