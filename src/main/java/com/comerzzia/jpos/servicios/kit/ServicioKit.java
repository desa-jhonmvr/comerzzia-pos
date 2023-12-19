/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.kit;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.persistencia.kit.KitBean;
import com.comerzzia.jpos.persistencia.kit.KitDao;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class ServicioKit {

private static Logger log = Logger.getMLogger(ServicioKit.class);

public static List<KitBean> consultarKitsArticulo(Articulos articuloBuscado, Boolean obligatorio) throws KitException {
        List<KitBean> res = new ArrayList<KitBean>();
        Connection conn = new Connection();
        try {
            log.debug("consultarKitsArticulo() - Consultando Stock de tiendas... ");
            conn.abrirConexion(Database.getConnection());
            res = KitDao.consultarKitsArticulo(conn, articuloBuscado.getCodart(),obligatorio);
        }
        catch (SQLException e) {
            log.error("consultarKitsArticulo() - " + e.getMessage(),e);
            String mensaje = "Error consultando Kit de artículo: " + e.getMessage();
            throw new KitException(mensaje, e);
        }
        catch (Exception e) {
            log.error("consultarKitsArticulo() - " + e.getMessage(),e);
            String mensaje = "Error consultando kit de artículo: " + e.getMessage();
            throw new KitException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
        return res;
    }

}
