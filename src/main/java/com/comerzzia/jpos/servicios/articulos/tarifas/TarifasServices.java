/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.articulos.tarifas;

import com.comerzzia.jpos.entity.db.TarifaId;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.articulos.tarifas.TarifasDao;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author MGRI
 */
public class TarifasServices {

    static TarifaId tarifaCab;
    static TarifasDao td = new TarifasDao();
    protected static Logger log = Logger.getMLogger(TarifasServices.class);

    /**
     * Constructor
     */
    public void TarifasServices() throws Exception {
        if (tarifaCab == null) {
            iniciaTarifaCab();
        }
    }

    /**
     * Obtiene la tarifa asociada a un artículo
     * @param codArticulo
     * @return 
     */
    public Tarifas getTarifaArticulo(String codArticulo) 
            throws TarifaArticuloNotFoundException, TarifaException {
        try{
            if (tarifaCab == null) {
                iniciaTarifaCab();
            }
            Tarifas tarifaArticulo = td.getTarifaArticulo(tarifaCab.getCodtar(), codArticulo);
            if (tarifaArticulo == null) {
                throw new TarifaArticuloNotFoundException("El artículo indicado no se encuentra tarificado.");
            }
            tarifaArticulo.setPrecioReal(tarifaArticulo.getPrecioVenta());
            return tarifaArticulo;
        }
        catch (TarifaArticuloNotFoundException e){
            throw e;
        }
        catch (Exception e){
            log.error("getTarifaArticulo() - Error consultando tarifa de artículo: " + e.getMessage(), e);
            throw new TarifaException("Error consultando tarifa de artículo: " + e.getMessage(), e);
            
        }
    }

    private void iniciaTarifaCab() throws Exception {
        TarifasServices.tarifaCab = td.getTarifa("GENERAL");
        if (tarifaCab == null) {
            log.error("iniciaTarifaCab() - Error consultando tarifa. La tarifa GENERAL no está registrada en el sistema.");
            throw new TarifaException("ERROR GRAVE: La tarifa GENERAL no está registrada en el Sistema.");
        }
    }
}
