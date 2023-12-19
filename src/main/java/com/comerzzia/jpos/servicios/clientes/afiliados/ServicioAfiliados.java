package com.comerzzia.jpos.servicios.clientes.afiliados;

import com.comerzzia.jpos.persistencia.clientes.afiliados.TipoAfiliadoBean;
import com.comerzzia.jpos.persistencia.clientes.afiliados.TipoAfiliadosDao;
import com.comerzzia.jpos.util.db.Database;
import java.sql.SQLException;

import com.comerzzia.jpos.util.db.Connection;
import es.mpsistemas.util.log.Logger;
import java.net.URL;
import java.util.Map;
import javax.swing.ImageIcon;

public class ServicioAfiliados {

    /**
     * Logger
     */
    protected static Logger log = Logger.getMLogger(ServicioAfiliados.class);

    public static Map<String, TipoAfiliadoBean> consultarTiposAfiliados() throws AfiliadosException {
        Connection conn = new Connection();

        try {
            log.debug("consultarTiposAfiliados() - Consultando todos los tipos de afiliados");
            conn.abrirConexion(Database.getConnection());
            Map<String, TipoAfiliadoBean> res = TipoAfiliadosDao.consultar(conn);
            for (TipoAfiliadoBean tab : res.values()) {
                try {
                    URL myurl = ServicioAfiliados.class.getResource("../../../../../../imagenesTarjeta/tarjetaSocio" + tab.getCodTipoAfiliado() + ".PNG");
                    tab.setImagenTarjetaAfiliado(new ImageIcon(myurl));

                    //D:\proyectos\AAPP\SUKASA\TPV\src\main\java\com\comerzzia\jpos\servicios\clientes\afiliados
                    
                    //D:\proyectos\AAPP\SUKASA\TPV\src\main\java\
                }
                catch (Exception ex) {
                    log.info("consultarTiposAfiliados() - Intentando obtener imagen de tema en pantalla Clientes ");
                }
            }
            return res;
        }
        catch (SQLException e) {
            log.error("consultarTiposAfiliados() - " + e.getMessage());
            String mensaje = "Error al consultar todos los tipos de afiliados: " + e.getMessage();

            throw new AfiliadosException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
    }
}
