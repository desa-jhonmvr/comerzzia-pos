/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.logs.transaccioneserradas;

import com.comerzzia.jpos.persistencia.logs.logacceso.LogException;
import com.comerzzia.jpos.persistencia.logs.transaccioneserradas.TransaccionErradaBean;
import com.comerzzia.jpos.persistencia.logs.transaccioneserradas.TransaccionesErradasDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import es.mpsistemas.util.log.Logger;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.db.Connection;
import es.mpsistemas.util.fechas.Fecha;
import java.sql.SQLException;

/**
 *
 * @author MGRI
 */
public class ServicioTransaccionesErradas {

    private static Logger log = Logger.getMLogger(ServicioTransaccionesErradas.class);

    public static void crearTransaccionErrada(Long idTransaccion, Throwable error, String tipoTransaccion)  {
        if (error instanceof TicketException){
            String causa = ((TicketException)error).getCausa();
            if (causa == null){
                crearTransaccionErrada(idTransaccion, TransaccionErradaBean.TIPO_PROCESANDO , tipoTransaccion);
            }
            else{ // equals("BBDD")
                crearTransaccionErrada(idTransaccion, TransaccionErradaBean.TIPO_GUARDANDO, tipoTransaccion);   
            }
        }
    }

    public static void crearTransaccionErrada(Long idTransaccion, String error, String tipoTransaccion)  {

        Connection conn = new Connection();
        try {
            log.debug("crearTransaccionErrada() - Creando log de transacción errada... ");
            conn.abrirConexion(Database.getConnection());
            conn.iniciaTransaccion();
            
            TransaccionErradaBean tranErr = new TransaccionErradaBean();
            tranErr.setCodAlm(Sesion.getTienda().getCodalm());
            tranErr.setCodCaja(Sesion.getCajaActual().getCajaActual().getCodcaja());
            tranErr.setIdTransaccion(idTransaccion);
            tranErr.setTipotransaccion(tipoTransaccion);
            tranErr.setUsaurio(Sesion.getUsuario().getUsuario());
            tranErr.setFechaHora(new Fecha());
            tranErr.setError(error);
            tranErr.setProcesado("N");
 
            TransaccionesErradasDao.insert(conn,tranErr);           
            conn.commit();
            conn.finalizaTransaccion();
        }
        catch (SQLException e) {
            conn.deshacerTransaccion();
            log.error("crearTransaccionErrada() - " + e.getMessage());
            String mensaje = "Error creando log de transacción erronea " + e.getMessage();
            
        }
        catch (Exception e) {
            log.error("crearTransaccionErrada() - " + e.getMessage());
            String mensaje = "Error creando log de transacción erronea " + e.getMessage();
            
        }
        finally {
            conn.cerrarConexion();
        }
    }


}
