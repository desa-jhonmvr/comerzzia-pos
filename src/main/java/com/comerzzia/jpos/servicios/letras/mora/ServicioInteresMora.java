/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.letras.mora;

import com.comerzzia.jpos.persistencia.letras.mora.InteresMoraDao;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;

/**
 *
 * @author amos
 */
public class ServicioInteresMora {
    
        protected static Logger log = Logger.getMLogger(ServicioInteresMora.class);

    
    public static BigDecimal consultarInteresMora() {
        Connection connCredito = new Connection();
        try {
            log.debug("consultarInteresMora() - Consultando interés por mora vigente a fecha actual...");
            connCredito.abrirConexion(Database.getConnectionCredito());
            BigDecimal interes = InteresMoraDao.consultarInteres(connCredito);
            if (interes == null){
                log.warn("consultarInteresMora() - INTERES POR MORA:: No se han encontrado resultado en la consulta del interés por mora vigente. Se tomará interés cero.");
                interes = BigDecimal.ZERO;
            }
            return interes;
        }
        catch (Exception e) {
            String mensaje = "Error al consultar interés por mora: " + e.getMessage();
            log.error("consultarInteresMora() - No se pudo obtener interés por mora. Se utilizará interés cero.");
            log.error("consultarInteresMora() - " + mensaje, e);
            return BigDecimal.ZERO;
        }
        finally {
            connCredito.cerrarConexion();
        }
        
    }
    
}
