package com.comerzzia.jpos.persistencia.tabla.amortizacion;

import com.comerzzia.jpos.entity.db.TablaAmortizacionDet;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.persistencia.tickets.TicketsDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.log.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class TablaAmortizacionDao  extends MantenimientoDao {

    private static final Logger log = Logger.getMLogger(TicketsDao.class);
    public static List<TablaAmortizacionDet> consultarTablaAmortizacionDet(String uidDocumento) throws TicketException, NoResultException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            Query consulta = em.createQuery("SELECT det FROM TablaAmortizacionDet det inner join  TablaAmortizacionCab cab on det.uidTablaAmortizacion = cab where cab.idDocumento =:uidDocumento");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("uidDocumento", uidDocumento);

            List<TablaAmortizacionDet> ticket = (List<TablaAmortizacionDet>) consulta.getResultList();

            return ticket;
        } catch (NoResultException e) {
            log.info("No existe la factura indicada o la factura está anulada.");
            throw e;
        } catch (Exception ex) {
            log.error("Error consultando  TablaAmortizacionDet: uidDocumento:" + uidDocumento, ex);
            throw new TicketException();
        } finally {
            em.close();
        }
    }

}
