/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.login;

import es.mpsistemas.util.log.Logger;
import com.comerzzia.jpos.entity.db.RecuentoCajaDet;
import com.comerzzia.jpos.entity.db.RecuentoCajaDetPK;
import com.comerzzia.jpos.persistencia.cajas.GestionDeCajasDao;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class RecuentoSesion {

    private List<RecuentoCajaDet> recuento;
    private BigDecimal totalRecuento;
    private static Logger log = Logger.getMLogger(RecuentoSesion.class);

    RecuentoSesion(List<RecuentoCajaDet> consultaRecuento) {
        recuento = consultaRecuento;
        totalRecuento = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        for (RecuentoCajaDet linea : consultaRecuento) {
            totalRecuento = totalRecuento.add(linea.getValor());
        }
    }

    RecuentoSesion() {
        recuento = new LinkedList();
        totalRecuento = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * añade una nueva linea de recuento a la sesion
     * @param rec RecuentoCajaDet a añadir
     */
    public void crearLineaRecuento(RecuentoCajaDet rec) {
        rec.setCajaCajero(Sesion.getCajaActual().getCajaParcialActual());
        rec.setUidDiarioCaja(Sesion.getCajaActual().getCajaActual());
        this.recuento.add(rec);
        this.totalRecuento = this.totalRecuento.add(rec.getValor());
    }

    public void guardarRecuento(String uidCajeroCaja) throws Exception {

        int linea = 0;//GestionDeCajasDao.consultaSiguenteNumeroLineaRecuento(uidDiarioCaja);
        Object[] recuentoarray = this.recuento.toArray();
        Object[] recuentoarraycopia = recuentoarray.clone();
        try {

            for (RecuentoCajaDet rec : recuento) {
                //if (rec.getRecuentoCajaDetPK() == null) {
                rec.setRecuentoCajaDetPK(new RecuentoCajaDetPK(uidCajeroCaja, linea));
                linea++;
                //}
            }
            GestionDeCajasDao.guardarRecuento(recuento, uidCajeroCaja);
        }
        catch (Exception e) {
            log.error("Error al guardar el recuento");
            recuento = new LinkedList();
            for (Object rec : recuentoarraycopia) {
                this.recuento.add((RecuentoCajaDet) rec);
            }
            throw new Exception("Error al guardar el recuento");
        }

    }

    public List<RecuentoCajaDet> getRecuento() {
        return recuento;
    }

    public void setRecuento(List<RecuentoCajaDet> recuento) {
        this.recuento = recuento;
    }

    public RecuentoCajaDet getNuevaLineaRecuento() {

        return (new RecuentoCajaDet());
    }

    public BigDecimal getTotalRecuento() {
        return totalRecuento;
    }

    public void setTotalRecuento(BigDecimal totalRecuento) {
        this.totalRecuento = totalRecuento;
    }

    public void eliminarLineaRecuento() {
        if (this.recuento.size() > 0) {
            eliminarLineaRecuento(this.recuento.size() - 1);
        }
    }

    public void eliminarLineaRecuento(int lineaSelecionada) {
        RecuentoCajaDet lineaSeleccionadaRecuento = this.recuento.get(lineaSelecionada);
        this.totalRecuento = this.totalRecuento.subtract(lineaSeleccionadaRecuento.getValor());
        if(this.totalRecuento.intValue()<0){
            this.totalRecuento = new BigDecimal(0);
        }
        this.recuento.remove(lineaSelecionada);
    }

    public void editarLineaRecuento(int lineaSelecionada, BigDecimal valor) {
        this.recuento.get(lineaSelecionada).setValor(valor);
    }
}
