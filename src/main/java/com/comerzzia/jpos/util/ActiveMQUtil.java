/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util;

import com.comerzzia.jpos.entity.db.ReprocesoGeneral;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.util.Date;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Simbania
 */
public class ActiveMQUtil {

    private static final Logger LOG = Logger.getMLogger(ActiveMQUtil.class);

    /**
     * @author Gabriel Simbania
     * @param url
     * @param mensaje
     * @param queue
     * @param proceso
     * @param uid
     */
    public void encolarMensaje(String url, String mensaje, String queue, String proceso, String uid) {
        if (!Sesion.getDatosConfiguracion().isModoDesarrollo()) {
            ActiveMQConnectionFactory connectionFactory = null;
            Connection connection = null;
            Session session = null;
            EntityManagerFactory emf = Sesion.getEmf();
            try {
                connectionFactory = new ActiveMQConnectionFactory(url);
                connection = connectionFactory.createConnection();
                connection.start();

                session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
                final Destination destination = session.createQueue(queue);

                final MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);

                final TextMessage textMessage = session.createTextMessage(mensaje);
                producer.send(textMessage);

            } catch (Throwable e) {
                LOG.error("Error en el encolamiento " + queue + " - " + e.getMessage());

                EntityManager em = emf.createEntityManager();
                try {
                    em.getTransaction().begin();
                    ReprocesoGeneral reprocesoGeneral = new ReprocesoGeneral(uid, proceso, new Date(), mensaje, 1L);
                    em.persist(reprocesoGeneral);
                    em.getTransaction().commit();
                } catch (Exception ex) {
                    em.getTransaction().rollback();
                    LOG.error(ex);
                } finally {
                    em.close();
                }

            } finally {
                if (session != null) {
                    try {
                        session.commit();
                        session.close();
                    } catch (JMSException ex) {
                        LOG.error("Error en el encolamiento " + queue + " - " + ex.getMessage());
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException ex) {
                        LOG.error("Error en el encolamiento " + queue + " - " + ex);
                    }
                }

            }
        }
    }

}
