/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb.paymentez;

import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class DebitRequestDTO implements Serializable{
    
    private static final long serialVersionUID = -871555377312438979L;
    
    private UserDTO user;
    private OrderDTO order;
    private CardDTO card;

    public DebitRequestDTO() {
        
    }
    
    public DebitRequestDTO(UserDTO user, OrderDTO order, CardDTO card) {
        this.user = user;
        this.order = order;
        this.card = card;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
    
    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }
    
    
}
