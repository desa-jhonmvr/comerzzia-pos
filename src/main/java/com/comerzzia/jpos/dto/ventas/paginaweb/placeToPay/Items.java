/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class Items implements Serializable{
    
    private static final long serialVersionUID = -597879486934304840L;
    
    private List<ItemPlaceToPay> item;

    public Items() {
    }
    
    

    public List<ItemPlaceToPay> getItem() {
        return item;
    }

    public void setItem(List<ItemPlaceToPay> item) {
        this.item = item;
    }
    
    
    
    
    
}
