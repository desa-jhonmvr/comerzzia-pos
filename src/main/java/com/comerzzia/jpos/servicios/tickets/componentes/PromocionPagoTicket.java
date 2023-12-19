/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import com.comerzzia.jpos.persistencia.promociones.TipoPromocionBean;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class PromocionPagoTicket {

    private Long idPromocion;
    private String textoPromocion;
    private String textoDetalle;
    private String printPromocion;
    private Integer numCuotasPromocion;
    private BigDecimal importeBasePromocion;
    private BigDecimal importeAhorro;
    private BigDecimal importeCuota;
    private TipoPromocionBean tipoPromocion;
    
    public PromocionPagoTicket(Promocion promocion){
        idPromocion = promocion.getIdPromocion();
        textoPromocion = promocion.getDescripcionImpresion();
        importeAhorro = BigDecimal.ZERO;
        tipoPromocion = promocion.getTipoPromocion();
    }

    public String getDesTipoPromocion() {
        return tipoPromocion.getDesTipoPromocion();
    }

    public Long getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Long idPromocion) {
        this.idPromocion = idPromocion;
    }

    public Long getIdTipoPromocion() {
        return tipoPromocion.getIdTipoPromocion();
    }

    public BigDecimal getImporteAhorro() {
        return importeAhorro;
    }

    public void setImporteAhorro(BigDecimal importeAhorro) {
        this.importeAhorro = importeAhorro;
    }

    public String getTextoPromocion() {
        return textoPromocion;
    }

    public void setTextoPromocion(String textoPromocion) {
        this.textoPromocion = textoPromocion;
    }

    public String getTextoDetalle() {
        return textoDetalle;
    }

    public void setTextoDetalleMesesGracia(int mesesGracia) {
        String mes = " MES";
        if (mesesGracia > 1){
            mes = " MESES";
        }
        this.textoDetalle = mesesGracia + mes + " DE GRACIA";
    }

    public void setTextoDetalleCuotasGratis(int numCuotas, BigDecimal importeAhorro) {
        String cuota = " CUOTA";
        if (numCuotas > 1){
            cuota = " CUOTAS";
        }
        this.textoDetalle = numCuotas + cuota + " GRATIS = $ " + importeAhorro;
    }

    public String getPrintPromocion() {
        return printPromocion;
    }

    public void setPrintPromocion(String printPromocion) {
        this.printPromocion = printPromocion;
    }

    public BigDecimal getImporteBasePromocion() {
        return importeBasePromocion;
    }

    public void setImporteBasePromocion(BigDecimal importeBasePromocion) {
        this.importeBasePromocion = importeBasePromocion;
    }

    public BigDecimal getImporteCuota() {
        return importeCuota;
    }

    public void setImporteCuota(BigDecimal importeCuota) {
        this.importeCuota = importeCuota;
    }

    public Integer getNumCuotasPromocion() {
        return numCuotasPromocion;
    }

    public void setNumCuotasPromocion(Integer numCuotasPromocion) {
        this.numCuotasPromocion = numCuotasPromocion;
    }

    public TipoPromocionBean getTipoPromocion() {
        return tipoPromocion;
    }
    
    
    
}