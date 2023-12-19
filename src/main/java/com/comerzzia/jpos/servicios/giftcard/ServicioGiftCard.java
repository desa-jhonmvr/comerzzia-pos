package com.comerzzia.jpos.servicios.giftcard;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.persistencia.cajas.MovimientoCajaException;
import com.comerzzia.jpos.persistencia.giftcard.GiftCardBean;
import com.comerzzia.jpos.persistencia.giftcard.GiftCardDao;
import com.comerzzia.jpos.persistencia.giftcard.logs.LogGiftCardBean;
import com.comerzzia.jpos.persistencia.giftcard.logs.LogGiftCardConsumoBean;
import com.comerzzia.jpos.persistencia.giftcard.logs.LogGiftCardConsumoDao;
import com.comerzzia.jpos.persistencia.giftcard.logs.LogGiftCardDao;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.bonos.BonosServices;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.reservaciones.ReservasException;
import com.comerzzia.jpos.servicios.tickets.ReferenciaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PagosTicket;
import com.comerzzia.jpos.servicios.tickets.xml.TicketXMLServices;
import com.comerzzia.jpos.servicios.tramas.TramaTarjetaGiftCard;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.util.base.KeyConstraintViolationException;
import java.sql.SQLException;


import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ServicioGiftCard {

    protected static Logger log = Logger.getMLogger(ServicioGiftCard.class);

    public static GiftCardBean consultar(String idGiftCard) throws GiftCardException, GiftCardNotFoundException, GiftCardAnuladaException {
        Connection conn = new Connection();
        try {
            log.debug("consultar() - Consultando datos de la giftcard: " + idGiftCard);
            conn.abrirConexion(Database.getConnection());
            GiftCardBean giftcard = GiftCardDao.consultar(conn, idGiftCard);

            if (giftcard == null) {
                String msg = "No se ha encontrado la giftcard con identificador: " + idGiftCard;
                log.info("consultar() - " + msg);
                throw new GiftCardNotFoundException(msg);
            }
            if (giftcard.isAnulado()) {
                String msg = "La GiftCard con identificador: " + idGiftCard + " está anulada";
                log.info("consultar() - " + msg);
                throw new GiftCardAnuladaException(msg);                
            }
            
            return giftcard;
        }
        catch (SQLException e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar datos de una giftcard: " + e.getMessage();
            throw new GiftCardException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
    }

    public static LogGiftCardBean crear(TramaTarjetaGiftCard trama, BigDecimal saldo, String autorizador, PagosTicket pagos, Cliente cliente) throws GiftCardException, GiftCardConstraintViolationException {

        Connection conn = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            log.debug("crear() - Creando nueva giftCard " + trama.getIdGiftCard());
            
            // Creamos el GiftCard
            GiftCardBean giftCard = new GiftCardBean();
            giftCard.setCodMedioPago(trama.getCodMedioPago());
            giftCard.setIdGiftCard(trama.getIdGiftCard());
            giftCard.setSaldo(saldo);
            giftCard.setFechaCargaInicial(new Fecha());
            giftCard.setProcesado(false);

            conn.abrirConexion(Database.getConnection());
            conn.iniciaTransaccion();

            // Antes de insertar la GiftCard, la borramos por si hubiera una GiftCard anulada
            GiftCardDao.delete(conn, giftCard);
            // insertamos con JDBC clasico el giftcard
            GiftCardDao.insert(conn, giftCard);

            LogGiftCardBean logGiftCard = registrarLogsPagos(conn, LogGiftCardBean.OPERACION_CARGA_INICIAL,cliente.getCodcli(), autorizador, giftCard, pagos);
            
            em.getTransaction().begin();
            
            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaGiftCard(trama.getIdGiftCard());
            TicketService.procesarMediosPagos(em, pagos.getPagos(), referencia, "GIF", giftCard.getIdGiftCard());
                         
            em.getTransaction().commit();
            
            conn.commit();
            conn.finalizaTransaccion();
            BonosServices.crearBonosPagos(pagos, giftCard.getIdGiftCard(), BonosServices.LLAMADO_DESDE_GIFTCARD, cliente);
            
            return logGiftCard;
        }
        catch (KeyConstraintViolationException e) {
            conn.deshacerTransaccion();
            log.info("crear() - No se ha podido registrar la giftcard: " + e.getMessage());
            throw new GiftCardConstraintViolationException("Error creando nueva giftcard: " + e.getDefaultMessage());
        }
        catch (MovimientoCajaException e){
            conn.deshacerTransaccion();
            em.getTransaction().rollback();
            return null;
        }
        catch (ReservasException ex){
            conn.deshacerTransaccion();
            em.getTransaction().rollback();
            throw new GiftCardException(ex.getMessage(),ex); 
        }
        catch (SQLException e) {
            conn.deshacerTransaccion();
            em.getTransaction().rollback();
            
            String msg = "Error creando nueva giftcard: " + e.getMessage();
            log.error("crear() - " + msg);
            throw new GiftCardException(msg, e);
        }
        catch (Exception e) {
            conn.deshacerTransaccion();
            em.getTransaction().rollback();
            String msg = "Error creando nueva giftcard: " + e.getMessage();
            log.error("crear() - " + msg);
            throw new GiftCardException(msg, e);
        }
        finally {
            conn.cerrarConexion();
            em.close();
        }
        
    }

    private static LogGiftCardBean registrarLogsPagos(Connection conn, String operacion, String codCliente, String autorizador, GiftCardBean giftCard, PagosTicket pagos) throws SQLException, TicketException, ContadorException {
        // registramos en el log
        LogGiftCardBean logGiftCard = new LogGiftCardBean();
        logGiftCard.setUsuario(Sesion.getUsuario().getUsuario());
        logGiftCard.setCodCliente(codCliente);
        logGiftCard.setUsuarioAutorizador(autorizador);
        logGiftCard.setIdGiftCard(giftCard.getIdGiftCard());
        logGiftCard.setCodOperacion(operacion);
        logGiftCard.setCodAlmacen(Sesion.getTienda().getCodalm());
        logGiftCard.setCodCaja(Sesion.getDatosConfiguracion().getCodcaja());
        logGiftCard.setIdCargaGiftCard(new Long(ServicioContadoresCaja.obtenerContadorGiftcard()));
        logGiftCard.setSaldo(giftCard.getSaldo());
        
        logGiftCard.setAbono(pagos.getPagado());
        logGiftCard.setPagos(TicketXMLServices.getXMLPagos(pagos));
        
        LogGiftCardDao.insert(conn, logGiftCard);
        ServicioContadoresCaja.incrementarContadorGiftcard(conn);
        
        return logGiftCard;
    }

    private static void registrarLogsConsumo(Connection conn, String idTicket, String operacion, GiftCardBean giftCard, BigDecimal valorPagado, String tipoConsumo) throws SQLException, TicketException, ContadorException {
        // registramos en el log
        LogGiftCardConsumoBean logGiftCardConsumo = new LogGiftCardConsumoBean(
                idTicket, 
                giftCard.getIdGiftCard(), 
                Sesion.getTienda().getCodalm(),
                Sesion.getUsuario().getUsuario(), 
                operacion, 
                valorPagado, 
                tipoConsumo);
        
        LogGiftCardConsumoDao.insert(conn, logGiftCardConsumo);
    }
    public static LogGiftCardBean recargar(GiftCardBean giftCard, BigDecimal saldoNuevo, String autorizador, PagosTicket pagos, Cliente cliente) throws GiftCardException, GiftCardConstraintViolationException {
                        
        Connection conn = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
            
        try {
            log.debug("recargar() - Recargando saldo de giftCard " + giftCard.getIdGiftCard());

            BigDecimal saldoAntiguo = giftCard.getSaldo();
            giftCard.setSaldo(saldoNuevo);
            giftCard.setProcesado(false);

            conn.abrirConexion(Database.getConnection());
            conn.iniciaTransaccion();

            // insertamos
            boolean actualizado = GiftCardDao.actualizaSaldo(conn, giftCard, saldoAntiguo);
            if (!actualizado) {
                throw new GiftCardConstraintViolationException("El saldo de la GiftCard ha varidado desde su consulta. Vuelva a repetir el proceso, por favor.");
            }
            
            LogGiftCardBean logGiftCard = registrarLogsPagos(conn, LogGiftCardBean.OPERACION_RECARGA, cliente.getCodcli(), autorizador, giftCard, pagos);
            
            em.getTransaction().begin();
            try {
                ReferenciaTicket referencia = ReferenciaTicket.getReferenciaGiftCard(giftCard.getIdGiftCard());
                TicketService.procesarMediosPagos(em, pagos.getPagos(), referencia, "GIF", giftCard.getIdGiftCard());
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw e;
            }
            
            conn.commit();
            conn.finalizaTransaccion();
            BonosServices.crearBonosPagos(pagos, giftCard.getIdGiftCard(), BonosServices.LLAMADO_DESDE_GIFTCARD, cliente);
            
            return logGiftCard;
        }
        catch (KeyConstraintViolationException e) {
            conn.deshacerTransaccion();
            log.info("crear() - No se ha podido registrar la giftcard: " + e.getMessage());
            throw new GiftCardConstraintViolationException("Error creando nueva giftcard: " + e.getDefaultMessage());
        }
        catch (SQLException e) {
            conn.deshacerTransaccion();
            String msg = "Error creando nueva giftcard: " + e.getMessage();
            log.error("crear() - " + msg);
            throw new GiftCardException(msg, e);
        }
        catch (MovimientoCajaException e){
            conn.deshacerTransaccion();
            em.getTransaction().rollback();
            return null;
        }
        catch (Exception e) {
            conn.deshacerTransaccion();
            String msg = "Error creando nueva giftcard: " + e.getMessage();
            log.error("crear() - " + msg);
            throw new GiftCardException(msg, e);
        }
        finally {
            conn.cerrarConexion();
            em.close();
        }
        
    }


    
     public static void actualizarSaldo(EntityManager em, GiftCardBean giftCard, BigDecimal saldoNuevo, BigDecimal valorPagado, ReferenciaTicket referencia) throws SQLException, GiftCardConstraintViolationException {
        log.debug("actualizarSaldo() - Actualizando saldo de giftCard " + giftCard.getIdGiftCard());

        Connection conn = new Connection();
        BigDecimal saldoAntiguo = giftCard.getSaldo();
        giftCard.setSaldo(saldoNuevo);
        giftCard.setProcesado(false);

        // insertamos
        boolean actualizado = GiftCardDao.actualizaSaldoJPA(em, giftCard, saldoAntiguo);
         if (!actualizado) {
            throw new GiftCardConstraintViolationException("El saldo de la GiftCard ha varidado desde su consulta. Vuelva a repetir el proceso, por favor.");
        }
        conn.abrirConexion(Database.getConnection());
        conn.iniciaTransaccion();
        try {
            registrarLogsConsumo(conn, referencia.getNumDocRefTarjeta(), LogGiftCardBean.OPERACION_CONSUMO,giftCard, valorPagado,referencia.getTipoReferencia());
        } catch (TicketException ex) {
            java.util.logging.Logger.getLogger(ServicioGiftCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ContadorException ex) {
            java.util.logging.Logger.getLogger(ServicioGiftCard.class.getName()).log(Level.SEVERE, null, ex);
        }
        conn.commit();
        conn.finalizaTransaccion();
    }
     
     public static LogGiftCardBean consultarLogGiftCard(String codAlm, String codCaja, Long idCargaGiftCard) throws GiftCardException{
        Connection conn = new Connection();
        try {
            log.debug("consultarLogGiftCard() - Consultando giftcard con codalm: " + codAlm+", codcaja: "+codCaja+", id: "+idCargaGiftCard);
            conn.abrirConexion(Database.getConnection());
            
            return LogGiftCardDao.consultar(conn, codAlm, codCaja, idCargaGiftCard);
        }
        catch (SQLException e) {
            log.error("consultarLogGiftCard() - " + e.getMessage());
            String mensaje = "Error al consultar giftcard: " + e.getMessage();
            throw new GiftCardException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
     }

     public static List<LogGiftCardBean> consultarUsosLogGiftCard(String idGiftCard) throws GiftCardException{
         Connection conn = new Connection();
         try{
             log.debug("consultarUsosLogGiftCard() - Consultando usos de giftcard con idGiftCard: " + idGiftCard);
             conn.abrirConexion(Database.getConnection());
             
             return LogGiftCardDao.consultarUsos(conn, idGiftCard);
         }
         catch (SQLException e) {
            log.error("consultarUsosLogGiftCard() - " + e.getMessage());
            String mensaje = "Error al consultar usos de la giftcard: " + e.getMessage();
            throw new GiftCardException(mensaje, e);             
         }
        finally {
            conn.cerrarConexion();
        }         
     }
}
