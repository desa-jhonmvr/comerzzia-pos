/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.entity.db.ConfigImpPorcentaje;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.devoluciones.Devolucion;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.pagos.credito.PlanPagoCredito;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoGiftCard;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.tickets.xml.TagTicketXML;
import com.comerzzia.jpos.util.StringParser;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import es.mpsistemas.util.xml.XMLDocumentNodeNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author MGRI
 */
public class PrintNotaCredito extends PrintDocument {

    private static Logger log = Logger.getMLogger(PrintNotaCredito.class);
    private String direccionEmpresa; // multilínea
    private String numeroTransaccion;
    private String hora;
    private String nombreCliente; // No es multilínea
    private String cedRuc;
    private String direccion; // No es multilínea
    private String telefono;
    private String observaciones; // Multilínea
    private String facturaOriginal;
    private String docuInterno;   // No se especifica que es
    private String subtotal;
    private String subtotal0;
    private String subtotal12;
    private String iva;
    // Pie
    private String autorizacionSRI;
    private String horaContinua;
    private String nroResolucionContribuyente;
    private String fechaInicioValidez;
    private String fechaFinValidez;
    private String fecha;
    private String motivoDevolucion;
    private TicketS ticket;
    private TicketOrigen ticketOriginal;
    private String ahorroPago;
    private String porcentajeAhorros;
    
    

    public PrintNotaCredito(Devolucion devolucion) {
        
        super(true, new Fecha());
        Fecha fecha = new Fecha();
        //Formato Devolucion Rd
         this.ticket = devolucion.getTicketDevolucion();
         this.ticketOriginal=devolucion.getTicketOriginal();
          byte[] xmlTicket;
          Map<String, ConfigImpPorcentaje> impuestos = new HashMap<String, ConfigImpPorcentaje>();
        try {
            //carga de blob
            xmlTicket = TicketService.consultarXmlTicket(new Long(ticketOriginal.getIdFactura().substring(8)),ticketOriginal.getCodcaja(),ticketOriginal.getTienda());  
            XMLDocument xml = new XMLDocument(xmlTicket);
            XMLDocumentNode cabecera = xml.getNodo(TagTicketXML.TAG_CABECERA);  
             XMLDocumentNode totalesXml = cabecera.getNodo(TagTicketXML.TAG_TOTALES);
             //Impuestos
             XMLDocumentNode subTotalesporIva = totalesXml.getNodo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA);
            TotalesXML totales = new TotalesXML();
            for (XMLDocumentNode impuesto : subTotalesporIva.getHijos()) {
                ConfigImpPorcentaje impPorcentaje = new ConfigImpPorcentaje(new BigDecimal(impuesto.getNodo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA_PORCENTAJE).getValue()), 
                        impuesto.getNodo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA_CODIGO).getValue());
                impuestos.put(impuesto.getNodo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA_CODIGO).getValue(), impPorcentaje);
            }
            totales.setSubtotalesImpuestos(impuestos);
            devolucion.getTicketOriginal().setTotales(totales);
             //decuentos
              porcentajeAhorros = totalesXml.getNodo(TagTicketXML.TAG_TOTALES_PORCENTAJE_DTO_PAGOS).getValue();
             ahorroPago = totalesXml.getNodo(TagTicketXML.TAG_TOTALES_AHORRO_PAGOS).getValue();
            
        } catch (TicketException ex) {
            java.util.logging.Logger.getLogger(PrintNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLDocumentException ex) {
            java.util.logging.Logger.getLogger(PrintNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        //calculo suma totales
         ticket.getTotales().setBase(new BigDecimal(BigInteger.ZERO));
          for(int j=0;j<ticket.getLineas().getLineas().size();j++){
         for(int i=0;i<ticketOriginal.getLineas().size();i++){
             if(ticket.getLineas().getLinea(j).getArticulo().getCodart().equals(ticketOriginal.getLineas().get(i).getArticulo().getCodart())){
              if(ticket.getLineas().getLinea(j).getCantidad().equals(ticketOriginal.getLineas().get(i).getCantidad()))   {
                 BigDecimal valor  =ticketOriginal.getLineas().get(i).getPrecio().setScale(2, RoundingMode.HALF_UP);
                 BigDecimal valorDevolucion  =ticket.getLineas().getLinea(j).getPrecio().setScale(2, RoundingMode.HALF_UP);
                 if(valorDevolucion.compareTo(valor) == 0){
                    ticket.getLineas().getLinea(j).setPrecioTotalTarifaOrigen(valor);
                    ticket.getLineas().getLinea(j).setPrecioTarifaOrigen(ticketOriginal.getLineas().get(i).getPrecioTarifaOrigen().setScale(2, RoundingMode.HALF_UP));
                    ticket.getLineas().getLinea(j).setDescuento(ticketOriginal.getLineas().get(i).getDescuento());
                    ticket.getTotales().setBase(ticket.getTotales().getBase().add(valor));
                 }
              }
             }
         }
          }
         BigDecimal ahor=ticket.getTotales().getBase().multiply(new BigDecimal(porcentajeAhorros)).divide(new BigDecimal(100));        
          ahor=ahor.setScale(2, RoundingMode.HALF_UP);
         ahorroPago=ahor.toString();
        Fecha junioPrimero = new Fecha("01/06/2016", Fecha.PATRON_FECHA_CORTA);
        if(junioPrimero.despues(devolucion.getTicketOriginal().getFecha())){
            super.setPorcentajeIvaEmpresa("12");
        }else{
            super.setPorcentajeIvaEmpresa(impuestos.get(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL).getPorcentajeImpresion().toString());
        }

        // modificamos el lugar y fecha       
        setLugarYfecha(Sesion.getTienda().getCodRegion().getDesregion() +", " +fecha.getString("EEEEE dd 'de' MMMMM 'del' yyyy"));
        
        this.hora = fecha.getString("HH:mm");
        this.horaContinua = fecha.getString("HHmm");
        this.fecha = fecha.getString("dd-MMM-yyyy").toUpperCase();

        this.direccionEmpresa = Sesion.getEmpresa().getDomicilio();
        this.numeroTransaccion = devolucion.getNotaCredito().noTrans();
        this.motivoDevolucion = devolucion.getDevolucion().getMotivo().getDescripcionMotivo();
        
        if (devolucion.getTicketOriginal().getFacturacion()!=null && 
                !devolucion.getTicketOriginal().getFacturacion().getIdent().equals(Variables.getVariable(Variables.POS_CONFIG_ID_CLIENTE_GENERICO))){
            this.nombreCliente = devolucion.getTicketOriginal().getFacturacion().getApellidosImpresion() + " " + devolucion.getTicketOriginal().getFacturacion().getNombreImpresion();
            this.cedRuc = devolucion.getTicketOriginal().getFacturacion().getIdent();       
            this.direccion = devolucion.getTicketOriginal().getFacturacion().getDireccion();   
            this.telefono = devolucion.getTicketOriginal().getFacturacion().getTelefono();
        }
        else{
            this.nombreCliente = "FINAL CONSUMIDOR";
            this.cedRuc = "9999999999999";     
            this.direccion = "Direccion : .............";   
            this.telefono = "......";
        }

        this.observaciones = StringParser.parsearXML(devolucion.getObservaciones());
        this.facturaOriginal = devolucion.getTicketOriginal().getIdFactura();
        this.docuInterno = "";


        this.subtotal = devolucion.getTicketDevolucion().getTotales().getBase().toString();
        this.iva = devolucion.getTicketDevolucion().getTotales().getImpuestosString();

        this.subtotal0 = devolucion.getTicketDevolucion().getTotales().getSubtotal0().toString();
        this.subtotal12 = devolucion.getTicketDevolucion().getTotales().getSubtotal12().toString();

        // Pie
        
        this.nroResolucionContribuyente = Sesion.getEmpresa().getNroResolucionContribuyente();
        try {
            this.autorizacionSRI = Sesion.getEmpresa().getNumAutorizacion().toString();
            Fecha fechaInicioValidezF = new Fecha(Sesion.getEmpresa().getFechaInicioAuorizacion());
            Fecha fechaFinValidezF = new Fecha(Sesion.getEmpresa().getFechafinAutorizacion());

            this.fechaInicioValidez = fechaInicioValidezF.getString("dd 'de' MMMMM 'del' yyyy"); // Poner formato correcto
            this.fechaFinValidez = fechaFinValidezF.getString("dd 'de' MMMMM 'del' yyyy");  // Poner formato correcto     
        }
        catch (NullPointerException e) {
            log.error("La Empresa, no tiene un número de autorización asignado o sus fechas de validez no están establecidas");
        }

    }
       public static List<Pago> getPagos(XMLDocumentNode pagos) throws TicketException {
        try {
            List<Pago> listaPagos = new LinkedList<Pago>();
            for (XMLDocumentNode linea : pagos.getHijos()) {
                
                MedioPagoBean mp;
                String codMedioPago = linea.getNodo(TagTicketXML.TAG_PAGO_CODMEDPAG).getValue();
                mp = MediosPago.getInstancia().getMedioPago(linea.getNodo(TagTicketXML.TAG_PAGO_CODMEDPAG).getValue());
                if (mp == null && MedioPagoBean.getMedioPagoAbonoReservacion().getCodMedioPago().equals(codMedioPago)) {
                    mp = MedioPagoBean.getMedioPagoAbonoReservacion();
                }
                else if (mp == null){
                    try{
                        mp = MediosPago.consultar(codMedioPago);
                    } catch (MedioPagoException e){
                        log.warn("[Parseando Ticket] No se ha encontrado el medio de pago :" + codMedioPago, e);
                    }
                    if(mp == null){
                        log.warn("[Parseando Ticket] No se ha encontrado el medio de pago :" + codMedioPago);
                        log.warn("[Parseando Ticket] Se utilizará forma de pago efectivo por defecto.");
                        mp = MediosPago.getInstancia().getPagoEfectivo();
                    }
                }
                Pago p = null;
                // 
                try {
                    String autorizacion = linea.getNodo(TagTicketXML.TAG_PAGO_AUTORIZACION).getValue();
                    String auditoria = linea.getNodo(TagTicketXML.TAG_PAGO_AUDITORIA).getValue();
                    String uidFacturacion = linea.getNodo(TagTicketXML.TAG_PAGO_FACTURACION).getValue();
                    String fechaCaducidadS = linea.getNodo(TagTicketXML.TAG_PAGO_FECHA_CADUCIDAD).getValue();
                    String cvv = linea.getNodo(TagTicketXML.TAG_PAGO_CVV).getValue();
                    XMLDocumentNode plan = linea.getNodo(TagTicketXML.TAG_PAGO_PLAN);
                    String cuotas = plan.getNodo(TagTicketXML.TAG_PAGO_PLAN_N_CUOTAS).getValue();
                    String importeInteres = linea.getNodo(TagTicketXML.TAG_PAGO_INTERES_IMP).getValue();
                    
                    PagoCredito pc;
                    try {
                        String numeroCredito = linea.getNodo(TagTicketXML.TAG_PAGO_NUMERO_CREDITO).getValue();
                        String cedulaTarjetaCD = linea.getNodo(TagTicketXML.TAG_PAGO_CLIENTE_TARJETA).getValue();
                        pc = new PagoCreditoSK(null, null, null, Integer.valueOf(numeroCredito), cedulaTarjetaCD);
                        
                    }
                    catch (XMLDocumentNodeNotFoundException e) {          
                        pc = new PagoCredito(null, null, null);
                    }
                    
                    PlanPagoCredito ppc = new PlanPagoCredito();  
                    ppc.setNumCuotas(new BigDecimal(cuotas));
                    ppc.setImporteInteres(new BigDecimal(importeInteres));
                    pc.setPlanSeleccionado(ppc);                    
                    pc.setCodigoValidacionManual(autorizacion);
                    pc.setAuditoria(Integer.valueOf(auditoria));
                    pc.setUidFacturacion(uidFacturacion);
                    pc.setCvv(cvv);
                    pc.setFechaCaducidad(fechaCaducidadS);
                    pc.setPagoActivo(Pago.PAGO_TARJETA);
                    p = (Pago) pc;
                }
                catch (XMLDocumentNodeNotFoundException e) {
                    // Comprobamos si el pago es de tipo Giftcard                    
                    if (mp != null && mp.isGiftCard()) {
                        p = new PagoGiftCard(null, null);
                        p.setReferencia(linea.getNodo(TagTicketXML.TAG_PAGO_INFO1).getValue());
                    }
                    else {
                        p = new Pago(null, null);
                    }
                }

                p.setTotal(new BigDecimal(linea.getNodo(TagTicketXML.TAG_PAGO_TOTAL).getValue()));
                p.setUstedPaga(new BigDecimal(linea.getNodo(TagTicketXML.TAG_PAGO_USTED_PAGA).getValue()));
                p.setDescuentoCalculado(new BigDecimal(linea.getNodo(TagTicketXML.TAG_PAGO_DESCUENTO).getValue()));
//                p.set
                p.setEntregado(new BigDecimal(linea.getNodo(TagTicketXML.TAG_PAGO_ENTREGADO).getValue()));
                p.setMedioPagoActivo(mp);
                listaPagos.add(p);

                // Si el pago es con una nota de crédito                
                if (mp != null && mp.isNotaCredito()) {
                    p.setReferencia(linea.getNodo(TagTicketXML.TAG_PAGO_NOTA_CREDITO).getValue());
                }
                // Comprobamos si es un Bono de Efectivo
                else if (mp != null && mp.isBonoEfectivo()) {
                    p.setReferencia(linea.getNodo(TagTicketXML.TAG_PAGO_INFO1).getValue());
                }
                else {
                    // Comprobamos si es una Letra
                    try {
                        String uidLetra = linea.getNodo(TagTicketXML.TAG_PAGO_LETRA_UID).getValue();
                        p.setReferencia(uidLetra);
                        mp.setCreditoDirecto(true);
                    }
                    catch (XMLDocumentNodeNotFoundException e) {
                        // no hacemos nada
                    }
                }
            }  
            return listaPagos;
        }
        catch (XMLDocumentNodeNotFoundException ex) {
            String msg = "Error leyendo xml de pagos anterior." + ex.getMessage();
            log.error(msg, ex);
            throw new TicketException(msg, ex);
        }           
   }
    public String getDireccionEmpresa() {
        return direccionEmpresa;
    }

    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCedRuc() {
        return cedRuc;
    }

    public void setCedRuc(String cedRuc) {
        this.cedRuc = cedRuc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFacturaOriginal() {
        return facturaOriginal;
    }

    public void setFacturaOriginal(String facturaOriginal) {
        this.facturaOriginal = facturaOriginal;
    }

    public String getDocuInterno() {
        return docuInterno;
    }

    public void setDocuInterno(String docuInterna) {
        this.docuInterno = docuInterna;
    }

    public LineaEnTicket getDireccionEmpresaAsLineas() {
        return new LineaEnTicket("MATRIZ : " + this.getDireccionEmpresa());
    }

    @Override
    public LineaEnTicket getDireccionLocalAsLineas() {
        return new LineaEnTicket("SUCURSAL : " + this.getDireccionLocal());
    }

    public LineaEnTicket getObservacionesAsLineas() {
        return new LineaEnTicket("Observ. : " + this.observaciones);
    }
    
    public LineaEnTicket getMotivoDevolucionAsLineas() {
        return new LineaEnTicket("Mot. Devolución: "+this.motivoDevolucion);
    }

    public LineaEnTicket getDireccionClienteAsLineas() {
        return new LineaEnTicket("Direccion : " + this.direccion);
    }

    public LineaEnTicket getNombreClienteAsLineas() {
        return new LineaEnTicket("Nombre : " + this.nombreCliente);
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getAutorizacionSRI() {
        return autorizacionSRI;
    }

    public String getHoraContinua() {
        return horaContinua;
    }

    public String getNroResolucionContribuyente() {
        return nroResolucionContribuyente;
    }

    public String getFechaInicioValidez() {
        return fechaInicioValidez;
    }

    public String getFechaFinValidez() {
        return fechaFinValidez;
    }

    public String getFecha() {
        return fecha;
    }

    public String getSubtotal0() {
        return subtotal0;
    }

    public void setSubtotal0(String subtotal0) {
        this.subtotal0 = subtotal0;
    }

    public String getSubtotal12() {
        return subtotal12;
    }

    public void setSubtotal12(String subtotal12) {
        this.subtotal12 = subtotal12;
    }
    
    @Override
    public LineaEnTicket getLugarYfechaAsLineas() {
        return  new LineaEnTicket("FECHA : "+getLugarYfecha());
    }
    
    public void setMotivoDevolucion(String motivoDevolucion){
        this.motivoDevolucion = motivoDevolucion;
    }
    
    public String getMotivoDevolucion(){
        return motivoDevolucion;
    }

    public TicketS getTicket() {
        return ticket;
    }

    public void setTicket(TicketS ticket) {
        this.ticket = ticket;
    }

    public TicketOrigen getTicketOriginal() {
        return ticketOriginal;
    }

    public void setTicketOriginal(TicketOrigen ticketOriginal) {
        this.ticketOriginal = ticketOriginal;
    }

    public String getAhorroPago() {
        return ahorroPago;
    }

    public void setAhorroPago(String ahorroPago) {
        this.ahorroPago = ahorroPago;
    }

    public String getPorcentajeAhorros() {
        return porcentajeAhorros;
    }

    public void setPorcentajeAhorros(String porcentajeAhorros) {
        this.porcentajeAhorros = porcentajeAhorros;
    }
}
