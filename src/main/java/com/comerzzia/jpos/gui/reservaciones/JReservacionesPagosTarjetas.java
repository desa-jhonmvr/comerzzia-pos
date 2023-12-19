package com.comerzzia.jpos.gui.reservaciones;//GEN-LINE:variables

import com.comerzzia.jpos.entity.db.Usuarios;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCredito;
import com.comerzzia.jpos.servicios.tickets.ImporteInvalidoException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.gui.components.JPanelImagenFondo;
import com.comerzzia.jpos.gui.modelos.PlanesTarjetaCellRenderer;
import com.comerzzia.jpos.gui.modelos.PlanesTarjetaTableModel;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuariosDao;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.permisos.SinPermisosException;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.jpos.servicios.pagos.credito.PlanPagoCredito;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.credito.supermaxi.CreditoSupermaxiServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.mediospago.tarjetas.TarjetaCreditoSK;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoBonoSuperMaxiNavidad;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoBuilder;
import com.comerzzia.jpos.servicios.pagos.credito.ServicioCodigoOTP;
import com.comerzzia.jpos.servicios.promociones.PromocionFormaPagoException;
import com.comerzzia.jpos.servicios.promociones.articulos.PromocionArticuloException;
import com.comerzzia.jpos.util.enums.EnumEstadosValidacionOtp;
import es.mpsistemas.util.log.Logger;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author MGRI
 */
public class JReservacionesPagosTarjetas extends JDialog implements KeyListener {

    private static final Logger log = Logger.getMLogger(JReservacionesPagosTarjetas.class);
    private static final long serialVersionUID = 1L;
    private JReservacionesPagosV ventana_padre = null;
    private PlanesTarjetaTableModel planesTableModel;
    private TicketS ticket;

    public void iniciaVista(TicketS ticket) {
        log.info("Función IniciaVista ()");
        this.ticket = ticket;
        jError.setText("");
        refrescarTablaPlanes();
        t_total.requestFocus();

    }

    public void iniciaVista(PlanPagoCredito p, TicketS ticket) {
        log.info("Función IniciaVista (PlanPagoCredito)");
        this.ticket = ticket;
        jError.setText("");
        refrescarTablaPlanes();
        seleccionaPlan(p);
        t_total.requestFocus();
    }

    public void setPlanesTableModel(PlanesTarjetaTableModel planesTableModel) {
        this.planesTableModel = planesTableModel;
    }
    private PagoCredito pagoCredito;

    public PagoCredito getPagoCredito() {
        return pagoCredito;
    }

    public void setPagoCredito(PagoCredito pagoCredito) {
        this.pagoCredito = pagoCredito;
    }

    public JReservacionesPagosTarjetas() {
        super();
        initComponents();
    }

    public JReservacionesPagosTarjetas(JReservacionesPagosV ventana_padre) {
        super();
        this.ventana_padre = ventana_padre;
        initComponents();

        // Establezco la imagen de fondo del panel de fondo
        if (MediosPago.getInstancia() != null) {  //Sin esta comprobación el editor grafico elevara una excepción porque no encuentra la imagen
            try {
                String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
                URL myurl = this.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_pagos_tarjetas.png");

                p_fondo.setImagenFondo((Image) ImageIO.read(new File(myurl.getPath())));
            } catch (IOException ex) {
                log.error("Error cargando imágen de fondo");
            }
        }

        //Transparencia para latabla
        Border empty = new EmptyBorder(0, 0, 0, 0);

        sp_t_planes.setBackground(null);
        sp_t_planes.setOpaque(false);
        sp_t_planes.getViewport().setBackground(null);
        sp_t_planes.setViewportBorder(empty);
        sp_t_planes.getViewport().setOpaque(false);
        sp_t_planes.setBorder(null);
        t_planes.setBorder(empty);
        t_planes.setBackground(null);

        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent we) {
            }

            @Override
            public void windowClosing(WindowEvent we) {
            }

            @Override
            public void windowClosed(WindowEvent we) {
            }

            @Override
            public void windowIconified(WindowEvent we) {
            }

            @Override
            public void windowDeiconified(WindowEvent we) {
            }

            @Override
            public void windowActivated(WindowEvent we) {
            }

            @Override
            public void windowDeactivated(WindowEvent we) {
            }
        });
        jError.setHorizontalAlignment(SwingConstants.CENTER);
        t_planes.setDefaultRenderer(Object.class, new PlanesTarjetaCellRenderer());
        t_total.addKeyListener(this);
        t_cuota.addKeyListener(this);
        t_aPagar.addKeyListener(this);
        addFunctionKeys();
        crearAccionFocoTabla(p_fondo, t_planes, KeyEvent.VK_T, InputEvent.CTRL_MASK);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new com.comerzzia.jpos.gui.components.JPanelImagenFondo(true);
        p_fondo = new com.comerzzia.jpos.gui.components.JPanelImagenFondo(true);
        t_total = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        t_aPagar = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        t_cuota = new com.comerzzia.jpos.gui.components.form.JTextFieldForm();
        jLabel2 = new javax.swing.JLabel();
        jError = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        sp_t_planes = new javax.swing.JScrollPane();
        t_planes = new javax.swing.JTable();

        setTitle("Seleccionar plan Alt+n ");
        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(830, 400));
        setResizable(false);

        jPanel1.setMaximumSize(new java.awt.Dimension(810, 389));
        jPanel1.setMinimumSize(new java.awt.Dimension(810, 389));
        jPanel1.setPreferredSize(new java.awt.Dimension(810, 389));

        p_fondo.setBackground(new java.awt.Color(255, 255, 234));
        p_fondo.setFocusable(false);
        p_fondo.setMaximumSize(new java.awt.Dimension(810, 389));
        p_fondo.setMinimumSize(new java.awt.Dimension(810, 389));
        p_fondo.setPreferredSize(new java.awt.Dimension(810, 389));
        p_fondo.setRequestFocusEnabled(false);

        t_total.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        t_total.setPreferredSize(new java.awt.Dimension(60, 20));
        t_total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_totalActionPerformed(evt);
            }
        });

        jLabel1.setDisplayedMnemonic('o');
        jLabel1.setLabelFor(t_total);
        jLabel1.setText("Total:");

        jLabel3.setDisplayedMnemonic('v');
        jLabel3.setLabelFor(t_aPagar);
        jLabel3.setText("Valor del voucher:");

        t_aPagar.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        t_aPagar.setNextFocusableComponent(t_total);
        t_aPagar.setPreferredSize(new java.awt.Dimension(60, 20));

        t_cuota.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        t_cuota.setPreferredSize(new java.awt.Dimension(60, 20));

        jLabel2.setDisplayedMnemonic('c');
        jLabel2.setLabelFor(t_cuota);
        jLabel2.setText("Valor de la cuota:");

        jError.setForeground(new java.awt.Color(255, 0, 0));
        jError.setFocusable(false);
        jError.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getStyle() | java.awt.Font.BOLD, jLabel4.getFont().getSize() + 2));
        jLabel4.setText("Saldo de la cuenta");

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD, jLabel5.getFont().getSize() + 2));
        jLabel5.setText("Elija su forma de pago");

        sp_t_planes.setAutoscrolls(true);
        sp_t_planes.setHorizontalScrollBar(null);
        sp_t_planes.setMaximumSize(new java.awt.Dimension(749, 200));
        sp_t_planes.setMinimumSize(new java.awt.Dimension(749, 200));
        sp_t_planes.setPreferredSize(new java.awt.Dimension(749, 200));

        t_planes.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {},
                    {},
                    {},
                    {},
                    {},
                    {},
                    {},
                    {}
                },
                new String[]{}
        ));
        t_planes.setMaximumSize(new java.awt.Dimension(749, 200));
        t_planes.setMinimumSize(new java.awt.Dimension(749, 200));
        t_planes.setOpaque(false);
        t_planes.setPreferredSize(new java.awt.Dimension(749, 180));
        t_planes.setRequestFocusEnabled(false);
        t_planes.setRowHeight(20);
        t_planes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_planesKeyTyped(evt);
            }
        });
        sp_t_planes.setViewportView(t_planes);

        javax.swing.GroupLayout p_fondoLayout = new javax.swing.GroupLayout(p_fondo);
        p_fondo.setLayout(p_fondoLayout);
        p_fondoLayout.setHorizontalGroup(
                p_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(p_fondoLayout.createSequentialGroup()
                                .addGroup(p_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(p_fondoLayout.createSequentialGroup()
                                                .addGap(38, 38, 38)
                                                .addComponent(jLabel4)
                                                .addGap(176, 176, 176)
                                                .addComponent(jLabel5))
                                        .addGroup(p_fondoLayout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addGroup(p_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(sp_t_planes, javax.swing.GroupLayout.PREFERRED_SIZE, 749, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(p_fondoLayout.createSequentialGroup()
                                                                .addComponent(jLabel1)
                                                                .addGap(6, 6, 6)
                                                                .addComponent(t_total, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(68, 68, 68)
                                                                .addComponent(jLabel2)
                                                                .addGap(6, 6, 6)
                                                                .addComponent(t_cuota, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel3)
                                                                .addGap(6, 6, 6)
                                                                .addComponent(t_aPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(p_fondoLayout.createSequentialGroup()
                                                .addGap(85, 85, 85)
                                                .addComponent(jError, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(37, Short.MAX_VALUE))
        );
        p_fondoLayout.setVerticalGroup(
                p_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(p_fondoLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(p_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5))
                                .addGap(12, 12, 12)
                                .addGroup(p_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(t_total, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(t_cuota, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(t_aPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(p_fondoLayout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addGroup(p_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel3))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jError, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(sp_t_planes, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(60, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(p_fondo, javax.swing.GroupLayout.PREFERRED_SIZE, 816, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(p_fondo, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(222, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(29, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void t_totalActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void t_planesKeyTyped(java.awt.event.KeyEvent evt) {
        if (evt.getKeyChar() == '\n') {

            añadelinea((t_planes.getSelectedRow() != 0) ? t_planes.getSelectedRow() - 1 : t_planes.getRowCount() - 1);
        }
    }
    // Variables declaration - do not modify                     
    private javax.swing.JLabel jError;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo jPanel1;
    private com.comerzzia.jpos.gui.components.JPanelImagenFondo p_fondo;
    private javax.swing.JScrollPane sp_t_planes;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_aPagar;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_cuota;
    private javax.swing.JTable t_planes;
    private com.comerzzia.jpos.gui.components.form.JTextFieldForm t_total;
    // End of variables declaration                   

    public void inicializar(MedioPagoBean medioPago, TicketS ticket, String autorizador, TarjetaCredito tc, Byte modo) throws ValidationException, PromocionArticuloException, PromocionFormaPagoException {
        boolean tarjetaDuplicada = false;

        if (!ticket.getPagos().getPagos().isEmpty()) {
            for (Pago pagos : ticket.getPagos().getPagos()) {
                if (pagos.getMedioPagoActivo().getCodMedioPago().equals("220")) {
                    if (!((PagoBonoSuperMaxiNavidad) pagos).getNumeroTarjetaBono().equals(tc.getNumero())) {
                        pagoCredito = PagoCreditoBuilder.createPagoCredito(medioPago, autorizador, tc, ticket, modo);
                        planesTableModel = new PlanesTarjetaTableModel(pagoCredito);
                        iniciaVista(ticket);
                        tarjetaDuplicada = false;
                    } else {
                        tarjetaDuplicada = true;
                        break;
                    }
                } else {
                    tarjetaDuplicada = false;
                }
            }
        } else {
            pagoCredito = PagoCreditoBuilder.createPagoCredito(medioPago, autorizador, tc, ticket, modo);
            planesTableModel = new PlanesTarjetaTableModel(pagoCredito);
            iniciaVista(ticket);
            tarjetaDuplicada = false;
        }
        if (tarjetaDuplicada) {
            throw new ValidationException("Ya se coloco la tarjeta.");
        } else {
            pagoCredito = PagoCreditoBuilder.createPagoCredito(medioPago, autorizador, tc, ticket, modo);
            planesTableModel = new PlanesTarjetaTableModel(pagoCredito);
            iniciaVista(ticket);
        }

    }

    public void refrescarTablaPlanes() {
        t_planes.setModel(planesTableModel);
        if (pagoCredito.getTotal() == null) {
            t_total.setText("");
        } else {
            t_total.setText(pagoCredito.getTotal().toString());
        }
        if (pagoCredito.getCuota() == null) {
            t_cuota.setText("");
        } else {
            t_cuota.setText(pagoCredito.getCuota().toString());
        }
        if (pagoCredito.getUstedPaga() == null) {
            t_aPagar.setText("");
        } else {
            t_aPagar.setText(pagoCredito.getUstedPaga().toString());
        }

        t_planes.setMinimumSize(new Dimension(729, t_planes.getRowCount() * t_planes.getRowHeight() + 2));
        t_planes.setSize(new Dimension(729, t_planes.getRowCount() * t_planes.getRowHeight() + 2));
        t_planes.setPreferredSize(new Dimension(729, t_planes.getRowCount() * t_planes.getRowHeight() + 2));

        t_planes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        t_planes.getColumnModel().getColumn(0).setPreferredWidth(27);
        t_planes.getColumnModel().getColumn(1).setPreferredWidth(140); // 113->181
        t_planes.getColumnModel().getColumn(2).setPreferredWidth(75);
        t_planes.getColumnModel().getColumn(3).setPreferredWidth(75); // Interes        
        t_planes.getColumnModel().getColumn(4).setPreferredWidth(160);
        t_planes.getColumnModel().getColumn(5).setPreferredWidth(90);
        t_planes.getColumnModel().getColumn(6).setPreferredWidth(90);

    }

    @Override
    public void keyTyped(KeyEvent ke) {
        if (ke.getKeyChar() != '\n') {
            return;
        }
        try {
            if (t_total.hasFocus()) {
                log.info("Recalcular planes por total");
                jError.setText("");
                try {
                    pagoCredito.recalcularPlanesFromTotal(t_total.getText());
                } catch (PromocionArticuloException ex) {
                    jError.setText("Esta promoción no existe");
                } catch (PromocionFormaPagoException ex) {
                    jError.setText("Debe existir solo una promoción para cada una.");
                }
            } else if (t_aPagar.hasFocus()) {
                jError.setText("");
                pagoCredito.recalcularPlanesFromAPagar(t_aPagar.getText());
                log.info("Acción realizar pago simple mediante intro en entregado");
                if (pagoCredito.getPlanes().isEmpty()) {
                    log.info("Acción realizar pago simple mediante intro en entregado");
                    jError.setText("No existen planes con la cantidad a pagar seleccionada.");
                }
            } else if (t_cuota.hasFocus()) {
                jError.setText("");
                pagoCredito.recalcularPlanesFromCuota(t_cuota.getText());
                log.info("Acción realizar pago simple mediante intro en entregado");
                if (pagoCredito.getPlanes().isEmpty()) {
                    jError.setText("No existen planes con la cuota seleccionada.");
                    log.info("Acción realizar pago simple mediante intro en entregado");
                }
            }
        } catch (PagoInvalidException e) {
            log.debug("Pago invalido", e);
            jError.setText(e.getMessage());
        }
        refrescarTablaPlanes();
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    private void addHotKey(KeyStroke keyStroke, String inputActionKey, Action listener) {
        ActionMap actionMap = jPanel1.getActionMap();
        InputMap inputMap = jPanel1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStroke, inputActionKey);
        actionMap.put(inputActionKey, listener);
    }

// <editor-fold defaultstate="collapsed" desc="FUNCTION KEYS">  
    private void addFunctionKeys() {
        KeyStroke alt1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK);
        Action listeneralt1 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                añadelinea(0);
            }
        };
        addHotKey(alt1, "IdentClientAlt1", listeneralt1);

        KeyStroke alt2 = KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK);
        Action listeneralt2 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                añadelinea(1);
            }
        };
        addHotKey(alt2, "IdentClientAlt2", listeneralt2);

        KeyStroke alt3 = KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK);
        Action listeneralt3 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                añadelinea(2);
            }
        };
        addHotKey(alt3, "IdentClientAlt3", listeneralt3);

        KeyStroke alt4 = KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_DOWN_MASK);
        Action listeneralt4 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                añadelinea(3);
            }
        };
        addHotKey(alt4, "IdentClientAlt4", listeneralt4);

        KeyStroke alt5 = KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.ALT_DOWN_MASK);
        Action listeneralt5 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                añadelinea(4);
            }
        };
        addHotKey(alt5, "IdentClientAlt5", listeneralt5);

        KeyStroke alt6 = KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.ALT_DOWN_MASK);
        Action listeneralt6 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                añadelinea(5);
            }
        };
        addHotKey(alt6, "IdentClientAlt6", listeneralt6);

        KeyStroke alt7 = KeyStroke.getKeyStroke(KeyEvent.VK_7, InputEvent.ALT_DOWN_MASK);
        Action listeneralt7 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                añadelinea(6);
            }
        };
        addHotKey(alt7, "IdentClientAlt7", listeneralt7);

        KeyStroke alt8 = KeyStroke.getKeyStroke(KeyEvent.VK_8, InputEvent.ALT_DOWN_MASK);
        Action listeneralt8 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                añadelinea(7);
            }
        };
        addHotKey(alt8, "IdentClientAlt8", listeneralt8);

        KeyStroke alt9 = KeyStroke.getKeyStroke(KeyEvent.VK_9, InputEvent.ALT_DOWN_MASK);
        Action listeneralt9 = new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                añadelinea(8);
            }
        };
        addHotKey(alt9, "IdentClientAlt9", listeneralt9);

        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action listeneresc = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
            }
        };
        addHotKey(esc, "IdentClientesc", listeneresc);

        KeyStroke ctrlM = KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK);
        Action listenerCtrlM = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                eliminarFiltroConsumoMinimo();
            }
        };
        addHotKey(ctrlM, "listenerCtrlM", listenerCtrlM);

    }
// </editor-fold>  

    private void eliminarFiltroConsumoMinimo() {
        try {
            String usuarioAdministrador = JPrincipal.getInstance().compruebaAutorizacion(Operaciones.AUTORIZAR_MEDIO_PAGO);
            ticket.setAutorizadorVenta(usuarioAdministrador);

            pagoCredito.setFiltrarConsumoMinimo(false);
            pagoCredito.recalcular();
            refrescarTablaPlanes();
        } catch (SinPermisosException e) {
            log.debug("eliminarFiltroConsumoMinimo() - El usuario no tiene permisos para eliminar filtro por consumo mínimo.");
        }
    }

    private void añadelinea(int i) {
        try {
            log.info("Acción realizar pago desde linea de plan, línea: " + i);
            if (!(i < 0) && !(i > (pagoCredito.getPlanes().size() - 1))) {
                pagoCredito.setPlanSeleccionado(i);
                //pagoCredito.getPlanSeleccionado().isPisoValido(BigDecimal.ZERO, rootPaneCheckingEnabled);
                if (!ventana_padre.isEditando()) {
                    boolean addLinea = true;

                    if (pagoCredito instanceof PagoBonoSuperMaxiNavidad && pagoCredito.getMedioPagoActivo().isBonoSuperMaxiNavidad()) {
                        try {
                            BigDecimal valor = CreditoSupermaxiServices.consultaSaldoBonoSupermaxi(pagoCredito.getNumeroTarjeta());
                            if (pagoCredito.getUstedPaga().compareTo(valor) > 0) {
                                JPrincipal.getInstance().crearAdvertencia("El total es mayor al saldo del bono, por favor colocar el valor correcto: " + valor);
                                return;
                            }
                        } catch (ValidationException v) {
                            JPrincipal.getInstance().crearAdvertencia(v.getMessage());
                            return;
                        } catch (Exception e) {
                            log.error("Error al intentar leer la tarjeta");
                        }
                    }

                    if (pagoCredito.getTarjetaCredito() != null && pagoCredito.getTarjetaCredito().isLecturaDesdeCedula()) {
                        // Se trata de una lectura de credito directo desde una cédula
                        BigDecimal importeMaximoAutorizacion = VariablesAlm.getVariableAsBigDecimal(VariablesAlm.POS_IMPORTE_MAXIMO_AUTORIZACION_CREDITO_DIRECTO);
                        if (pagoCredito.getUstedPaga().compareTo(importeMaximoAutorizacion) > 0) {
                            String compruebaAutorizacion = JPrincipal.getInstance().compruebaAutorizacion(Operaciones.AUTORIZAR_PAGO_CREDITO_DIRECTO_SOBRE_IMPORTE_MINIMO, "<html>Autorizar Crédito Directo desde Cédula <br/>sobre importe límite</html>");
                        } else {
                            String compruebaAutorizacion = JPrincipal.getInstance().compruebaAutorizacion(Operaciones.AUTORIZAR_PAGO_CREDITO_DIRECTO_BAJO_IMPORTE_MINIMO, "Autorizar Crédito Directo desde Cédula");
                        }

                    }

                    //Comprobamos si se trata de una tarjeta SUKASA (Crédito Directo) y si el piso (mínimo/máximo) es correcto
                    if (pagoCredito.getMedioPagoActivo().isTarjetaSukasa() && !pagoCredito.getPlanSeleccionado().isPisoValido(pagoCredito.getSaldoInicial(), pagoCredito.getPlanSeleccionado().getVencimiento().getPisoMinimo((Pago) pagoCredito) != BigDecimal.ZERO)) {
                        addLinea = false;
                        log.debug("añadelinea() - Solicitamos autorización telefónica");

                        String mensaje = null;
                        if (pagoCredito.getUstedPaga().compareTo(pagoCredito.getPlanSeleccionado().getVencimiento().getPisoMaximo()) > 0) {
                            //JPrincipal.getInstance().crearVentanaInformacion("El valor de esta venta supera el límite de piso. Solicite una autorización.", WIDTH);
                            mensaje = "<html><body>El valor de esta venta supera el límite de piso. Solicite una autorización.</body></html>";
                        } else {
                            //JPrincipal.getInstance().crearVentanaInformacion("El valor de esta venta es inferior al piso mínimo. Solicite una autorización.", WIDTH);
                            mensaje = "<html><body>El valor de esta venta es inferior al piso mínimo. Solicite una autorización.</body></html>";
                        }
                        this.setAlwaysOnTop(false);

                        EnumEstadosValidacionOtp estadoValido = EnumEstadosValidacionOtp.NO_VALIDO;

                        if (VariablesAlm.getVariableAsBooleanActual(Variables.POS_VALIDACION_OTP)) { //G.S. variable para la validacion
                            if (!Sesion.getDatosConfiguracion().isModoDesarrollo()) {
                                try {
                                    Integer numeroCredito = ((TarjetaCreditoSK) pagoCredito.getTarjetaCredito()).getPlastico().getNumeroCredito();
                                    Usuarios usuario = UsuariosDao.obtenerUsuarioporNumero(ticket.getCajero().getUsuario());
                                    ServicioCodigoOTP.enviarOTP((long) numeroCredito, usuario, ticket.getUid_ticket());
                                    estadoValido = JPrincipal.getInstance().crearVentanaIngresarCodigoOTP("<html> Ingrese el código OTP enviado a su número de celular </html>", ticket.getUid_ticket(), pagoCredito);
                                } catch (Exception ex) {
                                    estadoValido = EnumEstadosValidacionOtp.AUTORIZACION_MANUAL;
                                    log.error("Error al validar mediante el OTP: " + ex.getMessage(), ex);
                                }
                            } else {
                                estadoValido = EnumEstadosValidacionOtp.AUTORIZACION_MANUAL;
                            }
                        } else {
                            estadoValido = EnumEstadosValidacionOtp.AUTORIZACION_MANUAL;
                        }
                        if (EnumEstadosValidacionOtp.AUTORIZACION_MANUAL.equals(estadoValido)) {
                            JPrincipal.getInstance().crearVentanaIntroduccionAutorizacion(pagoCredito, mensaje, ticket.isVentaManual());
                            if (pagoCredito.getCodigoValidacionManual() != null && !pagoCredito.getCodigoValidacionManual().isEmpty()) {
                                pagoCredito.setFalloValidacion(false);
                                pagoCredito.setValidadoManual(true);
                                pagoCredito.setAuditoria(); // Este método asigna un nº de auditoría al medio de pago
                                addLinea = true;
                            }
                        } else if (EnumEstadosValidacionOtp.VALIDO.equals(estadoValido)) {
                            addLinea = true;
                        }

                    }

                    if (addLinea) {
                        // Si el permiso falla, no pasa por aquí.
                        ticket.crearNuevaLineaPago(pagoCredito);
//                        TicketPromocionesFiltrosPagos promoFiltroPagos = Sesion.getTicket().getTicketPromociones().getPromocionesFiltrosPagos();
//                        Promocion promocion = promoFiltroPagos.isTodosPagosSeleccionados(pagoCredito.getPagos());
//                        if(promocion==null){
////                            Promocion promocion2 = promoFiltroPagos.obtenerPromocion();
////                            List<PromocionTipoDescuentoCombinado> a = Sesion.getPromocionesPruebaricado();
////                            promocion2.aplicaLineasMultiple(ticket.getLineas());
//                             
//                        }
//                        programar aqui
                    }
                }
                ticket.getPagos().recalculaTotales();
                ventana_padre.setEditando(false);
                this.setVisible(false);
            }
        } catch (ImporteInvalidoException e) {
            log.error("añadelinea() - Importe invalido :" + e.getMessage(), e);
        } catch (SinPermisosException ex) {
            log.info("añadelinea() - El usuario no tien permiso para realizar la operación");
        } catch (Exception e) {
            log.error("añadelinea() - Error no controlado seleccionando plan :" + e.getMessage(), e);
        }

    }

    protected void crearAccionFocoTabla(JPanelImagenFondo panel, final JTable cmp, int tecla, int modificador) {
        KeyStroke ks = KeyStroke.getKeyStroke(tecla, modificador);
        Action listenerk = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (cmp.getRowCount() >= 0) {
                    ListSelectionModel selectionModel = cmp.getSelectionModel();
                    selectionModel.setSelectionInterval(0, 0);
                }
                cmp.requestFocus();
            }
        };
        addHotKey(panel, ks, "SelecciondeLineaTabla", listenerk);
    }

    protected void addHotKey(JPanelImagenFondo panel, KeyStroke keyStroke, String inputActionKey, Action listener) {
        ActionMap actionMap = panel.getActionMap();
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStroke, inputActionKey);
        actionMap.put(inputActionKey, listener);
    }

    void seleccionaPlan(PlanPagoCredito planSeleccionado) {
        t_planes.requestFocus();
        ListSelectionModel selectionModel = t_planes.getSelectionModel();
        int i = 0;
        boolean enc = false;
        while (i < t_planes.getModel().getRowCount() && !enc) {
            if (planSeleccionado.equals(t_planes.getModel().getValueAt(i, 0))) {
                enc = true;
                selectionModel.setSelectionInterval(i, i);
            }
            i++;
        }
    }

}
