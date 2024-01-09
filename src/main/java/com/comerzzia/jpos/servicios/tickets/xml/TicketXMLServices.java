/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.xml;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.ConfigImpPorcentaje;
import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.entity.db.FacturacionTarjeta;
import com.comerzzia.jpos.entity.db.FacturacionTicket;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.Vendedor;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosDeEnvio;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.tickets.componentes.VentaEntreLocales;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.persistencia.facturacion.tarjetas.FacturacionTarjetasDao;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaBean;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.garantia.GarantiaReferencia;
import com.comerzzia.jpos.servicios.kit.KitReferencia;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoNotaCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PlanPagoCredito;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoSK;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoLetra;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.pagos.especiales.PagoGiftCard;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.jpos.servicios.promociones.articulos.SukuponLinea;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDtoManualTotal;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionPagoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.util.enums.EnumTipoLectura;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.cadenas.Cadena;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import es.mpsistemas.util.xml.XMLDocumentNodeNotFoundException;
import es.mpsistemas.util.xml.XMLDocumentTransformerException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class TicketXMLServices {

    private static final Logger log = Logger.getMLogger(TicketXMLServices.class);

    private TicketXMLServices() {
    }

    /**
     * Crea un XML con la información del ticket actual almacenado en sesión
     *
     * @param ticket
     * @return
     * @throws com.comerzzia.jpos.servicios.tickets.TicketException
     */
    public static byte[] getXMLTicket(TicketS ticket) throws TicketException {
        try {
            return TicketXMLServices.getXMLDocumentTicket(ticket).getBytes();
        } catch (XMLDocumentTransformerException ex) {
            String msg = "Error generando XML del ticket: " + ex.getMessage();
            log.error(msg, ex);
            throw new TicketException(msg, ex);
        }
    }

    public static byte[] getXMLPagos(PagosTicket pagosTicket) throws TicketException {
        try {
            log.debug("Construyendo xml para pagos. ");
            XMLDocument xml = new XMLDocument();
            XMLDocumentNode root = construirTagPagos(xml, pagosTicket);
            xml.setRoot(root);
            log.debug("XML de pagos: ");
            log.debug(xml.getString());
            return xml.getBytes();
        } catch (XMLDocumentException ex) {
            String msg = "Error generando XML de pagos: " + ex.getMessage();
            log.error(msg, ex);
            throw new TicketException(msg, ex);
        } catch (XMLDocumentTransformerException ex) {
            String msg = "Error generando XML de pagos: " + ex.getMessage();
            log.error(msg, ex);
            throw new TicketException(msg, ex);
        }
    }

    public static TicketOrigen getTicketOrigen(XMLDocument xml) throws TicketException {
        try {
            TicketOrigen ticket = new TicketOrigen();
            ticket.setLineas(new LinkedList<LineaTicket>());

            // Rellenamos los datos de la cabecera en el ticketS
            XMLDocumentNode cabecera = xml.getNodo(TagTicketXML.TAG_CABECERA);
            ticket.setUid_ticket(cabecera.getNodo(TagTicketXML.TAG_UID_TICKET).getValue());
            ticket.setFecha(new Fecha(cabecera.getNodo(TagTicketXML.TAG_FECHA).getValue(), Fecha.PATRON_FECHA_HORA));
            ticket.setFechaFinDevolucion(cabecera.getNodo(TagTicketXML.TAG_FECHA_FIN_DEVOLUCION).getValueAsFecha());
            if (cabecera.getNodo(TagTicketXML.TAG_PORCENTAJE_COMPENSACION, true) != null) {
                ticket.setPorcentajeCompensacion(cabecera.getNodo(TagTicketXML.TAG_PORCENTAJE_COMPENSACION).getValueAsBigDecimal());
            } else {
                ticket.setPorcentajeCompensacion(BigDecimal.ZERO);
            }
            String tdp = "";
            try {
                XMLDocumentNode totalesXml = cabecera.getNodo(TagTicketXML.TAG_TOTALES);
                tdp = totalesXml.getNodo(TagTicketXML.TAG_TOTALES_PORCENTAJE_DTO_PAGOS).getValue();
                ticket.setTotalDtoPagos(new BigDecimal(tdp));
                ticket.setTotales(new TotalesXML(totalesXml));

            } catch (XMLDocumentNodeNotFoundException e) {
                //Se trata de un ticket antiguo.
                log.debug("Ticket sin tratamiento de porcentaje de descuento original");
            } catch (NumberFormatException e) {
                log.error("error recuperando el valor de total descuento pago :" + tdp);
            }
            try {
                ticket.setTienda(cabecera.getNodo(TagTicketXML.TAG_ID_TIENDA).getValue());
            } catch (XMLDocumentNodeNotFoundException e) {
                ticket.setTienda(cabecera.getNodo("IdTicket").getValue());
            }

            ticket.setCodcaja(cabecera.getNodo(TagTicketXML.TAG_CODCAJA).getValue());
            ticket.setId_ticket(cabecera.getNodo(TagTicketXML.TAG_ID_TICKET).getValueAsInteger());
            ticket.setUid_diario_caja(cabecera.getNodo(TagTicketXML.TAG_UID_DIARIO_CAJA).getValue());
            ticket.setUid_cajero_caja(cabecera.getNodo(TagTicketXML.TAG_UID_CAJERO_CAJA).getValue());

            // Datos del cliente
            XMLDocumentNode nodoCliente = cabecera.getNodo(TagTicketXML.TAG_CLIENTE);
            Cliente cliente = new Cliente();
            cliente.setTipoIdentificacion(nodoCliente.getNodo(TagTicketXML.TAG_CLIENTE_TIPO_ID).getValue());
            cliente.setIdentificacion(nodoCliente.getNodo(TagTicketXML.TAG_CLIENTE_ID).getValue());
            cliente.setCodigoTarjetaBabysClub(nodoCliente.getNodo(TagTicketXML.TAG_CLIENTE_NUM_AFILIADO).getValue());
            cliente.setNombre(nodoCliente.getNodo(TagTicketXML.TAG_CLIENTE_NOMBRE).getValue());
            cliente.setApellido(nodoCliente.getNodo(TagTicketXML.TAG_CLIENTE_APELLIDOS).getValue());
            ticket.setCliente(cliente);

            // Si hay facturación, sustituimos los datos, por los datos de facturación
            ticket.setNodoFacturacion(cabecera.getNodo(TagTicketXML.TAG_DATOS_FACTURACION));
            FacturacionTicket facturacion = null;
            if (ticket.getNodoFacturacion() != null) {
                facturacion = new FacturacionTicket();
                facturacion.setTipoIdent(ticket.getNodoFacturacion().getNodo(TagTicketXML.TAG_DATOS_FACT_TIPO_ID).getValue());
                facturacion.setNombre(ticket.getNodoFacturacion().getNodo(TagTicketXML.TAG_DATOS_FACT_NOMBRE).getValue());
                facturacion.setIdent(ticket.getNodoFacturacion().getNodo(TagTicketXML.TAG_DATOS_FACT_ID).getValue());
                facturacion.setApellidos(ticket.getNodoFacturacion().getNodo(TagTicketXML.TAG_DATOS_FACT_APELLIDOS).getValue());
                facturacion.setDireccion(ticket.getNodoFacturacion().getNodo(TagTicketXML.TAG_DATOS_FACT_DIRECCION).getValue());
                facturacion.setTelefono(ticket.getNodoFacturacion().getNodo(TagTicketXML.TAG_DATOS_FACT_TELEFONO).getValue());
            }
            ticket.setFacturacion(facturacion);

            // Datos de cajero
            XMLDocumentNode nodoCajero = cabecera.getNodo(TagTicketXML.TAG_CAJERO);
            UsuarioBean cajero = new UsuarioBean();
            cajero.setIdUsuario(nodoCajero.getNodo(TagTicketXML.TAG_USUARIO_ID).getValueAsLong());
            cajero.setDesUsuario(nodoCajero.getNodo(TagTicketXML.TAG_USUARIO_DES).getValue());
            ticket.setCajero(cajero);

            // Lineas
            XMLDocumentNode nodoLineas = xml.getNodo(TagTicketXML.TAG_LINEAS);

            for (XMLDocumentNode linea : nodoLineas.getHijos()) {
                LineaTicket l = new LineaTicket(linea);
                //DR
                LineaTicketOrigen lineaOrigen = TicketService.consultarLineaTicketOrigen(ticket.getUid_ticket(), new Long(l.getIdlinea()));

                //if para que no se caiga en la liquidacion de los plan novios
                if (lineaOrigen != null) {
                    l.setCodEmpleado(lineaOrigen.getCodVendedor());
                }

                ticket.getLineas().add(l);
            }

            // Promociones
            XMLDocumentNode nodoPromociones = xml.getNodo(TagTicketXML.TAG_PROMOCIONES, true);
            if (nodoPromociones != null) {
                XMLDocumentNode facturaDiaSocio = nodoPromociones.getNodo(TagTicketXML.TAG_PROMO_FACTURA_DIA_SOCIO, true);
                if (facturaDiaSocio != null) {
                    ticket.setUidTicketDiaSocio(facturaDiaSocio.getValue());
                }
            }

            // Puntos
            try {
                XMLDocumentNode nodoPuntos = xml.getNodo(TagTicketXML.TAG_PUNTOS);
                // Puntos acumulados
                try {
                    ticket.setPuntosAcumulados(nodoPuntos.getNodo(TagTicketXML.TAG_PUNTOS_ACUMULADOS).getValueAsInteger());
                } catch (XMLDocumentNodeNotFoundException e) {
                    try {
                        ticket.setPuntosAcumulados(nodoPuntos.getNodo(TagTicketXML.TAG_PUNTOS_CEDIDOS).getValueAsInteger());
                    } catch (XMLDocumentNodeNotFoundException e2) {
                        ticket.setPuntosAcumulados(0);
                    }
                }
                // puntos consumidos
                try {
                    ticket.setPuntosConsumidos(nodoPuntos.getNodo(TagTicketXML.TAG_PUNTOS_CONSUMIDOS).getValueAsInteger());
                } catch (XMLDocumentNodeNotFoundException e) {
                    ticket.setPuntosConsumidos(0);
                }
                // cliente de acumulación
                try {
                    ticket.setClienteAcumulacion(nodoPuntos.getNodo(TagTicketXML.TAG_PUNTOS_CLIENTE).getValue());
                } catch (XMLDocumentNodeNotFoundException e) {
                    ticket.setClienteAcumulacion(ticket.getCliente().getCodcli());
                }
            } catch (XMLDocumentNodeNotFoundException e) {
                ticket.setClienteAcumulacion(null);  //Si es null, el método setClienteAcumulacion devuelve falso.
            }

            // Guardamos el nodo de pagos y lo parseamos en una lista de pagos.
            ticket.setPagos(xml.getNodo(TagTicketXML.TAG_PAGOS));
            ticket.setListaPagos(getPagos(xml.getNodo(TagTicketXML.TAG_PAGOS)));
            return ticket;
        } catch (XMLDocumentNodeNotFoundException ex) {
            String msg = "Error leyendo xml de ticket anterior." + ex.getMessage();
            log.error(msg, ex);
            throw new TicketException(msg, ex);
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
                } else if (mp == null) {
                    try {
                        mp = MediosPago.consultar(codMedioPago);
                    } catch (MedioPagoException e) {
                        log.warn("[Parseando Ticket] No se ha encontrado el medio de pago :" + codMedioPago, e);
                    }
                    if (mp == null) {
                        log.warn("[Parseando Ticket] No se ha encontrado el medio de pago :" + codMedioPago);
                        log.warn("[Parseando Ticket] Se utilizará forma de pago efectivo por defecto.");
                        mp = MediosPago.getInstancia().getPagoEfectivo();
                    }
                }
                Pago p = null;
                String descuento = null;
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
                    descuento = linea.getNodo(TagTicketXML.TAG_PAGO_DESCUENTO).getValue();

                    String numeroTarjeta = null;
                    try {
                        numeroTarjeta = linea.getNodo(TagTicketXML.TAG_PAGO_NUMERO_TARJETA).getValue();
                    } catch (XMLDocumentNodeNotFoundException ex) {

                    }

                    PagoCredito pc;
                    try {
                        String numeroCredito = linea.getNodo(TagTicketXML.TAG_PAGO_NUMERO_CREDITO).getValue();
                        String cedulaTarjetaCD = linea.getNodo(TagTicketXML.TAG_PAGO_CLIENTE_TARJETA).getValue();
                        pc = new PagoCreditoSK(null, null, null, Integer.valueOf(numeroCredito), cedulaTarjetaCD);

                    } catch (XMLDocumentNodeNotFoundException e) {
                        pc = new PagoCredito(null, null, null);
                    }

                    PlanPagoCredito ppc = new PlanPagoCredito();
                    ppc.setNumCuotas(new BigDecimal(cuotas));
                    ppc.setImporteInteres(new BigDecimal(importeInteres));
                    pc.setPlanSeleccionado(ppc);
                    pc.setCodigoValidacionManual(autorizacion);
                    pc.setAuditoria(Integer.valueOf(auditoria));
                    pc.setUidFacturacion(uidFacturacion);
                    pc.setDescuento(descuento == null ? BigDecimal.ZERO : new BigDecimal(descuento));
                    pc.setCvv(cvv);
                    pc.setFechaCaducidad(fechaCaducidadS);
                    if (numeroTarjeta != null) {
                        pc.setNumeroTarjetaReimp(numeroTarjeta);
                    }
                    pc.setPagoActivo(Pago.PAGO_TARJETA);
                    p = (Pago) pc;
                } catch (XMLDocumentNodeNotFoundException e) {
                    // Comprobamos si el pago es de tipo Giftcard                    
                    if (mp != null && mp.isGiftCard()) {
                        p = new PagoGiftCard(null, null);
                        descuento = linea.getNodo(TagTicketXML.TAG_PAGO_DESCUENTO).getValue();
                        p.setDescuento(descuento == null ? BigDecimal.ZERO : new BigDecimal(descuento));
                        p.setReferencia(linea.getNodo(TagTicketXML.TAG_PAGO_INFO1).getValue());

                    } else {
                        p = new Pago(null, null);
                        descuento = linea.getNodo(TagTicketXML.TAG_PAGO_DESCUENTO).getValue();
                        p.setDescuento(descuento == null ? BigDecimal.ZERO : new BigDecimal(descuento));
                    }
                }

                p.setTotal(new BigDecimal(linea.getNodo(TagTicketXML.TAG_PAGO_TOTAL).getValue()));
                p.setUstedPaga(new BigDecimal(linea.getNodo(TagTicketXML.TAG_PAGO_USTED_PAGA).getValue()));
                p.setEntregado(new BigDecimal(linea.getNodo(TagTicketXML.TAG_PAGO_ENTREGADO).getValue()));
                p.setMedioPagoActivo(mp);
                listaPagos.add(p);

                // Si el pago es con una nota de crédito                
                if (mp != null && mp.isNotaCredito()) {
                    p.setReferencia(linea.getNodo(TagTicketXML.TAG_PAGO_NOTA_CREDITO).getValue());
                } // Comprobamos si es un Bono de Efectivo
                else if (mp != null && mp.isBonoEfectivo()) {
                    p.setReferencia(linea.getNodo(TagTicketXML.TAG_PAGO_INFO1).getValue());
                } else {
                    // Comprobamos si es una Letra
                    try {
                        String uidLetra = linea.getNodo(TagTicketXML.TAG_PAGO_LETRA_UID).getValue();
                        p.setReferencia(uidLetra);
                        mp.setCreditoDirecto(true);
                    } catch (XMLDocumentNodeNotFoundException e) {
                        // no hacemos nada
                    }
                }
            }
            return listaPagos;
        } catch (XMLDocumentNodeNotFoundException ex) {
            String msg = "Error leyendo xml de pagos anterior." + ex.getMessage();
            log.error(msg, ex);
            throw new TicketException(msg, ex);
        }
    }

    public static XMLDocument getXMLDocumentTicket(TicketS ticket) throws TicketException {
        try {
            log.debug("Construyendo xml para el ticket: " + ticket.getUid_ticket());
            XMLDocument xml = new XMLDocument();
            XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_TIKET);
            xml.setRoot(root);

            // Construimos tag líneas
            XMLDocumentNode lineas = contruirTagLineas(xml, ticket);

            //Añadimos la cabecera
            root.añadirHijo(construirTagCabecera(xml, ticket));
            //Añadimos las lineas del ticket
            root.añadirHijo(lineas);
            //Añadimos los pagos del ticket
            root.añadirHijo(construirTagPagos(xml, ticket.getPagos()));
            //Añadimos los cupones
            root.añadirHijo(construirTagCupones(xml, ticket));
            //Añadimos las descripciones de promoción            
            root.añadirHijo(construirTagPromociones(xml, ticket));
            //Añadimos el tag puntos  
            if (ticket.getPuntosTicket().isPromocionActiva()) {
                root.añadirHijo(construirTagPuntos(xml, ticket));
            }

            log.debug("XML del ticket: ");
            log.debug(xml.getString());
            return xml;
        } catch (XMLDocumentException ex) {
            String msg = "Error generando XML del ticket: " + ex.getMessage();
            log.error(msg, ex);
            throw new TicketException(msg, ex);
        } catch (XMLDocumentTransformerException ex) {
            String msg = "Error generando XML del ticket: " + ex.getMessage();
            log.error(msg, ex);
            throw new TicketException(msg, ex);
        }
    }

    // cliente
    private static XMLDocumentNode construirTagCliente(XMLDocument xml, Cliente cliente) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_CLIENTE);
        root.añadirHijo(TagTicketXML.TAG_CLIENTE_TIPO_ID, cliente.getTipoIdentificacion());
        root.añadirHijo(TagTicketXML.TAG_CLIENTE_ID, cliente.getIdentificacion());
        root.añadirHijo(TagTicketXML.TAG_CLIENTE_NUM_AFILIADO, cliente.getCodigoTarjetaBabysClub());
        root.añadirHijo(TagTicketXML.TAG_CLIENTE_NOMBRE, cliente.getNombre());
        root.añadirHijo(TagTicketXML.TAG_CLIENTE_APELLIDOS, cliente.getApellido());

        if (cliente.isAplicaTarjetaAfiliado()) {
            root.añadirHijo(TagTicketXML.TAG_CLIENTE_TARJETA_SUPERMAXI, Cadena.ofuscarTarjeta(cliente.getTarjetaAfiliacion().getNumero()));
            root.añadirHijo(TagTicketXML.TAG_CLIENTE_TARJETA_AFILIACION_TIPO, cliente.getTarjetaAfiliacion().getTipoAfiliacion());
            root.añadirHijo(TagTicketXML.TAG_CLIENTE_TARJETA_AFILIACION_NUMERO, cliente.getTarjetaAfiliacion().getNumero());
        }

        return root;

    }

    // cajero
    private static XMLDocumentNode construirTagCajero(XMLDocument xml, UsuarioBean usuario) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_CAJERO);
        root.añadirHijo(TagTicketXML.TAG_USUARIO_ID, usuario.getIdUsuario().toString());
        root.añadirHijo(TagTicketXML.TAG_USUARIO_DES, usuario.getDesUsuario());
        root.añadirHijo(TagTicketXML.TAG_USUARIO_COD, usuario.getUsuario());
        return root;
    }

    private static XMLDocumentNode construirTagAutorizador(XMLDocument xml, UsuarioBean usuario) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_AUTORIZADOR);
        root.añadirHijo(TagTicketXML.TAG_USUARIO_ID, usuario.getIdUsuario().toString());
        root.añadirHijo(TagTicketXML.TAG_USUARIO_COD, usuario.getUsuario());
        root.añadirHijo(TagTicketXML.TAG_USUARIO_DES, usuario.getDesUsuario());
        return root;
    }

    // vendedor
    private static XMLDocumentNode construirTagVendedor(XMLDocument xml, Vendedor vendedor) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_VENDEDOR);
        root.añadirHijo(TagTicketXML.TAG_VENDEDOR_ID, vendedor.getCodvendedor());
        root.añadirHijo(TagTicketXML.TAG_VENDEDOR_NOMBRE, vendedor.getNombreVendedor());
        root.añadirHijo(TagTicketXML.TAG_VENDEDOR_APELLIDOS, vendedor.getApellidosVendedor());
        return root;
    }

    // linea
    private static XMLDocumentNode construirTagLinea(XMLDocument xml, LineaTicket linea) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_LINEA);
        root.añadirAtributo(TagTicketXML.ATR_LINEA_IDLINEA, linea.getIdlinea().toString());
        root.añadirHijo(TagTicketXML.TAG_LINEA_CODART, linea.getArticulo().getCodart());
        if (linea.getReferenciaKit() != null) {
            root.añadirHijo(construirTagRefKit(xml, linea.getReferenciaKit()));
        }
        if (linea.getReferenciaGarantia() != null) {
            root.añadirHijo(construirTagRefGarantia(xml, linea.getReferenciaGarantia(), linea.getCantidad()));
        }
        root.añadirHijo(TagTicketXML.TAG_LINEA_DESART, linea.getArticulo().getDesart());
        root.añadirHijo(TagTicketXML.TAG_LINEA_CODBARRAS, linea.getCodigoBarras());
        root.añadirHijo(TagTicketXML.TAG_LINEA_MODELO, linea.getArticulo().getModelo());
        root.añadirHijo(TagTicketXML.TAG_LINEA_CODIMP, linea.getArticulo().getCodimp());
        root.añadirHijo(TagTicketXML.TAG_LINEA_CANTIDAD, linea.getCantidad().toString());
        root.añadirHijo(TagTicketXML.TAG_LINEA_PRECIO_TOTAL_TARIFA_ORIGEN, Numero.redondear(linea.getPrecioTotalTarifaOrigen()).toString());
        root.añadirHijo(TagTicketXML.TAG_LINEA_PRECIO_TARIFA_ORIGEN, Numero.redondear(linea.getPrecioTarifaOrigen()).toString());
        root.añadirHijo(TagTicketXML.TAG_LINEA_DESCUENTO_FINAL, String.valueOf(linea.getDescuentoFinal()));
        if (linea.getArticulo().getGarantiaOriginal() != null && linea.getArticulo().getGarantiaOriginal() > 0) {
            root.añadirHijo(TagTicketXML.TAG_LINEA_GARANTIA_ORIGINAL, String.valueOf(linea.getArticulo().getGarantiaOriginal()));
        }
        if (linea.getDatosAdicionales() != null) {
            root.añadirHijo(TagTicketXML.TAG_LINEA_DESCUENTO, linea.getDatosAdicionales().getDescuento().toString());
            root.añadirHijo(TagTicketXML.TAG_LINEA_ENVIO_DOMICILIO, linea.getDatosAdicionales().isEnvioDomicilio() ? "S" : "N");
            root.añadirHijo(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR, linea.getDatosAdicionales().isRecogidaPosterior() ? "S" : "N");
            if (linea.getDatosAdicionales().getAutorizador() != null) {
                root.añadirHijo(TagTicketXML.TAG_LINEA_AUTORIZADOR, linea.getDatosAdicionales().getAutorizador());
            }
        } else {
            root.añadirHijo(TagTicketXML.TAG_LINEA_DESCUENTO, linea.getDescuento() != null ? linea.getDescuento().toString() : "0");
        }
        root.añadirHijo(TagTicketXML.TAG_LINEA_PRECIO_TOTAL, Numero.redondear(linea.getPrecioTotal()).toString());
        root.añadirHijo(TagTicketXML.TAG_LINEA_PRECIO, Numero.redondear(linea.getPrecio()).toString());
        root.añadirHijo(TagTicketXML.TAG_LINEA_IMPORTE_TOTAL, Numero.redondear(linea.getImporteTotal()).toString());
        root.añadirHijo(TagTicketXML.TAG_LINEA_IMPORTE, Numero.redondear(linea.getImporte()).toString());
        root.añadirHijo(TagTicketXML.TAG_LINEA_IMPORTE_TOTAL_FINAL, Numero.redondear(linea.getImporteTotalFinalPagado()).toString());
        root.añadirHijo(TagTicketXML.TAG_LINEA_IMPORTE_FINAL, Numero.redondear(linea.getImporteFinalPagado()).toString());
        root.añadirHijo(TagTicketXML.TAG_LINEA_INTERES, Numero.redondear(linea.getInteres()).toString());
        if (linea.getArticulo().getCodimp().equals(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL)) {
            root.añadirHijo(TagTicketXML.TAG_PORCENTAJE_IVA, Sesion.getEmpresa().getPorcentajeIva().toString());
        } else {
            root.añadirHijo(TagTicketXML.TAG_PORCENTAJE_IVA, "0");
        }
        return root;
    }

    // promocion
    private static XMLDocumentNode construirTagPromocion(XMLDocument xml, PromocionLineaTicket promocion) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_PROMOCION);
        root.añadirHijo(TagTicketXML.TAG_PROMO_ID, promocion.getIdPromocion().toString());
        root.añadirHijo(TagTicketXML.TAG_PROMO_ID_TIPO, promocion.getIdTipoPromocion().toString());
        root.añadirHijo(TagTicketXML.TAG_PROMO_DES_TIPO, promocion.getDesTipoPromocion());
        root.añadirHijo(TagTicketXML.TAG_PROMO_TEXTO, promocion.getTextoPromocion());
        if (promocion.isTipoLineaMultiple() || promocion.isTipoLineaUnitaria()) {
            root.añadirHijo(TagTicketXML.TAG_PROMO_PRECIO_TARIFA, promocion.getPrecioTarifa().toString());
            root.añadirHijo(TagTicketXML.TAG_PROMO_PRECIO_TOTAL_TARIFA, promocion.getPrecioTarifaTotal().toString());
        }
        root.añadirHijo(TagTicketXML.TAG_PROMO_CANT_PROMO, promocion.getCantidadPromocion().toString());
        root.añadirHijo(TagTicketXML.TAG_PROMO_IMPORTE_PROMO, promocion.getImportePromocion() == null ? "" : promocion.getImportePromocion().toString());
        root.añadirHijo(TagTicketXML.TAG_PROMO_IMPORTE_TOTAL_PROMO, promocion.getImporteTotalPromocion() == null ? "" : promocion.getImporteTotalPromocion().toString());
        root.añadirHijo(TagTicketXML.TAG_PROMO_IMPORTE_TOTAL_AHORRO, Numero.redondear(promocion.getImporteTotalAhorro()).toString());
        root.añadirHijo(TagTicketXML.TAG_PROMO_IMPORTE_AHORRO, Numero.redondear(promocion.getImporteAhorro()).toString());
        return root;
    }

    //LineaPromocion
    private static XMLDocumentNode construirTagLineaPromocion(XMLDocument xml, TicketS ticket, LineaTicket linea) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_LINEAS_PROMOCIONES);
        if (ticket.getLineasPromocion().containsKey(linea)) {
            for (Long idPromocion : ticket.getLineasPromocion().get(linea)) {
                XMLDocumentNode lineaPromocion = new XMLDocumentNode(xml, TagTicketXML.TAG_LINEA_PROMOCION);
                lineaPromocion.añadirHijo(TagTicketXML.ATR_LINEA_IDLINEA, linea.getIdlinea().toString());
                lineaPromocion.añadirHijo(TagTicketXML.TAG_PROMO_ID, idPromocion.toString());
                lineaPromocion.añadirHijo(TagTicketXML.TAG_PROMO_ID_TIPO, Sesion.getPromocion(idPromocion).getIdTipoPromocion().toString());
                root.añadirHijo(lineaPromocion);
            }
        }
        return root;
    }

    private static XMLDocumentNode construirTagSukupones(XMLDocument xml, LineaTicket linea) {
        List<SukuponLinea> sukuponesEmitidos = linea.getSukuponesEmitidos();
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_SUKUPONES);
        for (SukuponLinea sukupon : sukuponesEmitidos) {
            XMLDocumentNode sukuponXML = new XMLDocumentNode(xml, TagTicketXML.TAG_SUKUPON);
            sukuponXML.añadirHijo(TagTicketXML.ATR_LINEA_IDLINEA, linea.getIdlinea().toString());
            sukuponXML.añadirHijo(TagTicketXML.TAG_PROMO_ID, sukupon.getSukupon().getIdPromocion().toString());
            sukuponXML.añadirHijo(TagTicketXML.TAG_CUPONES_ID_CUPON, sukupon.getSukupon().getIdCupon().toString());
            sukuponXML.añadirHijo(TagTicketXML.TAG_CUPONES_CODALM, sukupon.getSukupon().getCodAlmacen());
            sukuponXML.añadirHijo(TagTicketXML.TAG_SUKUPON_AUSPICIANTE, sukupon.getAuspiciante());
            sukuponXML.añadirHijo(TagTicketXML.TAG_SUKUPON_VALOR, sukupon.getValor().toString());
            root.añadirHijo(sukuponXML);
        }
        return root;
    }

    // pago
    private static XMLDocumentNode construirTagPago(XMLDocument xml, Pago pago, Integer idPago) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_PAGO);
        root.añadirAtributo(TagTicketXML.ATR_ID_PAGO, idPago.toString());
        root.añadirHijo(TagTicketXML.TAG_PAGO_CODMEDPAG, pago.getMedioPagoActivo().getCodMedioPago());
        root.añadirHijo(TagTicketXML.TAG_PAGO_DESMEDPAG, pago.getMedioPagoActivo().getDesMedioPago());
        root.añadirHijo(TagTicketXML.TAG_PAGO_TOTAL, pago.getTotal().toString());
        root.añadirHijo(TagTicketXML.TAG_PAGO_TOTAL_SIN_IVA, String.valueOf(pago.getTotalSinIva()));
        if (pago.getMedioPagoActivo().isRetencion()) {
            root.añadirHijo(TagTicketXML.TAG_PAGO_USTED_PAGA, pago.getTotal().toString());
            root.añadirHijo(TagTicketXML.TAG_PAGO_USTED_PAGA_SIN_IVA, String.valueOf(Numero.redondear(pago.getTotalSinIva())));
            root.añadirHijo(TagTicketXML.TAG_PAGO_ENTREGADO, pago.getTotal().toString());
        } else {
            root.añadirHijo(TagTicketXML.TAG_PAGO_USTED_PAGA, pago.getUstedPaga().toString());
            root.añadirHijo(TagTicketXML.TAG_PAGO_USTED_PAGA_SIN_IVA, String.valueOf(pago.getUstedPagaSinIva()));
            root.añadirHijo(TagTicketXML.TAG_PAGO_ENTREGADO, pago.getEntregado().toString());
        }
        root.añadirHijo(TagTicketXML.TAG_PAGO_INTERES_IMP, pago.getImporteInteres().toString());
        root.añadirHijo(TagTicketXML.TAG_PAGO_INTERES_PORC, pago.getPorcentajeInteres().toString());
        if (pago.getVuelta() != null) {
            root.añadirHijo(TagTicketXML.TAG_PAGO_VUELTA, pago.getVuelta());
        }
        root.añadirHijo(TagTicketXML.TAG_PAGO_AHORRO, pago.getAhorro().toString());
        root.añadirHijo(TagTicketXML.TAG_PAGO_DESCUENTO, pago.getDescuento().toPlainString());
        root.añadirHijo(TagTicketXML.TAG_PAGO_INFO1, pago.getInformacionExtra1());
        root.añadirHijo(TagTicketXML.TAG_PAGO_INFO2, pago.getInformacionExtra2());
        root.añadirHijo(TagTicketXML.TAG_PAGO_INFO3, pago.getInformacionExtra3());
        if (pago.getMedioPagoActivo().isTarjetaCredito() || pago.getMedioPagoActivo().isBonoSuperMaxiNavidad() || pago.getMedioPagoActivo().isCreditoFilial()) {
            root.añadirHijo(TagTicketXML.TAG_PAGO_AUDITORIA, String.valueOf(((PagoCredito) pago).getAuditoria()));
            root.añadirHijo(TagTicketXML.TAG_PAGO_AUTORIZACION, ((PagoCredito) pago).getNumeroAutorizacionTarjeta());
            root.añadirHijo(TagTicketXML.TAG_PAGO_TIPO_AUTORIZACION, ((PagoCredito) pago).isValidadoManual() ? "M" : "E");
            root.añadirHijo(TagTicketXML.TAG_PAGO_FACTURACION, ((PagoCredito) pago).getUidFacturacion());
            root.añadirHijo(TagTicketXML.TAG_PAGO_FECHA_CADUCIDAD, ((PagoCredito) pago).getFechaCaducidadTarjeta());
            root.añadirHijo(TagTicketXML.TAG_PAGO_CVV, ((PagoCredito) pago).getCVVFacturacion());
            root.añadirHijo(TagTicketXML.TAG_PAGO_NUMERO_TARJETA, ((PagoCredito) pago).getNumeroTarjeta());
            root.añadirHijo(TagTicketXML.TAG_PAGO_LECTURA_MANUAL, ((PagoCredito) pago).getLecturaBandaManual());
        }
        if (pago.getMedioPagoActivo().isTarjetaSukasa() && !pago.getMedioPagoActivo().isBonoSuperMaxiNavidad() && !pago.getMedioPagoActivo().isCreditoFilial()) {
            root.añadirHijo(TagTicketXML.TAG_PAGO_CLIENTE_TARJETA, ((PagoCreditoSK) pago).getPlastico() != null ? ((PagoCreditoSK) pago).getPlastico().getCedulaCliente() : ((TarjetaCreditoSK) ((PagoCreditoSK) pago).getTarjetaCredito()).getPlastico().getCedulaCliente());
            if (((PagoCreditoSK) pago).isPosfechado()) {
                root.añadirHijo(TagTicketXML.TAG_PAGO_MESES_POSFECHADO, ((PagoCreditoSK) pago).getMesesPosfechado().toString());
            }
        }
        if (pago.getMedioPagoActivo().isCreditoTemporal()) {
            PagoCreditoLetra pagoLetra = (PagoCreditoLetra) pago;
            root.añadirHijo(TagTicketXML.TAG_PAGO_LETRA_UID, pagoLetra.getLetra().getUidLetra());
            root.añadirHijo(TagTicketXML.TAG_PAGO_LETRA_INTERES, String.valueOf(pagoLetra.getLetra().getIntereses()));
            XMLDocumentNode cuotas = new XMLDocumentNode(xml, TagTicketXML.TAG_PAGO_LETRA_CUOTAS);
            for (LetraCuotaBean cuota : pagoLetra.getLetra().getCuotas()) {
                XMLDocumentNode cuotaNodo = new XMLDocumentNode(xml, TagTicketXML.TAG_PAGO_LETRA_CUOTA);
                cuotaNodo.añadirAtributo(TagTicketXML.ATR_PAGO_LETRA_CUOTA_NUM, cuota.getCuota().toString());
                cuotaNodo.añadirHijo(TagTicketXML.TAG_PAGO_LETRA_CUOTA_VALOR, cuota.getValor().toString());
                cuotaNodo.añadirHijo(TagTicketXML.TAG_PAGO_LETRA_CUOTA_VEN, cuota.getFechaVencimiento().getString());
                cuotas.añadirHijo(cuotaNodo);
            }
            root.añadirHijo(cuotas);
        }
        if (pago.getAutorizador() != null) {
            root.añadirHijo(TagTicketXML.TAG_PAGO_AUTORIZADOR, pago.getAutorizador());
        }
        if (pago.isPromocionAplicada()) {
            XMLDocumentNode promociones = new XMLDocumentNode(xml, TagTicketXML.TAG_PROMOCIONES);
            for (PromocionPagoTicket promocionPago : pago.getPromociones()) {
                XMLDocumentNode promocion;
                if (promocionPago.getTipoPromocion().isPromocionTipoNCuotasGratis()) {
                    promocion = new XMLDocumentNode(xml, TagTicketXML.TAG_PROMOCION_N_CUOTAS);
                } else {
                    promocion = new XMLDocumentNode(xml, TagTicketXML.TAG_PROMOCION_MESES_GRACIA);
                }
                promocion.añadirHijo(TagTicketXML.TAG_PROMO_ID, promocionPago.getIdPromocion().toString());
                promocion.añadirHijo(TagTicketXML.ATR_ID_PAGO, idPago.toString());
                promocion.añadirHijo(TagTicketXML.TAG_PROMO_ID_TIPO, promocionPago.getIdTipoPromocion().toString());
                promocion.añadirHijo(TagTicketXML.TAG_PROMO_DES_TIPO, promocionPago.getDesTipoPromocion());
                promocion.añadirHijo(TagTicketXML.TAG_PROMO_NUM_CUOTAS, promocionPago.getNumCuotasPromocion().toString());
                promocion.añadirHijo(TagTicketXML.TAG_PROMO_IMPORTE_CUOTA, promocionPago.getImporteCuota().toString());
                promocion.añadirHijo(TagTicketXML.TAG_PROMO_IMPORTE_PROMO, promocionPago.getImporteBasePromocion().toString());
                promocion.añadirHijo(TagTicketXML.TAG_PROMO_IMPORTE_AHORRO, promocionPago.getImporteAhorro().toString());
                promociones.añadirHijo(promocion);
            }
            root.añadirHijo(promociones);
        }

        // Autorizador del Pago
        if (pago instanceof PagoNotaCredito) {
            root.añadirHijo(TagTicketXML.TAG_PAGO_NOTA_CREDITO, ((PagoNotaCredito) pago).getUidNotaCredito());
        }
        if (pago.isPagoTarjeta()) {
            root.añadirHijo(construirTagPagoPlan(xml, ((PagoCredito) pago).getPlanSeleccionado()));
            if (pago instanceof PagoCreditoSK) {
                root.añadirHijo(TagTicketXML.TAG_PAGO_NUMERO_CREDITO, ((PagoCreditoSK) pago).getPlastico() != null ? ((PagoCreditoSK) pago).getPlastico().getNumeroCredito().toString() : ((TarjetaCreditoSK) ((PagoCreditoSK) pago).getTarjetaCredito()).getPlastico().getNumeroCredito().toString());
            }
        }
        return root;
    }

    /**
     * @author Gabriel Simbania
     * @param nodePagos
     * @param ticket
     * @return
     * @throws XMLDocumentNodeNotFoundException
     */
    public static PagosTicket construirPagoByTagPagoCredito(XMLDocumentNode nodePagos, TicketS ticket) throws XMLDocumentNodeNotFoundException, Exception {

        PagosTicket pagosTicket = new PagosTicket(ticket);
        List<XMLDocumentNode> pagoList = nodePagos.getHijos(TagTicketXML.TAG_PAGO);

        //Connection conn = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            for (XMLDocumentNode xmlPago : pagoList) {

                try {
                    String uidFacturacion = xmlPago.getNodo(TagTicketXML.TAG_PAGO_FACTURACION).getValue();

                    FacturacionTarjeta facturacionTarjeta = FacturacionTarjetasDao.consultarFacturacionTarjeta(em, uidFacturacion);
                    if (facturacionTarjeta != null) {
                        PagoCredito pagoCredito = facturacionTarjeta.convertirFacturacionAPago(facturacionTarjeta, null);
                        pagoCredito.setTotal(xmlPago.getNodo(TagTicketXML.TAG_PAGO_TOTAL).getValueAsBigDecimal());
                        pagoCredito.getPlanSeleccionado().setAhorroMasInteres(BigDecimal.ZERO);
                        pagoCredito.setMensajePromocional(facturacionTarjeta.getMensajePromocional());
                        pagoCredito.setPagoActivo(Pago.PAGO_TARJETA);
                        pagoCredito.getPinpadRespuesta().setModoLectura(EnumTipoLectura.findTipoLecturaByTipo(facturacionTarjeta.getTipoLecturaTarjeta()).getModoLectura());
                        pagoCredito.getPinpadRespuesta().setNombreGrupoTarjeta("");
                        pagoCredito.getPinpadRespuesta().setMerchantId(String.format("%09d", facturacionTarjeta.getCodigoLocal()));
                        pagoCredito.getPinpadRespuesta().setNumeroAutorizacion(facturacionTarjeta.getNumeroAutorizacion());
                        pagoCredito.getPinpadRespuesta().setNombreTarjetaHabiente(facturacionTarjeta.getTarjetaHabiente());
                        String secuencialTransaccion = facturacionTarjeta.getSecuencialTransaccion();
                        if (secuencialTransaccion == null) {
                            secuencialTransaccion = "";
                        }
                        pagoCredito.getPinpadRespuesta().setSecuencialTransaccion(secuencialTransaccion);
                        pagoCredito.getPinpadRespuesta().setNumeroLote(facturacionTarjeta.getLote());
                        pagoCredito.getPinpadRespuesta().setTerminalId(facturacionTarjeta.getTerminalId());
                        pagosTicket.addPago(pagoCredito);
                    }
                } catch (XMLDocumentNodeNotFoundException | NoResultException ex) {

                }
            }
        } finally {
            em.close();
        }
        return pagosTicket;
    }

    // plan pago
    private static XMLDocumentNode construirTagPagoPlan(XMLDocument xml, PlanPagoCredito plan) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_PAGO_PLAN);
        root.añadirHijo(TagTicketXML.TAG_PAGO_PLAN_DESC, plan.getPlan());
        root.añadirHijo(TagTicketXML.TAG_PAGO_PLAN_N_CUOTAS, new Integer(plan.getNumCuotas()).toString());
        root.añadirHijo(TagTicketXML.TAG_PAGO_PLAN_CUOTA, plan.getCuota().toString());
        return root;
    }

    // datos facturacion
    private static XMLDocumentNode construirTagFacturacion(XMLDocument xml, FacturacionTicketBean facturacion) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_DATOS_FACTURACION);
        root.añadirHijo(TagTicketXML.TAG_DATOS_FACT_TIPO_ID, facturacion.getTipoDocumento());
        root.añadirHijo(TagTicketXML.TAG_DATOS_FACT_ID, facturacion.getDocumento());
        root.añadirHijo(TagTicketXML.TAG_DATOS_FACT_NOMBRE, facturacion.getNombre());
        root.añadirHijo(TagTicketXML.TAG_DATOS_FACT_APELLIDOS, facturacion.getApellidos());
        root.añadirHijo(TagTicketXML.TAG_DATOS_FACT_DIRECCION, facturacion.getDireccion());
        root.añadirHijo(TagTicketXML.TAG_DATOS_FACT_CIUDAD, facturacion.getProvincia());
        root.añadirHijo(TagTicketXML.TAG_DATOS_FACT_TELEFONO, facturacion.getTelefono());
        return root;
    }

    // totales
    private static XMLDocumentNode construirTagTotales(XMLDocument xml, TotalesXML totales) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_TOTALES);
        //root.añadirHijo(TagTicketXML.TAG_TOTALES_TOTAL_SIN_PROMOCIONES, totales.getTotalSinPromociones().toString());
        //root.añadirHijo(TagTicketXML.TAG_TOTALES_TOTAL_PROMOCIONES_LINEAS, totales.getTotalPromocionesLineas().toString());
        //root.añadirHijo(TagTicketXML.TAG_TOTALES_TOTAL_PROMOCIONES_TOTALES, totales.getTotalPromocionesTotales().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_IMPORTE_TARIFA_ORIGEN, totales.getImporteTarifaOrigen().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_IMPORTE_TOTAL_TARIFA_ORIGEN, totales.getImporteTotalTarifaOrigen().toString());
        //root.añadirHijo(TagTicketXML.TAG_TOTALES_TOTAL_PROMOCIONES, totales.getTotalPromociones().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_TOTAL_A_PAGAR, totales.getTotalAPagar().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_INTERES, totales.getInteres().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_TOTAL_AHORRO_PAGOS, totales.getTotalAhorroPagos().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_AHORRO_PAGOS, totales.getAhorroPagos().setScale(2, RoundingMode.HALF_UP).toPlainString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_TOTAL_PAGADO, totales.getTotalPagado().toString()); // sin intereses 
        root.añadirHijo(TagTicketXML.TAG_TOTALES_PORCENTAJE_DTO_PAGOS, totales.getTotalDtoPagos().setScale(2, RoundingMode.HALF_UP).toPlainString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_BASE, totales.getBase().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_IMPUESTOS, totales.getImpuestos().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_IMPUESTOS_ICE, totales.getImpuestosIce().toString());
        if (PromocionTipoDtoManualTotal.isActiva()) {
            root.añadirHijo(TagTicketXML.TAG_TOTALES_DESCUENTO_GLOBAL, totales.getTotalPromocionesCabecera().toString());
            root.añadirHijo(TagTicketXML.TAG_PORCENTAJE_DESCUENTO_GLOBAL, PromocionTipoDtoManualTotal.getDescuento().toString());
        }

        XMLDocumentNode subtotalesIva = new XMLDocumentNode(xml, TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA);
        for (ConfigImpPorcentaje subtotalImpuestos : totales.getSubtotalesImpuestos().values()) {
            XMLDocumentNode subtotalIva = new XMLDocumentNode(xml, TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA_SUBTOTAL);
            subtotalIva.añadirHijo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA_CODIGO, subtotalImpuestos.getCodImpuesto());
            subtotalIva.añadirHijo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA_PORCENTAJE, subtotalImpuestos.getPorcentaje().toString());
            subtotalIva.añadirHijo(TagTicketXML.TAG_TOTALES_SUBTOTALES_IVA_IMPORTE, subtotalImpuestos.getTotal().toString());
            subtotalesIva.añadirHijo(subtotalIva);
        }
        root.añadirHijo(subtotalesIva);
        root.añadirHijo(TagTicketXML.TAG_TOTALES_DEDUCIBLE_ALIMENTACION, totales.getTotalDeducibleAlimentacion().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_DEDUCIBLE_MEDICINA, totales.getTotalDeducibleMedicina().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_DEDUCIBLE_ROPA, totales.getTotalDeducibleRopa().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_DEDUCIBLE_EDUCACION, totales.getTotalDeducibleEducacion().toString());
        root.añadirHijo(TagTicketXML.TAG_TOTALES_DEDUCIBLE_VIVIENDA, totales.getTotalDeducibleVivienda().toString());

        XMLDocumentNode promociones = new XMLDocumentNode(xml, TagTicketXML.TAG_TOTALES_PROMOCIONES);
        for (PromocionLineaTicket promocion : totales.getPromocionesATotal()) {
            if (promocion.isTipoManual()) {
                continue;
            }
            promociones.añadirHijo(construirTagPromocion(xml, promocion));
        }
        root.añadirHijo(promociones);

        return root;
    }

    // cabecera
    private static XMLDocumentNode construirTagCabecera(XMLDocument xml, TicketS ticket) {

        Fecha fechaFinDev;
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_CABECERA);
        root.añadirHijo(TagTicketXML.TAG_UID_TICKET, ticket.getUid_ticket());
        root.añadirHijo(TagTicketXML.TAG_VERSION, Constantes.VERSION_POS);
        if (ticket.getFecha() == null) {
            Fecha fechaDate = new Fecha();
            root.añadirHijo(TagTicketXML.TAG_FECHA, fechaDate.getStringHora());
            fechaFinDev = fechaDate;
        } else {
            root.añadirHijo(TagTicketXML.TAG_FECHA, ticket.getFecha().getStringHora());
            fechaFinDev = new Fecha(ticket.getFecha().getDate());
        }
        //Modificación para añadir la fecha de devolución máxima del ticket
        if (ticket.getCliente().isSocio() && ticket.getCliente().getSemanaEmbarazo() != null
                && ticket.getCliente().getSemanaEmbarazo().compareTo(new BigInteger("42")) < 0
                && ticket.getCliente().getSemanaEmbarazo().compareTo(BigInteger.ZERO) > 0) {
            int diasAmpliacionFechaDev = (42 - ticket.getCliente().getSemanaEmbarazo().intValue()) * 7;
            fechaFinDev.sumaDias(diasAmpliacionFechaDev + Variables.getVariableAsInt(Variables.POS_VENTA_TICKET_VALIDEZ_DIAS));
        } else {
            fechaFinDev.sumaDias(Variables.getVariableAsInt(Variables.POS_VENTA_TICKET_VALIDEZ_DIAS));
        }
        root.añadirHijo(TagTicketXML.TAG_FECHA_FIN_DEVOLUCION, fechaFinDev.getStringHora());
        root.añadirHijo(TagTicketXML.TAG_ID_TIENDA, ticket.getTienda());
        root.añadirHijo(TagTicketXML.TAG_CODCAJA, ticket.getCodcaja());
        root.añadirHijo(TagTicketXML.TAG_ID_TICKET, String.valueOf(ticket.getId_ticket()));
        root.añadirHijo(TagTicketXML.TAG_UID_DIARIO_CAJA, ticket.getUid_diario_caja());
        root.añadirHijo(TagTicketXML.TAG_UID_CAJERO_CAJA, ticket.getUid_cajero_caja());
        root.añadirHijo(TagTicketXML.TAG_COD_FORMATO, String.valueOf(Sesion.getTienda().getCodFormato()));
        root.añadirHijo(TagTicketXML.TAG_EMPRESA, Sesion.getEmpresa().getCodemp());

        // Datos adicionales para reimpresión de factura
        root.añadirHijo(TagTicketXML.TAG_NOMBRE_EMPRESA, Sesion.getEmpresa().getNombreComercial());
        root.añadirHijo(TagTicketXML.TAG_NOMBRE_TIENDA, Sesion.getTienda().getSriTienda().getDesalm());
        root.añadirHijo(TagTicketXML.TAG_DIRECCION_EMPRESA, Sesion.getEmpresa().getDomicilio());
        root.añadirHijo(TagTicketXML.TAG_DIRECCON_LOCAL, Sesion.getTienda().getSriTienda().getDomicilio());
        root.añadirHijo(TagTicketXML.TAG_REGISTRO_MERCANTIL, Sesion.getEmpresa().getCif()); //RUC
        root.añadirHijo(TagTicketXML.TAG_REGION, Sesion.getTienda().getCodRegion().getDesregion());
        root.añadirHijo(TagTicketXML.TAG_NRO_RESOLUCION_CONTRIBUYENTE, Sesion.getEmpresa().getNroResolucionContribuyente());

        root.añadirHijo(TagTicketXML.TAG_AUTORIZACION_SRI, Sesion.getEmpresa().getNumAutorizacion().toString());
        Fecha fechaInicioValidezF = new Fecha(Sesion.getEmpresa().getFechaInicioAuorizacion());
        Fecha fechaFinValidezF = new Fecha(Sesion.getEmpresa().getFechafinAutorizacion());

        root.añadirHijo(TagTicketXML.TAG_FECHA_INICIO_VALIDEZ_SRI, fechaInicioValidezF.getString("dd 'de' MMMMM 'del' yyyy"));
        root.añadirHijo(TagTicketXML.TAG_FECHA_FIN_VALIDEZ_SRI, fechaFinValidezF.getString("dd 'de' MMMMM 'del' yyyy"));
        if (ticket.getPagos() != null && ticket.getPagos().isCompensacionAplicada()) {
            root.añadirHijo(TagTicketXML.TAG_PORCENTAJE_COMPENSACION, VariablesAlm.getVariable(VariablesAlm.PORCENTAJE_COMPENSACION_GOBIERNO));
        }
        if (ticket.getObservaciones() != null && !"".equals(ticket.getObservaciones())) {
            root.añadirHijo(TagTicketXML.TAG_OBSERVACION, ticket.getObservaciones());
        }

        root.añadirHijo(construirTagCliente(xml, ticket.getCliente()));
        root.añadirHijo(construirTagFacturacion(xml, ticket.getFacturacion()));
        root.añadirHijo(construirTagCajero(xml, ticket.getCajero()));
        if (ticket.getAutorizadorVenta() != null) {
            root.añadirHijo(construirTagAutorizador(xml, ticket.getAutorizadorVenta()));
        }
        if (ticket.getVendedor() != null) {
            root.añadirHijo(construirTagVendedor(xml, ticket.getVendedor()));
        }
        root.añadirHijo(construirTagTotales(xml, ticket.getTotales()));
        root.añadirHijo(construirTagVentaOtroLocal(xml, ticket.getVentaEntreLocales(), ticket.getAutorizadorVentaOtroLocal()));
        if (ticket.getDatosEnvio() != null) {
            root.añadirHijo(construirTagDatosEnvio(xml, ticket.getDatosEnvio()));
        }
        if (ticket.getUsuarioAplazado() != null) {
            root.añadirHijo(construirTagAplazado(xml, ticket.getUsuarioAplazado()));
        }

        return root;
    }

    //aplazado
    private static XMLDocumentNode construirTagAplazado(XMLDocument xml, UsuarioBean usuarioAplazado) {

        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_APLAZAR_VENTA);
        root.añadirHijo(TagTicketXML.TAG_ID_USUARIO_APLAZADO, usuarioAplazado.getIdUsuario().toString());
        root.añadirHijo(TagTicketXML.TAG_USUARIO_APLAZADO, usuarioAplazado.getUsuario());
        root.añadirHijo(TagTicketXML.TAG_DES_USUARIO_APLAZADO, usuarioAplazado.getDesUsuario());

        return root;
    }

    // lineas
    private static XMLDocumentNode contruirTagLineas(XMLDocument xml, TicketS ticket) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_LINEAS);
        for (int i = 0; i < ticket.getLineas().getNumLineas(); i++) {
            LineaTicket linea = ticket.getLineas().getLinea(i);
            linea.setIdlinea(i + 1);
            XMLDocumentNode nodoLinea = construirTagLinea(xml, linea);
            if (linea.isPromocionAplicada()) {
                nodoLinea.añadirHijo(construirTagPromocion(xml, linea.getPromocionLinea()));
            }
            if (linea.tieneSukuponesEmitidos()) {
                nodoLinea.añadirHijo(construirTagSukupones(xml, linea));
            }
            nodoLinea.añadirHijo(construirTagLineaPromocion(xml, ticket, linea));
            root.añadirHijo(nodoLinea);
        }
        return root;
    }

    // pagos
    private static XMLDocumentNode construirTagPagos(XMLDocument xml, PagosTicket pagos) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_PAGOS);
        int idPago = 1;
        for (Pago pago : pagos.getPagos()) {
            root.añadirHijo(construirTagPago(xml, pago, idPago));
            idPago++;
        }
        return root;
    }

    // Venta otro local
    private static XMLDocumentNode construirTagVentaOtroLocal(XMLDocument xml, VentaEntreLocales venta, String autorizador) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_VENTA_ENTRE_LOCALES);
        if (venta != null) {
            root.añadirHijo(construirTagVentaOtroLocalRecogida(xml, venta));
            root.añadirHijo(construirTagVentaOtroLocalVenta(xml, venta));
            root.añadirHijo(TagTicketXML.TAG_VENTA_ENTRE_LOCALES_AUTORIZADOR, autorizador);

        }
        return root;
    }

    private static XMLDocumentNode construirTagVentaOtroLocalRecogida(XMLDocument xml, VentaEntreLocales venta) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_VENTA_ENTRE_LOCALES_LOCAL_RECOGIDA);
        root.añadirHijo(TagTicketXML.TAG_VENTA_ENTRE_LOCALES_LOCAL_RECOGIDA_COD_LOCAL, venta.getCodTiendaOrigen());
        root.añadirHijo(TagTicketXML.TAG_VENTA_ENTRE_LOCALES_LOCAL_RECOGIDA_DES_LOCAL, venta.getDesTiendaOrigen());
        return root;
    }

    private static XMLDocumentNode construirTagVentaOtroLocalVenta(XMLDocument xml, VentaEntreLocales venta) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_VENTA_ENTRE_LOCALES_LOCAL_VENTA);
        root.añadirHijo(TagTicketXML.TAG_VENTA_ENTRE_LOCALES_LOCAL_VENTA_COD_LOCAL, venta.getCodTiendaDestino());
        root.añadirHijo(TagTicketXML.TAG_VENTA_ENTRE_LOCALES_LOCAL_VENTA_DES_LOCAL, venta.getDesTiendaDestino());
        root.añadirHijo(TagTicketXML.TAG_VENTA_ENTRE_LOCALES_CODIGO_CONFIRMACION, venta.getCodigoConfirmacion());
        return root;
    }

    // Datos de envío
    private static XMLDocumentNode construirTagDatosEnvio(XMLDocument xml, DatosDeEnvio envio) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_ENVIO_DOMICILIO);
        root.añadirHijo(TagTicketXML.TAG_ENVIO_DOMICILIO_NOMBRE, envio.getNombreEnvio());
        root.añadirHijo(TagTicketXML.TAG_ENVIO_DOMICILIO_APELLIDOS, envio.getApellidosEnvio());
        root.añadirHijo(TagTicketXML.TAG_ENVIO_DOMICILIO_DIRECCION, envio.getDireccion());
        root.añadirHijo(TagTicketXML.TAG_ENVIO_DOMICILIO_TELEFONO, envio.getTelefonoEnvio());
        root.añadirHijo(TagTicketXML.TAG_ENVIO_DOMICILIO_MOVIL, envio.getMovilEnvio());
        root.añadirHijo(TagTicketXML.TAG_ENVIO_DOMICILIO_HORARIO, envio.getHorarioEnvio());
        root.añadirHijo(TagTicketXML.TAG_ENVIO_DOMICILIO_CIUDAD, envio.getCiudad());
        return root;

    }

    // Referencia Kit instalación
    private static XMLDocumentNode construirTagRefKit(XMLDocument xml, KitReferencia referencia) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_KIT);
        if (referencia.getTicketOrigen() != null) {
            root.añadirHijo(TagTicketXML.TAG_KIT_UID_FACTURA, referencia.getTicketOrigen().getUid_ticket());
            root.añadirHijo(TagTicketXML.TAG_KIT_FACTURA, referencia.getTicketOrigen().getIdFactura());
        }
        root.añadirHijo(TagTicketXML.TAG_KIT_ARTICULO, referencia.getArticulo().getCodart());
        return root;
    }

    // Referencia garantía extendida
    private static XMLDocumentNode construirTagRefGarantia(XMLDocument xml, GarantiaReferencia referencia, int cantidad) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_GARANTIA);
        if (referencia.getTicketOrigen() != null) {
            root.añadirHijo(TagTicketXML.TAG_GARANTIA_UID_FACTURA, referencia.getTicketOrigen().getUid_ticket());
            root.añadirHijo(TagTicketXML.TAG_GARANTIA_FACTURA, referencia.getTicketOrigen().getIdFactura());
        }
        root.añadirHijo(TagTicketXML.TAG_GARANTIA_ARTICULO, referencia.getArticulo().getCodart());
        BigDecimal precioTotalFinalPagado = referencia.getLineaOrigen().getImporteTotalFinalPagado().divide(new BigDecimal(referencia.getLineaOrigen().getCantidad()), 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        root.añadirHijo(TagTicketXML.TAG_GARANTIA_PRECIO, precioTotalFinalPagado.toString());
        root.añadirHijo(TagTicketXML.TAG_GARANTIA_IMPORTE, precioTotalFinalPagado.multiply(new BigDecimal(cantidad)).setScale(2, RoundingMode.HALF_UP).toString());
        return root;

    }

    // cupones
    private static XMLDocumentNode construirTagCupones(XMLDocument xml, TicketS ticket) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_CUPONES);
        XMLDocumentNode emitidos = new XMLDocumentNode(xml, TagTicketXML.TAG_CUPONES_EMITIDOS);
        XMLDocumentNode aplicados = new XMLDocumentNode(xml, TagTicketXML.TAG_CUPONES_APLICADOS);
        for (Cupon cupon : ticket.getCuponesEmitidos()) {
            emitidos.añadirHijo(construirTagCupon(xml, cupon));
        }
        for (Cupon cupon : ticket.getCuponesAplicados()) {
            aplicados.añadirHijo(construirTagCupon(xml, cupon));
        }
        root.añadirHijo(emitidos);
        root.añadirHijo(aplicados);
        return root;
    }

    // cupon
    private static XMLDocumentNode construirTagCupon(XMLDocument xml, Cupon cupon) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_CUPONES_CUPON);
        root.añadirHijo(TagTicketXML.TAG_PROMO_ID, cupon.getPromocion().getIdPromocion().toString());
        root.añadirHijo(TagTicketXML.TAG_PROMO_ID_TIPO, cupon.getPromocion().getIdTipoPromocion().toString());
        root.añadirHijo(TagTicketXML.TAG_CUPONES_ID_CUPON, cupon.getIdCupon().toString());
        root.añadirHijo(TagTicketXML.TAG_CUPONES_CODALM, cupon.getCodAlmacen());
        return root;
    }

    // promociones
    private static XMLDocumentNode construirTagPromociones(XMLDocument xml, TicketS ticket) {
        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_PROMOCIONES);
        if (ticket.getTicketPromociones().isFacturaAsociadaDiaSocio()) {
            root.añadirHijo(TagTicketXML.TAG_PROMO_DIA_SOCIO, Sesion.promocionDiaSocio.getIdPromocion().toString());
            root.añadirHijo(TagTicketXML.TAG_PROMO_FACTURA_DIA_SOCIO, ticket.getTicketPromociones().getFacturaOrigenDiaSocio().getUid_ticket());
        }
        XMLDocumentNode descripciones = new XMLDocumentNode(xml, TagTicketXML.TAG_PROMOCIONES_DESCRIPCIONES);
        for (LineaEnTicket descripcion : ticket.getTicketPromociones().getPromocionesPrint()) {
            descripciones.añadirHijo(TagTicketXML.TAG_PROMOCIONES_DESCRIPCIONES_DESCRIPCION, descripcion.getTextoOriginal());
        }
        root.añadirHijo(descripciones);
        return root;
    }

    // puntos
    private static XMLDocumentNode construirTagPuntos(XMLDocument xml, TicketS ticket) {

        XMLDocumentNode root = new XMLDocumentNode(xml, TagTicketXML.TAG_PUNTOS);

        if (!ticket.getPuntosTicket().isCedePuntos()) {
            root.añadirHijo(TagTicketXML.TAG_PUNTOS_ANTERIORES, String.valueOf(ticket.getPuntosTicket().getPuntosAnteriores()));
        }
        if (ticket.getPuntosTicket().isConsumePuntos()) {
            root.añadirHijo(TagTicketXML.TAG_PUNTOS_CONSUMIDOS, String.valueOf(ticket.getPuntosTicket().getPuntosConsumidos()));
        }
        if (ticket.getPuntosTicket().isClienteAcumulaPuntos()) {
            root.añadirHijo(TagTicketXML.TAG_PUNTOS_ACUMULADOS, String.valueOf(ticket.getPuntosTicket().getPuntosAcumulados()));
        }
        if (!ticket.getPuntosTicket().isCedePuntos()) {
            root.añadirHijo(TagTicketXML.TAG_PUNTOS_SALDO, String.valueOf(ticket.getPuntosTicket().getNuevoSaldoPuntos()));
        }
        root.añadirHijo(TagTicketXML.TAG_PUNTOS_CLIENTE, ((ticket.getPuntosTicket().getClienteAcumulacion() != null && ticket.getPuntosTicket().getClienteAcumulacion().getCodcli() != null)) ? ticket.getPuntosTicket().getClienteAcumulacion().getCodcli() : ticket.getCliente().getCodcli());
        if (ticket.getPuntosTicket().isCedePuntos()) {
            root.añadirHijo(TagTicketXML.TAG_PUNTOS_CEDIDOS, String.valueOf(ticket.getPuntosTicket().getPuntosAcumulados()));
        }

        return root;
    }
}
