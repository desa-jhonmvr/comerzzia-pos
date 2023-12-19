
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.servicios.login.LecturaConfiguracion;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.servicios.tickets.xml.TicketXMLServices;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentNode;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gabriel Simbania
 */
public class CrearVoucherTest {

    public static void main(String[] args) {

        String uidTicket = "73fdafbe-8f9c-42a1-b130-664965332bfb";
        String uidDocumento = "c3001d04-0029-4110-b400-d081ffa7d060";
        String usuario = "Administrador";
        String password = "Sukasa$2017";

        try {
            // La carga de datos puede ser realizada también en el constructor de datos coorporativos
            ClaseConfiguracionTest.leerConfiguracion();

            Sesion.iniciaSesion(usuario,password );

            // Leer los datos de sesión de la base de datos
            LecturaConfiguracion.leerDatosSesion(true);

            PrintServices ts = PrintServices.getInstance();
            TicketsAlm ticketsAlm = TicketService.consultarTicket(uidTicket);
            byte[] xmlTicket = ticketsAlm.getXMLTicket();
            TicketOrigen ticketOrigen = TicketOrigen.getTicketOrigen(new XMLDocument(xmlTicket));
            XMLDocumentNode node = ticketOrigen.getPagos();
            TicketS ticket = new TicketS(false);
            ticket.setUid_ticket(uidTicket);

           PagosTicket pagosTicket = TicketXMLServices.construirPagoByTagPagoCredito(node, ticket);
            if (!pagosTicket.getPagos().isEmpty()){
                // "035-012-00000478"
                ts.imprimirVouchers(pagosTicket, ticketOrigen.getIdFactura(), "VENTA");
            }
            
             //Registrar en la base de datos
             DocumentosBean doc = new DocumentosBean();
             doc.setUidDocumento(uidDocumento);
             doc.setImpresos(ts.getDocumentosImpresos());
             DocumentosService.crearDocumentoImpreso(doc, DocumentosImpresosBean.TIPO_PAGO);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

}
