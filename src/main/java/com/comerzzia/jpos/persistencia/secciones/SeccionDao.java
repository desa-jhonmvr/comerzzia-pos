/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.secciones;

import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author SMLM
 */
public class SeccionDao extends MantenimientoDao {
    private static Logger log = Logger.getMLogger(SeccionDao.class);
    private static String TABLA = "D_SECCIONES_TBL";
    
    public static List<SeccionBean> consultarSecciones(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SeccionBean> secciones = new ArrayList<SeccionBean>();
        String sql = "SELECT * FROM " + getNombreElementoEmpresa(TABLA) +
                     "WHERE ACTIVO = 'S' ORDER BY DESSECCION";        
        try{
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarSecciones() - " + pstmt);
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                SeccionBean seccion = new SeccionBean();
                seccion.setCodseccion(rs.getString("CODSECCION"));
                seccion.setDesseccion(rs.getString("DESSECCION"));
                seccion.setActivo(true);
                secciones.add(seccion);
            }
            return secciones;
        }
        catch (NoResultException e) {
            log.info("No existe ninguna sección: "+e);
            throw new Exception();
        }
        catch (Exception ex) {
            log.error("Error consultando las secciones: "+ex);
            throw new Exception();
        }
    }
}
