/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.stock;

/**
 *
 * @author MGRI
 */
public class StockBean {
    
    private String codAlm;
    private String desAlm;
    private Integer stock;
    private String codArt;
    private String bloqueado;

    
    public StockBean() {
        desAlm = "";        
    }
    public StockBean(int i) {
        codAlm = "00"+i;
        desAlm = "Almacen "+i;
        stock = i*10;
        codArt = "No sale en la tabla"; //sería el artículo buscado en la consulta
    }

    public String getCodAlm() {
        return codAlm;
    }

    public void setCodAlm(String codAlm) {
        this.codAlm = codAlm;
    }

    public String getDesAlm() {
        return desAlm;
    }

    public void setDesAlm(String desAlm) {
        this.desAlm = desAlm;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCodArt() {
        return codArt;
    }

    public void setCodArt(String codArt) {
        this.codArt = codArt;
    }

    public String getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado;
    }
    
    
}
