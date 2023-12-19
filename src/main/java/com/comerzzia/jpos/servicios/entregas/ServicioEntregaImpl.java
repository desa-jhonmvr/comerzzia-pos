/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.entregas;

import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.UsuarioDTO;
import com.comerzzia.jpos.dto.ventas.DocumentoDTO;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.entity.db.LogOperacionesDet;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.persistencia.devoluciones.articulos.ArticuloDevueltoBean;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogException;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.devoluciones.DevolucionesServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.tickets.xml.TagTicketXML;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.UtilUsuario;
import com.comerzzia.jpos.util.thread.ProcesoEncolarThread;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.InOutStream.UtilInputOutputStream;
import es.mpsistemas.util.log.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Gabriel Simbania
 */
public class ServicioEntregaImpl implements ServicioEntrega {

    private static final Logger LOG_POS = Logger.getMLogger(ServicioEntregaImpl.class);

    private static final char VALOR_P = 'P';
    private static final char VALOR_N = 'N';
    private static final char VALOR_E = 'E';

    @Override
    public void cambiaEstadoEntregado(String numeroDocumento, String observacion) throws TicketException, IOException, ParserConfigurationException, SAXException, TransformerException, Exception {

        if (Sesion.getUsuario() == null || Sesion.cajaActual == null || Sesion.cajaActual.getCajaActual() == null || Sesion.cajaActual.getCajaParcialActual() == null) {
            //Inicia sesion automatica
            LOG_POS.info("Inicio la sesion para el usuario " + Sesion.config.getUsuarioModoDesarrollo());
            Sesion.iniciaSesion(Sesion.config.getUsuarioVentaOnline(), Sesion.config.getPasswordVentaOnline());
        }

        String codAlm = numeroDocumento.substring(0, 3);
        String codCaja = numeroDocumento.substring(3, 6);
        Long idDocumento = Long.parseLong(numeroDocumento.substring(6));
        observacion = "CAMBIO DESDE EL ERP: " + observacion;
        List<LineaTicketOrigen> articulosAEliminar = new ArrayList<>();
        List<LineaTicketOrigen> deNadaAPendienteEnvioDomicilio = new ArrayList<>();
        List<LineaTicketOrigen> dePendienteEnvioDomicilioARecogidaPosterior = new ArrayList<>();
        List<LineaTicketOrigen> deRecogidaPosteriorAEntregadoRecogidaPosterior = new ArrayList<>();
        List<LineaTicketOrigen> deEntregadoRecogidaPosteriorANada = new ArrayList<>();
        List<ItemDTO> listaItemDTO = new ArrayList<>();

        TicketsAlm ticket = TicketService.consultarTicket(idDocumento, codCaja, codAlm);
        List<LineaTicketOrigen> lista = TicketService.consultarLineasTicket(ticket.getUidTicket());

        Map<Integer, ArticuloDevueltoBean> articulosDevueltos = DevolucionesServices.consultarArticulosDevueltos(ticket.getUidTicket());

        for (LineaTicketOrigen linea : lista) {
            linea.setTicket(ticket);
            linea.setEnvioDomicilioOriginal(linea.getEnvioDomicilio());
            linea.setRecogidaPosteriorOriginal(linea.getRecogidaPosterior());
            int cantidadArticulosDevueltos = 0;
            int idLinea = Integer.parseInt(linea.getLineaTicketOrigenPK().getIdLinea() + ""); // idLinea de la línea actual
            ArticuloDevueltoBean articulosDevueltosAcumulados = articulosDevueltos.get(idLinea); // acumulación de artículos devueltos para la línea
            if (articulosDevueltosAcumulados != null) {
                cantidadArticulosDevueltos = articulosDevueltosAcumulados.getCantidad();
            }
            linea.setCantidad(linea.getCantidad() - cantidadArticulosDevueltos);

            if (linea.isEnvioDomicilio()) {
                linea.setEnvioDomicilio(VALOR_N);
                linea.setRecogidaPosterior(VALOR_E);
                deRecogidaPosteriorAEntregadoRecogidaPosterior.add(linea);
            } else {
                articulosAEliminar.add(linea);
            }
        }
        lista.removeAll(articulosAEliminar);

        Iterator it = lista.iterator();
        while (it.hasNext()) {
            LineaTicketOrigen linea = (LineaTicketOrigen) it.next();
            if (linea.getCantidad() <= 0) {
                it.remove();
            }
        }

        if (lista.isEmpty()) {
            if (!articulosDevueltos.isEmpty()) {
                throw new TicketException("Esta factura tiene Nota de Credito. ");
            } else {
                throw new TicketException("No se han encontrado líneas de tickets para esa factura.");
            }
        }

        //Guardar LogOperaciones
        guardarLog(observacion, codAlm + "-" + codCaja + "-" + idDocumento, listaItemDTO, lista, ticket);

        //Actualizar las lineas
        for (LineaTicketOrigen lto : lista) {
            TicketService.modificarTicketDetalle(lto);
        }

        //Guardar Ticket
        procesaCambioEnvioDomicilio(deNadaAPendienteEnvioDomicilio, dePendienteEnvioDomicilioARecogidaPosterior, deRecogidaPosteriorAEntregadoRecogidaPosterior, deEntregadoRecogidaPosteriorANada, codAlm, codCaja, idDocumento, false, listaItemDTO, observacion);
        //Guardar Documentos Impresos
        actualizarDocumentoImpreso(deNadaAPendienteEnvioDomicilio, dePendienteEnvioDomicilioARecogidaPosterior, deRecogidaPosteriorAEntregadoRecogidaPosterior, deEntregadoRecogidaPosteriorANada, codAlm, codCaja, idDocumento);

    }

    @Override
    public void procesaCambioEnvioDomicilio(List<LineaTicketOrigen> deNadaAPendienteEnvioDomicilio,
            List<LineaTicketOrigen> dePendienteEnvioDomicilioARecogidaPosterior,
            List<LineaTicketOrigen> deRecogidaPosteriorAEntregadoRecogidaPosterior,
            List<LineaTicketOrigen> deEntregadoRecogidaPosteriorANada,
            String codAlm, String codCaja, Long idDocumento, boolean aplicativo,
            List<ItemDTO> itemDTOLista, String observacion) throws TicketException, IOException, ParserConfigurationException, SAXException, TransformerConfigurationException, TransformerException {

        TicketsAlm ticket = TicketService.consultarTicket(idDocumento, codCaja, codAlm);

        if (ticket != null) {

            byte[] a = ticket.getXMLTicket();
            if (a != null) {
                File xml = UtilInputOutputStream.transformardebytesAfile(a);
                Document documento = leerXmlyactualizarEntregaEnTicket(xml, deNadaAPendienteEnvioDomicilio,
                        dePendienteEnvioDomicilioARecogidaPosterior, deRecogidaPosteriorAEntregadoRecogidaPosterior,
                        deEntregadoRecogidaPosteriorANada);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                Result output = new StreamResult(xml);
                Source input = new DOMSource(documento);
                transformer.transform(input, output);

                ticket.setTicket(UtilInputOutputStream.trasformarFileabytes(xml));
                EntityManager em = null;
                try {
                    EntityManagerFactory emf = Sesion.getEmf();
                    em = emf.createEntityManager();
                    em.getTransaction().begin();
                    TicketService.modificarTicket(em, ticket);
                    em.getTransaction().commit();
                } catch (TicketException ex) {
                    if (em != null) {
                        em.getTransaction().rollback();
                    }
                    throw new TicketException("Error al modificar el ticket " + ex.getMessage());
                }

                if (aplicativo) {
                    if (!itemDTOLista.isEmpty()) {
                        generarEnvioADomicilio(itemDTOLista, ticket, observacion);
                    }
                }
                LOG_POS.debug("Ticket actualizado correctamente.");
            } else {
                String mensaje = "Xml blob no encontrador.";
                throw new TicketException(mensaje);

            }
        } else {
            String mensaje = "Ticket no encontrado.";
            throw new TicketException(mensaje);
        }

    }

    @Override
    public void actualizarDocumentoImpreso(List<LineaTicketOrigen> deNadaAPendienteEnvioDomicilio,
            List<LineaTicketOrigen> dePendienteEnvioDomicilioARecogidaPosterior,
            List<LineaTicketOrigen> deRecogidaPosteriorAEntregadoRecogidaPosterior,
            List<LineaTicketOrigen> deEntregadoRecogidaPosteriorANada,
            String codAlm, String codCaja, Long idDocumento) throws Exception {

        try {
            DocumentosBean documentoG = DocumentosService.consultarDoc(DocumentosImpresosBean.TIPO_FACTURA, codAlm, codCaja, String.valueOf(idDocumento));

            if (documentoG != null) {
                List<DocumentosImpresosBean> impresos = documentoG.getImpresos();
                if (impresos != null) {
                    for (int i = 0; i < impresos.size(); i++) {
                        if (impresos.get(i).isTipoDocumentoFactura()) {
                            byte[] a = null;
                            a = impresos.get(i).getImpreso();
                            if (a != null) {
                                File xml = UtilInputOutputStream.transformardebytesAfile(a);
                                Document documento = leerXmlyactualizarEntrega(xml, deNadaAPendienteEnvioDomicilio,
                                        dePendienteEnvioDomicilioARecogidaPosterior, deRecogidaPosteriorAEntregadoRecogidaPosterior,
                                        deEntregadoRecogidaPosteriorANada);

                                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                                Result output = new StreamResult(xml);
                                Source input = new DOMSource(documento);
                                transformer.transform(input, output);

                                byte[] result = null;
                                result = UtilInputOutputStream.trasformarFileabytes(xml);
                                impresos.get(i).setImpreso(result);

                                DocumentosService.updateDocumentosImpresos(impresos.get(i));

                                LOG_POS.debug("Documento impreso actualizado correctamente.");

                            } else {
                                throw new TicketException("Xml blob no encontrador.");
                            }
                            break;
                        }
                    }
                }

            }

        } catch (Exception e) {
            String mensaje = "Error al leer y actualizar el documento impreso.";
            LOG_POS.error(mensaje + e);
            throw new Exception(mensaje);

        }

    }

    /**
     *
     * @param xml
     * @param deNadaAPendienteEnvioDomicilio
     * @param dePendienteEnvioDomicilioARecogidaPosterior
     * @param deRecogidaPosteriorAEntregadoRecogidaPosterior
     * @param deEntregadoRecogidaPosteriorANada
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TicketException
     */
    private Document leerXmlyactualizarEntregaEnTicket(File xml,
            List<LineaTicketOrigen> deNadaAPendienteEnvioDomicilio,
            List<LineaTicketOrigen> dePendienteEnvioDomicilioARecogidaPosterior,
            List<LineaTicketOrigen> deRecogidaPosteriorAEntregadoRecogidaPosterior,
            List<LineaTicketOrigen> deEntregadoRecogidaPosteriorANada) throws ParserConfigurationException, SAXException, IOException, TicketException {
        // 1. cargar el XML original
        Document doc = null;
        String mensajeErrorEstructura = "Error en la estructura del documento impreso.";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = new FileInputStream(xml);
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        InputSource source = new InputSource(reader);
        doc = builder.parse(source);

        Node raiz = null;
        raiz = doc.getChildNodes().item(0);
        raiz = doc.getElementsByTagName("ticket").item(0);

        Node lineas = null;
        if (raiz != null) {
            if (raiz.getNodeName().equals("ticket")) {
                if (raiz.getChildNodes().getLength() >= 2) {
                    lineas = raiz.getChildNodes().item(1);
                    if (lineas.getNodeName().equals("lineas")) {
                        boolean encuentraItemED = false;
                        boolean encuentraItemRP = false;
                        Node referenciaInsert = null;
                        Element newNodeED = null;
                        Element newNodeRP = null;
                        //For por las lineas
                        for (int i = 0; i < lineas.getChildNodes().getLength(); i++) {
                            Node lineaX = lineas.getChildNodes().item(i);
                            Element elementX = (Element) lineaX;

                            String codMarcaItemEnXML = "";
                            codMarcaItemEnXML = elementX.getElementsByTagName("codArticulo").item(0).getTextContent().trim();

                            int cantidadItemEnXML = 0;
                            cantidadItemEnXML = Integer.parseInt(elementX.getElementsByTagName("cantidad").item(0).getTextContent().trim());

                            short idLineaEnXML = 0;
                            idLineaEnXML = Short.parseShort(elementX.getAttribute("idlinea").trim());

                            //Pasar de Nada a Pendiente Envio
                            for (int j = 0; j < deNadaAPendienteEnvioDomicilio.size(); j++) {
                                //deNadaAPendienteEnvioDomicilio.get(j).getLineaTicketOrigenPK().getIdLinea()
                                if (deNadaAPendienteEnvioDomicilio.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && deNadaAPendienteEnvioDomicilio.get(j).getCantidad() == cantidadItemEnXML && idLineaEnXML == deNadaAPendienteEnvioDomicilio.get(j).getLineaTicketOrigenPK().getIdLinea()) {
                                    //Encontro el item
                                    encuentraItemED = false;
                                    encuentraItemRP = false;
                                    referenciaInsert = null;
                                    newNodeED = null;
                                    newNodeRP = null;
                                    for (int k = 0; k < lineaX.getChildNodes().getLength(); k++) {
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals(TagTicketXML.TAG_ENVIO_DOMICILIO)) {
                                            encuentraItemED = true;
                                            lineaX.getChildNodes().item(k).setTextContent("S");
                                        }
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR)) {
                                            encuentraItemRP = true;
                                            lineaX.getChildNodes().item(k).setTextContent("N");
                                        }
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals("precioTotal")) {
                                            referenciaInsert = lineaX.getChildNodes().item(k);
                                        }
                                    }
                                    if (!encuentraItemED) {
                                        //Insertar el item envioDomicilio antes del item precioTotal si existe y sino como ultimo elemento
                                        if (referenciaInsert != null) {
                                            newNodeED = doc.createElement(TagTicketXML.TAG_ENVIO_DOMICILIO);
                                            newNodeED.setTextContent("S");
                                            lineaX.insertBefore(newNodeED, referenciaInsert);
                                        } else {
                                            newNodeED = doc.createElement(TagTicketXML.TAG_ENVIO_DOMICILIO);
                                            newNodeED.setTextContent("S");
                                            lineaX.appendChild(newNodeED);
                                        }
                                    }
                                    if (!encuentraItemRP) {
                                        //Insertar el item recogidaPosterior despues del item envioDomicilio si existe y sino como ultimo elemento
                                        if (referenciaInsert != null) {
                                            newNodeRP = doc.createElement(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR);
                                            newNodeRP.setTextContent("N");
                                            lineaX.insertBefore(newNodeRP, referenciaInsert);
                                        } else {
                                            newNodeRP = doc.createElement(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR);
                                            newNodeRP.setTextContent("N");
                                            lineaX.appendChild(newNodeRP);
                                        }
                                    }
                                }
                            }

                            //Pasar de Pendiente Envio a Recogida Posterior
                            for (int j = 0; j < dePendienteEnvioDomicilioARecogidaPosterior.size(); j++) {
                                if (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && dePendienteEnvioDomicilioARecogidaPosterior.get(j).getCantidad() == cantidadItemEnXML && idLineaEnXML == dePendienteEnvioDomicilioARecogidaPosterior.get(j).getLineaTicketOrigenPK().getIdLinea()) {
                                    //Encontro el item
                                    encuentraItemED = false;
                                    encuentraItemRP = false;
                                    referenciaInsert = null;
                                    newNodeED = null;
                                    newNodeRP = null;

                                    for (int k = 0; k < lineaX.getChildNodes().getLength(); k++) {
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals(TagTicketXML.TAG_ENVIO_DOMICILIO)) {
                                            encuentraItemED = true;
                                            lineaX.getChildNodes().item(k).setTextContent("N");
                                        }
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR)) {
                                            encuentraItemRP = true;
                                            lineaX.getChildNodes().item(k).setTextContent("S");
                                        }
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals("precioTotal")) {
                                            referenciaInsert = lineaX.getChildNodes().item(k);
                                        }
                                    }
                                    if (!encuentraItemED) {
                                        //Insertar el item envioDomicilio antes del item precioTotal si existe y sino como ultimo elemento
                                        if (referenciaInsert != null) {
                                            newNodeED = doc.createElement(TagTicketXML.TAG_ENVIO_DOMICILIO);
                                            newNodeED.setTextContent("N");
                                            lineaX.insertBefore(newNodeED, referenciaInsert);
                                        } else {
                                            newNodeED = doc.createElement(TagTicketXML.TAG_ENVIO_DOMICILIO);
                                            newNodeED.setTextContent("N");
                                            lineaX.appendChild(newNodeED);
                                        }
                                    }
                                    if (!encuentraItemRP) {
                                        //Insertar el item recogidaPosterior despues del item envioDomicilio si existe y sino como ultimo elemento
                                        if (referenciaInsert != null) {
                                            newNodeRP = doc.createElement(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR);
                                            newNodeRP.setTextContent("S");
                                            lineaX.insertBefore(newNodeRP, referenciaInsert);
                                        } else {
                                            newNodeRP = doc.createElement(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR);
                                            newNodeRP.setTextContent("S");
                                            lineaX.appendChild(newNodeRP);
                                        }
                                    }
                                }
                            }

                            //Pasar de Recogida Posterior a Entregado de una Recogida Posterior                                
                            for (int j = 0; j < deRecogidaPosteriorAEntregadoRecogidaPosterior.size(); j++) {
                                if (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getCantidad() == cantidadItemEnXML && idLineaEnXML == deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getLineaTicketOrigenPK().getIdLinea()) {
                                    //Encontro el item
                                    encuentraItemED = false;
                                    encuentraItemRP = false;
                                    referenciaInsert = null;
                                    newNodeED = null;
                                    newNodeRP = null;
                                    //revisar---
                                    for (int k = 0; k < lineaX.getChildNodes().getLength(); k++) {
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals(TagTicketXML.TAG_ENVIO_DOMICILIO)) {
                                            encuentraItemED = true;
                                            lineaX.getChildNodes().item(k).setTextContent("N");
                                        }
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR)) {
                                            encuentraItemRP = true;
                                            lineaX.getChildNodes().item(k).setTextContent("E");
                                        }
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals("precioTotal")) {
                                            referenciaInsert = lineaX.getChildNodes().item(k);
                                        }
                                    }
                                    if (!encuentraItemED) {
                                        //Insertar el item envioDomicilio antes del item precioTotal si existe y sino como ultimo elemento
                                        if (referenciaInsert != null) {
                                            newNodeED = doc.createElement(TagTicketXML.TAG_ENVIO_DOMICILIO);
                                            newNodeED.setTextContent("N");
                                            lineaX.insertBefore(newNodeED, referenciaInsert);
                                        } else {
                                            newNodeED = doc.createElement(TagTicketXML.TAG_ENVIO_DOMICILIO);
                                            newNodeED.setTextContent("N");
                                            lineaX.appendChild(newNodeED);
                                        }
                                    }
                                    if (!encuentraItemRP) {
                                        //Insertar el item recogidaPosterior despues del item envioDomicilio si existe y sino como ultimo elemento
                                        if (referenciaInsert != null) {
                                            newNodeRP = doc.createElement(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR);
                                            newNodeRP.setTextContent("E");
                                            lineaX.insertBefore(newNodeRP, referenciaInsert);
                                        } else {
                                            newNodeRP = doc.createElement(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR);
                                            newNodeRP.setTextContent("E");
                                            lineaX.appendChild(newNodeRP);
                                        }
                                    }
                                }
                            }

                            //Pasar de un Entregado Recogida Posterior a Nada
                            for (int j = 0; j < deEntregadoRecogidaPosteriorANada.size(); j++) {
                                if (deEntregadoRecogidaPosteriorANada.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && deEntregadoRecogidaPosteriorANada.get(j).getCantidad() == cantidadItemEnXML && idLineaEnXML == deEntregadoRecogidaPosteriorANada.get(j).getLineaTicketOrigenPK().getIdLinea()) {
                                    //Encontro el item
                                    encuentraItemED = false;
                                    encuentraItemRP = false;
                                    referenciaInsert = null;
                                    newNodeED = null;
                                    newNodeRP = null;

                                    for (int k = 0; k < lineaX.getChildNodes().getLength(); k++) {
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals(TagTicketXML.TAG_ENVIO_DOMICILIO)) {
                                            encuentraItemED = true;
                                            lineaX.getChildNodes().item(k).setTextContent("N");
                                        }
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR)) {
                                            encuentraItemRP = true;
                                            lineaX.getChildNodes().item(k).setTextContent("N");
                                        }
                                        if (lineaX.getChildNodes().item(k).getNodeName().equals("precioTotal")) {
                                            referenciaInsert = lineaX.getChildNodes().item(k);
                                        }
                                    }
                                    if (!encuentraItemED) {
                                        //Insertar el item envioDomicilio antes del item precioTotal si existe y sino como ultimo elemento
                                        if (referenciaInsert != null) {
                                            newNodeED = doc.createElement(TagTicketXML.TAG_ENVIO_DOMICILIO);
                                            newNodeED.setTextContent("N");
                                            lineaX.insertBefore(newNodeED, referenciaInsert);
                                        } else {
                                            newNodeED = doc.createElement(TagTicketXML.TAG_ENVIO_DOMICILIO);
                                            newNodeED.setTextContent("N");
                                            lineaX.appendChild(newNodeED);
                                        }
                                    }
                                    if (!encuentraItemRP) {
                                        //Insertar el item recogidaPosterior despues del item envioDomicilio si existe y sino como ultimo elemento
                                        if (referenciaInsert != null) {
                                            newNodeRP = doc.createElement(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR);
                                            newNodeRP.setTextContent("N");
                                            lineaX.insertBefore(newNodeRP, referenciaInsert);
                                        } else {
                                            newNodeRP = doc.createElement(TagTicketXML.TAG_LINEA_RECOGIDA_POSTERIOR);
                                            newNodeRP.setTextContent("N");
                                            lineaX.appendChild(newNodeRP);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        throw new TicketException(mensajeErrorEstructura);
                        //JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                    }
                } else {
                    throw new TicketException(mensajeErrorEstructura);
                    //JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                }
            } else {
                throw new TicketException(mensajeErrorEstructura);
                //JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
            }
        } else {
            throw new TicketException(mensajeErrorEstructura);
            //JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
        }

        return doc;
    }

    /**
     *
     * @param xml
     * @param deNadaAPendienteEnvioDomicilio
     * @param dePendienteEnvioDomicilioARecogidaPosterior
     * @param deRecogidaPosteriorAEntregadoRecogidaPosterior
     * @param deEntregadoRecogidaPosteriorANada
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws Exception
     */
    private Document leerXmlyactualizarEntrega(File xml,
            List<LineaTicketOrigen> deNadaAPendienteEnvioDomicilio,
            List<LineaTicketOrigen> dePendienteEnvioDomicilioARecogidaPosterior,
            List<LineaTicketOrigen> deRecogidaPosteriorAEntregadoRecogidaPosterior,
            List<LineaTicketOrigen> deEntregadoRecogidaPosteriorANada) throws ParserConfigurationException, SAXException, IOException, Exception {
        Document doc = null;
        String mensajeErrorEstructura = "Error en la estructura del documento impreso.";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = new FileInputStream(xml);
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            InputSource source = new InputSource(reader);
            doc = builder.parse(source);
        } catch (SAXException e1) {
            LOG_POS.error("Error de codificación en el fichero: " + e1.getMessage());
        } catch (IOException e2) {
            LOG_POS.error("Error de codificación en el fichero: " + e2.getMessage());
        } catch (Exception e3) {
            LOG_POS.error("Error de codificación en el fichero: " + e3.getMessage());
        }

        Node raiz = null;
        raiz = doc.getElementsByTagName("output").item(0);
        Node nodeTick = null;
        nodeTick = doc.getElementsByTagName("ticket").item(0);
        Element elementTick = null;

        Node lineaAnterior = null;
        Element lineaAnteriorElement = null;
        Node hijoLineaAnterior = null;

        Element lineaX = null;
        NodeList nodosTexto = null;
        int cantidad = 0;

        if (raiz != null) {
            if (raiz.getNodeName().equals("output")) {
                if (nodeTick != null) {
                    if (nodeTick.getNodeName().equals("ticket")) {
                        elementTick = (Element) nodeTick;

                        //For por las lineas
                        for (int i = 0; i < elementTick.getElementsByTagName(Constantes.LINE).getLength(); i++) {
                            lineaX = null;
                            nodosTexto = null;
                            cantidad = 0;

                            short tipoNodo = elementTick.getElementsByTagName(Constantes.LINE).item(i).getNodeType();
                            if (tipoNodo == Node.ELEMENT_NODE) {
                                lineaX = (Element) elementTick.getElementsByTagName(Constantes.LINE).item(i);
                                nodosTexto = lineaX.getElementsByTagName(Constantes.TEXT);
                                cantidad = lineaX.getElementsByTagName(Constantes.TEXT).getLength();
                            }
                            //Se asegura que es una linea de item
                            if (cantidad == 5 || cantidad == 6) {
                                Formatter fmt = new Formatter();
                                Formatter fmt2 = new Formatter();
                                String codMarcaItemEnXML = "";
                                String codMarca_ItemXML = "";
                                String codItem_ItemXML = "";
                                int cantidadItemEnXML = 0;
                                if (cantidad == 5) {
                                    codMarca_ItemXML = fmt.format("%04d", Integer.parseInt(nodosTexto.item(0).getTextContent().trim())).toString();
                                    codItem_ItemXML = fmt2.format("%04d", Integer.parseInt(nodosTexto.item(1).getTextContent().trim())).toString();
                                    codMarcaItemEnXML = codMarca_ItemXML + "." + codItem_ItemXML;
                                    cantidadItemEnXML = Integer.parseInt(nodosTexto.item(2).getTextContent().trim());
                                } else {
                                    codMarca_ItemXML = fmt.format("%04d", Integer.parseInt(nodosTexto.item(1).getTextContent().trim())).toString();
                                    codItem_ItemXML = fmt2.format("%04d", Integer.parseInt(nodosTexto.item(2).getTextContent().trim())).toString();
                                    codMarcaItemEnXML = codMarca_ItemXML + "." + codItem_ItemXML;
                                    cantidadItemEnXML = Integer.parseInt(nodosTexto.item(3).getTextContent().trim());
                                }

                                //Pasar de Nada a Pendiente Envio
                                for (int j = 0; j < deNadaAPendienteEnvioDomicilio.size(); j++) {
                                    if (deNadaAPendienteEnvioDomicilio.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && deNadaAPendienteEnvioDomicilio.get(j).getCantidad() == cantidadItemEnXML) {
                                        //Encontro el item
                                        lineaAnterior = null;
                                        lineaAnteriorElement = null;
                                        hijoLineaAnterior = null;
                                        if (deNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == VALOR_P) {
                                            //Buscar una linea que antes que indique que estaba como envio a domicilio, aunque este entrando en el caso: Nada a Pendiente Envio 
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;
                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (hijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                            deNadaAPendienteEnvioDomicilio.remove(j);
                                                            j--;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (deNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == VALOR_P) {
                                            //Buscar una linea que antes que indique que estaba como envio a domicilio, aunque este entrando en el caso: Nada a Pendiente Envio 
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;
                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (hijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************")) {
                                                            elementTick.removeChild(lineaAnterior);
                                                            elementTick.insertBefore(agregarLineaEntregaDomicilio(doc), lineaX);

                                                            deNadaAPendienteEnvioDomicilio.remove(j);
                                                            j--;
                                                        }
                                                    }
                                                }
                                            }
                                        } else if ((deNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == VALOR_N && deNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == VALOR_N)
                                                || (deNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == VALOR_E && deNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == VALOR_E)
                                                || (deNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == VALOR_E && deNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == VALOR_N)
                                                || (deNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == VALOR_N && deNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == VALOR_E)) {
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;
                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (!hijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************") && !hijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                            elementTick.insertBefore(agregarLineaEntregaDomicilio(doc), lineaX);

                                                            deNadaAPendienteEnvioDomicilio.remove(j);
                                                            j--;
                                                        }
                                                    } else {
                                                        elementTick.insertBefore(agregarLineaEntregaDomicilio(doc), lineaX);

                                                        deNadaAPendienteEnvioDomicilio.remove(j);
                                                        j--;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                //Pasar de Pendiente Envio a Recogida Posterior
                                for (int j = 0; j < dePendienteEnvioDomicilioARecogidaPosterior.size(); j++) {
                                    if (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && dePendienteEnvioDomicilioARecogidaPosterior.get(j).getCantidad() == cantidadItemEnXML) {
                                        //Encontro el item
                                        lineaAnterior = null;
                                        hijoLineaAnterior = null;
                                        if (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == VALOR_P) {
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;
                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (hijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                            elementTick.removeChild(lineaAnterior);

                                                            elementTick.insertBefore(agregarLineaPendienteEntregaXML(doc), lineaX);

                                                            dePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                    }
                                                    /*
                                                        else
                                                        {
                                                            elementTick.insertBefore(elementLinePendienteEntrega,lineaX);
                                                            dePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                     */
                                                }
                                            }
                                        } else if (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == VALOR_P) {
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;

                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (hijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************")) {
                                                            dePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                    }
                                                    /*
                                                        else
                                                        {
                                                            elementTick.insertBefore(elementLinePendienteEntrega,lineaX);
                                                            
                                                            dePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                     */
                                                }
                                            }
                                        } else if ((dePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == VALOR_N && dePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == VALOR_N)
                                                || (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == VALOR_E && dePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == VALOR_E)
                                                || (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == VALOR_E && dePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == VALOR_N)
                                                || (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == VALOR_N && dePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == VALOR_E)) {
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;

                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (!hijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************") && !hijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                            elementTick.insertBefore(agregarLineaPendienteEntregaXML(doc), lineaX);
                                                            dePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                    } else {
                                                        elementTick.insertBefore(agregarLineaPendienteEntregaXML(doc), lineaX);

                                                        dePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                        j--;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                //Pasar de Recogida Posterior a Entregado de una Recogida Posterior                                
                                for (int j = 0; j < deRecogidaPosteriorAEntregadoRecogidaPosterior.size(); j++) {
                                    if (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getCantidad() == cantidadItemEnXML) {
                                        //Encontro el item
                                        lineaAnterior = null;
                                        hijoLineaAnterior = null;
                                        if (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == VALOR_P) {
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;
                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (hijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                            elementTick.removeChild(lineaAnterior);
                                                            deRecogidaPosteriorAEntregadoRecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == VALOR_P) {
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;
                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (hijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************")) {
                                                            elementTick.removeChild(lineaAnterior);

                                                            deRecogidaPosteriorAEntregadoRecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                    }
                                                }
                                            }
                                        } else if ((deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == VALOR_N && deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == VALOR_N)
                                                || (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == VALOR_E && deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == VALOR_E)
                                                || (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == VALOR_E && deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == VALOR_N)
                                                || (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == VALOR_N && deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == VALOR_E)) {
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;
                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (!hijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************") && !hijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                            deRecogidaPosteriorAEntregadoRecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                    } else {
                                                        deRecogidaPosteriorAEntregadoRecogidaPosterior.remove(j);
                                                        j--;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                //Pasar de un Entregado Recogida Posterior a Nada
                                for (int j = 0; j < deEntregadoRecogidaPosteriorANada.size(); j++) {
                                    if (deEntregadoRecogidaPosteriorANada.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && deEntregadoRecogidaPosteriorANada.get(j).getCantidad() == cantidadItemEnXML) {
                                        //Encontro el item
                                        lineaAnterior = null;
                                        hijoLineaAnterior = null;
                                        if (deEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == VALOR_P) {
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;
                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (hijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                            elementTick.removeChild(lineaAnterior);

                                                            deEntregadoRecogidaPosteriorANada.remove(j);
                                                            j--;
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (deEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == VALOR_P) {
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;
                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (hijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************")) {
                                                            elementTick.removeChild(lineaAnterior);

                                                            deEntregadoRecogidaPosteriorANada.remove(j);
                                                            j--;
                                                        }
                                                    }
                                                }
                                            }
                                        } else if ((deEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == VALOR_N && deEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == VALOR_N)
                                                || (deEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == VALOR_E && deEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == VALOR_E)
                                                || (deEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == VALOR_E && deEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == VALOR_N)
                                                || (deEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == VALOR_N && deEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == VALOR_E)) {
                                            if (i >= 1) {
                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                if (lineaAnterior != null) {
                                                    lineaAnteriorElement = (Element) lineaAnterior;
                                                    if (lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                        hijoLineaAnterior = lineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                        if (!hijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************") && !hijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                            deEntregadoRecogidaPosteriorANada.remove(j);
                                                            j--;
                                                        }
                                                    } else {
                                                        deEntregadoRecogidaPosteriorANada.remove(j);
                                                        j--;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        throw new TicketException(mensajeErrorEstructura);
                        //JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                    }
                } else {
                    throw new TicketException(mensajeErrorEstructura);
                    //JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                }
            } else {
                throw new TicketException(mensajeErrorEstructura);
                //JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
            }
        } else {
            throw new TicketException(mensajeErrorEstructura);
            //JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
        }

        return doc;
    }

    /**
     *
     * @param doc
     * @return
     */
    private Element agregarLineaEntregaDomicilio(Document doc) {
        //Elemento entrega a domicilio
        Element elementLineEntregaDomicilio = doc.createElement(Constantes.LINE);
        Element elementTextEntregaDomicilio = doc.createElement(Constantes.TEXT);
        elementTextEntregaDomicilio.setAttribute("align", "left");
        elementTextEntregaDomicilio.setAttribute("length", "40");
        elementTextEntregaDomicilio.setTextContent("************ E. DOMICILIO *************");
        elementLineEntregaDomicilio.appendChild(elementTextEntregaDomicilio);
        return elementLineEntregaDomicilio;
    }

    /**
     *
     * @param doc
     * @return
     */
    private Element agregarLineaPendienteEntregaXML(Document doc) {
        //Elemento pendiente entrega
        Element elementLinePendienteEntrega = doc.createElement(Constantes.LINE);
        Element elementTextPendienteEntrega = doc.createElement(Constantes.TEXT);
        elementTextPendienteEntrega.setAttribute("align", "left");
        elementTextPendienteEntrega.setAttribute("length", "40");
        elementTextPendienteEntrega.setTextContent("************* P. ENTREGA **************");
        elementLinePendienteEntrega.appendChild(elementTextPendienteEntrega);
        return elementLinePendienteEntrega;
    }

    /**
     * @author Gabriel Simbania
     * @param idtos
     * @param ticket
     * @param observacion
     */
    private void generarEnvioADomicilio(List<ItemDTO> idtos, TicketsAlm ticket, String observacion) {

        try {
            String numeroFactura = ticket.getCodAlm() + ticket.getCodCaja() + String.format("%09d", ticket.getIdTicket());
            UsuarioDTO usuarioDTO = UtilUsuario.verificarUsuarioDTO(ticket.getUsuario());

            DocumentoDTO documentoDTO = new DocumentoDTO(numeroFactura, ticket.getCodAlm(), observacion, idtos, usuarioDTO);
            String envioDomicilioString = JsonUtil.objectToJson(documentoDTO);
            ProcesoEncolarThread envioDomicilioThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), envioDomicilioString, Variables.getVariable(Variables.QUEUE_ENVIO_DOMICILIO), Constantes.PROCESO_ENVIO_DOMICILIO, ticket.getUidTicket());
            envioDomicilioThread.start();
        } catch (Throwable e) {
            LOG_POS.error("No se pudo encolar el envio a domicilio " + e.getMessage());
        }

    }

    @Override
    public void guardarLog(String observacion, String referencia, List<ItemDTO> itemDTOLista,
            List<LineaTicketOrigen> lista, TicketsAlm ticket) {

        try {
            LogOperaciones logImpresiones = new LogOperaciones();
            logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logImpresiones.setFechaHora(new Date());
            logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
            logImpresiones.setReferencia(referencia);
            logImpresiones.setProcesado(VALOR_N);
            if (Sesion.getNumeroAutorizador() != null) {
                logImpresiones.setAutorizador(Sesion.getNumeroAutorizador());
            } else {
                logImpresiones.setAutorizador(Sesion.getUsuario().getUsuario());
            }
            logImpresiones.setCodOperacion("PENDIENTES ENTREGA");
            logImpresiones.setObservaciones(observacion);
            List<LogOperacionesDet> logDetalle = new ArrayList<LogOperacionesDet>();

            for (LineaTicketOrigen lto : lista) {
                if ((lto.getRecogidaPosteriorOriginal() != lto.getRecogidaPosterior()) || (lto.getEnvioDomicilioOriginal() != lto.getEnvioDomicilio())) {
                    String observacionLog = "Estado anterior envio domicilio: " + lto.getEnvioDomicilioOriginal() + ",recogida posterior: " + lto.getRecogidaPosteriorOriginal();
                    LogOperacionesDet logImpresionesDet = new LogOperacionesDet();
                    logImpresionesDet.setUidLog(logImpresiones.getLogOperacionesPK().getUid());
                    logImpresionesDet.setUidTicket(ticket.getUidTicket());
                    logImpresionesDet.setAutorizado(Sesion.getUsuario().getUsuario());
                    logImpresionesDet.setObservaciones(observacionLog);
                    logImpresionesDet.setCodart(lto.getCodart().getCodart());
                    logImpresionesDet.setCantidad(lto.getCantidad());
                    logImpresionesDet.setMotivoDescuento("PENDIENTES ENTREGA");
                    logDetalle.add(logImpresionesDet);
                }
            }

            /*for(ItemDTO itemDTO : itemDTOLista){
                LogOperacionesDet logImpresionesDet = new LogOperacionesDet();
                logImpresionesDet.setUidLog(logImpresiones.getLogOperacionesPK().getUid());
                logImpresionesDet.setUidTicket(ticket.getUidTicket());
                logImpresionesDet.setAutorizado(Sesion.getUsuario().getUsuario());
                logImpresionesDet.setObservaciones("Estado: " + itemDTO.getEstado());
                String codArt[] = itemDTO.getCodigoI().split("-");
                logImpresionesDet.setCodart(String.format("%04d", Integer.parseInt(codArt[0])) + "." + String.format("%04d", Integer.parseInt(codArt[1])));
                logImpresionesDet.setCantidad(Integer.parseInt(itemDTO.getCantidad().toString()));
                logImpresionesDet.setMotivoDescuento("PENDIENTES ENTREGA");
                logDetalle.add(logImpresionesDet);
            }*/
            insertarLog(logImpresiones, logDetalle);
        } catch (LogException ex) {
            LOG_POS.error("Error guardando el Log  de operación  ", ex);
        }
    }

    public void insertarLog(LogOperaciones logImpresiones, List<LogOperacionesDet> logDetalle) throws LogException {

        EntityManager em = null;
        try {
            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();
            ServicioLogAcceso.crearAccesoLog(logImpresiones, em);
            for (LogOperacionesDet det : logDetalle) {
                ServicioLogAcceso.crearAccesoLogDet(det, em);
            }
            em.getTransaction().commit();
        } catch (Throwable ex) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw new LogException("Error al registrar el log " + ex.getMessage());

        }
    }

}
