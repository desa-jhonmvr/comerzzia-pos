/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones;

import com.comerzzia.jpos.servicios.pagos.Pago;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author amos
 */
public class PagosReservacion {

    private List<Pago> pagos;
    private BigDecimal ustedPaga;
    private BigDecimal saldo;
    private BigDecimal pagado;
    private BigDecimal total;
    private Pago pagoEfectivo;
    private BigDecimal totalAhorro;

    public PagosReservacion(BigDecimal total) {
        pagos = new ArrayList<Pago>();
        this.total = total;
        resetearTotales();
    }

    private void setPagoEfectivo(Pago pago) {
        if (pagoEfectivo != null) {
            pagos.remove(pagoEfectivo);
        }
        pagoEfectivo = pago;
        pagos.add(pagoEfectivo);
    }

    public BigDecimal getMovimientoEfectivo() {
        BigDecimal res= BigDecimal.ZERO;
        if (this.pagoEfectivo !=null)
            if (this.pagoEfectivo.getUstedPaga()!=null)
                res= this.pagoEfectivo.getUstedPaga();
        return res;
    }

    protected void addPago(Pago pag) {
        if (pag.isPagoEfectivo()) {
            setPagoEfectivo(pag);
        }
        else {
            pagos.add(pag);
        }
        recalculaTotales();
    }
    
    public int getNumPagos(){
        return pagos.size();
    }
    
    public void recalculaTotales() {
        resetearTotales();
        for (Pago pago : pagos) {
            ustedPaga = ustedPaga.add(pago.getUstedPaga());
            saldo = saldo.subtract(pago.getTotal());
            totalAhorro = totalAhorro.add(pago.getAhorro());
        }
        pagado = total.subtract(saldo);
    }

    private void resetearTotales() {
        ustedPaga = BigDecimal.ZERO;
        pagado = BigDecimal.ZERO;
        totalAhorro = BigDecimal.ZERO;
        saldo = total;
    }

    public BigDecimal getPagado() {
        return pagado;
    }

    public void setPagado(BigDecimal pagado) {
        this.pagado = pagado;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getUstedPaga() {
        return ustedPaga;
    }

    public void setUstedPaga(BigDecimal ustedPaga) {
        this.ustedPaga = ustedPaga;
    }

    protected void eliminarPago(int i) {
        try {
            Pago p = pagos.get(i);
            if (p.isPagoEfectivo()) {
                pagoEfectivo = null;
            }
            pagos.remove(i);
            recalculaTotales();

        }
        catch (ArrayIndexOutOfBoundsException aex) {
            //No existe el pago que se intena borrar
        }
        catch (Exception e) {
            Logger.getLogger(this.getClass()).error("Excepción no controlada al eliminar pago", e);
        }
    }

    protected int getIndexUltimaLinea() {
        return pagos.size() - 1;
    }

    public BigDecimal getTotalAhorro() {
        return totalAhorro;
    }
    
    
    
}
