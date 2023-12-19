package com.comerzzia.jpos.servicios.core.contadores.caja;
import com.comerzzia.jpos.servicios.core.contadores.*;

import com.comerzzia.jpos.persistencia.core.contadores.caja.ContadoresCajaDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.util.logging.Level;

public class ServicioContadoresCaja {

    public static final String CONTADOR_COTIZACION      = "COTIZACION";
    public static final String CONTADOR_FACTURA         = "FACTURA"; 
    public static final String CONTADOR_GUIA_REMISION   = "GUIA_REMISION";
    public static final String CONTADOR_NOTA_CREDITO    = "NOTA_CREDITO";
    public static final String CONTADOR_ABONO_CREDITO   = "ABONO_CREDITO";
    public static final String CONTADOR_ABONO_LETRA     = "ABONO_LETRA";
    public static final String CONTADOR_CREDITO_TEMPORAL= "CREDITO_TEMPORAL";
    public static final String CONTADOR_GIFTCARD        = "GIFTCARD";
    public static final String CONTADOR_USO_GIFTCARD    = "USO_GIFTCARD";

    
    protected static Logger log = Logger.getMLogger(ServicioContadoresCaja.class);

    public static Long obtenerContadorCotizacion()throws ContadorException {
        return obtenerContador(CONTADOR_COTIZACION);
    }
    public static Long obtenerContadorFactura()throws ContadorException {
        return obtenerContador(CONTADOR_FACTURA);
    }
     public static Long obtenerContadorFacturaAnterior(String caja)throws ContadorException {
        return obtenerContadorPorCaja(CONTADOR_FACTURA,caja);
    }
    public static Long obtenerContadorGuiaRemision()throws ContadorException {
        return obtenerContador(CONTADOR_GUIA_REMISION);
    }
    public static Long obtenerContadorNotaCreditoAnterior(String caja)throws ContadorException {
       return obtenerContadorPorCaja(CONTADOR_NOTA_CREDITO,caja);
    }
    public static Long obtenerContadorNotaCredito()throws ContadorException {
        return obtenerContador(CONTADOR_NOTA_CREDITO);
    }
    //contado reimpresion ----
    public static Long obtenerContadorAbonoCredito()throws ContadorException {
        return obtenerContador(CONTADOR_ABONO_CREDITO);
    }
      public static Long obtenerContadorAbonoCreditoAnterior(String caja)throws ContadorException {
        return obtenerContadorPorCaja(CONTADOR_ABONO_CREDITO,caja);
    }
    public static Long obtenerContadorAbonoLetra()throws ContadorException {
        return obtenerContador(CONTADOR_ABONO_LETRA);
    }
     public static Long obtenerContadorAbonoLetraAnterior(String caja)throws ContadorException {
        return obtenerContadorPorCaja(CONTADOR_ABONO_LETRA,caja);
    }
    public static Integer obtenerContadorCreditoTemporal()throws ContadorException {
        return obtenerContador(CONTADOR_CREDITO_TEMPORAL).intValue();
    }
    public static Long obtenerContadorCreditoTemporalAnterior(String caja)throws ContadorException {
        return obtenerContadorPorCaja(CONTADOR_CREDITO_TEMPORAL,caja);
    }
    public static Integer obtenerContadorGiftcard()throws ContadorException {
        return obtenerContador(CONTADOR_GIFTCARD).intValue();
    }
     public static Integer obtenerContadorGiftcardAnterior(String caja)throws ContadorException {
        return obtenerContadorPorCaja(CONTADOR_GIFTCARD,caja).intValue();
    }
    public static Long obtenerContadorUsoGiftcard()throws ContadorException {
        return obtenerContador(CONTADOR_USO_GIFTCARD);
    } 
    public static Long obtenerContadorUsoGiftcardAnterior(String caja)throws ContadorException {
        return obtenerContadorPorCaja(CONTADOR_USO_GIFTCARD,caja);
    }
    public static void incrementarContadorCotizacion(Connection conn) throws ContadorException {
        incrementarContador(conn, CONTADOR_COTIZACION);
    }
    public static void incrementarContadorFactura(Connection conn) throws ContadorException {
        incrementarContador(conn, CONTADOR_FACTURA);
    }
    public static void incrementarContadorGuiaRemision(Connection conn) throws ContadorException {
        incrementarContador(conn, CONTADOR_GUIA_REMISION);
    }
    public static void incrementarContadorNotaCredito(Connection conn) throws ContadorException {
        incrementarContador(conn, CONTADOR_NOTA_CREDITO);
    }
    public static void incrementarContadorAbonoCredito(Connection conn) throws ContadorException {
        incrementarContador(conn, CONTADOR_ABONO_CREDITO);
    }
    public static void incrementarContadorAbonoLetra(Connection conn) throws ContadorException {
        incrementarContador(conn, CONTADOR_ABONO_LETRA);
    }
    public static void incrementarContadorCreditoTemporal(Connection conn) throws ContadorException {
        incrementarContador(conn, CONTADOR_CREDITO_TEMPORAL);
    }
    public static void incrementarContadorGiftcard(Connection conn) throws ContadorException {
        incrementarContador(conn, CONTADOR_GIFTCARD);
    }
    public static void incrementarContadorUsoGiftcard(Connection conn) throws ContadorException {
        incrementarContador(conn, CONTADOR_USO_GIFTCARD);
    }    
    public static void actualizarContadorCotizacion(Long valor) throws ContadorException {
        actualizarContador(CONTADOR_COTIZACION, valor);
    }
    public static void actualizarContadorFactura(Long valor) throws ContadorException {
        actualizarContador(CONTADOR_FACTURA, valor);
    }
    public static void actualizarContadorGuiaRemision(Long valor) throws ContadorException {
        actualizarContador(CONTADOR_GUIA_REMISION, valor);
    }
    public static void actualizarContadorNotaCredito(Long valor) throws ContadorException {
        actualizarContador(CONTADOR_NOTA_CREDITO, valor);
    }
    public static void actualizarContadorGiftcard(Long valor) throws ContadorException {
        actualizarContador(CONTADOR_GIFTCARD, valor);
    }
    public static void actualizarContadorUsoGiftcard(Long valor) throws ContadorException {
        actualizarContador(CONTADOR_USO_GIFTCARD, valor);
    }    
    
    private static Long obtenerContadorPorCaja(String idContador,String caja){
       Long valor = new Long(0);
        try {
            Connection conn = new Connection();
            log.debug("obtenerContador() - Obteniendo valor para contador:  " + idContador);
            conn.abrirConexion(Database.getConnection());
            conn.finalizaTransaccion(); // ponemos autocommit a true
           valor = ContadoresCajaDao.consultarPorCaja(conn, idContador,caja);
            log.debug("obtenerContador() - El valor para el contador obtenido es: " + valor);
            if(valor==null){
                  valor = new Long(0);
            }
           
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ServicioContadoresCaja.class.getName()).log(Level.SEVERE, null, ex);
        }
      
         return valor;
    }
    private static Long obtenerContador(String idContador) throws ContadorException {
        Connection conn = new Connection();
        try {
            log.debug("obtenerContador() - Obteniendo valor para contador:  " + idContador);
            conn.abrirConexion(Database.getConnection());
            conn.finalizaTransaccion(); // ponemos autocommit a true
            Long valor = ContadoresCajaDao.consultar(conn, idContador);
            log.debug("obtenerContador() - El valor para el contador obtenido es: " + valor);
            if (valor == null || valor == 0){
                conn.iniciaTransaccion();
                valor = Sesion.getTienda().getSriTienda().getCajaActiva().getContador(idContador);
                log.debug("obtenerContador() - Se utilizará contador configurado en caja por SRI: " + valor);
                log.debug("obtenerContador() - Insertamos este nuevo valor en tabla de contadores para su próximo uso: " + valor);
                ContadoresCajaDao.insert(conn, idContador, valor);
                conn.commit();
                conn.finalizaTransaccion();
            }
            return valor;
        }
        catch (SQLException e) {
            conn.deshacerTransaccion();
            log.error("obtenerContador() - " + e.getMessage());
            String mensaje = "Error al obtener valor de contador o insertar nuevo valor : " + e.getMessage();

            throw new ContadorException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
    }
    private static void incrementarContador(Connection conn, String idContador) throws ContadorException {
        try {
            log.debug("incrementarContador() - Incrementando valor para contador:  " + idContador);
            if (ContadoresCajaDao.incrementar(conn, idContador)){
                log.debug("incrementarContador() - Contador incrementado con éxito. ");
                return;
            };
            log.warn("incrementarContador() - Contador no se pudo incrementar. Reintentamos la operación... ");
            if (ContadoresCajaDao.incrementar(conn, idContador)){
                log.debug("incrementarContador() - Contador incrementado con éxito. ");
                return;
            }
            log.error("incrementarContador() - El contador no se ha podido incrementar. ");
        }
        catch (SQLException e) {
            log.error("incrementarContador() - " + e.getMessage());
            String mensaje = "Error al obtener valor de contador : " + e.getMessage();
            throw new ContadorException(mensaje, e);
        }
    }    
    private static void actualizarContador(String idContador, Long valor) throws ContadorException {
        Connection conn = new Connection();
        try {
            log.debug("actualizarContador() - Actualizando valor para contador:  " + idContador);
            conn.abrirConexion(Database.getConnection());
            ContadoresCajaDao.reiniciar(conn, idContador, valor);
        }
        catch (SQLException e) {
            log.error("actualizarContador() - " + e.getMessage());
            String mensaje = "Error al actualizar valor de contador : " + e.getMessage();

            throw new ContadorException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
    }    
    public static void actualizarContadores() throws ContadorException {
        Connection conn = new Connection();
        try {
            log.debug("actualizarContadores() - Comprobando si se requiere reiniciar contadores. Consultamos fecha de inicio de autorización...");
            // Comprobamos si la fecha de inicio de autorización es hoy
            Fecha fechaInicio = new Fecha(Sesion.getEmpresa().getFechaInicioAuorizacion());
            Fecha hoy = new Fecha();
            if (fechaInicio.equalsFecha(hoy)){
                log.debug("actualizarContadores() - Nueva autorización de empresa. Comprobamos si se solicita reinicio de contadores...");
                // Comprobamos si tenemos que reiniciar contadores
                if (Sesion.getEmpresa().isReinicioContadores()){
                    log.debug("actualizarContadores() - Se requiere reiniciar contadores para la nueva autorización >> Reiniciamos contadores...");
                    conn.abrirConexion(Database.getConnection());
                    ContadoresCajaDao.reiniciar(conn, hoy);
                    log.debug("actualizarContadores() - Contadores reiniciados correctamente");
                    return;
                }
            }
            log.debug("actualizarContadores() - No es necesario el reinicio de contadores.");
        }
        catch (SQLException e) {
            log.error("actualizarContadores() - " + e.getMessage());
            String mensaje = "Error al reiniciar contadores : " + e.getMessage();
            throw new ContadorException(mensaje, e);
        }
        finally {
            conn.cerrarConexion();
        }
    }

}
