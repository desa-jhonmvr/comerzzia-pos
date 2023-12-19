
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.persistencia.promociones.TipoPromocionBean;
import com.comerzzia.jpos.servicios.login.LecturaConfiguracion;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import es.mpsistemas.util.log.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gabriel Simbania
 */
public class CrearDocImpCuponesTest {

    protected static Logger log = Logger.getMLogger(CrearDocImpCuponesTest.class);
    protected static String modoReserva=null;

    public static void main(String[] args) {

        try {
            // La carga de datos puede ser realizada también en el constructor de datos coorporativos
            ClaseConfiguracionTest.leerConfiguracion();

            // Leer los datos de sesión de la base de datos
            LecturaConfiguracion.leerDatosSesion(true);
            
            String codCliente = "1705276333";
            String fechaFactura = "27-oct-2023 15:26";
            String nombreCupon = "Alfredo Adonay Jimenez Yepez";
            String direccionCupon = "Charles Darwin Y Pedro Rodeno";
            String fechaFacturaEnCupon = "27 de octubre de 2023";
            String codigoAlmacen = "001";
            String fechaValidez = "04-ene-2024 06:00";
            String uidTicket = "a63e6dd8-c2b1-448e-b5ea-cec366f8905f";
            String numeroFactura = "001-113-000032469";
            String tipoReferenciaOrigen = "FACTURA_VENTA";
            String uidDocumentoImpreso = "a9998aa4-11a3-45a1-8d2f-6ced73a659a6";
            Long tipoPromocionBean = TipoPromocionBean.TIPO_PROMOCION_CUPON_SORTEO_SUKASA;
            String tipoDocumentoImpreso = DocumentosImpresosBean.TIPO_CUPON;
            Long idPromocion = 5246L;
            int numeroCupones = 2;
            
            DocumentosService.reconstruirCupones(codCliente,
                    fechaFactura, nombreCupon, direccionCupon, fechaFacturaEnCupon, codigoAlmacen,
                    fechaValidez, uidTicket, numeroFactura, tipoReferenciaOrigen, uidDocumentoImpreso,
                    tipoPromocionBean, tipoDocumentoImpreso, idPromocion, numeroCupones);

            System.exit(0);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
