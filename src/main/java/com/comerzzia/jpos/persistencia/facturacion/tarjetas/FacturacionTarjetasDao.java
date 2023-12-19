/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.facturacion.tarjetas;

import com.comerzzia.jpos.entity.db.FacturacionTarjeta;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author MGRI
 */
public class FacturacionTarjetasDao {
    protected static Logger log = Logger.getMLogger(FacturacionTarjetasDao.class);

    public static void crearFacturacionTarjeta(FacturacionTarjeta fa, EntityManager em) throws Exception {
        em.persist(fa);
    }
    
    public static void actualizarFacturacionTarjeta(FacturacionTarjeta fa, EntityManager em) throws Exception {
        if(fa.getId() != null){
            fa.setProcesado('N');
            em.merge(fa);
        }
    }

    
    public static FacturacionTarjeta consultarFacturacionTarjeta(EntityManager em, String uid) throws Exception {
        Query consulta = em.createQuery("SELECT a FROM FacturacionTarjeta a WHERE a.id= :id").setHint("toplink.refresh", "true");
        consulta.setParameter("id", uid);
        FacturacionTarjeta res = (FacturacionTarjeta)consulta.getSingleResult();
        return res;
    }
    
    
     public static FacturacionTarjeta consultarFacturacionTarjetaByAuditoria(EntityManager em, Integer numeroAuditoria) throws Exception {
        Query consulta = em.createQuery("SELECT a FROM FacturacionTarjeta a WHERE a.numeroAuditoria= :numeroAuditoria");
        consulta.setParameter("numeroAuditoria", numeroAuditoria);
        
        FacturacionTarjeta res = (FacturacionTarjeta)consulta.getSingleResult();
        return res;
    }
     
     public static List<FacturacionTarjeta> consultarFacturacionTarjetaByFactura(EntityManager em, String idTicket) throws Exception {
        Query consulta = em.createQuery("SELECT a FROM FacturacionTarjeta a WHERE a.idReferencia= :idTicket and a.estatusTransaccion ='O'"  );
        consulta.setParameter("idTicket", idTicket);
        return consulta.getResultList();
    }
     //metodo Implementado Para Proceso De Control PinPad Plan D
       public static FacturacionTarjeta consultarMaximafacturaPinPad( String emisor,String caja) throws SQLException {       
        String connectionUrl =Sesion.datosDatabase.getUrlConexion();
        OracleDataSource ods = new OracleDataSource();
        ods.setURL(connectionUrl);
        ods.setUser(Sesion.datosDatabase.getUsuario());
        ods.setPassword(Sesion.datosDatabase.getPassword());
        java.sql.Connection con = ods.getConnection();
        Statement  stmt = con.createStatement();
        FacturacionTarjeta c = null;
        try {
           ResultSet rs = null;
//           String sql="SELECT f.LOTE,f.SECUENCIAL_TRANSACCION,f.TERMINAL_ID FROM D_FACTURACION_TARJETAS_TBL f,d_medios_pago_tbl m WHERE EMISOR='"+emisor+"' AND NUMERO_CAJA='"+caja+"' AND f.CODMEDPAG=m.CODMEDPAG AND TARJETA_SUKASA='N' AND TIENE_INFOEXTRA_1='N' AND NVL(SECUENCIAL_TRANSACCION,'0')= (SELECT MAX(NVL(SECUENCIAL_TRANSACCION,'0')) FROM D_FACTURACION_TARJETAS_TBL f,d_medios_pago_tbl m WHERE EMISOR='"+emisor+"' AND NUMERO_CAJA='"+caja+"' AND f.CODMEDPAG=m.CODMEDPAG AND TARJETA_SUKASA='N' AND TIENE_INFOEXTRA_1='N') and rownum = 1";
//         String sql="SELECT f.SECUENCIAL_TRANSACCION,f.TERMINAL_ID FROM D_FACTURACION_TARJETAS_TBL f,d_medios_pago_tbl m WHERE FECHA_REGISTRO >='09-may-2017' AND FECHA_REGISTRO<'10-may-2017' AND EMISOR='"+emisor+"' AND f.CODMEDPAG=m.CODMEDPAG AND TARJETA_SUKASA='N' AND TIENE_INFOEXTRA_1='N' AND NVL(SECUENCIAL_TRANSACCION,'0')= (SELECT MAX(NVL(SECUENCIAL_TRANSACCION,'0')) FROM D_FACTURACION_TARJETAS_TBL f,d_medios_pago_tbl m WHERE FECHA_REGISTRO>='09-may-2017' AND FECHA_REGISTRO<'10-may-2017' AND EMISOR='"+emisor+"' AND f.CODMEDPAG=m.CODMEDPAG AND TARJETA_SUKASA='N' AND TIENE_INFOEXTRA_1='N')";           

           String sql = "SELECT NVL (MAX(f.LOTE),0)LOTE FROM D_FACTURACION_TARJETAS_TBL f,d_medios_pago_tbl m WHERE EMISOR='"+emisor+"' AND NUMERO_CAJA='"+caja+"' AND f.CODMEDPAG=m.CODMEDPAG AND TARJETA_SUKASA='N' AND TIENE_INFOEXTRA_1='N'"; 
           rs = stmt.executeQuery(sql);
            while (rs.next()) {
               c = new FacturacionTarjeta();
               c.setLote(rs.getString("LOTE"));
           //    c.setSecuencialTransaccion(rs.getString("SECUENCIAL_TRANSACCION"));
           //    c.setTerminalId(rs.getString("TERMINAL_ID"));
           }
       } catch (SQLException ex) {
            log.error("Error Al Consultar D_FACTURACION_TARJETAS_TBL"+ ex);
        }
        return c;
    }


}
