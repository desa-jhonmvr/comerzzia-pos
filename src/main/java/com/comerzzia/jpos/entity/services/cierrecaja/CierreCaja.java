/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.cierrecaja;

import com.comerzzia.jpos.entity.db.MedioPagoCaja;
import java.math.BigDecimal;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author MGRI
 */
public class CierreCaja {

    private BigDecimal descuadreTotal;
    private TreeMap<String, LineaCierreCaja> listaCierreCaja;
    private RecuentoCierreCaja recuentoEntrada;
    private RecuentoCierreCaja recuentoSalida;

    public CierreCaja() {
        descuadreTotal = BigDecimal.ZERO;
        listaCierreCaja = new TreeMap();
        recuentoEntrada = new RecuentoCierreCaja();
        recuentoSalida = new RecuentoCierreCaja();
    }

    public void creaLineasCierrecaja(List lineasMovimientos, List lineasRecuentos) {
        //PRE : listas ordenadas por codigo de movimiento
        LineaCierreCaja lineaCC;
        MedioPagoCaja mp;
        Object[] velem;
        descuadreTotal = BigDecimal.ZERO;

        for (Object elem : lineasMovimientos) {
            velem = (Object[]) elem;
            mp = (MedioPagoCaja) velem[0];
            BigDecimal entrada = ((BigDecimal) velem[1]).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal salida = ((BigDecimal) velem[2]).setScale(2, BigDecimal.ROUND_HALF_UP);

            lineaCC = new LineaCierreCaja(mp, entrada, salida, entrada.add(salida));
            this.listaCierreCaja.put(mp.getCodmedpag(), lineaCC);
            lineaCC.setRecuento(BigDecimal.ZERO);
            lineaCC.setDescuadre(lineaCC.getTotal().negate());
        }

        for (Object elem : lineasRecuentos) {
            velem = (Object[]) elem;
            mp = (MedioPagoCaja) velem[0];
            if (this.listaCierreCaja.containsKey(mp.getCodmedpag())) {
                lineaCC = this.listaCierreCaja.get(mp.getCodmedpag());
            }
            else {
                lineaCC = new LineaCierreCaja(mp, BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
                this.listaCierreCaja.put(mp.getCodmedpag(), lineaCC);
            }
            lineaCC.setRecuento(((BigDecimal) velem[1]).setScale(2, BigDecimal.ROUND_HALF_UP));
            if (lineaCC.getRecuento() == null) {
                lineaCC.setRecuento(BigDecimal.ZERO);
            }
            if (lineaCC.getDescuadre() == null) {
                lineaCC.setDescuadre(BigDecimal.ZERO);
            }
            lineaCC.setDescuadre(lineaCC.getRecuento().subtract(lineaCC.getTotal()));

            this.listaCierreCaja.put(mp.getCodmedpag(), lineaCC);
        }
    }

    public RecuentoCierreCaja getRecuentoEntrada() {
        return recuentoEntrada;
    }

    public void setRecuentoEntrada(RecuentoCierreCaja recuentoEntrada) {
        this.recuentoEntrada = recuentoEntrada;
    }

    public RecuentoCierreCaja getRecuentoSalida() {
        return recuentoSalida;
    }

    public void setRecuentoSalida(RecuentoCierreCaja recuentoSalida) {
        this.recuentoSalida = recuentoSalida;
    }

    public TreeMap<String, LineaCierreCaja> getListaCierreCaja() {
        return listaCierreCaja;
    }

    public void setListaCierreCaja(TreeMap<String, LineaCierreCaja> listaCierreCaja) {
        this.listaCierreCaja = listaCierreCaja;
    }

    public void setRecuentos(List listaRecuentos) {
        this.recuentoEntrada = new RecuentoCierreCaja();
        this.recuentoSalida = new RecuentoCierreCaja();

        // En realidad siempre estará el movimiento actual y estará en el primer lugar del array
        if (listaRecuentos.size() > 0) {

            // Inicializamos los recuentos
            this.recuentoEntrada.setVenta(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
            this.recuentoSalida.setVenta(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
            this.recuentoSalida.setBonos(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
            this.recuentoEntrada.setBonos(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));            
            this.recuentoEntrada.setAbonos(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
            this.recuentoSalida.setAbonos(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
            this.recuentoEntrada.setMovimientos(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
            this.recuentoSalida.setMovimientos(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
                        
            for (Object rec : listaRecuentos) {
                // Leemos los movimientos
                Character caracter = ((Character)((Object[]) rec)[0]);
                if (caracter == 'M') {
                    this.recuentoEntrada.setMovimientos(((BigDecimal) ((Object[]) rec)[1]).setScale(2, BigDecimal.ROUND_HALF_UP));
                    this.recuentoSalida.setMovimientos(((BigDecimal) ((Object[]) rec)[2]).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (caracter == 'T' || caracter == 'A' || caracter == 'D' ) {
                    this.recuentoEntrada.setVenta(recuentoEntrada.getVenta().add((BigDecimal) ((Object[]) rec)[1]).setScale(2, BigDecimal.ROUND_HALF_UP));
                    this.recuentoSalida.setVenta(recuentoSalida.getVenta().add((BigDecimal) ((Object[]) rec)[2]).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (caracter == 'R'){ // Abonos
                    this.recuentoEntrada.setAbonos(recuentoEntrada.getBonos().add((BigDecimal) ((Object[]) rec)[1]).setScale(2, BigDecimal.ROUND_HALF_UP));
                    this.recuentoSalida.setAbonos(recuentoSalida.getBonos().add((BigDecimal) ((Object[]) rec)[2]).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (caracter == 'E'){ // Expedicion de Bonos
                    this.recuentoSalida.setBonos(recuentoSalida.getBonos().add((BigDecimal) ((Object[]) rec)[2]).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }

        this.recuentoEntrada.setSalida(recuentoEntrada.getMovimientos().add(recuentoEntrada.getVenta().add(recuentoEntrada.getAbonos())));
        this.recuentoSalida.setSalida(recuentoSalida.getMovimientos().add(recuentoSalida.getVenta().add(recuentoSalida.getBonos())));
    }

    public void calculaDescuadreTotal() {
        for (LineaCierreCaja elem : listaCierreCaja.values()) {
            this.descuadreTotal = this.getDescuadreTotal().add(elem.getDescuadre());
        }
    }

    public void estableceDescuadreACero() {
        this.descuadreTotal = BigDecimal.ZERO;
    }

    public BigDecimal getDescuadreTotal() {
        return descuadreTotal;
    }
}
