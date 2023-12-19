/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * JReservacionListaAbonos.java
 *
 * Created on 09-nov-2011, 1:57:42
 */
package com.comerzzia.jpos.gui.reservaciones;

import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.gui.components.JVentanaDialogo;
import com.comerzzia.jpos.gui.guiasremision.modelos.ArticulosEnvioDomicilioCellRenderer;
import com.comerzzia.jpos.gui.guiasremision.modelos.ArticulosEnvioDomicilioTableModel;
import com.comerzzia.jpos.persistencia.devoluciones.articulos.ArticuloDevueltoBean;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.devoluciones.DevolucionesServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.tickets.xml.TagTicketXML;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.InOutStream.UtilInputOutputStream;
import com.comerzzia.util.swing.acciones.Acciones;
import es.mpsistemas.util.log.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
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
import org.xml.sax.SAXException;

/**
 *
 * @author Administrador
 */
public class JReservacionListaArticulos extends JVentanaDialogo {

    private static Logger log = Logger.getMLogger(JReservacionListaArticulos.class);
    // private List<ReservaArticuloBean> listaAbonos;
    private Reservacion reserva;
    private TicketS tickets;

    private String codAlmacen;
    private String codCaja;
    private long idTicket;

    private List<LineaTicketOrigen> DeNadaAPendienteEnvioDomicilio;
    private List<LineaTicketOrigen> DePendienteEnvioDomicilioARecogidaPosterior;
    private List<LineaTicketOrigen> DeRecogidaPosteriorAEntregadoRecogidaPosterior;
    private List<LineaTicketOrigen> DeEntregadoRecogidaPosteriorANada;

    private List<LineaTicketOrigen> lista;

    /**
     * Creates new form JReservacionListaAbonos
     */
    public JReservacionListaArticulos() {
        initComponents();
    }

    public JReservacionListaArticulos(Reservacion reserva, TicketS tickets) {

        initComponents();
        addFunctionKeys();
        registraEventoEnterBoton();

        this.reserva = reserva;
        this.tickets = tickets;

        this.codAlmacen = tickets.getTienda();
        this.codCaja = tickets.getCodcaja();
        this.idTicket = tickets.getId_ticket();

        this.DeNadaAPendienteEnvioDomicilio = new ArrayList<LineaTicketOrigen>();
        this.DePendienteEnvioDomicilioARecogidaPosterior = new ArrayList<LineaTicketOrigen>();
        this.DeRecogidaPosteriorAEntregadoRecogidaPosterior = new ArrayList<LineaTicketOrigen>();
        this.DeEntregadoRecogidaPosteriorANada = new ArrayList<LineaTicketOrigen>();

        try {
            // lista = new ArrayList<LineaTicketOrigen>();
            tb_abonos.setDefaultRenderer(Object.class, new ArticulosEnvioDomicilioCellRenderer());
            TicketsAlm ticket = TicketService.consultarTicket(idTicket, codCaja, codAlmacen);
            lista = TicketService.consultarLineasTicket(ticket.getUidTicket());
            refrescarTablaArticulos(new ArticulosEnvioDomicilioTableModel(lista));

            //TODO: DEVOLUCION - COMPLETAR                    
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
            }

            Iterator it = lista.iterator();
            while (it.hasNext()) {
                LineaTicketOrigen linea = (LineaTicketOrigen) it.next();
                if (linea.getCantidad() <= 0) {
                    it.remove();
                }
            }
            refrescarTablaArticulos(new ArticulosEnvioDomicilioTableModel(lista));
            if (lista.size() >= 0) {
                ListSelectionModel selectionModel = tb_abonos.getSelectionModel();
                selectionModel.setSelectionInterval(0, 0);
                tb_abonos.requestFocus();
            }
//                    }
        } catch (NoResultException ex) {
            //  crearAdvertencia("No se han encontrado líneas de tickets de esa factura.");
        } catch (TicketException ex) {
            //   addError("Error consultando las líneas de ticket: "+ex.getMessage());
        } catch (NumberFormatException ex) {
            //   crearAdvertencia("No se han encontrado líneas de tickets para esa factura.");
        } catch (Exception ex) {
            //    addError("Error consultando las líneas de ticket: "+ex.getMessage());
        }

        if (lista.size() > 0) {
            ListSelectionModel selectionModel = tb_abonos.getSelectionModel();
            selectionModel.setSelectionInterval(0, 0);
        }

        /*listaAbonos= reserva.getReservacion().getReservaArticuloList();
        for(ReservaArticuloBean articulo : listaAbonos){
            articulo.setReserva(reserva.getReservacion());
        }
        initComponents();
       // super.registraEventoEnterBoton();
       
        //lb_reserva.setText(reserva.getReservacion().getUidReservacion());
        //lb_autor.setText(reserva.getReservacion().getCliente().getNombre() + " " + reserva.getReservacion().getCliente().getApellido());                
        
        // transparencia
        Border empty = new EmptyBorder(0, 0, 0, 0);
        js_tb_abonos.setViewportBorder(empty);
        tb_abonos.setBorder(empty);
        js_tb_abonos.getViewport().setOpaque(false);
        
        // Establecimiento de ancho de las columnas
        tb_abonos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb_abonos.getColumnModel().getColumn(0).setPreferredWidth(120);
        tb_abonos.getColumnModel().getColumn(1).setPreferredWidth(120);
        tb_abonos.getColumnModel().getColumn(2).setPreferredWidth(120);
        tb_abonos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tb_abonos.getColumnModel().getColumn(4).setPreferredWidth(200);
        iniciaFoco();
        tb_abonos.setDefaultRenderer(Object.class, new MostrarReservacionesCellRenderer());
        addFunctionKeys();*/
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        js_tb_abonos = new javax.swing.JScrollPane();
        tb_abonos = new javax.swing.JTable();
        b_ok = new com.comerzzia.jpos.gui.components.form.JButtonForm();

        setPreferredSize(new java.awt.Dimension(592, 523));

        jLabel5.setFont(jLabel5.getFont().deriveFont((float)18));
        jLabel5.setText("Configurar Entregas");

        tb_abonos.setModel(new ArticulosEnvioDomicilioTableModel(lista));
        tb_abonos.setMinimumSize(new java.awt.Dimension(0, 660));
        tb_abonos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_abonosKeyPressed(evt);
            }
        });
        js_tb_abonos.setViewportView(tb_abonos);

        b_ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        b_ok.setMnemonic('a');
        b_ok.setText("Aceptar");
        b_ok.setFont(b_ok.getFont().deriveFont((float)15));
        b_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_okActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(js_tb_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addComponent(b_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(js_tb_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(b_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void b_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_okActionPerformed
        accionAceptar();
}//GEN-LAST:event_b_okActionPerformed

    private void tb_abonosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_abonosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB && evt.isShiftDown()) {
            tb_abonos.transferFocusBackward();
        } else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            tb_abonos.transferFocus();
        }
    }//GEN-LAST:event_tb_abonosKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_ok;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane js_tb_abonos;
    private javax.swing.JTable tb_abonos;
    // End of variables declaration//GEN-END:variables

    @Override
    public void limpiarFormulario() {

    }

    public void iniciaFoco() {
        log.info("iniciando foco");
        b_ok.requestFocus();
    }

    @Override
    public void accionLeerTarjetaVD() {
    }

    /* private void addFunctionKeys() {

        log.info("Función de acciones de teclado");
        
        Acciones.crearAccionFocoTabla(this, tb_abonos);
        
        tb_abonos.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        tb_abonos.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
              accionEnter();
              log.info("ENTER");
            }  
        }); 
    }*/
 /* private void accionEnter(){
      try {
            int fila = tb_abonos.getSelectedRow();
            ReservaArticuloBean lto = reserva.getReservacion().getReservaArticuloList().get(fila);
            //LineaTicketOrigen lineaOrigen = TicketService.consultarLineaTicketOrigen(lto.getLineaTicketOrigenPK().getUidTicket(), new Long(lto.getLineaTicketOrigenPK().getIdLinea()));
            if(lto.getEntregado() == null || lto.getEntregado().equals("0"))
            {
                lto.setEntregado("1");
            }else  if(lto.getEntregado().equals("1")){
                lto.setEntregado("2");
            }else  if(lto.getEntregado().equals("2")){
                lto.setEntregado("0");
            }
            
            reserva.getReservacion().getReservaArticuloList().set(fila, lto);
            refrescarTabla();
            tb_abonos.setRowSelectionInterval(fila, fila);
            tb_abonos.changeSelection(fila, 0, false, false);
        }
        catch (IndexOutOfBoundsException e) {
            JPrincipal.getInstance().crearError("Debe de realizar una búsqueda antes de cambiar una línea de factura. ");
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            JPrincipal.getInstance().crearError("Error cambiando el estado de las líneas.");
        }
    }*/
 /* private void refrescarTabla() {
        tb_abonos.setDefaultRenderer(Object.class, new MostrarReservacionesCellRenderer());
        List<ReservaArticuloBean> articulos = reserva.getReservacion().getReservaArticuloList();
        MostrarReservacionesTableModel modelo = new MostrarReservacionesTableModel(articulos);
        tb_abonos.setModel(modelo);
        tb_abonos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb_abonos.getColumnModel().getColumn(0).setPreferredWidth(120);
        tb_abonos.getColumnModel().getColumn(1).setPreferredWidth(120);
        tb_abonos.getColumnModel().getColumn(2).setPreferredWidth(120);
        tb_abonos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tb_abonos.getColumnModel().getColumn(4).setPreferredWidth(200);
    }*/
    private void refrescarTablaArticulos(ArticulosEnvioDomicilioTableModel articulosEnvioDomicilioTableModel) {
        tb_abonos.setModel(articulosEnvioDomicilioTableModel);
//        tb_abonos.setDefaultRenderer(Object.class, new ArticulosEnvioDomicilioCellRenderer());
        tb_abonos.getColumnModel().getColumn(0).setPreferredWidth(100);
        tb_abonos.getColumnModel().getColumn(1).setPreferredWidth(300);
        tb_abonos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tb_abonos.getColumnModel().getColumn(3).setPreferredWidth(150);
    }

    private void accionEnter() {
        try {
            int fila = tb_abonos.getSelectedRow();
            LineaTicketOrigen lto = lista.get(fila);
            //LineaTicketOrigen lineaOrigen = TicketService.consultarLineaTicketOrigen(lto.getLineaTicketOrigenPK().getUidTicket(), new Long(lto.getLineaTicketOrigenPK().getIdLinea()));
            if (lto.getEnvioDomicilioOriginal() != 'E' && lto.getRecogidaPosteriorOriginal() != 'E') {
                if (lto.getEnvioDomicilio() == 'N' && lto.getRecogidaPosterior() == 'N') {
                    //Pasar de Nada a Pendiente Envio
                    lto.setEnvioDomicilio('P');
                    lto.setRecogidaPosterior('N');

                    this.DeNadaAPendienteEnvioDomicilio.add(lto);
                    //Quitar el codigo si esta en otro listado
                    DePendienteEnvioDomicilioARecogidaPosterior.remove(lto);
                    DeRecogidaPosteriorAEntregadoRecogidaPosterior.remove(lto);
                    DeEntregadoRecogidaPosteriorANada.remove(lto);
                } else if (lto.getEnvioDomicilio() == 'P' && lto.getRecogidaPosterior() != 'P') {
                    //Pasar de Pendiente Envio a Recogida Posterior
                    lto.setRecogidaPosterior('P');
                    lto.setEnvioDomicilio('N');

                    this.DePendienteEnvioDomicilioARecogidaPosterior.add(lto);

                    //Quitar el codigo si esta en otro listado
                    DeNadaAPendienteEnvioDomicilio.remove(lto);
                    DeRecogidaPosteriorAEntregadoRecogidaPosterior.remove(lto);
                    DeEntregadoRecogidaPosteriorANada.remove(lto);
                } else if (lto.getRecogidaPosterior() == 'P' && lto.getEnvioDomicilio() != 'P') {
                    //Pasar de Recogida Posterior a Entregado de una Recogida Posterior
                    lto.setEnvioDomicilio('N');
                    lto.setRecogidaPosterior('E');

                    this.DeRecogidaPosteriorAEntregadoRecogidaPosterior.add(lto);

                    //Quitar el codigo si esta en otro listado
                    DeNadaAPendienteEnvioDomicilio.remove(lto);
                    DePendienteEnvioDomicilioARecogidaPosterior.remove(lto);
                    DeEntregadoRecogidaPosteriorANada.remove(lto);
                } else if (lto.getRecogidaPosterior() == 'E' && lto.getEnvioDomicilio() != 'P') {
                    //Pasar de un Entregado Recogida Posterior a Nada
                    lto.setEnvioDomicilio('N');
                    lto.setRecogidaPosterior('N');

                    this.DeEntregadoRecogidaPosteriorANada.add(lto);

                    //Quitar el codigo si esta en otro listado
                    DeNadaAPendienteEnvioDomicilio.remove(lto);
                    DePendienteEnvioDomicilioARecogidaPosterior.remove(lto);
                    DeRecogidaPosteriorAEntregadoRecogidaPosterior.remove(lto);
                }
                /*else if(lto.getEnvioDomicilio() == 'E' && lto.getRecogidaPosterior() != 'P')
                {
                    //Pasar de un Envio a Domicilio a Nada
                    lto.setEnvioDomicilio('N');
                    lto.setRecogidaPosterior('N');
                    
                    this.DeEntregadoEnvioDomicilioANada.add(lto);                    
                    
                    //Quitar el codigo si esta en otro listado
                    DeNadaAPendienteEnvioDomicilio.remove(lto);
                    DePendienteEnvioDomicilioARecogidaPosterior.remove(lto);
                    DeRecogidaPosteriorAEntregadoRecogidaPosterior.remove(lto);
                }*/
                lista.set(fila, lto);
                refrescarTablaArticulos(new ArticulosEnvioDomicilioTableModel(lista));
                tb_abonos.setRowSelectionInterval(fila, fila);
                tb_abonos.changeSelection(fila, 0, false, false);
            }
        } catch (IndexOutOfBoundsException e) {
            //JPrincipal.getInstance().crearError("Debe de realizar una búsqueda antes de cambiar una línea de factura. ");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // JPrincipal.getInstance().crearError("Error cambiando el estado de las líneas.");
        }
    }

    private void addFunctionKeys() {

        log.info("Función de acciones de teclado");

        Acciones.crearAccionFocoTabla(this, tb_abonos);

        tb_abonos.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        tb_abonos.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                accionEnter();
            }
        });
    }

    @Override
    public void accionAceptar() {
        if (lista != null && !lista.isEmpty()) {
            //Actualizar las lineas
            List<LineaTicketOrigen> listaEntregados = new ArrayList<LineaTicketOrigen>();
            String error = "";
            for (LineaTicketOrigen lto : lista) {
                try {
//                    ventana_padre.crearVentanaObservaciones("");

                    TicketService.modificarTicketDetalle(lto);
                    //if (lto.isRecogidaPosteriorEntregado() &&  lto.getRecogidaPosteriorOriginal()!= 'E')
                    if (lto.getEnvioDomicilio() == 'P' || lto.getRecogidaPosterior() == 'P') {
                        listaEntregados.add(lto);

                    }
                } catch (TicketException e) {
                    error += " " + lto.getCodart().getDesart() + " ";
                }
            }

            //Actualizar el Ticket
            try {
                EntityManagerFactory emf = Sesion.getEmf();
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();

                TicketsAlm ticket = TicketService.consultarTicket(Long.valueOf(this.idTicket), this.codCaja, this.codAlmacen);
                if (ticket != null) {

                    byte[] a = ticket.getXMLTicket();
                    if (a != null) {
                        File xml = UtilInputOutputStream.transformardebytesAfile(a);
                        Document documento = leerXmlyactualizarEntregaEnTicket(xml);

                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        Result output = new StreamResult(xml);
                        Source input = new DOMSource(documento);
                        transformer.transform(input, output);

                        ticket.setTicket(UtilInputOutputStream.trasformarFileabytes(xml));
                        TicketService.modificarTicket(em, ticket);
                        em.getTransaction().commit();

                        log.debug("Ticket actualizado correctamente.");
                    } else {
                        // JPrincipal.getInstance().crearError("Xml blob no encontrador.");
                    }
                } else {
                    //    JPrincipal.getInstance().crearError("Ticket no encontrado.");
                }

            } catch (NoResultException ex) {
                // JPrincipal.getInstance().crearError("Ticket no encontrado.");
                log.error("Ticket no encontrado.");
            } catch (TicketException ex) {
                log.error(ex);
            } catch (IOException ex) {
                log.error("Error en conversion file bytes ." + ex);
                //Logger.getLogger(CambioFechaBlob.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                log.error("Error al escribir cambio xml ." + ex);
            } catch (TransformerConfigurationException ex) {
                //Logger.getLogger(CambioFechaBlob.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                log.error("Error al escribir cambio xml ." + ex);
            } catch (ParserConfigurationException ex) {
                log.error("Error al leer xml y actulaizar el ticket." + ex);
            } catch (Exception e) {
                //  JPrincipal.getInstance().crearError("Error al leer y actulaizar el ticket.");
                log.error("Error al leer xml y actulaizar el ticket." + e);
            }

            //Actualizar el documento impreso        
            try {
                DocumentosBean documentoG = DocumentosService.consultarDoc(DocumentosImpresosBean.TIPO_FACTURA, this.codAlmacen, this.codCaja, String.valueOf(this.idTicket));

                if (documentoG != null) {
                    List<DocumentosImpresosBean> impresos = documentoG.getImpresos();
                    if (impresos != null) {
                        for (int i = 0; i < impresos.size(); i++) {
                            if (impresos.get(i).isTipoDocumentoFactura()) {
                                byte[] a = null;
                                a = impresos.get(i).getImpreso();
                                if (a != null) {
                                    File xml = UtilInputOutputStream.transformardebytesAfile(a);
                                    Document documento = leerXmlyactualizarEntrega(xml);

                                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                                    Result output = new StreamResult(xml);
                                    Source input = new DOMSource(documento);
                                    transformer.transform(input, output);

                                    byte[] result = null;
                                    result = UtilInputOutputStream.trasformarFileabytes(xml);
                                    impresos.get(i).setImpreso(result);

                                    DocumentosService.updateDocumentosImpresos(impresos.get(i));

                                    log.debug("Documento impreso actualizado correctamente.");

                                    //JPrincipal.getInstance().crearError("Documento impreso actualizado correctamente.");
                                    //this.setVisible(false);
                                } else {
                                    //JPrincipal.getInstance().crearError("Xml blob no encontrador.");
                                }
                                break;
                            }
                        }
                    }

                }

            } catch (Exception e) {
                log.error("Error al leer xml y actulaizar el documento impreso." + e);
                // JPrincipal.getInstance().crearError("Error al leer y actualizar el documento impreso.");
            }

            // Tratamos los artículos despachados
            if (!listaEntregados.isEmpty()) {
                try {
                    PrintServices.getInstance().imprimirComprobantePendienteDespacho(listaEntregados);
                } catch (TicketException ex) {
                    log.error("accionAceptar() - Error imprimiendo documento Factura pendiente de despacho" + ex);
                    //  JPrincipal.getInstance().crearError("Error imprimiendo documento Factura pendiente de despacho");
                } catch (TicketPrinterException ex) {
                    log.error("accionAceptar() - Error imprimiendo documento Factura pendiente de despacho" + ex);
                    //  JPrincipal.getInstance().crearError("Error imprimiendo documento Factura pendiente de despacho");
                }
            }

            if (!error.isEmpty()) {
                //  addError("Los siguientes artículos no se han podido actualizar: "+error);
            } else {
                //crearAdvertencia("Actualización correcta.");
                cerrarVentana();
                // contenedor.setVisible(false);
            }
        } else {
            //  JPrincipal.getInstance().crearError("La lista de artículos está vacía.");
        }
    }

    private File transformardebytesAfile(byte[] by) throws IOException {
        File archivoDestino = new File("tmp");
        OutputStream out = new FileOutputStream(archivoDestino);
        out.write(by);
        out.close();
        return archivoDestino;
    }

    private File transformardebytesAfile2(byte[] by) throws IOException {
        File archivoDestino = new File("tmp");
        OutputStream out = new FileOutputStream(archivoDestino);
        out.write(by);
        out.close();
        return archivoDestino;
    }

    private byte[] trasformarFileabytes(File archi) throws FileNotFoundException, IOException {

        FileInputStream ficheroStream = new FileInputStream(archi);
        byte contenido[] = new byte[(int) archi.length()];
        ficheroStream.read(contenido);
        return contenido;
    }

    private Document leerXmlyactualizarEntregaEnTicket(File xml) throws ParserConfigurationException, SAXException, IOException {
        // 1. cargar el XML original
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(xml);

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
                                for (int j = 0; j < DeNadaAPendienteEnvioDomicilio.size(); j++) {
                                    //DeNadaAPendienteEnvioDomicilio.get(j).getLineaTicketOrigenPK().getIdLinea()
                                    if (DeNadaAPendienteEnvioDomicilio.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && DeNadaAPendienteEnvioDomicilio.get(j).getCantidad() == cantidadItemEnXML && idLineaEnXML == DeNadaAPendienteEnvioDomicilio.get(j).getLineaTicketOrigenPK().getIdLinea()) {
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
                                for (int j = 0; j < DePendienteEnvioDomicilioARecogidaPosterior.size(); j++) {
                                    if (DePendienteEnvioDomicilioARecogidaPosterior.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && DePendienteEnvioDomicilioARecogidaPosterior.get(j).getCantidad() == cantidadItemEnXML && idLineaEnXML == DePendienteEnvioDomicilioARecogidaPosterior.get(j).getLineaTicketOrigenPK().getIdLinea()) {
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
                                for (int j = 0; j < DeRecogidaPosteriorAEntregadoRecogidaPosterior.size(); j++) {
                                    if (DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getCantidad() == cantidadItemEnXML && idLineaEnXML == DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getLineaTicketOrigenPK().getIdLinea()) {
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
                                for (int j = 0; j < DeEntregadoRecogidaPosteriorANada.size(); j++) {
                                    if (DeEntregadoRecogidaPosteriorANada.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && DeEntregadoRecogidaPosteriorANada.get(j).getCantidad() == cantidadItemEnXML && idLineaEnXML == DeEntregadoRecogidaPosteriorANada.get(j).getLineaTicketOrigenPK().getIdLinea()) {
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
                            //       JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                        }
                    } else {
                        //        JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                    }
                } else {
                    //      JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                }
            } else {
                //  JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
            }
        } catch (Exception e) {
            log.error("Error actualizando el ticket: " + e.getMessage());
        }

        return doc;
    }

    private Document leerXmlyactualizarEntrega(File xml) throws ParserConfigurationException, SAXException, IOException {
        Document doc = null;
        try {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                doc = builder.parse(xml);
            } catch (SAXException e1) {
                log.error("Error de codificación en el fichero: " + e1.getMessage());
            } catch (IOException e2) {
                log.error("Error de codificación en el fichero: " + e2.getMessage());
            } catch (Exception e3) {
                log.error("Error de codificación en el fichero: " + e3.getMessage());
            }

            Node raiz = null;
            raiz = doc.getElementsByTagName("output").item(0);
            Node nodeTick = null;
            nodeTick = doc.getElementsByTagName("ticket").item(0);
            Element elementTick = null;

            Node LineaAnterior = null;
            Element LineaAnteriorElement = null;
            Node HijoLineaAnterior = null;

            Element lineaX = null;
            NodeList nodosTexto = null;
            int cantidad = 0;

            if (raiz != null) {
                if (raiz.getNodeName().equals("output")) {
                    if (nodeTick != null) {
                        if (nodeTick.getNodeName().equals("ticket")) {
                            elementTick = (Element) nodeTick;

                            //Elemento pendiente entrega
                            Element ElementLinePendienteEntrega = doc.createElement(Constantes.LINE);
                            Element ElementTextPendienteEntrega = doc.createElement(Constantes.TEXT);
                            ElementTextPendienteEntrega.setAttribute("align", "left");
                            ElementTextPendienteEntrega.setAttribute("length", "40");
                            ElementTextPendienteEntrega.setTextContent("************* P. ENTREGA **************");
                            ElementLinePendienteEntrega.appendChild(ElementTextPendienteEntrega);

                            //Elemento entrega a domicilio
                            Element ElementLineEntregaDomicilio = doc.createElement(Constantes.LINE);
                            Element ElementTextEntregaDomicilio = doc.createElement(Constantes.TEXT);
                            ElementTextEntregaDomicilio.setAttribute("align", "left");
                            ElementTextEntregaDomicilio.setAttribute("length", "40");
                            ElementTextEntregaDomicilio.setTextContent("************ E. DOMICILIO *************");
                            ElementLineEntregaDomicilio.appendChild(ElementTextEntregaDomicilio);

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
                                if (cantidad == 6) {
                                    Formatter fmt = new Formatter();
                                    Formatter fmt2 = new Formatter();

                                    String codMarcaItemEnXML = "";
                                    String codMarca_ItemXML = fmt.format("%04d", Integer.parseInt(nodosTexto.item(1).getTextContent().trim())).toString();
                                    String codItem_ItemXML = fmt2.format("%04d", Integer.parseInt(nodosTexto.item(2).getTextContent().trim())).toString();
                                    codMarcaItemEnXML = codMarca_ItemXML + "." + codItem_ItemXML;

                                    int cantidadItemEnXML = 0;
                                    cantidadItemEnXML = Integer.parseInt(nodosTexto.item(3).getTextContent().trim());

                                    //Pasar de Nada a Pendiente Envio
                                    for (int j = 0; j < DeNadaAPendienteEnvioDomicilio.size(); j++) {
                                        if (DeNadaAPendienteEnvioDomicilio.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && DeNadaAPendienteEnvioDomicilio.get(j).getCantidad() == cantidadItemEnXML) {
                                            //Encontro el item
                                            LineaAnterior = null;
                                            LineaAnteriorElement = null;
                                            HijoLineaAnterior = null;
                                            if (DeNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == 'P') {
                                                //Buscar una linea que antes que indique que estaba como envio a domicilio, aunque este entrando en el caso: Nada a Pendiente Envio 
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;
                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (HijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                                DeNadaAPendienteEnvioDomicilio.remove(j);
                                                                j--;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (DeNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == 'P') {
                                                //Buscar una linea que antes que indique que estaba como envio a domicilio, aunque este entrando en el caso: Nada a Pendiente Envio 
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;
                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (HijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************")) {
                                                                elementTick.removeChild(LineaAnterior);
                                                                elementTick.insertBefore(ElementLineEntregaDomicilio, lineaX);

                                                                DeNadaAPendienteEnvioDomicilio.remove(j);
                                                                j--;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else if ((DeNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == 'N' && DeNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (DeNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == 'E' && DeNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == 'E')
                                                    || (DeNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == 'E' && DeNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (DeNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == 'N' && DeNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == 'E')) {
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;
                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (!HijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************") && !HijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                                elementTick.insertBefore(ElementLineEntregaDomicilio, lineaX);

                                                                DeNadaAPendienteEnvioDomicilio.remove(j);
                                                                j--;
                                                            }
                                                        } else {
                                                            elementTick.insertBefore(ElementLineEntregaDomicilio, lineaX);

                                                            DeNadaAPendienteEnvioDomicilio.remove(j);
                                                            j--;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    //Pasar de Pendiente Envio a Recogida Posterior
                                    for (int j = 0; j < DePendienteEnvioDomicilioARecogidaPosterior.size(); j++) {
                                        if (DePendienteEnvioDomicilioARecogidaPosterior.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && DePendienteEnvioDomicilioARecogidaPosterior.get(j).getCantidad() == cantidadItemEnXML) {
                                            //Encontro el item
                                            LineaAnterior = null;
                                            HijoLineaAnterior = null;
                                            if (DePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'P') {
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;
                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (HijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                                elementTick.removeChild(LineaAnterior);

                                                                elementTick.insertBefore(ElementLinePendienteEntrega, lineaX);

                                                                DePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                                j--;
                                                            }
                                                        }
                                                        /*
                                                        else
                                                        {
                                                            elementTick.insertBefore(ElementLinePendienteEntrega,lineaX);
                                                            DePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                         */
                                                    }
                                                }
                                            } else if (DePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'P') {
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;

                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (HijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************")) {
                                                                DePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                                j--;
                                                            }
                                                        }
                                                        /*
                                                        else
                                                        {
                                                            elementTick.insertBefore(ElementLinePendienteEntrega,lineaX);
                                                            
                                                            DePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                         */
                                                    }
                                                }
                                            } else if ((DePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'N' && DePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (DePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'E' && DePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'E')
                                                    || (DePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'E' && DePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (DePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'N' && DePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'E')) {
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;

                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (!HijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************") && !HijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                                elementTick.insertBefore(ElementLinePendienteEntrega, lineaX);
                                                                DePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                                j--;
                                                            }
                                                        } else {
                                                            elementTick.insertBefore(ElementLinePendienteEntrega, lineaX);

                                                            DePendienteEnvioDomicilioARecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    //Pasar de Recogida Posterior a Entregado de una Recogida Posterior                                
                                    for (int j = 0; j < DeRecogidaPosteriorAEntregadoRecogidaPosterior.size(); j++) {
                                        if (DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getCantidad() == cantidadItemEnXML) {
                                            //Encontro el item
                                            LineaAnterior = null;
                                            HijoLineaAnterior = null;
                                            if (DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'P') {
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;
                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (HijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                                elementTick.removeChild(LineaAnterior);
                                                                DeRecogidaPosteriorAEntregadoRecogidaPosterior.remove(j);
                                                                j--;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else if (DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'P') {
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;
                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (HijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************")) {
                                                                elementTick.removeChild(LineaAnterior);

                                                                DeRecogidaPosteriorAEntregadoRecogidaPosterior.remove(j);
                                                                j--;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else if ((DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'N' && DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'E' && DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'E')
                                                    || (DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'E' && DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'N' && DeRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'E')) {
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;
                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (!HijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************") && !HijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                                DeRecogidaPosteriorAEntregadoRecogidaPosterior.remove(j);
                                                                j--;
                                                            }
                                                        } else {
                                                            DeRecogidaPosteriorAEntregadoRecogidaPosterior.remove(j);
                                                            j--;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    //Pasar de un Entregado Recogida Posterior a Nada
                                    for (int j = 0; j < DeEntregadoRecogidaPosteriorANada.size(); j++) {
                                        if (DeEntregadoRecogidaPosteriorANada.get(j).getCodart().getCodart().equals(codMarcaItemEnXML) && DeEntregadoRecogidaPosteriorANada.get(j).getCantidad() == cantidadItemEnXML) {
                                            //Encontro el item
                                            LineaAnterior = null;
                                            HijoLineaAnterior = null;
                                            if (DeEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == 'P') {
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;
                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (HijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                                elementTick.removeChild(LineaAnterior);

                                                                DeEntregadoRecogidaPosteriorANada.remove(j);
                                                                j--;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else if (DeEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == 'P') {
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;
                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (HijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************")) {
                                                                elementTick.removeChild(LineaAnterior);

                                                                DeEntregadoRecogidaPosteriorANada.remove(j);
                                                                j--;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else if ((DeEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == 'N' && DeEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (DeEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == 'E' && DeEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == 'E')
                                                    || (DeEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == 'E' && DeEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (DeEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == 'N' && DeEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == 'E')) {
                                                if (i >= 1) {
                                                    LineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 1);
                                                    if (LineaAnterior != null) {
                                                        LineaAnteriorElement = (Element) LineaAnterior;
                                                        if (LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).getLength() == 1) {
                                                            HijoLineaAnterior = LineaAnteriorElement.getElementsByTagName(Constantes.TEXT).item(0);
                                                            if (!HijoLineaAnterior.getTextContent().equals("************* P. ENTREGA **************") && !HijoLineaAnterior.getTextContent().equals("************ E. DOMICILIO *************")) {
                                                                DeEntregadoRecogidaPosteriorANada.remove(j);
                                                                j--;
                                                            }
                                                        } else {
                                                            DeEntregadoRecogidaPosteriorANada.remove(j);
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
                            //      JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                        }
                    } else {
                        //    JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                    }
                } else {
                    //        JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                }
            } else {
                //      JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
            }
        } catch (Exception e) {
            log.error("Error al leer xml y actulaizar el documento impreso ." + e);
        }

        return doc;
    }
}
