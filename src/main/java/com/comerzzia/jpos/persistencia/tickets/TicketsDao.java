/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.tickets;

import com.comerzzia.jpos.dto.ResponseDTO;
import com.comerzzia.jpos.dto.ventas.DocumentoDTO;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketId;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.log.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class TicketsDao extends MantenimientoDao {

    private static final Logger log = Logger.getMLogger(TicketsDao.class);

    public static void escribirTicket(EntityManager em, TicketsAlm ticket) throws TicketException {
        try {
            em.persist(ticket);
            em.flush(); // Para que el contador se incremente cuando debe
            ServicioContadoresCaja.incrementarContadorFactura(Connection.getConnection(em));
        } catch (Exception ex) {
            log.error("Error al salvar factura en base de datos: " + ex.getMessage(), ex);
            TicketException e = new TicketException();
            e.setCausa("BBDD");
            throw e;
        }
    }

    public static void modificarLineaOrigen(EntityManager em, LineaTicketOrigen lin) {
        em.merge(lin);
    }

    public static void modificarEnvioYRecogida(EntityManager em, LineaTicketOrigen lin) throws TicketException {

        try {
            String query = "UPDATE " + getNombreElementoEmpresa("D_TICKETS_DET_TBL") + " SET ENVIO_DOMICILIO = '" + lin.getEnvioDomicilio()
                    + "', RECOGIDA_POSTERIOR = '" + lin.getRecogidaPosterior()
                    + "' WHERE uid_ticket= '" + lin.getLineaTicketOrigenPK().getUidTicket()
                    + "' AND id_linea= '" + lin.getLineaTicketOrigenPK().getIdLinea() + "'";
            Query consulta = em.createNativeQuery(query);

            log.debug("modificarEnvioYRecogida() - " + consulta);

            consulta.executeUpdate();
        } catch (Exception ex) {
            log.error("modificarEnvioYRecogida() - Error actualizando línea de ticket", ex);
            throw new TicketException();
        }
    }

    public static byte[] consultarXmlTicket(Long idTicket, String codCaja, String codAlmacen) throws TicketException, NoResultException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        byte[] xml = null;
        try {
            Query consulta = em.createQuery("SELECT t FROM TicketsAlm t WHERE t.idTicket = :idticket AND t.codCaja = :codcaja AND t.codAlm = :tienda AND t.anulado = :anulado");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("tienda", codAlmacen);
            consulta.setParameter("codcaja", codCaja);
            consulta.setParameter("idticket", idTicket);
            consulta.setParameter("anulado", 'N');

            TicketsAlm ticket = (TicketsAlm) consulta.getSingleResult();
            xml = (byte[]) ticket.getTicket();

            return xml;
        } catch (NoResultException e) {
            log.info("No existe la factura indicada o la factura está anulada.");
            throw e;
        } catch (Exception ex) {
            log.error("Error consultando el Xml del ticket: Ticket:" + idTicket + " Caja:" + codCaja + " Alamcen:" + codAlmacen, ex);
            throw new TicketException();
        } finally {
            em.close();
        }

    }
    
    public static TicketsAlm consultarTicket(TicketId idTicket) throws TicketException, NoResultException {
        return consultarTicket(idTicket.getIdTicket(), idTicket.getCodCaja(), idTicket.getCodAlmacen());
    }

    public static TicketsAlm consultarTicket(Long idTicket, String codCaja, String codAlmacen) throws TicketException, NoResultException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            Query consulta = em.createQuery("SELECT t FROM TicketsAlm t WHERE t.idTicket = :idticket AND t.codCaja = :codcaja AND t.codAlm = :tienda");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("tienda", codAlmacen);
            consulta.setParameter("codcaja", codCaja);
            consulta.setParameter("idticket", idTicket);

            TicketsAlm ticket = (TicketsAlm) consulta.getSingleResult();

            return ticket;
        } catch (NoResultException e) {
            log.info("No existe la factura indicada o la factura está anulada.");
            throw e;
        } catch (Exception ex) {
            log.error("Error consultando el Xml del ticket: Ticket:" + idTicket + " Caja:" + codCaja + " Alamcen:" + codAlmacen, ex);
            throw new TicketException();
        } finally {
            em.close();
        }
    }

    public static void escribirLineaTicket(EntityManager em, LineaTicketOrigen linTO) throws TicketException {
        try {
            em.persist(linTO);
        } catch (Exception ex) {
            log.error("Error al salvar linea de ticket en base de datos " + ex.getMessage(), ex);
            TicketException e = new TicketException();
            e.setCausa("BBDD");
            throw e;
        }
    }

    public static List<LineaTicketOrigen> consultarLineasTicket(EntityManager em, String uidTicket) {
        List<LineaTicketOrigen> res = new ArrayList<LineaTicketOrigen>();

        Query consulta = em.createQuery("SELECT l FROM LineaTicketOrigen l LEFT JOIN FETCH l.codart WHERE l.lineaTicketOrigenPK.uidTicket = :uidTicket");
        consulta.setHint("eclipselink.left-join-fetch", "l.codart.codmarca");
        consulta.setParameter("uidTicket", uidTicket);
        consulta.setHint("eclipselink.refresh", "true");

        res = consulta.getResultList();

        return res;
    }

    public static LineaTicketOrigen consultarLineaTicket(EntityManager em, String uidTicket, Long idLinea) throws TicketException {
        try {
            Query consulta = em.createQuery("SELECT l FROM LineaTicketOrigen l LEFT JOIN FETCH l.codart WHERE l.lineaTicketOrigenPK.uidTicket = :uidTicket AND l.lineaTicketOrigenPK.idLinea = :idLinea");
            consulta.setHint("eclipselink.left-join-fetch", "l.codart.codmarca");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("uidTicket", uidTicket);
            consulta.setParameter("idLinea", idLinea);

            return (LineaTicketOrigen) consulta.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            log.error("Error consultando la línea de ticket (uidTicket, idLinea): (" + uidTicket + "," + idLinea + ")", ex);
            throw new TicketException();
        }
    }

    public static void modificarTicket(EntityManager em, TicketsAlm ticket) {
        em.merge(ticket);
    }

    public static TicketsAlm consultarTicket(String uidTicket) throws TicketException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            return consultarTicket(em, uidTicket);
        } finally {
            em.close();
        }
    }

    public static TicketsAlm consultarTicket(EntityManager em, String uidTicket) throws TicketException {
        try {
            Query consulta = em.createQuery("SELECT t FROM TicketsAlm t WHERE t.uidTicket = :uidTicket");
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("uidTicket", uidTicket);

            TicketsAlm ticket = (TicketsAlm) consulta.getSingleResult();

            return ticket;
        } catch (NoResultException e) {
            log.info("No existe la factura indicada");
            throw e;
        } catch (Exception ex) {
            log.error("Error consultando el ticket con uid: " + uidTicket, ex);
            throw new TicketException();
        }
    }

    public static TicketsAlm consultarTicketuid(EntityManager em, String uidTicket, String caja, String local, Date fechaInici, Date fechaFi) throws TicketException, ParseException {

        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy");
//         fechaInici = d.parse("22/06/2017");
        String convertido = fechaHora.format(fechaInici);
        String convertido2 = fechaHora.format(fechaFi);

        Date fechaInicio = d.parse(convertido);
        Date fechaFin = d.parse(convertido2);
        TicketsAlm ticket = null;
        try {
            Query consulta = em.createQuery("SELECT t FROM TicketsAlm t WHERE t.uidTicket =:uidTicket and t.codCaja=:cajas and t.codAlm=:locals and t.fecha>= :fechaIn and t.fecha< :fachaFi");

            consulta.setHint("eclipselink.refresh", "true");
            consulta.setParameter("uidTicket", uidTicket);
            consulta.setParameter("cajas", caja);
            consulta.setParameter("locals", local);
            consulta.setParameter("fechaIn", fechaInicio);
            consulta.setParameter("fachaFi", fechaFin);
            ticket = (TicketsAlm) consulta.getSingleResult();

        } catch (NoResultException e) {
            log.info("No existe la factura indicada");
//            throw e;
        }

//        catch (Exception ex) {
//            log.error("Error consultando el ticket con uid: "+uidTicket, ex);
//            throw new TicketException();
//        }
        return ticket;
    }

    //Opcion uno 
    public static List<LineaTicketOrigen> consultarTicketPendientesEntrega(EntityManager em, char estadoRecogida) {
        List<LineaTicketOrigen> res = new ArrayList<LineaTicketOrigen>();
        Query consulta = em.createQuery("SELECT t FROM LineaTicketOrigen t WHERE t.recogidaPosterior = :estadoRecogida  AND t.lineaTicketOrigenPK.uidTicket  NOT LIKE '%FACMIG%'");
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("estadoRecogida", estadoRecogida);
        ///Prueba Ricardo////
//            consulta.setParameter("estadoDomicilio", estadoRecogida);
        res = consulta.getResultList();
        return res;
    }

    //opcion dos
    public static List<LineaTicketOrigen> consultarTicketPendientesEnvio(EntityManager em, char estadoRecogida) {
        List<LineaTicketOrigen> res = new ArrayList<LineaTicketOrigen>();
        Query consulta = em.createQuery("SELECT t FROM LineaTicketOrigen t WHERE  t.envioDomicilio= :estadoDomicilio AND t.lineaTicketOrigenPK.uidTicket  NOT LIKE '%FACMIG%'");
        consulta.setHint("eclipselink.refresh", "true");
        ///Prueba Ricardo////
        consulta.setParameter("estadoDomicilio", estadoRecogida);
        res = consulta.getResultList();
        return res;
    }
//        public static List<LineaTicketOrigen> consultarTicketPendientesEntrega(EntityManager em,String estadoRecogida,String codigoCaja,String codigoAlm,String date){
//          List<LineaTicketOrigen> res = new ArrayList<LineaTicketOrigen>();
//        Query consulta = em.createQuery("SELECT t FROM LineaTicketOrigen t WHERE t.RECOGIDA_POSTERIOR = :estadoRecogida AND t.lineaTicketOrigenPK.uidTicket  NOT LIKE '%FACMIG%' and t.ticket.codCaja=:codigoCaja and ticket.codAlm:=codigoAlm and TRUNC(FECHA) = :date");                  
//            consulta.setHint("eclipselink.refresh", "true");
//            consulta.setParameter("RECOGIDA_POSTERIOR", estadoRecogida);
//            consulta.setParameter("ticket.codCaja", codigoCaja);
//            consulta.setParameter("ticket.codAlm", codigoAlm);
//            consulta.setParameter("ticket.fecha", date);
//            res = consulta.getResultList();
//            return res; 
//    }
    
    public static DocumentoDTO consultarTicketOtroLocal(String numeroFactura) throws Exception  {
        DocumentoDTO documento = null;
        ResponseDTO response = null;
        try {
            URL url = new URL(Variables.getVariable(Variables.WEBSERVICE_NOTACREDITO_URL) + "getFactura/" + numeroFactura);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = "";
            while ((output = br.readLine()) != null) {
                response = JsonUtil.jsonToObject(output, ResponseDTO.class);
            }
            if (response.getExito()) {
                String jsonObject = JsonUtil.objectToJson(response.getObjetoRespuesta());
                documento = JsonUtil.jsonToObject(jsonObject, DocumentoDTO.class);
            } else {
                conn.disconnect();
                throw new TicketException(response.getDescripcion());
            }
            conn.disconnect();
        } catch (ConnectException ex) {
            throw new Exception("Error al conectar con otro local");
        } catch (MalformedURLException e) {
            throw new Exception("Error al conectar con otro local");
        } catch (IOException e) {
            throw new Exception("Error al conectar con otro local");
        } catch (TicketException e) {
            throw new Exception(e.getMessage());
        }
        return documento;
    }
    
    
}
