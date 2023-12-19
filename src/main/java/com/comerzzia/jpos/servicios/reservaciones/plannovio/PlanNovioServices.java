/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.reservaciones.plannovio;

import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasAbono;
import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasRecibo;
import com.comerzzia.jpos.entity.db.AbonoPlanNovio;
import com.comerzzia.jpos.entity.db.ArticuloPlanNovio;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.ConfigImpPorcentaje;
import com.comerzzia.jpos.entity.db.InvitadoPlanNovio;
import com.comerzzia.jpos.entity.db.InvitadoPlanNovioPK;
import com.comerzzia.jpos.entity.db.MotivoDevolucion;
import com.comerzzia.jpos.entity.db.PlanNovio;
import com.comerzzia.jpos.entity.db.PlanNovioPK;
import com.comerzzia.jpos.entity.db.Vendedor;
import com.comerzzia.jpos.servicios.devoluciones.Devolucion;
import com.comerzzia.jpos.entity.services.reservaciones.ComprobanteAbono;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.ParametrosBuscarPlanNovio;
import com.comerzzia.jpos.entity.services.reservaciones.plannovio.PlanNovioOBJ;
import com.comerzzia.jpos.gui.JPrincipal;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaBean;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.persistencia.reservaciones.plannovios.PlanNovioDao;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.articulos.ArticulosServices;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.general.vendedores.VendedoresServices;
import com.comerzzia.jpos.servicios.bonos.BonosServices;
import com.comerzzia.jpos.servicios.core.contadores.ServicioContadores;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.devoluciones.DevolucionesServices;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.promociones.cupones.CuponException;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.listapda.ListaPDAServices;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.tickets.ReferenciaTicket;
import com.comerzzia.jpos.servicios.tickets.xml.TicketXMLServices;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.session.SqlSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import jxl.Cell;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.EmptyCell;
import jxl.read.biff.BiffException;

/**
 * Esta clase contendrá los servicios del Plan novio
 * @author MGRI
 */
public class PlanNovioServices {

    // Log
    private static Logger log = Logger.getMLogger(PlanNovioServices.class);
    
    public static BigInteger maximoIDPorCaja(String codCaja) throws PlanNovioException{
        
        try {
            
            EntityManager em;
            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
            
            return PlanNovioDao.maximoIDPorCaja(em,codCaja);
        } 
        catch (NoResultException e) {
            throw e;
        }
        catch (Exception e) {
            throw new PlanNovioException("No fue posible obtener el último plan novio creado en la caja que se indica.", e);
        }
    }
    
    public static BigInteger consultaMaximoIdAbonoPorPlan(BigInteger idPlan) throws PlanNovioException{
        
        try {
            
            EntityManager em;
            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
            
            return PlanNovioDao.consultaMaximoIdAbonoPorPlan(em,idPlan);
        } 
        catch (NoResultException e) {
            throw e;
        }
        catch (Exception e) {
            throw new PlanNovioException("No fue posible obtener el último abono del plan novio que se indica.", e);
        }
    }
    
    

    public static List<PlanNovio> buscar(ParametrosBuscarPlanNovio param) throws PlanNovioException {

        log.info("DAO(buscar): Consulta de planes de novio");
        List<PlanNovio> res = null;
        EntityManager em;
        //if (param.getTiendaPlan().isEmpty() || param.getTiendaPlan().equals(Sesion.getTienda().getAlmacen().getCodalm())) {
            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
      //  } else {
      //      EntityManagerFactory emfc = Sesion.getEmfc();
      //      em = emfc.createEntityManager();
       // }

        try {
            res = PlanNovioDao.buscar(em, param);
            return res;
        } catch (Exception e) {
            log.error("buscar(): Error" + e.getMessage(), e);
            throw new PlanNovioException("Error consltando los planes de novio.");
        } finally {
            em.close();
        }
    }

    public static PlanNovio consultarDetalle(PlanNovio planParam) throws PlanNovioException {

        log.info("DAO(consultarDetalle): Consulta un plan de novio");

        PlanNovio res = null;
        EntityManager em;
     //   if (planParam.getPlanNovioPK().getCodalm().isEmpty() || planParam.getPlanNovioPK().getCodalm().equals(Sesion.getTienda().getAlmacen().getCodalm())) {
            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
      //  } else {
     //       EntityManagerFactory emfc = Sesion.getEmfc();
     //       em = emfc.createEntityManager();
     //   }
        try {
            res = PlanNovioDao.consultarDetalle(em, planParam);
            return res;
        } catch (Exception e) {
            log.error("consultarDetalle(): Error" + e.getMessage(), e);
            throw new PlanNovioException("Error consltando datos del plan novios");
        } finally {
            em.close();
        }
    }

    public static PlanNovio consultarAbonosSinAnular(PlanNovio planParam) throws PlanNovioException {

        log.info("DAO(consultarAbonosSinAnular): Consulta un plan de novio");

        PlanNovio res = null;
        EntityManager em;
       // if (planParam.getPlanNovioPK().getCodalm().isEmpty() || planParam.getPlanNovioPK().getCodalm().equals(Sesion.getTienda().getAlmacen().getCodalm())) {
            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
      //  } else {
       //     EntityManagerFactory emfc = Sesion.getEmfc();
       //     em = emfc.createEntityManager();
       // }

        try {
            res = PlanNovioDao.consultarAbonosSinAnular(em, planParam);
            return res;
        } catch (Exception e) {
            log.error("consultarAbonosSinAnular(): Error" + e.getMessage(), e);
            throw new PlanNovioException("Error consltando abonos del plan novios");
        } finally {
            em.close();
        }
    }    
    
    /**
     * Método que crea un plan Novio
     * @param plan
     * @throws PlanNovioException 
     */
    public static void crear(PlanNovio plan) throws PlanNovioException {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            log.debug("-- Obteniendo contador de plan novio");
            Long idPlan = ServicioContadores.obtenerContadorDefinitivo("X_PLAN_NOVIO");
            plan.setPlanNovioPK(new PlanNovioPK(BigInteger.valueOf(idPlan), VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN)));

            log.debug("-- Creando Plan");
            PlanNovioDao.crear(em, plan);

            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error creando Plan de Novios en Base de datos :" + e.getMessage(), e);
            throw new PlanNovioException("Error creando Plan de Novios en Base de datos");
        } finally {
            em.close();
        }
    }

    /**
     * Método que añade artículos de un plan Novio
     * @param listaArt
     * @param plan
     * @param totalAPagar
     * @throws PlanNovioException 
     */
    public static void addArticulosPlan(List<ArticuloPlanNovio> listaArt, PlanNovio plan, BigDecimal totalAPagar, SesionPdaBean sesionPda) throws PlanNovioException {
        
        log.info("DAO(addArticulosPlan): Añadiendo artículos al plan novio");
        
        EntityManagerFactory emf;
        EntityManager em = null;

        try {
            emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            PlanNovioDao.addArticulos(em, listaArt);
            plan.addTemporalListaArticulos(listaArt);
            plan.addReservado(totalAPagar);
            plan.setProcesado('N');
            plan.setProcesadoTienda('N');
            plan.refrescaTotales();
            PlanNovioDao.modifica(em, plan);
            if (sesionPda != null) {
                ListaPDAServices.marcarComoUtilizado(sesionPda);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            if (totalAPagar != null) {
                plan.subtractReservado(totalAPagar);
            } else {
                log.debug("addArticulosPlan() - error - El total a pagar es null");
            }
            throw new PlanNovioException("No se pudo guardar el abono en la base de datos");
        } finally {
            em.close();
        }
    }

    /**
     * Método que elimina artículos de un plan Novio
     * @param articuloReservado
     * @param plan
     * @throws PlanNovioException 
     */
    public static void removeArticulo(ArticuloPlanNovio articuloReservado, PlanNovio plan) throws PlanNovioException {
        log.info("DAO(removeArticulo): Eliminando un artículo de un plan de novio");
        
        EntityManagerFactory emf;
        EntityManager em = null;

        try {
            emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            PlanNovioDao.removeArticulo(em, articuloReservado);
            plan.removeTemporalArticulos(articuloReservado);
            plan.refrescaTotales();
            PlanNovioDao.refrescaReserva(em, plan);

            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new PlanNovioException("No se pudo eliminar el artículo del plan novios");
        } finally {
            em.close();
        }
    }

    /**
     * Método que crea un invitado de un plan Novio
     * @param invitado
     * @param plan
     * @throws PlanNovioException 
     */
    public static void crearInvitado(InvitadoPlanNovio invitado, PlanNovio plan) throws PlanNovioException {
        log.info("DAO(crearInvitado): Creando invitado de un plan de novio");
        
        EntityManagerFactory emf;
        EntityManager em = null;

        try {
            emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            PlanNovioDao.crearInvitado(em, invitado);
            PlanNovioDao.refrescaReserva(em, plan);

            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new PlanNovioException("No se pudo crear el invitado para el plan novios");
        } finally {
            em.close();
        }
    }

    public static List<InvitadoPlanNovio> parseInvitados(File fichero) throws PlanNovioException {
        try {
            List<InvitadoPlanNovio> res = new ArrayList<InvitadoPlanNovio>();
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("cp1252");
            Workbook workbook = Workbook.getWorkbook(fichero, ws);
            Sheet sheet = workbook.getSheet(0);

            // Bucle de lineas
            int i = 0;
            InvitadoPlanNovio inv = null;
            boolean encontradoNulo = false;

            int numInvitados = sheet.getColumn(1).length;

            while (!encontradoNulo && i < numInvitados) {

                if (!(sheet.getCell(1, i) instanceof EmptyCell) && !(sheet.getCell(2, i) instanceof EmptyCell)) {
                    // obligatorias
                    LabelCell b = (LabelCell) sheet.getCell(1, i);
                    LabelCell c = (LabelCell) sheet.getCell(2, i);

                    // opcionales
                    Cell a = sheet.getCell(0, i);
                    Cell d = sheet.getCell(3, i);

                    if (a != null && b != null && c != null && d != null) {
                        inv = new InvitadoPlanNovio();
                        inv.setNombre(b.getString());
                        inv.setApellido(c.getString());
                        if (a instanceof LabelCell) {
                            String titulo = ((LabelCell) a).getString();
                            inv.setTitulo(titulo);
                        } else {
                            inv.setTitulo("");
                        }
                        if (d instanceof LabelCell) {
                            String telefono = ((LabelCell) d).getString();
                            inv.setTelefono(telefono);
                        } else {
                            inv.setTelefono("");
                        }

                        res.add(inv);
                    } else {
                        encontradoNulo = true;
                    }
                }
                i++;
            }

            return res;
        } catch (IOException ex) {
            log.error("Error parseando Excel " + ex.getMessage(), ex);
            throw new PlanNovioException("Error leyendo fichero Excel. Compruebe que el fichero se encuentra en la ruta correcta.");
        } catch (BiffException ex) {
            log.error("Error parseando Excel " + ex.getMessage(), ex);
            throw new PlanNovioException("Error leyendo fichero Excel");
        } catch (Exception ex) {
            log.error("Error parseando Excel " + ex.getMessage(), ex);
            throw new PlanNovioException("Error leyendo fichero Excel");
        }
    }

    public static void añadirInvitados(List<InvitadoPlanNovio> linvitados, PlanNovio plan) throws PlanNovioException {
        log.info("DAO(añadirInvitados): Añadiendo invitado de un plan de novio");
        EntityManagerFactory emf;
        EntityManager em = null;

        try {
            emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Long idInvitado = PlanNovioDao.consultaSiguienteIdInvitado(em, plan);
            // Guardamos cada invitado
            for (InvitadoPlanNovio invitado : linvitados) {
                if (plan.existeInvitado(invitado)) {
                    continue;
                }
                if(invitado.getTelefono().trim().length() > 12){
                    throw new PlanNovioException("El teléfono de "+ invitado.getNombre() + " " + invitado.getApellido() +  " (" + invitado.getTelefono() + ") no puede tener mas de 12 caracteres");
                }
                log.info("Importando " + invitado.getNombre());
                invitado.setInvitadoPlanNovioPK(new InvitadoPlanNovioPK(plan.getPlanNovioPK().getIdPlan(), new BigInteger(idInvitado.toString()), plan.getPlanNovioPK().getCodalm(), Sesion.getTienda().getCodalm()));
                invitado.setPlanNovio(plan);
                invitado.setProcesado('N');
                PlanNovioDao.crearInvitado(em, invitado);
                idInvitado++;
            }

            // Guardamos el Plan
            log.info("Importando 1");
            PlanNovioDao.refrescaReserva(em, plan);
            log.info("Importando 2");
            em.getTransaction().commit();
            log.info("Importando 3");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            em.getTransaction().rollback();
            throw new PlanNovioException(ex.getMessage());
        } finally {
            em.close();
        }
    }

    public static void importarInvitados(PlanNovio plan, File archivoInvitados) throws PlanNovioException {
        log.info("DAO(importarInvitados): Importando invitados de un plan de novio");
        // Leemos el archivo
        String urlArchivo = Sesion.config.getPlanNoviosExcelInvitados();

       // File archivoInvitados = new File(urlArchivo);
        List<InvitadoPlanNovio> listaInvitados = parseInvitados(archivoInvitados);
        añadirInvitados(listaInvitados, plan);

        // Añadimos los invitados a la vista (No podemos recargar el plan como en otros casos porque se va de la ventana)
        PlanNovioServices.refrescaInvitadosPlan(plan);
    }

    public static Long consultaSiguienteIdInvitado(PlanNovio plan) throws PlanNovioException {
        log.info("DAO(consultaSiguienteIdInvitado): Consultando siguiente id de invitado de un plan de novio");
        
        
        EntityManagerFactory emf;
        EntityManager em = null;
        Long res = null;
        try {
            emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            res = PlanNovioDao.consultaSiguienteIdInvitado(em, plan);

            em.getTransaction().commit();
        } catch (Exception ex) {
            log.error("consultaSiguienteIdInvitado() No se pudo consultar el siguiente Identificador para el invitado : " + ex.getMessage(), ex);
            throw new PlanNovioException("No se pudo consultar el siguiente Identificador para el invitado");
        } finally {
            em.close();
        }
        return res;
    }

    public static void crearPagoArticulos(TicketS ticket, PlanNovioOBJ planNovio, InvitadoPlanNovio invitado, Vendedor vendedor) throws PlanNovioException {
        log.info("DAO(crearPagoArticulos): Creando pagos de artículos de un plan de novio");

        BigDecimal efectivoEnCajaAnt = Sesion.getCajaActual().getEfectivoEnCaja();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
            
        try {
            em.getTransaction().begin();

            realizarPagoArticulos(ticket, planNovio, invitado, vendedor, false, em);

            em.getTransaction().commit();

            // IMPRIMIMOS EL TICKET (LOS PAGOS SE TRATAN DENTRO)
            PrintServices.getInstance().limpiarListaDocumentos();
            PrintServices.getInstance().imprimirFacturaPagoPlanArticulo(ticket, planNovio, invitado);

            // Insertamos el documento en BBDD con el ticket y la lista de documentos, que luego limpiamosos.
            DocumentosService.crearDocumento(ticket, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.FACTURA);
            PrintServices.getInstance().limpiarListaDocumentos();
            
            BonosServices.crearBonosPagos(ticket.getPagos(), planNovio.getCodPlanAsString(), "Compra Reserva", planNovio.getClienteSeleccionado());

        } catch (TicketPrinterException ex) {
            log.error(ex.getMessage(), ex);
            ticket = null;
            em.getTransaction().rollback();
            throw new PlanNovioException("Se guardó la reserva, pero hubo un error imprimiendo el ticket.");

        } catch (TicketException ex) {
            log.error(ex.getMessage(), ex);
            ticket = null;
            em.getTransaction().rollback();
            throw new PlanNovioException("Se guardó la reserva, pero hubo un error salvando el ticket.");
        } catch (Exception ex) {
            ticket = null;
            log.error(ex.getMessage(), ex);
            em.getTransaction().rollback();
            Sesion.getCajaActual().setEfectivoEnCaja(efectivoEnCajaAnt);
            throw new PlanNovioException("No se pudo guardar el pago de artículos en la base de datos");
        } finally {
            em.close();
        }
    }

    /**
     *  Método para realizar la compra de un Artículo en plan Novios
     * @param ticket
     * @param planNovio
     * @param invitado
     * @param vendedor
     * @throws PlanNovioException 
     */
    public static void realizarPagoArticulos(TicketS ticket, PlanNovioOBJ planNovio, InvitadoPlanNovio invitado, Vendedor vendedor, boolean isAbonosPropios, EntityManager em) throws PlanNovioException, CuponException, TicketException, MedioPagoException {
        log.info("DAO(realizarPagoArticulos): Realizando pagos de artículos de un plan de novio");
        
        
        BigDecimal total = new BigDecimal(0);
        
        
        // Especifico del pago de artículo
        List<ArticuloPlanNovio> listaResArtSel = new LinkedList<ArticuloPlanNovio>();
        List<ArticuloPlanNovio> listaresArt = new LinkedList<ArticuloPlanNovio>();

        // Totales 
        BigDecimal totalAPagar = BigDecimal.ZERO;

        // Recorrido de Líneas y Actualización de valores de artículos en función de líneas
        log.debug("Actualizando Articulos de la reserva");
        List<ArticuloPlanNovio> listaArticulosEnReserva = planNovio.getPlan().getListaArticulos();
        for (int h = 0; h<ticket.getLineas().getLineas().size();h++){
            LineaTicket lin=ticket.getLineas().getLineas().get(h); 
            for (int j = 0; j < lin.getCantidad(); j++) {
                boolean enc = false;
                for (ArticuloPlanNovio ra : listaArticulosEnReserva) {
                    if (ra.getCodBarras().equals(lin.getCodigoBarras()) && ((isAbonosPropios && ra.isComprado()) || (!isAbonosPropios && !ra.isComprado() && !ra.isPendienteEnvio() && !ra.isPendienteRecoger())) && ra.getBorrado() != 'S') {
                        enc = true;
                        listaresArt.add(ra);
                        ra.setInvitadoPlanNovio(invitado);
                        if(invitado != null && invitado.getInvitadoPlanNovioPK() != null){
                            ra.setCodAlmInvitado(invitado.getInvitadoPlanNovioPK().getCodAlmInvitado());
                        }
                        ra.setFechaCompra(new Date());
                        
                        //Si ya aparece como comprado, es un artículo comprado con abono, le ponemos el uidTicket y el IdLinea a 1
                        if(ra.isComprado()){
                            ra.setUidTicket(ticket.getUid_ticket());
                            ra.setIdLineaTicket(new Long(h+1));
                        }
                        if (!isAbonosPropios) {
                            ra.setComprado(true);
                            ra.setUidTicket(ticket.getUid_ticket());
                            ra.setIdLineaTicket(new Long(h+1));
                        } 

                        if (lin.getDatosAdicionales() != null && (lin.getDatosAdicionales().isEnvioDomicilio() || lin.getDatosAdicionales().isRecogidaPosterior())) {
                            ra.setEntregado(false);
                        } else {
                            ra.setEntregado(true);
                        }
                        ra.setPrecio(lin.getPrecio());
                        ra.setPrecioTotal(lin.getPrecioTotal());
                        ra.setTotalPagadoConDscto(lin.getImporteFinalPagado());
                        if (vendedor != null && !(vendedor.getNombreVendedor().trim()).isEmpty()) {
                            ra.setCodVendedor(vendedor.getCodvendedor());
                        }
                        ra.setProcesado('N');
                        ra.setId_ticket(ticket.getId_ticket());
                        ra.setCodVendedor(planNovio.getPlan().getCodVendedor());
                        listaResArtSel.add(ra);
                        listaArticulosEnReserva.remove(ra);
                        
                        
                        

                        break;
                    }
                }
                if (!enc) {
                    log.error("Error en el pago del Plan. No se encuentra el artículo en el plan correspondiente a la línea de ticket con código de barras" + lin.getCodigoBarras());
                    throw new PlanNovioException("Error en los artículos del Plan");
                }
            }
            totalAPagar = totalAPagar.add(lin.getPrecioTotal().multiply(new BigDecimal(lin.getCantidad())));
        }


        // ARTÍCULOS
        PlanNovioDao.actualizarArticulos(em, listaresArt);
        // Se añaden los articulos para el calculo de totales. 
        // Se volverá a consultar el plan para mostrar la página
        planNovio.addTemporalListaArticulos(listaresArt);
        // PLAN COMO NO PROCESADO
        planNovio.refrescaTotales();
        PlanNovioDao.modificaNoProcesado(em, planNovio.getPlan());

        // IMPRESIÓN DE COMPROBANTE / FACTURA            
        ticket.finalizarTicket(false);
        JPrincipal.crearVentanaPuntos(ticket);

        // si el invitado es NULL, quien está comprando es el dueño del plan novio con abonos. No procesamos los pagos 
        boolean procesarPagos = (invitado != null);
        TicketService.escribirTicket(em, ticket, procesarPagos);
        
        //DAINOVY
        try
        {
            
            String idDocumento = ticket.getUid_ticket();            
            String documento = ticket.getTienda() + "-" + ticket.getCodcaja() + "-" + String.format("%09d",ticket.getId_ticket()) ;
            if (isAbonosPropios) {
                MedioPagoBean mpb = MediosPago.consultar(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_ABONO_A_RESERVAS));
                Sesion.getCajaActual().crearApunte2(em,totalAPagar,"VENTA", documento, mpb,idDocumento);
            }
        }
        catch (Exception e)
        {
            
        }
        //DAINOVY
        

    }

    /**
     *  Método para realizar la compra de un Artículo en plan Novios
     * @param ticket
     * @param planNovio
     * @param invitado
     * @param vendedor
     * @throws PlanNovioException 
     */
    public static void realizarPagoArticulos(TicketS ticket, PlanNovioOBJ planNovio, InvitadoPlanNovio invitado, Vendedor vendedor, boolean isAbonosPropios, EntityManager em, List<ArticuloPlanNovio> lista) throws PlanNovioException, CuponException, TicketException, MedioPagoException {
        log.info("DAO(realizarPagoArticulos): Realizando pagos de artículos de un plan de novio");
        
        
        BigDecimal total = new BigDecimal(0);
        
        
        // Especifico del pago de artículo
        List<ArticuloPlanNovio> listaResArtSel = new LinkedList<ArticuloPlanNovio>();
        List<ArticuloPlanNovio> listaresArt = new LinkedList<ArticuloPlanNovio>();

        // Totales 
        BigDecimal totalAPagar = BigDecimal.ZERO;

        // Recorrido de Líneas y Actualización de valores de artículos en función de líneas
        log.debug("Actualizando Articulos de la reserva");
        List<ArticuloPlanNovio> listaArticulosEnReserva = planNovio.getPlan().getListaArticulos();
        for (int h = 0; h < ticket.getLineas().getLineas().size(); h++){
            LineaTicket lin=ticket.getLineas().getLineas().get(h); 
            for (int j = 0; j < lin.getCantidad(); j++) {
                boolean enc = false;
                for (ArticuloPlanNovio ra : lista) {
                    if (ra.getCodBarras().equals(lin.getCodigoBarras()) && ((isAbonosPropios && ra.isComprado()) || (!isAbonosPropios && !ra.isComprado() && !ra.isPendienteEnvio() && !ra.isPendienteRecoger())) && ra.getBorrado() != 'S') {
                        enc = true;
                        listaresArt.add(ra);
                        ra.setInvitadoPlanNovio(invitado);
                        if(invitado != null && invitado.getInvitadoPlanNovioPK() != null){
                            ra.setCodAlmInvitado(invitado.getInvitadoPlanNovioPK().getCodAlmInvitado());
                        }
                        ra.setFechaCompra(new Date());
                        
                        //Si ya aparece como comprado, es un artículo comprado con abono, le ponemos el uidTicket y el IdLinea a 1
                        if(ra.isComprado()){
                            ra.setUidTicket(ticket.getUid_ticket());
                            ra.setIdLineaTicket(new Long(h+1));
                        }
                        if (!isAbonosPropios) {
                            ra.setComprado(true);
                            ra.setUidTicket(ticket.getUid_ticket());
                            ra.setIdLineaTicket(new Long(h+1));
                        } 

                        if (lin.getDatosAdicionales() != null && (lin.getDatosAdicionales().isEnvioDomicilio() || lin.getDatosAdicionales().isRecogidaPosterior())) {
                            ra.setEntregado(false);
                        } else {
                            ra.setEntregado(true);
                        }
                        ra.setPrecio(lin.getPrecio());
                        ra.setPrecioTotal(lin.getPrecioTotal());
                        ra.setTotalPagadoConDscto(lin.getPrecioTotal());
                        if (vendedor != null && !(vendedor.getNombreVendedor().trim()).isEmpty()) {
                            ra.setCodVendedor(vendedor.getCodvendedor());
                        }
                        ra.setProcesado('N');
                        ra.setId_ticket(ticket.getId_ticket());
                        ra.setCodVendedor(planNovio.getPlan().getCodVendedor());
                        listaResArtSel.add(ra);
                        listaArticulosEnReserva.remove(ra);
                        
                        
                        

                       // break;
                    }
                }
                if (!enc) {
                    log.error("Error en el pago del Plan. No se encuentra el artículo en el plan correspondiente a la línea de ticket con código de barras" + lin.getCodigoBarras());
                    throw new PlanNovioException("Error en los artículos del Plan");
                }
            }
            totalAPagar = totalAPagar.add(lin.getPrecioTotal().multiply(new BigDecimal(lin.getCantidad())));
        }


        // ARTÍCULOS
        PlanNovioDao.actualizarArticulos(em, listaresArt);
        // Se añaden los articulos para el calculo de totales. 
        // Se volverá a consultar el plan para mostrar la página
        planNovio.addTemporalListaArticulos(listaresArt);
        // PLAN COMO NO PROCESADO
        planNovio.refrescaTotales();
        PlanNovioDao.modificaNoProcesado(em, planNovio.getPlan());

        // IMPRESIÓN DE COMPROBANTE / FACTURA            
        ticket.finalizarTicket(false);
        JPrincipal.crearVentanaPuntos(ticket);

        // si el invitado es NULL, quien está comprando es el dueño del plan novio con abonos. No procesamos los pagos 
        boolean procesarPagos = (invitado != null);
        TicketService.escribirTicket(em, ticket, procesarPagos);
        
        //DAINOVY
        try
        {
            
            String idDocumento = ticket.getUid_ticket();            
            String documento = ticket.getTienda() + "-" + ticket.getCodcaja() + "-" + String.format("%09d",ticket.getId_ticket()) ;
            if (isAbonosPropios) {
                MedioPagoBean mpb = MediosPago.consultar(Variables.getVariable(Variables.POS_CONFIG_MEDIO_PAGO_ABONO_A_RESERVAS));
                Sesion.getCajaActual().crearApunte2(em,totalAPagar,"VENTA", documento, mpb,idDocumento);
            }
        }
        catch (Exception e)
        {
            
        }
        //DAINOVY
        

    }
    /**
     *  Método que crea un nuevo abono a Planes Novio
     * @param planNovio
     * @param ticket
     * @throws PlanNovioException 
     */
    public static void crearAbono(PlanNovioOBJ planNovio, TicketS ticket) throws PlanNovioException {
        log.debug("crearAbono() - Creando Abono");
        //El ticket creado no tiene el codigo de caja. se establece
        ticket.setCodcaja(Sesion.getCajaActual().getCajaActual().getCodcaja());

        // Totales 
        BigDecimal totalPagos = BigDecimal.ZERO;
        BigDecimal totalAbonoEntregado = BigDecimal.ZERO;
        BigDecimal efectivoEnCajaAnt = null;

        // Podría devolverlo el método que procesa los pagos si es usado por todos
        // Procesamos los pagos para Insertar los tipos de pago adecuados
        if (ticket.getPagos() != null && ticket.getPagos().getPagos() != null) {
            for (Pago pag : ticket.getPagos().getPagos()) {
                // Calculamos el total de los pagos y abono entregado
                totalPagos = totalPagos.add(pag.getTotal());
                totalAbonoEntregado = totalAbonoEntregado.add(pag.getUstedPaga());
            }
        }

        efectivoEnCajaAnt = Sesion.getCajaActual().getEfectivoEnCaja();

        EntityManager em;
        EntityManagerFactory emtienda = Sesion.getEmf();
        EntityManager emt = emtienda.createEntityManager();
        boolean central = false;
   //     if (planNovio.getPlan().getPlanNovioPK().getCodalm().isEmpty() || planNovio.getPlan().getPlanNovioPK().getCodalm().equals(Sesion.getTienda().getAlmacen().getCodalm())) {
            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
    //    } else {
     //       EntityManagerFactory emfc = Sesion.getEmfc();
     //       em = emfc.createEntityManager();
     //       central = true;
     //   }        

        try {

            // Consultamos el siguiente Id del abono                
            BigInteger idAbono = PlanNovioDao.consultaSiguienteIdAbono(em, planNovio.getPlan());

            // Creamos el objeto Abono
            AbonoPlanNovio nuevoAbono = new AbonoPlanNovio(planNovio.getPlan().getPlanNovioPK().getIdPlan(), idAbono, planNovio.getPlan().getPlanNovioPK().getCodalm());  // id plan, idBono, codalm
            nuevoAbono.setPlanNovio(planNovio.getPlan());
            nuevoAbono.setInvitadoPlanNovio(planNovio.getInvitado());
            nuevoAbono.setCodcaja(ticket.getCodcaja());
            nuevoAbono.getAbonoPlanNovioPK().setCodAlmAbono(Sesion.getTienda().getAlmacen().getCodalm());
            Fecha f = new Fecha();
            nuevoAbono.setFecha(f.getDate());
            nuevoAbono.setIdCajero(Sesion.getUsuario().getUsuario());
            if (planNovio.getVendedorSeleccionado() != null && !(planNovio.getVendedorSeleccionado().getNombreVendedor().trim()).isEmpty()) {
                nuevoAbono.setCodVendedor(planNovio.getVendedorSeleccionado().getCodvendedor());
            }

            // Establecemos los importes del abono
            nuevoAbono.setCantidadConDcto(totalPagos);
            nuevoAbono.setCantidadSinDcto(totalAbonoEntregado);
            nuevoAbono.setProcesado('N');
            nuevoAbono.setProcesadoTienda('N');
            nuevoAbono.setAnulado('N');
            nuevoAbono.setCodAlmInvitado(nuevoAbono.getInvitadoPlanNovio().getInvitadoPlanNovioPK().getCodAlmInvitado());

            // Establecemos los pagos
            nuevoAbono.setPagos(TicketXMLServices.getXMLPagos(ticket.getPagos()));

            // EMPEZAMOS LA TRANSACCIÓN
            em.getTransaction().begin();
            if(central){
                emt.getTransaction().begin();
            }


            // ABONO
            if (nuevoAbono != null) {
                PlanNovioDao.crearAbono(em, nuevoAbono);
            }

            // GUARDAMOS LOS PAGOS COMUNES
            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaAbonoPlanNovios(planNovio, nuevoAbono);
            if(!central){
                emt=em;
            }
            String documento = nuevoAbono.getAbonoPlanNovioPK().getCodAlmAbono() + "-" + nuevoAbono.getCodcaja() + "-"+ String.format("%09d", nuevoAbono.getAbonoPlanNovioPK().getIdPlan());
            TicketService.procesarMediosPagos(emt, ticket.getPagos().getPagos(), referencia, nuevoAbono.getAbonoPlanNovioPK().getIdAbono().longValue(), "PLN", documento);

            // Para refrescar los totales, añadimos el abono
            planNovio.addTemporalAbono(nuevoAbono);

            // MARCAMOS PLAN COMO PROCESADO = N  
            planNovio.refrescaTotales();
            PlanNovioDao.refrescaReserva(em, planNovio.getPlan());

            // Datos de facturacion al invitado para el ticket
            FacturacionTicketBean ft = null;
            try{
                if (ticket != null && ticket.getFacturacion() != null) {
                    ft = ticket.getFacturacion();
                } else {
                    ft = planNovio.getDatosFacturacionTicketBean();
                }
            //Al cambiar el TicketFacturacionBean, tenemos que tener el cuenta el nullpointer
            } catch (NullPointerException e) {
                ft = null;
            }

            CodigoBarrasRecibo codigoBarras = new CodigoBarrasAbono(planNovio.getCodPlanAsNumber(), nuevoAbono.getAbonoPlanNovioPK().getIdAbono().longValue());
            if (ft == null) {
                ft = new FacturacionTicketBean();
                ft.setTipoDocumento("");
                ft.setDocumento("");
                if (planNovio.getInvitado() != null && planNovio.getInvitado().getNombre() != null) {
                    ft.setNombre(planNovio.getInvitado().getNombre());
                    ft.setApellidos(planNovio.getInvitado().getApellido());
                    ft.setTelefono("" + planNovio.getInvitado().getTelefono());
                    ft.setDireccion("");
                } else if (planNovio.getClienteSeleccionado() != null && planNovio.getClienteSeleccionado() != null) {
                    ft = new FacturacionTicketBean(planNovio.getClienteSeleccionado());
                }
            }
            
            em.getTransaction().commit();
            if(central){
                emt.getTransaction().commit();
            }
            // Creamos el comprobante
            ComprobanteAbono comprobante = new ComprobanteAbono(planNovio, ticket.getPagos(), Numero.redondear(totalPagos).toString(), ft, f.getString("dd/MM/yyy - HH:mm"), codigoBarras);
            comprobante.setTotalAbonoEntregado(totalAbonoEntregado.toString());
            comprobante.setTotalConDescuentos(totalPagos.toString());
            comprobante.setObservaciones(planNovio.getObservaciones());
            comprobante.setNombreBoda(planNovio.getPlan().getNovia().getApellido() + " - " + planNovio.getPlan().getNovio().getApellido());
            comprobante.setNumAbono(nuevoAbono.getAbonoPlanNovioPK().getIdAbono().intValue());

            PrintServices.getInstance().limpiarListaDocumentos();
            PrintServices.getInstance().imprimirComprobanteAbono(comprobante, true, false);
            //Como los vouchers no hay que reimprimirlos, los borramos de la lista de reimpresion
            Iterator it = PrintServices.getInstance().getDocumentosImpresos().iterator();
            while (it.hasNext()) {
                DocumentosImpresosBean docimpre = (DocumentosImpresosBean) it.next();
                if (docimpre.getTipoImpreso().equals(DocumentosImpresosBean.TIPO_PAGO)) {
                    it.remove();
                }
            }
            DocumentosService.crearAbonoReserva(planNovio.getPlan().getPlanNovioPK().getCodalm(), comprobante, idAbono.longValue(), PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.ABONO_PLAN_NOVIO, false);
            PrintServices.getInstance().limpiarListaDocumentos();

            BonosServices.crearBonosPagos(ticket.getPagos(), planNovio.getCodPlanAsString(), "Abono Reserva", planNovio.getClienteSeleccionado());

        } catch (TicketPrinterException ex) {
            ticket = null;
            log.error("Se guardó el abono, pero hubo un error imprimiendo el ticket.: " + ex.getMessage(), ex);
            throw new PlanNovioException("Se guardó el abono, pero hubo un error imprimiendo el ticket.");
        } catch (TicketException ex) {
            ticket = null;
            log.error("Se guardó el abono, pero hubo un error imprimiendo el ticket.: " + ex.getMessage(), ex);
            throw new PlanNovioException("Se guardó el abono, pero hubo un error imprimiendo el ticket.");
        } catch (Exception ex) {
            ticket = null;
            log.error("No se pudo guardar el abono en la base de datos: " + ex.getMessage(), ex);
            Sesion.getCajaActual().setEfectivoEnCaja(efectivoEnCajaAnt);
            throw new PlanNovioException("No se pudo guardar el abono en la base de datos");
        } finally {
            em.close();
            try{
                emt.close();
            } catch (Exception e){}
        }
    }

    /**
     * Método que modifica los datos Generales de un plan Novio
     * @param plan
     * @throws PlanNovioException 
     */
    public static void modificarDatosGenerales(PlanNovio plan) throws PlanNovioException {
        log.info("DAO(modificarDatosGenerales): Modificando datos generales de un plan de novio");
        
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            plan.setProcesado('N');
            PlanNovioDao.modifica(em, plan);

            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error modificando Plan de Novios en Base de datos :" + e.getMessage(), e);
            throw new PlanNovioException("Error modificando Plan de Novios en Base de datos");
        } finally {
            em.close();
        }
    }

    /**
     * Método que liquida un plan Novio
     * @param plan
     * @param lista
     * @throws PlanNovioException 
     */
    public static void liquidarPlan(PlanNovio plan, String autorizador, BigDecimal valorRematar, Boolean esLocalOrigen) throws PlanNovioException {
        log.info("DAO(liquidarPlan): Liquidando plan de novio");
        
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        TicketS ticket = new TicketS();
        Devolucion dev = null;

        try {
            em.getTransaction().begin();

            if(esLocalOrigen){
                //plan.refrescaTotales();
                plan.setLiquidado('S');
                plan.setFechaLiquidacion(new Date());
                plan.setProcesado('N');
                PlanNovioDao.modifica(em, plan);
            }else{
                plan.setProcesado('N');
                PlanNovioDao.modifica(em, plan);
            }

            // Creamos un Ticket
            ticket.setAutorizadorVenta(autorizador);
            ticket.iniciaDatosBaseTicket();
            ticket.iniciaUID();
            ticket.setCliente(plan.getNovia());
            ticket.setFacturacionCliente();

            // Establecemos el vendedor
            Vendedor vendedorPlan = null;
            if (plan.getCodVendedor() != null && !plan.getCodVendedor().isEmpty()) {
                VendedoresServices vS = new VendedoresServices();
                vendedorPlan = vS.consultarVendedor(plan.getCodVendedor());
            }
            ticket.setVendedor(vendedorPlan);

            // Creamos una línea con los importes por abonar
            if (plan.getAbonadoSinUtilizar().compareTo(BigDecimal.ZERO) > 0) {
                LineasTicket lineas = new LineasTicket();
                ArticulosServices aS = ArticulosServices.getInstance();
                Articulos art = aS.getArticuloCod(Variables.getVariable(Variables.CODART_NOTA_CREDITO_RESERVAS));
                String codBarras = aS.consultarCodigoBarras(art.getCodart());
                art.setCodimp(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL); // marcamos exento de iva
//                BigDecimal precioSinIva = Numero.getAntesDePorcentajeR(plan.getAbonadoSinDto().subtract(plan.getAbonadoUtilizado()), Sesion.getEmpresa().getPorcentajeIva());
                BigDecimal precioSinIva = Numero.getAntesDePorcentajeR(valorRematar, Sesion.getEmpresa().getPorcentajeIva());
                ///se agregar el campo new BigDecimal(BigInteger.ZERO) costoLanded RD
                LineaTicket linea = new LineaTicket(
                        codBarras,
                        art,
                        1,
                        precioSinIva,
                        valorRematar,new BigDecimal(BigInteger.ZERO));
                linea.setDescuentoFinalDev(BigDecimal.ZERO);
                linea.setCodimp(art.getCodimp());
                lineas.getLineas().add(linea);
                // Inicializamos los totales
                ticket.setLineas(lineas);
                ticket.inicializaTotales(valorRematar);

                // Obtenemos un ID_TICKET unico
                ticket.setId_ticket(ServicioContadoresCaja.obtenerContadorFactura());

                // Creamos un pago con medio de pago ABONO por el total del ticket (total de abonos sobrantes)
                Pago pag = new Pago(ticket.getPagos(), null);
                pag.setMedioPagoActivo(MedioPagoBean.getMedioPagoAbonoReservacion());
                pag.setSaldoInicial(valorRematar);
                pag.setTotal(valorRematar);
                pag.establecerDescuento(BigDecimal.ZERO);
                pag.establecerEntregado("" + valorRematar);
                pag.recalcularFromTotal(valorRematar);
                ticket.crearNuevaLineaPago(pag);

                TicketService.escribirTicket(em, ticket, false);

                for(AbonoPlanNovio abonoPlanNovio : plan.getAbonoPlanNovioList()){
                    if(abonoPlanNovio.getAbonoPlanNovioPK().getCodAlmAbono() != null && abonoPlanNovio.getAbonoPlanNovioPK().getCodAlmAbono().equals(Sesion.getTienda().getAlmacen().getCodalm()) &&
                            abonoPlanNovio.getEstadoLiquidacion() == null && abonoPlanNovio.getAnulado().equals('N')){
                        abonoPlanNovio.setEstadoLiquidacion("S");
                        abonoPlanNovio.setProcesado('N');
                        abonoPlanNovio.setFechaLiquidacion(new Date());
                        abonoPlanNovio.setUidReferencia(ticket.getUid_ticket());
                        PlanNovioDao.modifica(em, abonoPlanNovio);
                    }
                }
                            
                // Creamos la Devolucion.                
                TicketOrigen to = TicketOrigen.getTicketOrigen(ticket);
                dev = new Devolucion(to, ticket, new MotivoDevolucion(4, "OTROS"), "RESERVACION, IDPLAN: "+plan.getCodPlanAsString(), "LIQUIDACIÓN " + plan.getTitulo(), true);
                dev.setCodVendedor(ticket.getCajero().getUsuario());
                DevolucionesServices.realizarNotaCreditoPlanNovios(em, dev, linea, true);
            }
            if(esLocalOrigen){
                //Actualizamos el documento asociado
                DocumentosBean docu = DocumentosService.consultarDocByUniqueKey(DocumentosBean.PLAN_NOVIO, plan.getPlanNovioPK().getCodalm(), DocumentosBean.CODCAJA, plan.getPlanNovioPK().getIdPlan().toString());
                docu.setEstado("L");
                SqlSession sql = new SqlSession();
                sql.openSession(SessionFactory.openSession(Connection.getConnection(em)));  
                DocumentosService.updateDocumentosWithSql(docu, sql);
            }
            em.getTransaction().commit();
            if (dev != null) {
                DevolucionesServices.generarKardexVentas(dev);
            }
            
            // IMPRIMIMOS LOS TICKETS
            if (plan.getAbonadoSinUtilizar().compareTo(BigDecimal.ZERO) > 0) {
                PrintServices.getInstance().limpiarListaDocumentos();
                PrintServices.getInstance().imprimirTicket(ticket);
                DocumentosService.crearDocumento(ticket, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.FACTURA);
                PrintServices.getInstance().limpiarListaDocumentos();

                //Guardamos también la nota de crédito en base de datos
                PrintServices.getInstance().limpiarListaDocumentos();
                PrintServices.getInstance().imprimirTicketDevolucion(dev);
                DocumentosService.crearDevolucion(dev, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.NOTA_CREDITO);
                PrintServices.getInstance().limpiarListaDocumentos();
            }
            
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("Error modificando Plan de Novios en Base de datos :" + e.getMessage(), e);
            throw new PlanNovioException("Error modificando Plan de Novios en Base de datos", e);
        } finally {
            em.close();
        }
    }

    /**
     * Método que liquida un plan Novio
     * @param plan
     * @param lista
     * @throws PlanNovioException 
     */
    public static void comprarConAbonos(PlanNovio plan, PlanNovioOBJ planNovio, TicketS ticket, List<ArticuloPlanNovio> lista) throws PlanNovioException {
        log.info("DAO(comprarConAbonos): Comprando artículo con abono en plan de novio");
        
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        //crearPagoArticulos(ticket, this, this.invitado, this.vendedorSeleccionado);

        try {
            em.getTransaction().begin();

            realizarPagoArticulos(ticket, planNovio, null, planNovio.getVendedorSeleccionado(), true, em, lista);

            // Aqui se tiene en cuenta la modificación de los artículos para los totales           
            plan.refrescaTotales();
            plan.setProcesado('N');
            PlanNovioDao.modifica(em, plan);

            em.getTransaction().commit();

            PrintServices.getInstance().limpiarListaDocumentos();
            PrintServices.getInstance().imprimirFacturaPagoPlanArticulo(ticket, planNovio, planNovio.getInvitado());

            DocumentosService.crearDocumento(ticket, PrintServices.getInstance().getDocumentosImpresos(), DocumentosBean.FACTURA);
            PrintServices.getInstance().limpiarListaDocumentos();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("comprarConAbonos - Error modificando Plan de Novios en Base de datos :" + e.getMessage(), e);
            throw new PlanNovioException("Error modificando Plan de Novios en Base de datos");
        } finally {
            em.close();
        }
    }

    /**
     * Método que modifica un invitado de un plan Novio
     * @param invitadoGestionado
     * @param plan
     * @throws PlanNovioException 
     */
    public static void modificarInvitado(InvitadoPlanNovio invitadoGestionado, PlanNovio plan) throws PlanNovioException {
        log.info("DAO(modificarInvitado): Modificando invitado en plan de novio");
        
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            invitadoGestionado.setProcesado('N');
            PlanNovioDao.modifica(em, invitadoGestionado);
            plan.setProcesado('N');
            PlanNovioDao.modifica(em, plan);

            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error modificando Plan de Novios en Base de datos :" + e.getMessage(), e);
            throw new PlanNovioException("Error modificando Invitado de Plan de Novios en Base de datos");
        } finally {
            em.close();
        }
    }

    public static void refrescaInvitadosPlan(PlanNovio plan) throws PlanNovioException {
        log.info("DAO(refrescaInvitadosPlan): Refrescando invitado en plan de novio");
        EntityManager em;

      //  if (plan.getPlanNovioPK().getCodalm().isEmpty() || plan.getPlanNovioPK().getCodalm().equals(Sesion.getTienda().getAlmacen().getCodalm())) {
            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
        //} else {
        //    EntityManagerFactory emfc = Sesion.getEmfc();
        //    em = emfc.createEntityManager();
        //}

        try {
            em.getTransaction().begin();
            PlanNovioDao.refrescaInvitadosPlan(em, plan);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error modificando Plan de Novios en Base de datos :" + e.getMessage(), e);
            throw new PlanNovioException("Error refrescando Invitado de Plan de Novios desde Base de datos");
        } finally {
            em.close();
        }

    }

    public static void accionEliminaInvitado(PlanNovio plan, int lineaSelecionada) throws PlanNovioException {
        log.info("DAO(accionEliminaInvitado): Eliminando invitado en plan de novio");
        
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            InvitadoPlanNovio inv = plan.getListaInvitados().get(lineaSelecionada);
            if (!inv.getAbonoPlanNovioList().isEmpty() || !inv.getArticuloPlanNovioList().isEmpty()) {
                log.debug("invitado con abonos o pagos a artículos id" + inv.getInvitadoPlanNovioPK().getIdInvitado() + "-" + inv.getInvitadoPlanNovioPK().getIdPlan());
                throw new PlanNovioException("El invitado ha realizado compras y/o abonos. No es posible eliminarlo");
            }
            PlanNovioDao.elimina(em, inv);
            plan.getListaInvitados().remove(lineaSelecionada);
            plan.setProcesado('N');
            PlanNovioDao.modifica(em, plan);
            em.getTransaction().commit();
        } catch (PlanNovioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error eliminando invitado del Plan Novios :" + e.getMessage(), e);
            throw new PlanNovioException("Error eliminando invitado del Plan Novios. No se pudo realizar la operación");
        } finally {
            em.close();
        }
    }

    public static void modificarPlan(PlanNovio plan) throws PlanNovioException {
        log.info("DAO(modificarPlan): Modificando plan de novio");
        
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            plan.setProcesado('N');
            PlanNovioDao.modifica(em, plan);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("modificarPlan() - Error modificaondo Plan Novios :" + e.getMessage(), e);
            throw new PlanNovioException("Error modificaondo Plan Novios. No se pudo realizar la operación");
        } finally {
            em.close();
        }
    }

    public static void anularCompraArticulo(EntityManager em, String codalm, String uid_ticket) throws PlanNovioException{
        log.info("DAO(anularCompraArticulo): Anulando compra de artículo de plan de novio");
        
        List<ArticuloPlanNovio> articulos = PlanNovioDao.consultarArticulosUidTicket(em, uid_ticket);
        if(!articulos.isEmpty()){
            for(ArticuloPlanNovio articulo:articulos){
                articulo.setComprado('N');
                articulo.setTotalPagadoConDscto(null);
                articulo.setProcesado(false);
                articulo.setId_ticket(null);
                articulo.setCodVendedor(null);
                articulo.setInvitadoPlanNovio(null);
                articulo.setEntregado('N');
                articulo.setUidTicket(null);
                articulo.setIdLineaTicket(null);
            }
            PlanNovioDao.actualizarArticulos(em, articulos);

            //Actualizamos el plan novio para que se procese en central
            PlanNovio plan = PlanNovioDao.consultaPlanNovio(em, codalm, articulos.get(0).getPlanNovio().getPlanNovioPK().getIdPlan());
            PlanNovioDao.modificaNoProcesado(em, plan);
        }
    }
}
