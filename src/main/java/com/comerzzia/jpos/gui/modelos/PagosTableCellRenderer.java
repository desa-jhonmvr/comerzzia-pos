package com.comerzzia.jpos.gui.modelos;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCredito;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionPagoTicket;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.RoundingMode;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MGRI
 */
public class PagosTableCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object object, boolean isSelected, boolean hasFocus, int row, int col) {
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();        
        Pago pago = (Pago) object;
        etiqueta.setOpaque(true);
        Color colorFondoSeleccion = new Color(197, 239, 253);
        Color colorTextoSeleccion = new Color(155, 68, 4);
        Font negrita = new Font(etiqueta.getFont().getFontName(), Font.BOLD, etiqueta.getFont().getSize());

        PagoCredito pagoC = null;
        PagoCreditoSK pagoSK = null;
        if (pago instanceof PagoCredito) {
            pagoC = (PagoCredito) pago;
        }
        if (pago instanceof PagoCreditoSK) {
            pagoSK = (PagoCreditoSK) pago;
        }

        if (isSelected) {
            etiqueta.setBackground(colorFondoSeleccion);
            etiqueta.setForeground(colorTextoSeleccion);
        }
        else {
            etiqueta.setBackground(Color.white);
            etiqueta.setForeground(Color.blue);

        }

        etiqueta.setVerticalAlignment(SwingConstants.TOP);
        if (col == 0) {
            etiqueta.setText(new Integer(row + 1).toString());
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (col == 1) {
            /* sr igualar version produccion
            String text = pago.getMedioPagoActivo().getDesMedioPago();
            String text1 = pagoC.getNumeroTarjeta();
            int medioPago = Integer.parseInt(pago.getMedioPagoActivo().getCodMedioPago());
                  if(medioPago == 220 ){
                        jtable.setRowHeight(row, 35);
                        etiqueta.setText(getLineaBonos(text, text1)); 
                    }   else {
                        etiqueta.setText(text);
                    }*/
            String text = pago.getMedioPagoActivo().getDesMedioPago();
            if(pago instanceof PagoCreditoSK){
                text = pago.getMedioPagoActivo().getDesMedioPago().substring(0,3) + " " + ((PagoCreditoSK) pago).getTarjetaCredito().getNumero();
            }
            etiqueta.setText(text);
            if (jtable.getRowHeight(row) == 20 ) {
                // Si tengo meses de gracia y cuotas gratis
                if (pago.isPromocionAplicada() && pago.getPromociones().size() > 1){
                    jtable.setRowHeight(row, 20+(pago.getPromociones().size()*15));
                } // si tengo cuotas gratis y posfechado
                else if (pago.isPromocionAplicada() && pagoSK!=null && pagoSK.isPosfechado() && !pagoSK.isMesesGraciaAplicado()){
                    jtable.setRowHeight(row, 50);
                } // si tengo sólo una promoción y retención a la fuente
                else if (pago.isPromocionAplicada() && pago.isRetenido()){
                    jtable.setRowHeight(row, 50);
                } // si tengo posfechado y retención a la fuente
                else if (pagoSK!=null && pagoSK.isPosfechado() && pago.isRetenido()){
                    jtable.setRowHeight(row, 50);
                } // si tengo sólo una promoción sin retención ni posfechado
                else if (pagoSK!=null && pagoSK.isPosfechado()){
                    jtable.setRowHeight(row, 35);
                } // si tengo sólo una promoción sin retención ni posfechado
                else if (pago.isPromocionAplicada()){
                    jtable.setRowHeight(row, 35);
                } // si tengo retención a la fuente sin promoción ni posfechado
                else if(pago.isRetenido()){
                    jtable.setRowHeight(row, 35);
                }              
            }
            
            // si tengo promociones de tipo n cuotas
            if (pago.isNCuotasAplicada()){
                etiqueta.setText(getLineaPromocionesNCuotas(text, pago.getPromociones()));
            } // si tenemos más de una promo aplicada 
            else if (pago.isPromocionAplicada() && pago.getPromociones().size() > 1){
                etiqueta.setText(getLineaTriple2(text, pago.getPromociones().get(0).getTextoDetalle(), pago.getPromociones().get(1).getTextoDetalle()));
            } // si tengo cuotas gratis y posfechado
            else if (pago.isPromocionAplicada() && pagoSK!=null && pagoSK.isPosfechado() && !pagoSK.isMesesGraciaAplicado()){
                etiqueta.setText(getLineaTriple2(text, pago.getPromociones().get(0).getTextoDetalle(), pagoSK.getMesesPosfechado() + " MES(ES) DE GRACIA" ));
            } // si tengo sólo una promoción y retención a la fuente
            else if (pago.isPromocionAplicada() && pago.isRetenido()){
                etiqueta.setText(getLineaTriple2(text, pago.getPromociones().get(0).getTextoDetalle(),  "Retención: $ " + pago.getRetencion().getTotal()));
            } // si tengo posfechado y retención a la fuente
            else if (pagoSK!=null && pagoSK.isPosfechado() && pago.isRetenido()){
                etiqueta.setText(getLineaTriple2(text, pagoSK.getMesesPosfechado() + " MES(ES) DE GRACIA" , "Retención: $ " + pago.getRetencion().getTotal()));
            } // si tengo sólo una promoción sin retención ni posfechado
            else if (pagoSK!=null && pagoSK.isPosfechado()){
                etiqueta.setText(getLineaDoble2(text, pagoSK.getMesesPosfechado() + " MES(ES) DE GRACIA"));
            } // si tengo sólo una promoción sin retención ni posfechado
            else if (pago.isPromocionAplicada()){
                etiqueta.setText(getLineaDoble2(text, pago.getPromociones().get(0).getTextoDetalle()));
            } // si tengo retención a la fuente sin promoción ni posfechado
            else if(pago.isRetenido()){
                etiqueta.setText(getLineaDoble2(text, "Retención: $ " + pago.getRetencion().getTotal()));
            } 
            /*
            if (pago.isPromocionAplicada() && pago.getPromociones().size() > 1){
                etiqueta.setText(getLineaTriple2(pago.getMedioPagoActivo().getDesMedioPago(), pago.getPromociones().get(0).getTextoPromocion(), pago.getPromociones().get(1).getTextoPromocion()));
            }
            else if (pago.isPromocionAplicada()){
                etiqueta.setText(getLineaDoble2(pago.getMedioPagoActivo().getDesMedioPago(), pago.getPromociones().get(0).getTextoPromocion()));
            }        */    
        }
        else if (col == 2) {
            String text = "$"+pago.getTotal().toString();
            etiqueta.setText(text);
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else if (col == 3) {
            String text = "-"+pago.getDescuentoASString() + "%";
            etiqueta.setText(text);
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else if (col == 4) {
            String text = "+"+pago.getPorcentajeInteres().setScale(4, RoundingMode.UP) +"%  ";
            etiqueta.setText(text);
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else if (col == 5) {
            String text = "=$"+pago.getUstedPaga().toString();
            etiqueta.setText(text);
            etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        }     
        else if (col == 6) {
            etiqueta.setText(" " + pago.getCuotasRenderer() + " ");
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else if (col == 7) {
            etiqueta.setText(" $ " + pago.getAhorroRenderer().toString() + " ");
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            etiqueta.setForeground(Color.red);            
            etiqueta.setFont(negrita);
        }
        else if (col == 8) {
            etiqueta.setText(((pago.getVuelta().isEmpty()) ? "" : " $ ") + pago.getVuelta() + " ");
            etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
            etiqueta.setForeground(Color.black);
            etiqueta.setFont(negrita);
        }
        else if (col == 9) {
            // Si el pago es de tipo Credito
            if (pagoC != null && pagoC.isValidado() || !(pago.isPagoTarjeta())) {
                etiqueta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/aceptar.png")));
            }
            else if (pagoC != null && pagoC.isFalloValidacion()) {
                etiqueta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/comerzzia/jpos/imagenes/iconos/cancelar.png")));
            }

        }
        else if (col == 10) {
            // Si el pago es de tipo Credito
            if (pagoC != null && pagoC.isValidado()) {
                etiqueta.setForeground(Color.BLACK);
                etiqueta.setText(pagoC.getNumeroAutorizacionTarjeta());
            }
         }
            //else if (col == 11) {
//            String text = pagoC.getNumeroTarjeta();
//            etiqueta.setText(text);
//        }
        else {
            etiqueta.setText("");
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
    /* sr igualar version produccion
    private String getLineaBonos(String linea1, String linea2) {
        return "<html> <span style='font-size:7px; '>" + linea1 + "</span><br style='line-height:1px;' /><span style='font-size:7px;'>" + linea2 + "</span></html>";
    }*/
    
    private String getLineaDoble(String linea1, String linea2) {
        return "<html>" + linea1 + "<br/><span>" + linea2 + "</span></html>";
    }
    private String getLineaDoble2(String linea1, String linea2) {
         return "<html>" + linea1 + "<br/><span style='font-size:7px; color:red;'>" + linea2 + "</span></html>";
    }
    private String getLineaTriple2(String linea1, String linea2, String linea3) {
        return "<html>" + linea1 + "<br/><span style='font-size:7px; color:red;'>" + linea2 + "</span><br/><span style='font-size:7px; color:red;'>" + linea3 + "</span></html>";
    }
    private String getLineaPromocionesNCuotas(String linea1, List<PromocionPagoTicket> promociones){
        String res = "<html>" + linea1;
        for(PromocionPagoTicket promo:promociones){
            res+="<br/><span style='font-size:7px; color:red;'>" + promo.getTextoDetalle() + "</span>";
        }
        res+="</html>";
        return res;
    }
}
