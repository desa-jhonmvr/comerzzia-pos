/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * JEnvioDomicilio.java
 *
 * Created on 19-mar-2014, 9:20:23
 */
package com.comerzzia.jpos.gui.guiasremision;

import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.UsuarioDTO;
import com.comerzzia.jpos.dto.ventas.DocumentoDTO;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.entity.db.LogOperacionesDet;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.gui.IVista;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.gui.components.JVentanaDialogo;
import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import com.comerzzia.jpos.gui.guiasremision.modelos.ArticulosEnvioDomicilioCellRenderer;
import com.comerzzia.jpos.gui.guiasremision.modelos.ArticulosEnvioDomicilioTableModel;
import com.comerzzia.jpos.gui.validation.IValidableForm;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidadorObligatoriedad;
import com.comerzzia.jpos.gui.validation.ValidadorTexto;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.persistencia.devoluciones.articulos.ArticuloDevueltoBean;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogException;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.devoluciones.DevolucionesServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.UtilUsuario;
import com.comerzzia.jpos.util.thread.ProcesoEncolarThread;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.swing.acciones.Acciones;
import es.mpsistemas.util.log.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
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
 * @author SMLM
 */
public class JEnvioDomicilio extends JVentanaDialogo implements IVista, IViewerValidationFormError {

    private static Logger LOG_POS = Logger.getMLogger(JEnvioDomicilio.class);
    private JPrincipal ventana_padre;
    List<IValidableForm> formulario;
    private List<LineaTicketOrigen> lista;

    private String codAlmacen;
    private String codCaja;
    private int idDocumento;
    private TicketsAlm ticket;

    private List<LineaTicketOrigen> deNadaAPendienteEnvioDomicilio;
    private List<LineaTicketOrigen> dePendienteEnvioDomicilioARecogidaPosterior;
    private List<LineaTicketOrigen> deRecogidaPosteriorAEntregadoRecogidaPosterior;
    private List<LineaTicketOrigen> deEntregadoRecogidaPosteriorANada;

    /**
     * Creates new form JEnvioDomicilio
     */
    public JEnvioDomicilio() {
        super();

        this.codAlmacen = "";
        this.codCaja = "";
        this.idDocumento = 0;

        this.deNadaAPendienteEnvioDomicilio = new ArrayList<LineaTicketOrigen>();
        this.dePendienteEnvioDomicilioARecogidaPosterior = new ArrayList<LineaTicketOrigen>();
        this.deRecogidaPosteriorAEntregadoRecogidaPosterior = new ArrayList<LineaTicketOrigen>();
        this.deEntregadoRecogidaPosteriorANada = new ArrayList<LineaTicketOrigen>();

        initComponents();
        formulario = new LinkedList<IValidableForm>();
        inicializaValidacion();
        crearFormulario();
        addFunctionKeys();
        registraEventoEnterBoton();
        try {
            iniciaVista();
        } catch (Exception ex) {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_table = new javax.swing.JTable();
        b_aceptar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        b_cancelar = new com.comerzzia.jpos.gui.components.form.JButtonForm();
        lb_error = new javax.swing.JLabel();
        t_factura1 = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_factura2 = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_factura3 = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        label1 = new java.awt.Label();
        jScrollPane3 = new javax.swing.JScrollPane();
        observacion = new com.comerzzia.jpos.gui.components.form.JTextAreaForm();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setMaximumSize(new java.awt.Dimension(610,450));
        setMinimumSize(new java.awt.Dimension(610,650));

        jLabel1.setDisplayedMnemonic('F');
        jLabel1.setLabelFor(t_factura1);
        jLabel1.setText("Factura: ");

        t_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        t_table.setNextFocusableComponent(b_aceptar);
        t_table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_tableKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(t_table);

        b_aceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png"))); // NOI18N
        b_aceptar.setMnemonic('a');
        b_aceptar.setText("Aceptar");
        b_aceptar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        b_aceptar.setNextFocusableComponent(b_cancelar);
        b_aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_aceptarActionPerformed(evt);
            }
        });

        b_cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png"))); // NOI18N
        b_cancelar.setText("Cancelar");
        b_cancelar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        b_cancelar.setNextFocusableComponent(t_factura1);
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        lb_error.setForeground(new java.awt.Color(-3407872,true));

        t_factura1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_factura1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_factura1KeyTyped(evt);
            }
        });

        t_factura2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_factura2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_factura2keyTyped(evt);
            }
        });

        t_factura3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_factura3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_factura3ActionPerformed(evt);
            }
        });
        t_factura3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_factura3keyTyped(evt);
            }
        });

        jLabel5.setText("-");

        jLabel4.setText("-");

        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel6.setText("Gestión de Entregas a Domicilio");

        label1.setText("Observación:");

        observacion.setColumns(20);
        observacion.setRows(5);
        jScrollPane3.setViewportView(observacion);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(t_factura1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(t_factura2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(t_factura3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(b_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_factura1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(t_factura2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_factura3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(lb_error, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void b_aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_aceptarActionPerformed
        accionAceptar();
}//GEN-LAST:event_b_aceptarActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        accionCancelar();
}//GEN-LAST:event_b_cancelarActionPerformed

    private void t_factura1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_factura1KeyTyped
        accionIntro(evt);
    }//GEN-LAST:event_t_factura1KeyTyped

    private void t_factura2keyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_factura2keyTyped
        accionIntro(evt);
    }//GEN-LAST:event_t_factura2keyTyped

    private void t_factura3keyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_factura3keyTyped
        accionIntro(evt);
    }//GEN-LAST:event_t_factura3keyTyped

    private void t_tableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_tableKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB && evt.isShiftDown()) {
            t_table.transferFocusBackward();
        } else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            t_table.transferFocus();
        }
    }//GEN-LAST:event_t_tableKeyPressed

    private void t_factura3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_factura3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_factura3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_aceptar;
    private com.comerzzia.jpos.gui.components.form.JButtonForm b_cancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private java.awt.Label label1;
    private javax.swing.JLabel lb_error;
    private com.comerzzia.jpos.gui.components.form.JTextAreaForm observacion;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_factura1;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_factura2;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_factura3;
    private javax.swing.JTable t_table;
    // End of variables declaration//GEN-END:variables

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
                                            if (lineaX.getChildNodes().item(k).getNodeName().equals("envioDomicilio")) {
                                                encuentraItemED = true;
                                                lineaX.getChildNodes().item(k).setTextContent("S");
                                            }
                                            if (lineaX.getChildNodes().item(k).getNodeName().equals("recogidaPosterior")) {
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
                                                newNodeED = doc.createElement("envioDomicilio");
                                                newNodeED.setTextContent("S");
                                                lineaX.insertBefore(newNodeED, referenciaInsert);
                                            } else {
                                                newNodeED = doc.createElement("envioDomicilio");
                                                newNodeED.setTextContent("S");
                                                lineaX.appendChild(newNodeED);
                                            }
                                        }
                                        if (!encuentraItemRP) {
                                            //Insertar el item recogidaPosterior despues del item envioDomicilio si existe y sino como ultimo elemento
                                            if (referenciaInsert != null) {
                                                newNodeRP = doc.createElement("recogidaPosterior");
                                                newNodeRP.setTextContent("N");
                                                lineaX.insertBefore(newNodeRP, referenciaInsert);
                                            } else {
                                                newNodeRP = doc.createElement("recogidaPosterior");
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
                                            if (lineaX.getChildNodes().item(k).getNodeName().equals("envioDomicilio")) {
                                                encuentraItemED = true;
                                                lineaX.getChildNodes().item(k).setTextContent("N");
                                            }
                                            if (lineaX.getChildNodes().item(k).getNodeName().equals("recogidaPosterior")) {
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
                                                newNodeED = doc.createElement("envioDomicilio");
                                                newNodeED.setTextContent("N");
                                                lineaX.insertBefore(newNodeED, referenciaInsert);
                                            } else {
                                                newNodeED = doc.createElement("envioDomicilio");
                                                newNodeED.setTextContent("N");
                                                lineaX.appendChild(newNodeED);
                                            }
                                        }
                                        if (!encuentraItemRP) {
                                            //Insertar el item recogidaPosterior despues del item envioDomicilio si existe y sino como ultimo elemento
                                            if (referenciaInsert != null) {
                                                newNodeRP = doc.createElement("recogidaPosterior");
                                                newNodeRP.setTextContent("S");
                                                lineaX.insertBefore(newNodeRP, referenciaInsert);
                                            } else {
                                                newNodeRP = doc.createElement("recogidaPosterior");
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
                                            if (lineaX.getChildNodes().item(k).getNodeName().equals("envioDomicilio")) {
                                                encuentraItemED = true;
                                                lineaX.getChildNodes().item(k).setTextContent("N");
                                            }
                                            if (lineaX.getChildNodes().item(k).getNodeName().equals("recogidaPosterior")) {
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
                                                newNodeED = doc.createElement("envioDomicilio");
                                                newNodeED.setTextContent("N");
                                                lineaX.insertBefore(newNodeED, referenciaInsert);
                                            } else {
                                                newNodeED = doc.createElement("envioDomicilio");
                                                newNodeED.setTextContent("N");
                                                lineaX.appendChild(newNodeED);
                                            }
                                        }
                                        if (!encuentraItemRP) {
                                            //Insertar el item recogidaPosterior despues del item envioDomicilio si existe y sino como ultimo elemento
                                            if (referenciaInsert != null) {
                                                newNodeRP = doc.createElement("recogidaPosterior");
                                                newNodeRP.setTextContent("E");
                                                lineaX.insertBefore(newNodeRP, referenciaInsert);
                                            } else {
                                                newNodeRP = doc.createElement("recogidaPosterior");
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
                                            if (lineaX.getChildNodes().item(k).getNodeName().equals("envioDomicilio")) {
                                                encuentraItemED = true;
                                                lineaX.getChildNodes().item(k).setTextContent("N");
                                            }
                                            if (lineaX.getChildNodes().item(k).getNodeName().equals("recogidaPosterior")) {
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
                                                newNodeED = doc.createElement("envioDomicilio");
                                                newNodeED.setTextContent("N");
                                                lineaX.insertBefore(newNodeED, referenciaInsert);
                                            } else {
                                                newNodeED = doc.createElement("envioDomicilio");
                                                newNodeED.setTextContent("N");
                                                lineaX.appendChild(newNodeED);
                                            }
                                        }
                                        if (!encuentraItemRP) {
                                            //Insertar el item recogidaPosterior despues del item envioDomicilio si existe y sino como ultimo elemento
                                            if (referenciaInsert != null) {
                                                newNodeRP = doc.createElement("recogidaPosterior");
                                                newNodeRP.setTextContent("N");
                                                lineaX.insertBefore(newNodeRP, referenciaInsert);
                                            } else {
                                                newNodeRP = doc.createElement("recogidaPosterior");
                                                newNodeRP.setTextContent("N");
                                                lineaX.appendChild(newNodeRP);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                        }
                    } else {
                        JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                    }
                } else {
                    JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                }
            } else {
                JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
            }
        } catch (Exception e) {
            LOG_POS.error("Error actualizando el ticket: " + e.getMessage());
        }

        return doc;
    }

    private Document leerXmlyactualizarEntrega(File xml) throws ParserConfigurationException, SAXException, IOException {
        Document doc = null;
        try {
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
                                            if (deNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == 'P') {
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
                                            if (deNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == 'P') {
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
                                            } else if ((deNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == 'N' && deNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (deNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == 'E' && deNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == 'E')
                                                    || (deNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == 'E' && deNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (deNadaAPendienteEnvioDomicilio.get(j).getEnvioDomicilioOriginal() == 'N' && deNadaAPendienteEnvioDomicilio.get(j).getRecogidaPosteriorOriginal() == 'E')) {
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
                                            if (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'P') {
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
                                            } else if (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'P') {
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
                                            } else if ((dePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'N' && dePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'E' && dePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'E')
                                                    || (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'E' && dePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (dePendienteEnvioDomicilioARecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'N' && dePendienteEnvioDomicilioARecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'E')) {
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
                                            if (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'P') {
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
                                                             if (hijoLineaAnterior.getTextContent().equals("************* INTERCAMBIO *************")) {
                                                                elementTick.removeChild(lineaAnterior);
                                                                deRecogidaPosteriorAEntregadoRecogidaPosterior.remove(j);
                                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 2);
                                                                elementTick.removeChild(lineaAnterior);
                                                                j--;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else if (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'P') {
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
                                                            if (hijoLineaAnterior.getTextContent().equals("************* INTERCAMBIO *************")) {
                                                                elementTick.removeChild(lineaAnterior);
                                                                deRecogidaPosteriorAEntregadoRecogidaPosterior.remove(j);
                                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 2);
                                                                elementTick.removeChild(lineaAnterior);
                                                                j--;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else if ((deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'N' && deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'E' && deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'E')
                                                    || (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'E' && deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getEnvioDomicilioOriginal() == 'N' && deRecogidaPosteriorAEntregadoRecogidaPosterior.get(j).getRecogidaPosteriorOriginal() == 'E')) {
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
                                            if (deEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == 'P') {
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
                                                             if (hijoLineaAnterior.getTextContent().equals("************* INTERCAMBIO *************")) {
                                                                elementTick.removeChild(lineaAnterior);
                                                                deEntregadoRecogidaPosteriorANada.remove(j);
                                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 2);
                                                                elementTick.removeChild(lineaAnterior);
                                                                j--;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else if (deEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == 'P') {
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
                                                             if (hijoLineaAnterior.getTextContent().equals("************* INTERCAMBIO *************")) {
                                                                elementTick.removeChild(lineaAnterior);
                                                                deEntregadoRecogidaPosteriorANada.remove(j);
                                                                lineaAnterior = elementTick.getElementsByTagName(Constantes.LINE).item(i - 2);
                                                                elementTick.removeChild(lineaAnterior);
                                                                j--;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else if ((deEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == 'N' && deEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (deEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == 'E' && deEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == 'E')
                                                    || (deEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == 'E' && deEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == 'N')
                                                    || (deEntregadoRecogidaPosteriorANada.get(j).getEnvioDomicilioOriginal() == 'N' && deEntregadoRecogidaPosteriorANada.get(j).getRecogidaPosteriorOriginal() == 'E')) {
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
                            JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                        }
                    } else {
                        JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                    }
                } else {
                    JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
                }
            } else {
                JPrincipal.getInstance().crearAdvertencia("Error en la estructura del documento impreso.");
            }
        } catch (Exception e) {
            LOG_POS.error("Error al leer xml y actulaizar el documento impreso ." + e);
        }

        return doc;
    }
//        public String crearVentanaMotivoAnulacion() {
//        log.info("Creando ventana de motivo anulacion ");     
//        p_motivo.setMotivo("");
//        p_motivo.clearError();
//        v_motivo.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
//        v_motivo.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
//        v_motivo.setSize(400,320);
//        p_motivo.setSize(400,320);
//        v_motivo.setLocationRelativeTo(null);    
//        p_motivo.iniciaVista();
//        v_motivo.setVisible(true);
//        return p_motivo.getMotivo();        
//    }

    @Override
    public void accionAceptar() {
        int cont = 0;
        Calendar calendar = Calendar.getInstance();
        Calendar fechaFactura = Calendar.getInstance();
        Date dateObj = calendar.getTime();
        calendar.setTime(dateObj); // Configuramos la fecha que se recibe
        fechaFactura.setTime(ticket.getFecha()); // Configuramos la fecha que se recibe
        while (!fechaFactura.after(calendar)) {
            fechaFactura.add(Calendar.DAY_OF_MONTH, 1);
            cont = cont + 1;
        }
        if (observacion.getText().equals("")) {
            lb_error.setText("El campo observación es obligatorio");
        } else {
            this.ventana_padre = JPrincipal.getInstance();
            List<ItemDTO> itemDTOLista = new ArrayList<>();
            if (lista != null && !lista.isEmpty()) {
                Boolean continuar = true;
                for (LineaTicketOrigen lto : lista) {
//                    calendar.setTime(dateObj); // Configuramos la fecha que se recibe
//                    fechaFactura.setTime(lto.getTicket().getFecha()); // Configuramos la fecha que se recibe
//                    cont = 0;
//                    while (!fechaFactura.after(calendar)) {
//                        fechaFactura.add(Calendar.DAY_OF_MONTH, 1);
//                        cont = cont + 1;
//                    }
                    if ((cont > 3 && lto.getRecogidaPosterior() == 'P' && lto.getEnvioDomicilio() == 'N') || (cont > 3 && lto.getRecogidaPosterior() == 'N' && lto.getEnvioDomicilio() == 'P')) {
                        if ((lto.getRecogidaPosteriorOriginal() == 'P' && lto.getEnvioDomicilioOriginal() == 'N') || (lto.getRecogidaPosteriorOriginal() == 'N' && lto.getEnvioDomicilioOriginal() == 'P')) {
                            try {
                                if ((lto.getRecogidaPosteriorOriginal() != lto.getRecogidaPosterior()) || (lto.getEnvioDomicilioOriginal() != lto.getEnvioDomicilio())) {
                                    ventana_padre.compruebaAutorizacionEstatusEntrega(Operaciones.PERMITE_MODIFICAR_ESTATUS_ENVIO, lto.getImporteTotalFinal());
                                    if (lto.getEnvioDomicilioOriginal() != lto.getEnvioDomicilio() && 'P' == lto.getEnvioDomicilioOriginal()) {
                                        if (lto.getRecogidaPosteriorOriginal() != lto.getRecogidaPosterior() && 'E' == lto.getRecogidaPosterior()) {
                                            ItemDTO itemDTO = new ItemDTO();
                                            itemDTO.setCodigoI(lto.getCodart().getCodmarca().getCodmarca() + "-" + lto.getCodart().getIdItem());
                                            itemDTO.setEstado("E");
                                            itemDTO.setCantidad((long) lto.getCantidad());
                                            itemDTOLista.add(itemDTO);
                                        }
                                    }
                                }
                            } catch (SinPermisosException ex) {
                                continuar = false;
                                java.util.logging.Logger.getLogger(JEnvioDomicilio.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            continuar = false;
                            lb_error.setText("La fecha es mayor al plazo de 2 dias de la emisión de la factura. ");
                        }
                    } else {
                        try {
                            if ((lto.getRecogidaPosteriorOriginal() != lto.getRecogidaPosterior()) || (lto.getEnvioDomicilioOriginal() != lto.getEnvioDomicilio())) {
                                ventana_padre.compruebaAutorizacionEstatusEntrega(Operaciones.PERMITE_MODIFICAR_ESTATUS_ENVIO, lto.getImporteTotalFinal());
                                if (lto.getEnvioDomicilioOriginal() != lto.getEnvioDomicilio() && 'P' == lto.getEnvioDomicilioOriginal()) {
                                    if (lto.getRecogidaPosteriorOriginal() != lto.getRecogidaPosterior() && 'E' == lto.getRecogidaPosterior()) {
                                        ItemDTO itemDTO = new ItemDTO();
                                        itemDTO.setCodigoI(lto.getCodart().getCodmarca().getCodmarca() + "-" + lto.getCodart().getIdItem());
                                        itemDTO.setEstado("E");
                                        itemDTO.setCantidad((long) lto.getCantidad());
                                        itemDTOLista.add(itemDTO);
                                    }
                                }
                            }
                        } catch (SinPermisosException ex) {
                            continuar = false;
                            java.util.logging.Logger.getLogger(JEnvioDomicilio.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
                if (continuar) {
                    guardar(observacion.getText(), t_factura1.getText() + "-" + t_factura2.getText() + "-" + t_factura3.getText(), itemDTOLista);
                    //Actualizar las lineas
                    List<LineaTicketOrigen> listaEntregados = new ArrayList<LineaTicketOrigen>();
                    String error = "";
                    for (LineaTicketOrigen lto : lista) {
                        try {
                            TicketService.modificarTicketDetalle(lto);
                            if (lto.isRecogidaPosteriorEntregado() && lto.getRecogidaPosteriorOriginal() != 'E') {
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

                        TicketsAlm ticket = TicketService.consultarTicket(Long.valueOf(this.idDocumento), this.codCaja, this.codAlmacen);
                        if (ticket != null) {

                            byte[] a = ticket.getXMLTicket();
                            if (a != null) {
                                File xml = transformardebytesAfile(a);
                                Document documento = leerXmlyactualizarEntregaEnTicket(xml);

                                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                                Result output = new StreamResult(xml);
                                Source input = new DOMSource(documento);
                                transformer.transform(input, output);

                                ticket.setTicket(trasformarFileabytes(xml));
                                TicketService.modificarTicket(em, ticket);
                                em.getTransaction().commit();
                                if (!itemDTOLista.isEmpty()) {
                                    generarEnvioADomicilio(itemDTOLista, ticket);
                                }

                                LOG_POS.debug("Ticket actualizado correctamente.");
                            } else {
                                JPrincipal.getInstance().crearError("Xml blob no encontrador.");
                            }
                        } else {
                            JPrincipal.getInstance().crearError("Ticket no encontrado.");
                        }

                    } catch (NoResultException ex) {
                        JPrincipal.getInstance().crearError("Ticket no encontrado.");
                        LOG_POS.error("Ticket no encontrado.");
                    } catch (TicketException ex) {
                        LOG_POS.error(ex);
                    } catch (IOException ex) {
                        LOG_POS.error("Error en conversion file bytes ." + ex);
                        //Logger.getLogger(CambioFechaBlob.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SAXException ex) {
                        LOG_POS.error("Error al escribir cambio xml ." + ex);
                    } catch (TransformerConfigurationException ex) {
                        //Logger.getLogger(CambioFechaBlob.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (TransformerException ex) {
                        LOG_POS.error("Error al escribir cambio xml ." + ex);
                    } catch (ParserConfigurationException ex) {
                        LOG_POS.error("Error al leer xml y actulaizar el ticket." + ex);
                    } catch (Exception e) {
                        JPrincipal.getInstance().crearError("Error al leer y actulaizar el ticket.");
                        LOG_POS.error("Error al leer xml y actulaizar el ticket." + e);
                    }

                    //Actualizar el documento impreso        
                    try {
                        DocumentosBean documentoG = DocumentosService.consultarDoc(DocumentosImpresosBean.TIPO_FACTURA, this.codAlmacen, this.codCaja, String.valueOf(this.idDocumento));

                        if (documentoG != null) {
                            List<DocumentosImpresosBean> impresos = documentoG.getImpresos();
                            if (impresos != null) {
                                for (int i = 0; i < impresos.size(); i++) {
                                    if (impresos.get(i).isTipoDocumentoFactura()) {
                                        byte[] a = null;
                                        a = impresos.get(i).getImpreso();
                                        if (a != null) {
                                            File xml = transformardebytesAfile(a);
                                            Document documento = leerXmlyactualizarEntrega(xml);

                                            Transformer transformer = TransformerFactory.newInstance().newTransformer();
                                            Result output = new StreamResult(xml);
                                            Source input = new DOMSource(documento);
                                            transformer.transform(input, output);

                                            byte[] result = null;
                                            result = trasformarFileabytes(xml);
                                            impresos.get(i).setImpreso(result);

                                            DocumentosService.updateDocumentosImpresos(impresos.get(i));

                                            LOG_POS.debug("Documento impreso actualizado correctamente.");

                                        } else {
                                            JPrincipal.getInstance().crearError("Xml blob no encontrador.");
                                        }
                                        break;
                                    }
                                }
                            }

                        }

                    } catch (Exception e) {
                        LOG_POS.error("Error al leer xml y actulaizar el documento impreso." + e);
                        JPrincipal.getInstance().crearError("Error al leer y actualizar el documento impreso.");
                    }

                    // Tratamos los artículos despachados
                    if (!listaEntregados.isEmpty()) {
                        try {
                            PrintServices.getInstance().imprimirComprobantePendienteDespacho(listaEntregados);
                        } catch (TicketException ex) {
                            LOG_POS.error("accionAceptar() - Error imprimiendo documento Factura pendiente de despacho" + ex);
                            JPrincipal.getInstance().crearError("Error imprimiendo documento Factura pendiente de despacho");
                        } catch (TicketPrinterException ex) {
                            LOG_POS.error("accionAceptar() - Error imprimiendo documento Factura pendiente de despacho" + ex);
                            JPrincipal.getInstance().crearError("Error imprimiendo documento Factura pendiente de despacho");
                        }
                    }

                    if (!error.isEmpty()) {
                        addError("Los siguientes artículos no se han podido actualizar: " + error);
                    } else {
                        //crearAdvertencia("Actualización correcta.");
                        cerrarVentana();
                        // contenedor.setVisible(false);
                    }
                }
            } else {
                JPrincipal.getInstance().crearError("La lista de artículos está vacía.");
            }
        }

    }

    public void guardar(String observacion, String referencia, List<ItemDTO> itemDTOLista) {

        try {
            LogOperaciones logImpresiones = new LogOperaciones();
            logImpresiones = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logImpresiones.setFechaHora(new Date());
            logImpresiones.setUsuario(Sesion.getUsuario().getUsuario());
            logImpresiones.setReferencia(referencia);
            logImpresiones.setProcesado('N');
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
            java.util.logging.Logger.getLogger(JEnvioDomicilio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void insertarLog(LogOperaciones logImpresiones, List<LogOperacionesDet> logDetalle) throws LogException {
        try {
            EntityManagerFactory emf = Sesion.getEmf();
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            ServicioLogAcceso.crearAccesoLog(logImpresiones, em);
            for (LogOperacionesDet det : logDetalle) {
                ServicioLogAcceso.crearAccesoLogDet(det, em);
            }
            em.getTransaction().commit();
        } catch (Throwable ex) {
            throw new LogException("Error al registrar el log " + ex.getMessage());
        }
    }

    @Override
    public void limpiarFormulario() {
        desactivarValidacion();
        clearError();
        t_factura2.setText("");
        t_factura3.setText("");
        observacion.setText("");
        lista = new ArrayList<LineaTicketOrigen>();
        refrescarTablaArticulos(new ArticulosEnvioDomicilioTableModel(lista));
        t_factura1.setText(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
        iniciaFoco();
        activarValidacion();

        this.codAlmacen = "";
        this.codCaja = "";
        this.idDocumento = 0;

        this.deNadaAPendienteEnvioDomicilio.clear();
        this.dePendienteEnvioDomicilioARecogidaPosterior.clear();
        this.deRecogidaPosteriorAEntregadoRecogidaPosterior.clear();
        this.deEntregadoRecogidaPosteriorANada.clear();
    }

    @Override
    public void accionLeerTarjetaVD() {
    }

    @Override
    public void addError(ValidationFormException e) {
        URL myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/error.gif");
        lb_error.setIcon(new ImageIcon(myurl));
        lb_error.setText(e.getMessage());
    }

    public void addError(String s) {
        URL myurl = this.getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/error.gif");
        lb_error.setIcon(new ImageIcon(myurl));
        lb_error.setText(s);
    }

    @Override
    public void clearError() {
        lb_error.setIcon(null);
        lb_error.setText("");
    }

    private void realizarBusqueda(KeyEvent evt) {
        if (evt.getKeyChar() == '\n') {
            try {
                lista = new ArrayList<LineaTicketOrigen>();
                refrescarTablaArticulos(new ArticulosEnvioDomicilioTableModel(lista));

                String codAlmacen = t_factura1.getText();
                String codCaja = t_factura2.getText();
                Long idTicket = Long.parseLong(t_factura3.getText());

                this.codAlmacen = t_factura1.getText();
                this.codCaja = t_factura2.getText();
                this.idDocumento = Integer.parseInt(t_factura3.getText());

                ticket = TicketService.consultarTicket(idTicket, codCaja, codAlmacen);
                if (ticket.isAnulado()) {
                    crearAdvertencia("La factura está anulada.");
                    iniciaFoco();
                    return;
                }

                lista = TicketService.consultarLineasTicket(ticket.getUidTicket());

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

                if (lista.isEmpty()) {
                    if (!articulosDevueltos.isEmpty()) {
                        crearAdvertencia("Esta factura tiene Nota de Credito. ");
                    } else {
                        crearAdvertencia("No se han encontrado líneas de tickets para esa factura.");
                    }
                }
                refrescarTablaArticulos(new ArticulosEnvioDomicilioTableModel(lista));
                if (lista.size() >= 0) {
                    ListSelectionModel selectionModel = t_table.getSelectionModel();
                    selectionModel.setSelectionInterval(0, 0);
                    t_table.requestFocus();
                }
//                    }
            } catch (NoResultException ex) {
                crearAdvertencia("No se han encontrado líneas de tickets de esa factura.");
            } catch (TicketException ex) {
                addError("Error consultando las líneas de ticket: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                crearAdvertencia("No se han encontrado líneas de tickets para esa factura.");
            } catch (Exception ex) {
                addError("Error consultando las líneas de ticket: " + ex.getMessage());
            }
        }
    }

    private void refrescarTablaArticulos(ArticulosEnvioDomicilioTableModel articulosEnvioDomicilioTableModel) {
        t_table.setModel(articulosEnvioDomicilioTableModel);
        t_table.getColumnModel().getColumn(0).setPreferredWidth(100);
        t_table.getColumnModel().getColumn(1).setPreferredWidth(300);
        t_table.getColumnModel().getColumn(2).setPreferredWidth(100);
        t_table.getColumnModel().getColumn(3).setPreferredWidth(150);
    }

    public void iniciaVista() {

        desactivarValidacion();
        limpiarFormulario();
        t_factura1.setText(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));

        Border empty = new EmptyBorder(0, 0, 0, 0);
        t_table
                .setDefaultRenderer(Object.class,
                        new ArticulosEnvioDomicilioCellRenderer());

        jScrollPane1.setViewportBorder(empty);
        t_table.setBorder(empty);
        jScrollPane1.getViewport().setOpaque(false);

        iniciaFoco();
        activarValidacion();
    }

    private void addFunctionKeys() {

        LOG_POS.info("Función de acciones de teclado");

        Acciones.crearAccionFocoTabla(this, t_table);

        t_table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        t_table.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                accionEnter();
            }
        });
    }

    private void crearAdvertencia(String msg) {
        JPrincipal.getInstance().crearAdvertencia(msg);
    }

    private void accionIntro(KeyEvent ke) {
        if ((t_factura1.hasFocus() || t_factura2.hasFocus() || t_factura3.hasFocus()) && ke.getKeyChar() == '\n') {
            try {
                validarFormulario();
            } catch (ValidationFormException ex) {
                addError(ex);
                return;
            }
            JTextFieldForm focoEn = null;

            if (ke.getComponent() instanceof JTextFieldForm) {
                focoEn = (JTextFieldForm) ke.getComponent();
            }
            realizarBusqueda(ke);

            if (lista.size() < 1 && focoEn != null) {
                focoEn.requestFocus();
            }

            if (lista.size() > 0) {
                ListSelectionModel selectionModel = t_table.getSelectionModel();
                selectionModel.setSelectionInterval(0, 0);
                observacion.requestFocus();
//                t_table.requestFocus();
            }
        }
    }

    private void inicializaValidacion() {
        t_factura1.addValidador(new ValidadorObligatoriedad(), this);
        t_factura1.addValidador(new ValidadorTexto(3, true), this);
        t_factura2.addValidador(new ValidadorObligatoriedad(), this);
        t_factura2.addValidador(new ValidadorTexto(3, true), this);
        t_factura3.addValidador(new ValidadorObligatoriedad(), this);
        t_factura3.addValidador(new ValidadorTexto(9, true), this);
    }

    private void crearFormulario() {
        // Elementos del formulario susceptibles de validarse o resetearse
        formulario.add(t_factura1);
        formulario.add(t_factura2);
        formulario.add(t_factura3);
    }

    private void validarFormulario() throws ValidationFormException {
        for (IValidableForm e : formulario) {
            try {
                e.validar();
            } catch (ValidationFormException ex) {
                throw ex;
            }
        }
    }

    private void accionEnter() {
        try {
            int fila = t_table.getSelectedRow();
            LineaTicketOrigen lto = lista.get(fila);
            //LineaTicketOrigen lineaOrigen = TicketService.consultarLineaTicketOrigen(lto.getLineaTicketOrigenPK().getUidTicket(), new Long(lto.getLineaTicketOrigenPK().getIdLinea()));
            if (lto.getEnvioDomicilioOriginal() != 'E' && lto.getRecogidaPosteriorOriginal() != 'E') {
                if (lto.getEnvioDomicilio() == 'N' && lto.getRecogidaPosterior() == 'N') {
                    //Pasar de Nada a Pendiente Envio
                    lto.setEnvioDomicilio('P');
                    lto.setRecogidaPosterior('N');

                    this.deNadaAPendienteEnvioDomicilio.add(lto);
                    //Quitar el codigo si esta en otro listado
                    dePendienteEnvioDomicilioARecogidaPosterior.remove(lto);
                    deRecogidaPosteriorAEntregadoRecogidaPosterior.remove(lto);
                    deEntregadoRecogidaPosteriorANada.remove(lto);
                } else if (lto.getEnvioDomicilio() == 'P' && lto.getRecogidaPosterior() != 'P') {
                    //Pasar de Pendiente Envio a Recogida Posterior
                    lto.setRecogidaPosterior('P');
                    lto.setEnvioDomicilio('N');

                    this.dePendienteEnvioDomicilioARecogidaPosterior.add(lto);

                    //Quitar el codigo si esta en otro listado
                    deNadaAPendienteEnvioDomicilio.remove(lto);
                    deRecogidaPosteriorAEntregadoRecogidaPosterior.remove(lto);
                    deEntregadoRecogidaPosteriorANada.remove(lto);
                } else if (lto.getRecogidaPosterior() == 'P' && lto.getEnvioDomicilio() != 'P') {
                    //Pasar de Recogida Posterior a Entregado de una Recogida Posterior
                    lto.setEnvioDomicilio('N');
                    lto.setRecogidaPosterior('E');

                    this.deRecogidaPosteriorAEntregadoRecogidaPosterior.add(lto);

                    //Quitar el codigo si esta en otro listado
                    deNadaAPendienteEnvioDomicilio.remove(lto);
                    dePendienteEnvioDomicilioARecogidaPosterior.remove(lto);
                    deEntregadoRecogidaPosteriorANada.remove(lto);
                } else if (lto.getRecogidaPosterior() == 'E' && lto.getEnvioDomicilio() != 'P') {
                    //Pasar de un Entregado Recogida Posterior a Nada
                    lto.setEnvioDomicilio('N');
                    lto.setRecogidaPosterior('N');

                    this.deEntregadoRecogidaPosteriorANada.add(lto);

                    //Quitar el codigo si esta en otro listado
                    deNadaAPendienteEnvioDomicilio.remove(lto);
                    dePendienteEnvioDomicilioARecogidaPosterior.remove(lto);
                    deRecogidaPosteriorAEntregadoRecogidaPosterior.remove(lto);
                }
                /*else if(lto.getEnvioDomicilio() == 'E' && lto.getRecogidaPosterior() != 'P')
                {
                    //Pasar de un Envio a Domicilio a Nada
                    lto.setEnvioDomicilio('N');
                    lto.setRecogidaPosterior('N');
                    
                    this.DeEntregadoEnvioDomicilioANada.add(lto);                    
                    
                    //Quitar el codigo si esta en otro listado
                    deNadaAPendienteEnvioDomicilio.remove(lto);
                    dePendienteEnvioDomicilioARecogidaPosterior.remove(lto);
                    deRecogidaPosteriorAEntregadoRecogidaPosterior.remove(lto);
                }*/
                lista.set(fila, lto);
                refrescarTablaArticulos(new ArticulosEnvioDomicilioTableModel(lista));
                t_table.setRowSelectionInterval(fila, fila);
                t_table.changeSelection(fila, 0, false, false);
            }
        } catch (IndexOutOfBoundsException e) {
            JPrincipal.getInstance().crearError("Debe de realizar una búsqueda antes de cambiar una línea de factura. ");
        } catch (Exception e) {
            LOG_POS.error(e.getMessage(), e);
            JPrincipal.getInstance().crearError("Error cambiando el estado de las líneas.");
        }
    }

    public void eliminar(List<String> lista, String codio) {
        lista.remove(codio);
    }

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
     * @author Gabriel Simbania
     * @param idtos
     * @param ticket
     */
    private void generarEnvioADomicilio(List<ItemDTO> idtos, TicketsAlm ticket) {

        try {
            String numeroFactura = ticket.getCodAlm() + ticket.getCodCaja() + String.format("%09d", ticket.getIdTicket());
            UsuarioDTO usuarioDTO = UtilUsuario.verificarUsuarioDTO(ticket.getUsuario());

            DocumentoDTO documentoDTO = new DocumentoDTO(numeroFactura, ticket.getCodAlm(), observacion.getText(), idtos, usuarioDTO);
            String envioDomicilioString = JsonUtil.objectToJson(documentoDTO);
            ProcesoEncolarThread envioDomicilioThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), envioDomicilioString, Variables.getVariable(Variables.QUEUE_ENVIO_DOMICILIO), Constantes.PROCESO_ENVIO_DOMICILIO, ticket.getUidTicket());
            envioDomicilioThread.start();
        } catch (Throwable e) {
            LOG_POS.error("No se pudo encolar el envio a domicilio " + e.getMessage());
        }

    }

    @Override
    public void iniciaFoco() {
        desactivarValidacion();
        LOG_POS.info("Iniciando Foco");
        t_factura2.requestFocus();
        activarValidacion();
    }

    private void activarValidacion() {
        for (IValidableForm e : formulario) {
            e.setValidacionHabilitada(true);
        }
    }

    private void desactivarValidacion() {
        for (IValidableForm e : formulario) {
            e.setValidacionHabilitada(false);
        }
    }

    @Override
    public void cerrarVentana() {
        limpiarFormulario();
        contenedor.setVisible(false);
    }
}
