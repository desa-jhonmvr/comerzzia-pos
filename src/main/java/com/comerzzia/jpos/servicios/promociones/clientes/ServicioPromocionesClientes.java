package com.comerzzia.jpos.servicios.promociones.clientes;

import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.persistencia.promociones.PromocionSocioBean;
import com.comerzzia.jpos.persistencia.promociones.PromocionesDao;
import com.comerzzia.jpos.persistencia.promociones.TipoPromocionBean;
import com.comerzzia.jpos.persistencia.promociones.clientes.PromocionClienteBean;
import com.comerzzia.jpos.persistencia.promociones.clientes.PromocionClienteExample;
import com.comerzzia.jpos.persistencia.promociones.clientes.PromocionClienteMapper;
import com.comerzzia.jpos.persistencia.promociones.cupones.CuponesDao;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDtoManualTotal;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.enums.EnumNotaCreditoError;
import com.comerzzia.jpos.util.exception.SocketTPVException;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.exception.PersistenceExceptionFactory;
import es.mpsistemas.util.mybatis.session.SqlSession;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.ibatis.exceptions.PersistenceException;

public class ServicioPromocionesClientes {

    protected static Logger log = Logger.getMLogger(ServicioPromocionesClientes.class);

    public static boolean existePromocionNumCredito(Integer numCredito, Long idPromocion) throws PromocionException {
        log.debug("existePromocionNumCredito() - Consultando aplicaciones de la promoción: " + idPromocion + " para el número de crédito: " + numCredito);
        return existePromocionCliente(numCredito.toString(), idPromocion);
    }

    public static boolean existePromocionCliente(String codCliente, Long idPromocion) throws PromocionException {
        SqlSession sqlSession = new SqlSession();
        try {
            log.debug("existePromocionCliente() - Consultando aplicaciones de la promoción: " + idPromocion + " para el cliente: " + codCliente);
            sqlSession.openSession(SessionFactory.openSession());
            PromocionClienteMapper mapper = sqlSession.getMapper(PromocionClienteMapper.class);
            PromocionClienteExample example = new PromocionClienteExample();
            example.or().andCodClienteEqualTo(codCliente).andIdPromocionEqualTo(idPromocion).andAnuladaEqualTo("N");
            return !mapper.selectByExample(example).isEmpty();
        } catch (Exception e) {
            log.error("existePromocionCliente() - Error al consultar si el cliente ya consumió la promoción: " + e.getMessage(), e);
            throw new PromocionException("Error al consultar si el cliente ya consumió la promoción. ", e);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Consulta si el ticket indicado fue utilizado como primera factura en una
     * promoción día del socio
     */
    public static boolean existeTicketDiaSocioReferenciado(String uidTicket) throws PromocionException {
        SqlSession sqlSession = new SqlSession();
        try {
            log.debug("existeTicketDiaSocioReferenciado() - Consultando aplicaciones de cualquier promoción día del socio a una factura con uid: " + uidTicket);
            sqlSession.openSession(SessionFactory.openSession());
            PromocionClienteMapper mapper = sqlSession.getMapper(PromocionClienteMapper.class);
            PromocionClienteExample example = new PromocionClienteExample();
            example.or().andUidTicketEqualTo(uidTicket).andIdPromocionEqualTo(-1L).andAnuladaEqualTo("N");
            return !mapper.selectByExample(example).isEmpty();
        } catch (Exception e) {
            log.error("existeTicketDiaSocioReferenciado() - Error al consultar si la factura ya fue utilizada para aplicar la promoción día del socio: " + e.getMessage(), e);
            throw new PromocionException("Error al consultar si la factura ya fue utilizada para aplicar la promoción día del socio. ", e);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Comprueba si al ticket indicado se le ha aplicado la promoción indicada.
     */
    public static boolean existeTicketPromocion(String uidTicket, Long idPromocion) throws PromocionException {
        SqlSession sqlSession = new SqlSession();
        try {
            log.debug("existeTicketPromocion() - Consultando aplicaciones de promoción indicada a una factura con uid: " + uidTicket);
            sqlSession.openSession(SessionFactory.openSession());
            PromocionClienteMapper mapper = sqlSession.getMapper(PromocionClienteMapper.class);
            PromocionClienteExample example = new PromocionClienteExample();
            example.or().andUidTicketEqualTo(uidTicket).andIdPromocionEqualTo(idPromocion).andAnuladaEqualTo("N");
            return !mapper.selectByExample(example).isEmpty();
        } catch (Exception e) {
            log.error("existeTicketPromocion() - Error al consultar si la factura ya fue utilizada para aplicar la promoción indicada: " + e.getMessage(), e);
            throw new PromocionException("Error al consultar si la factura ya fue utilizada para aplicar la promoción indicada. ", e);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Comprueba si al ticket indicado se le ha aplicado alguna promoción día
     * del socio.
     */
    public static boolean existeTicketDiaSocioAplicado(String uidTicket) throws PromocionException {
        SqlSession sqlSession = new SqlSession();
        try {
            log.debug("existeTicketDiaSocioAplicado() - Consultando si se ha aplicado promoción día del socio a factura con uid: " + uidTicket);
            sqlSession.openSession(SessionFactory.openSession());
            PromocionClienteMapper mapper = sqlSession.getMapper(PromocionClienteMapper.class);
            return mapper.selectTicketDiaSocio(uidTicket) > 0;
        } catch (Exception e) {
            log.error("existeTicketDiaSocioAplicado() - Error Consultando si se ha aplicado promoción día del socio a factura: " + e.getMessage(), e);
            throw new PromocionException("Error Consultando si se ha aplicado promoción día del socio a factura. ", e);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Registra las promociones aplicadas al cliente del ticket (sólo aquellas
     * que requieren de este control).
     */
    public static void registrarPromocionesAplicadas(Connection conn, TicketS ticket) throws PromocionException {
        try {
            Map<Long, Integer> promocionesAplicadas = ticket.getTicketPromociones().getClientePromoAplicadas();
            if (promocionesAplicadas == null || promocionesAplicadas.isEmpty()) {
                log.debug("registrarPromocionesAplicadas() - La venta no incluye promociones aplicadas que requieran control por cliente.");
                return;
            }
            SqlSession sqlSession = new SqlSession();
            log.debug("registrarPromocionesAplicadas() - La venta incluye " + promocionesAplicadas.size() + " promociones que requieren control por cliente. Las registramos.");
            sqlSession.openSession(SessionFactory.openSession(conn));
            PromocionClienteMapper mapper = sqlSession.getMapper(PromocionClienteMapper.class);
            for (Long idPromocion : promocionesAplicadas.keySet()) {
                String cliente = ticket.getCliente().getIdentificacion();
                if (promocionesAplicadas.get(idPromocion) != null) { // asociada a número de crédito
                    cliente = promocionesAplicadas.get(idPromocion).toString();
                } else if (ticket.getCliente().isClienteGenerico()) { // asociada a cliente. Si es cliente genérico nos la saltamos
                    continue;
                }
                PromocionClienteBean promoCliente = new PromocionClienteBean();
                promoCliente.setCodAlmacen(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
                promoCliente.setCodCliente(cliente);
                promoCliente.setFecha(new Date());
                promoCliente.setProcesado(false);
                promoCliente.setAnulada("N");
                promoCliente.setIdPromocion(idPromocion);
                promoCliente.setUidTicket(ticket.getUid_ticket());
                promoCliente.setUidTicketDSocio(ticket.getUid_ticket());
                if (idPromocion == -1L) {
                    promoCliente.setUidTicket(ticket.getTicketPromociones().getFacturaOrigenDiaSocio().getUid_ticket());
                    promoCliente.setUidTicketDSocio(ticket.getUid_ticket());
                }
                try {
                    mapper.insert(promoCliente);
                } catch (PersistenceException e) { // principalmente, para promoción día socio en referencia de factura 1
                    if (PersistenceExceptionFactory.getPersistenceExpception(e).isKeyConstraintViolationException()) {
                        mapper.updateByPrimaryKey(promoCliente);
                    } else {
                        throw e;
                    }
                }
            }

        } catch (Exception e) {
            log.error("registrarPromocionesAplicadas() - Error salvando promociones a cliente que requieren control: " + e.getMessage(), e);
            throw new PromocionException("Error salvando promociones a cliente que requieren control.", e);
        }
    }

    /**
     * Anula las promociones aplicadas al ticket .
     */
    public static void anularPromocionesAplicadas(Connection conn, SqlSession sqlSession, String uidTicket, BigDecimal saldo) throws PromocionException, SocketTPVException {
        try {
            log.debug("anularPromocionesAplicadas() - Anulando promociones que se pudieron aplicar en la factura: " + uidTicket);
            PromocionClienteMapper mapper = sqlSession.getMapper(PromocionClienteMapper.class);

            // Esto comprueba si hay algún cupón de los que queremos utilizar como ya anulado.                      
            // if (CuponesDao.consultarCuponesUsados(conn, uidTicket)){
            if (false) {
                log.error("anularPromocionesAplicadas() - Existen sukupones/billetones utilizados de la factura que desea devolver.");
                throw new SocketTPVException(EnumNotaCreditoError.ERROR_SUKUPON.getCodigo(), EnumNotaCreditoError.ERROR_SUKUPON.getDescripcion());
            }

            CuponesDao.activarCuponesUsadosEnFactura(conn, uidTicket);
            CuponesDao.desactivarCuponesEmitidosEnFactura(conn, uidTicket, saldo);

            // Esto establece como anuladas todas las promociones
            PromocionClienteExample example = new PromocionClienteExample();
            example.or().andUidTicketEqualTo(uidTicket);

            PromocionClienteBean promoCliente = new PromocionClienteBean();
            promoCliente.setAnulada("S");
            promoCliente.setProcesado(false);

            mapper.updateByExampleSelective(promoCliente, example);
        } catch (SocketTPVException e) {
            throw e;
        } catch (Exception e) {
            log.error("anularPromocionesAplicadas() - Error anulando promociones a cliente que requieren control: " + e.getMessage(), e);
            throw new PromocionException("Error anulando promociones a cliente que requieren control.", e);
        }
    }

    public static void anularReferenciaFacturaDiaSocio(Connection conn, String uidTicketDS) throws PromocionException {

        try {
            log.debug("existeTicketDiaSocioAplicado() - Consultando si se ha aplicado promoción día del socio a factura con uid: " + uidTicketDS);
            SqlSession sqlSession = new SqlSession();
            sqlSession.openSession(SessionFactory.openSession(conn));
            log.debug("anularReferenciaFacturaDiaSocio() - Anulando referencia de primera factura para promoción Día del socio: " + uidTicketDS);
            PromocionClienteMapper mapper = sqlSession.getMapper(PromocionClienteMapper.class);

            PromocionClienteExample example = new PromocionClienteExample();
            // example.or().andUidTicketDSEqualTo(uidTicketDS).andIdPromocionEqualTo(-1L);
            example.or().andUidTicketDSEqualTo(uidTicketDS);

            PromocionClienteBean promoCliente = new PromocionClienteBean();
            promoCliente.setAnulada("S");
            promoCliente.setProcesado(false);

            mapper.updateByExampleSelective(promoCliente, example);
        } catch (Exception e) {
            log.error("anularReferenciaFacturaDiaSocio() - Error Anulando referencia de primera factura para promoción Día del socio: " + e.getMessage(), e);
            throw new PromocionException("Error Anulando referencia de primera factura para promoción Día del socio.", e);
        }
    }

    /**
     * Anula las promociones aplicadas al ticket .
     *
     * @param conn
     * @param sqlSession
     * @param devolucion
     * @return
     * @throws java.sql.SQLException
     * @throws com.comerzzia.jpos.util.exception.SocketTPVException
     * @throws com.comerzzia.jpos.servicios.promociones.PromocionException
     */
    public static Cupon saldosCupon(Connection conn, SqlSession sqlSession, com.comerzzia.jpos.servicios.devoluciones.Devolucion devolucion) throws SQLException, PromocionException, SocketTPVException {
        Cupon cupon = new Cupon();
        Cupon cuponDet = new Cupon();
        Cupon cuponTotal = new Cupon();
        cuponTotal = CuponesDao.consultarCuponesEmitidosEnFactura(conn, devolucion.getTicketOriginal().getUid_ticket());
        if (cuponTotal != null) {
            CuponesDao.desactivarCuponesEmitidosFactura(conn, devolucion.getTicketOriginal().getUid_ticket(), cuponTotal.getIdPromocion());
        }
        cupon = CuponesDao.consultarCuponesEmitidosEnFacturaSaldo(conn, devolucion.getTicketOriginal().getUid_ticket());
        if (cupon != null) {
            PromocionBean promocionBean = PromocionesDao.consultar(conn, cupon.getIdPromocion());
            cupon.setIdTipoPromocion(promocionBean.getIdTipoPromocion());
            BigDecimal valor = BigDecimal.ZERO;
            BigDecimal valorTotal = BigDecimal.ZERO;
            BigDecimal valorV = BigDecimal.ZERO;
            if (cupon.getIdTipoPromocion() != null) {
                if (Objects.equals(cupon.getIdTipoPromocion(), TipoPromocionBean.TIPO_PROMOCION_BILLETON)) {

                    for (LineaTicket linea : devolucion.getTicketDevolucion().getLineas().getLineas()) {
                        valor = BigDecimal.ZERO;
                        cuponDet = CuponesDao.consultarCuponesEmitidosEnFacturaDet(conn, cupon.getIdCupon(), linea.getArticulo().getCodart());
                        if (cuponDet.getIdCupon() != null) {
                            anularPromocionesAplicadasDetallada(conn, sqlSession, devolucion.getTicketOriginal().getUid_ticket(), linea.getArticulo().getCodart(), devolucion.getNotaCredito().getUidNotaCredito());
                            valor = new BigDecimal(cuponDet.getSaldo());
                            valorTotal = valorTotal.add(valor);
                        }
//            cuponDet.setCodart(cupon.getCodart());
                    }
                    if (cupon.getSaldo() == null) {
                        valorV = new BigDecimal(cupon.getVariable());
                    } else {
                        valorV = new BigDecimal(cupon.getSaldo());
                    }
                    BigDecimal totalSaldo = valorV.subtract(valorTotal);
                    if (totalSaldo.compareTo(new BigDecimal("0.00")) == 0) {
                        anularPromocionesAplicadas(conn, sqlSession, devolucion.getTicketOriginal().getUid_ticket(), totalSaldo);
                    } else {
                        CuponesDao.desactivarCuponEnFactura(conn, devolucion.getTicketOriginal().getUid_ticket(), totalSaldo);
                    }
                    if (cupon.getReferenciaUso() != null) {
                        CuponesDao.desactivarCuponEnFacturaTotal(conn, cupon.getReferenciaUso(), new BigDecimal("0.00"));
                    }
                }
            }
        }

        return cuponDet;
    }

    /**
     * Anula las promociones aplicadas al ticket .
     */
    public static void anularPromocionesAplicadasDetallada(Connection conn, SqlSession sqlSession, String uidTicket, String codArt, String uidNotaCredito) throws PromocionException, SocketTPVException {
        try {
            CuponesDao.desactivarCuponesEmitidosEnFacturaDet(conn, uidTicket, codArt, uidNotaCredito);
        } catch (Exception e) {
            log.error("anularPromocionesAplicadas() - Error anulando promociones a cliente que requieren control: " + e.getMessage(), e);
            throw new PromocionException("Error anulando promociones a cliente que requieren control.", e);
        }
    }

    
    /**
     * 
     * @return
     * @throws PromocionException
     * @throws SQLException 
     */
     public static PromocionSocioBean consultarPromoClienteSocio() throws PromocionException, SQLException {
         return consultarPromoClienteSocio(Sesion.getTicket().getCliente().getCodcli());
     }
    /**
     * Consulta si el socio puede aplicar a la promoción.
     *
     * @param codCli
     * @return
     * @throws PromocionException
     * @throws SQLException
     */
    public static PromocionSocioBean consultarPromoClienteSocio(String codCli) throws PromocionException, SQLException {
        Connection conn = new Connection();
        PromocionSocioBean promoSocio = new PromocionSocioBean();
        try {
            conn.abrirConexion(Database.getConnection());
            promoSocio = PromocionesDao.consultarPromoSocio(conn, codCli);
        } catch (Exception e) {
            log.error("Error al consultar clientes para la promoción de nuevo Socio. " + e.getMessage(), e);
            throw new PromocionException("Error al consultar clientes para la promoción de nuevo Socio.", e);
        } finally {
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
        return promoSocio;
    }

    /**
     * Inserta la factura en donde el cliente nuevo ya utilizo su beneficio del
     * decuento por dia del socio.
     */
    public static boolean updatePromoClienteSocio() throws SQLException, PromocionException {
        Connection conn = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        boolean promoSocio = false;
        String promoActiva = null;
        if (PromocionTipoDtoManualTotal.isActiva()) {
            promoActiva = "S";
        } else {
            promoActiva = "N";
        }
        try {
            em.getTransaction().begin();
            conn.abrirConexion(Database.getConnection());
            promoSocio = PromocionesDao.updatePromoClienteSocio(conn, Sesion.getTicket().getUid_ticket(), Sesion.getTicket().getCliente().getCodcli(), promoActiva);
            em.getTransaction().commit();
            return promoSocio;

        } catch (Exception e) {
            log.error("Error al consultar clientes para la promoción de nuevo Socio. " + e.getMessage(), e);

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PromocionException("Error al consultar clientes para la promoción de nuevo Socio.", e);
        } finally {
            em.close();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

    /**
     * .
     */
    public static boolean updatePromoClienteSocioNC(String factura, String cedula, String notaCredito) throws SQLException, PromocionException {
        Connection conn = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        conn.abrirConexion(Database.getConnection());
        boolean promoSocio = false;
        try {
            em.getTransaction().begin();
            conn.abrirConexion(Database.getConnection());
            em.getTransaction().commit();
            return promoSocio = PromocionesDao.updatePromoClienteSocioNC(conn, factura, cedula, notaCredito);
        } catch (Exception e) {
            log.error("Error al consultar clientes para la promoción de nuevo Socio. " + e.getMessage(), e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PromocionException("Error al consultar clientes para la promoción de nuevo Socio.", e);
        } finally {
            em.close();
            if (conn != null) {
                conn.cerrarConexion();
            }
        }
    }

}
