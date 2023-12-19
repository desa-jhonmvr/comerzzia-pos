/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.secciones;

import com.comerzzia.jpos.persistencia.secciones.SeccionBean;
import com.comerzzia.jpos.persistencia.secciones.SeccionDao;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import java.util.List;

/**
 *
 * @author SMLM
 */
public class ServicioSecciones {
    protected static Logger log = Logger.getMLogger(ServicioSecciones.class);  
    
    public static List<SeccionBean> consultarSecciones() throws Exception{
        Connection conn = new Connection();
        try{
            log.debug("consultarSecciones() - Consultando todas las secciones");
            conn.abrirConexion(Database.getConnection());
            return SeccionDao.consultarSecciones(conn);
        } catch (Exception e){
            throw new Exception();
        } finally {
            conn.cerrarConexion();
        }
    }
}
