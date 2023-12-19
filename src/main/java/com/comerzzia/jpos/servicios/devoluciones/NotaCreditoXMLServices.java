/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.devoluciones;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.entity.db.Vendedor;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.servicios.garantia.GarantiaReferencia;
import com.comerzzia.jpos.servicios.kit.KitReferencia;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import es.mpsistemas.util.xml.XMLDocumentTransformerException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author amos
 */
public class NotaCreditoXMLServices {
    
    
    private static final Logger log = Logger.getMLogger(NotaCreditoXMLServices.class);

    private NotaCreditoXMLServices() {
    }    
    
    
    /**
     * Crea un XML con la información del ticket actual almacenado en sesión
     * @return
     */
    public static void construirXML(NotasCredito nota, Devolucion devolucion) throws NotaCreditoException {
        try {
            log.debug("Construyendo xml para nota de crédito. ");
            XMLDocument xml = new XMLDocument();
            XMLDocumentNode root = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_NOTA_CREDITO);
            xml.setRoot(root);

            //Añadimos la cabecera
            root.añadirHijo(construirTagCabeceraDevolucion(xml, nota, devolucion));
            //Añadimos las lineas del ticket
            root.añadirHijo(contruirTagLineas(xml, devolucion.getTicketDevolucion()));
            //Añadimos los pagos del ticket
            root.añadirHijo(XMLDocumentNode.importarNodo(xml, devolucion.getTicketOriginal().getPagos().getNode()));

            log.debug("XML de la nota de crédito: ");
            log.debug(xml.getString());
            nota.setNotaCredito(xml.getBytes());
        }
        catch (XMLDocumentException ex) {
            String msg = "Error generando XML de nota de crédito: " + ex.getMessage();
            log.error(msg, ex);
            throw new NotaCreditoException(msg, ex);
        }
        catch (XMLDocumentTransformerException ex) {
            String msg = "Error generando XML de nota de crédito: " + ex.getMessage();
            log.error(msg, ex);
            throw new NotaCreditoException(msg, ex);
        }
    }
    
    public static BigDecimal dameCompensacion(byte[] nota) throws NotaCreditoException {
        try{
            XMLDocument xml = new XMLDocument(nota);
            XMLDocumentNode root = xml.getRoot();
            XMLDocumentNode cabecera = root.getNodo(TagNotaCreditoXML.TAG_CABECERA);
            XMLDocumentNode totales = cabecera.getNodo(TagNotaCreditoXML.TAG_TOTALES);
            XMLDocumentNode compensacion = totales.getNodo(TagNotaCreditoXML.TAG_TOTALES_COMPENSACION, true);
            if(compensacion != null){
                return compensacion.getValueAsBigDecimal().setScale(2,RoundingMode.HALF_DOWN);
            }
            return BigDecimal.ZERO;
        }
        catch (XMLDocumentException ex) {
            String msg = "Error obteniendo compensación de nota de crédito: " + ex.getMessage();
            log.error(msg, ex);
            throw new NotaCreditoException(msg, ex);
        }
    }

    public static void construirXMLAbono(NotasCredito nota, Reservacion reserva) throws NotaCreditoException {
        try {
            log.debug("Construyendo xml para nota de crédito. ");
            XMLDocument xml = new XMLDocument();
            XMLDocumentNode root = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_NOTA_CREDITO);
            xml.setRoot(root);

            //Añadimos la cabecera
            root.añadirHijo(construirTagCabeceraAbono(xml, nota, reserva));

            log.debug("XML de la nota de crédito: ");
            log.debug(xml.getString());
            nota.setNotaCredito(xml.getBytes());
        }
        catch (XMLDocumentException ex) {
            String msg = "Error generando XML de nota de crédito: " + ex.getMessage();
            log.error(msg, ex);
            throw new NotaCreditoException(msg, ex);
        }
        catch (XMLDocumentTransformerException ex) {
            String msg = "Error generando XML de nota de crédito: " + ex.getMessage();
            log.error(msg, ex);
            throw new NotaCreditoException(msg, ex);
        }
    }
    
    
    
    // cliente
    private static XMLDocumentNode construirTagCliente(XMLDocument xml, Cliente cliente) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_CLIENTE);
        root.añadirHijo(TagNotaCreditoXML.TAG_CLIENTE_TIPO_ID, cliente.getTipoIdentificacion());
        root.añadirHijo(TagNotaCreditoXML.TAG_CLIENTE_ID, cliente.getIdentificacion());
        root.añadirHijo(TagNotaCreditoXML.TAG_CLIENTE_NUM_AFILIADO, cliente.getCodigoTarjetaBabysClub());
        root.añadirHijo(TagNotaCreditoXML.TAG_CLIENTE_NOMBRE, cliente.getNombre());
        root.añadirHijo(TagNotaCreditoXML.TAG_CLIENTE_APELLIDOS, cliente.getApellido());
        
         // Dirección
        if (cliente.getDomicilio() != null) {            
             root.añadirHijo(TagNotaCreditoXML.TAG_CLIENTE_DIRECCION, cliente.getDomicilioImpresion());
        }
         // Teléfono
        String telefono = null;
        if (cliente.getTelefonoParticular() != null && cliente.getTelefonoParticular().length() == 9) {
            telefono = cliente.getTelefonoParticular();
        }
        else if (cliente.getTelefonoMovil() != null && cliente.getTelefonoMovil().length() == 9) {
            telefono = cliente.getTelefonoMovil();
        }            
        if (telefono != null){
            root.añadirHijo(TagNotaCreditoXML.TAG_CLIENTE_TELEFONO, telefono);
        }
         // Dirección / Teléfono
        return root;

    }

    // factura
    private static XMLDocumentNode construirTagFactura(XMLDocument xml, TicketOrigen ticketOriginal) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_FACTURA);
        root.añadirHijo(TagNotaCreditoXML.TAG_FACTURA_UID, ticketOriginal.getUid_ticket());
        root.añadirHijo(TagNotaCreditoXML.TAG_FACTURA_ID_TIENDA, ticketOriginal.getTienda());
        root.añadirHijo(TagNotaCreditoXML.TAG_FACTURA_CODCAJA, ticketOriginal.getCodcaja());
        root.añadirHijo(TagNotaCreditoXML.TAG_FACTURA_ID_FACTURA, String.valueOf(ticketOriginal.getId_ticket()));
        return root;

    }

    // cajero
    private static XMLDocumentNode construirTagCajero(XMLDocument xml, UsuarioBean usuario) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_CAJERO);
        root.añadirHijo(TagNotaCreditoXML.TAG_CAJERO_ID, usuario.getIdUsuario().toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_CAJERO_DES, usuario.getDesUsuario());
        return root;
    }

    // vendedor
    private static XMLDocumentNode construirTagVendedor(XMLDocument xml, Vendedor vendedor) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_VENDEDOR);
        root.añadirHijo(TagNotaCreditoXML.TAG_VENDEDOR_ID, vendedor.getCodvendedor());
        root.añadirHijo(TagNotaCreditoXML.TAG_VENDEDOR_NOMBRE, vendedor.getNombreVendedor());
        root.añadirHijo(TagNotaCreditoXML.TAG_VENDEDOR_APELLIDOS, vendedor.getApellidosVendedor());
        return root;
    }

    // linea
    private static XMLDocumentNode construirTagLinea(XMLDocument xml, LineaTicket linea) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_LINEA);
        root.añadirAtributo(TagNotaCreditoXML.ATR_LINEA_IDLINEA, linea.getIdlinea().toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_CODART, linea.getArticulo().getCodart());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_DESART, linea.getArticulo().getDesart());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_CODBARRAS, linea.getCodigoBarras());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_CODIMP, linea.getCodimp());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_CANTIDAD, linea.getCantidad().toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_PRECIO_TOTAL_TARIFA_ORIGEN, Numero.redondear(linea.getPrecioTotalTarifaOrigen()).toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_PRECIO_TARIFA_ORIGEN, Numero.redondear(linea.getPrecioTarifaOrigen()).toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_DESCUENTO, linea.getDescuento().toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_PRECIO_TOTAL, Numero.redondear(linea.getPrecioTotal()).toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_PRECIO, Numero.redondear(linea.getPrecio()).toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_IMPORTE_TOTAL, Numero.redondear(linea.getImporteTotal()).toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_IMPORTE, Numero.redondear(linea.getImporte()).toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_DESCUENTO_FINAL, Numero.redondear(linea.getDescuentoFinal()).toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_LINEA_MODELO, linea.getArticulo().getModelo());
        if (linea.getReferenciaKit() != null){
            root.añadirHijo(construirTagRefKit(xml, linea.getReferenciaKit()));
        }        
        if (linea.getReferenciaGarantia() != null){
            root.añadirHijo(construirTagRefGarantia(xml, linea.getReferenciaGarantia(), linea.getCantidad()));
        }        
        return root;
    }

    // cabecera
    private static XMLDocumentNode construirTagCabeceraDevolucion(XMLDocument xml, NotasCredito nota, Devolucion devolucion ) {
        XMLDocumentNode root = construirTagCabecera(xml, nota, devolucion.getAutorizador());
        root.añadirHijo(TagNotaCreditoXML.TAG_ID_MOTIVO_DEV, devolucion.getMotivo().getIdMotivo().toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_DES_MOTIVO_DEV, devolucion.getMotivo().getDescripcionMotivo());
        root.añadirHijo(TagNotaCreditoXML.TAG_UID_DIARIO_CAJA, devolucion.getTicketDevolucion().getUid_diario_caja());
        root.añadirHijo(TagNotaCreditoXML.TAG_UID_CAJERO_CAJA, devolucion.getTicketDevolucion().getUid_cajero_caja()); 
        root.añadirHijo(TagNotaCreditoXML.TAG_OBSERVACIONES, devolucion.getObservaciones());
        
        // Datos adicionales para reimpresión de factura
        root.añadirHijo(TagNotaCreditoXML.TAG_NOMBRE_EMPRESA, Sesion.getEmpresa().getNombreComercial());
        root.añadirHijo(TagNotaCreditoXML.TAG_NOMBRE_TIENDA, Sesion.getTienda().getSriTienda().getDesalm());
        root.añadirHijo(TagNotaCreditoXML.TAG_DIRECCION_EMPRESA, Sesion.getEmpresa().getDomicilio());
        root.añadirHijo(TagNotaCreditoXML.TAG_DIRECCON_LOCAL, Sesion.getTienda().getSriTienda().getDomicilio());
        root.añadirHijo(TagNotaCreditoXML.TAG_REGISTRO_MERCANTIL, Sesion.getEmpresa().getCif()); //RUC
        root.añadirHijo(TagNotaCreditoXML.TAG_REGION, Sesion.getTienda().getCodRegion().getDesregion());
        root.añadirHijo(TagNotaCreditoXML.TAG_NRO_RESOLUCION_CONTRIBUYENTE, Sesion.getEmpresa().getNroResolucionContribuyente());
        
        root.añadirHijo(TagNotaCreditoXML.TAG_AUTORIZACION_SRI, Sesion.getEmpresa().getNumAutorizacion().toString());
        Fecha fechaInicioValidezF = new Fecha(Sesion.getEmpresa().getFechaInicioAuorizacion());
        Fecha fechaFinValidezF = new Fecha(Sesion.getEmpresa().getFechafinAutorizacion());
                    
        root.añadirHijo(TagNotaCreditoXML.TAG_FECHA_INICIO_VALIDEZ_SRI,fechaInicioValidezF.getString("dd 'de' MMMMM 'del' yyyy"));
        root.añadirHijo(TagNotaCreditoXML.TAG_FECHA_FIN_VALIDEZ_SRI, fechaFinValidezF.getString("dd 'de' MMMMM 'del' yyyy"));
           
        
        root.añadirHijo(construirTagFactura(xml, devolucion.getTicketOriginal()));
        root.añadirHijo(construirTagCliente(xml, devolucion.getTicketOriginal().getCliente()));
        
        if (devolucion.getTicketOriginal().getNodoFacturacion()!=null){
            root.añadirHijo(XMLDocumentNode.importarNodo(xml, devolucion.getTicketOriginal().getNodoFacturacion().getNode()));
        }
        
        root.añadirHijo(construirTagCajero(xml, devolucion.getTicketDevolucion().getCajero()));
        if (devolucion.getTicketDevolucion().getVendedor()!= null){
            root.añadirHijo(construirTagVendedor(xml, devolucion.getTicketDevolucion().getVendedor()));
        }

        XMLDocumentNode totales = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_TOTALES);
        totales.añadirHijo(TagNotaCreditoXML.TAG_TOTALES_TOTAL, nota.getTotal().toString());
        if (devolucion.getTicketOriginal().getTotalDtoPagos()!=null){
            totales.añadirHijo(TagNotaCreditoXML.TAG_TOTALES_PORCENTAJE_DTO_PAGOS, devolucion.getTicketOriginal().getTotalDtoPagos().toString());
        }
        totales.añadirHijo(TagNotaCreditoXML.TAG_TOTALES_BASE, devolucion.getTicketDevolucion().getTotales().getBase().toString());
        totales.añadirHijo(TagNotaCreditoXML.TAG_TOTALES_IMPUESTOS, devolucion.getTicketDevolucion().getTotales().getImpuestosString());
        totales.añadirHijo(TagNotaCreditoXML.TAG_TOTALES_SUBTOTAL0, devolucion.getTicketDevolucion().getTotales().getSubtotal0().toString());
        totales.añadirHijo(TagNotaCreditoXML.TAG_TOTALES_SUBTOTAL_IVA_VALOR, devolucion.getTicketDevolucion().getTotales().getSubtotal12().toString());
        totales.añadirHijo(TagNotaCreditoXML.TAG_TOTALES_SUBTOTAL_IVA_PORCENTAJE, devolucion.getTicketDevolucion().getTotales().getPorcentajeSubtotal().toString());
        totales.añadirHijo(TagNotaCreditoXML.TAG_TOTALES_COMPENSACION, devolucion.getTicketDevolucion().getTotales().getCompensacionGobierno().toString());
        
        root.añadirHijo(totales);
        return root;
    }
    // cabecera
    private static XMLDocumentNode construirTagCabeceraAbono(XMLDocument xml, NotasCredito nota, Reservacion reserva) {
        XMLDocumentNode root = construirTagCabecera(xml, nota, ""); // TODO: NOTA CREDITO - meter usuario autorizador del abono
        root.añadirHijo(TagNotaCreditoXML.TAG_UID_DIARIO_CAJA, Sesion.getCajaActual().getCajaActual().getUidDiarioCaja());
        root.añadirHijo(TagNotaCreditoXML.TAG_UID_CAJERO_CAJA, Sesion.getCajaActual().getCajaParcialActual().getUidCajeroCaja()); 

        root.añadirHijo(construirTagCliente(xml, reserva.getReservacion().getCliente()));
        root.añadirHijo(construirTagCajero(xml, Sesion.getUsuario()));

        XMLDocumentNode totales = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_TOTALES);
        totales.añadirHijo(TagNotaCreditoXML.TAG_TOTALES_TOTAL, nota.getTotal().toString());
        root.añadirHijo(totales);
        return root;
    }

    // cabecera
    private static XMLDocumentNode construirTagCabecera(XMLDocument xml, NotasCredito nota, String usuarioAutorizador) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_CABECERA);
        root.añadirHijo(TagNotaCreditoXML.TAG_UID_NC, nota.getUidNotaCredito());
        root.añadirHijo(TagNotaCreditoXML.TAG_VERSION, Constantes.VERSION_POS);
        root.añadirHijo(TagNotaCreditoXML.TAG_FECHA, new Fecha(nota.getFecha()).getStringHora());
        root.añadirHijo(TagNotaCreditoXML.TAG_FECHA_VALIDEZ, new Fecha(nota.getFechaValidez()).getStringHora());
        root.añadirHijo(TagNotaCreditoXML.TAG_ID_TIENDA, nota.getCodalm());
        root.añadirHijo(TagNotaCreditoXML.TAG_CODCAJA, nota.getCodcaja());
        root.añadirHijo(TagNotaCreditoXML.TAG_ID_NC, nota.getIdNotaCredito().toString());
        root.añadirHijo(TagNotaCreditoXML.TAG_AUTORIZADOR, usuarioAutorizador);
        return root;
    }
    
    
    
    // lineas
    private static XMLDocumentNode contruirTagLineas(XMLDocument xml, TicketS ticket) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_LINEAS);
        for (int i = 0; i < ticket.getLineas().getNumLineas(); i++) {
            LineaTicket linea = ticket.getLineas().getLinea(i);
            linea.setIdlinea(i + 1);
            XMLDocumentNode nodoLinea = construirTagLinea(xml, linea);
            root.añadirHijo(nodoLinea);
        }
        return root;
    }

    private static XMLDocumentNode construirTagRefGarantia(XMLDocument xml, GarantiaReferencia referencia, int cantidad) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_GARANTIA);
        if (referencia.getTicketOrigen() != null){
            root.añadirHijo(TagNotaCreditoXML.TAG_GARANTIA_UID_FACTURA, referencia.getTicketOrigen().getUid_ticket());
            root.añadirHijo(TagNotaCreditoXML.TAG_GARANTIA_FACTURA, referencia.getTicketOrigen().getIdFactura());
        }
        root.añadirHijo(TagNotaCreditoXML.TAG_GARANTIA_ARTICULO, referencia.getArticulo().getCodart());
        root.añadirHijo(TagNotaCreditoXML.TAG_GARANTIA_PRECIO, Numero.redondear(referencia.getPreciotTotalFinalPagadoArticuloAsociado().multiply(new BigDecimal(cantidad))).toString());
        return root;        
    }
    
    // Referencia Kit instalación
    private static XMLDocumentNode construirTagRefKit(XMLDocument xml, KitReferencia referencia) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagNotaCreditoXML.TAG_KIT);
        if (referencia.getTicketOrigen() != null){
            root.añadirHijo(TagNotaCreditoXML.TAG_KIT_UID_FACTURA, referencia.getTicketOrigen().getUid_ticket());
            root.añadirHijo(TagNotaCreditoXML.TAG_KIT_FACTURA, referencia.getTicketOrigen().getIdFactura());
        }
        root.añadirHijo(TagNotaCreditoXML.TAG_KIT_ARTICULO, referencia.getArticulo().getCodart());
        return root;
    }    
}
