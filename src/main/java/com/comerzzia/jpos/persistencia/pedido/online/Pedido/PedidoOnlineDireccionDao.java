/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.pedido.online.Pedido;

import com.comerzzia.jpos.entity.db.PedidoOnlineDireccionTbl;
import javax.persistence.EntityManager;

/**
 *
 * @author Gabriel Simbania
 */
public class PedidoOnlineDireccionDao {
    
    public static void crear(PedidoOnlineDireccionTbl pedidoDireccion, EntityManager em) throws Exception {
        em.persist(pedidoDireccion);
    }
    
}
