
import com.comerzzia.jpos.entity.db.SriCaja;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasServices;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.credito.supermaxi.CreditoSupermaxiServices;
import com.comerzzia.jpos.servicios.login.DatosConfiguracion;
import com.comerzzia.jpos.servicios.login.DatosDatabase;
import com.comerzzia.jpos.servicios.login.LecturaConfiguracion;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.util.Constantes;
import es.mpsistemas.util.log.Logger;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.persistence.EntityManagerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gabriel Simbania
 */
public class CrearDocumentosFacturaTest {

    protected static Logger log = Logger.getMLogger(CrearDocumentosFacturaTest.class);
    protected static String modoReserva = null;

    public static void main(String[] args) {

        try {
            // La carga de datos puede ser realizada también en el constructor de datos coorporativos
            ClaseConfiguracionTest.leerConfiguracion();

            // Leer los datos de sesión de la base de datos
            LecturaConfiguracion.leerDatosSesion(true);
            
            BigDecimal saldo = CreditoSupermaxiServices.consultaSaldoBonoSupermaxi("2200011520478000");
            System.out.println("Saldo bono "+saldo);
            
            System.exit(0);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
