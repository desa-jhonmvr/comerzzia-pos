/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.listapda;

import com.comerzzia.jpos.persistencia.listapda.SesionPdaBean;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaExample;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaMapper;
import com.comerzzia.jpos.persistencia.listapda.detalle.DetalleSesionPdaExample;
import com.comerzzia.jpos.persistencia.listapda.detalle.DetalleSesionPdaMapper;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.session.SqlSession;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class ListaPDAServices {

    private static Logger log = Logger.getMLogger(ListaPDAServices.class);

    public static SesionPdaBean consultar(String codigoListaPDA) throws ListaPDAException {
        SesionPdaBean res = null;
        SqlSession sql = new SqlSession();

        try {
            sql.openSession(SessionFactory.openSession());
            SesionPdaMapper mapper = sql.getMapper(SesionPdaMapper.class);
            SesionPdaExample example = new SesionPdaExample();
            example.or().andCodigoEqualTo(codigoListaPDA).andUtilizadoEqualTo(false);
            
            res = mapper.selectByExample(example).get(0);
            
            DetalleSesionPdaMapper mapperDetalle = sql.getMapper(DetalleSesionPdaMapper.class);
            DetalleSesionPdaExample exampleDetalle = new DetalleSesionPdaExample();
            exampleDetalle.or().andUidSesionPdaEqualTo(res.getUidSesionPda());
            
            res.setDetalleSesionPdaList(mapperDetalle.selectByExample(exampleDetalle));
            
            return res;
        }
        catch (IndexOutOfBoundsException e) {
            log.warn("No se encontró una Sesión PDA con el identificador introducido: " + codigoListaPDA, e);
            throw new ListaPDAException("No se encontró una Sesión PDA con el identificador introducido", e);
        }        
        catch (Exception e) {
            log.error("consultarDetalle(): Error" + e.getMessage(), e);            
            throw new ListaPDAException("Error consultando sesión de PDA", e);
        }
        finally {
            sql.close();
        }
    }
    
    public static void marcarComoUtilizado(SesionPdaBean referenciaSesionPDA) throws ListaPDAException {
        SqlSession sql = new SqlSession();

        try {      
            sql.openSession(SessionFactory.openSession());
            SesionPdaMapper mapper = sql.getMapper(SesionPdaMapper.class);            
            referenciaSesionPDA.setUtilizado(true);
            
            mapper.updateByPrimaryKey(referenciaSesionPDA);
            
            sql.commit();
        }
        catch (Exception e) {
            log.error("consultarDetalle(): Error" + e.getMessage(), e);       
            sql.rollback();
            throw new ListaPDAException("Error consultando sesión de PDA", e);
        }
        finally {
            sql.close();
        }        
    }
    
    public static void marcarComoUtilizado(SqlSession sql, SesionPdaBean referenciaSesionPDA) throws ListaPDAException {

        try {      
            SesionPdaMapper mapper = sql.getMapper(SesionPdaMapper.class);            
            referenciaSesionPDA.setUtilizado(true);
            
            mapper.updateByPrimaryKey(referenciaSesionPDA);
        }
        catch (Exception e) {
            log.error("consultarDetalle(): Error" + e.getMessage(), e);            
            throw new ListaPDAException("Error consultando sesión de PDA", e);
        }   
    }    
    
}
