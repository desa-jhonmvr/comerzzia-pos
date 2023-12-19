/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.letras;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.persistencia.letras.LetraBean;
import com.comerzzia.jpos.persistencia.letras.LetraExample;
import com.comerzzia.jpos.persistencia.letras.LetraMapper;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaBean;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaExample;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaMapper;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.tickets.TicketsDao;
import com.comerzzia.jpos.servicios.bonos.BonosServices;
import com.comerzzia.jpos.servicios.clientes.ClientesServices;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoLetra;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.tickets.ReferenciaTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.servicios.tickets.xml.TicketXMLServices;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.exception.PersistenceException;
import es.mpsistemas.util.mybatis.session.SqlSession;
import es.mpsistemas.util.xml.XMLDocument;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class LetraCambiosServices {

    protected static Logger log = Logger.getMLogger(LetraCambiosServices.class);

    public static void crearLetra(Connection conn, PagoCreditoLetra pagoLetra, String uidTicket) throws LetraCambioException {
        SqlSession session = new SqlSession();
        com.comerzzia.jpos.util.db.Connection connCmz = new com.comerzzia.jpos.util.db.Connection();
        try {
            connCmz.abrirConexion(conn);
            // instanciamos la nueva letra
            LetraBean letra = new LetraBean();
            letra.setUidLetra(UUID.randomUUID().toString());
            letra.setUidTicket(uidTicket);
            letra.setCodAlmacen(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            letra.setCodCaja(Sesion.config.getCodcaja());
            letra.setCodCliente(pagoLetra.getCliente().getCodcli());
            letra.setTotal(pagoLetra.getUstedPaga()); // sin intereses
            letra.setPlazo(pagoLetra.getPlanSeleccionado().getNumCuotas());
            letra.setIntereses(pagoLetra.getPlanSeleccionado().getPorcentajeInteres());
            letra.setFecha(new Fecha());
            letra.setProcesado(false);
            letra.setIdentificador(ServicioContadoresCaja.obtenerContadorCreditoTemporal());
            ServicioContadoresCaja.incrementarContadorCreditoTemporal(connCmz);
            letra.setEstado(LetraBean.ESTADO_PENDIENTE);

            //consultar los plazos
            log.debug("crearLetra() - consultando variable para los plazos de las letras");
            Integer diasUnSoloPlazo = new Integer(Variables.getVariable(Variables.POS_LETRAS_PLAZO_UNA_CUOTA));
            //Integer diasVariosPlazos = new Integer(Variables.getVariable(Variables.POS_LETRAS_PLAZO_VARIAS_CUOTAS));
            BigDecimal importeCuota = letra.getImporteCuotaParcial(); // con intereses
            // creamos los detalles
            Integer diasPlazos = null;
            if (letra.getPlazo() <= 1) {
                diasPlazos = diasUnSoloPlazo;
            }
            for (short i = 1; i <= letra.getPlazo(); i++) {
                LetraCuotaBean cuota = new LetraCuotaBean();
                cuota.setUidLetra(letra.getUidLetra());
                cuota.setCuota(i);
                cuota.setValor(importeCuota);
                cuota.setEstado(LetraCuotaBean.ESTADO_PENDIENTE);
                cuota.setProcesado(false);
                Fecha fechaVencimiento = new Fecha();
                if (diasPlazos == null) {
                    fechaVencimiento.sumaMeses(i);
                } else {
                    fechaVencimiento.sumaDias(diasPlazos);
                }
                cuota.setFechaVencimiento(fechaVencimiento);
                letra.addCuota(cuota);
            }

            // registramos todo en base de datos
            session.openSession(SessionFactory.openSession(conn));
            LetraMapper mapper = session.getMapper(LetraMapper.class);
            LetraCuotaMapper mapperCuota = session.getMapper(LetraCuotaMapper.class);
            mapper.insert(letra);
            for (LetraCuotaBean cuota : letra.getCuotas()) {
                mapperCuota.insert(cuota);
            }
            pagoLetra.setLetra(letra);
        } catch (PersistenceException e) {
            String msg = "Error registrando nueva letra en base de datos: " + e.getMessage();
            log.error("crearLetra() - " + msg, e);
            throw new LetraCambioException(msg, e);
        } catch (Exception e) {
            String msg = "Error inesperado creando nueva letra en base de datos: " + e.getMessage();
            log.error("crearLetra() - " + msg, e);
            throw new LetraCambioException(msg, e);
        }
    }

    public static LetraBean consultarLetra(String uidLetra) throws LetraCambioException {
        SqlSession session = new SqlSession();
        try {
            log.debug("consultarLetra() - Consultando letra con uid: " + uidLetra);

            session.openSession(SessionFactory.openSession());
            LetraMapper mapper = session.getMapper(LetraMapper.class);
            LetraCuotaMapper mapperCuotas = session.getMapper(LetraCuotaMapper.class);

            // consultamos letra
            LetraBean letra = mapper.selectFromViewByPrimaryKey(uidLetra);

            // consultamos detalles
            LetraCuotaExample example = new LetraCuotaExample();
            example.or().andUidLetraEqualTo(uidLetra);
            example.setOrderByClause("CUOTA");
            letra.setCuotas(mapperCuotas.selectByExample(example));

            if (letra.getProximaCuotaCobro() != null && letra.getProximaCuotaCobro().isFechaVencida()) {
                //BigDecimal interesMora = ServicioInteresMora.consultarInteresMora();
                BigDecimal interesMora = Variables.getVariableAsBigDecimal(Variables.POS_CONFIG_INTERES_MORA);
                letra.establecerInteresMora(interesMora);
            }

            // consultamos cliente
            Cliente cliente = ClientesServices.getInstance().consultaClienteIdenti(letra.getCodCliente());
            letra.setCliente(cliente);

            // consultamos factura original
            TicketsAlm ticketAlm = TicketsDao.consultarTicket(letra.getUidTicket());
            TicketOrigen ticket = TicketOrigen.getTicketOrigen(new XMLDocument((byte[]) ticketAlm.getTicket()));
            letra.setTicketOrigen(ticket);
            return letra;
        } catch (NoResultException e) {
            String msg = "No se ha encontrado cliente para los datos introducidos";
            log.error("consultarLetra() - " + msg + e.getMessage());
            throw new LetraCambioException(msg, e);
        } catch (Exception e) {
            String msg = "Error consultando letra en el sistema. ";
            log.error("consultarLetra() - " + msg + e.getMessage(), e);
            throw new LetraCambioException(msg, e);
        } finally {
            session.close();
        }
    }

    public static List<LetraBean> consultarLetrasPorCliente(String cedula) throws LetraCambioException {
        SqlSession sqlSession = new SqlSession();
        try {
            log.debug("consultarLetrasPorCliente() - Consultamos letras de cambio del cliente: " + cedula);
            sqlSession.openSession(SessionFactory.openSession());
            LetraMapper mapper = sqlSession.getMapper(LetraMapper.class);

            LetraExample example = new LetraExample();
//            example.or().andCodClienteEqualTo(cedula).andEstadoEqualTo(LetraBean.ESTADO_PENDIENTE);
//            example.or().andCodClienteEqualTo(cedula).andEstadoEqualTo(LetraBean.ESTADO_PENDIENTE_R);
            example.or().andCodClienteEqualTo(cedula);
            example.setOrderByClause("FECHA DESC");

            List<LetraBean> letras = mapper.selectFromViewByExample(example);
            return letras;
        } catch (Exception e) {
            String msg = "Error consultando letras de cambio para el cliente: " + cedula;
            log.error("consultarLetrasPorCliente() - " + msg, e);
            throw new LetraCambioException(msg, e);
        } finally {
            sqlSession.close();
        }
    }

    public static List<LetraBean> consultarLetrasPorFactura(String codAlmacen, String codCaja, Long idTicket)
            throws LetraCambioException {
        SqlSession sqlSession = new SqlSession();
        try {
            log.debug("consultarLetrasPorFactura() - Consultamos letras de cambio de una factura... ");
            TicketsAlm ticket = TicketService.consultarTicket(idTicket, codCaja, codAlmacen);

            sqlSession.openSession(SessionFactory.openSession());
            LetraMapper mapper = sqlSession.getMapper(LetraMapper.class);

            LetraExample example = new LetraExample();
//            example.or().andUidTicketEqualTo(ticket.getUidTicket()).andEstadoEqualTo(LetraBean.ESTADO_PENDIENTE);
//            example.or().andUidTicketEqualTo(ticket.getUidTicket()).andEstadoEqualTo(LetraBean.ESTADO_PENDIENTE_R);
            example.or().andUidTicketEqualTo(ticket.getUidTicket());

            example.setOrderByClause("FECHA DESC");

            List<LetraBean> letras = mapper.selectFromViewByExample(example);
            return letras;
        } catch (NoResultException e) {
            throw new LetraCambioException("La factura indicada no existe.");
        } catch (Exception e) {
            String msg = "Error consultando letras de cambio para una factura. ";
            log.error("consultarLetrasPorFactura() - " + msg, e);
            throw new LetraCambioException(msg, e);
        } finally {
            sqlSession.close();
        }
    }

    public static List<LetraBean> consultarLetrasPorCaja(String codAlmacen, String codCaja)
            throws LetraCambioException {
        SqlSession sqlSession = new SqlSession();
        try {
            log.debug("consultarLetrasPorFactura() - Consultamos letras de cambio de una factura... ");
            // TicketsAlm ticket = TicketService.consultarCaja(codCaja, codAlmacen);

            sqlSession.openSession(SessionFactory.openSession());
            LetraMapper mapper = sqlSession.getMapper(LetraMapper.class);

            LetraExample example = new LetraExample();
//            example.or().andUidTicketEqualTo(ticket.getUidTicket()).andEstadoEqualTo(LetraBean.ESTADO_PENDIENTE);
//            example.or().andUidTicketEqualTo(ticket.getUidTicket()).andEstadoEqualTo(LetraBean.ESTADO_PENDIENTE_R);
            //example.or().andUidTicketEqualTo(codCaja);
            example.or().andCodCajaEqualTo(codCaja);
            example.setOrderByClause("FECHA DESC");

            List<LetraBean> letras = mapper.selectFromViewByExample(example);
            return letras;
        } catch (NoResultException e) {
            throw new LetraCambioException("La caja indicada no existe.");
        } catch (Exception e) {
            String msg = "Error consultando letras de cambio por caja. ";
            log.error("consultarLetrasPorFactura() - " + msg, e);
            throw new LetraCambioException(msg, e);
        } finally {
            sqlSession.close();
        }
    }
    
     public static List<LetraBean> consultarLetras()
            throws LetraCambioException {
        SqlSession sqlSession = new SqlSession();
        try {
            log.debug("consultarLetrasPorFactura() - Consultamos letras de cambio de una factura... ");
            // TicketsAlm ticket = TicketService.consultarCaja(codCaja, codAlmacen);

            sqlSession.openSession(SessionFactory.openSession());
            LetraMapper mapper = sqlSession.getMapper(LetraMapper.class);

            LetraExample example = new LetraExample();
//            example.or().andUidTicketEqualTo(ticket.getUidTicket()).andEstadoEqualTo(LetraBean.ESTADO_PENDIENTE);
//            example.or().andUidTicketEqualTo(ticket.getUidTicket()).andEstadoEqualTo(LetraBean.ESTADO_PENDIENTE_R);
            //example.or().andUidTicketEqualTo(codCaja);
           // example.or().andCodCajaEqualTo(codCaja);
            example.setOrderByClause("FECHA DESC");

            List<LetraBean> letras = mapper.selectFromViewByExample(example);
            return letras;
        } catch (NoResultException e) {
            throw new LetraCambioException("No existen Letras.");
        } catch (Exception e) {
            String msg = "Error consultando letras de cambio por caja. ";
            log.error("consultarLetrasPorFactura() - " + msg, e);
            throw new LetraCambioException(msg, e);
        } finally {
            sqlSession.close();
        }
    }

    public static void realizarPagoCuota(TicketS ticket, LetraBean letra, LetraCuotaBean letraCuota) throws LetraCambioException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            log.debug("Realizando pago de cuota de letra...");
            em.getTransaction().begin();

            // completamos datos de la cuota
            letraCuota.setPagos(TicketXMLServices.getXMLPagos(ticket.getPagos()));
            letraCuota.setProcesado(false);
            letraCuota.setEstado(LetraCuotaBean.ESTADO_COBRADA);
            letraCuota.setFechaCobro(new Fecha());
            letraCuota.setCodcajaAbono(Sesion.getCajaActual().getCajaActual().getCodcaja());
            letraCuota.setIdAbono(ServicioContadoresCaja.obtenerContadorAbonoLetra());
            letraCuota.setUsuarioAbono(Sesion.getUsuario().getUsuario());

            // procesamos pagos
            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaLetra(letra, letraCuota);
            String documento = letra.getCodAlmacen() + "-" + letraCuota.getCodcajaAbono() + "-"+ String.format("%09d", letraCuota.getIdAbono());
            TicketService.procesarMediosPagos(em, ticket.getPagos().getPagos(), referencia, "LET", documento);

            // actualizamos la cuota de la letra
            Connection conn = em.unwrap(Connection.class);
            SqlSession session = new SqlSession();
            session.openSession(SessionFactory.openSession(conn));
            LetraCuotaMapper mapperCuotas = session.getMapper(LetraCuotaMapper.class);
            mapperCuotas.updateByPrimaryKeyWithBLOBs(letraCuota);

            // actualizamos la letra si ya se ha pagado por completo
            if (!letra.isCuotasPendientesCobro()) {
                LetraBean letraUpdate = new LetraBean();
                letraUpdate.setEstado(LetraBean.ESTADO_COMPLETA);
                letraUpdate.setUidLetra(letra.getUidLetra());
                letraUpdate.setProcesado(false);
                LetraMapper mapper = session.getMapper(LetraMapper.class);
                mapper.updateByPrimaryKeySelective(letraUpdate);
            } else if (ticket.getPagos().contieneRetencion() != null) {
                LetraBean letraUpdate = new LetraBean();
                letraUpdate.setEstado(LetraBean.ESTADO_PENDIENTE_R);
                letraUpdate.setUidLetra(letra.getUidLetra());
                letraUpdate.setProcesado(false);
                LetraMapper mapper = session.getMapper(LetraMapper.class);
                mapper.updateByPrimaryKeySelective(letraUpdate);
            }

            em.getTransaction().commit();
            PrintServices.getInstance().limpiarListaDocumentos();
            PrintServices.getInstance().imprimirComprobantePagoLetraCambio(ticket, letra, letraCuota);

            DocumentosService.crearDocumentoLetraAbono(ticket, PrintServices.getInstance().getDocumentosImpresos(), letra, letraCuota, DocumentosBean.LETRA_ABONO);
            PrintServices.getInstance().limpiarListaDocumentos();
            BonosServices.crearBonosPagos(ticket.getPagos(), letra.getIdFactura(), BonosServices.LLAMADO_DESDE_LETRA, letra.getCliente());

            com.comerzzia.jpos.util.db.Connection connection = new com.comerzzia.jpos.util.db.Connection();
            connection.abrirConexion(Database.getConnection());
            ServicioContadoresCaja.incrementarContadorAbonoLetra(connection);

        } catch (PagoInvalidException e) {
            em.getTransaction().rollback();
            String msg = "Error registrando pagos de la cuota de letra de crédito. ";
            log.error("realizarPagoCuota() - " + msg, e);
            throw new LetraCambioException(msg, e);
        } catch (Exception e) {
            em.getTransaction().rollback();
            String msg = "Error registrando pagos de la cuota de letra de crédito. ";
            log.error("realizarPagoCuota() - " + msg, e);
            throw new LetraCambioException(msg, e);
        } finally {
            em.close();
        }
    }

    public static void anularLetra(com.comerzzia.jpos.util.db.Connection conn, String uidLetra) throws LetraCambioException {
        SqlSession session = new SqlSession();
        try {
            log.debug("anularLetra() - Anulando letra con uid: " + uidLetra);
            // instanciamos la nueva letra
            LetraBean letra = new LetraBean();
            letra.setProcesado(false);
            letra.setEstado(LetraBean.ESTADO_ANULADA);
            letra.setUidLetra(uidLetra);

            session.openSession(SessionFactory.openSession(conn));
            LetraMapper mapper = session.getMapper(LetraMapper.class);
            mapper.updateByPrimaryKeySelective(letra);
        } catch (PersistenceException e) {
            String msg = "Error anulando letra en base de datos: " + uidLetra + " -" + e.getMessage();
            log.error("anularLetra() - " + msg, e);
            throw new LetraCambioException(msg, e);
        } catch (Exception e) {
            String msg = "Error inesperado anulando letra en base de datos: " + uidLetra + " -" + e.getMessage();
            log.error("anularLetra() - " + msg, e);
            throw new LetraCambioException(msg, e);
        }
    }

    public static LetraCuotaBean consultarLetraCuota(SqlSession sql, String codAlmacen, String codCaja, Long idAbono) throws LetraCambioException {
        try {
            log.debug("consultarLetraCuota() - Consultando Cuota de letra con codAlm,codCaja,idAbono: " + codAlmacen + "," + codCaja + "," + String.valueOf(idAbono));

            LetraCuotaMapper mapper = sql.getMapper(LetraCuotaMapper.class);
            LetraCuotaExample example = new LetraCuotaExample();
            example.or().andCodCajaAbonoEqualTo(codCaja).andIdAbonoEqualTo(idAbono);
            return mapper.selectByExampleWithBLOBs(example).get(0);
        } catch (NoResultException e) {
            throw new LetraCambioException("La cuota de letra indicada no existe.");
        } catch (Exception e) {
            String msg = "Error consultando cuota de letras. ";
            log.error("consultarLetraCuota() - " + msg, e);
            throw new LetraCambioException(msg, e);
        }
    }

    public static void actualizarEstadoLetra(SqlSession session, String uidLetra, String estado) throws LetraCambioException {
        try {
            LetraBean letraUpdate = new LetraBean();
            letraUpdate.setEstado(estado);
            letraUpdate.setUidLetra(uidLetra);
            letraUpdate.setProcesado(false);
            LetraMapper mapper = session.getMapper(LetraMapper.class);
            mapper.updateByPrimaryKeySelective(letraUpdate);
        } catch (Exception e) {
            String msg = "Error actualizando estado de letra. ";
            log.error("actualizarEstadoLetra() - " + msg, e);
            throw new LetraCambioException(msg, e);
        }
    }

    public static void actualizaLetraCuota(SqlSession sql, LetraCuotaBean letraCuota) throws LetraCambioException {
        try {
            log.debug("actualizarLetraCuota() - Actualizando cuota de letra con uid: " + letraCuota.getUidLetra());
            LetraCuotaMapper mapper = sql.getMapper(LetraCuotaMapper.class);
            mapper.updateByPrimaryKeyWithBLOBs(letraCuota);
        } catch (Exception e) {
            String msg = "Error actualizando letra de cuota. ";
            log.error("actualizaLetraCuota() - " + msg, e);
            throw new LetraCambioException(msg, e);
        }
    }
}
