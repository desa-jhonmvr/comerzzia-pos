/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.pedido.online.Pedido;

import com.comerzzia.jpos.entity.db.PedidoOnlineDetalleTbl;
import javax.persistence.EntityManager;

/**
 *
 * @author Gabriel Simbania
 */
public class PedidoOnlineDetalleDao {

    public static void crear(PedidoOnlineDetalleTbl pedidoDetalle, EntityManager em) throws Exception {
        em.persist(pedidoDetalle);
    }

}
