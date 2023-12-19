package com.comerzzia.jpos.servicios.promociones.detalles;

import com.comerzzia.jpos.persistencia.promociones.combos.ComboCantidadPrecioBean;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetallePromocionCombo extends DetallePromocion {

    private String codArticulo;
    private String desArticulo;
    private BigDecimal precioTarifa;
    private BigDecimal precioTarifaConImpuestos;
    private Map<Integer, ComboCantidadPrecioBean> combos;
    private int cantComboMenor;
    private int cantComboMayor;
    private int cantOptima;
    private BigDecimal dtoOptimo;

    public DetallePromocionCombo(){
    }
    
    public void setCombos(List<ComboCantidadPrecioBean> combos){
        this.combos = new HashMap<Integer, ComboCantidadPrecioBean> ();
        if (combos == null || combos.isEmpty()){
            cantComboMenor = 0;
            cantComboMayor = 0;
        }
        else{
            // ordenamos la lista por cantidad ascendentes
            Collections.sort(combos);
            cantComboMenor = combos.get(0).getCantidad();
            cantComboMayor = combos.get(combos.size()-1).getCantidad();
            
            // vamos a construir el mapa de forma que para cada clave cantidad nos devuelva el combo que debería usar. 
            // Tendrá un conjunto de claves desde cantComboMenos hasta cantComboMayor, todos los valores incluidos.
            // Si para la cantidad n no existe un combo, su valor para esta clave deberá ser el combo para la clave m más cercana a n, siendo m<n
            ComboCantidadPrecioBean comboActual = null;
            int cantActual = cantComboMenor;
            for (ComboCantidadPrecioBean combo : combos) {
                while (combo.getCantidad()> cantActual){
                    this.combos.put(cantActual, comboActual);
                    cantActual++;
                }
                this.combos.put(combo.getCantidad(), combo);
                comboActual = combo;
                cantActual++;
            }
        }
    }
    
    public ComboCantidadPrecioBean getComboOptimo(int cantidad){
    	if (cantidad < cantComboMenor){
            return null;
        }
        if (cantidad == cantComboMenor){
            return getCombo(cantidad);
        }
        dtoOptimo = BigDecimal.ZERO;
        cantOptima = 0;
        calculaComboOptimo(cantidad);
        return getCombo(cantOptima);
    }    
    
    private ComboCantidadPrecioBean getCombo(int cantidad){
        // si la cantidad es menor que el menor combo disponible, no hay combo para esta cantidad
        if (cantidad < cantComboMenor){
            return null;
        }
        // si la cantidad es mayor que el mayor combo disponible, devolvemos el mayor combo disponible
        if (cantidad > cantComboMayor){
            return combos.get(cantComboMayor);
        }
        // si la cantidad está registrada en el mapa, devolvemos el combo correspondiente
        return combos.get(cantidad);
    }
    
    private BigDecimal calculaComboOptimo(int cantidad){
    	BigDecimal dtoCandidato = BigDecimal.ZERO;
        int cantCandidata = cantComboMayor;
        if (cantCandidata > cantidad){
            cantCandidata = cantidad;
        }
        while (cantCandidata >= cantComboMenor){
        	dtoCandidato = getCombo(cantCandidata).getPrecioTotal().multiply(new BigDecimal(cantCandidata));
        	dtoCandidato = dtoCandidato.add(calculaComboOptimo(cantidad - cantCandidata));
        	if (dtoCandidato.compareTo(dtoOptimo)>0){
        		dtoOptimo = new BigDecimal(dtoCandidato.doubleValue());
        		cantOptima = cantCandidata;
        	}
        	cantCandidata--;
        }
        return dtoCandidato;
    }
    
    
    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

    public String getDesArticulo() {
        return desArticulo;
    }

    public void setDesArticulo(String desArticulo) {
        this.desArticulo = desArticulo;
    }

    public BigDecimal getPrecioTarifa() {
        return precioTarifa;
    }

    public void setPrecioTarifa(BigDecimal precioTarifa) {
        this.precioTarifa = precioTarifa;
    }

    public BigDecimal getPrecioTarifaConImpuestos() {
        return precioTarifaConImpuestos;
    }

    public void setPrecioTarifaConImpuestos(BigDecimal precioTarifaConImpuestos) {
        this.precioTarifaConImpuestos = precioTarifaConImpuestos;
    }

    public int getCantComboMayor() {
        return cantComboMayor;
    }

    public int getCantComboMenor() {
        return cantComboMenor;
    }

}
