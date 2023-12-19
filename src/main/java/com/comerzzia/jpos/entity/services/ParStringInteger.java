/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services;

/**
 *
 * @author amos
 */
public class ParStringInteger implements Comparable<ParStringInteger> {
    private String valorString;
    private Integer valorInt;

    public ParStringInteger(String valorString, Integer valorInt) {
        this.valorString = valorString;
        this.valorInt = valorInt;      
    }

    public ParStringInteger() {
    }

    public Integer getValorInt() {
        return valorInt;
    }

    public void setValorInt(Integer valorInt) {
        this.valorInt = valorInt;
    }

    public String getValorString() {
        return valorString;
    }

    public void setValorString(String valorString) {
        this.valorString = valorString;
    }

    
    @Override
    public int compareTo(ParStringInteger t) {
        return t.getValorInt().compareTo(valorInt);
    }


    
}
