
import com.comerzzia.jpos.dto.ventas.BancoOnlineDTO;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServices;
import com.comerzzia.jpos.entity.services.pagos.FormaPagoServicesImpl;
import com.comerzzia.jpos.util.JsonUtil;
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
public class BancosOnlineTest {

    public static void main(String[] args) {

        try {
            ClaseConfiguracionTest.leerConfiguracion();

            FormaPagoServices formaPagoServices = new FormaPagoServicesImpl();
            //Sesion.iniciaSesion(usuario, password);

            System.out.println("**************** Ingreso de respuesta " + new Date());

            List<BancoOnlineDTO> bancos = formaPagoServices.obtenerBancos();

            System.out.println("**************** Fin de respuesta " + new Date());
            System.out.println(JsonUtil.objectToJson(bancos));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
