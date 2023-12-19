/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.articulos;

import java.math.BigDecimal;

/**
 *
 * @author AMS
 */
public class ArticuloPromociones implements Comparable<ArticuloPromociones> {
    
   private String codArticulo;
   private String desArticulo;
   private String desMarca;
   private BigDecimal descuento;
   private Integer limiteProducto;
   private Integer limiteLocalProducto;
   private Boolean check;

    public ArticuloPromociones() {
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

    public String getDesMarca() {
        return desMarca;
    }

    public void setDesMarca(String desMarca) {
        this.desMarca = desMarca;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public Integer getLimiteProducto() {
        return limiteProducto;
    }

    public void setLimiteProducto(Integer limiteProducto) {
        this.limiteProducto = limiteProducto;
    }

    @Override
    public int compareTo(ArticuloPromociones t) {
        return t.descuento.compareTo(descuento);
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
   
    public boolean isCheck(){
        return check;
    }

    public Integer getLimiteLocalProducto() {
        return limiteLocalProducto;
    }

    public void setLimiteLocalProducto(Integer limiteLocalProducto) {
        this.limiteLocalProducto = limiteLocalProducto;
    }
   
}
