package com.comerzzia.jpos.gui.modelos;

import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.persistencia.articulos.ArticulosDao;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MGRI
 */
public class TicketTableCellRenderer implements TableCellRenderer {

    public static final int MODO_SIN_CANTIDAD = 0;   //
    public static final int MODO_CON_CANTIDAD = 1;   // 
    public static int[] anchosColumnasModoSinCantidad = {88, 190, 1, 63};
    public static int[] anchosColumnasModoConCantidad = {72, 156, 70, 63};
    public static int[] tamanhosTextoColumnasSinCantidad = {-2, 1, -2, 0};
    public static int[] tamanhosTextoColumnasConCantidad = {-3, 0, -2, -1};
    public static int fuenteColumna0;
    public static int fuenteColumna1;
    public static int fuenteColumna2;
    public static int fuenteColumna3;

    public static int[] getAnchosColumna() {
        if (modo == MODO_CON_CANTIDAD) {
            return anchosColumnasModoConCantidad;
        } else {
            return anchosColumnasModoSinCantidad;
        }
    }
    private static int modo = 0;
    /* Variables */
    private static final Logger log = Logger.getMLogger(TicketTableCellRenderer.class);
    private static Font fuente;

    public TicketTableCellRenderer() {
        super();
        try {
            if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_LINEA_MULTIPLE_EN_TICKET)) {
                modo = MODO_CON_CANTIDAD;
                fuenteColumna0 = tamanhosTextoColumnasConCantidad[0];
                fuenteColumna1 = tamanhosTextoColumnasConCantidad[1];
                fuenteColumna2 = tamanhosTextoColumnasConCantidad[2];
                fuenteColumna3 = tamanhosTextoColumnasConCantidad[3];
            } else {
                modo = MODO_SIN_CANTIDAD;
                fuenteColumna0 = tamanhosTextoColumnasSinCantidad[0];
                fuenteColumna1 = tamanhosTextoColumnasSinCantidad[1];
                fuenteColumna2 = tamanhosTextoColumnasSinCantidad[2];
                fuenteColumna3 = tamanhosTextoColumnasSinCantidad[3];
            }
        } catch (Exception e) {
            log.error("Error obteniendo la variable de configuración POS.CONFIG.FUNC.LINEA_TICKET_MULTIPLE .Asegurense de que esta correctamente configurada. ");
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {

        if (Variables.getVariable(Variables.POS_UI_FUENTE_BOTONES_TIPO) != null && !Variables.getVariable(Variables.POS_UI_FUENTE_BOTONES_TAMANO).isEmpty()) {
            fuente = new Font(Variables.getVariable(Variables.POS_UI_FUENTE_BOTONES_TIPO), 0, Integer.parseInt(Variables.getVariable(Variables.POS_UI_FUENTE_BOTONES_TAMANO)));
        }

        if (object instanceof String) {
            return getTableCellRendererString(jtable, object, isSelected, hasFocus, row, col);
        }
        if (object instanceof LineaTicket) {
            return getTableCellRendererLinea(jtable, object, isSelected, hasFocus, row, col);
        }
        if (object instanceof DescuentoTicket) {
            return getTableCellRendererDescuento(jtable, object, isSelected, hasFocus, row, col);
        }
        if (object instanceof ItemDTO) {
            return getTableCellRendererLineaOtroLocal(jtable, object, isSelected, hasFocus, row, col);
        }
        log.fatal("ERROR: Imposible renderizar elemento en tabla ticket de venta: Objeto de clase no contemplado.");
        throw new RuntimeException("Se está intentando renderizar un objeto no contemplado.");

    }

    private Component getTableCellRendererString(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {

        JLabel etiqueta = new JLabel();
        etiqueta.setFont(fuente);
        String linea = (String) object;
        etiqueta.setText(linea);
        return etiqueta;
    }

    private Component getTableCellRendererDescuento(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {

        JLabel etiqueta = new JLabel();

        etiqueta.setFont(fuente);

        etiqueta.setVerticalAlignment(SwingConstants.TOP);
        DescuentoTicket descuento = (DescuentoTicket) object;

        switch (col) {
            case 0:
                etiqueta.setText("");
                break;
            case 1:
                etiqueta.setText(descuento.getDescripcion());
                etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
                break;
            case 2:
                etiqueta.setText("");
                break;
            case 3:
                etiqueta.setText(descuento.getDescuentoTotalString());
                etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
                break;
        }
        etiqueta.setForeground(Color.red);

        return etiqueta;
    }

    private Component getTableCellRendererLinea(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        etiqueta.setVerticalAlignment(SwingConstants.TOP);
        LineaTicket linea = (LineaTicket) object;
        etiqueta.setFont(fuente);

        Font f = etiqueta.getFont();
        switch (col) {
            case 0:
                String descripcion1 = "<html><span style='white-space:nowrap;'><span style='font-size: 11pt;'>";
                //etiqueta.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + fuenteColumna0));
                descripcion1 += linea.getArticulo().getCodart();
                descripcion1 += "</span>";
                if (linea.getStockDisponible() != null) {
                    descripcion1 += "<span style='font-size: 11pt;color:blue;'><br/>Stock L: " + linea.getStockDisponible();
                    descripcion1 += "</span>";
                }
                if (linea.getStockDisponibleBodega() != null) {
                    descripcion1 += "<span style='font-size: 11pt;color:blue;'><br/>Ext B: " + linea.getStockDisponibleBodega();
                    descripcion1 += "</span>";
                }
                if (linea.getStockDisponibleLocales() != null) {
                    if (jtable.getRowHeight(row) < 54) {
                        jtable.setRowHeight(row, 54);
                    }
                    descripcion1 += "<span style='font-size: 11pt;color:red;'><br/>Ext Loc: " + linea.getStockDisponibleLocales();
                    descripcion1 += "</span>";
                }
                descripcion1 += "</span></html>";
                etiqueta.setText(descripcion1);
                break;
            case 1:
                String descripcion = "<html><span style='white-space:nowrap;'><span style='color:blue;'><b>";
                if (linea.getDatosAdicionales() != null && linea.getDatosAdicionales().isEnvioDomicilio()) {
                    descripcion += "(E) ";
                }
                if (linea.getDatosAdicionales() != null && linea.getDatosAdicionales().isRecogidaPosterior()) {
                    descripcion += "(R) ";
                }
                descripcion += "</b></span>";
                if (linea.getDescripcionAdicional() != null) {
                    descripcion += "<span style='color:red;'>" + linea.getDescripcionAdicional() + " </span>";
                }
                descripcion += "<span style='letter-spacing=1px'>" + linea.getArticulo().getDesart() + "</span>";
                if (linea.tieneImpresionDescuento()) {
                    descripcion += "<br/><span style='color:red;'>" + linea.getImpresionLineaDescuento().getDescripcion() + "</span>";
                }
                if (linea.tieneGarantiaOriginal()) {
                    descripcion += "<br/><span style='color:blue;'>" + "Garantía original" + "</span>";
                }
                if (linea.getArticulo().getColeccion() != null && linea.getArticulo().getColeccion().equals(ArticulosDao.CODIGO_DESCONTINUADO)) {
                    descripcion += "<span style='color:red;font-size: 9pt'>" + " Descontinuado " + "</span>";
                }
                // TODO: TICKET - Este cambio de altura en la fila está provocando renderización
                // lenta de la segunda linea de esta fila. Habría que arreglarlo porque muestra
                // un efecto de pantallazo sobre esas líneas.
                jtable.setRowHeight(row, 36);
                if (linea.tieneImpresionDescuento() && linea.tieneGarantiaOriginal()) {
                    if (jtable.getRowHeight(row) != 54) {
                        jtable.setRowHeight(row, 54);
                    }
                } else if ((linea.tieneImpresionDescuento() || linea.tieneGarantiaOriginal()) && jtable.getRowHeight(row) != 36) {
                    jtable.setRowHeight(row, 54);
                }

                if (linea.getStockDisponibleBodega() != null) {
                    if (jtable.getRowHeight(row) < 54) {
                        jtable.setRowHeight(row, 54);
                    }
                }

                descripcion += "</span></html>";
                etiqueta.setText(descripcion);
                etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
                etiqueta.setFont(new Font(f.getName(), Font.PLAIN, f.getSize() + fuenteColumna1));
                break;
            case 2:
                if (modo == MODO_SIN_CANTIDAD) {
                    etiqueta.setText("");
                } else {
                    String texto = linea.getCantidad() + " x " + linea.getPrecioTotalPantalla();
                    etiqueta.setFont(new java.awt.Font(f.getName(), Font.PLAIN, f.getSize() + fuenteColumna2));

                    if (linea.tieneImpresionDescuento()) {
                        String textoDescuento = "-" + Numero.redondear4(linea.getDescuentoScale()) + "%";
                        texto = getLineaDoble(texto, textoDescuento);
                    }

                    etiqueta.setText(texto);
                    etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
                    etiqueta.setVerticalAlignment(SwingConstants.TOP);
                }
                break;
            case 3:

                etiqueta.setFont(new java.awt.Font(f.getName(), f.getStyle(), f.getSize() + fuenteColumna3));
                etiqueta.setText(linea.getImporteTotalPantalla());
                etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
                if (linea.tieneImpresionDescuento()) {
                    String textoDescuento = "$ -" + Numero.redondear(linea.getImpresionLineaDescuento().getDescuentoTotal());
                    etiqueta.setText(getLineaDoble(linea.getImporteTotalPantalla(), textoDescuento));
                }
                break;
        }
        if (row == jtable.getSelectionModel().getMinSelectionIndex()) {
            etiqueta.setOpaque(true);
            if (col != 0) //etiqueta.setFont(new java.awt.Font(f.getName(), Font.PLAIN, 11));
            {
                etiqueta.setBackground((Color) UIManager.get("Table.selectionBackground"));
            }
            etiqueta.setForeground((Color) UIManager.get("Table.selectionForeground"));
        }
        return etiqueta;
    }

    private Component getTableCellRendererLineaOtroLocal(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        etiqueta.setVerticalAlignment(SwingConstants.TOP);
        ItemDTO linea = (ItemDTO) object;
        etiqueta.setFont(fuente);

        Font f = etiqueta.getFont();
        switch (col) {
            case 0:
                etiqueta.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + fuenteColumna0));
                etiqueta.setText(linea.getCodigoI());
                // etiqueta.setText(linea.getArticulo().getCodart()+"-"+linea.getCodigoBarras());
                etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
                etiqueta.setVerticalAlignment(SwingConstants.TOP);
                Border paddingBorder = BorderFactory.createEmptyBorder(4, 0, 0, 0);
                etiqueta.setBorder(BorderFactory.createCompoundBorder(etiqueta.getBorder(), paddingBorder));

                break;
            case 1:
                String descripcion = "<html><span style='white-space:nowrap;'>";
//                if (linea.getDatosAdicionales() != null && linea.getDatosAdicionales().isEnvioDomicilio()) {
//                    descripcion += "(E) ";
//                }
//                if (linea.getDatosAdicionales() != null && linea.getDatosAdicionales().isRecogidaPosterior()) {
//                    descripcion += "(R) ";
//                }
//                descripcion += "</b></span>";
//                if (linea.getDescripcionAdicional() != null) {
//                    descripcion += "<span style='color:red;'>" + linea.getDescripcionAdicional() + " </span>";
//                }
                descripcion += "<span style='letter-spacing=1px'>" + linea.getDescripcion() + "</span>";
//                if (linea.tieneImpresionDescuento()) {
//                    descripcion += "<br/><span style='color:red;'>" + linea.getImpresionLineaDescuento().getDescripcion() + "</span>";
//                }
//                if (linea.tieneGarantiaOriginal()) {
//                    descripcion += "<br/><span style='color:blue;'>" + "Garantía original" + "</span>";
//                }
//                if (linea.getArticulo().getColeccion() != null && linea.getArticulo().getColeccion().equals(ArticulosDao.CODIGO_DESCONTINUADO)) {
//                    descripcion += "<span style='color:red;font-size: 9pt'>" + " Descontinuado " + "</span>";
//                }
                // TODO: TICKET - Este cambio de altura en la fila está provocando renderización
                // lenta de la segunda linea de esta fila. Habría que arreglarlo porque muestra
                // un efecto de pantallazo sobre esas líneas.
//                if (linea.tieneImpresionDescuento() && linea.tieneGarantiaOriginal()){
//                    if (jtable.getRowHeight(row) != 54){
//                        jtable.setRowHeight(row, 54);
//                    }
//                }
//                else if ((linea.tieneImpresionDescuento() || linea.tieneGarantiaOriginal()) && jtable.getRowHeight(row) != 36) {
                jtable.setRowHeight(row, 36);
//                }

                descripcion += "</span></html>";
                etiqueta.setText(descripcion);
                etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
                etiqueta.setFont(new Font(f.getName(), Font.PLAIN, f.getSize() + fuenteColumna1));
                break;
            case 2:
                if (modo == MODO_SIN_CANTIDAD) {
                    etiqueta.setText("");
                } else {
                    String texto = linea.getCantidad() + " x " + linea.getPrecioTotalOrigen();
                    etiqueta.setFont(new java.awt.Font(f.getName(), Font.PLAIN, f.getSize() + fuenteColumna2));

//                    if (linea.tieneImpresionDescuento()) {
//                        String textoDescuento = "-" + Numero.redondear(linea.getDescuentoScale()) + "%";
//                        texto = getLineaDoble(texto, textoDescuento);
//                    }
                    etiqueta.setText(texto);
                    etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
                    etiqueta.setVerticalAlignment(SwingConstants.TOP);
                }
                break;
            case 3:

                etiqueta.setFont(new java.awt.Font(f.getName(), f.getStyle(), f.getSize() + fuenteColumna3));
                etiqueta.setText(linea.getImporteTotalFinal().toString());
                etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
//                if (linea.tieneImpresionDescuento()) {
//                    String textoDescuento = "$ -" + Numero.redondear(linea.getImpresionLineaDescuento().getDescuentoTotal());
//                    etiqueta.setText(getLineaDoble(linea.getImporteTotalPantalla(), textoDescuento));
//                }
                break;
        }
        if (row == jtable.getSelectionModel().getMinSelectionIndex()) {
            etiqueta.setOpaque(true);
            if (col != 0) //etiqueta.setFont(new java.awt.Font(f.getName(), Font.PLAIN, 11));
            {
                etiqueta.setBackground((Color) UIManager.get("Table.selectionBackground"));
            }
            etiqueta.setForeground((Color) UIManager.get("Table.selectionForeground"));
        }
        return etiqueta;
    }

    private String getLineaDoble(String linea1, String linea2) {
        return "<html>" + linea1 + "<br/><span style='color:red;'>" + linea2 + "</span></html>";
    }

    public static boolean isModoConCantidad() {
        return (modo == MODO_CON_CANTIDAD);
    }
}
