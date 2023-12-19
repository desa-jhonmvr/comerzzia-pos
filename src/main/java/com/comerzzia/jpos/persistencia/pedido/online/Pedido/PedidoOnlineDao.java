/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.pedido.online.Pedido;

import com.comerzzia.jpos.entity.db.PedidoOnlineTbl;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Gabriel Simbania
 */
public class PedidoOnlineDao {

    public static void crear(PedidoOnlineTbl pedido, EntityManager em) throws Exception {
        em.persist(pedido);
    }

    public static PedidoOnlineTbl consultarPedidoOnline(EntityManager em, String idPedido) throws Exception {

        PedidoOnlineTbl pedidoOnline = null;
        try {

            Query consulta = em.createQuery("SELECT a FROM PedidoOnlineTbl a WHERE a.idPedido= :idPedido").setHint("toplink.refresh", "true");
            consulta.setParameter("idPedido", idPedido);
            pedidoOnline = (PedidoOnlineTbl) consulta.getSingleResult();
        } catch (NoResultException ex) {
            pedidoOnline = null;
        }
        return pedidoOnline;
    }

}
