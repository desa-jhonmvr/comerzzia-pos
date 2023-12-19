
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.util.ClaveAccesoSri;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gabriel Simbania
 */
public class ClaveAccesoTest {

    public static void main(String[] args) {

        String sDate1 = "09/06/2021";
        Date fechaEmision;
        try {
            
            PagoCredito pagoCredito = new PagoCredito();
            pagoCredito.setMensajePromocional(TicketService.VOUCHER_MANUAL);
            if(!TicketService.VOUCHER_MANUAL.equals(pagoCredito.getMensajePromocional())){
                System.out.println("Voucher normal ");
            }else{
                System.out.println("Voucher manual ");
            }
            
            long time = new Date().getTime() / 1000;
            System.out.println("Hora " +time);
            fechaEmision = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);

            String claveAcceso = ClaveAccesoSri.generaClave(fechaEmision, "04", "1790746119001", "2",
                    "030" + "012", String.format("%09d", 1512), "17907461", "1");

            System.out.println(claveAcceso);
        } catch (ParseException ex) {
            Logger.getLogger(ClaveAccesoTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
