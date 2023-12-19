/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.bonoEfectivo;

import com.comerzzia.jpos.persistencia.bonoEfectivo.BonoEfectivoBean;
import com.comerzzia.jpos.persistencia.bonoEfectivo.BonoEfectivoExample;
import com.comerzzia.jpos.persistencia.bonoEfectivo.BonoEfectivoMapper;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.session.SqlSession;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class BonoEfectivoServices {

    protected static Logger log = Logger.getMLogger(BonoEfectivoServices.class);

    public static List<BonoEfectivoBean> consultarLetrasPorCliente(String cedula) throws BonoEfectivoCambioException {
        SqlSession sqlSession = new SqlSession();
        try {
            log.debug("consultarLetrasPorCliente() - Consultamos bonos Efectivo del cliente: " + cedula);
            sqlSession.openSession(SessionFactory.openSession());
            BonoEfectivoMapper mapper = sqlSession.getMapper(BonoEfectivoMapper.class);

            BonoEfectivoExample example = new BonoEfectivoExample();
            example.or().andCodClienteEqualTo(cedula);
            List<BonoEfectivoBean> bonos = mapper.selectFromByExample(example);
            return bonos;
        } catch (Exception e) {
            String msg = "Error consultando bonoEfectivo " + cedula;
            log.error("consultarLetrasPorCliente() - " + msg, e);
            throw new BonoEfectivoCambioException(msg, e);
        } finally {
            sqlSession.close();
        }
    }

}
