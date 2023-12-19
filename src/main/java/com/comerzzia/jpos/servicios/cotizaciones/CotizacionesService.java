/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.cotizaciones;

import com.comerzzia.jpos.persistencia.cotizaciones.CotizacionBean;
import com.comerzzia.jpos.persistencia.cotizaciones.CotizacionExample;
import com.comerzzia.jpos.persistencia.cotizaciones.CotizacionMapper;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.print.documentos.DocumentoException;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.exception.PersistenceException;
import es.mpsistemas.util.mybatis.session.SqlSession;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author SMLM
 */
public class CotizacionesService {
    protected static Logger log = Logger.getMLogger(CotizacionesService.class);
    
    public static void crearCotizacion(CotizacionBean cotizacion, TicketS ticketNuevaVenta, List<DocumentosImpresosBean> documentosImpresos, String string) throws CotizacionException, ContadorException {
        SqlSession session = new SqlSession();
        try {
            Fecha today = new Fecha();
            cotizacion.setFecha(today);
            //A la fecha de hoy le tenemos que sumar los dias de vigencia para la fecha de vigencia
            int diasVigencia = Variables.getVariableAsInt(Variables.POS_COTIZACION_PLAZO_VIGENCIA);
            Fecha vigencia = new Fecha();
            vigencia.sumaDias(diasVigencia);
            cotizacion.setFechaVigencia(vigencia);
            cotizacion.setTotal(ticketNuevaVenta.getTotales().getTotalAPagar()); 
            
        
            log.debug("crearCotizacion() - creando documento con uid: " + cotizacion.getUidCotizacion());
            session.openSession(SessionFactory.openSession());
            CotizacionMapper mapper = session.getMapper(CotizacionMapper.class);
            mapper.insert(cotizacion);
            
            Connection conn = new Connection();
            conn.abrirConexion(Database.getConnection());
            ServicioContadoresCaja.incrementarContadorCotizacion(conn);     
            
            //Una vez hemos creado la cotizacion, creamos el documento
            DocumentosService.crearDocumentoCotizacion(ticketNuevaVenta, documentosImpresos, string, session);
            
            session.commit();
        } catch (DocumentoException e) {
            String msg = "Error inesperado creando nueva cotizacion en base de datos: " + e.getMessage();
            log.error("crearCotizacion() - " + msg, e);
            session.rollback();
            throw new CotizacionException(msg, e);
        } catch (SQLException e) {
            String msg = "Error inesperado creando nueva cotizacion en base de datos: " + e.getMessage();
            log.error("crearCotizacion() - " + msg, e);
            session.rollback();
            throw new CotizacionException(msg, e);
        } catch (ContadorException e) {
            String msg = "Error registrando nueva cotizacion en base de datos: " + e.getMessage();
            log.error("crearCotizacion() - " + msg, e); 
            session.rollback();
            throw new ContadorException(msg, e);
        } catch(PersistenceException e){
            String msg = "Error registrando nueva cotizacion en base de datos: " + e.getMessage();
            log.error("crearCotizacion() - " + msg, e);
            session.rollback();         
            throw new CotizacionException(msg, e);          
        } catch (Exception e) {
            String msg = "Error registrando nueva cotizacion en base de datos: " + e.getMessage();
            log.error("crearCotizacion() - " + msg, e); 
            session.rollback();
            throw new CotizacionException(msg, e);            
        }
        finally {
            session.close();
        }          
    }
    
    public static List<CotizacionBean> consultarCotizaciones(ParamBuscarCotizaciones paramCotizaciones) throws CotizacionException {
         SqlSession session = new SqlSession();
         try{
            log.debug("consultarCotizaciones() - consultando documentos");
            session.openSession(SessionFactory.openSession());
            CotizacionMapper mapper = session.getMapper(CotizacionMapper.class);
            
            CotizacionExample example = new CotizacionExample();
            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd-MMM-yyyy HH:mm", new Locale("es","ES"));
            String hoy = formatoDeFecha.format(new Date());

            example.or().andFechaMayorWithoutNull(paramCotizaciones.getFechaDesde())
                        .andFechaMenorWithoutNull(paramCotizaciones.getFechaHasta())
                        .andCodCliEqualWithoutNull(paramCotizaciones.getCodcli())
                        .andUsuarioEqualWithoutNull(paramCotizaciones.getVendedor())
                        .andFechaVigenciaIsCaducado(new Fecha(hoy, "dd-MMM-yyyy"), paramCotizaciones.getEstado());
            
            
            example.setOrderByClause("FECHA DESC");
            
            List<CotizacionBean> cotizaciones = mapper.selectByExampleWithCliente(example);
            
            for(CotizacionBean cotizacion:cotizaciones){
                if((new Fecha(hoy,"dd-MMM-yyyy")).despues(cotizacion.getFechaVigencia())){
                    cotizacion.setCaducado(true);
                } else {
                    cotizacion.setCaducado(false);
                }
           }
            
            return cotizaciones;
         }
        catch(Exception e){
            String msg = "Error inesperado consultando cotizaciones en base de datos: " + e.getMessage();
            log.error("consultarCotizaciones() - " + msg, e);
            throw new CotizacionException(msg, e);
        }
         finally {
             session.close();
         }
    }
    
}
