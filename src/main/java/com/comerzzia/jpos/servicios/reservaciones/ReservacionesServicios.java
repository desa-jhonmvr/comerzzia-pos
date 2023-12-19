/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.comerzzia.jpos.servicios.reservaciones;

import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.UsuarioDTO;
import com.comerzzia.jpos.dto.ventas.DocumentoDTO;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.ReservaProcesar;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.entity.gui.reservaciones.ArticuloReservado;
import com.comerzzia.jpos.entity.services.reservaciones.Reservacion;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaBean;
import com.comerzzia.jpos.persistencia.logs.logskdx.LogKardexBean;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaExample;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaMapper;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoExample;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoMapper;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloExample;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloMapper;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketExample;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketMapper;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoExample;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoMapper;
import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposExample;
import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposMapper;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.articulos.ArticuloNotFoundException;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.articulos.tarifas.TarifaArticuloNotFoundException;
import com.comerzzia.jpos.servicios.articulos.tarifas.TarifasServices;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.clientes.ClientesServices;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.listapda.ListaPDAServices;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.pagos.PagoInvalidException;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.print.documentos.DocumentoException;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import com.comerzzia.jpos.servicios.stock.StockException;
import com.comerzzia.jpos.servicios.tickets.ImporteInvalidoException;
import com.comerzzia.jpos.servicios.tickets.ReferenciaTicket;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosAdicionalesLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.util.EnumTipoDocumento;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.thread.ProcesoEncolarThread;
import com.comerzzia.jpos.util.UtilUsuario;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.session.SqlSession;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author SMLM
 */
public class ReservacionesServicios {
    private static Logger log = Logger.getMLogger(ReservacionesServicios.class);
    private static ClientesServices clientesService = new ClientesServices();    
    
    public static List<ReservaBean> consultar(ParamBuscarReservaciones p, Date fechaDesde, Date fechaHasta) throws ReservaNotFoundException, ReservasException {                
        SqlSession sql = new SqlSession();
        try {        
            //Para hacer abonos a reservaciones desde otro local, si el código de local no es el nuestro, buscamos la reservacion en central
            if(p.getTiendaReservacion().equals(Sesion.getTienda().getCodalm()) || p.getTiendaReservacion().isEmpty()){
                sql.openSession(SessionFactory.openSession());
            } else {
                Connection conn = new Connection();
                conn.abrirConexion(Database.getConnectionCentral());
                sql.openSession(SessionFactory.openSession(conn));
            }

            ReservaMapper mapper = sql.getMapper(ReservaMapper.class);
            ReservaExample example = new ReservaExample();
            ReservaTiposMapper mapperTipo = sql.getMapper(ReservaTiposMapper.class);
            ReservaTiposExample exampleTipo = new ReservaTiposExample();        
            List<ReservaBean> reservaciones = new ArrayList<ReservaBean>();

            if (!p.getNumeroFidelizado().isEmpty() || !p.getDocumento().isEmpty()) {
                Cliente cliente = null;
                if (!p.getNumeroFidelizado().isEmpty()) {
                    cliente = clientesService.consultaClienteNAfil(p.getNumeroFidelizado());
                }
                else if (!p.getDocumento().isEmpty()) {
                    cliente = clientesService.consultaClienteDoc(p.getDocumento(), p.getTipoDocumento());
                }
                example.or().andCodcliEqualToWithoutNull(cliente).andCodTipoEqualToWithoutNull(p.getTipoReserva()).andEstadoEqualTo(p.getEstado()).andCodAlmEqualToWithoutNull(p.getTiendaReservacion());
                example.setOrderByClause("FECHA_ALTA DESC");
                reservaciones = mapper.selectByExample(example);
            }
            else {
                Fecha fHasta = new Fecha(fechaHasta);
                if (fechaHasta != null){
                    fHasta.sumaDias(1);
                }
                if(p.getNumeroReserva()!=null){
                    example.or().andCodReservacionEqualTo(new BigDecimal(p.getNumeroReserva())).andCodTipoEqualToWithoutNull(p.getTipoReserva()).andEstadoEqualTo(p.getEstado()).andCodAlmEqualToWithoutNull(p.getTiendaReservacion()).
                            andFechaAltaBetweenWithoutNull(new Fecha(fechaDesde), fHasta).andNombreLikeToWithouthNull(p.getNombre()).andApellidosLikeToWithouthNull(p.getApellidos()).
                            andNombreOrgLikeToWithouthNull(p.getNombreOrg()).andApellidosOrgLikeToWithouthNull(p.getApellidosOrg());
                }else{
                    example.or().andCodTipoEqualToWithoutNull(p.getTipoReserva()).andEstadoEqualTo(p.getEstado()).andCodAlmEqualToWithoutNull(p.getTiendaReservacion()).
                            andFechaAltaBetweenWithoutNull(new Fecha(fechaDesde), fHasta).andNombreLikeToWithouthNull(p.getNombre()).andApellidosLikeToWithouthNull(p.getApellidos()).
                            andNombreOrgLikeToWithouthNull(p.getNombreOrg()).andApellidosOrgLikeToWithouthNull(p.getApellidosOrg());
                }
                example.setOrderByClause("FECHA_ALTA DESC");
                reservaciones =  mapper.selectByExample(example);
            }
            for(ReservaBean reservacion:reservaciones) {
                obtenerAbonosyArticulosReservacion(reservacion, sql);
            }
            return reservaciones;
        }
        catch (NoResultException e) {
            log.debug("El cliente por el que intenta consultar no existe.");
            throw new ReservaNotFoundException("El cliente por el que intenta consultar no existe.", e);
        }
        catch (ClienteException e) {
            log.debug("Se ha producido un error intentando consultar los clientes de las reservaciones.", e);
            throw new ReservaNotFoundException(e.getMessage());
        }
        catch (Exception e) {
            log.error("Se ha producido un error intentando consultar reservaciones.", e);
            throw new ReservasException("Se ha producido un error intentando consultar reservaciones.", e);
        }
        finally {
            sql.close();
        }
    }
    
    public static ReservaBean consultaById(BigInteger idReservacion) throws ReservaNotFoundException, ReservasException {
        SqlSession sql = new SqlSession(); 
        try {
            sql.openSession(SessionFactory.openSession());
            ReservaMapper mapper = sql.getMapper(ReservaMapper.class);
            ReservaExample example = new ReservaExample();
            
            example.or().andCodReservacionEqualTo(new BigDecimal(idReservacion));
            ReservaBean reserva = mapper.selectByExample(example).get(0);
            obtenerCamposReserva(reserva,sql);
            
            return reserva;
        }
        catch (NoResultException e) {
            log.debug("consultaById() - El código de reserva indicado no existe.");
            throw new ReservaNotFoundException("El código de reserva indicado no existe.");
        }
        catch (java.lang.IndexOutOfBoundsException e) {
            log.debug("consultaById() - El código de reserva indicado no existe.");
            throw new ReservaNotFoundException("El código de reserva indicado no existe.");
        }        
        catch (Exception e) {
            log.error("consultaById() - Se ha producido un error intentando consultar la reservación con el código indicado.", e);
            throw new ReservasException("Se ha producido un error intentando consultar la reservación con el código indicado.", e);
        } 
        finally {
            sql.close();
        }        
    }
    
    public static ReservaBean consultarReservaByUid(String uidReservacion) {
        SqlSession sql = new SqlSession();         
        try {
            sql.openSession(SessionFactory.openSession());   
            ReservaMapper mapper = sql.getMapper(ReservaMapper.class);
            ReservaExample example = new ReservaExample();
            
            example.or().andUidReservacionEqualTo(uidReservacion);
            ReservaBean reserva = mapper.selectByExample(example).get(0);
            // Como unicamente lo llamamos desde imprimirComprobanteBono no hace falta sacar el resto de campos            
            //obtenerCamposReserva(reserva,sql);
            
            return reserva;            
        }
        catch (Exception e) {
            String msg = "Error inesperado consultando reservacion.";
            log.error(msg, e);
            return null;
        }
        finally {
            sql.close();
        }        
    }
    
    public static List<ReservaTiposBean> consultarTipos() throws ReservasException {
        SqlSession sql = new SqlSession();         
        try {
            sql.openSession(SessionFactory.openSession());    
            ReservaTiposMapper mapper = sql.getMapper(ReservaTiposMapper.class);
            ReservaTiposExample example = new ReservaTiposExample();
            return mapper.selectByExample(example);
        }
        catch (NoResultException e) {
            String msg = "No se han definido los tipos de reservación.";
            log.error(msg, e);
            throw new ReservasException(msg, e);
        }
        catch (Exception e) {
            String msg = "Error inesperado consultando tipos de reservaciones.";
            log.error(msg, e);
            throw new ReservasException(msg, e);
        }
        finally {
            sql.close();
        }
    }  
    
    public static void crearInvitado(ReservaInvitadoBean invitado) throws ReservasException {
        SqlSession sql = new SqlSession(); 
        try {
            sql.openSession(SessionFactory.openSession());  
            ReservaInvitadoMapper mapper = sql.getMapper(ReservaInvitadoMapper.class);
            mapper.insert(invitado);
            sql.commit();
        }
        catch (Exception ex) {
            String msg = "Error inesperado creando invitados.";
            log.error(msg, ex);
            sql.rollback();
            throw new ReservasException(msg, ex);
        }
        finally {
            sql.close();
        }
    }
    
    public static void addArticulos(ReservaBean res, List<ReservaArticuloBean> listArt, SesionPdaBean sesionPda) throws ReservasException {
        SqlSession sql = new SqlSession(); 
        try {
            sql.openSession(SessionFactory.openSession());  
            ReservaArticuloMapper mapper = sql.getMapper(ReservaArticuloMapper.class);
            for(ReservaArticuloBean reservaArticulo: listArt) {
                mapper.insert(reservaArticulo);
                res.getReservaArticuloList().add(reservaArticulo);
            }
            res.setProcesadoTienda(false);
            modificarOnlyReservaWithSql(sql,res);
            if(sesionPda!=null){
                ListaPDAServices.marcarComoUtilizado(sql, sesionPda);
            }
            sql.commit();
        }
        catch (Exception ex) {
            String msg = "Error inesperado creando articulos.";
            log.error(msg, ex);
            sql.rollback();
            throw new ReservasException(msg, ex);
        }
        finally {
            sql.close();
        }        
    }
    
    public static void crearReserva(ReservaBean res, TicketS ticket) throws ReservasException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        SqlSession sql = new SqlSession();
        try {   
            em.getTransaction().begin();
            Connection conn = Connection.getConnection(em);
            
            sql.openSession(SessionFactory.openSession(conn));
            
            ReservaMapper mapper = sql.getMapper(ReservaMapper.class);
            
            mapper.insert(res);
            ReservaArticuloMapper mapperArticulo = sql.getMapper(ReservaArticuloMapper.class);       
            ReservaInvitadoMapper mapperInvitado = sql.getMapper(ReservaInvitadoMapper.class);
            ReservaAbonoMapper mapperAbono = sql.getMapper(ReservaAbonoMapper.class); 
            FacturacionTicketMapper mapperFacturacion = sql.getMapper(FacturacionTicketMapper.class);
            
            for(ReservaArticuloBean reservaArticulo:res.getReservaArticuloList()){
                mapperArticulo.insert(reservaArticulo);
            }
            for(ReservaInvitadoBean reservaInvitado:res.getReservaInvitadoList()){
                mapperInvitado.insert(reservaInvitado);
            }
            for(ReservaAbonoBean reservaAbono:res.getReservaAbonoList()){
                mapperAbono.insert(reservaAbono);
            }
            if(res.getDatosFacturacion()!=null) {
                mapperFacturacion.insert(res.getDatosFacturacion());       
            }

            //Si hay pagos, es que la reservación tiene un abono
            if (ticket.getPagos()!= null){
                ReservaAbonoBean abono = res.getReservaAbonoList().get(0);
                ReferenciaTicket referencia = ReferenciaTicket.getReferenciaAbonoReservacion(res, abono);
                String documento = res.getCodalm() + "-" + abono.getCodcaja() + "-"+ String.format("%09d", res.getCodReservacion().intValue());
                TicketService.procesarMediosPagos(em, ticket.getPagos().getPagos(), referencia,abono.getIdAbono() ,"RES", null);
            }
            
            if (ticket.getReferenciaSesionPDA() !=null){
                ListaPDAServices.marcarComoUtilizado(sql, ticket.getReferenciaSesionPDA());
            }
            em.getTransaction().commit();
            //G.S. Actualiza el kardex en el ERP
            ReservacionesServicios.generarKardexReserva(res, EnumTipoDocumento.RESERVA);

        }
        catch(Exception ex){
            String msg = "Error inesperado creando reservación.";
            log.error(msg, ex);
            em.getTransaction().rollback();
            throw new ReservasException(msg, ex);            
        }
        finally {
            em.close();
        }
    }    
    
    public static void crearAbono(ReservaBean reserva, ReservaAbonoBean reservaAbono, EntityManager em) throws ReservasException {
        SqlSession sql = new SqlSession(); 
        try {
            Connection conn = Connection.getConnection(em);
            sql.openSession(SessionFactory.openSession(conn));                
            ReservaAbonoMapper mapper = sql.getMapper(ReservaAbonoMapper.class);
            mapper.insert(reservaAbono);
            
            if(reserva.getCodalm().equals(Sesion.getTienda().getCodalm())){
                reserva.setProcesadoTienda(false);
                modificarOnlyReservaWithSql(sql,reserva);
            }
        }
        catch (Exception ex) {
            String msg = "Error inesperado creando abono.";
            log.error(msg, ex);
            throw new ReservasException(msg, ex);
        }     
    }
    
    public static void actualizarArticulo(ReservaArticuloBean articulo) throws ReservasException {
        SqlSession sql = new SqlSession(); 
        try {
            sql.openSession(SessionFactory.openSession());  
            ReservaArticuloMapper mapper = sql.getMapper(ReservaArticuloMapper.class);
            mapper.updateByPrimaryKey(articulo);
            sql.commit();
        }
        catch (Exception ex) {
            String msg = "Error inesperado actualizando artículos.";
            log.error(msg, ex);
            sql.rollback();
            throw new ReservasException(msg, ex);
        }
        finally {
            sql.close();
        }        
    }  
    
    public static void actualizarAbonoWithSql(ReservaAbonoBean abono, SqlSession sql) throws ReservasException {
        try {
            ReservaAbonoMapper mapper = sql.getMapper(ReservaAbonoMapper.class);
            mapper.updateByPrimaryKey(abono);
        }
        catch (Exception ex) {
            String msg = "Error inesperado actualizando abono.";
            log.error(msg, ex);
            throw new ReservasException(msg, ex);
        }      
    }
    
    public static void actualizarArticuloWithSql(SqlSession sql, ReservaArticuloBean articulo) throws ReservasException {
        try {
            ReservaArticuloMapper mapper = sql.getMapper(ReservaArticuloMapper.class);
            mapper.updateByPrimaryKey(articulo);
        }
        catch (Exception ex) {
            String msg = "Error inesperado actualizando artículos.";
            log.error(msg, ex);
            throw new ReservasException(msg, ex);
        }     
    }      
    
    public static Long consultaSiguienteIdInvitado(String uidReservacion) throws ReservasException {
        SqlSession sql = new SqlSession(); 
        try {
            sql.openSession(SessionFactory.openSession());  
            ReservaInvitadoMapper mapper = sql.getMapper(ReservaInvitadoMapper.class);
            ReservaInvitadoExample example = new ReservaInvitadoExample();
            example.or().andUidReservacionEqualTo(uidReservacion);
            Long id = mapper.consultarSiguienteIdInvitado(uidReservacion);
            if(id==null) {
                return new Long(0);
            }
            return id+1;
        }
        catch (Exception ex) {
            log.error("Excepción consultando el contador de Invitados ", ex);
            throw new ReservasException();
        }
        finally {
            sql.close();
        }  
    }
    
    public static Long consultaSiguienteIdLineaArticulo(String uidReservacion) throws ReservasException {
        SqlSession sql = new SqlSession(); 
        try {
            sql.openSession(SessionFactory.openSession());  
            ReservaArticuloMapper mapper = sql.getMapper(ReservaArticuloMapper.class);
            ReservaArticuloExample example = new ReservaArticuloExample();
            example.or().andUidReservacionEqualTo(uidReservacion);
            Long id = mapper.consultarSiguienteIdArticulo(uidReservacion);
            if(id==null) {
                return new Long(0);
            }
            return id+1;
        }
        catch (Exception ex) {
            log.error("Excepción consultando el contador de Artículos. ", ex);
            throw new ReservasException();
        }
        finally {
            sql.close();
        }  
    }
    
    public static Long consultaSiguienteIdAbono(String uidReservacion, boolean central) throws ReservasException {
        SqlSession sql = new SqlSession(); 
        try {
            if(central){
                sql.openSession(SessionFactory.openSession());  
            } else {
                Connection conn = new Connection();
                conn.abrirConexion(Database.getConnectionCentral());
                sql.openSession(SessionFactory.openSession(conn));                    
            }
            ReservaAbonoMapper mapper = sql.getMapper(ReservaAbonoMapper.class);
            ReservaAbonoExample example = new ReservaAbonoExample();
            example.or().andUidReservacionEqualTo(uidReservacion);
            Long id = mapper.consultarSiguienteIdAbono(uidReservacion);
            if(id==null) {
                return new Long(0);
            }
            return id+1;
        }
        catch (Exception ex) {
            log.error("Excepción consultando el contador de Invitados ", ex);
            throw new ReservasException();
        }
        finally {
            sql.close();
        }  
    }    
    public static void modificarReserva(ReservaBean reservacion) throws ReservasException {
        SqlSession sql = new SqlSession(); 
        try {
            sql.openSession(SessionFactory.openSession());  
            modificarReserva(reservacion, sql);
            sql.commit();
        }
        catch (Exception ex) {
            String msg = "Error inesperado modificando reserva.";
            log.error(msg, ex);
            sql.rollback();
            throw new ReservasException(msg, ex);
        }
        finally {
            sql.close();
        }
    }
    
    public static TicketS liquidarReserva(Reservacion reservacion, List<ReservaArticuloBean> listaArticulosComprados) throws ReservasException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        SqlSession sql = new SqlSession();
        TicketS facturaArticulosReserva;
        try {
            em.getTransaction().begin();
            sql.openSession(SessionFactory.openSession(Connection.getConnection(em)));  
            modificarReserva(reservacion.getReservacion(), sql);
                    // Actualizamos los Stocks
            if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK) || Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_KARDEX_POS)){
                for (ReservaArticuloBean ra : reservacion.getReservacion().getReservaArticuloList()) {
                    try {
                        LogKardexBean logKardex = new LogKardexBean();
                        logKardex.setTipoAccion(LogKardexBean.tipoAccionReservacion);
                        logKardex.setFactura(String.valueOf(reservacion.getReservacion().getCodReservacion()));
                        logKardex.setUsuarioAutorizacion(Sesion.getUsuario().getUsuario());      
                        if (listaArticulosComprados.contains(ra)) {                  
                            log.debug("liquidarReserva() - Disminuyendo stock de Artículo con ID:" + ra.getIdItem() + " y marca." + ra.getCodMarca());
                            ServicioStock.disminuyeStockReserva(ra.getCodMarca(), ra.getIdItem(), 1, logKardex);
                            ServicioStock.actualizaKardex(ra.getCodart(), ServicioStock.MOVIMIENTO_52, Sesion.getTienda().getCodalm(), 1L);
                            log.debug("liquidarReserva() - Aumentando stock de Ventas");
                            ServicioStock.aumentaStockVenta(ra.getCodMarca(), ra.getIdItem(), 1, logKardex);
                            ServicioStock.actualizaKardex(ra.getCodart(), ServicioStock.MOVIMIENTO_51, reservacion.getTicket().getTienda(), -1L);
                        }
                        else if (!ra.getComprado()) {
                            log.debug("liquidarReserva() - Disminuyendo Stock de Reserva:" + ra.getIdItem() + " " + ra.getCodMarca());
                            ServicioStock.disminuyeStockReserva(ra.getCodMarca(), ra.getIdItem(), 1, logKardex);
                            ServicioStock.actualizaKardex(ra.getCodart(), ServicioStock.MOVIMIENTO_52, Sesion.getTienda().getCodalm(), 1L);
                        }
                    }
                    catch (StockException e) {
                        log.error("liquidarReserva() - STOCK: No fué posible modificar el stock reservado para el artículo");
                    }
                }
            }          
            PrintServices.getInstance().limpiarListaDocumentos();
            facturaArticulosReserva = ReservacionesServicios.facturaArticulosReserva(reservacion, false, em);
            try {
                DocumentosService.crearDocumento(facturaArticulosReserva, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.FACTURA);
            } catch (DocumentoException ex) {
                log.error("liquidarReserva() - No se pudo almacenar el documento en la BBDD");
            }
            PrintServices.getInstance().limpiarListaDocumentos();
            log.debug("liquidarReserva() - Finalizada impresión del ticket");            
            
            em.getTransaction().commit();
            generarKardexReserva(reservacion.getReservacion(), EnumTipoDocumento.RESERVA_LIQUIDADA);
        }
        catch (Exception ex) {
            em.getTransaction().rollback();
            String msg = "Error inesperado modificando reserva.";
            log.error(msg, ex);
            throw new ReservasException(msg, ex);
        }
        finally {
            em.close();
        }   
        return facturaArticulosReserva;
    }
    
    public static void modificarReserva(ReservaBean reservacion, SqlSession sql) throws ReservasException {
        try {
            ReservaMapper mapper = sql.getMapper(ReservaMapper.class);
            
            ReservaArticuloMapper mapperArticulo = sql.getMapper(ReservaArticuloMapper.class);       
            ReservaInvitadoMapper mapperInvitado = sql.getMapper(ReservaInvitadoMapper.class);
            ReservaAbonoMapper mapperAbono = sql.getMapper(ReservaAbonoMapper.class); 
            FacturacionTicketMapper mapperFacturacion = sql.getMapper(FacturacionTicketMapper.class);
            
            for(ReservaArticuloBean reservaArticulo:reservacion.getReservaArticuloList()){
                mapperArticulo.updateByPrimaryKey(reservaArticulo);
            }
            for(ReservaInvitadoBean reservaInvitado:reservacion.getReservaInvitadoList()){
                mapperInvitado.updateByPrimaryKey(reservaInvitado);
            }
            for(ReservaAbonoBean reservaAbono:reservacion.getReservaAbonoList()){
                mapperAbono.updateByPrimaryKey(reservaAbono);
            }
            if(reservacion.getDatosFacturacion()!=null){
                mapperFacturacion.updateByPrimaryKey(reservacion.getDatosFacturacion()); 
            }
            
            mapper.updateByPrimaryKey(reservacion);
            if(reservacion.isLiquidado()){
            //Actualizamos el documento asociado
                DocumentosBean docu = DocumentosService.consultarDocByUniqueKey(DocumentosBean.RESERVACION, reservacion.getCodalm(), DocumentosBean.CODCAJA, reservacion.getCodReservacion().toString());
                docu.setEstado("L");
                DocumentosService.updateDocumentosWithSql(docu, sql);                
            }  
            if(reservacion.isCancelada()){
            //Actualizamos el documento asociado
                DocumentosBean docu = DocumentosService.consultarDocByUniqueKey(DocumentosBean.RESERVACION, reservacion.getCodalm(), DocumentosBean.CODCAJA, reservacion.getCodReservacion().toString());
                if (docu != null) {
                    docu.setEstado("A");
                    DocumentosService.updateDocumentosWithSql(docu, sql);
                }
            }                  
        }
        catch (Exception ex) {
            String msg = "Error inesperado modificando reserva.";
            log.error(msg, ex);
            throw new ReservasException(msg, ex);
        }
    }  
    
    public static void modificarOnlyReservaWithSql(SqlSession sql, ReservaBean reservacion) throws ReservasException {
        try {
            ReservaMapper mapper = sql.getMapper(ReservaMapper.class);
            
            mapper.updateByPrimaryKey(reservacion);
            
        }
        catch (Exception ex) {
            String msg = "Error inesperado modificando reserva.";
            log.error(msg, ex);
            throw new ReservasException(msg, ex);
        }
    }       
    
    
    public static void eliminarReservaArticulos(ReservaBean reserva, List<ReservaArticuloBean> listArt) {
        SqlSession sql = new SqlSession(); 
        try {
            sql.openSession(SessionFactory.openSession());  
            ReservaArticuloMapper mapper = sql.getMapper(ReservaArticuloMapper.class);
            ReservaArticuloExample example = new ReservaArticuloExample();
            for(ReservaArticuloBean reservaArticulo:listArt){
                example.or().andUidReservacionEqualTo(reservaArticulo.getUidReservacion()).andIdLineaEqualTo(reservaArticulo.getIdLinea());
                mapper.deleteByExample(example);
            }
            reserva.setProcesadoTienda(false);
            modificarOnlyReservaWithSql(sql,reserva);
            sql.commit();
        }
        catch (Exception ex) {
            String msg = "Error inesperado eliminando articulos de reserva.";
            log.error(msg, ex);
            sql.rollback();
        }
        finally {
            sql.close();
        }          
    } 
    
    public static TicketS facturaArticulosReserva(Reservacion reservacion) throws ReservasException {
        return facturaArticulosReserva(reservacion, false);
    }

    public static TicketS facturaArticulosReserva(Reservacion reservacion, boolean hayInitadosArticulo) throws ReservasException {
        try {
            ArticulosServices servicioArticulos = ArticulosServices.getInstance();
            TarifasServices tarifasServices = new TarifasServices();
            TicketS factura = new TicketS(Sesion.getCajaActual().getCajaActual().getUidDiarioCaja(), Sesion.getCajaActual().getCajaParcialActual().getUidCajeroCaja(), false);
            factura.setCliente(reservacion.getReservacion().getCliente());
            factura.setTienda(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            factura.setCodcaja(Sesion.getDatosConfiguracion().getCodcaja());
            factura.setCajero(Sesion.getUsuario());
            factura.setFecha(new Fecha());
            factura.setId_ticket(ServicioContadoresCaja.obtenerContadorFactura());
            //factura.setFacturacionCliente(); // Antes la facturación se ponia a cargo del cliente            
            // Añadimos al ticket los datos de facturacion de la reserva
            FacturacionTicketBean ft2 = reservacion.getReservacion().getDatosFacturacion();
            if (ft2 == null) {
                factura.setFacturacionCliente();
            }
            else {
                factura.setFacturacion(ft2);
            }

            // creamos una línea por artículo
            LineasTicket lineas = new LineasTicket();
            BigDecimal totalAPagar = BigDecimal.ZERO;
            for (ReservaArticuloBean articulo : reservacion.getReservacion().getReservaArticuloList()) {
                if (articulo.getComprado()) {
                    Tarifas tarifa = tarifasServices.getTarifaArticulo(articulo.getCodart());
                    Articulos articuloBean = servicioArticulos.getArticuloCB(articulo.getCodbarras());
                    //Se Agrega el campo costo landed RD
                    LineaTicket linea = new LineaTicket(
                            articulo.getCodbarras(), 
                            articuloBean, 
                            1, 
                            tarifa.getPrecioVenta(), 
//                 tarifa.getPrecioTotal(),tarifa.getCostoLanded());
                    tarifa.getPrecioTotal(),tarifa.getPrecioCosto(), articuloBean.getCodcategoria(), tarifa.getPrecioReal());
                    
                    if (articulo.getDescuento() != null){
                        DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
                        datosAdicionales.setEnvioDomicilio(false);
                        datosAdicionales.setDescuento(articulo.getDescuento());
                        linea.setDatosAdicionales(datosAdicionales);
                        linea.establecerDescuento(datosAdicionales.getDescuento());
                    }
                    lineas.getLineas().add(linea);
                    totalAPagar = totalAPagar.add(linea.getPrecioTotal());
                }
            }
            factura.setLineas(lineas);
            factura.inicializaTotales(totalAPagar);
            factura.inicializaPagos();
            if (reservacion.getTotalAbonado().compareTo(BigDecimal.ZERO) > 0) {
                Pago p = new Pago(factura.getPagos(), null);
                p.setPagoActivo(Pago.PAGO_OTROS);
                p.setMedioPagoActivo(MedioPagoBean.getMedioPagoAbonoReservacion());
               
                BigDecimal totalMedioPagoAbonos = totalAPagar;
                BigDecimal dctoAbonos = Numero.getTantoPorCientoMenosR(reservacion.getTotalAbonado(), reservacion.getAbonosReales());
                BigDecimal ustedPagaMedioPagoAbonos = Numero.menosPorcentajeR(totalAPagar, dctoAbonos);

                if (reservacion.getDatosAbono() != null && reservacion.getDatosAbono().isEsultimoAbono()) {
                    totalMedioPagoAbonos = reservacion.getTotalAbonado();
                    ustedPagaMedioPagoAbonos = reservacion.getAbonosReales().subtract(reservacion.getDatosAbono().getUltimoAbono().getCantAbonoSinDto());
                    dctoAbonos = Numero.getTantoPorCientoMenosR(totalMedioPagoAbonos, ustedPagaMedioPagoAbonos);
                }
                p.setTotal(totalMedioPagoAbonos);
                p.establecerDescuento(dctoAbonos);
                p.setUstedPaga(ustedPagaMedioPagoAbonos);
                p.establecerEntregado(ustedPagaMedioPagoAbonos.toString());
                
                factura.crearNuevaLineaPago(p);
            }

            if (reservacion.getDatosAbono() != null && reservacion.getDatosAbono().isEsultimoAbono()) {
                for (Pago pagoFinal : reservacion.getDatosAbono().getUltimosPagos().getPagos()) {
                    factura.crearNuevaLineaPago(pagoFinal);
                }
            }
            factura.getTotales().recalcularTotalesLineas(lineas);
            factura.recalcularFinalPagado();

            if (Sesion.getTicket() != null) {
                Sesion.getTicket().setId_ticket(ServicioContadoresCaja.obtenerContadorFactura());
            }

            TicketService.escribirTicket(factura, false,true);
            //PrintServices.getInstance().imprimirTicket(factura, false, PrintServices.IMPRESORA_PANTALLA);
            PrintServices.getInstance().imprimirTicket(factura);


            return factura;
        }
        catch (ImporteInvalidoException ex) {
            log.error("Error facturando el Artículo. Excepción en el ticket", ex);
            throw new ReservasException(ex.getMessage(), ex);
        }
        catch (TicketException ex) {
            log.error("Error facturando el Artículo. Excepción en el ticket", ex);
            throw new ReservasException(ex.getMessage(), ex);
        }
        catch (ArticuloNotFoundException ex) {
            log.error("Error facturando el Artículo. Artículo no encontrado", ex);
            throw new ReservasException(ex.getMessage(), ex);
        }
        catch (TarifaArticuloNotFoundException ex) {
            log.error("Error facturando el Artículo. Tarifa no encontrada", ex);
            throw new ReservasException(ex.getMessage(), ex);
        }
        catch (PagoInvalidException ex) {
            log.error("Error facturando el Artículo. Pago Invalido", ex);
            throw new ReservasException(ex.getMessage(), ex);
        }
        catch (Exception ex) {
            log.error("Error facturando el Artículo", ex);
            throw new ReservasException("Error facturando el Artículo", ex);
        }

    }
    
    public static TicketS facturaArticulosReserva(Reservacion reservacion, boolean hayInitadosArticulo, EntityManager em) throws ReservasException {
        try {
            ArticulosServices servicioArticulos = ArticulosServices.getInstance();
            TarifasServices tarifasServices = new TarifasServices();
            TicketS factura = new TicketS(Sesion.getCajaActual().getCajaActual().getUidDiarioCaja(), Sesion.getCajaActual().getCajaParcialActual().getUidCajeroCaja(), false);
            factura.setCliente(reservacion.getReservacion().getCliente());
            factura.setTienda(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            factura.setCodcaja(Sesion.getDatosConfiguracion().getCodcaja());
            factura.setCajero(Sesion.getUsuario());
            factura.setFecha(new Fecha());
            factura.setId_ticket(ServicioContadoresCaja.obtenerContadorFactura());
            //factura.setFacturacionCliente(); // Antes la facturación se ponia a cargo del cliente            
            // Añadimos al ticket los datos de facturacion de la reserva
            FacturacionTicketBean ft2 = reservacion.getReservacion().getDatosFacturacion();
            if (ft2 == null) {
                factura.setFacturacionCliente();
            }
            else {
                factura.setFacturacion(ft2);
            }

            // creamos una línea por artículo
            LineasTicket lineas = new LineasTicket();
            BigDecimal totalAPagar = BigDecimal.ZERO;
            for (ReservaArticuloBean articulo : reservacion.getReservacion().getReservaArticuloList()) {
                if (articulo.getComprado()) {
                    Tarifas tarifa = tarifasServices.getTarifaArticulo(articulo.getCodart());
                    Articulos articuloBean = servicioArticulos.getArticuloCB(articulo.getCodbarras());
                    //Se Agrega el campo costo landed 
                    LineaTicket linea = new LineaTicket(
                            articulo.getCodbarras(), 
                            articuloBean, 
                            1, 
                            tarifa.getPrecioVenta(), 
                            tarifa.getPrecioTotal(),tarifa.getPrecioCosto(), articuloBean.getCodcategoria(), tarifa.getPrecioReal());
//                            tarifa.getPrecioTotal(),tarifa.getCostoLanded());
                    
                    if (articulo.getDescuento() != null){
                        DatosAdicionalesLineaTicket datosAdicionales = new DatosAdicionalesLineaTicket();
//                        datosAdicionales.setEnvioDomicilio(false);
                        datosAdicionales.setDescuento(articulo.getDescuento());
                        if (reservacion.getTicket() != null && reservacion.getTicket().getLineas() != null) {
                            for (LineaTicket ticket : reservacion.getTicket().getLineas().getLineas()) {
                                if (ticket.getDatosAdicionales() != null && ticket.getArticulo().getCodart().equals(articulo.getCodart())) {
                                    datosAdicionales.setEnvioDomicilio(ticket.getDatosAdicionales().isEnvioDomicilio());
                                    datosAdicionales.setRecogidaPosterior(ticket.getDatosAdicionales().isRecogidaPosterior());
                                }
                            }
                        } else {
                            datosAdicionales.setEnvioDomicilio(false);
                        }
                        linea.setDatosAdicionales(datosAdicionales);
                        linea.establecerDescuento(datosAdicionales.getDescuento());
                    }
                    lineas.getLineas().add(linea);
                    totalAPagar = totalAPagar.add(linea.getPrecioTotal());
                }
            }
            factura.setLineas(lineas);
            factura.inicializaTotales(totalAPagar);
            factura.inicializaPagos();
            if (reservacion.getTotalAbonado().compareTo(BigDecimal.ZERO) > 0) {
                Pago p = new Pago(factura.getPagos(), null);
                p.setPagoActivo(Pago.PAGO_OTROS);
                p.setMedioPagoActivo(MedioPagoBean.getMedioPagoAbonoReservacion());
               
                BigDecimal totalMedioPagoAbonos = totalAPagar;
                BigDecimal dctoAbonos = Numero.getTantoPorCientoMenos(reservacion.getTotalAbonado(), reservacion.getAbonosReales());
                BigDecimal ustedPagaMedioPagoAbonos = Numero.menosPorcentajeR(totalAPagar, dctoAbonos);

                if (reservacion.getDatosAbono() != null && reservacion.getDatosAbono().isEsultimoAbono()) {
                    totalMedioPagoAbonos = reservacion.getTotalAbonado();
                    ustedPagaMedioPagoAbonos = reservacion.getAbonosReales().subtract(reservacion.getDatosAbono().getUltimoAbono().getCantAbonoSinDto());
                    dctoAbonos = Numero.getTantoPorCientoMenos(totalMedioPagoAbonos, ustedPagaMedioPagoAbonos);
                }
                p.setTotal(totalMedioPagoAbonos);
                p.establecerDescuento(dctoAbonos.setScale(0, RoundingMode.HALF_UP));
                p.setUstedPaga(ustedPagaMedioPagoAbonos);
                p.establecerEntregado(ustedPagaMedioPagoAbonos.toString());
                
                factura.crearNuevaLineaPago(p);
            }

            if (reservacion.getDatosAbono() != null && reservacion.getDatosAbono().isEsultimoAbono()) {
                for (Pago pagoFinal : reservacion.getDatosAbono().getUltimosPagos().getPagos()) {
                    factura.crearNuevaLineaPago(pagoFinal);
                }
            }
            factura.getTotales().recalcularTotalesLineas(lineas);
            factura.recalcularFinalPagado();

            if (Sesion.getTicket() != null) {
                Sesion.getTicket().setId_ticket(ServicioContadoresCaja.obtenerContadorFactura());
            }
            
            factura.finalizarTicket(false);
            TicketService.escribirTicket(em, factura, false);
            //PrintServices.getInstance().imprimirTicket(factura, false, PrintServices.IMPRESORA_PANTALLA);
            PrintServices.getInstance().imprimirTicket(factura);


            return factura;
        }
        catch (ImporteInvalidoException ex) {
            log.error("Error facturando el Artículo. Excepción en el ticket", ex);
            throw new ReservasException(ex.getMessage(), ex);
        }
        catch (TicketException ex) {
            log.error("Error facturando el Artículo. Excepción en el ticket", ex);
            throw new ReservasException(ex.getMessage(), ex);
        }
        catch (ArticuloNotFoundException ex) {
            log.error("Error facturando el Artículo. Artículo no encontrado", ex);
            throw new ReservasException(ex.getMessage(), ex);
        }
        catch (TarifaArticuloNotFoundException ex) {
            log.error("Error facturando el Artículo. Tarifa no encontrada", ex);
            throw new ReservasException(ex.getMessage(), ex);
        }
        catch (PagoInvalidException ex) {
            log.error("Error facturando el Artículo. Pago Invalido", ex);
            throw new ReservasException(ex.getMessage(), ex);
        }
        catch (Exception ex) {
            log.error("Error facturando el Artículo", ex);
            throw new ReservasException("Error facturando el Artículo", ex);
        }

    }    

    public static void comprobanteArticulosReserva(Reservacion reservacion, List<ArticuloReservado> listaArticulos) throws TicketPrinterException, TicketException, DocumentoException {
        PrintServices.getInstance().limpiarListaDocumentos();
        PrintServices.getInstance().imprimirArticulosReserva(listaArticulos, reservacion);
        DocumentosService.crearDocumentoLiquidaRE(reservacion, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.LIQUIDACION_RE);
        PrintServices.getInstance().limpiarListaDocumentos();
    }    
    
    public static Reservacion obtenerReserva(ReservaBean reserva) throws ReservasException {
        SqlSession sql = new SqlSession();
        try{
            if(reserva.getCodalm().equals(Sesion.getTienda().getCodalm())){
                sql.openSession(SessionFactory.openSession());  
            } else {
                Connection conn = new Connection();
                conn.abrirConexion(Database.getConnectionCentral());
                sql.openSession(SessionFactory.openSession(conn));                
            }            
            obtenerCamposReserva(reserva,sql);
            return new Reservacion(reserva);
        }
        catch (NoResultException e) {
            log.error("Se ha producido un error intentando consultar reservaciones.", e);
            throw new ReservasException("Se ha producido un error intentando consultar reservaciones.", e);            
        }
        catch (ClienteException e) {
            log.error("Se ha producido un error intentando consultar el cliente de las reservaciones.", e);
            throw new ReservasException("Se ha producido un error intentando consultar el cliente de las reservaciones.", e);    
        }
        catch (Exception e) {
            log.error("Se ha producido un error intentando consultar reservaciones.", e);
            throw new ReservasException("Se ha producido un error intentando consultar reservaciones.", e);               
        }
        finally {
            sql.close();
        }
    }
    
    public static void anularCompraArticulo(SqlSession sql,String uidTicket) throws ReservasException {
        try{
            ReservaArticuloMapper mapperArticulo = sql.getMapper(ReservaArticuloMapper.class);
            ReservaArticuloExample exampleArticulo = new ReservaArticuloExample();

            exampleArticulo.or().andUidTicketEqual(uidTicket);
            List<ReservaArticuloBean> listaArticulos = mapperArticulo.selectByExample(exampleArticulo);

            if(!listaArticulos.isEmpty()) {
                for(ReservaArticuloBean articulo:listaArticulos) {
                    articulo.setComprado(false);
                    articulo.setProcesado(false);
                    articulo.setProcesadoTienda(false);
                    articulo.setPrecioTotalSinDto(null);
                    articulo.setPrecioTotalConDto(null);
                    articulo.setUidTicket(null);
                    articulo.setIdInvitado(null);

                    mapperArticulo.updateByPrimaryKey(articulo);
                }
                // Actualizamos la reservacion para que se envie
                String uidReservacion = listaArticulos.get(0).getUidReservacion();
                ReservaMapper mapper = sql.getMapper(ReservaMapper.class);
                ReservaExample example = new ReservaExample();
                example.or().andUidReservacionEqualTo(uidReservacion);
                ReservaBean reserva = mapper.selectByExample(example).get(0);
                reserva.setProcesadoTienda(false);
                reserva.setProcesado(false);
                mapper.updateByPrimaryKey(reserva);
            }
        } catch (Exception e) {
            log.debug("anularCompraArticulo() - Fallo al anular la compra de los articulos",e);
            throw new ReservasException(e.getMessage(), e);
        }
    }
    
    private static void obtenerCamposReserva(ReservaBean reserva, SqlSession sql) throws NoResultException, ClienteException {
        ReservaArticuloMapper mapperArticulo = sql.getMapper(ReservaArticuloMapper.class);
        ReservaArticuloExample exampleArticulo = new ReservaArticuloExample();        
        ReservaInvitadoMapper mapperInvitado = sql.getMapper(ReservaInvitadoMapper.class);
        ReservaInvitadoExample exampleInvitado = new ReservaInvitadoExample();     
        ReservaAbonoMapper mapperAbono = sql.getMapper(ReservaAbonoMapper.class);
        ReservaAbonoExample exampleAbono = new ReservaAbonoExample();     
        FacturacionTicketMapper mapperFacturacion = sql.getMapper(FacturacionTicketMapper.class);
        FacturacionTicketExample exampleFacturacion = new FacturacionTicketExample();  
        ReservaTiposMapper mapperTipo = sql.getMapper(ReservaTiposMapper.class);
        ReservaTiposExample exampleTipo = new ReservaTiposExample();

        //Rellenamos el cliente de la reserva
        reserva.setCliente(ClientesServices.getInstance().consultaClienteDoc(reserva.getCodcli(), reserva.getTipoDocu()));
                
        //Rellenamos los invitados
        exampleInvitado = new ReservaInvitadoExample();    
        exampleInvitado.or().andUidReservacionEqualTo(reserva.getUidReservacion());
        reserva.setReservaInvitadoList(mapperInvitado.selectByExample(exampleInvitado));
        Map<Long,ReservaInvitadoBean> rIs = new HashMap<Long, ReservaInvitadoBean>();
        
        for(ReservaInvitadoBean ri:reserva.getReservaInvitadoList()){
            ri.setReserva(reserva);
            rIs.put(ri.getIdInvitado(), ri);
        }

        //Rellenamos los artículos
        exampleArticulo.or().andUidReservacionEqualTo(reserva.getUidReservacion());
        reserva.setReservaArticuloList(mapperArticulo.selectByExample(exampleArticulo));
        for(ReservaArticuloBean ra:reserva.getReservaArticuloList()){
            ra.setReserva(reserva);
            //Por cada artículo rellenamos el cliente
            ReservaInvitadoBean ri = rIs.get(ra.getIdInvitado());
            if(ri!=null){
                ra.setInvitadoPagador(ri);
            }
        }
        
        //Rellenamos los Abonos (que no estén anulados)
        exampleAbono.or().andUidReservacionEqualTo(reserva.getUidReservacion()).andAnuladoEqualTo(false);
        reserva.setReservaAbonoList(mapperAbono.selectByExampleWithBLOBs(exampleAbono));
        for(ReservaAbonoBean rb:reserva.getReservaAbonoList()){
            //Por cada abono rellenamos el invitado
            ReservaInvitadoBean ri = rIs.get(rb.getIdInvitado());
            if(ri!=null){
                rb.setReservaInvitado(ri);
            }
        }
        
        //Rellenamos los datos de facturacion
        exampleFacturacion.or().andUidReservacionEqualTo(reserva.getUidReservacion());
        try{
            reserva.setDatosFacturacion(mapperFacturacion.selectByExample(exampleFacturacion).get(0));
        } catch(IndexOutOfBoundsException e){
            //No hay datos de facturacion, se ignora
        }
        
        //Rellenamos el tipoReserva
        exampleTipo.or().andCodTipoEqualTo(reserva.getCodTipo());
        reserva.setReservaTipo(mapperTipo.selectByExample(exampleTipo).get(0));
    }
    
    private static void obtenerAbonosyArticulosReservacion(ReservaBean reserva, SqlSession sql) throws NoResultException{
        ReservaAbonoMapper mapperAbono = sql.getMapper(ReservaAbonoMapper.class);
        ReservaAbonoExample exampleAbono = new ReservaAbonoExample();    
        
        //Rellenamos los Abonos (que no estén anulados)
        exampleAbono.or().andUidReservacionEqualTo(reserva.getUidReservacion()).andAnuladoEqualTo(false);
        reserva.setReservaAbonoList(mapperAbono.selectByExampleWithBLOBs(exampleAbono));        
        
        ReservaArticuloMapper mapperArticulo = sql.getMapper(ReservaArticuloMapper.class);
        ReservaArticuloExample exampleArticulo = new ReservaArticuloExample();        
        
        //Rellenamos los artículos
        exampleArticulo.or().andUidReservacionEqualTo(reserva.getUidReservacion());
        reserva.setReservaArticuloList(mapperArticulo.selectByExample(exampleArticulo));        
    }
    
    public static void actualizarCampoBono(BigInteger codReservacion, SqlSession sql, String bono) throws ReservasException {
        try {
            ReservaBean reserva = consultaById(codReservacion);
            reserva.setBono(bono);
            modificarOnlyReservaWithSql(sql, reserva);
        }
        catch (Exception ex) {
            String msg = "Error inesperado actualizando abono.";
            log.error(msg, ex);
            throw new ReservasException(msg, ex);
        }      
    }
    
    /**
     * @author Gabriel Simbania
     * @param reserva
     * @throws ReservasException 
     */
    public static void modificarReservaProcesar(ReservaProcesar reserva) throws ReservasException {
        log.info("DAO(modificarReservaProcesar): Modificando datos generales de ReservaProcesar");

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(reserva);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("Error modificando ReservaProcesar Base de datos :" + e.getMessage(), e);
            throw new ReservasException("Error modificando ReservaProcesar en Base de datos");
        } finally {
            em.close();
        }
    }
    
    /**
     * @author Gabriel Simbania
     * @param reservaBean
     * @param enumTipoDocumento
     */
    public static void generarKardexReserva(ReservaBean reservaBean, EnumTipoDocumento enumTipoDocumento ) {

        try {
            log.debug("Creando la reserva para el kardex");

            List<ItemDTO> itemDTOLista = new ArrayList<>();
            for (ReservaArticuloBean linea : reservaBean.getReservaArticuloList() ) {
                ItemDTO itemDTO = new ItemDTO();
                if(EnumTipoDocumento.RESERVA.equals(enumTipoDocumento)){
                    itemDTO.setCantidad((long) linea.getCantidad()*-1);
                }else{
                    itemDTO.setCantidad((long) linea.getCantidad());
                }
                itemDTO.setCodigoI(Integer.parseInt(linea.getCodMarca()) + "-" + String.valueOf(linea.getIdItem()));
                itemDTOLista.add(itemDTO);
            }

            UsuarioDTO usuarioDTO = UtilUsuario.verificarUsuarioDTO(Sesion.getUsuario().getUsuario());
            String numeroDocumento = Sesion.getTienda().getCodalm() +  reservaBean.getCodcaja() + String.format("%09d", reservaBean.getCodReservacion().intValue());
            DocumentoDTO documentoDTO = new DocumentoDTO(numeroDocumento, Sesion.getTienda().getCodalmSRI(), null, itemDTOLista, usuarioDTO);
            documentoDTO.setTipoDocumento(enumTipoDocumento.getNombre());
            documentoDTO.setTipoMovimiento(Constantes.MOVIMIENTO_52);

            String documentoString = JsonUtil.objectToJson(documentoDTO);
            ProcesoEncolarThread encolarKardexThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), documentoString, Variables.getVariable(Variables.QUEUE_KARDEX_POS), Constantes.PROCESO_KARDEX_POS, reservaBean.getUidReservacion());
            encolarKardexThread.start();

        } catch (Throwable e) {
            log.error("Error en la reserva del kardex " + e.getMessage());
        }
    }
    
    /**
     * @author Gabriel Simbania
     * @param reservaBean
     * @param listaresArt
     * @param enumTipoDocumento
     */
    public static void generarKardexReservaArticulos(ReservaBean reservaBean,List<ReservaArticuloBean> listaresArt, EnumTipoDocumento enumTipoDocumento ) {

        try {
            log.debug("Creando la reserva para el kardex");

            List<ItemDTO> itemDTOLista = new ArrayList<>();
            for (ReservaArticuloBean linea : listaresArt) {
                ItemDTO itemDTO = new ItemDTO();
                if(EnumTipoDocumento.RESERVA.equals(enumTipoDocumento)){
                    itemDTO.setCantidad((long) linea.getCantidad()*-1);
                }else{
                    itemDTO.setCantidad((long) linea.getCantidad());
                }
                itemDTO.setCodigoI(Integer.parseInt(linea.getCodMarca()) + "-" + String.valueOf(linea.getIdItem()));
                itemDTOLista.add(itemDTO);
            }

            UsuarioDTO usuarioDTO = UtilUsuario.verificarUsuarioDTO(Sesion.getUsuario().getUsuario());
            String numeroDocumento = Sesion.getTienda().getCodalm() +  reservaBean.getCodcaja() + String.format("%09d", reservaBean.getCodReservacion().intValue());
            DocumentoDTO documentoDTO = new DocumentoDTO(numeroDocumento, Sesion.getTienda().getCodalmSRI(), null, itemDTOLista, usuarioDTO);
            documentoDTO.setTipoDocumento(enumTipoDocumento.getNombre());
            documentoDTO.setTipoMovimiento(Constantes.MOVIMIENTO_52);

            String documentoString = JsonUtil.objectToJson(documentoDTO);
            ProcesoEncolarThread encolarKardexThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), documentoString, Variables.getVariable(Variables.QUEUE_KARDEX_POS), Constantes.PROCESO_KARDEX_POS, reservaBean.getUidReservacion());
            encolarKardexThread.start();

        } catch (Throwable e) {
            log.error("Error en la reserva del kardex " + e.getMessage());
        }
    }
    
    
    /**
     * @author Gabriel Simbania
     * @param articuloReservado
     * @param reservacion 
     */
    public static void accionEliminarArticuloStock(ReservaArticuloBean articuloReservado, Reservacion reservacion) {
        try {
            LogKardexBean logKardex = new LogKardexBean();
            logKardex.setTipoAccion(LogKardexBean.tipoAccionReservacion);
            logKardex.setUsuarioAutorizacion(Sesion.getUsuario().getUsuario());    
            logKardex.setFactura(String.valueOf(String.valueOf(reservacion.getReservacion().getCodReservacion())));            
            log.debug("removeArticulosReserva() - Disminuyendo stock de Artículo con ID:" + articuloReservado.getIdItem() + " y marca." + articuloReservado.getCodMarca());
            ServicioStock.disminuyeStockReserva(articuloReservado.getCodMarca(), articuloReservado.getIdItem(), 1, logKardex);
            ServicioStock.actualizaKardex(articuloReservado.getCodart(), ServicioStock.MOVIMIENTO_52, Sesion.getTienda().getCodalm(), 1L);
        }
        catch (Exception ex) {
            log.error("removeArticulosReserva() - STOCK: No fué posible disminuir el stock reservado para el artículo");
        }
    }
    
}