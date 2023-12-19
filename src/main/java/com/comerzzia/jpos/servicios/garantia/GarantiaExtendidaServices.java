/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.garantia;

import com.comerzzia.jpos.entity.db.GarantiaExtendidaReg;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.persistencia.garantia.GarantiasDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.enums.EnumEstado;
import es.mpsistemas.util.log.Logger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author amos
 */
public class GarantiaExtendidaServices {

    private static final Logger log = Logger.getMLogger(GarantiaExtendidaServices.class);

    /**
     *
     * @param em
     * @param ticket
     */
    public static void salvarItemsGarantia(EntityManager em, TicketS ticket) {
        LineasTicket lineas = ticket.getLineas();

        for (LineaTicket linea : lineas.getLineas()) {
            GarantiaReferencia referencia = linea.getReferenciaGarantia();
            if (referencia != null) {
                LineaTicket lineaReferencia = referencia.getLineaOrigen();
                log.debug("salvarItemsGarantia() - Registrando artículo de garantía extendida...");

                GarantiaExtendidaReg garantiaReg = null;
                if (referencia.getTicketOrigen() == null) {
                    garantiaReg = new GarantiaExtendidaReg(ticket.getUid_ticket(), linea.getIdlinea().shortValue(), ticket.getUid_ticket());
                    garantiaReg.setCodalm(ticket.getTienda());
                    garantiaReg.setCodcaja(ticket.getCodcaja());
                    garantiaReg.setIdTicket(ticket.getId_ticket());
                    garantiaReg.setFechaHora(ticket.getFecha().getDate());
                    garantiaReg.setCodcli(ticket.getCliente().getCodcli());
                    garantiaReg.setIdUsuario(ticket.getCajero().getIdUsuario());
                    //Cambio requerimiento Garantia Rd
                    garantiaReg.setCodvendedor(linea.getCodEmpleado());
                } else {
//                    Integer idLinea = GarantiasDao.consultarMaxIdLineaGarantia(em, referencia.getTicketOrigen().getUid_ticket()) + 1;
                    Integer idLinea = referencia.getLineaOrigen().getIdlinea();
                    garantiaReg = new GarantiaExtendidaReg(referencia.getTicketOrigen().getUid_ticket(), idLinea.shortValue(), ticket.getUid_ticket());
                    garantiaReg.setCodalm(referencia.getTicketOrigen().getTienda());
                    garantiaReg.setCodcaja(referencia.getTicketOrigen().getCodcaja());
                    garantiaReg.setIdTicket(referencia.getTicketOrigen().getId_ticket());
                    garantiaReg.setFechaHora(referencia.getTicketOrigen().getFecha().getDate());
                    garantiaReg.setCodcli(referencia.getTicketOrigen().getCliente().getCodcli());
                    garantiaReg.setIdUsuario(referencia.getTicketOrigen().getCajero().getIdUsuario());
                }
                garantiaReg.setCodItem(lineaReferencia.getArticulo().getIdItem());
                garantiaReg.setCodmarca(lineaReferencia.getArticulo().getCodmarca().getCodmarca());
                garantiaReg.setCodmodelo(lineaReferencia.getArticulo().getModelo());
                garantiaReg.setCodart(lineaReferencia.getArticulo().getCodart());
                garantiaReg.setCodbarras(lineaReferencia.getCodigoBarras());
                garantiaReg.setImporteOrigen(lineaReferencia.getImporteTotal());
                garantiaReg.setImporteFinal(lineaReferencia.getImporteTotalFinalPagado());
                garantiaReg.setObservaciones(ticket.getObservaciones());
                garantiaReg.setEstado(EnumEstado.ACTIVO);
                if (lineaReferencia.getArticulo().getGarantiaOriginal() == null) {
                    garantiaReg.setGarantia(new Integer(0).shortValue());
                } else {
                    garantiaReg.setGarantia(lineaReferencia.getArticulo().getGarantiaOriginal().shortValue());
                }
                garantiaReg.setImporteGarantia(linea.getImporteTotalFinalPagado());
                garantiaReg.setCantidad(linea.getCantidad());

                GarantiasDao.salvar(em, garantiaReg);

            }
        }
    }

    /**
     *
     * @param uidTicket
     * @param codArticulo
     * @return
     * @throws GarantiaException
     */
    public static List<GarantiaExtendidaReg> consultarArticuloGarantia(String uidTicket, String codArticulo) throws GarantiaException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            return GarantiasDao.consultarGarantiaAnterior(em, uidTicket, codArticulo);
        } catch (Exception e) {
            log.error("consultarArticuloGarantia() - Se ha producido un error intentando consultar garantías vendidas anteriormente.", e);
            throw new GarantiaException("Se ha producido un error intentando consultar garantías vendidas anteriormente.", e);
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param uidTicket
     * @param codArticulo
     * @return
     * @throws GarantiaException
     */
    public static Integer getNumeroGarantiasVendidas(String uidTicket, String codArticulo) throws GarantiaException {
        List<GarantiaExtendidaReg> garantias = consultarArticuloGarantia(uidTicket, codArticulo);
        if (garantias == null) {
            return 0;
        }
        int total = 0;
        for (GarantiaExtendidaReg garantia : garantias) {
            total += garantia.getCantidad();
        }
        return total;
    }

    /**
     * Consultar ls GarantiaExtendidaReg por los diferentes criterios
     *
     * @author Gabriel Simbania
     * @param uidTicket
     * @param uidTicketReferencia
     * @param codArticulo
     * @return
     * @throws GarantiaException
     */
    public static GarantiaExtendidaReg consultarGarantiaByCriterio(String uidTicket, String uidTicketReferencia, String codArticulo, int lineaId) throws GarantiaException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            return GarantiasDao.consultarGarantiaByCriterio(em, uidTicket, uidTicketReferencia, codArticulo, lineaId);
        } catch (Exception e) {
            log.error("consultarArticuloGarantia() - Se ha producido un error intentando consultar garantías vendidas anteriormente.", e);
            throw new GarantiaException("Se ha producido un error intentando consultar garantías vendidas anteriormente.", e);
        } finally {
            em.close();
        }
    }

}
