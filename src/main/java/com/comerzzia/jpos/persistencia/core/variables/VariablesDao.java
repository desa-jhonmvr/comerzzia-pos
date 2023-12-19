/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.core.variables;

import com.comerzzia.jpos.entity.db.VariableAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author MGRI
 */
public class VariablesDao {

    private static final Logger LOG_POS = Logger.getMLogger(VariablesDao.class);

    public static List<VariableAlm> consultaVariables() throws Exception {

        List<VariableAlm> resultado = new LinkedList<>();

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            Query consulta = em.createNativeQuery("SELECT ID_VARIABLE, DESCRIPCION, VALOR_DEFECTO, VALOR, PESO FROM CONFIG_VARIABLES ");
            List<Object[]> resultadoLista = consulta.getResultList();

            for (Object[] ob : resultadoLista) {
                VariableAlm var = new VariableAlm();
                var.setIdVariable((String) ob[0]);
                var.setValor((String) ob[3]);
                var.setValorDefecto((String) ob[2]);
                resultado.add(var);
            }

        } catch (NoResultException e) {
            LOG_POS.debug("DAO: No se encontraron variables de almacen");
        } catch (Exception e) {
            LOG_POS.error("DAO: Error al Consultar las variables de almacen", e);
            throw e;
        } finally {
            em.close();
        }
        return resultado;
    }

    /**
     * Obtiene la hora y fecha del servidor del local
     *
     * @return
     * @throws Exception
     */
    public static Date consultaFechaHoraServidor() throws Exception {

        Date fechaActual = null;

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            Query consulta = em.createNativeQuery("select sysdate fecha from dual");
            fechaActual = (Date) consulta.getSingleResult();
        } catch (Exception e) {
            LOG_POS.error("DAO: Error al Consultar las variables de almacen", e);
            throw e;
        } finally {
            em.close();
        }
        return fechaActual;
    }
}
