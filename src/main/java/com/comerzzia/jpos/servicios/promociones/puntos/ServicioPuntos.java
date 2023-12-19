
package com.comerzzia.jpos.servicios.promociones.puntos;

import com.comerzzia.jpos.persistencia.puntos.acumulacion.AcumulacionBean;
import com.comerzzia.jpos.persistencia.puntos.acumulacion.AcumulacionExample;
import com.comerzzia.jpos.persistencia.puntos.acumulacion.AcumulacionMapper;
import com.comerzzia.jpos.persistencia.puntos.consumo.ConsumoBean;
import com.comerzzia.jpos.persistencia.puntos.consumo.ConsumoExample;
import com.comerzzia.jpos.persistencia.puntos.consumo.ConsumoMapper;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketPuntosBean;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.session.SqlSession;
import java.util.Date;

public class ServicioPuntos {

    protected static Logger log = Logger.getMLogger(ServicioPuntos.class);

    public static Integer consultarPuntosCliente(String codCliente) {
        SqlSession sqlSession = new SqlSession();
        try {
            log.debug("consultarPuntosCliente() - Consultando puntos del cliente: " + codCliente);
            sqlSession.openSession(SessionFactory.openSession());
            AcumulacionMapper mapperAcumula = sqlSession.getMapper(AcumulacionMapper.class);
            ConsumoMapper mapperConsumo = sqlSession.getMapper(ConsumoMapper.class);
            Integer puntosAcumulados = mapperAcumula.selectSumByCodCliente(codCliente);
            Integer puntosConsumidos = mapperConsumo.selectSumByCodCliente(codCliente);
            puntosAcumulados = puntosAcumulados == null ? 0 : puntosAcumulados;
            puntosConsumidos = puntosConsumidos == null ? 0 : puntosConsumidos;
            
            int puntos = puntosAcumulados - puntosConsumidos;
            log.debug("consultarPuntosCliente() - Puntos acumulados: " + puntosAcumulados 
                    + " Puntos consumidos: " + puntosConsumidos 
                    + " Total puntos: " + puntos);
            return puntos;
        }
        catch (Exception e) {
            log.error("consultarPuntosCliente() - Error al consultar puntos de cliente: " + e.getMessage(), e);
            log.error("consultarPuntosCliente() - SE ASUMIRÁ QUE EL CLIENTE TIENE CERO PUNTOS PARA CONTINUAR CON LA OPERACIÓN. ");
            return 0;
        }
        finally {
            sqlSession.close();
        }
    }

    public static void acumularPuntos(Connection conn, TicketS ticket) throws PuntosException {
        try {
            TicketPuntosBean puntosTicket = ticket.getPuntosTicket();
            if (!puntosTicket.isAcumulaPuntos()){
                log.debug("acumularPuntos() - La venta no acumula puntos.");
                return;
            }
            SqlSession sqlSession = new SqlSession();
            log.debug("acumularPuntos() - Acumulamos " + puntosTicket.getPuntosAcumulados() + " puntos al cliente: " + puntosTicket.getClienteAcumulacion());
            sqlSession.openSession(SessionFactory.openSession(conn));
            AcumulacionMapper mapperAcumula = sqlSession.getMapper(AcumulacionMapper.class);
            AcumulacionBean acumulacion = new AcumulacionBean();
            acumulacion.setCodAlm(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            acumulacion.setCodCliente(puntosTicket.getClienteAcumulacion().getIdentificacion());
            acumulacion.setFecha(new Date());
            acumulacion.setProcesado(false);
            acumulacion.setPuntos(puntosTicket.getPuntosAcumulados());
            acumulacion.setUidTicket(ticket.getUid_ticket());
            mapperAcumula.insert(acumulacion);
        }
        catch (Exception e) {
            log.error("acumularPuntos() - Error salvando puntos acumulados en la venta: " + e.getMessage(), e);
            throw new PuntosException("Error salvando puntos acumulados en la venta.", e);
        }
    }
    
    public static void consumirPuntos(Connection conn, TicketS ticket) throws PuntosException {
        try {
            TicketPuntosBean puntosTicket = ticket.getPuntosTicket();
            Integer puntosConsumidos = puntosTicket.getPuntosConsumidos();
            if (puntosConsumidos == null || puntosConsumidos == 0){
                log.debug("consumirPuntos() - La venta no consume puntos.");
                return;
            }
            SqlSession sqlSession = new SqlSession();
            log.debug("consumirPuntos() - " + puntosConsumidos + " puntos consumidos por el cliente: " + ticket.getCliente());
            sqlSession.openSession(SessionFactory.openSession(conn));
            ConsumoMapper mapper = sqlSession.getMapper(ConsumoMapper.class);
            ConsumoBean consumo = new ConsumoBean();
            consumo.setCodAlm(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            consumo.setCodCliente(ticket.getCliente().getIdentificacion());
            consumo.setFecha(new Date());
            consumo.setProcesado(false);
            consumo.setPuntos(puntosConsumidos);
            consumo.setUidTicket(ticket.getUid_ticket());
            mapper.insert(consumo);
        }
        catch (Exception e) {
            log.error("consumirPuntos() - Error salvando puntos consumidos en la venta: " + e.getMessage(), e);
            throw new PuntosException("Error salvando puntos consumidos en la venta.", e);
        }
    }
    
        public static void anularPuntos(SqlSession sqlSession, String uidTicket) throws PuntosException {
        try {
            Integer puntos = 0;
           
            log.debug("anularPuntos() -  anulando puntos consumidos para factura: " + uidTicket);
            ConsumoMapper mapper = sqlSession.getMapper(ConsumoMapper.class);
            ConsumoBean consumo = new ConsumoBean();
            
            consumo.setProcesado(false);
            consumo.setPuntos(puntos);
            
            ConsumoExample filtro = new ConsumoExample();
            filtro.or().andUidTicketEqualTo(uidTicket);
            mapper.updateByExampleSelective(consumo, filtro);
            
            
            log.debug("anularPuntos() - Acumulamos puntos acumulados para factura: " + uidTicket);
            
            AcumulacionMapper mapperAcumula = sqlSession.getMapper(AcumulacionMapper.class);
            AcumulacionBean acumulacion = new AcumulacionBean();
            
            acumulacion.setProcesado(false);
            acumulacion.setPuntos(puntos);
            
            AcumulacionExample filtroAcumulacion = new AcumulacionExample();            
            filtroAcumulacion.or().andUidTicketEqualTo(uidTicket);
            
            mapperAcumula.updateByExampleSelective(acumulacion, filtroAcumulacion);
            
        }
        catch (Exception e) {
            log.error("anularPuntos() - Error estableciendo puntos a cero en la venta: " + e.getMessage(), e);
            throw new PuntosException("Error modificando puntos del cliente", e);
        }
    }
    
    
}
